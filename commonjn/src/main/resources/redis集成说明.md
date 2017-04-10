1、redis的基本操作放置在com.sanshui.commonsh.redis中

2、redis的相关配置文件在redis.properties和spring-redis.xml（在spring-mybatis.xml中引入spring-redis.xml文件）中

3、在com.sanshui.commonsh.service.redis中有示例：TestCacheService 和 TestCacheServiceImpl ,
TestCacheService继承IRedisObject接口，TestCacheService继承RedisObject实现TestCacheService接口
（底层包含了基本的设置缓存，设置缓存失效时间，删除某个key缓存，清除缓存，清除全部缓存，获取缓存等，支持List,Map,Object等数据类型，
如有其它需要可再次进行扩展）

4、在interfaceAPIsweb项目中，新建了com.sanshui.interfaces.controller.TestCacheController缓存使用示例

5、在test文件夹中，新建了com.sanshui.interfaces.controller.TestCacheControllerTest该Junit测试类
（注:webSocket使用junit测试会出现
Caused by: java.lang.IllegalStateException: No suitable default RequestUpgradeStrategy found异常，
需要在pom.xml文件中添加如下依赖：
<dependency>
	<groupId>org.apache.tomcat.embed</groupId>
	<artifactId>tomcat-embed-websocket</artifactId>
	<version>7.0.52</version>
	<scope>test</scope>
</dependency>
）