/*   1:    */ package org.springframework.beans.factory.config;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Constructor;
/*   4:    */ import java.lang.reflect.InvocationHandler;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import java.lang.reflect.Proxy;
/*   7:    */ import java.util.Properties;
/*   8:    */ import org.springframework.beans.BeanUtils;
/*   9:    */ import org.springframework.beans.BeansException;
/*  10:    */ import org.springframework.beans.FatalBeanException;
/*  11:    */ import org.springframework.beans.factory.BeanFactory;
/*  12:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*  13:    */ import org.springframework.beans.factory.BeanFactoryUtils;
/*  14:    */ import org.springframework.beans.factory.FactoryBean;
/*  15:    */ import org.springframework.beans.factory.InitializingBean;
/*  16:    */ import org.springframework.beans.factory.ListableBeanFactory;
/*  17:    */ import org.springframework.util.ReflectionUtils;
/*  18:    */ import org.springframework.util.StringUtils;
/*  19:    */ 
/*  20:    */ public class ServiceLocatorFactoryBean
/*  21:    */   implements FactoryBean<Object>, BeanFactoryAware, InitializingBean
/*  22:    */ {
/*  23:    */   private Class serviceLocatorInterface;
/*  24:    */   private Constructor serviceLocatorExceptionConstructor;
/*  25:    */   private Properties serviceMappings;
/*  26:    */   private ListableBeanFactory beanFactory;
/*  27:    */   private Object proxy;
/*  28:    */   
/*  29:    */   public void setServiceLocatorInterface(Class interfaceType)
/*  30:    */   {
/*  31:211 */     this.serviceLocatorInterface = interfaceType;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setServiceLocatorExceptionClass(Class serviceLocatorExceptionClass)
/*  35:    */   {
/*  36:227 */     if ((serviceLocatorExceptionClass != null) && (!Exception.class.isAssignableFrom(serviceLocatorExceptionClass))) {
/*  37:228 */       throw new IllegalArgumentException(
/*  38:229 */         "serviceLocatorException [" + serviceLocatorExceptionClass.getName() + "] is not a subclass of Exception");
/*  39:    */     }
/*  40:231 */     this.serviceLocatorExceptionConstructor = 
/*  41:232 */       determineServiceLocatorExceptionConstructor(serviceLocatorExceptionClass);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setServiceMappings(Properties serviceMappings)
/*  45:    */   {
/*  46:246 */     this.serviceMappings = serviceMappings;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void setBeanFactory(BeanFactory beanFactory)
/*  50:    */     throws BeansException
/*  51:    */   {
/*  52:250 */     if (!(beanFactory instanceof ListableBeanFactory)) {
/*  53:251 */       throw new FatalBeanException(
/*  54:252 */         "ServiceLocatorFactoryBean needs to run in a BeanFactory that is a ListableBeanFactory");
/*  55:    */     }
/*  56:254 */     this.beanFactory = ((ListableBeanFactory)beanFactory);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void afterPropertiesSet()
/*  60:    */   {
/*  61:258 */     if (this.serviceLocatorInterface == null) {
/*  62:259 */       throw new IllegalArgumentException("Property 'serviceLocatorInterface' is required");
/*  63:    */     }
/*  64:263 */     this.proxy = Proxy.newProxyInstance(
/*  65:264 */       this.serviceLocatorInterface.getClassLoader(), 
/*  66:265 */       new Class[] { this.serviceLocatorInterface }, 
/*  67:266 */       new ServiceLocatorInvocationHandler(null));
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected Constructor determineServiceLocatorExceptionConstructor(Class exceptionClass)
/*  71:    */   {
/*  72:    */     try
/*  73:    */     {
/*  74:282 */       return exceptionClass.getConstructor(new Class[] { String.class, Throwable.class });
/*  75:    */     }
/*  76:    */     catch (NoSuchMethodException localNoSuchMethodException1)
/*  77:    */     {
/*  78:    */       try
/*  79:    */       {
/*  80:286 */         return exceptionClass.getConstructor(new Class[] { Throwable.class });
/*  81:    */       }
/*  82:    */       catch (NoSuchMethodException localNoSuchMethodException2)
/*  83:    */       {
/*  84:    */         try
/*  85:    */         {
/*  86:290 */           return exceptionClass.getConstructor(new Class[] { String.class });
/*  87:    */         }
/*  88:    */         catch (NoSuchMethodException localNoSuchMethodException3)
/*  89:    */         {
/*  90:293 */           throw new IllegalArgumentException(
/*  91:294 */             "Service locator exception [" + exceptionClass.getName() + 
/*  92:295 */             "] neither has a (String, Throwable) constructor nor a (String) constructor");
/*  93:    */         }
/*  94:    */       }
/*  95:    */     }
/*  96:    */   }
/*  97:    */   
/*  98:    */   protected Exception createServiceLocatorException(Constructor exceptionConstructor, BeansException cause)
/*  99:    */   {
/* 100:312 */     Class[] paramTypes = exceptionConstructor.getParameterTypes();
/* 101:313 */     Object[] args = new Object[paramTypes.length];
/* 102:314 */     for (int i = 0; i < paramTypes.length; i++) {
/* 103:315 */       if (paramTypes[i].equals(String.class)) {
/* 104:316 */         args[i] = cause.getMessage();
/* 105:318 */       } else if (paramTypes[i].isInstance(cause)) {
/* 106:319 */         args[i] = cause;
/* 107:    */       }
/* 108:    */     }
/* 109:322 */     return (Exception)BeanUtils.instantiateClass(exceptionConstructor, args);
/* 110:    */   }
/* 111:    */   
/* 112:    */   public Object getObject()
/* 113:    */   {
/* 114:327 */     return this.proxy;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public Class<?> getObjectType()
/* 118:    */   {
/* 119:331 */     return this.serviceLocatorInterface;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public boolean isSingleton()
/* 123:    */   {
/* 124:335 */     return true;
/* 125:    */   }
/* 126:    */   
/* 127:    */   private class ServiceLocatorInvocationHandler
/* 128:    */     implements InvocationHandler
/* 129:    */   {
/* 130:    */     private ServiceLocatorInvocationHandler() {}
/* 131:    */     
/* 132:    */     public Object invoke(Object proxy, Method method, Object[] args)
/* 133:    */       throws Throwable
/* 134:    */     {
/* 135:345 */       if (ReflectionUtils.isEqualsMethod(method))
/* 136:    */       {
/* 137:347 */         if (proxy == args[0]) {
/* 138:347 */           return Boolean.valueOf(true);
/* 139:    */         }
/* 140:347 */         return Boolean.valueOf(false);
/* 141:    */       }
/* 142:349 */       if (ReflectionUtils.isHashCodeMethod(method)) {
/* 143:351 */         return Integer.valueOf(System.identityHashCode(proxy));
/* 144:    */       }
/* 145:353 */       if (ReflectionUtils.isToStringMethod(method)) {
/* 146:354 */         return "Service locator: " + ServiceLocatorFactoryBean.this.serviceLocatorInterface.getName();
/* 147:    */       }
/* 148:357 */       return invokeServiceLocatorMethod(method, args);
/* 149:    */     }
/* 150:    */     
/* 151:    */     private Object invokeServiceLocatorMethod(Method method, Object[] args)
/* 152:    */       throws Exception
/* 153:    */     {
/* 154:363 */       Class serviceLocatorMethodReturnType = getServiceLocatorMethodReturnType(method);
/* 155:    */       try
/* 156:    */       {
/* 157:365 */         String beanName = tryGetBeanName(args);
/* 158:366 */         if (StringUtils.hasLength(beanName)) {
/* 159:368 */           return ServiceLocatorFactoryBean.this.beanFactory.getBean(beanName, serviceLocatorMethodReturnType);
/* 160:    */         }
/* 161:372 */         return BeanFactoryUtils.beanOfTypeIncludingAncestors(ServiceLocatorFactoryBean.this.beanFactory, serviceLocatorMethodReturnType);
/* 162:    */       }
/* 163:    */       catch (BeansException ex)
/* 164:    */       {
/* 165:376 */         if (ServiceLocatorFactoryBean.this.serviceLocatorExceptionConstructor != null) {
/* 166:377 */           throw ServiceLocatorFactoryBean.this.createServiceLocatorException(ServiceLocatorFactoryBean.this.serviceLocatorExceptionConstructor, ex);
/* 167:    */         }
/* 168:379 */         throw ex;
/* 169:    */       }
/* 170:    */     }
/* 171:    */     
/* 172:    */     private String tryGetBeanName(Object[] args)
/* 173:    */     {
/* 174:387 */       String beanName = "";
/* 175:388 */       if ((args != null) && (args.length == 1) && (args[0] != null)) {
/* 176:389 */         beanName = args[0].toString();
/* 177:    */       }
/* 178:392 */       if (ServiceLocatorFactoryBean.this.serviceMappings != null)
/* 179:    */       {
/* 180:393 */         String mappedName = ServiceLocatorFactoryBean.this.serviceMappings.getProperty(beanName);
/* 181:394 */         if (mappedName != null) {
/* 182:395 */           beanName = mappedName;
/* 183:    */         }
/* 184:    */       }
/* 185:398 */       return beanName;
/* 186:    */     }
/* 187:    */     
/* 188:    */     private Class getServiceLocatorMethodReturnType(Method method)
/* 189:    */       throws NoSuchMethodException
/* 190:    */     {
/* 191:402 */       Class[] paramTypes = method.getParameterTypes();
/* 192:403 */       Method interfaceMethod = ServiceLocatorFactoryBean.this.serviceLocatorInterface.getMethod(method.getName(), paramTypes);
/* 193:404 */       Class serviceLocatorReturnType = interfaceMethod.getReturnType();
/* 194:407 */       if ((paramTypes.length > 1) || (Void.TYPE.equals(serviceLocatorReturnType))) {
/* 195:408 */         throw new UnsupportedOperationException(
/* 196:409 */           "May only call methods with signature '<type> xxx()' or '<type> xxx(<idtype> id)' on factory interface, but tried to call: " + 
/* 197:410 */           interfaceMethod);
/* 198:    */       }
/* 199:412 */       return serviceLocatorReturnType;
/* 200:    */     }
/* 201:    */   }
/* 202:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.ServiceLocatorFactoryBean
 * JD-Core Version:    0.7.0.1
 */