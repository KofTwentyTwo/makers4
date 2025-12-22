package com.makers4.model.lookup;


import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.data.QField;
import com.kingsrook.qqq.backend.core.model.data.QRecordEntity;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.fields.DynamicDefaultValueBehavior;
import com.kingsrook.qqq.backend.core.model.metadata.fields.ValueTooLongBehavior;
import com.kingsrook.qqq.backend.core.model.metadata.joins.QJoinMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.layout.QIcon;
import com.kingsrook.qqq.backend.core.model.metadata.producers.MetaDataCustomizerInterface;
import com.kingsrook.qqq.backend.core.model.metadata.producers.annotations.ChildJoin;
import com.kingsrook.qqq.backend.core.model.metadata.producers.annotations.ChildRecordListWidget;
import com.kingsrook.qqq.backend.core.model.metadata.producers.annotations.ChildTable;
import com.kingsrook.qqq.backend.core.model.metadata.producers.annotations.QMetaDataProducingEntity;
import com.kingsrook.qqq.backend.core.model.metadata.tables.ExposedJoin;
import com.kingsrook.qqq.backend.core.model.metadata.tables.QFieldSection;
import com.kingsrook.qqq.backend.core.model.metadata.tables.QTableMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.tables.Tier;
import com.kingsrook.qqq.backend.core.model.metadata.tables.UniqueKey;
import com.makers4.model.Cabinet;
import com.makers4.metadata.Makers4MetaDataProvider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.List;


@Entity
@Table(name = ToeKickStyle.TABLE_NAME)
@QMetaDataProducingEntity(
   produceTableMetaData = true,
   tableMetaDataCustomizer = ToeKickStyle.TableMetaDataCustomizer.class,
   producePossibleValueSource = true,
   childTables = {
      @ChildTable(
         joinFieldName = "toeKickStyleId",
         childTableEntityClass = Cabinet.class,
         childJoin = @ChildJoin(enabled = true),
         childRecordListWidget = @ChildRecordListWidget(enabled = true, label = "Cabinets", maxRows = 50)
      )
   }
)
public class ToeKickStyle extends QRecordEntity
{
   public static final String TABLE_NAME  = "toe_kick_style";
   public static final String TABLE_LABEL = "Toe Kick Styles";
   public static final String ICON_NAME   = "height";

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   @QField(isEditable = false, isPrimaryKey = true)
   private Long id;

   @Column(name = "code", nullable = false, length = 40)
   @QField(isRequired = true, maxLength = 40, backendName = "code", valueTooLongBehavior = ValueTooLongBehavior.ERROR)
   private String code;

   @Column(name = "name", nullable = false, length = 100)
   @QField(isRequired = true, maxLength = 100, backendName = "name", valueTooLongBehavior = ValueTooLongBehavior.ERROR)
   private String name;

   @Column(name = "description", columnDefinition = "TEXT")
   @QField(backendName = "description")
   private String description;

   @Column(name = "sort_order", nullable = false)
   @QField(isRequired = true, backendName = "sort_order", defaultValue = "0")
   private Integer sortOrder;

   @Column(name = "is_active", nullable = false)
   @QField(isRequired = true, backendName = "is_active", defaultValue = "true")
   private Boolean isActive;

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



   public ToeKickStyle withId(Long id)
   {
      this.id = id;
      return this;
   }



   public void setId(Long id)
   {
      this.id = id;
   }



   public String getCode()
   {
      return code;
   }



   public ToeKickStyle withCode(String code)
   {
      this.code = code;
      return this;
   }



   public void setCode(String code)
   {
      this.code = code;
   }



   public String getName()
   {
      return name;
   }



   public ToeKickStyle withName(String name)
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



   public ToeKickStyle withDescription(String description)
   {
      this.description = description;
      return this;
   }



   public void setDescription(String description)
   {
      this.description = description;
   }



   public Integer getSortOrder()
   {
      return sortOrder;
   }



   public ToeKickStyle withSortOrder(Integer sortOrder)
   {
      this.sortOrder = sortOrder;
      return this;
   }



   public void setSortOrder(Integer sortOrder)
   {
      this.sortOrder = sortOrder;
   }



   public Boolean getIsActive()
   {
      return isActive;
   }



   public ToeKickStyle withIsActive(Boolean isActive)
   {
      this.isActive = isActive;
      return this;
   }



   public void setIsActive(Boolean isActive)
   {
      this.isActive = isActive;
   }



   public Instant getCreateDate()
   {
      return createDate;
   }



   public ToeKickStyle withCreateDate(Instant createDate)
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



   public ToeKickStyle withModifyDate(Instant modifyDate)
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
            .withUniqueKey(new UniqueKey("code"))
            .withIcon(new QIcon().withName(ICON_NAME))
            .withLabel(TABLE_LABEL)
            .withRecordLabelFormat("%s")
            .withRecordLabelFields(List.of("name"))
            .withBackendName(Makers4MetaDataProvider.RDBMS_BACKEND_NAME);

         table.addSection(new QFieldSection("identity", "Identity", new QIcon(ICON_NAME), Tier.T1, List.of("id", "code", "name", "description")));
         table.addSection(new QFieldSection("settings", "Settings", new QIcon("settings"), Tier.T2, List.of("sortOrder", "isActive")));

         ////////////////////////////////
         // Child Cabinets Section //
         ////////////////////////////////
         String cabinetsJoinName = QJoinMetaData.makeInferredJoinName(ToeKickStyle.TABLE_NAME, Cabinet.TABLE_NAME);
         table.addSection(new QFieldSection("cabinets", new QIcon().withName(Cabinet.ICON_NAME), Tier.T2).withLabel("Cabinets").withWidgetName(cabinetsJoinName));
         table.withExposedJoin(new ExposedJoin()
            .withLabel("Cabinets")
            .withJoinPath(List.of(cabinetsJoinName))
            .withJoinTable(Cabinet.TABLE_NAME));

         table.addSection(new QFieldSection("dates", "Dates", new QIcon("event"), Tier.T3, List.of("createDate", "modifyDate")));

         return table;
      }
   }
}
