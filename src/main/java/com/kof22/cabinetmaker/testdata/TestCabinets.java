package com.kof22.cabinetmaker.testdata;

import com.kof22.cabinetmaker.model.*;

/**
 * Factory for creating sample cabinet configurations for testing and demonstration.
 * Provides realistic cabinet dimensions following standard industry practices.
 */
public class TestCabinets {

    /**
     * Creates a simple base cabinet (typical kitchen cabinet).
     * Standard dimensions: 24"W x 34.5"H x 24"D
     * Includes: two sides, bottom, back, one shelf, and toe kick.
     */
    public static Cabinet simpleBaseCabinet() {
        Cabinet cabinet = new Cabinet("Simple Base Cabinet")
            .withDescription("24-inch wide base cabinet with one adjustable shelf")
            .withStyle(Cabinet.CabinetStyle.BASE)
            .withOverallDimensions(Dimensions3D.inches(24, 34.5, 24));

        Material plywood3_4 = Material.plywood3_4();
        Material plywood1_4 = Material.plywood1_4();

        // Cabinet box height (excluding toe kick): 34.5 - 4.5 = 30 inches
        double boxHeight = 30.0;
        double toeKickHeight = 4.5;
        double toeKickDepth = 3.0;

        // Side panels: full height of box, full depth minus back panel
        // Sides are 3/4" thick, so interior width = 24 - 1.5 = 22.5"
        cabinet.addPart(new Part("Left Side", PartType.SIDE_PANEL)
            .withDimensions(Dimensions3D.inches(0.75, boxHeight, 23.25))
            .withPosition(Position3D.inches(0, toeKickHeight, 0))
            .withMaterial(plywood3_4));

        cabinet.addPart(new Part("Right Side", PartType.SIDE_PANEL)
            .withDimensions(Dimensions3D.inches(0.75, boxHeight, 23.25))
            .withPosition(Position3D.inches(23.25, toeKickHeight, 0))
            .withMaterial(plywood3_4));

        // Bottom panel: spans between sides, sits on toe kick
        cabinet.addPart(new Part("Bottom", PartType.BOTTOM_PANEL)
            .withDimensions(Dimensions3D.inches(22.5, 0.75, 23.25))
            .withPosition(Position3D.inches(0.75, toeKickHeight, 0))
            .withMaterial(plywood3_4));

        // Back panel: 1/4" plywood, inset into rabbets
        cabinet.addPart(new Part("Back", PartType.BACK_PANEL)
            .withDimensions(Dimensions3D.inches(22.5, boxHeight - 0.75, 0.25))
            .withPosition(Position3D.inches(0.75, toeKickHeight + 0.75, 23.75))
            .withMaterial(plywood1_4));

        // Adjustable shelf: slightly narrower than interior for shelf pins
        cabinet.addPart(new Part("Shelf", PartType.SHELF)
            .withDimensions(Dimensions3D.inches(22.25, 0.75, 22.0))
            .withPosition(Position3D.inches(0.875, 20, 0.5))
            .withMaterial(plywood3_4));

        // Toe kick board: spans between sides at front
        cabinet.addPart(new Part("Toe Kick", PartType.TOEKICK)
            .withDimensions(Dimensions3D.inches(22.5, toeKickHeight, 0.75))
            .withPosition(Position3D.inches(0.75, 0, toeKickDepth))
            .withMaterial(plywood3_4));

        // Nailer strip at top back for mounting
        cabinet.addPart(new Part("Top Nailer", PartType.NAILER)
            .withDimensions(Dimensions3D.inches(22.5, 3.0, 0.75))
            .withPosition(Position3D.inches(0.75, 31.5, 22.5))
            .withMaterial(plywood3_4));

        return cabinet;
    }

    /**
     * Creates a simple wall cabinet.
     * Standard dimensions: 30"W x 30"H x 12"D
     * Includes: two sides, top, bottom, back, and two adjustable shelves.
     */
    public static Cabinet simpleWallCabinet() {
        Cabinet cabinet = new Cabinet("Simple Wall Cabinet")
            .withDescription("30-inch wide wall cabinet with two adjustable shelves")
            .withStyle(Cabinet.CabinetStyle.WALL)
            .withOverallDimensions(Dimensions3D.inches(30, 30, 12));

        Material plywood3_4 = Material.plywood3_4();
        Material plywood1_4 = Material.plywood1_4();

        // Interior dimensions: 30 - 1.5 = 28.5" wide, 30 - 1.5 = 28.5" tall
        double interiorWidth = 28.5;
        double interiorDepth = 11.25;  // 12 - 0.75 (back is inset)

        // Side panels
        cabinet.addPart(new Part("Left Side", PartType.SIDE_PANEL)
            .withDimensions(Dimensions3D.inches(0.75, 30, 11.25))
            .withPosition(Position3D.inches(0, 0, 0))
            .withMaterial(plywood3_4));

        cabinet.addPart(new Part("Right Side", PartType.SIDE_PANEL)
            .withDimensions(Dimensions3D.inches(0.75, 30, 11.25))
            .withPosition(Position3D.inches(29.25, 0, 0))
            .withMaterial(plywood3_4));

        // Top panel
        cabinet.addPart(new Part("Top", PartType.TOP_PANEL)
            .withDimensions(Dimensions3D.inches(interiorWidth, 0.75, interiorDepth))
            .withPosition(Position3D.inches(0.75, 29.25, 0))
            .withMaterial(plywood3_4));

        // Bottom panel
        cabinet.addPart(new Part("Bottom", PartType.BOTTOM_PANEL)
            .withDimensions(Dimensions3D.inches(interiorWidth, 0.75, interiorDepth))
            .withPosition(Position3D.inches(0.75, 0, 0))
            .withMaterial(plywood3_4));

        // Back panel
        cabinet.addPart(new Part("Back", PartType.BACK_PANEL)
            .withDimensions(Dimensions3D.inches(interiorWidth, 28.5, 0.25))
            .withPosition(Position3D.inches(0.75, 0.75, 11.75))
            .withMaterial(plywood1_4));

        // Two adjustable shelves
        cabinet.addPart(new Part("Upper Shelf", PartType.SHELF)
            .withDimensions(Dimensions3D.inches(28.25, 0.75, 10.5))
            .withPosition(Position3D.inches(0.875, 19.5, 0.5))
            .withMaterial(plywood3_4));

        cabinet.addPart(new Part("Lower Shelf", PartType.SHELF)
            .withDimensions(Dimensions3D.inches(28.25, 0.75, 10.5))
            .withPosition(Position3D.inches(0.875, 10, 0.5))
            .withMaterial(plywood3_4));

        return cabinet;
    }

    /**
     * Creates a tall pantry cabinet.
     * Standard dimensions: 24"W x 84"H x 24"D
     * Includes: two sides, top, bottom, back, and four adjustable shelves.
     */
    public static Cabinet tallPantryCabinet() {
        Cabinet cabinet = new Cabinet("Tall Pantry Cabinet")
            .withDescription("24-inch wide full-height pantry with four adjustable shelves")
            .withStyle(Cabinet.CabinetStyle.TALL)
            .withOverallDimensions(Dimensions3D.inches(24, 84, 24));

        Material plywood3_4 = Material.plywood3_4();
        Material plywood1_4 = Material.plywood1_4();

        double toeKickHeight = 4.5;
        double boxHeight = 84 - toeKickHeight;
        double interiorWidth = 22.5;
        double interiorDepth = 23.25;

        // Side panels (full height above toe kick)
        cabinet.addPart(new Part("Left Side", PartType.SIDE_PANEL)
            .withDimensions(Dimensions3D.inches(0.75, boxHeight, interiorDepth))
            .withPosition(Position3D.inches(0, toeKickHeight, 0))
            .withMaterial(plywood3_4));

        cabinet.addPart(new Part("Right Side", PartType.SIDE_PANEL)
            .withDimensions(Dimensions3D.inches(0.75, boxHeight, interiorDepth))
            .withPosition(Position3D.inches(23.25, toeKickHeight, 0))
            .withMaterial(plywood3_4));

        // Top panel
        cabinet.addPart(new Part("Top", PartType.TOP_PANEL)
            .withDimensions(Dimensions3D.inches(interiorWidth, 0.75, interiorDepth))
            .withPosition(Position3D.inches(0.75, 83.25, 0))
            .withMaterial(plywood3_4));

        // Bottom panel
        cabinet.addPart(new Part("Bottom", PartType.BOTTOM_PANEL)
            .withDimensions(Dimensions3D.inches(interiorWidth, 0.75, interiorDepth))
            .withPosition(Position3D.inches(0.75, toeKickHeight, 0))
            .withMaterial(plywood3_4));

        // Back panel
        cabinet.addPart(new Part("Back", PartType.BACK_PANEL)
            .withDimensions(Dimensions3D.inches(interiorWidth, boxHeight - 0.75, 0.25))
            .withPosition(Position3D.inches(0.75, toeKickHeight + 0.75, 23.75))
            .withMaterial(plywood1_4));

        // Four adjustable shelves
        double[] shelfHeights = {20, 35, 50, 65};
        for (int i = 0; i < shelfHeights.length; i++) {
            cabinet.addPart(new Part("Shelf " + (i + 1), PartType.SHELF)
                .withDimensions(Dimensions3D.inches(22.25, 0.75, 22.0))
                .withPosition(Position3D.inches(0.875, shelfHeights[i], 0.5))
                .withMaterial(plywood3_4));
        }

        // Toe kick
        cabinet.addPart(new Part("Toe Kick", PartType.TOEKICK)
            .withDimensions(Dimensions3D.inches(interiorWidth, toeKickHeight, 0.75))
            .withPosition(Position3D.inches(0.75, 0, 3))
            .withMaterial(plywood3_4));

        return cabinet;
    }

    /**
     * Creates a drawer base cabinet with three drawers.
     * Standard dimensions: 18"W x 34.5"H x 24"D
     */
    public static Cabinet drawerBaseCabinet() {
        Cabinet cabinet = new Cabinet("Three Drawer Base")
            .withDescription("18-inch wide base cabinet with three drawers")
            .withStyle(Cabinet.CabinetStyle.DRAWER_BASE)
            .withOverallDimensions(Dimensions3D.inches(18, 34.5, 24));

        Material plywood3_4 = Material.plywood3_4();
        Material plywood1_4 = Material.plywood1_4();
        Material plywood1_2 = Material.plywood1_2();

        double toeKickHeight = 4.5;
        double boxHeight = 30.0;
        double interiorWidth = 16.5;

        // Side panels
        cabinet.addPart(new Part("Left Side", PartType.SIDE_PANEL)
            .withDimensions(Dimensions3D.inches(0.75, boxHeight, 23.25))
            .withPosition(Position3D.inches(0, toeKickHeight, 0))
            .withMaterial(plywood3_4));

        cabinet.addPart(new Part("Right Side", PartType.SIDE_PANEL)
            .withDimensions(Dimensions3D.inches(0.75, boxHeight, 23.25))
            .withPosition(Position3D.inches(17.25, toeKickHeight, 0))
            .withMaterial(plywood3_4));

        // Bottom panel
        cabinet.addPart(new Part("Bottom", PartType.BOTTOM_PANEL)
            .withDimensions(Dimensions3D.inches(interiorWidth, 0.75, 23.25))
            .withPosition(Position3D.inches(0.75, toeKickHeight, 0))
            .withMaterial(plywood3_4));

        // Back panel
        cabinet.addPart(new Part("Back", PartType.BACK_PANEL)
            .withDimensions(Dimensions3D.inches(interiorWidth, boxHeight - 0.75, 0.25))
            .withPosition(Position3D.inches(0.75, toeKickHeight + 0.75, 23.75))
            .withMaterial(plywood1_4));

        // Toe kick
        cabinet.addPart(new Part("Toe Kick", PartType.TOEKICK)
            .withDimensions(Dimensions3D.inches(interiorWidth, toeKickHeight, 0.75))
            .withPosition(Position3D.inches(0.75, 0, 3))
            .withMaterial(plywood3_4));

        // Three drawer boxes (simplified - just fronts for visualization)
        double drawerHeight = 8.0;  // Approximate drawer front height
        double[] drawerYPositions = {5.5, 15.5, 25.5};

        for (int i = 0; i < 3; i++) {
            cabinet.addPart(new Part("Drawer Front " + (i + 1), PartType.DRAWER_FRONT)
                .withDimensions(Dimensions3D.inches(16.5, drawerHeight, 0.75))
                .withPosition(Position3D.inches(0.75, drawerYPositions[i], -0.75))
                .withMaterial(plywood3_4));
        }

        return cabinet;
    }
}
