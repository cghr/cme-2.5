/*   1:    */ package org.springframework.web.client;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.UnsupportedEncodingException;
/*   5:    */ import java.net.URI;
/*   6:    */ import java.net.URISyntaxException;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.List;
/*   9:    */ import java.util.Map;
/*  10:    */ import java.util.Set;
/*  11:    */ import org.apache.commons.logging.Log;
/*  12:    */ import org.springframework.http.HttpEntity;
/*  13:    */ import org.springframework.http.HttpHeaders;
/*  14:    */ import org.springframework.http.HttpMethod;
/*  15:    */ import org.springframework.http.MediaType;
/*  16:    */ import org.springframework.http.ResponseEntity;
/*  17:    */ import org.springframework.http.client.ClientHttpRequest;
/*  18:    */ import org.springframework.http.client.ClientHttpRequestFactory;
/*  19:    */ import org.springframework.http.client.ClientHttpResponse;
/*  20:    */ import org.springframework.http.client.support.InterceptingHttpAccessor;
/*  21:    */ import org.springframework.http.converter.ByteArrayHttpMessageConverter;
/*  22:    */ import org.springframework.http.converter.HttpMessageConverter;
/*  23:    */ import org.springframework.http.converter.ResourceHttpMessageConverter;
/*  24:    */ import org.springframework.http.converter.StringHttpMessageConverter;
/*  25:    */ import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter;
/*  26:    */ import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
/*  27:    */ import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
/*  28:    */ import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
/*  29:    */ import org.springframework.http.converter.xml.SourceHttpMessageConverter;
/*  30:    */ import org.springframework.http.converter.xml.XmlAwareFormHttpMessageConverter;
/*  31:    */ import org.springframework.util.Assert;
/*  32:    */ import org.springframework.util.ClassUtils;
/*  33:    */ import org.springframework.web.util.UriTemplate;
/*  34:    */ import org.springframework.web.util.UriUtils;
/*  35:    */ 
/*  36:    */ public class RestTemplate
/*  37:    */   extends InterceptingHttpAccessor
/*  38:    */   implements RestOperations
/*  39:    */ {
/*  40:119 */   private static final boolean jaxb2Present = ClassUtils.isPresent("javax.xml.bind.Binder", RestTemplate.class.getClassLoader());
/*  41:122 */   private static final boolean jacksonPresent = (ClassUtils.isPresent("org.codehaus.jackson.map.ObjectMapper", RestTemplate.class.getClassLoader())) && 
/*  42:123 */     (ClassUtils.isPresent("org.codehaus.jackson.JsonGenerator", RestTemplate.class.getClassLoader()));
/*  43:126 */   private static boolean romePresent = ClassUtils.isPresent("com.sun.syndication.feed.WireFeed", RestTemplate.class.getClassLoader());
/*  44:129 */   private final ResponseExtractor<HttpHeaders> headersExtractor = new HeadersExtractor(null);
/*  45:131 */   private List<HttpMessageConverter<?>> messageConverters = new ArrayList();
/*  46:133 */   private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();
/*  47:    */   
/*  48:    */   public RestTemplate()
/*  49:    */   {
/*  50:138 */     this.messageConverters.add(new ByteArrayHttpMessageConverter());
/*  51:139 */     this.messageConverters.add(new StringHttpMessageConverter());
/*  52:140 */     this.messageConverters.add(new ResourceHttpMessageConverter());
/*  53:141 */     this.messageConverters.add(new SourceHttpMessageConverter());
/*  54:142 */     this.messageConverters.add(new XmlAwareFormHttpMessageConverter());
/*  55:143 */     if (jaxb2Present) {
/*  56:144 */       this.messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
/*  57:    */     }
/*  58:146 */     if (jacksonPresent) {
/*  59:147 */       this.messageConverters.add(new MappingJacksonHttpMessageConverter());
/*  60:    */     }
/*  61:149 */     if (romePresent)
/*  62:    */     {
/*  63:150 */       this.messageConverters.add(new AtomFeedHttpMessageConverter());
/*  64:151 */       this.messageConverters.add(new RssChannelHttpMessageConverter());
/*  65:    */     }
/*  66:    */   }
/*  67:    */   
/*  68:    */   public RestTemplate(ClientHttpRequestFactory requestFactory)
/*  69:    */   {
/*  70:162 */     this();
/*  71:163 */     setRequestFactory(requestFactory);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters)
/*  75:    */   {
/*  76:172 */     Assert.notEmpty(messageConverters, "'messageConverters' must not be empty");
/*  77:173 */     this.messageConverters = messageConverters;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public List<HttpMessageConverter<?>> getMessageConverters()
/*  81:    */   {
/*  82:178 */     return this.messageConverters;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void setErrorHandler(ResponseErrorHandler errorHandler)
/*  86:    */   {
/*  87:183 */     Assert.notNull(errorHandler, "'errorHandler' must not be null");
/*  88:184 */     this.errorHandler = errorHandler;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public ResponseErrorHandler getErrorHandler()
/*  92:    */   {
/*  93:189 */     return this.errorHandler;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public <T> T getForObject(String url, Class<T> responseType, Object... urlVariables)
/*  97:    */     throws RestClientException
/*  98:    */   {
/*  99:196 */     AcceptHeaderRequestCallback requestCallback = new AcceptHeaderRequestCallback(responseType, null, null);
/* 100:197 */     HttpMessageConverterExtractor<T> responseExtractor = 
/* 101:198 */       new HttpMessageConverterExtractor(responseType, getMessageConverters(), this.logger);
/* 102:199 */     return execute(url, HttpMethod.GET, requestCallback, responseExtractor, urlVariables);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public <T> T getForObject(String url, Class<T> responseType, Map<String, ?> urlVariables)
/* 106:    */     throws RestClientException
/* 107:    */   {
/* 108:203 */     AcceptHeaderRequestCallback requestCallback = new AcceptHeaderRequestCallback(responseType, null, null);
/* 109:204 */     HttpMessageConverterExtractor<T> responseExtractor = 
/* 110:205 */       new HttpMessageConverterExtractor(responseType, getMessageConverters(), this.logger);
/* 111:206 */     return execute(url, HttpMethod.GET, requestCallback, responseExtractor, urlVariables);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public <T> T getForObject(URI url, Class<T> responseType)
/* 115:    */     throws RestClientException
/* 116:    */   {
/* 117:210 */     AcceptHeaderRequestCallback requestCallback = new AcceptHeaderRequestCallback(responseType, null, null);
/* 118:211 */     HttpMessageConverterExtractor<T> responseExtractor = 
/* 119:212 */       new HttpMessageConverterExtractor(responseType, getMessageConverters(), this.logger);
/* 120:213 */     return execute(url, HttpMethod.GET, requestCallback, responseExtractor);
/* 121:    */   }
/* 122:    */   
/* 123:    */   public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... urlVariables)
/* 124:    */     throws RestClientException
/* 125:    */   {
/* 126:218 */     AcceptHeaderRequestCallback requestCallback = new AcceptHeaderRequestCallback(responseType, null, null);
/* 127:219 */     ResponseEntityResponseExtractor<T> responseExtractor = 
/* 128:220 */       new ResponseEntityResponseExtractor(responseType);
/* 129:221 */     return (ResponseEntity)execute(url, HttpMethod.GET, requestCallback, responseExtractor, urlVariables);
/* 130:    */   }
/* 131:    */   
/* 132:    */   public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Map<String, ?> urlVariables)
/* 133:    */     throws RestClientException
/* 134:    */   {
/* 135:226 */     AcceptHeaderRequestCallback requestCallback = new AcceptHeaderRequestCallback(responseType, null, null);
/* 136:227 */     ResponseEntityResponseExtractor<T> responseExtractor = 
/* 137:228 */       new ResponseEntityResponseExtractor(responseType);
/* 138:229 */     return (ResponseEntity)execute(url, HttpMethod.GET, requestCallback, responseExtractor, urlVariables);
/* 139:    */   }
/* 140:    */   
/* 141:    */   public <T> ResponseEntity<T> getForEntity(URI url, Class<T> responseType)
/* 142:    */     throws RestClientException
/* 143:    */   {
/* 144:233 */     AcceptHeaderRequestCallback requestCallback = new AcceptHeaderRequestCallback(responseType, null, null);
/* 145:234 */     ResponseEntityResponseExtractor<T> responseExtractor = 
/* 146:235 */       new ResponseEntityResponseExtractor(responseType);
/* 147:236 */     return (ResponseEntity)execute(url, HttpMethod.GET, requestCallback, responseExtractor);
/* 148:    */   }
/* 149:    */   
/* 150:    */   public HttpHeaders headForHeaders(String url, Object... urlVariables)
/* 151:    */     throws RestClientException
/* 152:    */   {
/* 153:242 */     return (HttpHeaders)execute(url, HttpMethod.HEAD, null, this.headersExtractor, urlVariables);
/* 154:    */   }
/* 155:    */   
/* 156:    */   public HttpHeaders headForHeaders(String url, Map<String, ?> urlVariables)
/* 157:    */     throws RestClientException
/* 158:    */   {
/* 159:246 */     return (HttpHeaders)execute(url, HttpMethod.HEAD, null, this.headersExtractor, urlVariables);
/* 160:    */   }
/* 161:    */   
/* 162:    */   public HttpHeaders headForHeaders(URI url)
/* 163:    */     throws RestClientException
/* 164:    */   {
/* 165:250 */     return (HttpHeaders)execute(url, HttpMethod.HEAD, null, this.headersExtractor);
/* 166:    */   }
/* 167:    */   
/* 168:    */   public URI postForLocation(String url, Object request, Object... urlVariables)
/* 169:    */     throws RestClientException
/* 170:    */   {
/* 171:256 */     HttpEntityRequestCallback requestCallback = new HttpEntityRequestCallback(request, null);
/* 172:257 */     HttpHeaders headers = (HttpHeaders)execute(url, HttpMethod.POST, requestCallback, this.headersExtractor, urlVariables);
/* 173:258 */     return headers.getLocation();
/* 174:    */   }
/* 175:    */   
/* 176:    */   public URI postForLocation(String url, Object request, Map<String, ?> urlVariables)
/* 177:    */     throws RestClientException
/* 178:    */   {
/* 179:263 */     HttpEntityRequestCallback requestCallback = new HttpEntityRequestCallback(request, null);
/* 180:264 */     HttpHeaders headers = (HttpHeaders)execute(url, HttpMethod.POST, requestCallback, this.headersExtractor, urlVariables);
/* 181:265 */     return headers.getLocation();
/* 182:    */   }
/* 183:    */   
/* 184:    */   public URI postForLocation(URI url, Object request)
/* 185:    */     throws RestClientException
/* 186:    */   {
/* 187:269 */     HttpEntityRequestCallback requestCallback = new HttpEntityRequestCallback(request, null);
/* 188:270 */     HttpHeaders headers = (HttpHeaders)execute(url, HttpMethod.POST, requestCallback, this.headersExtractor);
/* 189:271 */     return headers.getLocation();
/* 190:    */   }
/* 191:    */   
/* 192:    */   public <T> T postForObject(String url, Object request, Class<T> responseType, Object... uriVariables)
/* 193:    */     throws RestClientException
/* 194:    */   {
/* 195:276 */     HttpEntityRequestCallback requestCallback = new HttpEntityRequestCallback(request, responseType, null);
/* 196:277 */     HttpMessageConverterExtractor<T> responseExtractor = 
/* 197:278 */       new HttpMessageConverterExtractor(responseType, getMessageConverters(), this.logger);
/* 198:279 */     return execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables);
/* 199:    */   }
/* 200:    */   
/* 201:    */   public <T> T postForObject(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables)
/* 202:    */     throws RestClientException
/* 203:    */   {
/* 204:284 */     HttpEntityRequestCallback requestCallback = new HttpEntityRequestCallback(request, responseType, null);
/* 205:285 */     HttpMessageConverterExtractor<T> responseExtractor = 
/* 206:286 */       new HttpMessageConverterExtractor(responseType, getMessageConverters(), this.logger);
/* 207:287 */     return execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables);
/* 208:    */   }
/* 209:    */   
/* 210:    */   public <T> T postForObject(URI url, Object request, Class<T> responseType)
/* 211:    */     throws RestClientException
/* 212:    */   {
/* 213:291 */     HttpEntityRequestCallback requestCallback = new HttpEntityRequestCallback(request, responseType, null);
/* 214:292 */     HttpMessageConverterExtractor<T> responseExtractor = 
/* 215:293 */       new HttpMessageConverterExtractor(responseType, getMessageConverters());
/* 216:294 */     return execute(url, HttpMethod.POST, requestCallback, responseExtractor);
/* 217:    */   }
/* 218:    */   
/* 219:    */   public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType, Object... uriVariables)
/* 220:    */     throws RestClientException
/* 221:    */   {
/* 222:299 */     HttpEntityRequestCallback requestCallback = new HttpEntityRequestCallback(request, responseType, null);
/* 223:300 */     ResponseEntityResponseExtractor<T> responseExtractor = 
/* 224:301 */       new ResponseEntityResponseExtractor(responseType);
/* 225:302 */     return (ResponseEntity)execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables);
/* 226:    */   }
/* 227:    */   
/* 228:    */   public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables)
/* 229:    */     throws RestClientException
/* 230:    */   {
/* 231:310 */     HttpEntityRequestCallback requestCallback = new HttpEntityRequestCallback(request, responseType, null);
/* 232:311 */     ResponseEntityResponseExtractor<T> responseExtractor = 
/* 233:312 */       new ResponseEntityResponseExtractor(responseType);
/* 234:313 */     return (ResponseEntity)execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables);
/* 235:    */   }
/* 236:    */   
/* 237:    */   public <T> ResponseEntity<T> postForEntity(URI url, Object request, Class<T> responseType)
/* 238:    */     throws RestClientException
/* 239:    */   {
/* 240:317 */     HttpEntityRequestCallback requestCallback = new HttpEntityRequestCallback(request, responseType, null);
/* 241:318 */     ResponseEntityResponseExtractor<T> responseExtractor = 
/* 242:319 */       new ResponseEntityResponseExtractor(responseType);
/* 243:320 */     return (ResponseEntity)execute(url, HttpMethod.POST, requestCallback, responseExtractor);
/* 244:    */   }
/* 245:    */   
/* 246:    */   public void put(String url, Object request, Object... urlVariables)
/* 247:    */     throws RestClientException
/* 248:    */   {
/* 249:326 */     HttpEntityRequestCallback requestCallback = new HttpEntityRequestCallback(request, null);
/* 250:327 */     execute(url, HttpMethod.PUT, requestCallback, null, urlVariables);
/* 251:    */   }
/* 252:    */   
/* 253:    */   public void put(String url, Object request, Map<String, ?> urlVariables)
/* 254:    */     throws RestClientException
/* 255:    */   {
/* 256:331 */     HttpEntityRequestCallback requestCallback = new HttpEntityRequestCallback(request, null);
/* 257:332 */     execute(url, HttpMethod.PUT, requestCallback, null, urlVariables);
/* 258:    */   }
/* 259:    */   
/* 260:    */   public void put(URI url, Object request)
/* 261:    */     throws RestClientException
/* 262:    */   {
/* 263:336 */     HttpEntityRequestCallback requestCallback = new HttpEntityRequestCallback(request, null);
/* 264:337 */     execute(url, HttpMethod.PUT, requestCallback, null);
/* 265:    */   }
/* 266:    */   
/* 267:    */   public void delete(String url, Object... urlVariables)
/* 268:    */     throws RestClientException
/* 269:    */   {
/* 270:343 */     execute(url, HttpMethod.DELETE, null, null, urlVariables);
/* 271:    */   }
/* 272:    */   
/* 273:    */   public void delete(String url, Map<String, ?> urlVariables)
/* 274:    */     throws RestClientException
/* 275:    */   {
/* 276:347 */     execute(url, HttpMethod.DELETE, null, null, urlVariables);
/* 277:    */   }
/* 278:    */   
/* 279:    */   public void delete(URI url)
/* 280:    */     throws RestClientException
/* 281:    */   {
/* 282:351 */     execute(url, HttpMethod.DELETE, null, null);
/* 283:    */   }
/* 284:    */   
/* 285:    */   public Set<HttpMethod> optionsForAllow(String url, Object... urlVariables)
/* 286:    */     throws RestClientException
/* 287:    */   {
/* 288:357 */     HttpHeaders headers = (HttpHeaders)execute(url, HttpMethod.OPTIONS, null, this.headersExtractor, urlVariables);
/* 289:358 */     return headers.getAllow();
/* 290:    */   }
/* 291:    */   
/* 292:    */   public Set<HttpMethod> optionsForAllow(String url, Map<String, ?> urlVariables)
/* 293:    */     throws RestClientException
/* 294:    */   {
/* 295:362 */     HttpHeaders headers = (HttpHeaders)execute(url, HttpMethod.OPTIONS, null, this.headersExtractor, urlVariables);
/* 296:363 */     return headers.getAllow();
/* 297:    */   }
/* 298:    */   
/* 299:    */   public Set<HttpMethod> optionsForAllow(URI url)
/* 300:    */     throws RestClientException
/* 301:    */   {
/* 302:367 */     HttpHeaders headers = (HttpHeaders)execute(url, HttpMethod.OPTIONS, null, this.headersExtractor);
/* 303:368 */     return headers.getAllow();
/* 304:    */   }
/* 305:    */   
/* 306:    */   public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables)
/* 307:    */     throws RestClientException
/* 308:    */   {
/* 309:375 */     HttpEntityRequestCallback requestCallback = new HttpEntityRequestCallback(requestEntity, responseType, null);
/* 310:376 */     ResponseEntityResponseExtractor<T> responseExtractor = new ResponseEntityResponseExtractor(responseType);
/* 311:377 */     return (ResponseEntity)execute(url, method, requestCallback, responseExtractor, uriVariables);
/* 312:    */   }
/* 313:    */   
/* 314:    */   public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables)
/* 315:    */     throws RestClientException
/* 316:    */   {
/* 317:382 */     HttpEntityRequestCallback requestCallback = new HttpEntityRequestCallback(requestEntity, responseType, null);
/* 318:383 */     ResponseEntityResponseExtractor<T> responseExtractor = new ResponseEntityResponseExtractor(responseType);
/* 319:384 */     return (ResponseEntity)execute(url, method, requestCallback, responseExtractor, uriVariables);
/* 320:    */   }
/* 321:    */   
/* 322:    */   public <T> ResponseEntity<T> exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType)
/* 323:    */     throws RestClientException
/* 324:    */   {
/* 325:389 */     HttpEntityRequestCallback requestCallback = new HttpEntityRequestCallback(requestEntity, responseType, null);
/* 326:390 */     ResponseEntityResponseExtractor<T> responseExtractor = new ResponseEntityResponseExtractor(responseType);
/* 327:391 */     return (ResponseEntity)execute(url, method, requestCallback, responseExtractor);
/* 328:    */   }
/* 329:    */   
/* 330:    */   public <T> T execute(String url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Object... urlVariables)
/* 331:    */     throws RestClientException
/* 332:    */   {
/* 333:399 */     UriTemplate uriTemplate = new HttpUrlTemplate(url);
/* 334:400 */     URI expanded = uriTemplate.expand(urlVariables);
/* 335:401 */     return doExecute(expanded, method, requestCallback, responseExtractor);
/* 336:    */   }
/* 337:    */   
/* 338:    */   public <T> T execute(String url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Map<String, ?> urlVariables)
/* 339:    */     throws RestClientException
/* 340:    */   {
/* 341:407 */     UriTemplate uriTemplate = new HttpUrlTemplate(url);
/* 342:408 */     URI expanded = uriTemplate.expand(urlVariables);
/* 343:409 */     return doExecute(expanded, method, requestCallback, responseExtractor);
/* 344:    */   }
/* 345:    */   
/* 346:    */   public <T> T execute(URI url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor)
/* 347:    */     throws RestClientException
/* 348:    */   {
/* 349:415 */     return doExecute(url, method, requestCallback, responseExtractor);
/* 350:    */   }
/* 351:    */   
/* 352:    */   protected <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor)
/* 353:    */     throws RestClientException
/* 354:    */   {
/* 355:430 */     Assert.notNull(url, "'url' must not be null");
/* 356:431 */     Assert.notNull(method, "'method' must not be null");
/* 357:432 */     ClientHttpResponse response = null;
/* 358:    */     try
/* 359:    */     {
/* 360:434 */       ClientHttpRequest request = createRequest(url, method);
/* 361:435 */       if (requestCallback != null) {
/* 362:436 */         requestCallback.doWithRequest(request);
/* 363:    */       }
/* 364:438 */       response = request.execute();
/* 365:439 */       if (!getErrorHandler().hasError(response)) {
/* 366:440 */         logResponseStatus(method, url, response);
/* 367:    */       } else {
/* 368:443 */         handleResponseError(method, url, response);
/* 369:    */       }
/* 370:445 */       if (responseExtractor != null) {
/* 371:446 */         return responseExtractor.extractData(response);
/* 372:    */       }
/* 373:    */       ClientHttpRequest request;
/* 374:449 */       return null;
/* 375:    */     }
/* 376:    */     catch (IOException ex)
/* 377:    */     {
/* 378:453 */       throw new ResourceAccessException("I/O error: " + ex.getMessage(), ex);
/* 379:    */     }
/* 380:    */     finally
/* 381:    */     {
/* 382:456 */       if (response != null) {
/* 383:457 */         response.close();
/* 384:    */       }
/* 385:    */     }
/* 386:    */   }
/* 387:    */   
/* 388:    */   private void logResponseStatus(HttpMethod method, URI url, ClientHttpResponse response)
/* 389:    */   {
/* 390:463 */     if (this.logger.isDebugEnabled()) {
/* 391:    */       try
/* 392:    */       {
/* 393:465 */         this.logger.debug(
/* 394:466 */           method.name() + " request for \"" + url + "\" resulted in " + response.getStatusCode() + " (" + 
/* 395:467 */           response.getStatusText() + ")");
/* 396:    */       }
/* 397:    */       catch (IOException localIOException) {}
/* 398:    */     }
/* 399:    */   }
/* 400:    */   
/* 401:    */   private void handleResponseError(HttpMethod method, URI url, ClientHttpResponse response)
/* 402:    */     throws IOException
/* 403:    */   {
/* 404:476 */     if (this.logger.isWarnEnabled()) {
/* 405:    */       try
/* 406:    */       {
/* 407:478 */         this.logger.warn(
/* 408:479 */           method.name() + " request for \"" + url + "\" resulted in " + response.getStatusCode() + " (" + 
/* 409:480 */           response.getStatusText() + "); invoking error handler");
/* 410:    */       }
/* 411:    */       catch (IOException localIOException) {}
/* 412:    */     }
/* 413:486 */     getErrorHandler().handleError(response);
/* 414:    */   }
/* 415:    */   
/* 416:    */   private class AcceptHeaderRequestCallback
/* 417:    */     implements RequestCallback
/* 418:    */   {
/* 419:    */     private final Class<?> responseType;
/* 420:    */     
/* 421:    */     private AcceptHeaderRequestCallback()
/* 422:    */     {
/* 423:498 */       this.responseType = responseType;
/* 424:    */     }
/* 425:    */     
/* 426:    */     public void doWithRequest(ClientHttpRequest request)
/* 427:    */       throws IOException
/* 428:    */     {
/* 429:503 */       if (this.responseType != null)
/* 430:    */       {
/* 431:504 */         List<MediaType> allSupportedMediaTypes = new ArrayList();
/* 432:505 */         for (HttpMessageConverter<?> messageConverter : RestTemplate.this.getMessageConverters()) {
/* 433:506 */           if (messageConverter.canRead(this.responseType, null))
/* 434:    */           {
/* 435:507 */             List<MediaType> supportedMediaTypes = messageConverter.getSupportedMediaTypes();
/* 436:508 */             for (MediaType supportedMediaType : supportedMediaTypes)
/* 437:    */             {
/* 438:509 */               if (supportedMediaType.getCharSet() != null) {
/* 439:510 */                 supportedMediaType = 
/* 440:511 */                   new MediaType(supportedMediaType.getType(), supportedMediaType.getSubtype());
/* 441:    */               }
/* 442:513 */               allSupportedMediaTypes.add(supportedMediaType);
/* 443:    */             }
/* 444:    */           }
/* 445:    */         }
/* 446:517 */         if (!allSupportedMediaTypes.isEmpty())
/* 447:    */         {
/* 448:518 */           MediaType.sortBySpecificity(allSupportedMediaTypes);
/* 449:519 */           if (RestTemplate.access$0(RestTemplate.this).isDebugEnabled()) {
/* 450:520 */             RestTemplate.access$0(RestTemplate.this).debug("Setting request Accept header to " + allSupportedMediaTypes);
/* 451:    */           }
/* 452:522 */           request.getHeaders().setAccept(allSupportedMediaTypes);
/* 453:    */         }
/* 454:    */       }
/* 455:    */     }
/* 456:    */   }
/* 457:    */   
/* 458:    */   private class HttpEntityRequestCallback
/* 459:    */     extends RestTemplate.AcceptHeaderRequestCallback
/* 460:    */   {
/* 461:    */     private final HttpEntity requestEntity;
/* 462:    */     
/* 463:    */     private HttpEntityRequestCallback(Object requestBody)
/* 464:    */     {
/* 465:537 */       this(requestBody, null);
/* 466:    */     }
/* 467:    */     
/* 468:    */     private HttpEntityRequestCallback(Class<?> requestBody)
/* 469:    */     {
/* 470:542 */       super(responseType, null);
/* 471:543 */       if ((requestBody instanceof HttpEntity)) {
/* 472:544 */         this.requestEntity = ((HttpEntity)requestBody);
/* 473:546 */       } else if (requestBody != null) {
/* 474:547 */         this.requestEntity = new HttpEntity(requestBody);
/* 475:    */       } else {
/* 476:550 */         this.requestEntity = HttpEntity.EMPTY;
/* 477:    */       }
/* 478:    */     }
/* 479:    */     
/* 480:    */     public void doWithRequest(ClientHttpRequest httpRequest)
/* 481:    */       throws IOException
/* 482:    */     {
/* 483:557 */       super.doWithRequest(httpRequest);
/* 484:558 */       if (!this.requestEntity.hasBody())
/* 485:    */       {
/* 486:559 */         HttpHeaders httpHeaders = httpRequest.getHeaders();
/* 487:560 */         HttpHeaders requestHeaders = this.requestEntity.getHeaders();
/* 488:561 */         if (!requestHeaders.isEmpty()) {
/* 489:562 */           httpHeaders.putAll(requestHeaders);
/* 490:    */         }
/* 491:564 */         if (httpHeaders.getContentLength() == -1L) {
/* 492:565 */           httpHeaders.setContentLength(0L);
/* 493:    */         }
/* 494:    */       }
/* 495:    */       else
/* 496:    */       {
/* 497:569 */         Object requestBody = this.requestEntity.getBody();
/* 498:570 */         Class<?> requestType = requestBody.getClass();
/* 499:571 */         HttpHeaders requestHeaders = this.requestEntity.getHeaders();
/* 500:572 */         MediaType requestContentType = requestHeaders.getContentType();
/* 501:573 */         for (HttpMessageConverter messageConverter : RestTemplate.this.getMessageConverters()) {
/* 502:574 */           if (messageConverter.canWrite(requestType, requestContentType))
/* 503:    */           {
/* 504:575 */             if (!requestHeaders.isEmpty()) {
/* 505:576 */               httpRequest.getHeaders().putAll(requestHeaders);
/* 506:    */             }
/* 507:578 */             if (RestTemplate.access$0(RestTemplate.this).isDebugEnabled()) {
/* 508:579 */               if (requestContentType != null) {
/* 509:580 */                 RestTemplate.access$0(RestTemplate.this).debug("Writing [" + requestBody + "] as \"" + requestContentType + 
/* 510:581 */                   "\" using [" + messageConverter + "]");
/* 511:    */               } else {
/* 512:584 */                 RestTemplate.access$0(RestTemplate.this).debug("Writing [" + requestBody + "] using [" + messageConverter + "]");
/* 513:    */               }
/* 514:    */             }
/* 515:588 */             messageConverter.write(requestBody, requestContentType, httpRequest);
/* 516:589 */             return;
/* 517:    */           }
/* 518:    */         }
/* 519:592 */         String message = "Could not write request: no suitable HttpMessageConverter found for request type [" + 
/* 520:593 */           requestType.getName() + "]";
/* 521:594 */         if (requestContentType != null) {
/* 522:595 */           message = message + " and content type [" + requestContentType + "]";
/* 523:    */         }
/* 524:597 */         throw new RestClientException(message);
/* 525:    */       }
/* 526:    */     }
/* 527:    */   }
/* 528:    */   
/* 529:    */   private class ResponseEntityResponseExtractor<T>
/* 530:    */     implements ResponseExtractor<ResponseEntity<T>>
/* 531:    */   {
/* 532:    */     private final HttpMessageConverterExtractor<T> delegate;
/* 533:    */     
/* 534:    */     public ResponseEntityResponseExtractor()
/* 535:    */     {
/* 536:610 */       if (responseType != null) {
/* 537:611 */         this.delegate = new HttpMessageConverterExtractor(responseType, RestTemplate.this.getMessageConverters(), RestTemplate.access$0(RestTemplate.this));
/* 538:    */       } else {
/* 539:613 */         this.delegate = null;
/* 540:    */       }
/* 541:    */     }
/* 542:    */     
/* 543:    */     public ResponseEntity<T> extractData(ClientHttpResponse response)
/* 544:    */       throws IOException
/* 545:    */     {
/* 546:618 */       if (this.delegate != null)
/* 547:    */       {
/* 548:619 */         T body = this.delegate.extractData(response);
/* 549:620 */         return new ResponseEntity(body, response.getHeaders(), response.getStatusCode());
/* 550:    */       }
/* 551:623 */       return new ResponseEntity(response.getHeaders(), response.getStatusCode());
/* 552:    */     }
/* 553:    */   }
/* 554:    */   
/* 555:    */   private static class HeadersExtractor
/* 556:    */     implements ResponseExtractor<HttpHeaders>
/* 557:    */   {
/* 558:    */     public HttpHeaders extractData(ClientHttpResponse response)
/* 559:    */       throws IOException
/* 560:    */     {
/* 561:635 */       return response.getHeaders();
/* 562:    */     }
/* 563:    */   }
/* 564:    */   
/* 565:    */   private static class HttpUrlTemplate
/* 566:    */     extends UriTemplate
/* 567:    */   {
/* 568:    */     public HttpUrlTemplate(String uriTemplate)
/* 569:    */     {
/* 570:645 */       super();
/* 571:    */     }
/* 572:    */     
/* 573:    */     protected URI encodeUri(String uri)
/* 574:    */     {
/* 575:    */       try
/* 576:    */       {
/* 577:651 */         String encoded = UriUtils.encodeHttpUrl(uri, "UTF-8");
/* 578:652 */         return new URI(encoded);
/* 579:    */       }
/* 580:    */       catch (UnsupportedEncodingException ex)
/* 581:    */       {
/* 582:656 */         throw new IllegalStateException(ex);
/* 583:    */       }
/* 584:    */       catch (URISyntaxException ex)
/* 585:    */       {
/* 586:659 */         throw new IllegalArgumentException("Could not create HTTP URL from [" + uri + "]: " + ex, ex);
/* 587:    */       }
/* 588:    */     }
/* 589:    */   }
/* 590:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.client.RestTemplate
 * JD-Core Version:    0.7.0.1
 */