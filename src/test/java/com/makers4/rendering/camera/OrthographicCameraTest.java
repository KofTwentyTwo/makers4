package com.makers4.rendering.camera;


import java.awt.geom.Point2D;
import com.makers4.rendering.core.Box3D;
import com.makers4.rendering.core.Vector3D;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import org.junit.jupiter.api.Test;


/*******************************************************************************
 ** Unit tests for OrthographicCamera class.
 *******************************************************************************/
class OrthographicCameraTest
{

   /*******************************************************************************
    ** Test creating camera for different views.
    *******************************************************************************/
   @Test
   void testForView()
   {
      OrthographicCamera frontCamera = OrthographicCamera.forView(ViewDirection.FRONT);
      assertThat(frontCamera.getDirection()).isEqualTo(ViewDirection.FRONT);
      assertThat(frontCamera.getScale()).isCloseTo(1.0, within(0.001));

      OrthographicCamera leftCamera = OrthographicCamera.forView(ViewDirection.LEFT);
      assertThat(leftCamera.getDirection()).isEqualTo(ViewDirection.LEFT);
   }



   /*******************************************************************************
    ** Test fluent setters.
    *******************************************************************************/
   @Test
   void testFluentSetters()
   {
      OrthographicCamera camera = OrthographicCamera.forView(ViewDirection.FRONT)
         .withScale(10.0)
         .withOffsetX(50.0)
         .withOffsetY(100.0);

      assertThat(camera.getScale()).isCloseTo(10.0, within(0.001));
      assertThat(camera.getOffsetX()).isCloseTo(50.0, within(0.001));
      assertThat(camera.getOffsetY()).isCloseTo(100.0, within(0.001));
   }



   /*******************************************************************************
    ** Test front view projection.
    *******************************************************************************/
   @Test
   void testFrontViewProjection()
   {
      OrthographicCamera camera = OrthographicCamera.forView(ViewDirection.FRONT)
         .withScale(1.0);

      // Front view: X -> screen X, Y -> screen Y
      Vector3D worldPoint = new Vector3D(5.0, 10.0, 15.0);
      Point2D screenPoint = camera.project(worldPoint);

      assertThat(screenPoint.getX()).isCloseTo(5.0, within(0.001));
      assertThat(screenPoint.getY()).isCloseTo(10.0, within(0.001));
   }



   /*******************************************************************************
    ** Test left view projection.
    *******************************************************************************/
   @Test
   void testLeftViewProjection()
   {
      OrthographicCamera camera = OrthographicCamera.forView(ViewDirection.LEFT)
         .withScale(1.0);

      // Left view: Z -> screen X, Y -> screen Y
      Vector3D worldPoint = new Vector3D(5.0, 10.0, 15.0);
      Point2D screenPoint = camera.project(worldPoint);

      assertThat(screenPoint.getX()).isCloseTo(15.0, within(0.001));  // Z
      assertThat(screenPoint.getY()).isCloseTo(10.0, within(0.001));  // Y
   }



   /*******************************************************************************
    ** Test top view projection.
    *******************************************************************************/
   @Test
   void testTopViewProjection()
   {
      OrthographicCamera camera = OrthographicCamera.forView(ViewDirection.TOP)
         .withScale(1.0);

      // Top view: X -> screen X, Z -> screen Y
      Vector3D worldPoint = new Vector3D(5.0, 10.0, 15.0);
      Point2D screenPoint = camera.project(worldPoint);

      assertThat(screenPoint.getX()).isCloseTo(5.0, within(0.001));   // X
      assertThat(screenPoint.getY()).isCloseTo(15.0, within(0.001));  // Z
   }



   /*******************************************************************************
    ** Test scale affects projection.
    *******************************************************************************/
   @Test
   void testScaleAffectsProjection()
   {
      OrthographicCamera camera = OrthographicCamera.forView(ViewDirection.FRONT)
         .withScale(10.0);

      Vector3D worldPoint = new Vector3D(5.0, 10.0, 0.0);
      Point2D screenPoint = camera.project(worldPoint);

      // Coordinates should be scaled by 10
      assertThat(screenPoint.getX()).isCloseTo(50.0, within(0.001));
      assertThat(screenPoint.getY()).isCloseTo(100.0, within(0.001));
   }



   /*******************************************************************************
    ** Test offset affects projection.
    *******************************************************************************/
   @Test
   void testOffsetAffectsProjection()
   {
      OrthographicCamera camera = OrthographicCamera.forView(ViewDirection.FRONT)
         .withScale(1.0)
         .withOffsetX(50.0)
         .withOffsetY(100.0);

      Vector3D worldPoint = new Vector3D(0.0, 0.0, 0.0);
      Point2D screenPoint = camera.project(worldPoint);

      // Origin should be at the offset
      assertThat(screenPoint.getX()).isCloseTo(50.0, within(0.001));
      assertThat(screenPoint.getY()).isCloseTo(100.0, within(0.001));
   }



   /*******************************************************************************
    ** Test projecting a box to 2D bounds.
    *******************************************************************************/
   @Test
   void testProjectBox()
   {
      OrthographicCamera camera = OrthographicCamera.forView(ViewDirection.FRONT)
         .withScale(10.0);

      Box3D box = new Box3D(
         new Vector3D(1.0, 2.0, 0.0),
         new Vector3D(5.0, 3.0, 10.0)
      );

      double[] projected = camera.projectBox(box);

      // x, y, width, height
      assertThat(projected[0]).isCloseTo(10.0, within(0.001));   // 1.0 * 10
      assertThat(projected[1]).isCloseTo(20.0, within(0.001));   // 2.0 * 10
      assertThat(projected[2]).isCloseTo(50.0, within(0.001));   // 5.0 * 10
      assertThat(projected[3]).isCloseTo(30.0, within(0.001));   // 3.0 * 10
   }



   /*******************************************************************************
    ** Test calculating projected size.
    *******************************************************************************/
   @Test
   void testCalculateProjectedSize()
   {
      OrthographicCamera camera = OrthographicCamera.forView(ViewDirection.FRONT)
         .withScale(10.0);

      Box3D box = new Box3D(
         new Vector3D(0.0, 0.0, 0.0),
         new Vector3D(24.0, 34.5, 24.0)  // Standard base cabinet
      );

      double[] size = camera.calculateProjectedSize(box);

      // Width (X) and Height (Y) in front view
      assertThat(size[0]).isCloseTo(240.0, within(0.001));  // 24.0 * 10
      assertThat(size[1]).isCloseTo(345.0, within(0.001));  // 34.5 * 10
   }



   /*******************************************************************************
    ** Test calculating projected size for different views.
    *******************************************************************************/
   @Test
   void testProjectedSizeDifferentViews()
   {
      Box3D box = new Box3D(
         new Vector3D(0.0, 0.0, 0.0),
         new Vector3D(24.0, 34.5, 12.0)  // 24"W x 34.5"H x 12"D
      );

      // Front view: shows width and height
      double[] frontSize = OrthographicCamera.forView(ViewDirection.FRONT)
         .withScale(1.0).calculateProjectedSize(box);
      assertThat(frontSize[0]).isCloseTo(24.0, within(0.001));  // Width
      assertThat(frontSize[1]).isCloseTo(34.5, within(0.001));  // Height

      // Left view: shows depth and height
      double[] leftSize = OrthographicCamera.forView(ViewDirection.LEFT)
         .withScale(1.0).calculateProjectedSize(box);
      assertThat(leftSize[0]).isCloseTo(12.0, within(0.001));   // Depth
      assertThat(leftSize[1]).isCloseTo(34.5, within(0.001));   // Height

      // Top view: shows width and depth
      double[] topSize = OrthographicCamera.forView(ViewDirection.TOP)
         .withScale(1.0).calculateProjectedSize(box);
      assertThat(topSize[0]).isCloseTo(24.0, within(0.001));    // Width
      assertThat(topSize[1]).isCloseTo(12.0, within(0.001));    // Depth
   }



   /*******************************************************************************
    ** Test auto-scale calculation.
    *******************************************************************************/
   @Test
   void testCalculateAutoScale()
   {
      OrthographicCamera camera = OrthographicCamera.forView(ViewDirection.FRONT)
         .withScale(1.0);

      Box3D box = new Box3D(
         new Vector3D(0.0, 0.0, 0.0),
         new Vector3D(24.0, 34.5, 24.0)
      );

      // Screen 400x500 with 50 margin each side = 300x400 usable
      double autoScale = camera.calculateAutoScale(box, 400.0, 500.0, 50.0);

      // Should fit both dimensions: min(300/24, 400/34.5) = min(12.5, 11.6) = 11.6
      assertThat(autoScale).isCloseTo(11.594, within(0.01));
   }



   /*******************************************************************************
    ** Test centering content.
    *******************************************************************************/
   @Test
   void testCenterOn()
   {
      OrthographicCamera camera = OrthographicCamera.forView(ViewDirection.FRONT)
         .withScale(10.0);

      Box3D box = new Box3D(
         new Vector3D(0.0, 0.0, 0.0),
         new Vector3D(20.0, 30.0, 20.0)
      );

      camera.centerOn(box, 400.0, 500.0, 50.0);

      // Content is 200x300 at scale 10, screen is 400x500
      // Offset should center: (400-200)/2=100, (500-300)/2=100
      assertThat(camera.getOffsetX()).isCloseTo(100.0, within(0.001));
      assertThat(camera.getOffsetY()).isCloseTo(100.0, within(0.001));
   }



   /*******************************************************************************
    ** Test changing direction.
    *******************************************************************************/
   @Test
   void testWithDirection()
   {
      OrthographicCamera camera = OrthographicCamera.forView(ViewDirection.FRONT);

      camera.withDirection(ViewDirection.LEFT);

      assertThat(camera.getDirection()).isEqualTo(ViewDirection.LEFT);
   }



   /*******************************************************************************
    ** Test isometric projection basics.
    *******************************************************************************/
   @Test
   void testIsometricProjection()
   {
      OrthographicCamera camera = OrthographicCamera.forView(ViewDirection.ISOMETRIC)
         .withScale(1.0);

      // Isometric uses 30-degree angles
      // For point at origin, screen position should be at origin (before offset)
      Vector3D origin = new Vector3D(0.0, 0.0, 0.0);
      Point2D screenOrigin = camera.project(origin);

      assertThat(screenOrigin.getX()).isCloseTo(0.0, within(0.001));
      assertThat(screenOrigin.getY()).isCloseTo(0.0, within(0.001));
   }
}
