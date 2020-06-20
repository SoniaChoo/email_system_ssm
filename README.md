# email_system_ssm

2. mysql5.7 创建新用户， 设置所有权限，允许远程访问。
```shell script
sudo apt update # 更新依赖包
sudo apt install mysql-server # 安装数据库
sudo mysql_secure_installation # 数据库初始化

sudo mysql -u root -p # 用刚刚设置的密码登陆进入数据库
    CREATE USER 'YOUR_USERNAME'@'localhost' IDENTIFIED BY 'YOUR_PASSWORD'; # 在数据库中新建一个用户
    GRANT ALL PRIVILEGES ON *.* TO 'YOUR_USERNAME'@'%' IDENTIFIED BY 'YOUR_PASSWORD' WITH GRANT OPTION; # 设置所有权限+允许远程访问
    FLUSH PRIVILEGES; # 更新权限
    exit; # 退出数据库



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