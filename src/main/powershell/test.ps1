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

    # Filter out phone numbers without an emergency location
    $allNumbers = $allNumbers | Where-Object { $_.EmergencyLocationId }

    # for every number, create a telephone number property and remove the +
    $telephoneNumbers = $allNumbers.TelephoneNumber | ForEach-Object { @{ TelephoneNumber = $_.Replace('+', '') } }

    $jsonObject = @{ TelephoneNumbers = $telephoneNumbers }
    $json = $jsonObject | ConvertTo-Json

    Set-Content -Path .\src\main\resources\phone_numbers.json -Value $json