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
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.List;


@Entity
@Table(name = User.TABLE_NAME)
@QMetaDataProducingEntity(produceTableMetaData = true, tableMetaDataCustomizer = User.TableMetaDataCustomizer.class, producePossibleValueSource = true)
public class User extends QRecordEntity
{
   public static final String TABLE_NAME  = "user";
   public static final String TABLE_LABEL = "Users";
   public static final String ICON_NAME   = "person";

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   @QField(isEditable = false, isPrimaryKey = true)
   private Long id;

   @Column(name = "email", nullable = false, length = 255)
   @QField(isRequired = true, maxLength = 255, backendName = "email", valueTooLongBehavior = ValueTooLongBehavior.ERROR)
   private String email;

   @Column(name = "name", nullable = false, length = 120)
   @QField(isRequired = true, maxLength = 120, backendName = "name", valueTooLongBehavior = ValueTooLongBehavior.ERROR)
   private String name;

   @Column(name = "password_hash", nullable = false, length = 255)
   @QField(isRequired = true, maxLength = 255, backendName = "password_hash", label = "Password Hash", isHidden = true)
   private String passwordHash;

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



   public User withId(Long id)
   {
      this.id = id;
      return this;
   }



   public void setId(Long id)
   {
      this.id = id;
   }



   public String getEmail()
   {
      return email;
   }



   public User withEmail(String email)
   {
      this.email = email;
      return this;
   }



   public void setEmail(String email)
   {
      this.email = email;
   }



   public String getName()
   {
      return name;
   }



   public User withName(String name)
   {
      this.name = name;
      return this;
   }



   public void setName(String name)
   {
      this.name = name;
   }



   public String getPasswordHash()
   {
      return passwordHash;
   }



   public User withPasswordHash(String passwordHash)
   {
      this.passwordHash = passwordHash;
      return this;
   }



   public void setPasswordHash(String passwordHash)
   {
      this.passwordHash = passwordHash;
   }



   public Boolean getIsActive()
   {
      return isActive;
   }



   public User withIsActive(Boolean isActive)
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



   public User withCreateDate(Instant createDate)
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



   public User withModifyDate(Instant modifyDate)
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
            .withUniqueKey(new UniqueKey("email"))
            .withIcon(new QIcon().withName(ICON_NAME))
            .withLabel(TABLE_LABEL)
            .withRecordLabelFormat("%s")
            .withRecordLabelFields(List.of("name"))
            .withBackendName(Makers4MetaDataProvider.RDBMS_BACKEND_NAME);

         table.addSection(new QFieldSection("identity", "Identity", new QIcon(ICON_NAME), Tier.T1, List.of("id", "email", "name")));
         table.addSection(new QFieldSection("security", "Security", new QIcon("lock"), Tier.T2, List.of("isActive")));
         table.addSection(new QFieldSection("dates", "Dates", new QIcon("event"), Tier.T3, List.of("createDate", "modifyDate")));

         return table;
      }
   }
}
