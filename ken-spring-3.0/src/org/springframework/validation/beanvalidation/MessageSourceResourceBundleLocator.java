/*  1:   */ package org.springframework.validation.beanvalidation;
/*  2:   */ 
/*  3:   */ import java.util.Locale;
/*  4:   */ import java.util.ResourceBundle;
/*  5:   */ import org.hibernate.validator.resourceloading.ResourceBundleLocator;
/*  6:   */ import org.springframework.context.MessageSource;
/*  7:   */ import org.springframework.context.support.MessageSourceResourceBundle;
/*  8:   */ import org.springframework.util.Assert;
/*  9:   */ 
/* 10:   */ public class MessageSourceResourceBundleLocator
/* 11:   */   implements ResourceBundleLocator
/* 12:   */ {
/* 13:   */   private final MessageSource messageSource;
/* 14:   */   
/* 15:   */   public MessageSourceResourceBundleLocator(MessageSource messageSource)
/* 16:   */   {
/* 17:47 */     Assert.notNull(messageSource, "MessageSource must not be null");
/* 18:48 */     this.messageSource = messageSource;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public ResourceBundle getResourceBundle(Locale locale)
/* 22:   */   {
/* 23:52 */     return new MessageSourceResourceBundle(this.messageSource, locale);
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator
 * JD-Core Version:    0.7.0.1
 */