package com.makers4.rendering;


import java.awt.Color;
import java.awt.Font;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import org.junit.jupiter.api.Test;


/*******************************************************************************
 ** Unit tests for RenderSettings class.
 *******************************************************************************/
class RenderSettingsTest
{

   /*******************************************************************************
    ** Test default values.
    *******************************************************************************/
   @Test
   void testDefaultValues()
   {
      RenderSettings settings = new RenderSettings();

      assertThat(settings.getScale()).isCloseTo(10.0, within(0.001));
      assertThat(settings.getMarginInches()).isCloseTo(1.0, within(0.001));
      assertThat(settings.getOutlineColor()).isEqualTo(Color.BLACK);
      assertThat(settings.getBackgroundColor()).isEqualTo(Color.WHITE);
      assertThat(settings.isShowDimensions()).isTrue();
      assertThat(settings.isShowPartLabels()).isTrue();
      assertThat(settings.isShowTitleBlock()).isTrue();
      assertThat(settings.isShowGrid()).isFalse();
   }



   /*******************************************************************************
    ** Test fluent setters.
    *******************************************************************************/
   @Test
   void testFluentSetters()
   {
      RenderSettings settings = new RenderSettings()
         .withScale(15.0)
         .withMargin(1.5)
         .withOutlineColor(Color.BLUE)
         .withFillColor(Color.LIGHT_GRAY)
         .withBackgroundColor(Color.WHITE)
         .withShowDimensions(false)
         .withShowPartLabels(false)
         .withShowTitleBlock(false)
         .withShowGrid(true);

      assertThat(settings.getScale()).isCloseTo(15.0, within(0.001));
      assertThat(settings.getMarginInches()).isCloseTo(1.5, within(0.001));
      assertThat(settings.getOutlineColor()).isEqualTo(Color.BLUE);
      assertThat(settings.getFillColor()).isEqualTo(Color.LIGHT_GRAY);
      assertThat(settings.isShowDimensions()).isFalse();
      assertThat(settings.isShowPartLabels()).isFalse();
      assertThat(settings.isShowTitleBlock()).isFalse();
      assertThat(settings.isShowGrid()).isTrue();
   }



   /*******************************************************************************
    ** Test blueprint preset.
    *******************************************************************************/
   @Test
   void testBlueprintPreset()
   {
      RenderSettings settings = RenderSettings.blueprint();

      assertThat(settings.getScale()).isCloseTo(15.0, within(0.001));
      assertThat(settings.getMarginInches()).isCloseTo(1.5, within(0.001));
      assertThat(settings.isShowDimensions()).isTrue();
      assertThat(settings.isShowPartLabels()).isTrue();
      assertThat(settings.isShowTitleBlock()).isTrue();
      assertThat(settings.isShowGrid()).isTrue();
   }



   /*******************************************************************************
    ** Test preview preset.
    *******************************************************************************/
   @Test
   void testPreviewPreset()
   {
      RenderSettings settings = RenderSettings.preview();

      assertThat(settings.getScale()).isCloseTo(8.0, within(0.001));
      assertThat(settings.getMarginInches()).isCloseTo(0.5, within(0.001));
      assertThat(settings.isShowDimensions()).isFalse();
      assertThat(settings.isShowPartLabels()).isFalse();
      assertThat(settings.isShowTitleBlock()).isFalse();
      assertThat(settings.isShowGrid()).isFalse();
   }



   /*******************************************************************************
    ** Test CAD export preset.
    *******************************************************************************/
   @Test
   void testCadExportPreset()
   {
      RenderSettings settings = RenderSettings.cadExport();

      assertThat(settings.getScale()).isCloseTo(1.0, within(0.001));
      assertThat(settings.getMarginInches()).isCloseTo(0.0, within(0.001));
      assertThat(settings.getFillColor()).isNull();  // No fill for clean lines
      assertThat(settings.isShowDimensions()).isTrue();
      assertThat(settings.isShowPartLabels()).isFalse();
   }



   /*******************************************************************************
    ** Test architectural blueprint preset.
    *******************************************************************************/
   @Test
   void testArchitecturalBlueprintPreset()
   {
      RenderSettings settings = RenderSettings.architecturalBlueprint();

      assertThat(settings.isUseFixedPageSize()).isTrue();
      assertThat(settings.getPageWidthPoints()).isCloseTo(792f, within(0.1f));   // 11"
      assertThat(settings.getPageHeightPoints()).isCloseTo(612f, within(0.1f)); // 8.5"
      assertThat(settings.isShowArchitecturalBorder()).isTrue();
      assertThat(settings.isShowGridReferences()).isTrue();
      assertThat(settings.isUseTraditionalTitleBlock()).isTrue();
      assertThat(settings.getFillColor()).isNull();  // Clean lines
   }



   /*******************************************************************************
    ** Test toPoints conversion.
    *******************************************************************************/
   @Test
   void testToPointsConversion()
   {
      RenderSettings settings = new RenderSettings().withScale(10.0);

      // 1 inch at scale 10 = 10 points
      assertThat(settings.toPoints(1.0)).isCloseTo(10.0, within(0.001));

      // 2.5 inches at scale 10 = 25 points
      assertThat(settings.toPoints(2.5)).isCloseTo(25.0, within(0.001));
   }



   /*******************************************************************************
    ** Test margin in points.
    *******************************************************************************/
   @Test
   void testMarginPoints()
   {
      RenderSettings settings = new RenderSettings()
         .withScale(10.0)
         .withMargin(1.5);

      // 1.5 inches at scale 10 = 15 points
      assertThat(settings.getMarginPoints()).isCloseTo(15.0, within(0.001));
   }



   /*******************************************************************************
    ** Test stroke width settings.
    *******************************************************************************/
   @Test
   void testStrokeWidthSettings()
   {
      RenderSettings settings = new RenderSettings()
         .withOutlineStrokeWidth(2.0f)
         .withDimensionLineStrokeWidth(0.5f);

      assertThat(settings.getOutlineStrokeWidth()).isCloseTo(2.0f, within(0.01f));
      assertThat(settings.getDimensionLineStrokeWidth()).isCloseTo(0.5f, within(0.01f));
   }



   /*******************************************************************************
    ** Test font settings.
    *******************************************************************************/
   @Test
   void testFontSettings()
   {
      Font customDimFont = new Font("Arial", Font.BOLD, 12);
      Font customLabelFont = new Font("Arial", Font.PLAIN, 10);

      RenderSettings settings = new RenderSettings()
         .withDimensionFont(customDimFont)
         .withLabelFont(customLabelFont);

      assertThat(settings.getDimensionFont()).isEqualTo(customDimFont);
      assertThat(settings.getLabelFont()).isEqualTo(customLabelFont);
   }



   /*******************************************************************************
    ** Test title block settings.
    *******************************************************************************/
   @Test
   void testTitleBlockSettings()
   {
      RenderSettings settings = new RenderSettings()
         .withProjectName("Kitchen Remodel")
         .withDrawnBy("John Doe")
         .withCompanyName("Cabinet Shop Inc")
         .withDate("2024-01-15")
         .withScaleNotation("1\" = 1'")
         .withCheckedBy("Jane Smith")
         .withRevision("A", "Initial release")
         .withSheetInfo("1", "3");

      assertThat(settings.getProjectName()).isEqualTo("Kitchen Remodel");
      assertThat(settings.getDrawnBy()).isEqualTo("John Doe");
      assertThat(settings.getCompanyName()).isEqualTo("Cabinet Shop Inc");
      assertThat(settings.getDate()).isEqualTo("2024-01-15");
      assertThat(settings.getScaleNotation()).isEqualTo("1\" = 1'");
      assertThat(settings.getCheckedBy()).isEqualTo("Jane Smith");
      assertThat(settings.getRevisionNumber()).isEqualTo("A");
      assertThat(settings.getRevisionDescription()).isEqualTo("Initial release");
      assertThat(settings.getSheetNumber()).isEqualTo("1");
      assertThat(settings.getTotalSheets()).isEqualTo("3");
   }



   /*******************************************************************************
    ** Test fixed page size settings.
    *******************************************************************************/
   @Test
   void testFixedPageSize()
   {
      RenderSettings settings = new RenderSettings()
         .withFixedPageSize(612f, 792f);  // 8.5x11 portrait

      assertThat(settings.isUseFixedPageSize()).isTrue();
      assertThat(settings.getPageWidthPoints()).isCloseTo(612f, within(0.1f));
      assertThat(settings.getPageHeightPoints()).isCloseTo(792f, within(0.1f));
   }



   /*******************************************************************************
    ** Test grid settings.
    *******************************************************************************/
   @Test
   void testGridSettings()
   {
      RenderSettings settings = new RenderSettings()
         .withShowGrid(true)
         .withGridSpacing(0.5)
         .withGridDivisions(10, 8);

      assertThat(settings.isShowGrid()).isTrue();
      assertThat(settings.getGridSpacingInches()).isCloseTo(0.5, within(0.001));
      assertThat(settings.getHorizontalGridDivisions()).isEqualTo(10);
      assertThat(settings.getVerticalGridDivisions()).isEqualTo(8);
   }



   /*******************************************************************************
    ** Test dimension offset setting.
    *******************************************************************************/
   @Test
   void testDimensionOffset()
   {
      RenderSettings settings = new RenderSettings()
         .withDimensionOffset(0.75);

      assertThat(settings.getDimensionOffset()).isCloseTo(0.75, within(0.001));
   }



   /*******************************************************************************
    ** Test stroke objects are created correctly.
    *******************************************************************************/
   @Test
   void testGetStrokeObjects()
   {
      RenderSettings settings = new RenderSettings();

      assertThat(settings.getOutlineStroke()).isNotNull();
      assertThat(settings.getDimensionStroke()).isNotNull();
      assertThat(settings.getGridStroke()).isNotNull();
   }
}
