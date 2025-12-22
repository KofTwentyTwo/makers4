package com.kof22.cabinetmaker.model;

/**
 * Classification of cabinet parts for rendering and cut-list generation.
 */
public enum PartType {
    // Cabinet box parts
    SIDE_PANEL("Side Panel"),
    TOP_PANEL("Top Panel"),
    BOTTOM_PANEL("Bottom Panel"),
    BACK_PANEL("Back Panel"),
    SHELF("Shelf"),
    DIVIDER("Divider"),

    // Face frame parts
    FACE_FRAME_STILE("Face Frame Stile"),
    FACE_FRAME_RAIL("Face Frame Rail"),

    // Door parts
    DOOR("Door"),
    DOOR_PANEL("Door Panel"),
    DOOR_STILE("Door Stile"),
    DOOR_RAIL("Door Rail"),

    // Drawer parts
    DRAWER_FRONT("Drawer Front"),
    DRAWER_SIDE("Drawer Side"),
    DRAWER_BACK("Drawer Back"),
    DRAWER_BOTTOM("Drawer Bottom"),

    // Other parts
    TOEKICK("Toe Kick"),
    NAILER("Nailer Strip"),
    CLEAT("Cleat"),
    COUNTERTOP("Countertop");

    private final String displayName;

    PartType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
