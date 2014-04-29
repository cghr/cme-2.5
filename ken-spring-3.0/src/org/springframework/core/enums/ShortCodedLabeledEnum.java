/*  1:   */ package org.springframework.core.enums;
/*  2:   */ 
/*  3:   */ @Deprecated
/*  4:   */ public class ShortCodedLabeledEnum
/*  5:   */   extends AbstractGenericLabeledEnum
/*  6:   */ {
/*  7:   */   private final Short code;
/*  8:   */   
/*  9:   */   public ShortCodedLabeledEnum(int code, String label)
/* 10:   */   {
/* 11:45 */     super(label);
/* 12:46 */     this.code = new Short((short)code);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public Comparable getCode()
/* 16:   */   {
/* 17:51 */     return this.code;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public short getShortCode()
/* 21:   */   {
/* 22:58 */     return ((Short)getCode()).shortValue();
/* 23:   */   }
/* 24:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.enums.ShortCodedLabeledEnum
 * JD-Core Version:    0.7.0.1
 */