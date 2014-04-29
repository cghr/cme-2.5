/*   1:    */ package org.springframework.remoting.caucho;
/*   2:    */ 
/*   3:    */ import com.caucho.hessian.HessianException;
/*   4:    */ import com.caucho.hessian.client.HessianConnectionException;
/*   5:    */ import com.caucho.hessian.client.HessianProxyFactory;
/*   6:    */ import com.caucho.hessian.client.HessianRuntimeException;
/*   7:    */ import com.caucho.hessian.io.SerializerFactory;
/*   8:    */ import java.lang.reflect.InvocationTargetException;
/*   9:    */ import java.lang.reflect.Method;
/*  10:    */ import java.lang.reflect.UndeclaredThrowableException;
/*  11:    */ import java.net.ConnectException;
/*  12:    */ import java.net.MalformedURLException;
/*  13:    */ import org.aopalliance.intercept.MethodInterceptor;
/*  14:    */ import org.aopalliance.intercept.MethodInvocation;
/*  15:    */ import org.springframework.remoting.RemoteAccessException;
/*  16:    */ import org.springframework.remoting.RemoteConnectFailureException;
/*  17:    */ import org.springframework.remoting.RemoteLookupFailureException;
/*  18:    */ import org.springframework.remoting.RemoteProxyFailureException;
/*  19:    */ import org.springframework.remoting.support.UrlBasedRemoteAccessor;
/*  20:    */ import org.springframework.util.Assert;
/*  21:    */ 
/*  22:    */ public class HessianClientInterceptor
/*  23:    */   extends UrlBasedRemoteAccessor
/*  24:    */   implements MethodInterceptor
/*  25:    */ {
/*  26: 66 */   private HessianProxyFactory proxyFactory = new HessianProxyFactory();
/*  27:    */   private Object hessianProxy;
/*  28:    */   
/*  29:    */   public void setProxyFactory(HessianProxyFactory proxyFactory)
/*  30:    */   {
/*  31: 78 */     this.proxyFactory = (proxyFactory != null ? proxyFactory : new HessianProxyFactory());
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setSerializerFactory(SerializerFactory serializerFactory)
/*  35:    */   {
/*  36: 88 */     this.proxyFactory.setSerializerFactory(serializerFactory);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setSendCollectionType(boolean sendCollectionType)
/*  40:    */   {
/*  41: 96 */     this.proxyFactory.getSerializerFactory().setSendCollectionType(sendCollectionType);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setOverloadEnabled(boolean overloadEnabled)
/*  45:    */   {
/*  46:105 */     this.proxyFactory.setOverloadEnabled(overloadEnabled);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void setUsername(String username)
/*  50:    */   {
/*  51:115 */     this.proxyFactory.setUser(username);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setPassword(String password)
/*  55:    */   {
/*  56:125 */     this.proxyFactory.setPassword(password);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void setDebug(boolean debug)
/*  60:    */   {
/*  61:134 */     this.proxyFactory.setDebug(debug);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void setChunkedPost(boolean chunkedPost)
/*  65:    */   {
/*  66:142 */     this.proxyFactory.setChunkedPost(chunkedPost);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void setReadTimeout(long timeout)
/*  70:    */   {
/*  71:150 */     this.proxyFactory.setReadTimeout(timeout);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void setHessian2(boolean hessian2)
/*  75:    */   {
/*  76:159 */     this.proxyFactory.setHessian2Request(hessian2);
/*  77:160 */     this.proxyFactory.setHessian2Reply(hessian2);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void setHessian2Request(boolean hessian2)
/*  81:    */   {
/*  82:169 */     this.proxyFactory.setHessian2Request(hessian2);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void setHessian2Reply(boolean hessian2)
/*  86:    */   {
/*  87:178 */     this.proxyFactory.setHessian2Reply(hessian2);
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void afterPropertiesSet()
/*  91:    */   {
/*  92:184 */     super.afterPropertiesSet();
/*  93:185 */     prepare();
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void prepare()
/*  97:    */     throws RemoteLookupFailureException
/*  98:    */   {
/*  99:    */     try
/* 100:    */     {
/* 101:194 */       this.hessianProxy = createHessianProxy(this.proxyFactory);
/* 102:    */     }
/* 103:    */     catch (MalformedURLException ex)
/* 104:    */     {
/* 105:197 */       throw new RemoteLookupFailureException("Service URL [" + getServiceUrl() + "] is invalid", ex);
/* 106:    */     }
/* 107:    */   }
/* 108:    */   
/* 109:    */   protected Object createHessianProxy(HessianProxyFactory proxyFactory)
/* 110:    */     throws MalformedURLException
/* 111:    */   {
/* 112:209 */     Assert.notNull(getServiceInterface(), "'serviceInterface' is required");
/* 113:210 */     return proxyFactory.create(getServiceInterface(), getServiceUrl());
/* 114:    */   }
/* 115:    */   
/* 116:    */   public Object invoke(MethodInvocation invocation)
/* 117:    */     throws Throwable
/* 118:    */   {
/* 119:215 */     if (this.hessianProxy == null) {
/* 120:216 */       throw new IllegalStateException("HessianClientInterceptor is not properly initialized - invoke 'prepare' before attempting any operations");
/* 121:    */     }
/* 122:220 */     ClassLoader originalClassLoader = overrideThreadContextClassLoader();
/* 123:    */     try
/* 124:    */     {
/* 125:222 */       return invocation.getMethod().invoke(this.hessianProxy, invocation.getArguments());
/* 126:    */     }
/* 127:    */     catch (InvocationTargetException ex)
/* 128:    */     {
/* 129:225 */       Throwable targetEx = ex.getTargetException();
/* 130:227 */       if ((targetEx instanceof InvocationTargetException)) {
/* 131:228 */         targetEx = ((InvocationTargetException)targetEx).getTargetException();
/* 132:    */       }
/* 133:230 */       if ((targetEx instanceof HessianConnectionException)) {
/* 134:231 */         throw convertHessianAccessException(targetEx);
/* 135:    */       }
/* 136:233 */       if (((targetEx instanceof HessianException)) || ((targetEx instanceof HessianRuntimeException)))
/* 137:    */       {
/* 138:234 */         Throwable cause = targetEx.getCause();
/* 139:235 */         throw convertHessianAccessException(cause != null ? cause : targetEx);
/* 140:    */       }
/* 141:237 */       if ((targetEx instanceof UndeclaredThrowableException))
/* 142:    */       {
/* 143:238 */         UndeclaredThrowableException utex = (UndeclaredThrowableException)targetEx;
/* 144:239 */         throw convertHessianAccessException(utex.getUndeclaredThrowable());
/* 145:    */       }
/* 146:242 */       throw targetEx;
/* 147:    */     }
/* 148:    */     catch (Throwable ex)
/* 149:    */     {
/* 150:246 */       throw new RemoteProxyFailureException(
/* 151:247 */         "Failed to invoke Hessian proxy for remote service [" + getServiceUrl() + "]", ex);
/* 152:    */     }
/* 153:    */     finally
/* 154:    */     {
/* 155:250 */       resetThreadContextClassLoader(originalClassLoader);
/* 156:    */     }
/* 157:    */   }
/* 158:    */   
/* 159:    */   protected RemoteAccessException convertHessianAccessException(Throwable ex)
/* 160:    */   {
/* 161:261 */     if (((ex instanceof HessianConnectionException)) || ((ex instanceof ConnectException))) {
/* 162:262 */       return new RemoteConnectFailureException(
/* 163:263 */         "Cannot connect to Hessian remote service at [" + getServiceUrl() + "]", ex);
/* 164:    */     }
/* 165:266 */     return new RemoteAccessException(
/* 166:267 */       "Cannot access Hessian remote service at [" + getServiceUrl() + "]", ex);
/* 167:    */   }
/* 168:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.caucho.HessianClientInterceptor
 * JD-Core Version:    0.7.0.1
 */