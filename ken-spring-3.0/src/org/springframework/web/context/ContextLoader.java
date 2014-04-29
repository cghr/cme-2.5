/*   1:    */ package org.springframework.web.context;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Map;
/*   9:    */ import java.util.Properties;
/*  10:    */ import java.util.concurrent.ConcurrentHashMap;
/*  11:    */ import javax.servlet.ServletContext;
/*  12:    */ import org.apache.commons.logging.Log;
/*  13:    */ import org.apache.commons.logging.LogFactory;
/*  14:    */ import org.springframework.beans.BeanUtils;
/*  15:    */ import org.springframework.beans.factory.access.BeanFactoryLocator;
/*  16:    */ import org.springframework.beans.factory.access.BeanFactoryReference;
/*  17:    */ import org.springframework.context.ApplicationContext;
/*  18:    */ import org.springframework.context.ApplicationContextException;
/*  19:    */ import org.springframework.context.ApplicationContextInitializer;
/*  20:    */ import org.springframework.context.ConfigurableApplicationContext;
/*  21:    */ import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
/*  22:    */ import org.springframework.core.GenericTypeResolver;
/*  23:    */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*  24:    */ import org.springframework.core.io.ClassPathResource;
/*  25:    */ import org.springframework.core.io.support.PropertiesLoaderUtils;
/*  26:    */ import org.springframework.util.Assert;
/*  27:    */ import org.springframework.util.ClassUtils;
/*  28:    */ import org.springframework.util.ObjectUtils;
/*  29:    */ import org.springframework.util.StringUtils;
/*  30:    */ 
/*  31:    */ public class ContextLoader
/*  32:    */ {
/*  33:    */   public static final String CONTEXT_CLASS_PARAM = "contextClass";
/*  34:    */   public static final String CONTEXT_ID_PARAM = "contextId";
/*  35:    */   public static final String CONTEXT_INITIALIZER_CLASSES_PARAM = "contextInitializerClasses";
/*  36:    */   public static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";
/*  37:    */   public static final String LOCATOR_FACTORY_SELECTOR_PARAM = "locatorFactorySelector";
/*  38:    */   public static final String LOCATOR_FACTORY_KEY_PARAM = "parentContextKey";
/*  39:    */   private static final String DEFAULT_STRATEGIES_PATH = "ContextLoader.properties";
/*  40:    */   private static final Properties defaultStrategies;
/*  41:    */   
/*  42:    */   static
/*  43:    */   {
/*  44:    */     try
/*  45:    */     {
/*  46:163 */       ClassPathResource resource = new ClassPathResource("ContextLoader.properties", ContextLoader.class);
/*  47:164 */       defaultStrategies = PropertiesLoaderUtils.loadProperties(resource);
/*  48:    */     }
/*  49:    */     catch (IOException ex)
/*  50:    */     {
/*  51:167 */       throw new IllegalStateException("Could not load 'ContextLoader.properties': " + ex.getMessage());
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55:176 */   private static final Map<ClassLoader, WebApplicationContext> currentContextPerThread = new ConcurrentHashMap(1);
/*  56:    */   private static volatile WebApplicationContext currentContext;
/*  57:    */   private WebApplicationContext context;
/*  58:    */   private BeanFactoryReference parentContextRef;
/*  59:    */   
/*  60:    */   public ContextLoader() {}
/*  61:    */   
/*  62:    */   public ContextLoader(WebApplicationContext context)
/*  63:    */   {
/*  64:248 */     this.context = context;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public WebApplicationContext initWebApplicationContext(ServletContext servletContext)
/*  68:    */   {
/*  69:263 */     if (servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE) != null) {
/*  70:264 */       throw new IllegalStateException(
/*  71:265 */         "Cannot initialize context because there is already a root application context present - check whether you have multiple ContextLoader* definitions in your web.xml!");
/*  72:    */     }
/*  73:269 */     Log logger = LogFactory.getLog(ContextLoader.class);
/*  74:270 */     servletContext.log("Initializing Spring root WebApplicationContext");
/*  75:271 */     if (logger.isInfoEnabled()) {
/*  76:272 */       logger.info("Root WebApplicationContext: initialization started");
/*  77:    */     }
/*  78:274 */     long startTime = System.currentTimeMillis();
/*  79:    */     try
/*  80:    */     {
/*  81:279 */       if (this.context == null) {
/*  82:280 */         this.context = createWebApplicationContext(servletContext);
/*  83:    */       }
/*  84:282 */       if ((this.context instanceof ConfigurableWebApplicationContext)) {
/*  85:283 */         configureAndRefreshWebApplicationContext((ConfigurableWebApplicationContext)this.context, servletContext);
/*  86:    */       }
/*  87:285 */       servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);
/*  88:    */       
/*  89:287 */       ClassLoader ccl = Thread.currentThread().getContextClassLoader();
/*  90:288 */       if (ccl == ContextLoader.class.getClassLoader()) {
/*  91:289 */         currentContext = this.context;
/*  92:291 */       } else if (ccl != null) {
/*  93:292 */         currentContextPerThread.put(ccl, this.context);
/*  94:    */       }
/*  95:295 */       if (logger.isDebugEnabled()) {
/*  96:296 */         logger.debug("Published root WebApplicationContext as ServletContext attribute with name [" + 
/*  97:297 */           WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE + "]");
/*  98:    */       }
/*  99:299 */       if (logger.isInfoEnabled())
/* 100:    */       {
/* 101:300 */         long elapsedTime = System.currentTimeMillis() - startTime;
/* 102:301 */         logger.info("Root WebApplicationContext: initialization completed in " + elapsedTime + " ms");
/* 103:    */       }
/* 104:304 */       return this.context;
/* 105:    */     }
/* 106:    */     catch (RuntimeException ex)
/* 107:    */     {
/* 108:307 */       logger.error("Context initialization failed", ex);
/* 109:308 */       servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, ex);
/* 110:309 */       throw ex;
/* 111:    */     }
/* 112:    */     catch (Error err)
/* 113:    */     {
/* 114:312 */       logger.error("Context initialization failed", err);
/* 115:313 */       servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, err);
/* 116:314 */       throw err;
/* 117:    */     }
/* 118:    */   }
/* 119:    */   
/* 120:    */   protected WebApplicationContext createWebApplicationContext(ServletContext sc)
/* 121:    */   {
/* 122:332 */     Class<?> contextClass = determineContextClass(sc);
/* 123:333 */     if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass)) {
/* 124:334 */       throw new ApplicationContextException("Custom context class [" + contextClass.getName() + 
/* 125:335 */         "] is not of type [" + ConfigurableWebApplicationContext.class.getName() + "]");
/* 126:    */     }
/* 127:337 */     ConfigurableWebApplicationContext wac = 
/* 128:338 */       (ConfigurableWebApplicationContext)BeanUtils.instantiateClass(contextClass);
/* 129:339 */     return wac;
/* 130:    */   }
/* 131:    */   
/* 132:    */   @Deprecated
/* 133:    */   protected WebApplicationContext createWebApplicationContext(ServletContext sc, ApplicationContext parent)
/* 134:    */   {
/* 135:349 */     return createWebApplicationContext(sc);
/* 136:    */   }
/* 137:    */   
/* 138:    */   protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac, ServletContext sc)
/* 139:    */   {
/* 140:353 */     if (ObjectUtils.identityToString(wac).equals(wac.getId()))
/* 141:    */     {
/* 142:356 */       String idParam = sc.getInitParameter("contextId");
/* 143:357 */       if (idParam != null) {
/* 144:358 */         wac.setId(idParam);
/* 145:362 */       } else if ((sc.getMajorVersion() == 2) && (sc.getMinorVersion() < 5)) {
/* 146:364 */         wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX + 
/* 147:365 */           ObjectUtils.getDisplayString(sc.getServletContextName()));
/* 148:    */       } else {
/* 149:368 */         wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX + 
/* 150:369 */           ObjectUtils.getDisplayString(sc.getContextPath()));
/* 151:    */       }
/* 152:    */     }
/* 153:375 */     ApplicationContext parent = loadParentContext(sc);
/* 154:    */     
/* 155:377 */     wac.setParent(parent);
/* 156:378 */     wac.setServletContext(sc);
/* 157:379 */     wac.setConfigLocation(sc.getInitParameter("contextConfigLocation"));
/* 158:380 */     customizeContext(sc, wac);
/* 159:381 */     wac.refresh();
/* 160:    */   }
/* 161:    */   
/* 162:    */   protected Class<?> determineContextClass(ServletContext servletContext)
/* 163:    */   {
/* 164:393 */     String contextClassName = servletContext.getInitParameter("contextClass");
/* 165:394 */     if (contextClassName != null) {
/* 166:    */       try
/* 167:    */       {
/* 168:396 */         return ClassUtils.forName(contextClassName, ClassUtils.getDefaultClassLoader());
/* 169:    */       }
/* 170:    */       catch (ClassNotFoundException ex)
/* 171:    */       {
/* 172:399 */         throw new ApplicationContextException(
/* 173:400 */           "Failed to load custom context class [" + contextClassName + "]", ex);
/* 174:    */       }
/* 175:    */     }
/* 176:404 */     contextClassName = defaultStrategies.getProperty(WebApplicationContext.class.getName());
/* 177:    */     try
/* 178:    */     {
/* 179:406 */       return ClassUtils.forName(contextClassName, ContextLoader.class.getClassLoader());
/* 180:    */     }
/* 181:    */     catch (ClassNotFoundException ex)
/* 182:    */     {
/* 183:409 */       throw new ApplicationContextException(
/* 184:410 */         "Failed to load default context class [" + contextClassName + "]", ex);
/* 185:    */     }
/* 186:    */   }
/* 187:    */   
/* 188:    */   protected List<Class<ApplicationContextInitializer<ConfigurableApplicationContext>>> determineContextInitializerClasses(ServletContext servletContext)
/* 189:    */   {
/* 190:424 */     String classNames = servletContext.getInitParameter("contextInitializerClasses");
/* 191:425 */     List<Class<ApplicationContextInitializer<ConfigurableApplicationContext>>> classes = 
/* 192:426 */       new ArrayList();
/* 193:427 */     if (classNames != null) {
/* 194:428 */       for (String className : StringUtils.tokenizeToStringArray(classNames, ",")) {
/* 195:    */         try
/* 196:    */         {
/* 197:430 */           Class<?> clazz = ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
/* 198:431 */           Assert.isAssignable(ApplicationContextInitializer.class, clazz, 
/* 199:432 */             "class [" + className + "] must implement ApplicationContextInitializer");
/* 200:433 */           classes.add(clazz);
/* 201:    */         }
/* 202:    */         catch (ClassNotFoundException ex)
/* 203:    */         {
/* 204:436 */           throw new ApplicationContextException(
/* 205:437 */             "Failed to load context initializer class [" + className + "]", ex);
/* 206:    */         }
/* 207:    */       }
/* 208:    */     }
/* 209:441 */     return classes;
/* 210:    */   }
/* 211:    */   
/* 212:    */   protected void customizeContext(ServletContext servletContext, ConfigurableWebApplicationContext applicationContext)
/* 213:    */   {
/* 214:463 */     ArrayList<ApplicationContextInitializer<ConfigurableApplicationContext>> initializerInstances = 
/* 215:464 */       new ArrayList();
/* 216:    */     
/* 217:    */ 
/* 218:467 */     Iterator localIterator = determineContextInitializerClasses(servletContext).iterator();
/* 219:466 */     while (localIterator.hasNext())
/* 220:    */     {
/* 221:467 */       Class<ApplicationContextInitializer<ConfigurableApplicationContext>> initializerClass = (Class)localIterator.next();
/* 222:468 */       Class<?> contextClass = applicationContext.getClass();
/* 223:469 */       Class<?> initializerContextClass = 
/* 224:470 */         GenericTypeResolver.resolveTypeArgument(initializerClass, ApplicationContextInitializer.class);
/* 225:471 */       Assert.isAssignable(initializerContextClass, contextClass, String.format(
/* 226:472 */         "Could not add context initializer [%s] as its generic parameter [%s] is not assignable from the type of application context used by this context loader [%s]", new Object[] {
/* 227:    */         
/* 228:474 */         initializerClass.getName(), initializerContextClass, contextClass }));
/* 229:475 */       initializerInstances.add((ApplicationContextInitializer)BeanUtils.instantiateClass(initializerClass));
/* 230:    */     }
/* 231:478 */     Collections.sort(initializerInstances, new AnnotationAwareOrderComparator());
/* 232:480 */     for (ApplicationContextInitializer<ConfigurableApplicationContext> initializer : initializerInstances) {
/* 233:481 */       initializer.initialize(applicationContext);
/* 234:    */     }
/* 235:    */   }
/* 236:    */   
/* 237:    */   protected ApplicationContext loadParentContext(ServletContext servletContext)
/* 238:    */   {
/* 239:506 */     ApplicationContext parentContext = null;
/* 240:507 */     String locatorFactorySelector = servletContext.getInitParameter("locatorFactorySelector");
/* 241:508 */     String parentContextKey = servletContext.getInitParameter("parentContextKey");
/* 242:510 */     if (parentContextKey != null)
/* 243:    */     {
/* 244:512 */       BeanFactoryLocator locator = ContextSingletonBeanFactoryLocator.getInstance(locatorFactorySelector);
/* 245:513 */       Log logger = LogFactory.getLog(ContextLoader.class);
/* 246:514 */       if (logger.isDebugEnabled()) {
/* 247:515 */         logger.debug("Getting parent context definition: using parent context key of '" + 
/* 248:516 */           parentContextKey + "' with BeanFactoryLocator");
/* 249:    */       }
/* 250:518 */       this.parentContextRef = locator.useBeanFactory(parentContextKey);
/* 251:519 */       parentContext = (ApplicationContext)this.parentContextRef.getFactory();
/* 252:    */     }
/* 253:522 */     return parentContext;
/* 254:    */   }
/* 255:    */   
/* 256:    */   public void closeWebApplicationContext(ServletContext servletContext)
/* 257:    */   {
/* 258:535 */     servletContext.log("Closing Spring root WebApplicationContext");
/* 259:    */     ClassLoader ccl;
/* 260:    */     try
/* 261:    */     {
/* 262:537 */       if ((this.context instanceof ConfigurableWebApplicationContext)) {
/* 263:538 */         ((ConfigurableWebApplicationContext)this.context).close();
/* 264:    */       }
/* 265:    */     }
/* 266:    */     finally
/* 267:    */     {
/* 268:542 */       ClassLoader ccl = Thread.currentThread().getContextClassLoader();
/* 269:543 */       if (ccl == ContextLoader.class.getClassLoader()) {
/* 270:544 */         currentContext = null;
/* 271:546 */       } else if (ccl != null) {
/* 272:547 */         currentContextPerThread.remove(ccl);
/* 273:    */       }
/* 274:549 */       servletContext.removeAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
/* 275:550 */       if (this.parentContextRef != null) {
/* 276:551 */         this.parentContextRef.release();
/* 277:    */       }
/* 278:    */     }
/* 279:    */   }
/* 280:    */   
/* 281:    */   public static WebApplicationContext getCurrentWebApplicationContext()
/* 282:    */   {
/* 283:566 */     ClassLoader ccl = Thread.currentThread().getContextClassLoader();
/* 284:567 */     if (ccl != null)
/* 285:    */     {
/* 286:568 */       WebApplicationContext ccpt = (WebApplicationContext)currentContextPerThread.get(ccl);
/* 287:569 */       if (ccpt != null) {
/* 288:570 */         return ccpt;
/* 289:    */       }
/* 290:    */     }
/* 291:573 */     return currentContext;
/* 292:    */   }
/* 293:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.ContextLoader
 * JD-Core Version:    0.7.0.1
 */