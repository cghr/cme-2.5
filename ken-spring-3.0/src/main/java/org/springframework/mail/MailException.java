/*  1:   */ package org.springframework.mail;
/*  2:   */ 
/*  3:   */ import org.springframework.core.NestedRuntimeException;
/*  4:   */ 
/*  5:   */ public abstract class MailException
/*  6:   */   extends NestedRuntimeException
/*  7:   */ {
/*  8:   */   public MailException(String msg)
/*  9:   */   {
/* 10:33 */     super(msg);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public MailException(String msg, Throwable cause)
/* 14:   */   {
/* 15:42 */     super(msg, cause);
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.mail.MailException
 * JD-Core Version:    0.7.0.1
 */