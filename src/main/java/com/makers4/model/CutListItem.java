package com.makers4.model;


import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.data.QField;
import com.kingsrook.qqq.backend.core.model.data.QRecordEntity;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.fields.DynamicDefaultValueBehavior;
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
@Table(name = CutListItem.TABLE_NAME)
@QMetaDataProducingEntity(produceTableMetaData = true, tableMetaDataCustomizer = CutListItem.TableMetaDataCustomizer.class, producePossibleValueSource = true)
public class CutListItem extends QRecordEntity
{
   public static final String TABLE_NAME  = "cut_list_item";
   public static final String TABLE_LABEL = "Cut List Items";
   public static final String ICON_NAME   = "carpenter";

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   @QField(isEditable = false, isPrimaryKey = true)
   private Long id;

   @ManyToOne
   @JoinColumn(name = "cut_list_id", insertable = false, updatable = false)
   private CutList cutList;

   @Column(name = "cut_list_id", nullable = false)
   @QField(isRequired = true, backendName = "cut_list_id", possibleValueSourceName = CutList.TABLE_NAME, label = "Cut List")
   private Long cutListId;

   @ManyToOne
   @JoinColumn(name = "part_id", insertable = false, updatable = false)
   private Part part;

   @Column(name = "part_id", nullable = false)
   @QField(isRequired = true, backendName = "part_id", possibleValueSourceName = Part.TABLE_NAME, label = "Part")
   private Long partId;

   ////////////////////////////////////////
   // Future nesting optimization fields //
   ////////////////////////////////////////
   @Column(name = "sheet_number")
   @QField(backendName = "sheet_number", label = "Sheet Number")
   private Integer sheetNumber;

   @Column(name = "position_x")
   @QField(backendName = "position_x", label = "Position X")
   private Integer positionX;

   @Column(name = "position_y")
   @QField(backendName = "position_y", label = "Position Y")
   private Integer positionY;

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



   public CutListItem withId(Long id)
   {
      this.id = id;
      return this;
   }



   public void setId(Long id)
   {
      this.id = id;
   }



   public CutList getCutList()
   {
      return cutList;
   }



   public CutListItem withCutList(CutList cutList)
   {
      this.cutList = cutList;
      return this;
   }



   public void setCutList(CutList cutList)
   {
      this.cutList = cutList;
   }



   public Long getCutListId()
   {
      return cutListId;
   }



   public CutListItem withCutListId(Long cutListId)
   {
      this.cutListId = cutListId;
      return this;
   }



   public void setCutListId(Long cutListId)
   {
      this.cutListId = cutListId;
   }



   public Part getPart()
   {
      return part;
   }



   public CutListItem withPart(Part part)
   {
      this.part = part;
      return this;
   }



   public void setPart(Part part)
   {
      this.part = part;
   }



   public Long getPartId()
   {
      return partId;
   }



   public CutListItem withPartId(Long partId)
   {
      this.partId = partId;
      return this;
   }



   public void setPartId(Long partId)
   {
      this.partId = partId;
   }



   public Integer getSheetNumber()
   {
      return sheetNumber;
   }



   public CutListItem withSheetNumber(Integer sheetNumber)
   {
      this.sheetNumber = sheetNumber;
      return this;
   }



   public void setSheetNumber(Integer sheetNumber)
   {
      this.sheetNumber = sheetNumber;
   }



   public Integer getPositionX()
   {
      return positionX;
   }



   public CutListItem withPositionX(Integer positionX)
   {
      this.positionX = positionX;
      return this;
   }



   public void setPositionX(Integer positionX)
   {
      this.positionX = positionX;
   }



   public Integer getPositionY()
   {
      return positionY;
   }



   public CutListItem withPositionY(Integer positionY)
   {
      this.positionY = positionY;
      return this;
   }



   public void setPositionY(Integer positionY)
   {
      this.positionY = positionY;
   }



   public Instant getCreateDate()
   {
      return createDate;
   }



   public CutListItem withCreateDate(Instant createDate)
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



   public CutListItem withModifyDate(Instant modifyDate)
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
            .withRecordLabelFormat("Item %s")
            .withRecordLabelFields(List.of("id"))
            .withBackendName(Makers4MetaDataProvider.RDBMS_BACKEND_NAME);

         table.addSection(new QFieldSection("identity", "Identity", new QIcon(ICON_NAME), Tier.T1, List.of("id", "cutListId", "partId")));
         table.addSection(new QFieldSection("nesting", "Nesting", new QIcon("grid_on"), Tier.T2, List.of("sheetNumber", "positionX", "positionY")));
         table.addSection(new QFieldSection("dates", "Dates", new QIcon("event"), Tier.T3, List.of("createDate", "modifyDate")));

         return table;
      }
   }
}
