package com.example.orders.model;


import com.example.orders.metadata.OrderAppMetaDataProvider;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.data.QField;
import com.kingsrook.qqq.backend.core.model.data.QRecordEntity;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;


@Entity
@Table(name = OrderLine.TABLE_NAME)
@QMetaDataProducingEntity(produceTableMetaData = true, tableMetaDataCustomizer = OrderLine.TableMetaDataCustomizer.class, producePossibleValueSource = true)
public class OrderLine extends QRecordEntity
{
   public static final String TABLE_NAME  = "order_line";
   public static final String TABLE_LABEL = "Order Lines";
   public static final String ICON_NAME   = "list_alt";

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   @QField(isEditable = false, isPrimaryKey = true)
   private Long id;

   @ManyToOne(optional = false)
   @JoinColumn(name = "order_id", nullable = false)
   private Order order;

   @QField(isRequired = true, backendName = "order_id", possibleValueSourceName = Order.TABLE_NAME)
   @Column(name = "order_id", nullable = false)
   private Long orderId;

   @ManyToOne(optional = false)
   @JoinColumn(name = "product_id", nullable = false)
   private Product product;

   @QField(isRequired = true, backendName = "product_id", possibleValueSourceName = Product.TABLE_NAME)
   @Column(name = "product_id", nullable = false)
   private Long productId;

   @Column(name = "quantity", nullable = false)
   @QField(isRequired = true, backendName = "quantity")
   private Integer quantity;

   @Column(name = "unit_price", nullable = false, precision = 14, scale = 2)
   @QField(isRequired = true, backendName = "unit_price")
   private BigDecimal unitPrice;

   @Column(name = "line_total", nullable = false, precision = 14, scale = 2)
   @QField(isRequired = true, backendName = "line_total")
   private BigDecimal lineTotal;

   @Column(name = "notes", length = 255)
   @QField(maxLength = 255, backendName = "notes", valueTooLongBehavior = ValueTooLongBehavior.TRUNCATE)
   private String notes;



   public Long getId()
   {
      return id;
   }



   public OrderLine withId(Long id)
   {
      this.id = id;
      return this;
   }



   public void setId(Long id)
   {
      this.id = id;
   }



   public Long getOrderId()
   {
      return orderId;
   }



   public OrderLine withOrderId(Long orderId)
   {
      this.orderId = orderId;
      return this;
   }



   public void setOrderId(Long orderId)
   {
      this.orderId = orderId;
   }



   public Long getProductId()
   {
      return productId;
   }



   public OrderLine withProductId(Long productId)
   {
      this.productId = productId;
      return this;
   }



   public void setProductId(Long productId)
   {
      this.productId = productId;
   }



   public Integer getQuantity()
   {
      return quantity;
   }



   public OrderLine withQuantity(Integer quantity)
   {
      this.quantity = quantity;
      return this;
   }



   public void setQuantity(Integer quantity)
   {
      this.quantity = quantity;
   }



   public BigDecimal getUnitPrice()
   {
      return unitPrice;
   }



   public OrderLine withUnitPrice(BigDecimal unitPrice)
   {
      this.unitPrice = unitPrice;
      return this;
   }



   public void setUnitPrice(BigDecimal unitPrice)
   {
      this.unitPrice = unitPrice;
   }



   public BigDecimal getLineTotal()
   {
      return lineTotal;
   }



   public OrderLine withLineTotal(BigDecimal lineTotal)
   {
      this.lineTotal = lineTotal;
      return this;
   }



   public void setLineTotal(BigDecimal lineTotal)
   {
      this.lineTotal = lineTotal;
   }



   public String getNotes()
   {
      return notes;
   }



   public OrderLine withNotes(String notes)
   {
      this.notes = notes;
      return this;
   }



   public void setNotes(String notes)
   {
      this.notes = notes;
   }



   public static class TableMetaDataCustomizer implements MetaDataCustomizerInterface<QTableMetaData>
   {
      @Override
      public QTableMetaData customizeMetaData(QInstance qInstance, QTableMetaData table) throws QException
      {
         table.withUniqueKey(new UniqueKey("id")).withIcon(new QIcon().withName(ICON_NAME)).withLabel(TABLE_LABEL).withBackendName(OrderAppMetaDataProvider.RDBMS_BACKEND_NAME);
         table.addSection(new QFieldSection("line", "Line", new QIcon(ICON_NAME), Tier.T1, java.util.List.of("id", "orderId", "productId", "quantity", "unitPrice", "lineTotal", "notes")));
         return table;
      }
   }
}

