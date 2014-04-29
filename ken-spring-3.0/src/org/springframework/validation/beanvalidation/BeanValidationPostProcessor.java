/*   1:    */ package org.springframework.validation.beanvalidation;
/*   2:    */ 
/*   3:    */ import java.util.Iterator;
/*   4:    */ import java.util.Set;
/*   5:    */ import javax.validation.ConstraintViolation;
/*   6:    */ import javax.validation.Validation;
/*   7:    */ import javax.validation.Validator;
/*   8:    */ import javax.validation.ValidatorFactory;
/*   9:    */ import org.springframework.beans.BeansException;
/*  10:    */ import org.springframework.beans.factory.BeanInitializationException;
/*  11:    */ import org.springframework.beans.factory.InitializingBean;
/*  12:    */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*  13:    */ 
/*  14:    */ public class BeanValidationPostProcessor
/*  15:    */   implements BeanPostProcessor, InitializingBean
/*  16:    */ {
/*  17:    */   private Validator validator;
/*  18: 43 */   private boolean afterInitialization = false;
/*  19:    */   
/*  20:    */   public void setValidator(Validator validator)
/*  21:    */   {
/*  22: 51 */     this.validator = validator;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void setValidatorFactory(ValidatorFactory validatorFactory)
/*  26:    */   {
/*  27: 61 */     this.validator = validatorFactory.getValidator();
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setAfterInitialization(boolean afterInitialization)
/*  31:    */   {
/*  32: 72 */     this.afterInitialization = afterInitialization;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void afterPropertiesSet()
/*  36:    */   {
/*  37: 76 */     if (this.validator == null) {
/*  38: 77 */       this.validator = Validation.buildDefaultValidatorFactory().getValidator();
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   public Object postProcessBeforeInitialization(Object bean, String beanName)
/*  43:    */     throws BeansException
/*  44:    */   {
/*  45: 83 */     if (!this.afterInitialization) {
/*  46: 84 */       doValidate(bean);
/*  47:    */     }
/*  48: 86 */     return bean;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public Object postProcessAfterInitialization(Object bean, String beanName)
/*  52:    */     throws BeansException
/*  53:    */   {
/*  54: 90 */     if (this.afterInitialization) {
/*  55: 91 */       doValidate(bean);
/*  56:    */     }
/*  57: 93 */     return bean;
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected void doValidate(Object bean)
/*  61:    */   {
/*  62:103 */     Set<ConstraintViolation<Object>> result = this.validator.validate(bean, new Class[0]);
/*  63:104 */     if (!result.isEmpty())
/*  64:    */     {
/*  65:105 */       StringBuilder sb = new StringBuilder("Bean state is invalid: ");
/*  66:106 */       for (Iterator<ConstraintViolation<Object>> it = result.iterator(); it.hasNext();)
/*  67:    */       {
/*  68:107 */         ConstraintViolation<Object> violation = (ConstraintViolation)it.next();
/*  69:108 */         sb.append(violation.getPropertyPath()).append(" - ").append(violation.getMessage());
/*  70:109 */         if (it.hasNext()) {
/*  71:110 */           sb.append("; ");
/*  72:    */         }
/*  73:    */       }
/*  74:113 */       throw new BeanInitializationException(sb.toString());
/*  75:    */     }
/*  76:    */   }
/*  77:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.beanvalidation.BeanValidationPostProcessor
 * JD-Core Version:    0.7.0.1
 */