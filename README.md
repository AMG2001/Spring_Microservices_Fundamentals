# All required fundamentals configuration

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


---

## OpenFeign clients for HTTP Requests.

- As the microservices need to communicate with each other, there are more than 1 way for communication :
    1. Using Imperative way with RestTemplate.
    2. Using declarative way with OpenFeign.
- And as OpenFeign is the more cleaner and smarter way of communication and also OpenFeign provide an awesome compatability with Spring cloud, we are going to use it.

## How to define OpenFiegn Client.

- As our product service want to communicate with Coupon service, then this is the way that we define the OpenFeign client for http call :
    1. Dependency :

        ```xml
        <properties>
            <java.version>17</java.version>
            <spring-cloud.version>2025.1.0</spring-cloud.version>
          </properties>
          
          <dependencies>
            <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-openfeign</artifactId>
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

    2. Contract of client :
        1. feign clients depend on defined interfaces with methods signature and Spring MVC annotation :

            ```java
            @FeignClient("COUPONSERVICE")
            public interface CouponClient {
            
                @GetMapping("/coupon/{couponName}")
                Coupon getCoupon(@PathVariable String couponName);
            
            }
            
            ```


## Issue you need to be aware with.

1. Sometimes that [localhost](http://localhost) name of your machine may contain some special character like space, _ or ‘.’, and in this case the URI and DNS resolver will not be able to reach for it.
    - Example for invalid hostname : mohamad daif, mohamad_daif.
- And to solve this issue, there are 2 ways for solving it :
    1. forcing the services to use explicit ‘[localhost](http://localhost)’ String value instead of binding it to the real name of the localhost on your machine :

        ```yaml
        eureka:
          client:
            service-url:
              defaultZone: http://localhost:8761/eureka/
          instance:
            hostname: localhost
        ```

        - Effect of this solution 👇.
            1. instead of [localhost](http://localhost) is binded automatically and the request become :

                ```text
                http://mohamad_daif:8080/<url>
                ```

            2. it become :

                ```text
                http://localhost:8080/<url>
                ```

    2. Using preferred id address ( this way is recommended only for local machine only, in case you want to move with docker then this way will ne be recommended).

        ```yaml
        eureka:
          client:
            service-url:
              defaultZone: http://localhost:8761/eureka/
          instance:
            prefer-ip-address: true
        ```


---

## Client Side load balance.

- As the same service may have more than 1 instance, openFeign provide a mechanism to route the requests between these service instances to load balance between instances.
- This can easily be performed by just adding :

    ```xml
      <properties>
        <java.version>17</java.version>
        <spring-cloud.version>2025.1.0</spring-cloud.version>
      </properties>
      
      <dependencies>
        <dependency>
          <groupId>org.springframework.cloud</groupId>
          <artifactId>spring-cloud-starter-loadbalancer</artifactId>
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


---

## Gateway

- From it’s name, the Gateway is acting as a single entry point for the all microservices that we have within our mesh.
- instead of hitting each service with each url and each port which lead to different base urls for all microservices, Gateway came to unify the base url for all microservices.

## How the Gateway component is working.

1. eureka server is the first launched component to be able to register all microservices and instances.
2. Gateway launch as a eureka client which means that each service will be regisetered within the erueka server, the Gateway will be synced and aware with.
3. All microservices will be launched and registered within the erueka server and after a couple of seconds the gateway will sync with the server and will be aware with the services and it’s instances.

## How to create and configure a Gateway.

- To be able to create a gateway service, then all what you need is :
    - Dependencies :

        ```xml
        <properties>
            <java.version>17</java.version>
            <spring-cloud.version>2025.1.0</spring-cloud.version>
          </properties>
          <dependencies>
        		<dependency>
        			<groupId>org.springframework.cloud</groupId>
        			<artifactId>spring-cloud-starter-gateway-server-webmvc</artifactId>
        		</dependency>
        
        		<dependency>
        			<groupId>org.springframework.cloud</groupId>
        			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        		</dependency>
        
        		<dependency>
        			<groupId>org.springframework.cloud</groupId>
        			<artifactId>spring-cloud-starter-loadbalancer</artifactId>
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

    - application configuration

        ```yaml
        server:
          port: 9095
        
        spring:
          application:
            name: Gateway
        
        # gateway configuration.
          cloud:
            gateway:
              server:
                webmvc:
                  routes:
                    - id: product-service-route
                      uri: lb://PRODUCTSSERVICE
                      predicates:
                        - Path=/product, /product/**
        
                    - id: coupon-service-route
                      uri: lb://COUPONSERVICE
                      predicates:
                        - Path=/coupon, /coupon/**
                        
        #Eureka client configuration.
        eureka:
          client:
            service-url:
              defaultZone: http://localhost:8761/eureka/ # the url of the service registry server.
          instance:
            hostname: localhost
        ```

    - enable eureka client dependency

        ```java
        @SpringBootApplication
        @EnableDiscoveryClient
        public class GatewayApplication {
        
        	public static void main(String[] args) {
        		SpringApplication.run(GatewayApplication.class, args);
        	}
        
        }
        ```


---