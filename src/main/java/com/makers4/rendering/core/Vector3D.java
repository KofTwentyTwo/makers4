package com.makers4.rendering.core;


/*******************************************************************************
 ** Immutable 3D vector representing a point or direction in 3D space.
 ** Uses double precision for coordinates.
 ** Coordinate system:
 **   X = left-right (width)
 **   Y = bottom-top (height)
 **   Z = front-back (depth)
 *******************************************************************************/
public record Vector3D(double x, double y, double z)
{

   /*******************************************************************************
    ** Origin point (0, 0, 0).
    *******************************************************************************/
   public static Vector3D origin()
   {
      return new Vector3D(0, 0, 0);
   }



   /*******************************************************************************
    ** Create a vector from inches values.
    *******************************************************************************/
   public static Vector3D inches(double x, double y, double z)
   {
      return new Vector3D(x, y, z);
   }



   /*******************************************************************************
    ** Create a vector from millimeter values (converts to inches internally).
    *******************************************************************************/
   public static Vector3D mm(double x, double y, double z)
   {
      double factor = 1.0 / 25.4;
      return new Vector3D(x * factor, y * factor, z * factor);
   }



   /*******************************************************************************
    ** Create a vector from millimeter Integer values (converts to inches internally).
    *******************************************************************************/
   public static Vector3D mm(Integer x, Integer y, Integer z)
   {
      double factor = 1.0 / 25.4;
      return new Vector3D(
         (x != null ? x : 0) * factor,
         (y != null ? y : 0) * factor,
         (z != null ? z : 0) * factor
      );
   }



   /*******************************************************************************
    ** Add another vector to this one.
    *******************************************************************************/
   public Vector3D add(Vector3D other)
   {
      return new Vector3D(x + other.x, y + other.y, z + other.z);
   }



   /*******************************************************************************
    ** Subtract another vector from this one.
    *******************************************************************************/
   public Vector3D subtract(Vector3D other)
   {
      return new Vector3D(x - other.x, y - other.y, z - other.z);
   }



   /*******************************************************************************
    ** Scale this vector by a scalar value.
    *******************************************************************************/
   public Vector3D scale(double scalar)
   {
      return new Vector3D(x * scalar, y * scalar, z * scalar);
   }



   /*******************************************************************************
    ** Get the length (magnitude) of this vector.
    *******************************************************************************/
   public double length()
   {
      return Math.sqrt(x * x + y * y + z * z);
   }



   /*******************************************************************************
    ** Normalize this vector (make it unit length).
    *******************************************************************************/
   public Vector3D normalize()
   {
      double len = length();
      if(len == 0)
      {
         return this;
      }
      return new Vector3D(x / len, y / len, z / len);
   }



   /*******************************************************************************
    ** Dot product with another vector.
    *******************************************************************************/
   public double dot(Vector3D other)
   {
      return x * other.x + y * other.y + z * other.z;
   }



   /*******************************************************************************
    ** Cross product with another vector.
    *******************************************************************************/
   public Vector3D cross(Vector3D other)
   {
      return new Vector3D(
         y * other.z - z * other.y,
         z * other.x - x * other.z,
         x * other.y - y * other.x
      );
   }



   /*******************************************************************************
    ** Get the maximum component value.
    *******************************************************************************/
   public double maxComponent()
   {
      return Math.max(Math.max(x, y), z);
   }



   /*******************************************************************************
    ** Get the minimum component value.
    *******************************************************************************/
   public double minComponent()
   {
      return Math.min(Math.min(x, y), z);
   }



   /*******************************************************************************
    ** Format for display.
    *******************************************************************************/
   public String format()
   {
      return String.format("(%.3f, %.3f, %.3f)", x, y, z);
   }



   @Override
   public String toString()
   {
      return format();
   }
}
