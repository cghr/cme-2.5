/*  1:   */ package org.springframework.context;
/*  2:   */ 
/*  3:   */ import java.util.Locale;
/*  4:   */ 
/*  5:   */ public class NoSuchMessageException
/*  6:   */   extends RuntimeException
/*  7:   */ {
/*  8:   */   public NoSuchMessageException(String code, Locale locale)
/*  9:   */   {
/* 10:34 */     super("No message found under code '" + code + "' for locale '" + locale + "'.");
/* 11:   */   }
/* 12:   */   
/* 13:   */   public NoSuchMessageException(String code)
/* 14:   */   {
/* 15:42 */     super("No message found under code '" + code + "' for locale '" + Locale.getDefault() + "'.");
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.NoSuchMessageException
 * JD-Core Version:    0.7.0.1
 */