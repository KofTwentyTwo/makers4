# Makers4 - Implementation TODO

## Completed

- [x] Rebrand project (package: com.makers4, artifactId: makers4)
- [x] Document domain model design (DESIGN.md)
- [x] Phase 1: All lookup tables implemented with descriptions
- [x] Phase 2: Core entities (User, Project, Cabinet, CabinetOpening)
- [x] Phase 3: Derived entities (Part, CutList, CutListItem)
- [x] Phase 4: Output entities (RenderJob, RenderArtifact)
- [x] Phase 5: Liquibase migrations and seed data
- [x] Phase 6: Metadata & UI - field sections and child record widgets
- [x] Child record display on all parent entities
- [x] Child record display on all lookup tables

## In Progress

### Board Feet Calculations
- [ ] Calculate material costs in board feet instead of square feet

## Backlog (Post-v1)

### Processes
- [ ] ValidateAndGenerateParts process
- [ ] GenerateCutList process
- [ ] GenerateDrawings process

### Features
- [ ] Authentication (OAuth, proper login)
- [ ] Multi-tenant (Workspace model)
- [ ] Versioning (CabinetVersion snapshots)
- [ ] Cut list optimization (nesting algorithm)
- [ ] Drawing generation (actual PDF/PNG rendering)
- [ ] Additional object types (Table, Bookshelf)

## Entity Relationships

### Parent -> Child Records Configured

| Parent | Children |
|--------|----------|
| Project | Cabinets, Cut Lists, Render Jobs |
| Cabinet | Openings, Parts |
| CabinetOpening | Parts |
| CutList | Cut List Items |
| RenderJob | Render Artifacts |
| MaterialType | Materials |
| CabinetType | Cabinets |
| FaceType | Cabinets |
| ToeKickStyle | Cabinets |
| DoorStyle | Cabinets |
| DoorPanelStyle | Cabinets |
| DrawerFrontStyle | Cabinets |
| DrawerSlideType | Cabinets |
| EdgeProfile | Cabinets |
| FinishedEndStyle | Cabinets |
| OpeningType | Cabinet Openings |
| PartType | Parts |
| UnitSystem | Projects |
