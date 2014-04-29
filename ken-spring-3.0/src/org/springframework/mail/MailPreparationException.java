/*  1:   */ package org.springframework.mail;
/*  2:   */ 
/*  3:   */ public class MailPreparationException
/*  4:   */   extends MailException
/*  5:   */ {
/*  6:   */   public MailPreparationException(String msg)
/*  7:   */   {
/*  8:35 */     super(msg);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public MailPreparationException(String msg, Throwable cause)
/* 12:   */   {
/* 13:44 */     super(msg, cause);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public MailPreparationException(Throwable cause)
/* 17:   */   {
/* 18:48 */     super("Could not prepare mail", cause);
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.mail.MailPreparationException
 * JD-Core Version:    0.7.0.1
 */