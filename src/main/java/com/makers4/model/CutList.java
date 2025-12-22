package com.makers4.model;


import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.data.QField;
import com.kingsrook.qqq.backend.core.model.data.QRecordEntity;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.fields.DynamicDefaultValueBehavior;
import com.kingsrook.qqq.backend.core.model.metadata.fields.ValueTooLongBehavior;
import com.kingsrook.qqq.backend.core.model.metadata.layout.QIcon;
import com.kingsrook.qqq.backend.core.model.metadata.producers.MetaDataCustomizerInterface;
import com.kingsrook.qqq.backend.core.model.metadata.producers.annotations.ChildJoin;
import com.kingsrook.qqq.backend.core.model.metadata.producers.annotations.ChildRecordListWidget;
import com.kingsrook.qqq.backend.core.model.metadata.producers.annotations.ChildTable;
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
@Table(name = CutList.TABLE_NAME)
@QMetaDataProducingEntity(
   produceTableMetaData = true,
   tableMetaDataCustomizer = CutList.TableMetaDataCustomizer.class,
   producePossibleValueSource = true,
   childTables = {
      @ChildTable(
         joinFieldName = "cutListId",
         childTableEntityClass = CutListItem.class,
         childJoin = @ChildJoin(enabled = true),
         childRecordListWidget = @ChildRecordListWidget(enabled = true, label = "Cut List Items", maxRows = 100)
      )
   }
)
public class CutList extends QRecordEntity
{
   public static final String TABLE_NAME  = "cut_list";
   public static final String TABLE_LABEL = "Cut Lists";
   public static final String ICON_NAME   = "content_cut";

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

   @Column(name = "name", nullable = false, length = 120)
   @QField(isRequired = true, maxLength = 120, backendName = "name", valueTooLongBehavior = ValueTooLongBehavior.ERROR)
   private String name;

   @Column(name = "generated_at", nullable = false)
   @QField(isRequired = true, backendName = "generated_at", label = "Generated At")
   private Instant generatedAt;

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



   public CutList withId(Long id)
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



   public CutList withProject(Project project)
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



   public CutList withProjectId(Long projectId)
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



   public CutList withCabinet(Cabinet cabinet)
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



   public CutList withCabinetId(Long cabinetId)
   {
      this.cabinetId = cabinetId;
      return this;
   }



   public void setCabinetId(Long cabinetId)
   {
      this.cabinetId = cabinetId;
   }



   public String getName()
   {
      return name;
   }



   public CutList withName(String name)
   {
      this.name = name;
      return this;
   }



   public void setName(String name)
   {
      this.name = name;
   }



   public Instant getGeneratedAt()
   {
      return generatedAt;
   }



   public CutList withGeneratedAt(Instant generatedAt)
   {
      this.generatedAt = generatedAt;
      return this;
   }



   public void setGeneratedAt(Instant generatedAt)
   {
      this.generatedAt = generatedAt;
   }



   public Instant getCreateDate()
   {
      return createDate;
   }



   public CutList withCreateDate(Instant createDate)
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



   public CutList withModifyDate(Instant modifyDate)
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

         table.addSection(new QFieldSection("identity", "Identity", new QIcon(ICON_NAME), Tier.T1, List.of("id", "projectId", "cabinetId", "name", "generatedAt")));
         table.addSection(new QFieldSection("dates", "Dates", new QIcon("event"), Tier.T3, List.of("createDate", "modifyDate")));

         return table;
      }
   }
}
