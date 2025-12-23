package com.makers4.rendering.scene;


import java.awt.Color;


/*******************************************************************************
 ** Styling options for rendering a scene node.
 ** Controls fill color, stroke color, line width, and label display.
 *******************************************************************************/
public class RenderStyle
{
   private Color   fillColor;
   private Color   strokeColor;
   private float   strokeWidth;
   private boolean showLabel;
   private Color   labelColor;
   private float   labelFontSize;



   /*******************************************************************************
    ** Default constructor with standard styling.
    *******************************************************************************/
   public RenderStyle()
   {
      this.fillColor = new Color(245, 245, 245);  // Light gray fill
      this.strokeColor = Color.BLACK;
      this.strokeWidth = 1.0f;
      this.showLabel = true;
      this.labelColor = Color.BLACK;
      this.labelFontSize = 8.0f;
   }



   /*******************************************************************************
    ** Create a style with no fill (outline only).
    *******************************************************************************/
   public static RenderStyle outline()
   {
      RenderStyle style = new RenderStyle();
      style.fillColor = null;
      return style;
   }



   /*******************************************************************************
    ** Create a style with light wood-like fill.
    *******************************************************************************/
   public static RenderStyle woodPanel()
   {
      RenderStyle style = new RenderStyle();
      style.fillColor = new Color(240, 230, 210);  // Light wood color
      return style;
   }



   /*******************************************************************************
    ** Create a blueprint-style rendering.
    *******************************************************************************/
   public static RenderStyle blueprint()
   {
      RenderStyle style = new RenderStyle();
      style.fillColor = null;
      style.strokeColor = new Color(20, 40, 80);  // Dark blue
      style.strokeWidth = 0.5f;
      style.labelColor = new Color(20, 40, 80);
      return style;
   }



   /*******************************************************************************
    ** Getter for fillColor
    *******************************************************************************/
   public Color getFillColor()
   {
      return fillColor;
   }



   /*******************************************************************************
    ** Fluent setter for fillColor
    *******************************************************************************/
   public RenderStyle withFillColor(Color fillColor)
   {
      this.fillColor = fillColor;
      return this;
   }



   /*******************************************************************************
    ** Getter for strokeColor
    *******************************************************************************/
   public Color getStrokeColor()
   {
      return strokeColor;
   }



   /*******************************************************************************
    ** Fluent setter for strokeColor
    *******************************************************************************/
   public RenderStyle withStrokeColor(Color strokeColor)
   {
      this.strokeColor = strokeColor;
      return this;
   }



   /*******************************************************************************
    ** Getter for strokeWidth
    *******************************************************************************/
   public float getStrokeWidth()
   {
      return strokeWidth;
   }



   /*******************************************************************************
    ** Fluent setter for strokeWidth
    *******************************************************************************/
   public RenderStyle withStrokeWidth(float strokeWidth)
   {
      this.strokeWidth = strokeWidth;
      return this;
   }



   /*******************************************************************************
    ** Getter for showLabel
    *******************************************************************************/
   public boolean isShowLabel()
   {
      return showLabel;
   }



   /*******************************************************************************
    ** Fluent setter for showLabel
    *******************************************************************************/
   public RenderStyle withShowLabel(boolean showLabel)
   {
      this.showLabel = showLabel;
      return this;
   }



   /*******************************************************************************
    ** Getter for labelColor
    *******************************************************************************/
   public Color getLabelColor()
   {
      return labelColor;
   }



   /*******************************************************************************
    ** Fluent setter for labelColor
    *******************************************************************************/
   public RenderStyle withLabelColor(Color labelColor)
   {
      this.labelColor = labelColor;
      return this;
   }



   /*******************************************************************************
    ** Getter for labelFontSize
    *******************************************************************************/
   public float getLabelFontSize()
   {
      return labelFontSize;
   }



   /*******************************************************************************
    ** Fluent setter for labelFontSize
    *******************************************************************************/
   public RenderStyle withLabelFontSize(float labelFontSize)
   {
      this.labelFontSize = labelFontSize;
      return this;
   }
}
