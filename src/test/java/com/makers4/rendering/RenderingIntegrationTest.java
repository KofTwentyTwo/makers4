package com.makers4.rendering;


import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import com.makers4.model.Cabinet;
import com.makers4.rendering.builders.CabinetSceneBuilder;
import com.makers4.rendering.camera.ViewDirection;
import com.makers4.rendering.export.PdfExporter;
import com.makers4.rendering.export.SvgExporter;
import com.makers4.rendering.scene.SceneNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;


/*******************************************************************************
 ** Integration test that generates actual PDF and SVG output files.
 ** Outputs are written to the output/ directory for visual inspection.
 *******************************************************************************/
class RenderingIntegrationTest
{
   private static final String OUTPUT_DIR = "output/";



   /*******************************************************************************
    ** Test rendering a base cabinet to PDF and SVG.
    *******************************************************************************/
   @Test
   void testRenderBaseCabinetToFiles() throws Exception
   {
      // Create output directory
      new File(OUTPUT_DIR).mkdirs();

      // Create a base cabinet
      Cabinet cabinet = new Cabinet()
         .withName("24\" Base Cabinet")
         .withWidthMm(610)      // 24"
         .withHeightMm(876)     // 34.5"
         .withDepthMm(610)      // 24"
         .withCabinetTypeId(1L) // Base cabinet
         .withToeKickHeightMm(114)  // 4.5"
         .withToeKickDepthMm(76);   // 3"

      // Build the scene graph
      CabinetSceneBuilder builder = new CabinetSceneBuilder();
      SceneNode scene = builder.buildScene(cabinet);

      assertThat(scene).isNotNull();
      assertThat(scene.getChildren()).isNotEmpty();

      // Export to PDF
      renderPdf(scene, "base-cabinet-24.pdf");

      // Export to SVG for each view
      renderSvg(scene, ViewDirection.FRONT, "base-cabinet-24-front.svg");
      renderSvg(scene, ViewDirection.LEFT, "base-cabinet-24-left.svg");
      renderSvg(scene, ViewDirection.TOP, "base-cabinet-24-top.svg");
      renderSvg(scene, ViewDirection.ISOMETRIC, "base-cabinet-24-isometric.svg");

      System.out.println("\n=== RENDERED OUTPUT ===");
      System.out.println("Files written to: " + new File(OUTPUT_DIR).getAbsolutePath());
      System.out.println("- base-cabinet-24.pdf (Multi-page blueprint)");
      System.out.println("- base-cabinet-24-front.svg");
      System.out.println("- base-cabinet-24-left.svg");
      System.out.println("- base-cabinet-24-top.svg");
      System.out.println("- base-cabinet-24-isometric.svg");
   }



   /*******************************************************************************
    ** Test rendering a wall cabinet.
    *******************************************************************************/
   @Test
   void testRenderWallCabinetToFiles() throws Exception
   {
      new File(OUTPUT_DIR).mkdirs();

      Cabinet cabinet = new Cabinet()
         .withName("30\" Wall Cabinet")
         .withWidthMm(762)      // 30"
         .withHeightMm(762)     // 30"
         .withDepthMm(305)      // 12"
         .withCabinetTypeId(2L); // Wall cabinet

      CabinetSceneBuilder builder = new CabinetSceneBuilder();
      SceneNode scene = builder.buildScene(cabinet);

      renderPdf(scene, "wall-cabinet-30.pdf");
      renderSvg(scene, ViewDirection.FRONT, "wall-cabinet-30-front.svg");
      renderSvg(scene, ViewDirection.ISOMETRIC, "wall-cabinet-30-isometric.svg");

      System.out.println("\n=== RENDERED OUTPUT ===");
      System.out.println("- wall-cabinet-30.pdf");
      System.out.println("- wall-cabinet-30-front.svg");
      System.out.println("- wall-cabinet-30-isometric.svg");
   }



   /*******************************************************************************
    ** Test rendering a tall cabinet.
    *******************************************************************************/
   @Test
   void testRenderTallCabinetToFiles() throws Exception
   {
      new File(OUTPUT_DIR).mkdirs();

      Cabinet cabinet = new Cabinet()
         .withName("24\" Tall Pantry")
         .withWidthMm(610)      // 24"
         .withHeightMm(2134)    // 84"
         .withDepthMm(610)      // 24"
         .withCabinetTypeId(3L) // Tall cabinet
         .withToeKickHeightMm(114)
         .withToeKickDepthMm(76);

      CabinetSceneBuilder builder = new CabinetSceneBuilder();
      SceneNode scene = builder.buildScene(cabinet);

      renderPdf(scene, "tall-cabinet-84.pdf");
      renderSvg(scene, ViewDirection.FRONT, "tall-cabinet-84-front.svg");
      renderSvg(scene, ViewDirection.ISOMETRIC, "tall-cabinet-84-isometric.svg");

      System.out.println("\n=== RENDERED OUTPUT ===");
      System.out.println("- tall-cabinet-84.pdf");
      System.out.println("- tall-cabinet-84-front.svg");
      System.out.println("- tall-cabinet-84-isometric.svg");
   }



   /*******************************************************************************
    ** Test rendering with different scales.
    *******************************************************************************/
   @Test
   void testRenderWithDifferentScales() throws Exception
   {
      new File(OUTPUT_DIR).mkdirs();

      Cabinet cabinet = new Cabinet()
         .withName("Scale Test Cabinet")
         .withWidthMm(610)
         .withHeightMm(876)
         .withDepthMm(610)
         .withCabinetTypeId(1L)
         .withToeKickHeightMm(114)
         .withToeKickDepthMm(76);

      CabinetSceneBuilder builder = new CabinetSceneBuilder();
      SceneNode scene = builder.buildScene(cabinet);

      // Render at different scales
      SvgExporter exporter = new SvgExporter();

      // Small scale (6 pixels per inch)
      RenderSettings smallScale = RenderSettings.preview().withScale(6.0);
      try(FileOutputStream fos = new FileOutputStream(OUTPUT_DIR + "scale-test-6ppi.svg"))
      {
         exporter.exportView(scene, ViewDirection.FRONT, smallScale, fos);
      }

      // Large scale (24 pixels per inch)
      RenderSettings largeScale = RenderSettings.preview().withScale(24.0);
      try(FileOutputStream fos = new FileOutputStream(OUTPUT_DIR + "scale-test-24ppi.svg"))
      {
         exporter.exportView(scene, ViewDirection.FRONT, largeScale, fos);
      }

      System.out.println("\n=== SCALE TEST OUTPUT ===");
      System.out.println("- scale-test-6ppi.svg (small)");
      System.out.println("- scale-test-24ppi.svg (large)");
   }



   // ════════════════════════════════════════════════════════════════════════════
   // Helper methods
   // ════════════════════════════════════════════════════════════════════════════

   private void renderPdf(SceneNode scene, String filename) throws Exception
   {
      PdfExporter exporter = new PdfExporter();

      RenderSettings settings = RenderSettings.architecturalBlueprint()
         .withCompanyName("Makers4")
         .withProjectName("Test Cabinet")
         .withDrawnBy("Integration Test")
         .withDate(java.time.LocalDate.now().toString());

      List<ViewDirection> views = List.of(
         ViewDirection.FRONT,
         ViewDirection.LEFT,
         ViewDirection.TOP,
         ViewDirection.ISOMETRIC
      );

      try(FileOutputStream fos = new FileOutputStream(OUTPUT_DIR + filename))
      {
         exporter.exportViews(scene, views, settings, fos);
      }

      File outFile = new File(OUTPUT_DIR + filename);
      assertThat(outFile).exists();
      assertThat(outFile.length()).isGreaterThan(1000);  // Should be at least 1KB
   }



   private void renderSvg(SceneNode scene, ViewDirection view, String filename) throws Exception
   {
      SvgExporter exporter = new SvgExporter();

      RenderSettings settings = RenderSettings.preview()
         .withScale(12.0)
         .withShowPartLabels(true);

      try(FileOutputStream fos = new FileOutputStream(OUTPUT_DIR + filename))
      {
         exporter.exportView(scene, view, settings, fos);
      }

      File outFile = new File(OUTPUT_DIR + filename);
      assertThat(outFile).exists();
      assertThat(outFile.length()).isGreaterThan(100);  // Should be at least 100 bytes
   }
}
