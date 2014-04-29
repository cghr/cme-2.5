/*   1:    */ package org.springframework.expression;
/*   2:    */ 
/*   3:    */ public class ExpressionException
/*   4:    */   extends RuntimeException
/*   5:    */ {
/*   6:    */   protected String expressionString;
/*   7:    */   protected int position;
/*   8:    */   
/*   9:    */   public ExpressionException(String expressionString, String message)
/*  10:    */   {
/*  11: 37 */     super(message);
/*  12: 38 */     this.position = -1;
/*  13: 39 */     this.expressionString = expressionString;
/*  14:    */   }
/*  15:    */   
/*  16:    */   public ExpressionException(String expressionString, int position, String message)
/*  17:    */   {
/*  18: 49 */     super(message);
/*  19: 50 */     this.position = position;
/*  20: 51 */     this.expressionString = expressionString;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public ExpressionException(int position, String message)
/*  24:    */   {
/*  25: 60 */     super(message);
/*  26: 61 */     this.position = position;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public ExpressionException(int position, String message, Throwable cause)
/*  30:    */   {
/*  31: 71 */     super(message, cause);
/*  32: 72 */     this.position = position;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public ExpressionException(String message)
/*  36:    */   {
/*  37: 80 */     super(message);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public ExpressionException(String message, Throwable cause)
/*  41:    */   {
/*  42: 84 */     super(message, cause);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public String toDetailedString()
/*  46:    */   {
/*  47: 88 */     StringBuilder output = new StringBuilder();
/*  48: 89 */     if (this.expressionString != null)
/*  49:    */     {
/*  50: 90 */       output.append("Expression '");
/*  51: 91 */       output.append(this.expressionString);
/*  52: 92 */       output.append("'");
/*  53: 93 */       if (this.position != -1)
/*  54:    */       {
/*  55: 94 */         output.append(" @ ");
/*  56: 95 */         output.append(this.position);
/*  57:    */       }
/*  58: 97 */       output.append(": ");
/*  59:    */     }
/*  60: 99 */     output.append(getMessage());
/*  61:100 */     return output.toString();
/*  62:    */   }
/*  63:    */   
/*  64:    */   public final String getExpressionString()
/*  65:    */   {
/*  66:104 */     return this.expressionString;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public final int getPosition()
/*  70:    */   {
/*  71:108 */     return this.position;
/*  72:    */   }
/*  73:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.ExpressionException
 * JD-Core Version:    0.7.0.1
 */