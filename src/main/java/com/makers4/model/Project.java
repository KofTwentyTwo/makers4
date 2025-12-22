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
import com.makers4.model.lookup.Material;
import com.makers4.model.lookup.UnitSystem;
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
@Table(name = Project.TABLE_NAME)
@QMetaDataProducingEntity(
   produceTableMetaData = true,
   tableMetaDataCustomizer = Project.TableMetaDataCustomizer.class,
   producePossibleValueSource = true,
   childTables = {
      @ChildTable(
         joinFieldName = "projectId",
         childTableEntityClass = Cabinet.class,
         childJoin = @ChildJoin(enabled = true),
         childRecordListWidget = @ChildRecordListWidget(enabled = true, label = "Cabinets", maxRows = 50)
      ),
      @ChildTable(
         joinFieldName = "projectId",
         childTableEntityClass = CutList.class,
         childJoin = @ChildJoin(enabled = true),
         childRecordListWidget = @ChildRecordListWidget(enabled = true, label = "Cut Lists", maxRows = 20)
      ),
      @ChildTable(
         joinFieldName = "projectId",
         childTableEntityClass = RenderJob.class,
         childJoin = @ChildJoin(enabled = true),
         childRecordListWidget = @ChildRecordListWidget(enabled = true, label = "Render Jobs", maxRows = 20)
      )
   }
)
public class Project extends QRecordEntity
{
   public static final String TABLE_NAME  = "project";
   public static final String TABLE_LABEL = "Projects";
   public static final String ICON_NAME   = "folder";

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   @QField(isEditable = false, isPrimaryKey = true)
   private Long id;

   @ManyToOne
   @JoinColumn(name = "user_id", insertable = false, updatable = false)
   private User user;

   @Column(name = "user_id", nullable = false)
   @QField(isRequired = true, backendName = "user_id", possibleValueSourceName = User.TABLE_NAME, label = "User")
   private Long userId;

   @Column(name = "name", nullable = false, length = 120)
   @QField(isRequired = true, maxLength = 120, backendName = "name", valueTooLongBehavior = ValueTooLongBehavior.ERROR)
   private String name;

   @Column(name = "description", columnDefinition = "TEXT")
   @QField(backendName = "description")
   private String description;

   ////////////////////
   // ── Defaults ── //
   ////////////////////

   @ManyToOne
   @JoinColumn(name = "unit_system_id", insertable = false, updatable = false)
   private UnitSystem unitSystem;

   @Column(name = "unit_system_id")
   @QField(backendName = "unit_system_id", possibleValueSourceName = UnitSystem.TABLE_NAME, label = "Unit System")
   private Long unitSystemId;

   @ManyToOne
   @JoinColumn(name = "default_box_material_id", insertable = false, updatable = false)
   private Material defaultBoxMaterial;

   @Column(name = "default_box_material_id")
   @QField(backendName = "default_box_material_id", possibleValueSourceName = Material.TABLE_NAME, label = "Default Box Material")
   private Long defaultBoxMaterialId;

   @ManyToOne
   @JoinColumn(name = "default_back_material_id", insertable = false, updatable = false)
   private Material defaultBackMaterial;

   @Column(name = "default_back_material_id")
   @QField(backendName = "default_back_material_id", possibleValueSourceName = Material.TABLE_NAME, label = "Default Back Material")
   private Long defaultBackMaterialId;

   @ManyToOne
   @JoinColumn(name = "default_face_frame_material_id", insertable = false, updatable = false)
   private Material defaultFaceFrameMaterial;

   @Column(name = "default_face_frame_material_id")
   @QField(backendName = "default_face_frame_material_id", possibleValueSourceName = Material.TABLE_NAME, label = "Default Face Frame Material")
   private Long defaultFaceFrameMaterialId;

   @ManyToOne
   @JoinColumn(name = "default_door_frame_material_id", insertable = false, updatable = false)
   private Material defaultDoorFrameMaterial;

   @Column(name = "default_door_frame_material_id")
   @QField(backendName = "default_door_frame_material_id", possibleValueSourceName = Material.TABLE_NAME, label = "Default Door Frame Material")
   private Long defaultDoorFrameMaterialId;

   @ManyToOne
   @JoinColumn(name = "default_door_panel_material_id", insertable = false, updatable = false)
   private Material defaultDoorPanelMaterial;

   @Column(name = "default_door_panel_material_id")
   @QField(backendName = "default_door_panel_material_id", possibleValueSourceName = Material.TABLE_NAME, label = "Default Door Panel Material")
   private Long defaultDoorPanelMaterialId;

   @ManyToOne
   @JoinColumn(name = "default_drawer_front_material_id", insertable = false, updatable = false)
   private Material defaultDrawerFrontMaterial;

   @Column(name = "default_drawer_front_material_id")
   @QField(backendName = "default_drawer_front_material_id", possibleValueSourceName = Material.TABLE_NAME, label = "Default Drawer Front Material")
   private Long defaultDrawerFrontMaterialId;

   @ManyToOne
   @JoinColumn(name = "default_drawer_box_material_id", insertable = false, updatable = false)
   private Material defaultDrawerBoxMaterial;

   @Column(name = "default_drawer_box_material_id")
   @QField(backendName = "default_drawer_box_material_id", possibleValueSourceName = Material.TABLE_NAME, label = "Default Drawer Box Material")
   private Long defaultDrawerBoxMaterialId;

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



   public Project withId(Long id)
   {
      this.id = id;
      return this;
   }



   public void setId(Long id)
   {
      this.id = id;
   }



   public User getUser()
   {
      return user;
   }



   public Project withUser(User user)
   {
      this.user = user;
      return this;
   }



   public void setUser(User user)
   {
      this.user = user;
   }



   public Long getUserId()
   {
      return userId;
   }



   public Project withUserId(Long userId)
   {
      this.userId = userId;
      return this;
   }



   public void setUserId(Long userId)
   {
      this.userId = userId;
   }



   public String getName()
   {
      return name;
   }



   public Project withName(String name)
   {
      this.name = name;
      return this;
   }



   public void setName(String name)
   {
      this.name = name;
   }



   public String getDescription()
   {
      return description;
   }



   public Project withDescription(String description)
   {
      this.description = description;
      return this;
   }



   public void setDescription(String description)
   {
      this.description = description;
   }



   public UnitSystem getUnitSystem()
   {
      return unitSystem;
   }



   public Project withUnitSystem(UnitSystem unitSystem)
   {
      this.unitSystem = unitSystem;
      return this;
   }



   public void setUnitSystem(UnitSystem unitSystem)
   {
      this.unitSystem = unitSystem;
   }



   public Long getUnitSystemId()
   {
      return unitSystemId;
   }



   public Project withUnitSystemId(Long unitSystemId)
   {
      this.unitSystemId = unitSystemId;
      return this;
   }



   public void setUnitSystemId(Long unitSystemId)
   {
      this.unitSystemId = unitSystemId;
   }



   public Material getDefaultBoxMaterial()
   {
      return defaultBoxMaterial;
   }



   public Project withDefaultBoxMaterial(Material defaultBoxMaterial)
   {
      this.defaultBoxMaterial = defaultBoxMaterial;
      return this;
   }



   public void setDefaultBoxMaterial(Material defaultBoxMaterial)
   {
      this.defaultBoxMaterial = defaultBoxMaterial;
   }



   public Long getDefaultBoxMaterialId()
   {
      return defaultBoxMaterialId;
   }



   public Project withDefaultBoxMaterialId(Long defaultBoxMaterialId)
   {
      this.defaultBoxMaterialId = defaultBoxMaterialId;
      return this;
   }



   public void setDefaultBoxMaterialId(Long defaultBoxMaterialId)
   {
      this.defaultBoxMaterialId = defaultBoxMaterialId;
   }



   public Material getDefaultBackMaterial()
   {
      return defaultBackMaterial;
   }



   public Project withDefaultBackMaterial(Material defaultBackMaterial)
   {
      this.defaultBackMaterial = defaultBackMaterial;
      return this;
   }



   public void setDefaultBackMaterial(Material defaultBackMaterial)
   {
      this.defaultBackMaterial = defaultBackMaterial;
   }



   public Long getDefaultBackMaterialId()
   {
      return defaultBackMaterialId;
   }



   public Project withDefaultBackMaterialId(Long defaultBackMaterialId)
   {
      this.defaultBackMaterialId = defaultBackMaterialId;
      return this;
   }



   public void setDefaultBackMaterialId(Long defaultBackMaterialId)
   {
      this.defaultBackMaterialId = defaultBackMaterialId;
   }



   public Material getDefaultFaceFrameMaterial()
   {
      return defaultFaceFrameMaterial;
   }



   public Project withDefaultFaceFrameMaterial(Material defaultFaceFrameMaterial)
   {
      this.defaultFaceFrameMaterial = defaultFaceFrameMaterial;
      return this;
   }



   public void setDefaultFaceFrameMaterial(Material defaultFaceFrameMaterial)
   {
      this.defaultFaceFrameMaterial = defaultFaceFrameMaterial;
   }



   public Long getDefaultFaceFrameMaterialId()
   {
      return defaultFaceFrameMaterialId;
   }



   public Project withDefaultFaceFrameMaterialId(Long defaultFaceFrameMaterialId)
   {
      this.defaultFaceFrameMaterialId = defaultFaceFrameMaterialId;
      return this;
   }



   public void setDefaultFaceFrameMaterialId(Long defaultFaceFrameMaterialId)
   {
      this.defaultFaceFrameMaterialId = defaultFaceFrameMaterialId;
   }



   public Material getDefaultDoorFrameMaterial()
   {
      return defaultDoorFrameMaterial;
   }



   public Project withDefaultDoorFrameMaterial(Material defaultDoorFrameMaterial)
   {
      this.defaultDoorFrameMaterial = defaultDoorFrameMaterial;
      return this;
   }



   public void setDefaultDoorFrameMaterial(Material defaultDoorFrameMaterial)
   {
      this.defaultDoorFrameMaterial = defaultDoorFrameMaterial;
   }



   public Long getDefaultDoorFrameMaterialId()
   {
      return defaultDoorFrameMaterialId;
   }



   public Project withDefaultDoorFrameMaterialId(Long defaultDoorFrameMaterialId)
   {
      this.defaultDoorFrameMaterialId = defaultDoorFrameMaterialId;
      return this;
   }



   public void setDefaultDoorFrameMaterialId(Long defaultDoorFrameMaterialId)
   {
      this.defaultDoorFrameMaterialId = defaultDoorFrameMaterialId;
   }



   public Material getDefaultDoorPanelMaterial()
   {
      return defaultDoorPanelMaterial;
   }



   public Project withDefaultDoorPanelMaterial(Material defaultDoorPanelMaterial)
   {
      this.defaultDoorPanelMaterial = defaultDoorPanelMaterial;
      return this;
   }



   public void setDefaultDoorPanelMaterial(Material defaultDoorPanelMaterial)
   {
      this.defaultDoorPanelMaterial = defaultDoorPanelMaterial;
   }



   public Long getDefaultDoorPanelMaterialId()
   {
      return defaultDoorPanelMaterialId;
   }



   public Project withDefaultDoorPanelMaterialId(Long defaultDoorPanelMaterialId)
   {
      this.defaultDoorPanelMaterialId = defaultDoorPanelMaterialId;
      return this;
   }



   public void setDefaultDoorPanelMaterialId(Long defaultDoorPanelMaterialId)
   {
      this.defaultDoorPanelMaterialId = defaultDoorPanelMaterialId;
   }



   public Material getDefaultDrawerFrontMaterial()
   {
      return defaultDrawerFrontMaterial;
   }



   public Project withDefaultDrawerFrontMaterial(Material defaultDrawerFrontMaterial)
   {
      this.defaultDrawerFrontMaterial = defaultDrawerFrontMaterial;
      return this;
   }



   public void setDefaultDrawerFrontMaterial(Material defaultDrawerFrontMaterial)
   {
      this.defaultDrawerFrontMaterial = defaultDrawerFrontMaterial;
   }



   public Long getDefaultDrawerFrontMaterialId()
   {
      return defaultDrawerFrontMaterialId;
   }



   public Project withDefaultDrawerFrontMaterialId(Long defaultDrawerFrontMaterialId)
   {
      this.defaultDrawerFrontMaterialId = defaultDrawerFrontMaterialId;
      return this;
   }



   public void setDefaultDrawerFrontMaterialId(Long defaultDrawerFrontMaterialId)
   {
      this.defaultDrawerFrontMaterialId = defaultDrawerFrontMaterialId;
   }



   public Material getDefaultDrawerBoxMaterial()
   {
      return defaultDrawerBoxMaterial;
   }



   public Project withDefaultDrawerBoxMaterial(Material defaultDrawerBoxMaterial)
   {
      this.defaultDrawerBoxMaterial = defaultDrawerBoxMaterial;
      return this;
   }



   public void setDefaultDrawerBoxMaterial(Material defaultDrawerBoxMaterial)
   {
      this.defaultDrawerBoxMaterial = defaultDrawerBoxMaterial;
   }



   public Long getDefaultDrawerBoxMaterialId()
   {
      return defaultDrawerBoxMaterialId;
   }



   public Project withDefaultDrawerBoxMaterialId(Long defaultDrawerBoxMaterialId)
   {
      this.defaultDrawerBoxMaterialId = defaultDrawerBoxMaterialId;
      return this;
   }



   public void setDefaultDrawerBoxMaterialId(Long defaultDrawerBoxMaterialId)
   {
      this.defaultDrawerBoxMaterialId = defaultDrawerBoxMaterialId;
   }



   public Instant getCreateDate()
   {
      return createDate;
   }



   public Project withCreateDate(Instant createDate)
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



   public Project withModifyDate(Instant modifyDate)
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

         table.addSection(new QFieldSection("identity", "Identity", new QIcon(ICON_NAME), Tier.T1, List.of("id", "userId", "name", "description", "unitSystemId")));
         table.addSection(new QFieldSection("boxMaterials", "Box Material Defaults", new QIcon("inventory_2"), Tier.T2, List.of("defaultBoxMaterialId", "defaultBackMaterialId", "defaultDrawerBoxMaterialId")));
         table.addSection(new QFieldSection("faceMaterials", "Face/Door Material Defaults", new QIcon("door_front"), Tier.T2, List.of("defaultFaceFrameMaterialId", "defaultDoorFrameMaterialId", "defaultDoorPanelMaterialId", "defaultDrawerFrontMaterialId")));
         table.addSection(new QFieldSection("dates", "Dates", new QIcon("event"), Tier.T3, List.of("createDate", "modifyDate")));

         return table;
      }
   }
}
