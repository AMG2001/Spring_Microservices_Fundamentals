# All required fundamentals configuration

- Link of Notion :

  [https://mohamad-daif-library.notion.site/All-required-fundamentals-configuration-30e7ee81afea809abc5de8bdaa021fcc?source=copy_link](https://www.notion.so/All-required-fundamentals-configuration-30e7ee81afea809abc5de8bdaa021fcc?pvs=21)


---

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

## Gateway

- From it’s name, the Gateway is acting as a single entry point for the all microservices that we have within our mesh.
- instead of hitting each service with each url and each port which lead to different base urls for all microservices, Gateway came to unify the base url for all microservices.

### How the Gateway component is working.

1. eureka server is the first launched component to be able to register all microservices and instances.
2. Gateway launch as a eureka client which means that each service will be regisetered within the erueka server, the Gateway will be synced and aware with.
3. All microservices will be launched and registered within the erueka server and after a couple of seconds the gateway will sync with the server and will be aware with the services and it’s instances.

### How to create and configure a Gateway.

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

## Config Server

### How Config Server name the configuration of each microservice ?

- While working with different microservices and each one has a different configuration for different stages, then coupling the code and configuration together within the same project is a nightmare.
- And for decoupling the configuration from the service files, we use a very important project within spring cloud called Config Server.
- Config server is the service which read the configuration of the services and each service with each all listed stages configuration ( Dev, Test, Qa, Prod ), and then expose and pass these configuration for the services.
    - And the way that this server is storing the configuration and able to identify that these configuraiton are related to this service is <nameOfService- name ofEnv> files ) i.e. accounts-qa.
        - Config server is heavily depend on the naming conventions so we need to be aware with the names that we list
- As we are Assuming that for each service, it has 3 configuration for 3 envs :
    - default configuration is named with the name of the service.
    - other envs are named like <nameOfService> - <nameOfEnv>.yml
    - It is mandatory to use ‘-’ not ‘_’ or any other symbols.

        ```
        resources/
        ├── config/
        │   ├── accounts.yml
        │   ├── accounts-prod.yml
        │   ├── accounts-qa.yml
        │   ├── cards.yml
        │   ├── cards-prod.yml
        │   ├── cards-qa.yml
        │   ├── loans.yml
        │   ├── loans-prod.yml
        │   └── loans-qa.yml
        └── application.yml
        ```


### How to configure Conig Server and Config client services.

- While creating Config server, there are 3 steps that we need to move within :
    1. Store the configuration of the services within a Git repo.
    2. Create a Config server which reads these configuration.
    3. Add config server url configuration within client services to be able to read the configuration from ( note that the name of the service must be the same as the name of the configuration file to enable the config server to identify that these configuration are related to this service.



### Storing configuration on Git repo.

```
accounts.yml
accounts-prod.yml
accounts-qa.yml
cards.yml
cards-prod.yml
cards-qa.yml
loans.yml
loans-prod.yml
loans-qa.yml
```

### Creating & Configuring Config Server.

- The dependencies that we need are :

    ```xml
    <properties>
    		<java.version>17</java.version>
    		<spring-cloud.version>2025.0.1</spring-cloud.version>
    	</properties>
    
    	<dependencies>
    	<!-- config server depdendency -->
    		<dependency>
    			<groupId>org.springframework.cloud</groupId>
    			<artifactId>spring-cloud-config-server</artifactId>
    		</dependency>
    		<!-- if you are using eureka server -->
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

- And inside main, you need to enable config server :

    ```java
    @SpringBootApplication
    @EnableConfigServer
    @EnableDiscoveryClient
    public class ConfigServerApplication {
    
    	public static void main(String[] args) {
    		SpringApplication.run(ConfigServerApplication.class, args);
    	}
    
    }
    
    ```

- And the last thing to enable the server to read the configuration from github is :

    ```yaml
    server:
      port: 8888
    
    spring:
      application:
        name: configserver
      cloud:
        config:
          server:
            git:
              uri: https://github.com/Mohamad-Daif/Product_and_Coupon_Remote_Configuration.git
              default-label: main
              clone-on-start: true
              force-pull: true
              timeout: 15
            bootstrap: false
    
    eureka:
      client:
        service-url:
          defaultZone: http://localhost:8761/eureka/
    ```


### Configure config client service.

- Inside the config client service, we just need to use dependency :

    ```xml
    		<dependency>
    			<groupId>org.springframework.cloud</groupId>
    			<artifactId>spring-cloud-starter-config</artifactId>
    		</dependency>
    ```

- And also just configure 2 things inside the application.yml file of the service :
    1. name of service and must match the name of the config file.
    2. the url of the config server to be able to connect and fetch the configuration.
        - In case you are not using erueka server, then you need to explicitly define the url of the config server :

            ```yaml
            spring:
              application:
                name: coupon
            
              config:
                import: "optional:configserver:http://localhost:8888"
            ```

        - If you are using eureka server, then there is no need to mention the url of the configserver :

            ```yaml
            spring:
              application:
                name: coupon
            
              config:
                import: "optional:configserver:"
            ```

- You can validate that the config server is working properly and seeing the configuration via trying to call any configuration of any service via :

    ```
    http://localhost:8888/<service-name>/<stage>
    ```

    ```
    http://localhost:8888/coupon/default
    ```

    - response will be something like :

        ```json
        {
          "name": "coupon",
          "profiles": [
            "default"
          ],
          "label": null,
          "version": "4b0e3d19e21f215f106599e3e929b2e57d5212ff",
          "state": "",
          "propertySources": [
            {
              "name": "https://github.com/Mohamad-Daif/Product_and_Coupon_Remote_Configuration.git/coupon.yml",
              "source": {
                "server.port": 8085,
                "eureka.client.service-url.defaultZone": "http://localhost:8761/eureka/",
                "eureka.instance.hostname": "localhost"
              }
            }
          ]
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

                ```
                http://mohamad_daif:8080/<url>
                ```

            2. it become :

                ```
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

## Resilience4J for Fault tolerance.

- While microservices are communicating with each other via http requests and responses, it one of the calls may fail, and to be able to defend the microservieces from aggregating the error, we can easily use Reseilience4J depdency.
- Fault tolerance means that in case of faliure we are going to retry multiple times and in case there is no response, then fail without terminating the app.

### Required dependencies.

- As Reselience4J depend on aop, then we need to add it :

    ```xml
    		<dependency>
    			<groupId>io.github.resilience4j</groupId>
    			<artifactId>resilience4j-spring-boot3</artifactId>
    			<version>2.2.0</version>
    		</dependency>
    		<dependency>
    			<groupId>org.springframework.boot</groupId>
    			<artifactId>spring-boot-starter-aop</artifactId>
    		</dependency>
    ```

    - yaml configuration for defining the number of tries and the duration for each try :

        ```yaml
        # resilience4j retries configuration
        resilience4j:
          retry:
            instances:
              getProductRetry:
                maxAttempts: 3              # Total 3 attempts (1 initial + 2 retries)
                waitDuration: 1s            # Wait 1 second between retries
        ```

- And inside the code, over the http call which is hapenning, we can define our retry mechanism :

    ```java
        @GetMapping
        @Retry(name = "getProductRetry", fallbackMethod = "fallbackMethod")
        public ResponseEntity<Product> getProduct(@RequestBody ProductRequest productRequest) {
            return ResponseEntity.ok(productService.getProduct(productRequest.productName(), productRequest.couponName()));
        }
    
        public ResponseEntity<Product> fallbackMethod(
                @RequestBody ProductRequest productRequest,  // Same as original
                Exception ex
        ) {
            // Your fallback logic
            return ResponseEntity.ok(productService.getProduct(productRequest.productName(), productRequest.couponName()));
        }
    }
    
    ```