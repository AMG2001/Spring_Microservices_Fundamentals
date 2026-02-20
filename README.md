# Spring_MicroServices_Fundamentals
This repo contain an implementation for the whole terms and concepts which i've learned while developing Spring Cloud project

# Required Configurations.

## For Eureka Server.

- For eureka-server, there are a pre-defined configurations that we need to provide to be able to launch it :
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


---

## Eureka Clients.

- To enable services to discover and register themselfs within the server, then all what we need is :
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