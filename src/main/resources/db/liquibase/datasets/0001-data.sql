-- liquibase formatted sql

-- ============================================================================
-- UNIT SYSTEMS
-- ============================================================================

-- changeset makers4:data-001 context:test,dev
INSERT INTO unit_system (code, name, description, sort_order, is_active, createdate, modifydate) VALUES
   ('IMPERIAL', 'Imperial (inches)', 'Traditional US measurement system using inches, feet, and fractions. Most common in American woodworking shops. Dimensions are typically expressed in inches with fractional subdivisions (1/4", 1/2", etc.).', 1, TRUE, NOW(), NOW()),
   ('METRIC', 'Metric (millimeters)', 'International standard measurement system using millimeters as the base unit. Common in European cabinet making and increasingly adopted worldwide for precision work. Allows for easy decimal calculations.', 2, TRUE, NOW(), NOW());

-- ============================================================================
-- MATERIAL TYPES
-- ============================================================================

-- changeset makers4:data-002 context:test,dev
INSERT INTO material_type (code, name, description, sort_order, is_active, createdate, modifydate) VALUES
   ('PLYWOOD', 'Plywood', 'Engineered wood made from thin layers (plies) of wood veneer glued together with adjacent layers having grain rotated 90 degrees. Offers excellent strength-to-weight ratio and dimensional stability. Available with decorative hardwood face veneers or utility grades.', 1, TRUE, NOW(), NOW()),
   ('SOLID_WOOD', 'Solid Wood', 'Natural lumber cut directly from trees and dried for woodworking. Offers authentic wood grain, can be shaped with hand tools, and develops patina over time. Requires attention to wood movement across the grain.', 2, TRUE, NOW(), NOW()),
   ('MDF', 'MDF', 'Medium-density fiberboard made from wood fibers combined with resin and pressed into dense, uniform panels. Provides smooth surfaces ideal for painting, precise machining, and consistent density throughout. Does not have grain direction.', 3, TRUE, NOW(), NOW()),
   ('MELAMINE', 'Melamine', 'Particle board or MDF core with factory-applied melamine resin surface coating. Provides durable, easy-to-clean finish in various colors and patterns. Commonly used for cabinet interiors and budget-friendly carcasses.', 4, TRUE, NOW(), NOW()),
   ('PARTICLE_BOARD', 'Particle Board', 'Engineered wood made from wood chips, shavings, and sawdust bonded with resin. Most economical sheet good option. Suitable for substrates and non-structural applications. Heavier than plywood of equivalent thickness.', 5, TRUE, NOW(), NOW());

-- ============================================================================
-- CABINET TYPES
-- ============================================================================

-- changeset makers4:data-003 context:test,dev
INSERT INTO cabinet_type (code, name, description, sort_order, is_active, createdate, modifydate) VALUES
   ('BASE', 'Base Cabinet', 'Floor-standing cabinets that support countertops and provide primary storage. Standard height is 34.5" (876mm) to achieve 36" (914mm) counter height with top. Depth typically 24" (610mm) to accommodate countertop overhang.', 1, TRUE, NOW(), NOW()),
   ('WALL', 'Wall Cabinet', 'Upper cabinets mounted to wall studs above countertops or appliances. Standard depths of 12" (305mm) or 15" (381mm). Heights vary from 12" to 42" depending on ceiling height and design requirements.', 2, TRUE, NOW(), NOW()),
   ('TALL', 'Tall Cabinet', 'Full-height cabinets extending from floor to near ceiling, typically 84" to 96" (2134-2438mm) tall. Used for pantries, utility storage, or housing built-in appliances like ovens and refrigerators.', 3, TRUE, NOW(), NOW()),
   ('VANITY', 'Vanity Cabinet', 'Bathroom-specific base cabinets designed for sink installation. Typically 30" to 36" (762-914mm) tall and 18" to 21" (457-533mm) deep to accommodate bathroom fixtures and provide comfortable use.', 4, TRUE, NOW(), NOW()),
   ('CORNER_BASE', 'Corner Base Cabinet', 'Specialized base cabinets designed for kitchen corners. Options include blind corner, lazy susan, or diagonal configurations. Maximizes storage in otherwise difficult-to-access corner spaces.', 5, TRUE, NOW(), NOW()),
   ('CORNER_WALL', 'Corner Wall Cabinet', 'Upper corner cabinets bridging perpendicular wall cabinet runs. Can be diagonal-front, blind corner, or specialty rotating shelf designs. Requires careful planning for door clearance and accessibility.', 6, TRUE, NOW(), NOW());

-- ============================================================================
-- FACE TYPES
-- ============================================================================

-- changeset makers4:data-004 context:test,dev
INSERT INTO face_type (code, name, description, sort_order, is_active, createdate, modifydate) VALUES
   ('FACE_FRAME', 'Face Frame', 'Traditional American cabinet construction with solid wood frame attached to front of cabinet box. Frame provides door mounting surface, adds rigidity, and conceals plywood edges. Doors overlay the frame partially or fully.', 1, TRUE, NOW(), NOW()),
   ('FRAMELESS', 'Frameless (Euro)', 'European-style construction where doors attach directly to cabinet sides via concealed hinges. Maximizes interior storage space and provides clean, modern aesthetic. Requires precise construction tolerances.', 2, TRUE, NOW(), NOW()),
   ('INSET', 'Inset', 'Doors and drawers fit flush within face frame opening rather than overlaying. Requires precise fitting with consistent reveals. Traditional look associated with high-end custom cabinetry. More labor-intensive but highly refined appearance.', 3, TRUE, NOW(), NOW());

-- ============================================================================
-- TOE KICK STYLES
-- ============================================================================

-- changeset makers4:data-005 context:test,dev
INSERT INTO toe_kick_style (code, name, description, sort_order, is_active, createdate, modifydate) VALUES
   ('INTEGRATED', 'Integrated (Part of Cabinet)', 'Toe kick built as permanent part of cabinet sides extending to floor. Traditional approach providing solid, stable base. Sides must be notched to create kick recess. Common in face frame construction.', 1, TRUE, NOW(), NOW()),
   ('SEPARATE_BASE', 'Separate Base Unit', 'Toe kick constructed as independent platform or base that cabinets sit upon. Allows cabinets to be built as complete boxes. Simplifies leveling on uneven floors. Popular in frameless construction.', 2, TRUE, NOW(), NOW()),
   ('LEGS', 'Legs/Feet', 'Metal or wood legs supporting cabinet above floor. Creates open space below cabinet. Modern aesthetic option that simplifies floor cleaning. Adjustable legs ease leveling. Common in contemporary European designs.', 3, TRUE, NOW(), NOW()),
   ('NONE', 'None (Floor to Bottom)', 'Cabinet extends fully to floor with no recessed kick space. Used for furniture-style pieces, islands, or specialized applications where kick is not needed or desired.', 4, TRUE, NOW(), NOW());

-- ============================================================================
-- FINISHED END STYLES
-- ============================================================================

-- changeset makers4:data-006 context:test,dev
INSERT INTO finished_end_style (code, name, description, sort_order, is_active, createdate, modifydate) VALUES
   ('APPLIED', 'Applied Panel', 'Separate finished panel glued or fastened to exposed cabinet side. Panel can be flat, have applied molding, or match door style. Allows upgrade of visible ends without changing cabinet construction.', 1, TRUE, NOW(), NOW()),
   ('FLUSH', 'Flush Panel', 'Cabinet side itself serves as finished surface using same material as door panels or premium plywood. More economical than applied panels. Requires finishing cabinet side during assembly.', 2, TRUE, NOW(), NOW());

-- ============================================================================
-- OPENING TYPES
-- ============================================================================

-- changeset makers4:data-007 context:test,dev
INSERT INTO opening_type (code, name, description, sort_order, is_active, createdate, modifydate) VALUES
   ('DOOR', 'Door', 'Single hinged door covering cabinet opening. Most common configuration for upper cabinets and smaller base cabinets. Door can swing left or right based on hinge placement.', 1, TRUE, NOW(), NOW()),
   ('DRAWER', 'Drawer', 'Sliding drawer box with applied front. Provides full-extension access to contents. Used for utensils, linens, and items benefiting from top-down visibility. Standard configurations include 4-drawer or 3-drawer stacks.', 2, TRUE, NOW(), NOW()),
   ('DOOR_DRAWER', 'Door with Drawer Above', 'Combination opening with drawer in upper portion and door below. Classic base cabinet configuration providing drawer storage for small items with larger door-accessible space beneath.', 3, TRUE, NOW(), NOW()),
   ('DOUBLE_DOOR', 'Double Door', 'Pair of doors meeting at center of wide cabinet opening. Used for cabinets typically 30" (762mm) or wider. Doors may have center stile or operate without one for full opening access.', 4, TRUE, NOW(), NOW()),
   ('SHELF', 'Open Shelf', 'Open horizontal surface without door or drawer. Used for display, frequently accessed items, or decorative purposes. May include plate grooves, stemware holders, or other specialty features.', 5, TRUE, NOW(), NOW()),
   ('OPEN', 'Open (No Door)', 'Cabinet opening intentionally left without door. Creates open storage cubbies or display niches. Interior finish quality important as contents remain visible.', 6, TRUE, NOW(), NOW()),
   ('FALSE_FRONT', 'False Front', 'Decorative panel that mimics drawer front but does not open. Used at sink locations where drawer would interfere with plumbing, or at cooktop for consistent visual appearance.', 7, TRUE, NOW(), NOW());

-- ============================================================================
-- DOOR STYLES
-- ============================================================================

-- changeset makers4:data-008 context:test,dev
INSERT INTO door_style (code, name, description, sort_order, is_active, createdate, modifydate) VALUES
   ('SLAB', 'Slab', 'Flat, single-piece door with no frame or panel construction. Clean, contemporary appearance. Can be solid wood, veneer over substrate, or painted MDF. Edges may be square, eased, or profiled.', 1, TRUE, NOW(), NOW()),
   ('5_PIECE', '5-Piece', 'Traditional frame-and-panel construction with two stiles, two rails, and center panel. Allows wood movement while maintaining flat door. Foundation for most traditional door profiles.', 2, TRUE, NOW(), NOW()),
   ('SHAKER', 'Shaker', 'Classic 5-piece door with flat center panel and simple square-edge profiles on frame. Clean lines originated with Shaker furniture movement. Versatile style working in traditional through contemporary settings.', 3, TRUE, NOW(), NOW()),
   ('RAISED_PANEL', 'Raised Panel', '5-piece door with center panel shaped to be thicker at center than edges, creating shadow lines. Traditional, formal appearance. Panel profile and edge details vary from simple bevels to complex shapes.', 4, TRUE, NOW(), NOW()),
   ('GLASS_FRAME', 'Glass Frame', '5-piece frame designed to hold glass panel instead of wood. Used for display cabinets, china hutches, and decorative upper cabinets. Glass can be clear, seeded, frosted, or leaded.', 5, TRUE, NOW(), NOW()),
   ('BEADBOARD', 'Beadboard', 'Door featuring vertical beaded tongue-and-groove boards as center panel. Cottage or farmhouse aesthetic. Individual boards or plywood with routed bead pattern. Classic kitchen and bathroom style.', 6, TRUE, NOW(), NOW());

-- ============================================================================
-- DOOR PANEL STYLES
-- ============================================================================

-- changeset makers4:data-009 context:test,dev
INSERT INTO door_panel_style (code, name, description, sort_order, is_active, createdate, modifydate) VALUES
   ('FLAT', 'Flat Panel', 'Center panel with flat surface and minimal or no edge profile. Creates clean lines and subtle shadow detail at panel-to-frame transition. Complements modern and transitional designs.', 1, TRUE, NOW(), NOW()),
   ('RAISED', 'Raised Panel', 'Panel machined to be thicker at center, tapering to thinner edges that fit frame grooves. Creates dimensional surface with shadow lines. Traditional, formal appearance suitable for classic kitchens.', 2, TRUE, NOW(), NOW()),
   ('BEAD_BOARD', 'Bead Board', 'Panel made from vertical tongue-and-groove boards with decorative bead at each joint, or plywood with routed beads. Country, cottage, or farmhouse styling. Adds texture and visual interest.', 3, TRUE, NOW(), NOW()),
   ('GLASS', 'Glass', 'Transparent or decorative glass panel held in frame with glass stops or applied molding. Options include clear, frosted, seeded, textured, or leaded glass. Showcases cabinet contents.', 4, TRUE, NOW(), NOW()),
   ('NONE', 'None (Slab Door)', 'No separate panel - door is single slab construction. Used with slab door style where no panel-in-frame construction exists. Clean, minimal contemporary appearance.', 5, TRUE, NOW(), NOW());

-- ============================================================================
-- DRAWER FRONT STYLES
-- ============================================================================

-- changeset makers4:data-010 context:test,dev
INSERT INTO drawer_front_style (code, name, description, sort_order, is_active, createdate, modifydate) VALUES
   ('SLAB', 'Slab', 'Flat, single-piece drawer front matching slab door aesthetic. Clean lines for contemporary designs. Can include subtle edge profiles. Simple construction from single board or panel.', 1, TRUE, NOW(), NOW()),
   ('5_PIECE', '5-Piece', 'Traditional frame-and-panel drawer front matching 5-piece door construction. Provides visual continuity with doors. Less common due to construction complexity for smaller pieces.', 2, TRUE, NOW(), NOW()),
   ('SHAKER', 'Shaker', 'Flat-panel frame construction with simple square profiles matching Shaker doors. Most popular drawer front style due to clean appearance and manufacturing efficiency.', 3, TRUE, NOW(), NOW()),
   ('ROUTED', 'Routed Profile', 'Slab front with decorative profile routed into face, creating visual interest without frame-and-panel complexity. Economical way to add detail. Profile can match door styling.', 4, TRUE, NOW(), NOW());

-- ============================================================================
-- EDGE PROFILES
-- ============================================================================

-- changeset makers4:data-011 context:test,dev
INSERT INTO edge_profile (code, name, description, sort_order, is_active, createdate, modifydate) VALUES
   ('SQUARE', 'Square', 'Sharp 90-degree edge with minimal easing. Clean, modern appearance. Edges may be lightly broken to prevent splinters but maintain crisp look. Most contemporary profile option.', 1, TRUE, NOW(), NOW()),
   ('ROUNDOVER', 'Roundover', 'Edge rounded to partial or full radius curve. Comfortable to touch, reduces sharp corners. Various radii from subtle 1/16" ease to bold 3/8" roundover. Classic, versatile profile.', 2, TRUE, NOW(), NOW()),
   ('OGEE', 'Ogee', 'S-curve profile combining concave and convex curves. Traditional, elegant appearance. Common in raised panel doors and formal millwork. Creates pronounced shadow lines.', 3, TRUE, NOW(), NOW()),
   ('BEVEL', 'Bevel', 'Angled cut creating flat chamfer at edge, typically 30 to 45 degrees. Adds visual interest while maintaining relatively sharp appearance. Can be wide or narrow bevel angle.', 4, TRUE, NOW(), NOW()),
   ('COVE', 'Cove', 'Concave quarter-round profile scooping inward. Opposite of roundover profile. Creates shadow and adds visual weight to edges. Traditional appearance, often combined with other profiles.', 5, TRUE, NOW(), NOW()),
   ('CHAMFER', 'Chamfer', 'Small angled flat cut removing sharp corner, typically 1/8" at 45 degrees. Subtle detail that prevents damage to sharp corners and adds refined touch without changing overall appearance.', 6, TRUE, NOW(), NOW()),
   ('BULLNOSE', 'Bullnose', 'Half-round profile creating fully rounded edge. Soft, comfortable feel. Common on countertops and table edges. Provides protection against chips on exposed edges.', 7, TRUE, NOW(), NOW());

-- ============================================================================
-- DRAWER SLIDE TYPES
-- ============================================================================

-- changeset makers4:data-012 context:test,dev
INSERT INTO drawer_slide_type (code, name, description, sort_order, is_active, createdate, modifydate) VALUES
   ('SIDE_MOUNT', 'Side Mount', 'Traditional slides mounted to drawer sides and cabinet interior. Simple installation, economical option. Various quality levels from basic to soft-close full-extension. Visible from side when drawer open.', 1, TRUE, NOW(), NOW()),
   ('UNDERMOUNT', 'Undermount', 'Premium slides hidden beneath drawer bottom. Clean appearance with no visible hardware when drawer is open. Requires specific drawer bottom construction. Typically full-extension with soft-close.', 2, TRUE, NOW(), NOW()),
   ('CENTER_MOUNT', 'Center Mount', 'Single slide mounted at center beneath drawer. Traditional style for vintage or reproduction furniture. Provides stable, wobble-free operation. Limits drawer depth capacity.', 3, TRUE, NOW(), NOW());

-- ============================================================================
-- PART TYPES
-- ============================================================================

-- changeset makers4:data-013 context:test,dev
INSERT INTO part_type (code, name, description, category, sort_order, is_active, createdate, modifydate) VALUES
   -- Carcass Parts
   ('LEFT_SIDE', 'Left Side', 'Vertical panel forming left exterior wall of cabinet carcass. Bears weight of shelves and may have shelf pin holes. May be finished if exposed as cabinet end.', 'Carcass', 1, TRUE, NOW(), NOW()),
   ('RIGHT_SIDE', 'Right Side', 'Vertical panel forming right exterior wall of cabinet carcass. Mirror of left side with same construction requirements. May require finished exterior if exposed.', 'Carcass', 2, TRUE, NOW(), NOW()),
   ('TOP', 'Top', 'Horizontal panel connecting side panels at top of carcass. Provides structural rigidity and may serve as nailer surface for wall-mounted cabinets. Not always present in base cabinets.', 'Carcass', 3, TRUE, NOW(), NOW()),
   ('BOTTOM', 'Bottom', 'Horizontal panel forming floor of cabinet interior. Bears weight of contents and provides attachment for drawer slides in frameless construction. Key structural component.', 'Carcass', 4, TRUE, NOW(), NOW()),
   ('BACK', 'Back Panel', 'Panel closing rear of cabinet, typically 1/4" (6mm) plywood or hardboard. Provides racking resistance (keeps cabinet square) and prevents items from falling behind cabinet.', 'Carcass', 5, TRUE, NOW(), NOW()),
   ('SHELF', 'Shelf', 'Horizontal interior surface for storing items. May be fixed or adjustable via shelf pins. Should match interior material and be sized to allow easy insertion/removal.', 'Carcass', 6, TRUE, NOW(), NOW()),
   ('NAILER', 'Nailer', 'Horizontal strip at top rear of cabinet for mounting to wall studs. Provides solid attachment point and prevents back panel damage during installation.', 'Carcass', 7, TRUE, NOW(), NOW()),
   ('STRETCHER', 'Stretcher', 'Horizontal member spanning cabinet width to maintain shape. Common at front of base cabinets below countertop. Adds rigidity without full panel.', 'Carcass', 8, TRUE, NOW(), NOW()),
   -- Toe Kick Parts
   ('TOE_KICK_FRONT', 'Toe Kick Front', 'Horizontal board spanning cabinet width at floor level, set back 3-4 inches. Provides foot clearance for comfortable standing at counter. Matches cabinet finish.', 'Toe Kick', 10, TRUE, NOW(), NOW()),
   ('TOE_KICK_SIDE', 'Toe Kick Side', 'Vertical boards at toe kick ends where cabinet meets wall or another cabinet. Encloses toe kick space and finishes exposed ends.', 'Toe Kick', 11, TRUE, NOW(), NOW()),
   -- Face Frame Parts
   ('TOP_RAIL', 'Top Rail', 'Horizontal face frame member at top of cabinet opening. Typically 1.5" (38mm) wide. Provides door mounting surface and conceals top of cabinet box.', 'Face Frame', 20, TRUE, NOW(), NOW()),
   ('BOTTOM_RAIL', 'Bottom Rail', 'Horizontal face frame member at bottom of cabinet opening. Often same width as top rail. Provides door stop surface and conceals bottom of cabinet box.', 'Face Frame', 21, TRUE, NOW(), NOW()),
   ('MID_RAIL', 'Mid Rail', 'Horizontal face frame member dividing openings vertically. Separates doors, drawers, or combination openings. Width may vary based on design and hinge requirements.', 'Face Frame', 22, TRUE, NOW(), NOW()),
   ('LEFT_STILE', 'Left Stile', 'Vertical face frame member at left cabinet edge. Typically 1.5" to 2" (38-51mm) wide. Full height of cabinet, attached to both rails. May be wider if edge is exposed.', 'Face Frame', 23, TRUE, NOW(), NOW()),
   ('RIGHT_STILE', 'Right Stile', 'Vertical face frame member at right cabinet edge. Matches left stile in width and construction. Provides door hinge mounting surface.', 'Face Frame', 24, TRUE, NOW(), NOW()),
   ('MID_STILE', 'Mid Stile', 'Vertical face frame member dividing openings horizontally. Located between double doors or adjacent openings. Width typically matches outer stiles.', 'Face Frame', 25, TRUE, NOW(), NOW()),
   -- Door Parts
   ('DOOR_SLAB', 'Door (Slab)', 'Complete single-piece door, flat construction without frame and panel. Made from single board, glued-up panel, or plywood with applied veneer or finish.', 'Door', 30, TRUE, NOW(), NOW()),
   ('DOOR_TOP_RAIL', 'Door Top Rail', 'Upper horizontal frame member of 5-piece door. Typically 2" to 2.5" (51-64mm) wide. Receives panel in groove or rabbet. May be arched for specialty designs.', 'Door', 31, TRUE, NOW(), NOW()),
   ('DOOR_BOTTOM_RAIL', 'Door Bottom Rail', 'Lower horizontal frame member of 5-piece door. Often wider than top rail (2.5" to 3") to prevent visual bottom-heavy appearance and provide durability.', 'Door', 32, TRUE, NOW(), NOW()),
   ('DOOR_LEFT_STILE', 'Door Left Stile', 'Left vertical frame member of 5-piece door. Contains groove for panel and receives rail tenons. Width typically matches rails or slightly wider.', 'Door', 33, TRUE, NOW(), NOW()),
   ('DOOR_RIGHT_STILE', 'Door Right Stile', 'Right vertical frame member of 5-piece door. Mirror of left stile in construction. Receives hinges and contains panel groove.', 'Door', 34, TRUE, NOW(), NOW()),
   ('DOOR_PANEL', 'Door Panel', 'Center panel floating within door frame groove. Can be flat, raised, glass, or beadboard construction. Sized to allow seasonal wood movement.', 'Door', 35, TRUE, NOW(), NOW()),
   -- Drawer Parts
   ('DRAWER_FRONT', 'Drawer Front', 'Visible face of drawer matching door style. Applied to drawer box front or integrated with box construction. May be slab, shaker, or detailed profile.', 'Drawer', 40, TRUE, NOW(), NOW()),
   ('DRAWER_BOX_FRONT', 'Drawer Box Front', 'Structural front of drawer box behind applied front. Typically 1/2" (12mm) material. Receives drawer slide brackets and connects to sides.', 'Drawer', 41, TRUE, NOW(), NOW()),
   ('DRAWER_BOX_BACK', 'Drawer Box Back', 'Rear panel of drawer box, same material as box front. May be lowered to allow filing or may be full height. Critical for box rigidity.', 'Drawer', 42, TRUE, NOW(), NOW()),
   ('DRAWER_BOX_LEFT', 'Drawer Box Left Side', 'Left side panel of drawer box. Contains dadoes for bottom and may have grooves for drawer slides. Height determined by opening dimensions less clearances.', 'Drawer', 43, TRUE, NOW(), NOW()),
   ('DRAWER_BOX_RIGHT', 'Drawer Box Right Side', 'Right side panel of drawer box. Mirror of left side with same joinery requirements. Must be sized precisely for slide mounting.', 'Drawer', 44, TRUE, NOW(), NOW()),
   ('DRAWER_BOX_BOTTOM', 'Drawer Box Bottom', 'Bottom panel of drawer box, typically 1/4" (6mm) plywood. Captured in dadoes in box sides and front. Sized for square reference during assembly.', 'Drawer', 45, TRUE, NOW(), NOW()),
   -- Finished End Parts
   ('FINISHED_END_LEFT', 'Finished End (Left)', 'Decorative panel applied to or integrated with exposed left cabinet side. Matches door style or features complementary design. May include molding details.', 'Finished End', 50, TRUE, NOW(), NOW()),
   ('FINISHED_END_RIGHT', 'Finished End (Right)', 'Decorative panel applied to exposed right cabinet side. Same construction as left finished end. Important for peninsula or island installations.', 'Finished End', 51, TRUE, NOW(), NOW()),
   ('FINISHED_END_BACK', 'Finished End (Back)', 'Finished panel for exposed cabinet backs, rare but used for island or peninsula installations where cabinet back is visible from seating area.', 'Finished End', 52, TRUE, NOW(), NOW());

-- ============================================================================
-- MATERIALS
-- ============================================================================

-- changeset makers4:data-014 context:test,dev
INSERT INTO material (code, name, material_type_id, species, thickness_mm, nominal_thickness, cost_per_sqft, sort_order, is_active, createdate, modifydate) VALUES
   -- Baltic Birch Plywood
   ('BB-6MM', 'Baltic Birch 1/4"', 1, 'Birch', 6, '1/4"', 2.50, 1, TRUE, NOW(), NOW()),
   ('BB-12MM', 'Baltic Birch 1/2"', 1, 'Birch', 12, '1/2"', 4.00, 2, TRUE, NOW(), NOW()),
   ('BB-18MM', 'Baltic Birch 3/4"', 1, 'Birch', 18, '3/4"', 5.50, 3, TRUE, NOW(), NOW()),
   -- Cherry Plywood
   ('CHR-PLY-6MM', 'Cherry Plywood 1/4"', 1, 'Cherry', 6, '1/4"', 3.00, 10, TRUE, NOW(), NOW()),
   ('CHR-PLY-12MM', 'Cherry Plywood 1/2"', 1, 'Cherry', 12, '1/2"', 5.00, 11, TRUE, NOW(), NOW()),
   ('CHR-PLY-18MM', 'Cherry Plywood 3/4"', 1, 'Cherry', 18, '3/4"', 7.00, 12, TRUE, NOW(), NOW()),
   -- Maple Plywood
   ('MPL-PLY-6MM', 'Maple Plywood 1/4"', 1, 'Maple', 6, '1/4"', 2.75, 20, TRUE, NOW(), NOW()),
   ('MPL-PLY-12MM', 'Maple Plywood 1/2"', 1, 'Maple', 12, '1/2"', 4.50, 21, TRUE, NOW(), NOW()),
   ('MPL-PLY-18MM', 'Maple Plywood 3/4"', 1, 'Maple', 18, '3/4"', 6.00, 22, TRUE, NOW(), NOW()),
   -- Oak Plywood
   ('OAK-PLY-6MM', 'Oak Plywood 1/4"', 1, 'Oak', 6, '1/4"', 2.50, 30, TRUE, NOW(), NOW()),
   ('OAK-PLY-12MM', 'Oak Plywood 1/2"', 1, 'Oak', 12, '1/2"', 4.25, 31, TRUE, NOW(), NOW()),
   ('OAK-PLY-18MM', 'Oak Plywood 3/4"', 1, 'Oak', 18, '3/4"', 5.75, 32, TRUE, NOW(), NOW()),
   -- Walnut Plywood
   ('WAL-PLY-6MM', 'Walnut Plywood 1/4"', 1, 'Walnut', 6, '1/4"', 4.00, 40, TRUE, NOW(), NOW()),
   ('WAL-PLY-12MM', 'Walnut Plywood 1/2"', 1, 'Walnut', 12, '1/2"', 6.50, 41, TRUE, NOW(), NOW()),
   ('WAL-PLY-18MM', 'Walnut Plywood 3/4"', 1, 'Walnut', 18, '3/4"', 9.00, 42, TRUE, NOW(), NOW()),
   -- Solid Cherry
   ('CHR-SOLID-18MM', 'Cherry Solid 3/4"', 2, 'Cherry', 18, '3/4"', 12.00, 100, TRUE, NOW(), NOW()),
   ('CHR-SOLID-25MM', 'Cherry Solid 1"', 2, 'Cherry', 25, '1"', 15.00, 101, TRUE, NOW(), NOW()),
   -- Solid Maple
   ('MPL-SOLID-18MM', 'Maple Solid 3/4"', 2, 'Maple', 18, '3/4"', 10.00, 110, TRUE, NOW(), NOW()),
   ('MPL-SOLID-25MM', 'Maple Solid 1"', 2, 'Maple', 25, '1"', 13.00, 111, TRUE, NOW(), NOW()),
   -- Solid Oak
   ('OAK-SOLID-18MM', 'Oak Solid 3/4"', 2, 'Oak', 18, '3/4"', 9.00, 120, TRUE, NOW(), NOW()),
   ('OAK-SOLID-25MM', 'Oak Solid 1"', 2, 'Oak', 25, '1"', 12.00, 121, TRUE, NOW(), NOW()),
   -- Solid Walnut
   ('WAL-SOLID-18MM', 'Walnut Solid 3/4"', 2, 'Walnut', 18, '3/4"', 16.00, 130, TRUE, NOW(), NOW()),
   ('WAL-SOLID-25MM', 'Walnut Solid 1"', 2, 'Walnut', 25, '1"', 20.00, 131, TRUE, NOW(), NOW()),
   -- Solid Poplar (paint grade)
   ('POP-SOLID-18MM', 'Poplar Solid 3/4"', 2, 'Poplar', 18, '3/4"', 5.00, 140, TRUE, NOW(), NOW()),
   -- MDF
   ('MDF-6MM', 'MDF 1/4"', 3, NULL, 6, '1/4"', 1.00, 200, TRUE, NOW(), NOW()),
   ('MDF-12MM', 'MDF 1/2"', 3, NULL, 12, '1/2"', 1.50, 201, TRUE, NOW(), NOW()),
   ('MDF-18MM', 'MDF 3/4"', 3, NULL, 18, '3/4"', 2.00, 202, TRUE, NOW(), NOW()),
   -- Melamine
   ('MEL-WHITE-18MM', 'White Melamine 3/4"', 4, NULL, 18, '3/4"', 2.25, 300, TRUE, NOW(), NOW()),
   ('MEL-ALMOND-18MM', 'Almond Melamine 3/4"', 4, NULL, 18, '3/4"', 2.25, 301, TRUE, NOW(), NOW()),
   -- Particle Board
   ('PB-18MM', 'Particle Board 3/4"', 5, NULL, 18, '3/4"', 1.25, 400, TRUE, NOW(), NOW());

-- ============================================================================
-- DEMO USER (for dev/test)
-- ============================================================================

-- changeset makers4:data-015 context:test,dev
INSERT INTO "user" (email, name, password_hash, is_active, createdate, modifydate) VALUES
   ('demo@makers4.com', 'Demo User', '$2a$10$dummyhashfordevonly', TRUE, NOW(), NOW());

-- ============================================================================
-- EXAMPLE PROJECT: Modern Kitchen
-- ============================================================================

-- changeset makers4:data-016 context:test,dev
INSERT INTO project (user_id, name, description, unit_system_id, default_box_material_id, default_back_material_id, default_face_frame_material_id, default_door_frame_material_id, default_door_panel_material_id, default_drawer_front_material_id, default_drawer_box_material_id, createdate, modifydate) VALUES
   (1, 'Modern Kitchen Remodel', 'Complete kitchen cabinet set for a modern home renovation. Features shaker-style maple cabinets with soft-close hardware throughout. Layout includes L-shaped base cabinets with island, full wall of uppers, and tall pantry unit.', 1, 9, 7, 18, 18, 9, 18, 2, NOW(), NOW());

-- changeset makers4:data-017 context:test,dev
INSERT INTO cabinet (project_id, name, description, width_mm, height_mm, depth_mm, cabinet_type_id, face_type_id, toe_kick_style_id, toe_kick_height_mm, toe_kick_depth_mm, toe_kick_material_id, box_material_id, back_material_id, shelf_material_id, left_end_finished, right_end_finished, back_finished, finished_end_material_id, finished_end_style_id, face_frame_material_id, face_frame_rail_width_mm, face_frame_stile_width_mm, door_style_id, door_frame_material_id, door_panel_style_id, door_panel_material_id, door_rail_width_mm, door_stile_width_mm, door_groove_depth_mm, door_panel_gap_mm, drawer_front_style_id, drawer_front_material_id, drawer_front_edge_profile_id, drawer_box_material_id, drawer_box_bottom_material_id, drawer_slide_type_id, createdate, modifydate) VALUES
   (1, 'B24-1', 'Base cabinet 24" wide with single door and drawer. Left side of sink cabinet run.', 610, 876, 610, 1, 1, 1, 100, 76, 9, 9, 7, 9, TRUE, FALSE, FALSE, 9, 2, 18, 38, 38, 3, 18, 1, 9, 57, 57, 10, 2, 3, 18, 2, 2, 1, 2, NOW(), NOW()),
   (1, 'BSink36', 'Sink base cabinet 36" wide. False drawer front above doors, no shelf due to plumbing.', 914, 876, 610, 1, 1, 1, 100, 76, 9, 9, 7, 9, FALSE, FALSE, FALSE, NULL, NULL, 18, 38, 38, 3, 18, 1, 9, 57, 57, 10, 2, 3, 18, 2, 2, 1, 2, NOW(), NOW()),
   (1, 'B24-2', 'Base cabinet 24" wide with single door and drawer. Right side of sink cabinet run.', 610, 876, 610, 1, 1, 1, 100, 76, 9, 9, 7, 9, FALSE, TRUE, FALSE, 9, 2, 18, 38, 38, 3, 18, 1, 9, 57, 57, 10, 2, 3, 18, 2, 2, 1, 2, NOW(), NOW()),
   (1, 'W3630', 'Wall cabinet 36" wide by 30" tall. Double door upper above sink.', 914, 762, 305, 2, 1, 4, NULL, NULL, NULL, 9, 7, 9, FALSE, FALSE, FALSE, NULL, NULL, 18, 38, 38, 3, 18, 1, 9, 57, 57, 10, 2, NULL, NULL, NULL, NULL, NULL, NULL, NOW(), NOW()),
   (1, 'W2430', 'Wall cabinet 24" wide by 30" tall. Single door upper left of stove.', 610, 762, 305, 2, 1, 4, NULL, NULL, NULL, 9, 7, 9, TRUE, FALSE, FALSE, 9, 2, 18, 38, 38, 3, 18, 1, 9, 57, 57, 10, 2, NULL, NULL, NULL, NULL, NULL, NULL, NOW(), NOW()),
   (1, 'TP2490', 'Tall pantry cabinet 24" wide by 90" tall. Full height storage with adjustable shelves.', 610, 2286, 610, 3, 1, 1, 100, 76, 9, 9, 7, 9, TRUE, TRUE, FALSE, 9, 2, 18, 38, 38, 3, 18, 1, 9, 57, 57, 10, 2, NULL, NULL, NULL, NULL, NULL, NULL, NOW(), NOW());

-- changeset makers4:data-018 context:test,dev
INSERT INTO cabinet_opening (cabinet_id, opening_type_id, sequence_number, height_mm, door_style_id, door_frame_material_id, door_panel_style_id, door_panel_material_id, drawer_front_style_id, drawer_front_material_id, drawer_front_edge_profile_id, createdate, modifydate) VALUES
   -- B24-1: drawer above, door below
   (1, 2, 1, 152, NULL, NULL, NULL, NULL, 3, 18, 2, NOW(), NOW()),
   (1, 1, 2, 648, 3, 18, 1, 9, NULL, NULL, NULL, NOW(), NOW()),
   -- BSink36: false front, double door
   (2, 7, 1, 152, NULL, NULL, NULL, NULL, 3, 18, 2, NOW(), NOW()),
   (2, 4, 2, 648, 3, 18, 1, 9, NULL, NULL, NULL, NOW(), NOW()),
   -- B24-2: drawer above, door below
   (3, 2, 1, 152, NULL, NULL, NULL, NULL, 3, 18, 2, NOW(), NOW()),
   (3, 1, 2, 648, 3, 18, 1, 9, NULL, NULL, NULL, NOW(), NOW()),
   -- W3630: double door
   (4, 4, 1, 686, 3, 18, 1, 9, NULL, NULL, NULL, NOW(), NOW()),
   -- W2430: single door
   (5, 1, 1, 686, 3, 18, 1, 9, NULL, NULL, NULL, NOW(), NOW()),
   -- TP2490: four doors stacked (2 upper, 2 lower)
   (6, 4, 1, 1016, 3, 18, 1, 9, NULL, NULL, NULL, NOW(), NOW()),
   (6, 4, 2, 1016, 3, 18, 1, 9, NULL, NULL, NULL, NOW(), NOW());
