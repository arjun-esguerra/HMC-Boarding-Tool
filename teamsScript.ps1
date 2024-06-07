Import-Module Teams

$credential = Get-Credential
Connect-MicrosoftTeams -Credential $credential

Get-Team