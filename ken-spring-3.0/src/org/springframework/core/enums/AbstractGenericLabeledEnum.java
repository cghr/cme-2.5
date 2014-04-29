/*  1:   */ package org.springframework.core.enums;
/*  2:   */ 
/*  3:   */ /**
/*  4:   */  * @deprecated
/*  5:   */  */
/*  6:   */ public abstract class AbstractGenericLabeledEnum
/*  7:   */   extends AbstractLabeledEnum
/*  8:   */ {
/*  9:   */   private final String label;
/* 10:   */   
/* 11:   */   protected AbstractGenericLabeledEnum(String label)
/* 12:   */   {
/* 13:40 */     this.label = label;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public String getLabel()
/* 17:   */   {
/* 18:45 */     if (this.label != null) {
/* 19:46 */       return this.label;
/* 20:   */     }
/* 21:49 */     return getCode().toString();
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.enums.AbstractGenericLabeledEnum
 * JD-Core Version:    0.7.0.1
 */