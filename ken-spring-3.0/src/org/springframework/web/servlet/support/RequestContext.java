/*   1:    */ package org.springframework.web.servlet.support;
/*   2:    */ 
/*   3:    */ import java.net.URI;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Locale;
/*   7:    */ import java.util.Map;
/*   8:    */ import javax.servlet.ServletContext;
/*   9:    */ import javax.servlet.http.HttpServletRequest;
/*  10:    */ import javax.servlet.http.HttpServletResponse;
/*  11:    */ import javax.servlet.http.HttpSession;
/*  12:    */ import javax.servlet.jsp.jstl.core.Config;
/*  13:    */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*  14:    */ import org.springframework.context.MessageSource;
/*  15:    */ import org.springframework.context.MessageSourceResolvable;
/*  16:    */ import org.springframework.context.NoSuchMessageException;
/*  17:    */ import org.springframework.ui.context.Theme;
/*  18:    */ import org.springframework.ui.context.ThemeSource;
/*  19:    */ import org.springframework.ui.context.support.ResourceBundleThemeSource;
/*  20:    */ import org.springframework.util.Assert;
/*  21:    */ import org.springframework.util.ClassUtils;
/*  22:    */ import org.springframework.validation.BindException;
/*  23:    */ import org.springframework.validation.BindingResult;
/*  24:    */ import org.springframework.validation.Errors;
/*  25:    */ import org.springframework.web.bind.EscapedErrors;
/*  26:    */ import org.springframework.web.context.WebApplicationContext;
/*  27:    */ import org.springframework.web.servlet.LocaleResolver;
/*  28:    */ import org.springframework.web.util.HtmlUtils;
/*  29:    */ import org.springframework.web.util.UriTemplate;
/*  30:    */ import org.springframework.web.util.UrlPathHelper;
/*  31:    */ import org.springframework.web.util.WebUtils;
/*  32:    */ 
/*  33:    */ public class RequestContext
/*  34:    */ {
/*  35:    */   public static final String DEFAULT_THEME_NAME = "theme";
/*  36: 83 */   public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = RequestContext.class.getName() + ".CONTEXT";
/*  37:    */   private static final String REQUEST_DATA_VALUE_PROCESSOR_BEAN_NAME = "requestDataValueProcessor";
/*  38: 91 */   protected static final boolean jstlPresent = ClassUtils.isPresent("javax.servlet.jsp.jstl.core.Config", 
/*  39: 92 */     RequestContext.class.getClassLoader());
/*  40:    */   private HttpServletRequest request;
/*  41:    */   private HttpServletResponse response;
/*  42:    */   private Map<String, Object> model;
/*  43:    */   private WebApplicationContext webApplicationContext;
/*  44:    */   private Locale locale;
/*  45:    */   private Theme theme;
/*  46:    */   private Boolean defaultHtmlEscape;
/*  47:    */   private UrlPathHelper urlPathHelper;
/*  48:    */   private RequestDataValueProcessor requestDataValueProcessor;
/*  49:    */   private Map<String, Errors> errorsMap;
/*  50:    */   
/*  51:    */   public RequestContext(HttpServletRequest request)
/*  52:    */   {
/*  53:124 */     initContext(request, null, null, null);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public RequestContext(HttpServletRequest request, ServletContext servletContext)
/*  57:    */   {
/*  58:139 */     initContext(request, null, servletContext, null);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public RequestContext(HttpServletRequest request, Map<String, Object> model)
/*  62:    */   {
/*  63:154 */     initContext(request, null, null, model);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public RequestContext(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext, Map<String, Object> model)
/*  67:    */   {
/*  68:173 */     initContext(request, response, servletContext, model);
/*  69:    */   }
/*  70:    */   
/*  71:    */   protected RequestContext() {}
/*  72:    */   
/*  73:    */   protected void initContext(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext, Map<String, Object> model)
/*  74:    */   {
/*  75:199 */     this.request = request;
/*  76:200 */     this.response = response;
/*  77:201 */     this.model = model;
/*  78:    */     
/*  79:    */ 
/*  80:    */ 
/*  81:205 */     this.webApplicationContext = ((WebApplicationContext)request.getAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE));
/*  82:206 */     if (this.webApplicationContext == null) {
/*  83:207 */       this.webApplicationContext = RequestContextUtils.getWebApplicationContext(request, servletContext);
/*  84:    */     }
/*  85:211 */     LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
/*  86:212 */     if (localeResolver != null) {
/*  87:214 */       this.locale = localeResolver.resolveLocale(request);
/*  88:    */     } else {
/*  89:217 */       this.locale = getFallbackLocale();
/*  90:    */     }
/*  91:222 */     this.defaultHtmlEscape = WebUtils.getDefaultHtmlEscape(this.webApplicationContext.getServletContext());
/*  92:    */     
/*  93:224 */     this.urlPathHelper = new UrlPathHelper();
/*  94:    */     try
/*  95:    */     {
/*  96:227 */       this.requestDataValueProcessor = 
/*  97:228 */         ((RequestDataValueProcessor)this.webApplicationContext.getBean("requestDataValueProcessor", RequestDataValueProcessor.class));
/*  98:    */     }
/*  99:    */     catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException) {}
/* 100:    */   }
/* 101:    */   
/* 102:    */   protected Locale getFallbackLocale()
/* 103:    */   {
/* 104:242 */     if (jstlPresent)
/* 105:    */     {
/* 106:243 */       Locale locale = JstlLocaleResolver.getJstlLocale(getRequest(), getServletContext());
/* 107:244 */       if (locale != null) {
/* 108:245 */         return locale;
/* 109:    */       }
/* 110:    */     }
/* 111:248 */     return getRequest().getLocale();
/* 112:    */   }
/* 113:    */   
/* 114:    */   protected Theme getFallbackTheme()
/* 115:    */   {
/* 116:257 */     ThemeSource themeSource = RequestContextUtils.getThemeSource(getRequest());
/* 117:258 */     if (themeSource == null) {
/* 118:259 */       themeSource = new ResourceBundleThemeSource();
/* 119:    */     }
/* 120:261 */     Theme theme = themeSource.getTheme("theme");
/* 121:262 */     if (theme == null) {
/* 122:263 */       throw new IllegalStateException("No theme defined and no fallback theme found");
/* 123:    */     }
/* 124:265 */     return theme;
/* 125:    */   }
/* 126:    */   
/* 127:    */   protected final HttpServletRequest getRequest()
/* 128:    */   {
/* 129:272 */     return this.request;
/* 130:    */   }
/* 131:    */   
/* 132:    */   protected final ServletContext getServletContext()
/* 133:    */   {
/* 134:279 */     return this.webApplicationContext.getServletContext();
/* 135:    */   }
/* 136:    */   
/* 137:    */   public final WebApplicationContext getWebApplicationContext()
/* 138:    */   {
/* 139:286 */     return this.webApplicationContext;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public final MessageSource getMessageSource()
/* 143:    */   {
/* 144:293 */     return this.webApplicationContext;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public final Map<String, Object> getModel()
/* 148:    */   {
/* 149:301 */     return this.model;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public final Locale getLocale()
/* 153:    */   {
/* 154:308 */     return this.locale;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public final Theme getTheme()
/* 158:    */   {
/* 159:316 */     if (this.theme == null)
/* 160:    */     {
/* 161:318 */       this.theme = RequestContextUtils.getTheme(this.request);
/* 162:319 */       if (this.theme == null) {
/* 163:321 */         this.theme = getFallbackTheme();
/* 164:    */       }
/* 165:    */     }
/* 166:324 */     return this.theme;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public void setDefaultHtmlEscape(boolean defaultHtmlEscape)
/* 170:    */   {
/* 171:333 */     this.defaultHtmlEscape = Boolean.valueOf(defaultHtmlEscape);
/* 172:    */   }
/* 173:    */   
/* 174:    */   public boolean isDefaultHtmlEscape()
/* 175:    */   {
/* 176:340 */     return (this.defaultHtmlEscape != null) && (this.defaultHtmlEscape.booleanValue());
/* 177:    */   }
/* 178:    */   
/* 179:    */   public Boolean getDefaultHtmlEscape()
/* 180:    */   {
/* 181:348 */     return this.defaultHtmlEscape;
/* 182:    */   }
/* 183:    */   
/* 184:    */   public void setUrlPathHelper(UrlPathHelper urlPathHelper)
/* 185:    */   {
/* 186:356 */     Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
/* 187:357 */     this.urlPathHelper = urlPathHelper;
/* 188:    */   }
/* 189:    */   
/* 190:    */   public UrlPathHelper getUrlPathHelper()
/* 191:    */   {
/* 192:365 */     return this.urlPathHelper;
/* 193:    */   }
/* 194:    */   
/* 195:    */   public RequestDataValueProcessor getRequestDataValueProcessor()
/* 196:    */   {
/* 197:374 */     return this.requestDataValueProcessor;
/* 198:    */   }
/* 199:    */   
/* 200:    */   public String getContextPath()
/* 201:    */   {
/* 202:385 */     return this.urlPathHelper.getOriginatingContextPath(this.request);
/* 203:    */   }
/* 204:    */   
/* 205:    */   public String getContextUrl(String relativeUrl)
/* 206:    */   {
/* 207:394 */     String url = getContextPath() + relativeUrl;
/* 208:395 */     if (this.response != null) {
/* 209:396 */       url = this.response.encodeURL(url);
/* 210:    */     }
/* 211:398 */     return url;
/* 212:    */   }
/* 213:    */   
/* 214:    */   public String getContextUrl(String relativeUrl, Map<String, ?> params)
/* 215:    */   {
/* 216:411 */     String url = getContextPath() + relativeUrl;
/* 217:412 */     UriTemplate template = new UriTemplate(url);
/* 218:413 */     url = template.expand(params).toASCIIString();
/* 219:414 */     if (this.response != null) {
/* 220:415 */       url = this.response.encodeURL(url);
/* 221:    */     }
/* 222:417 */     return url;
/* 223:    */   }
/* 224:    */   
/* 225:    */   public String getRequestUri()
/* 226:    */   {
/* 227:431 */     return this.urlPathHelper.getOriginatingRequestUri(this.request);
/* 228:    */   }
/* 229:    */   
/* 230:    */   public String getQueryString()
/* 231:    */   {
/* 232:445 */     return this.urlPathHelper.getOriginatingQueryString(this.request);
/* 233:    */   }
/* 234:    */   
/* 235:    */   public String getMessage(String code, String defaultMessage)
/* 236:    */   {
/* 237:455 */     return getMessage(code, null, defaultMessage, isDefaultHtmlEscape());
/* 238:    */   }
/* 239:    */   
/* 240:    */   public String getMessage(String code, Object[] args, String defaultMessage)
/* 241:    */   {
/* 242:466 */     return getMessage(code, args, defaultMessage, isDefaultHtmlEscape());
/* 243:    */   }
/* 244:    */   
/* 245:    */   public String getMessage(String code, List args, String defaultMessage)
/* 246:    */   {
/* 247:477 */     return getMessage(code, args != null ? args.toArray() : null, defaultMessage, isDefaultHtmlEscape());
/* 248:    */   }
/* 249:    */   
/* 250:    */   public String getMessage(String code, Object[] args, String defaultMessage, boolean htmlEscape)
/* 251:    */   {
/* 252:489 */     String msg = this.webApplicationContext.getMessage(code, args, defaultMessage, this.locale);
/* 253:490 */     return htmlEscape ? HtmlUtils.htmlEscape(msg) : msg;
/* 254:    */   }
/* 255:    */   
/* 256:    */   public String getMessage(String code)
/* 257:    */     throws NoSuchMessageException
/* 258:    */   {
/* 259:500 */     return getMessage(code, null, isDefaultHtmlEscape());
/* 260:    */   }
/* 261:    */   
/* 262:    */   public String getMessage(String code, Object[] args)
/* 263:    */     throws NoSuchMessageException
/* 264:    */   {
/* 265:511 */     return getMessage(code, args, isDefaultHtmlEscape());
/* 266:    */   }
/* 267:    */   
/* 268:    */   public String getMessage(String code, List args)
/* 269:    */     throws NoSuchMessageException
/* 270:    */   {
/* 271:522 */     return getMessage(code, args != null ? args.toArray() : null, isDefaultHtmlEscape());
/* 272:    */   }
/* 273:    */   
/* 274:    */   public String getMessage(String code, Object[] args, boolean htmlEscape)
/* 275:    */     throws NoSuchMessageException
/* 276:    */   {
/* 277:534 */     String msg = this.webApplicationContext.getMessage(code, args, this.locale);
/* 278:535 */     return htmlEscape ? HtmlUtils.htmlEscape(msg) : msg;
/* 279:    */   }
/* 280:    */   
/* 281:    */   public String getMessage(MessageSourceResolvable resolvable)
/* 282:    */     throws NoSuchMessageException
/* 283:    */   {
/* 284:545 */     return getMessage(resolvable, isDefaultHtmlEscape());
/* 285:    */   }
/* 286:    */   
/* 287:    */   public String getMessage(MessageSourceResolvable resolvable, boolean htmlEscape)
/* 288:    */     throws NoSuchMessageException
/* 289:    */   {
/* 290:556 */     String msg = this.webApplicationContext.getMessage(resolvable, this.locale);
/* 291:557 */     return htmlEscape ? HtmlUtils.htmlEscape(msg) : msg;
/* 292:    */   }
/* 293:    */   
/* 294:    */   public String getThemeMessage(String code, String defaultMessage)
/* 295:    */   {
/* 296:568 */     return getTheme().getMessageSource().getMessage(code, null, defaultMessage, this.locale);
/* 297:    */   }
/* 298:    */   
/* 299:    */   public String getThemeMessage(String code, Object[] args, String defaultMessage)
/* 300:    */   {
/* 301:580 */     return getTheme().getMessageSource().getMessage(code, args, defaultMessage, this.locale);
/* 302:    */   }
/* 303:    */   
/* 304:    */   public String getThemeMessage(String code, List args, String defaultMessage)
/* 305:    */   {
/* 306:592 */     return getTheme().getMessageSource().getMessage(code, args != null ? args.toArray() : null, defaultMessage, 
/* 307:593 */       this.locale);
/* 308:    */   }
/* 309:    */   
/* 310:    */   public String getThemeMessage(String code)
/* 311:    */     throws NoSuchMessageException
/* 312:    */   {
/* 313:604 */     return getTheme().getMessageSource().getMessage(code, null, this.locale);
/* 314:    */   }
/* 315:    */   
/* 316:    */   public String getThemeMessage(String code, Object[] args)
/* 317:    */     throws NoSuchMessageException
/* 318:    */   {
/* 319:616 */     return getTheme().getMessageSource().getMessage(code, args, this.locale);
/* 320:    */   }
/* 321:    */   
/* 322:    */   public String getThemeMessage(String code, List args)
/* 323:    */     throws NoSuchMessageException
/* 324:    */   {
/* 325:628 */     return getTheme().getMessageSource().getMessage(code, args != null ? args.toArray() : null, this.locale);
/* 326:    */   }
/* 327:    */   
/* 328:    */   public String getThemeMessage(MessageSourceResolvable resolvable)
/* 329:    */     throws NoSuchMessageException
/* 330:    */   {
/* 331:639 */     return getTheme().getMessageSource().getMessage(resolvable, this.locale);
/* 332:    */   }
/* 333:    */   
/* 334:    */   public Errors getErrors(String name)
/* 335:    */   {
/* 336:648 */     return getErrors(name, isDefaultHtmlEscape());
/* 337:    */   }
/* 338:    */   
/* 339:    */   public Errors getErrors(String name, boolean htmlEscape)
/* 340:    */   {
/* 341:658 */     if (this.errorsMap == null) {
/* 342:659 */       this.errorsMap = new HashMap();
/* 343:    */     }
/* 344:661 */     Errors errors = (Errors)this.errorsMap.get(name);
/* 345:662 */     boolean put = false;
/* 346:663 */     if (errors == null)
/* 347:    */     {
/* 348:664 */       errors = (Errors)getModelObject(BindingResult.MODEL_KEY_PREFIX + name);
/* 349:666 */       if ((errors instanceof BindException)) {
/* 350:667 */         errors = ((BindException)errors).getBindingResult();
/* 351:    */       }
/* 352:669 */       if (errors == null) {
/* 353:670 */         return null;
/* 354:    */       }
/* 355:672 */       put = true;
/* 356:    */     }
/* 357:674 */     if ((htmlEscape) && (!(errors instanceof EscapedErrors)))
/* 358:    */     {
/* 359:675 */       errors = new EscapedErrors(errors);
/* 360:676 */       put = true;
/* 361:    */     }
/* 362:677 */     else if ((!htmlEscape) && ((errors instanceof EscapedErrors)))
/* 363:    */     {
/* 364:678 */       errors = ((EscapedErrors)errors).getSource();
/* 365:679 */       put = true;
/* 366:    */     }
/* 367:681 */     if (put) {
/* 368:682 */       this.errorsMap.put(name, errors);
/* 369:    */     }
/* 370:684 */     return errors;
/* 371:    */   }
/* 372:    */   
/* 373:    */   protected Object getModelObject(String modelName)
/* 374:    */   {
/* 375:693 */     if (this.model != null) {
/* 376:694 */       return this.model.get(modelName);
/* 377:    */     }
/* 378:696 */     return this.request.getAttribute(modelName);
/* 379:    */   }
/* 380:    */   
/* 381:    */   public BindStatus getBindStatus(String path)
/* 382:    */     throws IllegalStateException
/* 383:    */   {
/* 384:707 */     return new BindStatus(this, path, isDefaultHtmlEscape());
/* 385:    */   }
/* 386:    */   
/* 387:    */   public BindStatus getBindStatus(String path, boolean htmlEscape)
/* 388:    */     throws IllegalStateException
/* 389:    */   {
/* 390:718 */     return new BindStatus(this, path, htmlEscape);
/* 391:    */   }
/* 392:    */   
/* 393:    */   private static class JstlLocaleResolver
/* 394:    */   {
/* 395:    */     public static Locale getJstlLocale(HttpServletRequest request, ServletContext servletContext)
/* 396:    */     {
/* 397:728 */       Object localeObject = Config.get(request, "javax.servlet.jsp.jstl.fmt.locale");
/* 398:729 */       if (localeObject == null)
/* 399:    */       {
/* 400:730 */         HttpSession session = request.getSession(false);
/* 401:731 */         if (session != null) {
/* 402:732 */           localeObject = Config.get(session, "javax.servlet.jsp.jstl.fmt.locale");
/* 403:    */         }
/* 404:734 */         if ((localeObject == null) && (servletContext != null)) {
/* 405:735 */           localeObject = Config.get(servletContext, "javax.servlet.jsp.jstl.fmt.locale");
/* 406:    */         }
/* 407:    */       }
/* 408:738 */       return (localeObject instanceof Locale) ? (Locale)localeObject : null;
/* 409:    */     }
/* 410:    */   }
/* 411:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.support.RequestContext
 * JD-Core Version:    0.7.0.1
 */