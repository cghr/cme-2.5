/*   1:    */ package org.springframework.web.servlet.handler;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Collection;
/*   6:    */ import java.util.Collections;
/*   7:    */ import java.util.Comparator;
/*   8:    */ import java.util.LinkedHashMap;
/*   9:    */ import java.util.List;
/*  10:    */ import java.util.Map;
/*  11:    */ import java.util.Set;
/*  12:    */ import javax.servlet.http.HttpServletRequest;
/*  13:    */ import org.apache.commons.logging.Log;
/*  14:    */ import org.springframework.context.ApplicationContext;
/*  15:    */ import org.springframework.context.ApplicationContextException;
/*  16:    */ import org.springframework.util.ClassUtils;
/*  17:    */ import org.springframework.util.LinkedMultiValueMap;
/*  18:    */ import org.springframework.util.MultiValueMap;
/*  19:    */ import org.springframework.util.PathMatcher;
/*  20:    */ import org.springframework.util.ReflectionUtils.MethodFilter;
/*  21:    */ import org.springframework.web.method.HandlerMethod;
/*  22:    */ import org.springframework.web.method.HandlerMethodSelector;
/*  23:    */ import org.springframework.web.servlet.HandlerMapping;
/*  24:    */ import org.springframework.web.util.UrlPathHelper;
/*  25:    */ 
/*  26:    */ public abstract class AbstractHandlerMethodMapping<T>
/*  27:    */   extends AbstractHandlerMapping
/*  28:    */ {
/*  29: 55 */   private final Map<T, HandlerMethod> handlerMethods = new LinkedHashMap();
/*  30: 57 */   private final MultiValueMap<String, T> urlMap = new LinkedMultiValueMap();
/*  31:    */   
/*  32:    */   public Map<T, HandlerMethod> getHandlerMethods()
/*  33:    */   {
/*  34: 63 */     return Collections.unmodifiableMap(this.handlerMethods);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void initApplicationContext()
/*  38:    */     throws ApplicationContextException
/*  39:    */   {
/*  40: 71 */     super.initApplicationContext();
/*  41: 72 */     initHandlerMethods();
/*  42:    */   }
/*  43:    */   
/*  44:    */   protected void initHandlerMethods()
/*  45:    */   {
/*  46: 82 */     if (this.logger.isDebugEnabled()) {
/*  47: 83 */       this.logger.debug("Looking for request mappings in application context: " + getApplicationContext());
/*  48:    */     }
/*  49: 85 */     for (String beanName : getApplicationContext().getBeanNamesForType(Object.class)) {
/*  50: 86 */       if (isHandler(getApplicationContext().getType(beanName))) {
/*  51: 87 */         detectHandlerMethods(beanName);
/*  52:    */       }
/*  53:    */     }
/*  54: 90 */     handlerMethodsInitialized(getHandlerMethods());
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected abstract boolean isHandler(Class<?> paramClass);
/*  58:    */   
/*  59:    */   protected void handlerMethodsInitialized(Map<T, HandlerMethod> handlerMethods) {}
/*  60:    */   
/*  61:    */   protected void detectHandlerMethods(Object handler)
/*  62:    */   {
/*  63:112 */     Class<?> handlerType = (handler instanceof String) ? 
/*  64:113 */       getApplicationContext().getType((String)handler) : handler.getClass();
/*  65:    */     
/*  66:115 */     final Class<?> userType = ClassUtils.getUserClass(handlerType);
/*  67:    */     
/*  68:117 */     Set<Method> methods = HandlerMethodSelector.selectMethods(userType, new ReflectionUtils.MethodFilter()
/*  69:    */     {
/*  70:    */       public boolean matches(Method method)
/*  71:    */       {
/*  72:119 */         return AbstractHandlerMethodMapping.this.getMappingForMethod(method, userType) != null;
/*  73:    */       }
/*  74:    */     });
/*  75:123 */     for (Method method : methods)
/*  76:    */     {
/*  77:124 */       T mapping = getMappingForMethod(method, userType);
/*  78:125 */       registerHandlerMethod(handler, method, mapping);
/*  79:    */     }
/*  80:    */   }
/*  81:    */   
/*  82:    */   protected abstract T getMappingForMethod(Method paramMethod, Class<?> paramClass);
/*  83:    */   
/*  84:    */   protected void registerHandlerMethod(Object handler, Method method, T mapping)
/*  85:    */   {
/*  86:    */     HandlerMethod handlerMethod;
/*  87:    */     HandlerMethod handlerMethod;
/*  88:151 */     if ((handler instanceof String))
/*  89:    */     {
/*  90:152 */       String beanName = (String)handler;
/*  91:153 */       handlerMethod = new HandlerMethod(beanName, getApplicationContext(), method);
/*  92:    */     }
/*  93:    */     else
/*  94:    */     {
/*  95:156 */       handlerMethod = new HandlerMethod(handler, method);
/*  96:    */     }
/*  97:159 */     HandlerMethod oldHandlerMethod = (HandlerMethod)this.handlerMethods.get(mapping);
/*  98:160 */     if ((oldHandlerMethod != null) && (!oldHandlerMethod.equals(handlerMethod))) {
/*  99:161 */       throw new IllegalStateException("Ambiguous mapping found. Cannot map '" + handlerMethod.getBean() + 
/* 100:162 */         "' bean method \n" + handlerMethod + "\nto " + mapping + ": There is already '" + 
/* 101:163 */         oldHandlerMethod.getBean() + "' bean method\n" + oldHandlerMethod + " mapped.");
/* 102:    */     }
/* 103:166 */     this.handlerMethods.put(mapping, handlerMethod);
/* 104:167 */     if (this.logger.isInfoEnabled()) {
/* 105:168 */       this.logger.info("Mapped \"" + mapping + "\" onto " + handlerMethod);
/* 106:    */     }
/* 107:171 */     Set<String> patterns = getMappingPathPatterns(mapping);
/* 108:172 */     for (String pattern : patterns) {
/* 109:173 */       if (!getPathMatcher().isPattern(pattern)) {
/* 110:174 */         this.urlMap.add(pattern, mapping);
/* 111:    */       }
/* 112:    */     }
/* 113:    */   }
/* 114:    */   
/* 115:    */   protected abstract Set<String> getMappingPathPatterns(T paramT);
/* 116:    */   
/* 117:    */   protected HandlerMethod getHandlerInternal(HttpServletRequest request)
/* 118:    */     throws Exception
/* 119:    */   {
/* 120:189 */     String lookupPath = getUrlPathHelper().getLookupPathForRequest(request);
/* 121:190 */     if (this.logger.isDebugEnabled()) {
/* 122:191 */       this.logger.debug("Looking up handler method for path " + lookupPath);
/* 123:    */     }
/* 124:194 */     HandlerMethod handlerMethod = lookupHandlerMethod(lookupPath, request);
/* 125:196 */     if (this.logger.isDebugEnabled()) {
/* 126:197 */       if (handlerMethod != null) {
/* 127:198 */         this.logger.debug("Returning handler method [" + handlerMethod + "]");
/* 128:    */       } else {
/* 129:201 */         this.logger.debug("Did not find handler method for [" + lookupPath + "]");
/* 130:    */       }
/* 131:    */     }
/* 132:205 */     return handlerMethod != null ? handlerMethod.createWithResolvedBean() : null;
/* 133:    */   }
/* 134:    */   
/* 135:    */   protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request)
/* 136:    */     throws Exception
/* 137:    */   {
/* 138:220 */     List<T> mappings = (List)this.urlMap.get(lookupPath);
/* 139:221 */     if (mappings == null) {
/* 140:222 */       mappings = new ArrayList((Collection)this.handlerMethods.keySet());
/* 141:    */     }
/* 142:225 */     List<AbstractHandlerMethodMapping<T>.Match> matches = new ArrayList();
/* 143:227 */     for (T mapping : mappings)
/* 144:    */     {
/* 145:228 */       T match = getMatchingMapping(mapping, request);
/* 146:229 */       if (match != null) {
/* 147:230 */         matches.add(new Match(match, (HandlerMethod)this.handlerMethods.get(mapping), null));
/* 148:    */       }
/* 149:    */     }
/* 150:234 */     if (!matches.isEmpty())
/* 151:    */     {
/* 152:235 */       Comparator<AbstractHandlerMethodMapping<T>.Match> comparator = new MatchComparator(getMappingComparator(request));
/* 153:236 */       Collections.sort(matches, comparator);
/* 154:238 */       if (this.logger.isTraceEnabled()) {
/* 155:239 */         this.logger.trace("Found " + matches.size() + " matching mapping(s) for [" + lookupPath + "] : " + matches);
/* 156:    */       }
/* 157:242 */       Object bestMatch = (Match)matches.get(0);
/* 158:243 */       if (matches.size() > 1)
/* 159:    */       {
/* 160:244 */         AbstractHandlerMethodMapping<T>.Match secondBestMatch = (Match)matches.get(1);
/* 161:245 */         if (comparator.compare(bestMatch, secondBestMatch) == 0)
/* 162:    */         {
/* 163:246 */           Method m1 = ((Match)bestMatch).handlerMethod.getMethod();
/* 164:247 */           Method m2 = secondBestMatch.handlerMethod.getMethod();
/* 165:248 */           throw new IllegalStateException(
/* 166:249 */             "Ambiguous handler methods mapped for HTTP path '" + request.getRequestURL() + "': {" + 
/* 167:250 */             m1 + ", " + m2 + "}");
/* 168:    */         }
/* 169:    */       }
/* 170:254 */       handleMatch(((Match)bestMatch).mapping, lookupPath, request);
/* 171:255 */       return ((Match)bestMatch).handlerMethod;
/* 172:    */     }
/* 173:258 */     return handleNoMatch(this.handlerMethods.keySet(), lookupPath, request);
/* 174:    */   }
/* 175:    */   
/* 176:    */   protected abstract T getMatchingMapping(T paramT, HttpServletRequest paramHttpServletRequest);
/* 177:    */   
/* 178:    */   protected abstract Comparator<T> getMappingComparator(HttpServletRequest paramHttpServletRequest);
/* 179:    */   
/* 180:    */   protected void handleMatch(T mapping, String lookupPath, HttpServletRequest request)
/* 181:    */   {
/* 182:287 */     request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, lookupPath);
/* 183:    */   }
/* 184:    */   
/* 185:    */   protected HandlerMethod handleNoMatch(Set<T> mappings, String lookupPath, HttpServletRequest request)
/* 186:    */     throws Exception
/* 187:    */   {
/* 188:299 */     return null;
/* 189:    */   }
/* 190:    */   
/* 191:    */   private class Match
/* 192:    */   {
/* 193:    */     private final T mapping;
/* 194:    */     private final HandlerMethod handlerMethod;
/* 195:    */     
/* 196:    */     private Match(HandlerMethod mapping)
/* 197:    */     {
/* 198:312 */       this.mapping = mapping;
/* 199:313 */       this.handlerMethod = handlerMethod;
/* 200:    */     }
/* 201:    */     
/* 202:    */     public String toString()
/* 203:    */     {
/* 204:318 */       return this.mapping.toString();
/* 205:    */     }
/* 206:    */   }
/* 207:    */   
/* 208:    */   private class MatchComparator
/* 209:    */     implements Comparator<AbstractHandlerMethodMapping<T>.Match>
/* 210:    */   {
/* 211:    */     private final Comparator<T> comparator;
/* 212:    */     
/* 213:    */     public MatchComparator()
/* 214:    */     {
/* 215:327 */       this.comparator = comparator;
/* 216:    */     }
/* 217:    */     
/* 218:    */     public int compare(AbstractHandlerMethodMapping<T>.Match match1, AbstractHandlerMethodMapping<T>.Match match2)
/* 219:    */     {
/* 220:331 */       return this.comparator.compare(match1.mapping, match2.mapping);
/* 221:    */     }
/* 222:    */   }
/* 223:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.handler.AbstractHandlerMethodMapping
 * JD-Core Version:    0.7.0.1
 */