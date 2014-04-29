/*  1:   */ package org.springframework.remoting.rmi;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.InvocationTargetException;
/*  4:   */ import java.rmi.Remote;
/*  5:   */ import org.apache.commons.logging.Log;
/*  6:   */ import org.springframework.remoting.support.RemoteInvocation;
/*  7:   */ import org.springframework.remoting.support.RemoteInvocationBasedExporter;
/*  8:   */ 
/*  9:   */ public abstract class RmiBasedExporter
/* 10:   */   extends RemoteInvocationBasedExporter
/* 11:   */ {
/* 12:   */   protected Remote getObjectToExport()
/* 13:   */   {
/* 14:51 */     if (((getService() instanceof Remote)) && (
/* 15:52 */       (getServiceInterface() == null) || (Remote.class.isAssignableFrom(getServiceInterface())))) {
/* 16:54 */       return (Remote)getService();
/* 17:   */     }
/* 18:58 */     if (this.logger.isDebugEnabled()) {
/* 19:59 */       this.logger.debug("RMI service [" + getService() + "] is an RMI invoker");
/* 20:   */     }
/* 21:61 */     return new RmiInvocationWrapper(getProxyForService(), this);
/* 22:   */   }
/* 23:   */   
/* 24:   */   protected Object invoke(RemoteInvocation invocation, Object targetObject)
/* 25:   */     throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
/* 26:   */   {
/* 27:73 */     return super.invoke(invocation, targetObject);
/* 28:   */   }
/* 29:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.rmi.RmiBasedExporter
 * JD-Core Version:    0.7.0.1
 */