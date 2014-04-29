/*  1:   */ package org.springframework.remoting.jaxws;
/*  2:   */ 
/*  3:   */ import javax.xml.ws.BindingProvider;
/*  4:   */ import org.springframework.aop.framework.ProxyFactory;
/*  5:   */ import org.springframework.beans.factory.FactoryBean;
/*  6:   */ 
/*  7:   */ public class JaxWsPortProxyFactoryBean
/*  8:   */   extends JaxWsPortClientInterceptor
/*  9:   */   implements FactoryBean<Object>
/* 10:   */ {
/* 11:   */   private Object serviceProxy;
/* 12:   */   
/* 13:   */   public void afterPropertiesSet()
/* 14:   */   {
/* 15:42 */     super.afterPropertiesSet();
/* 16:   */     
/* 17:   */ 
/* 18:45 */     ProxyFactory pf = new ProxyFactory();
/* 19:46 */     pf.addInterface(getServiceInterface());
/* 20:47 */     pf.addInterface(BindingProvider.class);
/* 21:48 */     pf.addAdvice(this);
/* 22:49 */     this.serviceProxy = pf.getProxy(getBeanClassLoader());
/* 23:   */   }
/* 24:   */   
/* 25:   */   public Object getObject()
/* 26:   */   {
/* 27:54 */     return this.serviceProxy;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public Class<?> getObjectType()
/* 31:   */   {
/* 32:58 */     return getServiceInterface();
/* 33:   */   }
/* 34:   */   
/* 35:   */   public boolean isSingleton()
/* 36:   */   {
/* 37:62 */     return true;
/* 38:   */   }
/* 39:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.jaxws.JaxWsPortProxyFactoryBean
 * JD-Core Version:    0.7.0.1
 */