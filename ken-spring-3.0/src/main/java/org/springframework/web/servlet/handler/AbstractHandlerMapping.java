/*   1:    */ package org.springframework.web.servlet.handler;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.Collection;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Map;
/*   8:    */ import javax.servlet.http.HttpServletRequest;
/*   9:    */ import org.springframework.beans.BeansException;
/*  10:    */ import org.springframework.beans.factory.BeanFactoryUtils;
/*  11:    */ import org.springframework.context.ApplicationContext;
/*  12:    */ import org.springframework.core.Ordered;
/*  13:    */ import org.springframework.util.AntPathMatcher;
/*  14:    */ import org.springframework.util.Assert;
/*  15:    */ import org.springframework.util.PathMatcher;
/*  16:    */ import org.springframework.web.context.request.WebRequestInterceptor;
/*  17:    */ import org.springframework.web.context.support.WebApplicationObjectSupport;
/*  18:    */ import org.springframework.web.servlet.HandlerExecutionChain;
/*  19:    */ import org.springframework.web.servlet.HandlerInterceptor;
/*  20:    */ import org.springframework.web.servlet.HandlerMapping;
/*  21:    */ import org.springframework.web.util.UrlPathHelper;
/*  22:    */ 
/*  23:    */ public abstract class AbstractHandlerMapping
/*  24:    */   extends WebApplicationObjectSupport
/*  25:    */   implements HandlerMapping, Ordered
/*  26:    */ {
/*  27: 61 */   private int order = 2147483647;
/*  28:    */   private Object defaultHandler;
/*  29: 65 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/*  30: 67 */   private PathMatcher pathMatcher = new AntPathMatcher();
/*  31: 69 */   private final List<Object> interceptors = new ArrayList();
/*  32: 71 */   private final List<HandlerInterceptor> adaptedInterceptors = new ArrayList();
/*  33: 73 */   private final List<MappedInterceptor> mappedInterceptors = new ArrayList();
/*  34:    */   
/*  35:    */   public final void setOrder(int order)
/*  36:    */   {
/*  37: 81 */     this.order = order;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public final int getOrder()
/*  41:    */   {
/*  42: 85 */     return this.order;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setDefaultHandler(Object defaultHandler)
/*  46:    */   {
/*  47: 94 */     this.defaultHandler = defaultHandler;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public Object getDefaultHandler()
/*  51:    */   {
/*  52:102 */     return this.defaultHandler;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setAlwaysUseFullPath(boolean alwaysUseFullPath)
/*  56:    */   {
/*  57:113 */     this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setUrlDecode(boolean urlDecode)
/*  61:    */   {
/*  62:124 */     this.urlPathHelper.setUrlDecode(urlDecode);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void setUrlPathHelper(UrlPathHelper urlPathHelper)
/*  66:    */   {
/*  67:134 */     Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
/*  68:135 */     this.urlPathHelper = urlPathHelper;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public UrlPathHelper getUrlPathHelper()
/*  72:    */   {
/*  73:142 */     return this.urlPathHelper;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void setPathMatcher(PathMatcher pathMatcher)
/*  77:    */   {
/*  78:151 */     Assert.notNull(pathMatcher, "PathMatcher must not be null");
/*  79:152 */     this.pathMatcher = pathMatcher;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public PathMatcher getPathMatcher()
/*  83:    */   {
/*  84:160 */     return this.pathMatcher;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void setInterceptors(Object[] interceptors)
/*  88:    */   {
/*  89:174 */     this.interceptors.addAll((Collection)Arrays.asList(interceptors));
/*  90:    */   }
/*  91:    */   
/*  92:    */   protected void initApplicationContext()
/*  93:    */     throws BeansException
/*  94:    */   {
/*  95:185 */     extendInterceptors(this.interceptors);
/*  96:186 */     detectMappedInterceptors(this.mappedInterceptors);
/*  97:187 */     initInterceptors();
/*  98:    */   }
/*  99:    */   
/* 100:    */   protected void extendInterceptors(List<Object> interceptors) {}
/* 101:    */   
/* 102:    */   protected void detectMappedInterceptors(List<MappedInterceptor> mappedInterceptors)
/* 103:    */   {
/* 104:211 */     mappedInterceptors.addAll(
/* 105:212 */       BeanFactoryUtils.beansOfTypeIncludingAncestors(
/* 106:213 */       getApplicationContext(), MappedInterceptor.class, true, false).values());
/* 107:    */   }
/* 108:    */   
/* 109:    */   protected void initInterceptors()
/* 110:    */   {
/* 111:223 */     if (!this.interceptors.isEmpty()) {
/* 112:224 */       for (int i = 0; i < this.interceptors.size(); i++)
/* 113:    */       {
/* 114:225 */         Object interceptor = this.interceptors.get(i);
/* 115:226 */         if (interceptor == null) {
/* 116:227 */           throw new IllegalArgumentException("Entry number " + i + " in interceptors array is null");
/* 117:    */         }
/* 118:229 */         if ((interceptor instanceof MappedInterceptor)) {
/* 119:230 */           this.mappedInterceptors.add((MappedInterceptor)interceptor);
/* 120:    */         } else {
/* 121:233 */           this.adaptedInterceptors.add(adaptInterceptor(interceptor));
/* 122:    */         }
/* 123:    */       }
/* 124:    */     }
/* 125:    */   }
/* 126:    */   
/* 127:    */   protected HandlerInterceptor adaptInterceptor(Object interceptor)
/* 128:    */   {
/* 129:251 */     if ((interceptor instanceof HandlerInterceptor)) {
/* 130:252 */       return (HandlerInterceptor)interceptor;
/* 131:    */     }
/* 132:254 */     if ((interceptor instanceof WebRequestInterceptor)) {
/* 133:255 */       return new WebRequestHandlerInterceptorAdapter((WebRequestInterceptor)interceptor);
/* 134:    */     }
/* 135:258 */     throw new IllegalArgumentException("Interceptor type not supported: " + interceptor.getClass().getName());
/* 136:    */   }
/* 137:    */   
/* 138:    */   protected final HandlerInterceptor[] getAdaptedInterceptors()
/* 139:    */   {
/* 140:267 */     int count = this.adaptedInterceptors.size();
/* 141:268 */     return count > 0 ? (HandlerInterceptor[])this.adaptedInterceptors.toArray(new HandlerInterceptor[count]) : null;
/* 142:    */   }
/* 143:    */   
/* 144:    */   protected final MappedInterceptor[] getMappedInterceptors()
/* 145:    */   {
/* 146:276 */     int count = this.mappedInterceptors.size();
/* 147:277 */     return count > 0 ? (MappedInterceptor[])this.mappedInterceptors.toArray(new MappedInterceptor[count]) : null;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public final HandlerExecutionChain getHandler(HttpServletRequest request)
/* 151:    */     throws Exception
/* 152:    */   {
/* 153:288 */     Object handler = getHandlerInternal(request);
/* 154:289 */     if (handler == null) {
/* 155:290 */       handler = getDefaultHandler();
/* 156:    */     }
/* 157:292 */     if (handler == null) {
/* 158:293 */       return null;
/* 159:    */     }
/* 160:296 */     if ((handler instanceof String))
/* 161:    */     {
/* 162:297 */       String handlerName = (String)handler;
/* 163:298 */       handler = getApplicationContext().getBean(handlerName);
/* 164:    */     }
/* 165:300 */     return getHandlerExecutionChain(handler, request);
/* 166:    */   }
/* 167:    */   
/* 168:    */   protected abstract Object getHandlerInternal(HttpServletRequest paramHttpServletRequest)
/* 169:    */     throws Exception;
/* 170:    */   
/* 171:    */   protected HandlerExecutionChain getHandlerExecutionChain(Object handler, HttpServletRequest request)
/* 172:    */   {
/* 173:333 */     HandlerExecutionChain chain = 
/* 174:334 */       (handler instanceof HandlerExecutionChain) ? 
/* 175:335 */       (HandlerExecutionChain)handler : new HandlerExecutionChain(handler);
/* 176:    */     
/* 177:337 */     chain.addInterceptors(getAdaptedInterceptors());
/* 178:    */     
/* 179:339 */     String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
/* 180:340 */     for (MappedInterceptor mappedInterceptor : this.mappedInterceptors) {
/* 181:341 */       if (mappedInterceptor.matches(lookupPath, this.pathMatcher)) {
/* 182:342 */         chain.addInterceptor(mappedInterceptor.getInterceptor());
/* 183:    */       }
/* 184:    */     }
/* 185:346 */     return chain;
/* 186:    */   }
/* 187:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.handler.AbstractHandlerMapping
 * JD-Core Version:    0.7.0.1
 */