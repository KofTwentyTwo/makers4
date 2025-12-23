package com.makers4.processes.rendering;


import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import com.kingsrook.qqq.backend.core.actions.processes.BackendStep;
import com.kingsrook.qqq.backend.core.actions.tables.QueryAction;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.logging.QLogger;
import com.kingsrook.qqq.backend.core.model.actions.processes.RunBackendStepInput;
import com.kingsrook.qqq.backend.core.model.actions.processes.RunBackendStepOutput;
import com.kingsrook.qqq.backend.core.model.actions.tables.query.QCriteriaOperator;
import com.kingsrook.qqq.backend.core.model.actions.tables.query.QFilterCriteria;
import com.kingsrook.qqq.backend.core.model.actions.tables.query.QQueryFilter;
import com.kingsrook.qqq.backend.core.model.actions.tables.query.QueryInput;
import com.kingsrook.qqq.backend.core.model.actions.tables.query.QueryOutput;
import com.kingsrook.qqq.backend.core.model.data.QRecord;
import com.kingsrook.qqq.backend.core.model.metadata.MetaDataProducerInterface;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.code.QCodeReference;
import com.kingsrook.qqq.backend.core.model.metadata.fields.QFieldMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.fields.QFieldType;
import com.kingsrook.qqq.backend.core.model.metadata.layout.QIcon;
import com.kingsrook.qqq.backend.core.model.metadata.processes.QBackendStepMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.processes.QFrontendStepMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.processes.QProcessMetaData;
import com.makers4.model.RenderArtifact;
import static com.kingsrook.qqq.backend.core.logging.LogUtils.logPair;


/*******************************************************************************
 ** Process to download a render artifact file.
 ** Writes the blob to a temp file and provides the path for download.
 *******************************************************************************/
public class DownloadRenderArtifactProcess implements BackendStep, MetaDataProducerInterface<QProcessMetaData>
{
   public static final String PROCESS_NAME = "downloadRenderArtifact";
   public static final String STEP_NAME    = "download";
   public static final String RESULT_STEP  = "result";

   private static final QLogger LOG = QLogger.getLogger(DownloadRenderArtifactProcess.class);



   /*******************************************************************************
    ** Produce the process metadata.
    *******************************************************************************/
   @Override
   public QProcessMetaData produce(QInstance qInstance) throws QException
   {
      return new QProcessMetaData()
         .withName(PROCESS_NAME)
         .withLabel("Download Artifact")
         .withIcon(new QIcon().withName("download"))
         .withTableName(RenderArtifact.TABLE_NAME)
         .withIsHidden(false)
         .withStepList(List.of(
            new QBackendStepMetaData()
               .withName(STEP_NAME)
               .withCode(new QCodeReference(getClass())),
            new QFrontendStepMetaData()
               .withName(RESULT_STEP)
               .withLabel("Download Ready")
               .withFormField(new QFieldMetaData("downloadPath", QFieldType.STRING).withLabel("Download Path"))
               .withFormField(new QFieldMetaData("message", QFieldType.STRING).withLabel("Result"))
         ));
   }



   /*******************************************************************************
    ** Execute the download process.
    *******************************************************************************/
   @Override
   public void run(RunBackendStepInput input, RunBackendStepOutput output) throws QException
   {
      Long artifactId = null;

      // Try to get artifact ID from various sources
      if(input.getValue("artifactId") != null)
      {
         artifactId = Long.parseLong(input.getValue("artifactId").toString());
      }
      else if(input.getValue("id") != null)
      {
         artifactId = Long.parseLong(input.getValue("id").toString());
      }
      else if(input.getValue("recordIds") != null)
      {
         Object recordIds = input.getValue("recordIds");
         if(recordIds instanceof String)
         {
            artifactId = Long.parseLong((String) recordIds);
         }
         else if(recordIds instanceof List)
         {
            @SuppressWarnings("unchecked")
            List<Object> idList = (List<Object>) recordIds;
            if(!idList.isEmpty())
            {
               artifactId = Long.parseLong(idList.get(0).toString());
            }
         }
      }
      else if(input.getRecords() != null && !input.getRecords().isEmpty())
      {
         artifactId = input.getRecords().get(0).getValueLong("id");
      }

      if(artifactId == null)
      {
         throw new QException("Artifact ID is required");
      }

      LOG.info("Downloading render artifact", logPair("artifactId", artifactId));

      // Load the artifact record with blob data using QueryAction for explicit field control
      QueryInput queryInput = new QueryInput();
      queryInput.setTableName(RenderArtifact.TABLE_NAME);
      queryInput.setFilter(new QQueryFilter(new QFilterCriteria("id", QCriteriaOperator.EQUALS, artifactId)));
      queryInput.setShouldFetchHeavyFields(true);

      QueryOutput queryOutput = new QueryAction().execute(queryInput);

      if(queryOutput.getRecords().isEmpty())
      {
         throw new QException("Render artifact not found: " + artifactId);
      }

      QRecord artifactRecord = queryOutput.getRecords().get(0);
      String  fileName       = artifactRecord.getValueString("name");
      String  mimeType       = artifactRecord.getValueString("mimeType");
      byte[]  fileData       = artifactRecord.getValueByteArray("fileData");

      LOG.info("Artifact loaded",
         logPair("fileName", fileName),
         logPair("mimeType", mimeType),
         logPair("fileDataNull", fileData == null),
         logPair("fileDataLength", fileData != null ? fileData.length : 0),
         logPair("recordValues", artifactRecord.getValues().keySet()));

      if(fileData == null || fileData.length == 0)
      {
         throw new QException("Artifact has no file data. Available fields: " + artifactRecord.getValues().keySet());
      }

      try
      {
         // Create temp directory for downloads
         File downloadDir = new File(System.getProperty("java.io.tmpdir"), "makers4-downloads");
         if(!downloadDir.exists())
         {
            downloadDir.mkdirs();
         }

         // Write file to temp location
         File outputFile = new File(downloadDir, fileName);
         try(FileOutputStream fos = new FileOutputStream(outputFile))
         {
            fos.write(fileData);
         }

         String downloadPath = outputFile.getAbsolutePath();

         output.addValue("downloadPath", downloadPath);
         output.addValue("message", "File ready: " + fileName + " (" + fileData.length + " bytes)");
         output.addValue("serverFilePath", downloadPath);

         LOG.info("Artifact download prepared",
            logPair("artifactId", artifactId),
            logPair("fileName", fileName),
            logPair("path", downloadPath));
      }
      catch(Exception e)
      {
         throw new QException("Failed to prepare download: " + e.getMessage(), e);
      }
   }
}
