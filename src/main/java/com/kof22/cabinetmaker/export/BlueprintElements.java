package com.kof22.cabinetmaker.export;

import com.kof22.cabinetmaker.rendering.RenderSettings;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.awt.geom.Rectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Helper class for drawing traditional architectural blueprint elements.
 * Includes borders, grid references, and title block rendering.
 */
public class BlueprintElements {

    // Standard page dimensions (11x8.5" landscape in points, 72 pts/inch)
    public static final float PAGE_WIDTH = 792f;
    public static final float PAGE_HEIGHT = 612f;

    // Border dimensions
    public static final float OUTER_MARGIN = 36f;      // 0.5" from edge
    public static final float BORDER_GAP = 9f;         // 0.125" between borders
    public static final float OUTER_BORDER_WIDTH = 3f;
    public static final float INNER_BORDER_WIDTH = 1f;

    // Title block dimensions
    public static final float TITLE_BLOCK_WIDTH = 252f;   // 3.5" wide
    public static final float TITLE_BLOCK_HEIGHT = 162f;  // 2.25" tall

    // Grid reference zone width
    public static final float GRID_ZONE_WIDTH = 18f;      // 0.25"

    private final RenderSettings settings;

    public BlueprintElements(RenderSettings settings) {
        this.settings = settings;
    }

    /**
     * Calculate the usable drawing area (inside all borders, excluding title block).
     * The drawing area is positioned to avoid overlap with the title block in bottom-right.
     */
    public Rectangle2D getDrawingArea() {
        float innerMargin = OUTER_MARGIN + BORDER_GAP + GRID_ZONE_WIDTH;

        // Left and top boundaries (inside the grid reference zone)
        float left = innerMargin + 15;
        float top = PAGE_HEIGHT - innerMargin - 15;

        // Right boundary - stop before title block area with padding
        float right = PAGE_WIDTH - innerMargin - TITLE_BLOCK_WIDTH - 30;

        // Bottom boundary - raised above title block height with padding
        float bottom = innerMargin + TITLE_BLOCK_HEIGHT + 30;

        return new Rectangle2D.Float(left, bottom, right - left, top - bottom);
    }

    /**
     * Draw the complete architectural border system.
     */
    public void drawBorders(PDPageContentStream cs) throws Exception {
        // Outer thick border
        cs.setStrokingColor(0f, 0f, 0f);
        cs.setLineWidth(OUTER_BORDER_WIDTH);
        cs.addRect(OUTER_MARGIN, OUTER_MARGIN,
                   PAGE_WIDTH - 2 * OUTER_MARGIN,
                   PAGE_HEIGHT - 2 * OUTER_MARGIN);
        cs.stroke();

        // Inner thin border
        float innerOffset = OUTER_MARGIN + BORDER_GAP;
        cs.setLineWidth(INNER_BORDER_WIDTH);
        cs.addRect(innerOffset, innerOffset,
                   PAGE_WIDTH - 2 * innerOffset,
                   PAGE_HEIGHT - 2 * innerOffset);
        cs.stroke();
    }

    /**
     * Draw the grid reference system (A-H horizontally, 1-6 vertically).
     */
    public void drawGridReferences(PDPageContentStream cs) throws Exception {
        float innerLeft = OUTER_MARGIN + BORDER_GAP;
        float innerRight = PAGE_WIDTH - OUTER_MARGIN - BORDER_GAP;
        float innerBottom = OUTER_MARGIN + BORDER_GAP;
        float innerTop = PAGE_HEIGHT - OUTER_MARGIN - BORDER_GAP;

        float drawableWidth = innerRight - innerLeft - 2 * GRID_ZONE_WIDTH;
        float drawableHeight = innerTop - innerBottom - 2 * GRID_ZONE_WIDTH;

        int horizDivisions = settings.getHorizontalGridDivisions();
        int vertDivisions = settings.getVerticalGridDivisions();

        cs.setLineWidth(0.5f);
        cs.setStrokingColor(0.3f, 0.3f, 0.3f);

        // Draw horizontal grid tick marks
        float cellWidth = drawableWidth / horizDivisions;
        for (int i = 0; i <= horizDivisions; i++) {
            float x = innerLeft + GRID_ZONE_WIDTH + i * cellWidth;

            // Tick marks at top
            cs.moveTo(x, innerTop - GRID_ZONE_WIDTH);
            cs.lineTo(x, innerTop);

            // Tick marks at bottom
            cs.moveTo(x, innerBottom);
            cs.lineTo(x, innerBottom + GRID_ZONE_WIDTH);
        }
        cs.stroke();

        // Draw letter labels (top and bottom)
        PDType1Font boldFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        cs.setNonStrokingColor(0f, 0f, 0f);

        for (int i = 0; i < horizDivisions; i++) {
            char label = (char) ('A' + i);
            float x = innerLeft + GRID_ZONE_WIDTH + i * cellWidth + cellWidth / 2 - 3;

            // Top label
            cs.beginText();
            cs.setFont(boldFont, 8);
            cs.newLineAtOffset(x, innerTop - GRID_ZONE_WIDTH + 5);
            cs.showText(String.valueOf(label));
            cs.endText();

            // Bottom label
            cs.beginText();
            cs.setFont(boldFont, 8);
            cs.newLineAtOffset(x, innerBottom + 5);
            cs.showText(String.valueOf(label));
            cs.endText();
        }

        // Draw vertical grid tick marks
        float cellHeight = drawableHeight / vertDivisions;
        for (int i = 0; i <= vertDivisions; i++) {
            float y = innerBottom + GRID_ZONE_WIDTH + i * cellHeight;

            // Tick marks at left
            cs.moveTo(innerLeft, y);
            cs.lineTo(innerLeft + GRID_ZONE_WIDTH, y);

            // Tick marks at right
            cs.moveTo(innerRight - GRID_ZONE_WIDTH, y);
            cs.lineTo(innerRight, y);
        }
        cs.stroke();

        // Draw number labels (left and right)
        for (int i = 0; i < vertDivisions; i++) {
            String label = String.valueOf(vertDivisions - i);
            float y = innerBottom + GRID_ZONE_WIDTH + i * cellHeight + cellHeight / 2 - 3;

            // Left label
            cs.beginText();
            cs.setFont(boldFont, 8);
            cs.newLineAtOffset(innerLeft + 6, y);
            cs.showText(label);
            cs.endText();

            // Right label
            cs.beginText();
            cs.setFont(boldFont, 8);
            cs.newLineAtOffset(innerRight - GRID_ZONE_WIDTH + 6, y);
            cs.showText(label);
            cs.endText();
        }
    }

    /**
     * Draw the traditional title block in the bottom-right corner.
     */
    public void drawTitleBlock(PDPageContentStream cs, String cabinetName,
                                String viewName) throws Exception {
        float innerRight = PAGE_WIDTH - OUTER_MARGIN - BORDER_GAP;
        float innerBottom = OUTER_MARGIN + BORDER_GAP;

        float blockX = innerRight - TITLE_BLOCK_WIDTH;
        float blockY = innerBottom;

        // Title block outer border
        cs.setLineWidth(2f);
        cs.setStrokingColor(0f, 0f, 0f);
        cs.addRect(blockX, blockY, TITLE_BLOCK_WIDTH, TITLE_BLOCK_HEIGHT);
        cs.stroke();

        // Fill with light background
        cs.setNonStrokingColor(0.98f, 0.98f, 0.98f);
        cs.addRect(blockX + 1, blockY + 1, TITLE_BLOCK_WIDTH - 2, TITLE_BLOCK_HEIGHT - 2);
        cs.fill();

        // Divide into rows (from top to bottom):
        // Row 1 (32pt): Company/Logo area
        // Row 2 (32pt): Drawing title + view
        // Row 3 (24pt): Scale | Date
        // Row 4 (24pt): Drawn by | Checked by
        // Row 5 (24pt): Revision info
        // Row 6 (26pt): Project | Sheet number
        float[] rowHeights = {32f, 32f, 24f, 24f, 24f, 26f};
        float currentY = blockY + TITLE_BLOCK_HEIGHT;

        cs.setLineWidth(0.5f);
        cs.setStrokingColor(0f, 0f, 0f);

        // Draw horizontal dividers
        for (int i = 0; i < rowHeights.length - 1; i++) {
            currentY -= rowHeights[i];
            cs.moveTo(blockX, currentY);
            cs.lineTo(blockX + TITLE_BLOCK_WIDTH, currentY);
        }
        cs.stroke();

        // Draw vertical divider for split rows (rows 3-6)
        float midX = blockX + TITLE_BLOCK_WIDTH / 2;
        float splitRowsTop = blockY + rowHeights[5] + rowHeights[4] + rowHeights[3] + rowHeights[2];
        cs.moveTo(midX, blockY);
        cs.lineTo(midX, splitRowsTop);
        cs.stroke();

        // Fill in text content
        PDType1Font boldFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        PDType1Font regularFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

        currentY = blockY + TITLE_BLOCK_HEIGHT;

        // Row 1: Company name
        currentY -= rowHeights[0];
        cs.beginText();
        cs.setFont(boldFont, 11);
        cs.setNonStrokingColor(0f, 0f, 0f);
        String company = settings.getCompanyName().isEmpty() ? "CABINET MAKER" : settings.getCompanyName();
        cs.newLineAtOffset(blockX + 8, currentY + 10);
        cs.showText(company);
        cs.endText();

        // Row 2: Drawing title (cabinet name + view)
        currentY -= rowHeights[1];
        cs.beginText();
        cs.setFont(boldFont, 10);
        cs.newLineAtOffset(blockX + 8, currentY + 18);
        cs.showText(cabinetName);
        cs.endText();
        cs.beginText();
        cs.setFont(regularFont, 9);
        cs.newLineAtOffset(blockX + 8, currentY + 6);
        cs.showText(viewName);
        cs.endText();

        // Row 3: Scale | Date
        currentY -= rowHeights[2];
        drawLabelValue(cs, blockX + 4, currentY, "SCALE:",
            settings.getScaleNotation().isEmpty() ? "AS NOTED" : settings.getScaleNotation());
        drawLabelValue(cs, midX + 4, currentY, "DATE:",
            settings.getDate().isEmpty() ?
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) :
                settings.getDate());

        // Row 4: Drawn by | Checked by
        currentY -= rowHeights[3];
        drawLabelValue(cs, blockX + 4, currentY, "DRAWN BY:",
            settings.getDrawnBy().isEmpty() ? "-" : settings.getDrawnBy());
        drawLabelValue(cs, midX + 4, currentY, "CHECKED BY:",
            settings.getCheckedBy().isEmpty() ? "-" : settings.getCheckedBy());

        // Row 5: Revision
        currentY -= rowHeights[4];
        drawLabelValue(cs, blockX + 4, currentY, "REV:",
            settings.getRevisionNumber().isEmpty() ? "-" : settings.getRevisionNumber());
        drawLabelValue(cs, midX + 4, currentY, "DESC:",
            settings.getRevisionDescription().isEmpty() ? "-" : settings.getRevisionDescription());

        // Row 6: Project | Sheet number
        currentY -= rowHeights[5];
        drawLabelValue(cs, blockX + 4, currentY, "PROJECT:",
            settings.getProjectName().isEmpty() ? "-" : settings.getProjectName());

        // Sheet number - larger and centered
        cs.beginText();
        cs.setFont(boldFont, 10);
        cs.newLineAtOffset(midX + 10, currentY + 8);
        cs.showText("SHEET " + settings.getSheetNumber() + " OF " + settings.getTotalSheets());
        cs.endText();
    }

    /**
     * Helper to draw a label and value pair.
     */
    private void drawLabelValue(PDPageContentStream cs, float x, float y,
                                 String label, String value) throws Exception {
        PDType1Font regularFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

        cs.beginText();
        cs.setFont(regularFont, 6);
        cs.setNonStrokingColor(0.3f, 0.3f, 0.3f);
        cs.newLineAtOffset(x, y + 14);
        cs.showText(label);
        cs.endText();

        cs.beginText();
        cs.setFont(regularFont, 8);
        cs.setNonStrokingColor(0f, 0f, 0f);
        cs.newLineAtOffset(x, y + 4);
        // Truncate if too long
        String displayValue = value.length() > 14 ? value.substring(0, 12) + ".." : value;
        cs.showText(displayValue);
        cs.endText();
    }

    /**
     * Calculate the auto-scale factor to fit cabinet in drawing area.
     * Returns points per inch.
     */
    public double calculateAutoScale(double cabinetWidthInches, double cabinetHeightInches) {
        Rectangle2D drawingArea = getDrawingArea();

        // Reserve space for dimensions annotations (lines + text on each side)
        double usableWidth = drawingArea.getWidth() - 60;   // 60pt for dimension annotations
        double usableHeight = drawingArea.getHeight() - 60;

        double scaleX = usableWidth / cabinetWidthInches;
        double scaleY = usableHeight / cabinetHeightInches;

        // Use the smaller scale to ensure it fits, reduced to 65% for comfortable margins
        return Math.min(scaleX, scaleY) * 0.65;
    }

    /**
     * Calculate the offset to center the cabinet in the drawing area.
     */
    public double[] calculateCenterOffset(double cabinetWidthInches,
                                           double cabinetHeightInches,
                                           double scale) {
        Rectangle2D drawingArea = getDrawingArea();

        double scaledWidth = cabinetWidthInches * scale;
        double scaledHeight = cabinetHeightInches * scale;

        // Center horizontally in full drawing area
        double offsetX = drawingArea.getX() + (drawingArea.getWidth() - scaledWidth) / 2;

        // Center vertically, but shift up slightly to leave room for width dimension
        double offsetY = drawingArea.getY() + (drawingArea.getHeight() - scaledHeight) / 2 + 15;

        return new double[] { offsetX, offsetY };
    }
}
