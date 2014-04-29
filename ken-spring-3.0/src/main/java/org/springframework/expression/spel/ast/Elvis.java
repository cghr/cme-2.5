/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.EvaluationException;
/*  4:   */ import org.springframework.expression.TypedValue;
/*  5:   */ import org.springframework.expression.spel.ExpressionState;
/*  6:   */ import org.springframework.expression.spel.SpelNode;
/*  7:   */ 
/*  8:   */ public class Elvis
/*  9:   */   extends SpelNodeImpl
/* 10:   */ {
/* 11:   */   public Elvis(int pos, SpelNodeImpl... args)
/* 12:   */   {
/* 13:33 */     super(pos, args);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public TypedValue getValueInternal(ExpressionState state)
/* 17:   */     throws EvaluationException
/* 18:   */   {
/* 19:44 */     TypedValue value = this.children[0].getValueInternal(state);
/* 20:45 */     if ((value.getValue() != null) && ((!(value.getValue() instanceof String)) || (((String)value.getValue()).length() != 0))) {
/* 21:46 */       return value;
/* 22:   */     }
/* 23:48 */     return this.children[1].getValueInternal(state);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public String toStringAST()
/* 27:   */   {
/* 28:54 */     return getChild(0).toStringAST() + " ?: " + getChild(1).toStringAST();
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.Elvis
 * JD-Core Version:    0.7.0.1
 */