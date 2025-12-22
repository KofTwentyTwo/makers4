package com.kof22.cabinetmaker.model;

import java.util.UUID;

/**
 * Individual component of a cabinet with dimensions, position, and material.
 * Uses a builder-style pattern for construction.
 */
public class Part {
    private final String id;
    private String name;
    private PartType type;
    private Dimensions3D dimensions;
    private Position3D position;
    private Material material;
    private GrainDirection grainDirection;

    /**
     * Grain direction for wood materials (affects cut optimization).
     */
    public enum GrainDirection {
        HORIZONTAL,  // Grain runs along width (X-axis)
        VERTICAL     // Grain runs along height (Y-axis)
    }

    public Part(String name, PartType type) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.type = type;
        this.position = Position3D.origin();
        this.grainDirection = GrainDirection.HORIZONTAL;
    }

    // Builder-style setters
    public Part withDimensions(Dimensions3D dimensions) {
        this.dimensions = dimensions;
        return this;
    }

    public Part withPosition(Position3D position) {
        this.position = position;
        return this;
    }

    public Part withMaterial(Material material) {
        this.material = material;
        return this;
    }

    public Part withGrainDirection(GrainDirection grainDirection) {
        this.grainDirection = grainDirection;
        return this;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PartType getType() {
        return type;
    }

    public Dimensions3D getDimensions() {
        return dimensions;
    }

    public Position3D getPosition() {
        return position;
    }

    public Material getMaterial() {
        return material;
    }

    public GrainDirection getGrainDirection() {
        return grainDirection;
    }

    /**
     * Get the actual thickness from material, or from dimensions.depth if no material.
     */
    public Dimension getThickness() {
        if (material != null) {
            return material.thickness();
        }
        return dimensions != null ? dimensions.depth() : Dimension.inches(0.75);
    }

    /**
     * Calculate the maximum X coordinate (position.x + width).
     */
    public double getMaxX() {
        return position.x().toInchesDouble() + dimensions.width().toInchesDouble();
    }

    /**
     * Calculate the maximum Y coordinate (position.y + height).
     */
    public double getMaxY() {
        return position.y().toInchesDouble() + dimensions.height().toInchesDouble();
    }

    /**
     * Calculate the maximum Z coordinate (position.z + depth).
     */
    public double getMaxZ() {
        return position.z().toInchesDouble() + dimensions.depth().toInchesDouble();
    }

    /**
     * Format part info for display.
     */
    public String format() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" (").append(type.getDisplayName()).append(")");
        if (dimensions != null) {
            sb.append(": ").append(dimensions.format());
        }
        if (material != null) {
            sb.append(" [").append(material.name()).append("]");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return format();
    }
}
