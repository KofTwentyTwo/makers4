package com.example.orders;


import java.util.List;
import com.example.orders.metadata.OrderAppMetaDataProvider;
import com.example.orders.startup.LiquibaseRunner;
import com.kingsrook.qqq.middleware.javalin.QApplicationJavalinServer;
import com.kingsrook.qqq.middleware.javalin.specs.v1.MiddlewareVersionV1;


/**
 * Minimal bootstrap for the sample order management app.
 */
public class Server
{
   private static final Integer DEFAULT_PORT = 8000;



   public static void main(String[] args)
   {
      new Server().start();
   }



   public void start()
   {
      try
      {
         // Run migrations up front so the sample starts with schema + seed data.
         LiquibaseRunner.runMigrations();

         QApplicationJavalinServer jServer = new QApplicationJavalinServer(new OrderAppMetaDataProvider())
            .withPort(DEFAULT_PORT)
            .withServeFrontendMaterialDashboard(true)
            .withFrontendMaterialDashboardHostedPath("/") // dashboard at root
            // expose middleware APIs that the dashboard expects
            .withServeLegacyUnversionedMiddlewareAPI(true)
            .withMiddlewareVersionList(List.of(new MiddlewareVersionV1()));

         jServer.start();

         System.out.println("Sample Orders app running at http://localhost:" + DEFAULT_PORT + "/");
         System.out.println("Material Dashboard: http://localhost:" + DEFAULT_PORT + "/");
         System.out.println("API: http://localhost:" + DEFAULT_PORT + "/qqq-api/");
      }
      catch(Exception e)
      {
         throw new RuntimeException("Failed to start sample app", e);
      }
   }
}

