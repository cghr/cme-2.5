/*   1:    */ package org.springframework.remoting.caucho;
/*   2:    */ 
/*   3:    */ import com.caucho.burlap.client.BurlapProxyFactory;
/*   4:    */ import com.caucho.burlap.client.BurlapRuntimeException;
/*   5:    */ import java.lang.reflect.InvocationTargetException;
/*   6:    */ import java.lang.reflect.Method;
/*   7:    */ import java.lang.reflect.UndeclaredThrowableException;
/*   8:    */ import java.net.ConnectException;
/*   9:    */ import java.net.MalformedURLException;
/*  10:    */ import org.aopalliance.intercept.MethodInterceptor;
/*  11:    */ import org.aopalliance.intercept.MethodInvocation;
/*  12:    */ import org.springframework.remoting.RemoteAccessException;
/*  13:    */ import org.springframework.remoting.RemoteConnectFailureException;
/*  14:    */ import org.springframework.remoting.RemoteLookupFailureException;
/*  15:    */ import org.springframework.remoting.RemoteProxyFailureException;
/*  16:    */ import org.springframework.remoting.support.UrlBasedRemoteAccessor;
/*  17:    */ import org.springframework.util.Assert;
/*  18:    */ 
/*  19:    */ public class BurlapClientInterceptor
/*  20:    */   extends UrlBasedRemoteAccessor
/*  21:    */   implements MethodInterceptor
/*  22:    */ {
/*  23: 63 */   private BurlapProxyFactory proxyFactory = new BurlapProxyFactory();
/*  24:    */   private Object burlapProxy;
/*  25:    */   
/*  26:    */   public void setProxyFactory(BurlapProxyFactory proxyFactory)
/*  27:    */   {
/*  28: 75 */     this.proxyFactory = (proxyFactory != null ? proxyFactory : new BurlapProxyFactory());
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setUsername(String username)
/*  32:    */   {
/*  33: 85 */     this.proxyFactory.setUser(username);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setPassword(String password)
/*  37:    */   {
/*  38: 95 */     this.proxyFactory.setPassword(password);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setOverloadEnabled(boolean overloadEnabled)
/*  42:    */   {
/*  43:104 */     this.proxyFactory.setOverloadEnabled(overloadEnabled);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void afterPropertiesSet()
/*  47:    */   {
/*  48:110 */     super.afterPropertiesSet();
/*  49:111 */     prepare();
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void prepare()
/*  53:    */     throws RemoteLookupFailureException
/*  54:    */   {
/*  55:    */     try
/*  56:    */     {
/*  57:120 */       this.burlapProxy = createBurlapProxy(this.proxyFactory);
/*  58:    */     }
/*  59:    */     catch (MalformedURLException ex)
/*  60:    */     {
/*  61:123 */       throw new RemoteLookupFailureException("Service URL [" + getServiceUrl() + "] is invalid", ex);
/*  62:    */     }
/*  63:    */   }
/*  64:    */   
/*  65:    */   protected Object createBurlapProxy(BurlapProxyFactory proxyFactory)
/*  66:    */     throws MalformedURLException
/*  67:    */   {
/*  68:135 */     Assert.notNull(getServiceInterface(), "Property 'serviceInterface' is required");
/*  69:136 */     return proxyFactory.create(getServiceInterface(), getServiceUrl());
/*  70:    */   }
/*  71:    */   
/*  72:    */   public Object invoke(MethodInvocation invocation)
/*  73:    */     throws Throwable
/*  74:    */   {
/*  75:141 */     if (this.burlapProxy == null) {
/*  76:142 */       throw new IllegalStateException("BurlapClientInterceptor is not properly initialized - invoke 'prepare' before attempting any operations");
/*  77:    */     }
/*  78:146 */     ClassLoader originalClassLoader = overrideThreadContextClassLoader();
/*  79:    */     try
/*  80:    */     {
/*  81:148 */       return invocation.getMethod().invoke(this.burlapProxy, invocation.getArguments());
/*  82:    */     }
/*  83:    */     catch (InvocationTargetException ex)
/*  84:    */     {
/*  85:151 */       Throwable targetEx = ex.getTargetException();
/*  86:152 */       if ((targetEx instanceof BurlapRuntimeException))
/*  87:    */       {
/*  88:153 */         Throwable cause = targetEx.getCause();
/*  89:154 */         throw convertBurlapAccessException(cause != null ? cause : targetEx);
/*  90:    */       }
/*  91:156 */       if ((targetEx instanceof UndeclaredThrowableException))
/*  92:    */       {
/*  93:157 */         UndeclaredThrowableException utex = (UndeclaredThrowableException)targetEx;
/*  94:158 */         throw convertBurlapAccessException(utex.getUndeclaredThrowable());
/*  95:    */       }
/*  96:161 */       throw targetEx;
/*  97:    */     }
/*  98:    */     catch (Throwable ex)
/*  99:    */     {
/* 100:165 */       throw new RemoteProxyFailureException(
/* 101:166 */         "Failed to invoke Burlap proxy for remote service [" + getServiceUrl() + "]", ex);
/* 102:    */     }
/* 103:    */     finally
/* 104:    */     {
/* 105:169 */       resetThreadContextClassLoader(originalClassLoader);
/* 106:    */     }
/* 107:    */   }
/* 108:    */   
/* 109:    */   protected RemoteAccessException convertBurlapAccessException(Throwable ex)
/* 110:    */   {
/* 111:180 */     if ((ex instanceof ConnectException)) {
/* 112:181 */       return new RemoteConnectFailureException(
/* 113:182 */         "Cannot connect to Burlap remote service at [" + getServiceUrl() + "]", ex);
/* 114:    */     }
/* 115:185 */     return new RemoteAccessException(
/* 116:186 */       "Cannot access Burlap remote service at [" + getServiceUrl() + "]", ex);
/* 117:    */   }
/* 118:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.caucho.BurlapClientInterceptor
 * JD-Core Version:    0.7.0.1
 */