package com.kof22.cabinetmaker.export;

import com.kof22.cabinetmaker.model.Cabinet;
import com.kof22.cabinetmaker.model.Dimensions3D;
import com.kof22.cabinetmaker.model.Part;
import com.kof22.cabinetmaker.rendering.OrthographicRenderer;
import com.kof22.cabinetmaker.rendering.RenderSettings;
import com.kof22.cabinetmaker.rendering.ViewType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.io.OutputStream;
import java.util.List;

/**
 * Exporter for PDF documents.
 * Uses Apache PDFBox for PDF generation.
 * Supports multi-page documents with multiple views.
 */
public class PdfExporter implements Exporter {

    @Override
    public void exportView(Cabinet cabinet, ViewType viewType,
                           RenderSettings settings, OutputStream output) throws ExportException {
        exportViews(cabinet, List.of(viewType), settings, output);
    }

    @Override
    public void exportViews(Cabinet cabinet, List<ViewType> viewTypes,
                            RenderSettings settings, OutputStream output) throws ExportException {
        // Check if blueprint mode is enabled
        if (settings.isUseFixedPageSize() && settings.isUseTraditionalTitleBlock()) {
            exportBlueprintViews(cabinet, viewTypes, settings, output);
            return;
        }

        // Original dynamic-size implementation
        try (PDDocument document = new PDDocument()) {
            for (ViewType viewType : viewTypes) {
                addViewPage(document, cabinet, viewType, settings);
            }

            document.save(output);
        } catch (Exception e) {
            throw new ExportException("Failed to generate PDF", e);
        }
    }

    /**
     * Export views in traditional architectural blueprint style.
     * Uses fixed page size with borders, grid references, and title block.
     */
    private void exportBlueprintViews(Cabinet cabinet, List<ViewType> viewTypes,
                                       RenderSettings settings, OutputStream output) throws ExportException {
        try (PDDocument document = new PDDocument()) {
            int sheetNumber = 1;
            int totalSheets = viewTypes.size();

            for (ViewType viewType : viewTypes) {
                addBlueprintPage(document, cabinet, viewType, settings,
                    String.valueOf(sheetNumber), String.valueOf(totalSheets));
                sheetNumber++;
            }

            document.save(output);
        } catch (Exception e) {
            throw new ExportException("Failed to generate blueprint PDF", e);
        }
    }

    /**
     * Add a page in architectural blueprint style.
     */
    private void addBlueprintPage(PDDocument document, Cabinet cabinet,
                                   ViewType viewType, RenderSettings settings,
                                   String sheetNum, String totalSheets) throws Exception {
        // Fixed page size: 11x8.5" landscape
        PDRectangle pageSize = new PDRectangle(BlueprintElements.PAGE_WIDTH,
                                                BlueprintElements.PAGE_HEIGHT);
        PDPage page = new PDPage(pageSize);
        document.addPage(page);

        // Create settings with sheet info
        RenderSettings pageSettings = RenderSettings.architecturalBlueprint()
            .withCompanyName(settings.getCompanyName())
            .withProjectName(settings.getProjectName())
            .withDrawnBy(settings.getDrawnBy())
            .withCheckedBy(settings.getCheckedBy())
            .withScaleNotation(settings.getScaleNotation())
            .withDate(settings.getDate())
            .withRevision(settings.getRevisionNumber(), settings.getRevisionDescription())
            .withSheetInfo(sheetNum, totalSheets);

        BlueprintElements blueprint = new BlueprintElements(pageSettings);

        try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
            // 1. White background
            cs.setNonStrokingColor(1f, 1f, 1f);
            cs.addRect(0, 0, pageSize.getWidth(), pageSize.getHeight());
            cs.fill();

            // 2. Draw architectural borders
            if (pageSettings.isShowArchitecturalBorder()) {
                blueprint.drawBorders(cs);
            }

            // 3. Draw grid reference system
            if (pageSettings.isShowGridReferences()) {
                blueprint.drawGridReferences(cs);
            }

            // 4. Calculate auto-scale and center offset
            Dimensions3D dims = cabinet.calculateBoundingBox();
            double viewWidth, viewHeight;

            // Isometric uses 30-degree angles, so bounding box is different
            double cos30 = Math.cos(Math.toRadians(30));
            double sin30 = Math.sin(Math.toRadians(30));

            switch (viewType) {
                case FRONT, BACK -> {
                    viewWidth = dims.width().toInchesDouble();
                    viewHeight = dims.height().toInchesDouble();
                }
                case LEFT, RIGHT -> {
                    viewWidth = dims.depth().toInchesDouble();
                    viewHeight = dims.height().toInchesDouble();
                }
                case TOP, BOTTOM -> {
                    viewWidth = dims.width().toInchesDouble();
                    viewHeight = dims.depth().toInchesDouble();
                }
                case ISOMETRIC -> {
                    // Isometric bounding box
                    double w = dims.width().toInchesDouble();
                    double h = dims.height().toInchesDouble();
                    double d = dims.depth().toInchesDouble();
                    viewWidth = (w + d) * cos30;
                    viewHeight = h + (w + d) * sin30 / 2;
                }
                default -> {
                    viewWidth = viewHeight = 10;
                }
            }

            double autoScale = blueprint.calculateAutoScale(viewWidth, viewHeight);
            double[] offset = blueprint.calculateCenterOffset(viewWidth, viewHeight, autoScale);

            // 5. Transform and draw cabinet content
            cs.saveGraphicsState();
            cs.transform(org.apache.pdfbox.util.Matrix.getTranslateInstance(
                (float) offset[0], (float) offset[1]));

            // Draw parts with auto-calculated scale
            RenderSettings scaledSettings = new RenderSettings()
                .withScale(autoScale)
                .withOutlineColor(pageSettings.getOutlineColor())
                .withFillColor(null)  // No fill for blueprint style
                .withShowPartLabels(true);

            if (viewType == ViewType.ISOMETRIC) {
                // Draw isometric view
                drawIsometricView(cs, cabinet, scaledSettings);
            } else {
                // Draw orthographic view
                for (Part part : cabinet.getParts()) {
                    drawPartToPdf(cs, part, viewType, scaledSettings);
                }
            }

            cs.restoreGraphicsState();

            // 6. Draw dimensions (outside the transform) - skip for isometric
            if (pageSettings.isShowDimensions() && viewType != ViewType.ISOMETRIC) {
                drawBlueprintDimensions(cs, cabinet, viewType, autoScale, offset);
            }

            // 7. Draw traditional title block
            if (pageSettings.isUseTraditionalTitleBlock()) {
                blueprint.drawTitleBlock(cs, cabinet.getName(), viewType.getDisplayName());
            }
        }
    }

    /**
     * Draw dimension annotations for blueprint style.
     */
    private void drawBlueprintDimensions(PDPageContentStream cs, Cabinet cabinet,
                                          ViewType viewType, double scale,
                                          double[] offset) throws Exception {
        Dimensions3D dims = cabinet.calculateBoundingBox();

        // Get view-specific dimensions
        String widthText, heightText;
        double drawWidth, drawHeight;

        switch (viewType) {
            case FRONT, BACK -> {
                widthText = dims.width().formatFractional();
                heightText = dims.height().formatFractional();
                drawWidth = dims.width().toInchesDouble() * scale;
                drawHeight = dims.height().toInchesDouble() * scale;
            }
            case LEFT, RIGHT -> {
                widthText = dims.depth().formatFractional();
                heightText = dims.height().formatFractional();
                drawWidth = dims.depth().toInchesDouble() * scale;
                drawHeight = dims.height().toInchesDouble() * scale;
            }
            case TOP, BOTTOM -> {
                widthText = dims.width().formatFractional();
                heightText = dims.depth().formatFractional();
                drawWidth = dims.width().toInchesDouble() * scale;
                drawHeight = dims.depth().toInchesDouble() * scale;
            }
            default -> {
                return;
            }
        }

        // Dark blue for dimensions
        cs.setStrokingColor(20f / 255f, 40f / 255f, 80f / 255f);
        cs.setLineWidth(0.5f);

        // Width dimension below drawing
        float dimY = (float) (offset[1] - 20);
        float startX = (float) offset[0];
        float endX = (float) (offset[0] + drawWidth);

        // Extension lines
        cs.moveTo(startX, (float) offset[1] - 5);
        cs.lineTo(startX, dimY - 5);
        cs.moveTo(endX, (float) offset[1] - 5);
        cs.lineTo(endX, dimY - 5);
        cs.stroke();

        // Dimension line with arrows
        cs.moveTo(startX, dimY);
        cs.lineTo(endX, dimY);
        cs.stroke();

        // Width text
        cs.beginText();
        cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
        cs.setNonStrokingColor(20f / 255f, 40f / 255f, 80f / 255f);
        cs.newLineAtOffset((startX + endX) / 2 - 15, dimY - 12);
        cs.showText(widthText);
        cs.endText();

        // Height dimension to right of drawing
        float dimX = (float) (offset[0] + drawWidth + 20);
        float startY = (float) offset[1];
        float endY = (float) (offset[1] + drawHeight);

        // Extension lines
        cs.moveTo((float) (offset[0] + drawWidth + 5), startY);
        cs.lineTo(dimX + 5, startY);
        cs.moveTo((float) (offset[0] + drawWidth + 5), endY);
        cs.lineTo(dimX + 5, endY);
        cs.stroke();

        // Dimension line
        cs.moveTo(dimX, startY);
        cs.lineTo(dimX, endY);
        cs.stroke();

        // Height text
        cs.beginText();
        cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
        cs.newLineAtOffset(dimX + 5, (startY + endY) / 2 - 3);
        cs.showText(heightText);
        cs.endText();
    }

    /**
     * Draw the cabinet in isometric projection.
     * Uses standard 30-degree isometric angles.
     */
    private void drawIsometricView(PDPageContentStream cs, Cabinet cabinet,
                                    RenderSettings settings) throws Exception {
        double scale = settings.getScale();
        double cos30 = Math.cos(Math.toRadians(30));
        double sin30 = Math.sin(Math.toRadians(30));

        // Sort parts by depth (back to front) for proper overlap
        var parts = cabinet.getParts().stream()
            .sorted((a, b) -> {
                // Sort by sum of position coordinates (back-to-front ordering)
                double aDepth = a.getPosition().x().toInchesDouble() +
                               a.getPosition().z().toInchesDouble();
                double bDepth = b.getPosition().x().toInchesDouble() +
                               b.getPosition().z().toInchesDouble();
                return Double.compare(aDepth, bDepth);
            })
            .toList();

        cs.setLineWidth(settings.getOutlineStrokeWidth());

        for (Part part : parts) {
            var pos = part.getPosition();
            var dims = part.getDimensions();

            // Get part dimensions in inches
            double px = pos.x().toInchesDouble();
            double py = pos.y().toInchesDouble();
            double pz = pos.z().toInchesDouble();
            double w = dims.width().toInchesDouble();
            double h = dims.height().toInchesDouble();
            double d = dims.depth().toInchesDouble();

            // Calculate isometric corner positions
            // Origin offset to center the drawing
            Dimensions3D cabinetDims = cabinet.calculateBoundingBox();
            double offsetX = cabinetDims.depth().toInchesDouble() * cos30 * scale;

            // Convert 3D point to 2D isometric coordinates
            // x moves right along 30° down
            // y moves straight up
            // z moves left along 30° down

            // Front-bottom-left corner of the part
            double isoX0 = offsetX + (px * cos30 - pz * cos30) * scale;
            double isoY0 = (py + px * sin30 + pz * sin30) * scale;

            // Calculate the 8 corners of the box in isometric
            double[][] corners = new double[8][2];

            // Bottom face corners
            corners[0] = new double[] { isoX0, isoY0 };  // front-left
            corners[1] = new double[] { isoX0 + w * cos30 * scale, isoY0 + w * sin30 * scale };  // front-right
            corners[2] = new double[] { isoX0 + w * cos30 * scale - d * cos30 * scale,
                                        isoY0 + w * sin30 * scale + d * sin30 * scale };  // back-right
            corners[3] = new double[] { isoX0 - d * cos30 * scale, isoY0 + d * sin30 * scale };  // back-left

            // Top face corners (same as bottom + height)
            double hScale = h * scale;
            corners[4] = new double[] { corners[0][0], corners[0][1] + hScale };  // front-left-top
            corners[5] = new double[] { corners[1][0], corners[1][1] + hScale };  // front-right-top
            corners[6] = new double[] { corners[2][0], corners[2][1] + hScale };  // back-right-top
            corners[7] = new double[] { corners[3][0], corners[3][1] + hScale };  // back-left-top

            // Draw visible faces with light fill
            cs.setNonStrokingColor(0.95f, 0.95f, 0.95f);

            // Top face
            cs.moveTo((float) corners[4][0], (float) corners[4][1]);
            cs.lineTo((float) corners[5][0], (float) corners[5][1]);
            cs.lineTo((float) corners[6][0], (float) corners[6][1]);
            cs.lineTo((float) corners[7][0], (float) corners[7][1]);
            cs.closePath();
            cs.fill();

            // Right face (slightly darker)
            cs.setNonStrokingColor(0.88f, 0.88f, 0.88f);
            cs.moveTo((float) corners[1][0], (float) corners[1][1]);
            cs.lineTo((float) corners[5][0], (float) corners[5][1]);
            cs.lineTo((float) corners[6][0], (float) corners[6][1]);
            cs.lineTo((float) corners[2][0], (float) corners[2][1]);
            cs.closePath();
            cs.fill();

            // Front face (medium shade)
            cs.setNonStrokingColor(0.92f, 0.92f, 0.92f);
            cs.moveTo((float) corners[0][0], (float) corners[0][1]);
            cs.lineTo((float) corners[1][0], (float) corners[1][1]);
            cs.lineTo((float) corners[5][0], (float) corners[5][1]);
            cs.lineTo((float) corners[4][0], (float) corners[4][1]);
            cs.closePath();
            cs.fill();

            // Draw outlines
            cs.setStrokingColor(settings.getOutlineColor());

            // Draw all visible edges
            // Front face edges
            cs.moveTo((float) corners[0][0], (float) corners[0][1]);
            cs.lineTo((float) corners[1][0], (float) corners[1][1]);
            cs.lineTo((float) corners[5][0], (float) corners[5][1]);
            cs.lineTo((float) corners[4][0], (float) corners[4][1]);
            cs.closePath();
            cs.stroke();

            // Top face edges
            cs.moveTo((float) corners[4][0], (float) corners[4][1]);
            cs.lineTo((float) corners[5][0], (float) corners[5][1]);
            cs.lineTo((float) corners[6][0], (float) corners[6][1]);
            cs.lineTo((float) corners[7][0], (float) corners[7][1]);
            cs.closePath();
            cs.stroke();

            // Right face edges
            cs.moveTo((float) corners[1][0], (float) corners[1][1]);
            cs.lineTo((float) corners[2][0], (float) corners[2][1]);
            cs.lineTo((float) corners[6][0], (float) corners[6][1]);
            cs.stroke();

            // Left face edge (vertical back-left edge)
            cs.moveTo((float) corners[3][0], (float) corners[3][1]);
            cs.lineTo((float) corners[7][0], (float) corners[7][1]);
            cs.stroke();
        }
    }

    /**
     * Add a page with a cabinet view to the document.
     */
    private void addViewPage(PDDocument document, Cabinet cabinet,
                             ViewType viewType, RenderSettings settings) throws Exception {
        OrthographicRenderer renderer = new OrthographicRenderer(settings);
        Dimension canvasSize = renderer.calculateCanvasSize(cabinet, viewType);

        // Create page with appropriate size
        PDRectangle pageSize = new PDRectangle(canvasSize.width, canvasSize.height);
        PDPage page = new PDPage(pageSize);
        document.addPage(page);

        try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
            // Draw background
            cs.setNonStrokingColor(settings.getBackgroundColor());
            cs.addRect(0, 0, pageSize.getWidth(), pageSize.getHeight());
            cs.fill();

            // Draw cabinet parts
            Dimensions3D dims = cabinet.calculateBoundingBox();
            double margin = settings.getMarginPoints();
            double titleBlockHeight = settings.isShowTitleBlock() ? settings.toPoints(1.5) : 0;

            // Set up coordinate transform
            cs.saveGraphicsState();

            // PDF has origin at bottom-left, which matches our drawing convention
            cs.transform(org.apache.pdfbox.util.Matrix.getTranslateInstance(
                (float) margin, (float) (margin + titleBlockHeight)));

            // Draw each part
            for (Part part : cabinet.getParts()) {
                drawPartToPdf(cs, part, viewType, settings);
            }

            cs.restoreGraphicsState();

            // Draw overall dimensions
            if (settings.isShowDimensions()) {
                drawDimensionsToPdf(cs, cabinet, viewType, settings, canvasSize);
            }

            // Draw title block
            if (settings.isShowTitleBlock()) {
                drawTitleBlockToPdf(cs, cabinet, viewType, settings, canvasSize);
            }
        }
    }

    /**
     * Draw a part to the PDF content stream.
     */
    private void drawPartToPdf(PDPageContentStream cs, Part part,
                               ViewType viewType, RenderSettings settings) throws Exception {
        Rectangle2D rect = projectPartToPdf(part, viewType, settings);

        if (rect.getWidth() < 0.1 || rect.getHeight() < 0.1) {
            return;
        }

        // Fill
        if (settings.getFillColor() != null) {
            cs.setNonStrokingColor(settings.getFillColor());
            cs.addRect((float) rect.getX(), (float) rect.getY(),
                (float) rect.getWidth(), (float) rect.getHeight());
            cs.fill();
        }

        // Stroke
        cs.setStrokingColor(settings.getOutlineColor());
        cs.setLineWidth(settings.getOutlineStrokeWidth());
        cs.addRect((float) rect.getX(), (float) rect.getY(),
            (float) rect.getWidth(), (float) rect.getHeight());
        cs.stroke();

        // Part label
        if (settings.isShowPartLabels() && rect.getWidth() > 20 && rect.getHeight() > 15) {
            cs.beginText();
            cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 8);
            cs.setNonStrokingColor(settings.getOutlineColor());
            cs.newLineAtOffset((float) (rect.getCenterX() - 15), (float) (rect.getCenterY() - 3));
            cs.showText(truncateLabel(part.getName(), 12));
            cs.endText();
        }
    }

    /**
     * Project a part to PDF coordinates.
     */
    private Rectangle2D projectPartToPdf(Part part, ViewType viewType, RenderSettings settings) {
        var pos = part.getPosition();
        var dims = part.getDimensions();
        double scale = settings.getScale();

        double x, y, w, h;

        switch (viewType) {
            case FRONT, BACK -> {
                x = pos.x().toInchesDouble() * scale;
                y = pos.y().toInchesDouble() * scale;
                w = dims.width().toInchesDouble() * scale;
                h = dims.height().toInchesDouble() * scale;
            }
            case LEFT, RIGHT -> {
                x = pos.z().toInchesDouble() * scale;
                y = pos.y().toInchesDouble() * scale;
                w = dims.depth().toInchesDouble() * scale;
                h = dims.height().toInchesDouble() * scale;
            }
            case TOP, BOTTOM -> {
                x = pos.x().toInchesDouble() * scale;
                y = pos.z().toInchesDouble() * scale;
                w = dims.width().toInchesDouble() * scale;
                h = dims.depth().toInchesDouble() * scale;
            }
            default -> {
                x = y = w = h = 0;
            }
        }

        return new Rectangle2D.Double(x, y, w, h);
    }

    /**
     * Draw overall dimensions.
     */
    private void drawDimensionsToPdf(PDPageContentStream cs, Cabinet cabinet,
                                     ViewType viewType, RenderSettings settings,
                                     Dimension canvasSize) throws Exception {
        Dimensions3D dims = cabinet.calculateBoundingBox();
        double margin = settings.getMarginPoints();
        double scale = settings.getScale();

        cs.setStrokingColor(settings.getDimensionColor());
        cs.setLineWidth(settings.getDimensionLineStrokeWidth());

        // Get dimensions based on view
        String widthText, heightText;
        double drawWidth, drawHeight;

        switch (viewType) {
            case FRONT, BACK -> {
                widthText = dims.width().formatFractional();
                heightText = dims.height().formatFractional();
                drawWidth = dims.width().toInchesDouble() * scale;
                drawHeight = dims.height().toInchesDouble() * scale;
            }
            case LEFT, RIGHT -> {
                widthText = dims.depth().formatFractional();
                heightText = dims.height().formatFractional();
                drawWidth = dims.depth().toInchesDouble() * scale;
                drawHeight = dims.height().toInchesDouble() * scale;
            }
            case TOP, BOTTOM -> {
                widthText = dims.width().formatFractional();
                heightText = dims.depth().formatFractional();
                drawWidth = dims.width().toInchesDouble() * scale;
                drawHeight = dims.depth().toInchesDouble() * scale;
            }
            default -> {
                return;
            }
        }

        double titleBlockHeight = settings.isShowTitleBlock() ? settings.toPoints(1.5) : 0;

        // Width dimension (at bottom)
        float dimY = (float) (margin + titleBlockHeight - 15);
        cs.moveTo((float) margin, dimY);
        cs.lineTo((float) (margin + drawWidth), dimY);
        cs.stroke();

        // Width text
        cs.beginText();
        cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
        cs.setNonStrokingColor(settings.getDimensionColor());
        cs.newLineAtOffset((float) (margin + drawWidth / 2 - 15), dimY - 12);
        cs.showText(widthText);
        cs.endText();

        // Height dimension (at right)
        float dimX = (float) (margin + drawWidth + 10);
        cs.moveTo(dimX, (float) (margin + titleBlockHeight));
        cs.lineTo(dimX, (float) (margin + titleBlockHeight + drawHeight));
        cs.stroke();

        // Height text
        cs.beginText();
        cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
        cs.newLineAtOffset(dimX + 5, (float) (margin + titleBlockHeight + drawHeight / 2));
        cs.showText(heightText);
        cs.endText();
    }

    /**
     * Draw the title block.
     */
    private void drawTitleBlockToPdf(PDPageContentStream cs, Cabinet cabinet,
                                     ViewType viewType, RenderSettings settings,
                                     Dimension canvasSize) throws Exception {
        double margin = settings.getMarginPoints();
        double blockHeight = settings.toPoints(1.2);
        float y = (float) margin;
        float width = (float) (canvasSize.width - margin * 2);

        // Background (PDFBox 3.x uses float 0-1 for colors)
        cs.setNonStrokingColor(250f / 255f, 250f / 255f, 250f / 255f);
        cs.addRect((float) margin, y, width, (float) blockHeight);
        cs.fill();

        // Border
        cs.setStrokingColor(0f, 0f, 0f);
        cs.setLineWidth(1);
        cs.addRect((float) margin, y, width, (float) blockHeight);
        cs.stroke();

        // Cabinet name
        cs.beginText();
        cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
        cs.setNonStrokingColor(0f, 0f, 0f);
        cs.newLineAtOffset((float) margin + 10, y + (float) blockHeight - 18);
        cs.showText(cabinet.getName());
        cs.endText();

        // View name
        cs.beginText();
        cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 11);
        cs.newLineAtOffset((float) margin + 10, y + (float) blockHeight - 35);
        cs.showText(viewType.getDisplayName());
        cs.endText();

        // Dimensions summary
        Dimensions3D dims = cabinet.calculateBoundingBox();
        String dimText = String.format("Overall: %s W x %s H x %s D",
            dims.width().formatFractional(),
            dims.height().formatFractional(),
            dims.depth().formatFractional());

        cs.beginText();
        cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
        cs.newLineAtOffset((float) (canvasSize.width - margin - 200), y + (float) blockHeight - 18);
        cs.showText(dimText);
        cs.endText();
    }

    /**
     * Truncate a label if too long.
     */
    private String truncateLabel(String label, int maxLen) {
        if (label.length() <= maxLen) {
            return label;
        }
        return label.substring(0, maxLen - 2) + "..";
    }

    @Override
    public String getFileExtension() {
        return "pdf";
    }

    @Override
    public String getMimeType() {
        return "application/pdf";
    }

    @Override
    public String getFormatName() {
        return "PDF Document";
    }
}
