/*   1:    */ package org.springframework.beans.factory.support;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.LinkedList;
/*   5:    */ import java.util.List;
/*   6:    */ import org.springframework.util.Assert;
/*   7:    */ import org.springframework.util.ObjectUtils;
/*   8:    */ 
/*   9:    */ public class ReplaceOverride
/*  10:    */   extends MethodOverride
/*  11:    */ {
/*  12:    */   private final String methodReplacerBeanName;
/*  13: 41 */   private List<String> typeIdentifiers = new LinkedList();
/*  14:    */   
/*  15:    */   public ReplaceOverride(String methodName, String methodReplacerBeanName)
/*  16:    */   {
/*  17: 50 */     super(methodName);
/*  18: 51 */     Assert.notNull(methodName, "Method replacer bean name must not be null");
/*  19: 52 */     this.methodReplacerBeanName = methodReplacerBeanName;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public String getMethodReplacerBeanName()
/*  23:    */   {
/*  24: 59 */     return this.methodReplacerBeanName;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void addTypeIdentifier(String identifier)
/*  28:    */   {
/*  29: 68 */     this.typeIdentifiers.add(identifier);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public boolean matches(Method method)
/*  33:    */   {
/*  34: 75 */     if (!method.getName().equals(getMethodName())) {
/*  35: 77 */       return false;
/*  36:    */     }
/*  37: 80 */     if (!isOverloaded()) {
/*  38: 82 */       return true;
/*  39:    */     }
/*  40: 86 */     if (this.typeIdentifiers.size() != method.getParameterTypes().length) {
/*  41: 87 */       return false;
/*  42:    */     }
/*  43: 89 */     for (int i = 0; i < this.typeIdentifiers.size(); i++)
/*  44:    */     {
/*  45: 90 */       String identifier = (String)this.typeIdentifiers.get(i);
/*  46: 91 */       if (!method.getParameterTypes()[i].getName().contains(identifier)) {
/*  47: 93 */         return false;
/*  48:    */       }
/*  49:    */     }
/*  50: 96 */     return true;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public String toString()
/*  54:    */   {
/*  55:102 */     return 
/*  56:103 */       "Replace override for method '" + getMethodName() + "; will call bean '" + this.methodReplacerBeanName + "'";
/*  57:    */   }
/*  58:    */   
/*  59:    */   public boolean equals(Object other)
/*  60:    */   {
/*  61:108 */     if ((!(other instanceof ReplaceOverride)) || (!super.equals(other))) {
/*  62:109 */       return false;
/*  63:    */     }
/*  64:111 */     ReplaceOverride that = (ReplaceOverride)other;
/*  65:    */     
/*  66:113 */     return (ObjectUtils.nullSafeEquals(this.methodReplacerBeanName, that.methodReplacerBeanName)) && (ObjectUtils.nullSafeEquals(this.typeIdentifiers, that.typeIdentifiers));
/*  67:    */   }
/*  68:    */   
/*  69:    */   public int hashCode()
/*  70:    */   {
/*  71:118 */     int hashCode = super.hashCode();
/*  72:119 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.methodReplacerBeanName);
/*  73:120 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.typeIdentifiers);
/*  74:121 */     return hashCode;
/*  75:    */   }
/*  76:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.ReplaceOverride
 * JD-Core Version:    0.7.0.1
 */