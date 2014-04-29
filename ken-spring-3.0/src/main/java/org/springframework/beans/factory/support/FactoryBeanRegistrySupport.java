/*   1:    */ package org.springframework.beans.factory.support;
/*   2:    */ 
/*   3:    */ import java.security.AccessControlContext;
/*   4:    */ import java.security.AccessController;
/*   5:    */ import java.security.PrivilegedAction;
/*   6:    */ import java.security.PrivilegedActionException;
/*   7:    */ import java.security.PrivilegedExceptionAction;
/*   8:    */ import java.util.Map;
/*   9:    */ import java.util.concurrent.ConcurrentHashMap;
/*  10:    */ import org.apache.commons.logging.Log;
/*  11:    */ import org.springframework.beans.BeansException;
/*  12:    */ import org.springframework.beans.factory.BeanCreationException;
/*  13:    */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*  14:    */ import org.springframework.beans.factory.FactoryBean;
/*  15:    */ import org.springframework.beans.factory.FactoryBeanNotInitializedException;
/*  16:    */ 
/*  17:    */ public abstract class FactoryBeanRegistrySupport
/*  18:    */   extends DefaultSingletonBeanRegistry
/*  19:    */ {
/*  20: 46 */   private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap();
/*  21:    */   
/*  22:    */   protected Class getTypeForFactoryBean(final FactoryBean factoryBean)
/*  23:    */   {
/*  24:    */     try
/*  25:    */     {
/*  26: 57 */       if (System.getSecurityManager() != null) {
/*  27: 58 */         (Class)AccessController.doPrivileged(new PrivilegedAction()
/*  28:    */         {
/*  29:    */           public Class run()
/*  30:    */           {
/*  31: 60 */             return factoryBean.getObjectType();
/*  32:    */           }
/*  33: 62 */         }, getAccessControlContext());
/*  34:    */       }
/*  35: 65 */       return factoryBean.getObjectType();
/*  36:    */     }
/*  37:    */     catch (Throwable ex)
/*  38:    */     {
/*  39: 70 */       this.logger.warn("FactoryBean threw exception from getObjectType, despite the contract saying that it should return null if the type of its object cannot be determined yet", 
/*  40: 71 */         ex);
/*  41:    */     }
/*  42: 72 */     return null;
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected Object getCachedObjectForFactoryBean(String beanName)
/*  46:    */   {
/*  47: 84 */     Object object = this.factoryBeanObjectCache.get(beanName);
/*  48: 85 */     return object != NULL_OBJECT ? object : null;
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected Object getObjectFromFactoryBean(FactoryBean factory, String beanName, boolean shouldPostProcess)
/*  52:    */   {
/*  53: 98 */     if ((factory.isSingleton()) && (containsSingleton(beanName))) {
/*  54: 99 */       synchronized (getSingletonMutex())
/*  55:    */       {
/*  56:100 */         Object object = this.factoryBeanObjectCache.get(beanName);
/*  57:101 */         if (object == null)
/*  58:    */         {
/*  59:102 */           object = doGetObjectFromFactoryBean(factory, beanName, shouldPostProcess);
/*  60:103 */           this.factoryBeanObjectCache.put(beanName, object != null ? object : NULL_OBJECT);
/*  61:    */         }
/*  62:105 */         return object != NULL_OBJECT ? object : null;
/*  63:    */       }
/*  64:    */     }
/*  65:109 */     return doGetObjectFromFactoryBean(factory, beanName, shouldPostProcess);
/*  66:    */   }
/*  67:    */   
/*  68:    */   private Object doGetObjectFromFactoryBean(final FactoryBean factory, String beanName, boolean shouldPostProcess)
/*  69:    */     throws BeanCreationException
/*  70:    */   {
/*  71:    */     try
/*  72:    */     {
/*  73:128 */       if (System.getSecurityManager() != null)
/*  74:    */       {
/*  75:129 */         AccessControlContext acc = getAccessControlContext();
/*  76:    */         try
/*  77:    */         {
/*  78:131 */           object = AccessController.doPrivileged(new PrivilegedExceptionAction()
/*  79:    */           {
/*  80:    */             public Object run()
/*  81:    */               throws Exception
/*  82:    */             {
/*  83:133 */               return factory.getObject();
/*  84:    */             }
/*  85:135 */           }, acc);
/*  86:    */         }
/*  87:    */         catch (PrivilegedActionException pae)
/*  88:    */         {
/*  89:    */           Object object;
/*  90:138 */           throw pae.getException();
/*  91:    */         }
/*  92:    */       }
/*  93:    */       else
/*  94:    */       {
/*  95:142 */         object = factory.getObject();
/*  96:    */       }
/*  97:    */     }
/*  98:    */     catch (FactoryBeanNotInitializedException ex)
/*  99:    */     {
/* 100:    */       Object object;
/* 101:146 */       throw new BeanCurrentlyInCreationException(beanName, ex.toString());
/* 102:    */     }
/* 103:    */     catch (Throwable ex)
/* 104:    */     {
/* 105:149 */       throw new BeanCreationException(beanName, "FactoryBean threw exception on object creation", ex);
/* 106:    */     }
/* 107:    */     Object object;
/* 108:155 */     if ((object == null) && (isSingletonCurrentlyInCreation(beanName))) {
/* 109:156 */       throw new BeanCurrentlyInCreationException(
/* 110:157 */         beanName, "FactoryBean which is currently in creation returned null from getObject");
/* 111:    */     }
/* 112:160 */     if ((object != null) && (shouldPostProcess)) {
/* 113:    */       try
/* 114:    */       {
/* 115:162 */         object = postProcessObjectFromFactoryBean(object, beanName);
/* 116:    */       }
/* 117:    */       catch (Throwable ex)
/* 118:    */       {
/* 119:165 */         throw new BeanCreationException(beanName, "Post-processing of the FactoryBean's object failed", ex);
/* 120:    */       }
/* 121:    */     }
/* 122:169 */     return object;
/* 123:    */   }
/* 124:    */   
/* 125:    */   protected Object postProcessObjectFromFactoryBean(Object object, String beanName)
/* 126:    */     throws BeansException
/* 127:    */   {
/* 128:183 */     return object;
/* 129:    */   }
/* 130:    */   
/* 131:    */   protected FactoryBean getFactoryBean(String beanName, Object beanInstance)
/* 132:    */     throws BeansException
/* 133:    */   {
/* 134:194 */     if (!(beanInstance instanceof FactoryBean)) {
/* 135:195 */       throw new BeanCreationException(beanName, 
/* 136:196 */         "Bean instance of type [" + beanInstance.getClass() + "] is not a FactoryBean");
/* 137:    */     }
/* 138:198 */     return (FactoryBean)beanInstance;
/* 139:    */   }
/* 140:    */   
/* 141:    */   protected void removeSingleton(String beanName)
/* 142:    */   {
/* 143:206 */     super.removeSingleton(beanName);
/* 144:207 */     this.factoryBeanObjectCache.remove(beanName);
/* 145:    */   }
/* 146:    */   
/* 147:    */   protected AccessControlContext getAccessControlContext()
/* 148:    */   {
/* 149:217 */     return AccessController.getContext();
/* 150:    */   }
/* 151:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.FactoryBeanRegistrySupport
 * JD-Core Version:    0.7.0.1
 */