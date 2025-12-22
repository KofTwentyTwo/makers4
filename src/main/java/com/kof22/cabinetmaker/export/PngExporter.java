package com.kof22.cabinetmaker.export;

import com.kof22.cabinetmaker.model.Cabinet;
import com.kof22.cabinetmaker.rendering.OrthographicRenderer;
import com.kof22.cabinetmaker.rendering.RenderSettings;
import com.kof22.cabinetmaker.rendering.ViewType;

import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

/**
 * Exporter for PNG raster images.
 * Uses Java2D's BufferedImage and ImageIO.
 */
public class PngExporter implements Exporter {

    private final int dpi;

    /**
     * Create a PNG exporter with default DPI (150).
     */
    public PngExporter() {
        this(150);
    }

    /**
     * Create a PNG exporter with specified DPI.
     *
     * @param dpi Dots per inch for the output image
     */
    public PngExporter(int dpi) {
        this.dpi = dpi;
    }

    @Override
    public void exportView(Cabinet cabinet, ViewType viewType,
                           RenderSettings settings, OutputStream output) throws ExportException {
        OrthographicRenderer renderer = new OrthographicRenderer(settings);
        Dimension size = renderer.calculateCanvasSize(cabinet, viewType);

        // Scale for DPI (base is 72 points per inch)
        double scaleFactor = dpi / 72.0;
        int imageWidth = (int) Math.ceil(size.width * scaleFactor);
        int imageHeight = (int) Math.ceil(size.height * scaleFactor);

        // Create buffered image with transparency support
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        try {
            // Enable high-quality rendering
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            // Scale for DPI
            g2d.scale(scaleFactor, scaleFactor);

            // Render the cabinet view
            renderer.render(cabinet, viewType, g2d);
        } finally {
            g2d.dispose();
        }

        // Write to output stream
        try {
            boolean success = ImageIO.write(image, "PNG", output);
            if (!success) {
                throw new ExportException("No PNG writer found");
            }
        } catch (Exception e) {
            throw new ExportException("Failed to write PNG image", e);
        }
    }

    @Override
    public String getFileExtension() {
        return "png";
    }

    @Override
    public String getMimeType() {
        return "image/png";
    }

    @Override
    public String getFormatName() {
        return "PNG Image";
    }

    /**
     * Get the DPI setting for this exporter.
     */
    public int getDpi() {
        return dpi;
    }
}
