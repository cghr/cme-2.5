/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.EvaluationException;
/*  4:   */ import org.springframework.expression.TypedValue;
/*  5:   */ import org.springframework.expression.spel.ExpressionState;
/*  6:   */ import org.springframework.expression.spel.SpelEvaluationException;
/*  7:   */ import org.springframework.expression.spel.SpelMessage;
/*  8:   */ import org.springframework.expression.spel.SpelNode;
/*  9:   */ 
/* 10:   */ public class Ternary
/* 11:   */   extends SpelNodeImpl
/* 12:   */ {
/* 13:   */   public Ternary(int pos, SpelNodeImpl... args)
/* 14:   */   {
/* 15:36 */     super(pos, args);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public TypedValue getValueInternal(ExpressionState state)
/* 19:   */     throws EvaluationException
/* 20:   */   {
/* 21:47 */     Boolean value = (Boolean)this.children[0].getValue(state, Boolean.class);
/* 22:48 */     if (value == null) {
/* 23:49 */       throw new SpelEvaluationException(getChild(0).getStartPosition(), 
/* 24:50 */         SpelMessage.TYPE_CONVERSION_ERROR, new Object[] { "null", "boolean" });
/* 25:   */     }
/* 26:52 */     if (value.booleanValue()) {
/* 27:53 */       return this.children[1].getValueInternal(state);
/* 28:   */     }
/* 29:55 */     return this.children[2].getValueInternal(state);
/* 30:   */   }
/* 31:   */   
/* 32:   */   public String toStringAST()
/* 33:   */   {
/* 34:61 */     return 
/* 35:62 */       getChild(0).toStringAST() + " ? " + getChild(1).toStringAST() + " : " + getChild(2).toStringAST();
/* 36:   */   }
/* 37:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.Ternary
 * JD-Core Version:    0.7.0.1
 */