/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.TypedValue;
/*  4:   */ import org.springframework.expression.spel.ExpressionState;
/*  5:   */ import org.springframework.expression.spel.SpelEvaluationException;
/*  6:   */ 
/*  7:   */ public class VariableReference
/*  8:   */   extends SpelNodeImpl
/*  9:   */ {
/* 10:   */   private static final String THIS = "this";
/* 11:   */   private static final String ROOT = "root";
/* 12:   */   private final String name;
/* 13:   */   
/* 14:   */   public VariableReference(String variableName, int pos)
/* 15:   */   {
/* 16:39 */     super(pos, new SpelNodeImpl[0]);
/* 17:40 */     this.name = variableName;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public TypedValue getValueInternal(ExpressionState state)
/* 21:   */     throws SpelEvaluationException
/* 22:   */   {
/* 23:46 */     if (this.name.equals("this")) {
/* 24:47 */       return state.getActiveContextObject();
/* 25:   */     }
/* 26:49 */     if (this.name.equals("root")) {
/* 27:50 */       return state.getRootContextObject();
/* 28:   */     }
/* 29:52 */     TypedValue result = state.lookupVariable(this.name);
/* 30:   */     
/* 31:54 */     return result;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public void setValue(ExpressionState state, Object value)
/* 35:   */     throws SpelEvaluationException
/* 36:   */   {
/* 37:59 */     state.setVariable(this.name, value);
/* 38:   */   }
/* 39:   */   
/* 40:   */   public String toStringAST()
/* 41:   */   {
/* 42:64 */     return "#" + this.name;
/* 43:   */   }
/* 44:   */   
/* 45:   */   public boolean isWritable(ExpressionState expressionState)
/* 46:   */     throws SpelEvaluationException
/* 47:   */   {
/* 48:69 */     return (!this.name.equals("this")) && (!this.name.equals("root"));
/* 49:   */   }
/* 50:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.VariableReference
 * JD-Core Version:    0.7.0.1
 */