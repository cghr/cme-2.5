/*    1:     */ package org.springframework.beans.factory.xml;
/*    2:     */ 
/*    3:     */ import java.util.ArrayList;
/*    4:     */ import java.util.Arrays;
/*    5:     */ import java.util.Collection;
/*    6:     */ import java.util.HashSet;
/*    7:     */ import java.util.List;
/*    8:     */ import java.util.Map;
/*    9:     */ import java.util.Properties;
/*   10:     */ import java.util.Set;
/*   11:     */ import org.apache.commons.logging.Log;
/*   12:     */ import org.apache.commons.logging.LogFactory;
/*   13:     */ import org.springframework.beans.BeanMetadataAttribute;
/*   14:     */ import org.springframework.beans.BeanMetadataAttributeAccessor;
/*   15:     */ import org.springframework.beans.MutablePropertyValues;
/*   16:     */ import org.springframework.beans.PropertyValue;
/*   17:     */ import org.springframework.beans.factory.config.BeanDefinition;
/*   18:     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*   19:     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*   20:     */ import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
/*   21:     */ import org.springframework.beans.factory.config.RuntimeBeanNameReference;
/*   22:     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*   23:     */ import org.springframework.beans.factory.config.TypedStringValue;
/*   24:     */ import org.springframework.beans.factory.parsing.BeanEntry;
/*   25:     */ import org.springframework.beans.factory.parsing.ConstructorArgumentEntry;
/*   26:     */ import org.springframework.beans.factory.parsing.ParseState;
/*   27:     */ import org.springframework.beans.factory.parsing.PropertyEntry;
/*   28:     */ import org.springframework.beans.factory.parsing.QualifierEntry;
/*   29:     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*   30:     */ import org.springframework.beans.factory.support.AutowireCandidateQualifier;
/*   31:     */ import org.springframework.beans.factory.support.BeanDefinitionDefaults;
/*   32:     */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*   33:     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*   34:     */ import org.springframework.beans.factory.support.LookupOverride;
/*   35:     */ import org.springframework.beans.factory.support.ManagedArray;
/*   36:     */ import org.springframework.beans.factory.support.ManagedList;
/*   37:     */ import org.springframework.beans.factory.support.ManagedMap;
/*   38:     */ import org.springframework.beans.factory.support.ManagedProperties;
/*   39:     */ import org.springframework.beans.factory.support.ManagedSet;
/*   40:     */ import org.springframework.beans.factory.support.MethodOverrides;
/*   41:     */ import org.springframework.beans.factory.support.ReplaceOverride;
/*   42:     */ import org.springframework.core.env.Environment;
/*   43:     */ import org.springframework.core.env.StandardEnvironment;
/*   44:     */ import org.springframework.util.Assert;
/*   45:     */ import org.springframework.util.ClassUtils;
/*   46:     */ import org.springframework.util.CollectionUtils;
/*   47:     */ import org.springframework.util.ObjectUtils;
/*   48:     */ import org.springframework.util.PatternMatchUtils;
/*   49:     */ import org.springframework.util.StringUtils;
/*   50:     */ import org.springframework.util.xml.DomUtils;
/*   51:     */ import org.w3c.dom.Element;
/*   52:     */ import org.w3c.dom.NamedNodeMap;
/*   53:     */ import org.w3c.dom.Node;
/*   54:     */ import org.w3c.dom.NodeList;
/*   55:     */ 
/*   56:     */ public class BeanDefinitionParserDelegate
/*   57:     */ {
/*   58:     */   public static final String BEANS_NAMESPACE_URI = "http://www.springframework.org/schema/beans";
/*   59:     */   public static final String MULTI_VALUE_ATTRIBUTE_DELIMITERS = ",; ";
/*   60:     */   /**
/*   61:     */    * @deprecated
/*   62:     */    */
/*   63:     */   public static final String BEAN_NAME_DELIMITERS = ",; ";
/*   64:     */   public static final String TRUE_VALUE = "true";
/*   65:     */   public static final String FALSE_VALUE = "false";
/*   66:     */   public static final String DEFAULT_VALUE = "default";
/*   67:     */   public static final String DESCRIPTION_ELEMENT = "description";
/*   68:     */   public static final String AUTOWIRE_NO_VALUE = "no";
/*   69:     */   public static final String AUTOWIRE_BY_NAME_VALUE = "byName";
/*   70:     */   public static final String AUTOWIRE_BY_TYPE_VALUE = "byType";
/*   71:     */   public static final String AUTOWIRE_CONSTRUCTOR_VALUE = "constructor";
/*   72:     */   public static final String AUTOWIRE_AUTODETECT_VALUE = "autodetect";
/*   73:     */   public static final String DEPENDENCY_CHECK_ALL_ATTRIBUTE_VALUE = "all";
/*   74:     */   public static final String DEPENDENCY_CHECK_SIMPLE_ATTRIBUTE_VALUE = "simple";
/*   75:     */   public static final String DEPENDENCY_CHECK_OBJECTS_ATTRIBUTE_VALUE = "objects";
/*   76:     */   public static final String NAME_ATTRIBUTE = "name";
/*   77:     */   public static final String BEAN_ELEMENT = "bean";
/*   78:     */   public static final String META_ELEMENT = "meta";
/*   79:     */   public static final String ID_ATTRIBUTE = "id";
/*   80:     */   public static final String PARENT_ATTRIBUTE = "parent";
/*   81:     */   public static final String CLASS_ATTRIBUTE = "class";
/*   82:     */   public static final String ABSTRACT_ATTRIBUTE = "abstract";
/*   83:     */   public static final String SCOPE_ATTRIBUTE = "scope";
/*   84:     */   public static final String SINGLETON_ATTRIBUTE = "singleton";
/*   85:     */   public static final String LAZY_INIT_ATTRIBUTE = "lazy-init";
/*   86:     */   public static final String AUTOWIRE_ATTRIBUTE = "autowire";
/*   87:     */   public static final String AUTOWIRE_CANDIDATE_ATTRIBUTE = "autowire-candidate";
/*   88:     */   public static final String PRIMARY_ATTRIBUTE = "primary";
/*   89:     */   public static final String DEPENDENCY_CHECK_ATTRIBUTE = "dependency-check";
/*   90:     */   public static final String DEPENDS_ON_ATTRIBUTE = "depends-on";
/*   91:     */   public static final String INIT_METHOD_ATTRIBUTE = "init-method";
/*   92:     */   public static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";
/*   93:     */   public static final String FACTORY_METHOD_ATTRIBUTE = "factory-method";
/*   94:     */   public static final String FACTORY_BEAN_ATTRIBUTE = "factory-bean";
/*   95:     */   public static final String CONSTRUCTOR_ARG_ELEMENT = "constructor-arg";
/*   96:     */   public static final String INDEX_ATTRIBUTE = "index";
/*   97:     */   public static final String TYPE_ATTRIBUTE = "type";
/*   98:     */   public static final String VALUE_TYPE_ATTRIBUTE = "value-type";
/*   99:     */   public static final String KEY_TYPE_ATTRIBUTE = "key-type";
/*  100:     */   public static final String PROPERTY_ELEMENT = "property";
/*  101:     */   public static final String REF_ATTRIBUTE = "ref";
/*  102:     */   public static final String VALUE_ATTRIBUTE = "value";
/*  103:     */   public static final String LOOKUP_METHOD_ELEMENT = "lookup-method";
/*  104:     */   public static final String REPLACED_METHOD_ELEMENT = "replaced-method";
/*  105:     */   public static final String REPLACER_ATTRIBUTE = "replacer";
/*  106:     */   public static final String ARG_TYPE_ELEMENT = "arg-type";
/*  107:     */   public static final String ARG_TYPE_MATCH_ATTRIBUTE = "match";
/*  108:     */   public static final String REF_ELEMENT = "ref";
/*  109:     */   public static final String IDREF_ELEMENT = "idref";
/*  110:     */   public static final String BEAN_REF_ATTRIBUTE = "bean";
/*  111:     */   public static final String LOCAL_REF_ATTRIBUTE = "local";
/*  112:     */   public static final String PARENT_REF_ATTRIBUTE = "parent";
/*  113:     */   public static final String VALUE_ELEMENT = "value";
/*  114:     */   public static final String NULL_ELEMENT = "null";
/*  115:     */   public static final String ARRAY_ELEMENT = "array";
/*  116:     */   public static final String LIST_ELEMENT = "list";
/*  117:     */   public static final String SET_ELEMENT = "set";
/*  118:     */   public static final String MAP_ELEMENT = "map";
/*  119:     */   public static final String ENTRY_ELEMENT = "entry";
/*  120:     */   public static final String KEY_ELEMENT = "key";
/*  121:     */   public static final String KEY_ATTRIBUTE = "key";
/*  122:     */   public static final String KEY_REF_ATTRIBUTE = "key-ref";
/*  123:     */   public static final String VALUE_REF_ATTRIBUTE = "value-ref";
/*  124:     */   public static final String PROPS_ELEMENT = "props";
/*  125:     */   public static final String PROP_ELEMENT = "prop";
/*  126:     */   public static final String MERGE_ATTRIBUTE = "merge";
/*  127:     */   public static final String QUALIFIER_ELEMENT = "qualifier";
/*  128:     */   public static final String QUALIFIER_ATTRIBUTE_ELEMENT = "attribute";
/*  129:     */   public static final String DEFAULT_LAZY_INIT_ATTRIBUTE = "default-lazy-init";
/*  130:     */   public static final String DEFAULT_MERGE_ATTRIBUTE = "default-merge";
/*  131:     */   public static final String DEFAULT_AUTOWIRE_ATTRIBUTE = "default-autowire";
/*  132:     */   public static final String DEFAULT_DEPENDENCY_CHECK_ATTRIBUTE = "default-dependency-check";
/*  133:     */   public static final String DEFAULT_AUTOWIRE_CANDIDATES_ATTRIBUTE = "default-autowire-candidates";
/*  134:     */   public static final String DEFAULT_INIT_METHOD_ATTRIBUTE = "default-init-method";
/*  135:     */   public static final String DEFAULT_DESTROY_METHOD_ATTRIBUTE = "default-destroy-method";
/*  136: 243 */   protected final Log logger = LogFactory.getLog(getClass());
/*  137:     */   private final XmlReaderContext readerContext;
/*  138: 247 */   private final DocumentDefaultsDefinition defaults = new DocumentDefaultsDefinition();
/*  139: 249 */   private final ParseState parseState = new ParseState();
/*  140:     */   private Environment environment;
/*  141: 258 */   private final Set<String> usedNames = new HashSet();
/*  142:     */   
/*  143:     */   public BeanDefinitionParserDelegate(XmlReaderContext readerContext, Environment environment)
/*  144:     */   {
/*  145: 266 */     Assert.notNull(readerContext, "XmlReaderContext must not be null");
/*  146: 267 */     Assert.notNull(readerContext, "Environment must not be null");
/*  147: 268 */     this.readerContext = readerContext;
/*  148: 269 */     this.environment = environment;
/*  149:     */   }
/*  150:     */   
/*  151:     */   @Deprecated
/*  152:     */   public BeanDefinitionParserDelegate(XmlReaderContext readerContext)
/*  153:     */   {
/*  154: 280 */     this(readerContext, new StandardEnvironment());
/*  155:     */   }
/*  156:     */   
/*  157:     */   public final XmlReaderContext getReaderContext()
/*  158:     */   {
/*  159: 287 */     return this.readerContext;
/*  160:     */   }
/*  161:     */   
/*  162:     */   public final Environment getEnvironment()
/*  163:     */   {
/*  164: 294 */     return this.environment;
/*  165:     */   }
/*  166:     */   
/*  167:     */   protected Object extractSource(Element ele)
/*  168:     */   {
/*  169: 302 */     return this.readerContext.extractSource(ele);
/*  170:     */   }
/*  171:     */   
/*  172:     */   protected void error(String message, Node source)
/*  173:     */   {
/*  174: 309 */     this.readerContext.error(message, source, this.parseState.snapshot());
/*  175:     */   }
/*  176:     */   
/*  177:     */   protected void error(String message, Element source)
/*  178:     */   {
/*  179: 316 */     this.readerContext.error(message, source, this.parseState.snapshot());
/*  180:     */   }
/*  181:     */   
/*  182:     */   protected void error(String message, Element source, Throwable cause)
/*  183:     */   {
/*  184: 323 */     this.readerContext.error(message, source, this.parseState.snapshot(), cause);
/*  185:     */   }
/*  186:     */   
/*  187:     */   public void initDefaults(Element root, BeanDefinitionParserDelegate parent)
/*  188:     */   {
/*  189: 336 */     populateDefaults(this.defaults, parent != null ? parent.defaults : null, root);
/*  190: 337 */     this.readerContext.fireDefaultsRegistered(this.defaults);
/*  191:     */   }
/*  192:     */   
/*  193:     */   /**
/*  194:     */    * @deprecated
/*  195:     */    */
/*  196:     */   public void initDefaults(Element root)
/*  197:     */   {
/*  198: 346 */     initDefaults(root, null);
/*  199:     */   }
/*  200:     */   
/*  201:     */   protected void populateDefaults(DocumentDefaultsDefinition defaults, DocumentDefaultsDefinition parentDefaults, Element root)
/*  202:     */   {
/*  203: 360 */     String lazyInit = root.getAttribute("default-lazy-init");
/*  204: 361 */     if ("default".equals(lazyInit)) {
/*  205: 362 */       lazyInit = parentDefaults != null ? parentDefaults.getLazyInit() : "false";
/*  206:     */     }
/*  207: 364 */     defaults.setLazyInit(lazyInit);
/*  208:     */     
/*  209: 366 */     String merge = root.getAttribute("default-merge");
/*  210: 367 */     if ("default".equals(merge)) {
/*  211: 368 */       merge = parentDefaults != null ? parentDefaults.getMerge() : "false";
/*  212:     */     }
/*  213: 370 */     defaults.setMerge(merge);
/*  214:     */     
/*  215: 372 */     String autowire = root.getAttribute("default-autowire");
/*  216: 373 */     if ("default".equals(autowire)) {
/*  217: 374 */       autowire = parentDefaults != null ? parentDefaults.getAutowire() : "no";
/*  218:     */     }
/*  219: 376 */     defaults.setAutowire(autowire);
/*  220:     */     
/*  221:     */ 
/*  222:     */ 
/*  223:     */ 
/*  224: 381 */     defaults.setDependencyCheck(root.getAttribute("default-dependency-check"));
/*  225: 383 */     if (root.hasAttribute("default-autowire-candidates")) {
/*  226: 384 */       defaults.setAutowireCandidates(root.getAttribute("default-autowire-candidates"));
/*  227: 386 */     } else if (parentDefaults != null) {
/*  228: 387 */       defaults.setAutowireCandidates(parentDefaults.getAutowireCandidates());
/*  229:     */     }
/*  230: 390 */     if (root.hasAttribute("default-init-method")) {
/*  231: 391 */       defaults.setInitMethod(root.getAttribute("default-init-method"));
/*  232: 393 */     } else if (parentDefaults != null) {
/*  233: 394 */       defaults.setInitMethod(parentDefaults.getInitMethod());
/*  234:     */     }
/*  235: 397 */     if (root.hasAttribute("default-destroy-method")) {
/*  236: 398 */       defaults.setDestroyMethod(root.getAttribute("default-destroy-method"));
/*  237: 400 */     } else if (parentDefaults != null) {
/*  238: 401 */       defaults.setDestroyMethod(parentDefaults.getDestroyMethod());
/*  239:     */     }
/*  240: 404 */     defaults.setSource(this.readerContext.extractSource(root));
/*  241:     */   }
/*  242:     */   
/*  243:     */   public DocumentDefaultsDefinition getDefaults()
/*  244:     */   {
/*  245: 412 */     return this.defaults;
/*  246:     */   }
/*  247:     */   
/*  248:     */   public BeanDefinitionDefaults getBeanDefinitionDefaults()
/*  249:     */   {
/*  250: 420 */     BeanDefinitionDefaults bdd = new BeanDefinitionDefaults();
/*  251: 421 */     bdd.setLazyInit("TRUE".equalsIgnoreCase(this.defaults.getLazyInit()));
/*  252: 422 */     bdd.setDependencyCheck(getDependencyCheck("default"));
/*  253: 423 */     bdd.setAutowireMode(getAutowireMode("default"));
/*  254: 424 */     bdd.setInitMethodName(this.defaults.getInitMethod());
/*  255: 425 */     bdd.setDestroyMethodName(this.defaults.getDestroyMethod());
/*  256: 426 */     return bdd;
/*  257:     */   }
/*  258:     */   
/*  259:     */   public String[] getAutowireCandidatePatterns()
/*  260:     */   {
/*  261: 434 */     String candidatePattern = this.defaults.getAutowireCandidates();
/*  262: 435 */     return candidatePattern != null ? StringUtils.commaDelimitedListToStringArray(candidatePattern) : null;
/*  263:     */   }
/*  264:     */   
/*  265:     */   public BeanDefinitionHolder parseBeanDefinitionElement(Element ele)
/*  266:     */   {
/*  267: 445 */     return parseBeanDefinitionElement(ele, null);
/*  268:     */   }
/*  269:     */   
/*  270:     */   public BeanDefinitionHolder parseBeanDefinitionElement(Element ele, BeanDefinition containingBean)
/*  271:     */   {
/*  272: 454 */     String id = ele.getAttribute("id");
/*  273: 455 */     String nameAttr = ele.getAttribute("name");
/*  274:     */     
/*  275: 457 */     List<String> aliases = new ArrayList();
/*  276: 458 */     if (StringUtils.hasLength(nameAttr))
/*  277:     */     {
/*  278: 459 */       String[] nameArr = StringUtils.tokenizeToStringArray(nameAttr, ",; ");
/*  279: 460 */       aliases.addAll((Collection)Arrays.asList(nameArr));
/*  280:     */     }
/*  281: 463 */     String beanName = id;
/*  282: 464 */     if ((!StringUtils.hasText(beanName)) && (!aliases.isEmpty()))
/*  283:     */     {
/*  284: 465 */       beanName = (String)aliases.remove(0);
/*  285: 466 */       if (this.logger.isDebugEnabled()) {
/*  286: 467 */         this.logger.debug("No XML 'id' specified - using '" + beanName + 
/*  287: 468 */           "' as bean name and " + aliases + " as aliases");
/*  288:     */       }
/*  289:     */     }
/*  290: 472 */     if (containingBean == null) {
/*  291: 473 */       checkNameUniqueness(beanName, aliases, ele);
/*  292:     */     }
/*  293: 476 */     AbstractBeanDefinition beanDefinition = parseBeanDefinitionElement(ele, beanName, containingBean);
/*  294: 477 */     if (beanDefinition != null)
/*  295:     */     {
/*  296: 478 */       if (!StringUtils.hasText(beanName)) {
/*  297:     */         try
/*  298:     */         {
/*  299: 480 */           if (containingBean != null)
/*  300:     */           {
/*  301: 481 */             beanName = BeanDefinitionReaderUtils.generateBeanName(
/*  302: 482 */               beanDefinition, this.readerContext.getRegistry(), true);
/*  303:     */           }
/*  304:     */           else
/*  305:     */           {
/*  306: 485 */             beanName = this.readerContext.generateBeanName(beanDefinition);
/*  307:     */             
/*  308:     */ 
/*  309:     */ 
/*  310: 489 */             String beanClassName = beanDefinition.getBeanClassName();
/*  311: 490 */             if ((beanClassName != null) && 
/*  312: 491 */               (beanName.startsWith(beanClassName)) && (beanName.length() > beanClassName.length()) && 
/*  313: 492 */               (!this.readerContext.getRegistry().isBeanNameInUse(beanClassName))) {
/*  314: 493 */               aliases.add(beanClassName);
/*  315:     */             }
/*  316:     */           }
/*  317: 496 */           if (this.logger.isDebugEnabled()) {
/*  318: 497 */             this.logger.debug("Neither XML 'id' nor 'name' specified - using generated bean name [" + 
/*  319: 498 */               beanName + "]");
/*  320:     */           }
/*  321:     */         }
/*  322:     */         catch (Exception ex)
/*  323:     */         {
/*  324: 502 */           error(ex.getMessage(), ele);
/*  325: 503 */           return null;
/*  326:     */         }
/*  327:     */       }
/*  328: 506 */       String[] aliasesArray = StringUtils.toStringArray(aliases);
/*  329: 507 */       return new BeanDefinitionHolder(beanDefinition, beanName, aliasesArray);
/*  330:     */     }
/*  331: 510 */     return null;
/*  332:     */   }
/*  333:     */   
/*  334:     */   protected void checkNameUniqueness(String beanName, List<String> aliases, Element beanElement)
/*  335:     */   {
/*  336: 518 */     String foundName = null;
/*  337: 520 */     if ((StringUtils.hasText(beanName)) && (this.usedNames.contains(beanName))) {
/*  338: 521 */       foundName = beanName;
/*  339:     */     }
/*  340: 523 */     if (foundName == null) {
/*  341: 524 */       foundName = (String)CollectionUtils.findFirstMatch(this.usedNames, aliases);
/*  342:     */     }
/*  343: 526 */     if (foundName != null) {
/*  344: 527 */       error("Bean name '" + foundName + "' is already used in this <beans> element", beanElement);
/*  345:     */     }
/*  346: 530 */     this.usedNames.add(beanName);
/*  347: 531 */     this.usedNames.addAll(aliases);
/*  348:     */   }
/*  349:     */   
/*  350:     */   public AbstractBeanDefinition parseBeanDefinitionElement(Element ele, String beanName, BeanDefinition containingBean)
/*  351:     */   {
/*  352: 541 */     this.parseState.push(new BeanEntry(beanName));
/*  353:     */     
/*  354: 543 */     String className = null;
/*  355: 544 */     if (ele.hasAttribute("class")) {
/*  356: 545 */       className = ele.getAttribute("class").trim();
/*  357:     */     }
/*  358:     */     try
/*  359:     */     {
/*  360: 549 */       String parent = null;
/*  361: 550 */       if (ele.hasAttribute("parent")) {
/*  362: 551 */         parent = ele.getAttribute("parent");
/*  363:     */       }
/*  364: 553 */       AbstractBeanDefinition bd = createBeanDefinition(className, parent);
/*  365:     */       
/*  366: 555 */       parseBeanDefinitionAttributes(ele, beanName, containingBean, bd);
/*  367: 556 */       bd.setDescription(DomUtils.getChildElementValueByTagName(ele, "description"));
/*  368:     */       
/*  369: 558 */       parseMetaElements(ele, bd);
/*  370: 559 */       parseLookupOverrideSubElements(ele, bd.getMethodOverrides());
/*  371: 560 */       parseReplacedMethodSubElements(ele, bd.getMethodOverrides());
/*  372:     */       
/*  373: 562 */       parseConstructorArgElements(ele, bd);
/*  374: 563 */       parsePropertyElements(ele, bd);
/*  375: 564 */       parseQualifierElements(ele, bd);
/*  376:     */       
/*  377: 566 */       bd.setResource(this.readerContext.getResource());
/*  378: 567 */       bd.setSource(extractSource(ele));
/*  379:     */       
/*  380: 569 */       return bd;
/*  381:     */     }
/*  382:     */     catch (ClassNotFoundException ex)
/*  383:     */     {
/*  384: 572 */       error("Bean class [" + className + "] not found", ele, ex);
/*  385:     */     }
/*  386:     */     catch (NoClassDefFoundError err)
/*  387:     */     {
/*  388: 575 */       error("Class that bean class [" + className + "] depends on not found", ele, err);
/*  389:     */     }
/*  390:     */     catch (Throwable ex)
/*  391:     */     {
/*  392: 578 */       error("Unexpected failure during bean definition parsing", ele, ex);
/*  393:     */     }
/*  394:     */     finally
/*  395:     */     {
/*  396: 581 */       this.parseState.pop();
/*  397:     */     }
/*  398: 584 */     return null;
/*  399:     */   }
/*  400:     */   
/*  401:     */   public AbstractBeanDefinition parseBeanDefinitionAttributes(Element ele, String beanName, BeanDefinition containingBean, AbstractBeanDefinition bd)
/*  402:     */   {
/*  403: 597 */     if (ele.hasAttribute("scope"))
/*  404:     */     {
/*  405: 599 */       bd.setScope(ele.getAttribute("scope"));
/*  406: 600 */       if (ele.hasAttribute("singleton")) {
/*  407: 601 */         error("Specify either 'scope' or 'singleton', not both", ele);
/*  408:     */       }
/*  409:     */     }
/*  410: 604 */     else if (ele.hasAttribute("singleton"))
/*  411:     */     {
/*  412: 606 */       bd.setScope("true".equals(ele.getAttribute("singleton")) ? 
/*  413: 607 */         "singleton" : "prototype");
/*  414:     */     }
/*  415: 609 */     else if (containingBean != null)
/*  416:     */     {
/*  417: 611 */       bd.setScope(containingBean.getScope());
/*  418:     */     }
/*  419: 614 */     if (ele.hasAttribute("abstract")) {
/*  420: 615 */       bd.setAbstract("true".equals(ele.getAttribute("abstract")));
/*  421:     */     }
/*  422: 618 */     String lazyInit = ele.getAttribute("lazy-init");
/*  423: 619 */     if ("default".equals(lazyInit)) {
/*  424: 620 */       lazyInit = this.defaults.getLazyInit();
/*  425:     */     }
/*  426: 622 */     bd.setLazyInit("true".equals(lazyInit));
/*  427:     */     
/*  428: 624 */     String autowire = ele.getAttribute("autowire");
/*  429: 625 */     bd.setAutowireMode(getAutowireMode(autowire));
/*  430:     */     
/*  431: 627 */     String dependencyCheck = ele.getAttribute("dependency-check");
/*  432: 628 */     bd.setDependencyCheck(getDependencyCheck(dependencyCheck));
/*  433: 630 */     if (ele.hasAttribute("depends-on"))
/*  434:     */     {
/*  435: 631 */       String dependsOn = ele.getAttribute("depends-on");
/*  436: 632 */       bd.setDependsOn(StringUtils.tokenizeToStringArray(dependsOn, ",; "));
/*  437:     */     }
/*  438: 635 */     String autowireCandidate = ele.getAttribute("autowire-candidate");
/*  439: 636 */     if (("".equals(autowireCandidate)) || ("default".equals(autowireCandidate)))
/*  440:     */     {
/*  441: 637 */       String candidatePattern = this.defaults.getAutowireCandidates();
/*  442: 638 */       if (candidatePattern != null)
/*  443:     */       {
/*  444: 639 */         String[] patterns = StringUtils.commaDelimitedListToStringArray(candidatePattern);
/*  445: 640 */         bd.setAutowireCandidate(PatternMatchUtils.simpleMatch(patterns, beanName));
/*  446:     */       }
/*  447:     */     }
/*  448:     */     else
/*  449:     */     {
/*  450: 644 */       bd.setAutowireCandidate("true".equals(autowireCandidate));
/*  451:     */     }
/*  452: 647 */     if (ele.hasAttribute("primary")) {
/*  453: 648 */       bd.setPrimary("true".equals(ele.getAttribute("primary")));
/*  454:     */     }
/*  455: 651 */     if (ele.hasAttribute("init-method"))
/*  456:     */     {
/*  457: 652 */       String initMethodName = ele.getAttribute("init-method");
/*  458: 653 */       if (!"".equals(initMethodName)) {
/*  459: 654 */         bd.setInitMethodName(initMethodName);
/*  460:     */       }
/*  461:     */     }
/*  462: 658 */     else if (this.defaults.getInitMethod() != null)
/*  463:     */     {
/*  464: 659 */       bd.setInitMethodName(this.defaults.getInitMethod());
/*  465: 660 */       bd.setEnforceInitMethod(false);
/*  466:     */     }
/*  467: 664 */     if (ele.hasAttribute("destroy-method"))
/*  468:     */     {
/*  469: 665 */       String destroyMethodName = ele.getAttribute("destroy-method");
/*  470: 666 */       if (!"".equals(destroyMethodName)) {
/*  471: 667 */         bd.setDestroyMethodName(destroyMethodName);
/*  472:     */       }
/*  473:     */     }
/*  474: 671 */     else if (this.defaults.getDestroyMethod() != null)
/*  475:     */     {
/*  476: 672 */       bd.setDestroyMethodName(this.defaults.getDestroyMethod());
/*  477: 673 */       bd.setEnforceDestroyMethod(false);
/*  478:     */     }
/*  479: 677 */     if (ele.hasAttribute("factory-method")) {
/*  480: 678 */       bd.setFactoryMethodName(ele.getAttribute("factory-method"));
/*  481:     */     }
/*  482: 680 */     if (ele.hasAttribute("factory-bean")) {
/*  483: 681 */       bd.setFactoryBeanName(ele.getAttribute("factory-bean"));
/*  484:     */     }
/*  485: 684 */     return bd;
/*  486:     */   }
/*  487:     */   
/*  488:     */   protected AbstractBeanDefinition createBeanDefinition(String className, String parentName)
/*  489:     */     throws ClassNotFoundException
/*  490:     */   {
/*  491: 697 */     return BeanDefinitionReaderUtils.createBeanDefinition(
/*  492: 698 */       parentName, className, this.readerContext.getBeanClassLoader());
/*  493:     */   }
/*  494:     */   
/*  495:     */   public void parseMetaElements(Element ele, BeanMetadataAttributeAccessor attributeAccessor)
/*  496:     */   {
/*  497: 702 */     NodeList nl = ele.getChildNodes();
/*  498: 703 */     for (int i = 0; i < nl.getLength(); i++)
/*  499:     */     {
/*  500: 704 */       Node node = nl.item(i);
/*  501: 705 */       if ((isCandidateElement(node)) && (nodeNameEquals(node, "meta")))
/*  502:     */       {
/*  503: 706 */         Element metaElement = (Element)node;
/*  504: 707 */         String key = metaElement.getAttribute("key");
/*  505: 708 */         String value = metaElement.getAttribute("value");
/*  506: 709 */         BeanMetadataAttribute attribute = new BeanMetadataAttribute(key, value);
/*  507: 710 */         attribute.setSource(extractSource(metaElement));
/*  508: 711 */         attributeAccessor.addMetadataAttribute(attribute);
/*  509:     */       }
/*  510:     */     }
/*  511:     */   }
/*  512:     */   
/*  513:     */   public int getAutowireMode(String attValue)
/*  514:     */   {
/*  515: 717 */     String att = attValue;
/*  516: 718 */     if ("default".equals(att)) {
/*  517: 719 */       att = this.defaults.getAutowire();
/*  518:     */     }
/*  519: 721 */     int autowire = 0;
/*  520: 722 */     if ("byName".equals(att)) {
/*  521: 723 */       autowire = 1;
/*  522: 725 */     } else if ("byType".equals(att)) {
/*  523: 726 */       autowire = 2;
/*  524: 728 */     } else if ("constructor".equals(att)) {
/*  525: 729 */       autowire = 3;
/*  526: 731 */     } else if ("autodetect".equals(att)) {
/*  527: 732 */       autowire = 4;
/*  528:     */     }
/*  529: 735 */     return autowire;
/*  530:     */   }
/*  531:     */   
/*  532:     */   public int getDependencyCheck(String attValue)
/*  533:     */   {
/*  534: 739 */     String att = attValue;
/*  535: 740 */     if ("default".equals(att)) {
/*  536: 741 */       att = this.defaults.getDependencyCheck();
/*  537:     */     }
/*  538: 743 */     if ("all".equals(att)) {
/*  539: 744 */       return 3;
/*  540:     */     }
/*  541: 746 */     if ("objects".equals(att)) {
/*  542: 747 */       return 1;
/*  543:     */     }
/*  544: 749 */     if ("simple".equals(att)) {
/*  545: 750 */       return 2;
/*  546:     */     }
/*  547: 753 */     return 0;
/*  548:     */   }
/*  549:     */   
/*  550:     */   public void parseConstructorArgElements(Element beanEle, BeanDefinition bd)
/*  551:     */   {
/*  552: 761 */     NodeList nl = beanEle.getChildNodes();
/*  553: 762 */     for (int i = 0; i < nl.getLength(); i++)
/*  554:     */     {
/*  555: 763 */       Node node = nl.item(i);
/*  556: 764 */       if ((isCandidateElement(node)) && (nodeNameEquals(node, "constructor-arg"))) {
/*  557: 765 */         parseConstructorArgElement((Element)node, bd);
/*  558:     */       }
/*  559:     */     }
/*  560:     */   }
/*  561:     */   
/*  562:     */   public void parsePropertyElements(Element beanEle, BeanDefinition bd)
/*  563:     */   {
/*  564: 774 */     NodeList nl = beanEle.getChildNodes();
/*  565: 775 */     for (int i = 0; i < nl.getLength(); i++)
/*  566:     */     {
/*  567: 776 */       Node node = nl.item(i);
/*  568: 777 */       if ((isCandidateElement(node)) && (nodeNameEquals(node, "property"))) {
/*  569: 778 */         parsePropertyElement((Element)node, bd);
/*  570:     */       }
/*  571:     */     }
/*  572:     */   }
/*  573:     */   
/*  574:     */   public void parseQualifierElements(Element beanEle, AbstractBeanDefinition bd)
/*  575:     */   {
/*  576: 787 */     NodeList nl = beanEle.getChildNodes();
/*  577: 788 */     for (int i = 0; i < nl.getLength(); i++)
/*  578:     */     {
/*  579: 789 */       Node node = nl.item(i);
/*  580: 790 */       if ((isCandidateElement(node)) && (nodeNameEquals(node, "qualifier"))) {
/*  581: 791 */         parseQualifierElement((Element)node, bd);
/*  582:     */       }
/*  583:     */     }
/*  584:     */   }
/*  585:     */   
/*  586:     */   public void parseLookupOverrideSubElements(Element beanEle, MethodOverrides overrides)
/*  587:     */   {
/*  588: 800 */     NodeList nl = beanEle.getChildNodes();
/*  589: 801 */     for (int i = 0; i < nl.getLength(); i++)
/*  590:     */     {
/*  591: 802 */       Node node = nl.item(i);
/*  592: 803 */       if ((isCandidateElement(node)) && (nodeNameEquals(node, "lookup-method")))
/*  593:     */       {
/*  594: 804 */         Element ele = (Element)node;
/*  595: 805 */         String methodName = ele.getAttribute("name");
/*  596: 806 */         String beanRef = ele.getAttribute("bean");
/*  597: 807 */         LookupOverride override = new LookupOverride(methodName, beanRef);
/*  598: 808 */         override.setSource(extractSource(ele));
/*  599: 809 */         overrides.addOverride(override);
/*  600:     */       }
/*  601:     */     }
/*  602:     */   }
/*  603:     */   
/*  604:     */   public void parseReplacedMethodSubElements(Element beanEle, MethodOverrides overrides)
/*  605:     */   {
/*  606: 818 */     NodeList nl = beanEle.getChildNodes();
/*  607: 819 */     for (int i = 0; i < nl.getLength(); i++)
/*  608:     */     {
/*  609: 820 */       Node node = nl.item(i);
/*  610: 821 */       if ((isCandidateElement(node)) && (nodeNameEquals(node, "replaced-method")))
/*  611:     */       {
/*  612: 822 */         Element replacedMethodEle = (Element)node;
/*  613: 823 */         String name = replacedMethodEle.getAttribute("name");
/*  614: 824 */         String callback = replacedMethodEle.getAttribute("replacer");
/*  615: 825 */         ReplaceOverride replaceOverride = new ReplaceOverride(name, callback);
/*  616:     */         
/*  617: 827 */         List<Element> argTypeEles = DomUtils.getChildElementsByTagName(replacedMethodEle, "arg-type");
/*  618: 828 */         for (Element argTypeEle : argTypeEles) {
/*  619: 829 */           replaceOverride.addTypeIdentifier(argTypeEle.getAttribute("match"));
/*  620:     */         }
/*  621: 831 */         replaceOverride.setSource(extractSource(replacedMethodEle));
/*  622: 832 */         overrides.addOverride(replaceOverride);
/*  623:     */       }
/*  624:     */     }
/*  625:     */   }
/*  626:     */   
/*  627:     */   public void parseConstructorArgElement(Element ele, BeanDefinition bd)
/*  628:     */   {
/*  629: 841 */     String indexAttr = ele.getAttribute("index");
/*  630: 842 */     String typeAttr = ele.getAttribute("type");
/*  631: 843 */     String nameAttr = ele.getAttribute("name");
/*  632: 844 */     if (StringUtils.hasLength(indexAttr)) {
/*  633:     */       try
/*  634:     */       {
/*  635: 846 */         int index = Integer.parseInt(indexAttr);
/*  636: 847 */         if (index < 0) {
/*  637: 848 */           error("'index' cannot be lower than 0", ele);
/*  638:     */         } else {
/*  639:     */           try
/*  640:     */           {
/*  641: 852 */             this.parseState.push(new ConstructorArgumentEntry(index));
/*  642: 853 */             Object value = parsePropertyValue(ele, bd, null);
/*  643: 854 */             valueHolder = new ConstructorArgumentValues.ValueHolder(value);
/*  644: 855 */             if (StringUtils.hasLength(typeAttr)) {
/*  645: 856 */               valueHolder.setType(typeAttr);
/*  646:     */             }
/*  647: 858 */             if (StringUtils.hasLength(nameAttr)) {
/*  648: 859 */               valueHolder.setName(nameAttr);
/*  649:     */             }
/*  650: 861 */             valueHolder.setSource(extractSource(ele));
/*  651: 862 */             if (bd.getConstructorArgumentValues().hasIndexedArgumentValue(index)) {
/*  652: 863 */               error("Ambiguous constructor-arg entries for index " + index, ele);
/*  653:     */             } else {
/*  654: 866 */               bd.getConstructorArgumentValues().addIndexedArgumentValue(index, valueHolder);
/*  655:     */             }
/*  656:     */           }
/*  657:     */           finally
/*  658:     */           {
/*  659: 870 */             this.parseState.pop();
/*  660:     */           }
/*  661:     */         }
/*  662:     */       }
/*  663:     */       catch (NumberFormatException localNumberFormatException)
/*  664:     */       {
/*  665: 875 */         error("Attribute 'index' of tag 'constructor-arg' must be an integer", ele);
/*  666:     */       }
/*  667:     */     } else {
/*  668:     */       try
/*  669:     */       {
/*  670: 880 */         this.parseState.push(new ConstructorArgumentEntry());
/*  671: 881 */         Object value = parsePropertyValue(ele, bd, null);
/*  672: 882 */         ConstructorArgumentValues.ValueHolder valueHolder = new ConstructorArgumentValues.ValueHolder(value);
/*  673: 883 */         if (StringUtils.hasLength(typeAttr)) {
/*  674: 884 */           valueHolder.setType(typeAttr);
/*  675:     */         }
/*  676: 886 */         if (StringUtils.hasLength(nameAttr)) {
/*  677: 887 */           valueHolder.setName(nameAttr);
/*  678:     */         }
/*  679: 889 */         valueHolder.setSource(extractSource(ele));
/*  680: 890 */         bd.getConstructorArgumentValues().addGenericArgumentValue(valueHolder);
/*  681:     */       }
/*  682:     */       finally
/*  683:     */       {
/*  684: 893 */         this.parseState.pop();
/*  685:     */       }
/*  686:     */     }
/*  687:     */   }
/*  688:     */   
/*  689:     */   public void parsePropertyElement(Element ele, BeanDefinition bd)
/*  690:     */   {
/*  691: 902 */     String propertyName = ele.getAttribute("name");
/*  692: 903 */     if (!StringUtils.hasLength(propertyName))
/*  693:     */     {
/*  694: 904 */       error("Tag 'property' must have a 'name' attribute", ele);
/*  695: 905 */       return;
/*  696:     */     }
/*  697: 907 */     this.parseState.push(new PropertyEntry(propertyName));
/*  698:     */     try
/*  699:     */     {
/*  700: 909 */       if (bd.getPropertyValues().contains(propertyName))
/*  701:     */       {
/*  702: 910 */         error("Multiple 'property' definitions for property '" + propertyName + "'", ele);
/*  703: 911 */         return;
/*  704:     */       }
/*  705: 913 */       Object val = parsePropertyValue(ele, bd, propertyName);
/*  706: 914 */       PropertyValue pv = new PropertyValue(propertyName, val);
/*  707: 915 */       parseMetaElements(ele, pv);
/*  708: 916 */       pv.setSource(extractSource(ele));
/*  709: 917 */       bd.getPropertyValues().addPropertyValue(pv);
/*  710:     */     }
/*  711:     */     finally
/*  712:     */     {
/*  713: 920 */       this.parseState.pop();
/*  714:     */     }
/*  715: 920 */     this.parseState.pop();
/*  716:     */   }
/*  717:     */   
/*  718:     */   public void parseQualifierElement(Element ele, AbstractBeanDefinition bd)
/*  719:     */   {
/*  720: 928 */     String typeName = ele.getAttribute("type");
/*  721: 929 */     if (!StringUtils.hasLength(typeName))
/*  722:     */     {
/*  723: 930 */       error("Tag 'qualifier' must have a 'type' attribute", ele);
/*  724: 931 */       return;
/*  725:     */     }
/*  726: 933 */     this.parseState.push(new QualifierEntry(typeName));
/*  727:     */     try
/*  728:     */     {
/*  729: 935 */       AutowireCandidateQualifier qualifier = new AutowireCandidateQualifier(typeName);
/*  730: 936 */       qualifier.setSource(extractSource(ele));
/*  731: 937 */       String value = ele.getAttribute("value");
/*  732: 938 */       if (StringUtils.hasLength(value)) {
/*  733: 939 */         qualifier.setAttribute(AutowireCandidateQualifier.VALUE_KEY, value);
/*  734:     */       }
/*  735: 941 */       NodeList nl = ele.getChildNodes();
/*  736: 942 */       for (int i = 0; i < nl.getLength(); i++)
/*  737:     */       {
/*  738: 943 */         Node node = nl.item(i);
/*  739: 944 */         if ((isCandidateElement(node)) && (nodeNameEquals(node, "attribute")))
/*  740:     */         {
/*  741: 945 */           Element attributeEle = (Element)node;
/*  742: 946 */           String attributeName = attributeEle.getAttribute("key");
/*  743: 947 */           String attributeValue = attributeEle.getAttribute("value");
/*  744: 948 */           if ((StringUtils.hasLength(attributeName)) && (StringUtils.hasLength(attributeValue)))
/*  745:     */           {
/*  746: 949 */             BeanMetadataAttribute attribute = new BeanMetadataAttribute(attributeName, attributeValue);
/*  747: 950 */             attribute.setSource(extractSource(attributeEle));
/*  748: 951 */             qualifier.addMetadataAttribute(attribute);
/*  749:     */           }
/*  750:     */           else
/*  751:     */           {
/*  752: 954 */             error("Qualifier 'attribute' tag must have a 'name' and 'value'", attributeEle);
/*  753: 955 */             return;
/*  754:     */           }
/*  755:     */         }
/*  756:     */       }
/*  757: 959 */       bd.addQualifier(qualifier);
/*  758:     */     }
/*  759:     */     finally
/*  760:     */     {
/*  761: 962 */       this.parseState.pop();
/*  762:     */     }
/*  763: 962 */     this.parseState.pop();
/*  764:     */   }
/*  765:     */   
/*  766:     */   public Object parsePropertyValue(Element ele, BeanDefinition bd, String propertyName)
/*  767:     */   {
/*  768: 971 */     String elementName = propertyName != null ? 
/*  769: 972 */       "<property> element for property '" + propertyName + "'" : 
/*  770: 973 */       "<constructor-arg> element";
/*  771:     */     
/*  772:     */ 
/*  773: 976 */     NodeList nl = ele.getChildNodes();
/*  774: 977 */     Element subElement = null;
/*  775: 978 */     for (int i = 0; i < nl.getLength(); i++)
/*  776:     */     {
/*  777: 979 */       Node node = nl.item(i);
/*  778: 980 */       if (((node instanceof Element)) && (!nodeNameEquals(node, "description")) && 
/*  779: 981 */         (!nodeNameEquals(node, "meta"))) {
/*  780: 983 */         if (subElement != null) {
/*  781: 984 */           error(elementName + " must not contain more than one sub-element", ele);
/*  782:     */         } else {
/*  783: 987 */           subElement = (Element)node;
/*  784:     */         }
/*  785:     */       }
/*  786:     */     }
/*  787: 992 */     boolean hasRefAttribute = ele.hasAttribute("ref");
/*  788: 993 */     boolean hasValueAttribute = ele.hasAttribute("value");
/*  789: 994 */     if (((hasRefAttribute) && (hasValueAttribute)) || (
/*  790: 995 */       ((hasRefAttribute) || (hasValueAttribute)) && (subElement != null))) {
/*  791: 996 */       error(elementName + 
/*  792: 997 */         " is only allowed to contain either 'ref' attribute OR 'value' attribute OR sub-element", ele);
/*  793:     */     }
/*  794:1000 */     if (hasRefAttribute)
/*  795:     */     {
/*  796:1001 */       String refName = ele.getAttribute("ref");
/*  797:1002 */       if (!StringUtils.hasText(refName)) {
/*  798:1003 */         error(elementName + " contains empty 'ref' attribute", ele);
/*  799:     */       }
/*  800:1005 */       RuntimeBeanReference ref = new RuntimeBeanReference(refName);
/*  801:1006 */       ref.setSource(extractSource(ele));
/*  802:1007 */       return ref;
/*  803:     */     }
/*  804:1009 */     if (hasValueAttribute)
/*  805:     */     {
/*  806:1010 */       TypedStringValue valueHolder = new TypedStringValue(ele.getAttribute("value"));
/*  807:1011 */       valueHolder.setSource(extractSource(ele));
/*  808:1012 */       return valueHolder;
/*  809:     */     }
/*  810:1014 */     if (subElement != null) {
/*  811:1015 */       return parsePropertySubElement(subElement, bd);
/*  812:     */     }
/*  813:1019 */     error(elementName + " must specify a ref or value", ele);
/*  814:1020 */     return null;
/*  815:     */   }
/*  816:     */   
/*  817:     */   public Object parsePropertySubElement(Element ele, BeanDefinition bd)
/*  818:     */   {
/*  819:1025 */     return parsePropertySubElement(ele, bd, null);
/*  820:     */   }
/*  821:     */   
/*  822:     */   public Object parsePropertySubElement(Element ele, BeanDefinition bd, String defaultValueType)
/*  823:     */   {
/*  824:1036 */     if (!isDefaultNamespace(ele)) {
/*  825:1037 */       return parseNestedCustomElement(ele, bd);
/*  826:     */     }
/*  827:1039 */     if (nodeNameEquals(ele, "bean"))
/*  828:     */     {
/*  829:1040 */       BeanDefinitionHolder nestedBd = parseBeanDefinitionElement(ele, bd);
/*  830:1041 */       if (nestedBd != null) {
/*  831:1042 */         nestedBd = decorateBeanDefinitionIfRequired(ele, nestedBd, bd);
/*  832:     */       }
/*  833:1044 */       return nestedBd;
/*  834:     */     }
/*  835:1046 */     if (nodeNameEquals(ele, "ref"))
/*  836:     */     {
/*  837:1048 */       String refName = ele.getAttribute("bean");
/*  838:1049 */       boolean toParent = false;
/*  839:1050 */       if (!StringUtils.hasLength(refName))
/*  840:     */       {
/*  841:1052 */         refName = ele.getAttribute("local");
/*  842:1053 */         if (!StringUtils.hasLength(refName))
/*  843:     */         {
/*  844:1055 */           refName = ele.getAttribute("parent");
/*  845:1056 */           toParent = true;
/*  846:1057 */           if (!StringUtils.hasLength(refName))
/*  847:     */           {
/*  848:1058 */             error("'bean', 'local' or 'parent' is required for <ref> element", ele);
/*  849:1059 */             return null;
/*  850:     */           }
/*  851:     */         }
/*  852:     */       }
/*  853:1063 */       if (!StringUtils.hasText(refName))
/*  854:     */       {
/*  855:1064 */         error("<ref> element contains empty target attribute", ele);
/*  856:1065 */         return null;
/*  857:     */       }
/*  858:1067 */       RuntimeBeanReference ref = new RuntimeBeanReference(refName, toParent);
/*  859:1068 */       ref.setSource(extractSource(ele));
/*  860:1069 */       return ref;
/*  861:     */     }
/*  862:1071 */     if (nodeNameEquals(ele, "idref")) {
/*  863:1072 */       return parseIdRefElement(ele);
/*  864:     */     }
/*  865:1074 */     if (nodeNameEquals(ele, "value")) {
/*  866:1075 */       return parseValueElement(ele, defaultValueType);
/*  867:     */     }
/*  868:1077 */     if (nodeNameEquals(ele, "null"))
/*  869:     */     {
/*  870:1080 */       TypedStringValue nullHolder = new TypedStringValue(null);
/*  871:1081 */       nullHolder.setSource(extractSource(ele));
/*  872:1082 */       return nullHolder;
/*  873:     */     }
/*  874:1084 */     if (nodeNameEquals(ele, "array")) {
/*  875:1085 */       return parseArrayElement(ele, bd);
/*  876:     */     }
/*  877:1087 */     if (nodeNameEquals(ele, "list")) {
/*  878:1088 */       return parseListElement(ele, bd);
/*  879:     */     }
/*  880:1090 */     if (nodeNameEquals(ele, "set")) {
/*  881:1091 */       return parseSetElement(ele, bd);
/*  882:     */     }
/*  883:1093 */     if (nodeNameEquals(ele, "map")) {
/*  884:1094 */       return parseMapElement(ele, bd);
/*  885:     */     }
/*  886:1096 */     if (nodeNameEquals(ele, "props")) {
/*  887:1097 */       return parsePropsElement(ele);
/*  888:     */     }
/*  889:1100 */     error("Unknown property sub-element: [" + ele.getNodeName() + "]", ele);
/*  890:1101 */     return null;
/*  891:     */   }
/*  892:     */   
/*  893:     */   public Object parseIdRefElement(Element ele)
/*  894:     */   {
/*  895:1110 */     String refName = ele.getAttribute("bean");
/*  896:1111 */     if (!StringUtils.hasLength(refName))
/*  897:     */     {
/*  898:1113 */       refName = ele.getAttribute("local");
/*  899:1114 */       if (!StringUtils.hasLength(refName))
/*  900:     */       {
/*  901:1115 */         error("Either 'bean' or 'local' is required for <idref> element", ele);
/*  902:1116 */         return null;
/*  903:     */       }
/*  904:     */     }
/*  905:1119 */     if (!StringUtils.hasText(refName))
/*  906:     */     {
/*  907:1120 */       error("<idref> element contains empty target attribute", ele);
/*  908:1121 */       return null;
/*  909:     */     }
/*  910:1123 */     RuntimeBeanNameReference ref = new RuntimeBeanNameReference(refName);
/*  911:1124 */     ref.setSource(extractSource(ele));
/*  912:1125 */     return ref;
/*  913:     */   }
/*  914:     */   
/*  915:     */   public Object parseValueElement(Element ele, String defaultTypeName)
/*  916:     */   {
/*  917:1133 */     String value = DomUtils.getTextValue(ele);
/*  918:1134 */     String specifiedTypeName = ele.getAttribute("type");
/*  919:1135 */     String typeName = specifiedTypeName;
/*  920:1136 */     if (!StringUtils.hasText(typeName)) {
/*  921:1137 */       typeName = defaultTypeName;
/*  922:     */     }
/*  923:     */     try
/*  924:     */     {
/*  925:1140 */       TypedStringValue typedValue = buildTypedStringValue(value, typeName);
/*  926:1141 */       typedValue.setSource(extractSource(ele));
/*  927:1142 */       typedValue.setSpecifiedTypeName(specifiedTypeName);
/*  928:1143 */       return typedValue;
/*  929:     */     }
/*  930:     */     catch (ClassNotFoundException ex)
/*  931:     */     {
/*  932:1146 */       error("Type class [" + typeName + "] not found for <value> element", ele, ex);
/*  933:     */     }
/*  934:1147 */     return value;
/*  935:     */   }
/*  936:     */   
/*  937:     */   protected TypedStringValue buildTypedStringValue(String value, String targetTypeName)
/*  938:     */     throws ClassNotFoundException
/*  939:     */   {
/*  940:1158 */     ClassLoader classLoader = this.readerContext.getBeanClassLoader();
/*  941:     */     TypedStringValue typedValue;
/*  942:     */     TypedStringValue typedValue;
/*  943:1160 */     if (!StringUtils.hasText(targetTypeName))
/*  944:     */     {
/*  945:1161 */       typedValue = new TypedStringValue(value);
/*  946:     */     }
/*  947:     */     else
/*  948:     */     {
/*  949:     */       TypedStringValue typedValue;
/*  950:1163 */       if (classLoader != null)
/*  951:     */       {
/*  952:1164 */         Class<?> targetType = ClassUtils.forName(targetTypeName, classLoader);
/*  953:1165 */         typedValue = new TypedStringValue(value, targetType);
/*  954:     */       }
/*  955:     */       else
/*  956:     */       {
/*  957:1168 */         typedValue = new TypedStringValue(value, targetTypeName);
/*  958:     */       }
/*  959:     */     }
/*  960:1170 */     return typedValue;
/*  961:     */   }
/*  962:     */   
/*  963:     */   public Object parseArrayElement(Element arrayEle, BeanDefinition bd)
/*  964:     */   {
/*  965:1177 */     String elementType = arrayEle.getAttribute("value-type");
/*  966:1178 */     NodeList nl = arrayEle.getChildNodes();
/*  967:1179 */     ManagedArray target = new ManagedArray(elementType, nl.getLength());
/*  968:1180 */     target.setSource(extractSource(arrayEle));
/*  969:1181 */     target.setElementTypeName(elementType);
/*  970:1182 */     target.setMergeEnabled(parseMergeAttribute(arrayEle));
/*  971:1183 */     parseCollectionElements(nl, target, bd, elementType);
/*  972:1184 */     return target;
/*  973:     */   }
/*  974:     */   
/*  975:     */   public List parseListElement(Element collectionEle, BeanDefinition bd)
/*  976:     */   {
/*  977:1191 */     String defaultElementType = collectionEle.getAttribute("value-type");
/*  978:1192 */     NodeList nl = collectionEle.getChildNodes();
/*  979:1193 */     ManagedList<Object> target = new ManagedList(nl.getLength());
/*  980:1194 */     target.setSource(extractSource(collectionEle));
/*  981:1195 */     target.setElementTypeName(defaultElementType);
/*  982:1196 */     target.setMergeEnabled(parseMergeAttribute(collectionEle));
/*  983:1197 */     parseCollectionElements(nl, target, bd, defaultElementType);
/*  984:1198 */     return target;
/*  985:     */   }
/*  986:     */   
/*  987:     */   public Set parseSetElement(Element collectionEle, BeanDefinition bd)
/*  988:     */   {
/*  989:1205 */     String defaultElementType = collectionEle.getAttribute("value-type");
/*  990:1206 */     NodeList nl = collectionEle.getChildNodes();
/*  991:1207 */     ManagedSet<Object> target = new ManagedSet(nl.getLength());
/*  992:1208 */     target.setSource(extractSource(collectionEle));
/*  993:1209 */     target.setElementTypeName(defaultElementType);
/*  994:1210 */     target.setMergeEnabled(parseMergeAttribute(collectionEle));
/*  995:1211 */     parseCollectionElements(nl, target, bd, defaultElementType);
/*  996:1212 */     return target;
/*  997:     */   }
/*  998:     */   
/*  999:     */   protected void parseCollectionElements(NodeList elementNodes, Collection<Object> target, BeanDefinition bd, String defaultElementType)
/* 1000:     */   {
/* 1001:1218 */     for (int i = 0; i < elementNodes.getLength(); i++)
/* 1002:     */     {
/* 1003:1219 */       Node node = elementNodes.item(i);
/* 1004:1220 */       if (((node instanceof Element)) && (!nodeNameEquals(node, "description"))) {
/* 1005:1221 */         target.add(parsePropertySubElement((Element)node, bd, defaultElementType));
/* 1006:     */       }
/* 1007:     */     }
/* 1008:     */   }
/* 1009:     */   
/* 1010:     */   public Map parseMapElement(Element mapEle, BeanDefinition bd)
/* 1011:     */   {
/* 1012:1230 */     String defaultKeyType = mapEle.getAttribute("key-type");
/* 1013:1231 */     String defaultValueType = mapEle.getAttribute("value-type");
/* 1014:     */     
/* 1015:1233 */     List<Element> entryEles = DomUtils.getChildElementsByTagName(mapEle, "entry");
/* 1016:1234 */     ManagedMap<Object, Object> map = new ManagedMap(entryEles.size());
/* 1017:1235 */     map.setSource(extractSource(mapEle));
/* 1018:1236 */     map.setKeyTypeName(defaultKeyType);
/* 1019:1237 */     map.setValueTypeName(defaultValueType);
/* 1020:1238 */     map.setMergeEnabled(parseMergeAttribute(mapEle));
/* 1021:1240 */     for (Element entryEle : entryEles)
/* 1022:     */     {
/* 1023:1243 */       NodeList entrySubNodes = entryEle.getChildNodes();
/* 1024:1244 */       Element keyEle = null;
/* 1025:1245 */       Element valueEle = null;
/* 1026:1246 */       for (int j = 0; j < entrySubNodes.getLength(); j++)
/* 1027:     */       {
/* 1028:1247 */         Node node = entrySubNodes.item(j);
/* 1029:1248 */         if ((node instanceof Element))
/* 1030:     */         {
/* 1031:1249 */           Element candidateEle = (Element)node;
/* 1032:1250 */           if (nodeNameEquals(candidateEle, "key"))
/* 1033:     */           {
/* 1034:1251 */             if (keyEle != null) {
/* 1035:1252 */               error("<entry> element is only allowed to contain one <key> sub-element", entryEle);
/* 1036:     */             } else {
/* 1037:1255 */               keyEle = candidateEle;
/* 1038:     */             }
/* 1039:     */           }
/* 1040:1260 */           else if (!nodeNameEquals(candidateEle, "description")) {
/* 1041:1263 */             if (valueEle != null) {
/* 1042:1264 */               error("<entry> element must not contain more than one value sub-element", entryEle);
/* 1043:     */             } else {
/* 1044:1267 */               valueEle = candidateEle;
/* 1045:     */             }
/* 1046:     */           }
/* 1047:     */         }
/* 1048:     */       }
/* 1049:1274 */       Object key = null;
/* 1050:1275 */       boolean hasKeyAttribute = entryEle.hasAttribute("key");
/* 1051:1276 */       boolean hasKeyRefAttribute = entryEle.hasAttribute("key-ref");
/* 1052:1277 */       if (((hasKeyAttribute) && (hasKeyRefAttribute)) || (
/* 1053:1278 */         ((hasKeyAttribute) || (hasKeyRefAttribute)) && (keyEle != null))) {
/* 1054:1279 */         error("<entry> element is only allowed to contain either a 'key' attribute OR a 'key-ref' attribute OR a <key> sub-element", 
/* 1055:1280 */           entryEle);
/* 1056:     */       }
/* 1057:1282 */       if (hasKeyAttribute)
/* 1058:     */       {
/* 1059:1283 */         key = buildTypedStringValueForMap(entryEle.getAttribute("key"), defaultKeyType, entryEle);
/* 1060:     */       }
/* 1061:1285 */       else if (hasKeyRefAttribute)
/* 1062:     */       {
/* 1063:1286 */         String refName = entryEle.getAttribute("key-ref");
/* 1064:1287 */         if (!StringUtils.hasText(refName)) {
/* 1065:1288 */           error("<entry> element contains empty 'key-ref' attribute", entryEle);
/* 1066:     */         }
/* 1067:1290 */         RuntimeBeanReference ref = new RuntimeBeanReference(refName);
/* 1068:1291 */         ref.setSource(extractSource(entryEle));
/* 1069:1292 */         key = ref;
/* 1070:     */       }
/* 1071:1294 */       else if (keyEle != null)
/* 1072:     */       {
/* 1073:1295 */         key = parseKeyElement(keyEle, bd, defaultKeyType);
/* 1074:     */       }
/* 1075:     */       else
/* 1076:     */       {
/* 1077:1298 */         error("<entry> element must specify a key", entryEle);
/* 1078:     */       }
/* 1079:1302 */       Object value = null;
/* 1080:1303 */       boolean hasValueAttribute = entryEle.hasAttribute("value");
/* 1081:1304 */       boolean hasValueRefAttribute = entryEle.hasAttribute("value-ref");
/* 1082:1305 */       if (((hasValueAttribute) && (hasValueRefAttribute)) || (
/* 1083:1306 */         ((hasValueAttribute) || (hasValueRefAttribute)) && (valueEle != null))) {
/* 1084:1307 */         error("<entry> element is only allowed to contain either 'value' attribute OR 'value-ref' attribute OR <value> sub-element", 
/* 1085:1308 */           entryEle);
/* 1086:     */       }
/* 1087:1310 */       if (hasValueAttribute)
/* 1088:     */       {
/* 1089:1311 */         value = buildTypedStringValueForMap(entryEle.getAttribute("value"), defaultValueType, entryEle);
/* 1090:     */       }
/* 1091:1313 */       else if (hasValueRefAttribute)
/* 1092:     */       {
/* 1093:1314 */         String refName = entryEle.getAttribute("value-ref");
/* 1094:1315 */         if (!StringUtils.hasText(refName)) {
/* 1095:1316 */           error("<entry> element contains empty 'value-ref' attribute", entryEle);
/* 1096:     */         }
/* 1097:1318 */         RuntimeBeanReference ref = new RuntimeBeanReference(refName);
/* 1098:1319 */         ref.setSource(extractSource(entryEle));
/* 1099:1320 */         value = ref;
/* 1100:     */       }
/* 1101:1322 */       else if (valueEle != null)
/* 1102:     */       {
/* 1103:1323 */         value = parsePropertySubElement(valueEle, bd, defaultValueType);
/* 1104:     */       }
/* 1105:     */       else
/* 1106:     */       {
/* 1107:1326 */         error("<entry> element must specify a value", entryEle);
/* 1108:     */       }
/* 1109:1330 */       map.put(key, value);
/* 1110:     */     }
/* 1111:1333 */     return map;
/* 1112:     */   }
/* 1113:     */   
/* 1114:     */   protected final Object buildTypedStringValueForMap(String value, String defaultTypeName, Element entryEle)
/* 1115:     */   {
/* 1116:     */     try
/* 1117:     */     {
/* 1118:1342 */       TypedStringValue typedValue = buildTypedStringValue(value, defaultTypeName);
/* 1119:1343 */       typedValue.setSource(extractSource(entryEle));
/* 1120:1344 */       return typedValue;
/* 1121:     */     }
/* 1122:     */     catch (ClassNotFoundException ex)
/* 1123:     */     {
/* 1124:1347 */       error("Type class [" + defaultTypeName + "] not found for Map key/value type", entryEle, ex);
/* 1125:     */     }
/* 1126:1348 */     return value;
/* 1127:     */   }
/* 1128:     */   
/* 1129:     */   protected Object parseKeyElement(Element keyEle, BeanDefinition bd, String defaultKeyTypeName)
/* 1130:     */   {
/* 1131:1356 */     NodeList nl = keyEle.getChildNodes();
/* 1132:1357 */     Element subElement = null;
/* 1133:1358 */     for (int i = 0; i < nl.getLength(); i++)
/* 1134:     */     {
/* 1135:1359 */       Node node = nl.item(i);
/* 1136:1360 */       if ((node instanceof Element)) {
/* 1137:1362 */         if (subElement != null) {
/* 1138:1363 */           error("<key> element must not contain more than one value sub-element", keyEle);
/* 1139:     */         } else {
/* 1140:1366 */           subElement = (Element)node;
/* 1141:     */         }
/* 1142:     */       }
/* 1143:     */     }
/* 1144:1370 */     return parsePropertySubElement(subElement, bd, defaultKeyTypeName);
/* 1145:     */   }
/* 1146:     */   
/* 1147:     */   public Properties parsePropsElement(Element propsEle)
/* 1148:     */   {
/* 1149:1377 */     ManagedProperties props = new ManagedProperties();
/* 1150:1378 */     props.setSource(extractSource(propsEle));
/* 1151:1379 */     props.setMergeEnabled(parseMergeAttribute(propsEle));
/* 1152:     */     
/* 1153:1381 */     List<Element> propEles = DomUtils.getChildElementsByTagName(propsEle, "prop");
/* 1154:1382 */     for (Element propEle : propEles)
/* 1155:     */     {
/* 1156:1383 */       String key = propEle.getAttribute("key");
/* 1157:     */       
/* 1158:     */ 
/* 1159:1386 */       String value = DomUtils.getTextValue(propEle).trim();
/* 1160:1387 */       TypedStringValue keyHolder = new TypedStringValue(key);
/* 1161:1388 */       keyHolder.setSource(extractSource(propEle));
/* 1162:1389 */       TypedStringValue valueHolder = new TypedStringValue(value);
/* 1163:1390 */       valueHolder.setSource(extractSource(propEle));
/* 1164:1391 */       props.put(keyHolder, valueHolder);
/* 1165:     */     }
/* 1166:1394 */     return props;
/* 1167:     */   }
/* 1168:     */   
/* 1169:     */   public boolean parseMergeAttribute(Element collectionElement)
/* 1170:     */   {
/* 1171:1401 */     String value = collectionElement.getAttribute("merge");
/* 1172:1402 */     if ("default".equals(value)) {
/* 1173:1403 */       value = this.defaults.getMerge();
/* 1174:     */     }
/* 1175:1405 */     return "true".equals(value);
/* 1176:     */   }
/* 1177:     */   
/* 1178:     */   public BeanDefinition parseCustomElement(Element ele)
/* 1179:     */   {
/* 1180:1409 */     return parseCustomElement(ele, null);
/* 1181:     */   }
/* 1182:     */   
/* 1183:     */   public BeanDefinition parseCustomElement(Element ele, BeanDefinition containingBd)
/* 1184:     */   {
/* 1185:1413 */     String namespaceUri = getNamespaceURI(ele);
/* 1186:1414 */     NamespaceHandler handler = this.readerContext.getNamespaceHandlerResolver().resolve(namespaceUri);
/* 1187:1415 */     if (handler == null)
/* 1188:     */     {
/* 1189:1416 */       error("Unable to locate Spring NamespaceHandler for XML schema namespace [" + namespaceUri + "]", ele);
/* 1190:1417 */       return null;
/* 1191:     */     }
/* 1192:1419 */     return handler.parse(ele, new ParserContext(this.readerContext, this, containingBd));
/* 1193:     */   }
/* 1194:     */   
/* 1195:     */   public BeanDefinitionHolder decorateBeanDefinitionIfRequired(Element ele, BeanDefinitionHolder definitionHolder)
/* 1196:     */   {
/* 1197:1423 */     return decorateBeanDefinitionIfRequired(ele, definitionHolder, null);
/* 1198:     */   }
/* 1199:     */   
/* 1200:     */   public BeanDefinitionHolder decorateBeanDefinitionIfRequired(Element ele, BeanDefinitionHolder definitionHolder, BeanDefinition containingBd)
/* 1201:     */   {
/* 1202:1429 */     BeanDefinitionHolder finalDefinition = definitionHolder;
/* 1203:     */     
/* 1204:     */ 
/* 1205:1432 */     NamedNodeMap attributes = ele.getAttributes();
/* 1206:1433 */     for (int i = 0; i < attributes.getLength(); i++)
/* 1207:     */     {
/* 1208:1434 */       Node node = attributes.item(i);
/* 1209:1435 */       finalDefinition = decorateIfRequired(node, finalDefinition, containingBd);
/* 1210:     */     }
/* 1211:1439 */     NodeList children = ele.getChildNodes();
/* 1212:1440 */     for (int i = 0; i < children.getLength(); i++)
/* 1213:     */     {
/* 1214:1441 */       Node node = children.item(i);
/* 1215:1442 */       if (node.getNodeType() == 1) {
/* 1216:1443 */         finalDefinition = decorateIfRequired(node, finalDefinition, containingBd);
/* 1217:     */       }
/* 1218:     */     }
/* 1219:1446 */     return finalDefinition;
/* 1220:     */   }
/* 1221:     */   
/* 1222:     */   private BeanDefinitionHolder decorateIfRequired(Node node, BeanDefinitionHolder originalDef, BeanDefinition containingBd)
/* 1223:     */   {
/* 1224:1452 */     String namespaceUri = getNamespaceURI(node);
/* 1225:1453 */     if (!isDefaultNamespace(namespaceUri))
/* 1226:     */     {
/* 1227:1454 */       NamespaceHandler handler = this.readerContext.getNamespaceHandlerResolver().resolve(namespaceUri);
/* 1228:1455 */       if (handler != null) {
/* 1229:1456 */         return handler.decorate(node, originalDef, new ParserContext(this.readerContext, this, containingBd));
/* 1230:     */       }
/* 1231:1458 */       if ((namespaceUri != null) && (namespaceUri.startsWith("http://www.springframework.org/"))) {
/* 1232:1459 */         error("Unable to locate Spring NamespaceHandler for XML schema namespace [" + namespaceUri + "]", node);
/* 1233:1463 */       } else if (this.logger.isDebugEnabled()) {
/* 1234:1464 */         this.logger.debug("No Spring NamespaceHandler found for XML schema namespace [" + namespaceUri + "]");
/* 1235:     */       }
/* 1236:     */     }
/* 1237:1468 */     return originalDef;
/* 1238:     */   }
/* 1239:     */   
/* 1240:     */   private BeanDefinitionHolder parseNestedCustomElement(Element ele, BeanDefinition containingBd)
/* 1241:     */   {
/* 1242:1472 */     BeanDefinition innerDefinition = parseCustomElement(ele, containingBd);
/* 1243:1473 */     if (innerDefinition == null)
/* 1244:     */     {
/* 1245:1474 */       error("Incorrect usage of element '" + ele.getNodeName() + "' in a nested manner. " + 
/* 1246:1475 */         "This tag cannot be used nested inside <property>.", ele);
/* 1247:1476 */       return null;
/* 1248:     */     }
/* 1249:1478 */     String id = ele.getNodeName() + "#" + 
/* 1250:1479 */       ObjectUtils.getIdentityHexString(innerDefinition);
/* 1251:1480 */     if (this.logger.isDebugEnabled()) {
/* 1252:1481 */       this.logger.debug("Using generated bean name [" + id + 
/* 1253:1482 */         "] for nested custom element '" + ele.getNodeName() + "'");
/* 1254:     */     }
/* 1255:1484 */     return new BeanDefinitionHolder(innerDefinition, id);
/* 1256:     */   }
/* 1257:     */   
/* 1258:     */   public String getNamespaceURI(Node node)
/* 1259:     */   {
/* 1260:1494 */     return node.getNamespaceURI();
/* 1261:     */   }
/* 1262:     */   
/* 1263:     */   public String getLocalName(Node node)
/* 1264:     */   {
/* 1265:1503 */     return node.getLocalName();
/* 1266:     */   }
/* 1267:     */   
/* 1268:     */   public boolean nodeNameEquals(Node node, String desiredName)
/* 1269:     */   {
/* 1270:1516 */     return (desiredName.equals(node.getNodeName())) || (desiredName.equals(getLocalName(node)));
/* 1271:     */   }
/* 1272:     */   
/* 1273:     */   public boolean isDefaultNamespace(String namespaceUri)
/* 1274:     */   {
/* 1275:1520 */     return (!StringUtils.hasLength(namespaceUri)) || ("http://www.springframework.org/schema/beans".equals(namespaceUri));
/* 1276:     */   }
/* 1277:     */   
/* 1278:     */   public boolean isDefaultNamespace(Node node)
/* 1279:     */   {
/* 1280:1524 */     return isDefaultNamespace(getNamespaceURI(node));
/* 1281:     */   }
/* 1282:     */   
/* 1283:     */   private boolean isCandidateElement(Node node)
/* 1284:     */   {
/* 1285:1528 */     return ((node instanceof Element)) && ((isDefaultNamespace(node)) || (!isDefaultNamespace(node.getParentNode())));
/* 1286:     */   }
/* 1287:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.BeanDefinitionParserDelegate
 * JD-Core Version:    0.7.0.1
 */