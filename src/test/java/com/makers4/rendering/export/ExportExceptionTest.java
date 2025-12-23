package com.makers4.rendering.export;


import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;


/*******************************************************************************
 ** Unit tests for ExportException class.
 *******************************************************************************/
class ExportExceptionTest
{

   /*******************************************************************************
    ** Test exception with message only.
    *******************************************************************************/
   @Test
   void testExceptionWithMessage()
   {
      ExportException exception = new ExportException("Test error message");

      assertThat(exception.getMessage()).isEqualTo("Test error message");
      assertThat(exception.getCause()).isNull();
   }



   /*******************************************************************************
    ** Test exception with message and cause.
    *******************************************************************************/
   @Test
   void testExceptionWithMessageAndCause()
   {
      RuntimeException cause = new RuntimeException("Root cause");
      ExportException exception = new ExportException("Wrapped error", cause);

      assertThat(exception.getMessage()).isEqualTo("Wrapped error");
      assertThat(exception.getCause()).isEqualTo(cause);
   }



   /*******************************************************************************
    ** Test exception inheritance.
    *******************************************************************************/
   @Test
   void testExceptionIsCheckedException()
   {
      ExportException exception = new ExportException("Test");

      assertThat(exception).isInstanceOf(Exception.class);
      assertThat(exception).isNotInstanceOf(RuntimeException.class);
   }
}
