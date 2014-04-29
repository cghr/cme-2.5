/*   1:    */ package org.springframework.web.servlet.mvc.method.annotation;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.concurrent.ConcurrentHashMap;
/*   8:    */ import javax.servlet.http.HttpServletRequest;
/*   9:    */ import javax.servlet.http.HttpServletResponse;
/*  10:    */ import org.apache.commons.logging.Log;
/*  11:    */ import org.springframework.beans.factory.InitializingBean;
/*  12:    */ import org.springframework.http.converter.ByteArrayHttpMessageConverter;
/*  13:    */ import org.springframework.http.converter.HttpMessageConverter;
/*  14:    */ import org.springframework.http.converter.StringHttpMessageConverter;
/*  15:    */ import org.springframework.http.converter.xml.SourceHttpMessageConverter;
/*  16:    */ import org.springframework.http.converter.xml.XmlAwareFormHttpMessageConverter;
/*  17:    */ import org.springframework.web.context.request.ServletWebRequest;
/*  18:    */ import org.springframework.web.method.HandlerMethod;
/*  19:    */ import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
/*  20:    */ import org.springframework.web.method.annotation.support.MapMethodProcessor;
/*  21:    */ import org.springframework.web.method.annotation.support.ModelAttributeMethodProcessor;
/*  22:    */ import org.springframework.web.method.annotation.support.ModelMethodProcessor;
/*  23:    */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*  24:    */ import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
/*  25:    */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*  26:    */ import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
/*  27:    */ import org.springframework.web.method.support.ModelAndViewContainer;
/*  28:    */ import org.springframework.web.servlet.ModelAndView;
/*  29:    */ import org.springframework.web.servlet.View;
/*  30:    */ import org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver;
/*  31:    */ import org.springframework.web.servlet.mvc.method.annotation.support.HttpEntityMethodProcessor;
/*  32:    */ import org.springframework.web.servlet.mvc.method.annotation.support.ModelAndViewMethodReturnValueHandler;
/*  33:    */ import org.springframework.web.servlet.mvc.method.annotation.support.RequestResponseBodyMethodProcessor;
/*  34:    */ import org.springframework.web.servlet.mvc.method.annotation.support.ServletRequestMethodArgumentResolver;
/*  35:    */ import org.springframework.web.servlet.mvc.method.annotation.support.ServletResponseMethodArgumentResolver;
/*  36:    */ import org.springframework.web.servlet.mvc.method.annotation.support.ViewMethodReturnValueHandler;
/*  37:    */ import org.springframework.web.servlet.mvc.method.annotation.support.ViewNameMethodReturnValueHandler;
/*  38:    */ 
/*  39:    */ public class ExceptionHandlerExceptionResolver
/*  40:    */   extends AbstractHandlerMethodExceptionResolver
/*  41:    */   implements InitializingBean
/*  42:    */ {
/*  43:    */   private List<HandlerMethodArgumentResolver> customArgumentResolvers;
/*  44:    */   private List<HandlerMethodReturnValueHandler> customReturnValueHandlers;
/*  45:    */   private List<HttpMessageConverter<?>> messageConverters;
/*  46: 80 */   private final Map<Class<?>, ExceptionHandlerMethodResolver> exceptionHandlerMethodResolvers = new ConcurrentHashMap();
/*  47:    */   private HandlerMethodArgumentResolverComposite argumentResolvers;
/*  48:    */   private HandlerMethodReturnValueHandlerComposite returnValueHandlers;
/*  49:    */   
/*  50:    */   public ExceptionHandlerExceptionResolver()
/*  51:    */   {
/*  52: 91 */     StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
/*  53: 92 */     stringHttpMessageConverter.setWriteAcceptCharset(false);
/*  54:    */     
/*  55: 94 */     this.messageConverters = new ArrayList();
/*  56: 95 */     this.messageConverters.add(new ByteArrayHttpMessageConverter());
/*  57: 96 */     this.messageConverters.add(stringHttpMessageConverter);
/*  58: 97 */     this.messageConverters.add(new SourceHttpMessageConverter());
/*  59: 98 */     this.messageConverters.add(new XmlAwareFormHttpMessageConverter());
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void setCustomArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers)
/*  63:    */   {
/*  64:107 */     this.customArgumentResolvers = argumentResolvers;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public List<HandlerMethodArgumentResolver> getCustomArgumentResolvers()
/*  68:    */   {
/*  69:114 */     return this.customArgumentResolvers;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers)
/*  73:    */   {
/*  74:122 */     if (argumentResolvers == null)
/*  75:    */     {
/*  76:123 */       this.argumentResolvers = null;
/*  77:    */     }
/*  78:    */     else
/*  79:    */     {
/*  80:126 */       this.argumentResolvers = new HandlerMethodArgumentResolverComposite();
/*  81:127 */       this.argumentResolvers.addResolvers(argumentResolvers);
/*  82:    */     }
/*  83:    */   }
/*  84:    */   
/*  85:    */   public HandlerMethodArgumentResolverComposite getArgumentResolvers()
/*  86:    */   {
/*  87:136 */     return this.argumentResolvers;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setCustomReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers)
/*  91:    */   {
/*  92:145 */     this.customReturnValueHandlers = returnValueHandlers;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public List<HandlerMethodReturnValueHandler> getCustomReturnValueHandlers()
/*  96:    */   {
/*  97:152 */     return this.customReturnValueHandlers;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void setReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers)
/* 101:    */   {
/* 102:160 */     if (returnValueHandlers == null)
/* 103:    */     {
/* 104:161 */       this.returnValueHandlers = null;
/* 105:    */     }
/* 106:    */     else
/* 107:    */     {
/* 108:164 */       this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite();
/* 109:165 */       this.returnValueHandlers.addHandlers(returnValueHandlers);
/* 110:    */     }
/* 111:    */   }
/* 112:    */   
/* 113:    */   public HandlerMethodReturnValueHandlerComposite getReturnValueHandlers()
/* 114:    */   {
/* 115:174 */     return this.returnValueHandlers;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters)
/* 119:    */   {
/* 120:182 */     this.messageConverters = messageConverters;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public List<HttpMessageConverter<?>> getMessageConverters()
/* 124:    */   {
/* 125:189 */     return this.messageConverters;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public void afterPropertiesSet()
/* 129:    */   {
/* 130:193 */     if (this.argumentResolvers == null)
/* 131:    */     {
/* 132:194 */       List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
/* 133:195 */       this.argumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
/* 134:    */     }
/* 135:197 */     if (this.returnValueHandlers == null)
/* 136:    */     {
/* 137:198 */       List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers();
/* 138:199 */       this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite().addHandlers(handlers);
/* 139:    */     }
/* 140:    */   }
/* 141:    */   
/* 142:    */   protected List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers()
/* 143:    */   {
/* 144:208 */     List<HandlerMethodArgumentResolver> resolvers = new ArrayList();
/* 145:    */     
/* 146:    */ 
/* 147:211 */     resolvers.add(new ServletRequestMethodArgumentResolver());
/* 148:212 */     resolvers.add(new ServletResponseMethodArgumentResolver());
/* 149:215 */     if (getCustomArgumentResolvers() != null) {
/* 150:216 */       resolvers.addAll(getCustomArgumentResolvers());
/* 151:    */     }
/* 152:219 */     return resolvers;
/* 153:    */   }
/* 154:    */   
/* 155:    */   protected List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers()
/* 156:    */   {
/* 157:227 */     List<HandlerMethodReturnValueHandler> handlers = new ArrayList();
/* 158:    */     
/* 159:    */ 
/* 160:230 */     handlers.add(new ModelAndViewMethodReturnValueHandler());
/* 161:231 */     handlers.add(new ModelMethodProcessor());
/* 162:232 */     handlers.add(new ViewMethodReturnValueHandler());
/* 163:233 */     handlers.add(new HttpEntityMethodProcessor(getMessageConverters()));
/* 164:    */     
/* 165:    */ 
/* 166:236 */     handlers.add(new ModelAttributeMethodProcessor(false));
/* 167:237 */     handlers.add(new RequestResponseBodyMethodProcessor(getMessageConverters()));
/* 168:    */     
/* 169:    */ 
/* 170:240 */     handlers.add(new ViewNameMethodReturnValueHandler());
/* 171:241 */     handlers.add(new MapMethodProcessor());
/* 172:244 */     if (getCustomReturnValueHandlers() != null) {
/* 173:245 */       handlers.addAll(getCustomReturnValueHandlers());
/* 174:    */     }
/* 175:249 */     handlers.add(new ModelAttributeMethodProcessor(true));
/* 176:    */     
/* 177:251 */     return handlers;
/* 178:    */   }
/* 179:    */   
/* 180:    */   protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception exception)
/* 181:    */   {
/* 182:263 */     if (handlerMethod == null) {
/* 183:264 */       return null;
/* 184:    */     }
/* 185:267 */     ServletInvocableHandlerMethod exceptionHandlerMethod = getExceptionHandlerMethod(handlerMethod, exception);
/* 186:268 */     if (exceptionHandlerMethod == null) {
/* 187:269 */       return null;
/* 188:    */     }
/* 189:272 */     exceptionHandlerMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
/* 190:273 */     exceptionHandlerMethod.setHandlerMethodReturnValueHandlers(this.returnValueHandlers);
/* 191:    */     
/* 192:275 */     ServletWebRequest webRequest = new ServletWebRequest(request, response);
/* 193:276 */     ModelAndViewContainer mavContainer = new ModelAndViewContainer();
/* 194:    */     try
/* 195:    */     {
/* 196:279 */       if (this.logger.isDebugEnabled()) {
/* 197:280 */         this.logger.debug("Invoking @ExceptionHandler method: " + exceptionHandlerMethod);
/* 198:    */       }
/* 199:282 */       exceptionHandlerMethod.invokeAndHandle(webRequest, mavContainer, new Object[] { exception });
/* 200:    */     }
/* 201:    */     catch (Exception invocationEx)
/* 202:    */     {
/* 203:285 */       this.logger.error("Failed to invoke @ExceptionHandler method: " + exceptionHandlerMethod, invocationEx);
/* 204:286 */       return null;
/* 205:    */     }
/* 206:289 */     if (mavContainer.isRequestHandled()) {
/* 207:290 */       return new ModelAndView();
/* 208:    */     }
/* 209:293 */     ModelAndView mav = new ModelAndView().addAllObjects(mavContainer.getModel());
/* 210:294 */     mav.setViewName(mavContainer.getViewName());
/* 211:295 */     if (!mavContainer.isViewReference()) {
/* 212:296 */       mav.setView((View)mavContainer.getView());
/* 213:    */     }
/* 214:298 */     return mav;
/* 215:    */   }
/* 216:    */   
/* 217:    */   protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception)
/* 218:    */   {
/* 219:311 */     Class<?> handlerType = handlerMethod.getBeanType();
/* 220:312 */     Method method = getExceptionHandlerMethodResolver(handlerType).resolveMethod(exception);
/* 221:313 */     return method != null ? new ServletInvocableHandlerMethod(handlerMethod.getBean(), method) : null;
/* 222:    */   }
/* 223:    */   
/* 224:    */   private ExceptionHandlerMethodResolver getExceptionHandlerMethodResolver(Class<?> handlerType)
/* 225:    */   {
/* 226:320 */     ExceptionHandlerMethodResolver resolver = (ExceptionHandlerMethodResolver)this.exceptionHandlerMethodResolvers.get(handlerType);
/* 227:321 */     if (resolver == null)
/* 228:    */     {
/* 229:322 */       resolver = new ExceptionHandlerMethodResolver(handlerType);
/* 230:323 */       this.exceptionHandlerMethodResolvers.put(handlerType, resolver);
/* 231:    */     }
/* 232:325 */     return resolver;
/* 233:    */   }
/* 234:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver
 * JD-Core Version:    0.7.0.1
 */