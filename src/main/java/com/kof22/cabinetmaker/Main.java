package com.kof22.cabinetmaker;

import com.kof22.cabinetmaker.export.*;
import com.kof22.cabinetmaker.model.Cabinet;
import com.kof22.cabinetmaker.rendering.RenderSettings;
import com.kof22.cabinetmaker.rendering.ViewType;
import com.kof22.cabinetmaker.testdata.TestCabinets;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * Demo application for the Cabinet Maker POC.
 * Generates sample cabinet drawings in multiple formats.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  Cabinet Maker POC - Rendering Demo");
        System.out.println("===========================================");
        System.out.println();

        try {
            // Create output directory
            Path outputDir = Path.of("output");
            Files.createDirectories(outputDir);
            System.out.println("Output directory: " + outputDir.toAbsolutePath());
            System.out.println();

            // Create test cabinets
            Cabinet baseCabinet = TestCabinets.simpleBaseCabinet();
            Cabinet wallCabinet = TestCabinets.simpleWallCabinet();

            // Configure render settings
            RenderSettings settings = new RenderSettings()
                .withScale(12.0)  // 12 points per inch
                .withMargin(1.0)
                .withShowDimensions(true)
                .withShowPartLabels(true)
                .withShowTitleBlock(true)
                .withProjectName("Kitchen Cabinet POC");

            // Export base cabinet
            System.out.println("Generating Base Cabinet outputs...");
            exportCabinet(baseCabinet, "base_cabinet", settings, outputDir);

            // Export wall cabinet
            System.out.println();
            System.out.println("Generating Wall Cabinet outputs...");
            exportCabinet(wallCabinet, "wall_cabinet", settings, outputDir);

            // Generate a multi-page PDF with all views
            System.out.println();
            System.out.println("Generating multi-page PDF blueprint...");
            generateBlueprint(baseCabinet, outputDir);

            // Generate architectural blueprint-style PDF
            System.out.println();
            System.out.println("Generating architectural blueprint...");
            generateArchitecturalBlueprint(baseCabinet, outputDir);

            System.out.println();
            System.out.println("===========================================");
            System.out.println("  All exports complete!");
            System.out.println("===========================================");
            System.out.println();
            System.out.println("Generated files:");
            Files.list(outputDir)
                .sorted()
                .forEach(p -> System.out.println("  - " + p.getFileName()));

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Export a cabinet to all formats for standard views.
     */
    private static void exportCabinet(Cabinet cabinet, String prefix,
                                      RenderSettings settings, Path outputDir) throws ExportException {
        List<ViewType> views = Arrays.asList(ViewType.FRONT, ViewType.LEFT, ViewType.TOP);

        // Create exporters
        Exporter pngExporter = new PngExporter(150);  // 150 DPI
        Exporter svgExporter = new SvgExporter();
        Exporter pdfExporter = new PdfExporter();
        Exporter dxfExporter = new DxfExporter();

        for (ViewType view : views) {
            String viewName = view.name().toLowerCase();

            // PNG
            Path pngPath = outputDir.resolve(prefix + "_" + viewName + ".png");
            pngExporter.exportToFile(cabinet, view, settings, pngPath);
            System.out.println("  Created: " + pngPath.getFileName());

            // SVG
            Path svgPath = outputDir.resolve(prefix + "_" + viewName + ".svg");
            svgExporter.exportToFile(cabinet, view, settings, svgPath);
            System.out.println("  Created: " + svgPath.getFileName());

            // PDF (single view)
            Path pdfPath = outputDir.resolve(prefix + "_" + viewName + ".pdf");
            pdfExporter.exportToFile(cabinet, view, settings, pdfPath);
            System.out.println("  Created: " + pdfPath.getFileName());

            // DXF
            Path dxfPath = outputDir.resolve(prefix + "_" + viewName + ".dxf");
            dxfExporter.exportToFile(cabinet, view, settings, dxfPath);
            System.out.println("  Created: " + dxfPath.getFileName());
        }
    }

    /**
     * Generate a multi-page PDF blueprint with all standard views.
     */
    private static void generateBlueprint(Cabinet cabinet, Path outputDir) throws ExportException {
        RenderSettings blueprintSettings = RenderSettings.blueprint()
            .withProjectName("Kitchen Renovation")
            .withDrawnBy("Cabinet Maker POC");

        List<ViewType> allViews = Arrays.asList(
            ViewType.FRONT,
            ViewType.LEFT,
            ViewType.RIGHT,
            ViewType.TOP
        );

        Path blueprintPath = outputDir.resolve("cabinet_blueprint.pdf");
        Exporter pdfExporter = new PdfExporter();
        pdfExporter.exportViewsToFile(cabinet, allViews, blueprintSettings, blueprintPath);
        System.out.println("  Created: " + blueprintPath.getFileName() + " (multi-page)");
    }

    /**
     * Generate an architectural blueprint-style PDF with traditional borders and title block.
     * Uses fixed 11x8.5" landscape pages.
     */
    private static void generateArchitecturalBlueprint(Cabinet cabinet, Path outputDir) throws ExportException {
        RenderSettings blueprintSettings = RenderSettings.architecturalBlueprint()
            .withCompanyName("KOF22 CABINET WORKS")
            .withProjectName("Kitchen Renovation 2025")
            .withDrawnBy("J. Smith")
            .withCheckedBy("M. Johnson")
            .withScaleNotation("AS NOTED")
            .withDate("2025-01-15")
            .withRevision("A", "Initial release");

        List<ViewType> allViews = Arrays.asList(
            ViewType.FRONT,
            ViewType.LEFT,
            ViewType.TOP,
            ViewType.ISOMETRIC
        );

        Path blueprintPath = outputDir.resolve("architectural_blueprint.pdf");
        Exporter pdfExporter = new PdfExporter();
        pdfExporter.exportViewsToFile(cabinet, allViews, blueprintSettings, blueprintPath);
        System.out.println("  Created: " + blueprintPath.getFileName() + " (architectural style, " + allViews.size() + " sheets)");
    }
}
