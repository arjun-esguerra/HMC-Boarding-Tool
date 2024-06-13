Connect-MgGraph -Scopes User.ReadWrite.All, Organization.Read.All
Connect-MicrosoftTeams

$skip = 0
$allNumbers = @()

# Loop until Get-CsPhoneNumberAssignment returns no more results
do {
    $res = Get-CsPhoneNumberAssignment -CapabilitiesContain 'UserAssignment' -NumberType 'CallingPlan' -PstnAssignmentStatus 'Unassigned' -Skip $skip
    
    $allNumbers += $res

    $skip += 500

} while ($res)

# for every number, create a telephone number property and remove the +
$telephoneNumbers = $allNumbers.TelephoneNumber | ForEach-Object { @{ TelephoneNumber = $_.Replace('+', '') } }

$jsonObject = @{ TelephoneNumbers = $telephoneNumbers }
$json = $jsonObject | ConvertTo-Json

Set-Content -Path .\src\main\resources\phone_numbers.json -Value $json
