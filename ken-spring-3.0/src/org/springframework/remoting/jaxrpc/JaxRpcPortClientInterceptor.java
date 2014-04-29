/*   1:    */ package org.springframework.remoting.jaxrpc;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.InvocationTargetException;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import java.rmi.Remote;
/*   6:    */ import java.rmi.RemoteException;
/*   7:    */ import java.util.HashMap;
/*   8:    */ import java.util.Map;
/*   9:    */ import java.util.Map.Entry;
/*  10:    */ import java.util.Properties;
/*  11:    */ import javax.xml.namespace.QName;
/*  12:    */ import javax.xml.rpc.Call;
/*  13:    */ import javax.xml.rpc.JAXRPCException;
/*  14:    */ import javax.xml.rpc.Service;
/*  15:    */ import javax.xml.rpc.ServiceException;
/*  16:    */ import javax.xml.rpc.Stub;
/*  17:    */ import javax.xml.rpc.soap.SOAPFaultException;
/*  18:    */ import org.aopalliance.intercept.MethodInterceptor;
/*  19:    */ import org.aopalliance.intercept.MethodInvocation;
/*  20:    */ import org.apache.commons.logging.Log;
/*  21:    */ import org.springframework.aop.support.AopUtils;
/*  22:    */ import org.springframework.beans.factory.InitializingBean;
/*  23:    */ import org.springframework.remoting.RemoteLookupFailureException;
/*  24:    */ import org.springframework.remoting.RemoteProxyFailureException;
/*  25:    */ import org.springframework.remoting.rmi.RmiClientInterceptorUtils;
/*  26:    */ import org.springframework.util.CollectionUtils;
/*  27:    */ import org.springframework.util.ReflectionUtils;
/*  28:    */ 
/*  29:    */ @Deprecated
/*  30:    */ public class JaxRpcPortClientInterceptor
/*  31:    */   extends LocalJaxRpcServiceFactory
/*  32:    */   implements MethodInterceptor, InitializingBean
/*  33:    */ {
/*  34:    */   private Service jaxRpcService;
/*  35:    */   private Service serviceToUse;
/*  36:    */   private String portName;
/*  37:    */   private String username;
/*  38:    */   private String password;
/*  39:    */   private String endpointAddress;
/*  40:    */   private boolean maintainSession;
/*  41: 97 */   private final Map<String, Object> customPropertyMap = new HashMap();
/*  42:    */   private Class serviceInterface;
/*  43:    */   private Class portInterface;
/*  44:103 */   private boolean lookupServiceOnStartup = true;
/*  45:105 */   private boolean refreshServiceAfterConnectFailure = false;
/*  46:    */   private QName portQName;
/*  47:    */   private Remote portStub;
/*  48:111 */   private final Object preparationMonitor = new Object();
/*  49:    */   
/*  50:    */   public void setJaxRpcService(Service jaxRpcService)
/*  51:    */   {
/*  52:125 */     this.jaxRpcService = jaxRpcService;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public Service getJaxRpcService()
/*  56:    */   {
/*  57:132 */     return this.jaxRpcService;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setPortName(String portName)
/*  61:    */   {
/*  62:140 */     this.portName = portName;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public String getPortName()
/*  66:    */   {
/*  67:147 */     return this.portName;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void setUsername(String username)
/*  71:    */   {
/*  72:156 */     this.username = username;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public String getUsername()
/*  76:    */   {
/*  77:163 */     return this.username;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void setPassword(String password)
/*  81:    */   {
/*  82:172 */     this.password = password;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public String getPassword()
/*  86:    */   {
/*  87:179 */     return this.password;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setEndpointAddress(String endpointAddress)
/*  91:    */   {
/*  92:188 */     this.endpointAddress = endpointAddress;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public String getEndpointAddress()
/*  96:    */   {
/*  97:195 */     return this.endpointAddress;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void setMaintainSession(boolean maintainSession)
/* 101:    */   {
/* 102:204 */     this.maintainSession = maintainSession;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public boolean isMaintainSession()
/* 106:    */   {
/* 107:211 */     return this.maintainSession;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void setCustomProperties(Properties customProperties)
/* 111:    */   {
/* 112:222 */     CollectionUtils.mergePropertiesIntoMap(customProperties, this.customPropertyMap);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public void setCustomPropertyMap(Map<String, Object> customProperties)
/* 116:    */   {
/* 117:232 */     if (customProperties != null) {
/* 118:233 */       for (Map.Entry<String, Object> entry : customProperties.entrySet()) {
/* 119:234 */         addCustomProperty((String)entry.getKey(), entry.getValue());
/* 120:    */       }
/* 121:    */     }
/* 122:    */   }
/* 123:    */   
/* 124:    */   public Map<String, Object> getCustomPropertyMap()
/* 125:    */   {
/* 126:247 */     return this.customPropertyMap;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void addCustomProperty(String name, Object value)
/* 130:    */   {
/* 131:258 */     this.customPropertyMap.put(name, value);
/* 132:    */   }
/* 133:    */   
/* 134:    */   public void setServiceInterface(Class serviceInterface)
/* 135:    */   {
/* 136:277 */     if ((serviceInterface != null) && (!serviceInterface.isInterface())) {
/* 137:278 */       throw new IllegalArgumentException("'serviceInterface' must be an interface");
/* 138:    */     }
/* 139:280 */     this.serviceInterface = serviceInterface;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public Class getServiceInterface()
/* 143:    */   {
/* 144:287 */     return this.serviceInterface;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public void setPortInterface(Class portInterface)
/* 148:    */   {
/* 149:305 */     if ((portInterface != null) && (
/* 150:306 */       (!portInterface.isInterface()) || (!Remote.class.isAssignableFrom(portInterface)))) {
/* 151:307 */       throw new IllegalArgumentException(
/* 152:308 */         "'portInterface' must be an interface derived from [java.rmi.Remote]");
/* 153:    */     }
/* 154:310 */     this.portInterface = portInterface;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public Class getPortInterface()
/* 158:    */   {
/* 159:317 */     return this.portInterface;
/* 160:    */   }
/* 161:    */   
/* 162:    */   public void setLookupServiceOnStartup(boolean lookupServiceOnStartup)
/* 163:    */   {
/* 164:327 */     this.lookupServiceOnStartup = lookupServiceOnStartup;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public void setRefreshServiceAfterConnectFailure(boolean refreshServiceAfterConnectFailure)
/* 168:    */   {
/* 169:339 */     this.refreshServiceAfterConnectFailure = refreshServiceAfterConnectFailure;
/* 170:    */   }
/* 171:    */   
/* 172:    */   public void afterPropertiesSet()
/* 173:    */   {
/* 174:348 */     if (this.lookupServiceOnStartup) {
/* 175:349 */       prepare();
/* 176:    */     }
/* 177:    */   }
/* 178:    */   
/* 179:    */   public void prepare()
/* 180:    */     throws RemoteLookupFailureException
/* 181:    */   {
/* 182:364 */     if (getPortName() == null) {
/* 183:365 */       throw new IllegalArgumentException("Property 'portName' is required");
/* 184:    */     }
/* 185:368 */     synchronized (this.preparationMonitor)
/* 186:    */     {
/* 187:369 */       this.serviceToUse = null;
/* 188:    */       
/* 189:    */ 
/* 190:372 */       this.portQName = getQName(getPortName());
/* 191:    */       try
/* 192:    */       {
/* 193:375 */         Service service = getJaxRpcService();
/* 194:376 */         if (service == null) {
/* 195:377 */           service = createJaxRpcService();
/* 196:    */         } else {
/* 197:380 */           postProcessJaxRpcService(service);
/* 198:    */         }
/* 199:383 */         Class portInterface = getPortInterface();
/* 200:384 */         if ((portInterface != null) && (!alwaysUseJaxRpcCall()))
/* 201:    */         {
/* 202:387 */           if (this.logger.isDebugEnabled()) {
/* 203:388 */             this.logger.debug("Creating JAX-RPC proxy for JAX-RPC port [" + this.portQName + 
/* 204:389 */               "], using port interface [" + portInterface.getName() + "]");
/* 205:    */           }
/* 206:391 */           Remote remoteObj = service.getPort(this.portQName, portInterface);
/* 207:393 */           if (this.logger.isDebugEnabled())
/* 208:    */           {
/* 209:394 */             Class serviceInterface = getServiceInterface();
/* 210:395 */             if (serviceInterface != null)
/* 211:    */             {
/* 212:396 */               boolean isImpl = serviceInterface.isInstance(remoteObj);
/* 213:397 */               this.logger.debug("Using service interface [" + serviceInterface.getName() + "] for JAX-RPC port [" + 
/* 214:398 */                 this.portQName + "] - " + (!isImpl ? "not" : "") + " directly implemented");
/* 215:    */             }
/* 216:    */           }
/* 217:402 */           if (!(remoteObj instanceof Stub)) {
/* 218:403 */             throw new RemoteLookupFailureException("Port stub of class [" + remoteObj.getClass().getName() + 
/* 219:404 */               "] is not a valid JAX-RPC stub: it does not implement interface [javax.xml.rpc.Stub]");
/* 220:    */           }
/* 221:406 */           Stub stub = (Stub)remoteObj;
/* 222:    */           
/* 223:    */ 
/* 224:409 */           preparePortStub(stub);
/* 225:    */           
/* 226:    */ 
/* 227:412 */           postProcessPortStub(stub);
/* 228:    */           
/* 229:414 */           this.portStub = remoteObj;
/* 230:    */         }
/* 231:419 */         else if (this.logger.isDebugEnabled())
/* 232:    */         {
/* 233:420 */           this.logger.debug("Using JAX-RPC dynamic calls for JAX-RPC port [" + this.portQName + "]");
/* 234:    */         }
/* 235:424 */         this.serviceToUse = service;
/* 236:    */       }
/* 237:    */       catch (ServiceException ex)
/* 238:    */       {
/* 239:427 */         throw new RemoteLookupFailureException(
/* 240:428 */           "Failed to initialize service for JAX-RPC port [" + this.portQName + "]", ex);
/* 241:    */       }
/* 242:    */     }
/* 243:    */   }
/* 244:    */   
/* 245:    */   protected boolean alwaysUseJaxRpcCall()
/* 246:    */   {
/* 247:446 */     return false;
/* 248:    */   }
/* 249:    */   
/* 250:    */   protected void reset()
/* 251:    */   {
/* 252:454 */     synchronized (this.preparationMonitor)
/* 253:    */     {
/* 254:455 */       this.serviceToUse = null;
/* 255:    */     }
/* 256:    */   }
/* 257:    */   
/* 258:    */   protected boolean isPrepared()
/* 259:    */   {
/* 260:464 */     synchronized (this.preparationMonitor)
/* 261:    */     {
/* 262:465 */       return this.serviceToUse != null;
/* 263:    */     }
/* 264:    */   }
/* 265:    */   
/* 266:    */   protected final QName getPortQName()
/* 267:    */   {
/* 268:475 */     return this.portQName;
/* 269:    */   }
/* 270:    */   
/* 271:    */   protected void preparePortStub(Stub stub)
/* 272:    */   {
/* 273:494 */     String username = getUsername();
/* 274:495 */     if (username != null) {
/* 275:496 */       stub._setProperty("javax.xml.rpc.security.auth.username", username);
/* 276:    */     }
/* 277:498 */     String password = getPassword();
/* 278:499 */     if (password != null) {
/* 279:500 */       stub._setProperty("javax.xml.rpc.security.auth.password", password);
/* 280:    */     }
/* 281:502 */     String endpointAddress = getEndpointAddress();
/* 282:503 */     if (endpointAddress != null) {
/* 283:504 */       stub._setProperty("javax.xml.rpc.service.endpoint.address", endpointAddress);
/* 284:    */     }
/* 285:506 */     if (isMaintainSession()) {
/* 286:507 */       stub._setProperty("javax.xml.rpc.session.maintain", Boolean.TRUE);
/* 287:    */     }
/* 288:509 */     if (this.customPropertyMap != null) {
/* 289:510 */       for (Map.Entry<String, Object> entry : this.customPropertyMap.entrySet()) {
/* 290:511 */         stub._setProperty((String)entry.getKey(), entry.getValue());
/* 291:    */       }
/* 292:    */     }
/* 293:    */   }
/* 294:    */   
/* 295:    */   protected void postProcessPortStub(Stub stub) {}
/* 296:    */   
/* 297:    */   protected Remote getPortStub()
/* 298:    */   {
/* 299:534 */     return this.portStub;
/* 300:    */   }
/* 301:    */   
/* 302:    */   public Object invoke(MethodInvocation invocation)
/* 303:    */     throws Throwable
/* 304:    */   {
/* 305:546 */     if (AopUtils.isToStringMethod(invocation.getMethod())) {
/* 306:547 */       return "JAX-RPC proxy for port [" + getPortName() + "] of service [" + getServiceName() + "]";
/* 307:    */     }
/* 308:550 */     synchronized (this.preparationMonitor)
/* 309:    */     {
/* 310:551 */       if (!isPrepared()) {
/* 311:552 */         prepare();
/* 312:    */       }
/* 313:    */     }
/* 314:555 */     return doInvoke(invocation);
/* 315:    */   }
/* 316:    */   
/* 317:    */   protected Object doInvoke(MethodInvocation invocation)
/* 318:    */     throws Throwable
/* 319:    */   {
/* 320:570 */     Remote stub = getPortStub();
/* 321:    */     try
/* 322:    */     {
/* 323:572 */       if (stub != null)
/* 324:    */       {
/* 325:574 */         if (this.logger.isTraceEnabled()) {
/* 326:575 */           this.logger.trace("Invoking operation '" + invocation.getMethod().getName() + "' on JAX-RPC port stub");
/* 327:    */         }
/* 328:577 */         return doInvoke(invocation, stub);
/* 329:    */       }
/* 330:581 */       if (this.logger.isTraceEnabled()) {
/* 331:582 */         this.logger.trace("Invoking operation '" + invocation.getMethod().getName() + "' as JAX-RPC dynamic call");
/* 332:    */       }
/* 333:584 */       return performJaxRpcCall(invocation, this.serviceToUse);
/* 334:    */     }
/* 335:    */     catch (RemoteException ex)
/* 336:    */     {
/* 337:588 */       throw handleRemoteException(invocation.getMethod(), ex);
/* 338:    */     }
/* 339:    */     catch (SOAPFaultException ex)
/* 340:    */     {
/* 341:591 */       throw new JaxRpcSoapFaultException(ex);
/* 342:    */     }
/* 343:    */     catch (JAXRPCException ex)
/* 344:    */     {
/* 345:594 */       throw new RemoteProxyFailureException("Invalid JAX-RPC call configuration", ex);
/* 346:    */     }
/* 347:    */   }
/* 348:    */   
/* 349:    */   protected Object doInvoke(MethodInvocation invocation, Remote portStub)
/* 350:    */     throws Throwable
/* 351:    */   {
/* 352:    */     try
/* 353:    */     {
/* 354:610 */       return RmiClientInterceptorUtils.invokeRemoteMethod(invocation, portStub);
/* 355:    */     }
/* 356:    */     catch (InvocationTargetException ex)
/* 357:    */     {
/* 358:613 */       throw ex.getTargetException();
/* 359:    */     }
/* 360:    */   }
/* 361:    */   
/* 362:    */   protected Object performJaxRpcCall(MethodInvocation invocation, Service service)
/* 363:    */     throws Throwable
/* 364:    */   {
/* 365:633 */     Method method = invocation.getMethod();
/* 366:634 */     QName portQName = this.portQName;
/* 367:    */     
/* 368:    */ 
/* 369:    */ 
/* 370:638 */     Call call = null;
/* 371:639 */     synchronized (service)
/* 372:    */     {
/* 373:640 */       call = service.createCall(portQName, method.getName());
/* 374:    */     }
/* 375:644 */     prepareJaxRpcCall(call);
/* 376:    */     
/* 377:    */ 
/* 378:647 */     postProcessJaxRpcCall(call, invocation);
/* 379:    */     
/* 380:    */ 
/* 381:650 */     return call.invoke(invocation.getArguments());
/* 382:    */   }
/* 383:    */   
/* 384:    */   protected void prepareJaxRpcCall(Call call)
/* 385:    */   {
/* 386:667 */     String username = getUsername();
/* 387:668 */     if (username != null) {
/* 388:669 */       call.setProperty("javax.xml.rpc.security.auth.username", username);
/* 389:    */     }
/* 390:671 */     String password = getPassword();
/* 391:672 */     if (password != null) {
/* 392:673 */       call.setProperty("javax.xml.rpc.security.auth.password", password);
/* 393:    */     }
/* 394:675 */     String endpointAddress = getEndpointAddress();
/* 395:676 */     if (endpointAddress != null) {
/* 396:677 */       call.setTargetEndpointAddress(endpointAddress);
/* 397:    */     }
/* 398:679 */     if (isMaintainSession()) {
/* 399:680 */       call.setProperty("javax.xml.rpc.session.maintain", Boolean.TRUE);
/* 400:    */     }
/* 401:682 */     if (this.customPropertyMap != null) {
/* 402:683 */       for (Map.Entry<String, Object> entry : this.customPropertyMap.entrySet()) {
/* 403:684 */         call.setProperty((String)entry.getKey(), entry.getValue());
/* 404:    */       }
/* 405:    */     }
/* 406:    */   }
/* 407:    */   
/* 408:    */   protected void postProcessJaxRpcCall(Call call, MethodInvocation invocation) {}
/* 409:    */   
/* 410:    */   protected Throwable handleRemoteException(Method method, RemoteException ex)
/* 411:    */   {
/* 412:714 */     boolean isConnectFailure = isConnectFailure(ex);
/* 413:715 */     if ((isConnectFailure) && (this.refreshServiceAfterConnectFailure)) {
/* 414:716 */       reset();
/* 415:    */     }
/* 416:718 */     Throwable cause = ex.getCause();
/* 417:719 */     if ((cause != null) && (ReflectionUtils.declaresException(method, cause.getClass())))
/* 418:    */     {
/* 419:720 */       if (this.logger.isDebugEnabled()) {
/* 420:721 */         this.logger.debug("Rethrowing wrapped exception of type [" + cause.getClass().getName() + "] as-is");
/* 421:    */       }
/* 422:724 */       return ex.getCause();
/* 423:    */     }
/* 424:729 */     return RmiClientInterceptorUtils.convertRmiAccessException(
/* 425:730 */       method, ex, isConnectFailure, this.portQName.toString());
/* 426:    */   }
/* 427:    */   
/* 428:    */   protected boolean isConnectFailure(RemoteException ex)
/* 429:    */   {
/* 430:746 */     return (!ex.getClass().getName().contains("Fault")) && (!ex.getClass().getSuperclass().getName().contains("Fault"));
/* 431:    */   }
/* 432:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.jaxrpc.JaxRpcPortClientInterceptor
 * JD-Core Version:    0.7.0.1
 */