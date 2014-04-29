/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.TypedValue;
/*  4:   */ import org.springframework.expression.spel.ExpressionState;
/*  5:   */ 
/*  6:   */ public class Identifier
/*  7:   */   extends SpelNodeImpl
/*  8:   */ {
/*  9:   */   private final TypedValue id;
/* 10:   */   
/* 11:   */   public Identifier(String payload, int pos)
/* 12:   */   {
/* 13:31 */     super(pos, new SpelNodeImpl[0]);
/* 14:32 */     this.id = new TypedValue(payload);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public String toStringAST()
/* 18:   */   {
/* 19:37 */     return (String)this.id.getValue();
/* 20:   */   }
/* 21:   */   
/* 22:   */   public TypedValue getValueInternal(ExpressionState state)
/* 23:   */   {
/* 24:42 */     return this.id;
/* 25:   */   }
/* 26:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.Identifier
 * JD-Core Version:    0.7.0.1
 */