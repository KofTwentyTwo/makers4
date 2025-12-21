# CLAUDE.md - Makers4 Project Guide

## Project Overview

**Makers4** is a web application for woodworkers and cabinet makers, built on the QQQ low-code framework. Domain: makers4.com

**Stack:** QQQ Framework + PostgreSQL + Liquibase + Material Dashboard
**Package:** `com.makers4`
**License:** Proprietary (Kof22)

## Build Commands

```bash
# Build and run
mvn clean package -DskipTests
java -jar target/makers4.jar

# Run tests (H2)
mvn test

# Apply database migrations
mvn liquibase:update

# Start local dev environment (Postgres + nginx proxy)
docker compose -f src/test/resources/local-dev_docker-compose.yml up -d

# Or start just Postgres
docker compose -f src/test/resources/postgres/docker-compose.yml up -d
```

## Environment Variables

```bash
export RDBMS_VENDOR=postgresql
export RDBMS_HOSTNAME=localhost
export RDBMS_PORT=5432
export RDBMS_DATABASE_NAME=makers4
export RDBMS_USERNAME=devuser
export RDBMS_PASSWORD=devpass
```

## Code Style

Follow QQQ conventions (see `/Users/james.maes/Git.Local/QRunIO/qqq/CLAUDE.md`):
- 3-space indentation, braces on next line
- Wrapper types (Integer, not int)
- Fluent style (`.withX()` over `.setX()`)
- Flower box Javadoc comments
- QLogger with LogPair objects

## Project Structure

```
src/main/java/com/makers4/
├── metadata/       # MetaDataProducers, QInstance setup
├── model/          # RecordEntity classes
├── processes/      # Business processes
├── startup/        # Liquibase, initialization
└── Server.java     # Entry point
```

## Key Files

- `pom.xml` - Maven config, QQQ dependencies
- `src/main/resources/db/liquibase/` - Database migrations
- `Makers4MetaDataProvider.java` - QInstance configuration
