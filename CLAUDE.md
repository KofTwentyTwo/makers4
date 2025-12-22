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

## Entity Model

### Core Entities
- **Project** - Top-level container for a cabinet-making project
- **Cabinet** - Individual cabinet with dimensions, materials, and styles
- **CabinetOpening** - Doors/drawers within a cabinet
- **Part** - Individual cut parts for a cabinet

### Output Entities
- **CutList** - Generated cut list for a project/cabinet
- **CutListItem** - Individual items in a cut list
- **RenderJob** - Job tracking for rendering operations
- **RenderArtifact** - Output files from rendering

### Lookup Tables (in `model/lookup/`)
- **MaterialType** - Categories of materials (plywood, hardwood, etc.)
- **Material** - Specific materials with thickness and cost
- **CabinetType** - Base, wall, tall cabinet types
- **FaceType** - Face frame vs frameless construction
- **ToeKickStyle** - Toe kick configurations
- **DoorStyle**, **DoorPanelStyle** - Door construction options
- **DrawerFrontStyle**, **DrawerSlideType** - Drawer options
- **EdgeProfile** - Edge treatment profiles
- **FinishedEndStyle** - End panel finishing options
- **OpeningType** - Types of cabinet openings
- **PartType** - Categories of cabinet parts
- **UnitSystem** - Imperial vs metric measurements

## Child Record Configuration

To display child records on parent entity pages, three things are needed:

1. **@ChildTable annotation** on the parent entity class:
```java
@QMetaDataProducingEntity(
   childTables = {
      @ChildTable(
         joinFieldName = "parentId",
         childTableEntityClass = Child.class,
         childJoin = @ChildJoin(enabled = true),
         childRecordListWidget = @ChildRecordListWidget(enabled = true, label = "Children", maxRows = 50)
      )
   }
)
```

2. **Section with widget** in TableMetaDataCustomizer:
```java
String joinName = QJoinMetaData.makeInferredJoinName(Parent.TABLE_NAME, Child.TABLE_NAME);
table.addSection(new QFieldSection("children", new QIcon().withName(Child.ICON_NAME), Tier.T2)
   .withLabel("Children").withWidgetName(joinName));
```

3. **Exposed join** in TableMetaDataCustomizer:
```java
table.withExposedJoin(new ExposedJoin()
   .withLabel("Children")
   .withJoinPath(List.of(joinName))
   .withJoinTable(Child.TABLE_NAME));
```
