/*  1:   */ package org.springframework.validation;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.PropertyAccessException;
/*  4:   */ import org.springframework.context.support.DefaultMessageSourceResolvable;
/*  5:   */ import org.springframework.util.ObjectUtils;
/*  6:   */ import org.springframework.util.StringUtils;
/*  7:   */ 
/*  8:   */ public class DefaultBindingErrorProcessor
/*  9:   */   implements BindingErrorProcessor
/* 10:   */ {
/* 11:   */   public static final String MISSING_FIELD_ERROR_CODE = "required";
/* 12:   */   
/* 13:   */   public void processMissingFieldError(String missingField, BindingResult bindingResult)
/* 14:   */   {
/* 15:57 */     String fixedField = bindingResult.getNestedPath() + missingField;
/* 16:58 */     String[] codes = bindingResult.resolveMessageCodes("required", missingField);
/* 17:59 */     Object[] arguments = getArgumentsForBindError(bindingResult.getObjectName(), fixedField);
/* 18:60 */     bindingResult.addError(new FieldError(
/* 19:61 */       bindingResult.getObjectName(), fixedField, "", true, 
/* 20:62 */       codes, arguments, "Field '" + fixedField + "' is required"));
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void processPropertyAccessException(PropertyAccessException ex, BindingResult bindingResult)
/* 24:   */   {
/* 25:67 */     String field = ex.getPropertyName();
/* 26:68 */     String[] codes = bindingResult.resolveMessageCodes(ex.getErrorCode(), field);
/* 27:69 */     Object[] arguments = getArgumentsForBindError(bindingResult.getObjectName(), field);
/* 28:70 */     Object rejectedValue = ex.getValue();
/* 29:71 */     if ((rejectedValue != null) && (rejectedValue.getClass().isArray())) {
/* 30:72 */       rejectedValue = StringUtils.arrayToCommaDelimitedString(ObjectUtils.toObjectArray(rejectedValue));
/* 31:   */     }
/* 32:74 */     bindingResult.addError(new FieldError(
/* 33:75 */       bindingResult.getObjectName(), field, rejectedValue, true, 
/* 34:76 */       codes, arguments, ex.getLocalizedMessage()));
/* 35:   */   }
/* 36:   */   
/* 37:   */   protected Object[] getArgumentsForBindError(String objectName, String field)
/* 38:   */   {
/* 39:91 */     String[] codes = { objectName + "." + field, field };
/* 40:92 */     return new Object[] { new DefaultMessageSourceResolvable(codes, field) };
/* 41:   */   }
/* 42:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.DefaultBindingErrorProcessor
 * JD-Core Version:    0.7.0.1
 */