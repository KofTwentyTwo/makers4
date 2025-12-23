package com.makers4.rendering.camera;


import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;


/*******************************************************************************
 ** Unit tests for ViewDirection enum.
 *******************************************************************************/
class ViewDirectionTest
{

   /*******************************************************************************
    ** Test all view directions have display names.
    *******************************************************************************/
   @Test
   void testDisplayNames()
   {
      assertThat(ViewDirection.FRONT.getDisplayName()).isEqualTo("Front View");
      assertThat(ViewDirection.BACK.getDisplayName()).isEqualTo("Back View");
      assertThat(ViewDirection.LEFT.getDisplayName()).isEqualTo("Left Side");
      assertThat(ViewDirection.RIGHT.getDisplayName()).isEqualTo("Right Side");
      assertThat(ViewDirection.TOP.getDisplayName()).isEqualTo("Top View");
      assertThat(ViewDirection.BOTTOM.getDisplayName()).isEqualTo("Bottom View");
      assertThat(ViewDirection.ISOMETRIC.getDisplayName()).isEqualTo("Isometric View");
   }



   /*******************************************************************************
    ** Test all view directions have descriptions.
    *******************************************************************************/
   @Test
   void testDescriptions()
   {
      for(ViewDirection direction : ViewDirection.values())
      {
         assertThat(direction.getDescription()).isNotNull();
         assertThat(direction.getDescription()).isNotEmpty();
      }
   }



   /*******************************************************************************
    ** Test front view axis mapping.
    *******************************************************************************/
   @Test
   void testFrontViewAxes()
   {
      ViewDirection front = ViewDirection.FRONT;

      // Front view: X horizontal, Y vertical
      assertThat(front.getHorizontalAxis()).isEqualTo(ViewDirection.Axis.X);
      assertThat(front.getVerticalAxis()).isEqualTo(ViewDirection.Axis.Y);
      assertThat(front.isMirrorHorizontal()).isFalse();
      assertThat(front.isMirrorVertical()).isFalse();
   }



   /*******************************************************************************
    ** Test back view axis mapping (mirrored horizontally).
    *******************************************************************************/
   @Test
   void testBackViewAxes()
   {
      ViewDirection back = ViewDirection.BACK;

      assertThat(back.getHorizontalAxis()).isEqualTo(ViewDirection.Axis.X);
      assertThat(back.getVerticalAxis()).isEqualTo(ViewDirection.Axis.Y);
      assertThat(back.isMirrorHorizontal()).isTrue();  // Mirrored!
      assertThat(back.isMirrorVertical()).isFalse();
   }



   /*******************************************************************************
    ** Test left view axis mapping.
    *******************************************************************************/
   @Test
   void testLeftViewAxes()
   {
      ViewDirection left = ViewDirection.LEFT;

      // Left view: Z horizontal, Y vertical
      assertThat(left.getHorizontalAxis()).isEqualTo(ViewDirection.Axis.Z);
      assertThat(left.getVerticalAxis()).isEqualTo(ViewDirection.Axis.Y);
      assertThat(left.isMirrorHorizontal()).isFalse();
   }



   /*******************************************************************************
    ** Test right view axis mapping (mirrored horizontally).
    *******************************************************************************/
   @Test
   void testRightViewAxes()
   {
      ViewDirection right = ViewDirection.RIGHT;

      assertThat(right.getHorizontalAxis()).isEqualTo(ViewDirection.Axis.Z);
      assertThat(right.getVerticalAxis()).isEqualTo(ViewDirection.Axis.Y);
      assertThat(right.isMirrorHorizontal()).isTrue();  // Mirrored!
   }



   /*******************************************************************************
    ** Test top view axis mapping (plan view).
    *******************************************************************************/
   @Test
   void testTopViewAxes()
   {
      ViewDirection top = ViewDirection.TOP;

      // Top view: X horizontal, Z vertical
      assertThat(top.getHorizontalAxis()).isEqualTo(ViewDirection.Axis.X);
      assertThat(top.getVerticalAxis()).isEqualTo(ViewDirection.Axis.Z);
      assertThat(top.isMirrorHorizontal()).isFalse();
      assertThat(top.isMirrorVertical()).isFalse();
   }



   /*******************************************************************************
    ** Test bottom view axis mapping (mirrored vertically).
    *******************************************************************************/
   @Test
   void testBottomViewAxes()
   {
      ViewDirection bottom = ViewDirection.BOTTOM;

      assertThat(bottom.getHorizontalAxis()).isEqualTo(ViewDirection.Axis.X);
      assertThat(bottom.getVerticalAxis()).isEqualTo(ViewDirection.Axis.Z);
      assertThat(bottom.isMirrorHorizontal()).isFalse();
      assertThat(bottom.isMirrorVertical()).isTrue();  // Mirrored!
   }



   /*******************************************************************************
    ** Test isometric detection.
    *******************************************************************************/
   @Test
   void testIsIsometric()
   {
      assertThat(ViewDirection.ISOMETRIC.isIsometric()).isTrue();
      assertThat(ViewDirection.FRONT.isIsometric()).isFalse();
      assertThat(ViewDirection.LEFT.isIsometric()).isFalse();
      assertThat(ViewDirection.TOP.isIsometric()).isFalse();
   }



   /*******************************************************************************
    ** Test isometric has null axes (uses special projection).
    *******************************************************************************/
   @Test
   void testIsometricNullAxes()
   {
      ViewDirection iso = ViewDirection.ISOMETRIC;

      // Isometric uses special 30-degree projection, not simple axis mapping
      assertThat(iso.getHorizontalAxis()).isNull();
      assertThat(iso.getVerticalAxis()).isNull();
   }



   /*******************************************************************************
    ** Test standard views for cabinet blueprints.
    *******************************************************************************/
   @Test
   void testStandardViews()
   {
      ViewDirection[] standard = ViewDirection.standardViews();

      assertThat(standard).hasSize(3);
      assertThat(standard).containsExactly(
         ViewDirection.FRONT,
         ViewDirection.LEFT,
         ViewDirection.TOP
      );
   }



   /*******************************************************************************
    ** Test elevation views (all side views).
    *******************************************************************************/
   @Test
   void testElevationViews()
   {
      ViewDirection[] elevations = ViewDirection.elevationViews();

      assertThat(elevations).hasSize(4);
      assertThat(elevations).containsExactly(
         ViewDirection.FRONT,
         ViewDirection.BACK,
         ViewDirection.LEFT,
         ViewDirection.RIGHT
      );
   }



   /*******************************************************************************
    ** Test orthographic views (all except isometric).
    *******************************************************************************/
   @Test
   void testOrthographicViews()
   {
      ViewDirection[] orthographic = ViewDirection.orthographicViews();

      assertThat(orthographic).hasSize(6);
      assertThat(orthographic).containsExactly(
         ViewDirection.FRONT,
         ViewDirection.BACK,
         ViewDirection.LEFT,
         ViewDirection.RIGHT,
         ViewDirection.TOP,
         ViewDirection.BOTTOM
      );
      assertThat(orthographic).doesNotContain(ViewDirection.ISOMETRIC);
   }
}
