Connect-MgGraph -Scopes User.ReadWrite.All, Organization.Read.All
Connect-MicrosoftTeams

$P = Import-Csv -Path .\output.csv

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


# Creates a user with the inputted data
if ($office -eq 'Ontario 01' -or $office -eq 'Ontario 05') { $Path = "OU=Users,OU=Ontario,OU=HMC,DC=hmcarch,DC=com" } 
else { $Path = "OU=Users,OU=$Office,OU=HMC,DC=hmcarch,DC=com" }

    New-ADUser -GivenName $FirstName -Surname $LastName -Name $FullName -SamAccountName $Username -AccountPassword $Password -OtherAttributes @{
        'title' = $Title
        'DisplayName' = $FullName
        'userPrincipalName' = $Email
        'mail' = $Email
        'telephoneNumber' = $Phone
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

foreach ($groupPolicy in $GroupPolicies[$Office]) {
    Add-ADGroupMember -Identity $groupPolicy -Members $Username
}

# Assign licenses
$success = $false

while (-not $success) {
    try {
        $params = @{
            AccountEnabled = $true
            UsageLocation  = 'US'
        }
        Update-MgUser -UserId $Email -BodyParameter $params -ErrorAction Stop

        $OfficeE3 = Get-MgSubscribedSku -All | Where SkuPartNumber -eq 'SPE_E3'
        $PowerBIPro = Get-MgSubscribedSku -All | Where SkuPartNumber -eq 'POWER_BI_PRO'
        $DomesticPlan = Get-MgSubscribedSku -All | Where SkuPartNumber -eq 'MCOPSTN1'
        $StandardPlan = Get-MgSubscribedSku -All | Where SkuPartNumber -eq 'MCOEV'

        $addLicenses = @(
            @{SkuId = $OfficeE3.SkuId },
            @{SkuId = $PowerBIPro.SkuId },
            @{SkuId = $DomesticPlan.SkuId },
            @{SkuId = $StandardPlan.SkuId }
        )

        Set-MgUserLicense -UserId $Email -AddLicenses $addLicenses -RemoveLicenses @() -ErrorAction Stop

        # Check if the user exists
        $userExists = Get-MgUser -UserId $Email -ErrorAction SilentlyContinue

        # If the user exists, the operation is successful
        if ($userExists) {
            $success = $true
        }
    }
    catch {
        # If an error is thrown, wait for 60 seconds before the next attempt
        Start-Sleep -Seconds 60
        Write-Output "Assigning Licenses..."
    }
}

$success = $false

while (-not $success) {
    try {
        # Assign teams number
        Set-CsPhoneNumberAssignment -Identity $Email -PhoneNumber $Phone -PhoneNumberType CallingPlan -ErrorAction Stop

        # If no error is thrown, the operation is successful
        $success = $true
    }
    catch {
        # If an error is thrown, wait for 15 seconds before the next attempt
        Start-Sleep -Seconds 15
        Write-Output "Assigning Number..."
    }
}

