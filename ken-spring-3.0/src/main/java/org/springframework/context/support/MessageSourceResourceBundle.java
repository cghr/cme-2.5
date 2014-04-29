/*  1:   */ package org.springframework.context.support;
/*  2:   */ 
/*  3:   */ import java.util.Enumeration;
/*  4:   */ import java.util.Locale;
/*  5:   */ import java.util.ResourceBundle;
/*  6:   */ import org.springframework.context.MessageSource;
/*  7:   */ import org.springframework.context.NoSuchMessageException;
/*  8:   */ import org.springframework.util.Assert;
/*  9:   */ 
/* 10:   */ public class MessageSourceResourceBundle
/* 11:   */   extends ResourceBundle
/* 12:   */ {
/* 13:   */   private final MessageSource messageSource;
/* 14:   */   private final Locale locale;
/* 15:   */   
/* 16:   */   public MessageSourceResourceBundle(MessageSource source, Locale locale)
/* 17:   */   {
/* 18:51 */     Assert.notNull(source, "MessageSource must not be null");
/* 19:52 */     this.messageSource = source;
/* 20:53 */     this.locale = locale;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public MessageSourceResourceBundle(MessageSource source, Locale locale, ResourceBundle parent)
/* 24:   */   {
/* 25:63 */     this(source, locale);
/* 26:64 */     setParent(parent);
/* 27:   */   }
/* 28:   */   
/* 29:   */   protected Object handleGetObject(String code)
/* 30:   */   {
/* 31:   */     try
/* 32:   */     {
/* 33:75 */       return this.messageSource.getMessage(code, null, this.locale);
/* 34:   */     }
/* 35:   */     catch (NoSuchMessageException localNoSuchMessageException) {}
/* 36:78 */     return null;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public Enumeration<String> getKeys()
/* 40:   */   {
/* 41:88 */     return null;
/* 42:   */   }
/* 43:   */   
/* 44:   */   public Locale getLocale()
/* 45:   */   {
/* 46:97 */     return this.locale;
/* 47:   */   }
/* 48:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.MessageSourceResourceBundle
 * JD-Core Version:    0.7.0.1
 */