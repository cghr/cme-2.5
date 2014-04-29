/*  1:   */ package org.springframework.validation.beanvalidation;
/*  2:   */ 
/*  3:   */ import javax.validation.ConstraintValidator;
/*  4:   */ import javax.validation.ConstraintValidatorFactory;
/*  5:   */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*  6:   */ import org.springframework.util.Assert;
/*  7:   */ 
/*  8:   */ public class SpringConstraintValidatorFactory
/*  9:   */   implements ConstraintValidatorFactory
/* 10:   */ {
/* 11:   */   private final AutowireCapableBeanFactory beanFactory;
/* 12:   */   
/* 13:   */   public SpringConstraintValidatorFactory(AutowireCapableBeanFactory beanFactory)
/* 14:   */   {
/* 15:44 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/* 16:45 */     this.beanFactory = beanFactory;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key)
/* 20:   */   {
/* 21:50 */     return (ConstraintValidator)this.beanFactory.createBean(key);
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory
 * JD-Core Version:    0.7.0.1
 */