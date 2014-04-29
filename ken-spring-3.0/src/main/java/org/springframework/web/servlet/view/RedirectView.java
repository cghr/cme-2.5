/*   1:    */ package org.springframework.web.servlet.view;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.UnsupportedEncodingException;
/*   5:    */ import java.lang.reflect.Array;
/*   6:    */ import java.net.URLEncoder;
/*   7:    */ import java.util.Arrays;
/*   8:    */ import java.util.Collection;
/*   9:    */ import java.util.Collections;
/*  10:    */ import java.util.Iterator;
/*  11:    */ import java.util.LinkedHashMap;
/*  12:    */ import java.util.List;
/*  13:    */ import java.util.Map;
/*  14:    */ import java.util.Map.Entry;
/*  15:    */ import java.util.Set;
/*  16:    */ import java.util.regex.Matcher;
/*  17:    */ import java.util.regex.Pattern;
/*  18:    */ import javax.servlet.http.HttpServletRequest;
/*  19:    */ import javax.servlet.http.HttpServletResponse;
/*  20:    */ import org.springframework.beans.BeanUtils;
/*  21:    */ import org.springframework.http.HttpStatus;
/*  22:    */ import org.springframework.util.Assert;
/*  23:    */ import org.springframework.util.CollectionUtils;
/*  24:    */ import org.springframework.util.ObjectUtils;
/*  25:    */ import org.springframework.util.StringUtils;
/*  26:    */ import org.springframework.web.servlet.FlashMap;
/*  27:    */ import org.springframework.web.servlet.HandlerMapping;
/*  28:    */ import org.springframework.web.servlet.SmartView;
/*  29:    */ import org.springframework.web.servlet.View;
/*  30:    */ import org.springframework.web.servlet.support.RequestContext;
/*  31:    */ import org.springframework.web.servlet.support.RequestContextUtils;
/*  32:    */ import org.springframework.web.servlet.support.RequestDataValueProcessor;
/*  33:    */ import org.springframework.web.util.UriComponents;
/*  34:    */ import org.springframework.web.util.UriComponentsBuilder;
/*  35:    */ import org.springframework.web.util.UriUtils;
/*  36:    */ 
/*  37:    */ public class RedirectView
/*  38:    */   extends AbstractUrlBasedView
/*  39:    */   implements SmartView
/*  40:    */ {
/*  41: 95 */   private static final Pattern URI_TEMPLATE_VARIABLE_PATTERN = Pattern.compile("\\{([^/]+?)\\}");
/*  42: 97 */   private boolean contextRelative = false;
/*  43: 99 */   private boolean http10Compatible = true;
/*  44:101 */   private boolean exposeModelAttributes = true;
/*  45:    */   private String encodingScheme;
/*  46:    */   private HttpStatus statusCode;
/*  47:    */   
/*  48:    */   public RedirectView()
/*  49:    */   {
/*  50:112 */     setExposePathVariables(false);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public RedirectView(String url)
/*  54:    */   {
/*  55:123 */     super(url);
/*  56:124 */     setExposePathVariables(false);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public RedirectView(String url, boolean contextRelative)
/*  60:    */   {
/*  61:134 */     super(url);
/*  62:135 */     this.contextRelative = contextRelative;
/*  63:136 */     setExposePathVariables(false);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public RedirectView(String url, boolean contextRelative, boolean http10Compatible)
/*  67:    */   {
/*  68:147 */     super(url);
/*  69:148 */     this.contextRelative = contextRelative;
/*  70:149 */     this.http10Compatible = http10Compatible;
/*  71:150 */     setExposePathVariables(false);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public RedirectView(String url, boolean contextRelative, boolean http10Compatible, boolean exposeModelAttributes)
/*  75:    */   {
/*  76:163 */     super(url);
/*  77:164 */     this.contextRelative = contextRelative;
/*  78:165 */     this.http10Compatible = http10Compatible;
/*  79:166 */     this.exposeModelAttributes = exposeModelAttributes;
/*  80:167 */     setExposePathVariables(false);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void setContextRelative(boolean contextRelative)
/*  84:    */   {
/*  85:181 */     this.contextRelative = contextRelative;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void setHttp10Compatible(boolean http10Compatible)
/*  89:    */   {
/*  90:196 */     this.http10Compatible = http10Compatible;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void setExposeModelAttributes(boolean exposeModelAttributes)
/*  94:    */   {
/*  95:205 */     this.exposeModelAttributes = exposeModelAttributes;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void setEncodingScheme(String encodingScheme)
/*  99:    */   {
/* 100:214 */     this.encodingScheme = encodingScheme;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void setStatusCode(HttpStatus statusCode)
/* 104:    */   {
/* 105:223 */     this.statusCode = statusCode;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public boolean isRedirectView()
/* 109:    */   {
/* 110:230 */     return true;
/* 111:    */   }
/* 112:    */   
/* 113:    */   protected boolean isContextRequired()
/* 114:    */   {
/* 115:238 */     return false;
/* 116:    */   }
/* 117:    */   
/* 118:    */   protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/* 119:    */     throws IOException
/* 120:    */   {
/* 121:251 */     String targetUrl = createTargetUrl(model, request);
/* 122:253 */     if (getWebApplicationContext() != null)
/* 123:    */     {
/* 124:254 */       RequestContext requestContext = createRequestContext(request, response, model);
/* 125:255 */       RequestDataValueProcessor processor = requestContext.getRequestDataValueProcessor();
/* 126:256 */       if (processor != null) {
/* 127:257 */         targetUrl = processor.processUrl(request, targetUrl);
/* 128:    */       }
/* 129:    */     }
/* 130:261 */     FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
/* 131:262 */     if (!CollectionUtils.isEmpty(flashMap))
/* 132:    */     {
/* 133:263 */       UriComponents uriComponents = UriComponentsBuilder.fromUriString(targetUrl).build();
/* 134:264 */       flashMap.setTargetRequestPath(uriComponents.getPath());
/* 135:265 */       flashMap.addTargetRequestParams(uriComponents.getQueryParams());
/* 136:    */     }
/* 137:268 */     sendRedirect(request, response, targetUrl.toString(), this.http10Compatible);
/* 138:    */   }
/* 139:    */   
/* 140:    */   protected final String createTargetUrl(Map<String, Object> model, HttpServletRequest request)
/* 141:    */     throws UnsupportedEncodingException
/* 142:    */   {
/* 143:280 */     StringBuilder targetUrl = new StringBuilder();
/* 144:281 */     if ((this.contextRelative) && (getUrl().startsWith("/"))) {
/* 145:283 */       targetUrl.append(request.getContextPath());
/* 146:    */     }
/* 147:285 */     targetUrl.append(getUrl());
/* 148:    */     
/* 149:287 */     String enc = this.encodingScheme;
/* 150:288 */     if (enc == null) {
/* 151:289 */       enc = request.getCharacterEncoding();
/* 152:    */     }
/* 153:291 */     if (enc == null) {
/* 154:292 */       enc = "ISO-8859-1";
/* 155:    */     }
/* 156:295 */     if (StringUtils.hasText(targetUrl))
/* 157:    */     {
/* 158:296 */       Map<String, String> variables = getCurrentRequestUriVariables(request);
/* 159:297 */       targetUrl = replaceUriTemplateVariables(targetUrl.toString(), model, variables, enc);
/* 160:    */     }
/* 161:300 */     if (this.exposeModelAttributes) {
/* 162:301 */       appendQueryProperties(targetUrl, model, enc);
/* 163:    */     }
/* 164:304 */     return targetUrl.toString();
/* 165:    */   }
/* 166:    */   
/* 167:    */   protected StringBuilder replaceUriTemplateVariables(String targetUrl, Map<String, Object> model, Map<String, String> currentUriVariables, String encodingScheme)
/* 168:    */     throws UnsupportedEncodingException
/* 169:    */   {
/* 170:321 */     StringBuilder result = new StringBuilder();
/* 171:322 */     Matcher m = URI_TEMPLATE_VARIABLE_PATTERN.matcher(targetUrl);
/* 172:323 */     int endLastMatch = 0;
/* 173:324 */     while (m.find())
/* 174:    */     {
/* 175:325 */       String name = m.group(1);
/* 176:326 */       Object value = model.containsKey(name) ? model.remove(name) : currentUriVariables.get(name);
/* 177:327 */       Assert.notNull(value, "Model has no value for '" + name + "'");
/* 178:328 */       result.append(targetUrl.substring(endLastMatch, m.start()));
/* 179:329 */       result.append(UriUtils.encodePathSegment(value.toString(), encodingScheme));
/* 180:330 */       endLastMatch = m.end();
/* 181:    */     }
/* 182:332 */     result.append(targetUrl.substring(endLastMatch, targetUrl.length()));
/* 183:333 */     return result;
/* 184:    */   }
/* 185:    */   
/* 186:    */   private Map<String, String> getCurrentRequestUriVariables(HttpServletRequest request)
/* 187:    */   {
/* 188:338 */     Map<String, String> uriVars = 
/* 189:339 */       (Map)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
/* 190:340 */     return uriVars != null ? uriVars : Collections.emptyMap();
/* 191:    */   }
/* 192:    */   
/* 193:    */   protected void appendQueryProperties(StringBuilder targetUrl, Map<String, Object> model, String encodingScheme)
/* 194:    */     throws UnsupportedEncodingException
/* 195:    */   {
/* 196:356 */     String fragment = null;
/* 197:357 */     int anchorIndex = targetUrl.indexOf("#");
/* 198:358 */     if (anchorIndex > -1)
/* 199:    */     {
/* 200:359 */       fragment = targetUrl.substring(anchorIndex);
/* 201:360 */       targetUrl.delete(anchorIndex, targetUrl.length());
/* 202:    */     }
/* 203:364 */     boolean first = targetUrl.toString().indexOf('?') < 0;
/* 204:    */     Iterator<Object> valueIter;
/* 205:365 */     for (Iterator localIterator = queryProperties(model).entrySet().iterator(); localIterator.hasNext(); valueIter.hasNext())
/* 206:    */     {
/* 207:365 */       Map.Entry<String, Object> entry = (Map.Entry)localIterator.next();
/* 208:366 */       Object rawValue = entry.getValue();
/* 209:    */       Iterator<Object> valueIter;
/* 210:368 */       if ((rawValue != null) && (rawValue.getClass().isArray()))
/* 211:    */       {
/* 212:369 */         valueIter = Arrays.asList(ObjectUtils.toObjectArray(rawValue)).iterator();
/* 213:    */       }
/* 214:    */       else
/* 215:    */       {
/* 216:    */         Iterator<Object> valueIter;
/* 217:371 */         if ((rawValue instanceof Collection))
/* 218:    */         {
/* 219:372 */           valueIter = ((Collection)rawValue).iterator();
/* 220:    */         }
/* 221:    */         else
/* 222:    */         {
/* 223:375 */           valueIter = Collections.singleton(rawValue).iterator();
/* 224:    */           
/* 225:377 */           continue;
/* 226:378 */           Object value = valueIter.next();
/* 227:379 */           if (first)
/* 228:    */           {
/* 229:380 */             targetUrl.append('?');
/* 230:381 */             first = false;
/* 231:    */           }
/* 232:    */           else
/* 233:    */           {
/* 234:384 */             targetUrl.append('&');
/* 235:    */           }
/* 236:386 */           String encodedKey = urlEncode((String)entry.getKey(), encodingScheme);
/* 237:387 */           String encodedValue = value != null ? urlEncode(value.toString(), encodingScheme) : "";
/* 238:388 */           targetUrl.append(encodedKey).append('=').append(encodedValue);
/* 239:    */         }
/* 240:    */       }
/* 241:    */     }
/* 242:393 */     if (fragment != null) {
/* 243:394 */       targetUrl.append(fragment);
/* 244:    */     }
/* 245:    */   }
/* 246:    */   
/* 247:    */   protected Map<String, Object> queryProperties(Map<String, Object> model)
/* 248:    */   {
/* 249:409 */     Map<String, Object> result = new LinkedHashMap();
/* 250:410 */     for (Map.Entry<String, Object> entry : model.entrySet()) {
/* 251:411 */       if (isEligibleProperty((String)entry.getKey(), entry.getValue())) {
/* 252:412 */         result.put((String)entry.getKey(), entry.getValue());
/* 253:    */       }
/* 254:    */     }
/* 255:415 */     return result;
/* 256:    */   }
/* 257:    */   
/* 258:    */   protected boolean isEligibleProperty(String key, Object value)
/* 259:    */   {
/* 260:429 */     if (value == null) {
/* 261:430 */       return false;
/* 262:    */     }
/* 263:432 */     if (isEligibleValue(value)) {
/* 264:433 */       return true;
/* 265:    */     }
/* 266:    */     Object element;
/* 267:436 */     if (value.getClass().isArray())
/* 268:    */     {
/* 269:437 */       int length = Array.getLength(value);
/* 270:438 */       if (length == 0) {
/* 271:439 */         return false;
/* 272:    */       }
/* 273:441 */       for (int i = 0; i < length; i++)
/* 274:    */       {
/* 275:442 */         element = Array.get(value, i);
/* 276:443 */         if (!isEligibleValue(element)) {
/* 277:444 */           return false;
/* 278:    */         }
/* 279:    */       }
/* 280:447 */       return true;
/* 281:    */     }
/* 282:450 */     if ((value instanceof Collection))
/* 283:    */     {
/* 284:451 */       Collection coll = (Collection)value;
/* 285:452 */       if (coll.isEmpty()) {
/* 286:453 */         return false;
/* 287:    */       }
/* 288:455 */       for (Object element : coll) {
/* 289:456 */         if (!isEligibleValue(element)) {
/* 290:457 */           return false;
/* 291:    */         }
/* 292:    */       }
/* 293:460 */       return true;
/* 294:    */     }
/* 295:463 */     return false;
/* 296:    */   }
/* 297:    */   
/* 298:    */   protected boolean isEligibleValue(Object value)
/* 299:    */   {
/* 300:475 */     return (value != null) && (BeanUtils.isSimpleValueType(value.getClass()));
/* 301:    */   }
/* 302:    */   
/* 303:    */   protected String urlEncode(String input, String encodingScheme)
/* 304:    */     throws UnsupportedEncodingException
/* 305:    */   {
/* 306:489 */     return input != null ? URLEncoder.encode(input, encodingScheme) : null;
/* 307:    */   }
/* 308:    */   
/* 309:    */   protected void sendRedirect(HttpServletRequest request, HttpServletResponse response, String targetUrl, boolean http10Compatible)
/* 310:    */     throws IOException
/* 311:    */   {
/* 312:504 */     String encodedRedirectURL = response.encodeRedirectURL(targetUrl);
/* 313:506 */     if (http10Compatible)
/* 314:    */     {
/* 315:507 */       if (this.statusCode != null)
/* 316:    */       {
/* 317:508 */         response.setStatus(this.statusCode.value());
/* 318:509 */         response.setHeader("Location", encodedRedirectURL);
/* 319:    */       }
/* 320:    */       else
/* 321:    */       {
/* 322:513 */         response.sendRedirect(encodedRedirectURL);
/* 323:    */       }
/* 324:    */     }
/* 325:    */     else
/* 326:    */     {
/* 327:517 */       HttpStatus statusCode = getHttp11StatusCode(request, response, targetUrl);
/* 328:518 */       response.setStatus(statusCode.value());
/* 329:519 */       response.setHeader("Location", encodedRedirectURL);
/* 330:    */     }
/* 331:    */   }
/* 332:    */   
/* 333:    */   protected HttpStatus getHttp11StatusCode(HttpServletRequest request, HttpServletResponse response, String targetUrl)
/* 334:    */   {
/* 335:536 */     if (this.statusCode != null) {
/* 336:537 */       return this.statusCode;
/* 337:    */     }
/* 338:539 */     HttpStatus attributeStatusCode = (HttpStatus)request.getAttribute(View.RESPONSE_STATUS_ATTRIBUTE);
/* 339:540 */     if (attributeStatusCode != null) {
/* 340:541 */       return attributeStatusCode;
/* 341:    */     }
/* 342:543 */     return HttpStatus.SEE_OTHER;
/* 343:    */   }
/* 344:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.RedirectView
 * JD-Core Version:    0.7.0.1
 */