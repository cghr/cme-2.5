/*  1:   */ package org.springframework.validation.beanvalidation;
/*  2:   */ 
/*  3:   */ import javax.validation.MessageInterpolator;
/*  4:   */ import javax.validation.TraversableResolver;
/*  5:   */ import javax.validation.Validation;
/*  6:   */ import javax.validation.Validator;
/*  7:   */ import javax.validation.ValidatorContext;
/*  8:   */ import javax.validation.ValidatorFactory;
/*  9:   */ import org.springframework.beans.factory.InitializingBean;
/* 10:   */ 
/* 11:   */ public class CustomValidatorBean
/* 12:   */   extends SpringValidatorAdapter
/* 13:   */   implements Validator, InitializingBean
/* 14:   */ {
/* 15:   */   private ValidatorFactory validatorFactory;
/* 16:   */   private MessageInterpolator messageInterpolator;
/* 17:   */   private TraversableResolver traversableResolver;
/* 18:   */   
/* 19:   */   public void setValidatorFactory(ValidatorFactory validatorFactory)
/* 20:   */   {
/* 21:50 */     this.validatorFactory = validatorFactory;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void setMessageInterpolator(MessageInterpolator messageInterpolator)
/* 25:   */   {
/* 26:57 */     this.messageInterpolator = messageInterpolator;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void setTraversableResolver(TraversableResolver traversableResolver)
/* 30:   */   {
/* 31:64 */     this.traversableResolver = traversableResolver;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public void afterPropertiesSet()
/* 35:   */   {
/* 36:69 */     if (this.validatorFactory == null) {
/* 37:70 */       this.validatorFactory = Validation.buildDefaultValidatorFactory();
/* 38:   */     }
/* 39:73 */     ValidatorContext validatorContext = this.validatorFactory.usingContext();
/* 40:74 */     MessageInterpolator targetInterpolator = this.messageInterpolator;
/* 41:75 */     if (targetInterpolator == null) {
/* 42:76 */       targetInterpolator = this.validatorFactory.getMessageInterpolator();
/* 43:   */     }
/* 44:78 */     validatorContext.messageInterpolator(new LocaleContextMessageInterpolator(targetInterpolator));
/* 45:79 */     if (this.traversableResolver != null) {
/* 46:80 */       validatorContext.traversableResolver(this.traversableResolver);
/* 47:   */     }
/* 48:83 */     setTargetValidator(validatorContext.getValidator());
/* 49:   */   }
/* 50:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.beanvalidation.CustomValidatorBean
 * JD-Core Version:    0.7.0.1
 */