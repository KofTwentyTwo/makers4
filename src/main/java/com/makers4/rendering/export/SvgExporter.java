package com.makers4.rendering.export;


import java.awt.Dimension;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import com.makers4.rendering.RenderSettings;
import com.makers4.rendering.SceneRenderer;
import com.makers4.rendering.camera.OrthographicCamera;
import com.makers4.rendering.camera.ViewDirection;
import com.makers4.rendering.scene.SceneNode;
import org.jfree.graphics2d.svg.SVGGraphics2D;


/*******************************************************************************
 ** Exporter for SVG vector graphics.
 ** Uses JFreeSVG library which provides a Graphics2D implementation that outputs SVG.
 *******************************************************************************/
public class SvgExporter implements Exporter
{

   /*******************************************************************************
    ** Export a single view to SVG.
    *******************************************************************************/
   @Override
   public void exportView(SceneNode sceneRoot, ViewDirection viewDirection,
                          RenderSettings settings, OutputStream output) throws ExportException
   {
      // Set up camera
      OrthographicCamera camera = OrthographicCamera.forView(viewDirection)
         .withScale(settings.getScale());

      // Create renderer
      SceneRenderer renderer = new SceneRenderer(settings);

      // Calculate canvas size
      Dimension size = renderer.calculateCanvasSize(sceneRoot, camera);

      // Center content in canvas
      camera.centerOn(sceneRoot.calculateTotalBounds(),
         size.getWidth(), size.getHeight(), settings.getMarginPoints());

      // JFreeSVG provides a Graphics2D that outputs SVG
      SVGGraphics2D g2d = new SVGGraphics2D(size.width, size.height);

      // Draw background
      if(settings.getBackgroundColor() != null)
      {
         g2d.setColor(settings.getBackgroundColor());
         g2d.fillRect(0, 0, size.width, size.height);
      }

      // Render using our standard Graphics2D renderer
      renderer.render(sceneRoot, camera, g2d);

      // Get the SVG document
      String svgDocument = g2d.getSVGDocument();

      // Write to output stream
      try(OutputStreamWriter writer = new OutputStreamWriter(output, StandardCharsets.UTF_8))
      {
         writer.write(svgDocument);
      }
      catch(Exception e)
      {
         throw new ExportException("Failed to write SVG document", e);
      }
   }



   @Override
   public String getFileExtension()
   {
      return "svg";
   }



   @Override
   public String getMimeType()
   {
      return "image/svg+xml";
   }



   @Override
   public String getFormatName()
   {
      return "SVG Vector";
   }
}
