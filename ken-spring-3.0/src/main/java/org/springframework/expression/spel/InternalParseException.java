/*  1:   */ package org.springframework.expression.spel;
/*  2:   */ 
/*  3:   */ public class InternalParseException
/*  4:   */   extends RuntimeException
/*  5:   */ {
/*  6:   */   public InternalParseException(SpelParseException cause)
/*  7:   */   {
/*  8:31 */     super(cause);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public SpelParseException getCause()
/* 12:   */   {
/* 13:35 */     return (SpelParseException)super.getCause();
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.InternalParseException
 * JD-Core Version:    0.7.0.1
 */