/*  1:   */ package org.springframework.remoting.jaxws;
/*  2:   */ 
/*  3:   */ import javax.xml.ws.Service;
/*  4:   */ import org.springframework.beans.factory.FactoryBean;
/*  5:   */ import org.springframework.beans.factory.InitializingBean;
/*  6:   */ 
/*  7:   */ public class LocalJaxWsServiceFactoryBean
/*  8:   */   extends LocalJaxWsServiceFactory
/*  9:   */   implements FactoryBean<Service>, InitializingBean
/* 10:   */ {
/* 11:   */   private Service service;
/* 12:   */   
/* 13:   */   public void afterPropertiesSet()
/* 14:   */   {
/* 15:45 */     this.service = createJaxWsService();
/* 16:   */   }
/* 17:   */   
/* 18:   */   public Service getObject()
/* 19:   */   {
/* 20:49 */     return this.service;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public Class<? extends Service> getObjectType()
/* 24:   */   {
/* 25:53 */     return this.service != null ? this.service.getClass() : Service.class;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public boolean isSingleton()
/* 29:   */   {
/* 30:57 */     return true;
/* 31:   */   }
/* 32:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.jaxws.LocalJaxWsServiceFactoryBean
 * JD-Core Version:    0.7.0.1
 */