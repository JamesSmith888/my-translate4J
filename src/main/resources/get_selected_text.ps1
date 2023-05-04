# 设置输入和输出编码为 UTF-8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::InputEncoding = [System.Text.Encoding]::UTF8

# 模拟 Ctrl+C (复制) 键的按下和释放
[void] [System.Reflection.Assembly]::LoadWithPartialName("'System.Windows.Forms")
[System.Windows.Forms.SendKeys]::SendWait("^c")

# 等待文本复制到剪贴板
Start-Sleep -Milliseconds 100

# 从剪贴板获取文本
Add-Type -AssemblyName System.Windows.Forms
$clipboard = [System.Windows.Forms.Clipboard]::GetText()
Write-Output $clipboard