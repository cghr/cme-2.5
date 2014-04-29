/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.TypedValue;
/*  4:   */ 
/*  5:   */ public class RealLiteral
/*  6:   */   extends Literal
/*  7:   */ {
/*  8:   */   private final TypedValue value;
/*  9:   */   
/* 10:   */   public RealLiteral(String payload, int pos, double value)
/* 11:   */   {
/* 12:30 */     super(payload, pos);
/* 13:31 */     this.value = new TypedValue(Double.valueOf(value));
/* 14:   */   }
/* 15:   */   
/* 16:   */   public TypedValue getLiteralValue()
/* 17:   */   {
/* 18:36 */     return this.value;
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.RealLiteral
 * JD-Core Version:    0.7.0.1
 */