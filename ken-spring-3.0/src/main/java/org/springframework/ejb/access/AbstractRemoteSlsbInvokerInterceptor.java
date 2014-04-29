/*   1:    */ package org.springframework.ejb.access;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.InvocationTargetException;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import java.rmi.RemoteException;
/*   6:    */ import javax.ejb.EJBHome;
/*   7:    */ import javax.ejb.EJBObject;
/*   8:    */ import javax.naming.NamingException;
/*   9:    */ import javax.rmi.PortableRemoteObject;
/*  10:    */ import org.aopalliance.intercept.MethodInvocation;
/*  11:    */ import org.apache.commons.logging.Log;
/*  12:    */ import org.springframework.remoting.RemoteConnectFailureException;
/*  13:    */ import org.springframework.remoting.RemoteLookupFailureException;
/*  14:    */ import org.springframework.remoting.rmi.RmiClientInterceptorUtils;
/*  15:    */ 
/*  16:    */ public abstract class AbstractRemoteSlsbInvokerInterceptor
/*  17:    */   extends AbstractSlsbInvokerInterceptor
/*  18:    */ {
/*  19:    */   private Class homeInterface;
/*  20: 48 */   private boolean refreshHomeOnConnectFailure = false;
/*  21: 50 */   private volatile boolean homeAsComponent = false;
/*  22:    */   
/*  23:    */   public void setHomeInterface(Class homeInterface)
/*  24:    */   {
/*  25: 64 */     if ((homeInterface != null) && (!homeInterface.isInterface())) {
/*  26: 65 */       throw new IllegalArgumentException(
/*  27: 66 */         "Home interface class [" + homeInterface.getClass() + "] is not an interface");
/*  28:    */     }
/*  29: 68 */     this.homeInterface = homeInterface;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setRefreshHomeOnConnectFailure(boolean refreshHomeOnConnectFailure)
/*  33:    */   {
/*  34: 83 */     this.refreshHomeOnConnectFailure = refreshHomeOnConnectFailure;
/*  35:    */   }
/*  36:    */   
/*  37:    */   protected boolean isHomeRefreshable()
/*  38:    */   {
/*  39: 88 */     return this.refreshHomeOnConnectFailure;
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected Object lookup()
/*  43:    */     throws NamingException
/*  44:    */   {
/*  45:100 */     Object homeObject = super.lookup();
/*  46:101 */     if (this.homeInterface != null) {
/*  47:    */       try
/*  48:    */       {
/*  49:103 */         homeObject = PortableRemoteObject.narrow(homeObject, this.homeInterface);
/*  50:    */       }
/*  51:    */       catch (ClassCastException ex)
/*  52:    */       {
/*  53:106 */         throw new RemoteLookupFailureException(
/*  54:107 */           "Could not narrow EJB home stub to home interface [" + this.homeInterface.getName() + "]", ex);
/*  55:    */       }
/*  56:    */     }
/*  57:110 */     return homeObject;
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected Method getCreateMethod(Object home)
/*  61:    */     throws EjbAccessException
/*  62:    */   {
/*  63:118 */     if (this.homeAsComponent) {
/*  64:119 */       return null;
/*  65:    */     }
/*  66:121 */     if (!(home instanceof EJBHome))
/*  67:    */     {
/*  68:123 */       this.homeAsComponent = true;
/*  69:124 */       return null;
/*  70:    */     }
/*  71:126 */     return super.getCreateMethod(home);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public Object invokeInContext(MethodInvocation invocation)
/*  75:    */     throws Throwable
/*  76:    */   {
/*  77:    */     try
/*  78:    */     {
/*  79:141 */       return doInvoke(invocation);
/*  80:    */     }
/*  81:    */     catch (RemoteConnectFailureException ex)
/*  82:    */     {
/*  83:144 */       return handleRemoteConnectFailure(invocation, ex);
/*  84:    */     }
/*  85:    */     catch (RemoteException ex)
/*  86:    */     {
/*  87:147 */       if (isConnectFailure(ex)) {
/*  88:148 */         return handleRemoteConnectFailure(invocation, ex);
/*  89:    */       }
/*  90:151 */       throw ex;
/*  91:    */     }
/*  92:    */   }
/*  93:    */   
/*  94:    */   protected boolean isConnectFailure(RemoteException ex)
/*  95:    */   {
/*  96:164 */     return RmiClientInterceptorUtils.isConnectFailure(ex);
/*  97:    */   }
/*  98:    */   
/*  99:    */   private Object handleRemoteConnectFailure(MethodInvocation invocation, Exception ex)
/* 100:    */     throws Throwable
/* 101:    */   {
/* 102:168 */     if (this.refreshHomeOnConnectFailure)
/* 103:    */     {
/* 104:169 */       if (this.logger.isDebugEnabled()) {
/* 105:170 */         this.logger.debug("Could not connect to remote EJB [" + getJndiName() + "] - retrying", ex);
/* 106:172 */       } else if (this.logger.isWarnEnabled()) {
/* 107:173 */         this.logger.warn("Could not connect to remote EJB [" + getJndiName() + "] - retrying");
/* 108:    */       }
/* 109:175 */       return refreshAndRetry(invocation);
/* 110:    */     }
/* 111:178 */     throw ex;
/* 112:    */   }
/* 113:    */   
/* 114:    */   protected Object refreshAndRetry(MethodInvocation invocation)
/* 115:    */     throws Throwable
/* 116:    */   {
/* 117:    */     try
/* 118:    */     {
/* 119:192 */       refreshHome();
/* 120:    */     }
/* 121:    */     catch (NamingException ex)
/* 122:    */     {
/* 123:195 */       throw new RemoteLookupFailureException("Failed to locate remote EJB [" + getJndiName() + "]", ex);
/* 124:    */     }
/* 125:197 */     return doInvoke(invocation);
/* 126:    */   }
/* 127:    */   
/* 128:    */   protected abstract Object doInvoke(MethodInvocation paramMethodInvocation)
/* 129:    */     throws Throwable;
/* 130:    */   
/* 131:    */   protected Object newSessionBeanInstance()
/* 132:    */     throws NamingException, InvocationTargetException
/* 133:    */   {
/* 134:222 */     if (this.logger.isDebugEnabled()) {
/* 135:223 */       this.logger.debug("Trying to create reference to remote EJB");
/* 136:    */     }
/* 137:225 */     Object ejbInstance = create();
/* 138:226 */     if (this.logger.isDebugEnabled()) {
/* 139:227 */       this.logger.debug("Obtained reference to remote EJB: " + ejbInstance);
/* 140:    */     }
/* 141:229 */     return ejbInstance;
/* 142:    */   }
/* 143:    */   
/* 144:    */   protected void removeSessionBeanInstance(EJBObject ejb)
/* 145:    */   {
/* 146:239 */     if ((ejb != null) && (!this.homeAsComponent)) {
/* 147:    */       try
/* 148:    */       {
/* 149:241 */         ejb.remove();
/* 150:    */       }
/* 151:    */       catch (Throwable ex)
/* 152:    */       {
/* 153:244 */         this.logger.warn("Could not invoke 'remove' on remote EJB proxy", ex);
/* 154:    */       }
/* 155:    */     }
/* 156:    */   }
/* 157:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ejb.access.AbstractRemoteSlsbInvokerInterceptor
 * JD-Core Version:    0.7.0.1
 */