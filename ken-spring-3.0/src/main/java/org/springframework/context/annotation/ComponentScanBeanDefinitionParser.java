/*   1:    */ package org.springframework.context.annotation;
/*   2:    */ 
/*   3:    */ import java.util.Set;
/*   4:    */ import java.util.regex.Pattern;
/*   5:    */ import org.springframework.beans.BeanUtils;
/*   6:    */ import org.springframework.beans.FatalBeanException;
/*   7:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*   8:    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*   9:    */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*  10:    */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*  11:    */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*  12:    */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*  13:    */ import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
/*  14:    */ import org.springframework.beans.factory.xml.ParserContext;
/*  15:    */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*  16:    */ import org.springframework.core.io.ResourceLoader;
/*  17:    */ import org.springframework.core.type.filter.AnnotationTypeFilter;
/*  18:    */ import org.springframework.core.type.filter.AspectJTypeFilter;
/*  19:    */ import org.springframework.core.type.filter.AssignableTypeFilter;
/*  20:    */ import org.springframework.core.type.filter.RegexPatternTypeFilter;
/*  21:    */ import org.springframework.core.type.filter.TypeFilter;
/*  22:    */ import org.springframework.util.StringUtils;
/*  23:    */ import org.w3c.dom.Element;
/*  24:    */ import org.w3c.dom.Node;
/*  25:    */ import org.w3c.dom.NodeList;
/*  26:    */ 
/*  27:    */ public class ComponentScanBeanDefinitionParser
/*  28:    */   implements BeanDefinitionParser
/*  29:    */ {
/*  30:    */   private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";
/*  31:    */   private static final String RESOURCE_PATTERN_ATTRIBUTE = "resource-pattern";
/*  32:    */   private static final String USE_DEFAULT_FILTERS_ATTRIBUTE = "use-default-filters";
/*  33:    */   private static final String ANNOTATION_CONFIG_ATTRIBUTE = "annotation-config";
/*  34:    */   private static final String NAME_GENERATOR_ATTRIBUTE = "name-generator";
/*  35:    */   private static final String SCOPE_RESOLVER_ATTRIBUTE = "scope-resolver";
/*  36:    */   private static final String SCOPED_PROXY_ATTRIBUTE = "scoped-proxy";
/*  37:    */   private static final String EXCLUDE_FILTER_ELEMENT = "exclude-filter";
/*  38:    */   private static final String INCLUDE_FILTER_ELEMENT = "include-filter";
/*  39:    */   private static final String FILTER_TYPE_ATTRIBUTE = "type";
/*  40:    */   private static final String FILTER_EXPRESSION_ATTRIBUTE = "expression";
/*  41:    */   
/*  42:    */   public BeanDefinition parse(Element element, ParserContext parserContext)
/*  43:    */   {
/*  44: 79 */     String[] basePackages = StringUtils.tokenizeToStringArray(element.getAttribute("base-package"), 
/*  45: 80 */       ",; \t\n");
/*  46:    */     
/*  47:    */ 
/*  48: 83 */     ClassPathBeanDefinitionScanner scanner = configureScanner(parserContext, element);
/*  49: 84 */     Set<BeanDefinitionHolder> beanDefinitions = scanner.doScan(basePackages);
/*  50: 85 */     registerComponents(parserContext.getReaderContext(), beanDefinitions, element);
/*  51:    */     
/*  52: 87 */     return null;
/*  53:    */   }
/*  54:    */   
/*  55:    */   protected ClassPathBeanDefinitionScanner configureScanner(ParserContext parserContext, Element element)
/*  56:    */   {
/*  57: 91 */     XmlReaderContext readerContext = parserContext.getReaderContext();
/*  58:    */     
/*  59: 93 */     boolean useDefaultFilters = true;
/*  60: 94 */     if (element.hasAttribute("use-default-filters")) {
/*  61: 95 */       useDefaultFilters = Boolean.valueOf(element.getAttribute("use-default-filters")).booleanValue();
/*  62:    */     }
/*  63: 99 */     ClassPathBeanDefinitionScanner scanner = createScanner(readerContext, useDefaultFilters);
/*  64:100 */     scanner.setResourceLoader(readerContext.getResourceLoader());
/*  65:101 */     scanner.setEnvironment(parserContext.getDelegate().getEnvironment());
/*  66:102 */     scanner.setBeanDefinitionDefaults(parserContext.getDelegate().getBeanDefinitionDefaults());
/*  67:103 */     scanner.setAutowireCandidatePatterns(parserContext.getDelegate().getAutowireCandidatePatterns());
/*  68:105 */     if (element.hasAttribute("resource-pattern")) {
/*  69:106 */       scanner.setResourcePattern(element.getAttribute("resource-pattern"));
/*  70:    */     }
/*  71:    */     try
/*  72:    */     {
/*  73:110 */       parseBeanNameGenerator(element, scanner);
/*  74:    */     }
/*  75:    */     catch (Exception ex)
/*  76:    */     {
/*  77:113 */       readerContext.error(ex.getMessage(), readerContext.extractSource(element), ex.getCause());
/*  78:    */     }
/*  79:    */     try
/*  80:    */     {
/*  81:117 */       parseScope(element, scanner);
/*  82:    */     }
/*  83:    */     catch (Exception ex)
/*  84:    */     {
/*  85:120 */       readerContext.error(ex.getMessage(), readerContext.extractSource(element), ex.getCause());
/*  86:    */     }
/*  87:123 */     parseTypeFilters(element, scanner, readerContext, parserContext);
/*  88:    */     
/*  89:125 */     return scanner;
/*  90:    */   }
/*  91:    */   
/*  92:    */   protected ClassPathBeanDefinitionScanner createScanner(XmlReaderContext readerContext, boolean useDefaultFilters)
/*  93:    */   {
/*  94:129 */     return new ClassPathBeanDefinitionScanner(readerContext.getRegistry(), useDefaultFilters);
/*  95:    */   }
/*  96:    */   
/*  97:    */   protected void registerComponents(XmlReaderContext readerContext, Set<BeanDefinitionHolder> beanDefinitions, Element element)
/*  98:    */   {
/*  99:135 */     Object source = readerContext.extractSource(element);
/* 100:136 */     CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(element.getTagName(), source);
/* 101:138 */     for (BeanDefinitionHolder beanDefHolder : beanDefinitions) {
/* 102:139 */       compositeDef.addNestedComponent(new BeanComponentDefinition(beanDefHolder));
/* 103:    */     }
/* 104:143 */     boolean annotationConfig = true;
/* 105:144 */     if (element.hasAttribute("annotation-config")) {
/* 106:145 */       annotationConfig = Boolean.valueOf(element.getAttribute("annotation-config")).booleanValue();
/* 107:    */     }
/* 108:147 */     if (annotationConfig)
/* 109:    */     {
/* 110:148 */       Object processorDefinitions = 
/* 111:149 */         AnnotationConfigUtils.registerAnnotationConfigProcessors(readerContext.getRegistry(), source);
/* 112:150 */       for (BeanDefinitionHolder processorDefinition : (Set)processorDefinitions) {
/* 113:151 */         compositeDef.addNestedComponent(new BeanComponentDefinition(processorDefinition));
/* 114:    */       }
/* 115:    */     }
/* 116:155 */     readerContext.fireComponentRegistered(compositeDef);
/* 117:    */   }
/* 118:    */   
/* 119:    */   protected void parseBeanNameGenerator(Element element, ClassPathBeanDefinitionScanner scanner)
/* 120:    */   {
/* 121:159 */     if (element.hasAttribute("name-generator"))
/* 122:    */     {
/* 123:160 */       BeanNameGenerator beanNameGenerator = (BeanNameGenerator)instantiateUserDefinedStrategy(
/* 124:161 */         element.getAttribute("name-generator"), BeanNameGenerator.class, 
/* 125:162 */         scanner.getResourceLoader().getClassLoader());
/* 126:163 */       scanner.setBeanNameGenerator(beanNameGenerator);
/* 127:    */     }
/* 128:    */   }
/* 129:    */   
/* 130:    */   protected void parseScope(Element element, ClassPathBeanDefinitionScanner scanner)
/* 131:    */   {
/* 132:169 */     if (element.hasAttribute("scope-resolver"))
/* 133:    */     {
/* 134:170 */       if (element.hasAttribute("scoped-proxy")) {
/* 135:171 */         throw new IllegalArgumentException(
/* 136:172 */           "Cannot define both 'scope-resolver' and 'scoped-proxy' on <component-scan> tag");
/* 137:    */       }
/* 138:174 */       ScopeMetadataResolver scopeMetadataResolver = (ScopeMetadataResolver)instantiateUserDefinedStrategy(
/* 139:175 */         element.getAttribute("scope-resolver"), ScopeMetadataResolver.class, 
/* 140:176 */         scanner.getResourceLoader().getClassLoader());
/* 141:177 */       scanner.setScopeMetadataResolver(scopeMetadataResolver);
/* 142:    */     }
/* 143:180 */     if (element.hasAttribute("scoped-proxy"))
/* 144:    */     {
/* 145:181 */       String mode = element.getAttribute("scoped-proxy");
/* 146:182 */       if ("targetClass".equals(mode)) {
/* 147:183 */         scanner.setScopedProxyMode(ScopedProxyMode.TARGET_CLASS);
/* 148:185 */       } else if ("interfaces".equals(mode)) {
/* 149:186 */         scanner.setScopedProxyMode(ScopedProxyMode.INTERFACES);
/* 150:188 */       } else if ("no".equals(mode)) {
/* 151:189 */         scanner.setScopedProxyMode(ScopedProxyMode.NO);
/* 152:    */       } else {
/* 153:192 */         throw new IllegalArgumentException("scoped-proxy only supports 'no', 'interfaces' and 'targetClass'");
/* 154:    */       }
/* 155:    */     }
/* 156:    */   }
/* 157:    */   
/* 158:    */   protected void parseTypeFilters(Element element, ClassPathBeanDefinitionScanner scanner, XmlReaderContext readerContext, ParserContext parserContext)
/* 159:    */   {
/* 160:201 */     ClassLoader classLoader = scanner.getResourceLoader().getClassLoader();
/* 161:202 */     NodeList nodeList = element.getChildNodes();
/* 162:203 */     for (int i = 0; i < nodeList.getLength(); i++)
/* 163:    */     {
/* 164:204 */       Node node = nodeList.item(i);
/* 165:205 */       if (node.getNodeType() == 1)
/* 166:    */       {
/* 167:206 */         String localName = parserContext.getDelegate().getLocalName(node);
/* 168:    */         try
/* 169:    */         {
/* 170:208 */           if ("include-filter".equals(localName))
/* 171:    */           {
/* 172:209 */             TypeFilter typeFilter = createTypeFilter((Element)node, classLoader);
/* 173:210 */             scanner.addIncludeFilter(typeFilter);
/* 174:    */           }
/* 175:212 */           else if ("exclude-filter".equals(localName))
/* 176:    */           {
/* 177:213 */             TypeFilter typeFilter = createTypeFilter((Element)node, classLoader);
/* 178:214 */             scanner.addExcludeFilter(typeFilter);
/* 179:    */           }
/* 180:    */         }
/* 181:    */         catch (Exception ex)
/* 182:    */         {
/* 183:218 */           readerContext.error(ex.getMessage(), readerContext.extractSource(element), ex.getCause());
/* 184:    */         }
/* 185:    */       }
/* 186:    */     }
/* 187:    */   }
/* 188:    */   
/* 189:    */   protected TypeFilter createTypeFilter(Element element, ClassLoader classLoader)
/* 190:    */   {
/* 191:226 */     String filterType = element.getAttribute("type");
/* 192:227 */     String expression = element.getAttribute("expression");
/* 193:    */     try
/* 194:    */     {
/* 195:229 */       if ("annotation".equals(filterType)) {
/* 196:230 */         return new AnnotationTypeFilter(classLoader.loadClass(expression));
/* 197:    */       }
/* 198:232 */       if ("assignable".equals(filterType)) {
/* 199:233 */         return new AssignableTypeFilter(classLoader.loadClass(expression));
/* 200:    */       }
/* 201:235 */       if ("aspectj".equals(filterType)) {
/* 202:236 */         return new AspectJTypeFilter(expression, classLoader);
/* 203:    */       }
/* 204:238 */       if ("regex".equals(filterType)) {
/* 205:239 */         return new RegexPatternTypeFilter(Pattern.compile(expression));
/* 206:    */       }
/* 207:241 */       if ("custom".equals(filterType))
/* 208:    */       {
/* 209:242 */         Class filterClass = classLoader.loadClass(expression);
/* 210:243 */         if (!TypeFilter.class.isAssignableFrom(filterClass)) {
/* 211:244 */           throw new IllegalArgumentException(
/* 212:245 */             "Class is not assignable to [" + TypeFilter.class.getName() + "]: " + expression);
/* 213:    */         }
/* 214:247 */         return (TypeFilter)BeanUtils.instantiateClass(filterClass);
/* 215:    */       }
/* 216:250 */       throw new IllegalArgumentException("Unsupported filter type: " + filterType);
/* 217:    */     }
/* 218:    */     catch (ClassNotFoundException ex)
/* 219:    */     {
/* 220:254 */       throw new FatalBeanException("Type filter class not found: " + expression, ex);
/* 221:    */     }
/* 222:    */   }
/* 223:    */   
/* 224:    */   private Object instantiateUserDefinedStrategy(String className, Class strategyType, ClassLoader classLoader)
/* 225:    */   {
/* 226:260 */     Object result = null;
/* 227:    */     try
/* 228:    */     {
/* 229:262 */       result = classLoader.loadClass(className).newInstance();
/* 230:    */     }
/* 231:    */     catch (ClassNotFoundException ex)
/* 232:    */     {
/* 233:265 */       throw new IllegalArgumentException("Class [" + className + "] for strategy [" + 
/* 234:266 */         strategyType.getName() + "] not found", ex);
/* 235:    */     }
/* 236:    */     catch (Exception ex)
/* 237:    */     {
/* 238:269 */       throw new IllegalArgumentException("Unable to instantiate class [" + className + "] for strategy [" + 
/* 239:270 */         strategyType.getName() + "]. A zero-argument constructor is required", ex);
/* 240:    */     }
/* 241:273 */     if (!strategyType.isAssignableFrom(result.getClass())) {
/* 242:274 */       throw new IllegalArgumentException("Provided class name must be an implementation of " + strategyType);
/* 243:    */     }
/* 244:276 */     return result;
/* 245:    */   }
/* 246:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.ComponentScanBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */