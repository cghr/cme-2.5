/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.EvaluationException;
/*  4:   */ import org.springframework.expression.Operation;
/*  5:   */ import org.springframework.expression.TypedValue;
/*  6:   */ import org.springframework.expression.spel.ExpressionState;
/*  7:   */ 
/*  8:   */ public class OperatorPower
/*  9:   */   extends Operator
/* 10:   */ {
/* 11:   */   public OperatorPower(int pos, SpelNodeImpl... operands)
/* 12:   */   {
/* 13:33 */     super("^", pos, operands);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public TypedValue getValueInternal(ExpressionState state)
/* 17:   */     throws EvaluationException
/* 18:   */   {
/* 19:38 */     SpelNodeImpl leftOp = getLeftOperand();
/* 20:39 */     SpelNodeImpl rightOp = getRightOperand();
/* 21:   */     
/* 22:41 */     Object operandOne = leftOp.getValueInternal(state).getValue();
/* 23:42 */     Object operandTwo = rightOp.getValueInternal(state).getValue();
/* 24:43 */     if (((operandOne instanceof Number)) && ((operandTwo instanceof Number)))
/* 25:   */     {
/* 26:44 */       Number op1 = (Number)operandOne;
/* 27:45 */       Number op2 = (Number)operandTwo;
/* 28:46 */       if (((op1 instanceof Double)) || ((op2 instanceof Double))) {
/* 29:47 */         return new TypedValue(Double.valueOf(Math.pow(op1.doubleValue(), op2.doubleValue())));
/* 30:   */       }
/* 31:48 */       if (((op1 instanceof Long)) || ((op2 instanceof Long)))
/* 32:   */       {
/* 33:49 */         double d = Math.pow(op1.longValue(), op2.longValue());
/* 34:50 */         return new TypedValue(Long.valueOf(d));
/* 35:   */       }
/* 36:52 */       double d = Math.pow(op1.longValue(), op2.longValue());
/* 37:53 */       if (d > 2147483647.0D) {
/* 38:54 */         return new TypedValue(Long.valueOf(d));
/* 39:   */       }
/* 40:56 */       return new TypedValue(Integer.valueOf((int)d));
/* 41:   */     }
/* 42:60 */     return state.operate(Operation.POWER, operandOne, operandTwo);
/* 43:   */   }
/* 44:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.OperatorPower
 * JD-Core Version:    0.7.0.1
 */