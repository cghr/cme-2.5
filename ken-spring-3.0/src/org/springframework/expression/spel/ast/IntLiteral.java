/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.TypedValue;
/*  4:   */ 
/*  5:   */ public class IntLiteral
/*  6:   */   extends Literal
/*  7:   */ {
/*  8:   */   private final TypedValue value;
/*  9:   */   
/* 10:   */   IntLiteral(String payload, int pos, int value)
/* 11:   */   {
/* 12:31 */     super(payload, pos);
/* 13:32 */     this.value = new TypedValue(Integer.valueOf(value));
/* 14:   */   }
/* 15:   */   
/* 16:   */   public TypedValue getLiteralValue()
/* 17:   */   {
/* 18:37 */     return this.value;
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.IntLiteral
 * JD-Core Version:    0.7.0.1
 */