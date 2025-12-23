package com.makers4.rendering.core;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import org.junit.jupiter.api.Test;


/*******************************************************************************
 ** Unit tests for Vector3D class.
 *******************************************************************************/
class Vector3DTest
{

   /*******************************************************************************
    ** Test origin constant.
    *******************************************************************************/
   @Test
   void testOrigin()
   {
      Vector3D origin = Vector3D.origin();
      assertThat(origin.x()).isCloseTo(0.0, within(0.001));
      assertThat(origin.y()).isCloseTo(0.0, within(0.001));
      assertThat(origin.z()).isCloseTo(0.0, within(0.001));
   }



   /*******************************************************************************
    ** Test creating vector with components.
    *******************************************************************************/
   @Test
   void testComponents()
   {
      Vector3D vec = new Vector3D(1.0, 2.0, 3.0);
      assertThat(vec.x()).isCloseTo(1.0, within(0.001));
      assertThat(vec.y()).isCloseTo(2.0, within(0.001));
      assertThat(vec.z()).isCloseTo(3.0, within(0.001));
   }



   /*******************************************************************************
    ** Test creating from inches.
    *******************************************************************************/
   @Test
   void testFromInches()
   {
      Vector3D vec = Vector3D.inches(24, 30, 24);
      assertThat(vec.x()).isCloseTo(24.0, within(0.001));
      assertThat(vec.y()).isCloseTo(30.0, within(0.001));
      assertThat(vec.z()).isCloseTo(24.0, within(0.001));
   }



   /*******************************************************************************
    ** Test creating from millimeters.
    *******************************************************************************/
   @Test
   void testFromMm()
   {
      Vector3D vec = Vector3D.mm(25.4, 50.8, 76.2);
      assertThat(vec.x()).isCloseTo(1.0, within(0.001));  // 25.4mm = 1 inch
      assertThat(vec.y()).isCloseTo(2.0, within(0.001));  // 50.8mm = 2 inches
      assertThat(vec.z()).isCloseTo(3.0, within(0.001));  // 76.2mm = 3 inches
   }



   /*******************************************************************************
    ** Test creating from mm with Integer values.
    *******************************************************************************/
   @Test
   void testFromMmIntegers()
   {
      Vector3D vec = Vector3D.mm(Integer.valueOf(610), Integer.valueOf(876), Integer.valueOf(610));
      assertThat(vec.x()).isCloseTo(24.0, within(0.1));
      assertThat(vec.y()).isCloseTo(34.5, within(0.1));
      assertThat(vec.z()).isCloseTo(24.0, within(0.1));
   }



   /*******************************************************************************
    ** Test vector addition.
    *******************************************************************************/
   @Test
   void testAdd()
   {
      Vector3D a = new Vector3D(1.0, 2.0, 3.0);
      Vector3D b = new Vector3D(4.0, 5.0, 6.0);
      Vector3D result = a.add(b);

      assertThat(result.x()).isCloseTo(5.0, within(0.001));
      assertThat(result.y()).isCloseTo(7.0, within(0.001));
      assertThat(result.z()).isCloseTo(9.0, within(0.001));
   }



   /*******************************************************************************
    ** Test vector subtraction.
    *******************************************************************************/
   @Test
   void testSubtract()
   {
      Vector3D a = new Vector3D(5.0, 7.0, 9.0);
      Vector3D b = new Vector3D(1.0, 2.0, 3.0);
      Vector3D result = a.subtract(b);

      assertThat(result.x()).isCloseTo(4.0, within(0.001));
      assertThat(result.y()).isCloseTo(5.0, within(0.001));
      assertThat(result.z()).isCloseTo(6.0, within(0.001));
   }



   /*******************************************************************************
    ** Test scalar multiplication.
    *******************************************************************************/
   @Test
   void testScale()
   {
      Vector3D vec = new Vector3D(2.0, 3.0, 4.0);
      Vector3D result = vec.scale(2.0);

      assertThat(result.x()).isCloseTo(4.0, within(0.001));
      assertThat(result.y()).isCloseTo(6.0, within(0.001));
      assertThat(result.z()).isCloseTo(8.0, within(0.001));
   }



   /*******************************************************************************
    ** Test length calculation.
    *******************************************************************************/
   @Test
   void testLength()
   {
      Vector3D vec = new Vector3D(3.0, 4.0, 0.0);
      assertThat(vec.length()).isCloseTo(5.0, within(0.001));

      Vector3D unitX = new Vector3D(1.0, 0.0, 0.0);
      assertThat(unitX.length()).isCloseTo(1.0, within(0.001));
   }



   /*******************************************************************************
    ** Test normalization.
    *******************************************************************************/
   @Test
   void testNormalize()
   {
      Vector3D vec = new Vector3D(3.0, 4.0, 0.0);
      Vector3D normalized = vec.normalize();

      assertThat(normalized.length()).isCloseTo(1.0, within(0.001));
      assertThat(normalized.x()).isCloseTo(0.6, within(0.001));
      assertThat(normalized.y()).isCloseTo(0.8, within(0.001));
   }



   /*******************************************************************************
    ** Test normalize of zero vector.
    *******************************************************************************/
   @Test
   void testNormalizeZeroVector()
   {
      Vector3D zero = Vector3D.origin();
      Vector3D normalized = zero.normalize();

      assertThat(normalized.x()).isCloseTo(0.0, within(0.001));
      assertThat(normalized.y()).isCloseTo(0.0, within(0.001));
      assertThat(normalized.z()).isCloseTo(0.0, within(0.001));
   }



   /*******************************************************************************
    ** Test dot product.
    *******************************************************************************/
   @Test
   void testDot()
   {
      Vector3D a = new Vector3D(1.0, 0.0, 0.0);
      Vector3D b = new Vector3D(0.0, 1.0, 0.0);
      assertThat(a.dot(b)).isCloseTo(0.0, within(0.001));  // Perpendicular

      Vector3D c = new Vector3D(1.0, 2.0, 3.0);
      Vector3D d = new Vector3D(4.0, 5.0, 6.0);
      assertThat(c.dot(d)).isCloseTo(32.0, within(0.001));  // 1*4 + 2*5 + 3*6 = 32
   }



   /*******************************************************************************
    ** Test cross product.
    *******************************************************************************/
   @Test
   void testCross()
   {
      Vector3D x = new Vector3D(1.0, 0.0, 0.0);
      Vector3D y = new Vector3D(0.0, 1.0, 0.0);
      Vector3D z = x.cross(y);

      assertThat(z.x()).isCloseTo(0.0, within(0.001));
      assertThat(z.y()).isCloseTo(0.0, within(0.001));
      assertThat(z.z()).isCloseTo(1.0, within(0.001));
   }



   /*******************************************************************************
    ** Test minComponent and maxComponent.
    *******************************************************************************/
   @Test
   void testMinMaxComponent()
   {
      Vector3D vec = new Vector3D(3.0, 1.0, 2.0);
      assertThat(vec.minComponent()).isCloseTo(1.0, within(0.001));
      assertThat(vec.maxComponent()).isCloseTo(3.0, within(0.001));
   }



   /*******************************************************************************
    ** Test format and toString.
    *******************************************************************************/
   @Test
   void testFormat()
   {
      Vector3D vec = new Vector3D(1.0, 2.0, 3.0);
      assertThat(vec.format()).isEqualTo("(1.000, 2.000, 3.000)");
      assertThat(vec.toString()).isEqualTo("(1.000, 2.000, 3.000)");
   }



   /*******************************************************************************
    ** Test immutability.
    *******************************************************************************/
   @Test
   void testImmutability()
   {
      Vector3D original = new Vector3D(1.0, 2.0, 3.0);
      Vector3D added = original.add(new Vector3D(1.0, 1.0, 1.0));

      // Original should be unchanged
      assertThat(original.x()).isCloseTo(1.0, within(0.001));
      assertThat(original.y()).isCloseTo(2.0, within(0.001));
      assertThat(original.z()).isCloseTo(3.0, within(0.001));

      // New vector should have the result
      assertThat(added.x()).isCloseTo(2.0, within(0.001));
      assertThat(added.y()).isCloseTo(3.0, within(0.001));
      assertThat(added.z()).isCloseTo(4.0, within(0.001));
   }
}
