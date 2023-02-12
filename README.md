# base-app-java
[![CI](https://github.com/ChmilevFA/base-app-java/actions/workflows/main.yml/badge.svg)](https://github.com/ChmilevFA/base-app-java/actions/workflows/main.yml)

Template for the simplest Http CRUD Java service

## To test the app locally
### Prerequisites:
* Java 17
* Docker

### Commands:
0. Ensure configs in `/development/config.yml` are relevant for local start up
1. Run the PostgreSQL:
```bash
docker rm -f template-postgres;
docker run --name template-postgres -e POSTGRES_USER=ddl-template -e POSTGRES_PASSWORD=password -e POSTGRES_DB=template -p 5433:5432 postgres:15
```
2. Build and run the app itself
```bash
./gradlew clean shadowJar
java -Dconfig.file="file:$(pwd)/development/config.yml" -jar template-app/build/libs/template-app.jar 
```

## Once app started
Example query to test the app:
```curl
curl localhost:7000/healthcheck
```

Swagger docs - http://localhost:7000/swagger-ui/