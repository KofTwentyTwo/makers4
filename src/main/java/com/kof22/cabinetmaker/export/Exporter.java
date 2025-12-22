package com.kof22.cabinetmaker.export;

import com.kof22.cabinetmaker.model.Cabinet;
import com.kof22.cabinetmaker.rendering.RenderSettings;
import com.kof22.cabinetmaker.rendering.ViewType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Common interface for all export formats.
 * Implementations handle specific format details (PNG, SVG, PDF, DXF).
 */
public interface Exporter {

    /**
     * Export a single view of a cabinet to the output stream.
     *
     * @param cabinet  The cabinet to render
     * @param viewType The view angle to render
     * @param settings Rendering configuration
     * @param output   The output stream to write to
     * @throws ExportException if export fails
     */
    void exportView(Cabinet cabinet, ViewType viewType,
                    RenderSettings settings, OutputStream output) throws ExportException;

    /**
     * Export multiple views to a single file (for formats that support it, like PDF).
     * Default implementation exports only the first view.
     *
     * @param cabinet   The cabinet to render
     * @param viewTypes List of views to include
     * @param settings  Rendering configuration
     * @param output    The output stream to write to
     * @throws ExportException if export fails
     */
    default void exportViews(Cabinet cabinet, List<ViewType> viewTypes,
                             RenderSettings settings, OutputStream output) throws ExportException {
        if (!viewTypes.isEmpty()) {
            exportView(cabinet, viewTypes.get(0), settings, output);
        }
    }

    /**
     * Convenience method to export a single view to a file.
     *
     * @param cabinet    The cabinet to render
     * @param viewType   The view angle to render
     * @param settings   Rendering configuration
     * @param outputPath The file path to write to
     * @throws ExportException if export fails
     */
    default void exportToFile(Cabinet cabinet, ViewType viewType,
                              RenderSettings settings, Path outputPath) throws ExportException {
        try (OutputStream out = Files.newOutputStream(outputPath)) {
            exportView(cabinet, viewType, settings, out);
        } catch (IOException e) {
            throw new ExportException("Failed to write to file: " + outputPath, e);
        }
    }

    /**
     * Convenience method to export multiple views to a file.
     *
     * @param cabinet    The cabinet to render
     * @param viewTypes  List of views to include
     * @param settings   Rendering configuration
     * @param outputPath The file path to write to
     * @throws ExportException if export fails
     */
    default void exportViewsToFile(Cabinet cabinet, List<ViewType> viewTypes,
                                   RenderSettings settings, Path outputPath) throws ExportException {
        try (OutputStream out = Files.newOutputStream(outputPath)) {
            exportViews(cabinet, viewTypes, settings, out);
        } catch (IOException e) {
            throw new ExportException("Failed to write to file: " + outputPath, e);
        }
    }

    /**
     * Get the file extension this exporter produces (without dot).
     */
    String getFileExtension();

    /**
     * Get the MIME type for the output format.
     */
    String getMimeType();

    /**
     * Get a display name for this format.
     */
    default String getFormatName() {
        return getFileExtension().toUpperCase();
    }
}
