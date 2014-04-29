/*   1:    */ package org.springframework.expression.spel;
/*   2:    */ 
/*   3:    */ import org.springframework.expression.ParseException;
/*   4:    */ 
/*   5:    */ public class SpelParseException
/*   6:    */   extends ParseException
/*   7:    */ {
/*   8:    */   private SpelMessage message;
/*   9:    */   private Object[] inserts;
/*  10:    */   
/*  11:    */   public SpelParseException(String expressionString, int position, SpelMessage message, Object... inserts)
/*  12:    */   {
/*  13: 41 */     super(expressionString, position, message.formatMessage(position, inserts));
/*  14: 42 */     this.position = position;
/*  15: 43 */     this.message = message;
/*  16: 44 */     this.inserts = inserts;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public SpelParseException(int position, SpelMessage message, Object... inserts)
/*  20:    */   {
/*  21: 48 */     super(position, message.formatMessage(position, inserts));
/*  22: 49 */     this.position = position;
/*  23: 50 */     this.message = message;
/*  24: 51 */     this.inserts = inserts;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public SpelParseException(int position, Throwable cause, SpelMessage message, Object... inserts)
/*  28:    */   {
/*  29: 55 */     super(position, message.formatMessage(position, inserts), cause);
/*  30: 56 */     this.position = position;
/*  31: 57 */     this.message = message;
/*  32: 58 */     this.inserts = inserts;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public String getMessage()
/*  36:    */   {
/*  37: 87 */     if (this.message != null) {
/*  38: 88 */       return this.message.formatMessage(this.position, this.inserts);
/*  39:    */     }
/*  40: 90 */     return super.getMessage();
/*  41:    */   }
/*  42:    */   
/*  43:    */   public SpelMessage getMessageCode()
/*  44:    */   {
/*  45: 97 */     return this.message;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public Object[] getInserts()
/*  49:    */   {
/*  50:104 */     return this.inserts;
/*  51:    */   }
/*  52:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.SpelParseException
 * JD-Core Version:    0.7.0.1
 */