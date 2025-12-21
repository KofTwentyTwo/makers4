package com.makers4.model;


import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.data.QField;
import com.kingsrook.qqq.backend.core.model.data.QRecordEntity;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.fields.DynamicDefaultValueBehavior;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.List;


@Entity
@Table(name = RenderJob.TABLE_NAME)
@QMetaDataProducingEntity(produceTableMetaData = true, tableMetaDataCustomizer = RenderJob.TableMetaDataCustomizer.class, producePossibleValueSource = true)
public class RenderJob extends QRecordEntity
{
   public static final String TABLE_NAME  = "render_job";
   public static final String TABLE_LABEL = "Render Jobs";
   public static final String ICON_NAME   = "image";

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   @QField(isEditable = false, isPrimaryKey = true)
   private Long id;

   @ManyToOne
   @JoinColumn(name = "project_id", insertable = false, updatable = false)
   private Project project;

   @Column(name = "project_id")
   @QField(backendName = "project_id", possibleValueSourceName = Project.TABLE_NAME, label = "Project")
   private Long projectId;

   @ManyToOne
   @JoinColumn(name = "cabinet_id", insertable = false, updatable = false)
   private Cabinet cabinet;

   @Column(name = "cabinet_id")
   @QField(backendName = "cabinet_id", possibleValueSourceName = Cabinet.TABLE_NAME, label = "Cabinet")
   private Long cabinetId;

   @Column(name = "render_type", nullable = false, length = 40)
   @QField(isRequired = true, maxLength = 40, backendName = "render_type", label = "Render Type", valueTooLongBehavior = ValueTooLongBehavior.ERROR)
   private String renderType;

   @Column(name = "status", nullable = false, length = 40)
   @QField(isRequired = true, maxLength = 40, backendName = "status", valueTooLongBehavior = ValueTooLongBehavior.ERROR)
   private String status;

   @Column(name = "requested_at", nullable = false)
   @QField(isRequired = true, backendName = "requested_at", label = "Requested At")
   private Instant requestedAt;

   @Column(name = "started_at")
   @QField(backendName = "started_at", label = "Started At")
   private Instant startedAt;

   @Column(name = "completed_at")
   @QField(backendName = "completed_at", label = "Completed At")
   private Instant completedAt;

   @Column(name = "error_message", columnDefinition = "TEXT")
   @QField(backendName = "error_message", label = "Error Message")
   private String errorMessage;

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



   public RenderJob withId(Long id)
   {
      this.id = id;
      return this;
   }



   public void setId(Long id)
   {
      this.id = id;
   }



   public Project getProject()
   {
      return project;
   }



   public RenderJob withProject(Project project)
   {
      this.project = project;
      return this;
   }



   public void setProject(Project project)
   {
      this.project = project;
   }



   public Long getProjectId()
   {
      return projectId;
   }



   public RenderJob withProjectId(Long projectId)
   {
      this.projectId = projectId;
      return this;
   }



   public void setProjectId(Long projectId)
   {
      this.projectId = projectId;
   }



   public Cabinet getCabinet()
   {
      return cabinet;
   }



   public RenderJob withCabinet(Cabinet cabinet)
   {
      this.cabinet = cabinet;
      return this;
   }



   public void setCabinet(Cabinet cabinet)
   {
      this.cabinet = cabinet;
   }



   public Long getCabinetId()
   {
      return cabinetId;
   }



   public RenderJob withCabinetId(Long cabinetId)
   {
      this.cabinetId = cabinetId;
      return this;
   }



   public void setCabinetId(Long cabinetId)
   {
      this.cabinetId = cabinetId;
   }



   public String getRenderType()
   {
      return renderType;
   }



   public RenderJob withRenderType(String renderType)
   {
      this.renderType = renderType;
      return this;
   }



   public void setRenderType(String renderType)
   {
      this.renderType = renderType;
   }



   public String getStatus()
   {
      return status;
   }



   public RenderJob withStatus(String status)
   {
      this.status = status;
      return this;
   }



   public void setStatus(String status)
   {
      this.status = status;
   }



   public Instant getRequestedAt()
   {
      return requestedAt;
   }



   public RenderJob withRequestedAt(Instant requestedAt)
   {
      this.requestedAt = requestedAt;
      return this;
   }



   public void setRequestedAt(Instant requestedAt)
   {
      this.requestedAt = requestedAt;
   }



   public Instant getStartedAt()
   {
      return startedAt;
   }



   public RenderJob withStartedAt(Instant startedAt)
   {
      this.startedAt = startedAt;
      return this;
   }



   public void setStartedAt(Instant startedAt)
   {
      this.startedAt = startedAt;
   }



   public Instant getCompletedAt()
   {
      return completedAt;
   }



   public RenderJob withCompletedAt(Instant completedAt)
   {
      this.completedAt = completedAt;
      return this;
   }



   public void setCompletedAt(Instant completedAt)
   {
      this.completedAt = completedAt;
   }



   public String getErrorMessage()
   {
      return errorMessage;
   }



   public RenderJob withErrorMessage(String errorMessage)
   {
      this.errorMessage = errorMessage;
      return this;
   }



   public void setErrorMessage(String errorMessage)
   {
      this.errorMessage = errorMessage;
   }



   public Instant getCreateDate()
   {
      return createDate;
   }



   public RenderJob withCreateDate(Instant createDate)
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



   public RenderJob withModifyDate(Instant modifyDate)
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
            .withRecordLabelFormat("Job %s - %s")
            .withRecordLabelFields(List.of("id", "status"))
            .withBackendName(Makers4MetaDataProvider.RDBMS_BACKEND_NAME);

         table.addSection(new QFieldSection("identity", "Identity", new QIcon(ICON_NAME), Tier.T1, List.of("id", "projectId", "cabinetId", "renderType", "status")));
         table.addSection(new QFieldSection("timing", "Timing", new QIcon("schedule"), Tier.T2, List.of("requestedAt", "startedAt", "completedAt")));
         table.addSection(new QFieldSection("error", "Error", new QIcon("error"), Tier.T2, List.of("errorMessage")));
         table.addSection(new QFieldSection("dates", "Dates", new QIcon("event"), Tier.T3, List.of("createDate", "modifyDate")));

         return table;
      }
   }
}
