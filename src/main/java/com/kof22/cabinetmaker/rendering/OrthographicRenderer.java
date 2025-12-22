package com.kof22.cabinetmaker.rendering;

import com.kof22.cabinetmaker.model.Cabinet;
import com.kof22.cabinetmaker.model.Dimensions3D;
import com.kof22.cabinetmaker.model.Part;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Core renderer that projects 3D cabinet parts onto 2D orthographic views.
 * Works with any Graphics2D context (PNG, SVG, PDF, etc.).
 */
public class OrthographicRenderer {

    private final RenderSettings settings;

    public OrthographicRenderer(RenderSettings settings) {
        this.settings = settings;
    }

    public OrthographicRenderer() {
        this(new RenderSettings());
    }

    /**
     * Calculate the required canvas size for rendering a view.
     */
    public Dimension calculateCanvasSize(Cabinet cabinet, ViewType viewType) {
        Rectangle2D viewBounds = calculateViewBounds(cabinet, viewType);
        double margin = settings.getMarginPoints() * 2;
        double titleBlockHeight = settings.isShowTitleBlock() ? settings.toPoints(1.5) : 0;

        int width = (int) Math.ceil(viewBounds.getWidth() * settings.getScale() + margin);
        int height = (int) Math.ceil(viewBounds.getHeight() * settings.getScale() + margin + titleBlockHeight);

        return new Dimension(Math.max(width, 100), Math.max(height, 100));
    }

    /**
     * Render a cabinet view to a Graphics2D context.
     * This is the main entry point used by all exporters.
     */
    public void render(Cabinet cabinet, ViewType viewType, Graphics2D g2d) {
        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        Rectangle2D viewBounds = calculateViewBounds(cabinet, viewType);
        Dimension canvasSize = calculateCanvasSize(cabinet, viewType);

        // Fill background
        g2d.setColor(settings.getBackgroundColor());
        g2d.fillRect(0, 0, canvasSize.width, canvasSize.height);

        // Set up coordinate transform: origin at bottom-left with margin
        AffineTransform savedTransform = g2d.getTransform();
        double margin = settings.getMarginPoints();
        double titleBlockHeight = settings.isShowTitleBlock() ? settings.toPoints(1.5) : 0;

        // Flip Y-axis (drawing coordinates have Y increasing upward)
        g2d.translate(margin, canvasSize.height - margin - titleBlockHeight);
        g2d.scale(settings.getScale(), -settings.getScale());

        // Draw grid if enabled
        if (settings.isShowGrid()) {
            drawGrid(g2d, viewBounds);
        }

        // Sort parts for proper z-ordering (back to front)
        List<Part> sortedParts = sortPartsForView(cabinet.getParts(), viewType);

        // Draw all parts
        for (Part part : sortedParts) {
            drawPart(g2d, part, viewType);
        }

        // Draw overall dimensions
        if (settings.isShowDimensions()) {
            drawOverallDimensions(g2d, cabinet, viewType, viewBounds);
        }

        // Restore transform for title block
        g2d.setTransform(savedTransform);

        // Draw title block
        if (settings.isShowTitleBlock()) {
            drawTitleBlock(g2d, cabinet, viewType, canvasSize);
        }
    }

    /**
     * Calculate the 2D bounding box for a view (in inches).
     */
    private Rectangle2D calculateViewBounds(Cabinet cabinet, ViewType viewType) {
        Dimensions3D dims = cabinet.calculateBoundingBox();

        double cos30 = Math.cos(Math.toRadians(30));
        double sin30 = Math.sin(Math.toRadians(30));

        return switch (viewType) {
            case FRONT, BACK -> new Rectangle2D.Double(0, 0,
                dims.width().toInchesDouble(),
                dims.height().toInchesDouble());
            case LEFT, RIGHT -> new Rectangle2D.Double(0, 0,
                dims.depth().toInchesDouble(),
                dims.height().toInchesDouble());
            case TOP, BOTTOM -> new Rectangle2D.Double(0, 0,
                dims.width().toInchesDouble(),
                dims.depth().toInchesDouble());
            case ISOMETRIC -> {
                // Isometric bounding box
                double w = dims.width().toInchesDouble();
                double h = dims.height().toInchesDouble();
                double d = dims.depth().toInchesDouble();
                yield new Rectangle2D.Double(0, 0,
                    (w + d) * cos30,
                    h + (w + d) * sin30 / 2);
            }
        };
    }

    /**
     * Sort parts for proper layering in the given view.
     */
    private List<Part> sortPartsForView(List<Part> parts, ViewType viewType) {
        Comparator<Part> comparator = switch (viewType) {
            case FRONT -> Comparator.comparingDouble(p -> p.getPosition().z().toInchesDouble());
            case BACK -> Comparator.comparingDouble(p -> -p.getPosition().z().toInchesDouble());
            case LEFT -> Comparator.comparingDouble(p -> -p.getPosition().x().toInchesDouble());
            case RIGHT -> Comparator.comparingDouble(p -> p.getPosition().x().toInchesDouble());
            case TOP -> Comparator.comparingDouble(p -> p.getPosition().y().toInchesDouble());
            case BOTTOM -> Comparator.comparingDouble(p -> -p.getPosition().y().toInchesDouble());
            case ISOMETRIC -> Comparator.comparingDouble(p ->
                p.getPosition().x().toInchesDouble() + p.getPosition().z().toInchesDouble());
        };

        return parts.stream().sorted(comparator).collect(Collectors.toList());
    }

    /**
     * Project a part to a 2D rectangle for the given view.
     */
    private Rectangle2D projectPart(Part part, ViewType viewType) {
        var pos = part.getPosition();
        var dims = part.getDimensions();

        double x, y, w, h;

        switch (viewType) {
            case FRONT -> {
                x = pos.x().toInchesDouble();
                y = pos.y().toInchesDouble();
                w = dims.width().toInchesDouble();
                h = dims.height().toInchesDouble();
            }
            case BACK -> {
                // Mirror X for back view
                double totalWidth = dims.width().toInchesDouble();
                x = totalWidth - pos.x().toInchesDouble() - dims.width().toInchesDouble();
                y = pos.y().toInchesDouble();
                w = dims.width().toInchesDouble();
                h = dims.height().toInchesDouble();
            }
            case LEFT -> {
                x = pos.z().toInchesDouble();
                y = pos.y().toInchesDouble();
                w = dims.depth().toInchesDouble();
                h = dims.height().toInchesDouble();
            }
            case RIGHT -> {
                // Mirror Z for right view
                x = pos.z().toInchesDouble();
                y = pos.y().toInchesDouble();
                w = dims.depth().toInchesDouble();
                h = dims.height().toInchesDouble();
            }
            case TOP -> {
                x = pos.x().toInchesDouble();
                y = pos.z().toInchesDouble();
                w = dims.width().toInchesDouble();
                h = dims.depth().toInchesDouble();
            }
            case BOTTOM -> {
                x = pos.x().toInchesDouble();
                y = pos.z().toInchesDouble();
                w = dims.width().toInchesDouble();
                h = dims.depth().toInchesDouble();
            }
            default -> {
                x = y = w = h = 0;
            }
        }

        return new Rectangle2D.Double(x, y, w, h);
    }

    /**
     * Draw a single part.
     */
    private void drawPart(Graphics2D g2d, Part part, ViewType viewType) {
        Rectangle2D rect = projectPart(part, viewType);

        // Skip parts with zero size in this view
        if (rect.getWidth() < 0.01 || rect.getHeight() < 0.01) {
            return;
        }

        // Save current transform
        AffineTransform saved = g2d.getTransform();

        // Fill the part
        Color fillColor = settings.getFillColor();
        if (fillColor != null) {
            g2d.setColor(fillColor);
            g2d.fill(rect);
        }

        // Draw outline
        g2d.setColor(settings.getOutlineColor());
        g2d.setStroke(settings.getOutlineStroke());
        g2d.draw(rect);

        // Draw part label
        if (settings.isShowPartLabels()) {
            drawPartLabel(g2d, part, rect);
        }

        g2d.setTransform(saved);
    }

    /**
     * Draw a label for a part.
     */
    private void drawPartLabel(Graphics2D g2d, Part part, Rectangle2D rect) {
        // Only label if part is big enough
        if (rect.getWidth() < 2 || rect.getHeight() < 1) {
            return;
        }

        String label = part.getName();
        double centerX = rect.getCenterX();
        double centerY = rect.getCenterY();

        // Temporarily flip transform for text
        AffineTransform saved = g2d.getTransform();
        g2d.translate(centerX, centerY);
        g2d.scale(1 / settings.getScale(), -1 / settings.getScale());

        g2d.setFont(settings.getLabelFont());
        g2d.setColor(settings.getOutlineColor());

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(label);
        int textHeight = fm.getAscent();

        // Check if text fits
        double availableWidth = rect.getWidth() * settings.getScale() - 4;
        if (textWidth <= availableWidth) {
            g2d.drawString(label, -textWidth / 2, textHeight / 2 - 2);
        }

        g2d.setTransform(saved);
    }

    /**
     * Draw overall cabinet dimensions.
     */
    private void drawOverallDimensions(Graphics2D g2d, Cabinet cabinet, ViewType viewType, Rectangle2D bounds) {
        g2d.setColor(settings.getDimensionColor());
        g2d.setStroke(settings.getDimensionStroke());

        double offset = settings.getDimensionOffset();
        double arrowSize = settings.getArrowSize();

        Dimensions3D dims = cabinet.calculateBoundingBox();

        // Width dimension (bottom)
        String widthText = switch (viewType) {
            case FRONT, BACK, TOP, BOTTOM -> dims.width().formatFractional();
            case LEFT, RIGHT -> dims.depth().formatFractional();
            case ISOMETRIC -> dims.width().formatFractional();  // Show width for isometric
        };
        drawHorizontalDimension(g2d, 0, bounds.getWidth(), -offset, widthText, arrowSize);

        // Height dimension (right side)
        String heightText = switch (viewType) {
            case FRONT, BACK, LEFT, RIGHT -> dims.height().formatFractional();
            case TOP, BOTTOM -> dims.depth().formatFractional();
            case ISOMETRIC -> dims.height().formatFractional();  // Show height for isometric
        };
        drawVerticalDimension(g2d, bounds.getWidth() + offset, 0, bounds.getHeight(), heightText, arrowSize);
    }

    /**
     * Draw a horizontal dimension line with text.
     */
    private void drawHorizontalDimension(Graphics2D g2d, double x1, double x2, double y,
                                         String text, double arrowSize) {
        // Extension lines
        double extLen = Math.abs(y) + 0.2;
        g2d.draw(new Line2D.Double(x1, 0, x1, y - 0.1));
        g2d.draw(new Line2D.Double(x2, 0, x2, y - 0.1));

        // Dimension line
        g2d.draw(new Line2D.Double(x1, y, x2, y));

        // Arrows
        drawArrowHead(g2d, x1, y, arrowSize, 0);      // Left arrow
        drawArrowHead(g2d, x2, y, arrowSize, Math.PI); // Right arrow

        // Text
        double textX = (x1 + x2) / 2;
        AffineTransform saved = g2d.getTransform();
        g2d.translate(textX, y - 0.15);
        g2d.scale(1 / settings.getScale(), -1 / settings.getScale());
        g2d.setFont(settings.getDimensionFont());

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        g2d.drawString(text, -textWidth / 2, 0);

        g2d.setTransform(saved);
    }

    /**
     * Draw a vertical dimension line with text.
     */
    private void drawVerticalDimension(Graphics2D g2d, double x, double y1, double y2,
                                       String text, double arrowSize) {
        // Extension lines
        g2d.draw(new Line2D.Double(x - 0.2, y1, x + 0.1, y1));
        g2d.draw(new Line2D.Double(x - 0.2, y2, x + 0.1, y2));

        // Dimension line
        g2d.draw(new Line2D.Double(x, y1, x, y2));

        // Arrows
        drawArrowHead(g2d, x, y1, arrowSize, -Math.PI / 2); // Bottom arrow
        drawArrowHead(g2d, x, y2, arrowSize, Math.PI / 2);  // Top arrow

        // Text (rotated)
        double textY = (y1 + y2) / 2;
        AffineTransform saved = g2d.getTransform();
        g2d.translate(x + 0.25, textY);
        g2d.scale(1 / settings.getScale(), -1 / settings.getScale());
        g2d.rotate(-Math.PI / 2);
        g2d.setFont(settings.getDimensionFont());

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        g2d.drawString(text, -textWidth / 2, -2);

        g2d.setTransform(saved);
    }

    /**
     * Draw an arrow head at the given position.
     */
    private void drawArrowHead(Graphics2D g2d, double x, double y, double size, double angle) {
        Path2D arrow = new Path2D.Double();
        arrow.moveTo(0, 0);
        arrow.lineTo(-size, size / 3);
        arrow.lineTo(-size, -size / 3);
        arrow.closePath();

        AffineTransform saved = g2d.getTransform();
        g2d.translate(x, y);
        g2d.rotate(angle);
        g2d.fill(arrow);
        g2d.setTransform(saved);
    }

    /**
     * Draw a grid overlay.
     */
    private void drawGrid(Graphics2D g2d, Rectangle2D bounds) {
        g2d.setColor(settings.getGridColor());
        g2d.setStroke(settings.getGridStroke());

        double spacing = settings.getGridSpacingInches();

        // Vertical lines
        for (double x = 0; x <= bounds.getWidth(); x += spacing) {
            g2d.draw(new Line2D.Double(x, 0, x, bounds.getHeight()));
        }

        // Horizontal lines
        for (double y = 0; y <= bounds.getHeight(); y += spacing) {
            g2d.draw(new Line2D.Double(0, y, bounds.getWidth(), y));
        }
    }

    /**
     * Draw the title block.
     */
    private void drawTitleBlock(Graphics2D g2d, Cabinet cabinet, ViewType viewType, Dimension canvasSize) {
        double blockHeight = settings.toPoints(1.2);
        double y = canvasSize.height - blockHeight - 10;
        double margin = settings.getMarginPoints();

        // Title block background
        g2d.setColor(new Color(250, 250, 250));
        g2d.fillRect((int) margin, (int) y, (int) (canvasSize.width - margin * 2), (int) blockHeight);

        // Border
        g2d.setColor(Color.BLACK);
        g2d.setStroke(settings.getOutlineStroke());
        g2d.drawRect((int) margin, (int) y, (int) (canvasSize.width - margin * 2), (int) blockHeight);

        // Text
        g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2d.drawString(cabinet.getName(), (int) margin + 10, (int) y + 20);

        g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g2d.drawString(viewType.getDisplayName(), (int) margin + 10, (int) y + 38);

        // Dimensions summary on the right
        Dimensions3D dims = cabinet.calculateBoundingBox();
        String dimText = String.format("Overall: %s W x %s H x %s D",
            dims.width().formatFractional(),
            dims.height().formatFractional(),
            dims.depth().formatFractional());
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(dimText);
        g2d.drawString(dimText, canvasSize.width - (int) margin - textWidth - 10, (int) y + 20);

        // Project name if set
        if (!settings.getProjectName().isEmpty()) {
            g2d.drawString("Project: " + settings.getProjectName(),
                canvasSize.width - (int) margin - textWidth - 10, (int) y + 38);
        }
    }
}
