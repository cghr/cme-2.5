/*   1:    */ package org.springframework.beans.factory.config;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.InvocationHandler;
/*   4:    */ import java.lang.reflect.InvocationTargetException;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import java.lang.reflect.Proxy;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.apache.commons.logging.LogFactory;
/*   9:    */ import org.springframework.beans.SimpleTypeConverter;
/*  10:    */ import org.springframework.beans.TypeConverter;
/*  11:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  12:    */ import org.springframework.beans.factory.BeanFactory;
/*  13:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*  14:    */ import org.springframework.beans.factory.DisposableBean;
/*  15:    */ import org.springframework.beans.factory.FactoryBean;
/*  16:    */ import org.springframework.beans.factory.FactoryBeanNotInitializedException;
/*  17:    */ import org.springframework.beans.factory.InitializingBean;
/*  18:    */ import org.springframework.util.ClassUtils;
/*  19:    */ import org.springframework.util.ObjectUtils;
/*  20:    */ import org.springframework.util.ReflectionUtils;
/*  21:    */ 
/*  22:    */ public abstract class AbstractFactoryBean<T>
/*  23:    */   implements FactoryBean<T>, BeanClassLoaderAware, BeanFactoryAware, InitializingBean, DisposableBean
/*  24:    */ {
/*  25: 64 */   protected final Log logger = LogFactory.getLog(getClass());
/*  26: 66 */   private boolean singleton = true;
/*  27: 68 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*  28:    */   private BeanFactory beanFactory;
/*  29: 72 */   private boolean initialized = false;
/*  30:    */   private T singletonInstance;
/*  31:    */   private T earlySingletonInstance;
/*  32:    */   
/*  33:    */   public void setSingleton(boolean singleton)
/*  34:    */   {
/*  35: 84 */     this.singleton = singleton;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public boolean isSingleton()
/*  39:    */   {
/*  40: 88 */     return this.singleton;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setBeanClassLoader(ClassLoader classLoader)
/*  44:    */   {
/*  45: 92 */     this.beanClassLoader = classLoader;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setBeanFactory(BeanFactory beanFactory)
/*  49:    */   {
/*  50: 96 */     this.beanFactory = beanFactory;
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected BeanFactory getBeanFactory()
/*  54:    */   {
/*  55:103 */     return this.beanFactory;
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected TypeConverter getBeanTypeConverter()
/*  59:    */   {
/*  60:115 */     BeanFactory beanFactory = getBeanFactory();
/*  61:116 */     if ((beanFactory instanceof ConfigurableBeanFactory)) {
/*  62:117 */       return ((ConfigurableBeanFactory)beanFactory).getTypeConverter();
/*  63:    */     }
/*  64:120 */     return new SimpleTypeConverter();
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void afterPropertiesSet()
/*  68:    */     throws Exception
/*  69:    */   {
/*  70:128 */     if (isSingleton())
/*  71:    */     {
/*  72:129 */       this.initialized = true;
/*  73:130 */       this.singletonInstance = createInstance();
/*  74:131 */       this.earlySingletonInstance = null;
/*  75:    */     }
/*  76:    */   }
/*  77:    */   
/*  78:    */   public final T getObject()
/*  79:    */     throws Exception
/*  80:    */   {
/*  81:142 */     if (isSingleton()) {
/*  82:143 */       return this.initialized ? this.singletonInstance : getEarlySingletonInstance();
/*  83:    */     }
/*  84:146 */     return createInstance();
/*  85:    */   }
/*  86:    */   
/*  87:    */   private T getEarlySingletonInstance()
/*  88:    */     throws Exception
/*  89:    */   {
/*  90:156 */     Class[] ifcs = getEarlySingletonInterfaces();
/*  91:157 */     if (ifcs == null) {
/*  92:158 */       throw new FactoryBeanNotInitializedException(
/*  93:159 */         getClass().getName() + " does not support circular references");
/*  94:    */     }
/*  95:161 */     if (this.earlySingletonInstance == null) {
/*  96:162 */       this.earlySingletonInstance = Proxy.newProxyInstance(
/*  97:163 */         this.beanClassLoader, ifcs, new EarlySingletonInvocationHandler(null));
/*  98:    */     }
/*  99:165 */     return this.earlySingletonInstance;
/* 100:    */   }
/* 101:    */   
/* 102:    */   private T getSingletonInstance()
/* 103:    */     throws IllegalStateException
/* 104:    */   {
/* 105:174 */     if (!this.initialized) {
/* 106:175 */       throw new IllegalStateException("Singleton instance not initialized yet");
/* 107:    */     }
/* 108:177 */     return this.singletonInstance;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void destroy()
/* 112:    */     throws Exception
/* 113:    */   {
/* 114:185 */     if (isSingleton()) {
/* 115:186 */       destroyInstance(this.singletonInstance);
/* 116:    */     }
/* 117:    */   }
/* 118:    */   
/* 119:    */   public abstract Class<?> getObjectType();
/* 120:    */   
/* 121:    */   protected abstract T createInstance()
/* 122:    */     throws Exception;
/* 123:    */   
/* 124:    */   protected Class[] getEarlySingletonInterfaces()
/* 125:    */   {
/* 126:222 */     Class type = getObjectType();
/* 127:223 */     return (type != null) && (type.isInterface()) ? new Class[] { type } : null;
/* 128:    */   }
/* 129:    */   
/* 130:    */   protected void destroyInstance(T instance)
/* 131:    */     throws Exception
/* 132:    */   {}
/* 133:    */   
/* 134:    */   private class EarlySingletonInvocationHandler
/* 135:    */     implements InvocationHandler
/* 136:    */   {
/* 137:    */     private EarlySingletonInvocationHandler() {}
/* 138:    */     
/* 139:    */     public Object invoke(Object proxy, Method method, Object[] args)
/* 140:    */       throws Throwable
/* 141:    */     {
/* 142:245 */       if (ReflectionUtils.isEqualsMethod(method))
/* 143:    */       {
/* 144:247 */         if (proxy == args[0]) {
/* 145:247 */           return Boolean.valueOf(true);
/* 146:    */         }
/* 147:247 */         return Boolean.valueOf(false);
/* 148:    */       }
/* 149:249 */       if (ReflectionUtils.isHashCodeMethod(method)) {
/* 150:251 */         return Integer.valueOf(System.identityHashCode(proxy));
/* 151:    */       }
/* 152:253 */       if ((!AbstractFactoryBean.this.initialized) && (ReflectionUtils.isToStringMethod(method))) {
/* 153:254 */         return 
/* 154:255 */           "Early singleton proxy for interfaces " + ObjectUtils.nullSafeToString(AbstractFactoryBean.this.getEarlySingletonInterfaces());
/* 155:    */       }
/* 156:    */       try
/* 157:    */       {
/* 158:258 */         return method.invoke(AbstractFactoryBean.this.getSingletonInstance(), args);
/* 159:    */       }
/* 160:    */       catch (InvocationTargetException ex)
/* 161:    */       {
/* 162:261 */         throw ex.getTargetException();
/* 163:    */       }
/* 164:    */     }
/* 165:    */   }
/* 166:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.AbstractFactoryBean
 * JD-Core Version:    0.7.0.1
 */