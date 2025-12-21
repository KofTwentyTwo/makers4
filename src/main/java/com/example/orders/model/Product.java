package com.example.orders.model;


import com.example.orders.metadata.OrderAppMetaDataProvider;
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
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;


@Entity
@Table(name = Product.TABLE_NAME)
@QMetaDataProducingEntity(produceTableMetaData = true, tableMetaDataCustomizer = Product.TableMetaDataCustomizer.class, producePossibleValueSource = true)
public class Product extends QRecordEntity
{
   public static final String TABLE_NAME  = "product";
   public static final String TABLE_LABEL = "Products";
   public static final String ICON_NAME   = "inventory_2";

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   @QField(isEditable = false, isPrimaryKey = true)
   private Long id;

   @Column(name = "name", nullable = false, length = 120)
   @QField(isRequired = true, maxLength = 120, backendName = "name", valueTooLongBehavior = ValueTooLongBehavior.ERROR)
   private String name;

   @Column(name = "sku", nullable = false, length = 60)
   @QField(isRequired = true, maxLength = 60, backendName = "sku")
   private String sku;

   @Column(name = "price", nullable = false, precision = 12, scale = 2)
   @QField(isRequired = true, backendName = "price")
   private BigDecimal price;

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



   public Product withId(Long id)
   {
      this.id = id;
      return this;
   }



   public void setId(Long id)
   {
      this.id = id;
   }



   public String getName()
   {
      return name;
   }



   public Product withName(String name)
   {
      this.name = name;
      return this;
   }



   public void setName(String name)
   {
      this.name = name;
   }



   public String getSku()
   {
      return sku;
   }



   public Product withSku(String sku)
   {
      this.sku = sku;
      return this;
   }



   public void setSku(String sku)
   {
      this.sku = sku;
   }



   public BigDecimal getPrice()
   {
      return price;
   }



   public Product withPrice(BigDecimal price)
   {
      this.price = price;
      return this;
   }



   public void setPrice(BigDecimal price)
   {
      this.price = price;
   }



   public Instant getCreateDate()
   {
      return createDate;
   }



   public Product withCreateDate(Instant createDate)
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



   public Product withModifyDate(Instant modifyDate)
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
         table.withUniqueKey(new UniqueKey("id")).withIcon(new QIcon().withName(ICON_NAME)).withLabel(TABLE_LABEL).withBackendName(OrderAppMetaDataProvider.RDBMS_BACKEND_NAME);
         table.addSection(new QFieldSection("product", "Product", new QIcon(ICON_NAME), Tier.T1, java.util.List.of("id", "name", "sku", "price")));
         table.addSection(new QFieldSection("dates", "Dates", new QIcon("event"), Tier.T2, java.util.List.of("createDate", "modifyDate")));
         return table;
      }
   }
}

