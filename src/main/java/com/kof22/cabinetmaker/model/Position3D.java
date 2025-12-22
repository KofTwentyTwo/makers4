package com.kof22.cabinetmaker.model;

/**
 * Position of a part's origin point (typically bottom-left-front corner).
 * X (left-right), Y (bottom-top), Z (front-back).
 */
public record Position3D(
    Dimension x,
    Dimension y,
    Dimension z
) {
    /**
     * Origin point (0, 0, 0).
     */
    public static Position3D origin() {
        return new Position3D(
            Dimension.inches(0),
            Dimension.inches(0),
            Dimension.inches(0)
        );
    }

    /**
     * Create position in inches.
     */
    public static Position3D inches(double x, double y, double z) {
        return new Position3D(
            Dimension.inches(x),
            Dimension.inches(y),
            Dimension.inches(z)
        );
    }

    /**
     * Create position in millimeters.
     */
    public static Position3D mm(double x, double y, double z) {
        return new Position3D(
            Dimension.mm(x),
            Dimension.mm(y),
            Dimension.mm(z)
        );
    }

    /**
     * Translate this position by the given offsets.
     */
    public Position3D translate(Dimension dx, Dimension dy, Dimension dz) {
        return new Position3D(
            x.add(dx),
            y.add(dy),
            z.add(dz)
        );
    }

    /**
     * Format for display.
     */
    public String format() {
        return "(" + x.format() + ", " + y.format() + ", " + z.format() + ")";
    }
}
