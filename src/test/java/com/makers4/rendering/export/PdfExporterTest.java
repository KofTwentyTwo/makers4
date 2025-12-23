package com.makers4.rendering.export;


import java.io.ByteArrayOutputStream;
import java.util.List;
import com.makers4.rendering.RenderSettings;
import com.makers4.rendering.camera.ViewDirection;
import com.makers4.rendering.scene.RenderStyle;
import com.makers4.rendering.scene.SceneNode;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/*******************************************************************************
 ** Unit tests for PdfExporter class.
 *******************************************************************************/
class PdfExporterTest
{
   private PdfExporter exporter;
   private RenderSettings settings;



   @BeforeEach
   void setUp()
   {
      exporter = new PdfExporter();
      settings = RenderSettings.preview();
   }



   /*******************************************************************************
    ** Test exporter metadata.
    *******************************************************************************/
   @Test
   void testExporterMetadata()
   {
      assertThat(exporter.getFileExtension()).isEqualTo("pdf");
      assertThat(exporter.getMimeType()).isEqualTo("application/pdf");
      assertThat(exporter.getFormatName()).isEqualTo("PDF Document");
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

      byte[] pdfData = output.toByteArray();

      // PDF should start with %PDF header
      assertThat(pdfData.length).isGreaterThan(0);
      assertThat(new String(pdfData, 0, 4)).isEqualTo("%PDF");
   }



   /*******************************************************************************
    ** Test exporting multiple views.
    *******************************************************************************/
   @Test
   void testExportMultipleViews() throws ExportException
   {
      SceneNode scene = createTestScene();
      ByteArrayOutputStream output = new ByteArrayOutputStream();

      List<ViewDirection> views = List.of(
         ViewDirection.FRONT,
         ViewDirection.LEFT,
         ViewDirection.TOP
      );

      exporter.exportViews(scene, views, settings, output);

      byte[] pdfData = output.toByteArray();
      assertThat(pdfData.length).isGreaterThan(0);
      assertThat(new String(pdfData, 0, 4)).isEqualTo("%PDF");
   }



   /*******************************************************************************
    ** Test exporting with blueprint settings.
    *******************************************************************************/
   @Test
   void testExportBlueprintStyle() throws ExportException
   {
      SceneNode scene = createTestScene();
      ByteArrayOutputStream output = new ByteArrayOutputStream();

      RenderSettings blueprintSettings = RenderSettings.architecturalBlueprint()
         .withProjectName("Test Project")
         .withDrawnBy("Test User");

      exporter.exportView(scene, ViewDirection.FRONT, blueprintSettings, output);

      byte[] pdfData = output.toByteArray();
      assertThat(pdfData.length).isGreaterThan(0);
   }



   /*******************************************************************************
    ** Test exporting isometric view.
    *******************************************************************************/
   @Test
   void testExportIsometricView() throws ExportException
   {
      SceneNode scene = createTestScene();
      ByteArrayOutputStream output = new ByteArrayOutputStream();

      exporter.exportView(scene, ViewDirection.ISOMETRIC, settings, output);

      byte[] pdfData = output.toByteArray();
      assertThat(pdfData.length).isGreaterThan(0);
   }



   /*******************************************************************************
    ** Test exporting empty scene.
    *******************************************************************************/
   @Test
   void testExportEmptyScene() throws ExportException
   {
      SceneNode emptyScene = new SceneNode("empty");
      ByteArrayOutputStream output = new ByteArrayOutputStream();

      exporter.exportView(emptyScene, ViewDirection.FRONT, settings, output);

      // Should still produce valid (albeit empty) PDF
      byte[] pdfData = output.toByteArray();
      assertThat(pdfData.length).isGreaterThan(0);
   }



   /*******************************************************************************
    ** Test export all standard views.
    *******************************************************************************/
   @Test
   void testExportAllStandardViews() throws ExportException
   {
      SceneNode scene = createTestScene();

      for(ViewDirection view : ViewDirection.standardViews())
      {
         ByteArrayOutputStream output = new ByteArrayOutputStream();
         exporter.exportView(scene, view, settings, output);
         assertThat(output.toByteArray().length).isGreaterThan(0);
      }
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
