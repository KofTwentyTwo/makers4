package com.kof22.cabinetmaker.export;

import com.kof22.cabinetmaker.model.Cabinet;
import com.kof22.cabinetmaker.rendering.OrthographicRenderer;
import com.kof22.cabinetmaker.rendering.RenderSettings;
import com.kof22.cabinetmaker.rendering.ViewType;
import org.jfree.graphics2d.svg.SVGGraphics2D;

import java.awt.Dimension;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * Exporter for SVG vector graphics.
 * Uses JFreeSVG library which provides a Graphics2D implementation that outputs SVG.
 */
public class SvgExporter implements Exporter {

    @Override
    public void exportView(Cabinet cabinet, ViewType viewType,
                           RenderSettings settings, OutputStream output) throws ExportException {
        OrthographicRenderer renderer = new OrthographicRenderer(settings);
        Dimension size = renderer.calculateCanvasSize(cabinet, viewType);

        // JFreeSVG provides a Graphics2D that outputs SVG
        SVGGraphics2D g2d = new SVGGraphics2D(size.width, size.height);

        // Render using our standard Graphics2D renderer
        renderer.render(cabinet, viewType, g2d);

        // Get the SVG document
        String svgDocument = g2d.getSVGDocument();

        // Write to output stream
        try (OutputStreamWriter writer = new OutputStreamWriter(output, StandardCharsets.UTF_8)) {
            writer.write(svgDocument);
        } catch (Exception e) {
            throw new ExportException("Failed to write SVG document", e);
        }
    }

    @Override
    public String getFileExtension() {
        return "svg";
    }

    @Override
    public String getMimeType() {
        return "image/svg+xml";
    }

    @Override
    public String getFormatName() {
        return "SVG Vector";
    }
}
