/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.EvaluationException;
/*  4:   */ import org.springframework.expression.Operation;
/*  5:   */ import org.springframework.expression.TypedValue;
/*  6:   */ import org.springframework.expression.spel.ExpressionState;
/*  7:   */ 
/*  8:   */ public class OpModulus
/*  9:   */   extends Operator
/* 10:   */ {
/* 11:   */   public OpModulus(int pos, SpelNodeImpl... operands)
/* 12:   */   {
/* 13:33 */     super("%", pos, operands);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public TypedValue getValueInternal(ExpressionState state)
/* 17:   */     throws EvaluationException
/* 18:   */   {
/* 19:38 */     Object operandOne = getLeftOperand().getValueInternal(state).getValue();
/* 20:39 */     Object operandTwo = getRightOperand().getValueInternal(state).getValue();
/* 21:40 */     if (((operandOne instanceof Number)) && ((operandTwo instanceof Number)))
/* 22:   */     {
/* 23:41 */       Number op1 = (Number)operandOne;
/* 24:42 */       Number op2 = (Number)operandTwo;
/* 25:43 */       if (((op1 instanceof Double)) || ((op2 instanceof Double))) {
/* 26:44 */         return new TypedValue(Double.valueOf(op1.doubleValue() % op2.doubleValue()));
/* 27:   */       }
/* 28:45 */       if (((op1 instanceof Long)) || ((op2 instanceof Long))) {
/* 29:46 */         return new TypedValue(Long.valueOf(op1.longValue() % op2.longValue()));
/* 30:   */       }
/* 31:48 */       return new TypedValue(Integer.valueOf(op1.intValue() % op2.intValue()));
/* 32:   */     }
/* 33:51 */     return state.operate(Operation.MODULUS, operandOne, operandTwo);
/* 34:   */   }
/* 35:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.OpModulus
 * JD-Core Version:    0.7.0.1
 */