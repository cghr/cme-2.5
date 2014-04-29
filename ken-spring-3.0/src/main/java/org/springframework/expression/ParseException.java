/*  1:   */ package org.springframework.expression;
/*  2:   */ 
/*  3:   */ public class ParseException
/*  4:   */   extends ExpressionException
/*  5:   */ {
/*  6:   */   public ParseException(String expressionString, int position, String message)
/*  7:   */   {
/*  8:34 */     super(expressionString, position, message);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public ParseException(int position, String message, Throwable cause)
/* 12:   */   {
/* 13:44 */     super(position, message, cause);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public ParseException(int position, String message)
/* 17:   */   {
/* 18:53 */     super(position, message);
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.ParseException
 * JD-Core Version:    0.7.0.1
 */