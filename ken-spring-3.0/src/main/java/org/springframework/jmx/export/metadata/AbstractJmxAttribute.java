/*  1:   */ package org.springframework.jmx.export.metadata;
/*  2:   */ 
/*  3:   */ public class AbstractJmxAttribute
/*  4:   */ {
/*  5:27 */   private String description = "";
/*  6:29 */   private int currencyTimeLimit = -1;
/*  7:   */   
/*  8:   */   public void setDescription(String description)
/*  9:   */   {
/* 10:36 */     this.description = description;
/* 11:   */   }
/* 12:   */   
/* 13:   */   public String getDescription()
/* 14:   */   {
/* 15:43 */     return this.description;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void setCurrencyTimeLimit(int currencyTimeLimit)
/* 19:   */   {
/* 20:50 */     this.currencyTimeLimit = currencyTimeLimit;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public int getCurrencyTimeLimit()
/* 24:   */   {
/* 25:57 */     return this.currencyTimeLimit;
/* 26:   */   }
/* 27:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.metadata.AbstractJmxAttribute
 * JD-Core Version:    0.7.0.1
 */