/*   1:    */ package org.springframework.validation.beanvalidation;
/*   2:    */ 
/*   3:    */ import java.lang.annotation.Annotation;
/*   4:    */ import java.util.HashSet;
/*   5:    */ import java.util.LinkedList;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Map.Entry;
/*   9:    */ import java.util.Set;
/*  10:    */ import java.util.TreeMap;
/*  11:    */ import javax.validation.ConstraintViolation;
/*  12:    */ import javax.validation.metadata.BeanDescriptor;
/*  13:    */ import javax.validation.metadata.ConstraintDescriptor;
/*  14:    */ import org.springframework.beans.NotReadablePropertyException;
/*  15:    */ import org.springframework.context.support.DefaultMessageSourceResolvable;
/*  16:    */ import org.springframework.util.Assert;
/*  17:    */ import org.springframework.validation.Errors;
/*  18:    */ import org.springframework.validation.FieldError;
/*  19:    */ 
/*  20:    */ public class SpringValidatorAdapter
/*  21:    */   implements org.springframework.validation.Validator, javax.validation.Validator
/*  22:    */ {
/*  23: 49 */   private static final Set<String> internalAnnotationAttributes = new HashSet(3);
/*  24:    */   private javax.validation.Validator targetValidator;
/*  25:    */   
/*  26:    */   static
/*  27:    */   {
/*  28: 52 */     internalAnnotationAttributes.add("message");
/*  29: 53 */     internalAnnotationAttributes.add("groups");
/*  30: 54 */     internalAnnotationAttributes.add("payload");
/*  31:    */   }
/*  32:    */   
/*  33:    */   public SpringValidatorAdapter(javax.validation.Validator targetValidator)
/*  34:    */   {
/*  35: 65 */     Assert.notNull(targetValidator, "Target Validator must not be null");
/*  36: 66 */     this.targetValidator = targetValidator;
/*  37:    */   }
/*  38:    */   
/*  39:    */   SpringValidatorAdapter() {}
/*  40:    */   
/*  41:    */   void setTargetValidator(javax.validation.Validator targetValidator)
/*  42:    */   {
/*  43: 73 */     this.targetValidator = targetValidator;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public boolean supports(Class<?> clazz)
/*  47:    */   {
/*  48: 82 */     return true;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void validate(Object target, Errors errors)
/*  52:    */   {
/*  53: 86 */     Set<ConstraintViolation<Object>> result = this.targetValidator.validate(target, new Class[0]);
/*  54: 87 */     for (ConstraintViolation<Object> violation : result)
/*  55:    */     {
/*  56: 88 */       String field = violation.getPropertyPath().toString();
/*  57: 89 */       FieldError fieldError = errors.getFieldError(field);
/*  58: 90 */       if ((fieldError == null) || (!fieldError.isBindingFailure())) {
/*  59:    */         try
/*  60:    */         {
/*  61: 92 */           errors.rejectValue(field, 
/*  62: 93 */             ((Annotation)violation.getConstraintDescriptor().getAnnotation()).annotationType().getSimpleName(), 
/*  63: 94 */             getArgumentsForConstraint(errors.getObjectName(), field, violation.getConstraintDescriptor()), 
/*  64: 95 */             violation.getMessage());
/*  65:    */         }
/*  66:    */         catch (NotReadablePropertyException ex)
/*  67:    */         {
/*  68: 98 */           throw new IllegalStateException("JSR-303 validated property '" + field + 
/*  69: 99 */             "' does not have a corresponding accessor for Spring data binding - " + 
/*  70:100 */             "check your DataBinder's configuration (bean property versus direct field access)", ex);
/*  71:    */         }
/*  72:    */       }
/*  73:    */     }
/*  74:    */   }
/*  75:    */   
/*  76:    */   protected Object[] getArgumentsForConstraint(String objectName, String field, ConstraintDescriptor<?> descriptor)
/*  77:    */   {
/*  78:123 */     List<Object> arguments = new LinkedList();
/*  79:124 */     String[] codes = { objectName + "." + field, field };
/*  80:125 */     arguments.add(new DefaultMessageSourceResolvable(codes, field));
/*  81:    */     
/*  82:127 */     Map<String, Object> attributesToExpose = new TreeMap();
/*  83:128 */     for (Map.Entry<String, Object> entry : descriptor.getAttributes().entrySet())
/*  84:    */     {
/*  85:129 */       String attributeName = (String)entry.getKey();
/*  86:130 */       Object attributeValue = entry.getValue();
/*  87:131 */       if (!internalAnnotationAttributes.contains(attributeName)) {
/*  88:132 */         attributesToExpose.put(attributeName, attributeValue);
/*  89:    */       }
/*  90:    */     }
/*  91:135 */     arguments.addAll(attributesToExpose.values());
/*  92:136 */     return arguments.toArray(new Object[arguments.size()]);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups)
/*  96:    */   {
/*  97:145 */     return this.targetValidator.validate(object, groups);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName, Class<?>... groups)
/* 101:    */   {
/* 102:149 */     return this.targetValidator.validateProperty(object, propertyName, groups);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName, Object value, Class<?>... groups)
/* 106:    */   {
/* 107:155 */     return this.targetValidator.validateValue(beanType, propertyName, value, groups);
/* 108:    */   }
/* 109:    */   
/* 110:    */   public BeanDescriptor getConstraintsForClass(Class<?> clazz)
/* 111:    */   {
/* 112:159 */     return this.targetValidator.getConstraintsForClass(clazz);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public <T> T unwrap(Class<T> type)
/* 116:    */   {
/* 117:163 */     return this.targetValidator.unwrap(type);
/* 118:    */   }
/* 119:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.beanvalidation.SpringValidatorAdapter
 * JD-Core Version:    0.7.0.1
 */