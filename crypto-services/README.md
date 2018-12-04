# Crypto Microservices

This is the services where crypto logic is present. For simplicity i have only implemented AES algorithm with CBC/PKCS5Padding.
I may implement with other algos in future.

# Directory Structure 

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
        │               │   CrypoServiceApplication.java
        │               │
        │               ├───config
        │               │       Config.java
        │               │
        │               ├───endpoint
        │               │       Endpoint.java
        │               │
        │               └───service
        │                       ServiceLayer.java
        │
        └───resources
                bootstrap.properties

```

# Config.java
This is the configuration file which fetch key bytes for crypto operation using cloud config server which then stores the value on git server.

# Endpoint.java

This is the controller having 2 services exposed:

1) For Encryption 2) For Decryption

In this, we can not access our crypto services directly. All request must goes through API gateway. Hence we are using 
API gateway port here.

For Encryption:

```
url:  http://localhost:8765/crypto/encrypt/symmetric

form variables:

data:   tushar
transformation:  AES/CBC/PKCS5Padding
iv:  00010203040506070001020304050607

iv is optional
```

result is: 82C6D529548058265DF857FD4E559E46


For Decryption:

```
url:  http://localhost:8765/crypto/decrypt/symmetric

form variables:

ciphertext:  82C6D529548058265DF857FD4E559E46
transformation:  AES/CBC/PKCS5Padding
iv:  00010203040506070001020304050607

if iv is passed during encryption than it should be passed in decryption as well

```

result: tushar

