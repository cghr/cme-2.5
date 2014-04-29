/*  1:   */ package org.springframework.core.enums;
/*  2:   */ 
/*  3:   */ @Deprecated
/*  4:   */ public abstract class StaticLabeledEnum
/*  5:   */   extends AbstractLabeledEnum
/*  6:   */ {
/*  7:   */   private final Short code;
/*  8:   */   private final transient String label;
/*  9:   */   
/* 10:   */   protected StaticLabeledEnum(int code, String label)
/* 11:   */   {
/* 12:66 */     this.code = new Short((short)code);
/* 13:67 */     if (label != null) {
/* 14:68 */       this.label = label;
/* 15:   */     } else {
/* 16:71 */       this.label = this.code.toString();
/* 17:   */     }
/* 18:   */   }
/* 19:   */   
/* 20:   */   public Comparable getCode()
/* 21:   */   {
/* 22:76 */     return this.code;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public String getLabel()
/* 26:   */   {
/* 27:80 */     return this.label;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public short shortValue()
/* 31:   */   {
/* 32:87 */     return ((Number)getCode()).shortValue();
/* 33:   */   }
/* 34:   */   
/* 35:   */   protected Object readResolve()
/* 36:   */   {
/* 37:99 */     return StaticLabeledEnumResolver.instance().getLabeledEnumByCode(getType(), getCode());
/* 38:   */   }
/* 39:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.enums.StaticLabeledEnum
 * JD-Core Version:    0.7.0.1
 */