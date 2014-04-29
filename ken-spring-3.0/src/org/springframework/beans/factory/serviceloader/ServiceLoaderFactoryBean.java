/*  1:   */ package org.springframework.beans.factory.serviceloader;
/*  2:   */ 
/*  3:   */ import java.util.ServiceLoader;
/*  4:   */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  5:   */ 
/*  6:   */ public class ServiceLoaderFactoryBean
/*  7:   */   extends AbstractServiceLoaderBasedFactoryBean
/*  8:   */   implements BeanClassLoaderAware
/*  9:   */ {
/* 10:   */   protected Object getObjectToExpose(ServiceLoader serviceLoader)
/* 11:   */   {
/* 12:35 */     return serviceLoader;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public Class getObjectType()
/* 16:   */   {
/* 17:40 */     return ServiceLoader.class;
/* 18:   */   }
/* 19:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.serviceloader.ServiceLoaderFactoryBean
 * JD-Core Version:    0.7.0.1
 */