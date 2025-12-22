package com.makers4.model;


import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.data.QField;
import com.kingsrook.qqq.backend.core.model.data.QRecordEntity;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.fields.DynamicDefaultValueBehavior;
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
import com.makers4.model.lookup.DoorPanelStyle;
import com.makers4.model.lookup.DoorStyle;
import com.makers4.model.lookup.DrawerFrontStyle;
import com.makers4.model.lookup.EdgeProfile;
import com.makers4.model.lookup.Material;
import com.makers4.model.lookup.OpeningType;
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
@Table(name = CabinetOpening.TABLE_NAME)
@QMetaDataProducingEntity(
   produceTableMetaData = true,
   tableMetaDataCustomizer = CabinetOpening.TableMetaDataCustomizer.class,
   producePossibleValueSource = true,
   childTables = {
      @ChildTable(
         joinFieldName = "cabinetOpeningId",
         childTableEntityClass = Part.class,
         childJoin = @ChildJoin(enabled = true),
         childRecordListWidget = @ChildRecordListWidget(enabled = true, label = "Parts", maxRows = 30)
      )
   }
)
public class CabinetOpening extends QRecordEntity
{
   public static final String TABLE_NAME  = "cabinet_opening";
   public static final String TABLE_LABEL = "Cabinet Openings";
   public static final String ICON_NAME   = "door_front";

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   @QField(isEditable = false, isPrimaryKey = true)
   private Long id;

   @ManyToOne
   @JoinColumn(name = "cabinet_id", insertable = false, updatable = false)
   private Cabinet cabinet;

   @Column(name = "cabinet_id", nullable = false)
   @QField(isRequired = true, backendName = "cabinet_id", possibleValueSourceName = Cabinet.TABLE_NAME, label = "Cabinet")
   private Long cabinetId;

   @ManyToOne
   @JoinColumn(name = "opening_type_id", insertable = false, updatable = false)
   private OpeningType openingType;

   @Column(name = "opening_type_id", nullable = false)
   @QField(isRequired = true, backendName = "opening_type_id", possibleValueSourceName = OpeningType.TABLE_NAME, label = "Opening Type")
   private Long openingTypeId;

   @Column(name = "sequence_number", nullable = false)
   @QField(isRequired = true, backendName = "sequence_number", label = "Sequence Number")
   private Integer sequenceNumber;

   @Column(name = "height_mm", nullable = false)
   @QField(isRequired = true, backendName = "height_mm", label = "Height (mm)")
   private Integer heightMm;

   ////////////////////////////////////////////////////////////
   // ── Door Overrides (nullable = inherit from Cabinet) ── //
   ////////////////////////////////////////////////////////////

   @ManyToOne
   @JoinColumn(name = "door_style_id", insertable = false, updatable = false)
   private DoorStyle doorStyle;

   @Column(name = "door_style_id")
   @QField(backendName = "door_style_id", possibleValueSourceName = DoorStyle.TABLE_NAME, label = "Door Style Override")
   private Long doorStyleId;

   @ManyToOne
   @JoinColumn(name = "door_frame_material_id", insertable = false, updatable = false)
   private Material doorFrameMaterial;

   @Column(name = "door_frame_material_id")
   @QField(backendName = "door_frame_material_id", possibleValueSourceName = Material.TABLE_NAME, label = "Door Frame Material Override")
   private Long doorFrameMaterialId;

   @ManyToOne
   @JoinColumn(name = "door_panel_style_id", insertable = false, updatable = false)
   private DoorPanelStyle doorPanelStyle;

   @Column(name = "door_panel_style_id")
   @QField(backendName = "door_panel_style_id", possibleValueSourceName = DoorPanelStyle.TABLE_NAME, label = "Door Panel Style Override")
   private Long doorPanelStyleId;

   @ManyToOne
   @JoinColumn(name = "door_panel_material_id", insertable = false, updatable = false)
   private Material doorPanelMaterial;

   @Column(name = "door_panel_material_id")
   @QField(backendName = "door_panel_material_id", possibleValueSourceName = Material.TABLE_NAME, label = "Door Panel Material Override")
   private Long doorPanelMaterialId;

   ////////////////////////////
   // ── Drawer Overrides ── //
   ////////////////////////////

   @ManyToOne
   @JoinColumn(name = "drawer_front_style_id", insertable = false, updatable = false)
   private DrawerFrontStyle drawerFrontStyle;

   @Column(name = "drawer_front_style_id")
   @QField(backendName = "drawer_front_style_id", possibleValueSourceName = DrawerFrontStyle.TABLE_NAME, label = "Drawer Front Style Override")
   private Long drawerFrontStyleId;

   @ManyToOne
   @JoinColumn(name = "drawer_front_material_id", insertable = false, updatable = false)
   private Material drawerFrontMaterial;

   @Column(name = "drawer_front_material_id")
   @QField(backendName = "drawer_front_material_id", possibleValueSourceName = Material.TABLE_NAME, label = "Drawer Front Material Override")
   private Long drawerFrontMaterialId;

   @ManyToOne
   @JoinColumn(name = "drawer_front_edge_profile_id", insertable = false, updatable = false)
   private EdgeProfile drawerFrontEdgeProfile;

   @Column(name = "drawer_front_edge_profile_id")
   @QField(backendName = "drawer_front_edge_profile_id", possibleValueSourceName = EdgeProfile.TABLE_NAME, label = "Drawer Front Edge Profile Override")
   private Long drawerFrontEdgeProfileId;

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



   public CabinetOpening withId(Long id)
   {
      this.id = id;
      return this;
   }



   public void setId(Long id)
   {
      this.id = id;
   }



   public Cabinet getCabinet()
   {
      return cabinet;
   }



   public CabinetOpening withCabinet(Cabinet cabinet)
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



   public CabinetOpening withCabinetId(Long cabinetId)
   {
      this.cabinetId = cabinetId;
      return this;
   }



   public void setCabinetId(Long cabinetId)
   {
      this.cabinetId = cabinetId;
   }



   public OpeningType getOpeningType()
   {
      return openingType;
   }



   public CabinetOpening withOpeningType(OpeningType openingType)
   {
      this.openingType = openingType;
      return this;
   }



   public void setOpeningType(OpeningType openingType)
   {
      this.openingType = openingType;
   }



   public Long getOpeningTypeId()
   {
      return openingTypeId;
   }



   public CabinetOpening withOpeningTypeId(Long openingTypeId)
   {
      this.openingTypeId = openingTypeId;
      return this;
   }



   public void setOpeningTypeId(Long openingTypeId)
   {
      this.openingTypeId = openingTypeId;
   }



   public Integer getSequenceNumber()
   {
      return sequenceNumber;
   }



   public CabinetOpening withSequenceNumber(Integer sequenceNumber)
   {
      this.sequenceNumber = sequenceNumber;
      return this;
   }



   public void setSequenceNumber(Integer sequenceNumber)
   {
      this.sequenceNumber = sequenceNumber;
   }



   public Integer getHeightMm()
   {
      return heightMm;
   }



   public CabinetOpening withHeightMm(Integer heightMm)
   {
      this.heightMm = heightMm;
      return this;
   }



   public void setHeightMm(Integer heightMm)
   {
      this.heightMm = heightMm;
   }



   public DoorStyle getDoorStyle()
   {
      return doorStyle;
   }



   public CabinetOpening withDoorStyle(DoorStyle doorStyle)
   {
      this.doorStyle = doorStyle;
      return this;
   }



   public void setDoorStyle(DoorStyle doorStyle)
   {
      this.doorStyle = doorStyle;
   }



   public Long getDoorStyleId()
   {
      return doorStyleId;
   }



   public CabinetOpening withDoorStyleId(Long doorStyleId)
   {
      this.doorStyleId = doorStyleId;
      return this;
   }



   public void setDoorStyleId(Long doorStyleId)
   {
      this.doorStyleId = doorStyleId;
   }



   public Material getDoorFrameMaterial()
   {
      return doorFrameMaterial;
   }



   public CabinetOpening withDoorFrameMaterial(Material doorFrameMaterial)
   {
      this.doorFrameMaterial = doorFrameMaterial;
      return this;
   }



   public void setDoorFrameMaterial(Material doorFrameMaterial)
   {
      this.doorFrameMaterial = doorFrameMaterial;
   }



   public Long getDoorFrameMaterialId()
   {
      return doorFrameMaterialId;
   }



   public CabinetOpening withDoorFrameMaterialId(Long doorFrameMaterialId)
   {
      this.doorFrameMaterialId = doorFrameMaterialId;
      return this;
   }



   public void setDoorFrameMaterialId(Long doorFrameMaterialId)
   {
      this.doorFrameMaterialId = doorFrameMaterialId;
   }



   public DoorPanelStyle getDoorPanelStyle()
   {
      return doorPanelStyle;
   }



   public CabinetOpening withDoorPanelStyle(DoorPanelStyle doorPanelStyle)
   {
      this.doorPanelStyle = doorPanelStyle;
      return this;
   }



   public void setDoorPanelStyle(DoorPanelStyle doorPanelStyle)
   {
      this.doorPanelStyle = doorPanelStyle;
   }



   public Long getDoorPanelStyleId()
   {
      return doorPanelStyleId;
   }



   public CabinetOpening withDoorPanelStyleId(Long doorPanelStyleId)
   {
      this.doorPanelStyleId = doorPanelStyleId;
      return this;
   }



   public void setDoorPanelStyleId(Long doorPanelStyleId)
   {
      this.doorPanelStyleId = doorPanelStyleId;
   }



   public Material getDoorPanelMaterial()
   {
      return doorPanelMaterial;
   }



   public CabinetOpening withDoorPanelMaterial(Material doorPanelMaterial)
   {
      this.doorPanelMaterial = doorPanelMaterial;
      return this;
   }



   public void setDoorPanelMaterial(Material doorPanelMaterial)
   {
      this.doorPanelMaterial = doorPanelMaterial;
   }



   public Long getDoorPanelMaterialId()
   {
      return doorPanelMaterialId;
   }



   public CabinetOpening withDoorPanelMaterialId(Long doorPanelMaterialId)
   {
      this.doorPanelMaterialId = doorPanelMaterialId;
      return this;
   }



   public void setDoorPanelMaterialId(Long doorPanelMaterialId)
   {
      this.doorPanelMaterialId = doorPanelMaterialId;
   }



   public DrawerFrontStyle getDrawerFrontStyle()
   {
      return drawerFrontStyle;
   }



   public CabinetOpening withDrawerFrontStyle(DrawerFrontStyle drawerFrontStyle)
   {
      this.drawerFrontStyle = drawerFrontStyle;
      return this;
   }



   public void setDrawerFrontStyle(DrawerFrontStyle drawerFrontStyle)
   {
      this.drawerFrontStyle = drawerFrontStyle;
   }



   public Long getDrawerFrontStyleId()
   {
      return drawerFrontStyleId;
   }



   public CabinetOpening withDrawerFrontStyleId(Long drawerFrontStyleId)
   {
      this.drawerFrontStyleId = drawerFrontStyleId;
      return this;
   }



   public void setDrawerFrontStyleId(Long drawerFrontStyleId)
   {
      this.drawerFrontStyleId = drawerFrontStyleId;
   }



   public Material getDrawerFrontMaterial()
   {
      return drawerFrontMaterial;
   }



   public CabinetOpening withDrawerFrontMaterial(Material drawerFrontMaterial)
   {
      this.drawerFrontMaterial = drawerFrontMaterial;
      return this;
   }



   public void setDrawerFrontMaterial(Material drawerFrontMaterial)
   {
      this.drawerFrontMaterial = drawerFrontMaterial;
   }



   public Long getDrawerFrontMaterialId()
   {
      return drawerFrontMaterialId;
   }



   public CabinetOpening withDrawerFrontMaterialId(Long drawerFrontMaterialId)
   {
      this.drawerFrontMaterialId = drawerFrontMaterialId;
      return this;
   }



   public void setDrawerFrontMaterialId(Long drawerFrontMaterialId)
   {
      this.drawerFrontMaterialId = drawerFrontMaterialId;
   }



   public EdgeProfile getDrawerFrontEdgeProfile()
   {
      return drawerFrontEdgeProfile;
   }



   public CabinetOpening withDrawerFrontEdgeProfile(EdgeProfile drawerFrontEdgeProfile)
   {
      this.drawerFrontEdgeProfile = drawerFrontEdgeProfile;
      return this;
   }



   public void setDrawerFrontEdgeProfile(EdgeProfile drawerFrontEdgeProfile)
   {
      this.drawerFrontEdgeProfile = drawerFrontEdgeProfile;
   }



   public Long getDrawerFrontEdgeProfileId()
   {
      return drawerFrontEdgeProfileId;
   }



   public CabinetOpening withDrawerFrontEdgeProfileId(Long drawerFrontEdgeProfileId)
   {
      this.drawerFrontEdgeProfileId = drawerFrontEdgeProfileId;
      return this;
   }



   public void setDrawerFrontEdgeProfileId(Long drawerFrontEdgeProfileId)
   {
      this.drawerFrontEdgeProfileId = drawerFrontEdgeProfileId;
   }



   public Instant getCreateDate()
   {
      return createDate;
   }



   public CabinetOpening withCreateDate(Instant createDate)
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



   public CabinetOpening withModifyDate(Instant modifyDate)
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
            .withRecordLabelFormat("Opening %s")
            .withRecordLabelFields(List.of("sequenceNumber"))
            .withBackendName(Makers4MetaDataProvider.RDBMS_BACKEND_NAME);

         table.addSection(new QFieldSection("identity", "Identity", new QIcon(ICON_NAME), Tier.T1, List.of("id", "cabinetId", "openingTypeId", "sequenceNumber", "heightMm")));
         table.addSection(new QFieldSection("doorOverrides", "Door Overrides", new QIcon("door_sliding"), Tier.T2, List.of("doorStyleId", "doorFrameMaterialId", "doorPanelStyleId", "doorPanelMaterialId")));
         table.addSection(new QFieldSection("drawerOverrides", "Drawer Overrides", new QIcon("drag_indicator"), Tier.T2, List.of("drawerFrontStyleId", "drawerFrontMaterialId", "drawerFrontEdgeProfileId")));
         table.addSection(new QFieldSection("dates", "Dates", new QIcon("event"), Tier.T3, List.of("createDate", "modifyDate")));

         return table;
      }
   }
}
