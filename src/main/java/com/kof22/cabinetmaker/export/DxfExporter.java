package com.kof22.cabinetmaker.export;

import com.kof22.cabinetmaker.model.Cabinet;
import com.kof22.cabinetmaker.model.Dimensions3D;
import com.kof22.cabinetmaker.model.Part;
import com.kof22.cabinetmaker.rendering.RenderSettings;
import com.kof22.cabinetmaker.rendering.ViewType;

import java.awt.geom.Rectangle2D;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Exporter for DXF (AutoCAD Drawing Exchange Format) files.
 * Generates ASCII DXF format for maximum compatibility.
 * DXF is a text-based format, making it easy to generate directly.
 */
public class DxfExporter implements Exporter {

    // DXF uses 1 unit = 1 inch by convention for cabinet work
    private static final double SCALE = 1.0;

    @Override
    public void exportView(Cabinet cabinet, ViewType viewType,
                           RenderSettings settings, OutputStream output) throws ExportException {
        try (PrintWriter writer = new PrintWriter(output)) {
            writeDxfHeader(writer, cabinet, viewType);
            writeDxfTables(writer);
            writeDxfEntities(writer, cabinet, viewType, settings);
            writeDxfFooter(writer);
        } catch (Exception e) {
            throw new ExportException("Failed to generate DXF", e);
        }
    }

    /**
     * Write the DXF header section.
     */
    private void writeDxfHeader(PrintWriter w, Cabinet cabinet, ViewType viewType) {
        Dimensions3D dims = cabinet.calculateBoundingBox();

        // Calculate drawing limits based on view
        double maxX, maxY;
        switch (viewType) {
            case FRONT, BACK -> {
                maxX = dims.width().toInchesDouble();
                maxY = dims.height().toInchesDouble();
            }
            case LEFT, RIGHT -> {
                maxX = dims.depth().toInchesDouble();
                maxY = dims.height().toInchesDouble();
            }
            case TOP, BOTTOM -> {
                maxX = dims.width().toInchesDouble();
                maxY = dims.depth().toInchesDouble();
            }
            default -> {
                maxX = maxY = 100;
            }
        }

        w.println("0");
        w.println("SECTION");
        w.println("2");
        w.println("HEADER");

        // AutoCAD version (R14 for compatibility)
        writeGroupCode(w, 9, "$ACADVER");
        writeGroupCode(w, 1, "AC1014");

        // Drawing units (inches)
        writeGroupCode(w, 9, "$INSUNITS");
        writeGroupCode(w, 70, "1");  // 1 = inches

        // Drawing limits
        writeGroupCode(w, 9, "$LIMMIN");
        writeGroupCode(w, 10, "0.0");
        writeGroupCode(w, 20, "0.0");
        writeGroupCode(w, 9, "$LIMMAX");
        writeGroupCode(w, 10, String.valueOf(maxX + 5));
        writeGroupCode(w, 20, String.valueOf(maxY + 5));

        w.println("0");
        w.println("ENDSEC");
    }

    /**
     * Write the DXF tables section (layers).
     */
    private void writeDxfTables(PrintWriter w) {
        w.println("0");
        w.println("SECTION");
        w.println("2");
        w.println("TABLES");

        // Layer table
        w.println("0");
        w.println("TABLE");
        w.println("2");
        w.println("LAYER");
        writeGroupCode(w, 70, "3");  // Number of layers

        // Layer 0 - default
        writeLayer(w, "0", 7);  // White

        // PARTS layer
        writeLayer(w, "PARTS", 7);  // White

        // DIMENSIONS layer
        writeLayer(w, "DIMENSIONS", 5);  // Blue

        w.println("0");
        w.println("ENDTAB");
        w.println("0");
        w.println("ENDSEC");
    }

    /**
     * Write a layer definition.
     */
    private void writeLayer(PrintWriter w, String name, int color) {
        w.println("0");
        w.println("LAYER");
        writeGroupCode(w, 2, name);
        writeGroupCode(w, 70, "0");
        writeGroupCode(w, 62, String.valueOf(color));
        writeGroupCode(w, 6, "CONTINUOUS");
    }

    /**
     * Write the DXF entities section.
     */
    private void writeDxfEntities(PrintWriter w, Cabinet cabinet,
                                  ViewType viewType, RenderSettings settings) {
        w.println("0");
        w.println("SECTION");
        w.println("2");
        w.println("ENTITIES");

        // Draw each part as a closed polyline
        for (Part part : cabinet.getParts()) {
            Rectangle2D rect = projectPart(part, viewType);
            if (rect.getWidth() > 0.01 && rect.getHeight() > 0.01) {
                writeRectangle(w, rect, "PARTS", part.getName());
            }
        }

        // Draw dimensions if enabled
        if (settings.isShowDimensions()) {
            writeDimensions(w, cabinet, viewType);
        }

        w.println("0");
        w.println("ENDSEC");
    }

    /**
     * Write the DXF footer.
     */
    private void writeDxfFooter(PrintWriter w) {
        w.println("0");
        w.println("EOF");
    }

    /**
     * Project a part to 2D coordinates for the given view.
     */
    private Rectangle2D projectPart(Part part, ViewType viewType) {
        var pos = part.getPosition();
        var dims = part.getDimensions();

        double x, y, w, h;

        switch (viewType) {
            case FRONT, BACK -> {
                x = pos.x().toInchesDouble();
                y = pos.y().toInchesDouble();
                w = dims.width().toInchesDouble();
                h = dims.height().toInchesDouble();
            }
            case LEFT, RIGHT -> {
                x = pos.z().toInchesDouble();
                y = pos.y().toInchesDouble();
                w = dims.depth().toInchesDouble();
                h = dims.height().toInchesDouble();
            }
            case TOP, BOTTOM -> {
                x = pos.x().toInchesDouble();
                y = pos.z().toInchesDouble();
                w = dims.width().toInchesDouble();
                h = dims.depth().toInchesDouble();
            }
            default -> {
                x = y = w = h = 0;
            }
        }

        return new Rectangle2D.Double(x * SCALE, y * SCALE, w * SCALE, h * SCALE);
    }

    /**
     * Write a rectangle as a closed LWPOLYLINE.
     */
    private void writeRectangle(PrintWriter w, Rectangle2D rect, String layer, String label) {
        double x1 = rect.getX();
        double y1 = rect.getY();
        double x2 = rect.getX() + rect.getWidth();
        double y2 = rect.getY() + rect.getHeight();

        w.println("0");
        w.println("LWPOLYLINE");
        writeGroupCode(w, 8, layer);
        writeGroupCode(w, 90, "4");    // Number of vertices
        writeGroupCode(w, 70, "1");    // Closed polyline

        // Four corners
        writeVertex(w, x1, y1);
        writeVertex(w, x2, y1);
        writeVertex(w, x2, y2);
        writeVertex(w, x1, y2);

        // Add part label as text
        if (label != null && rect.getWidth() > 2 && rect.getHeight() > 1) {
            writeText(w, rect.getCenterX(), rect.getCenterY(), label, layer, 0.5);
        }
    }

    /**
     * Write a 2D vertex.
     */
    private void writeVertex(PrintWriter w, double x, double y) {
        writeGroupCode(w, 10, String.format("%.4f", x));
        writeGroupCode(w, 20, String.format("%.4f", y));
    }

    /**
     * Write a text entity.
     */
    private void writeText(PrintWriter w, double x, double y, String text, String layer, double height) {
        w.println("0");
        w.println("TEXT");
        writeGroupCode(w, 8, layer);
        writeGroupCode(w, 10, String.format("%.4f", x));
        writeGroupCode(w, 20, String.format("%.4f", y));
        writeGroupCode(w, 40, String.format("%.4f", height));  // Text height
        writeGroupCode(w, 1, text);
        writeGroupCode(w, 72, "1");  // Horizontal justification: center
        writeGroupCode(w, 11, String.format("%.4f", x));  // Alignment point
        writeGroupCode(w, 21, String.format("%.4f", y));
    }

    /**
     * Write dimension entities.
     */
    private void writeDimensions(PrintWriter w, Cabinet cabinet, ViewType viewType) {
        Dimensions3D dims = cabinet.calculateBoundingBox();

        double maxX, maxY;
        String widthText, heightText;

        switch (viewType) {
            case FRONT, BACK -> {
                maxX = dims.width().toInchesDouble();
                maxY = dims.height().toInchesDouble();
                widthText = dims.width().formatFractional();
                heightText = dims.height().formatFractional();
            }
            case LEFT, RIGHT -> {
                maxX = dims.depth().toInchesDouble();
                maxY = dims.height().toInchesDouble();
                widthText = dims.depth().formatFractional();
                heightText = dims.height().formatFractional();
            }
            case TOP, BOTTOM -> {
                maxX = dims.width().toInchesDouble();
                maxY = dims.depth().toInchesDouble();
                widthText = dims.width().formatFractional();
                heightText = dims.depth().formatFractional();
            }
            default -> {
                return;
            }
        }

        // Width dimension line (below drawing)
        writeLine(w, 0, -1.5, maxX, -1.5, "DIMENSIONS");
        writeText(w, maxX / 2, -2.5, widthText, "DIMENSIONS", 0.75);

        // Height dimension line (right of drawing)
        writeLine(w, maxX + 1.5, 0, maxX + 1.5, maxY, "DIMENSIONS");
        writeText(w, maxX + 2.5, maxY / 2, heightText, "DIMENSIONS", 0.75);
    }

    /**
     * Write a line entity.
     */
    private void writeLine(PrintWriter w, double x1, double y1, double x2, double y2, String layer) {
        w.println("0");
        w.println("LINE");
        writeGroupCode(w, 8, layer);
        writeGroupCode(w, 10, String.format("%.4f", x1));
        writeGroupCode(w, 20, String.format("%.4f", y1));
        writeGroupCode(w, 11, String.format("%.4f", x2));
        writeGroupCode(w, 21, String.format("%.4f", y2));
    }

    /**
     * Write a DXF group code pair.
     */
    private void writeGroupCode(PrintWriter w, int code, String value) {
        w.println(code);
        w.println(value);
    }

    @Override
    public String getFileExtension() {
        return "dxf";
    }

    @Override
    public String getMimeType() {
        return "application/dxf";
    }

    @Override
    public String getFormatName() {
        return "DXF (CAD)";
    }
}
