# Demo DB level Multi-Tenancy application

This project showcases how to achieve multi-tenancy at DB level(i.e, each tenant having their separate DB in a Saas based application) 
using Java & Spring boot.

Tech Stack:
- Java 17
- Spring Boot 3
- Maven
- Postgres DB

## MultiTenant Architecture
- Class that implements MultiTenantConnectionProvider
- CurrentTenant Resolver
- To save and return current Tenant
- Fetch tenant id from header within filter and save to Thread
- While constructing EntityTransactionManager bean, multiTenantConnectionProvider, CurrentTenantResolver needs to be configured.

## Build and deploy
### Prerequisites
- JDK 17
- Maven latest version
- Postgres DB installed and running on your machine
- A database named ```default``` created
- Run the following sql to create a table named tenant_datasources in the default db
```sh
CREATE TABLE tenant_datasources (
tenant_datasource_id SERIAL PRIMARY KEY,
tenant_identifier VARCHAR(100) UNIQUE,
datasource_url VARCHAR(300),
username VARCHAR(100),
password VARCHAR(100),
active BOOLEAN
);
```
- update the application.properties file with your postgres username and password
```
spring.datasource.username=<TODO: your-potgres username>
spring.datasource.password=<TODO: your-potgres password>
```

### Build Source
- If your default IDE is intellij: Import the project -> Maven build
- On command line:
To build source code use the below command
```sh
    mvn clean package -DskipTest
```

### Deploy
- If your default IDE is intellij: Run the MultiTenancyDemoApplication class
- On command line:
To deploy the application use the below command
```sh
    mvn spring-boot:run
```
URL to access the app:
http://localhost:8080/multi-tenancy-demo/swagger-ui/index.html#

### How to use the demo application
- Once the swagger is successfully accessible:
- Use the /tenant POST api to create a tenant. This creates a tenant 
and a separate DB for the tenant with the tenantId 
- Use the tenant/initiate api to initiate the tenantDB 
with the required resource table to perform CRUD operation
- Use the GET, POST, PUT, DELETE end-points under resource with tenant created to access data