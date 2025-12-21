# Quick Start

Use this template to bootstrap your own QQQ-based app. Steps cover clone → run → customize → test → deploy.

## 0) Prereqs
- Java 17, Maven 3.8+
- Docker (to run Postgres 17 locally)  
- Node (optional, only if you rebuild frontend assets yourself)

## 1) Clone
```bash
git clone https://github.com/<your-org>/<your-app>.git
cd <your-app>
```

## 2) Start Postgres 17 locally (named container)
```bash
# build tagged image
docker build -t qqq-orders-db -f src/test/resources/postgres/Dockerfile src/test/resources/postgres
# run with name
docker run --rm -d --name qqq-orders-db -p 5432:5432 qqq-orders-db

# alternative: docker compose (also uses qqq-orders-db container_name)
docker compose -f src/test/resources/postgres/docker-compose.yml up -d
```

## 3) Set env vars (sample defaults)
```bash
export RDBMS_VENDOR=postgresql
export RDBMS_HOSTNAME=localhost
export RDBMS_PORT=5432
export RDBMS_DATABASE_NAME=qqq_orders
export RDBMS_USERNAME=devuser
export RDBMS_PASSWORD=devpass
```

## 4) Apply schema + seed data
```bash
mvn liquibase:update
```

## 5) Build and run
```bash
mvn clean package -DskipTests
java -jar target/new-qqq-application-template.jar
```
Open:  
- Dashboard: http://localhost:8000/  
- API: http://localhost:8000/qqq-api/

## 6) Customize for your app
- Run: `python3 scripts/customize_template.py` (set package, groupId, artifactId, app label)
- Metadata/entities: `src/main/java/<your/package>/model/`
- Processes: `src/main/java/<your/package>/processes/`
- Branding/auth/backends: `OrderAppMetaDataProvider`
- DB changes: Liquibase under `src/main/resources/db/liquibase/`

## 7) Run tests (H2)
```bash
mvn test
```

## 8) Commit and push
```bash
git add .
git commit -m "feat: your change"
git push origin main
```

## 9) Deploy (typical flow)
- Ensure your target Postgres has the env vars set
- Run `mvn liquibase:update` against that DB
- Build and run the shaded jar (or containerize around the jar)

## 10) Stopping local DB
```bash
docker rm -f qqq-orders-db
```

