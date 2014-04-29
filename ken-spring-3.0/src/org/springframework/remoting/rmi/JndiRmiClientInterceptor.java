/*   1:    */ package org.springframework.remoting.rmi;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.InvocationTargetException;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import java.rmi.RemoteException;
/*   6:    */ import javax.naming.NamingException;
/*   7:    */ import javax.rmi.PortableRemoteObject;
/*   8:    */ import org.aopalliance.intercept.MethodInterceptor;
/*   9:    */ import org.aopalliance.intercept.MethodInvocation;
/*  10:    */ import org.apache.commons.logging.Log;
/*  11:    */ import org.omg.CORBA.OBJECT_NOT_EXIST;
/*  12:    */ import org.omg.CORBA.SystemException;
/*  13:    */ import org.springframework.aop.support.AopUtils;
/*  14:    */ import org.springframework.beans.factory.InitializingBean;
/*  15:    */ import org.springframework.jndi.JndiObjectLocator;
/*  16:    */ import org.springframework.remoting.RemoteAccessException;
/*  17:    */ import org.springframework.remoting.RemoteConnectFailureException;
/*  18:    */ import org.springframework.remoting.RemoteInvocationFailureException;
/*  19:    */ import org.springframework.remoting.RemoteLookupFailureException;
/*  20:    */ import org.springframework.remoting.support.DefaultRemoteInvocationFactory;
/*  21:    */ import org.springframework.remoting.support.RemoteInvocation;
/*  22:    */ import org.springframework.remoting.support.RemoteInvocationFactory;
/*  23:    */ import org.springframework.util.ReflectionUtils;
/*  24:    */ 
/*  25:    */ public class JndiRmiClientInterceptor
/*  26:    */   extends JndiObjectLocator
/*  27:    */   implements MethodInterceptor, InitializingBean
/*  28:    */ {
/*  29:    */   private Class serviceInterface;
/*  30: 83 */   private RemoteInvocationFactory remoteInvocationFactory = new DefaultRemoteInvocationFactory();
/*  31: 85 */   private boolean lookupStubOnStartup = true;
/*  32: 87 */   private boolean cacheStub = true;
/*  33: 89 */   private boolean refreshStubOnConnectFailure = false;
/*  34:    */   private Object cachedStub;
/*  35: 93 */   private final Object stubMonitor = new Object();
/*  36:    */   
/*  37:    */   public void setServiceInterface(Class serviceInterface)
/*  38:    */   {
/*  39:103 */     if ((serviceInterface != null) && (!serviceInterface.isInterface())) {
/*  40:104 */       throw new IllegalArgumentException("'serviceInterface' must be an interface");
/*  41:    */     }
/*  42:106 */     this.serviceInterface = serviceInterface;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public Class getServiceInterface()
/*  46:    */   {
/*  47:113 */     return this.serviceInterface;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setRemoteInvocationFactory(RemoteInvocationFactory remoteInvocationFactory)
/*  51:    */   {
/*  52:123 */     this.remoteInvocationFactory = remoteInvocationFactory;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public RemoteInvocationFactory getRemoteInvocationFactory()
/*  56:    */   {
/*  57:130 */     return this.remoteInvocationFactory;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setLookupStubOnStartup(boolean lookupStubOnStartup)
/*  61:    */   {
/*  62:140 */     this.lookupStubOnStartup = lookupStubOnStartup;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void setCacheStub(boolean cacheStub)
/*  66:    */   {
/*  67:151 */     this.cacheStub = cacheStub;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void setRefreshStubOnConnectFailure(boolean refreshStubOnConnectFailure)
/*  71:    */   {
/*  72:166 */     this.refreshStubOnConnectFailure = refreshStubOnConnectFailure;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void afterPropertiesSet()
/*  76:    */     throws NamingException
/*  77:    */   {
/*  78:172 */     super.afterPropertiesSet();
/*  79:173 */     prepare();
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void prepare()
/*  83:    */     throws RemoteLookupFailureException
/*  84:    */   {
/*  85:184 */     if (this.lookupStubOnStartup)
/*  86:    */     {
/*  87:185 */       Object remoteObj = lookupStub();
/*  88:186 */       if (this.logger.isDebugEnabled()) {
/*  89:187 */         if ((remoteObj instanceof RmiInvocationHandler))
/*  90:    */         {
/*  91:188 */           this.logger.debug("JNDI RMI object [" + getJndiName() + "] is an RMI invoker");
/*  92:    */         }
/*  93:190 */         else if (getServiceInterface() != null)
/*  94:    */         {
/*  95:191 */           boolean isImpl = getServiceInterface().isInstance(remoteObj);
/*  96:192 */           this.logger.debug("Using service interface [" + getServiceInterface().getName() + 
/*  97:193 */             "] for JNDI RMI object [" + getJndiName() + "] - " + (
/*  98:194 */             !isImpl ? "not " : "") + "directly implemented");
/*  99:    */         }
/* 100:    */       }
/* 101:197 */       if (this.cacheStub) {
/* 102:198 */         this.cachedStub = remoteObj;
/* 103:    */       }
/* 104:    */     }
/* 105:    */   }
/* 106:    */   
/* 107:    */   protected Object lookupStub()
/* 108:    */     throws RemoteLookupFailureException
/* 109:    */   {
/* 110:    */     try
/* 111:    */     {
/* 112:216 */       Object stub = lookup();
/* 113:217 */       if ((getServiceInterface() != null) && (!(stub instanceof RmiInvocationHandler))) {
/* 114:    */         try
/* 115:    */         {
/* 116:219 */           stub = PortableRemoteObject.narrow(stub, getServiceInterface());
/* 117:    */         }
/* 118:    */         catch (ClassCastException ex)
/* 119:    */         {
/* 120:222 */           throw new RemoteLookupFailureException(
/* 121:223 */             "Could not narrow RMI stub to service interface [" + getServiceInterface().getName() + "]", ex);
/* 122:    */         }
/* 123:    */       }
/* 124:226 */       return stub;
/* 125:    */     }
/* 126:    */     catch (NamingException ex)
/* 127:    */     {
/* 128:229 */       throw new RemoteLookupFailureException("JNDI lookup for RMI service [" + getJndiName() + "] failed", ex);
/* 129:    */     }
/* 130:    */   }
/* 131:    */   
/* 132:    */   protected Object getStub()
/* 133:    */     throws NamingException, RemoteLookupFailureException
/* 134:    */   {
/* 135:245 */     if ((!this.cacheStub) || ((this.lookupStubOnStartup) && (!this.refreshStubOnConnectFailure))) {
/* 136:246 */       return this.cachedStub != null ? this.cachedStub : lookupStub();
/* 137:    */     }
/* 138:249 */     synchronized (this.stubMonitor)
/* 139:    */     {
/* 140:250 */       if (this.cachedStub == null) {
/* 141:251 */         this.cachedStub = lookupStub();
/* 142:    */       }
/* 143:253 */       return this.cachedStub;
/* 144:    */     }
/* 145:    */   }
/* 146:    */   
/* 147:    */   public Object invoke(MethodInvocation invocation)
/* 148:    */     throws Throwable
/* 149:    */   {
/* 150:271 */     Object stub = null;
/* 151:    */     try
/* 152:    */     {
/* 153:273 */       stub = getStub();
/* 154:    */     }
/* 155:    */     catch (NamingException ex)
/* 156:    */     {
/* 157:276 */       throw new RemoteLookupFailureException("JNDI lookup for RMI service [" + getJndiName() + "] failed", ex);
/* 158:    */     }
/* 159:    */     try
/* 160:    */     {
/* 161:279 */       return doInvoke(invocation, stub);
/* 162:    */     }
/* 163:    */     catch (RemoteConnectFailureException ex)
/* 164:    */     {
/* 165:282 */       return handleRemoteConnectFailure(invocation, ex);
/* 166:    */     }
/* 167:    */     catch (RemoteException ex)
/* 168:    */     {
/* 169:285 */       if (isConnectFailure(ex)) {
/* 170:286 */         return handleRemoteConnectFailure(invocation, ex);
/* 171:    */       }
/* 172:289 */       throw ex;
/* 173:    */     }
/* 174:    */     catch (SystemException ex)
/* 175:    */     {
/* 176:293 */       if (isConnectFailure(ex)) {
/* 177:294 */         return handleRemoteConnectFailure(invocation, ex);
/* 178:    */       }
/* 179:297 */       throw ex;
/* 180:    */     }
/* 181:    */   }
/* 182:    */   
/* 183:    */   protected boolean isConnectFailure(RemoteException ex)
/* 184:    */   {
/* 185:310 */     return RmiClientInterceptorUtils.isConnectFailure(ex);
/* 186:    */   }
/* 187:    */   
/* 188:    */   protected boolean isConnectFailure(SystemException ex)
/* 189:    */   {
/* 190:321 */     return ex instanceof OBJECT_NOT_EXIST;
/* 191:    */   }
/* 192:    */   
/* 193:    */   private Object handleRemoteConnectFailure(MethodInvocation invocation, Exception ex)
/* 194:    */     throws Throwable
/* 195:    */   {
/* 196:334 */     if (this.refreshStubOnConnectFailure)
/* 197:    */     {
/* 198:335 */       if (this.logger.isDebugEnabled()) {
/* 199:336 */         this.logger.debug("Could not connect to RMI service [" + getJndiName() + "] - retrying", ex);
/* 200:338 */       } else if (this.logger.isWarnEnabled()) {
/* 201:339 */         this.logger.warn("Could not connect to RMI service [" + getJndiName() + "] - retrying");
/* 202:    */       }
/* 203:341 */       return refreshAndRetry(invocation);
/* 204:    */     }
/* 205:344 */     throw ex;
/* 206:    */   }
/* 207:    */   
/* 208:    */   protected Object refreshAndRetry(MethodInvocation invocation)
/* 209:    */     throws Throwable
/* 210:    */   {
/* 211:357 */     Object freshStub = null;
/* 212:358 */     synchronized (this.stubMonitor)
/* 213:    */     {
/* 214:359 */       this.cachedStub = null;
/* 215:360 */       freshStub = lookupStub();
/* 216:361 */       if (this.cacheStub) {
/* 217:362 */         this.cachedStub = freshStub;
/* 218:    */       }
/* 219:    */     }
/* 220:365 */     return doInvoke(invocation, freshStub);
/* 221:    */   }
/* 222:    */   
/* 223:    */   protected Object doInvoke(MethodInvocation invocation, Object stub)
/* 224:    */     throws Throwable
/* 225:    */   {
/* 226:377 */     if ((stub instanceof RmiInvocationHandler)) {
/* 227:    */       try
/* 228:    */       {
/* 229:380 */         return doInvoke(invocation, (RmiInvocationHandler)stub);
/* 230:    */       }
/* 231:    */       catch (RemoteException ex)
/* 232:    */       {
/* 233:383 */         throw convertRmiAccessException(ex, invocation.getMethod());
/* 234:    */       }
/* 235:    */       catch (SystemException ex)
/* 236:    */       {
/* 237:386 */         throw convertCorbaAccessException(ex, invocation.getMethod());
/* 238:    */       }
/* 239:    */       catch (InvocationTargetException ex)
/* 240:    */       {
/* 241:389 */         throw ex.getTargetException();
/* 242:    */       }
/* 243:    */       catch (Throwable ex)
/* 244:    */       {
/* 245:392 */         throw new RemoteInvocationFailureException("Invocation of method [" + invocation.getMethod() + 
/* 246:393 */           "] failed in RMI service [" + getJndiName() + "]", ex);
/* 247:    */       }
/* 248:    */     }
/* 249:    */     try
/* 250:    */     {
/* 251:399 */       return RmiClientInterceptorUtils.invokeRemoteMethod(invocation, stub);
/* 252:    */     }
/* 253:    */     catch (InvocationTargetException ex)
/* 254:    */     {
/* 255:402 */       Throwable targetEx = ex.getTargetException();
/* 256:403 */       if ((targetEx instanceof RemoteException)) {
/* 257:404 */         throw convertRmiAccessException((RemoteException)targetEx, invocation.getMethod());
/* 258:    */       }
/* 259:406 */       if ((targetEx instanceof SystemException)) {
/* 260:407 */         throw convertCorbaAccessException((SystemException)targetEx, invocation.getMethod());
/* 261:    */       }
/* 262:410 */       throw targetEx;
/* 263:    */     }
/* 264:    */   }
/* 265:    */   
/* 266:    */   protected Object doInvoke(MethodInvocation methodInvocation, RmiInvocationHandler invocationHandler)
/* 267:    */     throws RemoteException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
/* 268:    */   {
/* 269:431 */     if (AopUtils.isToStringMethod(methodInvocation.getMethod())) {
/* 270:432 */       return "RMI invoker proxy for service URL [" + getJndiName() + "]";
/* 271:    */     }
/* 272:435 */     return invocationHandler.invoke(createRemoteInvocation(methodInvocation));
/* 273:    */   }
/* 274:    */   
/* 275:    */   protected RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation)
/* 276:    */   {
/* 277:451 */     return getRemoteInvocationFactory().createRemoteInvocation(methodInvocation);
/* 278:    */   }
/* 279:    */   
/* 280:    */   private Exception convertRmiAccessException(RemoteException ex, Method method)
/* 281:    */   {
/* 282:463 */     return RmiClientInterceptorUtils.convertRmiAccessException(method, ex, isConnectFailure(ex), getJndiName());
/* 283:    */   }
/* 284:    */   
/* 285:    */   private Exception convertCorbaAccessException(SystemException ex, Method method)
/* 286:    */   {
/* 287:475 */     if (ReflectionUtils.declaresException(method, RemoteException.class)) {
/* 288:477 */       return new RemoteException("Failed to access CORBA service [" + getJndiName() + "]", ex);
/* 289:    */     }
/* 290:480 */     if (isConnectFailure(ex)) {
/* 291:481 */       return new RemoteConnectFailureException("Could not connect to CORBA service [" + getJndiName() + "]", ex);
/* 292:    */     }
/* 293:484 */     return new RemoteAccessException("Could not access CORBA service [" + getJndiName() + "]", ex);
/* 294:    */   }
/* 295:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.rmi.JndiRmiClientInterceptor
 * JD-Core Version:    0.7.0.1
 */