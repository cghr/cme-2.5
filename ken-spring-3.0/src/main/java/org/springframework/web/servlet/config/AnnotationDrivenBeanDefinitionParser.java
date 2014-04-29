/*   1:    */ package org.springframework.web.servlet.config;
/*   2:    */ 
/*   3:    */ import java.util.List;
/*   4:    */ import org.springframework.beans.MutablePropertyValues;
/*   5:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*   6:    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*   7:    */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*   8:    */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*   9:    */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*  10:    */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*  11:    */ import org.springframework.beans.factory.support.ManagedList;
/*  12:    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*  13:    */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*  14:    */ import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
/*  15:    */ import org.springframework.beans.factory.xml.ParserContext;
/*  16:    */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*  17:    */ import org.springframework.format.support.FormattingConversionServiceFactoryBean;
/*  18:    */ import org.springframework.http.converter.ByteArrayHttpMessageConverter;
/*  19:    */ import org.springframework.http.converter.HttpMessageConverter;
/*  20:    */ import org.springframework.http.converter.ResourceHttpMessageConverter;
/*  21:    */ import org.springframework.http.converter.StringHttpMessageConverter;
/*  22:    */ import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter;
/*  23:    */ import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
/*  24:    */ import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
/*  25:    */ import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
/*  26:    */ import org.springframework.http.converter.xml.SourceHttpMessageConverter;
/*  27:    */ import org.springframework.http.converter.xml.XmlAwareFormHttpMessageConverter;
/*  28:    */ import org.springframework.util.ClassUtils;
/*  29:    */ import org.springframework.util.xml.DomUtils;
/*  30:    */ import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
/*  31:    */ import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
/*  32:    */ import org.springframework.web.bind.support.WebArgumentResolver;
/*  33:    */ import org.springframework.web.servlet.handler.ConversionServiceExposingInterceptor;
/*  34:    */ import org.springframework.web.servlet.handler.MappedInterceptor;
/*  35:    */ import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
/*  36:    */ import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
/*  37:    */ import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
/*  38:    */ import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
/*  39:    */ import org.springframework.web.servlet.mvc.method.annotation.support.ServletWebArgumentResolverAdapter;
/*  40:    */ import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
/*  41:    */ import org.w3c.dom.Element;
/*  42:    */ 
/*  43:    */ class AnnotationDrivenBeanDefinitionParser
/*  44:    */   implements BeanDefinitionParser
/*  45:    */ {
/*  46: 88 */   private static final boolean jsr303Present = ClassUtils.isPresent(
/*  47: 89 */     "javax.validation.Validator", AnnotationDrivenBeanDefinitionParser.class.getClassLoader());
/*  48: 92 */   private static final boolean jaxb2Present = ClassUtils.isPresent("javax.xml.bind.Binder", AnnotationDrivenBeanDefinitionParser.class.getClassLoader());
/*  49: 95 */   private static final boolean jacksonPresent = (ClassUtils.isPresent("org.codehaus.jackson.map.ObjectMapper", AnnotationDrivenBeanDefinitionParser.class.getClassLoader())) && 
/*  50: 96 */     (ClassUtils.isPresent("org.codehaus.jackson.JsonGenerator", AnnotationDrivenBeanDefinitionParser.class.getClassLoader()));
/*  51: 99 */   private static boolean romePresent = ClassUtils.isPresent("com.sun.syndication.feed.WireFeed", AnnotationDrivenBeanDefinitionParser.class.getClassLoader());
/*  52:    */   
/*  53:    */   public BeanDefinition parse(Element element, ParserContext parserContext)
/*  54:    */   {
/*  55:102 */     Object source = parserContext.extractSource(element);
/*  56:    */     
/*  57:104 */     CompositeComponentDefinition compDefinition = new CompositeComponentDefinition(element.getTagName(), source);
/*  58:105 */     parserContext.pushContainingComponent(compDefinition);
/*  59:    */     
/*  60:107 */     RootBeanDefinition methodMappingDef = new RootBeanDefinition(RequestMappingHandlerMapping.class);
/*  61:108 */     methodMappingDef.setSource(source);
/*  62:109 */     methodMappingDef.setRole(2);
/*  63:110 */     methodMappingDef.getPropertyValues().add("order", Integer.valueOf(0));
/*  64:111 */     String methodMappingName = parserContext.getReaderContext().registerWithGeneratedName(methodMappingDef);
/*  65:    */     
/*  66:113 */     RuntimeBeanReference conversionService = getConversionService(element, source, parserContext);
/*  67:114 */     RuntimeBeanReference validator = getValidator(element, source, parserContext);
/*  68:115 */     RuntimeBeanReference messageCodesResolver = getMessageCodesResolver(element, source, parserContext);
/*  69:    */     
/*  70:117 */     RootBeanDefinition bindingDef = new RootBeanDefinition(ConfigurableWebBindingInitializer.class);
/*  71:118 */     bindingDef.setSource(source);
/*  72:119 */     bindingDef.setRole(2);
/*  73:120 */     bindingDef.getPropertyValues().add("conversionService", conversionService);
/*  74:121 */     bindingDef.getPropertyValues().add("validator", validator);
/*  75:122 */     bindingDef.getPropertyValues().add("messageCodesResolver", messageCodesResolver);
/*  76:    */     
/*  77:124 */     ManagedList<?> messageConverters = getMessageConverters(element, source, parserContext);
/*  78:125 */     ManagedList<?> argumentResolvers = getArgumentResolvers(element, source, parserContext);
/*  79:126 */     ManagedList<?> returnValueHandlers = getReturnValueHandlers(element, source, parserContext);
/*  80:    */     
/*  81:128 */     RootBeanDefinition methodAdapterDef = new RootBeanDefinition(RequestMappingHandlerAdapter.class);
/*  82:129 */     methodAdapterDef.setSource(source);
/*  83:130 */     methodAdapterDef.setRole(2);
/*  84:131 */     methodAdapterDef.getPropertyValues().add("webBindingInitializer", bindingDef);
/*  85:132 */     methodAdapterDef.getPropertyValues().add("messageConverters", messageConverters);
/*  86:133 */     methodAdapterDef.getPropertyValues().add("ignoreDefaultModelOnRedirect", Boolean.valueOf(true));
/*  87:134 */     if (argumentResolvers != null) {
/*  88:135 */       methodAdapterDef.getPropertyValues().add("customArgumentResolvers", argumentResolvers);
/*  89:    */     }
/*  90:137 */     if (returnValueHandlers != null) {
/*  91:138 */       methodAdapterDef.getPropertyValues().add("customReturnValueHandlers", returnValueHandlers);
/*  92:    */     }
/*  93:140 */     String methodAdapterName = parserContext.getReaderContext().registerWithGeneratedName(methodAdapterDef);
/*  94:    */     
/*  95:142 */     RootBeanDefinition csInterceptorDef = new RootBeanDefinition(ConversionServiceExposingInterceptor.class);
/*  96:143 */     csInterceptorDef.setSource(source);
/*  97:144 */     csInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(0, conversionService);
/*  98:145 */     RootBeanDefinition mappedCsInterceptorDef = new RootBeanDefinition(MappedInterceptor.class);
/*  99:146 */     mappedCsInterceptorDef.setSource(source);
/* 100:147 */     mappedCsInterceptorDef.setRole(2);
/* 101:148 */     mappedCsInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(0, null);
/* 102:149 */     mappedCsInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(1, csInterceptorDef);
/* 103:150 */     String mappedInterceptorName = parserContext.getReaderContext().registerWithGeneratedName(mappedCsInterceptorDef);
/* 104:    */     
/* 105:152 */     RootBeanDefinition methodExceptionResolver = new RootBeanDefinition(ExceptionHandlerExceptionResolver.class);
/* 106:153 */     methodExceptionResolver.setSource(source);
/* 107:154 */     methodExceptionResolver.setRole(2);
/* 108:155 */     methodExceptionResolver.getPropertyValues().add("messageConverters", messageConverters);
/* 109:156 */     methodExceptionResolver.getPropertyValues().add("order", Integer.valueOf(0));
/* 110:157 */     String methodExceptionResolverName = 
/* 111:158 */       parserContext.getReaderContext().registerWithGeneratedName(methodExceptionResolver);
/* 112:    */     
/* 113:160 */     RootBeanDefinition responseStatusExceptionResolver = new RootBeanDefinition(ResponseStatusExceptionResolver.class);
/* 114:161 */     responseStatusExceptionResolver.setSource(source);
/* 115:162 */     responseStatusExceptionResolver.setRole(2);
/* 116:163 */     responseStatusExceptionResolver.getPropertyValues().add("order", Integer.valueOf(1));
/* 117:164 */     String responseStatusExceptionResolverName = 
/* 118:165 */       parserContext.getReaderContext().registerWithGeneratedName(responseStatusExceptionResolver);
/* 119:    */     
/* 120:167 */     RootBeanDefinition defaultExceptionResolver = new RootBeanDefinition(DefaultHandlerExceptionResolver.class);
/* 121:168 */     defaultExceptionResolver.setSource(source);
/* 122:169 */     defaultExceptionResolver.setRole(2);
/* 123:170 */     defaultExceptionResolver.getPropertyValues().add("order", Integer.valueOf(2));
/* 124:171 */     String defaultExceptionResolverName = 
/* 125:172 */       parserContext.getReaderContext().registerWithGeneratedName(defaultExceptionResolver);
/* 126:    */     
/* 127:174 */     parserContext.registerComponent(new BeanComponentDefinition(methodMappingDef, methodMappingName));
/* 128:175 */     parserContext.registerComponent(new BeanComponentDefinition(methodAdapterDef, methodAdapterName));
/* 129:176 */     parserContext.registerComponent(new BeanComponentDefinition(methodExceptionResolver, methodExceptionResolverName));
/* 130:177 */     parserContext.registerComponent(new BeanComponentDefinition(responseStatusExceptionResolver, responseStatusExceptionResolverName));
/* 131:178 */     parserContext.registerComponent(new BeanComponentDefinition(defaultExceptionResolver, defaultExceptionResolverName));
/* 132:179 */     parserContext.registerComponent(new BeanComponentDefinition(mappedCsInterceptorDef, mappedInterceptorName));
/* 133:    */     
/* 134:    */ 
/* 135:182 */     MvcNamespaceUtils.registerBeanNameUrlHandlerMapping(parserContext, source);
/* 136:    */     
/* 137:    */ 
/* 138:185 */     MvcNamespaceUtils.registerDefaultHandlerAdapters(parserContext, source);
/* 139:    */     
/* 140:187 */     parserContext.popAndRegisterContainingComponent();
/* 141:    */     
/* 142:189 */     return null;
/* 143:    */   }
/* 144:    */   
/* 145:    */   private RuntimeBeanReference getConversionService(Element element, Object source, ParserContext parserContext)
/* 146:    */   {
/* 147:    */     RuntimeBeanReference conversionServiceRef;
/* 148:    */     RuntimeBeanReference conversionServiceRef;
/* 149:194 */     if (element.hasAttribute("conversion-service"))
/* 150:    */     {
/* 151:195 */       conversionServiceRef = new RuntimeBeanReference(element.getAttribute("conversion-service"));
/* 152:    */     }
/* 153:    */     else
/* 154:    */     {
/* 155:198 */       RootBeanDefinition conversionDef = new RootBeanDefinition(FormattingConversionServiceFactoryBean.class);
/* 156:199 */       conversionDef.setSource(source);
/* 157:200 */       conversionDef.setRole(2);
/* 158:201 */       String conversionName = parserContext.getReaderContext().registerWithGeneratedName(conversionDef);
/* 159:202 */       parserContext.registerComponent(new BeanComponentDefinition(conversionDef, conversionName));
/* 160:203 */       conversionServiceRef = new RuntimeBeanReference(conversionName);
/* 161:    */     }
/* 162:205 */     return conversionServiceRef;
/* 163:    */   }
/* 164:    */   
/* 165:    */   private RuntimeBeanReference getValidator(Element element, Object source, ParserContext parserContext)
/* 166:    */   {
/* 167:209 */     if (element.hasAttribute("validator")) {
/* 168:210 */       return new RuntimeBeanReference(element.getAttribute("validator"));
/* 169:    */     }
/* 170:212 */     if (jsr303Present)
/* 171:    */     {
/* 172:213 */       RootBeanDefinition validatorDef = new RootBeanDefinition(LocalValidatorFactoryBean.class);
/* 173:214 */       validatorDef.setSource(source);
/* 174:215 */       validatorDef.setRole(2);
/* 175:216 */       String validatorName = parserContext.getReaderContext().registerWithGeneratedName(validatorDef);
/* 176:217 */       parserContext.registerComponent(new BeanComponentDefinition(validatorDef, validatorName));
/* 177:218 */       return new RuntimeBeanReference(validatorName);
/* 178:    */     }
/* 179:221 */     return null;
/* 180:    */   }
/* 181:    */   
/* 182:    */   private RuntimeBeanReference getMessageCodesResolver(Element element, Object source, ParserContext parserContext)
/* 183:    */   {
/* 184:226 */     if (element.hasAttribute("message-codes-resolver")) {
/* 185:227 */       return new RuntimeBeanReference(element.getAttribute("message-codes-resolver"));
/* 186:    */     }
/* 187:229 */     return null;
/* 188:    */   }
/* 189:    */   
/* 190:    */   private ManagedList<?> getArgumentResolvers(Element element, Object source, ParserContext parserContext)
/* 191:    */   {
/* 192:234 */     Element resolversElement = DomUtils.getChildElementByTagName(element, "argument-resolvers");
/* 193:235 */     if (resolversElement != null)
/* 194:    */     {
/* 195:236 */       ManagedList<BeanDefinitionHolder> argumentResolvers = extractBeanSubElements(resolversElement, parserContext);
/* 196:237 */       return wrapWebArgumentResolverBeanDefs(argumentResolvers);
/* 197:    */     }
/* 198:239 */     return null;
/* 199:    */   }
/* 200:    */   
/* 201:    */   private ManagedList<?> getReturnValueHandlers(Element element, Object source, ParserContext parserContext)
/* 202:    */   {
/* 203:243 */     Element handlersElement = DomUtils.getChildElementByTagName(element, "return-value-handlers");
/* 204:244 */     if (handlersElement != null) {
/* 205:245 */       return extractBeanSubElements(handlersElement, parserContext);
/* 206:    */     }
/* 207:247 */     return null;
/* 208:    */   }
/* 209:    */   
/* 210:    */   private ManagedList<?> getMessageConverters(Element element, Object source, ParserContext parserContext)
/* 211:    */   {
/* 212:251 */     Element convertersElement = DomUtils.getChildElementByTagName(element, "message-converters");
/* 213:252 */     ManagedList<? super Object> messageConverters = new ManagedList();
/* 214:253 */     if (convertersElement != null)
/* 215:    */     {
/* 216:254 */       messageConverters.setSource(source);
/* 217:255 */       for (Element converter : DomUtils.getChildElementsByTagName(convertersElement, "bean"))
/* 218:    */       {
/* 219:256 */         BeanDefinitionHolder beanDef = parserContext.getDelegate().parseBeanDefinitionElement(converter);
/* 220:257 */         beanDef = parserContext.getDelegate().decorateBeanDefinitionIfRequired(converter, beanDef);
/* 221:258 */         messageConverters.add(beanDef);
/* 222:    */       }
/* 223:    */     }
/* 224:262 */     if ((convertersElement == null) || (Boolean.valueOf(convertersElement.getAttribute("register-defaults")).booleanValue()))
/* 225:    */     {
/* 226:263 */       messageConverters.setSource(source);
/* 227:264 */       messageConverters.add(createConverterBeanDefinition(ByteArrayHttpMessageConverter.class, source));
/* 228:    */       
/* 229:266 */       RootBeanDefinition stringConverterDef = createConverterBeanDefinition(StringHttpMessageConverter.class, 
/* 230:267 */         source);
/* 231:268 */       stringConverterDef.getPropertyValues().add("writeAcceptCharset", Boolean.valueOf(false));
/* 232:269 */       messageConverters.add(stringConverterDef);
/* 233:    */       
/* 234:271 */       messageConverters.add(createConverterBeanDefinition(ResourceHttpMessageConverter.class, source));
/* 235:272 */       messageConverters.add(createConverterBeanDefinition(SourceHttpMessageConverter.class, source));
/* 236:273 */       messageConverters.add(createConverterBeanDefinition(XmlAwareFormHttpMessageConverter.class, source));
/* 237:274 */       if (jaxb2Present) {
/* 238:276 */         messageConverters.add(createConverterBeanDefinition(Jaxb2RootElementHttpMessageConverter.class, source));
/* 239:    */       }
/* 240:278 */       if (jacksonPresent) {
/* 241:279 */         messageConverters.add(createConverterBeanDefinition(MappingJacksonHttpMessageConverter.class, source));
/* 242:    */       }
/* 243:281 */       if (romePresent)
/* 244:    */       {
/* 245:282 */         messageConverters.add(createConverterBeanDefinition(AtomFeedHttpMessageConverter.class, source));
/* 246:283 */         messageConverters.add(createConverterBeanDefinition(RssChannelHttpMessageConverter.class, source));
/* 247:    */       }
/* 248:    */     }
/* 249:286 */     return messageConverters;
/* 250:    */   }
/* 251:    */   
/* 252:    */   private RootBeanDefinition createConverterBeanDefinition(Class<? extends HttpMessageConverter> converterClass, Object source)
/* 253:    */   {
/* 254:291 */     RootBeanDefinition beanDefinition = new RootBeanDefinition(converterClass);
/* 255:292 */     beanDefinition.setSource(source);
/* 256:293 */     beanDefinition.setRole(2);
/* 257:    */     
/* 258:295 */     return beanDefinition;
/* 259:    */   }
/* 260:    */   
/* 261:    */   private ManagedList<BeanDefinitionHolder> extractBeanSubElements(Element parentElement, ParserContext parserContext)
/* 262:    */   {
/* 263:299 */     ManagedList<BeanDefinitionHolder> list = new ManagedList();
/* 264:300 */     list.setSource(parserContext.extractSource(parentElement));
/* 265:301 */     for (Element beanElement : DomUtils.getChildElementsByTagName(parentElement, "bean"))
/* 266:    */     {
/* 267:302 */       BeanDefinitionHolder beanDef = parserContext.getDelegate().parseBeanDefinitionElement(beanElement);
/* 268:303 */       beanDef = parserContext.getDelegate().decorateBeanDefinitionIfRequired(beanElement, beanDef);
/* 269:304 */       list.add(beanDef);
/* 270:    */     }
/* 271:306 */     return list;
/* 272:    */   }
/* 273:    */   
/* 274:    */   private ManagedList<BeanDefinitionHolder> wrapWebArgumentResolverBeanDefs(List<BeanDefinitionHolder> beanDefs)
/* 275:    */   {
/* 276:310 */     ManagedList<BeanDefinitionHolder> result = new ManagedList();
/* 277:312 */     for (BeanDefinitionHolder beanDef : beanDefs)
/* 278:    */     {
/* 279:313 */       String className = beanDef.getBeanDefinition().getBeanClassName();
/* 280:314 */       Class<?> clazz = ClassUtils.resolveClassName(className, ClassUtils.getDefaultClassLoader());
/* 281:316 */       if (WebArgumentResolver.class.isAssignableFrom(clazz))
/* 282:    */       {
/* 283:317 */         RootBeanDefinition adapter = new RootBeanDefinition(ServletWebArgumentResolverAdapter.class);
/* 284:318 */         adapter.getConstructorArgumentValues().addIndexedArgumentValue(0, beanDef);
/* 285:319 */         result.add(new BeanDefinitionHolder(adapter, beanDef.getBeanName() + "Adapter"));
/* 286:    */       }
/* 287:    */       else
/* 288:    */       {
/* 289:321 */         result.add(beanDef);
/* 290:    */       }
/* 291:    */     }
/* 292:325 */     return result;
/* 293:    */   }
/* 294:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.config.AnnotationDrivenBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */