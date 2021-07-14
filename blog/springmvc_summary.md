## I. 简介

在说什么是SpringMVC之前，我们先来说说什么是**MVC**三层架构。MVC是**模型（Model）、视图（View）和控制器（Controller）**的简写，是一种软件设计规范。它们各自的定义和分工如下：

- **Model**：数据模型，提供要展示的数据，因此包含数据和行为，可以认为是领域模型或JavaBean组件（包含数据和行为），不过现在一般都分离开来：**Value Object（数据Dao）和服务层（行为Service）**。也就是模型提供了模型数据查询和模型数据的状态更新等功能，包括**数据和业务**。
- **View**: 负责进行模型的展示，一般就是我们见到的用户界面，客户想看到的东西。
- **Controller**: **接收用户请求，委托给模型进行处理（状态改变），处理完毕后把返回的模型数据返回给视图**，由视图负责展示。 也就是说控制器做了个调度员的工作。

最典型的MVC就是JSP+Servlet+Javabean的模式。其中JSP负责视图部分，即展示数据；Servlet负责Controller部分，处理用户的响应和请求；而Service和Dao层用于将数据库中的数据以Java Bean的形式提取到程序中并处理相应的业务。

![mvc_theory](C:\study\SpringMVC\blog\springmvc\mvc_theory.png)



在了解了MVC三层架构后，我们来说说什么是SpringMVC。**SpringMVC是Spring用于实现MVC框架的产品**。Spring MVC属于SpringFrameWork的后续产品，已经融合在Spring Web Flow里面。Spring 框架提供了构建Web应用程序的全功能 MVC 模块。

使用SpringMVC进行开发大大地减小了web开发难度，提升了开发效率且提升了性能。除此之外，SpringMVC还有以下优势：

- 轻量级，简单易学

- 高效 , 基于请求响应的MVC框架

- 与Spring兼容性好，无缝结合（无缝集成SpringIOC和AOP）

- 约定优于配置

- 功能强大：RESTful、数据验证、格式化、本地化、主题等

- 简洁灵活

## II. 原理

SpringMVC工作的核心是一个**DispatcherServlet**类。Spring的web框架就是围绕它而设计的。简单来说，原本JavaWeb框架中，用户每发送一个请求，程序首先要找到请求对应的Servlet(因此每写一个Servlet都要在web.xml中配置)，然后再来执行这个Servlet。而在SpringMVC中，在这个过程中增加了一层类似过滤器的DispatcherServlet。当用户发送请求到服务器后，请求首先会被DispatcherServlet接收，然后由DispatcherServlet将请求转发给对应的Controller(Servlet)，当请求的业务完成后，Controller会返回一个**ModelAndView**对象给DispatcherServlet。接着DispatcherServlet再将这个对象发送给视图层（通常是前端），最后将相应由View层展示给用户。

![springmvc执行过程](C:\study\SpringMVC\blog\springmvc\springmvc执行过程.png)

**SpringMVC的执行流程**

SpringMVC的执行流程如下图所示。其中实线部分是其自动执行的业务，而虚线部分是我们真正需要在代码中实现的业务。

1. DispatcherServlet接收到用户请求
2. DispatcherServlet调用HandlerMapping，由HandlerMapping根据请求的url查钊Handler
3. HandlerExecution表示具体的Handler,接下来它会根据url查找对应的Controller
4. 将解析后的信息传回给DispatcherServlet(如解析控制器映射等)
5. DispatcherServlet调用HandlerAdapter(处理适配器)，它会按照特定的规则来执行Handler
6. Handler让具体的Controller被执行（接下来是Controller调用Model层执行业务）
7. Controller将执行结果返回给HandlerAdapter
8. HandlerAdapter将结果返回给DispatcherServlet（ModelAndView名或者Model名）
9. DispatcherServlet调用视图解析器来解析HandlerAdapter传递来的逻辑视图名
10. ViewResolver解析逻辑视图名并传回给DispatcherServlet
11. DispatcherServlet根据视图解析器解析的视图结果，调用具体的视图
12. View层将最终视图呈现给用户

从中总结我们发现DispatcherServlet主要做了以下事情：

1. 接收用户请求并匹配对应的Handler
2. 找到对应的Handler后，使其调用对应的Controller从而执行业务
3. 收到Controller的执行结果后，将其交给视图解析器ViewResolver,并将结果交给View层最终把视图展示给用户

![springmvc流程图](C:\study\SpringMVC\blog\springmvc\springmvc流程图.png)

## III. 使用

### 1. 准备工作（配置项目）

1. 新建一个Maven项目，导入所需要的依赖并添加web支持

	```xml
	    <dependencies>
	        <dependency>
	            <groupId>junit</groupId>
	            <artifactId>junit</artifactId>
	            <version>4.12</version>
	        </dependency>
	        <dependency>
	            <groupId>org.springframework</groupId>
	            <artifactId>spring-webmvc</artifactId>
	            <version>5.1.9.RELEASE</version>
	        </dependency>
	        <dependency>
	            <groupId>javax.servlet</groupId>
	            <artifactId>servlet-api</artifactId>
	            <version>2.5</version>
	        </dependency>
	        <dependency>
	            <groupId>javax.servlet.jsp</groupId>
	            <artifactId>jsp-api</artifactId>
	            <version>2.2</version>
	        </dependency>
	        <dependency>
	            <groupId>javax.servlet</groupId>
	            <artifactId>jstl</artifactId>
	            <version>1.2</version>
	        </dependency>
	        <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
	        <dependency>
	            <groupId>org.projectlombok</groupId>
	            <artifactId>lombok</artifactId>
	            <version>1.18.20</version>
	            <scope>provided</scope>
	        </dependency>
	    </dependencies>
	```

	

2. 配置Tomcat

3. 配置web.xml，注册DispatcherServlet

	```xml
	<?xml version="1.0" encoding="UTF-8"?>
	<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
	         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
	http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
	         version="4.0">
	    <!--1.注册DispatcherServlet-->
	    <servlet>
	        <servlet-name>springmvc</servlet-name>
	        <servlet-class>
	            org.springframework.web.servlet.DispatcherServlet
	        </servlet-class>
	        <!--关联一个springmvc的配置文件: applicationContext.xml-->
	        <init-param>
	            <param-name>contextConfigLocation</param-name>
	            <param-value>classpath:applicationContext.xml</param-value>
	        </init-param>
	        <!--启动级别-1-->
	        <load-on-startup>1</load-on-startup>
	    </servlet>
	    <!--/ 匹配所有的请求；（不包括.jsp）-->
	    <!--/* 匹配所有的请求；（包括.jsp）-->
	    <servlet-mapping>
	        <servlet-name>springmvc</servlet-name>
	        <url-pattern>/</url-pattern>
	    </servlet-mapping>
	</web-app>
	```

4. 在resources中创建并编写总的配置文件applicationContext.xml和springmvc-servlet.xml,并在applicationContext.xml中import springmvc-servlet.xml

	```xml
	<!-- applicationContext.xml -->
	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
	       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	       xsi:schemaLocation="http://www.springframework.org/schema/beans
	       http://www.springframework.org/schema/beans/spring-beans.xsd">
	    
	    <!-- import springmvc配置文件 -->
	    <import resource="springmvc-servlet.xml"/>
	    
	</beans>
	```

	```xml
	<!-- springmvc-servlet.xml -->
	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
	       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	       xmlns:context="http://www.springframework.org/schema/context"
	       xmlns:mvc="http://www.springframework.org/schema/mvc"
	       xsi:schemaLocation="http://www.springframework.org/schema/beans
	       http://www.springframework.org/schema/beans/spring-beans.xsd
	       http://www.springframework.org/schema/context
	       https://www.springframework.org/schema/context/spring-context.xsd
	       http://www.springframework.org/schema/mvc
	       https://www.springframework.org/schema/mvc/spring-mvc.xsd">
	    <!-- 自动扫描包，让指定包下的注解生效,由IOC容器统一管理 -->
	    <context:component-scan base-package="com.soul.controller"/>
	    <!-- 配置各种handler，（让Spring MVC不处理静态资源） -->
	    <mvc:default-servlet-handler/>
	    <!--
	    支持mvc注解驱动
	    在spring中一般采用@RequestMapping注解来完成映射关系
	    要想使@RequestMapping注解生效
	    必须向上下文中注册DefaultAnnotationHandlerMapping
	    和一个AnnotationMethodHandlerAdapter实例
	    这两个实例分别在类级别和方法级别处理。
	    而annotation-driven配置帮助我们自动完成上述两个实例的注入。
	    -->
	    <mvc:annotation-driven/>
	    <!-- 视图解析器 -->
	    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" id="internalResourceViewResolver">
	        <!-- 前缀 -->
	        <property name="prefix" value="/WEB-INF/jsp/"/>
	        <!-- 后缀 -->
	        <property name="suffix" value=".jsp"/>
	    </bean>
	
	</beans>
	```

	这样我们的SpringMVC项目就配置完毕了

### 2. Controller实现

我们可以以注解的方式编写Controller

```java
@Controller	// 使用这个注解表示这个Controller已被SpringMVC托管
@RequestMapping("/encoding")	// 这个类下所有url的总访问路径
public class HelloController {
    @PostMapping("/t1")	// 设置url地址，可以为restful风格
    public String sendChinese(Model model, String name){	// 如果需要传递参数可以使用Model对象
        model.addAttribute("msg",name); //获取表单提交的值
        return "hello"; //跳转到test页面显示输入的值
    }
    
    /** 
     * 转发和重定向
     */
    
    @GetMapping("/forward")	
    public String forwardTest() {
        return "hello";	// 转发到jsp/hello.jsp 前缀和后缀是在springmvc的配置文件中定义的
    }

    @GetMapping("/redirect")
    public String redirectTest() {
        return "redirect:/index.jsp";	// 重定向到web/index.jsp
    }
    
    
    @GetMapping("/receiveObject")
    public String receiveObject(User user) {	// 可以接收对象
        System.out.println(user);
        return "hello";
    }
    
    @GetMapping("/add/{a}/{b}") // Restful风格
    public String add(Model model, @PathVariable int a, @PathVariable int b) {
        int res = a + b;
        model.addAttribute("msg", "result is: " + res);
        return "hello";
    }
    
    @RequestMapping("/json2")
    @ResponseBody // 使用这个标签就不会走视图解析器，会直接返回一个字符串
    public String json2() {

        User user = new User("David", 3);
        return JsonUtils.getJson(user);	// 需要导入fastjson包
    }
}
```

我们也可以在类上使用@RestController使得返回值都为json

```java
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String sayHello(Model model) {
        model.addAttribute("msg", "Hello, Springmvc-annotation!");
        return "hello";
    }
```

## IV. 整合SSM框架

SSM框架集即为：Spring+SpringMVC+MyBatis。其分工如下：

- **Spring**
	Spring就像是整个项目中装配bean的大工厂，在配置文件中可以指定使用特定的参数去调用实体类的构造方法来实例化对象。也可以称之为项目中的粘合剂。
	Spring的核心思想是IoC（控制反转），即不再需要程序员去显式地`new`一个对象，而是让Spring框架帮你来完成这一切。
- **SpringMVC**
	SpringMVC在项目中拦截用户请求，它的核心Servlet即DispatcherServlet承担中介或是前台这样的职责，将用户请求通过HandlerMapping去匹配Controller，Controller就是具体对应请求所执行的操作。SpringMVC相当于SSH框架中struts。
- **mybatis**
	mybatis是对jdbc的封装，它让数据库底层操作变的透明。mybatis的操作都是围绕一个sqlSessionFactory实例展开的。mybatis通过配置文件关联到各实体类的Mapper文件，Mapper文件中配置了每个类对数据库所需进行的sql语句映射。在每次与数据库交互时，通过sqlSessionFactory拿到一个sqlSession，再执行sql命令。

页面发送请求给控制器，控制器调用业务层处理逻辑，逻辑层向持久层发送请求，持久层与数据库交互，后将结果返回给业务层，业务层将处理逻辑发送给控制器，控制器再调用视图展现数据。

**配置SSM框架**

整合SSM框架只需要在SpringMVC配置流程的基础上加上以下步骤：

1. 在resource中新建必备的配置文件：

	- applicationContext.xml (已建)
	- springmvc-servlet.xml (已建)
	- database.properties
	- mybatis-config.xml
	- spring-dao.xml
	- spring-service.xml

2. 编写配置文件

	- database.properties

		```properties
		jdbc.driver=com.mysql.jdbc.Driver
		# 如果使用MySQL8.0以上加上时区的配置
		jdbc.url=jdbc:mysql://localhost:3306/ssmbuild?useSSL=true&useUnicode=true&characterEncoding=utf8&serverTimezone=UTC
		jdbc.username=root
		jdbc.password=123456
		```

	- mybatis-config.xml

		```xml
		<?xml version="1.0" encoding="UTF-8" ?>
		<!DOCTYPE configuration
		        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
		        "http://mybatis.org/dtd/mybatis-3-config.dtd">
		<configuration>
		    <!-- 别名 -->
		    <typeAliases
		        <package name="com.soul.pojo"/>
		    </typeAliases>
		</configuration>
		```

	- spring-dao.xml

		```xml
		<?xml version="1.0" encoding="UTF-8"?>
		<beans xmlns="http://www.springframework.org/schema/beans"
		       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		       xmlns:context="http://www.springframework.org/schema/context"
		       xsi:schemaLocation="http://www.springframework.org/schema/beans
		       http://www.springframework.org/schema/beans/spring-beans.xsd
		       http://www.springframework.org/schema/context
		       https://www.springframework.org/schema/context/spring-context.xsd">
		
		    <!-- 配置整合mybatis -->
		    <!-- 1.关联数据库文件 -->
		    <context:property-placeholder location="classpath:database.properties"/>
		
		
		    <!-- 2.数据库连接池 -->
		    <!--数据库连接池
		    dbcp 半自动化操作 不能自动连接
		    c3p0 自动化操作（自动的加载配置文件 并且设置到对象里面）
		    -->
		    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
		        <!-- 配置连接池属性 -->
		        <property name="driverClass" value="${jdbc.driver}"/>
		        <property name="jdbcUrl" value="${jdbc.url}"/>
		        <property name="user" value="${jdbc.username}"/>
		        <property name="password" value="${jdbc.password}"/>
		        <!-- c3p0连接池的私有属性 -->
		        <property name="maxPoolSize" value="30"/>
		        <property name="minPoolSize" value="10"/>
		        <!-- 关闭连接后不自动commit -->
		        <property name="autoCommitOnClose" value="false"/>
		        <!-- 获取连接超时时间 -->
		        <property name="checkoutTimeout" value="10000"/>
		        <!-- 当获取连接失败重试次数 -->
		        <property name="acquireRetryAttempts" value="2"/>
		    </bean>
		
		
		    <!-- 3.配置SqlSessionFactory对象 -->
		    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		        <!-- 注入数据库连接池 -->
		        <property name="dataSource" ref="dataSource"/>
		        <!-- 配置MyBaties全局配置文件:mybatis-config.xml -->
		        <property name="configLocation" value="classpath:mybatis-config.xml"/>
		    </bean>
		
		
		    <!-- 4.配置扫描Dao接口包，动态实现Dao接口注入到spring容器中 -->
		    <!--解释 ： https://www.cnblogs.com/jpfss/p/7799806.html-->
		    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
		        <!-- 注入sqlSessionFactory -->
		        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
		        <!-- 给出需要扫描Dao接口包 -->
		        <property name="basePackage" value="com.soul.dao"/>
		    </bean>
		</beans>
		```

	- spring-service.xml

		```xml
		<?xml version="1.0" encoding="UTF-8"?>
		<beans xmlns="http://www.springframework.org/schema/beans"
		       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		       xmlns:context="http://www.springframework.org/schema/context"
		       xsi:schemaLocation="http://www.springframework.org/schema/beans
		        http://www.springframework.org/schema/beans/spring-beans.xsd
		        http://www.springframework.org/schema/context
		        http://www.springframework.org/schema/context/spring-context.xsd">
		    <!-- 扫描service相关的bean -->
		    <context:component-scan base-package="com.soul.service" />
		
		    <!--BookServiceImpl注入到IOC容器中-->
		    <bean id="BookServiceImpl" class="com.soul.service.BookServiceImpl">
		        <property name="bookMapper" ref="bookMapper"/>
		    </bean>
		
		    <!-- 配置事务管理器 -->
		    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		        <!-- 注入数据库连接池 -->
		        <property name="dataSource" ref="dataSource" />
		    </bean>
		</beans>
		```

	

3. 在applicationContext.xml中import spring和mybatis(当然包括之前import的springmvc-servlet)的配置文件：

	```xml
	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
	       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	       xsi:schemaLocation="http://www.springframework.org/schema/beans
	       http://www.springframework.org/schema/beans/spring-beans.xsd">
	    <import resource="spring-dao.xml"/>
	    <import resource="spring-service.xml"/>
	    <import resource="springmvc-servlet.xml"/>
	</beans>
	```

**加入功能流程**

1. 编写controller层的controller

	```java
	package com.soul.controller;
	
	import com.soul.pojo.Book;
	import com.soul.service.BookService;
	import org.apache.ibatis.annotations.Param;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.beans.factory.annotation.Qualifier;
	import org.springframework.stereotype.Controller;
	import org.springframework.ui.Model;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.PathVariable;
	import org.springframework.web.bind.annotation.PostMapping;
	import org.springframework.web.bind.annotation.RequestMapping;
	
	import java.util.List;
	
	@Controller
	@RequestMapping("/book")
	public class BookController {
	    @Autowired
	    @Qualifier("BookServiceImpl")
	    BookService bookService;
	
	    @GetMapping("/getAllBooks")
	    public String getAllBooks(Model model) {
	        List<Book> books = bookService.getAllBooks();
	        model.addAttribute("booklist", books);
	        return "displayAllbooks";
	    }
	
	    @RequestMapping("/toAddBook")
	    // 转跳到addBook的页面
	    public String toAddBookPage() {
	        return "addBook";
	    }
	
	    @RequestMapping("/addBook")
	    public String addBook(Book book) {
	        System.out.println(book);
	        bookService.addBook(book);
	        return "redirect:/book/getAllBooks";
	    }
	
	    @RequestMapping("/toUpdateBook")
	    public String toUpdateBook(Model model, @Param("id") int id) {
	        System.out.println("entered toUpdateBook method");
	        Book book = bookService.getBookById(id);
	        model.addAttribute("book", book);
	        return "updateBook";
	    }
	
	    @PostMapping("/updateBook")
	    public String updateBook(Model model, Book book) {
	        System.out.println("entered updateBook method");
	        bookService.updateBook(book);
	        System.out.println("executed bookService.updateBook() method");
	        return "redirect:/book/getAllBooks";
	    }
	
	    @RequestMapping("/deleteBook/{bookId}")
	    public String deleteBook(@PathVariable("bookId") int id) {
	        bookService.deleteBookById(id);
	        return "redirect:/book/getAllBooks";
	    }
	
	
	
	}
	
	```

	

2. 编写service层业务接口和实现类

  ```java
  package com.soul.service;
  
  import com.soul.dao.BookMapper;
  import com.soul.pojo.Book;
  import org.apache.ibatis.annotations.Param;
  
  import java.util.List;
  
  public interface BookService {
  
      int addBook(Book book);
  
      int deleteBookById(int id);
  
      int updateBook(Book book);
  
      Book getBookById(int id);
  
      List<Book> getAllBooks();
  }
  ```

  ```java
  package com.soul.service;
  
  import com.soul.dao.BookMapper;
  import com.soul.pojo.Book;
  
  import java.util.List;
  
  public class BookServiceImpl implements BookService{
  
      BookMapper bookMapper;
  
      public void setBookMapper(BookMapper bookMapper) {
          this.bookMapper = bookMapper;
      }
  
      @Override
      public int addBook(Book book) {
          return bookMapper.addBook(book);
      }
  
      @Override
      public int deleteBookById(int id) {
          return bookMapper.deleteBookById(id);
      }
  
      @Override
      public int updateBook(Book book) {
          return bookMapper.updateBook(book);
      }
  
      @Override
      public Book getBookById(int id) {
          return bookMapper.getBookById(id);
      }
  
      @Override
      public List<Book> getAllBooks() {
          return bookMapper.getAllBooks();
      }
  }
  ```

3. 编写dao层接口和mapper.xml:

  ```java
  import org.apache.ibatis.annotations.Param;
  
  import java.util.List;
  
  public interface BookMapper {
  
      int addBook(Book book);
  
      int deleteBookById(@Param("bookID") int id);
  
      int updateBook(Book book);
  
      Book getBookById(@Param("bookID") int id);
  
      List<Book> getAllBooks();
  }
  ```

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper
          PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <mapper namespace="com.soul.dao.BookMapper">
      <insert id="addBook" parameterType="book">
          insert into ssmbuild.books(bookName, bookCounts, detail)
          VALUES (#{bookName}, #{bookCounts}, #{detail});
      </insert>
  
      <delete id="deleteBookById">
          delete from ssmbuild.books where bookID = #{bookID};
      </delete>
  
      <update id="updateBook" parameterType="book">
          update ssmbuild.books
          set bookName = #{bookName}, bookCounts = #{bookCounts}, detail = #{detail}
          where bookID = #{bookID};
      </update>
  
      <select id="getBookById" resultType="book">
          select * from ssmbuild.books where bookID = #{bookID};
      </select>
  
      <select id="getAllBooks" resultType="book">
          select * from ssmbuild.books;
      </select>
  </mapper>
  ```

## V. 容易踩的坑

- 导包到Maven项目后，不要忘记也要把包通过Project Structure导入到Tomcat中
	1. 打开Project Structure后点击Artifacts
	2. 选择对应的项目
	3. 在WEB-INF下创建lib文件夹
	4. 导入所有jar包（点击小+号）
- 不要忘记配置tomcat

## VI.总结

**SpringMVC是Spring用于实现MVC框架的产品**。由于其易用性和高性能，使得开发难度大大降低，开发效率大大提高。但是，我们在学习过程中不难发现，SpringMVC的配置过程仍然非常繁琐，于是乎Springboot应运而生。比起SpringMVC，SpringBoot更加简单容易，而我们现在还去学习SpringMVC的目的是为了了解其原理，这样我们才能很好的使用SpringBoot。

## VII. 参考

[狂神说: SpringMVC][https://www.bilibili.com/video/BV1aE41167Tu]

[百度百科][https://baike.baidu.com/]

