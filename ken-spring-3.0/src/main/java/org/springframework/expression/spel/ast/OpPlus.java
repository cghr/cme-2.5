/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.EvaluationException;
/*  4:   */ import org.springframework.expression.Operation;
/*  5:   */ import org.springframework.expression.TypedValue;
/*  6:   */ import org.springframework.expression.spel.ExpressionState;
/*  7:   */ 
/*  8:   */ public class OpPlus
/*  9:   */   extends Operator
/* 10:   */ {
/* 11:   */   public OpPlus(int pos, SpelNodeImpl... operands)
/* 12:   */   {
/* 13:41 */     super("+", pos, operands);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public TypedValue getValueInternal(ExpressionState state)
/* 17:   */     throws EvaluationException
/* 18:   */   {
/* 19:46 */     SpelNodeImpl leftOp = getLeftOperand();
/* 20:47 */     SpelNodeImpl rightOp = getRightOperand();
/* 21:48 */     if (rightOp == null)
/* 22:   */     {
/* 23:49 */       Object operandOne = leftOp.getValueInternal(state).getValue();
/* 24:50 */       if ((operandOne instanceof Number))
/* 25:   */       {
/* 26:51 */         if ((operandOne instanceof Double)) {
/* 27:52 */           return new TypedValue(Double.valueOf(((Double)operandOne).doubleValue()));
/* 28:   */         }
/* 29:53 */         if ((operandOne instanceof Long)) {
/* 30:54 */           return new TypedValue(Long.valueOf(((Long)operandOne).longValue()));
/* 31:   */         }
/* 32:56 */         return new TypedValue(Integer.valueOf(((Integer)operandOne).intValue()));
/* 33:   */       }
/* 34:59 */       return state.operate(Operation.ADD, operandOne, null);
/* 35:   */     }
/* 36:62 */     Object operandOne = leftOp.getValueInternal(state).getValue();
/* 37:63 */     Object operandTwo = rightOp.getValueInternal(state).getValue();
/* 38:64 */     if (((operandOne instanceof Number)) && ((operandTwo instanceof Number)))
/* 39:   */     {
/* 40:65 */       Number op1 = (Number)operandOne;
/* 41:66 */       Number op2 = (Number)operandTwo;
/* 42:67 */       if (((op1 instanceof Double)) || ((op2 instanceof Double))) {
/* 43:68 */         return new TypedValue(Double.valueOf(op1.doubleValue() + op2.doubleValue()));
/* 44:   */       }
/* 45:69 */       if (((op1 instanceof Long)) || ((op2 instanceof Long))) {
/* 46:70 */         return new TypedValue(Long.valueOf(op1.longValue() + op2.longValue()));
/* 47:   */       }
/* 48:72 */       return new TypedValue(Integer.valueOf(op1.intValue() + op2.intValue()));
/* 49:   */     }
/* 50:74 */     if (((operandOne instanceof String)) && ((operandTwo instanceof String))) {
/* 51:75 */       return new TypedValue((String)operandOne + (String)operandTwo);
/* 52:   */     }
/* 53:76 */     if ((operandOne instanceof String))
/* 54:   */     {
/* 55:77 */       StringBuilder result = new StringBuilder((String)operandOne);
/* 56:78 */       result.append(operandTwo == null ? "null" : operandTwo.toString());
/* 57:79 */       return new TypedValue(result.toString());
/* 58:   */     }
/* 59:80 */     if ((operandTwo instanceof String))
/* 60:   */     {
/* 61:81 */       StringBuilder result = new StringBuilder(operandOne == null ? "null" : operandOne.toString());
/* 62:82 */       result.append((String)operandTwo);
/* 63:83 */       return new TypedValue(result.toString());
/* 64:   */     }
/* 65:85 */     return state.operate(Operation.ADD, operandOne, operandTwo);
/* 66:   */   }
/* 67:   */   
/* 68:   */   public String toStringAST()
/* 69:   */   {
/* 70:91 */     if (this.children.length < 2) {
/* 71:92 */       return "+" + getLeftOperand().toStringAST();
/* 72:   */     }
/* 73:94 */     return super.toStringAST();
/* 74:   */   }
/* 75:   */   
/* 76:   */   public SpelNodeImpl getRightOperand()
/* 77:   */   {
/* 78:98 */     if (this.children.length < 2) {
/* 79:98 */       return null;
/* 80:   */     }
/* 81:99 */     return this.children[1];
/* 82:   */   }
/* 83:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.OpPlus
 * JD-Core Version:    0.7.0.1
 */