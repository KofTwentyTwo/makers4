package com.makers4.rendering.core;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import org.junit.jupiter.api.Test;


/*******************************************************************************
 ** Unit tests for Dimension class.
 *******************************************************************************/
class DimensionTest
{

   /*******************************************************************************
    ** Test creating dimension from millimeters.
    *******************************************************************************/
   @Test
   void testFromMillimeters()
   {
      Dimension dim = Dimension.mm(25.4);
      assertThat(dim.toMillimetersDouble()).isCloseTo(25.4, within(0.001));
      assertThat(dim.toInchesDouble()).isCloseTo(1.0, within(0.001));
   }



   /*******************************************************************************
    ** Test creating dimension from inches.
    *******************************************************************************/
   @Test
   void testFromInches()
   {
      Dimension dim = Dimension.inches(1.0);
      assertThat(dim.toInchesDouble()).isCloseTo(1.0, within(0.001));
      assertThat(dim.toMillimetersDouble()).isCloseTo(25.4, within(0.001));
   }



   /*******************************************************************************
    ** Test creating dimension from centimeters.
    *******************************************************************************/
   @Test
   void testFromCentimeters()
   {
      Dimension dim = Dimension.cm(2.54);
      assertThat(dim.toInchesDouble()).isCloseTo(1.0, within(0.001));
      assertThat(dim.toMillimetersDouble()).isCloseTo(25.4, within(0.001));
   }



   /*******************************************************************************
    ** Test dimension arithmetic operations.
    *******************************************************************************/
   @Test
   void testArithmetic()
   {
      Dimension a = Dimension.inches(2.0);
      Dimension b = Dimension.inches(1.5);

      assertThat(a.add(b).toInchesDouble()).isCloseTo(3.5, within(0.001));
      assertThat(a.subtract(b).toInchesDouble()).isCloseTo(0.5, within(0.001));
   }



   /*******************************************************************************
    ** Test fractional formatting with standard woodworking fractions.
    *******************************************************************************/
   @Test
   void testFractionalFormatting()
   {
      assertThat(Dimension.inches(1.0).formatFractional()).isEqualTo("1\"");
      assertThat(Dimension.inches(0.5).formatFractional()).isEqualTo("1/2\"");
      assertThat(Dimension.inches(0.25).formatFractional()).isEqualTo("1/4\"");
      assertThat(Dimension.inches(0.75).formatFractional()).isEqualTo("3/4\"");
      assertThat(Dimension.inches(1.5).formatFractional()).isEqualTo("1 1/2\"");
      assertThat(Dimension.inches(12.75).formatFractional()).isEqualTo("12 3/4\"");
   }



   /*******************************************************************************
    ** Test zero dimension.
    *******************************************************************************/
   @Test
   void testZero()
   {
      Dimension zero = Dimension.mm(0);
      assertThat(zero.toMillimetersDouble()).isCloseTo(0.0, within(0.001));
      assertThat(zero.toInchesDouble()).isCloseTo(0.0, within(0.001));
   }



   /*******************************************************************************
    ** Test equality.
    *******************************************************************************/
   @Test
   void testEquality()
   {
      Dimension a = Dimension.inches(1.0);
      Dimension b = Dimension.mm(25.4);

      // Same value created differently
      assertThat(a.toMillimetersDouble()).isCloseTo(b.toMillimetersDouble(), within(0.001));
   }



   /*******************************************************************************
    ** Test standard cabinet dimensions.
    *******************************************************************************/
   @Test
   void testCabinetDimensions()
   {
      // Standard base cabinet height (34.5")
      Dimension baseHeight = Dimension.inches(34.5);
      assertThat(baseHeight.toMillimetersDouble()).isCloseTo(876.3, within(0.1));

      // Standard toe kick height (4.5")
      Dimension toeKick = Dimension.inches(4.5);
      assertThat(toeKick.toMillimetersDouble()).isCloseTo(114.3, within(0.1));

      // Standard plywood thickness (3/4")
      Dimension plywood = Dimension.inches(0.75);
      assertThat(plywood.toMillimetersDouble()).isCloseTo(19.05, within(0.1));
   }



   /*******************************************************************************
    ** Test format method.
    *******************************************************************************/
   @Test
   void testFormat()
   {
      Dimension dim = Dimension.inches(24);
      assertThat(dim.format()).isEqualTo("24 in");

      Dimension mmDim = Dimension.mm(600);
      assertThat(mmDim.format()).isEqualTo("600 mm");
   }



   /*******************************************************************************
    ** Test toInches and toMillimeters BigDecimal methods.
    *******************************************************************************/
   @Test
   void testBigDecimalConversions()
   {
      Dimension dim = Dimension.inches(2.0);
      assertThat(dim.toInches().doubleValue()).isCloseTo(2.0, within(0.001));
      assertThat(dim.toMillimeters().doubleValue()).isCloseTo(50.8, within(0.001));
   }



   /*******************************************************************************
    ** Test unit getters.
    *******************************************************************************/
   @Test
   void testUnitGetters()
   {
      assertThat(Dimension.Unit.INCHES.getSymbol()).isEqualTo("in");
      assertThat(Dimension.Unit.MILLIMETERS.getSymbol()).isEqualTo("mm");
      assertThat(Dimension.Unit.CENTIMETERS.getSymbol()).isEqualTo("cm");

      assertThat(Dimension.Unit.INCHES.getToInchFactor()).isCloseTo(1.0, within(0.001));
      assertThat(Dimension.Unit.MILLIMETERS.getToInchFactor()).isCloseTo(25.4, within(0.001));
   }
}
