package com.makers4;


import java.util.List;
import com.makers4.metadata.Makers4MetaDataProvider;
import com.makers4.startup.LiquibaseRunner;
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
         ///////////////////////////////////////////////////////////////////////////
         // Run migrations up front so the sample starts with schema + seed data. //
         ///////////////////////////////////////////////////////////////////////////
         LiquibaseRunner.runMigrations();

         QApplicationJavalinServer jServer = new QApplicationJavalinServer(new Makers4MetaDataProvider())
            .withPort(DEFAULT_PORT)
            ///////////////////////////////////////////////////////////////////////
            // Material Dashboard is configured via DashboardRouteMetaDataProvider //
            // which sets up IsolatedSpaRouteProvider with deep linking support    //
            ///////////////////////////////////////////////////////////////////////
            .withServeFrontendMaterialDashboard(false)
            ///////////////////////////////////////////////////////
            // expose middleware APIs that the dashboard expects //
            ///////////////////////////////////////////////////////
            .withServeLegacyUnversionedMiddlewareAPI(true)
            .withMiddlewareVersionList(List.of(new MiddlewareVersionV1()));

         jServer.start();

         System.out.println("Makers4 running at http://localhost:" + DEFAULT_PORT + "/");
         System.out.println("API: http://localhost:" + DEFAULT_PORT + "/qqq/v1/");
      }
      catch(Exception e)
      {
         throw new RuntimeException("Failed to start sample app", e);
      }
   }
}
