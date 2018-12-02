This is configuration server which stores all the common configuration.

All micro services which requires to read common config will use this.

I have used my git repo(spring-cloud-config-server-db) to store configuration as per services.

Directory structure:

```bash

config-server

│   pom.xml
│
└───src
    └───main
        ├───java
        │   └───com
        │       └───tushar
        │           └───crypto
        │               └───configserver
        │                       ConfigServerApplication.java
        │
        └───resources
                application.properties

```
