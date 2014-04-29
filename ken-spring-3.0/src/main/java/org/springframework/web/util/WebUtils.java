/*   1:    */ package org.springframework.web.util;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileNotFoundException;
/*   5:    */ import java.util.Enumeration;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.Map.Entry;
/*   8:    */ import java.util.Properties;
/*   9:    */ import java.util.TreeMap;
/*  10:    */ import javax.servlet.ServletContext;
/*  11:    */ import javax.servlet.ServletRequest;
/*  12:    */ import javax.servlet.ServletRequestWrapper;
/*  13:    */ import javax.servlet.ServletResponse;
/*  14:    */ import javax.servlet.ServletResponseWrapper;
/*  15:    */ import javax.servlet.http.Cookie;
/*  16:    */ import javax.servlet.http.HttpServletRequest;
/*  17:    */ import javax.servlet.http.HttpSession;
/*  18:    */ import org.springframework.util.Assert;
/*  19:    */ import org.springframework.util.StringUtils;
/*  20:    */ 
/*  21:    */ public abstract class WebUtils
/*  22:    */ {
/*  23:    */   public static final String INCLUDE_REQUEST_URI_ATTRIBUTE = "javax.servlet.include.request_uri";
/*  24:    */   public static final String INCLUDE_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.include.context_path";
/*  25:    */   public static final String INCLUDE_SERVLET_PATH_ATTRIBUTE = "javax.servlet.include.servlet_path";
/*  26:    */   public static final String INCLUDE_PATH_INFO_ATTRIBUTE = "javax.servlet.include.path_info";
/*  27:    */   public static final String INCLUDE_QUERY_STRING_ATTRIBUTE = "javax.servlet.include.query_string";
/*  28:    */   public static final String FORWARD_REQUEST_URI_ATTRIBUTE = "javax.servlet.forward.request_uri";
/*  29:    */   public static final String FORWARD_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.forward.context_path";
/*  30:    */   public static final String FORWARD_SERVLET_PATH_ATTRIBUTE = "javax.servlet.forward.servlet_path";
/*  31:    */   public static final String FORWARD_PATH_INFO_ATTRIBUTE = "javax.servlet.forward.path_info";
/*  32:    */   public static final String FORWARD_QUERY_STRING_ATTRIBUTE = "javax.servlet.forward.query_string";
/*  33:    */   public static final String ERROR_STATUS_CODE_ATTRIBUTE = "javax.servlet.error.status_code";
/*  34:    */   public static final String ERROR_EXCEPTION_TYPE_ATTRIBUTE = "javax.servlet.error.exception_type";
/*  35:    */   public static final String ERROR_MESSAGE_ATTRIBUTE = "javax.servlet.error.message";
/*  36:    */   public static final String ERROR_EXCEPTION_ATTRIBUTE = "javax.servlet.error.exception";
/*  37:    */   public static final String ERROR_REQUEST_URI_ATTRIBUTE = "javax.servlet.error.request_uri";
/*  38:    */   public static final String ERROR_SERVLET_NAME_ATTRIBUTE = "javax.servlet.error.servlet_name";
/*  39:    */   public static final String CONTENT_TYPE_CHARSET_PREFIX = ";charset=";
/*  40:    */   public static final String DEFAULT_CHARACTER_ENCODING = "ISO-8859-1";
/*  41:    */   public static final String TEMP_DIR_CONTEXT_ATTRIBUTE = "javax.servlet.context.tempdir";
/*  42:    */   public static final String HTML_ESCAPE_CONTEXT_PARAM = "defaultHtmlEscape";
/*  43:    */   public static final String WEB_APP_ROOT_KEY_PARAM = "webAppRootKey";
/*  44:    */   public static final String DEFAULT_WEB_APP_ROOT_KEY = "webapp.root";
/*  45:117 */   public static final String[] SUBMIT_IMAGE_SUFFIXES = { ".x", ".y" };
/*  46:120 */   public static final String SESSION_MUTEX_ATTRIBUTE = WebUtils.class.getName() + ".MUTEX";
/*  47:    */   
/*  48:    */   public static void setWebAppRootSystemProperty(ServletContext servletContext)
/*  49:    */     throws IllegalStateException
/*  50:    */   {
/*  51:138 */     Assert.notNull(servletContext, "ServletContext must not be null");
/*  52:139 */     String root = servletContext.getRealPath("/");
/*  53:140 */     if (root == null) {
/*  54:141 */       throw new IllegalStateException(
/*  55:142 */         "Cannot set web app root system property when WAR file is not expanded");
/*  56:    */     }
/*  57:144 */     String param = servletContext.getInitParameter("webAppRootKey");
/*  58:145 */     String key = param != null ? param : "webapp.root";
/*  59:146 */     String oldValue = System.getProperty(key);
/*  60:147 */     if ((oldValue != null) && (!StringUtils.pathEquals(oldValue, root))) {
/*  61:148 */       throw new IllegalStateException(
/*  62:149 */         "Web app root system property already set to different value: '" + 
/*  63:150 */         key + "' = [" + oldValue + "] instead of [" + root + "] - " + 
/*  64:151 */         "Choose unique values for the 'webAppRootKey' context-param in your web.xml files!");
/*  65:    */     }
/*  66:153 */     System.setProperty(key, root);
/*  67:154 */     servletContext.log("Set web app root system property: '" + key + "' = [" + root + "]");
/*  68:    */   }
/*  69:    */   
/*  70:    */   public static void removeWebAppRootSystemProperty(ServletContext servletContext)
/*  71:    */   {
/*  72:164 */     Assert.notNull(servletContext, "ServletContext must not be null");
/*  73:165 */     String param = servletContext.getInitParameter("webAppRootKey");
/*  74:166 */     String key = param != null ? param : "webapp.root";
/*  75:167 */     System.getProperties().remove(key);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public static boolean isDefaultHtmlEscape(ServletContext servletContext)
/*  79:    */   {
/*  80:178 */     if (servletContext == null) {
/*  81:179 */       return false;
/*  82:    */     }
/*  83:181 */     String param = servletContext.getInitParameter("defaultHtmlEscape");
/*  84:182 */     return Boolean.valueOf(param).booleanValue();
/*  85:    */   }
/*  86:    */   
/*  87:    */   public static Boolean getDefaultHtmlEscape(ServletContext servletContext)
/*  88:    */   {
/*  89:196 */     if (servletContext == null) {
/*  90:197 */       return null;
/*  91:    */     }
/*  92:199 */     Assert.notNull(servletContext, "ServletContext must not be null");
/*  93:200 */     String param = servletContext.getInitParameter("defaultHtmlEscape");
/*  94:201 */     return StringUtils.hasText(param) ? Boolean.valueOf(param) : null;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public static File getTempDir(ServletContext servletContext)
/*  98:    */   {
/*  99:211 */     Assert.notNull(servletContext, "ServletContext must not be null");
/* 100:212 */     return (File)servletContext.getAttribute("javax.servlet.context.tempdir");
/* 101:    */   }
/* 102:    */   
/* 103:    */   public static String getRealPath(ServletContext servletContext, String path)
/* 104:    */     throws FileNotFoundException
/* 105:    */   {
/* 106:229 */     Assert.notNull(servletContext, "ServletContext must not be null");
/* 107:231 */     if (!path.startsWith("/")) {
/* 108:232 */       path = "/" + path;
/* 109:    */     }
/* 110:234 */     String realPath = servletContext.getRealPath(path);
/* 111:235 */     if (realPath == null) {
/* 112:236 */       throw new FileNotFoundException(
/* 113:237 */         "ServletContext resource [" + path + "] cannot be resolved to absolute file path - " + 
/* 114:238 */         "web application archive not expanded?");
/* 115:    */     }
/* 116:240 */     return realPath;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public static String getSessionId(HttpServletRequest request)
/* 120:    */   {
/* 121:250 */     Assert.notNull(request, "Request must not be null");
/* 122:251 */     HttpSession session = request.getSession(false);
/* 123:252 */     return session != null ? session.getId() : null;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public static Object getSessionAttribute(HttpServletRequest request, String name)
/* 127:    */   {
/* 128:264 */     Assert.notNull(request, "Request must not be null");
/* 129:265 */     HttpSession session = request.getSession(false);
/* 130:266 */     return session != null ? session.getAttribute(name) : null;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public static Object getRequiredSessionAttribute(HttpServletRequest request, String name)
/* 134:    */     throws IllegalStateException
/* 135:    */   {
/* 136:281 */     Object attr = getSessionAttribute(request, name);
/* 137:282 */     if (attr == null) {
/* 138:283 */       throw new IllegalStateException("No session attribute '" + name + "' found");
/* 139:    */     }
/* 140:285 */     return attr;
/* 141:    */   }
/* 142:    */   
/* 143:    */   public static void setSessionAttribute(HttpServletRequest request, String name, Object value)
/* 144:    */   {
/* 145:297 */     Assert.notNull(request, "Request must not be null");
/* 146:298 */     if (value != null)
/* 147:    */     {
/* 148:299 */       request.getSession().setAttribute(name, value);
/* 149:    */     }
/* 150:    */     else
/* 151:    */     {
/* 152:302 */       HttpSession session = request.getSession(false);
/* 153:303 */       if (session != null) {
/* 154:304 */         session.removeAttribute(name);
/* 155:    */       }
/* 156:    */     }
/* 157:    */   }
/* 158:    */   
/* 159:    */   public static Object getOrCreateSessionAttribute(HttpSession session, String name, Class clazz)
/* 160:    */     throws IllegalArgumentException
/* 161:    */   {
/* 162:322 */     Assert.notNull(session, "Session must not be null");
/* 163:323 */     Object sessionObject = session.getAttribute(name);
/* 164:324 */     if (sessionObject == null)
/* 165:    */     {
/* 166:    */       try
/* 167:    */       {
/* 168:326 */         sessionObject = clazz.newInstance();
/* 169:    */       }
/* 170:    */       catch (InstantiationException ex)
/* 171:    */       {
/* 172:329 */         throw new IllegalArgumentException(
/* 173:330 */           "Could not instantiate class [" + clazz.getName() + 
/* 174:331 */           "] for session attribute '" + name + "': " + ex.getMessage());
/* 175:    */       }
/* 176:    */       catch (IllegalAccessException ex)
/* 177:    */       {
/* 178:334 */         throw new IllegalArgumentException(
/* 179:335 */           "Could not access default constructor of class [" + clazz.getName() + 
/* 180:336 */           "] for session attribute '" + name + "': " + ex.getMessage());
/* 181:    */       }
/* 182:338 */       session.setAttribute(name, sessionObject);
/* 183:    */     }
/* 184:340 */     return sessionObject;
/* 185:    */   }
/* 186:    */   
/* 187:    */   public static Object getSessionMutex(HttpSession session)
/* 188:    */   {
/* 189:364 */     Assert.notNull(session, "Session must not be null");
/* 190:365 */     Object mutex = session.getAttribute(SESSION_MUTEX_ATTRIBUTE);
/* 191:366 */     if (mutex == null) {
/* 192:367 */       mutex = session;
/* 193:    */     }
/* 194:369 */     return mutex;
/* 195:    */   }
/* 196:    */   
/* 197:    */   public static <T> T getNativeRequest(ServletRequest request, Class<T> requiredType)
/* 198:    */   {
/* 199:383 */     if (requiredType != null)
/* 200:    */     {
/* 201:384 */       if (requiredType.isInstance(request)) {
/* 202:385 */         return request;
/* 203:    */       }
/* 204:387 */       if ((request instanceof ServletRequestWrapper)) {
/* 205:388 */         return getNativeRequest(((ServletRequestWrapper)request).getRequest(), requiredType);
/* 206:    */       }
/* 207:    */     }
/* 208:391 */     return null;
/* 209:    */   }
/* 210:    */   
/* 211:    */   public static <T> T getNativeResponse(ServletResponse response, Class<T> requiredType)
/* 212:    */   {
/* 213:404 */     if (requiredType != null)
/* 214:    */     {
/* 215:405 */       if (requiredType.isInstance(response)) {
/* 216:406 */         return response;
/* 217:    */       }
/* 218:408 */       if ((response instanceof ServletResponseWrapper)) {
/* 219:409 */         return getNativeResponse(((ServletResponseWrapper)response).getResponse(), requiredType);
/* 220:    */       }
/* 221:    */     }
/* 222:412 */     return null;
/* 223:    */   }
/* 224:    */   
/* 225:    */   public static boolean isIncludeRequest(ServletRequest request)
/* 226:    */   {
/* 227:425 */     return request.getAttribute("javax.servlet.include.request_uri") != null;
/* 228:    */   }
/* 229:    */   
/* 230:    */   public static void exposeForwardRequestAttributes(HttpServletRequest request)
/* 231:    */   {
/* 232:442 */     exposeRequestAttributeIfNotPresent(request, "javax.servlet.forward.request_uri", request.getRequestURI());
/* 233:443 */     exposeRequestAttributeIfNotPresent(request, "javax.servlet.forward.context_path", request.getContextPath());
/* 234:444 */     exposeRequestAttributeIfNotPresent(request, "javax.servlet.forward.servlet_path", request.getServletPath());
/* 235:445 */     exposeRequestAttributeIfNotPresent(request, "javax.servlet.forward.path_info", request.getPathInfo());
/* 236:446 */     exposeRequestAttributeIfNotPresent(request, "javax.servlet.forward.query_string", request.getQueryString());
/* 237:    */   }
/* 238:    */   
/* 239:    */   public static void exposeErrorRequestAttributes(HttpServletRequest request, Throwable ex, String servletName)
/* 240:    */   {
/* 241:468 */     exposeRequestAttributeIfNotPresent(request, "javax.servlet.error.status_code", Integer.valueOf(200));
/* 242:469 */     exposeRequestAttributeIfNotPresent(request, "javax.servlet.error.exception_type", ex.getClass());
/* 243:470 */     exposeRequestAttributeIfNotPresent(request, "javax.servlet.error.message", ex.getMessage());
/* 244:471 */     exposeRequestAttributeIfNotPresent(request, "javax.servlet.error.exception", ex);
/* 245:472 */     exposeRequestAttributeIfNotPresent(request, "javax.servlet.error.request_uri", request.getRequestURI());
/* 246:473 */     exposeRequestAttributeIfNotPresent(request, "javax.servlet.error.servlet_name", servletName);
/* 247:    */   }
/* 248:    */   
/* 249:    */   private static void exposeRequestAttributeIfNotPresent(ServletRequest request, String name, Object value)
/* 250:    */   {
/* 251:483 */     if (request.getAttribute(name) == null) {
/* 252:484 */       request.setAttribute(name, value);
/* 253:    */     }
/* 254:    */   }
/* 255:    */   
/* 256:    */   public static void clearErrorRequestAttributes(HttpServletRequest request)
/* 257:    */   {
/* 258:500 */     request.removeAttribute("javax.servlet.error.status_code");
/* 259:501 */     request.removeAttribute("javax.servlet.error.exception_type");
/* 260:502 */     request.removeAttribute("javax.servlet.error.message");
/* 261:503 */     request.removeAttribute("javax.servlet.error.exception");
/* 262:504 */     request.removeAttribute("javax.servlet.error.request_uri");
/* 263:505 */     request.removeAttribute("javax.servlet.error.servlet_name");
/* 264:    */   }
/* 265:    */   
/* 266:    */   public static void exposeRequestAttributes(ServletRequest request, Map<String, ?> attributes)
/* 267:    */   {
/* 268:515 */     Assert.notNull(request, "Request must not be null");
/* 269:516 */     Assert.notNull(attributes, "Attributes Map must not be null");
/* 270:517 */     for (Map.Entry<String, ?> entry : attributes.entrySet()) {
/* 271:518 */       request.setAttribute((String)entry.getKey(), entry.getValue());
/* 272:    */     }
/* 273:    */   }
/* 274:    */   
/* 275:    */   public static Cookie getCookie(HttpServletRequest request, String name)
/* 276:    */   {
/* 277:530 */     Assert.notNull(request, "Request must not be null");
/* 278:531 */     Cookie[] cookies = request.getCookies();
/* 279:532 */     if (cookies != null) {
/* 280:533 */       for (Cookie cookie : cookies) {
/* 281:534 */         if (name.equals(cookie.getName())) {
/* 282:535 */           return cookie;
/* 283:    */         }
/* 284:    */       }
/* 285:    */     }
/* 286:539 */     return null;
/* 287:    */   }
/* 288:    */   
/* 289:    */   public static boolean hasSubmitParameter(ServletRequest request, String name)
/* 290:    */   {
/* 291:552 */     Assert.notNull(request, "Request must not be null");
/* 292:553 */     if (request.getParameter(name) != null) {
/* 293:554 */       return true;
/* 294:    */     }
/* 295:556 */     for (String suffix : SUBMIT_IMAGE_SUFFIXES) {
/* 296:557 */       if (request.getParameter(name + suffix) != null) {
/* 297:558 */         return true;
/* 298:    */       }
/* 299:    */     }
/* 300:561 */     return false;
/* 301:    */   }
/* 302:    */   
/* 303:    */   public static String findParameterValue(ServletRequest request, String name)
/* 304:    */   {
/* 305:575 */     return findParameterValue(request.getParameterMap(), name);
/* 306:    */   }
/* 307:    */   
/* 308:    */   public static String findParameterValue(Map<String, ?> parameters, String name)
/* 309:    */   {
/* 310:603 */     Object value = parameters.get(name);
/* 311:604 */     if ((value instanceof String[]))
/* 312:    */     {
/* 313:605 */       String[] values = (String[])value;
/* 314:606 */       return values.length > 0 ? values[0] : null;
/* 315:    */     }
/* 316:608 */     if (value != null) {
/* 317:609 */       return value.toString();
/* 318:    */     }
/* 319:612 */     String prefix = name + "_";
/* 320:613 */     for (String paramName : parameters.keySet()) {
/* 321:614 */       if (paramName.startsWith(prefix))
/* 322:    */       {
/* 323:616 */         for (String suffix : SUBMIT_IMAGE_SUFFIXES) {
/* 324:617 */           if (paramName.endsWith(suffix)) {
/* 325:618 */             return paramName.substring(prefix.length(), paramName.length() - suffix.length());
/* 326:    */           }
/* 327:    */         }
/* 328:621 */         return paramName.substring(prefix.length());
/* 329:    */       }
/* 330:    */     }
/* 331:625 */     return null;
/* 332:    */   }
/* 333:    */   
/* 334:    */   public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix)
/* 335:    */   {
/* 336:643 */     Assert.notNull(request, "Request must not be null");
/* 337:644 */     Enumeration paramNames = request.getParameterNames();
/* 338:645 */     Map<String, Object> params = new TreeMap();
/* 339:646 */     if (prefix == null) {
/* 340:647 */       prefix = "";
/* 341:    */     }
/* 342:649 */     while ((paramNames != null) && (paramNames.hasMoreElements()))
/* 343:    */     {
/* 344:650 */       String paramName = (String)paramNames.nextElement();
/* 345:651 */       if (("".equals(prefix)) || (paramName.startsWith(prefix)))
/* 346:    */       {
/* 347:652 */         String unprefixed = paramName.substring(prefix.length());
/* 348:653 */         String[] values = request.getParameterValues(paramName);
/* 349:654 */         if ((values != null) && (values.length != 0)) {
/* 350:657 */           if (values.length > 1) {
/* 351:658 */             params.put(unprefixed, values);
/* 352:    */           } else {
/* 353:661 */             params.put(unprefixed, values[0]);
/* 354:    */           }
/* 355:    */         }
/* 356:    */       }
/* 357:    */     }
/* 358:665 */     return params;
/* 359:    */   }
/* 360:    */   
/* 361:    */   public static int getTargetPage(ServletRequest request, String paramPrefix, int currentPage)
/* 362:    */   {
/* 363:678 */     Enumeration paramNames = request.getParameterNames();
/* 364:679 */     while (paramNames.hasMoreElements())
/* 365:    */     {
/* 366:680 */       String paramName = (String)paramNames.nextElement();
/* 367:681 */       if (paramName.startsWith(paramPrefix))
/* 368:    */       {
/* 369:682 */         for (int i = 0; i < SUBMIT_IMAGE_SUFFIXES.length; i++)
/* 370:    */         {
/* 371:683 */           String suffix = SUBMIT_IMAGE_SUFFIXES[i];
/* 372:684 */           if (paramName.endsWith(suffix)) {
/* 373:685 */             paramName = paramName.substring(0, paramName.length() - suffix.length());
/* 374:    */           }
/* 375:    */         }
/* 376:688 */         return Integer.parseInt(paramName.substring(paramPrefix.length()));
/* 377:    */       }
/* 378:    */     }
/* 379:691 */     return currentPage;
/* 380:    */   }
/* 381:    */   
/* 382:    */   public static String extractFilenameFromUrlPath(String urlPath)
/* 383:    */   {
/* 384:702 */     String filename = extractFullFilenameFromUrlPath(urlPath);
/* 385:703 */     int dotIndex = filename.lastIndexOf('.');
/* 386:704 */     if (dotIndex != -1) {
/* 387:705 */       filename = filename.substring(0, dotIndex);
/* 388:    */     }
/* 389:707 */     return filename;
/* 390:    */   }
/* 391:    */   
/* 392:    */   public static String extractFullFilenameFromUrlPath(String urlPath)
/* 393:    */   {
/* 394:716 */     int end = urlPath.indexOf(';');
/* 395:717 */     if (end == -1)
/* 396:    */     {
/* 397:718 */       end = urlPath.indexOf('?');
/* 398:719 */       if (end == -1) {
/* 399:720 */         end = urlPath.length();
/* 400:    */       }
/* 401:    */     }
/* 402:723 */     int begin = urlPath.lastIndexOf('/', end) + 1;
/* 403:724 */     return urlPath.substring(begin, end);
/* 404:    */   }
/* 405:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.util.WebUtils
 * JD-Core Version:    0.7.0.1
 */