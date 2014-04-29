/*   1:    */ package org.springframework.web.servlet.config.annotation;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import javax.servlet.ServletContext;
/*   6:    */ import javax.servlet.http.HttpServletRequest;
/*   7:    */ import org.springframework.beans.BeanUtils;
/*   8:    */ import org.springframework.beans.BeansException;
/*   9:    */ import org.springframework.beans.factory.BeanInitializationException;
/*  10:    */ import org.springframework.context.ApplicationContext;
/*  11:    */ import org.springframework.context.ApplicationContextAware;
/*  12:    */ import org.springframework.context.annotation.Bean;
/*  13:    */ import org.springframework.format.FormatterRegistry;
/*  14:    */ import org.springframework.format.support.DefaultFormattingConversionService;
/*  15:    */ import org.springframework.format.support.FormattingConversionService;
/*  16:    */ import org.springframework.http.converter.ByteArrayHttpMessageConverter;
/*  17:    */ import org.springframework.http.converter.HttpMessageConverter;
/*  18:    */ import org.springframework.http.converter.ResourceHttpMessageConverter;
/*  19:    */ import org.springframework.http.converter.StringHttpMessageConverter;
/*  20:    */ import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter;
/*  21:    */ import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
/*  22:    */ import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
/*  23:    */ import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
/*  24:    */ import org.springframework.http.converter.xml.SourceHttpMessageConverter;
/*  25:    */ import org.springframework.http.converter.xml.XmlAwareFormHttpMessageConverter;
/*  26:    */ import org.springframework.util.ClassUtils;
/*  27:    */ import org.springframework.validation.Errors;
/*  28:    */ import org.springframework.validation.Validator;
/*  29:    */ import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
/*  30:    */ import org.springframework.web.context.ServletContextAware;
/*  31:    */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*  32:    */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*  33:    */ import org.springframework.web.servlet.HandlerExceptionResolver;
/*  34:    */ import org.springframework.web.servlet.HandlerMapping;
/*  35:    */ import org.springframework.web.servlet.handler.AbstractHandlerMapping;
/*  36:    */ import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
/*  37:    */ import org.springframework.web.servlet.handler.ConversionServiceExposingInterceptor;
/*  38:    */ import org.springframework.web.servlet.handler.HandlerExceptionResolverComposite;
/*  39:    */ import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
/*  40:    */ import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter;
/*  41:    */ import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
/*  42:    */ import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
/*  43:    */ import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
/*  44:    */ import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
/*  45:    */ import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
/*  46:    */ 
/*  47:    */ public abstract class WebMvcConfigurationSupport
/*  48:    */   implements ApplicationContextAware, ServletContextAware
/*  49:    */ {
/*  50:    */   private ServletContext servletContext;
/*  51:    */   private ApplicationContext applicationContext;
/*  52:    */   private List<Object> interceptors;
/*  53:    */   private List<HttpMessageConverter<?>> messageConverters;
/*  54:    */   
/*  55:    */   public void setServletContext(ServletContext servletContext)
/*  56:    */   {
/*  57:149 */     this.servletContext = servletContext;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setApplicationContext(ApplicationContext applicationContext)
/*  61:    */     throws BeansException
/*  62:    */   {
/*  63:153 */     this.applicationContext = applicationContext;
/*  64:    */   }
/*  65:    */   
/*  66:    */   @Bean
/*  67:    */   public RequestMappingHandlerMapping requestMappingHandlerMapping()
/*  68:    */   {
/*  69:162 */     RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping();
/*  70:163 */     handlerMapping.setOrder(0);
/*  71:164 */     handlerMapping.setInterceptors(getInterceptors());
/*  72:165 */     return handlerMapping;
/*  73:    */   }
/*  74:    */   
/*  75:    */   protected final Object[] getInterceptors()
/*  76:    */   {
/*  77:174 */     if (this.interceptors == null)
/*  78:    */     {
/*  79:175 */       InterceptorRegistry registry = new InterceptorRegistry();
/*  80:176 */       addInterceptors(registry);
/*  81:177 */       registry.addInterceptor(new ConversionServiceExposingInterceptor(mvcConversionService()));
/*  82:178 */       this.interceptors = registry.getInterceptors();
/*  83:    */     }
/*  84:180 */     return this.interceptors.toArray();
/*  85:    */   }
/*  86:    */   
/*  87:    */   protected void addInterceptors(InterceptorRegistry registry) {}
/*  88:    */   
/*  89:    */   @Bean
/*  90:    */   public HandlerMapping viewControllerHandlerMapping()
/*  91:    */   {
/*  92:198 */     ViewControllerRegistry registry = new ViewControllerRegistry();
/*  93:199 */     addViewControllers(registry);
/*  94:    */     
/*  95:201 */     AbstractHandlerMapping handlerMapping = registry.getHandlerMapping();
/*  96:202 */     handlerMapping = handlerMapping != null ? handlerMapping : new EmptyHandlerMapping(null);
/*  97:203 */     handlerMapping.setInterceptors(getInterceptors());
/*  98:204 */     return handlerMapping;
/*  99:    */   }
/* 100:    */   
/* 101:    */   protected void addViewControllers(ViewControllerRegistry registry) {}
/* 102:    */   
/* 103:    */   @Bean
/* 104:    */   public BeanNameUrlHandlerMapping beanNameHandlerMapping()
/* 105:    */   {
/* 106:220 */     BeanNameUrlHandlerMapping mapping = new BeanNameUrlHandlerMapping();
/* 107:221 */     mapping.setOrder(2);
/* 108:222 */     mapping.setInterceptors(getInterceptors());
/* 109:223 */     return mapping;
/* 110:    */   }
/* 111:    */   
/* 112:    */   @Bean
/* 113:    */   public HandlerMapping resourceHandlerMapping()
/* 114:    */   {
/* 115:233 */     ResourceHandlerRegistry registry = new ResourceHandlerRegistry(this.applicationContext, this.servletContext);
/* 116:234 */     addResourceHandlers(registry);
/* 117:235 */     AbstractHandlerMapping handlerMapping = registry.getHandlerMapping();
/* 118:236 */     handlerMapping = handlerMapping != null ? handlerMapping : new EmptyHandlerMapping(null);
/* 119:237 */     return handlerMapping;
/* 120:    */   }
/* 121:    */   
/* 122:    */   protected void addResourceHandlers(ResourceHandlerRegistry registry) {}
/* 123:    */   
/* 124:    */   @Bean
/* 125:    */   public HandlerMapping defaultServletHandlerMapping()
/* 126:    */   {
/* 127:254 */     DefaultServletHandlerConfigurer configurer = new DefaultServletHandlerConfigurer(this.servletContext);
/* 128:255 */     configureDefaultServletHandling(configurer);
/* 129:256 */     AbstractHandlerMapping handlerMapping = configurer.getHandlerMapping();
/* 130:257 */     handlerMapping = handlerMapping != null ? handlerMapping : new EmptyHandlerMapping(null);
/* 131:258 */     return handlerMapping;
/* 132:    */   }
/* 133:    */   
/* 134:    */   protected void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {}
/* 135:    */   
/* 136:    */   @Bean
/* 137:    */   public RequestMappingHandlerAdapter requestMappingHandlerAdapter()
/* 138:    */   {
/* 139:280 */     ConfigurableWebBindingInitializer webBindingInitializer = new ConfigurableWebBindingInitializer();
/* 140:281 */     webBindingInitializer.setConversionService(mvcConversionService());
/* 141:282 */     webBindingInitializer.setValidator(mvcValidator());
/* 142:    */     
/* 143:284 */     List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList();
/* 144:285 */     addArgumentResolvers(argumentResolvers);
/* 145:    */     
/* 146:287 */     List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList();
/* 147:288 */     addReturnValueHandlers(returnValueHandlers);
/* 148:    */     
/* 149:290 */     RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
/* 150:291 */     adapter.setMessageConverters(getMessageConverters());
/* 151:292 */     adapter.setWebBindingInitializer(webBindingInitializer);
/* 152:293 */     adapter.setCustomArgumentResolvers(argumentResolvers);
/* 153:294 */     adapter.setCustomReturnValueHandlers(returnValueHandlers);
/* 154:295 */     adapter.setIgnoreDefaultModelOnRedirect(true);
/* 155:296 */     return adapter;
/* 156:    */   }
/* 157:    */   
/* 158:    */   protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {}
/* 159:    */   
/* 160:    */   protected void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {}
/* 161:    */   
/* 162:    */   protected final List<HttpMessageConverter<?>> getMessageConverters()
/* 163:    */   {
/* 164:337 */     if (this.messageConverters == null)
/* 165:    */     {
/* 166:338 */       this.messageConverters = new ArrayList();
/* 167:339 */       configureMessageConverters(this.messageConverters);
/* 168:340 */       if (this.messageConverters.isEmpty()) {
/* 169:341 */         addDefaultHttpMessageConverters(this.messageConverters);
/* 170:    */       }
/* 171:    */     }
/* 172:344 */     return this.messageConverters;
/* 173:    */   }
/* 174:    */   
/* 175:    */   protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {}
/* 176:    */   
/* 177:    */   protected final void addDefaultHttpMessageConverters(List<HttpMessageConverter<?>> messageConverters)
/* 178:    */   {
/* 179:365 */     StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
/* 180:366 */     stringConverter.setWriteAcceptCharset(false);
/* 181:    */     
/* 182:368 */     messageConverters.add(new ByteArrayHttpMessageConverter());
/* 183:369 */     messageConverters.add(stringConverter);
/* 184:370 */     messageConverters.add(new ResourceHttpMessageConverter());
/* 185:371 */     messageConverters.add(new SourceHttpMessageConverter());
/* 186:372 */     messageConverters.add(new XmlAwareFormHttpMessageConverter());
/* 187:    */     
/* 188:374 */     ClassLoader classLoader = getClass().getClassLoader();
/* 189:375 */     if (ClassUtils.isPresent("javax.xml.bind.Binder", classLoader)) {
/* 190:376 */       messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
/* 191:    */     }
/* 192:378 */     if (ClassUtils.isPresent("org.codehaus.jackson.map.ObjectMapper", classLoader)) {
/* 193:379 */       messageConverters.add(new MappingJacksonHttpMessageConverter());
/* 194:    */     }
/* 195:381 */     if (ClassUtils.isPresent("com.sun.syndication.feed.WireFeed", classLoader))
/* 196:    */     {
/* 197:382 */       messageConverters.add(new AtomFeedHttpMessageConverter());
/* 198:383 */       messageConverters.add(new RssChannelHttpMessageConverter());
/* 199:    */     }
/* 200:    */   }
/* 201:    */   
/* 202:    */   @Bean
/* 203:    */   public FormattingConversionService mvcConversionService()
/* 204:    */   {
/* 205:394 */     FormattingConversionService conversionService = new DefaultFormattingConversionService();
/* 206:395 */     addFormatters(conversionService);
/* 207:396 */     return conversionService;
/* 208:    */   }
/* 209:    */   
/* 210:    */   protected void addFormatters(FormatterRegistry registry) {}
/* 211:    */   
/* 212:    */   @Bean
/* 213:    */   public Validator mvcValidator()
/* 214:    */   {
/* 215:415 */     Validator validator = getValidator();
/* 216:416 */     if (validator == null) {
/* 217:417 */       if (ClassUtils.isPresent("javax.validation.Validator", getClass().getClassLoader()))
/* 218:    */       {
/* 219:    */         try
/* 220:    */         {
/* 221:420 */           String className = "org.springframework.validation.beanvalidation.LocalValidatorFactoryBean";
/* 222:421 */           clazz = ClassUtils.forName(className, WebMvcConfigurationSupport.class.getClassLoader());
/* 223:    */         }
/* 224:    */         catch (ClassNotFoundException localClassNotFoundException)
/* 225:    */         {
/* 226:    */           Class<?> clazz;
/* 227:423 */           throw new BeanInitializationException("Could not find default validator");
/* 228:    */         }
/* 229:    */         catch (LinkageError localLinkageError)
/* 230:    */         {
/* 231:425 */           throw new BeanInitializationException("Could not find default validator");
/* 232:    */         }
/* 233:    */         Class<?> clazz;
/* 234:427 */         validator = (Validator)BeanUtils.instantiate(clazz);
/* 235:    */       }
/* 236:    */       else
/* 237:    */       {
/* 238:430 */         validator = new Validator()
/* 239:    */         {
/* 240:    */           public boolean supports(Class<?> clazz)
/* 241:    */           {
/* 242:432 */             return false;
/* 243:    */           }
/* 244:    */           
/* 245:    */           public void validate(Object target, Errors errors) {}
/* 246:    */         };
/* 247:    */       }
/* 248:    */     }
/* 249:439 */     return validator;
/* 250:    */   }
/* 251:    */   
/* 252:    */   protected Validator getValidator()
/* 253:    */   {
/* 254:446 */     return null;
/* 255:    */   }
/* 256:    */   
/* 257:    */   @Bean
/* 258:    */   public HttpRequestHandlerAdapter httpRequestHandlerAdapter()
/* 259:    */   {
/* 260:455 */     return new HttpRequestHandlerAdapter();
/* 261:    */   }
/* 262:    */   
/* 263:    */   @Bean
/* 264:    */   public SimpleControllerHandlerAdapter simpleControllerHandlerAdapter()
/* 265:    */   {
/* 266:464 */     return new SimpleControllerHandlerAdapter();
/* 267:    */   }
/* 268:    */   
/* 269:    */   @Bean
/* 270:    */   public HandlerExceptionResolver handlerExceptionResolver()
/* 271:    */     throws Exception
/* 272:    */   {
/* 273:474 */     List<HandlerExceptionResolver> exceptionResolvers = new ArrayList();
/* 274:475 */     configureHandlerExceptionResolvers(exceptionResolvers);
/* 275:477 */     if (exceptionResolvers.isEmpty()) {
/* 276:478 */       addDefaultHandlerExceptionResolvers(exceptionResolvers);
/* 277:    */     }
/* 278:481 */     HandlerExceptionResolverComposite composite = new HandlerExceptionResolverComposite();
/* 279:482 */     composite.setOrder(0);
/* 280:483 */     composite.setExceptionResolvers(exceptionResolvers);
/* 281:484 */     return composite;
/* 282:    */   }
/* 283:    */   
/* 284:    */   protected void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {}
/* 285:    */   
/* 286:    */   protected final void addDefaultHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers)
/* 287:    */   {
/* 288:513 */     ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver = new ExceptionHandlerExceptionResolver();
/* 289:514 */     exceptionHandlerExceptionResolver.setMessageConverters(getMessageConverters());
/* 290:515 */     exceptionHandlerExceptionResolver.afterPropertiesSet();
/* 291:    */     
/* 292:517 */     exceptionResolvers.add(exceptionHandlerExceptionResolver);
/* 293:518 */     exceptionResolvers.add(new ResponseStatusExceptionResolver());
/* 294:519 */     exceptionResolvers.add(new DefaultHandlerExceptionResolver());
/* 295:    */   }
/* 296:    */   
/* 297:    */   private static final class EmptyHandlerMapping
/* 298:    */     extends AbstractHandlerMapping
/* 299:    */   {
/* 300:    */     protected Object getHandlerInternal(HttpServletRequest request)
/* 301:    */       throws Exception
/* 302:    */     {
/* 303:526 */       return null;
/* 304:    */     }
/* 305:    */   }
/* 306:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
 * JD-Core Version:    0.7.0.1
 */