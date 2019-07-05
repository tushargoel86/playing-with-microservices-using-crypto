This service is front end of our application. All user requests and microservices to microservices interaction will be
done through API gateway only.

This API gateway is responsible for:

1) Routing the request to asked service. It will identify the service URL through naming server i.e Eureka
2) Request for token. API gateway than send reqeust to AUTH service for token request
3) Create user. It will than forward request to AUTH. Only user with ADMIN role can create the user
4) It will authorize the use for user creation
5) It validates user token
6) Custom exception handling for 'Access Denied' and 'Invalid Credentials'

Directory structure:

```
C:.
│   pom.xml
│
└───src
    └───main
        ├───java
        │   └───com
        │       └───tushar
        │           └───crypto
        │               └───apigateway
        │                   │   APIGatewayServer.java
        │                   │
        │                   ├───config
        │                   │       JwtConfig.java
        │                   │       SecurityConfig.java
        │                   │
        │                   ├───endpoint
        │                   │       CreateUserProxy.java
        │                   │       EndPoint.java
        │                   │
        │                   ├───exceptionhandler
        │                   │       CustomAccessDeniedHandler.java
        │                   │       CustomAuthenticationEntryPointHandler.java
        │                   │       CustomAuthenticationFailureHandler.java
        │                   │       ExceptionHandlerFilter.java
        │                   │
        │                   └───filter
        │                           JwtAuthorizationFilter.java
        │
        └───resources
                bootstrap.properties
```

REST APIs:

PORTS used

```bash

Services	    Ports
API Gateway	   8765
Eureka Server	 8761
Config Server	 9999
Auth Service	 8899

```


# for token creation:   http://localhost:8765/auth/token

request:  JSON request

{
	"username": xxxx,
	"password" :  xxx
}

# for user creation:  http://localhost:8765/createUser

In addition to form variables defined as below we also need to send JWT token as authroization header.

Only user with **admin** access can create a new user.

form variables:
```
username:  
password:
role:  (USER/ADMIN)
```

# APIGateWayServer.java

```
@EnableFeignClients("com.tushar.crypto.apigateway")
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
@EnableHystrix
public class APIGatewayServer {

	public static void main(String[] args) {
		SpringApplication.run(APIGatewayServer.class, args);
	}
}
```

@EnableFeignClients  : Use to enable Feign Client so that microservices can interact with each other
@EnableDiscoveryClient : For discovering clients
@EnableZuulProxy   :  To Enable Zuul server
@EnableHystrix  : For fallback. Help to implement circuit breaker


# SecurityConfig.java:

To configure security configuration in the application. It helps to configure:

1) Disable cors and csrf
2) Allowed all users to request for token
3) All other request through API gateway must be authenticated
4) Exception handling for 'Authorization Failure' and 'Access Denied'
5) Also to configure Feign client timeout

```
	@Bean
    public static Request.Options requestOptions(ConfigurableEnvironment env) {
        int ribbonReadTimeout = env.getProperty("ribbon.ReadTimeout", int.class, 70000);
        int ribbonConnectionTimeout = env.getProperty("ribbon.ConnectTimeout", int.class, 60000);

        return new Request.Options(ribbonConnectionTimeout, ribbonReadTimeout);
    }
 ```

# CreateUserProxy.java:

This service will interact with other microservices for user creation. In this instead calling directly microservices,
we are routing request through API gateway.  

```
@FeignClient(name="netflix-zul-service")
@RibbonClient(name="auth-service")
@EnableDiscoveryClient
public interface CreateUserProxy {

	@PostMapping("/auth-service/api/createUser")
	public String createUser(@RequestHeader("Authorization") String header, 
			@RequestParam("username") String username, 
			@RequestParam("password") String password,
			@RequestParam("role")String role);
}
```

# EndPoint.java

This is the controller for our api gateway. createUser request first comes here than using FeignClient request goes to auth-service through API gateway. 

@HystrixCommand is used to implement circuit breaker and we have introduced fallback method in case request is failed.

```
	@PostMapping("/createUser")
	@HystrixCommand(fallbackMethod="unableToCreateUser")
	public String createUser(HttpServletRequest request,  String username, String password, String role) {
		try {
			return proxy.createUser(request.getHeader("Authorization"), username, password, role);
		} catch (Exception e)  {
			throw new UsernameNotFoundException(e.getMessage());
		}
	}
	
	private String unableToCreateUser(HttpServletRequest request,  String username, String password, String role) {
		return "unable to create user";
	}
``` 

# JwtAuthorizationFilter.java

This class extends OncePerRequestFilter means we want to execute our filter only once per request. It may possible that there are several filters are in the chain so we don't want it to be executed again.

We override its method doFilterInternal for token validation, authorization the user. Once user is authenticated, it adds this into SecurityContext so that user needs not to authenticated again within the same request.

There are cases when no token is provided or no user info present in the token, in such cases we move to another filter chain.

# custom exception handlers:

This package contains custom implementaion of the errors occurs during Authentication or Authorization.

