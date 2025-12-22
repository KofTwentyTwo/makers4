package com.kof22.cabinetmaker.rendering;

/**
 * Orthographic view types for 2D projection of 3D cabinet models.
 * Each view represents looking at the cabinet from a specific direction.
 */
public enum ViewType {
    /**
     * Front view - looking at the X-Y plane from the front (Z=0).
     * Shows width (X) horizontally and height (Y) vertically.
     */
    FRONT("Front View", "Looking at cabinet from the front"),

    /**
     * Back view - looking at the X-Y plane from the rear.
     * Shows width (X) horizontally (mirrored) and height (Y) vertically.
     */
    BACK("Back View", "Looking at cabinet from the back"),

    /**
     * Left side view - looking at the Z-Y plane from the left.
     * Shows depth (Z) horizontally and height (Y) vertically.
     */
    LEFT("Left Side", "Looking at cabinet from the left side"),

    /**
     * Right side view - looking at the Z-Y plane from the right.
     * Shows depth (Z) horizontally (mirrored) and height (Y) vertically.
     */
    RIGHT("Right Side", "Looking at cabinet from the right side"),

    /**
     * Top view (plan view) - looking at the X-Z plane from above.
     * Shows width (X) horizontally and depth (Z) vertically.
     */
    TOP("Top View", "Looking at cabinet from above"),

    /**
     * Bottom view - looking at the X-Z plane from below.
     * Shows width (X) horizontally and depth (Z) vertically (mirrored).
     */
    BOTTOM("Bottom View", "Looking at cabinet from below"),

    /**
     * Isometric view - 3D projection showing all three dimensions.
     * Standard 30-degree isometric projection commonly used in technical drawings.
     */
    ISOMETRIC("Isometric View", "3D isometric projection showing width, height, and depth");

    private final String displayName;
    private final String description;

    ViewType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Get the common views used for cabinet blueprints.
     */
    public static ViewType[] standardViews() {
        return new ViewType[] { FRONT, LEFT, TOP, ISOMETRIC };
    }

    /**
     * Get all elevation views (front, back, left, right).
     */
    public static ViewType[] elevationViews() {
        return new ViewType[] { FRONT, BACK, LEFT, RIGHT };
    }
}
