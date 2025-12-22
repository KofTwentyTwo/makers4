package com.kof22.cabinetmaker.rendering;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;

/**
 * Configuration settings for rendering cabinet views.
 * Uses builder pattern for easy configuration.
 */
public class RenderSettings {

    // Scale: points per inch (72 points = 1 inch at 100%)
    private double scale = 10.0;  // 10 points per inch (compact default)

    // Margins around the drawing (in inches)
    private double marginInches = 1.0;

    // Line styles
    private float outlineStrokeWidth = 1.5f;
    private float dimensionLineStrokeWidth = 0.5f;
    private float gridLineStrokeWidth = 0.25f;

    // Colors
    private Color outlineColor = Color.BLACK;
    private Color fillColor = new Color(245, 245, 245);  // Light gray fill
    private Color dimensionColor = new Color(0, 0, 180);  // Blue for dimensions
    private Color backgroundColor = Color.WHITE;
    private Color gridColor = new Color(220, 220, 220);

    // Dimension annotations
    private boolean showDimensions = true;
    private boolean showPartLabels = true;
    private Font dimensionFont = new Font("SansSerif", Font.PLAIN, 10);
    private Font labelFont = new Font("SansSerif", Font.PLAIN, 8);
    private double dimensionOffset = 0.5;  // inches from part edge
    private double arrowSize = 0.15;       // inches

    // Title block
    private boolean showTitleBlock = true;
    private String projectName = "";
    private String drawnBy = "";

    // Grid
    private boolean showGrid = false;
    private double gridSpacingInches = 1.0;

    // Blueprint-specific settings (fixed page size)
    private boolean useFixedPageSize = false;
    private float pageWidthPoints = 792f;   // 11" in landscape
    private float pageHeightPoints = 612f;  // 8.5" in landscape

    // Architectural border settings
    private boolean showArchitecturalBorder = false;
    private boolean showGridReferences = false;
    private int horizontalGridDivisions = 8;  // A, B, C, D, E, F, G, H
    private int verticalGridDivisions = 6;    // 1, 2, 3, 4, 5, 6

    // Traditional title block settings
    private boolean useTraditionalTitleBlock = false;
    private String companyName = "";
    private String drawingTitle = "";
    private String scaleNotation = "";
    private String date = "";
    private String checkedBy = "";
    private String revisionNumber = "";
    private String revisionDescription = "";
    private String sheetNumber = "1";
    private String totalSheets = "1";

    // Default constructor with sensible defaults
    public RenderSettings() {
    }

    // Builder-style setters

    public RenderSettings withScale(double scale) {
        this.scale = scale;
        return this;
    }

    public RenderSettings withMargin(double marginInches) {
        this.marginInches = marginInches;
        return this;
    }

    public RenderSettings withOutlineStrokeWidth(float width) {
        this.outlineStrokeWidth = width;
        return this;
    }

    public RenderSettings withDimensionLineStrokeWidth(float width) {
        this.dimensionLineStrokeWidth = width;
        return this;
    }

    public RenderSettings withOutlineColor(Color color) {
        this.outlineColor = color;
        return this;
    }

    public RenderSettings withFillColor(Color color) {
        this.fillColor = color;
        return this;
    }

    public RenderSettings withDimensionColor(Color color) {
        this.dimensionColor = color;
        return this;
    }

    public RenderSettings withBackgroundColor(Color color) {
        this.backgroundColor = color;
        return this;
    }

    public RenderSettings withShowDimensions(boolean show) {
        this.showDimensions = show;
        return this;
    }

    public RenderSettings withShowPartLabels(boolean show) {
        this.showPartLabels = show;
        return this;
    }

    public RenderSettings withDimensionFont(Font font) {
        this.dimensionFont = font;
        return this;
    }

    public RenderSettings withLabelFont(Font font) {
        this.labelFont = font;
        return this;
    }

    public RenderSettings withDimensionOffset(double offset) {
        this.dimensionOffset = offset;
        return this;
    }

    public RenderSettings withShowTitleBlock(boolean show) {
        this.showTitleBlock = show;
        return this;
    }

    public RenderSettings withProjectName(String name) {
        this.projectName = name;
        return this;
    }

    public RenderSettings withDrawnBy(String drawnBy) {
        this.drawnBy = drawnBy;
        return this;
    }

    public RenderSettings withShowGrid(boolean show) {
        this.showGrid = show;
        return this;
    }

    public RenderSettings withGridSpacing(double spacingInches) {
        this.gridSpacingInches = spacingInches;
        return this;
    }

    // Blueprint-specific builder methods

    public RenderSettings withFixedPageSize(float widthPoints, float heightPoints) {
        this.useFixedPageSize = true;
        this.pageWidthPoints = widthPoints;
        this.pageHeightPoints = heightPoints;
        return this;
    }

    public RenderSettings withArchitecturalBorder(boolean show) {
        this.showArchitecturalBorder = show;
        return this;
    }

    public RenderSettings withGridReferences(boolean show) {
        this.showGridReferences = show;
        return this;
    }

    public RenderSettings withGridDivisions(int horizontal, int vertical) {
        this.horizontalGridDivisions = horizontal;
        this.verticalGridDivisions = vertical;
        return this;
    }

    public RenderSettings withTraditionalTitleBlock(boolean use) {
        this.useTraditionalTitleBlock = use;
        return this;
    }

    public RenderSettings withCompanyName(String name) {
        this.companyName = name;
        return this;
    }

    public RenderSettings withDrawingTitle(String title) {
        this.drawingTitle = title;
        return this;
    }

    public RenderSettings withScaleNotation(String scale) {
        this.scaleNotation = scale;
        return this;
    }

    public RenderSettings withDate(String date) {
        this.date = date;
        return this;
    }

    public RenderSettings withCheckedBy(String checkedBy) {
        this.checkedBy = checkedBy;
        return this;
    }

    public RenderSettings withRevision(String number, String description) {
        this.revisionNumber = number;
        this.revisionDescription = description;
        return this;
    }

    public RenderSettings withSheetInfo(String sheetNumber, String totalSheets) {
        this.sheetNumber = sheetNumber;
        this.totalSheets = totalSheets;
        return this;
    }

    // Getters

    public double getScale() {
        return scale;
    }

    public double getMarginInches() {
        return marginInches;
    }

    public float getOutlineStrokeWidth() {
        return outlineStrokeWidth;
    }

    public float getDimensionLineStrokeWidth() {
        return dimensionLineStrokeWidth;
    }

    public Color getOutlineColor() {
        return outlineColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public Color getDimensionColor() {
        return dimensionColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public boolean isShowDimensions() {
        return showDimensions;
    }

    public boolean isShowPartLabels() {
        return showPartLabels;
    }

    public Font getDimensionFont() {
        return dimensionFont;
    }

    public Font getLabelFont() {
        return labelFont;
    }

    public double getDimensionOffset() {
        return dimensionOffset;
    }

    public double getArrowSize() {
        return arrowSize;
    }

    public boolean isShowTitleBlock() {
        return showTitleBlock;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getDrawnBy() {
        return drawnBy;
    }

    public boolean isShowGrid() {
        return showGrid;
    }

    public double getGridSpacingInches() {
        return gridSpacingInches;
    }

    public Color getGridColor() {
        return gridColor;
    }

    // Blueprint-specific getters

    public boolean isUseFixedPageSize() {
        return useFixedPageSize;
    }

    public float getPageWidthPoints() {
        return pageWidthPoints;
    }

    public float getPageHeightPoints() {
        return pageHeightPoints;
    }

    public boolean isShowArchitecturalBorder() {
        return showArchitecturalBorder;
    }

    public boolean isShowGridReferences() {
        return showGridReferences;
    }

    public int getHorizontalGridDivisions() {
        return horizontalGridDivisions;
    }

    public int getVerticalGridDivisions() {
        return verticalGridDivisions;
    }

    public boolean isUseTraditionalTitleBlock() {
        return useTraditionalTitleBlock;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getDrawingTitle() {
        return drawingTitle;
    }

    public String getScaleNotation() {
        return scaleNotation;
    }

    public String getDate() {
        return date;
    }

    public String getCheckedBy() {
        return checkedBy;
    }

    public String getRevisionNumber() {
        return revisionNumber;
    }

    public String getRevisionDescription() {
        return revisionDescription;
    }

    public String getSheetNumber() {
        return sheetNumber;
    }

    public String getTotalSheets() {
        return totalSheets;
    }

    // Derived stroke objects

    public Stroke getOutlineStroke() {
        return new BasicStroke(outlineStrokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }

    public Stroke getDimensionStroke() {
        return new BasicStroke(dimensionLineStrokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }

    public Stroke getGridStroke() {
        return new BasicStroke(gridLineStrokeWidth);
    }

    /**
     * Convert inches to points at current scale.
     */
    public double toPoints(double inches) {
        return inches * scale;
    }

    /**
     * Get margin in points.
     */
    public double getMarginPoints() {
        return toPoints(marginInches);
    }

    /**
     * Preset for detailed blueprints (larger scale, all annotations).
     */
    public static RenderSettings blueprint() {
        return new RenderSettings()
            .withScale(15.0)
            .withMargin(1.5)
            .withShowDimensions(true)
            .withShowPartLabels(true)
            .withShowTitleBlock(true)
            .withShowGrid(true);
    }

    /**
     * Preset for simple preview (smaller scale, minimal annotations).
     */
    public static RenderSettings preview() {
        return new RenderSettings()
            .withScale(8.0)
            .withMargin(0.5)
            .withShowDimensions(false)
            .withShowPartLabels(false)
            .withShowTitleBlock(false)
            .withShowGrid(false);
    }

    /**
     * Preset for CAD export (clean lines, no fills).
     */
    public static RenderSettings cadExport() {
        return new RenderSettings()
            .withScale(1.0)  // 1:1 scale for CAD
            .withMargin(0)
            .withFillColor(null)  // No fill
            .withShowDimensions(true)
            .withShowPartLabels(false)
            .withShowTitleBlock(false)
            .withShowGrid(false);
    }

    /**
     * Preset for traditional architectural blueprint style.
     * Fixed 11x8.5" landscape, white background with dark lines,
     * full architectural border and traditional title block.
     */
    public static RenderSettings architecturalBlueprint() {
        return new RenderSettings()
            .withFixedPageSize(792f, 612f)  // 11x8.5" landscape
            .withBackgroundColor(Color.WHITE)
            .withOutlineColor(new Color(20, 40, 80))  // Dark blue
            .withDimensionColor(new Color(20, 40, 80))
            .withFillColor(null)  // No fill for cleaner look
            .withArchitecturalBorder(true)
            .withGridReferences(true)
            .withGridDivisions(8, 6)  // A-H, 1-6
            .withTraditionalTitleBlock(true)
            .withShowDimensions(true)
            .withShowPartLabels(true)
            .withShowTitleBlock(false);  // Use traditional instead
    }
}
