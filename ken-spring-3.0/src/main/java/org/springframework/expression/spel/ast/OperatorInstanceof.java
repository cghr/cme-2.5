/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.EvaluationException;
/*  4:   */ import org.springframework.expression.TypedValue;
/*  5:   */ import org.springframework.expression.spel.ExpressionState;
/*  6:   */ import org.springframework.expression.spel.SpelEvaluationException;
/*  7:   */ import org.springframework.expression.spel.SpelMessage;
/*  8:   */ import org.springframework.expression.spel.support.BooleanTypedValue;
/*  9:   */ 
/* 10:   */ public class OperatorInstanceof
/* 11:   */   extends Operator
/* 12:   */ {
/* 13:   */   public OperatorInstanceof(int pos, SpelNodeImpl... operands)
/* 14:   */   {
/* 15:36 */     super("instanceof", pos, operands);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public BooleanTypedValue getValueInternal(ExpressionState state)
/* 19:   */     throws EvaluationException
/* 20:   */   {
/* 21:48 */     TypedValue left = getLeftOperand().getValueInternal(state);
/* 22:49 */     TypedValue right = getRightOperand().getValueInternal(state);
/* 23:50 */     Object leftValue = left.getValue();
/* 24:51 */     Object rightValue = right.getValue();
/* 25:52 */     if (leftValue == null) {
/* 26:53 */       return BooleanTypedValue.FALSE;
/* 27:   */     }
/* 28:55 */     if ((rightValue == null) || (!(rightValue instanceof Class))) {
/* 29:56 */       throw new SpelEvaluationException(getRightOperand().getStartPosition(), 
/* 30:57 */         SpelMessage.INSTANCEOF_OPERATOR_NEEDS_CLASS_OPERAND, new Object[] {
/* 31:58 */         rightValue == null ? "null" : rightValue.getClass().getName() });
/* 32:   */     }
/* 33:60 */     Class<?> rightClass = (Class)rightValue;
/* 34:61 */     return BooleanTypedValue.forValue(rightClass.isAssignableFrom(leftValue.getClass()));
/* 35:   */   }
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.OperatorInstanceof
 * JD-Core Version:    0.7.0.1
 */