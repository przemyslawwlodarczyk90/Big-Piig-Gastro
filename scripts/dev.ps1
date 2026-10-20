param(
    [string]$JavaHome = $env:JAVA17_HOME,
    [switch]$Verify,
    [ValidateRange(1, 65535)][int]$Port = 8080
)
$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path -Parent $PSScriptRoot
if (-not $JavaHome) {
    $candidates = @($env:JAVA_HOME) + @(Get-ChildItem -LiteralPath (Join-Path $env:USERPROFILE '.jdks') -Directory -ErrorAction SilentlyContinue | Select-Object -ExpandProperty FullName)
    foreach ($candidate in $candidates) {
        if (-not $candidate) { continue }
        $release = Join-Path $candidate 'release'
        if ((Test-Path -LiteralPath $release) -and (Select-String -LiteralPath $release -Pattern '^JAVA_VERSION="17[."]' -Quiet)) {
            $JavaHome = $candidate
            break
        }
    }
}
if (-not $JavaHome -or -not (Test-Path -LiteralPath (Join-Path $JavaHome 'bin/java.exe'))) {
    throw 'Wskaz JDK 17: .\scripts\dev.ps1 -JavaHome C:\sciezka\do\jdk-17'
}
if (-not (Select-String -LiteralPath (Join-Path $JavaHome 'release') -Pattern '^JAVA_VERSION="17[."]' -Quiet)) {
    throw 'Ten projekt wymaga JDK 17. Globalna Java 25 moze pozostac bez zmian.'
}
$previousJava = $env:JAVA_HOME
Push-Location $projectRoot
try {
    $env:JAVA_HOME = $JavaHome
    if ($Verify) {
        & .\mvnw.cmd verify
    } else {
        if (-not $env:JWT_SECRET) {
            $bytes = New-Object byte[] 32
            $rng = [Security.Cryptography.RandomNumberGenerator]::Create()
            try { $rng.GetBytes($bytes) } finally { $rng.Dispose() }
            $env:JWT_SECRET = [Convert]::ToBase64String($bytes)
        }
        & .\mvnw.cmd spring-boot:run '-Dspring-boot.run.profiles=local' "-Dspring-boot.run.arguments=--server.port=$Port"
    }
    if ($LASTEXITCODE -ne 0) { throw "Maven zakonczyl sie kodem $LASTEXITCODE" }
} finally {
    $env:JAVA_HOME = $previousJava
    Pop-Location
}
