package com.makers4.rendering.export;


import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import com.makers4.rendering.RenderSettings;
import com.makers4.rendering.camera.ViewDirection;
import com.makers4.rendering.scene.SceneNode;


/*******************************************************************************
 ** Common interface for all export formats.
 ** Implementations handle specific format details (PNG, SVG, PDF).
 ** Works with scene graphs (SceneNode) for format-agnostic rendering.
 *******************************************************************************/
public interface Exporter
{

   /*******************************************************************************
    ** Export a single view of a scene to the output stream.
    **
    ** @param sceneRoot     The root scene node to render
    ** @param viewDirection The view angle to render
    ** @param settings      Rendering configuration
    ** @param output        The output stream to write to
    ** @throws ExportException if export fails
    *******************************************************************************/
   void exportView(SceneNode sceneRoot, ViewDirection viewDirection,
                   RenderSettings settings, OutputStream output) throws ExportException;



   /*******************************************************************************
    ** Export multiple views to a single file (for formats that support it, like PDF).
    ** Default implementation exports only the first view.
    **
    ** @param sceneRoot      The root scene node to render
    ** @param viewDirections List of views to include
    ** @param settings       Rendering configuration
    ** @param output         The output stream to write to
    ** @throws ExportException if export fails
    *******************************************************************************/
   default void exportViews(SceneNode sceneRoot, List<ViewDirection> viewDirections,
                            RenderSettings settings, OutputStream output) throws ExportException
   {
      if(!viewDirections.isEmpty())
      {
         exportView(sceneRoot, viewDirections.get(0), settings, output);
      }
   }



   /*******************************************************************************
    ** Convenience method to export a single view to a file.
    **
    ** @param sceneRoot     The root scene node to render
    ** @param viewDirection The view angle to render
    ** @param settings      Rendering configuration
    ** @param outputPath    The file path to write to
    ** @throws ExportException if export fails
    *******************************************************************************/
   default void exportToFile(SceneNode sceneRoot, ViewDirection viewDirection,
                             RenderSettings settings, Path outputPath) throws ExportException
   {
      try(OutputStream out = Files.newOutputStream(outputPath))
      {
         exportView(sceneRoot, viewDirection, settings, out);
      }
      catch(IOException e)
      {
         throw new ExportException("Failed to write to file: " + outputPath, e);
      }
   }



   /*******************************************************************************
    ** Convenience method to export multiple views to a file.
    **
    ** @param sceneRoot      The root scene node to render
    ** @param viewDirections List of views to include
    ** @param settings       Rendering configuration
    ** @param outputPath     The file path to write to
    ** @throws ExportException if export fails
    *******************************************************************************/
   default void exportViewsToFile(SceneNode sceneRoot, List<ViewDirection> viewDirections,
                                  RenderSettings settings, Path outputPath) throws ExportException
   {
      try(OutputStream out = Files.newOutputStream(outputPath))
      {
         exportViews(sceneRoot, viewDirections, settings, out);
      }
      catch(IOException e)
      {
         throw new ExportException("Failed to write to file: " + outputPath, e);
      }
   }



   /*******************************************************************************
    ** Get the file extension this exporter produces (without dot).
    *******************************************************************************/
   String getFileExtension();



   /*******************************************************************************
    ** Get the MIME type for the output format.
    *******************************************************************************/
   String getMimeType();



   /*******************************************************************************
    ** Get a display name for this format.
    *******************************************************************************/
   default String getFormatName()
   {
      return getFileExtension().toUpperCase();
   }
}
