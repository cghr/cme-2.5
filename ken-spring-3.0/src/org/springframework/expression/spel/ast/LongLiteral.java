/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.TypedValue;
/*  4:   */ 
/*  5:   */ public class LongLiteral
/*  6:   */   extends Literal
/*  7:   */ {
/*  8:   */   private final TypedValue value;
/*  9:   */   
/* 10:   */   LongLiteral(String payload, int pos, long value)
/* 11:   */   {
/* 12:32 */     super(payload, pos);
/* 13:33 */     this.value = new TypedValue(Long.valueOf(value));
/* 14:   */   }
/* 15:   */   
/* 16:   */   public TypedValue getLiteralValue()
/* 17:   */   {
/* 18:38 */     return this.value;
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.LongLiteral
 * JD-Core Version:    0.7.0.1
 */