/*   1:    */ package org.springframework.remoting.rmi;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.lang.reflect.InvocationTargetException;
/*   5:    */ import java.net.MalformedURLException;
/*   6:    */ import java.net.URL;
/*   7:    */ import java.net.URLConnection;
/*   8:    */ import java.net.URLStreamHandler;
/*   9:    */ import java.rmi.Naming;
/*  10:    */ import java.rmi.NotBoundException;
/*  11:    */ import java.rmi.Remote;
/*  12:    */ import java.rmi.RemoteException;
/*  13:    */ import java.rmi.registry.LocateRegistry;
/*  14:    */ import java.rmi.registry.Registry;
/*  15:    */ import java.rmi.server.RMIClientSocketFactory;
/*  16:    */ import org.aopalliance.intercept.MethodInterceptor;
/*  17:    */ import org.aopalliance.intercept.MethodInvocation;
/*  18:    */ import org.apache.commons.logging.Log;
/*  19:    */ import org.springframework.aop.support.AopUtils;
/*  20:    */ import org.springframework.remoting.RemoteConnectFailureException;
/*  21:    */ import org.springframework.remoting.RemoteInvocationFailureException;
/*  22:    */ import org.springframework.remoting.RemoteLookupFailureException;
/*  23:    */ import org.springframework.remoting.support.RemoteInvocationBasedAccessor;
/*  24:    */ import org.springframework.remoting.support.RemoteInvocationUtils;
/*  25:    */ 
/*  26:    */ public class RmiClientInterceptor
/*  27:    */   extends RemoteInvocationBasedAccessor
/*  28:    */   implements MethodInterceptor
/*  29:    */ {
/*  30: 73 */   private boolean lookupStubOnStartup = true;
/*  31: 75 */   private boolean cacheStub = true;
/*  32: 77 */   private boolean refreshStubOnConnectFailure = false;
/*  33:    */   private RMIClientSocketFactory registryClientSocketFactory;
/*  34:    */   private Remote cachedStub;
/*  35: 83 */   private final Object stubMonitor = new Object();
/*  36:    */   
/*  37:    */   public void setLookupStubOnStartup(boolean lookupStubOnStartup)
/*  38:    */   {
/*  39: 93 */     this.lookupStubOnStartup = lookupStubOnStartup;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setCacheStub(boolean cacheStub)
/*  43:    */   {
/*  44:104 */     this.cacheStub = cacheStub;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setRefreshStubOnConnectFailure(boolean refreshStubOnConnectFailure)
/*  48:    */   {
/*  49:119 */     this.refreshStubOnConnectFailure = refreshStubOnConnectFailure;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setRegistryClientSocketFactory(RMIClientSocketFactory registryClientSocketFactory)
/*  53:    */   {
/*  54:128 */     this.registryClientSocketFactory = registryClientSocketFactory;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void afterPropertiesSet()
/*  58:    */   {
/*  59:134 */     super.afterPropertiesSet();
/*  60:135 */     prepare();
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void prepare()
/*  64:    */     throws RemoteLookupFailureException
/*  65:    */   {
/*  66:146 */     if (this.lookupStubOnStartup)
/*  67:    */     {
/*  68:147 */       Remote remoteObj = lookupStub();
/*  69:148 */       if (this.logger.isDebugEnabled()) {
/*  70:149 */         if ((remoteObj instanceof RmiInvocationHandler))
/*  71:    */         {
/*  72:150 */           this.logger.debug("RMI stub [" + getServiceUrl() + "] is an RMI invoker");
/*  73:    */         }
/*  74:152 */         else if (getServiceInterface() != null)
/*  75:    */         {
/*  76:153 */           boolean isImpl = getServiceInterface().isInstance(remoteObj);
/*  77:154 */           this.logger.debug("Using service interface [" + getServiceInterface().getName() + 
/*  78:155 */             "] for RMI stub [" + getServiceUrl() + "] - " + (
/*  79:156 */             !isImpl ? "not " : "") + "directly implemented");
/*  80:    */         }
/*  81:    */       }
/*  82:159 */       if (this.cacheStub) {
/*  83:160 */         this.cachedStub = remoteObj;
/*  84:    */       }
/*  85:    */     }
/*  86:    */   }
/*  87:    */   
/*  88:    */   protected Remote lookupStub()
/*  89:    */     throws RemoteLookupFailureException
/*  90:    */   {
/*  91:    */     try
/*  92:    */     {
/*  93:178 */       Remote stub = null;
/*  94:179 */       if (this.registryClientSocketFactory != null)
/*  95:    */       {
/*  96:184 */         URL url = new URL(null, getServiceUrl(), new DummyURLStreamHandler(null));
/*  97:185 */         String protocol = url.getProtocol();
/*  98:186 */         if ((protocol != null) && (!"rmi".equals(protocol))) {
/*  99:187 */           throw new MalformedURLException("Invalid URL scheme '" + protocol + "'");
/* 100:    */         }
/* 101:189 */         String host = url.getHost();
/* 102:190 */         int port = url.getPort();
/* 103:191 */         String name = url.getPath();
/* 104:192 */         if ((name != null) && (name.startsWith("/"))) {
/* 105:193 */           name = name.substring(1);
/* 106:    */         }
/* 107:195 */         Registry registry = LocateRegistry.getRegistry(host, port, this.registryClientSocketFactory);
/* 108:196 */         stub = registry.lookup(name);
/* 109:    */       }
/* 110:    */       else
/* 111:    */       {
/* 112:200 */         stub = Naming.lookup(getServiceUrl());
/* 113:    */       }
/* 114:202 */       if (this.logger.isDebugEnabled()) {
/* 115:203 */         this.logger.debug("Located RMI stub with URL [" + getServiceUrl() + "]");
/* 116:    */       }
/* 117:205 */       return stub;
/* 118:    */     }
/* 119:    */     catch (MalformedURLException ex)
/* 120:    */     {
/* 121:208 */       throw new RemoteLookupFailureException("Service URL [" + getServiceUrl() + "] is invalid", ex);
/* 122:    */     }
/* 123:    */     catch (NotBoundException ex)
/* 124:    */     {
/* 125:211 */       throw new RemoteLookupFailureException(
/* 126:212 */         "Could not find RMI service [" + getServiceUrl() + "] in RMI registry", ex);
/* 127:    */     }
/* 128:    */     catch (RemoteException ex)
/* 129:    */     {
/* 130:215 */       throw new RemoteLookupFailureException("Lookup of RMI stub failed", ex);
/* 131:    */     }
/* 132:    */   }
/* 133:    */   
/* 134:    */   protected Remote getStub()
/* 135:    */     throws RemoteLookupFailureException
/* 136:    */   {
/* 137:231 */     if ((!this.cacheStub) || ((this.lookupStubOnStartup) && (!this.refreshStubOnConnectFailure))) {
/* 138:232 */       return this.cachedStub != null ? this.cachedStub : lookupStub();
/* 139:    */     }
/* 140:235 */     synchronized (this.stubMonitor)
/* 141:    */     {
/* 142:236 */       if (this.cachedStub == null) {
/* 143:237 */         this.cachedStub = lookupStub();
/* 144:    */       }
/* 145:239 */       return this.cachedStub;
/* 146:    */     }
/* 147:    */   }
/* 148:    */   
/* 149:    */   public Object invoke(MethodInvocation invocation)
/* 150:    */     throws Throwable
/* 151:    */   {
/* 152:257 */     Remote stub = getStub();
/* 153:    */     try
/* 154:    */     {
/* 155:259 */       return doInvoke(invocation, stub);
/* 156:    */     }
/* 157:    */     catch (RemoteConnectFailureException ex)
/* 158:    */     {
/* 159:262 */       return handleRemoteConnectFailure(invocation, ex);
/* 160:    */     }
/* 161:    */     catch (RemoteException ex)
/* 162:    */     {
/* 163:265 */       if (isConnectFailure(ex)) {
/* 164:266 */         return handleRemoteConnectFailure(invocation, ex);
/* 165:    */       }
/* 166:269 */       throw ex;
/* 167:    */     }
/* 168:    */   }
/* 169:    */   
/* 170:    */   protected boolean isConnectFailure(RemoteException ex)
/* 171:    */   {
/* 172:282 */     return RmiClientInterceptorUtils.isConnectFailure(ex);
/* 173:    */   }
/* 174:    */   
/* 175:    */   private Object handleRemoteConnectFailure(MethodInvocation invocation, Exception ex)
/* 176:    */     throws Throwable
/* 177:    */   {
/* 178:298 */     if (this.refreshStubOnConnectFailure)
/* 179:    */     {
/* 180:299 */       String msg = "Could not connect to RMI service [" + getServiceUrl() + "] - retrying";
/* 181:300 */       if (this.logger.isDebugEnabled()) {
/* 182:301 */         this.logger.warn(msg, ex);
/* 183:303 */       } else if (this.logger.isWarnEnabled()) {
/* 184:304 */         this.logger.warn(msg);
/* 185:    */       }
/* 186:306 */       return refreshAndRetry(invocation);
/* 187:    */     }
/* 188:309 */     throw ex;
/* 189:    */   }
/* 190:    */   
/* 191:    */   protected Object refreshAndRetry(MethodInvocation invocation)
/* 192:    */     throws Throwable
/* 193:    */   {
/* 194:322 */     Remote freshStub = null;
/* 195:323 */     synchronized (this.stubMonitor)
/* 196:    */     {
/* 197:324 */       this.cachedStub = null;
/* 198:325 */       freshStub = lookupStub();
/* 199:326 */       if (this.cacheStub) {
/* 200:327 */         this.cachedStub = freshStub;
/* 201:    */       }
/* 202:    */     }
/* 203:330 */     return doInvoke(invocation, freshStub);
/* 204:    */   }
/* 205:    */   
/* 206:    */   protected Object doInvoke(MethodInvocation invocation, Remote stub)
/* 207:    */     throws Throwable
/* 208:    */   {
/* 209:341 */     if ((stub instanceof RmiInvocationHandler)) {
/* 210:    */       try
/* 211:    */       {
/* 212:344 */         return doInvoke(invocation, (RmiInvocationHandler)stub);
/* 213:    */       }
/* 214:    */       catch (RemoteException ex)
/* 215:    */       {
/* 216:347 */         throw RmiClientInterceptorUtils.convertRmiAccessException(
/* 217:348 */           invocation.getMethod(), ex, isConnectFailure(ex), getServiceUrl());
/* 218:    */       }
/* 219:    */       catch (InvocationTargetException ex)
/* 220:    */       {
/* 221:351 */         Throwable exToThrow = ex.getTargetException();
/* 222:352 */         RemoteInvocationUtils.fillInClientStackTraceIfPossible(exToThrow);
/* 223:353 */         throw exToThrow;
/* 224:    */       }
/* 225:    */       catch (Throwable ex)
/* 226:    */       {
/* 227:356 */         throw new RemoteInvocationFailureException("Invocation of method [" + invocation.getMethod() + 
/* 228:357 */           "] failed in RMI service [" + getServiceUrl() + "]", ex);
/* 229:    */       }
/* 230:    */     }
/* 231:    */     try
/* 232:    */     {
/* 233:363 */       return RmiClientInterceptorUtils.invokeRemoteMethod(invocation, stub);
/* 234:    */     }
/* 235:    */     catch (InvocationTargetException ex)
/* 236:    */     {
/* 237:366 */       Throwable targetEx = ex.getTargetException();
/* 238:367 */       if ((targetEx instanceof RemoteException))
/* 239:    */       {
/* 240:368 */         RemoteException rex = (RemoteException)targetEx;
/* 241:369 */         throw RmiClientInterceptorUtils.convertRmiAccessException(
/* 242:370 */           invocation.getMethod(), rex, isConnectFailure(rex), getServiceUrl());
/* 243:    */       }
/* 244:373 */       throw targetEx;
/* 245:    */     }
/* 246:    */   }
/* 247:    */   
/* 248:    */   protected Object doInvoke(MethodInvocation methodInvocation, RmiInvocationHandler invocationHandler)
/* 249:    */     throws RemoteException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
/* 250:    */   {
/* 251:394 */     if (AopUtils.isToStringMethod(methodInvocation.getMethod())) {
/* 252:395 */       return "RMI invoker proxy for service URL [" + getServiceUrl() + "]";
/* 253:    */     }
/* 254:398 */     return invocationHandler.invoke(createRemoteInvocation(methodInvocation));
/* 255:    */   }
/* 256:    */   
/* 257:    */   private static class DummyURLStreamHandler
/* 258:    */     extends URLStreamHandler
/* 259:    */   {
/* 260:    */     protected URLConnection openConnection(URL url)
/* 261:    */       throws IOException
/* 262:    */     {
/* 263:411 */       throw new UnsupportedOperationException();
/* 264:    */     }
/* 265:    */   }
/* 266:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.rmi.RmiClientInterceptor
 * JD-Core Version:    0.7.0.1
 */