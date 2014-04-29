/*   1:    */ package org.springframework.web.servlet.mvc.annotation;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.Collection;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.LinkedHashSet;
/*   8:    */ import java.util.Map;
/*   9:    */ import java.util.Set;
/*  10:    */ import javax.servlet.http.HttpServletRequest;
/*  11:    */ import org.springframework.context.ApplicationContext;
/*  12:    */ import org.springframework.core.annotation.AnnotationUtils;
/*  13:    */ import org.springframework.util.PathMatcher;
/*  14:    */ import org.springframework.util.ReflectionUtils;
/*  15:    */ import org.springframework.util.ReflectionUtils.MethodCallback;
/*  16:    */ import org.springframework.util.StringUtils;
/*  17:    */ import org.springframework.web.HttpRequestMethodNotSupportedException;
/*  18:    */ import org.springframework.web.bind.ServletRequestBindingException;
/*  19:    */ import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
/*  20:    */ import org.springframework.web.bind.annotation.RequestMapping;
/*  21:    */ import org.springframework.web.bind.annotation.RequestMethod;
/*  22:    */ import org.springframework.web.servlet.handler.AbstractDetectingUrlHandlerMapping;
/*  23:    */ 
/*  24:    */ public class DefaultAnnotationHandlerMapping
/*  25:    */   extends AbstractDetectingUrlHandlerMapping
/*  26:    */ {
/*  27: 84 */   private boolean useDefaultSuffixPattern = true;
/*  28: 86 */   private final Map<Class, RequestMapping> cachedMappings = new HashMap();
/*  29:    */   
/*  30:    */   public void setUseDefaultSuffixPattern(boolean useDefaultSuffixPattern)
/*  31:    */   {
/*  32: 98 */     this.useDefaultSuffixPattern = useDefaultSuffixPattern;
/*  33:    */   }
/*  34:    */   
/*  35:    */   protected String[] determineUrlsForHandler(String beanName)
/*  36:    */   {
/*  37:108 */     ApplicationContext context = getApplicationContext();
/*  38:109 */     Class<?> handlerType = context.getType(beanName);
/*  39:110 */     RequestMapping mapping = (RequestMapping)context.findAnnotationOnBean(beanName, RequestMapping.class);
/*  40:111 */     if (mapping != null)
/*  41:    */     {
/*  42:113 */       this.cachedMappings.put(handlerType, mapping);
/*  43:114 */       Set<String> urls = new LinkedHashSet();
/*  44:115 */       String[] typeLevelPatterns = mapping.value();
/*  45:116 */       if (typeLevelPatterns.length > 0)
/*  46:    */       {
/*  47:118 */         String[] methodLevelPatterns = determineUrlsForHandlerMethods(handlerType, true);
/*  48:119 */         for (String typeLevelPattern : typeLevelPatterns)
/*  49:    */         {
/*  50:120 */           if (!typeLevelPattern.startsWith("/")) {
/*  51:121 */             typeLevelPattern = "/" + typeLevelPattern;
/*  52:    */           }
/*  53:123 */           boolean hasEmptyMethodLevelMappings = false;
/*  54:124 */           for (String methodLevelPattern : methodLevelPatterns) {
/*  55:125 */             if (methodLevelPattern == null)
/*  56:    */             {
/*  57:126 */               hasEmptyMethodLevelMappings = true;
/*  58:    */             }
/*  59:    */             else
/*  60:    */             {
/*  61:129 */               String combinedPattern = getPathMatcher().combine(typeLevelPattern, methodLevelPattern);
/*  62:130 */               addUrlsForPath(urls, combinedPattern);
/*  63:    */             }
/*  64:    */           }
/*  65:133 */           if ((hasEmptyMethodLevelMappings) || 
/*  66:134 */             (org.springframework.web.servlet.mvc.Controller.class.isAssignableFrom(handlerType))) {
/*  67:135 */             addUrlsForPath(urls, typeLevelPattern);
/*  68:    */           }
/*  69:    */         }
/*  70:138 */         return StringUtils.toStringArray(urls);
/*  71:    */       }
/*  72:142 */       return determineUrlsForHandlerMethods(handlerType, false);
/*  73:    */     }
/*  74:145 */     if (AnnotationUtils.findAnnotation(handlerType, org.springframework.stereotype.Controller.class) != null) {
/*  75:147 */       return determineUrlsForHandlerMethods(handlerType, false);
/*  76:    */     }
/*  77:150 */     return null;
/*  78:    */   }
/*  79:    */   
/*  80:    */   protected String[] determineUrlsForHandlerMethods(Class<?> handlerType, final boolean hasTypeLevelMapping)
/*  81:    */   {
/*  82:162 */     String[] subclassResult = determineUrlsForHandlerMethods(handlerType);
/*  83:163 */     if (subclassResult != null) {
/*  84:164 */       return subclassResult;
/*  85:    */     }
/*  86:167 */     final Set<String> urls = new LinkedHashSet();
/*  87:168 */     Set<Class<?>> handlerTypes = new LinkedHashSet();
/*  88:169 */     handlerTypes.add(handlerType);
/*  89:170 */     handlerTypes.addAll((Collection)Arrays.asList(handlerType.getInterfaces()));
/*  90:171 */     for (Class<?> currentHandlerType : handlerTypes) {
/*  91:172 */       ReflectionUtils.doWithMethods(currentHandlerType, new ReflectionUtils.MethodCallback()
/*  92:    */       {
/*  93:    */         public void doWith(Method method)
/*  94:    */         {
/*  95:174 */           RequestMapping mapping = (RequestMapping)AnnotationUtils.findAnnotation(method, RequestMapping.class);
/*  96:175 */           if (mapping != null)
/*  97:    */           {
/*  98:176 */             String[] mappedPatterns = mapping.value();
/*  99:177 */             if (mappedPatterns.length > 0) {
/* 100:178 */               for (String mappedPattern : mappedPatterns)
/* 101:    */               {
/* 102:179 */                 if ((!hasTypeLevelMapping) && (!mappedPattern.startsWith("/"))) {
/* 103:180 */                   mappedPattern = "/" + mappedPattern;
/* 104:    */                 }
/* 105:182 */                 DefaultAnnotationHandlerMapping.this.addUrlsForPath(urls, mappedPattern);
/* 106:    */               }
/* 107:185 */             } else if (hasTypeLevelMapping) {
/* 108:187 */               urls.add(null);
/* 109:    */             }
/* 110:    */           }
/* 111:    */         }
/* 112:191 */       }, ReflectionUtils.USER_DECLARED_METHODS);
/* 113:    */     }
/* 114:193 */     return StringUtils.toStringArray(urls);
/* 115:    */   }
/* 116:    */   
/* 117:    */   protected String[] determineUrlsForHandlerMethods(Class<?> handlerType)
/* 118:    */   {
/* 119:202 */     return null;
/* 120:    */   }
/* 121:    */   
/* 122:    */   protected void addUrlsForPath(Set<String> urls, String path)
/* 123:    */   {
/* 124:211 */     urls.add(path);
/* 125:212 */     if ((this.useDefaultSuffixPattern) && (path.indexOf('.') == -1) && (!path.endsWith("/")))
/* 126:    */     {
/* 127:213 */       urls.add(path + ".*");
/* 128:214 */       urls.add(path + "/");
/* 129:    */     }
/* 130:    */   }
/* 131:    */   
/* 132:    */   protected void validateHandler(Object handler, HttpServletRequest request)
/* 133:    */     throws Exception
/* 134:    */   {
/* 135:225 */     RequestMapping mapping = (RequestMapping)this.cachedMappings.get(handler.getClass());
/* 136:226 */     if (mapping == null) {
/* 137:227 */       mapping = (RequestMapping)AnnotationUtils.findAnnotation(handler.getClass(), RequestMapping.class);
/* 138:    */     }
/* 139:229 */     if (mapping != null) {
/* 140:230 */       validateMapping(mapping, request);
/* 141:    */     }
/* 142:    */   }
/* 143:    */   
/* 144:    */   protected void validateMapping(RequestMapping mapping, HttpServletRequest request)
/* 145:    */     throws Exception
/* 146:    */   {
/* 147:242 */     RequestMethod[] mappedMethods = mapping.method();
/* 148:243 */     if (!ServletAnnotationMappingUtils.checkRequestMethod(mappedMethods, request))
/* 149:    */     {
/* 150:244 */       String[] supportedMethods = new String[mappedMethods.length];
/* 151:245 */       for (int i = 0; i < mappedMethods.length; i++) {
/* 152:246 */         supportedMethods[i] = mappedMethods[i].name();
/* 153:    */       }
/* 154:248 */       throw new HttpRequestMethodNotSupportedException(request.getMethod(), supportedMethods);
/* 155:    */     }
/* 156:251 */     String[] mappedParams = mapping.params();
/* 157:252 */     if (!ServletAnnotationMappingUtils.checkParameters(mappedParams, request)) {
/* 158:253 */       throw new UnsatisfiedServletRequestParameterException(mappedParams, request.getParameterMap());
/* 159:    */     }
/* 160:256 */     String[] mappedHeaders = mapping.headers();
/* 161:257 */     if (!ServletAnnotationMappingUtils.checkHeaders(mappedHeaders, request)) {
/* 162:258 */       throw new ServletRequestBindingException("Header conditions \"" + 
/* 163:259 */         StringUtils.arrayToDelimitedString(mappedHeaders, ", ") + 
/* 164:260 */         "\" not met for actual request");
/* 165:    */     }
/* 166:    */   }
/* 167:    */   
/* 168:    */   protected boolean supportsTypeLevelMappings()
/* 169:    */   {
/* 170:266 */     return true;
/* 171:    */   }
/* 172:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping
 * JD-Core Version:    0.7.0.1
 */