/*  1:   */ package org.springframework.remoting.jaxrpc;
/*  2:   */ 
/*  3:   */ import javax.xml.rpc.Service;
/*  4:   */ import javax.xml.rpc.ServiceException;
/*  5:   */ import org.springframework.beans.factory.FactoryBean;
/*  6:   */ import org.springframework.beans.factory.InitializingBean;
/*  7:   */ 
/*  8:   */ @Deprecated
/*  9:   */ public class LocalJaxRpcServiceFactoryBean
/* 10:   */   extends LocalJaxRpcServiceFactory
/* 11:   */   implements FactoryBean<Service>, InitializingBean
/* 12:   */ {
/* 13:   */   private Service service;
/* 14:   */   
/* 15:   */   public void afterPropertiesSet()
/* 16:   */     throws ServiceException
/* 17:   */   {
/* 18:48 */     this.service = createJaxRpcService();
/* 19:   */   }
/* 20:   */   
/* 21:   */   public Service getObject()
/* 22:   */     throws Exception
/* 23:   */   {
/* 24:53 */     return this.service;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public Class<? extends Service> getObjectType()
/* 28:   */   {
/* 29:57 */     return this.service != null ? this.service.getClass() : Service.class;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public boolean isSingleton()
/* 33:   */   {
/* 34:61 */     return true;
/* 35:   */   }
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.jaxrpc.LocalJaxRpcServiceFactoryBean
 * JD-Core Version:    0.7.0.1
 */