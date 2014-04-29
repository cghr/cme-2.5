/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.core.convert.TypeDescriptor;
/*  4:   */ import org.springframework.expression.EvaluationException;
/*  5:   */ import org.springframework.expression.TypedValue;
/*  6:   */ import org.springframework.expression.spel.ExpressionState;
/*  7:   */ import org.springframework.expression.spel.SpelEvaluationException;
/*  8:   */ import org.springframework.expression.spel.SpelMessage;
/*  9:   */ import org.springframework.expression.spel.support.BooleanTypedValue;
/* 10:   */ 
/* 11:   */ public class OpAnd
/* 12:   */   extends Operator
/* 13:   */ {
/* 14:   */   public OpAnd(int pos, SpelNodeImpl... operands)
/* 15:   */   {
/* 16:37 */     super("and", pos, operands);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public TypedValue getValueInternal(ExpressionState state)
/* 20:   */     throws EvaluationException
/* 21:   */   {
/* 22:   */     try
/* 23:   */     {
/* 24:46 */       TypedValue typedValue = getLeftOperand().getValueInternal(state);
/* 25:47 */       assertTypedValueNotNull(typedValue);
/* 26:48 */       leftValue = ((Boolean)state.convertValue(typedValue, TypeDescriptor.valueOf(Boolean.class))).booleanValue();
/* 27:   */     }
/* 28:   */     catch (SpelEvaluationException ee)
/* 29:   */     {
/* 30:   */       boolean leftValue;
/* 31:51 */       ee.setPosition(getLeftOperand().getStartPosition());
/* 32:52 */       throw ee;
/* 33:   */     }
/* 34:   */     boolean leftValue;
/* 35:55 */     if (!leftValue) {
/* 36:56 */       return BooleanTypedValue.forValue(false);
/* 37:   */     }
/* 38:   */     try
/* 39:   */     {
/* 40:60 */       TypedValue typedValue = getRightOperand().getValueInternal(state);
/* 41:61 */       assertTypedValueNotNull(typedValue);
/* 42:62 */       rightValue = ((Boolean)state.convertValue(typedValue, TypeDescriptor.valueOf(Boolean.class))).booleanValue();
/* 43:   */     }
/* 44:   */     catch (SpelEvaluationException ee)
/* 45:   */     {
/* 46:   */       boolean rightValue;
/* 47:65 */       ee.setPosition(getRightOperand().getStartPosition());
/* 48:66 */       throw ee;
/* 49:   */     }
/* 50:   */     boolean rightValue;
/* 51:69 */     return BooleanTypedValue.forValue(rightValue);
/* 52:   */   }
/* 53:   */   
/* 54:   */   private void assertTypedValueNotNull(TypedValue typedValue)
/* 55:   */   {
/* 56:73 */     if (TypedValue.NULL.equals(typedValue)) {
/* 57:74 */       throw new SpelEvaluationException(SpelMessage.TYPE_CONVERSION_ERROR, new Object[] { "null", "boolean" });
/* 58:   */     }
/* 59:   */   }
/* 60:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.OpAnd
 * JD-Core Version:    0.7.0.1
 */