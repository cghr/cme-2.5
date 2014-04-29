/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.spel.support.BooleanTypedValue;
/*  4:   */ 
/*  5:   */ public class BooleanLiteral
/*  6:   */   extends Literal
/*  7:   */ {
/*  8:   */   private final BooleanTypedValue value;
/*  9:   */   
/* 10:   */   public BooleanLiteral(String payload, int pos, boolean value)
/* 11:   */   {
/* 12:31 */     super(payload, pos);
/* 13:32 */     this.value = BooleanTypedValue.forValue(value);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public BooleanTypedValue getLiteralValue()
/* 17:   */   {
/* 18:37 */     return this.value;
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.BooleanLiteral
 * JD-Core Version:    0.7.0.1
 */