package com.example.orders;


import static org.assertj.core.api.Assertions.assertThat;

import com.example.orders.metadata.OrderAppMetaDataProvider;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import org.junit.jupiter.api.Test;


class MetadataSmokeTest
{
   @Test
   void shouldLoadTablesAndProcesses() throws Exception
   {
      QInstance instance = OrderAppMetaDataProvider.defineTestInstance();

      assertThat(instance.getTables().keySet())
         .contains("customer", "product", "orders", "order_line");

      assertThat(instance.getProcesses().keySet())
         .contains("createOrder");
   }
}

