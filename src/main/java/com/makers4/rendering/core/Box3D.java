package com.makers4.rendering.core;


/*******************************************************************************
 ** Immutable axis-aligned bounding box in 3D space.
 ** Defined by a position (minimum corner) and size (dimensions).
 *******************************************************************************/
public record Box3D(Vector3D position, Vector3D size)
{

   /*******************************************************************************
    ** Create a box at origin with given size.
    *******************************************************************************/
   public static Box3D atOrigin(double width, double height, double depth)
   {
      return new Box3D(Vector3D.origin(), new Vector3D(width, height, depth));
   }



   /*******************************************************************************
    ** Create a box at origin with given size vector.
    *******************************************************************************/
   public static Box3D atOrigin(Vector3D size)
   {
      return new Box3D(Vector3D.origin(), size);
   }



   /*******************************************************************************
    ** Create a box from position and size.
    *******************************************************************************/
   public static Box3D of(double x, double y, double z, double width, double height, double depth)
   {
      return new Box3D(new Vector3D(x, y, z), new Vector3D(width, height, depth));
   }



   /*******************************************************************************
    ** Get the minimum corner (same as position).
    *******************************************************************************/
   public Vector3D min()
   {
      return position;
   }



   /*******************************************************************************
    ** Get the maximum corner.
    *******************************************************************************/
   public Vector3D max()
   {
      return position.add(size);
   }



   /*******************************************************************************
    ** Get the center point of the box.
    *******************************************************************************/
   public Vector3D center()
   {
      return position.add(size.scale(0.5));
   }



   /*******************************************************************************
    ** Get the width (X dimension).
    *******************************************************************************/
   public double width()
   {
      return size.x();
   }



   /*******************************************************************************
    ** Get the height (Y dimension).
    *******************************************************************************/
   public double height()
   {
      return size.y();
   }



   /*******************************************************************************
    ** Get the depth (Z dimension).
    *******************************************************************************/
   public double depth()
   {
      return size.z();
   }



   /*******************************************************************************
    ** Translate (move) this box by a vector.
    *******************************************************************************/
   public Box3D translate(Vector3D offset)
   {
      return new Box3D(position.add(offset), size);
   }



   /*******************************************************************************
    ** Check if this box contains a point.
    *******************************************************************************/
   public boolean contains(Vector3D point)
   {
      Vector3D min = min();
      Vector3D max = max();
      return point.x() >= min.x() && point.x() <= max.x()
         && point.y() >= min.y() && point.y() <= max.y()
         && point.z() >= min.z() && point.z() <= max.z();
   }



   /*******************************************************************************
    ** Check if this box intersects with another box.
    *******************************************************************************/
   public boolean intersects(Box3D other)
   {
      Vector3D thisMin  = this.min();
      Vector3D thisMax  = this.max();
      Vector3D otherMin = other.min();
      Vector3D otherMax = other.max();

      return thisMin.x() <= otherMax.x() && thisMax.x() >= otherMin.x()
         && thisMin.y() <= otherMax.y() && thisMax.y() >= otherMin.y()
         && thisMin.z() <= otherMax.z() && thisMax.z() >= otherMin.z();
   }



   /*******************************************************************************
    ** Expand this box to include another box.
    *******************************************************************************/
   public Box3D union(Box3D other)
   {
      double minX = Math.min(this.position.x(), other.position.x());
      double minY = Math.min(this.position.y(), other.position.y());
      double minZ = Math.min(this.position.z(), other.position.z());

      double maxX = Math.max(this.max().x(), other.max().x());
      double maxY = Math.max(this.max().y(), other.max().y());
      double maxZ = Math.max(this.max().z(), other.max().z());

      return new Box3D(
         new Vector3D(minX, minY, minZ),
         new Vector3D(maxX - minX, maxY - minY, maxZ - minZ)
      );
   }



   /*******************************************************************************
    ** Format for display.
    *******************************************************************************/
   public String format()
   {
      return String.format("Box3D[pos=%s, size=%s]", position.format(), size.format());
   }



   @Override
   public String toString()
   {
      return format();
   }
}
