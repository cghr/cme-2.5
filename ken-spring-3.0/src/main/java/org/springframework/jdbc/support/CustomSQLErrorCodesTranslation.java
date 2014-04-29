/*  1:   */ package org.springframework.jdbc.support;
/*  2:   */ 
/*  3:   */ import org.springframework.dao.DataAccessException;
/*  4:   */ import org.springframework.util.StringUtils;
/*  5:   */ 
/*  6:   */ public class CustomSQLErrorCodesTranslation
/*  7:   */ {
/*  8:33 */   private String[] errorCodes = new String[0];
/*  9:   */   private Class exceptionClass;
/* 10:   */   
/* 11:   */   public void setErrorCodes(String[] errorCodes)
/* 12:   */   {
/* 13:42 */     this.errorCodes = StringUtils.sortStringArray(errorCodes);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public String[] getErrorCodes()
/* 17:   */   {
/* 18:49 */     return this.errorCodes;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void setExceptionClass(Class exceptionClass)
/* 22:   */   {
/* 23:56 */     if (!DataAccessException.class.isAssignableFrom(exceptionClass)) {
/* 24:57 */       throw new IllegalArgumentException("Invalid exception class [" + exceptionClass + 
/* 25:58 */         "]: needs to be a subclass of [org.springframework.dao.DataAccessException]");
/* 26:   */     }
/* 27:60 */     this.exceptionClass = exceptionClass;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public Class getExceptionClass()
/* 31:   */   {
/* 32:67 */     return this.exceptionClass;
/* 33:   */   }
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.CustomSQLErrorCodesTranslation
 * JD-Core Version:    0.7.0.1
 */