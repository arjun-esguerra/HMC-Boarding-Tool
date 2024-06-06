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

