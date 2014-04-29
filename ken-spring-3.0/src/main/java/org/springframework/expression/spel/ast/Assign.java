/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.EvaluationException;
/*  4:   */ import org.springframework.expression.TypedValue;
/*  5:   */ import org.springframework.expression.spel.ExpressionState;
/*  6:   */ import org.springframework.expression.spel.SpelNode;
/*  7:   */ 
/*  8:   */ public class Assign
/*  9:   */   extends SpelNodeImpl
/* 10:   */ {
/* 11:   */   public Assign(int pos, SpelNodeImpl... operands)
/* 12:   */   {
/* 13:34 */     super(pos, operands);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public TypedValue getValueInternal(ExpressionState state)
/* 17:   */     throws EvaluationException
/* 18:   */   {
/* 19:39 */     TypedValue newValue = this.children[1].getValueInternal(state);
/* 20:40 */     getChild(0).setValue(state, newValue.getValue());
/* 21:41 */     return newValue;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public String toStringAST()
/* 25:   */   {
/* 26:46 */     return getChild(0).toStringAST() + "=" + getChild(1).toStringAST();
/* 27:   */   }
/* 28:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.Assign
 * JD-Core Version:    0.7.0.1
 */