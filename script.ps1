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
New-ADUser -GivenName $FirstName -Surname $LastName -Name $FullName -SamAccountName $Username -AccountPassword $Password -OtherAttributes @{
    'title' = $Title
    'DisplayName' = $FullName
    'userPrincipalName' = $Email
    'mail' = $Email
    'telephoneNumber' = $Phone
}


# Associates group policies to the specific user
$groupNames = @("HMCStaff", "HMCStaff-gs", "SAC Office", "Sacramento Architects gs", "Sacramento Office", "Sacramento Staff gs", "RDrive-Staff")

foreach ($groupName in $groupNames) {
    Add-ADGroupMember -Identity $groupName -Members $Username
}
