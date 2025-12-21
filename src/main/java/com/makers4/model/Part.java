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
import com.makers4.model.lookup.Material;
import com.makers4.model.lookup.PartType;
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
@Table(name = Part.TABLE_NAME)
@QMetaDataProducingEntity(produceTableMetaData = true, tableMetaDataCustomizer = Part.TableMetaDataCustomizer.class, producePossibleValueSource = true)
public class Part extends QRecordEntity
{
   public static final String TABLE_NAME  = "part";
   public static final String TABLE_LABEL = "Parts";
   public static final String ICON_NAME   = "handyman";

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
   @JoinColumn(name = "cabinet_opening_id", insertable = false, updatable = false)
   private CabinetOpening cabinetOpening;

   @Column(name = "cabinet_opening_id")
   @QField(backendName = "cabinet_opening_id", possibleValueSourceName = CabinetOpening.TABLE_NAME, label = "Cabinet Opening")
   private Long cabinetOpeningId;

   @ManyToOne
   @JoinColumn(name = "part_type_id", insertable = false, updatable = false)
   private PartType partType;

   @Column(name = "part_type_id", nullable = false)
   @QField(isRequired = true, backendName = "part_type_id", possibleValueSourceName = PartType.TABLE_NAME, label = "Part Type")
   private Long partTypeId;

   @Column(name = "name", nullable = false, length = 120)
   @QField(isRequired = true, maxLength = 120, backendName = "name", valueTooLongBehavior = ValueTooLongBehavior.ERROR)
   private String name;

   // ── Dimensions (mm) ──

   @Column(name = "length_mm", nullable = false)
   @QField(isRequired = true, backendName = "length_mm", label = "Length (mm)")
   private Integer lengthMm;

   @Column(name = "width_mm", nullable = false)
   @QField(isRequired = true, backendName = "width_mm", label = "Width (mm)")
   private Integer widthMm;

   @Column(name = "thickness_mm", nullable = false)
   @QField(isRequired = true, backendName = "thickness_mm", label = "Thickness (mm)")
   private Integer thicknessMm;

   @ManyToOne
   @JoinColumn(name = "material_id", insertable = false, updatable = false)
   private Material material;

   @Column(name = "material_id", nullable = false)
   @QField(isRequired = true, backendName = "material_id", possibleValueSourceName = Material.TABLE_NAME, label = "Material")
   private Long materialId;

   @Column(name = "quantity", nullable = false)
   @QField(isRequired = true, backendName = "quantity", defaultValue = "1")
   private Integer quantity;

   @Column(name = "notes", columnDefinition = "TEXT")
   @QField(backendName = "notes")
   private String notes;

   @Column(name = "edge_banding_notes", length = 255)
   @QField(maxLength = 255, backendName = "edge_banding_notes", label = "Edge Banding Notes")
   private String edgeBandingNotes;

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



   public Part withId(Long id)
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



   public Part withCabinet(Cabinet cabinet)
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



   public Part withCabinetId(Long cabinetId)
   {
      this.cabinetId = cabinetId;
      return this;
   }



   public void setCabinetId(Long cabinetId)
   {
      this.cabinetId = cabinetId;
   }



   public CabinetOpening getCabinetOpening()
   {
      return cabinetOpening;
   }



   public Part withCabinetOpening(CabinetOpening cabinetOpening)
   {
      this.cabinetOpening = cabinetOpening;
      return this;
   }



   public void setCabinetOpening(CabinetOpening cabinetOpening)
   {
      this.cabinetOpening = cabinetOpening;
   }



   public Long getCabinetOpeningId()
   {
      return cabinetOpeningId;
   }



   public Part withCabinetOpeningId(Long cabinetOpeningId)
   {
      this.cabinetOpeningId = cabinetOpeningId;
      return this;
   }



   public void setCabinetOpeningId(Long cabinetOpeningId)
   {
      this.cabinetOpeningId = cabinetOpeningId;
   }



   public PartType getPartType()
   {
      return partType;
   }



   public Part withPartType(PartType partType)
   {
      this.partType = partType;
      return this;
   }



   public void setPartType(PartType partType)
   {
      this.partType = partType;
   }



   public Long getPartTypeId()
   {
      return partTypeId;
   }



   public Part withPartTypeId(Long partTypeId)
   {
      this.partTypeId = partTypeId;
      return this;
   }



   public void setPartTypeId(Long partTypeId)
   {
      this.partTypeId = partTypeId;
   }



   public String getName()
   {
      return name;
   }



   public Part withName(String name)
   {
      this.name = name;
      return this;
   }



   public void setName(String name)
   {
      this.name = name;
   }



   public Integer getLengthMm()
   {
      return lengthMm;
   }



   public Part withLengthMm(Integer lengthMm)
   {
      this.lengthMm = lengthMm;
      return this;
   }



   public void setLengthMm(Integer lengthMm)
   {
      this.lengthMm = lengthMm;
   }



   public Integer getWidthMm()
   {
      return widthMm;
   }



   public Part withWidthMm(Integer widthMm)
   {
      this.widthMm = widthMm;
      return this;
   }



   public void setWidthMm(Integer widthMm)
   {
      this.widthMm = widthMm;
   }



   public Integer getThicknessMm()
   {
      return thicknessMm;
   }



   public Part withThicknessMm(Integer thicknessMm)
   {
      this.thicknessMm = thicknessMm;
      return this;
   }



   public void setThicknessMm(Integer thicknessMm)
   {
      this.thicknessMm = thicknessMm;
   }



   public Material getMaterial()
   {
      return material;
   }



   public Part withMaterial(Material material)
   {
      this.material = material;
      return this;
   }



   public void setMaterial(Material material)
   {
      this.material = material;
   }



   public Long getMaterialId()
   {
      return materialId;
   }



   public Part withMaterialId(Long materialId)
   {
      this.materialId = materialId;
      return this;
   }



   public void setMaterialId(Long materialId)
   {
      this.materialId = materialId;
   }



   public Integer getQuantity()
   {
      return quantity;
   }



   public Part withQuantity(Integer quantity)
   {
      this.quantity = quantity;
      return this;
   }



   public void setQuantity(Integer quantity)
   {
      this.quantity = quantity;
   }



   public String getNotes()
   {
      return notes;
   }



   public Part withNotes(String notes)
   {
      this.notes = notes;
      return this;
   }



   public void setNotes(String notes)
   {
      this.notes = notes;
   }



   public String getEdgeBandingNotes()
   {
      return edgeBandingNotes;
   }



   public Part withEdgeBandingNotes(String edgeBandingNotes)
   {
      this.edgeBandingNotes = edgeBandingNotes;
      return this;
   }



   public void setEdgeBandingNotes(String edgeBandingNotes)
   {
      this.edgeBandingNotes = edgeBandingNotes;
   }



   public Instant getCreateDate()
   {
      return createDate;
   }



   public Part withCreateDate(Instant createDate)
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



   public Part withModifyDate(Instant modifyDate)
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

         table.addSection(new QFieldSection("identity", "Identity", new QIcon(ICON_NAME), Tier.T1, List.of("id", "cabinetId", "cabinetOpeningId", "partTypeId", "name")));
         table.addSection(new QFieldSection("dimensions", "Dimensions", new QIcon("straighten"), Tier.T2, List.of("lengthMm", "widthMm", "thicknessMm")));
         table.addSection(new QFieldSection("material", "Material", new QIcon("inventory_2"), Tier.T2, List.of("materialId", "quantity")));
         table.addSection(new QFieldSection("notes", "Notes", new QIcon("notes"), Tier.T2, List.of("notes", "edgeBandingNotes")));
         table.addSection(new QFieldSection("dates", "Dates", new QIcon("event"), Tier.T3, List.of("createDate", "modifyDate")));

         return table;
      }
   }
}
