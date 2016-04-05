#GraduationDesign

>如果你有maven在命令行内输入 mvn spring-boot:run
>如果木有的话 mvnw spring-boot:run试一试

>数据库在使用的是mysql在`src\main\resources\application.properties`文件夹中进行配置


* 开发软件 intellij idea
* 使用java版本 java8
* 使用框架 
  * spring-boot

2016年4月6日 
>完成了登录注册的一些简单的响应；
登录访问 `/api/login`    请求方式 POST 需要数据 `email`,`passowrd`
注册访问 `/api/user`     请求方式 POST 需要数据 `email`,`name`,`passowrd`
还有一个是查看对应id的用户 `/api/user/{id}` (id为对应值，应该是个long类型，用于调试)   请求方式 GET 

下一步要实现登录注册功能，从页面能看到效果，并且登陆后自动进入个人中心（暂定）