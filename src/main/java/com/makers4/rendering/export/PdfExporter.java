package com.makers4.rendering.export;


import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.makers4.rendering.RenderSettings;
import com.makers4.rendering.camera.OrthographicCamera;
import com.makers4.rendering.camera.ViewDirection;
import com.makers4.rendering.core.Box3D;
import com.makers4.rendering.core.Dimension;
import com.makers4.rendering.core.Vector3D;
import com.makers4.rendering.scene.RenderStyle;
import com.makers4.rendering.scene.SceneNode;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;


/*******************************************************************************
 ** Exporter for PDF documents.
 ** Uses Apache PDFBox for PDF generation.
 ** Supports multi-page documents with multiple views.
 *******************************************************************************/
public class PdfExporter implements Exporter
{
   // Isometric projection constants
   private static final double ISO_COS = Math.cos(Math.toRadians(30));
   private static final double ISO_SIN = Math.sin(Math.toRadians(30));



   /*******************************************************************************
    ** Export a single view to PDF.
    *******************************************************************************/
   @Override
   public void exportView(SceneNode sceneRoot, ViewDirection viewDirection,
                          RenderSettings settings, OutputStream output) throws ExportException
   {
      exportViews(sceneRoot, List.of(viewDirection), settings, output);
   }



   /*******************************************************************************
    ** Export multiple views to a multi-page PDF.
    *******************************************************************************/
   @Override
   public void exportViews(SceneNode sceneRoot, List<ViewDirection> viewDirections,
                           RenderSettings settings, OutputStream output) throws ExportException
   {
      // Check if blueprint mode is enabled
      if(settings.isUseFixedPageSize() && settings.isUseTraditionalTitleBlock())
      {
         exportBlueprintViews(sceneRoot, viewDirections, settings, output);
         return;
      }

      // Dynamic-size implementation
      try(PDDocument document = new PDDocument())
      {
         for(ViewDirection viewDirection : viewDirections)
         {
            addViewPage(document, sceneRoot, viewDirection, settings);
         }

         document.save(output);
      }
      catch(Exception e)
      {
         throw new ExportException("Failed to generate PDF", e);
      }
   }



   /*******************************************************************************
    ** Export views in traditional architectural blueprint style.
    ** Uses fixed page size with borders, grid references, and title block.
    *******************************************************************************/
   private void exportBlueprintViews(SceneNode sceneRoot, List<ViewDirection> viewDirections,
                                     RenderSettings settings, OutputStream output) throws ExportException
   {
      try(PDDocument document = new PDDocument())
      {
         int sheetNumber = 1;
         int totalSheets = viewDirections.size();

         for(ViewDirection viewDirection : viewDirections)
         {
            addBlueprintPage(document, sceneRoot, viewDirection, settings,
               String.valueOf(sheetNumber), String.valueOf(totalSheets));
            sheetNumber++;
         }

         document.save(output);
      }
      catch(Exception e)
      {
         throw new ExportException("Failed to generate blueprint PDF", e);
      }
   }



   /*******************************************************************************
    ** Add a page in architectural blueprint style.
    *******************************************************************************/
   private void addBlueprintPage(PDDocument document, SceneNode sceneRoot,
                                 ViewDirection viewDirection, RenderSettings settings,
                                 String sheetNum, String totalSheets) throws Exception
   {
      // Fixed page size: 11x8.5" landscape
      PDRectangle pageSize = new PDRectangle(BlueprintElements.PAGE_WIDTH,
         BlueprintElements.PAGE_HEIGHT);
      PDPage page = new PDPage(pageSize);
      document.addPage(page);

      // Create settings with sheet info
      RenderSettings pageSettings = RenderSettings.architecturalBlueprint()
         .withCompanyName(settings.getCompanyName())
         .withProjectName(settings.getProjectName())
         .withDrawnBy(settings.getDrawnBy())
         .withCheckedBy(settings.getCheckedBy())
         .withScaleNotation(settings.getScaleNotation())
         .withDate(settings.getDate())
         .withRevision(settings.getRevisionNumber(), settings.getRevisionDescription())
         .withSheetInfo(sheetNum, totalSheets);

      BlueprintElements blueprint = new BlueprintElements(pageSettings);

      try(PDPageContentStream cs = new PDPageContentStream(document, page))
      {
         // 1. White background
         cs.setNonStrokingColor(1f, 1f, 1f);
         cs.addRect(0, 0, pageSize.getWidth(), pageSize.getHeight());
         cs.fill();

         // 2. Draw architectural borders
         if(pageSettings.isShowArchitecturalBorder())
         {
            blueprint.drawBorders(cs);
         }

         // 3. Draw grid reference system
         if(pageSettings.isShowGridReferences())
         {
            blueprint.drawGridReferences(cs);
         }

         // 4. Calculate auto-scale and center offset
         Box3D  bounds = sceneRoot.calculateTotalBounds();
         double viewWidth, viewHeight;

         // Get view-specific dimensions
         switch(viewDirection)
         {
            case FRONT, BACK ->
            {
               viewWidth = bounds.width();
               viewHeight = bounds.height();
            }
            case LEFT, RIGHT ->
            {
               viewWidth = bounds.depth();
               viewHeight = bounds.height();
            }
            case TOP, BOTTOM ->
            {
               viewWidth = bounds.width();
               viewHeight = bounds.depth();
            }
            case ISOMETRIC ->
            {
               // Isometric bounding box
               viewWidth = (bounds.width() + bounds.depth()) * ISO_COS;
               viewHeight = bounds.height() + (bounds.width() + bounds.depth()) * ISO_SIN / 2;
            }
            default ->
            {
               viewWidth = viewHeight = 10;
            }
         }

         double   autoScale = blueprint.calculateAutoScale(viewWidth, viewHeight);
         double[] offset    = blueprint.calculateCenterOffset(viewWidth, viewHeight, autoScale);

         // 5. Transform and draw scene content
         cs.saveGraphicsState();
         cs.transform(org.apache.pdfbox.util.Matrix.getTranslateInstance(
            (float) offset[0], (float) offset[1]));

         // Render the scene graph
         renderSceneToContentStream(cs, sceneRoot, viewDirection, autoScale, pageSettings);

         cs.restoreGraphicsState();

         // 6. Draw dimensions (outside the transform) - skip for isometric
         if(pageSettings.isShowDimensions() && !viewDirection.isIsometric())
         {
            drawBlueprintDimensions(cs, bounds, viewDirection, autoScale, offset);
         }

         // 7. Draw traditional title block
         if(pageSettings.isUseTraditionalTitleBlock())
         {
            String label = sceneRoot.getLabel() != null ? sceneRoot.getLabel() : sceneRoot.getName();
            blueprint.drawTitleBlock(cs, label, viewDirection.getDisplayName());
         }
      }
   }



   /*******************************************************************************
    ** Add a page with dynamic sizing.
    *******************************************************************************/
   private void addViewPage(PDDocument document, SceneNode sceneRoot,
                            ViewDirection viewDirection, RenderSettings settings) throws Exception
   {
      // Calculate canvas size
      OrthographicCamera camera = OrthographicCamera.forView(viewDirection)
         .withScale(settings.getScale());

      Box3D    bounds        = sceneRoot.calculateTotalBounds();
      double[] projectedSize = camera.calculateProjectedSize(bounds);
      double   margin        = settings.getMarginPoints();
      double   titleBlockHeight = settings.isShowTitleBlock() ? settings.toPoints(1.5) : 0;

      int width  = (int) Math.ceil(projectedSize[0] + 2 * margin);
      int height = (int) Math.ceil(projectedSize[1] + 2 * margin + titleBlockHeight);

      // Create page
      PDRectangle pageSize = new PDRectangle(width, height);
      PDPage      page     = new PDPage(pageSize);
      document.addPage(page);

      try(PDPageContentStream cs = new PDPageContentStream(document, page))
      {
         // Draw background
         java.awt.Color bgColor = settings.getBackgroundColor();
         cs.setNonStrokingColor(bgColor.getRed() / 255f, bgColor.getGreen() / 255f, bgColor.getBlue() / 255f);
         cs.addRect(0, 0, pageSize.getWidth(), pageSize.getHeight());
         cs.fill();

         // Set up coordinate transform
         cs.saveGraphicsState();
         cs.transform(org.apache.pdfbox.util.Matrix.getTranslateInstance(
            (float) margin, (float) (margin + titleBlockHeight)));

         // Render the scene graph
         renderSceneToContentStream(cs, sceneRoot, viewDirection, settings.getScale(), settings);

         cs.restoreGraphicsState();

         // Draw overall dimensions
         if(settings.isShowDimensions())
         {
            drawDimensions(cs, bounds, viewDirection, settings, width, height);
         }

         // Draw title block
         if(settings.isShowTitleBlock())
         {
            drawTitleBlock(cs, sceneRoot, viewDirection, settings, width, height);
         }
      }
   }



   /*******************************************************************************
    ** Render the scene graph to a PDF content stream.
    *******************************************************************************/
   private void renderSceneToContentStream(PDPageContentStream cs, SceneNode sceneRoot,
                                           ViewDirection viewDirection, double scale,
                                           RenderSettings settings) throws Exception
   {
      // Collect all nodes with geometry
      List<SceneNode> nodesToRender = new ArrayList<>();
      collectNodes(sceneRoot, nodesToRender);

      // Sort by depth for proper z-ordering
      sortByDepth(nodesToRender, viewDirection);

      // Set up stroke
      cs.setLineWidth(settings.getOutlineStrokeWidth());

      // Render each node
      for(SceneNode node : nodesToRender)
      {
         if(viewDirection.isIsometric())
         {
            renderIsometricNode(cs, node, scale, settings);
         }
         else
         {
            renderOrthographicNode(cs, node, viewDirection, scale, settings);
         }
      }
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
    ** Sort nodes by depth for proper z-ordering.
    *******************************************************************************/
   private void sortByDepth(List<SceneNode> nodes, ViewDirection direction)
   {
      Comparator<SceneNode> comparator = (a, b) ->
      {
         Vector3D posA = a.getWorldPosition();
         Vector3D posB = b.getWorldPosition();

         double depthA, depthB;
         switch(direction)
         {
            case FRONT ->
            {
               depthA = posA.z();
               depthB = posB.z();
            }
            case BACK ->
            {
               depthA = -posA.z();
               depthB = -posB.z();
            }
            case LEFT ->
            {
               depthA = posA.x();
               depthB = posB.x();
            }
            case RIGHT ->
            {
               depthA = -posA.x();
               depthB = -posB.x();
            }
            case TOP ->
            {
               depthA = posA.y();
               depthB = posB.y();
            }
            case BOTTOM ->
            {
               depthA = -posA.y();
               depthB = -posB.y();
            }
            case ISOMETRIC ->
            {
               depthA = posA.x() + posA.z();
               depthB = posB.x() + posB.z();
            }
            default ->
            {
               depthA = depthB = 0;
            }
         }
         return Double.compare(depthA, depthB);
      };

      nodes.sort(comparator);
   }



   /*******************************************************************************
    ** Render a node in orthographic projection.
    *******************************************************************************/
   private void renderOrthographicNode(PDPageContentStream cs, SceneNode node,
                                       ViewDirection viewDirection, double scale,
                                       RenderSettings settings) throws Exception
   {
      Box3D bounds = node.getWorldBounds();

      // Project based on view direction
      double x, y, w, h;
      switch(viewDirection)
      {
         case FRONT, BACK ->
         {
            x = bounds.position().x() * scale;
            y = bounds.position().y() * scale;
            w = bounds.width() * scale;
            h = bounds.height() * scale;
         }
         case LEFT, RIGHT ->
         {
            x = bounds.position().z() * scale;
            y = bounds.position().y() * scale;
            w = bounds.depth() * scale;
            h = bounds.height() * scale;
         }
         case TOP, BOTTOM ->
         {
            x = bounds.position().x() * scale;
            y = bounds.position().z() * scale;
            w = bounds.width() * scale;
            h = bounds.depth() * scale;
         }
         default ->
         {
            return;
         }
      }

      // Skip if too small
      if(w < 0.5 || h < 0.5)
      {
         return;
      }

      RenderStyle style = node.getStyle();

      // Fill
      java.awt.Color fillColor = style.getFillColor();
      if(fillColor == null)
      {
         fillColor = settings.getFillColor();
      }
      if(fillColor != null)
      {
         cs.setNonStrokingColor(fillColor.getRed() / 255f,
            fillColor.getGreen() / 255f, fillColor.getBlue() / 255f);
         cs.addRect((float) x, (float) y, (float) w, (float) h);
         cs.fill();
      }

      // Stroke
      java.awt.Color strokeColor = style.getStrokeColor();
      if(strokeColor == null)
      {
         strokeColor = settings.getOutlineColor();
      }
      cs.setStrokingColor(strokeColor.getRed() / 255f,
         strokeColor.getGreen() / 255f, strokeColor.getBlue() / 255f);
      cs.setLineWidth(style.getStrokeWidth());
      cs.addRect((float) x, (float) y, (float) w, (float) h);
      cs.stroke();

      // Label
      if(style.isShowLabel() && settings.isShowPartLabels() && w > 20 && h > 15)
      {
         String label = node.getLabel();
         if(label != null && !label.isEmpty())
         {
            cs.beginText();
            cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 8);
            java.awt.Color labelColor = style.getLabelColor();
            cs.setNonStrokingColor(labelColor.getRed() / 255f,
               labelColor.getGreen() / 255f, labelColor.getBlue() / 255f);
            cs.newLineAtOffset((float) (x + w / 2 - 15), (float) (y + h / 2 - 3));
            cs.showText(truncateLabel(label, 12));
            cs.endText();
         }
      }
   }



   /*******************************************************************************
    ** Render a node in isometric projection.
    *******************************************************************************/
   private void renderIsometricNode(PDPageContentStream cs, SceneNode node,
                                    double scale, RenderSettings settings) throws Exception
   {
      Box3D bounds = node.getWorldBounds();

      double px = bounds.position().x();
      double py = bounds.position().y();
      double pz = bounds.position().z();
      double w  = bounds.width();
      double h  = bounds.height();
      double d  = bounds.depth();

      // Offset for isometric origin
      double offsetX = 50;  // Shift right to account for leftward depth projection

      // Calculate isometric corner positions
      double isoX0 = offsetX + (px * ISO_COS - pz * ISO_COS) * scale;
      double isoY0 = (py + px * ISO_SIN + pz * ISO_SIN) * scale;

      double[][] corners = new double[8][2];

      // Bottom face corners
      corners[0] = new double[] { isoX0, isoY0 };  // front-left
      corners[1] = new double[] { isoX0 + w * ISO_COS * scale, isoY0 + w * ISO_SIN * scale };  // front-right
      corners[2] = new double[] { isoX0 + w * ISO_COS * scale - d * ISO_COS * scale,
         isoY0 + w * ISO_SIN * scale + d * ISO_SIN * scale };  // back-right
      corners[3] = new double[] { isoX0 - d * ISO_COS * scale, isoY0 + d * ISO_SIN * scale };  // back-left

      // Top face corners
      double hScale = h * scale;
      corners[4] = new double[] { corners[0][0], corners[0][1] + hScale };  // front-left-top
      corners[5] = new double[] { corners[1][0], corners[1][1] + hScale };  // front-right-top
      corners[6] = new double[] { corners[2][0], corners[2][1] + hScale };  // back-right-top
      corners[7] = new double[] { corners[3][0], corners[3][1] + hScale };  // back-left-top

      RenderStyle style = node.getStyle();

      // Draw filled faces
      java.awt.Color fillColor = style.getFillColor();
      if(fillColor == null)
      {
         fillColor = settings.getFillColor();
      }

      if(fillColor != null)
      {
         // Top face (lightest)
         cs.setNonStrokingColor(
            Math.min(1f, fillColor.getRed() / 255f * 1.05f),
            Math.min(1f, fillColor.getGreen() / 255f * 1.05f),
            Math.min(1f, fillColor.getBlue() / 255f * 1.05f));
         cs.moveTo((float) corners[4][0], (float) corners[4][1]);
         cs.lineTo((float) corners[5][0], (float) corners[5][1]);
         cs.lineTo((float) corners[6][0], (float) corners[6][1]);
         cs.lineTo((float) corners[7][0], (float) corners[7][1]);
         cs.closePath();
         cs.fill();

         // Front face (medium)
         cs.setNonStrokingColor(fillColor.getRed() / 255f,
            fillColor.getGreen() / 255f, fillColor.getBlue() / 255f);
         cs.moveTo((float) corners[0][0], (float) corners[0][1]);
         cs.lineTo((float) corners[1][0], (float) corners[1][1]);
         cs.lineTo((float) corners[5][0], (float) corners[5][1]);
         cs.lineTo((float) corners[4][0], (float) corners[4][1]);
         cs.closePath();
         cs.fill();

         // Right face (darker)
         cs.setNonStrokingColor(
            fillColor.getRed() / 255f * 0.9f,
            fillColor.getGreen() / 255f * 0.9f,
            fillColor.getBlue() / 255f * 0.9f);
         cs.moveTo((float) corners[1][0], (float) corners[1][1]);
         cs.lineTo((float) corners[2][0], (float) corners[2][1]);
         cs.lineTo((float) corners[6][0], (float) corners[6][1]);
         cs.lineTo((float) corners[5][0], (float) corners[5][1]);
         cs.closePath();
         cs.fill();
      }

      // Draw outlines
      java.awt.Color strokeColor = style.getStrokeColor();
      if(strokeColor == null)
      {
         strokeColor = settings.getOutlineColor();
      }
      cs.setStrokingColor(strokeColor.getRed() / 255f,
         strokeColor.getGreen() / 255f, strokeColor.getBlue() / 255f);
      cs.setLineWidth(style.getStrokeWidth());

      // Front face edges
      cs.moveTo((float) corners[0][0], (float) corners[0][1]);
      cs.lineTo((float) corners[1][0], (float) corners[1][1]);
      cs.lineTo((float) corners[5][0], (float) corners[5][1]);
      cs.lineTo((float) corners[4][0], (float) corners[4][1]);
      cs.closePath();
      cs.stroke();

      // Top face edges
      cs.moveTo((float) corners[4][0], (float) corners[4][1]);
      cs.lineTo((float) corners[5][0], (float) corners[5][1]);
      cs.lineTo((float) corners[6][0], (float) corners[6][1]);
      cs.lineTo((float) corners[7][0], (float) corners[7][1]);
      cs.closePath();
      cs.stroke();

      // Right face vertical edges
      cs.moveTo((float) corners[1][0], (float) corners[1][1]);
      cs.lineTo((float) corners[2][0], (float) corners[2][1]);
      cs.lineTo((float) corners[6][0], (float) corners[6][1]);
      cs.stroke();
   }



   /*******************************************************************************
    ** Draw dimension annotations for blueprint style.
    *******************************************************************************/
   private void drawBlueprintDimensions(PDPageContentStream cs, Box3D bounds,
                                        ViewDirection viewDirection, double scale,
                                        double[] offset) throws Exception
   {
      // Get view-specific dimensions
      String widthText, heightText;
      double drawWidth, drawHeight;

      switch(viewDirection)
      {
         case FRONT, BACK ->
         {
            widthText = Dimension.inches(bounds.width()).formatFractional();
            heightText = Dimension.inches(bounds.height()).formatFractional();
            drawWidth = bounds.width() * scale;
            drawHeight = bounds.height() * scale;
         }
         case LEFT, RIGHT ->
         {
            widthText = Dimension.inches(bounds.depth()).formatFractional();
            heightText = Dimension.inches(bounds.height()).formatFractional();
            drawWidth = bounds.depth() * scale;
            drawHeight = bounds.height() * scale;
         }
         case TOP, BOTTOM ->
         {
            widthText = Dimension.inches(bounds.width()).formatFractional();
            heightText = Dimension.inches(bounds.depth()).formatFractional();
            drawWidth = bounds.width() * scale;
            drawHeight = bounds.depth() * scale;
         }
         default ->
         {
            return;
         }
      }

      // Dark blue for dimensions
      cs.setStrokingColor(20f / 255f, 40f / 255f, 80f / 255f);
      cs.setLineWidth(0.5f);

      // Width dimension below drawing
      float dimY   = (float) (offset[1] - 20);
      float startX = (float) offset[0];
      float endX   = (float) (offset[0] + drawWidth);

      // Extension lines
      cs.moveTo(startX, (float) offset[1] - 5);
      cs.lineTo(startX, dimY - 5);
      cs.moveTo(endX, (float) offset[1] - 5);
      cs.lineTo(endX, dimY - 5);
      cs.stroke();

      // Dimension line with arrows
      cs.moveTo(startX, dimY);
      cs.lineTo(endX, dimY);
      cs.stroke();

      // Width text
      cs.beginText();
      cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
      cs.setNonStrokingColor(20f / 255f, 40f / 255f, 80f / 255f);
      cs.newLineAtOffset((startX + endX) / 2 - 15, dimY - 12);
      cs.showText(widthText);
      cs.endText();

      // Height dimension to right of drawing
      float dimX   = (float) (offset[0] + drawWidth + 20);
      float startY = (float) offset[1];
      float endY   = (float) (offset[1] + drawHeight);

      // Extension lines
      cs.moveTo((float) (offset[0] + drawWidth + 5), startY);
      cs.lineTo(dimX + 5, startY);
      cs.moveTo((float) (offset[0] + drawWidth + 5), endY);
      cs.lineTo(dimX + 5, endY);
      cs.stroke();

      // Dimension line
      cs.moveTo(dimX, startY);
      cs.lineTo(dimX, endY);
      cs.stroke();

      // Height text
      cs.beginText();
      cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
      cs.newLineAtOffset(dimX + 5, (startY + endY) / 2 - 3);
      cs.showText(heightText);
      cs.endText();
   }



   /*******************************************************************************
    ** Draw overall dimensions (dynamic page size mode).
    *******************************************************************************/
   private void drawDimensions(PDPageContentStream cs, Box3D bounds,
                               ViewDirection viewDirection, RenderSettings settings,
                               int canvasWidth, int canvasHeight) throws Exception
   {
      double margin = settings.getMarginPoints();
      double scale  = settings.getScale();

      java.awt.Color dimColor = settings.getDimensionColor();
      cs.setStrokingColor(dimColor.getRed() / 255f,
         dimColor.getGreen() / 255f, dimColor.getBlue() / 255f);
      cs.setLineWidth(settings.getDimensionLineStrokeWidth());

      // Get dimensions based on view
      String widthText, heightText;
      double drawWidth, drawHeight;

      switch(viewDirection)
      {
         case FRONT, BACK ->
         {
            widthText = Dimension.inches(bounds.width()).formatFractional();
            heightText = Dimension.inches(bounds.height()).formatFractional();
            drawWidth = bounds.width() * scale;
            drawHeight = bounds.height() * scale;
         }
         case LEFT, RIGHT ->
         {
            widthText = Dimension.inches(bounds.depth()).formatFractional();
            heightText = Dimension.inches(bounds.height()).formatFractional();
            drawWidth = bounds.depth() * scale;
            drawHeight = bounds.height() * scale;
         }
         case TOP, BOTTOM ->
         {
            widthText = Dimension.inches(bounds.width()).formatFractional();
            heightText = Dimension.inches(bounds.depth()).formatFractional();
            drawWidth = bounds.width() * scale;
            drawHeight = bounds.depth() * scale;
         }
         default ->
         {
            return;
         }
      }

      double titleBlockHeight = settings.isShowTitleBlock() ? settings.toPoints(1.5) : 0;

      // Width dimension (at bottom)
      float dimY = (float) (margin + titleBlockHeight - 15);
      cs.moveTo((float) margin, dimY);
      cs.lineTo((float) (margin + drawWidth), dimY);
      cs.stroke();

      // Width text
      cs.beginText();
      cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
      cs.setNonStrokingColor(dimColor.getRed() / 255f,
         dimColor.getGreen() / 255f, dimColor.getBlue() / 255f);
      cs.newLineAtOffset((float) (margin + drawWidth / 2 - 15), dimY - 12);
      cs.showText(widthText);
      cs.endText();

      // Height dimension (at right)
      float dimX = (float) (margin + drawWidth + 10);
      cs.moveTo(dimX, (float) (margin + titleBlockHeight));
      cs.lineTo(dimX, (float) (margin + titleBlockHeight + drawHeight));
      cs.stroke();

      // Height text
      cs.beginText();
      cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
      cs.newLineAtOffset(dimX + 5, (float) (margin + titleBlockHeight + drawHeight / 2));
      cs.showText(heightText);
      cs.endText();
   }



   /*******************************************************************************
    ** Draw the title block (dynamic page size mode).
    *******************************************************************************/
   private void drawTitleBlock(PDPageContentStream cs, SceneNode sceneRoot,
                               ViewDirection viewDirection, RenderSettings settings,
                               int canvasWidth, int canvasHeight) throws Exception
   {
      double margin      = settings.getMarginPoints();
      double blockHeight = settings.toPoints(1.2);
      float  y           = (float) margin;
      float  width       = (float) (canvasWidth - margin * 2);

      // Background
      cs.setNonStrokingColor(250f / 255f, 250f / 255f, 250f / 255f);
      cs.addRect((float) margin, y, width, (float) blockHeight);
      cs.fill();

      // Border
      cs.setStrokingColor(0f, 0f, 0f);
      cs.setLineWidth(1);
      cs.addRect((float) margin, y, width, (float) blockHeight);
      cs.stroke();

      // Name
      String label = sceneRoot.getLabel() != null ? sceneRoot.getLabel() : sceneRoot.getName();
      cs.beginText();
      cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
      cs.setNonStrokingColor(0f, 0f, 0f);
      cs.newLineAtOffset((float) margin + 10, y + (float) blockHeight - 18);
      cs.showText(label);
      cs.endText();

      // View name
      cs.beginText();
      cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 11);
      cs.newLineAtOffset((float) margin + 10, y + (float) blockHeight - 35);
      cs.showText(viewDirection.getDisplayName());
      cs.endText();

      // Dimensions summary
      Box3D  bounds  = sceneRoot.calculateTotalBounds();
      String dimText = String.format("Overall: %s W x %s H x %s D",
         Dimension.inches(bounds.width()).formatFractional(),
         Dimension.inches(bounds.height()).formatFractional(),
         Dimension.inches(bounds.depth()).formatFractional());

      cs.beginText();
      cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
      cs.newLineAtOffset((float) (canvasWidth - margin - 200), y + (float) blockHeight - 18);
      cs.showText(dimText);
      cs.endText();
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



   @Override
   public String getFileExtension()
   {
      return "pdf";
   }



   @Override
   public String getMimeType()
   {
      return "application/pdf";
   }



   @Override
   public String getFormatName()
   {
      return "PDF Document";
   }
}
