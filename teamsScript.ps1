Connect-MicrosoftTeams
<#
$unassignedNumbers = Get-CsPhoneNumberAssignment | Where-Object  { $_.LineURI -like $null }
$json = $unassignedNumbers | Select-Object TelephoneNumber | ConvertTo-Json

Set-Content -Path .\src\main\resources\phone_numbers.json -Value $json
#>

$skip = 0
$allNumbers = @()

# Loop until Get-CsPhoneNumberAssignment returns no more resutls
do {
    $res = Get-CsPhoneNumberAssignment -CapabilitiesContain 'UserAssignment' -NumberType 'CallingPlan' -PstnAssignmentStatus 'Unassigned' -Skip $skip
    
    $allNumbers += $res

    $skip += 500

} while ($res)

$allNumbers.TelephoneNumber