package com.makers4.rendering.export;


/*******************************************************************************
 ** Exception thrown when an export operation fails.
 *******************************************************************************/
public class ExportException extends Exception
{

   /*******************************************************************************
    ** Constructor with message.
    *******************************************************************************/
   public ExportException(String message)
   {
      super(message);
   }



   /*******************************************************************************
    ** Constructor with message and cause.
    *******************************************************************************/
   public ExportException(String message, Throwable cause)
   {
      super(message, cause);
   }
}
