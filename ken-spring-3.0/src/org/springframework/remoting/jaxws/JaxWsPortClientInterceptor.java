/*   1:    */ package org.springframework.remoting.jaxws;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.InvocationTargetException;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import java.net.MalformedURLException;
/*   6:    */ import java.net.URL;
/*   7:    */ import java.util.HashMap;
/*   8:    */ import java.util.Map;
/*   9:    */ import javax.jws.WebService;
/*  10:    */ import javax.xml.namespace.QName;
/*  11:    */ import javax.xml.ws.BindingProvider;
/*  12:    */ import javax.xml.ws.ProtocolException;
/*  13:    */ import javax.xml.ws.Service;
/*  14:    */ import javax.xml.ws.WebServiceException;
/*  15:    */ import javax.xml.ws.WebServiceFeature;
/*  16:    */ import javax.xml.ws.soap.SOAPFaultException;
/*  17:    */ import org.aopalliance.intercept.MethodInterceptor;
/*  18:    */ import org.aopalliance.intercept.MethodInvocation;
/*  19:    */ import org.springframework.aop.support.AopUtils;
/*  20:    */ import org.springframework.beans.BeanUtils;
/*  21:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  22:    */ import org.springframework.beans.factory.InitializingBean;
/*  23:    */ import org.springframework.remoting.RemoteAccessException;
/*  24:    */ import org.springframework.remoting.RemoteConnectFailureException;
/*  25:    */ import org.springframework.remoting.RemoteLookupFailureException;
/*  26:    */ import org.springframework.remoting.RemoteProxyFailureException;
/*  27:    */ import org.springframework.util.Assert;
/*  28:    */ import org.springframework.util.ClassUtils;
/*  29:    */ import org.springframework.util.StringUtils;
/*  30:    */ 
/*  31:    */ public class JaxWsPortClientInterceptor
/*  32:    */   extends LocalJaxWsServiceFactory
/*  33:    */   implements MethodInterceptor, BeanClassLoaderAware, InitializingBean
/*  34:    */ {
/*  35:    */   private Service jaxWsService;
/*  36:    */   private String portName;
/*  37:    */   private String username;
/*  38:    */   private String password;
/*  39:    */   private String endpointAddress;
/*  40:    */   private boolean maintainSession;
/*  41:    */   private boolean useSoapAction;
/*  42:    */   private String soapActionUri;
/*  43:    */   private Map<String, Object> customProperties;
/*  44:    */   private Object[] webServiceFeatures;
/*  45:    */   private Class<?> serviceInterface;
/*  46: 90 */   private boolean lookupServiceOnStartup = true;
/*  47: 92 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*  48:    */   private QName portQName;
/*  49:    */   private Object portStub;
/*  50: 98 */   private final Object preparationMonitor = new Object();
/*  51:    */   
/*  52:    */   public void setJaxWsService(Service jaxWsService)
/*  53:    */   {
/*  54:111 */     this.jaxWsService = jaxWsService;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public Service getJaxWsService()
/*  58:    */   {
/*  59:118 */     return this.jaxWsService;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void setPortName(String portName)
/*  63:    */   {
/*  64:126 */     this.portName = portName;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public String getPortName()
/*  68:    */   {
/*  69:133 */     return this.portName;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setUsername(String username)
/*  73:    */   {
/*  74:141 */     this.username = username;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public String getUsername()
/*  78:    */   {
/*  79:148 */     return this.username;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void setPassword(String password)
/*  83:    */   {
/*  84:156 */     this.password = password;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public String getPassword()
/*  88:    */   {
/*  89:163 */     return this.password;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void setEndpointAddress(String endpointAddress)
/*  93:    */   {
/*  94:171 */     this.endpointAddress = endpointAddress;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public String getEndpointAddress()
/*  98:    */   {
/*  99:178 */     return this.endpointAddress;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void setMaintainSession(boolean maintainSession)
/* 103:    */   {
/* 104:186 */     this.maintainSession = maintainSession;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public boolean isMaintainSession()
/* 108:    */   {
/* 109:193 */     return this.maintainSession;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void setUseSoapAction(boolean useSoapAction)
/* 113:    */   {
/* 114:201 */     this.useSoapAction = useSoapAction;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public boolean isUseSoapAction()
/* 118:    */   {
/* 119:208 */     return this.useSoapAction;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public void setSoapActionUri(String soapActionUri)
/* 123:    */   {
/* 124:216 */     this.soapActionUri = soapActionUri;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public String getSoapActionUri()
/* 128:    */   {
/* 129:223 */     return this.soapActionUri;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public void setCustomProperties(Map<String, Object> customProperties)
/* 133:    */   {
/* 134:233 */     this.customProperties = customProperties;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public Map<String, Object> getCustomProperties()
/* 138:    */   {
/* 139:244 */     if (this.customProperties == null) {
/* 140:245 */       this.customProperties = new HashMap();
/* 141:    */     }
/* 142:247 */     return this.customProperties;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public void addCustomProperty(String name, Object value)
/* 146:    */   {
/* 147:257 */     getCustomProperties().put(name, value);
/* 148:    */   }
/* 149:    */   
/* 150:    */   public void setWebServiceFeatures(Object[] webServiceFeatures)
/* 151:    */   {
/* 152:266 */     this.webServiceFeatures = webServiceFeatures;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void setServiceInterface(Class<?> serviceInterface)
/* 156:    */   {
/* 157:273 */     if ((serviceInterface != null) && (!serviceInterface.isInterface())) {
/* 158:274 */       throw new IllegalArgumentException("'serviceInterface' must be an interface");
/* 159:    */     }
/* 160:276 */     this.serviceInterface = serviceInterface;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public Class<?> getServiceInterface()
/* 164:    */   {
/* 165:283 */     return this.serviceInterface;
/* 166:    */   }
/* 167:    */   
/* 168:    */   public void setLookupServiceOnStartup(boolean lookupServiceOnStartup)
/* 169:    */   {
/* 170:293 */     this.lookupServiceOnStartup = lookupServiceOnStartup;
/* 171:    */   }
/* 172:    */   
/* 173:    */   public void setBeanClassLoader(ClassLoader classLoader)
/* 174:    */   {
/* 175:303 */     this.beanClassLoader = classLoader;
/* 176:    */   }
/* 177:    */   
/* 178:    */   protected ClassLoader getBeanClassLoader()
/* 179:    */   {
/* 180:310 */     return this.beanClassLoader;
/* 181:    */   }
/* 182:    */   
/* 183:    */   public void afterPropertiesSet()
/* 184:    */   {
/* 185:315 */     if (this.lookupServiceOnStartup) {
/* 186:316 */       prepare();
/* 187:    */     }
/* 188:    */   }
/* 189:    */   
/* 190:    */   public void prepare()
/* 191:    */   {
/* 192:324 */     Class<?> ifc = getServiceInterface();
/* 193:325 */     if (ifc == null) {
/* 194:326 */       throw new IllegalArgumentException("Property 'serviceInterface' is required");
/* 195:    */     }
/* 196:328 */     WebService ann = (WebService)ifc.getAnnotation(WebService.class);
/* 197:329 */     if (ann != null) {
/* 198:330 */       applyDefaultsFromAnnotation(ann);
/* 199:    */     }
/* 200:332 */     Service serviceToUse = getJaxWsService();
/* 201:333 */     if (serviceToUse == null) {
/* 202:334 */       serviceToUse = createJaxWsService();
/* 203:    */     }
/* 204:336 */     this.portQName = getQName(getPortName() != null ? getPortName() : getServiceInterface().getName());
/* 205:337 */     Object stub = getPortStub(serviceToUse, getPortName() != null ? this.portQName : null);
/* 206:338 */     preparePortStub(stub);
/* 207:339 */     this.portStub = stub;
/* 208:    */   }
/* 209:    */   
/* 210:    */   protected void applyDefaultsFromAnnotation(WebService ann)
/* 211:    */   {
/* 212:350 */     if (getWsdlDocumentUrl() == null)
/* 213:    */     {
/* 214:351 */       String wsdl = ann.wsdlLocation();
/* 215:352 */       if (StringUtils.hasText(wsdl)) {
/* 216:    */         try
/* 217:    */         {
/* 218:354 */           setWsdlDocumentUrl(new URL(wsdl));
/* 219:    */         }
/* 220:    */         catch (MalformedURLException ex)
/* 221:    */         {
/* 222:357 */           throw new IllegalStateException(
/* 223:358 */             "Encountered invalid @Service wsdlLocation value [" + wsdl + "]", ex);
/* 224:    */         }
/* 225:    */       }
/* 226:    */     }
/* 227:362 */     if (getNamespaceUri() == null)
/* 228:    */     {
/* 229:363 */       String ns = ann.targetNamespace();
/* 230:364 */       if (StringUtils.hasText(ns)) {
/* 231:365 */         setNamespaceUri(ns);
/* 232:    */       }
/* 233:    */     }
/* 234:368 */     if (getServiceName() == null)
/* 235:    */     {
/* 236:369 */       String sn = ann.serviceName();
/* 237:370 */       if (StringUtils.hasText(sn)) {
/* 238:371 */         setServiceName(sn);
/* 239:    */       }
/* 240:    */     }
/* 241:374 */     if (getPortName() == null)
/* 242:    */     {
/* 243:375 */       String pn = ann.portName();
/* 244:376 */       if (StringUtils.hasText(pn)) {
/* 245:377 */         setPortName(pn);
/* 246:    */       }
/* 247:    */     }
/* 248:    */   }
/* 249:    */   
/* 250:    */   protected boolean isPrepared()
/* 251:    */   {
/* 252:387 */     synchronized (this.preparationMonitor)
/* 253:    */     {
/* 254:388 */       return this.portStub != null;
/* 255:    */     }
/* 256:    */   }
/* 257:    */   
/* 258:    */   protected final QName getPortQName()
/* 259:    */   {
/* 260:398 */     return this.portQName;
/* 261:    */   }
/* 262:    */   
/* 263:    */   protected Object getPortStub(Service service, QName portQName)
/* 264:    */   {
/* 265:409 */     if (this.webServiceFeatures != null) {
/* 266:410 */       return new FeaturePortProvider(null).getPortStub(service, portQName, this.webServiceFeatures);
/* 267:    */     }
/* 268:413 */     return portQName != null ? service.getPort(portQName, getServiceInterface()) : 
/* 269:414 */       service.getPort(getServiceInterface());
/* 270:    */   }
/* 271:    */   
/* 272:    */   protected void preparePortStub(Object stub)
/* 273:    */   {
/* 274:429 */     Map<String, Object> stubProperties = new HashMap();
/* 275:430 */     String username = getUsername();
/* 276:431 */     if (username != null) {
/* 277:432 */       stubProperties.put("javax.xml.ws.security.auth.username", username);
/* 278:    */     }
/* 279:434 */     String password = getPassword();
/* 280:435 */     if (password != null) {
/* 281:436 */       stubProperties.put("javax.xml.ws.security.auth.password", password);
/* 282:    */     }
/* 283:438 */     String endpointAddress = getEndpointAddress();
/* 284:439 */     if (endpointAddress != null) {
/* 285:440 */       stubProperties.put("javax.xml.ws.service.endpoint.address", endpointAddress);
/* 286:    */     }
/* 287:442 */     if (isMaintainSession()) {
/* 288:443 */       stubProperties.put("javax.xml.ws.session.maintain", Boolean.TRUE);
/* 289:    */     }
/* 290:445 */     if (isUseSoapAction()) {
/* 291:446 */       stubProperties.put("javax.xml.ws.soap.http.soapaction.use", Boolean.TRUE);
/* 292:    */     }
/* 293:448 */     String soapActionUri = getSoapActionUri();
/* 294:449 */     if (soapActionUri != null) {
/* 295:450 */       stubProperties.put("javax.xml.ws.soap.http.soapaction.uri", soapActionUri);
/* 296:    */     }
/* 297:452 */     stubProperties.putAll(getCustomProperties());
/* 298:453 */     if (!stubProperties.isEmpty())
/* 299:    */     {
/* 300:454 */       if (!(stub instanceof BindingProvider)) {
/* 301:455 */         throw new RemoteLookupFailureException("Port stub of class [" + stub.getClass().getName() + 
/* 302:456 */           "] is not a customizable JAX-WS stub: it does not implement interface [javax.xml.ws.BindingProvider]");
/* 303:    */       }
/* 304:458 */       ((BindingProvider)stub).getRequestContext().putAll(stubProperties);
/* 305:    */     }
/* 306:    */   }
/* 307:    */   
/* 308:    */   protected Object getPortStub()
/* 309:    */   {
/* 310:467 */     return this.portStub;
/* 311:    */   }
/* 312:    */   
/* 313:    */   public Object invoke(MethodInvocation invocation)
/* 314:    */     throws Throwable
/* 315:    */   {
/* 316:472 */     if (AopUtils.isToStringMethod(invocation.getMethod())) {
/* 317:473 */       return "JAX-WS proxy for port [" + getPortName() + "] of service [" + getServiceName() + "]";
/* 318:    */     }
/* 319:476 */     synchronized (this.preparationMonitor)
/* 320:    */     {
/* 321:477 */       if (!isPrepared()) {
/* 322:478 */         prepare();
/* 323:    */       }
/* 324:    */     }
/* 325:481 */     return doInvoke(invocation);
/* 326:    */   }
/* 327:    */   
/* 328:    */   protected Object doInvoke(MethodInvocation invocation)
/* 329:    */     throws Throwable
/* 330:    */   {
/* 331:    */     try
/* 332:    */     {
/* 333:494 */       return doInvoke(invocation, getPortStub());
/* 334:    */     }
/* 335:    */     catch (SOAPFaultException ex)
/* 336:    */     {
/* 337:497 */       throw new JaxWsSoapFaultException(ex);
/* 338:    */     }
/* 339:    */     catch (ProtocolException ex)
/* 340:    */     {
/* 341:500 */       throw new RemoteConnectFailureException(
/* 342:501 */         "Could not connect to remote service [" + getEndpointAddress() + "]", ex);
/* 343:    */     }
/* 344:    */     catch (WebServiceException ex)
/* 345:    */     {
/* 346:504 */       throw new RemoteAccessException(
/* 347:505 */         "Could not access remote service at [" + getEndpointAddress() + "]", ex);
/* 348:    */     }
/* 349:    */   }
/* 350:    */   
/* 351:    */   protected Object doInvoke(MethodInvocation invocation, Object portStub)
/* 352:    */     throws Throwable
/* 353:    */   {
/* 354:518 */     Method method = invocation.getMethod();
/* 355:    */     try
/* 356:    */     {
/* 357:520 */       return method.invoke(portStub, invocation.getArguments());
/* 358:    */     }
/* 359:    */     catch (InvocationTargetException ex)
/* 360:    */     {
/* 361:523 */       throw ex.getTargetException();
/* 362:    */     }
/* 363:    */     catch (Throwable ex)
/* 364:    */     {
/* 365:526 */       throw new RemoteProxyFailureException("Invocation of stub method failed: " + method, ex);
/* 366:    */     }
/* 367:    */   }
/* 368:    */   
/* 369:    */   private class FeaturePortProvider
/* 370:    */   {
/* 371:    */     private FeaturePortProvider() {}
/* 372:    */     
/* 373:    */     public Object getPortStub(Service service, QName portQName, Object[] features)
/* 374:    */     {
/* 375:537 */       WebServiceFeature[] wsFeatures = new WebServiceFeature[features.length];
/* 376:538 */       for (int i = 0; i < features.length; i++) {
/* 377:539 */         wsFeatures[i] = convertWebServiceFeature(features[i]);
/* 378:    */       }
/* 379:541 */       return portQName != null ? service.getPort(portQName, JaxWsPortClientInterceptor.this.getServiceInterface(), wsFeatures) : 
/* 380:542 */         service.getPort(JaxWsPortClientInterceptor.this.getServiceInterface(), wsFeatures);
/* 381:    */     }
/* 382:    */     
/* 383:    */     private WebServiceFeature convertWebServiceFeature(Object feature)
/* 384:    */     {
/* 385:546 */       Assert.notNull(feature, "WebServiceFeature specification object must not be null");
/* 386:547 */       if ((feature instanceof WebServiceFeature)) {
/* 387:548 */         return (WebServiceFeature)feature;
/* 388:    */       }
/* 389:550 */       if ((feature instanceof Class)) {
/* 390:551 */         return (WebServiceFeature)BeanUtils.instantiate((Class)feature);
/* 391:    */       }
/* 392:553 */       if ((feature instanceof String)) {
/* 393:    */         try
/* 394:    */         {
/* 395:555 */           Class<?> featureClass = JaxWsPortClientInterceptor.this.getBeanClassLoader().loadClass((String)feature);
/* 396:556 */           return (WebServiceFeature)BeanUtils.instantiate(featureClass);
/* 397:    */         }
/* 398:    */         catch (ClassNotFoundException localClassNotFoundException)
/* 399:    */         {
/* 400:559 */           throw new IllegalArgumentException("Could not load WebServiceFeature class [" + feature + "]");
/* 401:    */         }
/* 402:    */       }
/* 403:563 */       throw new IllegalArgumentException("Unknown WebServiceFeature specification type: " + feature.getClass());
/* 404:    */     }
/* 405:    */   }
/* 406:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.jaxws.JaxWsPortClientInterceptor
 * JD-Core Version:    0.7.0.1
 */