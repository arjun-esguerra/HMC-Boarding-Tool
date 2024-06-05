$P = Import-Csv -Path .\output.csv

$P | Format-Table

<## Creates a user with the inputted data
$GivenName = 'Santa'
$Surname = 'Claus'
$Name = $GivenName + ' ' + $Surname
$SamAccountName = $GivenName.Substring(0,1) + $Surname
$Password = ConvertTo-SecureString "abcd123!" -AsPlainText -Force

New-ADUser -GivenName $GivenName -Surname $Surname -Name $Name -SamAccountName $SamAccountName -AccountPassword $Password -OtherAttributes @{
    'title' = 'Test Title'
    'DisplayName' = 'Santa Claus'
    'userPrincipalName' = 'Santa.Claus@hmcarchitects.com'
    'mail' = 'Santa.Claus@hmcarchitects.com'
    'telephoneNumber' = '123.123.1234'
}


# Associates group policies to the specific user
$groupNames = @("HMCStaff", "HMCStaff-gs", "SAC Office", "Sacramento Architects gs", "Sacramento Office", "Sacramento Staff gs", "RDrive-Staff")

foreach ($groupName in $groupNames) {
    Add-ADGroupMember -Identity $groupName -Members $SamAccountName
}
#>