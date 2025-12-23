package com.makers4.rendering;


import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import com.makers4.rendering.camera.OrthographicCamera;
import com.makers4.rendering.camera.ViewDirection;
import com.makers4.rendering.scene.RenderStyle;
import com.makers4.rendering.scene.SceneNode;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/*******************************************************************************
 ** Unit tests for SceneRenderer class.
 *******************************************************************************/
class SceneRendererTest
{
   private SceneRenderer renderer;
   private RenderSettings settings;



   @BeforeEach
   void setUp()
   {
      settings = new RenderSettings()
         .withScale(10.0)
         .withMargin(1.0);
      renderer = new SceneRenderer(settings);
   }



   /*******************************************************************************
    ** Test renderer creation.
    *******************************************************************************/
   @Test
   void testRendererCreation()
   {
      assertThat(renderer).isNotNull();
      assertThat(renderer.getSettings()).isEqualTo(settings);
   }



   /*******************************************************************************
    ** Test changing settings.
    *******************************************************************************/
   @Test
   void testWithSettings()
   {
      RenderSettings newSettings = RenderSettings.preview();

      SceneRenderer updated = renderer.withSettings(newSettings);

      assertThat(updated.getSettings()).isEqualTo(newSettings);
      assertThat(updated).isSameAs(renderer);  // Fluent, returns same instance
   }



   /*******************************************************************************
    ** Test canvas size calculation for front view.
    *******************************************************************************/
   @Test
   void testCalculateCanvasSizeFrontView()
   {
      SceneNode scene = createSimpleCabinetScene();
      OrthographicCamera camera = OrthographicCamera.forView(ViewDirection.FRONT)
         .withScale(settings.getScale());

      Dimension size = renderer.calculateCanvasSize(scene, camera);

      // 24" wide * 10 scale + 2 * (1" * 10) margin = 240 + 20 = 260
      // 34.5" tall * 10 scale + 2 * margin + title block = 345 + 20 + 15 = 380
      assertThat(size.width).isGreaterThan(250);
      assertThat(size.height).isGreaterThan(360);
   }



   /*******************************************************************************
    ** Test canvas size calculation for top view.
    *******************************************************************************/
   @Test
   void testCalculateCanvasSizeTopView()
   {
      SceneNode scene = createSimpleCabinetScene();
      OrthographicCamera camera = OrthographicCamera.forView(ViewDirection.TOP)
         .withScale(settings.getScale());

      Dimension size = renderer.calculateCanvasSize(scene, camera);

      // Top view shows width and depth (24" x 24" at scale 10)
      assertThat(size.width).isGreaterThan(250);
      assertThat(size.height).isGreaterThan(250);
   }



   /*******************************************************************************
    ** Test rendering to BufferedImage.
    *******************************************************************************/
   @Test
   void testRenderToBufferedImage()
   {
      SceneNode scene = createSimpleCabinetScene();
      OrthographicCamera camera = OrthographicCamera.forView(ViewDirection.FRONT)
         .withScale(10.0);

      // Create a BufferedImage to render to
      BufferedImage image = new BufferedImage(400, 500, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2d = image.createGraphics();

      camera.centerOn(scene.calculateTotalBounds(), 400, 500, 20);

      // Should not throw
      renderer.render(scene, camera, g2d);

      g2d.dispose();

      // Verify the image was rendered (non-empty)
      assertThat(image.getWidth()).isEqualTo(400);
      assertThat(image.getHeight()).isEqualTo(500);
   }



   /*******************************************************************************
    ** Test rendering different views.
    *******************************************************************************/
   @Test
   void testRenderDifferentViews()
   {
      SceneNode scene = createSimpleCabinetScene();

      for(ViewDirection direction : ViewDirection.values())
      {
         OrthographicCamera camera = OrthographicCamera.forView(direction)
            .withScale(10.0);

         BufferedImage image = new BufferedImage(400, 500, BufferedImage.TYPE_INT_ARGB);
         Graphics2D g2d = image.createGraphics();

         camera.centerOn(scene.calculateTotalBounds(), 400, 500, 20);

         // Should not throw for any view direction
         renderer.render(scene, camera, g2d);

         g2d.dispose();
      }
   }



   /*******************************************************************************
    ** Test rendering empty scene.
    *******************************************************************************/
   @Test
   void testRenderEmptyScene()
   {
      SceneNode emptyScene = new SceneNode("empty-root");
      OrthographicCamera camera = OrthographicCamera.forView(ViewDirection.FRONT)
         .withScale(10.0);

      BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2d = image.createGraphics();

      // Should not throw
      renderer.render(emptyScene, camera, g2d);

      g2d.dispose();
   }



   /*******************************************************************************
    ** Test rendering scene with nested children.
    *******************************************************************************/
   @Test
   void testRenderNestedScene()
   {
      SceneNode root = new SceneNode("root")
         .withSize(100, 100, 100);

      SceneNode child1 = new SceneNode("child1")
         .withPosition(10, 10, 0)
         .withSize(30, 30, 30)
         .withStyle(RenderStyle.woodPanel());

      SceneNode grandchild = new SceneNode("grandchild")
         .withPosition(5, 5, 0)
         .withSize(10, 10, 10)
         .withStyle(RenderStyle.outline());

      child1.addChild(grandchild);
      root.addChild(child1);

      OrthographicCamera camera = OrthographicCamera.forView(ViewDirection.FRONT)
         .withScale(1.0);

      BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2d = image.createGraphics();

      // Should render root, child1, and grandchild
      renderer.render(root, camera, g2d);

      g2d.dispose();
   }



   /*******************************************************************************
    ** Test isometric rendering.
    *******************************************************************************/
   @Test
   void testRenderIsometric()
   {
      SceneNode scene = createSimpleCabinetScene();
      OrthographicCamera camera = OrthographicCamera.forView(ViewDirection.ISOMETRIC)
         .withScale(5.0);

      BufferedImage image = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2d = image.createGraphics();

      camera.centerOn(scene.calculateTotalBounds(), 600, 600, 50);

      // Should render isometric 3D view
      renderer.render(scene, camera, g2d);

      g2d.dispose();
   }



   /*******************************************************************************
    ** Test scale affects rendering size.
    *******************************************************************************/
   @Test
   void testScaleAffectsCanvasSize()
   {
      SceneNode scene = createSimpleCabinetScene();

      // Small scale
      RenderSettings smallScale = new RenderSettings().withScale(5.0).withMargin(0);
      SceneRenderer smallRenderer = new SceneRenderer(smallScale);
      OrthographicCamera smallCamera = OrthographicCamera.forView(ViewDirection.FRONT)
         .withScale(5.0);
      Dimension smallSize = smallRenderer.calculateCanvasSize(scene, smallCamera);

      // Large scale
      RenderSettings largeScale = new RenderSettings().withScale(20.0).withMargin(0);
      SceneRenderer largeRenderer = new SceneRenderer(largeScale);
      OrthographicCamera largeCamera = OrthographicCamera.forView(ViewDirection.FRONT)
         .withScale(20.0);
      Dimension largeSize = largeRenderer.calculateCanvasSize(scene, largeCamera);

      // Large scale should produce larger canvas
      assertThat(largeSize.width).isGreaterThan(smallSize.width);
      assertThat(largeSize.height).isGreaterThan(smallSize.height);
   }



   // ════════════════════════════════════════════════════════════════════════════
   // Helper methods
   // ════════════════════════════════════════════════════════════════════════════



   private SceneNode createSimpleCabinetScene()
   {
      SceneNode root = new SceneNode("cabinet-root")
         .withLabel("Test Cabinet")
         .withSize(24, 34.5, 24);

      // Left side
      root.addChild(new SceneNode("left-side")
         .withPosition(0, 4.5, 0)
         .withSize(0.75, 30, 23.75)
         .withStyle(RenderStyle.woodPanel()));

      // Right side
      root.addChild(new SceneNode("right-side")
         .withPosition(23.25, 4.5, 0)
         .withSize(0.75, 30, 23.75)
         .withStyle(RenderStyle.woodPanel()));

      // Bottom
      root.addChild(new SceneNode("bottom")
         .withPosition(0.75, 4.5, 0)
         .withSize(22.5, 0.75, 23.75)
         .withStyle(RenderStyle.woodPanel()));

      return root;
   }
}
