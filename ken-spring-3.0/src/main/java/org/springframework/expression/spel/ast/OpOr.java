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
/* 11:   */ public class OpOr
/* 12:   */   extends Operator
/* 13:   */ {
/* 14:   */   public OpOr(int pos, SpelNodeImpl... operands)
/* 15:   */   {
/* 16:37 */     super("or", pos, operands);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public BooleanTypedValue getValueInternal(ExpressionState state)
/* 20:   */     throws EvaluationException
/* 21:   */   {
/* 22:   */     try
/* 23:   */     {
/* 24:45 */       TypedValue typedValue = getLeftOperand().getValueInternal(state);
/* 25:46 */       assertTypedValueNotNull(typedValue);
/* 26:47 */       leftValue = ((Boolean)state.convertValue(typedValue, TypeDescriptor.valueOf(Boolean.class))).booleanValue();
/* 27:   */     }
/* 28:   */     catch (SpelEvaluationException see)
/* 29:   */     {
/* 30:   */       boolean leftValue;
/* 31:50 */       see.setPosition(getLeftOperand().getStartPosition());
/* 32:51 */       throw see;
/* 33:   */     }
/* 34:   */     boolean leftValue;
/* 35:54 */     if (leftValue) {
/* 36:55 */       return BooleanTypedValue.TRUE;
/* 37:   */     }
/* 38:   */     try
/* 39:   */     {
/* 40:59 */       TypedValue typedValue = getRightOperand().getValueInternal(state);
/* 41:60 */       assertTypedValueNotNull(typedValue);
/* 42:61 */       rightValue = ((Boolean)state.convertValue(typedValue, TypeDescriptor.valueOf(Boolean.class))).booleanValue();
/* 43:   */     }
/* 44:   */     catch (SpelEvaluationException see)
/* 45:   */     {
/* 46:   */       boolean rightValue;
/* 47:64 */       see.setPosition(getRightOperand().getStartPosition());
/* 48:65 */       throw see;
/* 49:   */     }
/* 50:   */     boolean rightValue;
/* 51:68 */     return BooleanTypedValue.forValue((leftValue) || (rightValue));
/* 52:   */   }
/* 53:   */   
/* 54:   */   private void assertTypedValueNotNull(TypedValue typedValue)
/* 55:   */   {
/* 56:72 */     if (TypedValue.NULL.equals(typedValue)) {
/* 57:73 */       throw new SpelEvaluationException(SpelMessage.TYPE_CONVERSION_ERROR, new Object[] { "null", "boolean" });
/* 58:   */     }
/* 59:   */   }
/* 60:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.OpOr
 * JD-Core Version:    0.7.0.1
 */