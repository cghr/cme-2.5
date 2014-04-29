/*   1:    */ package org.springframework.web.servlet.view.tiles2;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.lang.reflect.Constructor;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import java.net.URL;
/*   7:    */ import java.util.HashMap;
/*   8:    */ import java.util.LinkedList;
/*   9:    */ import java.util.List;
/*  10:    */ import java.util.Map;
/*  11:    */ import java.util.Properties;
/*  12:    */ import javax.servlet.ServletContext;
/*  13:    */ import javax.servlet.jsp.JspApplicationContext;
/*  14:    */ import javax.servlet.jsp.JspFactory;
/*  15:    */ import org.apache.commons.logging.Log;
/*  16:    */ import org.apache.commons.logging.LogFactory;
/*  17:    */ import org.apache.tiles.TilesApplicationContext;
/*  18:    */ import org.apache.tiles.TilesContainer;
/*  19:    */ import org.apache.tiles.TilesException;
/*  20:    */ import org.apache.tiles.awareness.TilesApplicationContextAware;
/*  21:    */ import org.apache.tiles.context.TilesRequestContextFactory;
/*  22:    */ import org.apache.tiles.definition.DefinitionsFactory;
/*  23:    */ import org.apache.tiles.definition.DefinitionsFactoryException;
/*  24:    */ import org.apache.tiles.definition.DefinitionsReader;
/*  25:    */ import org.apache.tiles.definition.Refreshable;
/*  26:    */ import org.apache.tiles.definition.dao.BaseLocaleUrlDefinitionDAO;
/*  27:    */ import org.apache.tiles.definition.dao.CachingLocaleUrlDefinitionDAO;
/*  28:    */ import org.apache.tiles.definition.digester.DigesterDefinitionsReader;
/*  29:    */ import org.apache.tiles.evaluator.AttributeEvaluator;
/*  30:    */ import org.apache.tiles.evaluator.el.ELAttributeEvaluator;
/*  31:    */ import org.apache.tiles.evaluator.impl.DirectAttributeEvaluator;
/*  32:    */ import org.apache.tiles.factory.AbstractTilesContainerFactory;
/*  33:    */ import org.apache.tiles.factory.BasicTilesContainerFactory;
/*  34:    */ import org.apache.tiles.impl.BasicTilesContainer;
/*  35:    */ import org.apache.tiles.impl.mgmt.CachingTilesContainer;
/*  36:    */ import org.apache.tiles.locale.LocaleResolver;
/*  37:    */ import org.apache.tiles.preparer.BasicPreparerFactory;
/*  38:    */ import org.apache.tiles.preparer.PreparerFactory;
/*  39:    */ import org.apache.tiles.renderer.RendererFactory;
/*  40:    */ import org.apache.tiles.servlet.context.ServletUtil;
/*  41:    */ import org.apache.tiles.startup.BasicTilesInitializer;
/*  42:    */ import org.apache.tiles.startup.TilesInitializer;
/*  43:    */ import org.springframework.beans.BeanUtils;
/*  44:    */ import org.springframework.beans.BeanWrapper;
/*  45:    */ import org.springframework.beans.PropertyAccessorFactory;
/*  46:    */ import org.springframework.beans.factory.DisposableBean;
/*  47:    */ import org.springframework.beans.factory.InitializingBean;
/*  48:    */ import org.springframework.util.ClassUtils;
/*  49:    */ import org.springframework.util.CollectionUtils;
/*  50:    */ import org.springframework.util.ReflectionUtils;
/*  51:    */ import org.springframework.util.StringUtils;
/*  52:    */ import org.springframework.web.context.ServletContextAware;
/*  53:    */ 
/*  54:    */ public class TilesConfigurer
/*  55:    */   implements ServletContextAware, InitializingBean, DisposableBean
/*  56:    */ {
/*  57:    */   static
/*  58:    */   {
/*  59:117 */     if (ClassUtils.isPresent(
/*  60:118 */       "javax.servlet.jsp.JspApplicationContext", TilesConfigurer.class.getClassLoader())) {}
/*  61:    */   }
/*  62:    */   
/*  63:119 */   private static final boolean tilesElPresent = ClassUtils.isPresent(
/*  64:120 */     "org.apache.tiles.evaluator.el.ELAttributeEvaluator", TilesConfigurer.class.getClassLoader());
/*  65:122 */   private static final boolean tiles22Present = ClassUtils.isPresent(
/*  66:123 */     "org.apache.tiles.evaluator.AttributeEvaluatorFactory", TilesConfigurer.class.getClassLoader());
/*  67:126 */   protected final Log logger = LogFactory.getLog(getClass());
/*  68:    */   private TilesInitializer tilesInitializer;
/*  69:130 */   private boolean overrideLocaleResolver = false;
/*  70:    */   private String[] definitions;
/*  71:134 */   private boolean checkRefresh = false;
/*  72:136 */   private boolean validateDefinitions = true;
/*  73:    */   private Class<? extends DefinitionsFactory> definitionsFactoryClass;
/*  74:    */   private Class<? extends PreparerFactory> preparerFactoryClass;
/*  75:142 */   private boolean useMutableTilesContainer = false;
/*  76:144 */   private final Map<String, String> tilesPropertyMap = new HashMap();
/*  77:    */   private ServletContext servletContext;
/*  78:    */   
/*  79:    */   public TilesConfigurer()
/*  80:    */   {
/*  81:151 */     StringBuilder sb = new StringBuilder("org.apache.tiles.servlet.context.ServletTilesRequestContextFactory");
/*  82:152 */     addClassNameIfPresent(sb, "org.apache.tiles.portlet.context.PortletTilesRequestContextFactory");
/*  83:153 */     addClassNameIfPresent(sb, "org.apache.tiles.jsp.context.JspTilesRequestContextFactory");
/*  84:154 */     this.tilesPropertyMap.put("org.apache.tiles.context.ChainedTilesRequestContextFactory.FACTORY_CLASS_NAMES", sb.toString());
/*  85:    */     
/*  86:    */ 
/*  87:157 */     this.tilesPropertyMap.put("org.apache.tiles.context.AbstractTilesApplicationContextFactory", 
/*  88:158 */       SpringTilesApplicationContextFactory.class.getName());
/*  89:159 */     this.tilesPropertyMap.put("org.apache.tiles.locale.LocaleResolver", 
/*  90:160 */       SpringLocaleResolver.class.getName());
/*  91:161 */     this.tilesPropertyMap.put("org.apache.tiles.preparer.PreparerFactory", 
/*  92:162 */       BasicPreparerFactory.class.getName());
/*  93:163 */     this.tilesPropertyMap.put("org.apache.tiles.factory.TilesContainerFactory.MUTABLE", 
/*  94:164 */       Boolean.toString(false));
/*  95:    */   }
/*  96:    */   
/*  97:    */   private static void addClassNameIfPresent(StringBuilder sb, String className)
/*  98:    */   {
/*  99:168 */     if (ClassUtils.isPresent(className, TilesConfigurer.class.getClassLoader())) {
/* 100:169 */       sb.append(',').append(className);
/* 101:    */     }
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void setTilesInitializer(TilesInitializer tilesInitializer)
/* 105:    */   {
/* 106:183 */     this.tilesInitializer = tilesInitializer;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public void setCompleteAutoload(boolean completeAutoload)
/* 110:    */   {
/* 111:197 */     if (completeAutoload) {
/* 112:    */       try
/* 113:    */       {
/* 114:199 */         Class clazz = getClass().getClassLoader().loadClass(
/* 115:200 */           "org.apache.tiles.extras.complete.CompleteAutoloadTilesInitializer");
/* 116:201 */         this.tilesInitializer = ((TilesInitializer)clazz.newInstance());
/* 117:    */       }
/* 118:    */       catch (Exception ex)
/* 119:    */       {
/* 120:204 */         throw new IllegalStateException("Tiles-Extras 2.2 not available", ex);
/* 121:    */       }
/* 122:    */     } else {
/* 123:208 */       this.tilesInitializer = null;
/* 124:    */     }
/* 125:210 */     this.overrideLocaleResolver = completeAutoload;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public void setDefinitions(String[] definitions)
/* 129:    */   {
/* 130:218 */     this.definitions = definitions;
/* 131:219 */     if (definitions != null)
/* 132:    */     {
/* 133:220 */       String defs = StringUtils.arrayToCommaDelimitedString(definitions);
/* 134:221 */       if (this.logger.isInfoEnabled()) {
/* 135:222 */         this.logger.info("TilesConfigurer: adding definitions [" + defs + "]");
/* 136:    */       }
/* 137:224 */       this.tilesPropertyMap.put("org.apache.tiles.definition.DefinitionsFactory.DEFINITIONS_CONFIG", defs);
/* 138:    */     }
/* 139:    */     else
/* 140:    */     {
/* 141:227 */       this.tilesPropertyMap.remove("org.apache.tiles.definition.DefinitionsFactory.DEFINITIONS_CONFIG");
/* 142:    */     }
/* 143:    */   }
/* 144:    */   
/* 145:    */   public void setCheckRefresh(boolean checkRefresh)
/* 146:    */   {
/* 147:236 */     this.checkRefresh = checkRefresh;
/* 148:237 */     this.tilesPropertyMap.put("org.apache.tiles.definition.dao.LocaleUrlDefinitionDAO.CHECK_REFRESH", 
/* 149:238 */       Boolean.toString(checkRefresh));
/* 150:    */   }
/* 151:    */   
/* 152:    */   public void setValidateDefinitions(boolean validateDefinitions)
/* 153:    */   {
/* 154:245 */     this.validateDefinitions = validateDefinitions;
/* 155:246 */     this.tilesPropertyMap.put("org.apache.tiles.definition.digester.DigesterDefinitionsReader.PARSER_VALIDATE", 
/* 156:247 */       Boolean.toString(validateDefinitions));
/* 157:    */   }
/* 158:    */   
/* 159:    */   public void setDefinitionsFactoryClass(Class<? extends DefinitionsFactory> definitionsFactoryClass)
/* 160:    */   {
/* 161:260 */     this.definitionsFactoryClass = definitionsFactoryClass;
/* 162:261 */     this.tilesPropertyMap.put("org.apache.tiles.definition.DefinitionsFactory", 
/* 163:262 */       definitionsFactoryClass.getName());
/* 164:    */   }
/* 165:    */   
/* 166:    */   public void setPreparerFactoryClass(Class<? extends PreparerFactory> preparerFactoryClass)
/* 167:    */   {
/* 168:285 */     this.preparerFactoryClass = preparerFactoryClass;
/* 169:286 */     this.tilesPropertyMap.put("org.apache.tiles.preparer.PreparerFactory", 
/* 170:287 */       preparerFactoryClass.getName());
/* 171:    */   }
/* 172:    */   
/* 173:    */   public void setUseMutableTilesContainer(boolean useMutableTilesContainer)
/* 174:    */   {
/* 175:297 */     this.useMutableTilesContainer = useMutableTilesContainer;
/* 176:298 */     this.tilesPropertyMap.put("org.apache.tiles.factory.TilesContainerFactory.MUTABLE", 
/* 177:299 */       Boolean.toString(useMutableTilesContainer));
/* 178:    */   }
/* 179:    */   
/* 180:    */   public void setTilesProperties(Properties tilesProperties)
/* 181:    */   {
/* 182:309 */     CollectionUtils.mergePropertiesIntoMap(tilesProperties, this.tilesPropertyMap);
/* 183:    */   }
/* 184:    */   
/* 185:    */   public void setServletContext(ServletContext servletContext)
/* 186:    */   {
/* 187:313 */     this.servletContext = servletContext;
/* 188:    */   }
/* 189:    */   
/* 190:    */   public void afterPropertiesSet()
/* 191:    */     throws TilesException
/* 192:    */   {
/* 193:324 */     boolean activateEl = false;
/* 194:325 */     if (tilesElPresent)
/* 195:    */     {
/* 196:326 */       activateEl = new JspExpressionChecker(null).isExpressionFactoryAvailable();
/* 197:327 */       if (!this.tilesPropertyMap.containsKey("org.apache.tiles.evaluator.AttributeEvaluator")) {
/* 198:328 */         this.tilesPropertyMap.put("org.apache.tiles.evaluator.AttributeEvaluator", activateEl ? 
/* 199:329 */           "org.apache.tiles.evaluator.el.ELAttributeEvaluator" : DirectAttributeEvaluator.class.getName());
/* 200:    */       }
/* 201:    */     }
/* 202:333 */     SpringTilesApplicationContextFactory factory = new SpringTilesApplicationContextFactory();
/* 203:334 */     factory.init(this.tilesPropertyMap);
/* 204:335 */     TilesApplicationContext preliminaryContext = factory.createApplicationContext(this.servletContext);
/* 205:336 */     if (this.tilesInitializer == null) {
/* 206:337 */       this.tilesInitializer = createTilesInitializer();
/* 207:    */     }
/* 208:339 */     this.tilesInitializer.initialize(preliminaryContext);
/* 209:341 */     if (this.overrideLocaleResolver)
/* 210:    */     {
/* 211:345 */       this.logger.debug("Registering Tiles 2.2 LocaleResolver for complete-autoload setup");
/* 212:    */       try
/* 213:    */       {
/* 214:347 */         BasicTilesContainer container = (BasicTilesContainer)ServletUtil.getContainer(this.servletContext);
/* 215:348 */         DefinitionsFactory definitionsFactory = container.getDefinitionsFactory();
/* 216:349 */         Method setter = definitionsFactory.getClass().getMethod("setLocaleResolver", new Class[] { LocaleResolver.class });
/* 217:350 */         setter.invoke(definitionsFactory, new Object[] { new SpringLocaleResolver() });
/* 218:    */       }
/* 219:    */       catch (Exception ex)
/* 220:    */       {
/* 221:353 */         throw new IllegalStateException("Cannot override LocaleResolver with SpringLocaleResolver", ex);
/* 222:    */       }
/* 223:    */     }
/* 224:357 */     if ((activateEl) && ((this.tilesInitializer instanceof SpringTilesInitializer)))
/* 225:    */     {
/* 226:361 */       BasicTilesContainer container = (BasicTilesContainer)ServletUtil.getContainer(this.servletContext);
/* 227:362 */       new TilesElActivator(null).registerEvaluator(container);
/* 228:    */     }
/* 229:    */   }
/* 230:    */   
/* 231:    */   protected TilesInitializer createTilesInitializer()
/* 232:    */   {
/* 233:372 */     return tiles22Present ? new SpringTilesInitializer(null) : new BasicTilesInitializer();
/* 234:    */   }
/* 235:    */   
/* 236:    */   public void destroy()
/* 237:    */     throws TilesException
/* 238:    */   {
/* 239:    */     try
/* 240:    */     {
/* 241:382 */       ReflectionUtils.invokeMethod(TilesInitializer.class.getMethod("destroy", new Class[0]), this.tilesInitializer);
/* 242:    */     }
/* 243:    */     catch (NoSuchMethodException localNoSuchMethodException)
/* 244:    */     {
/* 245:386 */       ServletUtil.setContainer(this.servletContext, null);
/* 246:    */     }
/* 247:    */   }
/* 248:    */   
/* 249:    */   private class SpringTilesInitializer
/* 250:    */     extends BasicTilesInitializer
/* 251:    */   {
/* 252:    */     private SpringTilesInitializer() {}
/* 253:    */     
/* 254:    */     protected AbstractTilesContainerFactory createContainerFactory(TilesApplicationContext context)
/* 255:    */     {
/* 256:395 */       return new TilesConfigurer.SpringTilesContainerFactory(TilesConfigurer.this, null);
/* 257:    */     }
/* 258:    */   }
/* 259:    */   
/* 260:    */   private class SpringTilesContainerFactory
/* 261:    */     extends BasicTilesContainerFactory
/* 262:    */   {
/* 263:    */     private SpringTilesContainerFactory() {}
/* 264:    */     
/* 265:    */     protected BasicTilesContainer instantiateContainer(TilesApplicationContext context)
/* 266:    */     {
/* 267:404 */       return TilesConfigurer.this.useMutableTilesContainer ? new CachingTilesContainer() : new BasicTilesContainer();
/* 268:    */     }
/* 269:    */     
/* 270:    */     protected void registerRequestContextFactory(String className, List<TilesRequestContextFactory> factories, TilesRequestContextFactory parent)
/* 271:    */     {
/* 272:411 */       if (ClassUtils.isPresent(className, TilesConfigurer.class.getClassLoader())) {
/* 273:412 */         super.registerRequestContextFactory(className, factories, parent);
/* 274:    */       }
/* 275:    */     }
/* 276:    */     
/* 277:    */     protected List<URL> getSourceURLs(TilesApplicationContext applicationContext, TilesRequestContextFactory contextFactory)
/* 278:    */     {
/* 279:419 */       if (TilesConfigurer.this.definitions != null) {
/* 280:    */         try
/* 281:    */         {
/* 282:421 */           List<URL> result = new LinkedList();
/* 283:422 */           for (String definition : TilesConfigurer.this.definitions) {
/* 284:423 */             result.addAll(applicationContext.getResources(definition));
/* 285:    */           }
/* 286:425 */           return result;
/* 287:    */         }
/* 288:    */         catch (IOException ex)
/* 289:    */         {
/* 290:428 */           throw new DefinitionsFactoryException("Cannot load definition URLs", ex);
/* 291:    */         }
/* 292:    */       }
/* 293:432 */       return super.getSourceURLs(applicationContext, contextFactory);
/* 294:    */     }
/* 295:    */     
/* 296:    */     protected BaseLocaleUrlDefinitionDAO instantiateLocaleDefinitionDao(TilesApplicationContext applicationContext, TilesRequestContextFactory contextFactory, LocaleResolver resolver)
/* 297:    */     {
/* 298:439 */       BaseLocaleUrlDefinitionDAO dao = super.instantiateLocaleDefinitionDao(
/* 299:440 */         applicationContext, contextFactory, resolver);
/* 300:441 */       if ((TilesConfigurer.this.checkRefresh) && ((dao instanceof CachingLocaleUrlDefinitionDAO))) {
/* 301:442 */         ((CachingLocaleUrlDefinitionDAO)dao).setCheckRefresh(TilesConfigurer.this.checkRefresh);
/* 302:    */       }
/* 303:444 */       return dao;
/* 304:    */     }
/* 305:    */     
/* 306:    */     protected DefinitionsReader createDefinitionsReader(TilesApplicationContext applicationContext, TilesRequestContextFactory contextFactory)
/* 307:    */     {
/* 308:450 */       DigesterDefinitionsReader reader = new DigesterDefinitionsReader();
/* 309:451 */       if (!TilesConfigurer.this.validateDefinitions)
/* 310:    */       {
/* 311:452 */         Map<String, String> map = new HashMap();
/* 312:453 */         map.put("org.apache.tiles.definition.digester.DigesterDefinitionsReader.PARSER_VALIDATE", Boolean.FALSE.toString());
/* 313:454 */         reader.init(map);
/* 314:    */       }
/* 315:456 */       return reader;
/* 316:    */     }
/* 317:    */     
/* 318:    */     protected DefinitionsFactory createDefinitionsFactory(TilesApplicationContext applicationContext, TilesRequestContextFactory contextFactory, LocaleResolver resolver)
/* 319:    */     {
/* 320:462 */       if (TilesConfigurer.this.definitionsFactoryClass != null)
/* 321:    */       {
/* 322:463 */         DefinitionsFactory factory = (DefinitionsFactory)BeanUtils.instantiate(TilesConfigurer.this.definitionsFactoryClass);
/* 323:464 */         if ((factory instanceof TilesApplicationContextAware)) {
/* 324:465 */           ((TilesApplicationContextAware)factory).setApplicationContext(applicationContext);
/* 325:    */         }
/* 326:467 */         BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(factory);
/* 327:468 */         if (bw.isWritableProperty("localeResolver")) {
/* 328:469 */           bw.setPropertyValue("localeResolver", resolver);
/* 329:    */         }
/* 330:471 */         if (bw.isWritableProperty("definitionDAO")) {
/* 331:472 */           bw.setPropertyValue("definitionDAO", 
/* 332:473 */             createLocaleDefinitionDao(applicationContext, contextFactory, resolver));
/* 333:    */         }
/* 334:475 */         if ((factory instanceof Refreshable)) {
/* 335:476 */           ((Refreshable)factory).refresh();
/* 336:    */         }
/* 337:478 */         return factory;
/* 338:    */       }
/* 339:481 */       return super.createDefinitionsFactory(applicationContext, contextFactory, resolver);
/* 340:    */     }
/* 341:    */     
/* 342:    */     protected PreparerFactory createPreparerFactory(TilesApplicationContext applicationContext, TilesRequestContextFactory contextFactory)
/* 343:    */     {
/* 344:488 */       if (TilesConfigurer.this.preparerFactoryClass != null) {
/* 345:489 */         return (PreparerFactory)BeanUtils.instantiate(TilesConfigurer.this.preparerFactoryClass);
/* 346:    */       }
/* 347:492 */       return super.createPreparerFactory(applicationContext, contextFactory);
/* 348:    */     }
/* 349:    */     
/* 350:    */     protected LocaleResolver createLocaleResolver(TilesApplicationContext applicationContext, TilesRequestContextFactory contextFactory)
/* 351:    */     {
/* 352:499 */       return new SpringLocaleResolver();
/* 353:    */     }
/* 354:    */   }
/* 355:    */   
/* 356:    */   private class JspExpressionChecker
/* 357:    */   {
/* 358:    */     private JspExpressionChecker() {}
/* 359:    */     
/* 360:    */     public boolean isExpressionFactoryAvailable()
/* 361:    */     {
/* 362:    */       try
/* 363:    */       {
/* 364:508 */         JspFactory factory = JspFactory.getDefaultFactory();
/* 365:509 */         if ((factory != null) && 
/* 366:510 */           (factory.getJspApplicationContext(TilesConfigurer.this.servletContext).getExpressionFactory() != null))
/* 367:    */         {
/* 368:511 */           TilesConfigurer.this.logger.info("Found JSP 2.1 ExpressionFactory");
/* 369:512 */           return true;
/* 370:    */         }
/* 371:    */       }
/* 372:    */       catch (Throwable ex)
/* 373:    */       {
/* 374:516 */         TilesConfigurer.this.logger.warn("Could not obtain JSP 2.1 ExpressionFactory", ex);
/* 375:    */       }
/* 376:518 */       return false;
/* 377:    */     }
/* 378:    */   }
/* 379:    */   
/* 380:    */   private class TilesElActivator
/* 381:    */   {
/* 382:    */     private TilesElActivator() {}
/* 383:    */     
/* 384:    */     public void registerEvaluator(BasicTilesContainer container)
/* 385:    */     {
/* 386:526 */       TilesConfigurer.this.logger.debug("Registering Tiles 2.2 AttributeEvaluatorFactory for JSP 2.1");
/* 387:    */       try
/* 388:    */       {
/* 389:528 */         ClassLoader cl = TilesElActivator.class.getClassLoader();
/* 390:529 */         Class aef = cl.loadClass("org.apache.tiles.evaluator.AttributeEvaluatorFactory");
/* 391:530 */         Class baef = cl.loadClass("org.apache.tiles.evaluator.BasicAttributeEvaluatorFactory");
/* 392:531 */         Constructor baefCtor = baef.getConstructor(new Class[] { AttributeEvaluator.class });
/* 393:532 */         ELAttributeEvaluator evaluator = new ELAttributeEvaluator();
/* 394:533 */         evaluator.setApplicationContext(container.getApplicationContext());
/* 395:534 */         evaluator.init(new HashMap());
/* 396:535 */         Object baefValue = baefCtor.newInstance(new Object[] { evaluator });
/* 397:536 */         Method setter = container.getClass().getMethod("setAttributeEvaluatorFactory", new Class[] { aef });
/* 398:537 */         setter.invoke(container, new Object[] { baefValue });
/* 399:538 */         Method getRequestContextFactory = BasicTilesContainer.class.getDeclaredMethod("getRequestContextFactory", new Class[0]);
/* 400:539 */         getRequestContextFactory.setAccessible(true);
/* 401:540 */         Method createRendererFactory = BasicTilesContainerFactory.class.getDeclaredMethod("createRendererFactory", new Class[] {
/* 402:541 */           TilesApplicationContext.class, TilesRequestContextFactory.class, TilesContainer.class, aef });
/* 403:542 */         createRendererFactory.setAccessible(true);
/* 404:543 */         BasicTilesContainerFactory tcf = new BasicTilesContainerFactory();
/* 405:544 */         RendererFactory rendererFactory = (RendererFactory)createRendererFactory.invoke(
/* 406:545 */           tcf, new Object[] { container.getApplicationContext(), getRequestContextFactory.invoke(container, new Object[0]), 
/* 407:546 */           container, baefValue });
/* 408:547 */         container.setRendererFactory(rendererFactory);
/* 409:    */       }
/* 410:    */       catch (Exception ex)
/* 411:    */       {
/* 412:550 */         throw new IllegalStateException("Cannot activate ELAttributeEvaluator", ex);
/* 413:    */       }
/* 414:    */     }
/* 415:    */   }
/* 416:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.tiles2.TilesConfigurer
 * JD-Core Version:    0.7.0.1
 */