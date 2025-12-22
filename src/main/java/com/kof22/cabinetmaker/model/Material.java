package com.kof22.cabinetmaker.model;

/**
 * Material specification for a part, including type and nominal thickness.
 */
public record Material(
    String name,
    MaterialType type,
    Dimension thickness
) {
    public enum MaterialType {
        PLYWOOD("Plywood"),
        MDF("MDF"),
        PARTICLE_BOARD("Particle Board"),
        SOLID_WOOD("Solid Wood"),
        MELAMINE("Melamine"),
        HARDBOARD("Hardboard");

        private final String displayName;

        MaterialType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Common material presets
    public static Material plywood3_4() {
        return new Material("3/4\" Plywood", MaterialType.PLYWOOD, Dimension.inches(0.75));
    }

    public static Material plywood1_2() {
        return new Material("1/2\" Plywood", MaterialType.PLYWOOD, Dimension.inches(0.5));
    }

    public static Material plywood1_4() {
        return new Material("1/4\" Plywood", MaterialType.PLYWOOD, Dimension.inches(0.25));
    }

    public static Material mdf3_4() {
        return new Material("3/4\" MDF", MaterialType.MDF, Dimension.inches(0.75));
    }

    public static Material mdf1_2() {
        return new Material("1/2\" MDF", MaterialType.MDF, Dimension.inches(0.5));
    }

    public static Material hardboard1_8() {
        return new Material("1/8\" Hardboard", MaterialType.HARDBOARD, Dimension.inches(0.125));
    }

    public static Material melamine3_4() {
        return new Material("3/4\" Melamine", MaterialType.MELAMINE, Dimension.inches(0.75));
    }

    public static Material solidWood3_4() {
        return new Material("3/4\" Solid Wood", MaterialType.SOLID_WOOD, Dimension.inches(0.75));
    }

    /**
     * Format for display.
     */
    public String format() {
        return name + " (" + thickness.formatFractional() + ")";
    }
}
