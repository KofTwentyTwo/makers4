package com.example.orders.startup;


import java.util.Optional;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;


/**
 * Tiny helper to run Liquibase on startup using env vars.
 */
public final class LiquibaseRunner
{
   private LiquibaseRunner()
   {
   }



   public static void runMigrations()
   {
      String url      = requiredEnv("RDBMS_URL", "jdbc:postgresql://localhost:5432/qqq_orders");
      String user     = requiredEnv("RDBMS_USERNAME", "devuser");
      String password = requiredEnv("RDBMS_PASSWORD", "devpass");

      Database database = null;
      try
      {
         database = DatabaseFactory.getInstance().openDatabase(url, user, password, null, new ClassLoaderResourceAccessor());
         try (Liquibase liquibase = new Liquibase("db/liquibase/changelog.yaml", new ClassLoaderResourceAccessor(), database))
         {
            liquibase.update((String) null);
         }
      }
      catch(LiquibaseException e)
      {
         throw new RuntimeException("Failed to run Liquibase migrations", e);
      }
      finally
      {
         if(database != null)
         {
            try
            {
               database.close();
            }
            catch(Exception ignored)
            {
               // ignore close errors
            }
         }
      }
   }



   private static String requiredEnv(String name, String defaultValue)
   {
      return Optional.ofNullable(System.getenv(name)).orElse(defaultValue);
   }
}

