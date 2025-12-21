package com.example.orders.metadata;


import com.example.orders.processes.CreateOrderProcess;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.instances.AbstractQQQApplication;
import com.kingsrook.qqq.backend.core.instances.QInstanceEnricher;
import com.kingsrook.qqq.backend.core.instances.QMetaDataVariableInterpreter;
import com.kingsrook.qqq.backend.core.model.metadata.MetaDataProducerHelper;
import com.kingsrook.qqq.backend.core.model.metadata.QBackendMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.QAuthenticationType;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.authentication.QAuthenticationMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.branding.QBrandingMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.tables.QTableMetaData;
import com.kingsrook.qqq.backend.core.modules.backend.implementations.memory.MemoryBackendModule;
import com.kingsrook.qqq.backend.module.filesystem.local.model.metadata.FilesystemBackendMetaData;
import com.kingsrook.qqq.backend.module.rdbms.jdbc.ConnectionManager;
import com.kingsrook.qqq.backend.module.rdbms.jdbc.QueryManager;
import com.kingsrook.qqq.backend.module.rdbms.model.metadata.RDBMSBackendMetaData;
import com.kingsrook.qqq.backend.module.rdbms.model.metadata.RDBMSTableBackendDetails;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.List;
import org.apache.commons.io.IOUtils;


/**
 * Minimal metadata provider for the template order app.
 */
public class OrderAppMetaDataProvider extends AbstractQQQApplication
{
   public static final String RDBMS_BACKEND_NAME      = "rdbms";
   public static final String MEMORY_BACKEND_NAME     = "memory";
   public static final String FILESYSTEM_BACKEND_NAME = "filesystem";



   @Override
   public QInstance defineQInstance() throws QException
   {
      return defineInstance(false);
   }



   public static QInstance defineInstance(boolean useH2ForTests) throws QException
   {
      QInstance qInstance = new QInstance();

      qInstance.addBackend(defineRdbmsBackend(useH2ForTests));
      qInstance.addBackend(defineMemoryBackend());
      qInstance.addBackend(defineFilesystemBackend());

      MetaDataProducerHelper.processAllMetaDataProducersInPackage(qInstance, "com.example.orders.model");
      MetaDataProducerHelper.processAllMetaDataProducersInPackage(qInstance, "com.example.orders.processes");
      MetaDataProducerHelper.processAllMetaDataProducersInPackage(qInstance, "com.example.orders.metadata");

      // Ensure backend names inferred for tables
      qInstance.getTables().values().forEach(OrderAppMetaDataProvider::setTableBackendNamesForRdbms);

      defineBranding(qInstance);
      defineAuthentication(qInstance);

      // Add sample process metadata explicitly
      qInstance.addProcess(CreateOrderProcess.getProcessMetaData());

      return qInstance;
   }



   public static QInstance defineTestInstance() throws QException
   {
      return defineInstance(true);
   }



   private static QBackendMetaData defineMemoryBackend()
   {
      return new com.kingsrook.qqq.backend.core.model.metadata.QBackendMetaData()
         .withName(MEMORY_BACKEND_NAME)
         .withBackendType(MemoryBackendModule.class);
   }



   private static FilesystemBackendMetaData defineFilesystemBackend()
   {
      return new FilesystemBackendMetaData().withBasePath("/tmp/qqq-files").withName(FILESYSTEM_BACKEND_NAME);
   }



   private static RDBMSBackendMetaData defineRdbmsBackend(boolean useH2ForTests)
   {
      if(useH2ForTests)
      {
         return new RDBMSBackendMetaData()
            .withName(RDBMS_BACKEND_NAME)
            .withVendor("h2")
            .withHostName("mem")
            .withDatabaseName("qqq_orders_test")
            .withUsername("sa");
      }

      QMetaDataVariableInterpreter interpreter = new QMetaDataVariableInterpreter();
      String vendor       = interpreter.interpret("${env.RDBMS_VENDOR}");
      String hostname     = interpreter.interpret("${env.RDBMS_HOSTNAME}");
      Integer port        = Integer.valueOf(interpreter.interpret("${env.RDBMS_PORT}"));
      String databaseName = interpreter.interpret("${env.RDBMS_DATABASE_NAME}");
      String username     = interpreter.interpret("${env.RDBMS_USERNAME}");
      String password     = interpreter.interpret("${env.RDBMS_PASSWORD}");

      if("postgresql".equalsIgnoreCase(vendor) || "postgres".equalsIgnoreCase(vendor))
      {
         return new com.kingsrook.qqq.backend.module.postgres.model.metadata.PostgreSQLBackendMetaData()
            .withName(RDBMS_BACKEND_NAME)
            .withHostName(hostname)
            .withPort(port)
            .withDatabaseName(databaseName)
            .withUsername(username)
            .withPassword(password);
      }

      return new RDBMSBackendMetaData()
         .withName(RDBMS_BACKEND_NAME)
         .withVendor(vendor)
         .withHostName(hostname)
         .withPort(port)
         .withDatabaseName(databaseName)
         .withUsername(username)
         .withPassword(password);
   }



   private static void defineBranding(QInstance qInstance)
   {
      qInstance.setBranding(new QBrandingMetaData().withLogo("/qqq-logo.png").withIcon("/kr-icon.png"));
   }



   private static void defineAuthentication(QInstance qInstance)
   {
      QAuthenticationMetaData mockAuth = new QAuthenticationMetaData()
         .withName("mock")
         .withType(QAuthenticationType.MOCK);
      qInstance.setAuthentication(mockAuth);
   }



   private static QTableMetaData setTableBackendNamesForRdbms(QTableMetaData table)
   {
      table.setBackendDetails(new RDBMSTableBackendDetails().withTableName(QInstanceEnricher.inferBackendName(table.getName())));
      QInstanceEnricher.setInferredFieldBackendNames(table);
      return table;
   }



   /**
    * Utility to prime the test database from a SQL resource.
    */
   public static void primeTestDatabase(String sqlFileName) throws Exception
   {
      try(Connection connection = ConnectionManager.getConnection(defineRdbmsBackend(true)))
      {
         InputStream  sqlStream = OrderAppMetaDataProvider.class.getResourceAsStream("/" + sqlFileName);
         List<String> lines     = IOUtils.readLines(sqlStream, StandardCharsets.UTF_8);
         lines = lines.stream().filter(line -> !line.startsWith("-- ")).toList();
         String joinedSQL = String.join("\n", lines);
         for(String sql : joinedSQL.split(";"))
         {
            QueryManager.executeUpdate(connection, sql);
         }
      }
   }
}

