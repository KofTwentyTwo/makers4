package com.makers4.rendering.scene;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.makers4.rendering.core.Box3D;
import com.makers4.rendering.core.Vector3D;


/*******************************************************************************
 ** A node in the scene graph representing an object in 3D space.
 ** Each node has a position and size relative to its parent.
 ** Nodes can have children, forming a hierarchical tree.
 *******************************************************************************/
public class SceneNode
{
   private String           name;
   private String           label;
   private Vector3D         position;
   private Vector3D         size;
   private List<SceneNode>  children;
   private RenderStyle      style;
   private SceneNode        parent;



   /*******************************************************************************
    ** Constructor with name.
    *******************************************************************************/
   public SceneNode(String name)
   {
      this.name = name;
      this.label = name;
      this.position = Vector3D.origin();
      this.size = Vector3D.origin();
      this.children = new ArrayList<>();
      this.style = new RenderStyle();
   }



   /*******************************************************************************
    ** Add a child node to this node.
    *******************************************************************************/
   public SceneNode addChild(SceneNode child)
   {
      child.parent = this;
      this.children.add(child);
      return this;
   }



   /*******************************************************************************
    ** Get the local bounds of this node (position + size).
    *******************************************************************************/
   public Box3D getLocalBounds()
   {
      return new Box3D(position, size);
   }



   /*******************************************************************************
    ** Get the world position by accumulating parent positions.
    *******************************************************************************/
   public Vector3D getWorldPosition()
   {
      if(parent == null)
      {
         return position;
      }
      return parent.getWorldPosition().add(position);
   }



   /*******************************************************************************
    ** Get the world bounds of this node.
    *******************************************************************************/
   public Box3D getWorldBounds()
   {
      return new Box3D(getWorldPosition(), size);
   }



   /*******************************************************************************
    ** Calculate the bounding box that contains this node and all children.
    *******************************************************************************/
   public Box3D calculateTotalBounds()
   {
      Box3D bounds = getWorldBounds();

      for(SceneNode child : children)
      {
         Box3D childBounds = child.calculateTotalBounds();
         bounds = bounds.union(childBounds);
      }

      return bounds;
   }



   /*******************************************************************************
    ** Check if this node has geometry to render (non-zero size).
    *******************************************************************************/
   public boolean hasGeometry()
   {
      return size.x() > 0 || size.y() > 0 || size.z() > 0;
   }



   /*******************************************************************************
    ** Getter for name
    *******************************************************************************/
   public String getName()
   {
      return name;
   }



   /*******************************************************************************
    ** Fluent setter for name
    *******************************************************************************/
   public SceneNode withName(String name)
   {
      this.name = name;
      return this;
   }



   /*******************************************************************************
    ** Getter for label
    *******************************************************************************/
   public String getLabel()
   {
      return label;
   }



   /*******************************************************************************
    ** Fluent setter for label
    *******************************************************************************/
   public SceneNode withLabel(String label)
   {
      this.label = label;
      return this;
   }



   /*******************************************************************************
    ** Getter for position
    *******************************************************************************/
   public Vector3D getPosition()
   {
      return position;
   }



   /*******************************************************************************
    ** Fluent setter for position
    *******************************************************************************/
   public SceneNode withPosition(Vector3D position)
   {
      this.position = position;
      return this;
   }



   /*******************************************************************************
    ** Fluent setter for position with individual coordinates.
    *******************************************************************************/
   public SceneNode withPosition(double x, double y, double z)
   {
      this.position = new Vector3D(x, y, z);
      return this;
   }



   /*******************************************************************************
    ** Getter for size
    *******************************************************************************/
   public Vector3D getSize()
   {
      return size;
   }



   /*******************************************************************************
    ** Fluent setter for size
    *******************************************************************************/
   public SceneNode withSize(Vector3D size)
   {
      this.size = size;
      return this;
   }



   /*******************************************************************************
    ** Fluent setter for size with individual dimensions.
    *******************************************************************************/
   public SceneNode withSize(double width, double height, double depth)
   {
      this.size = new Vector3D(width, height, depth);
      return this;
   }



   /*******************************************************************************
    ** Getter for children (unmodifiable view)
    *******************************************************************************/
   public List<SceneNode> getChildren()
   {
      return Collections.unmodifiableList(children);
   }



   /*******************************************************************************
    ** Getter for style
    *******************************************************************************/
   public RenderStyle getStyle()
   {
      return style;
   }



   /*******************************************************************************
    ** Fluent setter for style
    *******************************************************************************/
   public SceneNode withStyle(RenderStyle style)
   {
      this.style = style;
      return this;
   }



   /*******************************************************************************
    ** Getter for parent
    *******************************************************************************/
   public SceneNode getParent()
   {
      return parent;
   }



   /*******************************************************************************
    ** Format for display.
    *******************************************************************************/
   public String format()
   {
      return String.format("SceneNode[%s, pos=%s, size=%s, children=%d]",
         name, position.format(), size.format(), children.size());
   }



   @Override
   public String toString()
   {
      return format();
   }
}
