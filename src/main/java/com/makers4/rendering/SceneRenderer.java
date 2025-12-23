package com.makers4.rendering;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.makers4.rendering.camera.OrthographicCamera;
import com.makers4.rendering.camera.ViewDirection;
import com.makers4.rendering.core.Box3D;
import com.makers4.rendering.core.Vector3D;
import com.makers4.rendering.scene.RenderStyle;
import com.makers4.rendering.scene.SceneNode;


/*******************************************************************************
 ** Renders a scene graph to a 2D Graphics context using an orthographic camera.
 ** Traverses the scene graph, projects each node, and draws to the target.
 *******************************************************************************/
public class SceneRenderer
{
   private RenderSettings settings;



   /*******************************************************************************
    ** Constructor with settings.
    *******************************************************************************/
   public SceneRenderer(RenderSettings settings)
   {
      this.settings = settings;
   }



   /*******************************************************************************
    ** Render a scene to a Graphics2D context.
    *******************************************************************************/
   public void render(SceneNode rootNode, OrthographicCamera camera, Graphics2D g2d)
   {
      // Set up rendering hints for quality
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

      // Collect all nodes with geometry
      List<SceneNode> nodesToRender = new ArrayList<>();
      collectNodes(rootNode, nodesToRender);

      // Sort nodes by depth for proper overlapping (back to front)
      sortByDepth(nodesToRender, camera.getDirection());

      // Render each node
      for(SceneNode node : nodesToRender)
      {
         renderNode(node, camera, g2d);
      }
   }



   /*******************************************************************************
    ** Calculate the required canvas size for a scene.
    *******************************************************************************/
   public Dimension calculateCanvasSize(SceneNode rootNode, OrthographicCamera camera)
   {
      Box3D bounds = rootNode.calculateTotalBounds();

      double[] projectedSize = camera.calculateProjectedSize(bounds);
      double   margin        = settings.getMarginPoints();
      double   titleBlockHeight = settings.isShowTitleBlock() ? settings.toPoints(1.5) : 0;

      int width  = (int) Math.ceil(projectedSize[0] + 2 * margin);
      int height = (int) Math.ceil(projectedSize[1] + 2 * margin + titleBlockHeight);

      return new Dimension(width, height);
   }



   /*******************************************************************************
    ** Collect all nodes with geometry from the scene graph.
    *******************************************************************************/
   private void collectNodes(SceneNode node, List<SceneNode> result)
   {
      if(node.hasGeometry())
      {
         result.add(node);
      }
      for(SceneNode child : node.getChildren())
      {
         collectNodes(child, result);
      }
   }



   /*******************************************************************************
    ** Sort nodes by depth for proper z-ordering based on view direction.
    *******************************************************************************/
   private void sortByDepth(List<SceneNode> nodes, ViewDirection direction)
   {
      // Determine which axis represents depth for this view
      Comparator<SceneNode> comparator = (a, b) ->
      {
         Vector3D posA = a.getWorldPosition();
         Vector3D posB = b.getWorldPosition();

         double depthA, depthB;
         switch(direction)
         {
            case FRONT -> {
               depthA = posA.z();
               depthB = posB.z();
            }
            case BACK -> {
               depthA = -posA.z();
               depthB = -posB.z();
            }
            case LEFT -> {
               depthA = posA.x();
               depthB = posB.x();
            }
            case RIGHT -> {
               depthA = -posA.x();
               depthB = -posB.x();
            }
            case TOP -> {
               depthA = posA.y();
               depthB = posB.y();
            }
            case BOTTOM -> {
               depthA = -posA.y();
               depthB = -posB.y();
            }
            case ISOMETRIC -> {
               // For isometric, sort by combined x + z (back-to-front)
               depthA = posA.x() + posA.z();
               depthB = posB.x() + posB.z();
            }
            default -> {
               depthA = depthB = 0;
            }
         }
         return Double.compare(depthA, depthB);
      };

      nodes.sort(comparator);
   }



   /*******************************************************************************
    ** Render a single scene node.
    *******************************************************************************/
   private void renderNode(SceneNode node, OrthographicCamera camera, Graphics2D g2d)
   {
      Box3D worldBounds = node.getWorldBounds();

      if(camera.getDirection().isIsometric())
      {
         renderIsometricNode(node, worldBounds, camera, g2d);
      }
      else
      {
         renderOrthographicNode(node, worldBounds, camera, g2d);
      }
   }



   /*******************************************************************************
    ** Render a node in orthographic projection.
    *******************************************************************************/
   private void renderOrthographicNode(SceneNode node, Box3D bounds, OrthographicCamera camera, Graphics2D g2d)
   {
      // Project the box to 2D
      double[] projected = camera.projectBox(bounds);
      double   x         = projected[0];
      double   y         = projected[1];
      double   width     = projected[2];
      double   height    = projected[3];

      // Skip if too small to render
      if(width < 0.5 || height < 0.5)
      {
         return;
      }

      RenderStyle style = node.getStyle();

      // Fill
      Color fillColor = style.getFillColor();
      if(fillColor == null)
      {
         fillColor = settings.getFillColor();
      }
      if(fillColor != null)
      {
         g2d.setColor(fillColor);
         g2d.fill(new Rectangle2D.Double(x, y, width, height));
      }

      // Stroke
      Color strokeColor = style.getStrokeColor();
      if(strokeColor == null)
      {
         strokeColor = settings.getOutlineColor();
      }
      g2d.setColor(strokeColor);
      g2d.setStroke(new BasicStroke(style.getStrokeWidth()));
      g2d.draw(new Rectangle2D.Double(x, y, width, height));

      // Label
      if(style.isShowLabel() && settings.isShowPartLabels() && width > 20 && height > 15)
      {
         String label = node.getLabel();
         if(label != null && !label.isEmpty())
         {
            g2d.setColor(style.getLabelColor());
            g2d.setFont(new Font("SansSerif", Font.PLAIN, (int) style.getLabelFontSize()));

            // Truncate label if needed
            String displayLabel = truncateLabel(label, 12);

            // Center the label in the rectangle
            double labelX = x + width / 2 - displayLabel.length() * 3;
            double labelY = y + height / 2 + 3;
            g2d.drawString(displayLabel, (float) labelX, (float) labelY);
         }
      }
   }



   /*******************************************************************************
    ** Render a node in isometric projection.
    *******************************************************************************/
   private void renderIsometricNode(SceneNode node, Box3D bounds, OrthographicCamera camera, Graphics2D g2d)
   {
      Vector3D pos  = bounds.position();
      Vector3D size = bounds.size();

      double scale = camera.getScale();
      double cos30 = Math.cos(Math.toRadians(30));
      double sin30 = Math.sin(Math.toRadians(30));

      // Calculate the 8 corners of the box in isometric coordinates
      double[][] corners = calculateIsometricCorners(pos, size, scale, cos30, sin30, camera);

      RenderStyle style = node.getStyle();

      // Draw filled faces
      Color fillColor = style.getFillColor();
      if(fillColor == null)
      {
         fillColor = settings.getFillColor();
      }

      if(fillColor != null)
      {
         // Top face (lightest)
         g2d.setColor(adjustBrightness(fillColor, 1.05f));
         int[] topX = { (int) corners[4][0], (int) corners[5][0], (int) corners[6][0], (int) corners[7][0] };
         int[] topY = { (int) corners[4][1], (int) corners[5][1], (int) corners[6][1], (int) corners[7][1] };
         g2d.fillPolygon(topX, topY, 4);

         // Front face (medium)
         g2d.setColor(fillColor);
         int[] frontX = { (int) corners[0][0], (int) corners[1][0], (int) corners[5][0], (int) corners[4][0] };
         int[] frontY = { (int) corners[0][1], (int) corners[1][1], (int) corners[5][1], (int) corners[4][1] };
         g2d.fillPolygon(frontX, frontY, 4);

         // Right face (darker)
         g2d.setColor(adjustBrightness(fillColor, 0.9f));
         int[] rightX = { (int) corners[1][0], (int) corners[2][0], (int) corners[6][0], (int) corners[5][0] };
         int[] rightY = { (int) corners[1][1], (int) corners[2][1], (int) corners[6][1], (int) corners[5][1] };
         g2d.fillPolygon(rightX, rightY, 4);
      }

      // Draw outlines
      Color strokeColor = style.getStrokeColor();
      if(strokeColor == null)
      {
         strokeColor = settings.getOutlineColor();
      }
      g2d.setColor(strokeColor);
      g2d.setStroke(new BasicStroke(style.getStrokeWidth()));

      // Front face
      drawLine(g2d, corners[0], corners[1]);
      drawLine(g2d, corners[1], corners[5]);
      drawLine(g2d, corners[5], corners[4]);
      drawLine(g2d, corners[4], corners[0]);

      // Top face
      drawLine(g2d, corners[4], corners[5]);
      drawLine(g2d, corners[5], corners[6]);
      drawLine(g2d, corners[6], corners[7]);
      drawLine(g2d, corners[7], corners[4]);

      // Right face vertical edges
      drawLine(g2d, corners[1], corners[2]);
      drawLine(g2d, corners[2], corners[6]);
   }



   /*******************************************************************************
    ** Calculate isometric corner positions.
    *******************************************************************************/
   private double[][] calculateIsometricCorners(Vector3D pos, Vector3D size,
                                                 double scale, double cos30, double sin30,
                                                 OrthographicCamera camera)
   {
      double px = pos.x();
      double py = pos.y();
      double pz = pos.z();
      double w  = size.x();
      double h  = size.y();
      double d  = size.z();

      double offsetX = camera.getOffsetX();
      double offsetY = camera.getOffsetY();

      // Base isometric coordinates for front-bottom-left corner
      double isoX0 = offsetX + (px * cos30 - pz * cos30) * scale;
      double isoY0 = offsetY + (py + px * sin30 + pz * sin30) * scale;

      double[][] corners = new double[8][2];

      // Bottom face corners
      corners[0] = new double[] { isoX0, isoY0 };  // front-left
      corners[1] = new double[] { isoX0 + w * cos30 * scale, isoY0 + w * sin30 * scale };  // front-right
      corners[2] = new double[] { isoX0 + w * cos30 * scale - d * cos30 * scale,
         isoY0 + w * sin30 * scale + d * sin30 * scale };  // back-right
      corners[3] = new double[] { isoX0 - d * cos30 * scale, isoY0 + d * sin30 * scale };  // back-left

      // Top face corners (same as bottom + height)
      double hScale = h * scale;
      corners[4] = new double[] { corners[0][0], corners[0][1] + hScale };  // front-left-top
      corners[5] = new double[] { corners[1][0], corners[1][1] + hScale };  // front-right-top
      corners[6] = new double[] { corners[2][0], corners[2][1] + hScale };  // back-right-top
      corners[7] = new double[] { corners[3][0], corners[3][1] + hScale };  // back-left-top

      return corners;
   }



   /*******************************************************************************
    ** Draw a line between two corner points.
    *******************************************************************************/
   private void drawLine(Graphics2D g2d, double[] p1, double[] p2)
   {
      g2d.drawLine((int) p1[0], (int) p1[1], (int) p2[0], (int) p2[1]);
   }



   /*******************************************************************************
    ** Adjust the brightness of a color.
    *******************************************************************************/
   private Color adjustBrightness(Color color, float factor)
   {
      int r = Math.min(255, Math.max(0, (int) (color.getRed() * factor)));
      int g = Math.min(255, Math.max(0, (int) (color.getGreen() * factor)));
      int b = Math.min(255, Math.max(0, (int) (color.getBlue() * factor)));
      return new Color(r, g, b);
   }



   /*******************************************************************************
    ** Truncate a label if too long.
    *******************************************************************************/
   private String truncateLabel(String label, int maxLen)
   {
      if(label.length() <= maxLen)
      {
         return label;
      }
      return label.substring(0, maxLen - 2) + "..";
   }



   /*******************************************************************************
    ** Getter for settings
    *******************************************************************************/
   public RenderSettings getSettings()
   {
      return settings;
   }



   /*******************************************************************************
    ** Fluent setter for settings
    *******************************************************************************/
   public SceneRenderer withSettings(RenderSettings settings)
   {
      this.settings = settings;
      return this;
   }
}
