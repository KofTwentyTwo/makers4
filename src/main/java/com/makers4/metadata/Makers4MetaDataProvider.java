package com.makers4.metadata;


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
 * Metadata provider for Makers4.
 */
public class Makers4MetaDataProvider extends AbstractQQQApplication
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

      MetaDataProducerHelper.processAllMetaDataProducersInPackage(qInstance, "com.makers4.model");
      MetaDataProducerHelper.processAllMetaDataProducersInPackage(qInstance, "com.makers4.processes");
      MetaDataProducerHelper.processAllMetaDataProducersInPackage(qInstance, "com.makers4.metadata");

      // Ensure backend names inferred for tables
      qInstance.getTables().values().forEach(Makers4MetaDataProvider::setTableBackendNamesForRdbms);

      defineBranding(qInstance);
      defineAuthentication(qInstance);

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
            .withDatabaseName("makers4_test")
            .withUsername("sa");
      }

      QMetaDataVariableInterpreter interpreter = new QMetaDataVariableInterpreter();
      String  vendor       = getEnvOrDefault(interpreter, "RDBMS_VENDOR", "postgresql");
      String  hostname     = getEnvOrDefault(interpreter, "RDBMS_HOSTNAME", "localhost");
      Integer port         = Integer.valueOf(getEnvOrDefault(interpreter, "RDBMS_PORT", "5432"));
      String  databaseName = getEnvOrDefault(interpreter, "RDBMS_DATABASE_NAME", "makers4");
      String  username     = getEnvOrDefault(interpreter, "RDBMS_USERNAME", "devuser");
      String  password     = getEnvOrDefault(interpreter, "RDBMS_PASSWORD", "devpass");

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



   private static String getEnvOrDefault(QMetaDataVariableInterpreter interpreter, String envVar, String defaultValue)
   {
      String value = interpreter.interpret("${env." + envVar + "}");
      if(value == null || value.isEmpty() || value.equals("${env." + envVar + "}"))
      {
         return defaultValue;
      }
      return value;
   }



   private static void defineBranding(QInstance qInstance)
   {
      qInstance.setBranding(new QBrandingMetaData()
         .withCompanyName("Makers4")
         .withCompanyUrl("https://makers4.com"));
   }



   private static void defineAuthentication(QInstance qInstance)
   {
      ///////////////////////////////////////////////////////////////////////
      // Use FULLY_ANONYMOUS for local development - no login required     //
      // For production, this would be replaced with OAuth2 or other auth  //
      ///////////////////////////////////////////////////////////////////////
      QAuthenticationMetaData anonAuth = new QAuthenticationMetaData()
         .withName("anonymous")
         .withType(QAuthenticationType.FULLY_ANONYMOUS);
      qInstance.setAuthentication(anonAuth);
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
         InputStream  sqlStream = Makers4MetaDataProvider.class.getResourceAsStream("/" + sqlFileName);
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

