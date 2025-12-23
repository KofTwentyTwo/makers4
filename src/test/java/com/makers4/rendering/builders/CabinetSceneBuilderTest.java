package com.makers4.rendering.builders;


import java.util.ArrayList;
import java.util.List;
import com.makers4.model.Cabinet;
import com.makers4.rendering.core.Box3D;
import com.makers4.rendering.scene.SceneNode;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/*******************************************************************************
 ** Unit tests for CabinetSceneBuilder class.
 *******************************************************************************/
class CabinetSceneBuilderTest
{
   private CabinetSceneBuilder builder;



   @BeforeEach
   void setUp()
   {
      builder = new CabinetSceneBuilder();
   }



   /*******************************************************************************
    ** Test building a base cabinet scene.
    *******************************************************************************/
   @Test
   void testBuildBaseCabinetScene()
   {
      Cabinet cabinet = createBaseCabinet();

      SceneNode scene = builder.buildScene(cabinet);

      assertThat(scene).isNotNull();
      assertThat(scene.getName()).isEqualTo("cabinet-root");
      assertThat(scene.getLabel()).isEqualTo("Test Base Cabinet");

      // Should have child nodes for parts
      assertThat(scene.getChildren()).isNotEmpty();
   }



   /*******************************************************************************
    ** Test base cabinet has expected parts.
    *******************************************************************************/
   @Test
   void testBaseCabinetHasExpectedParts()
   {
      Cabinet cabinet = createBaseCabinet();
      SceneNode scene = builder.buildScene(cabinet);

      List<SceneNode> allNodes = getAllDescendants(scene);

      // Base cabinet should have: left side, right side, bottom, back, toe kick, top nailer, shelf
      assertThat(allNodes.stream().anyMatch(n -> n.getName().contains("Left Side"))).isTrue();
      assertThat(allNodes.stream().anyMatch(n -> n.getName().contains("Right Side"))).isTrue();
      assertThat(allNodes.stream().anyMatch(n -> n.getName().contains("Bottom"))).isTrue();
      assertThat(allNodes.stream().anyMatch(n -> n.getName().contains("Back"))).isTrue();
      assertThat(allNodes.stream().anyMatch(n -> n.getName().contains("Toe Kick"))).isTrue();
   }



   /*******************************************************************************
    ** Test building a wall cabinet scene.
    *******************************************************************************/
   @Test
   void testBuildWallCabinetScene()
   {
      Cabinet cabinet = createWallCabinet();

      SceneNode scene = builder.buildScene(cabinet);

      assertThat(scene).isNotNull();
      assertThat(scene.getChildren()).isNotEmpty();

      List<SceneNode> allNodes = getAllDescendants(scene);

      // Wall cabinet should have: left side, right side, top, bottom, back, shelves
      assertThat(allNodes.stream().anyMatch(n -> n.getName().contains("Left Side"))).isTrue();
      assertThat(allNodes.stream().anyMatch(n -> n.getName().contains("Right Side"))).isTrue();
      assertThat(allNodes.stream().anyMatch(n -> n.getName().contains("Top"))).isTrue();
      assertThat(allNodes.stream().anyMatch(n -> n.getName().contains("Bottom"))).isTrue();
      assertThat(allNodes.stream().anyMatch(n -> n.getName().contains("Back"))).isTrue();

      // Wall cabinet should NOT have toe kick
      assertThat(allNodes.stream().noneMatch(n -> n.getName().contains("Toe Kick"))).isTrue();
   }



   /*******************************************************************************
    ** Test building a tall cabinet scene.
    *******************************************************************************/
   @Test
   void testBuildTallCabinetScene()
   {
      Cabinet cabinet = createTallCabinet();

      SceneNode scene = builder.buildScene(cabinet);

      assertThat(scene).isNotNull();
      assertThat(scene.getChildren()).isNotEmpty();

      List<SceneNode> allNodes = getAllDescendants(scene);

      // Tall cabinet should have: left side, right side, top, bottom, back, toe kick, multiple shelves
      assertThat(allNodes.stream().anyMatch(n -> n.getName().contains("Left Side"))).isTrue();
      assertThat(allNodes.stream().anyMatch(n -> n.getName().contains("Right Side"))).isTrue();
      assertThat(allNodes.stream().anyMatch(n -> n.getName().contains("Top"))).isTrue();
      assertThat(allNodes.stream().anyMatch(n -> n.getName().contains("Bottom"))).isTrue();
      assertThat(allNodes.stream().anyMatch(n -> n.getName().contains("Back"))).isTrue();
      assertThat(allNodes.stream().anyMatch(n -> n.getName().contains("Toe Kick"))).isTrue();

      // Should have multiple shelves
      long shelfCount = allNodes.stream().filter(n -> n.getName().contains("Shelf")).count();
      assertThat(shelfCount).isGreaterThanOrEqualTo(3);
   }



   /*******************************************************************************
    ** Test scene total bounds match cabinet dimensions.
    *******************************************************************************/
   @Test
   void testSceneBoundsMatchCabinetDimensions()
   {
      Cabinet cabinet = createBaseCabinet();  // 610mm x 876mm x 610mm

      SceneNode scene = builder.buildScene(cabinet);
      Box3D bounds = scene.calculateTotalBounds();

      // Convert mm to inches: 610mm = 24", 876mm = 34.5"
      double expectedWidth = 24.0;
      double expectedHeight = 34.5;
      double expectedDepth = 24.0;

      assertThat(bounds.width()).isCloseTo(expectedWidth, within(0.5));
      assertThat(bounds.height()).isCloseTo(expectedHeight, within(0.5));
      // Depth should be close to expected (allow for mm to inch conversion rounding)
      assertThat(bounds.depth()).isCloseTo(expectedDepth, within(0.5));
   }



   /*******************************************************************************
    ** Test side panels start at toe kick height for base cabinet.
    *******************************************************************************/
   @Test
   void testBaseCabinetSidesStartAtToeKick()
   {
      Cabinet cabinet = createBaseCabinet();
      cabinet.setToeKickHeightMm(114);  // 4.5" in mm

      SceneNode scene = builder.buildScene(cabinet);

      SceneNode leftSide = findDescendant(scene, "Left Side");
      assertThat(leftSide).isNotNull();

      // Left side should start at approximately toe kick height (4.5")
      assertThat(leftSide.getPosition().y()).isCloseTo(4.5, within(0.1));
   }



   /*******************************************************************************
    ** Test wall cabinet sides start at zero.
    *******************************************************************************/
   @Test
   void testWallCabinetSidesStartAtZero()
   {
      Cabinet cabinet = createWallCabinet();

      SceneNode scene = builder.buildScene(cabinet);

      SceneNode leftSide = findDescendant(scene, "Left Side");
      assertThat(leftSide).isNotNull();

      // Wall cabinet sides start at 0 (no toe kick)
      assertThat(leftSide.getPosition().y()).isCloseTo(0.0, within(0.01));
   }



   /*******************************************************************************
    ** Test cabinet with null type defaults to base.
    *******************************************************************************/
   @Test
   void testNullCabinetTypeDefaultsToBase()
   {
      Cabinet cabinet = createBaseCabinet();
      cabinet.setCabinetTypeId(null);  // No type set

      SceneNode scene = builder.buildScene(cabinet);

      // Should still build successfully and have toe kick (base cabinet default)
      List<SceneNode> allNodes = getAllDescendants(scene);
      assertThat(allNodes.stream().anyMatch(n -> n.getName().contains("Toe Kick"))).isTrue();
   }



   /*******************************************************************************
    ** Test all nodes have geometry (size set).
    *******************************************************************************/
   @Test
   void testAllPartsHaveGeometry()
   {
      Cabinet cabinet = createBaseCabinet();

      SceneNode scene = builder.buildScene(cabinet);

      for(SceneNode part : getAllDescendants(scene))
      {
         // All parts should have geometry
         assertThat(part.hasGeometry())
            .withFailMessage("Part '%s' should have geometry", part.getName())
            .isTrue();
      }
   }



   /*******************************************************************************
    ** Test all nodes have styles set.
    *******************************************************************************/
   @Test
   void testAllPartsHaveStyles()
   {
      Cabinet cabinet = createBaseCabinet();

      SceneNode scene = builder.buildScene(cabinet);

      for(SceneNode part : getAllDescendants(scene))
      {
         assertThat(part.getStyle())
            .withFailMessage("Part '%s' should have a style", part.getName())
            .isNotNull();
      }
   }



   /*******************************************************************************
    ** Test custom toe kick dimensions are used.
    *******************************************************************************/
   @Test
   void testCustomToeKickDimensions()
   {
      Cabinet cabinet = createBaseCabinet();
      cabinet.setToeKickHeightMm(100);  // ~3.94"
      cabinet.setToeKickDepthMm(80);    // ~3.15"

      SceneNode scene = builder.buildScene(cabinet);

      SceneNode toeKick = findDescendant(scene, "Toe Kick");
      assertThat(toeKick).isNotNull();

      // Verify toe kick height matches (converting mm to inches)
      assertThat(toeKick.getSize().y()).isCloseTo(3.94, within(0.1));
   }



   // ════════════════════════════════════════════════════════════════════════════
   // Helper methods for test utilities
   // ════════════════════════════════════════════════════════════════════════════



   /*******************************************************************************
    ** Recursively collect all descendants of a scene node.
    *******************************************************************************/
   private List<SceneNode> getAllDescendants(SceneNode node)
   {
      List<SceneNode> result = new ArrayList<>();
      collectDescendants(node, result);
      return result;
   }



   private void collectDescendants(SceneNode node, List<SceneNode> result)
   {
      for(SceneNode child : node.getChildren())
      {
         result.add(child);
         collectDescendants(child, result);
      }
   }



   /*******************************************************************************
    ** Find a descendant node by name (partial match).
    *******************************************************************************/
   private SceneNode findDescendant(SceneNode root, String nameContains)
   {
      return getAllDescendants(root).stream()
         .filter(n -> n.getName().contains(nameContains))
         .findFirst()
         .orElse(null);
   }



   // ════════════════════════════════════════════════════════════════════════════
   // Helper methods to create test cabinets
   // ════════════════════════════════════════════════════════════════════════════



   private Cabinet createBaseCabinet()
   {
      return new Cabinet()
         .withName("Test Base Cabinet")
         .withWidthMm(610)      // 24"
         .withHeightMm(876)     // 34.5"
         .withDepthMm(610)      // 24"
         .withCabinetTypeId(1L) // Base cabinet
         .withToeKickHeightMm(114)  // 4.5"
         .withToeKickDepthMm(76);   // 3"
   }



   private Cabinet createWallCabinet()
   {
      return new Cabinet()
         .withName("Test Wall Cabinet")
         .withWidthMm(762)      // 30"
         .withHeightMm(762)     // 30"
         .withDepthMm(305)      // 12"
         .withCabinetTypeId(2L); // Wall cabinet
   }



   private Cabinet createTallCabinet()
   {
      return new Cabinet()
         .withName("Test Tall Cabinet")
         .withWidthMm(610)      // 24"
         .withHeightMm(2134)    // 84"
         .withDepthMm(610)      // 24"
         .withCabinetTypeId(3L) // Tall cabinet
         .withToeKickHeightMm(114)  // 4.5"
         .withToeKickDepthMm(76);   // 3"
   }
}
