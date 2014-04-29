/*  1:   */ package org.springframework.validation.beanvalidation;
/*  2:   */ 
/*  3:   */ import java.util.Locale;
/*  4:   */ import javax.validation.MessageInterpolator;
/*  5:   */ import javax.validation.MessageInterpolator.Context;
/*  6:   */ import org.springframework.context.i18n.LocaleContextHolder;
/*  7:   */ import org.springframework.util.Assert;
/*  8:   */ 
/*  9:   */ public class LocaleContextMessageInterpolator
/* 10:   */   implements MessageInterpolator
/* 11:   */ {
/* 12:   */   private final MessageInterpolator targetInterpolator;
/* 13:   */   
/* 14:   */   public LocaleContextMessageInterpolator(MessageInterpolator targetInterpolator)
/* 15:   */   {
/* 16:43 */     Assert.notNull(targetInterpolator, "Target MessageInterpolator must not be null");
/* 17:44 */     this.targetInterpolator = targetInterpolator;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public String interpolate(String message, MessageInterpolator.Context context)
/* 21:   */   {
/* 22:49 */     return this.targetInterpolator.interpolate(message, context, LocaleContextHolder.getLocale());
/* 23:   */   }
/* 24:   */   
/* 25:   */   public String interpolate(String message, MessageInterpolator.Context context, Locale locale)
/* 26:   */   {
/* 27:53 */     return this.targetInterpolator.interpolate(message, context, locale);
/* 28:   */   }
/* 29:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.beanvalidation.LocaleContextMessageInterpolator
 * JD-Core Version:    0.7.0.1
 */