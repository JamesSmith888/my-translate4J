@echo off
cd /d "%~dp0"

REM 创建 startLog 文件夹
if not exist startLog mkdir startLog

REM 定义日志文件路径
set "LOG_FILE1=startLog\run_my_translate_log.txt"
set "LOG_FILE2=startLog\run_server_log.txt"

REM 启动 run_my_translate.bat 脚本，并将日志输出到文件
start /b call run_my_translate.bat > "%LOG_FILE1%" 2>&1

REM 启动 run_server.bat 脚本，并将日志输出到另一个文件
start /b call run_server.bat > "%LOG_FILE2%" 2>&1

echo translate server is running...
echo if for long time no output, please check the log files in startLog folder.
