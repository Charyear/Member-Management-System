@echo off
echo Starting Member Management System...

REM 设置JAVA_HOME（如果需要）
REM set JAVA_HOME=C:\Program Files\Java\jdk-19

REM 设置CATALINA_HOME（请根据实际Tomcat安装路径修改）
set CATALINA_HOME="D:\Tomcat-10.1.19"

REM 清理并构建项目
call mvn clean package

REM 删除旧的部署
if exist "%CATALINA_HOME%\webapps\member-management.war" del /F "%CATALINA_HOME%\webapps\member-management.war"
if exist "%CATALINA_HOME%\webapps\member-management" rmdir /S /Q "%CATALINA_HOME%\webapps\member-management"

REM 复制新的WAR文件
copy /Y "target\member-management.war" "%CATALINA_HOME%\webapps\"

REM 启动Tomcat
echo Starting Tomcat...
call "%CATALINA_HOME%\bin\startup.bat"

echo.
echo 项目已成功部署！
echo 请等待Tomcat完全启动（约30秒）...
echo 然后访问: http://localhost:8080/member-management
echo.
pause