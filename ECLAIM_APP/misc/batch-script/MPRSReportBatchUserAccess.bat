@echo off
for /f "usebackq" %%i in (`PowerShell $date ^= Get-Date^; $date ^= $date.AddDays^(-1^)^; $date.ToString^('yyyyMMdd'^)`) do set YESTERDAY=%%i

for /f "usebackq" %%i in (`PowerShell $date ^= Get-Date^; $date.ToString^('yyyyMMddHHmmss'^)`) do set CUR_DATETIME=%%i

::echo %CUR_DATETIME%
set date=%CUR_DATETIME:~0,8%
set time=%CUR_DATETIME:~8,6%

set hour=%time:~0,2%
::echo hour=%hour%
set min=%time:~2,2%
::echo min=%min%
set secs=%time:~4,2%
::echo secs=%secs%

set year=%date:~0,4%
::echo year=%year%
set month=%date:~4,2%
::echo month=%month%
set day=%date:~6,2%
::echo day=%day%

set datetimef=%year%-%month%-%day% %hour%:%min%:%secs%

::echo datetimef=%datetimef%

SET REPORT_PATH=C:\MPRS\BatchReport\staging
SET LOG_PATH=C:\MPRS\BatchReport\bin\log

SET DATA_FILE=%REPORT_PATH%\%YESTERDAY%_Report_Batch_User_Access.txt

echo [%datetimef%] Starting to maintain Report Batch User Access... >>%LOG_PATH%\MPRSReportBatchUserAccess_%CUR_DATETIME%.txt

for /f "tokens=1,2,3 delims= " %%a in (%DATA_FILE%) do (
	if %%a==REMOVE (
		for /f "delims=" %%i in ('"net group "MPRS CLUS RPT USERS - %%b" %%c /delete /domain 2>&1"') do (
			echo [%datetimef%] %%i >>%LOG_PATH%\MPRSReportBatchUserAccess_%CUR_DATETIME%.txt
		)
	) else (
		for /f "delims=" %%i in ('"net group "MPRS CLUS RPT USERS - %%b" %%c /add /domain 2>&1"') do (
			echo [%datetimef%] %%i >>%LOG_PATH%\MPRSReportBatchUserAccess_%CUR_DATETIME%.txt
		)
	)

)

echo [%datetimef%] Complete maintain Report Batch User Access... >>%LOG_PATH%\MPRSReportBatchUserAccess_%CUR_DATETIME%.txt

echo [%datetimef%] Starting to archive... >>%LOG_PATH%\MPRSReportBatchUserAccess_%CUR_DATETIME%.txt

move %REPORT_PATH%\%YESTERDAY%_Report_Batch_User_Access.txt %REPORT_PATH%\user_access\%YESTERDAY%_Report_Batch_User_Access.txt >>%LOG_PATH%\MPRSReportBatchUserAccess_%CUR_DATETIME%.txt 2>>&1

echo [%datetimef%] Complete archive... >>%LOG_PATH%\MPRSReportBatchUserAccess_%CUR_DATETIME%.txt
