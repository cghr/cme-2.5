/*  1:   */ package org.springframework.expression.spel;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.EvaluationException;
/*  4:   */ 
/*  5:   */ public class SpelEvaluationException
/*  6:   */   extends EvaluationException
/*  7:   */ {
/*  8:   */   private SpelMessage message;
/*  9:   */   private Object[] inserts;
/* 10:   */   
/* 11:   */   public SpelEvaluationException(SpelMessage message, Object... inserts)
/* 12:   */   {
/* 13:34 */     super(message.formatMessage(0, inserts));
/* 14:35 */     this.message = message;
/* 15:36 */     this.inserts = inserts;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public SpelEvaluationException(int position, SpelMessage message, Object... inserts)
/* 19:   */   {
/* 20:40 */     super(position, message.formatMessage(position, inserts));
/* 21:41 */     this.message = message;
/* 22:42 */     this.inserts = inserts;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public SpelEvaluationException(int position, Throwable cause, SpelMessage message, Object... inserts)
/* 26:   */   {
/* 27:47 */     super(position, message.formatMessage(position, inserts), cause);
/* 28:48 */     this.message = message;
/* 29:49 */     this.inserts = inserts;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public SpelEvaluationException(Throwable cause, SpelMessage message, Object... inserts)
/* 33:   */   {
/* 34:53 */     super(message.formatMessage(0, inserts), cause);
/* 35:54 */     this.message = message;
/* 36:55 */     this.inserts = inserts;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public String getMessage()
/* 40:   */   {
/* 41:63 */     if (this.message != null) {
/* 42:64 */       return this.message.formatMessage(this.position, this.inserts);
/* 43:   */     }
/* 44:66 */     return super.getMessage();
/* 45:   */   }
/* 46:   */   
/* 47:   */   public SpelMessage getMessageCode()
/* 48:   */   {
/* 49:73 */     return this.message;
/* 50:   */   }
/* 51:   */   
/* 52:   */   public void setPosition(int position)
/* 53:   */   {
/* 54:82 */     this.position = position;
/* 55:   */   }
/* 56:   */   
/* 57:   */   public Object[] getInserts()
/* 58:   */   {
/* 59:89 */     return this.inserts;
/* 60:   */   }
/* 61:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.SpelEvaluationException
 * JD-Core Version:    0.7.0.1
 */