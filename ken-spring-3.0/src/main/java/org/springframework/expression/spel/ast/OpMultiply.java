/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.EvaluationException;
/*  4:   */ import org.springframework.expression.Operation;
/*  5:   */ import org.springframework.expression.TypedValue;
/*  6:   */ import org.springframework.expression.spel.ExpressionState;
/*  7:   */ 
/*  8:   */ public class OpMultiply
/*  9:   */   extends Operator
/* 10:   */ {
/* 11:   */   public OpMultiply(int pos, SpelNodeImpl... operands)
/* 12:   */   {
/* 13:43 */     super("*", pos, operands);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public TypedValue getValueInternal(ExpressionState state)
/* 17:   */     throws EvaluationException
/* 18:   */   {
/* 19:57 */     Object operandOne = getLeftOperand().getValueInternal(state).getValue();
/* 20:58 */     Object operandTwo = getRightOperand().getValueInternal(state).getValue();
/* 21:59 */     if (((operandOne instanceof Number)) && ((operandTwo instanceof Number)))
/* 22:   */     {
/* 23:60 */       Number leftNumber = (Number)operandOne;
/* 24:61 */       Number rightNumber = (Number)operandTwo;
/* 25:62 */       if (((leftNumber instanceof Double)) || ((rightNumber instanceof Double))) {
/* 26:63 */         return new TypedValue(Double.valueOf(leftNumber.doubleValue() * rightNumber.doubleValue()));
/* 27:   */       }
/* 28:64 */       if (((leftNumber instanceof Long)) || ((rightNumber instanceof Long))) {
/* 29:65 */         return new TypedValue(Long.valueOf(leftNumber.longValue() * rightNumber.longValue()));
/* 30:   */       }
/* 31:67 */       return new TypedValue(Integer.valueOf(leftNumber.intValue() * rightNumber.intValue()));
/* 32:   */     }
/* 33:69 */     if (((operandOne instanceof String)) && ((operandTwo instanceof Integer)))
/* 34:   */     {
/* 35:70 */       int repeats = ((Integer)operandTwo).intValue();
/* 36:71 */       StringBuilder result = new StringBuilder();
/* 37:72 */       for (int i = 0; i < repeats; i++) {
/* 38:73 */         result.append(operandOne);
/* 39:   */       }
/* 40:75 */       return new TypedValue(result.toString());
/* 41:   */     }
/* 42:77 */     return state.operate(Operation.MULTIPLY, operandOne, operandTwo);
/* 43:   */   }
/* 44:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.OpMultiply
 * JD-Core Version:    0.7.0.1
 */