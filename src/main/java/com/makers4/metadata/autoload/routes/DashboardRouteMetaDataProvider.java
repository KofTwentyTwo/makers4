package com.makers4.metadata.autoload.routes;


import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.metadata.MetaDataProducer;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.javalin.QJavalinMetaData;
import com.kingsrook.qqq.middleware.javalin.metadata.JavalinRouteProviderMetaData;


/*******************************************************************************
 ** Provides route metadata for the Material Dashboard SPA at root path.
 **
 ** This provider configures an IsolatedSpaRouteProvider that:
 ** - Serves the Material Dashboard SPA at /
 ** - Loads static files from JAR (material-dashboard/)
 ** - Enables deep linking for client-side routing
 *******************************************************************************/
public class DashboardRouteMetaDataProvider extends MetaDataProducer<QJavalinMetaData>
{
   public static final String NAME = "Dashboard";



   /*******************************************************************************
    **
    *******************************************************************************/
   @Override
   public QJavalinMetaData produce(QInstance qInstance) throws QException
   {
      /////////////////////////////////////////////////////////////////////////////////
      // Get existing QJavalinMetaData or create new one to allow multiple producers //
      // to contribute route providers without overwriting each other                //
      /////////////////////////////////////////////////////////////////////////////////
      QJavalinMetaData javalinMetaData = QJavalinMetaData.ofOrWithNew(qInstance);

      javalinMetaData.withRouteProvider(new JavalinRouteProviderMetaData()
         .withName(NAME)
         .withSpaPath("/")
         .withStaticFilesPath("material-dashboard")
         .withSpaIndexFile("material-dashboard/index.html")
         .withEnableDeepLinking(true)
         .withLoadFromJar(true));

      return javalinMetaData;
   }

}
