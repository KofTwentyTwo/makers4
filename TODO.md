# Makers4 - Implementation TODO

## Completed
- [x] Rebrand project (package: com.makers4, artifactId: makers4)
- [x] Document domain model design (DESIGN.md)

## Phase 1: Lookup Tables

### 1.1 Simple Lookups (code, name, sortOrder pattern)
- [ ] UnitSystem
- [ ] MaterialType
- [ ] CabinetType
- [ ] FaceType
- [ ] ToeKickStyle
- [ ] FinishedEndStyle
- [ ] OpeningType
- [ ] DoorStyle
- [ ] DoorPanelStyle
- [ ] DrawerFrontStyle
- [ ] EdgeProfile
- [ ] DrawerSlideType
- [ ] PartType

### 1.2 Complex Lookups
- [ ] Material (with materialTypeId FK, species, thickness, cost)

## Phase 2: Core Entities

- [ ] User (simple auth)
- [ ] Project (with default material FKs)
- [ ] Cabinet (full spec with all material/style FKs)
- [ ] CabinetOpening (child of Cabinet with overrides)

## Phase 3: Derived Entities

- [ ] Part (read-only, linked to Cabinet)
- [ ] CutList
- [ ] CutListItem

## Phase 4: Output Entities

- [ ] RenderJob
- [ ] RenderArtifact (with blob storage)

## Phase 5: Liquibase Migrations

- [ ] Create migration for all lookup tables
- [ ] Create migration for core entities
- [ ] Create migration for derived entities
- [ ] Create migration for output entities
- [ ] Seed data: UnitSystem (IMPERIAL, METRIC)
- [ ] Seed data: MaterialType
- [ ] Seed data: CabinetType
- [ ] Seed data: FaceType
- [ ] Seed data: ToeKickStyle
- [ ] Seed data: FinishedEndStyle
- [ ] Seed data: OpeningType
- [ ] Seed data: DoorStyle
- [ ] Seed data: DoorPanelStyle
- [ ] Seed data: DrawerFrontStyle
- [ ] Seed data: EdgeProfile
- [ ] Seed data: DrawerSlideType
- [ ] Seed data: PartType
- [ ] Seed data: Material (exhaustive list)

## Phase 6: Metadata & UI

- [ ] Update Makers4AppMetaDataProducer with new entities
- [ ] Configure field sections (Tier.T1, T2, T3)
- [ ] Configure child table widgets (Cabinet → Openings, Cabinet → Parts)
- [ ] Configure exposed joins

## Phase 7: Processes

- [ ] ValidateAndGenerateParts process
- [ ] GenerateCutList process
- [ ] GenerateDrawings process (stub for v1)

## Phase 8: Cleanup

- [ ] Remove sample Order/Customer/Product entities
- [ ] Remove sample CreateOrderProcess
- [ ] Update tests
- [ ] Verify build and tests pass

## Backlog (Post-v1)

- [ ] Authentication (OAuth, proper login)
- [ ] Multi-tenant (Workspace model)
- [ ] Versioning (CabinetVersion snapshots)
- [ ] Cut list optimization (nesting algorithm)
- [ ] Drawing generation (actual PDF/PNG rendering)
- [ ] Additional object types (Table, Bookshelf)
