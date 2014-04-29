/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import java.util.List;
/*  4:   */ import org.springframework.expression.EvaluationException;
/*  5:   */ import org.springframework.expression.TypeComparator;
/*  6:   */ import org.springframework.expression.TypedValue;
/*  7:   */ import org.springframework.expression.spel.ExpressionState;
/*  8:   */ import org.springframework.expression.spel.SpelEvaluationException;
/*  9:   */ import org.springframework.expression.spel.SpelMessage;
/* 10:   */ import org.springframework.expression.spel.support.BooleanTypedValue;
/* 11:   */ 
/* 12:   */ public class OperatorBetween
/* 13:   */   extends Operator
/* 14:   */ {
/* 15:   */   public OperatorBetween(int pos, SpelNodeImpl... operands)
/* 16:   */   {
/* 17:39 */     super("between", pos, operands);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public BooleanTypedValue getValueInternal(ExpressionState state)
/* 21:   */     throws EvaluationException
/* 22:   */   {
/* 23:51 */     Object left = getLeftOperand().getValueInternal(state).getValue();
/* 24:52 */     Object right = getRightOperand().getValueInternal(state).getValue();
/* 25:53 */     if ((!(right instanceof List)) || (((List)right).size() != 2)) {
/* 26:54 */       throw new SpelEvaluationException(getRightOperand().getStartPosition(), 
/* 27:55 */         SpelMessage.BETWEEN_RIGHT_OPERAND_MUST_BE_TWO_ELEMENT_LIST, new Object[0]);
/* 28:   */     }
/* 29:57 */     List<?> l = (List)right;
/* 30:58 */     Object low = l.get(0);
/* 31:59 */     Object high = l.get(1);
/* 32:60 */     TypeComparator comparator = state.getTypeComparator();
/* 33:   */     try
/* 34:   */     {
/* 35:62 */       return BooleanTypedValue.forValue((comparator.compare(left, low) >= 0) && (comparator.compare(left, high) <= 0));
/* 36:   */     }
/* 37:   */     catch (SpelEvaluationException ex)
/* 38:   */     {
/* 39:64 */       ex.setPosition(getStartPosition());
/* 40:65 */       throw ex;
/* 41:   */     }
/* 42:   */   }
/* 43:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.OperatorBetween
 * JD-Core Version:    0.7.0.1
 */