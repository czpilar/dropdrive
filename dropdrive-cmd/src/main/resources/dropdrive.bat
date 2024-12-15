@echo off
setlocal

set DROPDRIVE_DIR=.
set DROPDRIVE_ARTIFACT=${project.artifactId}-${project.version}

java -jar %DROPDRIVE_DIR%\lib\%DROPDRIVE_ARTIFACT%-spring-boot.jar %*

endlocal
@echo on
