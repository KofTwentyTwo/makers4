package com.makers4.rendering.export;


import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import com.makers4.rendering.RenderSettings;
import com.makers4.rendering.camera.ViewDirection;
import com.makers4.rendering.scene.RenderStyle;
import com.makers4.rendering.scene.SceneNode;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/*******************************************************************************
 ** Unit tests for SvgExporter class.
 *******************************************************************************/
class SvgExporterTest
{
   private SvgExporter exporter;
   private RenderSettings settings;



   @BeforeEach
   void setUp()
   {
      exporter = new SvgExporter();
      settings = RenderSettings.preview().withScale(10.0);
   }



   /*******************************************************************************
    ** Test exporter metadata.
    *******************************************************************************/
   @Test
   void testExporterMetadata()
   {
      assertThat(exporter.getFileExtension()).isEqualTo("svg");
      assertThat(exporter.getMimeType()).isEqualTo("image/svg+xml");
      assertThat(exporter.getFormatName()).isEqualTo("SVG Vector");
   }



   /*******************************************************************************
    ** Test exporting a single view.
    *******************************************************************************/
   @Test
   void testExportSingleView() throws ExportException
   {
      SceneNode scene = createTestScene();
      ByteArrayOutputStream output = new ByteArrayOutputStream();

      exporter.exportView(scene, ViewDirection.FRONT, settings, output);

      String svgContent = output.toString(StandardCharsets.UTF_8);

      // SVG should have proper structure
      assertThat(svgContent).contains("<svg");
      assertThat(svgContent).contains("</svg>");
      assertThat(svgContent).contains("xmlns");
   }



   /*******************************************************************************
    ** Test exporting front view.
    *******************************************************************************/
   @Test
   void testExportFrontView() throws ExportException
   {
      SceneNode scene = createTestScene();
      ByteArrayOutputStream output = new ByteArrayOutputStream();

      exporter.exportView(scene, ViewDirection.FRONT, settings, output);

      String svgContent = output.toString(StandardCharsets.UTF_8);
      assertThat(svgContent).isNotEmpty();
   }



   /*******************************************************************************
    ** Test exporting left view.
    *******************************************************************************/
   @Test
   void testExportLeftView() throws ExportException
   {
      SceneNode scene = createTestScene();
      ByteArrayOutputStream output = new ByteArrayOutputStream();

      exporter.exportView(scene, ViewDirection.LEFT, settings, output);

      String svgContent = output.toString(StandardCharsets.UTF_8);
      assertThat(svgContent).contains("<svg");
   }



   /*******************************************************************************
    ** Test exporting top view.
    *******************************************************************************/
   @Test
   void testExportTopView() throws ExportException
   {
      SceneNode scene = createTestScene();
      ByteArrayOutputStream output = new ByteArrayOutputStream();

      exporter.exportView(scene, ViewDirection.TOP, settings, output);

      String svgContent = output.toString(StandardCharsets.UTF_8);
      assertThat(svgContent).contains("<svg");
   }



   /*******************************************************************************
    ** Test exporting empty scene.
    *******************************************************************************/
   @Test
   void testExportEmptyScene() throws ExportException
   {
      SceneNode emptyScene = new SceneNode("empty")
         .withSize(10, 10, 10);
      ByteArrayOutputStream output = new ByteArrayOutputStream();

      exporter.exportView(emptyScene, ViewDirection.FRONT, settings, output);

      String svgContent = output.toString(StandardCharsets.UTF_8);
      assertThat(svgContent).contains("<svg");
   }



   /*******************************************************************************
    ** Test all standard views.
    *******************************************************************************/
   @Test
   void testExportAllStandardViews() throws ExportException
   {
      SceneNode scene = createTestScene();

      for(ViewDirection view : ViewDirection.standardViews())
      {
         ByteArrayOutputStream output = new ByteArrayOutputStream();
         exporter.exportView(scene, view, settings, output);

         String svgContent = output.toString(StandardCharsets.UTF_8);
         assertThat(svgContent)
            .withFailMessage("SVG for %s should contain svg element", view)
            .contains("<svg");
      }
   }



   /*******************************************************************************
    ** Test SVG contains rect elements for boxes.
    *******************************************************************************/
   @Test
   void testSvgContainsRects() throws ExportException
   {
      SceneNode scene = createTestScene();
      ByteArrayOutputStream output = new ByteArrayOutputStream();

      exporter.exportView(scene, ViewDirection.FRONT, settings, output);

      String svgContent = output.toString(StandardCharsets.UTF_8);

      // Should contain rectangle elements for the cabinet parts
      assertThat(svgContent).contains("rect");
   }



   // ════════════════════════════════════════════════════════════════════════════
   // Helper methods
   // ════════════════════════════════════════════════════════════════════════════



   private SceneNode createTestScene()
   {
      SceneNode root = new SceneNode("test-cabinet")
         .withLabel("Test Cabinet")
         .withSize(24, 34.5, 24);

      root.addChild(new SceneNode("left-side")
         .withPosition(0, 4.5, 0)
         .withSize(0.75, 30, 23.75)
         .withStyle(RenderStyle.woodPanel()));

      root.addChild(new SceneNode("right-side")
         .withPosition(23.25, 4.5, 0)
         .withSize(0.75, 30, 23.75)
         .withStyle(RenderStyle.woodPanel()));

      root.addChild(new SceneNode("bottom")
         .withPosition(0.75, 4.5, 0)
         .withSize(22.5, 0.75, 23.75)
         .withStyle(RenderStyle.woodPanel()));

      return root;
   }
}
