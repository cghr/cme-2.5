/*   1:    */ package org.springframework.remoting.support;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.lang.reflect.InvocationTargetException;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.Map;
/*   8:    */ import org.aopalliance.intercept.MethodInvocation;
/*   9:    */ import org.springframework.util.ClassUtils;
/*  10:    */ 
/*  11:    */ public class RemoteInvocation
/*  12:    */   implements Serializable
/*  13:    */ {
/*  14:    */   private static final long serialVersionUID = 6876024250231820554L;
/*  15:    */   private String methodName;
/*  16:    */   private Class[] parameterTypes;
/*  17:    */   private Object[] arguments;
/*  18:    */   private Map<String, Serializable> attributes;
/*  19:    */   
/*  20:    */   public RemoteInvocation() {}
/*  21:    */   
/*  22:    */   public RemoteInvocation(String methodName, Class[] parameterTypes, Object[] arguments)
/*  23:    */   {
/*  24: 74 */     this.methodName = methodName;
/*  25: 75 */     this.parameterTypes = parameterTypes;
/*  26: 76 */     this.arguments = arguments;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public RemoteInvocation(MethodInvocation methodInvocation)
/*  30:    */   {
/*  31: 84 */     this.methodName = methodInvocation.getMethod().getName();
/*  32: 85 */     this.parameterTypes = methodInvocation.getMethod().getParameterTypes();
/*  33: 86 */     this.arguments = methodInvocation.getArguments();
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setMethodName(String methodName)
/*  37:    */   {
/*  38: 94 */     this.methodName = methodName;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public String getMethodName()
/*  42:    */   {
/*  43:101 */     return this.methodName;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setParameterTypes(Class[] parameterTypes)
/*  47:    */   {
/*  48:108 */     this.parameterTypes = parameterTypes;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public Class[] getParameterTypes()
/*  52:    */   {
/*  53:115 */     return this.parameterTypes;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setArguments(Object[] arguments)
/*  57:    */   {
/*  58:122 */     this.arguments = arguments;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public Object[] getArguments()
/*  62:    */   {
/*  63:129 */     return this.arguments;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void addAttribute(String key, Serializable value)
/*  67:    */     throws IllegalStateException
/*  68:    */   {
/*  69:145 */     if (this.attributes == null) {
/*  70:146 */       this.attributes = new HashMap();
/*  71:    */     }
/*  72:148 */     if (this.attributes.containsKey(key)) {
/*  73:149 */       throw new IllegalStateException("There is already an attribute with key '" + key + "' bound");
/*  74:    */     }
/*  75:151 */     this.attributes.put(key, value);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public Serializable getAttribute(String key)
/*  79:    */   {
/*  80:162 */     if (this.attributes == null) {
/*  81:163 */       return null;
/*  82:    */     }
/*  83:165 */     return (Serializable)this.attributes.get(key);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setAttributes(Map<String, Serializable> attributes)
/*  87:    */   {
/*  88:176 */     this.attributes = attributes;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public Map<String, Serializable> getAttributes()
/*  92:    */   {
/*  93:187 */     return this.attributes;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public Object invoke(Object targetObject)
/*  97:    */     throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
/*  98:    */   {
/*  99:204 */     Method method = targetObject.getClass().getMethod(this.methodName, this.parameterTypes);
/* 100:205 */     return method.invoke(targetObject, this.arguments);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public String toString()
/* 104:    */   {
/* 105:211 */     return 
/* 106:212 */       "RemoteInvocation: method name '" + this.methodName + "'; parameter types " + ClassUtils.classNamesToString(this.parameterTypes);
/* 107:    */   }
/* 108:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.support.RemoteInvocation
 * JD-Core Version:    0.7.0.1
 */