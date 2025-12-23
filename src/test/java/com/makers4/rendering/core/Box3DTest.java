package com.makers4.rendering.core;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import org.junit.jupiter.api.Test;


/*******************************************************************************
 ** Unit tests for Box3D class.
 *******************************************************************************/
class Box3DTest
{

   /*******************************************************************************
    ** Test basic box creation and accessors.
    *******************************************************************************/
   @Test
   void testBasicBox()
   {
      Vector3D pos = new Vector3D(0.0, 0.0, 0.0);
      Vector3D size = new Vector3D(24.0, 30.0, 24.0);
      Box3D box = new Box3D(pos, size);

      assertThat(box.position()).isEqualTo(pos);
      assertThat(box.size()).isEqualTo(size);
      assertThat(box.width()).isCloseTo(24.0, within(0.001));
      assertThat(box.height()).isCloseTo(30.0, within(0.001));
      assertThat(box.depth()).isCloseTo(24.0, within(0.001));
   }



   /*******************************************************************************
    ** Test min and max corners.
    *******************************************************************************/
   @Test
   void testMinMaxCorners()
   {
      Box3D box = new Box3D(
         new Vector3D(1.0, 2.0, 3.0),
         new Vector3D(4.0, 5.0, 6.0)
      );

      Vector3D min = box.min();
      assertThat(min.x()).isCloseTo(1.0, within(0.001));
      assertThat(min.y()).isCloseTo(2.0, within(0.001));
      assertThat(min.z()).isCloseTo(3.0, within(0.001));

      Vector3D max = box.max();
      assertThat(max.x()).isCloseTo(5.0, within(0.001));  // 1 + 4
      assertThat(max.y()).isCloseTo(7.0, within(0.001));  // 2 + 5
      assertThat(max.z()).isCloseTo(9.0, within(0.001));  // 3 + 6
   }



   /*******************************************************************************
    ** Test center calculation.
    *******************************************************************************/
   @Test
   void testCenter()
   {
      Box3D box = new Box3D(
         new Vector3D(0.0, 0.0, 0.0),
         new Vector3D(10.0, 20.0, 30.0)
      );

      Vector3D center = box.center();
      assertThat(center.x()).isCloseTo(5.0, within(0.001));
      assertThat(center.y()).isCloseTo(10.0, within(0.001));
      assertThat(center.z()).isCloseTo(15.0, within(0.001));
   }



   /*******************************************************************************
    ** Test translation.
    *******************************************************************************/
   @Test
   void testTranslate()
   {
      Box3D box = new Box3D(
         new Vector3D(0.0, 0.0, 0.0),
         new Vector3D(10.0, 10.0, 10.0)
      );

      Box3D translated = box.translate(new Vector3D(5.0, 5.0, 5.0));

      assertThat(translated.position().x()).isCloseTo(5.0, within(0.001));
      assertThat(translated.position().y()).isCloseTo(5.0, within(0.001));
      assertThat(translated.position().z()).isCloseTo(5.0, within(0.001));

      // Size should remain unchanged
      assertThat(translated.width()).isCloseTo(10.0, within(0.001));
      assertThat(translated.height()).isCloseTo(10.0, within(0.001));
      assertThat(translated.depth()).isCloseTo(10.0, within(0.001));
   }



   /*******************************************************************************
    ** Test intersection detection.
    *******************************************************************************/
   @Test
   void testIntersects()
   {
      Box3D a = new Box3D(
         new Vector3D(0.0, 0.0, 0.0),
         new Vector3D(10.0, 10.0, 10.0)
      );

      Box3D b = new Box3D(
         new Vector3D(5.0, 5.0, 5.0),
         new Vector3D(10.0, 10.0, 10.0)
      );

      Box3D c = new Box3D(
         new Vector3D(20.0, 20.0, 20.0),
         new Vector3D(10.0, 10.0, 10.0)
      );

      assertThat(a.intersects(b)).isTrue();
      assertThat(a.intersects(c)).isFalse();
      assertThat(b.intersects(c)).isFalse();
   }



   /*******************************************************************************
    ** Test containment.
    *******************************************************************************/
   @Test
   void testContains()
   {
      Box3D box = new Box3D(
         new Vector3D(0.0, 0.0, 0.0),
         new Vector3D(10.0, 10.0, 10.0)
      );

      assertThat(box.contains(new Vector3D(5.0, 5.0, 5.0))).isTrue();   // Inside
      assertThat(box.contains(new Vector3D(0.0, 0.0, 0.0))).isTrue();   // On corner
      assertThat(box.contains(new Vector3D(10.0, 10.0, 10.0))).isTrue(); // On max corner
      assertThat(box.contains(new Vector3D(15.0, 5.0, 5.0))).isFalse();  // Outside
      assertThat(box.contains(new Vector3D(-1.0, 5.0, 5.0))).isFalse();  // Outside
   }



   /*******************************************************************************
    ** Test union of two boxes.
    *******************************************************************************/
   @Test
   void testUnion()
   {
      Box3D a = new Box3D(
         new Vector3D(0.0, 0.0, 0.0),
         new Vector3D(10.0, 10.0, 10.0)
      );

      Box3D b = new Box3D(
         new Vector3D(5.0, 5.0, 5.0),
         new Vector3D(10.0, 10.0, 10.0)
      );

      Box3D union = a.union(b);

      // Min corner should be at origin
      assertThat(union.position().x()).isCloseTo(0.0, within(0.001));
      assertThat(union.position().y()).isCloseTo(0.0, within(0.001));
      assertThat(union.position().z()).isCloseTo(0.0, within(0.001));

      // Max corner should be at (15, 15, 15)
      assertThat(union.max().x()).isCloseTo(15.0, within(0.001));
      assertThat(union.max().y()).isCloseTo(15.0, within(0.001));
      assertThat(union.max().z()).isCloseTo(15.0, within(0.001));
   }



   /*******************************************************************************
    ** Test cabinet panel dimensions.
    *******************************************************************************/
   @Test
   void testCabinetPanelDimensions()
   {
      // Left side panel of a 24"W x 34.5"H x 24"D base cabinet
      // Panel is 3/4" thick, starts at toe kick height (4.5"), goes full depth minus back (23.75")
      Box3D leftSide = new Box3D(
         new Vector3D(0.0, 4.5, 0.0),
         new Vector3D(0.75, 30.0, 23.75)
      );

      assertThat(leftSide.width()).isCloseTo(0.75, within(0.001));    // 3/4" thick
      assertThat(leftSide.height()).isCloseTo(30.0, within(0.001));   // 34.5" - 4.5" toe kick
      assertThat(leftSide.depth()).isCloseTo(23.75, within(0.001));   // 24" - 0.25" back panel
   }



   /*******************************************************************************
    ** Test immutability.
    *******************************************************************************/
   @Test
   void testImmutability()
   {
      Box3D original = new Box3D(
         new Vector3D(0.0, 0.0, 0.0),
         new Vector3D(10.0, 10.0, 10.0)
      );

      Box3D translated = original.translate(new Vector3D(5.0, 5.0, 5.0));

      // Original should be unchanged
      assertThat(original.position().x()).isCloseTo(0.0, within(0.001));
      assertThat(original.position().y()).isCloseTo(0.0, within(0.001));
      assertThat(original.position().z()).isCloseTo(0.0, within(0.001));

      // Translated should have new position
      assertThat(translated.position().x()).isCloseTo(5.0, within(0.001));
   }



   /*******************************************************************************
    ** Test atOrigin factory method.
    *******************************************************************************/
   @Test
   void testAtOriginFactory()
   {
      Box3D box = Box3D.atOrigin(10.0, 20.0, 30.0);

      assertThat(box.position().x()).isCloseTo(0.0, within(0.001));
      assertThat(box.position().y()).isCloseTo(0.0, within(0.001));
      assertThat(box.position().z()).isCloseTo(0.0, within(0.001));
      assertThat(box.width()).isCloseTo(10.0, within(0.001));
      assertThat(box.height()).isCloseTo(20.0, within(0.001));
      assertThat(box.depth()).isCloseTo(30.0, within(0.001));
   }



   /*******************************************************************************
    ** Test atOrigin factory method with Vector.
    *******************************************************************************/
   @Test
   void testAtOriginFactoryWithVector()
   {
      Vector3D size = new Vector3D(10.0, 20.0, 30.0);
      Box3D box = Box3D.atOrigin(size);

      assertThat(box.position().x()).isCloseTo(0.0, within(0.001));
      assertThat(box.size()).isEqualTo(size);
   }



   /*******************************************************************************
    ** Test of factory method.
    *******************************************************************************/
   @Test
   void testOfFactory()
   {
      Box3D box = Box3D.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);

      assertThat(box.position().x()).isCloseTo(1.0, within(0.001));
      assertThat(box.position().y()).isCloseTo(2.0, within(0.001));
      assertThat(box.position().z()).isCloseTo(3.0, within(0.001));
      assertThat(box.width()).isCloseTo(4.0, within(0.001));
      assertThat(box.height()).isCloseTo(5.0, within(0.001));
      assertThat(box.depth()).isCloseTo(6.0, within(0.001));
   }



   /*******************************************************************************
    ** Test format and toString.
    *******************************************************************************/
   @Test
   void testFormat()
   {
      Box3D box = Box3D.of(0.0, 0.0, 0.0, 10.0, 10.0, 10.0);
      assertThat(box.format()).contains("Box3D");
      assertThat(box.toString()).contains("Box3D");
   }
}
