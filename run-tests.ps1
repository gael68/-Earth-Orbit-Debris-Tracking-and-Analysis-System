Write-Host "Starting test runner script..." -ForegroundColor Cyan

# Clean
Write-Host "Cleaning output directory..." -ForegroundColor Yellow
Remove-Item -Recurse -Force -ErrorAction Ignore out
New-Item -ItemType Directory -Path out | Out-Null

# Compile
Write-Host "Compiling Java source and tests..." -ForegroundColor Yellow
javac -d out -cp "lib\junit-platform-console-standalone-1.10.2.jar" src\main\java\com\team22\*.java src\test\java\com\team22\*.java

if ($LASTEXITCODE -eq 0) {
    Write-Host "Compilation successful." -ForegroundColor Green
} else {
    Write-Host "Compilation failed." -ForegroundColor Red
    exit 1
}

# Run
Write-Host "Running tests..." -ForegroundColor Yellow
java -jar lib\junit-platform-console-standalone-1.10.2.jar --class-path out --select-class com.team22.ImpactAnalysisTest

Write-Host "`nAll tests done! 🎯" -ForegroundColor Cyan
