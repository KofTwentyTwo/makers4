# Makers4 Domain Model Design

## Overview

Makers4 is a web application for woodworkers and cabinet makers that generates woodworking plans (diagrams, cut lists, PDFs) from parametric user inputs. V1 focuses on cabinet design, with extensibility for other object types (tables, shelves) in future versions.

## Architecture

- **Framework:** QQQ low-code framework (Java 17, PostgreSQL, Javalin)
- **Pattern:** User defines Cabinet specs → Process generates Parts → User generates outputs (cut lists, drawings)
- **Units:** Store mm internally, display based on project UnitSystem preference

## Entity Groups

### 1. Lookup Tables (Account-wide reference data)

Seeded with starter data. Users cannot modify in v1 (admin-only in future).

| Entity | Purpose | Key Fields |
|--------|---------|------------|
| UnitSystem | Measurement preference | code, name |
| MaterialType | Material classification | code, name |
| Material | Wood/sheet goods catalog | code, name, materialTypeId, species, thicknessMm, nominalThickness, costPerSqFt |
| CabinetType | Cabinet classification | code, name (BASE, WALL, TALL, VANITY, CORNER) |
| FaceType | Construction style | code, name (FACE_FRAME, FRAMELESS, INSET) |
| ToeKickStyle | Toe kick construction | code, name (INTEGRATED, SEPARATE_BASE, LEGS, NONE) |
| FinishedEndStyle | End panel treatment | code, name (APPLIED, FLUSH) |
| OpeningType | Opening classification | code, name (DOOR, DRAWER, SHELF, OPEN) |
| DoorStyle | Door construction | code, name (SLAB, 5_PIECE, SHAKER, RAISED_PANEL, GLASS_FRAME) |
| DoorPanelStyle | Door panel type | code, name (FLAT, RAISED, BEAD_BOARD, GLASS) |
| DrawerFrontStyle | Drawer front construction | code, name (SLAB, 5_PIECE, SHAKER) |
| EdgeProfile | Edge treatment | code, name (SQUARE, ROUNDOVER, OGEE, BEVEL, COVE, CHAMFER) |
| DrawerSlideType | Slide mechanism | code, name (SIDE_MOUNT, UNDERMOUNT, CENTER_MOUNT) |
| PartType | Part classification | code, name, category |

### 2. Core Entities (User-editable)

| Entity | Purpose |
|--------|---------|
| User | Account holder with simple auth |
| Project | Container for cabinets, holds defaults (units, materials) |
| Cabinet | Source of truth - all cabinet specifications |
| CabinetOpening | Child of Cabinet - door/drawer/shelf definitions |

### 3. Derived Entities (Read-only, process-generated)

| Entity | Purpose |
|--------|---------|
| Part | Individual cut piece derived from Cabinet specs |
| CutList | Named grouping of parts for cutting |
| CutListItem | Part reference within a cut list |

### 4. Output Entities

| Entity | Purpose |
|--------|---------|
| RenderJob | Async job to generate drawings/PDFs |
| RenderArtifact | Generated file with blob storage |

## Detailed Entity Specifications

### User

```
User
  ├── id                     Long, PK
  ├── email                  String(255), required, unique
  ├── name                   String(120), required
  ├── passwordHash           String(255), required
  ├── isActive               Boolean, default true
  ├── createDate             Instant
  └── modifyDate             Instant
```

### Project

```
Project
  ├── id                     Long, PK
  ├── userId                 Long, FK → User
  ├── name                   String(120), required
  ├── description            Text
  │
  │  ── Defaults ──
  ├── unitSystemId           Long, FK → UnitSystem
  ├── defaultBoxMaterialId   Long, FK → Material
  ├── defaultBackMaterialId  Long, FK → Material
  ├── defaultFaceFrameMaterialId  Long, FK → Material
  ├── defaultDoorFrameMaterialId  Long, FK → Material
  ├── defaultDoorPanelMaterialId  Long, FK → Material
  ├── defaultDrawerFrontMaterialId Long, FK → Material
  ├── defaultDrawerBoxMaterialId  Long, FK → Material
  │
  ├── createDate             Instant
  └── modifyDate             Instant
```

### Cabinet

```
Cabinet
  ├── id                     Long, PK
  ├── projectId              Long, FK → Project, required
  ├── name                   String(120), required
  ├── description            Text
  │
  │  ── Core Dimensions (mm) ──
  ├── widthMm                Integer, required, > 0
  ├── heightMm               Integer, required, > 0
  ├── depthMm                Integer, required, > 0
  │
  │  ── Construction ──
  ├── cabinetTypeId          Long, FK → CabinetType, required
  ├── faceTypeId             Long, FK → FaceType, required
  │
  │  ── Toe Kick ──
  ├── toeKickStyleId         Long, FK → ToeKickStyle, required
  ├── toeKickHeightMm        Integer (nullable if style=NONE)
  ├── toeKickDepthMm         Integer (nullable if style=NONE)
  ├── toeKickMaterialId      Long, FK → Material (nullable, defaults to box)
  │
  │  ── Box/Carcass ──
  ├── boxMaterialId          Long, FK → Material (nullable, inherits project)
  ├── backMaterialId         Long, FK → Material (nullable, defaults to box)
  ├── shelfMaterialId        Long, FK → Material (nullable, defaults to box)
  │
  │  ── Finished Ends ──
  ├── leftEndFinished        Boolean, default false
  ├── rightEndFinished       Boolean, default false
  ├── backFinished           Boolean, default false
  ├── finishedEndMaterialId  Long, FK → Material (nullable)
  ├── finishedEndStyleId     Long, FK → FinishedEndStyle (nullable)
  │
  │  ── Face Frame (if faceType = FACE_FRAME) ──
  ├── faceFrameMaterialId    Long, FK → Material (nullable)
  ├── faceFrameRailWidthMm   Integer (nullable)
  ├── faceFrameStileWidthMm  Integer (nullable)
  │
  │  ── Door Defaults ──
  ├── doorStyleId            Long, FK → DoorStyle (nullable)
  ├── doorFrameMaterialId    Long, FK → Material (nullable)
  ├── doorPanelStyleId       Long, FK → DoorPanelStyle (nullable)
  ├── doorPanelMaterialId    Long, FK → Material (nullable)
  ├── doorRailWidthMm        Integer (nullable)
  ├── doorStileWidthMm       Integer (nullable)
  ├── doorGrooveDepthMm      Integer, default 10
  ├── doorPanelGapMm         Integer, default 2
  │
  │  ── Drawer Front Defaults ──
  ├── drawerFrontStyleId     Long, FK → DrawerFrontStyle (nullable)
  ├── drawerFrontMaterialId  Long, FK → Material (nullable)
  ├── drawerFrontEdgeProfileId Long, FK → EdgeProfile (nullable)
  │
  │  ── Drawer Box ──
  ├── drawerBoxMaterialId    Long, FK → Material (nullable)
  ├── drawerBoxBottomMaterialId Long, FK → Material (nullable)
  ├── drawerSlideTypeId      Long, FK → DrawerSlideType (nullable)
  │
  ├── createDate             Instant
  └── modifyDate             Instant
```

### CabinetOpening

```
CabinetOpening
  ├── id                     Long, PK
  ├── cabinetId              Long, FK → Cabinet, required
  ├── openingTypeId          Long, FK → OpeningType, required
  ├── sequenceNumber         Integer, required (order top to bottom)
  ├── heightMm               Integer, required, > 0
  │
  │  ── Door Overrides (nullable = inherit from Cabinet) ──
  ├── doorStyleId            Long, FK → DoorStyle
  ├── doorFrameMaterialId    Long, FK → Material
  ├── doorPanelStyleId       Long, FK → DoorPanelStyle
  ├── doorPanelMaterialId    Long, FK → Material
  │
  │  ── Drawer Overrides ──
  ├── drawerFrontStyleId     Long, FK → DrawerFrontStyle
  ├── drawerFrontMaterialId  Long, FK → Material
  ├── drawerFrontEdgeProfileId Long, FK → EdgeProfile
  │
  ├── createDate             Instant
  └── modifyDate             Instant
```

### Part (Derived, read-only)

```
Part
  ├── id                     Long, PK
  ├── cabinetId              Long, FK → Cabinet, required
  ├── cabinetOpeningId       Long, FK → CabinetOpening (nullable)
  ├── partTypeId             Long, FK → PartType, required
  ├── name                   String(120), required
  │
  │  ── Dimensions (mm) ──
  ├── lengthMm               Integer, required
  ├── widthMm                Integer, required
  ├── thicknessMm            Integer, required
  │
  ├── materialId             Long, FK → Material, required
  ├── quantity               Integer, required, default 1
  ├── notes                  Text
  ├── edgeBandingNotes       String(255)
  │
  ├── createDate             Instant
  └── modifyDate             Instant
```

### CutList

```
CutList
  ├── id                     Long, PK
  ├── projectId              Long, FK → Project (nullable, for project-level)
  ├── cabinetId              Long, FK → Cabinet (nullable, for cabinet-level)
  ├── name                   String(120), required
  ├── generatedAt            Instant, required
  │
  ├── createDate             Instant
  └── modifyDate             Instant
```

### CutListItem

```
CutListItem
  ├── id                     Long, PK
  ├── cutListId              Long, FK → CutList, required
  ├── partId                 Long, FK → Part, required
  ├── sheetNumber            Integer (for future nesting optimization)
  ├── positionX              Integer (for future nesting optimization)
  ├── positionY              Integer (for future nesting optimization)
  │
  ├── createDate             Instant
  └── modifyDate             Instant
```

### RenderJob

```
RenderJob
  ├── id                     Long, PK
  ├── projectId              Long, FK → Project (nullable)
  ├── cabinetId              Long, FK → Cabinet (nullable)
  ├── renderType             String(40) (PDF, PNG, DXF, SVG)
  ├── status                 String(40) (PENDING, PROCESSING, COMPLETE, FAILED)
  ├── requestedAt            Instant, required
  ├── startedAt              Instant
  ├── completedAt            Instant
  ├── errorMessage           Text
  │
  ├── createDate             Instant
  └── modifyDate             Instant
```

### RenderArtifact

```
RenderArtifact
  ├── id                     Long, PK
  ├── renderJobId            Long, FK → RenderJob, required
  ├── name                   String(255), required
  ├── artifactType           String(40) (FRONT_VIEW, SIDE_VIEW, ISOMETRIC, CUT_LIST, etc.)
  ├── mimeType               String(100), required
  ├── fileSizeBytes          Long
  ├── fileData               Blob, required
  │
  ├── createDate             Instant
  └── modifyDate             Instant
```

## Relationships Diagram

```
User (1)
  └── Project (many)
        ├── unitSystemId → UnitSystem
        ├── default*MaterialId → Material (multiple FKs)
        │
        ├── Cabinet (many)
        │     ├── cabinetTypeId → CabinetType
        │     ├── faceTypeId → FaceType
        │     ├── toeKickStyleId → ToeKickStyle
        │     ├── *MaterialId → Material (multiple FKs)
        │     ├── doorStyleId → DoorStyle
        │     ├── doorPanelStyleId → DoorPanelStyle
        │     ├── drawerFrontStyleId → DrawerFrontStyle
        │     ├── drawerSlideTypeId → DrawerSlideType
        │     ├── edgeProfileId → EdgeProfile
        │     │
        │     ├── CabinetOpening (many) [child table]
        │     │     ├── openingTypeId → OpeningType
        │     │     └── override FKs → various lookups
        │     │
        │     ├── Part (many) [derived, read-only]
        │     │     ├── partTypeId → PartType
        │     │     └── materialId → Material
        │     │
        │     └── RenderJob (many)
        │           └── RenderArtifact (many)
        │
        ├── CutList (many)
        │     └── CutListItem (many)
        │           └── partId → Part
        │
        └── RenderJob (many) [project-level]
              └── RenderArtifact (many)
```

## Process Flow

```
┌─────────────────────────────────────────────────────────────┐
│  User creates/updates Cabinet                               │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  QQQ Process: ValidateAndGenerateParts                      │
│  1. Validate dimensions (all > 0)                           │
│  2. Validate material selections                            │
│  3. Validate openings fit within cabinet height             │
│  4. Delete existing Parts for this Cabinet                  │
│  5. Generate Parts based on:                                │
│     • Cabinet dimensions & construction type                │
│     • Face type rules (face frame vs frameless)             │
│     • Toe kick style                                        │
│     • Opening definitions                                   │
│     • Material selections (with inheritance)                │
│  6. Save Part records (read-only)                           │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  User views Parts list on Cabinet detail                    │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  User triggers: GenerateCutList                             │
│  1. Group Parts by material/thickness                       │
│  2. Create CutList + CutListItems                           │
│  3. (Future: Run nesting optimization)                      │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  User triggers: GenerateDrawings                            │
│  1. Create RenderJob (status=PENDING)                       │
│  2. Async: Generate views (front, side, isometric)          │
│  3. Async: Generate PDF/PNG files                           │
│  4. Create RenderArtifact records with file blobs           │
│  5. Update RenderJob (status=COMPLETE)                      │
└─────────────────────────────────────────────────────────────┘
```

## Material Inheritance

Materials cascade with fallback chain:

```
CabinetOpening.drawerFrontMaterialId  (if set)
  └── Cabinet.drawerFrontMaterialId   (if set)
        └── Project.defaultDrawerFrontMaterialId  (if set)
              └── [Validation Error - required]
```

## Seed Data Requirements

### Materials (exhaustive starter list)

Plywood:
- Baltic Birch 1/4", 1/2", 3/4"
- Cherry Plywood 1/4", 1/2", 3/4"
- Maple Plywood 1/4", 1/2", 3/4"
- Oak Plywood 1/4", 1/2", 3/4"
- Walnut Plywood 1/4", 1/2", 3/4"

Solid Wood:
- Cherry 3/4", 1"
- Maple 3/4", 1"
- Oak 3/4", 1"
- Walnut 3/4", 1"
- Poplar 3/4" (paint grade)

Sheet Goods:
- MDF 1/4", 1/2", 3/4"
- Melamine 3/4"
- Particle Board 3/4"

## Future Extensions

### Adding New Object Types (e.g., Table, Bookshelf)

1. Create new entity (e.g., `Table`) with type-specific fields
2. Add `tableId` FK to Part (nullable, like cabinetId)
3. Create new process `ValidateAndGenerateTableParts`
4. Lookup tables (Material, PartType, etc.) are reused

### Multi-tenant (Workspace)

1. Add `Workspace` entity between User and Project
2. Add `WorkspaceMember` junction (workspaceId, userId, role)
3. Add `workspaceId` FK to Material (workspace-scoped catalogs)
4. Update all queries to filter by workspace

### Versioning

1. Add `CabinetVersion` entity (snapshot of cabinet + openings)
2. Link Part to CabinetVersion instead of Cabinet
3. Keep history of versions with timestamps
