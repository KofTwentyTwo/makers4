package com.example.orders;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.orders.processes.CreateOrderProcess;
import com.kingsrook.qqq.backend.core.model.actions.processes.RunBackendStepInput;
import com.kingsrook.qqq.backend.core.model.actions.processes.RunBackendStepOutput;
import com.kingsrook.qqq.backend.core.model.data.QRecord;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;


class CreateOrderProcessTest
{
   @Test
   void shouldTotalOrderAndLines() throws Exception
   {
      QRecord order = new QRecord();
      QRecord line1 = new QRecord();
      line1.setValue("quantity", 2);
      line1.setValue("unitPrice", new BigDecimal("10.00"));

      QRecord line2 = new QRecord();
      line2.setValue("quantity", 1);
      line2.setValue("unitPrice", new BigDecimal("5.50"));

      order.setValue("order_line", List.of(line1, line2));

      RunBackendStepOutput output = new RunBackendStepOutput();
      new CreateOrderProcess.ValidateAndTotalStep().runOnePage(
         new RunBackendStepInput().withRecords(List.of(order)),
         output);

      QRecord processed = output.getRecords().get(0);
      assertThat(processed.getValueBigDecimal("orderTotal")).isEqualByComparingTo("25.50");
      assertThat(line1.getValueBigDecimal("lineTotal")).isEqualByComparingTo("20.00");
      assertThat(line2.getValueBigDecimal("lineTotal")).isEqualByComparingTo("5.50");
   }



   @Test
   void shouldRejectMissingLines()
   {
      QRecord order = new QRecord();
      RunBackendStepOutput output = new RunBackendStepOutput();

      assertThatThrownBy(() -> new CreateOrderProcess.ValidateAndTotalStep().runOnePage(
         new RunBackendStepInput().withRecords(List.of(order)),
         output)).hasMessageContaining("at least one line");
   }
}

