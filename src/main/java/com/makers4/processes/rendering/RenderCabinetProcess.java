package com.makers4.processes.rendering;


import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.List;
import com.kingsrook.qqq.backend.core.actions.processes.BackendStep;
import com.kingsrook.qqq.backend.core.actions.tables.GetAction;
import com.kingsrook.qqq.backend.core.actions.tables.InsertAction;
import com.kingsrook.qqq.backend.core.actions.tables.UpdateAction;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.logging.QLogger;
import com.kingsrook.qqq.backend.core.model.actions.processes.RunBackendStepInput;
import com.kingsrook.qqq.backend.core.model.actions.processes.RunBackendStepOutput;
import com.kingsrook.qqq.backend.core.model.actions.tables.get.GetInput;
import com.kingsrook.qqq.backend.core.model.actions.tables.get.GetOutput;
import com.kingsrook.qqq.backend.core.model.actions.tables.insert.InsertInput;
import com.kingsrook.qqq.backend.core.model.actions.tables.insert.InsertOutput;
import com.kingsrook.qqq.backend.core.model.actions.tables.update.UpdateInput;
import com.kingsrook.qqq.backend.core.model.data.QRecord;
import com.kingsrook.qqq.backend.core.model.metadata.MetaDataProducerInterface;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.code.QCodeReference;
import com.kingsrook.qqq.backend.core.model.metadata.layout.QIcon;
import com.kingsrook.qqq.backend.core.model.metadata.fields.QFieldMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.fields.QFieldType;
import com.kingsrook.qqq.backend.core.model.metadata.processes.QBackendStepMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.processes.QFrontendStepMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.processes.QProcessMetaData;
import com.makers4.model.Cabinet;
import com.makers4.model.RenderArtifact;
import com.makers4.model.RenderJob;
import com.makers4.rendering.RenderSettings;
import com.makers4.rendering.builders.CabinetSceneBuilder;
import com.makers4.rendering.camera.ViewDirection;
import com.makers4.rendering.export.PdfExporter;
import com.makers4.rendering.export.SvgExporter;
import com.makers4.rendering.scene.SceneNode;
import static com.kingsrook.qqq.backend.core.logging.LogUtils.logPair;


/*******************************************************************************
 ** Process to render a cabinet to PDF and SVG outputs.
 ** Creates a RenderJob record and generates RenderArtifact records for each output.
 *******************************************************************************/
public class RenderCabinetProcess implements BackendStep, MetaDataProducerInterface<QProcessMetaData>
{
   public static final String PROCESS_NAME = "renderCabinet";
   public static final String STEP_NAME    = "render";
   public static final String RESULT_STEP  = "result";

   private static final QLogger LOG = QLogger.getLogger(RenderCabinetProcess.class);



   /*******************************************************************************
    ** Produce the process metadata.
    *******************************************************************************/
   @Override
   public QProcessMetaData produce(QInstance qInstance) throws QException
   {
      return new QProcessMetaData()
         .withName(PROCESS_NAME)
         .withLabel("Render Cabinet")
         .withIcon(new QIcon().withName("image"))
         .withTableName(Cabinet.TABLE_NAME)
         .withIsHidden(false)
         .withStepList(List.of(
            new QBackendStepMetaData()
               .withName(STEP_NAME)
               .withCode(new QCodeReference(getClass())),
            new QFrontendStepMetaData()
               .withName(RESULT_STEP)
               .withLabel("Render Complete")
               .withFormField(new QFieldMetaData("message", QFieldType.STRING).withLabel("Result"))
               .withFormField(new QFieldMetaData("renderJobId", QFieldType.LONG).withLabel("Render Job ID"))
         ));
   }



   /*******************************************************************************
    ** Execute the render process.
    *******************************************************************************/
   @Override
   public void run(RunBackendStepInput input, RunBackendStepOutput output) throws QException
   {
      Long cabinetId = null;

      // Try to get cabinet ID from various sources
      if(input.getValue("cabinetId") != null)
      {
         cabinetId = Long.parseLong(input.getValue("cabinetId").toString());
      }
      else if(input.getValue("id") != null)
      {
         cabinetId = Long.parseLong(input.getValue("id").toString());
      }
      else if(input.getValue("recordIds") != null)
      {
         // Record IDs can come as a String or List depending on context
         Object recordIds = input.getValue("recordIds");
         if(recordIds instanceof String)
         {
            cabinetId = Long.parseLong((String) recordIds);
         }
         else if(recordIds instanceof List)
         {
            @SuppressWarnings("unchecked")
            List<Object> idList = (List<Object>) recordIds;
            if(!idList.isEmpty())
            {
               cabinetId = Long.parseLong(idList.get(0).toString());
            }
         }
      }
      else if(input.getRecords() != null && !input.getRecords().isEmpty())
      {
         cabinetId = input.getRecords().get(0).getValueLong("id");
      }

      if(cabinetId == null)
      {
         LOG.warn("Cabinet ID not found in input. Available values: " + input.getValues());
         throw new QException("Cabinet ID is required");
      }

      LOG.info("Starting cabinet render", logPair("cabinetId", cabinetId));

      // Load the cabinet record
      QRecord cabinetRecord = loadCabinetRecord(cabinetId);
      String cabinetName = cabinetRecord.getValueString("name");
      Long projectId = cabinetRecord.getValueLong("projectId");

      // Create the RenderJob
      Long renderJobId = createRenderJob(projectId, cabinetId);

      try
      {
         // Mark as started
         updateRenderJobStarted(renderJobId);

         // Build the scene graph
         CabinetSceneBuilder builder   = new CabinetSceneBuilder();
         SceneNode           sceneRoot = builder.buildScene(cabinetRecord);

         // Render to PDF (multi-view blueprint)
         byte[] pdfData = renderToPdf(sceneRoot, cabinetName);
         createRenderArtifact(renderJobId, cabinetName + " - Blueprint.pdf",
            "PDF", "application/pdf", pdfData);

         // Render individual SVG views
         for(ViewDirection view : ViewDirection.standardViews())
         {
            byte[] svgData = renderToSvg(sceneRoot, view);
            createRenderArtifact(renderJobId, cabinetName + " - " + view.getDisplayName() + ".svg",
               "SVG", "image/svg+xml", svgData);
         }

         // Mark as completed
         updateRenderJobCompleted(renderJobId);

         output.addValue("renderJobId", renderJobId);
         output.addValue("message", "Successfully rendered cabinet to PDF and SVG");

         LOG.info("Cabinet render completed", logPair("cabinetId", cabinetId), logPair("renderJobId", renderJobId));
      }
      catch(Exception e)
      {
         updateRenderJobFailed(renderJobId, e.getMessage());
         throw new QException("Failed to render cabinet: " + e.getMessage(), e);
      }
   }



   /*******************************************************************************
    ** Load a cabinet record by ID.
    *******************************************************************************/
   private QRecord loadCabinetRecord(Long cabinetId) throws QException
   {
      GetInput getInput = new GetInput();
      getInput.setTableName(Cabinet.TABLE_NAME);
      getInput.setPrimaryKey(cabinetId);

      GetOutput getOutput = new GetAction().execute(getInput);

      if(getOutput.getRecord() == null)
      {
         throw new QException("Cabinet not found: " + cabinetId);
      }

      return getOutput.getRecord();
   }



   /*******************************************************************************
    ** Create a RenderJob record.
    *******************************************************************************/
   private Long createRenderJob(Long projectId, Long cabinetId) throws QException
   {
      QRecord jobRecord = new QRecord()
         .withValue("projectId", projectId)
         .withValue("cabinetId", cabinetId)
         .withValue("renderType", "CABINET_BLUEPRINT")
         .withValue("status", "PENDING")
         .withValue("requestedAt", Instant.now());

      InsertInput insertInput = new InsertInput();
      insertInput.setTableName(RenderJob.TABLE_NAME);
      insertInput.setRecords(List.of(jobRecord));

      InsertOutput insertOutput = new InsertAction().execute(insertInput);

      return insertOutput.getRecords().get(0).getValueLong("id");
   }



   /*******************************************************************************
    ** Update RenderJob to RUNNING status.
    *******************************************************************************/
   private void updateRenderJobStarted(Long renderJobId) throws QException
   {
      QRecord updateRecord = new QRecord()
         .withValue("id", renderJobId)
         .withValue("status", "RUNNING")
         .withValue("startedAt", Instant.now());

      UpdateInput updateInput = new UpdateInput();
      updateInput.setTableName(RenderJob.TABLE_NAME);
      updateInput.setRecords(List.of(updateRecord));

      new UpdateAction().execute(updateInput);
   }



   /*******************************************************************************
    ** Update RenderJob to COMPLETED status.
    *******************************************************************************/
   private void updateRenderJobCompleted(Long renderJobId) throws QException
   {
      QRecord updateRecord = new QRecord()
         .withValue("id", renderJobId)
         .withValue("status", "COMPLETED")
         .withValue("completedAt", Instant.now());

      UpdateInput updateInput = new UpdateInput();
      updateInput.setTableName(RenderJob.TABLE_NAME);
      updateInput.setRecords(List.of(updateRecord));

      new UpdateAction().execute(updateInput);
   }



   /*******************************************************************************
    ** Update RenderJob to FAILED status.
    *******************************************************************************/
   private void updateRenderJobFailed(Long renderJobId, String errorMessage) throws QException
   {
      QRecord updateRecord = new QRecord()
         .withValue("id", renderJobId)
         .withValue("status", "FAILED")
         .withValue("completedAt", Instant.now())
         .withValue("errorMessage", errorMessage);

      UpdateInput updateInput = new UpdateInput();
      updateInput.setTableName(RenderJob.TABLE_NAME);
      updateInput.setRecords(List.of(updateRecord));

      new UpdateAction().execute(updateInput);
   }



   /*******************************************************************************
    ** Create a RenderArtifact record.
    *******************************************************************************/
   private void createRenderArtifact(Long renderJobId, String name, String artifactType,
                                     String mimeType, byte[] fileData) throws QException
   {
      QRecord artifactRecord = new QRecord()
         .withValue("renderJobId", renderJobId)
         .withValue("name", name)
         .withValue("artifactType", artifactType)
         .withValue("mimeType", mimeType)
         .withValue("fileSizeBytes", (long) fileData.length)
         .withValue("fileData", fileData);

      InsertInput insertInput = new InsertInput();
      insertInput.setTableName(RenderArtifact.TABLE_NAME);
      insertInput.setRecords(List.of(artifactRecord));

      new InsertAction().execute(insertInput);
   }



   /*******************************************************************************
    ** Render the scene to a multi-view PDF blueprint.
    *******************************************************************************/
   private byte[] renderToPdf(SceneNode sceneRoot, String cabinetName) throws Exception
   {
      PdfExporter exporter = new PdfExporter();

      // Use architectural blueprint settings
      RenderSettings settings = RenderSettings.architecturalBlueprint()
         .withCompanyName("Makers4")
         .withProjectName("Cabinet: " + cabinetName)
         .withDrawnBy("Makers4 System")
         .withDate(java.time.LocalDate.now().toString());

      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      // Render all standard views (Front, Left, Top) plus Isometric
      List<ViewDirection> views = List.of(
         ViewDirection.FRONT,
         ViewDirection.LEFT,
         ViewDirection.TOP,
         ViewDirection.ISOMETRIC
      );

      exporter.exportViews(sceneRoot, views, settings, baos);

      return baos.toByteArray();
   }



   /*******************************************************************************
    ** Render the scene to SVG for a specific view.
    *******************************************************************************/
   private byte[] renderToSvg(SceneNode sceneRoot, ViewDirection view) throws Exception
   {
      SvgExporter exporter = new SvgExporter();

      RenderSettings settings = RenderSettings.preview()
         .withScale(12.0)
         .withShowPartLabels(true);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      exporter.exportView(sceneRoot, view, settings, baos);

      return baos.toByteArray();
   }
}
