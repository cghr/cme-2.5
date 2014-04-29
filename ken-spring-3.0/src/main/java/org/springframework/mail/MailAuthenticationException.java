/*  1:   */ package org.springframework.mail;
/*  2:   */ 
/*  3:   */ public class MailAuthenticationException
/*  4:   */   extends MailException
/*  5:   */ {
/*  6:   */   public MailAuthenticationException(String msg)
/*  7:   */   {
/*  8:32 */     super(msg);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public MailAuthenticationException(String msg, Throwable cause)
/* 12:   */   {
/* 13:41 */     super(msg, cause);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public MailAuthenticationException(Throwable cause)
/* 17:   */   {
/* 18:49 */     super("Authentication failed", cause);
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.mail.MailAuthenticationException
 * JD-Core Version:    0.7.0.1
 */