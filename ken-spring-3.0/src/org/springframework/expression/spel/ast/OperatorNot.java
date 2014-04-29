/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.core.convert.TypeDescriptor;
/*  4:   */ import org.springframework.expression.EvaluationException;
/*  5:   */ import org.springframework.expression.TypedValue;
/*  6:   */ import org.springframework.expression.spel.ExpressionState;
/*  7:   */ import org.springframework.expression.spel.SpelEvaluationException;
/*  8:   */ import org.springframework.expression.spel.SpelMessage;
/*  9:   */ import org.springframework.expression.spel.SpelNode;
/* 10:   */ import org.springframework.expression.spel.support.BooleanTypedValue;
/* 11:   */ 
/* 12:   */ public class OperatorNot
/* 13:   */   extends SpelNodeImpl
/* 14:   */ {
/* 15:   */   public OperatorNot(int pos, SpelNodeImpl operand)
/* 16:   */   {
/* 17:37 */     super(pos, new SpelNodeImpl[] { operand });
/* 18:   */   }
/* 19:   */   
/* 20:   */   public BooleanTypedValue getValueInternal(ExpressionState state)
/* 21:   */     throws EvaluationException
/* 22:   */   {
/* 23:   */     try
/* 24:   */     {
/* 25:43 */       TypedValue typedValue = this.children[0].getValueInternal(state);
/* 26:44 */       if (TypedValue.NULL.equals(typedValue)) {
/* 27:45 */         throw new SpelEvaluationException(SpelMessage.TYPE_CONVERSION_ERROR, new Object[] { "null", "boolean" });
/* 28:   */       }
/* 29:47 */       boolean value = ((Boolean)state.convertValue(typedValue, TypeDescriptor.valueOf(Boolean.class))).booleanValue();
/* 30:48 */       return BooleanTypedValue.forValue(!value);
/* 31:   */     }
/* 32:   */     catch (SpelEvaluationException see)
/* 33:   */     {
/* 34:51 */       see.setPosition(getChild(0).getStartPosition());
/* 35:52 */       throw see;
/* 36:   */     }
/* 37:   */   }
/* 38:   */   
/* 39:   */   public String toStringAST()
/* 40:   */   {
/* 41:58 */     StringBuilder sb = new StringBuilder();
/* 42:59 */     sb.append("!").append(getChild(0).toStringAST());
/* 43:60 */     return sb.toString();
/* 44:   */   }
/* 45:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.OperatorNot
 * JD-Core Version:    0.7.0.1
 */