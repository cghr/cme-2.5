/*  1:   */ package org.springframework.context.support;
/*  2:   */ 
/*  3:   */ import java.util.Locale;
/*  4:   */ import org.springframework.context.HierarchicalMessageSource;
/*  5:   */ import org.springframework.context.MessageSource;
/*  6:   */ import org.springframework.context.MessageSourceResolvable;
/*  7:   */ import org.springframework.context.NoSuchMessageException;
/*  8:   */ 
/*  9:   */ public class DelegatingMessageSource
/* 10:   */   extends MessageSourceSupport
/* 11:   */   implements HierarchicalMessageSource
/* 12:   */ {
/* 13:   */   private MessageSource parentMessageSource;
/* 14:   */   
/* 15:   */   public void setParentMessageSource(MessageSource parent)
/* 16:   */   {
/* 17:43 */     this.parentMessageSource = parent;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public MessageSource getParentMessageSource()
/* 21:   */   {
/* 22:47 */     return this.parentMessageSource;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public String getMessage(String code, Object[] args, String defaultMessage, Locale locale)
/* 26:   */   {
/* 27:52 */     if (this.parentMessageSource != null) {
/* 28:53 */       return this.parentMessageSource.getMessage(code, args, defaultMessage, locale);
/* 29:   */     }
/* 30:56 */     return renderDefaultMessage(defaultMessage, args, locale);
/* 31:   */   }
/* 32:   */   
/* 33:   */   public String getMessage(String code, Object[] args, Locale locale)
/* 34:   */     throws NoSuchMessageException
/* 35:   */   {
/* 36:61 */     if (this.parentMessageSource != null) {
/* 37:62 */       return this.parentMessageSource.getMessage(code, args, locale);
/* 38:   */     }
/* 39:65 */     throw new NoSuchMessageException(code, locale);
/* 40:   */   }
/* 41:   */   
/* 42:   */   public String getMessage(MessageSourceResolvable resolvable, Locale locale)
/* 43:   */     throws NoSuchMessageException
/* 44:   */   {
/* 45:70 */     if (this.parentMessageSource != null) {
/* 46:71 */       return this.parentMessageSource.getMessage(resolvable, locale);
/* 47:   */     }
/* 48:74 */     if (resolvable.getDefaultMessage() != null) {
/* 49:75 */       return renderDefaultMessage(resolvable.getDefaultMessage(), resolvable.getArguments(), locale);
/* 50:   */     }
/* 51:77 */     String[] codes = resolvable.getCodes();
/* 52:78 */     String code = (codes != null) && (codes.length > 0) ? codes[0] : null;
/* 53:79 */     throw new NoSuchMessageException(code, locale);
/* 54:   */   }
/* 55:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.DelegatingMessageSource
 * JD-Core Version:    0.7.0.1
 */