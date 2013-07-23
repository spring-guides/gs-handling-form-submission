
# Getting Started: Handling Form Submission


What you'll build
-----------------

This guide walks you through creating a web form with Spring. The service will accept HTTP GET requests at:

    http://localhost:8080/greeting

and respond with a web page displaying a form. You can submit a greeting by populating the `id` and `content` form fields. A results page will be displayed when a form is submitted.


What you'll need
----------------

 - About 15 minutes
 - A favorite text editor or IDE
 - [JDK 6][jdk] or later
 - [Maven 3.0][mvn] or later

[jdk]: http://www.oracle.com/technetwork/java/javase/downloads/index.html
[mvn]: http://maven.apache.org/download.cgi


How to complete this guide
--------------------------

Like all Spring's [Getting Started guides](/guides/gs), you can start from scratch and complete each step, or you can bypass basic setup steps that are already familiar to you. Either way, you end up with working code.

To **start from scratch**, move on to [Set up the project](#scratch).

To **skip the basics**, do the following:

 - [Download][zip] and unzip the source repository for this guide, or clone it using [git](/understanding/git):
`git clone https://github.com/springframework-meta/gs-handling-form-submission.git`
 - cd into `gs-handling-form-submission/initial`.
 - Jump ahead to [Create a web controller](#initial).

**When you're finished**, you can check your results against the code in `gs-handling-form-submission/complete`.
[zip]: https://github.com/springframework-meta/gs-handling-form-submission/archive/master.zip


<a name="scratch"></a>
Set up the project
------------------

First you set up a basic build script. You can use any build system you like when building apps with Spring, but the code you need to work with [Maven](https://maven.apache.org) and [Gradle](http://gradle.org) is included here. If you're not familiar with either, refer to [Building Java Projects with Maven](/guides/gs/maven/content) or [Building Java Projects with Gradle](/guides/gs/gradle/content).

### Create the directory structure

In a project directory of your choosing, create the following subdirectory structure; for example, with `mkdir -p src/main/java/hello` on *nix systems:

    └── src
        └── main
            └── java
                └── hello

### Create a Maven POM

`pom.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.springframework</groupId>
    <artifactId>gs-handling-form-submission</artifactId>
    <version>0.1.0</version>

    <parent>
        <groupId>org.springframework.bootstrap</groupId>
        <artifactId>spring-bootstrap-starters</artifactId>
        <version>0.5.0.BUILD-SNAPSHOT</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.bootstrap</groupId>
            <artifactId>spring-bootstrap-web-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.thymeleaf</groupId>
            <artifactId>thymeleaf-spring3</artifactId>
        </dependency>
    </dependencies>

    <!-- TODO: remove once bootstrap goes GA -->
    <repositories>
        <repository>
            <id>spring-snapshots</id>
            <url>http://repo.springsource.org/snapshot</url>
            <snapshots><enabled>true</enabled></snapshots>
        </repository>
    </repositories>
    <pluginRepositories>
        <pluginRepository>
            <id>spring-snapshots</id>
            <url>http://repo.springsource.org/snapshot</url>
            <snapshots><enabled>true</enabled></snapshots>
        </pluginRepository>
    </pluginRepositories>
</project>
```

TODO: mention that we're using Spring Bootstrap's [_starter POMs_](../gs-bootstrap-starter) here.

Note to experienced Maven users who are unaccustomed to using an external parent project: you can take it out later, it's just there to reduce the amount of code you have to write to get started.


<a name="initial"></a>
Create a web controller
-----------------------

In Spring's approach to building web sites, HTTP requests are handled by a controller. These components are easily identified by the [`@Controller`] annotation, and the GreetingController below handles GET requests for /greeting by returning the name of a [`View`], in this case, "greeting". A `View` is responsible for rendering the HTML content:

`src/main/java/hello/GreetingController.java`
```java
package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

    @RequestMapping(value="/greeting", method=RequestMethod.GET)
    public String greetingForm(Model model) {
        model.addAttribute("greeting", new Greeting());
        return "greeting";
    }

    @RequestMapping(value="/greeting", method=RequestMethod.POST)
    public String greetingSubmit(@ModelAttribute Greeting greeting, Model model) {
    	model.addAttribute("greeting", greeting);
        return "result";
    }

}
```

This controller is concise and simple, but there is plenty happening. Let's analyze it step by step.

The `@RequestMapping` annotation allows you to map HTTP requests to specific controller methods. The two methods in this controller are both mapped to `/greeting`. By default `@RequestMapping` maps all HTTP operations, i.e. `GET`, `POST`, etc. But in this case the `greetingForm()` method is specifically mapped to `GET` using `@RequestMapping(method=GET)`, while `greetingSubmit()` is mapped to `POST` with `@RequestMapping(method=POST)`. This allows the controller to differentiate the requests to the  `/greeting` endpoint.

The `greetingForm()` method uses a [`Model`] object to expose a new `Greeting` to the view template. The `Greeting` object contains fields, such as `id` and `content` that correspond to the form fields in the `greeting` view, and will be used to capture the information from the form.

`src/main/java/hello/Greeting.java`
```java
package hello;

public class Greeting {

    private long id;
    private String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
```

The implementation of the method body is relying on a view technology, in this case [Thymeleaf][u-thymeleaf], to perform server-side rendering of the HTML. Thymeleaf parses the `greeting.html` template below and evaluates the various template expressions to render the form.

`src/main/resources/templates/greeting.html`
```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head> 
    <title>Getting Started: Handing Form Submission</title> 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
	<h1>Form</h1>
    <form action="#" th:action="@{/greeting}" th:object="${greeting}" method="post">
    	<p>Id: <input type="text" th:field="*{id}" /></p>
        <p>Message: <input type="text" th:field="*{content}" /></p>
        <p><input type="submit" value="Submit" /> <input type="reset" value="Reset" /></p>
    </form>
</body>
</html>
```

The `th:action="@{/greeting}"` expression directs the form to POST to the `/greeting` endpoint, while the `th:object="${greeting}"` expression declares the model object to use for collecting the form data. The two form fields, expressed with `th:field="*{id}"` and `th:field="*{content}"` correspond to the fields in the `Greeting` object above.

That covers the controller, model, and view for presenting the form. Now let's review the process of submitting the form. As noted above, the form submits to the `/greeting` endpoint using a `POST`. The `greetingSubmit()` method receives the `Greeting` object that was populated by the form. It then adds that populated object to the model so the submitted data can be rendered in the view. The `id` is rendered in the `<p th:text="'id: ' + ${greeting.id}" />` expression. Likewise the `content` is rendered in the `<p th:text="'content: ' + ${greeting.content}" />` expression.

`src/main/resources/templates/result.html`
```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head> 
    <title>Getting Started: Handing Form Submission</title> 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
	<h1>Result</h1>
    <p th:text="'id: ' + ${greeting.id}" />
    <p th:text="'content: ' + ${greeting.content}" />
    <a href="/greeting">Submit another message</a>
</body>
</html>
```

For clarity, this example utilizes two separate view templates for rendering the form and displaying the submitted data, however it is certainly possible to use a single view for both purposes.


Make the application executable
-------------------------------

Although it is possible to package this service as a traditional [WAR][u-war] file for deployment to an external application server, the simpler approach demonstrated below creates a standalone application. You package everything in a single, executable JAR file, driven by a good old Java `main()` method. Along the way, you use Spring's support for embedding the [Tomcat][u-tomcat] servlet container as the HTTP runtime, instead of deploying to an external instance.

### Create an Application class

`src/main/java/hello/Application.java`
```java
package hello;

import org.springframework.bootstrap.SpringApplication;
import org.springframework.bootstrap.context.annotation.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

The `main()` method defers to the [`SpringApplication`][] helper class, providing `Application.class` as an argument to its `run()` method. This tells Spring to read the annotation metadata from `Application` and to manage it as a component in the [Spring application context][u-application-context].

The [`@ComponentScan`][] annotation tells Spring to search recursively through the `hello` package and its children for classes marked directly or indirectly with Spring's [`@Component`][] annotation. This directive ensures that Spring finds and registers the `GreetingController`, because it is marked with [`@Controller`][], which in turn is a kind of [`@Component`][] annotation.

The [`@EnableAutoConfiguration`][] annotation switches on reasonable default behaviors based on the content of your classpath. For example, because the application depends on the embeddable version of Tomcat (tomcat-embed-core.jar), a Tomcat server is set up and configured with reasonable defaults on your behalf. And because the application also depends on Spring MVC (spring-webmvc.jar), a Spring MVC [`DispatcherServlet`][] is configured and registered for you — no `web.xml` necessary! Auto-configuration is a powerful, flexible mechanism. See the [API documentation][`@EnableAutoConfiguration`] for further details.

### Build an executable JAR

Now that your `Application` class is ready, you simply instruct the build system to create a single, executable jar containing everything. This makes it easy to ship, version, and deploy the service as an application throughout the development lifecycle, across different environments, and so forth.

Add the following configuration to your existing Maven POM:

`pom.xml`
```xml
    <properties>
        <start-class>hello.Application</start-class>
    </properties>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.zero</groupId>
                <artifactId>spring-package-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

The `start-class` property tells Maven to create a `META-INF/MANIFEST.MF` file with a `Main-Class: hello.Application` entry. This entry enables you to run the jar with `java -jar`.

The [Spring Package maven plugin][spring-package-maven-plugin] collects all the jars on the classpath and builds a single "über-jar", which makes it more convenient to execute and transport your service.

Now run the following to produce a single executable JAR file containing all necessary dependency classes and resources:

```sh
$ mvn package
```

[spring-package-maven-plugin]: https://github.com/SpringSource/spring-zero/tree/master/spring-package-maven-plugin

> **Note:** The procedure above will create a runnable JAR. You can also opt to [build a classic WAR file](/guides/gs/convert-jar-to-war/content) instead.

Run the service
-------------------
Run your service with `java -jar` at the command line:

```sh
$ java -jar target/gs-handling-form-submission-0.1.0.jar
```


Logging output is displayed. The service should be up and running within a few seconds.


Test the service
----------------

Now that the web site is running, visit <http://localhost:8080/greeting>, where you see the following form:

![Form](images/form.png)

Submit an id and message to see the results:

![Result](images/result.png)


Summary
-------

Congratulations! You have just submitted a form using Spring.


[u-war]: /understanding/war
[u-tomcat]: /understanding/tomcat
[u-application-context]: /understanding/application-context
[u-thymeleaf]: /understanding/thymeleaf
[`View`]: http://static.springsource.org/spring/docs/current/javadoc-api/org/springframework/web/servlet/View.html
[`Model`]: http://static.springsource.org/spring/docs/current/javadoc-api/org/springframework/ui/Model.html
[`@Configuration`]:http://static.springsource.org/spring/docs/current/javadoc-api/org/springframework/context/annotation/Configuration.html
[`@ComponentScan`]: http://static.springsource.org/spring/docs/current/javadoc-api/org/springframework/context/annotation/ComponentScan.html
[`@Component`]: http://static.springsource.org/spring/docs/current/javadoc-api/org/springframework/stereotype/Component.html
[`@Controller`]: http://static.springsource.org/spring/docs/current/javadoc-api/org/springframework/stereotype/Controller.html
[`SpringApplication`]: http://static.springsource.org/spring-bootstrap/docs/0.5.0.BUILD-SNAPSHOT/javadoc-api/org/springframework/bootstrap/SpringApplication.html
[`DispatcherServlet`]: http://static.springsource.org/spring/docs/current/javadoc-api/org/springframework/web/servlet/DispatcherServlet.html
[`@EnableAutoConfiguration`]: http://static.springsource.org/spring-bootstrap/docs/0.5.0.BUILD-SNAPSHOT/javadoc-api/org/springframework/bootstrap/context/annotation/SpringApplication.html
