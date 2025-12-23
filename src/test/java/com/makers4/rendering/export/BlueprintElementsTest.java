package com.makers4.rendering.export;


import java.awt.geom.Rectangle2D;
import com.makers4.rendering.RenderSettings;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/*******************************************************************************
 ** Unit tests for BlueprintElements class.
 *******************************************************************************/
class BlueprintElementsTest
{
   private RenderSettings settings;
   private BlueprintElements blueprint;



   @BeforeEach
   void setUp()
   {
      settings = RenderSettings.architecturalBlueprint();
      blueprint = new BlueprintElements(settings);
   }



   /*******************************************************************************
    ** Test page dimension constants.
    *******************************************************************************/
   @Test
   void testPageDimensionConstants()
   {
      // 11" x 8.5" landscape at 72 points/inch
      assertThat(BlueprintElements.PAGE_WIDTH).isCloseTo(792f, within(0.1f));   // 11 * 72
      assertThat(BlueprintElements.PAGE_HEIGHT).isCloseTo(612f, within(0.1f)); // 8.5 * 72
   }



   /*******************************************************************************
    ** Test margin constants.
    *******************************************************************************/
   @Test
   void testMarginConstants()
   {
      assertThat(BlueprintElements.OUTER_MARGIN).isCloseTo(36f, within(0.1f));  // 0.5"
      assertThat(BlueprintElements.BORDER_GAP).isCloseTo(9f, within(0.1f));     // 0.125"
   }



   /*******************************************************************************
    ** Test title block dimension constants.
    *******************************************************************************/
   @Test
   void testTitleBlockConstants()
   {
      assertThat(BlueprintElements.TITLE_BLOCK_WIDTH).isCloseTo(252f, within(0.1f));   // 3.5"
      assertThat(BlueprintElements.TITLE_BLOCK_HEIGHT).isCloseTo(162f, within(0.1f)); // 2.25"
   }



   /*******************************************************************************
    ** Test drawing area calculation.
    *******************************************************************************/
   @Test
   void testGetDrawingArea()
   {
      Rectangle2D drawingArea = blueprint.getDrawingArea();

      assertThat(drawingArea).isNotNull();
      assertThat(drawingArea.getWidth()).isGreaterThan(0);
      assertThat(drawingArea.getHeight()).isGreaterThan(0);

      // Drawing area should be inside the page
      assertThat(drawingArea.getMinX()).isGreaterThan(BlueprintElements.OUTER_MARGIN);
      assertThat(drawingArea.getMinY()).isGreaterThan(BlueprintElements.OUTER_MARGIN);
      assertThat(drawingArea.getMaxX()).isLessThan(BlueprintElements.PAGE_WIDTH - BlueprintElements.OUTER_MARGIN);
      assertThat(drawingArea.getMaxY()).isLessThan(BlueprintElements.PAGE_HEIGHT - BlueprintElements.OUTER_MARGIN);
   }



   /*******************************************************************************
    ** Test auto-scale calculation for small cabinet.
    *******************************************************************************/
   @Test
   void testCalculateAutoScaleSmallCabinet()
   {
      // Small cabinet: 24" x 30"
      double scale = blueprint.calculateAutoScale(24.0, 30.0);

      // Should produce a positive scale
      assertThat(scale).isGreaterThan(0);

      // Scale should be reasonable (not too small, not too large)
      assertThat(scale).isGreaterThan(1.0);
      assertThat(scale).isLessThan(50.0);
   }



   /*******************************************************************************
    ** Test auto-scale calculation for large cabinet.
    *******************************************************************************/
   @Test
   void testCalculateAutoScaleLargeCabinet()
   {
      // Large cabinet: 48" x 84" (tall pantry)
      double scale = blueprint.calculateAutoScale(48.0, 84.0);

      assertThat(scale).isGreaterThan(0);

      // Large cabinet should have smaller scale
      assertThat(scale).isLessThan(10.0);
   }



   /*******************************************************************************
    ** Test center offset calculation.
    *******************************************************************************/
   @Test
   void testCalculateCenterOffset()
   {
      double scale = 10.0;
      double[] offset = blueprint.calculateCenterOffset(24.0, 34.5, scale);

      assertThat(offset).hasSize(2);

      // Offset X should be positive (shift right)
      assertThat(offset[0]).isGreaterThan(0);

      // Offset Y should be positive (shift up)
      assertThat(offset[1]).isGreaterThan(0);

      // Offsets should be within the page
      Rectangle2D drawingArea = blueprint.getDrawingArea();
      assertThat(offset[0]).isLessThan(drawingArea.getMaxX());
      assertThat(offset[1]).isLessThan(drawingArea.getMaxY());
   }



   /*******************************************************************************
    ** Test center offset keeps content in drawing area.
    *******************************************************************************/
   @Test
   void testCenterOffsetKeepsContentInDrawingArea()
   {
      double cabinetWidth = 24.0;
      double cabinetHeight = 34.5;
      double scale = blueprint.calculateAutoScale(cabinetWidth, cabinetHeight);
      double[] offset = blueprint.calculateCenterOffset(cabinetWidth, cabinetHeight, scale);

      Rectangle2D drawingArea = blueprint.getDrawingArea();

      // Content origin should be inside drawing area
      assertThat(offset[0]).isGreaterThanOrEqualTo(drawingArea.getMinX());
      assertThat(offset[1]).isGreaterThanOrEqualTo(drawingArea.getMinY());

      // Content max should be inside drawing area (approximately)
      double maxX = offset[0] + cabinetWidth * scale;
      double maxY = offset[1] + cabinetHeight * scale;
      assertThat(maxX).isLessThanOrEqualTo(drawingArea.getMaxX() + 30);  // Some margin for annotations
      assertThat(maxY).isLessThanOrEqualTo(drawingArea.getMaxY() + 30);
   }



   /*******************************************************************************
    ** Test with different settings.
    *******************************************************************************/
   @Test
   void testWithDifferentSettings()
   {
      RenderSettings customSettings = RenderSettings.architecturalBlueprint()
         .withGridDivisions(10, 8);

      BlueprintElements customBlueprint = new BlueprintElements(customSettings);

      // Should work with different grid divisions
      Rectangle2D drawingArea = customBlueprint.getDrawingArea();
      assertThat(drawingArea).isNotNull();
   }
}
