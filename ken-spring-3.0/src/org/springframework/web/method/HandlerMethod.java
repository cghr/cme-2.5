/*   1:    */ package org.springframework.web.method;
/*   2:    */ 
/*   3:    */ import java.lang.annotation.Annotation;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import org.apache.commons.logging.Log;
/*   6:    */ import org.apache.commons.logging.LogFactory;
/*   7:    */ import org.springframework.beans.factory.BeanFactory;
/*   8:    */ import org.springframework.core.BridgeMethodResolver;
/*   9:    */ import org.springframework.core.MethodParameter;
/*  10:    */ import org.springframework.core.annotation.AnnotationUtils;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ import org.springframework.util.ClassUtils;
/*  13:    */ 
/*  14:    */ public class HandlerMethod
/*  15:    */ {
/*  16: 47 */   protected final Log logger = LogFactory.getLog(getClass());
/*  17:    */   private final Object bean;
/*  18:    */   private final Method method;
/*  19:    */   private final BeanFactory beanFactory;
/*  20:    */   private MethodParameter[] parameters;
/*  21:    */   private final Method bridgedMethod;
/*  22:    */   
/*  23:    */   public HandlerMethod(Object bean, Method method)
/*  24:    */   {
/*  25: 65 */     Assert.notNull(bean, "bean must not be null");
/*  26: 66 */     Assert.notNull(method, "method must not be null");
/*  27: 67 */     this.bean = bean;
/*  28: 68 */     this.beanFactory = null;
/*  29: 69 */     this.method = method;
/*  30: 70 */     this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public HandlerMethod(Object bean, String methodName, Class<?>... parameterTypes)
/*  34:    */     throws NoSuchMethodException
/*  35:    */   {
/*  36: 81 */     Assert.notNull(bean, "bean must not be null");
/*  37: 82 */     Assert.notNull(methodName, "method must not be null");
/*  38: 83 */     this.bean = bean;
/*  39: 84 */     this.beanFactory = null;
/*  40: 85 */     this.method = bean.getClass().getMethod(methodName, parameterTypes);
/*  41: 86 */     this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(this.method);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public HandlerMethod(String beanName, BeanFactory beanFactory, Method method)
/*  45:    */   {
/*  46: 97 */     Assert.hasText(beanName, "'beanName' must not be null");
/*  47: 98 */     Assert.notNull(beanFactory, "'beanFactory' must not be null");
/*  48: 99 */     Assert.notNull(method, "'method' must not be null");
/*  49:100 */     Assert.isTrue(beanFactory.containsBean(beanName), 
/*  50:101 */       "Bean factory [" + beanFactory + "] does not contain bean " + "with name [" + beanName + "]");
/*  51:102 */     this.bean = beanName;
/*  52:103 */     this.beanFactory = beanFactory;
/*  53:104 */     this.method = method;
/*  54:105 */     this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public Object getBean()
/*  58:    */   {
/*  59:112 */     return this.bean;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public Method getMethod()
/*  63:    */   {
/*  64:119 */     return this.method;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public Class<?> getBeanType()
/*  68:    */   {
/*  69:127 */     if ((this.bean instanceof String))
/*  70:    */     {
/*  71:128 */       String beanName = (String)this.bean;
/*  72:129 */       return this.beanFactory.getType(beanName);
/*  73:    */     }
/*  74:132 */     return ClassUtils.getUserClass(this.bean.getClass());
/*  75:    */   }
/*  76:    */   
/*  77:    */   protected Method getBridgedMethod()
/*  78:    */   {
/*  79:141 */     return this.bridgedMethod;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public MethodParameter[] getMethodParameters()
/*  83:    */   {
/*  84:148 */     if (this.parameters == null)
/*  85:    */     {
/*  86:149 */       int parameterCount = this.bridgedMethod.getParameterTypes().length;
/*  87:150 */       MethodParameter[] p = new MethodParameter[parameterCount];
/*  88:151 */       for (int i = 0; i < parameterCount; i++) {
/*  89:152 */         p[i] = new HandlerMethodParameter(this.bridgedMethod, i);
/*  90:    */       }
/*  91:154 */       this.parameters = p;
/*  92:    */     }
/*  93:156 */     return this.parameters;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public MethodParameter getReturnType()
/*  97:    */   {
/*  98:163 */     return new HandlerMethodParameter(this.bridgedMethod, -1);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public boolean isVoid()
/* 102:    */   {
/* 103:170 */     return Void.TYPE.equals(getReturnType().getParameterType());
/* 104:    */   }
/* 105:    */   
/* 106:    */   public <A extends Annotation> A getMethodAnnotation(Class<A> annotationType)
/* 107:    */   {
/* 108:180 */     return AnnotationUtils.findAnnotation(this.method, annotationType);
/* 109:    */   }
/* 110:    */   
/* 111:    */   public HandlerMethod createWithResolvedBean()
/* 112:    */   {
/* 113:188 */     Object handler = this.bean;
/* 114:189 */     if ((this.bean instanceof String))
/* 115:    */     {
/* 116:190 */       String beanName = (String)this.bean;
/* 117:191 */       handler = this.beanFactory.getBean(beanName);
/* 118:    */     }
/* 119:193 */     return new HandlerMethod(handler, this.method);
/* 120:    */   }
/* 121:    */   
/* 122:    */   public boolean equals(Object o)
/* 123:    */   {
/* 124:198 */     if (this == o) {
/* 125:199 */       return true;
/* 126:    */     }
/* 127:201 */     if ((o != null) && ((o instanceof HandlerMethod)))
/* 128:    */     {
/* 129:202 */       HandlerMethod other = (HandlerMethod)o;
/* 130:203 */       return (this.bean.equals(other.bean)) && (this.method.equals(other.method));
/* 131:    */     }
/* 132:205 */     return false;
/* 133:    */   }
/* 134:    */   
/* 135:    */   public int hashCode()
/* 136:    */   {
/* 137:210 */     return 31 * this.bean.hashCode() + this.method.hashCode();
/* 138:    */   }
/* 139:    */   
/* 140:    */   public String toString()
/* 141:    */   {
/* 142:215 */     return this.method.toGenericString();
/* 143:    */   }
/* 144:    */   
/* 145:    */   private class HandlerMethodParameter
/* 146:    */     extends MethodParameter
/* 147:    */   {
/* 148:    */     public HandlerMethodParameter(Method method, int parameterIndex)
/* 149:    */     {
/* 150:226 */       super(parameterIndex);
/* 151:    */     }
/* 152:    */     
/* 153:    */     public Class<?> getDeclaringClass()
/* 154:    */     {
/* 155:235 */       return HandlerMethod.this.getBeanType();
/* 156:    */     }
/* 157:    */     
/* 158:    */     public <T extends Annotation> T getMethodAnnotation(Class<T> annotationType)
/* 159:    */     {
/* 160:244 */       return HandlerMethod.this.getMethodAnnotation(annotationType);
/* 161:    */     }
/* 162:    */   }
/* 163:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.HandlerMethod
 * JD-Core Version:    0.7.0.1
 */