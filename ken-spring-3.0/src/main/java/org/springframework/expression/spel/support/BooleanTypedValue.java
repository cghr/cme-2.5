/*  1:   */ package org.springframework.expression.spel.support;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.TypedValue;
/*  4:   */ 
/*  5:   */ public class BooleanTypedValue
/*  6:   */   extends TypedValue
/*  7:   */ {
/*  8:27 */   public static final BooleanTypedValue TRUE = new BooleanTypedValue(true);
/*  9:29 */   public static final BooleanTypedValue FALSE = new BooleanTypedValue(false);
/* 10:   */   
/* 11:   */   private BooleanTypedValue(boolean b)
/* 12:   */   {
/* 13:33 */     super(Boolean.valueOf(b));
/* 14:   */   }
/* 15:   */   
/* 16:   */   public static BooleanTypedValue forValue(boolean b)
/* 17:   */   {
/* 18:38 */     if (b) {
/* 19:39 */       return TRUE;
/* 20:   */     }
/* 21:42 */     return FALSE;
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.support.BooleanTypedValue
 * JD-Core Version:    0.7.0.1
 */