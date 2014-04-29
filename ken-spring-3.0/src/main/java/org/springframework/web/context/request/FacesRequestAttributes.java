/*   1:    */ package org.springframework.web.context.request;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.Map;
/*   6:    */ import javax.faces.application.Application;
/*   7:    */ import javax.faces.component.UIViewRoot;
/*   8:    */ import javax.faces.context.ExternalContext;
/*   9:    */ import javax.faces.context.FacesContext;
/*  10:    */ import javax.portlet.PortletSession;
/*  11:    */ import org.apache.commons.logging.Log;
/*  12:    */ import org.apache.commons.logging.LogFactory;
/*  13:    */ import org.springframework.util.Assert;
/*  14:    */ import org.springframework.util.ClassUtils;
/*  15:    */ import org.springframework.util.ReflectionUtils;
/*  16:    */ import org.springframework.util.StringUtils;
/*  17:    */ import org.springframework.web.util.WebUtils;
/*  18:    */ 
/*  19:    */ public class FacesRequestAttributes
/*  20:    */   implements RequestAttributes
/*  21:    */ {
/*  22: 56 */   private static final boolean portletApiPresent = ClassUtils.isPresent("javax.portlet.PortletSession", FacesRequestAttributes.class.getClassLoader());
/*  23: 61 */   private static final Log logger = LogFactory.getLog(FacesRequestAttributes.class);
/*  24:    */   private final FacesContext facesContext;
/*  25:    */   
/*  26:    */   public FacesRequestAttributes(FacesContext facesContext)
/*  27:    */   {
/*  28: 72 */     Assert.notNull(facesContext, "FacesContext must not be null");
/*  29: 73 */     this.facesContext = facesContext;
/*  30:    */   }
/*  31:    */   
/*  32:    */   protected final FacesContext getFacesContext()
/*  33:    */   {
/*  34: 81 */     return this.facesContext;
/*  35:    */   }
/*  36:    */   
/*  37:    */   protected final ExternalContext getExternalContext()
/*  38:    */   {
/*  39: 89 */     return getFacesContext().getExternalContext();
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected Map<String, Object> getAttributeMap(int scope)
/*  43:    */   {
/*  44:100 */     if (scope == 0) {
/*  45:101 */       return getExternalContext().getRequestMap();
/*  46:    */     }
/*  47:104 */     return getExternalContext().getSessionMap();
/*  48:    */   }
/*  49:    */   
/*  50:    */   public Object getAttribute(String name, int scope)
/*  51:    */   {
/*  52:110 */     if ((scope == 2) && (portletApiPresent)) {
/*  53:111 */       return PortletSessionAccessor.getAttribute(name, getExternalContext());
/*  54:    */     }
/*  55:114 */     return getAttributeMap(scope).get(name);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setAttribute(String name, Object value, int scope)
/*  59:    */   {
/*  60:119 */     if ((scope == 2) && (portletApiPresent)) {
/*  61:120 */       PortletSessionAccessor.setAttribute(name, value, getExternalContext());
/*  62:    */     } else {
/*  63:123 */       getAttributeMap(scope).put(name, value);
/*  64:    */     }
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void removeAttribute(String name, int scope)
/*  68:    */   {
/*  69:128 */     if ((scope == 2) && (portletApiPresent)) {
/*  70:129 */       PortletSessionAccessor.removeAttribute(name, getExternalContext());
/*  71:    */     } else {
/*  72:132 */       getAttributeMap(scope).remove(name);
/*  73:    */     }
/*  74:    */   }
/*  75:    */   
/*  76:    */   public String[] getAttributeNames(int scope)
/*  77:    */   {
/*  78:137 */     if ((scope == 2) && (portletApiPresent)) {
/*  79:138 */       return PortletSessionAccessor.getAttributeNames(getExternalContext());
/*  80:    */     }
/*  81:141 */     return StringUtils.toStringArray((Collection)getAttributeMap(scope).keySet());
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void registerDestructionCallback(String name, Runnable callback, int scope)
/*  85:    */   {
/*  86:146 */     if (logger.isWarnEnabled()) {
/*  87:147 */       logger.warn("Could not register destruction callback [" + callback + "] for attribute '" + name + 
/*  88:148 */         "' because FacesRequestAttributes does not support such callbacks");
/*  89:    */     }
/*  90:    */   }
/*  91:    */   
/*  92:    */   public Object resolveReference(String key)
/*  93:    */   {
/*  94:153 */     if ("request".equals(key)) {
/*  95:154 */       return getExternalContext().getRequest();
/*  96:    */     }
/*  97:156 */     if ("session".equals(key)) {
/*  98:157 */       return getExternalContext().getSession(true);
/*  99:    */     }
/* 100:159 */     if ("application".equals(key)) {
/* 101:160 */       return getExternalContext().getContext();
/* 102:    */     }
/* 103:162 */     if ("requestScope".equals(key)) {
/* 104:163 */       return getExternalContext().getRequestMap();
/* 105:    */     }
/* 106:165 */     if ("sessionScope".equals(key)) {
/* 107:166 */       return getExternalContext().getSessionMap();
/* 108:    */     }
/* 109:168 */     if ("applicationScope".equals(key)) {
/* 110:169 */       return getExternalContext().getApplicationMap();
/* 111:    */     }
/* 112:171 */     if ("facesContext".equals(key)) {
/* 113:172 */       return getFacesContext();
/* 114:    */     }
/* 115:174 */     if ("cookie".equals(key)) {
/* 116:175 */       return getExternalContext().getRequestCookieMap();
/* 117:    */     }
/* 118:177 */     if ("header".equals(key)) {
/* 119:178 */       return getExternalContext().getRequestHeaderMap();
/* 120:    */     }
/* 121:180 */     if ("headerValues".equals(key)) {
/* 122:181 */       return getExternalContext().getRequestHeaderValuesMap();
/* 123:    */     }
/* 124:183 */     if ("param".equals(key)) {
/* 125:184 */       return getExternalContext().getRequestParameterMap();
/* 126:    */     }
/* 127:186 */     if ("paramValues".equals(key)) {
/* 128:187 */       return getExternalContext().getRequestParameterValuesMap();
/* 129:    */     }
/* 130:189 */     if ("initParam".equals(key)) {
/* 131:190 */       return getExternalContext().getInitParameterMap();
/* 132:    */     }
/* 133:192 */     if ("view".equals(key)) {
/* 134:193 */       return getFacesContext().getViewRoot();
/* 135:    */     }
/* 136:195 */     if ("viewScope".equals(key)) {
/* 137:    */       try
/* 138:    */       {
/* 139:197 */         return ReflectionUtils.invokeMethod(UIViewRoot.class.getMethod("getViewMap", new Class[0]), getFacesContext().getViewRoot());
/* 140:    */       }
/* 141:    */       catch (NoSuchMethodException ex)
/* 142:    */       {
/* 143:200 */         throw new IllegalStateException("JSF 2.0 API not available", ex);
/* 144:    */       }
/* 145:    */     }
/* 146:203 */     if ("flash".equals(key)) {
/* 147:    */       try
/* 148:    */       {
/* 149:205 */         return ReflectionUtils.invokeMethod(ExternalContext.class.getMethod("getFlash", new Class[0]), getExternalContext());
/* 150:    */       }
/* 151:    */       catch (NoSuchMethodException ex)
/* 152:    */       {
/* 153:208 */         throw new IllegalStateException("JSF 2.0 API not available", ex);
/* 154:    */       }
/* 155:    */     }
/* 156:211 */     if ("resource".equals(key)) {
/* 157:    */       try
/* 158:    */       {
/* 159:213 */         return ReflectionUtils.invokeMethod(Application.class.getMethod("getResourceHandler", new Class[0]), getFacesContext().getApplication());
/* 160:    */       }
/* 161:    */       catch (NoSuchMethodException ex)
/* 162:    */       {
/* 163:216 */         throw new IllegalStateException("JSF 2.0 API not available", ex);
/* 164:    */       }
/* 165:    */     }
/* 166:220 */     return null;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public String getSessionId()
/* 170:    */   {
/* 171:225 */     Object session = getExternalContext().getSession(true);
/* 172:    */     try
/* 173:    */     {
/* 174:228 */       Method getIdMethod = session.getClass().getMethod("getId", new Class[0]);
/* 175:229 */       return ReflectionUtils.invokeMethod(getIdMethod, session).toString();
/* 176:    */     }
/* 177:    */     catch (NoSuchMethodException localNoSuchMethodException)
/* 178:    */     {
/* 179:232 */       throw new IllegalStateException("Session object [" + session + "] does not have a getId() method");
/* 180:    */     }
/* 181:    */   }
/* 182:    */   
/* 183:    */   public Object getSessionMutex()
/* 184:    */   {
/* 185:239 */     Object session = getExternalContext().getSession(true);
/* 186:240 */     Object mutex = getExternalContext().getSessionMap().get(WebUtils.SESSION_MUTEX_ATTRIBUTE);
/* 187:241 */     if (mutex == null) {
/* 188:242 */       mutex = session;
/* 189:    */     }
/* 190:244 */     return mutex;
/* 191:    */   }
/* 192:    */   
/* 193:    */   private static class PortletSessionAccessor
/* 194:    */   {
/* 195:    */     public static Object getAttribute(String name, ExternalContext externalContext)
/* 196:    */     {
/* 197:254 */       Object session = externalContext.getSession(false);
/* 198:255 */       if ((session instanceof PortletSession)) {
/* 199:256 */         return ((PortletSession)session).getAttribute(name, 1);
/* 200:    */       }
/* 201:258 */       if (session != null) {
/* 202:259 */         return externalContext.getSessionMap().get(name);
/* 203:    */       }
/* 204:262 */       return null;
/* 205:    */     }
/* 206:    */     
/* 207:    */     public static void setAttribute(String name, Object value, ExternalContext externalContext)
/* 208:    */     {
/* 209:267 */       Object session = externalContext.getSession(true);
/* 210:268 */       if ((session instanceof PortletSession)) {
/* 211:269 */         ((PortletSession)session).setAttribute(name, value, 1);
/* 212:    */       } else {
/* 213:272 */         externalContext.getSessionMap().put(name, value);
/* 214:    */       }
/* 215:    */     }
/* 216:    */     
/* 217:    */     public static void removeAttribute(String name, ExternalContext externalContext)
/* 218:    */     {
/* 219:277 */       Object session = externalContext.getSession(false);
/* 220:278 */       if ((session instanceof PortletSession)) {
/* 221:279 */         ((PortletSession)session).removeAttribute(name, 1);
/* 222:281 */       } else if (session != null) {
/* 223:282 */         externalContext.getSessionMap().remove(name);
/* 224:    */       }
/* 225:    */     }
/* 226:    */     
/* 227:    */     public static String[] getAttributeNames(ExternalContext externalContext)
/* 228:    */     {
/* 229:287 */       Object session = externalContext.getSession(false);
/* 230:288 */       if ((session instanceof PortletSession)) {
/* 231:289 */         return StringUtils.toStringArray(
/* 232:290 */           ((PortletSession)session).getAttributeNames(1));
/* 233:    */       }
/* 234:292 */       if (session != null) {
/* 235:293 */         return StringUtils.toStringArray((Collection)externalContext.getSessionMap().keySet());
/* 236:    */       }
/* 237:296 */       return new String[0];
/* 238:    */     }
/* 239:    */   }
/* 240:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.request.FacesRequestAttributes
 * JD-Core Version:    0.7.0.1
 */