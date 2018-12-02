This service is front end of our application. All user requests and microservices to microservices interaction will be
done through API gateway only.

This API gateway is responsible for:

1) Routing the request to asked service. It will identify the service URL through naming server i.e Eureka
2) Request for token. API gateway than send reqeust to AUTH service for token request
3) Create user. It will than forward request to AUTH. Only user with ADMIN role can create the user
4) It will authorize the use for user creation
5) It validates user token
6) Custom exception handling for 'Access Denied' and 'Invalid Credentials'

REST APIs:

PORTS used

```bash

Services	    Ports
API Gateway	   8765
Eureka Server	 8761
Config Server	 9999
Auth Service	 8899

```


# for token creation:

http://localhost:8765/auth/token

request:  JSON request

{
	"username": xxxx,
	"password" :  xxx
}

# for user creation:

form variables:
```
username:  
password:
role:  (USER/ADMIN)
```

http://localhost:8765/createUser

Initially a user needs to be added in database which admin access.


