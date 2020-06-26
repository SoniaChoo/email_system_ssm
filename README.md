# email_system_ssm

3. 数据库全部需要 default value, 比如说captcha表read字段.

2. mysql5.7 创建新用户， 设置所有权限，允许远程访问。
```shell script
sudo apt update # 更新依赖包
sudo apt install mysql-server # 安装数据库
sudo mysql_secure_installation # 数据库初始化

sudo mysql -u root -p # 用刚刚设置的密码登陆进入数据库
    CREATE USER 'YOUR_USERNAME'@'localhost' IDENTIFIED BY 'YOUR_PASSWORD'; # 在数据库中新建一个用户
    GRANT ALL PRIVILEGES ON *.* TO 'YOUR_USERNAME'@'localhost'; # 设置用户拥有所有本地权限
    GRANT ALL PRIVILEGES ON *.* TO 'YOUR_USERNAME'@'%' IDENTIFIED BY 'YOUR_PASSWORD' WITH GRANT OPTION; # 设置用户远程访问时拥有所有权限
    FLUSH PRIVILEGES; # 更新权限
    exit; # 退出数据库

#除了允许 mysql 用户远程连接, 还需要修改 mysql 设置文件, /etc/mysql/mysql.conf.d/mysqld.cnf 将其中的 bind-address=127.0.0.1 注释掉.



```



1.  jdk JDK在8之前的版本，对日期时间的处理相当麻烦，
           有些方法设计非常反人类。而Joda-Time使用起来不仅方便，
           而且可读性强。虽然JDK 8引用了新的时间处理类，而且参与设计的人也正是Joda-Time的作者，
           但是由于各种原因，很多项目还是使用的JDK7，使用Joda-Time还是一个不错的选择。
           <!--<dependency>
               <groupId>joda-time</groupId>
               <artifactId>joda-time</artifactId>
               <version>2.10.6</version>
           </dependency>-->