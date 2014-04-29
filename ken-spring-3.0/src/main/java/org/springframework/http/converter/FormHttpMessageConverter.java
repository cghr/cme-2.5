/*   1:    */ package org.springframework.http.converter;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStreamReader;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.io.UnsupportedEncodingException;
/*   7:    */ import java.net.URLDecoder;
/*   8:    */ import java.net.URLEncoder;
/*   9:    */ import java.nio.charset.Charset;
/*  10:    */ import java.util.ArrayList;
/*  11:    */ import java.util.Collections;
/*  12:    */ import java.util.Iterator;
/*  13:    */ import java.util.List;
/*  14:    */ import java.util.Map;
/*  15:    */ import java.util.Map.Entry;
/*  16:    */ import java.util.Random;
/*  17:    */ import java.util.Set;
/*  18:    */ import org.springframework.core.io.Resource;
/*  19:    */ import org.springframework.http.HttpEntity;
/*  20:    */ import org.springframework.http.HttpHeaders;
/*  21:    */ import org.springframework.http.HttpInputMessage;
/*  22:    */ import org.springframework.http.HttpOutputMessage;
/*  23:    */ import org.springframework.http.MediaType;
/*  24:    */ import org.springframework.util.Assert;
/*  25:    */ import org.springframework.util.FileCopyUtils;
/*  26:    */ import org.springframework.util.LinkedMultiValueMap;
/*  27:    */ import org.springframework.util.MultiValueMap;
/*  28:    */ import org.springframework.util.StringUtils;
/*  29:    */ 
/*  30:    */ public class FormHttpMessageConverter
/*  31:    */   implements HttpMessageConverter<MultiValueMap<String, ?>>
/*  32:    */ {
/*  33: 76 */   private static final byte[] BOUNDARY_CHARS = { 45, 95, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 97, 98, 99, 100, 101, 102, 103, 
/*  34: 77 */     104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 65, 
/*  35: 78 */     66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 
/*  36: 79 */     86, 87, 88, 89, 90 };
/*  37: 81 */   private final Random rnd = new Random();
/*  38: 83 */   private Charset charset = Charset.forName("UTF-8");
/*  39: 85 */   private List<MediaType> supportedMediaTypes = new ArrayList();
/*  40: 87 */   private List<HttpMessageConverter<?>> partConverters = new ArrayList();
/*  41:    */   
/*  42:    */   public FormHttpMessageConverter()
/*  43:    */   {
/*  44: 91 */     this.supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
/*  45: 92 */     this.supportedMediaTypes.add(MediaType.MULTIPART_FORM_DATA);
/*  46:    */     
/*  47: 94 */     this.partConverters.add(new ByteArrayHttpMessageConverter());
/*  48: 95 */     StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
/*  49: 96 */     stringHttpMessageConverter.setWriteAcceptCharset(false);
/*  50: 97 */     this.partConverters.add(stringHttpMessageConverter);
/*  51: 98 */     this.partConverters.add(new ResourceHttpMessageConverter());
/*  52:    */   }
/*  53:    */   
/*  54:    */   public final void setPartConverters(List<HttpMessageConverter<?>> partConverters)
/*  55:    */   {
/*  56:106 */     Assert.notEmpty(partConverters, "'partConverters' must not be empty");
/*  57:107 */     this.partConverters = partConverters;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public final void addPartConverter(HttpMessageConverter<?> partConverter)
/*  61:    */   {
/*  62:114 */     Assert.notNull(partConverter, "'partConverter' must not be NULL");
/*  63:115 */     this.partConverters.add(partConverter);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void setCharset(Charset charset)
/*  67:    */   {
/*  68:122 */     this.charset = charset;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public boolean canRead(Class<?> clazz, MediaType mediaType)
/*  72:    */   {
/*  73:126 */     if (!MultiValueMap.class.isAssignableFrom(clazz)) {
/*  74:127 */       return false;
/*  75:    */     }
/*  76:129 */     if (mediaType == null) {
/*  77:130 */       return true;
/*  78:    */     }
/*  79:132 */     for (MediaType supportedMediaType : getSupportedMediaTypes()) {
/*  80:134 */       if ((!supportedMediaType.equals(MediaType.MULTIPART_FORM_DATA)) && 
/*  81:135 */         (supportedMediaType.includes(mediaType))) {
/*  82:136 */         return true;
/*  83:    */       }
/*  84:    */     }
/*  85:139 */     return false;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public boolean canWrite(Class<?> clazz, MediaType mediaType)
/*  89:    */   {
/*  90:143 */     if (!MultiValueMap.class.isAssignableFrom(clazz)) {
/*  91:144 */       return false;
/*  92:    */     }
/*  93:146 */     if ((mediaType == null) || (MediaType.ALL.equals(mediaType))) {
/*  94:147 */       return true;
/*  95:    */     }
/*  96:149 */     for (MediaType supportedMediaType : getSupportedMediaTypes()) {
/*  97:150 */       if (supportedMediaType.isCompatibleWith(mediaType)) {
/*  98:151 */         return true;
/*  99:    */       }
/* 100:    */     }
/* 101:154 */     return false;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes)
/* 105:    */   {
/* 106:161 */     this.supportedMediaTypes = supportedMediaTypes;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public List<MediaType> getSupportedMediaTypes()
/* 110:    */   {
/* 111:165 */     return Collections.unmodifiableList(this.supportedMediaTypes);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public MultiValueMap<String, String> read(Class<? extends MultiValueMap<String, ?>> clazz, HttpInputMessage inputMessage)
/* 115:    */     throws IOException, HttpMessageNotReadableException
/* 116:    */   {
/* 117:171 */     MediaType contentType = inputMessage.getHeaders().getContentType();
/* 118:172 */     Charset charset = contentType.getCharSet() != null ? contentType.getCharSet() : this.charset;
/* 119:173 */     String body = FileCopyUtils.copyToString(new InputStreamReader(inputMessage.getBody(), charset));
/* 120:    */     
/* 121:175 */     String[] pairs = StringUtils.tokenizeToStringArray(body, "&");
/* 122:    */     
/* 123:177 */     MultiValueMap<String, String> result = new LinkedMultiValueMap(pairs.length);
/* 124:179 */     for (String pair : pairs)
/* 125:    */     {
/* 126:180 */       int idx = pair.indexOf('=');
/* 127:181 */       if (idx == -1)
/* 128:    */       {
/* 129:182 */         result.add(URLDecoder.decode(pair, charset.name()), null);
/* 130:    */       }
/* 131:    */       else
/* 132:    */       {
/* 133:185 */         String name = URLDecoder.decode(pair.substring(0, idx), charset.name());
/* 134:186 */         String value = URLDecoder.decode(pair.substring(idx + 1), charset.name());
/* 135:187 */         result.add(name, value);
/* 136:    */       }
/* 137:    */     }
/* 138:190 */     return result;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void write(MultiValueMap<String, ?> map, MediaType contentType, HttpOutputMessage outputMessage)
/* 142:    */     throws IOException, HttpMessageNotWritableException
/* 143:    */   {
/* 144:196 */     if (!isMultipart(map, contentType)) {
/* 145:197 */       writeForm(map, contentType, outputMessage);
/* 146:    */     } else {
/* 147:200 */       writeMultipart(map, outputMessage);
/* 148:    */     }
/* 149:    */   }
/* 150:    */   
/* 151:    */   private boolean isMultipart(MultiValueMap<String, ?> map, MediaType contentType)
/* 152:    */   {
/* 153:205 */     if (contentType != null) {
/* 154:206 */       return MediaType.MULTIPART_FORM_DATA.equals(contentType);
/* 155:    */     }
/* 156:    */     Iterator localIterator2;
/* 157:208 */     for (Iterator localIterator1 = map.keySet().iterator(); localIterator1.hasNext(); localIterator2.hasNext())
/* 158:    */     {
/* 159:208 */       String name = (String)localIterator1.next();
/* 160:209 */       localIterator2 = ((List)map.get(name)).iterator(); continue;Object value = localIterator2.next();
/* 161:210 */       if ((value != null) && (!(value instanceof String))) {
/* 162:211 */         return true;
/* 163:    */       }
/* 164:    */     }
/* 165:215 */     return false;
/* 166:    */   }
/* 167:    */   
/* 168:    */   private void writeForm(MultiValueMap<String, String> form, MediaType contentType, HttpOutputMessage outputMessage)
/* 169:    */     throws IOException
/* 170:    */   {
/* 171:    */     Charset charset;
/* 172:    */     Charset charset;
/* 173:221 */     if (contentType != null)
/* 174:    */     {
/* 175:222 */       outputMessage.getHeaders().setContentType(contentType);
/* 176:223 */       charset = contentType.getCharSet() != null ? contentType.getCharSet() : this.charset;
/* 177:    */     }
/* 178:    */     else
/* 179:    */     {
/* 180:226 */       outputMessage.getHeaders().setContentType(MediaType.APPLICATION_FORM_URLENCODED);
/* 181:227 */       charset = this.charset;
/* 182:    */     }
/* 183:229 */     StringBuilder builder = new StringBuilder();
/* 184:230 */     for (Iterator<String> nameIterator = form.keySet().iterator(); nameIterator.hasNext();)
/* 185:    */     {
/* 186:231 */       String name = (String)nameIterator.next();
/* 187:232 */       for (Iterator<String> valueIterator = ((List)form.get(name)).iterator(); valueIterator.hasNext();)
/* 188:    */       {
/* 189:233 */         String value = (String)valueIterator.next();
/* 190:234 */         builder.append(URLEncoder.encode(name, charset.name()));
/* 191:235 */         if (value != null)
/* 192:    */         {
/* 193:236 */           builder.append('=');
/* 194:237 */           builder.append(URLEncoder.encode(value, charset.name()));
/* 195:238 */           if (valueIterator.hasNext()) {
/* 196:239 */             builder.append('&');
/* 197:    */           }
/* 198:    */         }
/* 199:    */       }
/* 200:243 */       if (nameIterator.hasNext()) {
/* 201:244 */         builder.append('&');
/* 202:    */       }
/* 203:    */     }
/* 204:247 */     byte[] bytes = builder.toString().getBytes(charset.name());
/* 205:248 */     outputMessage.getHeaders().setContentLength(bytes.length);
/* 206:249 */     FileCopyUtils.copy(bytes, outputMessage.getBody());
/* 207:    */   }
/* 208:    */   
/* 209:    */   private void writeMultipart(MultiValueMap<String, Object> parts, HttpOutputMessage outputMessage)
/* 210:    */     throws IOException
/* 211:    */   {
/* 212:254 */     byte[] boundary = generateMultipartBoundary();
/* 213:    */     
/* 214:256 */     Map<String, String> parameters = Collections.singletonMap("boundary", new String(boundary, "US-ASCII"));
/* 215:257 */     MediaType contentType = new MediaType(MediaType.MULTIPART_FORM_DATA, parameters);
/* 216:258 */     outputMessage.getHeaders().setContentType(contentType);
/* 217:    */     
/* 218:260 */     writeParts(outputMessage.getBody(), parts, boundary);
/* 219:261 */     writeEnd(boundary, outputMessage.getBody());
/* 220:    */   }
/* 221:    */   
/* 222:    */   private void writeParts(OutputStream os, MultiValueMap<String, Object> parts, byte[] boundary)
/* 223:    */     throws IOException
/* 224:    */   {
/* 225:    */     Iterator localIterator2;
/* 226:265 */     for (Iterator localIterator1 = parts.entrySet().iterator(); localIterator1.hasNext(); localIterator2.hasNext())
/* 227:    */     {
/* 228:265 */       Map.Entry<String, List<Object>> entry = (Map.Entry)localIterator1.next();
/* 229:266 */       String name = (String)entry.getKey();
/* 230:267 */       localIterator2 = ((List)entry.getValue()).iterator(); continue;Object part = localIterator2.next();
/* 231:268 */       writeBoundary(boundary, os);
/* 232:269 */       HttpEntity entity = getEntity(part);
/* 233:270 */       writePart(name, entity, os);
/* 234:271 */       writeNewLine(os);
/* 235:    */     }
/* 236:    */   }
/* 237:    */   
/* 238:    */   private void writeBoundary(byte[] boundary, OutputStream os)
/* 239:    */     throws IOException
/* 240:    */   {
/* 241:277 */     os.write(45);
/* 242:278 */     os.write(45);
/* 243:279 */     os.write(boundary);
/* 244:280 */     writeNewLine(os);
/* 245:    */   }
/* 246:    */   
/* 247:    */   private HttpEntity getEntity(Object part)
/* 248:    */   {
/* 249:285 */     if ((part instanceof HttpEntity)) {
/* 250:286 */       return (HttpEntity)part;
/* 251:    */     }
/* 252:289 */     return new HttpEntity(part);
/* 253:    */   }
/* 254:    */   
/* 255:    */   private void writePart(String name, HttpEntity partEntity, OutputStream os)
/* 256:    */     throws IOException
/* 257:    */   {
/* 258:295 */     Object partBody = partEntity.getBody();
/* 259:296 */     Class<?> partType = partBody.getClass();
/* 260:297 */     HttpHeaders partHeaders = partEntity.getHeaders();
/* 261:298 */     MediaType partContentType = partHeaders.getContentType();
/* 262:299 */     for (HttpMessageConverter messageConverter : this.partConverters) {
/* 263:300 */       if (messageConverter.canWrite(partType, partContentType))
/* 264:    */       {
/* 265:301 */         HttpOutputMessage multipartOutputMessage = new MultipartHttpOutputMessage(os);
/* 266:302 */         multipartOutputMessage.getHeaders().setContentDispositionFormData(name, getFilename(partBody));
/* 267:303 */         if (!partHeaders.isEmpty()) {
/* 268:304 */           multipartOutputMessage.getHeaders().putAll(partHeaders);
/* 269:    */         }
/* 270:306 */         messageConverter.write(partBody, partContentType, multipartOutputMessage);
/* 271:307 */         return;
/* 272:    */       }
/* 273:    */     }
/* 274:310 */     throw new HttpMessageNotWritableException(
/* 275:311 */       "Could not write request: no suitable HttpMessageConverter found for request type [" + 
/* 276:312 */       partType.getName() + "]");
/* 277:    */   }
/* 278:    */   
/* 279:    */   private void writeEnd(byte[] boundary, OutputStream os)
/* 280:    */     throws IOException
/* 281:    */   {
/* 282:316 */     os.write(45);
/* 283:317 */     os.write(45);
/* 284:318 */     os.write(boundary);
/* 285:319 */     os.write(45);
/* 286:320 */     os.write(45);
/* 287:321 */     writeNewLine(os);
/* 288:    */   }
/* 289:    */   
/* 290:    */   private void writeNewLine(OutputStream os)
/* 291:    */     throws IOException
/* 292:    */   {
/* 293:325 */     os.write(13);
/* 294:326 */     os.write(10);
/* 295:    */   }
/* 296:    */   
/* 297:    */   protected byte[] generateMultipartBoundary()
/* 298:    */   {
/* 299:335 */     byte[] boundary = new byte[this.rnd.nextInt(11) + 30];
/* 300:336 */     for (int i = 0; i < boundary.length; i++) {
/* 301:337 */       boundary[i] = BOUNDARY_CHARS[this.rnd.nextInt(BOUNDARY_CHARS.length)];
/* 302:    */     }
/* 303:339 */     return boundary;
/* 304:    */   }
/* 305:    */   
/* 306:    */   protected String getFilename(Object part)
/* 307:    */   {
/* 308:351 */     if ((part instanceof Resource))
/* 309:    */     {
/* 310:352 */       Resource resource = (Resource)part;
/* 311:353 */       return resource.getFilename();
/* 312:    */     }
/* 313:356 */     return null;
/* 314:    */   }
/* 315:    */   
/* 316:    */   private class MultipartHttpOutputMessage
/* 317:    */     implements HttpOutputMessage
/* 318:    */   {
/* 319:366 */     private final HttpHeaders headers = new HttpHeaders();
/* 320:    */     private final OutputStream os;
/* 321:370 */     private boolean headersWritten = false;
/* 322:    */     
/* 323:    */     public MultipartHttpOutputMessage(OutputStream os)
/* 324:    */     {
/* 325:373 */       this.os = os;
/* 326:    */     }
/* 327:    */     
/* 328:    */     public HttpHeaders getHeaders()
/* 329:    */     {
/* 330:377 */       return this.headersWritten ? HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers;
/* 331:    */     }
/* 332:    */     
/* 333:    */     public OutputStream getBody()
/* 334:    */       throws IOException
/* 335:    */     {
/* 336:381 */       writeHeaders();
/* 337:382 */       return this.os;
/* 338:    */     }
/* 339:    */     
/* 340:    */     private void writeHeaders()
/* 341:    */       throws IOException
/* 342:    */     {
/* 343:386 */       if (!this.headersWritten)
/* 344:    */       {
/* 345:    */         Iterator localIterator2;
/* 346:387 */         for (Iterator localIterator1 = this.headers.entrySet().iterator(); localIterator1.hasNext(); localIterator2.hasNext())
/* 347:    */         {
/* 348:387 */           Map.Entry<String, List<String>> entry = (Map.Entry)localIterator1.next();
/* 349:388 */           byte[] headerName = getAsciiBytes((String)entry.getKey());
/* 350:389 */           localIterator2 = ((List)entry.getValue()).iterator(); continue;String headerValueString = (String)localIterator2.next();
/* 351:390 */           byte[] headerValue = getAsciiBytes(headerValueString);
/* 352:391 */           this.os.write(headerName);
/* 353:392 */           this.os.write(58);
/* 354:393 */           this.os.write(32);
/* 355:394 */           this.os.write(headerValue);
/* 356:395 */           FormHttpMessageConverter.this.writeNewLine(this.os);
/* 357:    */         }
/* 358:398 */         FormHttpMessageConverter.this.writeNewLine(this.os);
/* 359:399 */         this.headersWritten = true;
/* 360:    */       }
/* 361:    */     }
/* 362:    */     
/* 363:    */     protected byte[] getAsciiBytes(String name)
/* 364:    */     {
/* 365:    */       try
/* 366:    */       {
/* 367:405 */         return name.getBytes("US-ASCII");
/* 368:    */       }
/* 369:    */       catch (UnsupportedEncodingException ex)
/* 370:    */       {
/* 371:409 */         throw new IllegalStateException(ex);
/* 372:    */       }
/* 373:    */     }
/* 374:    */   }
/* 375:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.converter.FormHttpMessageConverter
 * JD-Core Version:    0.7.0.1
 */