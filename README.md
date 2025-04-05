# Member-Management-System
会员管理系统使用说明

(一）系统要求
Java 19 或更高版本
MySQL 数据库
Tomcat 10.1.19 或更高版本
Maven 构建工具（构建项目，运行 mvn clean install）

（二）安装步骤
1. 环境准备
安装 Java 19 或更高版本
安装 MySQL 数据库
安装 Tomcat 10.1.19（建议安装在 D:\Tomcat-10.1.19）
安装 Maven
2. 数据库初始化
打开命令提示符或PowerShell
运行数据库初始化脚本init_db.bat：
根据提示输入MySQL root用户密码
3. 项目部署
确保Tomcat已正确安装，默认路径为 D:\Tomcat-10.1.19
运行启动脚本start.bat（建议手动启动）：
该脚本会自动执行以下操作：
（1）清理并构建项目
（2）删除旧的部署文件
（3）部署新的WAR文件
（4）启动Tomcat服务器
4. 访问系统
等待Tomcat完全启动（约30秒）打开浏览器访问：http://localhost:8080/member-management

（三）项目结构说明
src/: 源代码目录
pom.xml: Maven项目配置文件
start.bat: 项目启动脚本
init_db.bat: 数据库初始化脚本

#注意事项：
1、首次运行前必须执行数据库初始化脚本
2、确保MySQL服务已启动
3、确保Tomcat安装路径并启动成功，如需修改请编辑 start.bat 中的 CATALINA_HOME 变量
4、如果使用不同版本的Java，请修改 pom.xml 中的 maven.compiler.source 和 maven.compiler.target 版本号
