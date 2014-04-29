/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.TypedValue;
/*  4:   */ 
/*  5:   */ public class NullLiteral
/*  6:   */   extends Literal
/*  7:   */ {
/*  8:   */   public NullLiteral(int pos)
/*  9:   */   {
/* 10:28 */     super(null, pos);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public TypedValue getLiteralValue()
/* 14:   */   {
/* 15:33 */     return TypedValue.NULL;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public String toString()
/* 19:   */   {
/* 20:38 */     return "null";
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.NullLiteral
 * JD-Core Version:    0.7.0.1
 */