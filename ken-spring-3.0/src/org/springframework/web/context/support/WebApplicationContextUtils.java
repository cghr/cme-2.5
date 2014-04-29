/*   1:    */ package org.springframework.web.context.support;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.Enumeration;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.Map;
/*   8:    */ import javax.faces.context.ExternalContext;
/*   9:    */ import javax.faces.context.FacesContext;
/*  10:    */ import javax.servlet.ServletConfig;
/*  11:    */ import javax.servlet.ServletContext;
/*  12:    */ import javax.servlet.ServletRequest;
/*  13:    */ import javax.servlet.http.HttpServletRequest;
/*  14:    */ import javax.servlet.http.HttpSession;
/*  15:    */ import org.springframework.beans.factory.ObjectFactory;
/*  16:    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*  17:    */ import org.springframework.core.env.MutablePropertySources;
/*  18:    */ import org.springframework.util.Assert;
/*  19:    */ import org.springframework.util.ClassUtils;
/*  20:    */ import org.springframework.web.context.WebApplicationContext;
/*  21:    */ import org.springframework.web.context.request.RequestAttributes;
/*  22:    */ import org.springframework.web.context.request.RequestContextHolder;
/*  23:    */ import org.springframework.web.context.request.RequestScope;
/*  24:    */ import org.springframework.web.context.request.ServletRequestAttributes;
/*  25:    */ import org.springframework.web.context.request.ServletWebRequest;
/*  26:    */ import org.springframework.web.context.request.SessionScope;
/*  27:    */ import org.springframework.web.context.request.WebRequest;
/*  28:    */ 
/*  29:    */ public abstract class WebApplicationContextUtils
/*  30:    */ {
/*  31: 68 */   private static final boolean jsfPresent = ClassUtils.isPresent("javax.faces.context.FacesContext", RequestContextHolder.class.getClassLoader());
/*  32:    */   
/*  33:    */   public static WebApplicationContext getRequiredWebApplicationContext(ServletContext sc)
/*  34:    */     throws IllegalStateException
/*  35:    */   {
/*  36: 84 */     WebApplicationContext wac = getWebApplicationContext(sc);
/*  37: 85 */     if (wac == null) {
/*  38: 86 */       throw new IllegalStateException("No WebApplicationContext found: no ContextLoaderListener registered?");
/*  39:    */     }
/*  40: 88 */     return wac;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public static WebApplicationContext getWebApplicationContext(ServletContext sc)
/*  44:    */   {
/*  45:101 */     return getWebApplicationContext(sc, WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public static WebApplicationContext getWebApplicationContext(ServletContext sc, String attrName)
/*  49:    */   {
/*  50:111 */     Assert.notNull(sc, "ServletContext must not be null");
/*  51:112 */     Object attr = sc.getAttribute(attrName);
/*  52:113 */     if (attr == null) {
/*  53:114 */       return null;
/*  54:    */     }
/*  55:116 */     if ((attr instanceof RuntimeException)) {
/*  56:117 */       throw ((RuntimeException)attr);
/*  57:    */     }
/*  58:119 */     if ((attr instanceof Error)) {
/*  59:120 */       throw ((Error)attr);
/*  60:    */     }
/*  61:122 */     if ((attr instanceof Exception)) {
/*  62:123 */       throw new IllegalStateException((Exception)attr);
/*  63:    */     }
/*  64:125 */     if (!(attr instanceof WebApplicationContext)) {
/*  65:126 */       throw new IllegalStateException("Context attribute is not of type WebApplicationContext: " + attr);
/*  66:    */     }
/*  67:128 */     return (WebApplicationContext)attr;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public static void registerWebApplicationScopes(ConfigurableListableBeanFactory beanFactory)
/*  71:    */   {
/*  72:138 */     registerWebApplicationScopes(beanFactory, null);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public static void registerWebApplicationScopes(ConfigurableListableBeanFactory beanFactory, ServletContext sc)
/*  76:    */   {
/*  77:148 */     beanFactory.registerScope("request", new RequestScope());
/*  78:149 */     beanFactory.registerScope("session", new SessionScope(false));
/*  79:150 */     beanFactory.registerScope("globalSession", new SessionScope(true));
/*  80:151 */     if (sc != null)
/*  81:    */     {
/*  82:152 */       ServletContextScope appScope = new ServletContextScope(sc);
/*  83:153 */       beanFactory.registerScope("application", appScope);
/*  84:    */       
/*  85:155 */       sc.setAttribute(ServletContextScope.class.getName(), appScope);
/*  86:    */     }
/*  87:158 */     beanFactory.registerResolvableDependency(ServletRequest.class, new RequestObjectFactory(null));
/*  88:159 */     beanFactory.registerResolvableDependency(HttpSession.class, new SessionObjectFactory(null));
/*  89:160 */     beanFactory.registerResolvableDependency(WebRequest.class, new WebRequestObjectFactory(null));
/*  90:161 */     if (jsfPresent) {
/*  91:162 */       FacesDependencyRegistrar.registerFacesDependencies(beanFactory);
/*  92:    */     }
/*  93:    */   }
/*  94:    */   
/*  95:    */   public static void registerEnvironmentBeans(ConfigurableListableBeanFactory bf, ServletContext sc)
/*  96:    */   {
/*  97:173 */     registerEnvironmentBeans(bf, sc, null);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public static void registerEnvironmentBeans(ConfigurableListableBeanFactory bf, ServletContext sc, ServletConfig config)
/* 101:    */   {
/* 102:186 */     if ((sc != null) && (!bf.containsBean("servletContext"))) {
/* 103:187 */       bf.registerSingleton("servletContext", sc);
/* 104:    */     }
/* 105:190 */     if ((config != null) && (!bf.containsBean("servletConfig"))) {
/* 106:191 */       bf.registerSingleton("servletConfig", config);
/* 107:    */     }
/* 108:194 */     if (!bf.containsBean("contextParameters"))
/* 109:    */     {
/* 110:195 */       Map<String, String> parameterMap = new HashMap();
/* 111:196 */       if (sc != null)
/* 112:    */       {
/* 113:197 */         Enumeration<?> paramNameEnum = sc.getInitParameterNames();
/* 114:198 */         while (paramNameEnum.hasMoreElements())
/* 115:    */         {
/* 116:199 */           String paramName = (String)paramNameEnum.nextElement();
/* 117:200 */           parameterMap.put(paramName, sc.getInitParameter(paramName));
/* 118:    */         }
/* 119:    */       }
/* 120:203 */       if (config != null)
/* 121:    */       {
/* 122:204 */         Enumeration<?> paramNameEnum = config.getInitParameterNames();
/* 123:205 */         while (paramNameEnum.hasMoreElements())
/* 124:    */         {
/* 125:206 */           String paramName = (String)paramNameEnum.nextElement();
/* 126:207 */           parameterMap.put(paramName, config.getInitParameter(paramName));
/* 127:    */         }
/* 128:    */       }
/* 129:210 */       bf.registerSingleton("contextParameters", 
/* 130:211 */         Collections.unmodifiableMap(parameterMap));
/* 131:    */     }
/* 132:214 */     if (!bf.containsBean("contextAttributes"))
/* 133:    */     {
/* 134:215 */       Map<String, Object> attributeMap = new HashMap();
/* 135:216 */       if (sc != null)
/* 136:    */       {
/* 137:217 */         Enumeration<?> attrNameEnum = sc.getAttributeNames();
/* 138:218 */         while (attrNameEnum.hasMoreElements())
/* 139:    */         {
/* 140:219 */           String attrName = (String)attrNameEnum.nextElement();
/* 141:220 */           attributeMap.put(attrName, sc.getAttribute(attrName));
/* 142:    */         }
/* 143:    */       }
/* 144:223 */       bf.registerSingleton("contextAttributes", 
/* 145:224 */         Collections.unmodifiableMap(attributeMap));
/* 146:    */     }
/* 147:    */   }
/* 148:    */   
/* 149:    */   public static void initServletPropertySources(MutablePropertySources propertySources, ServletContext servletContext)
/* 150:    */   {
/* 151:237 */     initServletPropertySources(propertySources, servletContext, null);
/* 152:    */   }
/* 153:    */   
/* 154:    */   public static void initServletPropertySources(MutablePropertySources propertySources, ServletContext servletContext, ServletConfig servletConfig)
/* 155:    */   {
/* 156:249 */     Assert.notNull(propertySources, "propertySources must not be null");
/* 157:250 */     if ((servletContext != null) && (propertySources.contains("servletContextInitParams"))) {
/* 158:251 */       propertySources.replace("servletContextInitParams", 
/* 159:252 */         new ServletContextPropertySource("servletContextInitParams", servletContext));
/* 160:    */     }
/* 161:254 */     if ((servletConfig != null) && (propertySources.contains("servletConfigInitParams"))) {
/* 162:255 */       propertySources.replace("servletConfigInitParams", 
/* 163:256 */         new ServletConfigPropertySource("servletConfigInitParams", servletConfig));
/* 164:    */     }
/* 165:    */   }
/* 166:    */   
/* 167:    */   private static ServletRequestAttributes currentRequestAttributes()
/* 168:    */   {
/* 169:265 */     RequestAttributes requestAttr = RequestContextHolder.currentRequestAttributes();
/* 170:266 */     if (!(requestAttr instanceof ServletRequestAttributes)) {
/* 171:267 */       throw new IllegalStateException("Current request is not a servlet request");
/* 172:    */     }
/* 173:269 */     return (ServletRequestAttributes)requestAttr;
/* 174:    */   }
/* 175:    */   
/* 176:    */   private static class RequestObjectFactory
/* 177:    */     implements ObjectFactory<ServletRequest>, Serializable
/* 178:    */   {
/* 179:    */     public ServletRequest getObject()
/* 180:    */     {
/* 181:280 */       return WebApplicationContextUtils.access$0().getRequest();
/* 182:    */     }
/* 183:    */     
/* 184:    */     public String toString()
/* 185:    */     {
/* 186:285 */       return "Current HttpServletRequest";
/* 187:    */     }
/* 188:    */   }
/* 189:    */   
/* 190:    */   private static class SessionObjectFactory
/* 191:    */     implements ObjectFactory<HttpSession>, Serializable
/* 192:    */   {
/* 193:    */     public HttpSession getObject()
/* 194:    */     {
/* 195:297 */       return WebApplicationContextUtils.access$0().getRequest().getSession();
/* 196:    */     }
/* 197:    */     
/* 198:    */     public String toString()
/* 199:    */     {
/* 200:302 */       return "Current HttpSession";
/* 201:    */     }
/* 202:    */   }
/* 203:    */   
/* 204:    */   private static class WebRequestObjectFactory
/* 205:    */     implements ObjectFactory<WebRequest>, Serializable
/* 206:    */   {
/* 207:    */     public WebRequest getObject()
/* 208:    */     {
/* 209:314 */       return new ServletWebRequest(WebApplicationContextUtils.access$0().getRequest());
/* 210:    */     }
/* 211:    */     
/* 212:    */     public String toString()
/* 213:    */     {
/* 214:319 */       return "Current ServletWebRequest";
/* 215:    */     }
/* 216:    */   }
/* 217:    */   
/* 218:    */   private static class FacesDependencyRegistrar
/* 219:    */   {
/* 220:    */     public static void registerFacesDependencies(ConfigurableListableBeanFactory beanFactory)
/* 221:    */     {
/* 222:330 */       beanFactory.registerResolvableDependency(FacesContext.class, new ObjectFactory()
/* 223:    */       {
/* 224:    */         public FacesContext getObject()
/* 225:    */         {
/* 226:332 */           return FacesContext.getCurrentInstance();
/* 227:    */         }
/* 228:    */         
/* 229:    */         public String toString()
/* 230:    */         {
/* 231:336 */           return "Current JSF FacesContext";
/* 232:    */         }
/* 233:338 */       });
/* 234:339 */       beanFactory.registerResolvableDependency(ExternalContext.class, new ObjectFactory()
/* 235:    */       {
/* 236:    */         public ExternalContext getObject()
/* 237:    */         {
/* 238:341 */           return FacesContext.getCurrentInstance().getExternalContext();
/* 239:    */         }
/* 240:    */         
/* 241:    */         public String toString()
/* 242:    */         {
/* 243:345 */           return "Current JSF ExternalContext";
/* 244:    */         }
/* 245:    */       });
/* 246:    */     }
/* 247:    */   }
/* 248:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.support.WebApplicationContextUtils
 * JD-Core Version:    0.7.0.1
 */