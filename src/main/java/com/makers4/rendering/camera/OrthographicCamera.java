package com.makers4.rendering.camera;


import java.awt.geom.Point2D;
import com.makers4.rendering.core.Box3D;
import com.makers4.rendering.core.Vector3D;


/*******************************************************************************
 ** Orthographic camera for projecting 3D scenes to 2D.
 ** Handles the transform from world coordinates to screen coordinates.
 *******************************************************************************/
public class OrthographicCamera
{
   private ViewDirection direction;
   private double        scale;
   private double        offsetX;
   private double        offsetY;

   // Isometric projection constants
   private static final double ISO_ANGLE = Math.toRadians(30);
   private static final double ISO_COS   = Math.cos(ISO_ANGLE);
   private static final double ISO_SIN   = Math.sin(ISO_ANGLE);



   /*******************************************************************************
    ** Constructor with view direction.
    *******************************************************************************/
   public OrthographicCamera(ViewDirection direction)
   {
      this.direction = direction;
      this.scale = 1.0;
      this.offsetX = 0;
      this.offsetY = 0;
   }



   /*******************************************************************************
    ** Create a camera for a specific view.
    *******************************************************************************/
   public static OrthographicCamera forView(ViewDirection direction)
   {
      return new OrthographicCamera(direction);
   }



   /*******************************************************************************
    ** Project a 3D world point to 2D screen coordinates.
    *******************************************************************************/
   public Point2D project(Vector3D worldPoint)
   {
      double screenX, screenY;

      if(direction.isIsometric())
      {
         // Isometric projection
         screenX = (worldPoint.x() - worldPoint.z()) * ISO_COS;
         screenY = worldPoint.y() + (worldPoint.x() + worldPoint.z()) * ISO_SIN;
      }
      else
      {
         // Orthographic projection based on view direction
         screenX = getAxisValue(worldPoint, direction.getHorizontalAxis());
         screenY = getAxisValue(worldPoint, direction.getVerticalAxis());

         // Apply mirroring if needed
         if(direction.isMirrorHorizontal())
         {
            screenX = -screenX;
         }
         if(direction.isMirrorVertical())
         {
            screenY = -screenY;
         }
      }

      // Apply scale and offset
      return new Point2D.Double(
         screenX * scale + offsetX,
         screenY * scale + offsetY
      );
   }



   /*******************************************************************************
    ** Project a 3D box to 2D bounds (returns screen rectangle dimensions).
    *******************************************************************************/
   public double[] projectBox(Box3D box)
   {
      Point2D minPoint = project(box.position());
      Point2D maxPoint = project(box.max());

      double x = Math.min(minPoint.getX(), maxPoint.getX());
      double y = Math.min(minPoint.getY(), maxPoint.getY());
      double width  = Math.abs(maxPoint.getX() - minPoint.getX());
      double height = Math.abs(maxPoint.getY() - minPoint.getY());

      return new double[] { x, y, width, height };
   }



   /*******************************************************************************
    ** Calculate the 2D size of a 3D box in this view (without position).
    *******************************************************************************/
   public double[] calculateProjectedSize(Box3D box)
   {
      if(direction.isIsometric())
      {
         // Isometric bounding box size
         double w = box.width();
         double h = box.height();
         double d = box.depth();
         double width  = (w + d) * ISO_COS;
         double height = h + (w + d) * ISO_SIN;
         return new double[] { width * scale, height * scale };
      }
      else
      {
         double width  = getAxisValue(box.size(), direction.getHorizontalAxis());
         double height = getAxisValue(box.size(), direction.getVerticalAxis());
         return new double[] { width * scale, height * scale };
      }
   }



   /*******************************************************************************
    ** Get a component value from a vector based on axis.
    *******************************************************************************/
   private double getAxisValue(Vector3D vector, ViewDirection.Axis axis)
   {
      return switch(axis)
      {
         case X -> vector.x();
         case Y -> vector.y();
         case Z -> vector.z();
      };
   }



   /*******************************************************************************
    ** Calculate auto-scale to fit a bounding box within given screen dimensions.
    *******************************************************************************/
   public double calculateAutoScale(Box3D bounds, double screenWidth, double screenHeight, double margin)
   {
      double[] projectedSize = calculateProjectedSize(bounds);

      // Reset scale temporarily to get unscaled size
      double oldScale = this.scale;
      this.scale = 1.0;
      projectedSize = calculateProjectedSize(bounds);
      this.scale = oldScale;

      double availableWidth  = screenWidth - 2 * margin;
      double availableHeight = screenHeight - 2 * margin;

      double scaleX = availableWidth / projectedSize[0];
      double scaleY = availableHeight / projectedSize[1];

      return Math.min(scaleX, scaleY);
   }



   /*******************************************************************************
    ** Configure camera to center content within screen bounds.
    *******************************************************************************/
   public void centerOn(Box3D bounds, double screenWidth, double screenHeight, double margin)
   {
      double[] projectedSize = calculateProjectedSize(bounds);

      // Center the content
      this.offsetX = (screenWidth - projectedSize[0]) / 2;
      this.offsetY = (screenHeight - projectedSize[1]) / 2;

      // For isometric, we need additional offset for the depth axis
      if(direction.isIsometric())
      {
         this.offsetX += bounds.depth() * ISO_COS * scale;
      }
   }



   /*******************************************************************************
    ** Getter for direction
    *******************************************************************************/
   public ViewDirection getDirection()
   {
      return direction;
   }



   /*******************************************************************************
    ** Fluent setter for direction
    *******************************************************************************/
   public OrthographicCamera withDirection(ViewDirection direction)
   {
      this.direction = direction;
      return this;
   }



   /*******************************************************************************
    ** Getter for scale
    *******************************************************************************/
   public double getScale()
   {
      return scale;
   }



   /*******************************************************************************
    ** Fluent setter for scale
    *******************************************************************************/
   public OrthographicCamera withScale(double scale)
   {
      this.scale = scale;
      return this;
   }



   /*******************************************************************************
    ** Getter for offsetX
    *******************************************************************************/
   public double getOffsetX()
   {
      return offsetX;
   }



   /*******************************************************************************
    ** Fluent setter for offsetX
    *******************************************************************************/
   public OrthographicCamera withOffsetX(double offsetX)
   {
      this.offsetX = offsetX;
      return this;
   }



   /*******************************************************************************
    ** Getter for offsetY
    *******************************************************************************/
   public double getOffsetY()
   {
      return offsetY;
   }



   /*******************************************************************************
    ** Fluent setter for offsetY
    *******************************************************************************/
   public OrthographicCamera withOffsetY(double offsetY)
   {
      this.offsetY = offsetY;
      return this;
   }
}
