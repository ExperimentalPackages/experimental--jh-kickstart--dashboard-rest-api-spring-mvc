# Dashboard Backend

REST API for implementing dashboard.

## Technology stack

java 21, Maven, Spring Boot, JPA, Postgresql, Liquibase.

## Database configuration

Application uses a Postgres as a persistent storage. Options to get it up:

1. Use remote existing postgres. It is needed to specify the path in configs.
2. Start local postgres in docker `docker compose -f compose.yaml up -d`