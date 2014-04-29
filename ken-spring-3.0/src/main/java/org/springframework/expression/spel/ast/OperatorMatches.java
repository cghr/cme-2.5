/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import java.util.regex.Matcher;
/*  4:   */ import java.util.regex.Pattern;
/*  5:   */ import java.util.regex.PatternSyntaxException;
/*  6:   */ import org.springframework.expression.EvaluationException;
/*  7:   */ import org.springframework.expression.TypedValue;
/*  8:   */ import org.springframework.expression.spel.ExpressionState;
/*  9:   */ import org.springframework.expression.spel.SpelEvaluationException;
/* 10:   */ import org.springframework.expression.spel.SpelMessage;
/* 11:   */ import org.springframework.expression.spel.support.BooleanTypedValue;
/* 12:   */ 
/* 13:   */ public class OperatorMatches
/* 14:   */   extends Operator
/* 15:   */ {
/* 16:   */   public OperatorMatches(int pos, SpelNodeImpl... operands)
/* 17:   */   {
/* 18:39 */     super("matches", pos, operands);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public BooleanTypedValue getValueInternal(ExpressionState state)
/* 22:   */     throws EvaluationException
/* 23:   */   {
/* 24:50 */     SpelNodeImpl leftOp = getLeftOperand();
/* 25:51 */     SpelNodeImpl rightOp = getRightOperand();
/* 26:52 */     Object left = leftOp.getValue(state, String.class);
/* 27:53 */     Object right = getRightOperand().getValueInternal(state).getValue();
/* 28:   */     try
/* 29:   */     {
/* 30:55 */       if (!(left instanceof String)) {
/* 31:56 */         throw new SpelEvaluationException(leftOp.getStartPosition(), 
/* 32:57 */           SpelMessage.INVALID_FIRST_OPERAND_FOR_MATCHES_OPERATOR, new Object[] { left });
/* 33:   */       }
/* 34:59 */       if (!(right instanceof String)) {
/* 35:60 */         throw new SpelEvaluationException(rightOp.getStartPosition(), 
/* 36:61 */           SpelMessage.INVALID_SECOND_OPERAND_FOR_MATCHES_OPERATOR, new Object[] { right });
/* 37:   */       }
/* 38:63 */       Pattern pattern = Pattern.compile((String)right);
/* 39:64 */       Matcher matcher = pattern.matcher((String)left);
/* 40:65 */       return BooleanTypedValue.forValue(matcher.matches());
/* 41:   */     }
/* 42:   */     catch (PatternSyntaxException pse)
/* 43:   */     {
/* 44:68 */       throw new SpelEvaluationException(rightOp.getStartPosition(), pse, SpelMessage.INVALID_PATTERN, new Object[] { right });
/* 45:   */     }
/* 46:   */   }
/* 47:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.OperatorMatches
 * JD-Core Version:    0.7.0.1
 */