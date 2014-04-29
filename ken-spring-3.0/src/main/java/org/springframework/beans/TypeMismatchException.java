/*   1:    */ package org.springframework.beans;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyChangeEvent;
/*   4:    */ import org.springframework.util.ClassUtils;
/*   5:    */ 
/*   6:    */ public class TypeMismatchException
/*   7:    */   extends PropertyAccessException
/*   8:    */ {
/*   9:    */   public static final String ERROR_CODE = "typeMismatch";
/*  10:    */   private transient Object value;
/*  11:    */   private Class requiredType;
/*  12:    */   
/*  13:    */   public TypeMismatchException(PropertyChangeEvent propertyChangeEvent, Class requiredType)
/*  14:    */   {
/*  15: 48 */     this(propertyChangeEvent, requiredType, null);
/*  16:    */   }
/*  17:    */   
/*  18:    */   public TypeMismatchException(PropertyChangeEvent propertyChangeEvent, Class requiredType, Throwable cause)
/*  19:    */   {
/*  20: 65 */     super(propertyChangeEvent, "Failed to convert property value of type '" + ClassUtils.getDescriptiveType(propertyChangeEvent.getNewValue()) + "'" + (requiredType != null ? " to required type '" + ClassUtils.getQualifiedName(requiredType) + "'" : "") + (propertyChangeEvent.getPropertyName() != null ? " for property '" + propertyChangeEvent.getPropertyName() + "'" : ""), cause);
/*  21: 66 */     this.value = propertyChangeEvent.getNewValue();
/*  22: 67 */     this.requiredType = requiredType;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public TypeMismatchException(Object value, Class requiredType)
/*  26:    */   {
/*  27: 76 */     this(value, requiredType, null);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public TypeMismatchException(Object value, Class requiredType, Throwable cause)
/*  31:    */   {
/*  32: 88 */     super("Failed to convert value of type '" + ClassUtils.getDescriptiveType(value) + "'" + (requiredType != null ? " to required type '" + ClassUtils.getQualifiedName(requiredType) + "'" : ""), cause);
/*  33: 89 */     this.value = value;
/*  34: 90 */     this.requiredType = requiredType;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public Object getValue()
/*  38:    */   {
/*  39: 99 */     return this.value;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public Class getRequiredType()
/*  43:    */   {
/*  44:106 */     return this.requiredType;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public String getErrorCode()
/*  48:    */   {
/*  49:110 */     return "typeMismatch";
/*  50:    */   }
/*  51:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.TypeMismatchException
 * JD-Core Version:    0.7.0.1
 */