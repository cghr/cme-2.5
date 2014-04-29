/*   1:    */ package org.springframework.web.servlet.handler;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.Comparator;
/*   6:    */ import java.util.LinkedHashMap;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Map;
/*   9:    */ import javax.servlet.http.HttpServletRequest;
/*  10:    */ import javax.servlet.http.HttpServletResponse;
/*  11:    */ import org.apache.commons.logging.Log;
/*  12:    */ import org.springframework.beans.BeansException;
/*  13:    */ import org.springframework.context.ApplicationContext;
/*  14:    */ import org.springframework.util.Assert;
/*  15:    */ import org.springframework.util.CollectionUtils;
/*  16:    */ import org.springframework.util.PathMatcher;
/*  17:    */ import org.springframework.web.servlet.HandlerExecutionChain;
/*  18:    */ import org.springframework.web.servlet.HandlerMapping;
/*  19:    */ import org.springframework.web.util.UrlPathHelper;
/*  20:    */ 
/*  21:    */ public abstract class AbstractUrlHandlerMapping
/*  22:    */   extends AbstractHandlerMapping
/*  23:    */ {
/*  24:    */   private Object rootHandler;
/*  25: 58 */   private boolean lazyInitHandlers = false;
/*  26: 60 */   private final Map<String, Object> handlerMap = new LinkedHashMap();
/*  27:    */   
/*  28:    */   public void setRootHandler(Object rootHandler)
/*  29:    */   {
/*  30: 69 */     this.rootHandler = rootHandler;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public Object getRootHandler()
/*  34:    */   {
/*  35: 77 */     return this.rootHandler;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setLazyInitHandlers(boolean lazyInitHandlers)
/*  39:    */   {
/*  40: 91 */     this.lazyInitHandlers = lazyInitHandlers;
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected Object getHandlerInternal(HttpServletRequest request)
/*  44:    */     throws Exception
/*  45:    */   {
/*  46:101 */     String lookupPath = getUrlPathHelper().getLookupPathForRequest(request);
/*  47:102 */     Object handler = lookupHandler(lookupPath, request);
/*  48:103 */     if (handler == null)
/*  49:    */     {
/*  50:106 */       Object rawHandler = null;
/*  51:107 */       if ("/".equals(lookupPath)) {
/*  52:108 */         rawHandler = getRootHandler();
/*  53:    */       }
/*  54:110 */       if (rawHandler == null) {
/*  55:111 */         rawHandler = getDefaultHandler();
/*  56:    */       }
/*  57:113 */       if (rawHandler != null)
/*  58:    */       {
/*  59:115 */         if ((rawHandler instanceof String))
/*  60:    */         {
/*  61:116 */           String handlerName = (String)rawHandler;
/*  62:117 */           rawHandler = getApplicationContext().getBean(handlerName);
/*  63:    */         }
/*  64:119 */         validateHandler(rawHandler, request);
/*  65:120 */         handler = buildPathExposingHandler(rawHandler, lookupPath, lookupPath, null);
/*  66:    */       }
/*  67:    */     }
/*  68:123 */     if ((handler != null) && (this.logger.isDebugEnabled())) {
/*  69:124 */       this.logger.debug("Mapping [" + lookupPath + "] to " + handler);
/*  70:126 */     } else if ((handler == null) && (this.logger.isTraceEnabled())) {
/*  71:127 */       this.logger.trace("No handler mapping found for [" + lookupPath + "]");
/*  72:    */     }
/*  73:129 */     return handler;
/*  74:    */   }
/*  75:    */   
/*  76:    */   protected Object lookupHandler(String urlPath, HttpServletRequest request)
/*  77:    */     throws Exception
/*  78:    */   {
/*  79:147 */     Object handler = this.handlerMap.get(urlPath);
/*  80:148 */     if (handler != null)
/*  81:    */     {
/*  82:150 */       if ((handler instanceof String))
/*  83:    */       {
/*  84:151 */         String handlerName = (String)handler;
/*  85:152 */         handler = getApplicationContext().getBean(handlerName);
/*  86:    */       }
/*  87:154 */       validateHandler(handler, request);
/*  88:155 */       return buildPathExposingHandler(handler, urlPath, urlPath, null);
/*  89:    */     }
/*  90:158 */     List<String> matchingPatterns = new ArrayList();
/*  91:159 */     for (String registeredPattern : this.handlerMap.keySet()) {
/*  92:160 */       if (getPathMatcher().match(registeredPattern, urlPath)) {
/*  93:161 */         matchingPatterns.add(registeredPattern);
/*  94:    */       }
/*  95:    */     }
/*  96:164 */     String bestPatternMatch = null;
/*  97:165 */     Object patternComparator = getPathMatcher().getPatternComparator(urlPath);
/*  98:166 */     if (!matchingPatterns.isEmpty())
/*  99:    */     {
/* 100:167 */       Collections.sort(matchingPatterns, (Comparator)patternComparator);
/* 101:168 */       if (this.logger.isDebugEnabled()) {
/* 102:169 */         this.logger.debug("Matching patterns for request [" + urlPath + "] are " + matchingPatterns);
/* 103:    */       }
/* 104:171 */       bestPatternMatch = (String)matchingPatterns.get(0);
/* 105:    */     }
/* 106:173 */     if (bestPatternMatch != null)
/* 107:    */     {
/* 108:174 */       handler = this.handlerMap.get(bestPatternMatch);
/* 109:176 */       if ((handler instanceof String))
/* 110:    */       {
/* 111:177 */         String handlerName = (String)handler;
/* 112:178 */         handler = getApplicationContext().getBean(handlerName);
/* 113:    */       }
/* 114:180 */       validateHandler(handler, request);
/* 115:181 */       String pathWithinMapping = getPathMatcher().extractPathWithinPattern(bestPatternMatch, urlPath);
/* 116:    */       
/* 117:    */ 
/* 118:    */ 
/* 119:185 */       Map<String, String> uriTemplateVariables = new LinkedHashMap();
/* 120:186 */       for (String matchingPattern : matchingPatterns) {
/* 121:187 */         if (((Comparator)patternComparator).compare(bestPatternMatch, matchingPattern) == 0) {
/* 122:189 */           uriTemplateVariables.putAll(getPathMatcher().extractUriTemplateVariables(matchingPattern, urlPath));
/* 123:    */         }
/* 124:    */       }
/* 125:192 */       if (this.logger.isDebugEnabled()) {
/* 126:193 */         this.logger.debug("URI Template variables for request [" + urlPath + "] are " + uriTemplateVariables);
/* 127:    */       }
/* 128:195 */       return buildPathExposingHandler(handler, bestPatternMatch, pathWithinMapping, uriTemplateVariables);
/* 129:    */     }
/* 130:198 */     return null;
/* 131:    */   }
/* 132:    */   
/* 133:    */   protected void validateHandler(Object handler, HttpServletRequest request)
/* 134:    */     throws Exception
/* 135:    */   {}
/* 136:    */   
/* 137:    */   protected Object buildPathExposingHandler(Object rawHandler, String bestMatchingPattern, String pathWithinMapping, Map<String, String> uriTemplateVariables)
/* 138:    */   {
/* 139:226 */     HandlerExecutionChain chain = new HandlerExecutionChain(rawHandler);
/* 140:227 */     chain.addInterceptor(new PathExposingHandlerInterceptor(bestMatchingPattern, pathWithinMapping));
/* 141:228 */     if (!CollectionUtils.isEmpty(uriTemplateVariables)) {
/* 142:229 */       chain.addInterceptor(new UriTemplateVariablesHandlerInterceptor(uriTemplateVariables));
/* 143:    */     }
/* 144:231 */     return chain;
/* 145:    */   }
/* 146:    */   
/* 147:    */   protected void exposePathWithinMapping(String bestMatchingPattern, String pathWithinMapping, HttpServletRequest request)
/* 148:    */   {
/* 149:241 */     request.setAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE, bestMatchingPattern);
/* 150:242 */     request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, pathWithinMapping);
/* 151:    */   }
/* 152:    */   
/* 153:    */   protected void exposeUriTemplateVariables(Map<String, String> uriTemplateVariables, HttpServletRequest request)
/* 154:    */   {
/* 155:252 */     request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, uriTemplateVariables);
/* 156:    */   }
/* 157:    */   
/* 158:    */   protected void registerHandler(String[] urlPaths, String beanName)
/* 159:    */     throws BeansException, IllegalStateException
/* 160:    */   {
/* 161:263 */     Assert.notNull(urlPaths, "URL path array must not be null");
/* 162:264 */     for (String urlPath : urlPaths) {
/* 163:265 */       registerHandler(urlPath, beanName);
/* 164:    */     }
/* 165:    */   }
/* 166:    */   
/* 167:    */   protected void registerHandler(String urlPath, Object handler)
/* 168:    */     throws BeansException, IllegalStateException
/* 169:    */   {
/* 170:278 */     Assert.notNull(urlPath, "URL path must not be null");
/* 171:279 */     Assert.notNull(handler, "Handler object must not be null");
/* 172:280 */     Object resolvedHandler = handler;
/* 173:283 */     if ((!this.lazyInitHandlers) && ((handler instanceof String)))
/* 174:    */     {
/* 175:284 */       String handlerName = (String)handler;
/* 176:285 */       if (getApplicationContext().isSingleton(handlerName)) {
/* 177:286 */         resolvedHandler = getApplicationContext().getBean(handlerName);
/* 178:    */       }
/* 179:    */     }
/* 180:290 */     Object mappedHandler = this.handlerMap.get(urlPath);
/* 181:291 */     if (mappedHandler != null)
/* 182:    */     {
/* 183:292 */       if (mappedHandler != resolvedHandler) {
/* 184:293 */         throw new IllegalStateException(
/* 185:294 */           "Cannot map " + getHandlerDescription(handler) + " to URL path [" + urlPath + 
/* 186:295 */           "]: There is already " + getHandlerDescription(mappedHandler) + " mapped.");
/* 187:    */       }
/* 188:    */     }
/* 189:299 */     else if (urlPath.equals("/"))
/* 190:    */     {
/* 191:300 */       if (this.logger.isInfoEnabled()) {
/* 192:301 */         this.logger.info("Root mapping to " + getHandlerDescription(handler));
/* 193:    */       }
/* 194:303 */       setRootHandler(resolvedHandler);
/* 195:    */     }
/* 196:305 */     else if (urlPath.equals("/*"))
/* 197:    */     {
/* 198:306 */       if (this.logger.isInfoEnabled()) {
/* 199:307 */         this.logger.info("Default mapping to " + getHandlerDescription(handler));
/* 200:    */       }
/* 201:309 */       setDefaultHandler(resolvedHandler);
/* 202:    */     }
/* 203:    */     else
/* 204:    */     {
/* 205:312 */       this.handlerMap.put(urlPath, resolvedHandler);
/* 206:313 */       if (this.logger.isInfoEnabled()) {
/* 207:314 */         this.logger.info("Mapped URL path [" + urlPath + "] onto " + getHandlerDescription(handler));
/* 208:    */       }
/* 209:    */     }
/* 210:    */   }
/* 211:    */   
/* 212:    */   private String getHandlerDescription(Object handler)
/* 213:    */   {
/* 214:321 */     return "handler " + ((handler instanceof String) ? "'" + handler + "'" : new StringBuilder("of type [").append(handler.getClass()).append("]").toString());
/* 215:    */   }
/* 216:    */   
/* 217:    */   public final Map<String, Object> getHandlerMap()
/* 218:    */   {
/* 219:332 */     return Collections.unmodifiableMap(this.handlerMap);
/* 220:    */   }
/* 221:    */   
/* 222:    */   protected boolean supportsTypeLevelMappings()
/* 223:    */   {
/* 224:339 */     return false;
/* 225:    */   }
/* 226:    */   
/* 227:    */   private class PathExposingHandlerInterceptor
/* 228:    */     extends HandlerInterceptorAdapter
/* 229:    */   {
/* 230:    */     private final String bestMatchingPattern;
/* 231:    */     private final String pathWithinMapping;
/* 232:    */     
/* 233:    */     public PathExposingHandlerInterceptor(String bestMatchingPattern, String pathWithinMapping)
/* 234:    */     {
/* 235:355 */       this.bestMatchingPattern = bestMatchingPattern;
/* 236:356 */       this.pathWithinMapping = pathWithinMapping;
/* 237:    */     }
/* 238:    */     
/* 239:    */     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
/* 240:    */     {
/* 241:361 */       AbstractUrlHandlerMapping.this.exposePathWithinMapping(this.bestMatchingPattern, this.pathWithinMapping, request);
/* 242:362 */       request.setAttribute(HandlerMapping.INTROSPECT_TYPE_LEVEL_MAPPING, Boolean.valueOf(AbstractUrlHandlerMapping.this.supportsTypeLevelMappings()));
/* 243:363 */       return true;
/* 244:    */     }
/* 245:    */   }
/* 246:    */   
/* 247:    */   private class UriTemplateVariablesHandlerInterceptor
/* 248:    */     extends HandlerInterceptorAdapter
/* 249:    */   {
/* 250:    */     private final Map<String, String> uriTemplateVariables;
/* 251:    */     
/* 252:    */     public UriTemplateVariablesHandlerInterceptor()
/* 253:    */     {
/* 254:378 */       this.uriTemplateVariables = uriTemplateVariables;
/* 255:    */     }
/* 256:    */     
/* 257:    */     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
/* 258:    */     {
/* 259:383 */       AbstractUrlHandlerMapping.this.exposeUriTemplateVariables(this.uriTemplateVariables, request);
/* 260:384 */       return true;
/* 261:    */     }
/* 262:    */   }
/* 263:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.handler.AbstractUrlHandlerMapping
 * JD-Core Version:    0.7.0.1
 */