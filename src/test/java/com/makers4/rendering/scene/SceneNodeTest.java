package com.makers4.rendering.scene;


import java.awt.Color;
import java.util.List;
import com.makers4.rendering.core.Box3D;
import com.makers4.rendering.core.Vector3D;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import org.junit.jupiter.api.Test;


/*******************************************************************************
 ** Unit tests for SceneNode class.
 *******************************************************************************/
class SceneNodeTest
{

   /*******************************************************************************
    ** Test basic node creation.
    *******************************************************************************/
   @Test
   void testBasicCreation()
   {
      SceneNode node = new SceneNode("test-node");

      assertThat(node.getName()).isEqualTo("test-node");
      assertThat(node.getLabel()).isEqualTo("test-node");  // Label defaults to name
      assertThat(node.getChildren()).isEmpty();
      assertThat(node.hasGeometry()).isFalse();
   }



   /*******************************************************************************
    ** Test fluent setters.
    *******************************************************************************/
   @Test
   void testFluentSetters()
   {
      SceneNode node = new SceneNode("panel")
         .withLabel("Left Side")
         .withPosition(0.0, 4.5, 0.0)
         .withSize(0.75, 30.0, 23.75)
         .withStyle(RenderStyle.woodPanel());

      assertThat(node.getName()).isEqualTo("panel");
      assertThat(node.getLabel()).isEqualTo("Left Side");
      assertThat(node.hasGeometry()).isTrue();

      Vector3D pos = node.getPosition();
      assertThat(pos.x()).isCloseTo(0.0, within(0.001));
      assertThat(pos.y()).isCloseTo(4.5, within(0.001));
      assertThat(pos.z()).isCloseTo(0.0, within(0.001));

      Vector3D size = node.getSize();
      assertThat(size.x()).isCloseTo(0.75, within(0.001));
      assertThat(size.y()).isCloseTo(30.0, within(0.001));
      assertThat(size.z()).isCloseTo(23.75, within(0.001));
   }



   /*******************************************************************************
    ** Test adding children.
    *******************************************************************************/
   @Test
   void testAddChildren()
   {
      SceneNode parent = new SceneNode("cabinet-root");
      SceneNode leftSide = new SceneNode("left-side")
         .withPosition(0, 0, 0)
         .withSize(0.75, 30, 24);
      SceneNode rightSide = new SceneNode("right-side")
         .withPosition(23.25, 0, 0)
         .withSize(0.75, 30, 24);

      parent.addChild(leftSide);
      parent.addChild(rightSide);

      assertThat(parent.getChildren()).hasSize(2);
      assertThat(parent.getChildren()).containsExactly(leftSide, rightSide);

      // Check parent reference
      assertThat(leftSide.getParent()).isEqualTo(parent);
      assertThat(rightSide.getParent()).isEqualTo(parent);
   }



   /*******************************************************************************
    ** Test world position calculation.
    *******************************************************************************/
   @Test
   void testWorldPosition()
   {
      SceneNode cabinet = new SceneNode("cabinet")
         .withPosition(10.0, 0.0, 5.0);

      SceneNode shelf = new SceneNode("shelf")
         .withPosition(0.75, 15.0, 0.5)
         .withSize(22.5, 0.75, 23.0);

      cabinet.addChild(shelf);

      // Shelf's world position should be cabinet position + shelf's local position
      Vector3D worldPos = shelf.getWorldPosition();
      assertThat(worldPos.x()).isCloseTo(10.75, within(0.001));  // 10 + 0.75
      assertThat(worldPos.y()).isCloseTo(15.0, within(0.001));   // 0 + 15
      assertThat(worldPos.z()).isCloseTo(5.5, within(0.001));    // 5 + 0.5
   }



   /*******************************************************************************
    ** Test nested world position calculation (3 levels deep).
    *******************************************************************************/
   @Test
   void testNestedWorldPosition()
   {
      SceneNode room = new SceneNode("room")
         .withPosition(100.0, 0.0, 50.0);

      SceneNode cabinet = new SceneNode("cabinet")
         .withPosition(10.0, 0.0, 5.0);

      SceneNode shelf = new SceneNode("shelf")
         .withPosition(0.75, 15.0, 0.5);

      room.addChild(cabinet);
      cabinet.addChild(shelf);

      Vector3D worldPos = shelf.getWorldPosition();
      assertThat(worldPos.x()).isCloseTo(110.75, within(0.001));  // 100 + 10 + 0.75
      assertThat(worldPos.y()).isCloseTo(15.0, within(0.001));    // 0 + 0 + 15
      assertThat(worldPos.z()).isCloseTo(55.5, within(0.001));    // 50 + 5 + 0.5
   }



   /*******************************************************************************
    ** Test world bounds calculation.
    *******************************************************************************/
   @Test
   void testWorldBounds()
   {
      SceneNode cabinet = new SceneNode("cabinet")
         .withPosition(10.0, 0.0, 5.0);

      SceneNode panel = new SceneNode("left-side")
         .withPosition(0.0, 4.5, 0.0)
         .withSize(0.75, 30.0, 23.75);

      cabinet.addChild(panel);

      Box3D worldBounds = panel.getWorldBounds();

      // Position should be cabinet position + panel's local position
      assertThat(worldBounds.position().x()).isCloseTo(10.0, within(0.001));
      assertThat(worldBounds.position().y()).isCloseTo(4.5, within(0.001));
      assertThat(worldBounds.position().z()).isCloseTo(5.0, within(0.001));

      // Size should be unchanged
      assertThat(worldBounds.width()).isCloseTo(0.75, within(0.001));
      assertThat(worldBounds.height()).isCloseTo(30.0, within(0.001));
      assertThat(worldBounds.depth()).isCloseTo(23.75, within(0.001));
   }



   /*******************************************************************************
    ** Test total bounds calculation for all children.
    *******************************************************************************/
   @Test
   void testCalculateTotalBounds()
   {
      SceneNode root = new SceneNode("cabinet-root")
         .withSize(24.0, 34.5, 24.0);

      // Add side panels
      root.addChild(new SceneNode("left-side")
         .withPosition(0, 4.5, 0)
         .withSize(0.75, 30.0, 23.75));

      root.addChild(new SceneNode("right-side")
         .withPosition(23.25, 4.5, 0)
         .withSize(0.75, 30.0, 23.75));

      // Add bottom
      root.addChild(new SceneNode("bottom")
         .withPosition(0.75, 4.5, 0)
         .withSize(22.5, 0.75, 23.75));

      Box3D totalBounds = root.calculateTotalBounds();

      // Should encompass all children
      assertThat(totalBounds.position().x()).isCloseTo(0.0, within(0.001));
      assertThat(totalBounds.position().y()).isCloseTo(0.0, within(0.001));  // Root starts at 0
      assertThat(totalBounds.max().x()).isCloseTo(24.0, within(0.001));
      assertThat(totalBounds.max().y()).isCloseTo(34.5, within(0.001));
   }



   /*******************************************************************************
    ** Test hasGeometry flag.
    *******************************************************************************/
   @Test
   void testHasGeometry()
   {
      // Node without size has no geometry
      SceneNode rootOnly = new SceneNode("root");
      assertThat(rootOnly.hasGeometry()).isFalse();

      // Node with size has geometry
      SceneNode withSize = new SceneNode("panel")
         .withSize(10.0, 10.0, 10.0);
      assertThat(withSize.hasGeometry()).isTrue();
   }



   /*******************************************************************************
    ** Test local bounds calculation.
    *******************************************************************************/
   @Test
   void testLocalBounds()
   {
      SceneNode node = new SceneNode("panel")
         .withPosition(5.0, 10.0, 15.0)
         .withSize(10.0, 20.0, 30.0);

      Box3D localBounds = node.getLocalBounds();

      assertThat(localBounds.position().x()).isCloseTo(5.0, within(0.001));
      assertThat(localBounds.position().y()).isCloseTo(10.0, within(0.001));
      assertThat(localBounds.position().z()).isCloseTo(15.0, within(0.001));
      assertThat(localBounds.width()).isCloseTo(10.0, within(0.001));
      assertThat(localBounds.height()).isCloseTo(20.0, within(0.001));
      assertThat(localBounds.depth()).isCloseTo(30.0, within(0.001));
   }



   /*******************************************************************************
    ** Test style getter and setter.
    *******************************************************************************/
   @Test
   void testStyle()
   {
      SceneNode node = new SceneNode("panel");
      assertThat(node.getStyle()).isNotNull();

      RenderStyle customStyle = RenderStyle.woodPanel();
      node.withStyle(customStyle);
      assertThat(node.getStyle()).isEqualTo(customStyle);
   }



   /*******************************************************************************
    ** Test withPosition with Vector3D.
    *******************************************************************************/
   @Test
   void testWithPositionVector()
   {
      Vector3D pos = new Vector3D(1.0, 2.0, 3.0);
      SceneNode node = new SceneNode("test")
         .withPosition(pos);

      assertThat(node.getPosition()).isEqualTo(pos);
   }



   /*******************************************************************************
    ** Test withSize with Vector3D.
    *******************************************************************************/
   @Test
   void testWithSizeVector()
   {
      Vector3D size = new Vector3D(10.0, 20.0, 30.0);
      SceneNode node = new SceneNode("test")
         .withSize(size);

      assertThat(node.getSize()).isEqualTo(size);
   }



   /*******************************************************************************
    ** Test withName method.
    *******************************************************************************/
   @Test
   void testWithName()
   {
      SceneNode node = new SceneNode("original");
      node.withName("updated");

      assertThat(node.getName()).isEqualTo("updated");
   }



   /*******************************************************************************
    ** Test format method.
    *******************************************************************************/
   @Test
   void testFormat()
   {
      SceneNode node = new SceneNode("test-node")
         .withSize(10.0, 20.0, 30.0);

      String format = node.format();
      assertThat(format).contains("test-node");
      assertThat(format).contains("SceneNode");

      assertThat(node.toString()).isEqualTo(format);
   }
}
