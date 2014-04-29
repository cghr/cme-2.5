/*   1:    */ package org.springframework.validation;
/*   2:    */ 
/*   3:    */ import org.apache.commons.logging.Log;
/*   4:    */ import org.apache.commons.logging.LogFactory;
/*   5:    */ import org.springframework.util.Assert;
/*   6:    */ import org.springframework.util.StringUtils;
/*   7:    */ 
/*   8:    */ public abstract class ValidationUtils
/*   9:    */ {
/*  10: 40 */   private static Log logger = LogFactory.getLog(ValidationUtils.class);
/*  11:    */   
/*  12:    */   public static void invokeValidator(Validator validator, Object obj, Errors errors)
/*  13:    */   {
/*  14: 54 */     Assert.notNull(validator, "Validator must not be null");
/*  15: 55 */     Assert.notNull(errors, "Errors object must not be null");
/*  16: 56 */     if (logger.isDebugEnabled()) {
/*  17: 57 */       logger.debug("Invoking validator [" + validator + "]");
/*  18:    */     }
/*  19: 59 */     if ((obj != null) && (!validator.supports(obj.getClass()))) {
/*  20: 60 */       throw new IllegalArgumentException(
/*  21: 61 */         "Validator [" + validator.getClass() + "] does not support [" + obj.getClass() + "]");
/*  22:    */     }
/*  23: 63 */     validator.validate(obj, errors);
/*  24: 64 */     if (logger.isDebugEnabled()) {
/*  25: 65 */       if (errors.hasErrors()) {
/*  26: 66 */         logger.debug("Validator found " + errors.getErrorCount() + " errors");
/*  27:    */       } else {
/*  28: 69 */         logger.debug("Validator found no errors");
/*  29:    */       }
/*  30:    */     }
/*  31:    */   }
/*  32:    */   
/*  33:    */   public static void rejectIfEmpty(Errors errors, String field, String errorCode)
/*  34:    */   {
/*  35: 87 */     rejectIfEmpty(errors, field, errorCode, null, null);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public static void rejectIfEmpty(Errors errors, String field, String errorCode, String defaultMessage)
/*  39:    */   {
/*  40:104 */     rejectIfEmpty(errors, field, errorCode, null, defaultMessage);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public static void rejectIfEmpty(Errors errors, String field, String errorCode, Object[] errorArgs)
/*  44:    */   {
/*  45:122 */     rejectIfEmpty(errors, field, errorCode, errorArgs, null);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public static void rejectIfEmpty(Errors errors, String field, String errorCode, Object[] errorArgs, String defaultMessage)
/*  49:    */   {
/*  50:143 */     Assert.notNull(errors, "Errors object must not be null");
/*  51:144 */     Object value = errors.getFieldValue(field);
/*  52:145 */     if ((value == null) || (!StringUtils.hasLength(value.toString()))) {
/*  53:146 */       errors.rejectValue(field, errorCode, errorArgs, defaultMessage);
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   public static void rejectIfEmptyOrWhitespace(Errors errors, String field, String errorCode)
/*  58:    */   {
/*  59:163 */     rejectIfEmptyOrWhitespace(errors, field, errorCode, null, null);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public static void rejectIfEmptyOrWhitespace(Errors errors, String field, String errorCode, String defaultMessage)
/*  63:    */   {
/*  64:182 */     rejectIfEmptyOrWhitespace(errors, field, errorCode, null, defaultMessage);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public static void rejectIfEmptyOrWhitespace(Errors errors, String field, String errorCode, Object[] errorArgs)
/*  68:    */   {
/*  69:202 */     rejectIfEmptyOrWhitespace(errors, field, errorCode, errorArgs, null);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public static void rejectIfEmptyOrWhitespace(Errors errors, String field, String errorCode, Object[] errorArgs, String defaultMessage)
/*  73:    */   {
/*  74:223 */     Assert.notNull(errors, "Errors object must not be null");
/*  75:224 */     Object value = errors.getFieldValue(field);
/*  76:225 */     if ((value == null) || (!StringUtils.hasText(value.toString()))) {
/*  77:226 */       errors.rejectValue(field, errorCode, errorArgs, defaultMessage);
/*  78:    */     }
/*  79:    */   }
/*  80:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.ValidationUtils
 * JD-Core Version:    0.7.0.1
 */