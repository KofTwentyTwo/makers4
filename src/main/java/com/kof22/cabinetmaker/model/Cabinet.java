package com.kof22.cabinetmaker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Complete cabinet assembly containing multiple parts.
 * Acts as the root aggregate for the domain model.
 */
public class Cabinet {
    private final String id;
    private String name;
    private String description;
    private Dimensions3D overallDimensions;
    private final List<Part> parts;
    private CabinetStyle style;

    /**
     * Cabinet style/type classification.
     */
    public enum CabinetStyle {
        BASE("Base Cabinet"),           // Floor-standing with toe kick
        WALL("Wall Cabinet"),           // Wall-mounted
        TALL("Tall Cabinet"),           // Full-height (pantry, etc.)
        VANITY("Vanity"),               // Bathroom vanity
        DRAWER_BASE("Drawer Base");     // Base with drawers

        private final String displayName;

        CabinetStyle(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public Cabinet(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.parts = new ArrayList<>();
    }

    // Builder-style setters
    public Cabinet withDescription(String description) {
        this.description = description;
        return this;
    }

    public Cabinet withOverallDimensions(Dimensions3D dimensions) {
        this.overallDimensions = dimensions;
        return this;
    }

    public Cabinet withStyle(CabinetStyle style) {
        this.style = style;
        return this;
    }

    public Cabinet addPart(Part part) {
        this.parts.add(part);
        return this;
    }

    public Cabinet addParts(Part... partsToAdd) {
        for (Part part : partsToAdd) {
            this.parts.add(part);
        }
        return this;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Dimensions3D getOverallDimensions() {
        return overallDimensions;
    }

    public List<Part> getParts() {
        return Collections.unmodifiableList(parts);
    }

    public CabinetStyle getStyle() {
        return style;
    }

    /**
     * Get parts filtered by type.
     */
    public List<Part> getPartsByType(PartType type) {
        return parts.stream()
            .filter(p -> p.getType() == type)
            .collect(Collectors.toList());
    }

    /**
     * Get parts filtered by material type.
     */
    public List<Part> getPartsByMaterial(Material.MaterialType materialType) {
        return parts.stream()
            .filter(p -> p.getMaterial() != null && p.getMaterial().type() == materialType)
            .collect(Collectors.toList());
    }

    /**
     * Calculate bounding box from part positions and dimensions.
     * Returns overallDimensions if set, otherwise calculates from parts.
     */
    public Dimensions3D calculateBoundingBox() {
        if (overallDimensions != null) {
            return overallDimensions;
        }

        if (parts.isEmpty()) {
            return Dimensions3D.inches(0, 0, 0);
        }

        double maxX = 0, maxY = 0, maxZ = 0;
        for (Part part : parts) {
            maxX = Math.max(maxX, part.getMaxX());
            maxY = Math.max(maxY, part.getMaxY());
            maxZ = Math.max(maxZ, part.getMaxZ());
        }

        return Dimensions3D.inches(maxX, maxY, maxZ);
    }

    /**
     * Get total part count.
     */
    public int getPartCount() {
        return parts.size();
    }

    /**
     * Generate a simple cut list summary.
     */
    public String generateCutListSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cut List for: ").append(name).append("\n");
        sb.append("=".repeat(40)).append("\n");

        for (Part part : parts) {
            sb.append(String.format("%-20s %s\n",
                part.getName() + ":",
                part.getDimensions().format2D()));
        }

        return sb.toString();
    }

    /**
     * Format cabinet info for display.
     */
    public String format() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        if (style != null) {
            sb.append(" (").append(style.getDisplayName()).append(")");
        }
        if (overallDimensions != null) {
            sb.append(": ").append(overallDimensions.format());
        }
        sb.append(" - ").append(parts.size()).append(" parts");
        return sb.toString();
    }

    @Override
    public String toString() {
        return format();
    }
}
