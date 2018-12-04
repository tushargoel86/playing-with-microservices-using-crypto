# playing-with-microservices-using-crypto-operations
In this i am trying to perform crypto operations using microservices


# Description: 

In this project we will going to perform crypto operation using our crypto microservices. We will going to use
following things:

1) API Gateway (Zuul)         :   Gateway for all the services. All request must come here  
2) Load Balancer  (Ribbon)    :   Optimize usage of our sevices
3) Service Discovery (Eureka) :   For discovering our services
4) Spring cloud Config Server :   To store all common config 
5) Authentication using JWT   :   JWT for authentication
6) Database (mysql)           :   To store user credentials and roles using hibernate
7) Feign Client               :   To communicate with other microservices
8) Hystrix                    :   For Fault tolreance our services



Component of our project are: 

![Alt](CryptoMicroservices.svg)

 

We will going to have all this component implemented step by step. 

# Authentication Service:  [auth-service](auth-service)

# Config Server:  [config-server](config-server)

# Naming server:  [service-discovery](service-discovery)

# API Gateway: [api-gateway-service](api-gateway-service)

# Crypto Services: [crypto-services](crypto-services)

I have used following ports:

PORTS used

```bash

Services	    Ports
API Gateway	   8765
Eureka Server	 8761
Config Server	 9999
Auth Service	  8899
Crypto Serice  8889

```

Steps to use:

1) Create a first user in the database using [file](auth-service/data.sql)
2) Send a token request to the server as defined [here](api-gateway-service/README.md)
3) Once you have token either you can create a new user [here](api-gateway-service/README.md) or send a crypto request as defined [here](crypto-services)



