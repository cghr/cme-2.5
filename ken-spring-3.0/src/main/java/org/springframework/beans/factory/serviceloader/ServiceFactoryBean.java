/*  1:   */ package org.springframework.beans.factory.serviceloader;
/*  2:   */ 
/*  3:   */ import java.util.Iterator;
/*  4:   */ import java.util.ServiceLoader;
/*  5:   */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  6:   */ 
/*  7:   */ public class ServiceFactoryBean
/*  8:   */   extends AbstractServiceLoaderBasedFactoryBean
/*  9:   */   implements BeanClassLoaderAware
/* 10:   */ {
/* 11:   */   protected Object getObjectToExpose(ServiceLoader serviceLoader)
/* 12:   */   {
/* 13:37 */     Iterator it = serviceLoader.iterator();
/* 14:38 */     if (!it.hasNext()) {
/* 15:39 */       throw new IllegalStateException(
/* 16:40 */         "ServiceLoader could not find service for type [" + getServiceType() + "]");
/* 17:   */     }
/* 18:42 */     return it.next();
/* 19:   */   }
/* 20:   */   
/* 21:   */   public Class getObjectType()
/* 22:   */   {
/* 23:47 */     return getServiceType();
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.serviceloader.ServiceFactoryBean
 * JD-Core Version:    0.7.0.1
 */