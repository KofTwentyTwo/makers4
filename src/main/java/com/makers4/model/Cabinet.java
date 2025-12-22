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
import com.makers4.model.lookup.CabinetType;
import com.makers4.model.lookup.DoorPanelStyle;
import com.makers4.model.lookup.DoorStyle;
import com.makers4.model.lookup.DrawerFrontStyle;
import com.makers4.model.lookup.DrawerSlideType;
import com.makers4.model.lookup.EdgeProfile;
import com.makers4.model.lookup.FaceType;
import com.makers4.model.lookup.FinishedEndStyle;
import com.makers4.model.lookup.Material;
import com.makers4.model.lookup.ToeKickStyle;
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
@Table(name = Cabinet.TABLE_NAME)
@QMetaDataProducingEntity(
   produceTableMetaData = true,
   tableMetaDataCustomizer = Cabinet.TableMetaDataCustomizer.class,
   producePossibleValueSource = true,
   childTables = {
      @ChildTable(
         joinFieldName = "cabinetId",
         childTableEntityClass = CabinetOpening.class,
         childJoin = @ChildJoin(enabled = true),
         childRecordListWidget = @ChildRecordListWidget(enabled = true, label = "Cabinet Openings", maxRows = 20)
      ),
      @ChildTable(
         joinFieldName = "cabinetId",
         childTableEntityClass = Part.class,
         childJoin = @ChildJoin(enabled = true),
         childRecordListWidget = @ChildRecordListWidget(enabled = true, label = "Parts", maxRows = 50)
      )
   }
)
public class Cabinet extends QRecordEntity
{
   public static final String TABLE_NAME  = "cabinet";
   public static final String TABLE_LABEL = "Cabinets";
   public static final String ICON_NAME   = "kitchen";

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   @QField(isEditable = false, isPrimaryKey = true)
   private Long id;

   @ManyToOne
   @JoinColumn(name = "project_id", insertable = false, updatable = false)
   private Project project;

   @Column(name = "project_id", nullable = false)
   @QField(isRequired = true, backendName = "project_id", possibleValueSourceName = Project.TABLE_NAME, label = "Project")
   private Long projectId;

   @Column(name = "name", nullable = false, length = 120)
   @QField(isRequired = true, maxLength = 120, backendName = "name", valueTooLongBehavior = ValueTooLongBehavior.ERROR)
   private String name;

   @Column(name = "description", columnDefinition = "TEXT")
   @QField(backendName = "description")
   private String description;

   ////////////////////////////////
   // ── Core Dimensions (mm) ── //
   ////////////////////////////////

   @Column(name = "width_mm", nullable = false)
   @QField(isRequired = true, backendName = "width_mm", label = "Width (mm)")
   private Integer widthMm;

   @Column(name = "height_mm", nullable = false)
   @QField(isRequired = true, backendName = "height_mm", label = "Height (mm)")
   private Integer heightMm;

   @Column(name = "depth_mm", nullable = false)
   @QField(isRequired = true, backendName = "depth_mm", label = "Depth (mm)")
   private Integer depthMm;

   ////////////////////////
   // ── Construction ── //
   ////////////////////////

   @ManyToOne
   @JoinColumn(name = "cabinet_type_id", insertable = false, updatable = false)
   private CabinetType cabinetType;

   @Column(name = "cabinet_type_id", nullable = false)
   @QField(isRequired = true, backendName = "cabinet_type_id", possibleValueSourceName = CabinetType.TABLE_NAME, label = "Cabinet Type")
   private Long cabinetTypeId;

   @ManyToOne
   @JoinColumn(name = "face_type_id", insertable = false, updatable = false)
   private FaceType faceType;

   @Column(name = "face_type_id", nullable = false)
   @QField(isRequired = true, backendName = "face_type_id", possibleValueSourceName = FaceType.TABLE_NAME, label = "Face Type")
   private Long faceTypeId;

   ////////////////////
   // ── Toe Kick ── //
   ////////////////////

   @ManyToOne
   @JoinColumn(name = "toe_kick_style_id", insertable = false, updatable = false)
   private ToeKickStyle toeKickStyle;

   @Column(name = "toe_kick_style_id", nullable = false)
   @QField(isRequired = true, backendName = "toe_kick_style_id", possibleValueSourceName = ToeKickStyle.TABLE_NAME, label = "Toe Kick Style")
   private Long toeKickStyleId;

   @Column(name = "toe_kick_height_mm")
   @QField(backendName = "toe_kick_height_mm", label = "Toe Kick Height (mm)")
   private Integer toeKickHeightMm;

   @Column(name = "toe_kick_depth_mm")
   @QField(backendName = "toe_kick_depth_mm", label = "Toe Kick Depth (mm)")
   private Integer toeKickDepthMm;

   @ManyToOne
   @JoinColumn(name = "toe_kick_material_id", insertable = false, updatable = false)
   private Material toeKickMaterial;

   @Column(name = "toe_kick_material_id")
   @QField(backendName = "toe_kick_material_id", possibleValueSourceName = Material.TABLE_NAME, label = "Toe Kick Material")
   private Long toeKickMaterialId;

   ///////////////////////
   // ── Box/Carcass ── //
   ///////////////////////

   @ManyToOne
   @JoinColumn(name = "box_material_id", insertable = false, updatable = false)
   private Material boxMaterial;

   @Column(name = "box_material_id")
   @QField(backendName = "box_material_id", possibleValueSourceName = Material.TABLE_NAME, label = "Box Material")
   private Long boxMaterialId;

   @ManyToOne
   @JoinColumn(name = "back_material_id", insertable = false, updatable = false)
   private Material backMaterial;

   @Column(name = "back_material_id")
   @QField(backendName = "back_material_id", possibleValueSourceName = Material.TABLE_NAME, label = "Back Material")
   private Long backMaterialId;

   @ManyToOne
   @JoinColumn(name = "shelf_material_id", insertable = false, updatable = false)
   private Material shelfMaterial;

   @Column(name = "shelf_material_id")
   @QField(backendName = "shelf_material_id", possibleValueSourceName = Material.TABLE_NAME, label = "Shelf Material")
   private Long shelfMaterialId;

   /////////////////////////
   // ── Finished Ends ── //
   /////////////////////////

   @Column(name = "left_end_finished", nullable = false)
   @QField(backendName = "left_end_finished", defaultValue = "false", label = "Left End Finished")
   private Boolean leftEndFinished;

   @Column(name = "right_end_finished", nullable = false)
   @QField(backendName = "right_end_finished", defaultValue = "false", label = "Right End Finished")
   private Boolean rightEndFinished;

   @Column(name = "back_finished", nullable = false)
   @QField(backendName = "back_finished", defaultValue = "false", label = "Back Finished")
   private Boolean backFinished;

   @ManyToOne
   @JoinColumn(name = "finished_end_material_id", insertable = false, updatable = false)
   private Material finishedEndMaterial;

   @Column(name = "finished_end_material_id")
   @QField(backendName = "finished_end_material_id", possibleValueSourceName = Material.TABLE_NAME, label = "Finished End Material")
   private Long finishedEndMaterialId;

   @ManyToOne
   @JoinColumn(name = "finished_end_style_id", insertable = false, updatable = false)
   private FinishedEndStyle finishedEndStyle;

   @Column(name = "finished_end_style_id")
   @QField(backendName = "finished_end_style_id", possibleValueSourceName = FinishedEndStyle.TABLE_NAME, label = "Finished End Style")
   private Long finishedEndStyleId;

   //////////////////////
   // ── Face Frame ── //
   //////////////////////

   @ManyToOne
   @JoinColumn(name = "face_frame_material_id", insertable = false, updatable = false)
   private Material faceFrameMaterial;

   @Column(name = "face_frame_material_id")
   @QField(backendName = "face_frame_material_id", possibleValueSourceName = Material.TABLE_NAME, label = "Face Frame Material")
   private Long faceFrameMaterialId;

   @Column(name = "face_frame_rail_width_mm")
   @QField(backendName = "face_frame_rail_width_mm", label = "Face Frame Rail Width (mm)")
   private Integer faceFrameRailWidthMm;

   @Column(name = "face_frame_stile_width_mm")
   @QField(backendName = "face_frame_stile_width_mm", label = "Face Frame Stile Width (mm)")
   private Integer faceFrameStileWidthMm;

   /////////////////////////
   // ── Door Defaults ── //
   /////////////////////////

   @ManyToOne
   @JoinColumn(name = "door_style_id", insertable = false, updatable = false)
   private DoorStyle doorStyle;

   @Column(name = "door_style_id")
   @QField(backendName = "door_style_id", possibleValueSourceName = DoorStyle.TABLE_NAME, label = "Door Style")
   private Long doorStyleId;

   @ManyToOne
   @JoinColumn(name = "door_frame_material_id", insertable = false, updatable = false)
   private Material doorFrameMaterial;

   @Column(name = "door_frame_material_id")
   @QField(backendName = "door_frame_material_id", possibleValueSourceName = Material.TABLE_NAME, label = "Door Frame Material")
   private Long doorFrameMaterialId;

   @ManyToOne
   @JoinColumn(name = "door_panel_style_id", insertable = false, updatable = false)
   private DoorPanelStyle doorPanelStyle;

   @Column(name = "door_panel_style_id")
   @QField(backendName = "door_panel_style_id", possibleValueSourceName = DoorPanelStyle.TABLE_NAME, label = "Door Panel Style")
   private Long doorPanelStyleId;

   @ManyToOne
   @JoinColumn(name = "door_panel_material_id", insertable = false, updatable = false)
   private Material doorPanelMaterial;

   @Column(name = "door_panel_material_id")
   @QField(backendName = "door_panel_material_id", possibleValueSourceName = Material.TABLE_NAME, label = "Door Panel Material")
   private Long doorPanelMaterialId;

   @Column(name = "door_rail_width_mm")
   @QField(backendName = "door_rail_width_mm", label = "Door Rail Width (mm)")
   private Integer doorRailWidthMm;

   @Column(name = "door_stile_width_mm")
   @QField(backendName = "door_stile_width_mm", label = "Door Stile Width (mm)")
   private Integer doorStileWidthMm;

   @Column(name = "door_groove_depth_mm")
   @QField(backendName = "door_groove_depth_mm", label = "Door Groove Depth (mm)", defaultValue = "10")
   private Integer doorGrooveDepthMm;

   @Column(name = "door_panel_gap_mm")
   @QField(backendName = "door_panel_gap_mm", label = "Door Panel Gap (mm)", defaultValue = "2")
   private Integer doorPanelGapMm;
   /////////////////////////////////
   // ── Drawer Front Defaults ── //
   /////////////////////////////////

   @ManyToOne
   @JoinColumn(name = "drawer_front_style_id", insertable = false, updatable = false)
   private DrawerFrontStyle drawerFrontStyle;

   @Column(name = "drawer_front_style_id")
   @QField(backendName = "drawer_front_style_id", possibleValueSourceName = DrawerFrontStyle.TABLE_NAME, label = "Drawer Front Style")
   private Long drawerFrontStyleId;

   @ManyToOne
   @JoinColumn(name = "drawer_front_material_id", insertable = false, updatable = false)
   private Material drawerFrontMaterial;

   @Column(name = "drawer_front_material_id")
   @QField(backendName = "drawer_front_material_id", possibleValueSourceName = Material.TABLE_NAME, label = "Drawer Front Material")
   private Long drawerFrontMaterialId;

   @ManyToOne
   @JoinColumn(name = "drawer_front_edge_profile_id", insertable = false, updatable = false)
   private EdgeProfile drawerFrontEdgeProfile;

   @Column(name = "drawer_front_edge_profile_id")
   @QField(backendName = "drawer_front_edge_profile_id", possibleValueSourceName = EdgeProfile.TABLE_NAME, label = "Drawer Front Edge Profile")
   private Long drawerFrontEdgeProfileId;

   // ── Drawer Box ──

   @ManyToOne
   @JoinColumn(name = "drawer_box_material_id", insertable = false, updatable = false)
   private Material drawerBoxMaterial;

   @Column(name = "drawer_box_material_id")
   @QField(backendName = "drawer_box_material_id", possibleValueSourceName = Material.TABLE_NAME, label = "Drawer Box Material")
   private Long drawerBoxMaterialId;

   @ManyToOne
   @JoinColumn(name = "drawer_box_bottom_material_id", insertable = false, updatable = false)
   private Material drawerBoxBottomMaterial;

   @Column(name = "drawer_box_bottom_material_id")
   @QField(backendName = "drawer_box_bottom_material_id", possibleValueSourceName = Material.TABLE_NAME, label = "Drawer Box Bottom Material")
   private Long drawerBoxBottomMaterialId;

   @ManyToOne
   @JoinColumn(name = "drawer_slide_type_id", insertable = false, updatable = false)
   private DrawerSlideType drawerSlideType;

   @Column(name = "drawer_slide_type_id")
   @QField(backendName = "drawer_slide_type_id", possibleValueSourceName = DrawerSlideType.TABLE_NAME, label = "Drawer Slide Type")
   private Long drawerSlideTypeId;

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



   public Cabinet withId(Long id)
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



   public Cabinet withProject(Project project)
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



   public Cabinet withProjectId(Long projectId)
   {
      this.projectId = projectId;
      return this;
   }



   public void setProjectId(Long projectId)
   {
      this.projectId = projectId;
   }



   public String getName()
   {
      return name;
   }



   public Cabinet withName(String name)
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



   public Cabinet withDescription(String description)
   {
      this.description = description;
      return this;
   }



   public void setDescription(String description)
   {
      this.description = description;
   }



   public Integer getWidthMm()
   {
      return widthMm;
   }



   public Cabinet withWidthMm(Integer widthMm)
   {
      this.widthMm = widthMm;
      return this;
   }



   public void setWidthMm(Integer widthMm)
   {
      this.widthMm = widthMm;
   }



   public Integer getHeightMm()
   {
      return heightMm;
   }



   public Cabinet withHeightMm(Integer heightMm)
   {
      this.heightMm = heightMm;
      return this;
   }



   public void setHeightMm(Integer heightMm)
   {
      this.heightMm = heightMm;
   }



   public Integer getDepthMm()
   {
      return depthMm;
   }



   public Cabinet withDepthMm(Integer depthMm)
   {
      this.depthMm = depthMm;
      return this;
   }



   public void setDepthMm(Integer depthMm)
   {
      this.depthMm = depthMm;
   }



   public CabinetType getCabinetType()
   {
      return cabinetType;
   }



   public Cabinet withCabinetType(CabinetType cabinetType)
   {
      this.cabinetType = cabinetType;
      return this;
   }



   public void setCabinetType(CabinetType cabinetType)
   {
      this.cabinetType = cabinetType;
   }



   public Long getCabinetTypeId()
   {
      return cabinetTypeId;
   }



   public Cabinet withCabinetTypeId(Long cabinetTypeId)
   {
      this.cabinetTypeId = cabinetTypeId;
      return this;
   }



   public void setCabinetTypeId(Long cabinetTypeId)
   {
      this.cabinetTypeId = cabinetTypeId;
   }



   public FaceType getFaceType()
   {
      return faceType;
   }



   public Cabinet withFaceType(FaceType faceType)
   {
      this.faceType = faceType;
      return this;
   }



   public void setFaceType(FaceType faceType)
   {
      this.faceType = faceType;
   }



   public Long getFaceTypeId()
   {
      return faceTypeId;
   }



   public Cabinet withFaceTypeId(Long faceTypeId)
   {
      this.faceTypeId = faceTypeId;
      return this;
   }



   public void setFaceTypeId(Long faceTypeId)
   {
      this.faceTypeId = faceTypeId;
   }



   public ToeKickStyle getToeKickStyle()
   {
      return toeKickStyle;
   }



   public Cabinet withToeKickStyle(ToeKickStyle toeKickStyle)
   {
      this.toeKickStyle = toeKickStyle;
      return this;
   }



   public void setToeKickStyle(ToeKickStyle toeKickStyle)
   {
      this.toeKickStyle = toeKickStyle;
   }



   public Long getToeKickStyleId()
   {
      return toeKickStyleId;
   }



   public Cabinet withToeKickStyleId(Long toeKickStyleId)
   {
      this.toeKickStyleId = toeKickStyleId;
      return this;
   }



   public void setToeKickStyleId(Long toeKickStyleId)
   {
      this.toeKickStyleId = toeKickStyleId;
   }



   public Integer getToeKickHeightMm()
   {
      return toeKickHeightMm;
   }



   public Cabinet withToeKickHeightMm(Integer toeKickHeightMm)
   {
      this.toeKickHeightMm = toeKickHeightMm;
      return this;
   }



   public void setToeKickHeightMm(Integer toeKickHeightMm)
   {
      this.toeKickHeightMm = toeKickHeightMm;
   }



   public Integer getToeKickDepthMm()
   {
      return toeKickDepthMm;
   }



   public Cabinet withToeKickDepthMm(Integer toeKickDepthMm)
   {
      this.toeKickDepthMm = toeKickDepthMm;
      return this;
   }



   public void setToeKickDepthMm(Integer toeKickDepthMm)
   {
      this.toeKickDepthMm = toeKickDepthMm;
   }



   public Material getToeKickMaterial()
   {
      return toeKickMaterial;
   }



   public Cabinet withToeKickMaterial(Material toeKickMaterial)
   {
      this.toeKickMaterial = toeKickMaterial;
      return this;
   }



   public void setToeKickMaterial(Material toeKickMaterial)
   {
      this.toeKickMaterial = toeKickMaterial;
   }



   public Long getToeKickMaterialId()
   {
      return toeKickMaterialId;
   }



   public Cabinet withToeKickMaterialId(Long toeKickMaterialId)
   {
      this.toeKickMaterialId = toeKickMaterialId;
      return this;
   }



   public void setToeKickMaterialId(Long toeKickMaterialId)
   {
      this.toeKickMaterialId = toeKickMaterialId;
   }



   public Material getBoxMaterial()
   {
      return boxMaterial;
   }



   public Cabinet withBoxMaterial(Material boxMaterial)
   {
      this.boxMaterial = boxMaterial;
      return this;
   }



   public void setBoxMaterial(Material boxMaterial)
   {
      this.boxMaterial = boxMaterial;
   }



   public Long getBoxMaterialId()
   {
      return boxMaterialId;
   }



   public Cabinet withBoxMaterialId(Long boxMaterialId)
   {
      this.boxMaterialId = boxMaterialId;
      return this;
   }



   public void setBoxMaterialId(Long boxMaterialId)
   {
      this.boxMaterialId = boxMaterialId;
   }



   public Material getBackMaterial()
   {
      return backMaterial;
   }



   public Cabinet withBackMaterial(Material backMaterial)
   {
      this.backMaterial = backMaterial;
      return this;
   }



   public void setBackMaterial(Material backMaterial)
   {
      this.backMaterial = backMaterial;
   }



   public Long getBackMaterialId()
   {
      return backMaterialId;
   }



   public Cabinet withBackMaterialId(Long backMaterialId)
   {
      this.backMaterialId = backMaterialId;
      return this;
   }



   public void setBackMaterialId(Long backMaterialId)
   {
      this.backMaterialId = backMaterialId;
   }



   public Material getShelfMaterial()
   {
      return shelfMaterial;
   }



   public Cabinet withShelfMaterial(Material shelfMaterial)
   {
      this.shelfMaterial = shelfMaterial;
      return this;
   }



   public void setShelfMaterial(Material shelfMaterial)
   {
      this.shelfMaterial = shelfMaterial;
   }



   public Long getShelfMaterialId()
   {
      return shelfMaterialId;
   }



   public Cabinet withShelfMaterialId(Long shelfMaterialId)
   {
      this.shelfMaterialId = shelfMaterialId;
      return this;
   }



   public void setShelfMaterialId(Long shelfMaterialId)
   {
      this.shelfMaterialId = shelfMaterialId;
   }



   public Boolean getLeftEndFinished()
   {
      return leftEndFinished;
   }



   public Cabinet withLeftEndFinished(Boolean leftEndFinished)
   {
      this.leftEndFinished = leftEndFinished;
      return this;
   }



   public void setLeftEndFinished(Boolean leftEndFinished)
   {
      this.leftEndFinished = leftEndFinished;
   }



   public Boolean getRightEndFinished()
   {
      return rightEndFinished;
   }



   public Cabinet withRightEndFinished(Boolean rightEndFinished)
   {
      this.rightEndFinished = rightEndFinished;
      return this;
   }



   public void setRightEndFinished(Boolean rightEndFinished)
   {
      this.rightEndFinished = rightEndFinished;
   }



   public Boolean getBackFinished()
   {
      return backFinished;
   }



   public Cabinet withBackFinished(Boolean backFinished)
   {
      this.backFinished = backFinished;
      return this;
   }



   public void setBackFinished(Boolean backFinished)
   {
      this.backFinished = backFinished;
   }



   public Material getFinishedEndMaterial()
   {
      return finishedEndMaterial;
   }



   public Cabinet withFinishedEndMaterial(Material finishedEndMaterial)
   {
      this.finishedEndMaterial = finishedEndMaterial;
      return this;
   }



   public void setFinishedEndMaterial(Material finishedEndMaterial)
   {
      this.finishedEndMaterial = finishedEndMaterial;
   }



   public Long getFinishedEndMaterialId()
   {
      return finishedEndMaterialId;
   }



   public Cabinet withFinishedEndMaterialId(Long finishedEndMaterialId)
   {
      this.finishedEndMaterialId = finishedEndMaterialId;
      return this;
   }



   public void setFinishedEndMaterialId(Long finishedEndMaterialId)
   {
      this.finishedEndMaterialId = finishedEndMaterialId;
   }



   public FinishedEndStyle getFinishedEndStyle()
   {
      return finishedEndStyle;
   }



   public Cabinet withFinishedEndStyle(FinishedEndStyle finishedEndStyle)
   {
      this.finishedEndStyle = finishedEndStyle;
      return this;
   }



   public void setFinishedEndStyle(FinishedEndStyle finishedEndStyle)
   {
      this.finishedEndStyle = finishedEndStyle;
   }



   public Long getFinishedEndStyleId()
   {
      return finishedEndStyleId;
   }



   public Cabinet withFinishedEndStyleId(Long finishedEndStyleId)
   {
      this.finishedEndStyleId = finishedEndStyleId;
      return this;
   }



   public void setFinishedEndStyleId(Long finishedEndStyleId)
   {
      this.finishedEndStyleId = finishedEndStyleId;
   }



   public Material getFaceFrameMaterial()
   {
      return faceFrameMaterial;
   }



   public Cabinet withFaceFrameMaterial(Material faceFrameMaterial)
   {
      this.faceFrameMaterial = faceFrameMaterial;
      return this;
   }



   public void setFaceFrameMaterial(Material faceFrameMaterial)
   {
      this.faceFrameMaterial = faceFrameMaterial;
   }



   public Long getFaceFrameMaterialId()
   {
      return faceFrameMaterialId;
   }



   public Cabinet withFaceFrameMaterialId(Long faceFrameMaterialId)
   {
      this.faceFrameMaterialId = faceFrameMaterialId;
      return this;
   }



   public void setFaceFrameMaterialId(Long faceFrameMaterialId)
   {
      this.faceFrameMaterialId = faceFrameMaterialId;
   }



   public Integer getFaceFrameRailWidthMm()
   {
      return faceFrameRailWidthMm;
   }



   public Cabinet withFaceFrameRailWidthMm(Integer faceFrameRailWidthMm)
   {
      this.faceFrameRailWidthMm = faceFrameRailWidthMm;
      return this;
   }



   public void setFaceFrameRailWidthMm(Integer faceFrameRailWidthMm)
   {
      this.faceFrameRailWidthMm = faceFrameRailWidthMm;
   }



   public Integer getFaceFrameStileWidthMm()
   {
      return faceFrameStileWidthMm;
   }



   public Cabinet withFaceFrameStileWidthMm(Integer faceFrameStileWidthMm)
   {
      this.faceFrameStileWidthMm = faceFrameStileWidthMm;
      return this;
   }



   public void setFaceFrameStileWidthMm(Integer faceFrameStileWidthMm)
   {
      this.faceFrameStileWidthMm = faceFrameStileWidthMm;
   }



   public DoorStyle getDoorStyle()
   {
      return doorStyle;
   }



   public Cabinet withDoorStyle(DoorStyle doorStyle)
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



   public Cabinet withDoorStyleId(Long doorStyleId)
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



   public Cabinet withDoorFrameMaterial(Material doorFrameMaterial)
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



   public Cabinet withDoorFrameMaterialId(Long doorFrameMaterialId)
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



   public Cabinet withDoorPanelStyle(DoorPanelStyle doorPanelStyle)
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



   public Cabinet withDoorPanelStyleId(Long doorPanelStyleId)
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



   public Cabinet withDoorPanelMaterial(Material doorPanelMaterial)
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



   public Cabinet withDoorPanelMaterialId(Long doorPanelMaterialId)
   {
      this.doorPanelMaterialId = doorPanelMaterialId;
      return this;
   }



   public void setDoorPanelMaterialId(Long doorPanelMaterialId)
   {
      this.doorPanelMaterialId = doorPanelMaterialId;
   }



   public Integer getDoorRailWidthMm()
   {
      return doorRailWidthMm;
   }



   public Cabinet withDoorRailWidthMm(Integer doorRailWidthMm)
   {
      this.doorRailWidthMm = doorRailWidthMm;
      return this;
   }



   public void setDoorRailWidthMm(Integer doorRailWidthMm)
   {
      this.doorRailWidthMm = doorRailWidthMm;
   }



   public Integer getDoorStileWidthMm()
   {
      return doorStileWidthMm;
   }



   public Cabinet withDoorStileWidthMm(Integer doorStileWidthMm)
   {
      this.doorStileWidthMm = doorStileWidthMm;
      return this;
   }



   public void setDoorStileWidthMm(Integer doorStileWidthMm)
   {
      this.doorStileWidthMm = doorStileWidthMm;
   }



   public Integer getDoorGrooveDepthMm()
   {
      return doorGrooveDepthMm;
   }



   public Cabinet withDoorGrooveDepthMm(Integer doorGrooveDepthMm)
   {
      this.doorGrooveDepthMm = doorGrooveDepthMm;
      return this;
   }



   public void setDoorGrooveDepthMm(Integer doorGrooveDepthMm)
   {
      this.doorGrooveDepthMm = doorGrooveDepthMm;
   }



   public Integer getDoorPanelGapMm()
   {
      return doorPanelGapMm;
   }



   public Cabinet withDoorPanelGapMm(Integer doorPanelGapMm)
   {
      this.doorPanelGapMm = doorPanelGapMm;
      return this;
   }



   public void setDoorPanelGapMm(Integer doorPanelGapMm)
   {
      this.doorPanelGapMm = doorPanelGapMm;
   }



   public DrawerFrontStyle getDrawerFrontStyle()
   {
      return drawerFrontStyle;
   }



   public Cabinet withDrawerFrontStyle(DrawerFrontStyle drawerFrontStyle)
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



   public Cabinet withDrawerFrontStyleId(Long drawerFrontStyleId)
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



   public Cabinet withDrawerFrontMaterial(Material drawerFrontMaterial)
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



   public Cabinet withDrawerFrontMaterialId(Long drawerFrontMaterialId)
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



   public Cabinet withDrawerFrontEdgeProfile(EdgeProfile drawerFrontEdgeProfile)
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



   public Cabinet withDrawerFrontEdgeProfileId(Long drawerFrontEdgeProfileId)
   {
      this.drawerFrontEdgeProfileId = drawerFrontEdgeProfileId;
      return this;
   }



   public void setDrawerFrontEdgeProfileId(Long drawerFrontEdgeProfileId)
   {
      this.drawerFrontEdgeProfileId = drawerFrontEdgeProfileId;
   }



   public Material getDrawerBoxMaterial()
   {
      return drawerBoxMaterial;
   }



   public Cabinet withDrawerBoxMaterial(Material drawerBoxMaterial)
   {
      this.drawerBoxMaterial = drawerBoxMaterial;
      return this;
   }



   public void setDrawerBoxMaterial(Material drawerBoxMaterial)
   {
      this.drawerBoxMaterial = drawerBoxMaterial;
   }



   public Long getDrawerBoxMaterialId()
   {
      return drawerBoxMaterialId;
   }



   public Cabinet withDrawerBoxMaterialId(Long drawerBoxMaterialId)
   {
      this.drawerBoxMaterialId = drawerBoxMaterialId;
      return this;
   }



   public void setDrawerBoxMaterialId(Long drawerBoxMaterialId)
   {
      this.drawerBoxMaterialId = drawerBoxMaterialId;
   }



   public Material getDrawerBoxBottomMaterial()
   {
      return drawerBoxBottomMaterial;
   }



   public Cabinet withDrawerBoxBottomMaterial(Material drawerBoxBottomMaterial)
   {
      this.drawerBoxBottomMaterial = drawerBoxBottomMaterial;
      return this;
   }



   public void setDrawerBoxBottomMaterial(Material drawerBoxBottomMaterial)
   {
      this.drawerBoxBottomMaterial = drawerBoxBottomMaterial;
   }



   public Long getDrawerBoxBottomMaterialId()
   {
      return drawerBoxBottomMaterialId;
   }



   public Cabinet withDrawerBoxBottomMaterialId(Long drawerBoxBottomMaterialId)
   {
      this.drawerBoxBottomMaterialId = drawerBoxBottomMaterialId;
      return this;
   }



   public void setDrawerBoxBottomMaterialId(Long drawerBoxBottomMaterialId)
   {
      this.drawerBoxBottomMaterialId = drawerBoxBottomMaterialId;
   }



   public DrawerSlideType getDrawerSlideType()
   {
      return drawerSlideType;
   }



   public Cabinet withDrawerSlideType(DrawerSlideType drawerSlideType)
   {
      this.drawerSlideType = drawerSlideType;
      return this;
   }



   public void setDrawerSlideType(DrawerSlideType drawerSlideType)
   {
      this.drawerSlideType = drawerSlideType;
   }



   public Long getDrawerSlideTypeId()
   {
      return drawerSlideTypeId;
   }



   public Cabinet withDrawerSlideTypeId(Long drawerSlideTypeId)
   {
      this.drawerSlideTypeId = drawerSlideTypeId;
      return this;
   }



   public void setDrawerSlideTypeId(Long drawerSlideTypeId)
   {
      this.drawerSlideTypeId = drawerSlideTypeId;
   }



   public Instant getCreateDate()
   {
      return createDate;
   }



   public Cabinet withCreateDate(Instant createDate)
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



   public Cabinet withModifyDate(Instant modifyDate)
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

         table.addSection(new QFieldSection("identity", "Identity", new QIcon(ICON_NAME), Tier.T1, List.of("id", "projectId", "name", "description")));
         table.addSection(new QFieldSection("dimensions", "Dimensions", new QIcon("straighten"), Tier.T2, List.of("widthMm", "heightMm", "depthMm")));
         table.addSection(new QFieldSection("construction", "Construction", new QIcon("build"), Tier.T2, List.of("cabinetTypeId", "faceTypeId")));
         table.addSection(new QFieldSection("toeKick", "Toe Kick", new QIcon("height"), Tier.T2, List.of("toeKickStyleId", "toeKickHeightMm", "toeKickDepthMm", "toeKickMaterialId")));
         table.addSection(new QFieldSection("boxMaterials", "Box/Carcass Materials", new QIcon("inventory_2"), Tier.T2, List.of("boxMaterialId", "backMaterialId", "shelfMaterialId")));
         table.addSection(new QFieldSection("finishedEnds", "Finished Ends", new QIcon("flip"), Tier.T2, List.of("leftEndFinished", "rightEndFinished", "backFinished", "finishedEndMaterialId", "finishedEndStyleId")));
         table.addSection(new QFieldSection("faceFrame", "Face Frame", new QIcon("view_quilt"), Tier.T2, List.of("faceFrameMaterialId", "faceFrameRailWidthMm", "faceFrameStileWidthMm")));
         table.addSection(new QFieldSection("doors", "Door Defaults", new QIcon("door_sliding"), Tier.T2, List.of("doorStyleId", "doorFrameMaterialId", "doorPanelStyleId", "doorPanelMaterialId", "doorRailWidthMm", "doorStileWidthMm", "doorGrooveDepthMm", "doorPanelGapMm")));
         table.addSection(new QFieldSection("drawerFronts", "Drawer Front Defaults", new QIcon("drag_indicator"), Tier.T2, List.of("drawerFrontStyleId", "drawerFrontMaterialId", "drawerFrontEdgeProfileId")));
         table.addSection(new QFieldSection("drawerBox", "Drawer Box", new QIcon("inbox"), Tier.T2, List.of("drawerBoxMaterialId", "drawerBoxBottomMaterialId", "drawerSlideTypeId")));
         table.addSection(new QFieldSection("dates", "Dates", new QIcon("event"), Tier.T3, List.of("createDate", "modifyDate")));

         return table;
      }
   }
}
