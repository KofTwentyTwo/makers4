package com.makers4.rendering.builders;


import com.kingsrook.qqq.backend.core.model.data.QRecord;
import com.makers4.model.Cabinet;
import com.makers4.rendering.core.Dimension;
import com.makers4.rendering.core.Vector3D;
import com.makers4.rendering.scene.RenderStyle;
import com.makers4.rendering.scene.SceneNode;


/*******************************************************************************
 ** Parametric scene builder that constructs a 3D scene graph from a Cabinet entity.
 ** Uses cabinet dimensions, type, and construction rules to generate positioned parts.
 *******************************************************************************/
public class CabinetSceneBuilder
{
   // Default material thicknesses (in inches) - TODO: lookup from Material table
   private static final double BOX_THICKNESS  = 0.75;   // 3/4" plywood
   private static final double BACK_THICKNESS = 0.25;   // 1/4" plywood
   private static final double DEFAULT_TOE_KICK_HEIGHT = 4.5;
   private static final double DEFAULT_TOE_KICK_DEPTH  = 3.0;



   /*******************************************************************************
    ** Build a scene graph from a Cabinet entity.
    *******************************************************************************/
   public SceneNode buildScene(Cabinet cabinet)
   {
      SceneNode root = new SceneNode("cabinet-root")
         .withLabel(cabinet.getName());

      // Convert cabinet dimensions from mm to inches
      double width  = Dimension.mm(cabinet.getWidthMm()).toInchesDouble();
      double height = Dimension.mm(cabinet.getHeightMm()).toInchesDouble();
      double depth  = Dimension.mm(cabinet.getDepthMm()).toInchesDouble();

      // Set root node size for bounding box calculations
      root.withSize(width, height, depth);

      // Determine cabinet type and build accordingly
      Long cabinetTypeId = cabinet.getCabinetTypeId();

      // CabinetType IDs: 1=Base, 2=Wall, 3=Tall (from seed data)
      if(cabinetTypeId != null)
      {
         switch(cabinetTypeId.intValue())
         {
            case 1 -> buildBaseCabinet(cabinet, root, width, height, depth);
            case 2 -> buildWallCabinet(cabinet, root, width, height, depth);
            case 3 -> buildTallCabinet(cabinet, root, width, height, depth);
            default -> buildBaseCabinet(cabinet, root, width, height, depth);
         }
      }
      else
      {
         // Default to base cabinet
         buildBaseCabinet(cabinet, root, width, height, depth);
      }

      return root;
   }



   /*******************************************************************************
    ** Build a scene graph from a QRecord (for use when Cabinet entity is not available).
    *******************************************************************************/
   public SceneNode buildScene(QRecord record)
   {
      String name = record.getValueString("name");
      SceneNode root = new SceneNode("cabinet-root")
         .withLabel(name != null ? name : "Cabinet");

      // Convert cabinet dimensions from mm to inches
      Integer widthMm = record.getValueInteger("widthMm");
      Integer heightMm = record.getValueInteger("heightMm");
      Integer depthMm = record.getValueInteger("depthMm");

      double width  = widthMm != null ? Dimension.mm(widthMm).toInchesDouble() : 24.0;
      double height = heightMm != null ? Dimension.mm(heightMm).toInchesDouble() : 34.5;
      double depth  = depthMm != null ? Dimension.mm(depthMm).toInchesDouble() : 24.0;

      // Set root node size for bounding box calculations
      root.withSize(width, height, depth);

      // Get toe kick dimensions
      Integer toeKickHeightMm = record.getValueInteger("toeKickHeightMm");
      Integer toeKickDepthMm = record.getValueInteger("toeKickDepthMm");
      double toeKickHeight = toeKickHeightMm != null && toeKickHeightMm > 0
         ? Dimension.mm(toeKickHeightMm).toInchesDouble()
         : DEFAULT_TOE_KICK_HEIGHT;
      double toeKickDepth = toeKickDepthMm != null && toeKickDepthMm > 0
         ? Dimension.mm(toeKickDepthMm).toInchesDouble()
         : DEFAULT_TOE_KICK_DEPTH;

      // Determine cabinet type and build accordingly
      Long cabinetTypeId = record.getValueLong("cabinetTypeId");

      // CabinetType IDs: 1=Base, 2=Wall, 3=Tall (from seed data)
      if(cabinetTypeId != null)
      {
         switch(cabinetTypeId.intValue())
         {
            case 1 -> buildBaseCabinetFromRecord(root, width, height, depth, toeKickHeight, toeKickDepth);
            case 2 -> buildWallCabinetFromRecord(root, width, height, depth);
            case 3 -> buildTallCabinetFromRecord(root, width, height, depth, toeKickHeight, toeKickDepth);
            default -> buildBaseCabinetFromRecord(root, width, height, depth, toeKickHeight, toeKickDepth);
         }
      }
      else
      {
         // Default to base cabinet
         buildBaseCabinetFromRecord(root, width, height, depth, toeKickHeight, toeKickDepth);
      }

      return root;
   }



   /*******************************************************************************
    ** Build a base cabinet from record data.
    *******************************************************************************/
   private void buildBaseCabinetFromRecord(SceneNode root, double width, double height, double depth,
                                           double toeKickHeight, double toeKickDepth)
   {
      double boxHeight = height - toeKickHeight;
      double interiorWidth = width - 2 * BOX_THICKNESS;
      double interiorDepth = depth - BACK_THICKNESS;

      RenderStyle panelStyle = RenderStyle.woodPanel();

      root.addChild(new SceneNode("Left Side")
         .withPosition(0, toeKickHeight, 0)
         .withSize(BOX_THICKNESS, boxHeight, interiorDepth)
         .withStyle(panelStyle));

      root.addChild(new SceneNode("Right Side")
         .withPosition(width - BOX_THICKNESS, toeKickHeight, 0)
         .withSize(BOX_THICKNESS, boxHeight, interiorDepth)
         .withStyle(panelStyle));

      root.addChild(new SceneNode("Bottom")
         .withPosition(BOX_THICKNESS, toeKickHeight, 0)
         .withSize(interiorWidth, BOX_THICKNESS, interiorDepth)
         .withStyle(panelStyle));

      root.addChild(new SceneNode("Back")
         .withPosition(BOX_THICKNESS, toeKickHeight + BOX_THICKNESS, depth - BACK_THICKNESS)
         .withSize(interiorWidth, boxHeight - BOX_THICKNESS, BACK_THICKNESS)
         .withStyle(panelStyle));

      root.addChild(new SceneNode("Toe Kick")
         .withPosition(BOX_THICKNESS, 0, toeKickDepth)
         .withSize(interiorWidth, toeKickHeight, BOX_THICKNESS)
         .withStyle(panelStyle));

      root.addChild(new SceneNode("Top Nailer")
         .withPosition(BOX_THICKNESS, height - 3.0, depth - 3.75)
         .withSize(interiorWidth, 3.0, BOX_THICKNESS)
         .withStyle(panelStyle));

      double shelfY = toeKickHeight + boxHeight / 2;
      root.addChild(new SceneNode("Shelf")
         .withPosition(BOX_THICKNESS + 0.125, shelfY, 0.5)
         .withSize(interiorWidth - 0.25, BOX_THICKNESS, interiorDepth - 1.0)
         .withStyle(panelStyle));
   }



   /*******************************************************************************
    ** Build a wall cabinet from record data.
    *******************************************************************************/
   private void buildWallCabinetFromRecord(SceneNode root, double width, double height, double depth)
   {
      double interiorWidth = width - 2 * BOX_THICKNESS;
      double interiorDepth = depth - BACK_THICKNESS;

      RenderStyle panelStyle = RenderStyle.woodPanel();

      root.addChild(new SceneNode("Left Side")
         .withPosition(0, 0, 0)
         .withSize(BOX_THICKNESS, height, interiorDepth)
         .withStyle(panelStyle));

      root.addChild(new SceneNode("Right Side")
         .withPosition(width - BOX_THICKNESS, 0, 0)
         .withSize(BOX_THICKNESS, height, interiorDepth)
         .withStyle(panelStyle));

      root.addChild(new SceneNode("Top")
         .withPosition(BOX_THICKNESS, height - BOX_THICKNESS, 0)
         .withSize(interiorWidth, BOX_THICKNESS, interiorDepth)
         .withStyle(panelStyle));

      root.addChild(new SceneNode("Bottom")
         .withPosition(BOX_THICKNESS, 0, 0)
         .withSize(interiorWidth, BOX_THICKNESS, interiorDepth)
         .withStyle(panelStyle));

      root.addChild(new SceneNode("Back")
         .withPosition(BOX_THICKNESS, BOX_THICKNESS, depth - BACK_THICKNESS)
         .withSize(interiorWidth, height - 2 * BOX_THICKNESS, BACK_THICKNESS)
         .withStyle(panelStyle));

      double shelfY1 = height / 3;
      double shelfY2 = 2 * height / 3;

      root.addChild(new SceneNode("Lower Shelf")
         .withPosition(BOX_THICKNESS + 0.125, shelfY1, 0.5)
         .withSize(interiorWidth - 0.25, BOX_THICKNESS, interiorDepth - 1.0)
         .withStyle(panelStyle));

      root.addChild(new SceneNode("Upper Shelf")
         .withPosition(BOX_THICKNESS + 0.125, shelfY2, 0.5)
         .withSize(interiorWidth - 0.25, BOX_THICKNESS, interiorDepth - 1.0)
         .withStyle(panelStyle));
   }



   /*******************************************************************************
    ** Build a tall cabinet from record data.
    *******************************************************************************/
   private void buildTallCabinetFromRecord(SceneNode root, double width, double height, double depth,
                                           double toeKickHeight, double toeKickDepth)
   {
      double boxHeight = height - toeKickHeight;
      double interiorWidth = width - 2 * BOX_THICKNESS;
      double interiorDepth = depth - BACK_THICKNESS;

      RenderStyle panelStyle = RenderStyle.woodPanel();

      root.addChild(new SceneNode("Left Side")
         .withPosition(0, toeKickHeight, 0)
         .withSize(BOX_THICKNESS, boxHeight, interiorDepth)
         .withStyle(panelStyle));

      root.addChild(new SceneNode("Right Side")
         .withPosition(width - BOX_THICKNESS, toeKickHeight, 0)
         .withSize(BOX_THICKNESS, boxHeight, interiorDepth)
         .withStyle(panelStyle));

      root.addChild(new SceneNode("Top")
         .withPosition(BOX_THICKNESS, height - BOX_THICKNESS, 0)
         .withSize(interiorWidth, BOX_THICKNESS, interiorDepth)
         .withStyle(panelStyle));

      root.addChild(new SceneNode("Bottom")
         .withPosition(BOX_THICKNESS, toeKickHeight, 0)
         .withSize(interiorWidth, BOX_THICKNESS, interiorDepth)
         .withStyle(panelStyle));

      root.addChild(new SceneNode("Back")
         .withPosition(BOX_THICKNESS, toeKickHeight + BOX_THICKNESS, depth - BACK_THICKNESS)
         .withSize(interiorWidth, boxHeight - BOX_THICKNESS, BACK_THICKNESS)
         .withStyle(panelStyle));

      root.addChild(new SceneNode("Toe Kick")
         .withPosition(BOX_THICKNESS, 0, toeKickDepth)
         .withSize(interiorWidth, toeKickHeight, BOX_THICKNESS)
         .withStyle(panelStyle));

      double shelfSpacing = boxHeight / 5;
      for(int i = 1; i <= 4; i++)
      {
         double shelfY = toeKickHeight + shelfSpacing * i;
         root.addChild(new SceneNode("Shelf " + i)
            .withPosition(BOX_THICKNESS + 0.125, shelfY, 0.5)
            .withSize(interiorWidth - 0.25, BOX_THICKNESS, interiorDepth - 1.0)
            .withStyle(panelStyle));
      }
   }



   /*******************************************************************************
    ** Build a base cabinet (floor-standing with toe kick).
    ** Standard dimensions: 34.5"H with 4.5" toe kick = 30" box height
    *******************************************************************************/
   private void buildBaseCabinet(Cabinet cabinet, SceneNode root, double width, double height, double depth)
   {
      // Get toe kick dimensions
      double toeKickHeight = getToeKickHeight(cabinet);
      double toeKickDepth  = getToeKickDepth(cabinet);

      // Box height is total height minus toe kick
      double boxHeight = height - toeKickHeight;

      // Interior dimensions (accounting for material thickness)
      double interiorWidth = width - 2 * BOX_THICKNESS;
      double interiorDepth = depth - BACK_THICKNESS;

      RenderStyle panelStyle = RenderStyle.woodPanel();

      // Left side panel
      root.addChild(new SceneNode("Left Side")
         .withPosition(0, toeKickHeight, 0)
         .withSize(BOX_THICKNESS, boxHeight, interiorDepth)
         .withStyle(panelStyle));

      // Right side panel
      root.addChild(new SceneNode("Right Side")
         .withPosition(width - BOX_THICKNESS, toeKickHeight, 0)
         .withSize(BOX_THICKNESS, boxHeight, interiorDepth)
         .withStyle(panelStyle));

      // Bottom panel
      root.addChild(new SceneNode("Bottom")
         .withPosition(BOX_THICKNESS, toeKickHeight, 0)
         .withSize(interiorWidth, BOX_THICKNESS, interiorDepth)
         .withStyle(panelStyle));

      // Back panel (1/4" plywood, inset into rabbet)
      root.addChild(new SceneNode("Back")
         .withPosition(BOX_THICKNESS, toeKickHeight + BOX_THICKNESS, depth - BACK_THICKNESS)
         .withSize(interiorWidth, boxHeight - BOX_THICKNESS, BACK_THICKNESS)
         .withStyle(panelStyle));

      // Toe kick board
      root.addChild(new SceneNode("Toe Kick")
         .withPosition(BOX_THICKNESS, 0, toeKickDepth)
         .withSize(interiorWidth, toeKickHeight, BOX_THICKNESS)
         .withStyle(panelStyle));

      // Top nailer strip
      root.addChild(new SceneNode("Top Nailer")
         .withPosition(BOX_THICKNESS, height - 3.0, depth - 3.75)
         .withSize(interiorWidth, 3.0, BOX_THICKNESS)
         .withStyle(panelStyle));

      // Add a shelf at middle height
      double shelfY = toeKickHeight + boxHeight / 2;
      root.addChild(new SceneNode("Shelf")
         .withPosition(BOX_THICKNESS + 0.125, shelfY, 0.5)
         .withSize(interiorWidth - 0.25, BOX_THICKNESS, interiorDepth - 1.0)
         .withStyle(panelStyle));
   }



   /*******************************************************************************
    ** Build a wall cabinet (wall-mounted, no toe kick).
    ** Standard dimensions: 30"H x 12"D
    *******************************************************************************/
   private void buildWallCabinet(Cabinet cabinet, SceneNode root, double width, double height, double depth)
   {
      // Interior dimensions
      double interiorWidth = width - 2 * BOX_THICKNESS;
      double interiorDepth = depth - BACK_THICKNESS;

      RenderStyle panelStyle = RenderStyle.woodPanel();

      // Left side panel
      root.addChild(new SceneNode("Left Side")
         .withPosition(0, 0, 0)
         .withSize(BOX_THICKNESS, height, interiorDepth)
         .withStyle(panelStyle));

      // Right side panel
      root.addChild(new SceneNode("Right Side")
         .withPosition(width - BOX_THICKNESS, 0, 0)
         .withSize(BOX_THICKNESS, height, interiorDepth)
         .withStyle(panelStyle));

      // Top panel
      root.addChild(new SceneNode("Top")
         .withPosition(BOX_THICKNESS, height - BOX_THICKNESS, 0)
         .withSize(interiorWidth, BOX_THICKNESS, interiorDepth)
         .withStyle(panelStyle));

      // Bottom panel
      root.addChild(new SceneNode("Bottom")
         .withPosition(BOX_THICKNESS, 0, 0)
         .withSize(interiorWidth, BOX_THICKNESS, interiorDepth)
         .withStyle(panelStyle));

      // Back panel
      root.addChild(new SceneNode("Back")
         .withPosition(BOX_THICKNESS, BOX_THICKNESS, depth - BACK_THICKNESS)
         .withSize(interiorWidth, height - 2 * BOX_THICKNESS, BACK_THICKNESS)
         .withStyle(panelStyle));

      // Two shelves at 1/3 and 2/3 height
      double shelfY1 = height / 3;
      double shelfY2 = 2 * height / 3;

      root.addChild(new SceneNode("Lower Shelf")
         .withPosition(BOX_THICKNESS + 0.125, shelfY1, 0.5)
         .withSize(interiorWidth - 0.25, BOX_THICKNESS, interiorDepth - 1.0)
         .withStyle(panelStyle));

      root.addChild(new SceneNode("Upper Shelf")
         .withPosition(BOX_THICKNESS + 0.125, shelfY2, 0.5)
         .withSize(interiorWidth - 0.25, BOX_THICKNESS, interiorDepth - 1.0)
         .withStyle(panelStyle));
   }



   /*******************************************************************************
    ** Build a tall cabinet (full-height pantry/utility).
    ** Standard dimensions: 84"H with 4.5" toe kick
    *******************************************************************************/
   private void buildTallCabinet(Cabinet cabinet, SceneNode root, double width, double height, double depth)
   {
      // Get toe kick dimensions
      double toeKickHeight = getToeKickHeight(cabinet);
      double toeKickDepth  = getToeKickDepth(cabinet);

      // Box height is total height minus toe kick
      double boxHeight = height - toeKickHeight;

      // Interior dimensions
      double interiorWidth = width - 2 * BOX_THICKNESS;
      double interiorDepth = depth - BACK_THICKNESS;

      RenderStyle panelStyle = RenderStyle.woodPanel();

      // Left side panel
      root.addChild(new SceneNode("Left Side")
         .withPosition(0, toeKickHeight, 0)
         .withSize(BOX_THICKNESS, boxHeight, interiorDepth)
         .withStyle(panelStyle));

      // Right side panel
      root.addChild(new SceneNode("Right Side")
         .withPosition(width - BOX_THICKNESS, toeKickHeight, 0)
         .withSize(BOX_THICKNESS, boxHeight, interiorDepth)
         .withStyle(panelStyle));

      // Top panel
      root.addChild(new SceneNode("Top")
         .withPosition(BOX_THICKNESS, height - BOX_THICKNESS, 0)
         .withSize(interiorWidth, BOX_THICKNESS, interiorDepth)
         .withStyle(panelStyle));

      // Bottom panel
      root.addChild(new SceneNode("Bottom")
         .withPosition(BOX_THICKNESS, toeKickHeight, 0)
         .withSize(interiorWidth, BOX_THICKNESS, interiorDepth)
         .withStyle(panelStyle));

      // Back panel
      root.addChild(new SceneNode("Back")
         .withPosition(BOX_THICKNESS, toeKickHeight + BOX_THICKNESS, depth - BACK_THICKNESS)
         .withSize(interiorWidth, boxHeight - BOX_THICKNESS, BACK_THICKNESS)
         .withStyle(panelStyle));

      // Toe kick board
      root.addChild(new SceneNode("Toe Kick")
         .withPosition(BOX_THICKNESS, 0, toeKickDepth)
         .withSize(interiorWidth, toeKickHeight, BOX_THICKNESS)
         .withStyle(panelStyle));

      // Four evenly-spaced shelves
      double shelfSpacing = boxHeight / 5;
      for(int i = 1; i <= 4; i++)
      {
         double shelfY = toeKickHeight + shelfSpacing * i;
         root.addChild(new SceneNode("Shelf " + i)
            .withPosition(BOX_THICKNESS + 0.125, shelfY, 0.5)
            .withSize(interiorWidth - 0.25, BOX_THICKNESS, interiorDepth - 1.0)
            .withStyle(panelStyle));
      }
   }



   /*******************************************************************************
    ** Get toe kick height from cabinet or use default.
    *******************************************************************************/
   private double getToeKickHeight(Cabinet cabinet)
   {
      Integer toeKickHeightMm = cabinet.getToeKickHeightMm();
      if(toeKickHeightMm != null && toeKickHeightMm > 0)
      {
         return Dimension.mm(toeKickHeightMm).toInchesDouble();
      }
      return DEFAULT_TOE_KICK_HEIGHT;
   }



   /*******************************************************************************
    ** Get toe kick depth from cabinet or use default.
    *******************************************************************************/
   private double getToeKickDepth(Cabinet cabinet)
   {
      Integer toeKickDepthMm = cabinet.getToeKickDepthMm();
      if(toeKickDepthMm != null && toeKickDepthMm > 0)
      {
         return Dimension.mm(toeKickDepthMm).toInchesDouble();
      }
      return DEFAULT_TOE_KICK_DEPTH;
   }
}
