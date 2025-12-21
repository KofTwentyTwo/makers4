# new-qqq-application-template

Minimal QQQ starter project with a simple order-management domain (Customer, Product, Order, OrderLine), Liquibase migrations + seed data, mock auth, and the Material Dashboard served at `/`.

## Run locally (Postgres)
```bash
export RDBMS_VENDOR=postgresql
export RDBMS_HOSTNAME=localhost
export RDBMS_PORT=5432
export RDBMS_DATABASE_NAME=qqq_orders
export RDBMS_USERNAME=devuser
export RDBMS_PASSWORD=devpass

mvn liquibase:update
mvn clean package -DskipTests
java -jar target/new-qqq-application-template.jar
```
Open `http://localhost:8000/` for the dashboard and `http://localhost:8000/qqq-api/` for APIs.

### Start a local Postgres 17 with Docker (named container)
```bash
# build once (tagged image)
docker build -t qqq-orders-db -f src/test/resources/postgres/Dockerfile src/test/resources/postgres

# run as a named container
docker run --rm -d --name qqq-orders-db -p 5432:5432 qqq-orders-db

# stop
docker rm -f qqq-orders-db

# alternative: docker compose (also uses the qqq-orders-db name)
docker compose -f src/test/resources/postgres/docker-compose.yml up -d
docker compose -f src/test/resources/postgres/docker-compose.yml down
```

## Tests (H2)
```bash
mvn test
```

## What’s included
- Working pom with QQQ BOM + Javalin middleware + Material Dashboard
- Liquibase schema + seed data under `src/main/resources/db/liquibase/`
- Sample metadata, entities, and a CreateOrder process under `src/main/java/<your/package>/`
- Mock authentication and branding defaults

### Customize the template
```bash
python3 scripts/customize_template.py
# follow prompts to set package, groupId, artifactId, and app label
mvn clean package -DskipTests
```
After customizing, update metadata/entities/processes under your new package path.
