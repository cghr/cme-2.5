/*  1:   */ package org.springframework.beans.factory.support;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Method;
/*  4:   */ import org.springframework.util.Assert;
/*  5:   */ import org.springframework.util.ObjectUtils;
/*  6:   */ 
/*  7:   */ public class LookupOverride
/*  8:   */   extends MethodOverride
/*  9:   */ {
/* 10:   */   private final String beanName;
/* 11:   */   
/* 12:   */   public LookupOverride(String methodName, String beanName)
/* 13:   */   {
/* 14:46 */     super(methodName);
/* 15:47 */     Assert.notNull(beanName, "Bean name must not be null");
/* 16:48 */     this.beanName = beanName;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public String getBeanName()
/* 20:   */   {
/* 21:55 */     return this.beanName;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public boolean matches(Method method)
/* 25:   */   {
/* 26:64 */     return (method.getName().equals(getMethodName())) && (method.getParameterTypes().length == 0);
/* 27:   */   }
/* 28:   */   
/* 29:   */   public String toString()
/* 30:   */   {
/* 31:70 */     return "LookupOverride for method '" + getMethodName() + "'; will return bean '" + this.beanName + "'";
/* 32:   */   }
/* 33:   */   
/* 34:   */   public boolean equals(Object other)
/* 35:   */   {
/* 36:76 */     return ((other instanceof LookupOverride)) && (super.equals(other)) && (ObjectUtils.nullSafeEquals(this.beanName, ((LookupOverride)other).beanName));
/* 37:   */   }
/* 38:   */   
/* 39:   */   public int hashCode()
/* 40:   */   {
/* 41:81 */     return 29 * super.hashCode() + ObjectUtils.nullSafeHashCode(this.beanName);
/* 42:   */   }
/* 43:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.LookupOverride
 * JD-Core Version:    0.7.0.1
 */