# Spring_Cloud_Fundamentals

---

# Required Configurations for Eureka Server & Client.

## For Eureka Server.

- For eureka-server, there are a pre-defined configurations that we need to provide to be able to launch it :
    - Dependencies :

        ```xml
          <properties>
            <java.version>17</java.version>
            <spring-cloud.version>2025.0.1</spring-cloud.version>
          </properties>
          
          <dependencies>
            <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
            </dependency>
          </dependencies>
          
          <dependencyManagement>
            <dependencies>
              <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
              </dependency>
            </dependencies>
          </dependencyManagement>
        ```

    - application.yml content :

        ```yaml
        spring:
          application:
            name: eureka-server
        
        server:
          port: 8761
        
        eureka:
          client:
            register-with-eureka: false
            fetch-registry: false
        ```

    - And in Main class :

        ```java
        @SpringBootApplication
        @EnableEurekaServer
        public class ServiceRegisterServerApplication {
        
        	public static void main(String[] args) {
        		SpringApplication.run(ServiceRegisterServerApplication.class, args);
        	}
        
        }
        ```

    - And the URL for viewing the report of eureka server :

        ```
        http://localhost:8761/
        ```


---

## Eureka Clients.

- To enable services to discover and register themselfs within the server, then all what we need is :
    - Dependencies :

        ```xml
        <properties>
            <java.version>17</java.version>
            <spring-cloud.version>2025.0.1</spring-cloud.version>
          </properties>
          
          <dependencies>
            <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
            </dependency>
          </dependencies>
          
          <dependencyManagement>
            <dependencies>
              <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
              </dependency>
            </dependencies>
          </dependencyManagement>
        ```

    - inside application.yml :

        ```yaml
        spring:
          application:
            name: CouponService # very important as it is the id of the service.
        
        server:
          port: 8081   # Each service must have unique port
        
        eureka:
          client:
            service-url:
              defaultZone: http://localhost:8761/eureka/ # the url of the service registry server.
        ```

    - in Main class :

        ```java
        @SpringBootApplication
        @EnableDiscoveryClient
        public class CouponServiceApplication {
        
        	public static void main(String[] args) {
        		SpringApplication.run(CouponServiceApplication.class, args);
        	}
        
        }
        
        ```