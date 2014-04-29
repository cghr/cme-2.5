/*  1:   */ package org.springframework.core.enums;
/*  2:   */ 
/*  3:   */ import org.springframework.util.Assert;
/*  4:   */ 
/*  5:   */ @Deprecated
/*  6:   */ public class StringCodedLabeledEnum
/*  7:   */   extends AbstractGenericLabeledEnum
/*  8:   */ {
/*  9:   */   private final String code;
/* 10:   */   
/* 11:   */   public StringCodedLabeledEnum(String code, String label)
/* 12:   */   {
/* 13:49 */     super(label);
/* 14:50 */     Assert.notNull(code, "'code' must not be null");
/* 15:51 */     this.code = code;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public Comparable getCode()
/* 19:   */   {
/* 20:56 */     return this.code;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public String getStringCode()
/* 24:   */   {
/* 25:63 */     return (String)getCode();
/* 26:   */   }
/* 27:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.enums.StringCodedLabeledEnum
 * JD-Core Version:    0.7.0.1
 */