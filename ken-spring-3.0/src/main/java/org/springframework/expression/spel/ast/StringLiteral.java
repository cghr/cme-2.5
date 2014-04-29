/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.TypedValue;
/*  4:   */ 
/*  5:   */ public class StringLiteral
/*  6:   */   extends Literal
/*  7:   */ {
/*  8:   */   private final TypedValue value;
/*  9:   */   
/* 10:   */   public StringLiteral(String payload, int pos, String value)
/* 11:   */   {
/* 12:31 */     super(payload, pos);
/* 13:   */     
/* 14:33 */     value = value.substring(1, value.length() - 1);
/* 15:34 */     this.value = new TypedValue(value.replaceAll("''", "'"));
/* 16:   */   }
/* 17:   */   
/* 18:   */   public TypedValue getLiteralValue()
/* 19:   */   {
/* 20:39 */     return this.value;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public String toString()
/* 24:   */   {
/* 25:44 */     return "'" + getLiteralValue().getValue() + "'";
/* 26:   */   }
/* 27:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.StringLiteral
 * JD-Core Version:    0.7.0.1
 */