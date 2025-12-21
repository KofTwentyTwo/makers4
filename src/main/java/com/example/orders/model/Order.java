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
import com.kingsrook.qqq.backend.core.model.metadata.producers.annotations.ChildJoin;
import com.kingsrook.qqq.backend.core.model.metadata.producers.annotations.ChildRecordListWidget;
import com.kingsrook.qqq.backend.core.model.metadata.producers.annotations.ChildTable;
import com.kingsrook.qqq.backend.core.model.metadata.producers.annotations.QMetaDataProducingEntity;
import com.kingsrook.qqq.backend.core.model.metadata.tables.ExposedJoin;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;


@Entity
@Table(name = Order.TABLE_NAME)
@QMetaDataProducingEntity(produceTableMetaData = true, tableMetaDataCustomizer = Order.TableMetaDataCustomizer.class, producePossibleValueSource = true, childTables = {
   @ChildTable(joinFieldName = "orderId", childTableEntityClass = OrderLine.class, childJoin = @ChildJoin(enabled = true), childRecordListWidget = @ChildRecordListWidget(enabled = true, label = "Order Lines", maxRows = 20)) })
public class Order extends QRecordEntity
{
   public static final String TABLE_NAME  = "orders";
   public static final String TABLE_LABEL = "Orders";
   public static final String ICON_NAME   = "shopping_cart";

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   @QField(isEditable = false, isPrimaryKey = true)
   private Long id;

   @ManyToOne(optional = false)
   @JoinColumn(name = "customer_id", nullable = false)
   private Customer customer;

   @QField(isRequired = true, possibleValueSourceName = Customer.TABLE_NAME, backendName = "customer_id")
   @Column(name = "customer_id", nullable = false)
   private Long customerId;

   @Column(name = "status", nullable = false, length = 40)
   @QField(isRequired = true, backendName = "status", defaultValue = "NEW", maxLength = 40, valueTooLongBehavior = ValueTooLongBehavior.ERROR)
   private String status;

   @Column(name = "order_total", nullable = false, precision = 14, scale = 2)
   @QField(isRequired = true, backendName = "order_total")
   private BigDecimal orderTotal;

   @Column(name = "createdate", nullable = false, updatable = false)
   @QField(dynamicDefaultValueBehavior = DynamicDefaultValueBehavior.CREATE_DATE, backendName = "createdate")
   private Instant createDate;

   @Column(name = "modifydate", nullable = false)
   @QField(dynamicDefaultValueBehavior = DynamicDefaultValueBehavior.MODIFY_DATE, backendName = "modifydate")
   private Instant modifyDate;

   @OneToMany(mappedBy = "order", cascade = jakarta.persistence.CascadeType.ALL)
   private List<OrderLine> lines;



   public Long getId()
   {
      return id;
   }



   public Order withId(Long id)
   {
      this.id = id;
      return this;
   }



   public void setId(Long id)
   {
      this.id = id;
   }



   public Long getCustomerId()
   {
      return customerId;
   }



   public Order withCustomerId(Long customerId)
   {
      this.customerId = customerId;
      return this;
   }



   public void setCustomerId(Long customerId)
   {
      this.customerId = customerId;
   }



   public String getStatus()
   {
      return status;
   }



   public Order withStatus(String status)
   {
      this.status = status;
      return this;
   }



   public void setStatus(String status)
   {
      this.status = status;
   }



   public BigDecimal getOrderTotal()
   {
      return orderTotal;
   }



   public Order withOrderTotal(BigDecimal orderTotal)
   {
      this.orderTotal = orderTotal;
      return this;
   }



   public void setOrderTotal(BigDecimal orderTotal)
   {
      this.orderTotal = orderTotal;
   }



   public Instant getCreateDate()
   {
      return createDate;
   }



   public Order withCreateDate(Instant createDate)
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



   public Order withModifyDate(Instant modifyDate)
   {
      this.modifyDate = modifyDate;
      return this;
   }



   public void setModifyDate(Instant modifyDate)
   {
      this.modifyDate = modifyDate;
   }



   public List<OrderLine> getLines()
   {
      return lines;
   }



   public Order withLines(List<OrderLine> lines)
   {
      this.lines = lines;
      return this;
   }



   public void setLines(List<OrderLine> lines)
   {
      this.lines = lines;
   }



   public static class TableMetaDataCustomizer implements MetaDataCustomizerInterface<QTableMetaData>
   {
      @Override
      public QTableMetaData customizeMetaData(QInstance qInstance, QTableMetaData table) throws QException
      {
         table.withUniqueKey(new UniqueKey("id")).withIcon(new QIcon().withName(ICON_NAME)).withLabel(TABLE_LABEL).withBackendName(OrderAppMetaDataProvider.RDBMS_BACKEND_NAME);
         table.addSection(new QFieldSection("order", "Order", new QIcon(ICON_NAME), Tier.T1, java.util.List.of("id", "customerId", "status", "orderTotal")));
         table.addSection(new QFieldSection("dates", "Dates", new QIcon("event"), Tier.T2, java.util.List.of("createDate", "modifyDate")));
         String joinName = com.kingsrook.qqq.backend.core.model.metadata.joins.QJoinMetaData.makeInferredJoinName(TABLE_NAME, OrderLine.TABLE_NAME);
         table.addSection(new QFieldSection("lines", new QIcon(OrderLine.ICON_NAME), Tier.T2).withWidgetName(joinName));
         table.withExposedJoin(new ExposedJoin().withLabel("Order Lines").withJoinPath(java.util.List.of(joinName)).withJoinTable(OrderLine.TABLE_NAME));
         return table;
      }
   }
}

