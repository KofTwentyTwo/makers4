package com.makers4.rendering.camera;


/*******************************************************************************
 ** Orthographic view directions for 2D projection of 3D scenes.
 ** Each direction represents looking at the scene from a specific angle.
 *******************************************************************************/
public enum ViewDirection
{
   /*******************************************************************************
    ** Front view - looking at the X-Y plane from the front (Z=0).
    ** Shows width (X) horizontally and height (Y) vertically.
    *******************************************************************************/
   FRONT("Front View", "Looking from the front", Axis.X, Axis.Y, false, false),

   /*******************************************************************************
    ** Back view - looking at the X-Y plane from the rear.
    ** Shows width (X) horizontally (mirrored) and height (Y) vertically.
    *******************************************************************************/
   BACK("Back View", "Looking from the back", Axis.X, Axis.Y, true, false),

   /*******************************************************************************
    ** Left side view - looking at the Z-Y plane from the left.
    ** Shows depth (Z) horizontally and height (Y) vertically.
    *******************************************************************************/
   LEFT("Left Side", "Looking from the left side", Axis.Z, Axis.Y, false, false),

   /*******************************************************************************
    ** Right side view - looking at the Z-Y plane from the right.
    ** Shows depth (Z) horizontally (mirrored) and height (Y) vertically.
    *******************************************************************************/
   RIGHT("Right Side", "Looking from the right side", Axis.Z, Axis.Y, true, false),

   /*******************************************************************************
    ** Top view (plan view) - looking at the X-Z plane from above.
    ** Shows width (X) horizontally and depth (Z) vertically.
    *******************************************************************************/
   TOP("Top View", "Looking from above", Axis.X, Axis.Z, false, false),

   /*******************************************************************************
    ** Bottom view - looking at the X-Z plane from below.
    ** Shows width (X) horizontally and depth (Z) vertically (mirrored).
    *******************************************************************************/
   BOTTOM("Bottom View", "Looking from below", Axis.X, Axis.Z, false, true),

   /*******************************************************************************
    ** Isometric view - 3D projection showing all three dimensions.
    ** Standard 30-degree isometric projection.
    *******************************************************************************/
   ISOMETRIC("Isometric View", "3D isometric projection", null, null, false, false);


   private final String  displayName;
   private final String  description;
   private final Axis    horizontalAxis;
   private final Axis    verticalAxis;
   private final boolean mirrorHorizontal;
   private final boolean mirrorVertical;



   /*******************************************************************************
    ** Axis enumeration for coordinate mapping.
    *******************************************************************************/
   public enum Axis
   {
      X, Y, Z
   }



   /*******************************************************************************
    ** Constructor
    *******************************************************************************/
   ViewDirection(String displayName, String description,
                 Axis horizontalAxis, Axis verticalAxis,
                 boolean mirrorHorizontal, boolean mirrorVertical)
   {
      this.displayName = displayName;
      this.description = description;
      this.horizontalAxis = horizontalAxis;
      this.verticalAxis = verticalAxis;
      this.mirrorHorizontal = mirrorHorizontal;
      this.mirrorVertical = mirrorVertical;
   }



   /*******************************************************************************
    ** Get the common views used for cabinet blueprints.
    *******************************************************************************/
   public static ViewDirection[] standardViews()
   {
      return new ViewDirection[] { FRONT, LEFT, TOP };
   }



   /*******************************************************************************
    ** Get all elevation views (front, back, left, right).
    *******************************************************************************/
   public static ViewDirection[] elevationViews()
   {
      return new ViewDirection[] { FRONT, BACK, LEFT, RIGHT };
   }



   /*******************************************************************************
    ** Get all orthographic views (excluding isometric).
    *******************************************************************************/
   public static ViewDirection[] orthographicViews()
   {
      return new ViewDirection[] { FRONT, BACK, LEFT, RIGHT, TOP, BOTTOM };
   }



   /*******************************************************************************
    ** Check if this is an isometric view.
    *******************************************************************************/
   public boolean isIsometric()
   {
      return this == ISOMETRIC;
   }



   /*******************************************************************************
    ** Getter for displayName
    *******************************************************************************/
   public String getDisplayName()
   {
      return displayName;
   }



   /*******************************************************************************
    ** Getter for description
    *******************************************************************************/
   public String getDescription()
   {
      return description;
   }



   /*******************************************************************************
    ** Getter for horizontalAxis
    *******************************************************************************/
   public Axis getHorizontalAxis()
   {
      return horizontalAxis;
   }



   /*******************************************************************************
    ** Getter for verticalAxis
    *******************************************************************************/
   public Axis getVerticalAxis()
   {
      return verticalAxis;
   }



   /*******************************************************************************
    ** Getter for mirrorHorizontal
    *******************************************************************************/
   public boolean isMirrorHorizontal()
   {
      return mirrorHorizontal;
   }



   /*******************************************************************************
    ** Getter for mirrorVertical
    *******************************************************************************/
   public boolean isMirrorVertical()
   {
      return mirrorVertical;
   }
}
