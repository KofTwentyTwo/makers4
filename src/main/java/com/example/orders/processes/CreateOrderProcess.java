package com.example.orders.processes;


import com.example.orders.model.Order;
import com.example.orders.model.OrderLine;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.actions.processes.RunBackendStepInput;
import com.kingsrook.qqq.backend.core.model.actions.processes.RunBackendStepOutput;
import com.kingsrook.qqq.backend.core.model.data.QRecord;
import com.kingsrook.qqq.backend.core.model.metadata.processes.QProcessMetaData;
import com.kingsrook.qqq.backend.core.processes.implementations.etl.streamedwithfrontend.AbstractTransformStep;
import com.kingsrook.qqq.backend.core.processes.implementations.etl.streamedwithfrontend.ExtractViaQueryStep;
import com.kingsrook.qqq.backend.core.processes.implementations.etl.streamedwithfrontend.LoadViaUpdateStep;
import com.kingsrook.qqq.backend.core.processes.implementations.etl.streamedwithfrontend.StreamedETLWithFrontendProcess;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * Sample process that validates and totals an order before save.
 */
public final class CreateOrderProcess
{
   private CreateOrderProcess()
   {
   }



   public static QProcessMetaData getProcessMetaData()
   {
      Map<String, java.io.Serializable> values = new java.util.HashMap<>();
      values.put(StreamedETLWithFrontendProcess.FIELD_SOURCE_TABLE, Order.TABLE_NAME);
      values.put(StreamedETLWithFrontendProcess.FIELD_DESTINATION_TABLE, Order.TABLE_NAME);
      return StreamedETLWithFrontendProcess.defineProcessMetaData(
         ExtractStep.class,
         ValidateAndTotalStep.class,
         LoadStep.class,
         values).withName("createOrder").withTableName(Order.TABLE_NAME);
   }



   public static class ExtractStep extends ExtractViaQueryStep
   {
      // default query behavior
   }



   public static class LoadStep extends LoadViaUpdateStep
   {
      // default update behavior
   }



   public static class ValidateAndTotalStep extends AbstractTransformStep
   {
      @Override
      public java.util.ArrayList<com.kingsrook.qqq.backend.core.model.actions.processes.ProcessSummaryLineInterface> getProcessSummary(
         RunBackendStepOutput runBackendStepOutput,
         boolean isForResultScreen)
      {
         return new java.util.ArrayList<>();
      }



      @Override
      public void runOnePage(RunBackendStepInput runBackendStepInput, RunBackendStepOutput runBackendStepOutput) throws QException
      {
         for(QRecord record : runBackendStepInput.getRecords())
         {
            @SuppressWarnings("unchecked")
            List<QRecord> lines = (List<QRecord>) record.getValue(OrderLine.TABLE_NAME);
            if(lines == null || lines.isEmpty())
            {
               throw new QException("Order must have at least one line");
            }

            BigDecimal total = BigDecimal.ZERO;
            for(QRecord line : lines)
            {
               Integer qty = line.getValueInteger("quantity");
               if(qty == null || qty <= 0)
               {
                  throw new QException("Quantity must be greater than zero");
               }
               BigDecimal unitPrice = line.getValueBigDecimal("unitPrice");
               if(unitPrice == null)
               {
                  throw new QException("Unit price is required");
               }
               BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(qty));
               line.setValue("lineTotal", lineTotal);
               total = total.add(lineTotal);
            }

            record.setValue("orderTotal", total);
            record.setValue("status", record.getValue("status") == null ? "NEW" : record.getValue("status"));

            runBackendStepOutput.getRecords().add(record);
         }
      }
   }
}

