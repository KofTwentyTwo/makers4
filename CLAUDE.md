# CLAUDE.md - Makers4 Project Guide

## Project Overview

**Makers4** is a web application for woodworkers and cabinet makers, built on the QQQ low-code framework. Domain: makers4.com

**Stack:** QQQ Framework 0.34.0-SNAPSHOT + PostgreSQL + Liquibase + Material Dashboard
**Package:** `com.makers4`
**License:** Proprietary (Kof22)

## Build Commands

```bash
# Build and run
mvn clean package -DskipTests
java -jar target/makers4.jar

# Run tests (H2) with coverage
mvn test
# Coverage report: target/site/jacoco/index.html

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
├── processes/      # Business processes (rendering/)
├── rendering/      # Cabinet rendering system
│   ├── core/       # Dimension, Vector3D, Box3D
│   ├── scene/      # SceneNode, RenderStyle
│   ├── camera/     # OrthographicCamera, ViewDirection
│   ├── builders/   # CabinetSceneBuilder
│   └── export/     # PdfExporter, SvgExporter, BlueprintElements
├── startup/        # Liquibase, initialization
└── Server.java     # Entry point
```

## Key Files

- `pom.xml` - Maven config, QQQ dependencies, JaCoCo coverage
- `src/main/resources/db/liquibase/` - Database migrations
- `Makers4MetaDataProvider.java` - QInstance configuration (scans `com.makers4.model`, `com.makers4.processes`, `com.makers4.metadata`)

## Entity Model

### Core Entities
- **Project** - Top-level container for a cabinet-making project
- **Cabinet** - Individual cabinet with dimensions, materials, and styles
- **CabinetOpening** - Doors/drawers within a cabinet
- **Part** - Individual cut parts for a cabinet

### Output Entities
- **CutList** - Generated cut list for a project/cabinet
- **CutListItem** - Individual items in a cut list
- **RenderJob** - Job tracking for rendering operations (status: PENDING, RUNNING, COMPLETED, FAILED)
- **RenderArtifact** - Output files from rendering (PDF/SVG stored as BLOB)

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

## Rendering System

The rendering system uses a **scene graph architecture** inspired by game engines and CAD systems.

### Architecture Flow
```
Cabinet (QQQ Entity)
       ↓
CabinetSceneBuilder    → Builds scene graph from Cabinet config (parametric)
       ↓
SceneNode (tree)       → Root node with children (parts as nodes)
       ↓
SceneRenderer          → Traverses scene, projects to 2D via Camera
   + OrthographicCamera (FRONT/LEFT/TOP/ISOMETRIC)
       ↓
PdfExporter/SvgExporter → Output to file
```

### Key Classes

| Class | Purpose |
|-------|---------|
| `Vector3D` | 3D point/vector (x, y, z) |
| `Box3D` | Axis-aligned bounding box (position + size) |
| `Dimension` | Immutable dimension with mm/inches conversion |
| `SceneNode` | Node in scene graph with position, size, children, style |
| `RenderStyle` | Fill color, stroke, label styling |
| `OrthographicCamera` | Projects 3D → 2D for orthographic views |
| `ViewDirection` | Enum: FRONT, BACK, LEFT, RIGHT, TOP, BOTTOM, ISOMETRIC |
| `CabinetSceneBuilder` | Cabinet → SceneNode tree (parametric rules) |
| `SceneRenderer` | Renders SceneNode tree to Graphics2D |
| `PdfExporter` | Multi-page PDF with architectural blueprint styling |
| `SvgExporter` | SVG vector output |
| `RenderSettings` | Configuration (scale, colors, labels, title block) |

### Parametric Calculation

Cabinet part positions are calculated at render time (NOT stored in database):
- `CabinetSceneBuilder.buildScene(cabinet)` calculates positions based on:
  - Cabinet dimensions (widthMm, heightMm, depthMm)
  - Cabinet type (base, wall, tall)
  - Toe kick dimensions
  - Material thicknesses

## Processes

### RenderCabinetProcess
Table action on Cabinet that:
1. Creates RenderJob record (status=PENDING → RUNNING)
2. Builds scene graph with CabinetSceneBuilder
3. Renders to PDF (multi-page blueprint) and SVG (individual views)
4. Stores output as RenderArtifact records (BLOB)
5. Updates RenderJob to COMPLETED

### DownloadRenderArtifactProcess
Table action on RenderArtifact that downloads blob to temp file.

## Heavy Fields (BLOB Storage)

For large binary fields (like `RenderArtifact.fileData`):

1. **Mark field as heavy** in TableMetaDataCustomizer:
```java
table.getField("fileData")
   .withIsHeavy(true)
   .withFieldAdornment(new FieldAdornment(AdornmentType.FILE_DOWNLOAD)
      .withValue(AdornmentType.FileDownloadValues.FILE_NAME_FIELD, "name"));
```

2. **Fetch heavy fields explicitly** when needed:
```java
QueryInput queryInput = new QueryInput();
queryInput.setTableName(RenderArtifact.TABLE_NAME);
queryInput.setFilter(new QQueryFilter(...));
queryInput.setShouldFetchHeavyFields(true);  // Important!
```

3. **For ETL processes**, use:
```java
values.put(StreamedETLWithFrontendProcess.FIELD_FETCH_HEAVY_FIELDS, true);
```

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

## Test Coverage

JaCoCo configured with thresholds:
- **70% line coverage**
- **50% branch coverage**

Excluded packages (QQQ boilerplate):
- `com/makers4/model/**`
- `com/makers4/metadata/**`
- `com/makers4/startup/**`
- `com/makers4/Server*`

Current: 158 tests, ~75% line / ~58% branch coverage

## Dependencies

Key dependencies beyond QQQ:
- **PDFBox 3.0.1** - PDF generation
- **JFreeSVG 3.4.3** - SVG generation
- **JaCoCo 0.8.11** - Test coverage
