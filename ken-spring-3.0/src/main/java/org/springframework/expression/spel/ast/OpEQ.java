/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.EvaluationException;
/*  4:   */ import org.springframework.expression.TypeComparator;
/*  5:   */ import org.springframework.expression.TypedValue;
/*  6:   */ import org.springframework.expression.spel.ExpressionState;
/*  7:   */ import org.springframework.expression.spel.support.BooleanTypedValue;
/*  8:   */ 
/*  9:   */ public class OpEQ
/* 10:   */   extends Operator
/* 11:   */ {
/* 12:   */   public OpEQ(int pos, SpelNodeImpl... operands)
/* 13:   */   {
/* 14:32 */     super("==", pos, operands);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public BooleanTypedValue getValueInternal(ExpressionState state)
/* 18:   */     throws EvaluationException
/* 19:   */   {
/* 20:37 */     Object left = getLeftOperand().getValueInternal(state).getValue();
/* 21:38 */     Object right = getRightOperand().getValueInternal(state).getValue();
/* 22:39 */     if (((left instanceof Number)) && ((right instanceof Number)))
/* 23:   */     {
/* 24:40 */       Number op1 = (Number)left;
/* 25:41 */       Number op2 = (Number)right;
/* 26:42 */       if (((op1 instanceof Double)) || ((op2 instanceof Double))) {
/* 27:43 */         return BooleanTypedValue.forValue(op1.doubleValue() == op2.doubleValue());
/* 28:   */       }
/* 29:44 */       if (((op1 instanceof Long)) || ((op2 instanceof Long))) {
/* 30:45 */         return BooleanTypedValue.forValue(op1.longValue() == op2.longValue());
/* 31:   */       }
/* 32:47 */       return BooleanTypedValue.forValue(op1.intValue() == op2.intValue());
/* 33:   */     }
/* 34:50 */     if ((left != null) && ((left instanceof Comparable))) {
/* 35:51 */       return BooleanTypedValue.forValue(state.getTypeComparator().compare(left, right) == 0);
/* 36:   */     }
/* 37:53 */     return BooleanTypedValue.forValue(left == right);
/* 38:   */   }
/* 39:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.OpEQ
 * JD-Core Version:    0.7.0.1
 */