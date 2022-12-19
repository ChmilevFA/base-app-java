# base-app-java
[![CI](https://github.com/ChmilevFA/base-app-java/actions/workflows/main.yml/badge.svg)](https://github.com/ChmilevFA/base-app-java/actions/workflows/main.yml)


Template for the simplest Http CRUD Java service

To start the app locally first run the PostgreSQL:
```bash
docker rm -f template-postgres;
docker run --name template-postgres -e POSTGRES_USER=ddl-template -e POSTGRES_PASSWORD=password -e POSTGRES_DB=template -p 5433:5432 postgres:15
```

```bash
./gradlew clean run -Dconfig.file="file:$(pwd)/development/config.yml"
```

Example query to test the app:

```curl
curl localhost:7000/healthcheck
```