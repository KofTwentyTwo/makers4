package com.makers4.model;


import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.data.QField;
import com.kingsrook.qqq.backend.core.model.data.QRecordEntity;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.fields.AdornmentType;
import com.kingsrook.qqq.backend.core.model.metadata.fields.DynamicDefaultValueBehavior;
import com.kingsrook.qqq.backend.core.model.metadata.fields.FieldAdornment;
import com.kingsrook.qqq.backend.core.model.metadata.fields.ValueTooLongBehavior;
import com.kingsrook.qqq.backend.core.model.metadata.layout.QIcon;
import com.kingsrook.qqq.backend.core.model.metadata.producers.MetaDataCustomizerInterface;
import com.kingsrook.qqq.backend.core.model.metadata.producers.annotations.QMetaDataProducingEntity;
import com.kingsrook.qqq.backend.core.model.metadata.tables.QFieldSection;
import com.kingsrook.qqq.backend.core.model.metadata.tables.QTableMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.tables.Tier;
import com.kingsrook.qqq.backend.core.model.metadata.tables.UniqueKey;
import com.makers4.metadata.Makers4MetaDataProvider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.List;


@Entity
@Table(name = RenderArtifact.TABLE_NAME)
@QMetaDataProducingEntity(produceTableMetaData = true, tableMetaDataCustomizer = RenderArtifact.TableMetaDataCustomizer.class, producePossibleValueSource = true)
public class RenderArtifact extends QRecordEntity
{
   public static final String TABLE_NAME  = "render_artifact";
   public static final String TABLE_LABEL = "Render Artifacts";
   public static final String ICON_NAME   = "insert_drive_file";

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   @QField(isEditable = false, isPrimaryKey = true)
   private Long id;

   @ManyToOne
   @JoinColumn(name = "render_job_id", insertable = false, updatable = false)
   private RenderJob renderJob;

   @Column(name = "render_job_id", nullable = false)
   @QField(isRequired = true, backendName = "render_job_id", possibleValueSourceName = RenderJob.TABLE_NAME, label = "Render Job")
   private Long renderJobId;

   @Column(name = "name", nullable = false, length = 255)
   @QField(isRequired = true, maxLength = 255, backendName = "name", valueTooLongBehavior = ValueTooLongBehavior.ERROR)
   private String name;

   @Column(name = "artifact_type", nullable = false, length = 40)
   @QField(isRequired = true, maxLength = 40, backendName = "artifact_type", label = "Artifact Type", valueTooLongBehavior = ValueTooLongBehavior.ERROR)
   private String artifactType;

   @Column(name = "mime_type", nullable = false, length = 100)
   @QField(isRequired = true, maxLength = 100, backendName = "mime_type", label = "MIME Type", valueTooLongBehavior = ValueTooLongBehavior.ERROR)
   private String mimeType;

   @Column(name = "file_size_bytes")
   @QField(backendName = "file_size_bytes", label = "File Size (bytes)")
   private Long fileSizeBytes;

   @Lob
   @Column(name = "file_data", nullable = false)
   @QField(isRequired = true, backendName = "file_data", label = "File Data")
   private byte[] fileData;

   @Column(name = "createdate", nullable = false, updatable = false)
   @QField(dynamicDefaultValueBehavior = DynamicDefaultValueBehavior.CREATE_DATE, backendName = "createdate")
   private Instant createDate;

   @Column(name = "modifydate", nullable = false)
   @QField(dynamicDefaultValueBehavior = DynamicDefaultValueBehavior.MODIFY_DATE, backendName = "modifydate")
   private Instant modifyDate;



   public Long getId()
   {
      return id;
   }



   public RenderArtifact withId(Long id)
   {
      this.id = id;
      return this;
   }



   public void setId(Long id)
   {
      this.id = id;
   }



   public RenderJob getRenderJob()
   {
      return renderJob;
   }



   public RenderArtifact withRenderJob(RenderJob renderJob)
   {
      this.renderJob = renderJob;
      return this;
   }



   public void setRenderJob(RenderJob renderJob)
   {
      this.renderJob = renderJob;
   }



   public Long getRenderJobId()
   {
      return renderJobId;
   }



   public RenderArtifact withRenderJobId(Long renderJobId)
   {
      this.renderJobId = renderJobId;
      return this;
   }



   public void setRenderJobId(Long renderJobId)
   {
      this.renderJobId = renderJobId;
   }



   public String getName()
   {
      return name;
   }



   public RenderArtifact withName(String name)
   {
      this.name = name;
      return this;
   }



   public void setName(String name)
   {
      this.name = name;
   }



   public String getArtifactType()
   {
      return artifactType;
   }



   public RenderArtifact withArtifactType(String artifactType)
   {
      this.artifactType = artifactType;
      return this;
   }



   public void setArtifactType(String artifactType)
   {
      this.artifactType = artifactType;
   }



   public String getMimeType()
   {
      return mimeType;
   }



   public RenderArtifact withMimeType(String mimeType)
   {
      this.mimeType = mimeType;
      return this;
   }



   public void setMimeType(String mimeType)
   {
      this.mimeType = mimeType;
   }



   public Long getFileSizeBytes()
   {
      return fileSizeBytes;
   }



   public RenderArtifact withFileSizeBytes(Long fileSizeBytes)
   {
      this.fileSizeBytes = fileSizeBytes;
      return this;
   }



   public void setFileSizeBytes(Long fileSizeBytes)
   {
      this.fileSizeBytes = fileSizeBytes;
   }



   public byte[] getFileData()
   {
      return fileData;
   }



   public RenderArtifact withFileData(byte[] fileData)
   {
      this.fileData = fileData;
      return this;
   }



   public void setFileData(byte[] fileData)
   {
      this.fileData = fileData;
   }



   public Instant getCreateDate()
   {
      return createDate;
   }



   public RenderArtifact withCreateDate(Instant createDate)
   {
      this.createDate = createDate;
      return this;
   }



   public void setCreateDate(Instant createDate)
   {
      this.createDate = createDate;
   }



   public Instant getModifyDate()
   {
      return modifyDate;
   }



   public RenderArtifact withModifyDate(Instant modifyDate)
   {
      this.modifyDate = modifyDate;
      return this;
   }



   public void setModifyDate(Instant modifyDate)
   {
      this.modifyDate = modifyDate;
   }



   public static class TableMetaDataCustomizer implements MetaDataCustomizerInterface<QTableMetaData>
   {
      @Override
      public QTableMetaData customizeMetaData(QInstance qInstance, QTableMetaData table) throws QException
      {
         table.withUniqueKey(new UniqueKey("id"))
            .withIcon(new QIcon().withName(ICON_NAME))
            .withLabel(TABLE_LABEL)
            .withRecordLabelFormat("%s")
            .withRecordLabelFields(List.of("name"))
            .withBackendName(Makers4MetaDataProvider.RDBMS_BACKEND_NAME);

         // Mark fileData as heavy field with download adornment
         table.getField("fileData")
            .withIsHeavy(true)
            .withFieldAdornment(new FieldAdornment(AdornmentType.FILE_DOWNLOAD)
               .withValue(AdornmentType.FileDownloadValues.FILE_NAME_FIELD, "name"));

         table.addSection(new QFieldSection("identity", "Identity", new QIcon(ICON_NAME), Tier.T1, List.of("id", "renderJobId", "name", "artifactType")));
         table.addSection(new QFieldSection("file", "File", new QIcon("attachment"), Tier.T2, List.of("mimeType", "fileSizeBytes")));
         table.addSection(new QFieldSection("download", "Download", new QIcon("download"), Tier.T2, List.of("fileData")));
         table.addSection(new QFieldSection("dates", "Dates", new QIcon("event"), Tier.T3, List.of("createDate", "modifyDate")));

         return table;
      }
   }
}
