/*   1:    */ package org.springframework.beans.factory.support;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import org.springframework.beans.BeanMetadataElement;
/*   5:    */ import org.springframework.util.Assert;
/*   6:    */ import org.springframework.util.ObjectUtils;
/*   7:    */ 
/*   8:    */ public abstract class MethodOverride
/*   9:    */   implements BeanMetadataElement
/*  10:    */ {
/*  11:    */   private final String methodName;
/*  12: 40 */   private boolean overloaded = true;
/*  13:    */   private Object source;
/*  14:    */   
/*  15:    */   protected MethodOverride(String methodName)
/*  16:    */   {
/*  17: 50 */     Assert.notNull(methodName, "Method name must not be null");
/*  18: 51 */     this.methodName = methodName;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public String getMethodName()
/*  22:    */   {
/*  23: 58 */     return this.methodName;
/*  24:    */   }
/*  25:    */   
/*  26:    */   protected void setOverloaded(boolean overloaded)
/*  27:    */   {
/*  28: 67 */     this.overloaded = overloaded;
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected boolean isOverloaded()
/*  32:    */   {
/*  33: 75 */     return this.overloaded;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setSource(Object source)
/*  37:    */   {
/*  38: 83 */     this.source = source;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public Object getSource()
/*  42:    */   {
/*  43: 87 */     return this.source;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public abstract boolean matches(Method paramMethod);
/*  47:    */   
/*  48:    */   public boolean equals(Object other)
/*  49:    */   {
/*  50:103 */     if (this == other) {
/*  51:104 */       return true;
/*  52:    */     }
/*  53:106 */     if (!(other instanceof MethodOverride)) {
/*  54:107 */       return false;
/*  55:    */     }
/*  56:109 */     MethodOverride that = (MethodOverride)other;
/*  57:    */     
/*  58:    */ 
/*  59:112 */     return (ObjectUtils.nullSafeEquals(this.methodName, that.methodName)) && (this.overloaded == that.overloaded) && (ObjectUtils.nullSafeEquals(this.source, that.source));
/*  60:    */   }
/*  61:    */   
/*  62:    */   public int hashCode()
/*  63:    */   {
/*  64:117 */     int hashCode = ObjectUtils.nullSafeHashCode(this.methodName);
/*  65:118 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.source);
/*  66:119 */     hashCode = 29 * hashCode + (this.overloaded ? 1 : 0);
/*  67:120 */     return hashCode;
/*  68:    */   }
/*  69:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.MethodOverride
 * JD-Core Version:    0.7.0.1
 */