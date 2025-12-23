package com.makers4.rendering.scene;


import java.awt.Color;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import org.junit.jupiter.api.Test;


/*******************************************************************************
 ** Unit tests for RenderStyle class.
 *******************************************************************************/
class RenderStyleTest
{

   /*******************************************************************************
    ** Test default constructor values.
    *******************************************************************************/
   @Test
   void testDefaultValues()
   {
      RenderStyle style = new RenderStyle();

      // Default has light gray fill
      assertThat(style.getFillColor()).isEqualTo(new Color(245, 245, 245));
      assertThat(style.getStrokeColor()).isEqualTo(Color.BLACK);
      assertThat(style.getStrokeWidth()).isCloseTo(1.0f, within(0.01f));
      assertThat(style.isShowLabel()).isTrue();
      assertThat(style.getLabelColor()).isEqualTo(Color.BLACK);
      assertThat(style.getLabelFontSize()).isCloseTo(8.0f, within(0.01f));
   }



   /*******************************************************************************
    ** Test fluent setters.
    *******************************************************************************/
   @Test
   void testFluentSetters()
   {
      RenderStyle style = new RenderStyle()
         .withFillColor(Color.BLUE)
         .withStrokeColor(Color.RED)
         .withStrokeWidth(2.5f)
         .withShowLabel(false)
         .withLabelColor(Color.WHITE)
         .withLabelFontSize(12.0f);

      assertThat(style.getFillColor()).isEqualTo(Color.BLUE);
      assertThat(style.getStrokeColor()).isEqualTo(Color.RED);
      assertThat(style.getStrokeWidth()).isCloseTo(2.5f, within(0.01f));
      assertThat(style.isShowLabel()).isFalse();
      assertThat(style.getLabelColor()).isEqualTo(Color.WHITE);
      assertThat(style.getLabelFontSize()).isCloseTo(12.0f, within(0.01f));
   }



   /*******************************************************************************
    ** Test outline preset.
    *******************************************************************************/
   @Test
   void testOutlinePreset()
   {
      RenderStyle style = RenderStyle.outline();

      assertThat(style.getFillColor()).isNull();
      assertThat(style.getStrokeColor()).isEqualTo(Color.BLACK);
      assertThat(style.isShowLabel()).isTrue();
   }



   /*******************************************************************************
    ** Test woodPanel preset.
    *******************************************************************************/
   @Test
   void testWoodPanelPreset()
   {
      RenderStyle style = RenderStyle.woodPanel();

      // Should have a light tan/wood fill color
      assertThat(style.getFillColor()).isNotNull();
      assertThat(style.getStrokeColor()).isNotNull();
      assertThat(style.isShowLabel()).isTrue();
   }



   /*******************************************************************************
    ** Test blueprint preset.
    *******************************************************************************/
   @Test
   void testBlueprintPreset()
   {
      RenderStyle style = RenderStyle.blueprint();

      // Blueprint style has no fill (outlines only)
      assertThat(style.getFillColor()).isNull();
      assertThat(style.getStrokeColor()).isNotNull();
      assertThat(style.isShowLabel()).isTrue();
   }



   /*******************************************************************************
    ** Test creating custom colors.
    *******************************************************************************/
   @Test
   void testCustomColors()
   {
      Color customFill = new Color(245, 222, 179);  // Wheat color
      Color customStroke = new Color(139, 90, 43);  // Dark wood

      RenderStyle style = new RenderStyle()
         .withFillColor(customFill)
         .withStrokeColor(customStroke);

      assertThat(style.getFillColor()).isEqualTo(customFill);
      assertThat(style.getStrokeColor()).isEqualTo(customStroke);
   }



   /*******************************************************************************
    ** Test stroke width variations.
    *******************************************************************************/
   @Test
   void testStrokeWidthVariations()
   {
      RenderStyle thin = new RenderStyle().withStrokeWidth(0.5f);
      RenderStyle thick = new RenderStyle().withStrokeWidth(3.0f);

      assertThat(thin.getStrokeWidth()).isCloseTo(0.5f, within(0.01f));
      assertThat(thick.getStrokeWidth()).isCloseTo(3.0f, within(0.01f));
   }



   /*******************************************************************************
    ** Test label customization.
    *******************************************************************************/
   @Test
   void testLabelCustomization()
   {
      RenderStyle style = new RenderStyle()
         .withShowLabel(true)
         .withLabelColor(Color.BLUE)
         .withLabelFontSize(14.0f);

      assertThat(style.isShowLabel()).isTrue();
      assertThat(style.getLabelColor()).isEqualTo(Color.BLUE);
      assertThat(style.getLabelFontSize()).isCloseTo(14.0f, within(0.01f));
   }



   /*******************************************************************************
    ** Test disabling labels.
    *******************************************************************************/
   @Test
   void testDisableLabels()
   {
      RenderStyle style = new RenderStyle().withShowLabel(false);

      assertThat(style.isShowLabel()).isFalse();
   }
}
