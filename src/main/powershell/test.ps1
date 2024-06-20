$Phone = '19163215923'

$FormattedPhone = $Phone -replace "^1(...)(...)(....)", '$1.$2.$3'

Write-Output $FormattedPhone