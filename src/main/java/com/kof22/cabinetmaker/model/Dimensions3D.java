package com.kof22.cabinetmaker.model;

/**
 * Three-dimensional size specification for parts.
 * Width (X-axis), Height (Y-axis), Depth (Z-axis).
 */
public record Dimensions3D(
    Dimension width,   // X-axis (left-right)
    Dimension height,  // Y-axis (bottom-top)
    Dimension depth    // Z-axis (front-back)
) {
    /**
     * Create dimensions in inches.
     */
    public static Dimensions3D inches(double width, double height, double depth) {
        return new Dimensions3D(
            Dimension.inches(width),
            Dimension.inches(height),
            Dimension.inches(depth)
        );
    }

    /**
     * Create dimensions in millimeters.
     */
    public static Dimensions3D mm(double width, double height, double depth) {
        return new Dimensions3D(
            Dimension.mm(width),
            Dimension.mm(height),
            Dimension.mm(depth)
        );
    }

    /**
     * Format for display.
     */
    public String format() {
        return width.format() + " x " + height.format() + " x " + depth.format();
    }

    /**
     * Format as W x H (2D, ignoring depth).
     */
    public String format2D() {
        return width.format() + " x " + height.format();
    }
}
