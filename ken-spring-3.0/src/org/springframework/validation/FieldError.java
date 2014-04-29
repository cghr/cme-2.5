/*   1:    */ package org.springframework.validation;
/*   2:    */ 
/*   3:    */ import org.springframework.util.Assert;
/*   4:    */ import org.springframework.util.ObjectUtils;
/*   5:    */ 
/*   6:    */ public class FieldError
/*   7:    */   extends ObjectError
/*   8:    */ {
/*   9:    */   private final String field;
/*  10:    */   private final Object rejectedValue;
/*  11:    */   private final boolean bindingFailure;
/*  12:    */   
/*  13:    */   public FieldError(String objectName, String field, String defaultMessage)
/*  14:    */   {
/*  15: 51 */     this(objectName, field, null, false, null, null, defaultMessage);
/*  16:    */   }
/*  17:    */   
/*  18:    */   public FieldError(String objectName, String field, Object rejectedValue, boolean bindingFailure, String[] codes, Object[] arguments, String defaultMessage)
/*  19:    */   {
/*  20: 69 */     super(objectName, codes, arguments, defaultMessage);
/*  21: 70 */     Assert.notNull(field, "Field must not be null");
/*  22: 71 */     this.field = field;
/*  23: 72 */     this.rejectedValue = rejectedValue;
/*  24: 73 */     this.bindingFailure = bindingFailure;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public String getField()
/*  28:    */   {
/*  29: 81 */     return this.field;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public Object getRejectedValue()
/*  33:    */   {
/*  34: 88 */     return this.rejectedValue;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public boolean isBindingFailure()
/*  38:    */   {
/*  39: 96 */     return this.bindingFailure;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public String toString()
/*  43:    */   {
/*  44:102 */     return 
/*  45:103 */       "Field error in object '" + getObjectName() + "' on field '" + this.field + "': rejected value [" + this.rejectedValue + "]; " + resolvableToString();
/*  46:    */   }
/*  47:    */   
/*  48:    */   public boolean equals(Object other)
/*  49:    */   {
/*  50:108 */     if (this == other) {
/*  51:109 */       return true;
/*  52:    */     }
/*  53:111 */     if (!super.equals(other)) {
/*  54:112 */       return false;
/*  55:    */     }
/*  56:114 */     FieldError otherError = (FieldError)other;
/*  57:    */     
/*  58:    */ 
/*  59:117 */     return (getField().equals(otherError.getField())) && (ObjectUtils.nullSafeEquals(getRejectedValue(), otherError.getRejectedValue())) && (isBindingFailure() == otherError.isBindingFailure());
/*  60:    */   }
/*  61:    */   
/*  62:    */   public int hashCode()
/*  63:    */   {
/*  64:122 */     int hashCode = super.hashCode();
/*  65:123 */     hashCode = 29 * hashCode + getField().hashCode();
/*  66:124 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(getRejectedValue());
/*  67:125 */     hashCode = 29 * hashCode + (isBindingFailure() ? 1 : 0);
/*  68:126 */     return hashCode;
/*  69:    */   }
/*  70:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.FieldError
 * JD-Core Version:    0.7.0.1
 */