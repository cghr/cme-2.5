/*  1:   */ package org.springframework.beans.factory.serviceloader;
/*  2:   */ 
/*  3:   */ import java.util.LinkedList;
/*  4:   */ import java.util.List;
/*  5:   */ import java.util.ServiceLoader;
/*  6:   */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  7:   */ 
/*  8:   */ public class ServiceListFactoryBean
/*  9:   */   extends AbstractServiceLoaderBasedFactoryBean
/* 10:   */   implements BeanClassLoaderAware
/* 11:   */ {
/* 12:   */   protected Object getObjectToExpose(ServiceLoader serviceLoader)
/* 13:   */   {
/* 14:38 */     List<Object> result = new LinkedList();
/* 15:39 */     for (Object loaderObject : serviceLoader) {
/* 16:40 */       result.add(loaderObject);
/* 17:   */     }
/* 18:42 */     return result;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public Class getObjectType()
/* 22:   */   {
/* 23:47 */     return List.class;
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.serviceloader.ServiceListFactoryBean
 * JD-Core Version:    0.7.0.1
 */