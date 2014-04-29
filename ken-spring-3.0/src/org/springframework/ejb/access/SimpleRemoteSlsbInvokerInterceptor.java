/*   1:    */ package org.springframework.ejb.access;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.InvocationTargetException;
/*   4:    */ import java.rmi.RemoteException;
/*   5:    */ import javax.ejb.CreateException;
/*   6:    */ import javax.ejb.EJBObject;
/*   7:    */ import javax.naming.NamingException;
/*   8:    */ import org.aopalliance.intercept.MethodInvocation;
/*   9:    */ import org.springframework.beans.factory.DisposableBean;
/*  10:    */ import org.springframework.remoting.RemoteLookupFailureException;
/*  11:    */ import org.springframework.remoting.rmi.RmiClientInterceptorUtils;
/*  12:    */ 
/*  13:    */ public class SimpleRemoteSlsbInvokerInterceptor
/*  14:    */   extends AbstractRemoteSlsbInvokerInterceptor
/*  15:    */   implements DisposableBean
/*  16:    */ {
/*  17: 68 */   private boolean cacheSessionBean = false;
/*  18:    */   private Object beanInstance;
/*  19: 72 */   private final Object beanInstanceMonitor = new Object();
/*  20:    */   
/*  21:    */   public void setCacheSessionBean(boolean cacheSessionBean)
/*  22:    */   {
/*  23: 83 */     this.cacheSessionBean = cacheSessionBean;
/*  24:    */   }
/*  25:    */   
/*  26:    */   protected Object doInvoke(MethodInvocation invocation)
/*  27:    */     throws Throwable
/*  28:    */   {
/*  29: 96 */     Object ejb = null;
/*  30:    */     try
/*  31:    */     {
/*  32: 98 */       ejb = getSessionBeanInstance();
/*  33: 99 */       return RmiClientInterceptorUtils.invokeRemoteMethod(invocation, ejb);
/*  34:    */     }
/*  35:    */     catch (NamingException ex)
/*  36:    */     {
/*  37:102 */       throw new RemoteLookupFailureException("Failed to locate remote EJB [" + getJndiName() + "]", ex);
/*  38:    */     }
/*  39:    */     catch (InvocationTargetException ex)
/*  40:    */     {
/*  41:105 */       Throwable targetEx = ex.getTargetException();
/*  42:106 */       if ((targetEx instanceof RemoteException))
/*  43:    */       {
/*  44:107 */         RemoteException rex = (RemoteException)targetEx;
/*  45:108 */         throw RmiClientInterceptorUtils.convertRmiAccessException(
/*  46:109 */           invocation.getMethod(), rex, isConnectFailure(rex), getJndiName());
/*  47:    */       }
/*  48:111 */       if ((targetEx instanceof CreateException)) {
/*  49:112 */         throw RmiClientInterceptorUtils.convertRmiAccessException(
/*  50:113 */           invocation.getMethod(), targetEx, "Could not create remote EJB [" + getJndiName() + "]");
/*  51:    */       }
/*  52:115 */       throw targetEx;
/*  53:    */     }
/*  54:    */     finally
/*  55:    */     {
/*  56:118 */       if ((ejb instanceof EJBObject)) {
/*  57:119 */         releaseSessionBeanInstance((EJBObject)ejb);
/*  58:    */       }
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   protected Object getSessionBeanInstance()
/*  63:    */     throws NamingException, InvocationTargetException
/*  64:    */   {
/*  65:133 */     if (this.cacheSessionBean) {
/*  66:134 */       synchronized (this.beanInstanceMonitor)
/*  67:    */       {
/*  68:135 */         if (this.beanInstance == null) {
/*  69:136 */           this.beanInstance = newSessionBeanInstance();
/*  70:    */         }
/*  71:138 */         return this.beanInstance;
/*  72:    */       }
/*  73:    */     }
/*  74:142 */     return newSessionBeanInstance();
/*  75:    */   }
/*  76:    */   
/*  77:    */   protected void releaseSessionBeanInstance(EJBObject ejb)
/*  78:    */   {
/*  79:153 */     if (!this.cacheSessionBean) {
/*  80:154 */       removeSessionBeanInstance(ejb);
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   protected void refreshHome()
/*  85:    */     throws NamingException
/*  86:    */   {
/*  87:163 */     super.refreshHome();
/*  88:164 */     if (this.cacheSessionBean) {
/*  89:165 */       synchronized (this.beanInstanceMonitor)
/*  90:    */       {
/*  91:166 */         this.beanInstance = null;
/*  92:    */       }
/*  93:    */     }
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void destroy()
/*  97:    */   {
/*  98:175 */     if (this.cacheSessionBean) {
/*  99:176 */       synchronized (this.beanInstanceMonitor)
/* 100:    */       {
/* 101:177 */         if ((this.beanInstance instanceof EJBObject)) {
/* 102:178 */           removeSessionBeanInstance((EJBObject)this.beanInstance);
/* 103:    */         }
/* 104:    */       }
/* 105:    */     }
/* 106:    */   }
/* 107:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ejb.access.SimpleRemoteSlsbInvokerInterceptor
 * JD-Core Version:    0.7.0.1
 */