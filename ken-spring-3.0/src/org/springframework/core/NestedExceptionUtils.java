/*  1:   */ package org.springframework.core;
/*  2:   */ 
/*  3:   */ public abstract class NestedExceptionUtils
/*  4:   */ {
/*  5:   */   public static String buildMessage(String message, Throwable cause)
/*  6:   */   {
/*  7:42 */     if (cause != null)
/*  8:   */     {
/*  9:43 */       StringBuilder sb = new StringBuilder();
/* 10:44 */       if (message != null) {
/* 11:45 */         sb.append(message).append("; ");
/* 12:   */       }
/* 13:47 */       sb.append("nested exception is ").append(cause);
/* 14:48 */       return sb.toString();
/* 15:   */     }
/* 16:51 */     return message;
/* 17:   */   }
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.NestedExceptionUtils
 * JD-Core Version:    0.7.0.1
 */