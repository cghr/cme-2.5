/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.EvaluationException;
/*  4:   */ import org.springframework.expression.Operation;
/*  5:   */ import org.springframework.expression.TypedValue;
/*  6:   */ import org.springframework.expression.spel.ExpressionState;
/*  7:   */ 
/*  8:   */ public class OpMinus
/*  9:   */   extends Operator
/* 10:   */ {
/* 11:   */   public OpMinus(int pos, SpelNodeImpl... operands)
/* 12:   */   {
/* 13:42 */     super("-", pos, operands);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public TypedValue getValueInternal(ExpressionState state)
/* 17:   */     throws EvaluationException
/* 18:   */   {
/* 19:47 */     SpelNodeImpl leftOp = getLeftOperand();
/* 20:48 */     SpelNodeImpl rightOp = getRightOperand();
/* 21:49 */     if (rightOp == null)
/* 22:   */     {
/* 23:50 */       Object operand = leftOp.getValueInternal(state).getValue();
/* 24:51 */       if ((operand instanceof Number))
/* 25:   */       {
/* 26:52 */         Number n = (Number)operand;
/* 27:53 */         if ((operand instanceof Double)) {
/* 28:54 */           return new TypedValue(Double.valueOf(0.0D - n.doubleValue()));
/* 29:   */         }
/* 30:55 */         if ((operand instanceof Long)) {
/* 31:56 */           return new TypedValue(Long.valueOf(0L - n.longValue()));
/* 32:   */         }
/* 33:58 */         return new TypedValue(Integer.valueOf(0 - n.intValue()));
/* 34:   */       }
/* 35:61 */       return state.operate(Operation.SUBTRACT, operand, null);
/* 36:   */     }
/* 37:63 */     Object left = leftOp.getValueInternal(state).getValue();
/* 38:64 */     Object right = rightOp.getValueInternal(state).getValue();
/* 39:65 */     if (((left instanceof Number)) && ((right instanceof Number)))
/* 40:   */     {
/* 41:66 */       Number op1 = (Number)left;
/* 42:67 */       Number op2 = (Number)right;
/* 43:68 */       if (((op1 instanceof Double)) || ((op2 instanceof Double))) {
/* 44:69 */         return new TypedValue(Double.valueOf(op1.doubleValue() - op2.doubleValue()));
/* 45:   */       }
/* 46:70 */       if (((op1 instanceof Long)) || ((op2 instanceof Long))) {
/* 47:71 */         return new TypedValue(Long.valueOf(op1.longValue() - op2.longValue()));
/* 48:   */       }
/* 49:73 */       return new TypedValue(Integer.valueOf(op1.intValue() - op2.intValue()));
/* 50:   */     }
/* 51:75 */     if (((left instanceof String)) && ((right instanceof Integer)) && (((String)left).length() == 1))
/* 52:   */     {
/* 53:76 */       String theString = (String)left;
/* 54:77 */       Integer theInteger = (Integer)right;
/* 55:   */       
/* 56:79 */       return new TypedValue(Character.toString((char)(theString.charAt(0) - theInteger.intValue())));
/* 57:   */     }
/* 58:81 */     return state.operate(Operation.SUBTRACT, left, right);
/* 59:   */   }
/* 60:   */   
/* 61:   */   public String toStringAST()
/* 62:   */   {
/* 63:87 */     if (getRightOperand() == null) {
/* 64:88 */       return "-" + getLeftOperand().toStringAST();
/* 65:   */     }
/* 66:90 */     return super.toStringAST();
/* 67:   */   }
/* 68:   */   
/* 69:   */   public SpelNodeImpl getRightOperand()
/* 70:   */   {
/* 71:93 */     if (this.children.length < 2) {
/* 72:93 */       return null;
/* 73:   */     }
/* 74:94 */     return this.children[1];
/* 75:   */   }
/* 76:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.OpMinus
 * JD-Core Version:    0.7.0.1
 */