function getPhoneNumbers
{
    Connect-MgGraph -Scopes User.ReadWrite.All, Organization.Read.All -NoWelcome
    Connect-MicrosoftTeams

    $skip = 0
    $allNumbers = @()

    # Loop until Get-CsPhoneNumberAssignment returns no more results
    do
    {
        $res = Get-CsPhoneNumberAssignment -CapabilitiesContain 'UserAssignment' -NumberType 'CallingPlan' -PstnAssignmentStatus 'Unassigned' -Skip $skip
        $allNumbers += $res

        $skip += 500

    } while ($res)

    # for every number, create a telephone number property and remove the +
    $telephoneNumbers = $allNumbers.TelephoneNumber | ForEach-Object { @{ TelephoneNumber = $_.Replace('+', '') } }

    $jsonObject = @{ TelephoneNumbers = $telephoneNumbers }
    $json = $jsonObject | ConvertTo-Json

    Set-Content -Path .\src\main\resources\phone_numbers.json -Value $json
}

function getNames
{
    $OUs = @("Los Angeles", "San Francisco", "Ontario", "San Diego", "San Jose", "Sacramento")

    $allNames = @()

    # Loop over each OU
    foreach ($OU in $OUs)
    {
        $users = Get-ADUser -Filter * -SearchBase "OU=Users,OU=$OU,OU=HMC,DC=hmcarch,DC=com"

        foreach ($user in $users)
        {
            $allNames += $user.Name
        }
    }

    $sortedNames = $allNames | Sort-Object

    # Convert the sorted names to a JSON object
    $jsonObject = @{ UserNames = $sortedNames }
    $json = $jsonObject | ConvertTo-Json

    # Write the JSON object to a file
    Set-Content -Path .\src\main\resources\users.json -Value $json
}

function onboardUser
{

    Connect-MgGraph -Scopes User.ReadWrite.All, Organization.Read.All -NoWelcome
    Connect-MicrosoftTeams

    $P = Import-Csv -Path .\src\main\resources\output.csv

    $row = $P[0]

    $FirstName = $row.'First Name'
    $LastName = $row.'Last Name'
    $FullName = $row.'First Name' + ' ' + $row.'Last Name'
    $Username = $row.'Username'
    $Password = ConvertTo-SecureString $row.'Password' -AsPlainText -Force
    $Title = $row.'Job Title'
    $Email = $row.'Email'
    $Office = $row.'Office'
    $Phone = $row.'Phone Number'

    $FormattedPhone = $Phone -replace "^1(...)(...)(....)", '$1.$2.$3'

    # gets plain password
    $BSTR = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($Password)
    $PlainPassword = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($BSTR)

    Write-Host "First Name: $FirstName"
    Write-Host "Last Name: $LastName"
    Write-Host "Email Address: $Email"
    Write-Host "Password: $PlainPassword"
    Write-Host "Office: $Office"
    Write-Host "Phone Number: $Phone`n"

    # Creates a user with the inputted data
    if ($office -eq 'Ontario 01' -or $office -eq 'Ontario 05')
    {
        $Path = "OU=Users,OU=Ontario,OU=HMC,DC=hmcarch,DC=com"
    }
    else
    {
        $Path = "OU=Users,OU=$Office,OU=HMC,DC=hmcarch,DC=com"
    }

    New-ADUser -GivenName $FirstName -Surname $LastName -Name $FullName -SamAccountName $Username -AccountPassword $Password -Enabled $true -OtherAttributes @{
        'title' = $Title
        'DisplayName' = $FullName
        'userPrincipalName' = $Email
        'mail' = $Email
        'telephoneNumber' = $FormattedPhone
    } -Path $Path


    # Associates group policies to the specific user
    $GroupPolicies = @{
        "Ontario 01" = @("HMCStaff", "HMCStaff-gs", "ONT Studio 1", "Ontario Architects gs", "Ontario Office", "Ontario Staff gs", "RDrive-Staff")
        "Ontario 05" = @("HMCStaff", "HMCStaff-gs", "ONT Studio 5", "Ontario Architects gs", "Ontario Office", "Ontario Staff gs", "RDrive-Staff")
        "Los Angeles" = @("HMCStaff", "HMCStaff-gs", "LA Office", "Los Angeles Architects gs", "Los Angeles Office", "Los Angeles Staff gs", "RDrive-Staff")
        "Sacramento" = @("HMCStaff", "HMCStaff-gs", "SAC Office", "Sacramento Architects gs", "Sacramento Office", "Sacramento Staff gs", "RDrive-Staff")
        "San Jose" = @("HMCStaff", "HMCStaff-gs", "SJ Office", "San Jose Architects gs", "San Jose Office", "San Jose Staff gs", "RDrive-Staff")
        "San Diego" = @("HMCStaff", "HMCStaff-gs", "SD Office", "San Diego Architects gs", "San Diego Office", "San Diego Staff gs", "San Diego Project Access", "RDrive-Staff")
        "San Francisco" = @("HMCStaff", "HMCStaff-gs", "SF Office", "San Francisco Architects gs", "San Francisco Office", "San Francisco Staff gs", "RDrive-Staff")
    }

    foreach ($groupPolicy in $GroupPolicies[$Office])
    {
        Add-ADGroupMember -Identity $groupPolicy -Members $Username
    }

    Write-Output " "
    Write-Output "Assigning Licenses..."

    # Assign licenses
    $success = $false

    while (-not $success)
    {
        try
        {

            Update-MgUser -UserId $Email -UsageLocation 'US' -ErrorAction Stop

            $OfficeE3 = Get-MgSubscribedSku -All | Where SkuPartNumber -eq 'SPE_E3'
            $PowerBIPro = Get-MgSubscribedSku -All | Where SkuPartNumber -eq 'POWER_BI_PRO'
            $DomesticPlan = Get-MgSubscribedSku -All | Where SkuPartNumber -eq 'MCOPSTN1'
            $StandardPlan = Get-MgSubscribedSku -All | Where SkuPartNumber -eq 'MCOEV'

            $addLicenses = @(
                @{ SkuId = $OfficeE3.SkuId },
                @{ SkuId = $PowerBIPro.SkuId },
                @{ SkuId = $DomesticPlan.SkuId },
                @{ SkuId = $StandardPlan.SkuId }
            )

            Set-MgUserLicense -UserId $Email -AddLicenses $addLicenses -RemoveLicenses @() -ErrorAction Stop

            $userExists = Get-MgUser -UserId $Email -ErrorAction SilentlyContinue

            # If the user exists, the operation is successful
            if ($userExists)
            {
                $success = $true
            }
        }
        catch
        {
            Start-Sleep -Seconds 120
            Write-Output "."
        }
    }


    # Assign teams number
    $success = $false

    while (-not $success)
    {
        try
        {
            Set-CsPhoneNumberAssignment -Identity $Email -PhoneNumber $Phone -PhoneNumberType CallingPlan -ErrorAction Stop

            $success = $true
        }
        catch
        {
            Start-Sleep -Seconds 30
            Write-Output "Assigning Number..."

        }

    }

    Write-Output "User successfully onboarded!"

}

function offboardUser($Name)
{
    # Remove licenses
    Connect-MgGraph -Scopes User.ReadWrite.All, Organization.Read.All -NoWelcome
    $nameParts = $Name -split ' '
    $userEmail = ($nameParts[0].ToLower() + '.' + $nameParts[1].ToLower() + '@hmcarchitects.com')

    $allLicenses = Get-MgUserLicenseDetail -UserId $userEmail -Property SkuPartNumber

    $licenseSkuIds = $allLicenses.SkuPartNumber | ForEach-Object {
        $skuPartNumber = $_
        $sku = Get-MgSubscribedSku -All | Where-Object { $_.SkuPartNumber -eq $skuPartNumber }
        $sku.SkuId
    }

    if ($licenseSkuIds) {
        Set-MgUserLicense -UserId $userEmail -RemoveLicenses $licenseSkuIds -AddLicenses @{}
    }

    # Move user to Separations OU
    $user = Get-ADUser -Filter { UserPrincipalName -eq $userEmail } -Properties ObjectGUID
    Move-ADObject -Identity $user.ObjectGUID -TargetPath "OU=Separations,OU=HMC,DC=hmcarch,DC=com"




}


