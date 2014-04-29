/*   1:    */ package org.springframework.beans.factory.config;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.InvocationTargetException;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import org.springframework.beans.TypeConverter;
/*   6:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*   7:    */ import org.springframework.beans.factory.BeanFactory;
/*   8:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*   9:    */ import org.springframework.beans.factory.FactoryBean;
/*  10:    */ import org.springframework.beans.factory.FactoryBeanNotInitializedException;
/*  11:    */ import org.springframework.beans.factory.InitializingBean;
/*  12:    */ import org.springframework.beans.support.ArgumentConvertingMethodInvoker;
/*  13:    */ import org.springframework.util.ClassUtils;
/*  14:    */ 
/*  15:    */ public class MethodInvokingFactoryBean
/*  16:    */   extends ArgumentConvertingMethodInvoker
/*  17:    */   implements FactoryBean<Object>, BeanClassLoaderAware, BeanFactoryAware, InitializingBean
/*  18:    */ {
/*  19: 93 */   private boolean singleton = true;
/*  20: 95 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*  21:    */   private ConfigurableBeanFactory beanFactory;
/*  22: 99 */   private boolean initialized = false;
/*  23:    */   private Object singletonObject;
/*  24:    */   
/*  25:    */   public void setSingleton(boolean singleton)
/*  26:    */   {
/*  27:110 */     this.singleton = singleton;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public boolean isSingleton()
/*  31:    */   {
/*  32:114 */     return this.singleton;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setBeanClassLoader(ClassLoader classLoader)
/*  36:    */   {
/*  37:118 */     this.beanClassLoader = classLoader;
/*  38:    */   }
/*  39:    */   
/*  40:    */   protected Class resolveClassName(String className)
/*  41:    */     throws ClassNotFoundException
/*  42:    */   {
/*  43:123 */     return ClassUtils.forName(className, this.beanClassLoader);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setBeanFactory(BeanFactory beanFactory)
/*  47:    */   {
/*  48:127 */     if ((beanFactory instanceof ConfigurableBeanFactory)) {
/*  49:128 */       this.beanFactory = ((ConfigurableBeanFactory)beanFactory);
/*  50:    */     }
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected TypeConverter getDefaultTypeConverter()
/*  54:    */   {
/*  55:139 */     if (this.beanFactory != null) {
/*  56:140 */       return this.beanFactory.getTypeConverter();
/*  57:    */     }
/*  58:143 */     return super.getDefaultTypeConverter();
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void afterPropertiesSet()
/*  62:    */     throws Exception
/*  63:    */   {
/*  64:149 */     prepare();
/*  65:150 */     if (this.singleton)
/*  66:    */     {
/*  67:151 */       this.initialized = true;
/*  68:152 */       this.singletonObject = doInvoke();
/*  69:    */     }
/*  70:    */   }
/*  71:    */   
/*  72:    */   private Object doInvoke()
/*  73:    */     throws Exception
/*  74:    */   {
/*  75:    */     try
/*  76:    */     {
/*  77:162 */       return invoke();
/*  78:    */     }
/*  79:    */     catch (InvocationTargetException ex)
/*  80:    */     {
/*  81:165 */       if ((ex.getTargetException() instanceof Exception)) {
/*  82:166 */         throw ((Exception)ex.getTargetException());
/*  83:    */       }
/*  84:168 */       if ((ex.getTargetException() instanceof Error)) {
/*  85:169 */         throw ((Error)ex.getTargetException());
/*  86:    */       }
/*  87:171 */       throw ex;
/*  88:    */     }
/*  89:    */   }
/*  90:    */   
/*  91:    */   public Object getObject()
/*  92:    */     throws Exception
/*  93:    */   {
/*  94:182 */     if (this.singleton)
/*  95:    */     {
/*  96:183 */       if (!this.initialized) {
/*  97:184 */         throw new FactoryBeanNotInitializedException();
/*  98:    */       }
/*  99:187 */       return this.singletonObject;
/* 100:    */     }
/* 101:191 */     return doInvoke();
/* 102:    */   }
/* 103:    */   
/* 104:    */   public Class<?> getObjectType()
/* 105:    */   {
/* 106:200 */     if (!isPrepared()) {
/* 107:202 */       return null;
/* 108:    */     }
/* 109:204 */     return getPreparedMethod().getReturnType();
/* 110:    */   }
/* 111:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.MethodInvokingFactoryBean
 * JD-Core Version:    0.7.0.1
 */