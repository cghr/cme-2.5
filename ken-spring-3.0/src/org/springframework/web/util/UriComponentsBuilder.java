/*   1:    */ package org.springframework.web.util;
/*   2:    */ 
/*   3:    */ import java.net.URI;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.regex.Matcher;
/*   8:    */ import java.util.regex.Pattern;
/*   9:    */ import org.springframework.util.Assert;
/*  10:    */ import org.springframework.util.LinkedMultiValueMap;
/*  11:    */ import org.springframework.util.MultiValueMap;
/*  12:    */ import org.springframework.util.ObjectUtils;
/*  13:    */ import org.springframework.util.StringUtils;
/*  14:    */ 
/*  15:    */ public class UriComponentsBuilder
/*  16:    */ {
/*  17: 54 */   private static final Pattern QUERY_PARAM_PATTERN = Pattern.compile("([^&=]+)=?([^&=]+)?");
/*  18:    */   private static final String SCHEME_PATTERN = "([^:/?#]+):";
/*  19:    */   private static final String HTTP_PATTERN = "(http|https):";
/*  20:    */   private static final String USERINFO_PATTERN = "([^@/]*)";
/*  21:    */   private static final String HOST_PATTERN = "([^/?#:]*)";
/*  22:    */   private static final String PORT_PATTERN = "(\\d*)";
/*  23:    */   private static final String PATH_PATTERN = "([^?#]*)";
/*  24:    */   private static final String QUERY_PATTERN = "([^#]*)";
/*  25:    */   private static final String LAST_PATTERN = "(.*)";
/*  26: 73 */   private static final Pattern URI_PATTERN = Pattern.compile(
/*  27: 74 */     "^(([^:/?#]+):)?(//(([^@/]*)@)?([^/?#:]*)(:(\\d*))?)?([^?#]*)(\\?([^#]*))?(#(.*))?");
/*  28: 77 */   private static final Pattern HTTP_URL_PATTERN = Pattern.compile(
/*  29: 78 */     "^(http|https):(//(([^@/]*)@)?([^/?#:]*)(:(\\d*))?)?([^?#]*)(\\?(.*))?");
/*  30:    */   private String scheme;
/*  31:    */   private String userInfo;
/*  32:    */   private String host;
/*  33: 88 */   private int port = -1;
/*  34: 90 */   private PathComponentBuilder pathBuilder = NULL_PATH_COMPONENT_BUILDER;
/*  35: 92 */   private final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap();
/*  36:    */   private String fragment;
/*  37:    */   
/*  38:    */   public static UriComponentsBuilder newInstance()
/*  39:    */   {
/*  40:114 */     return new UriComponentsBuilder();
/*  41:    */   }
/*  42:    */   
/*  43:    */   public static UriComponentsBuilder fromPath(String path)
/*  44:    */   {
/*  45:124 */     UriComponentsBuilder builder = new UriComponentsBuilder();
/*  46:125 */     builder.path(path);
/*  47:126 */     return builder;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public static UriComponentsBuilder fromUri(URI uri)
/*  51:    */   {
/*  52:136 */     UriComponentsBuilder builder = new UriComponentsBuilder();
/*  53:137 */     builder.uri(uri);
/*  54:138 */     return builder;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public static UriComponentsBuilder fromUriString(String uri)
/*  58:    */   {
/*  59:148 */     Assert.hasLength(uri, "'uri' must not be empty");
/*  60:149 */     Matcher m = URI_PATTERN.matcher(uri);
/*  61:150 */     if (m.matches())
/*  62:    */     {
/*  63:151 */       UriComponentsBuilder builder = new UriComponentsBuilder();
/*  64:    */       
/*  65:153 */       builder.scheme(m.group(2));
/*  66:154 */       builder.userInfo(m.group(5));
/*  67:155 */       builder.host(m.group(6));
/*  68:156 */       String port = m.group(8);
/*  69:157 */       if (StringUtils.hasLength(port)) {
/*  70:158 */         builder.port(Integer.parseInt(port));
/*  71:    */       }
/*  72:160 */       builder.path(m.group(9));
/*  73:161 */       builder.query(m.group(11));
/*  74:162 */       builder.fragment(m.group(13));
/*  75:    */       
/*  76:164 */       return builder;
/*  77:    */     }
/*  78:167 */     throw new IllegalArgumentException("[" + uri + "] is not a valid URI");
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static UriComponentsBuilder fromHttpUrl(String httpUrl)
/*  82:    */   {
/*  83:178 */     Assert.notNull(httpUrl, "'httpUrl' must not be null");
/*  84:179 */     Matcher m = HTTP_URL_PATTERN.matcher(httpUrl);
/*  85:180 */     if (m.matches())
/*  86:    */     {
/*  87:181 */       UriComponentsBuilder builder = new UriComponentsBuilder();
/*  88:    */       
/*  89:183 */       builder.scheme(m.group(1));
/*  90:184 */       builder.userInfo(m.group(4));
/*  91:185 */       builder.host(m.group(5));
/*  92:186 */       String port = m.group(7);
/*  93:187 */       if (StringUtils.hasLength(port)) {
/*  94:188 */         builder.port(Integer.parseInt(port));
/*  95:    */       }
/*  96:190 */       builder.path(m.group(8));
/*  97:191 */       builder.query(m.group(10));
/*  98:    */       
/*  99:193 */       return builder;
/* 100:    */     }
/* 101:196 */     throw new IllegalArgumentException("[" + httpUrl + "] is not a valid HTTP URL");
/* 102:    */   }
/* 103:    */   
/* 104:    */   public UriComponents build()
/* 105:    */   {
/* 106:210 */     return build(false);
/* 107:    */   }
/* 108:    */   
/* 109:    */   public UriComponents build(boolean encoded)
/* 110:    */   {
/* 111:220 */     return new UriComponents(this.scheme, this.userInfo, this.host, this.port, this.pathBuilder.build(), this.queryParams, this.fragment, encoded);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public UriComponentsBuilder uri(URI uri)
/* 115:    */   {
/* 116:232 */     Assert.notNull(uri, "'uri' must not be null");
/* 117:233 */     Assert.isTrue(!uri.isOpaque(), "Opaque URI [" + uri + "] not supported");
/* 118:    */     
/* 119:235 */     this.scheme = uri.getScheme();
/* 120:237 */     if (uri.getUserInfo() != null) {
/* 121:238 */       this.userInfo = uri.getUserInfo();
/* 122:    */     }
/* 123:240 */     if (uri.getHost() != null) {
/* 124:241 */       this.host = uri.getHost();
/* 125:    */     }
/* 126:243 */     if (uri.getPort() != -1) {
/* 127:244 */       this.port = uri.getPort();
/* 128:    */     }
/* 129:246 */     if (StringUtils.hasLength(uri.getPath())) {
/* 130:247 */       this.pathBuilder = new FullPathComponentBuilder(uri.getPath(), null);
/* 131:    */     }
/* 132:249 */     if (StringUtils.hasLength(uri.getQuery()))
/* 133:    */     {
/* 134:250 */       this.queryParams.clear();
/* 135:251 */       query(uri.getQuery());
/* 136:    */     }
/* 137:253 */     if (uri.getFragment() != null) {
/* 138:254 */       this.fragment = uri.getFragment();
/* 139:    */     }
/* 140:256 */     return this;
/* 141:    */   }
/* 142:    */   
/* 143:    */   public UriComponentsBuilder scheme(String scheme)
/* 144:    */   {
/* 145:267 */     this.scheme = scheme;
/* 146:268 */     return this;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public UriComponentsBuilder userInfo(String userInfo)
/* 150:    */   {
/* 151:279 */     this.userInfo = userInfo;
/* 152:280 */     return this;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public UriComponentsBuilder host(String host)
/* 156:    */   {
/* 157:291 */     this.host = host;
/* 158:292 */     return this;
/* 159:    */   }
/* 160:    */   
/* 161:    */   public UriComponentsBuilder port(int port)
/* 162:    */   {
/* 163:302 */     Assert.isTrue(port >= -1, "'port' must not be < -1");
/* 164:303 */     this.port = port;
/* 165:304 */     return this;
/* 166:    */   }
/* 167:    */   
/* 168:    */   public UriComponentsBuilder path(String path)
/* 169:    */   {
/* 170:314 */     if (path != null) {
/* 171:315 */       this.pathBuilder = this.pathBuilder.appendPath(path);
/* 172:    */     } else {
/* 173:317 */       this.pathBuilder = NULL_PATH_COMPONENT_BUILDER;
/* 174:    */     }
/* 175:319 */     return this;
/* 176:    */   }
/* 177:    */   
/* 178:    */   public UriComponentsBuilder pathSegment(String... pathSegments)
/* 179:    */     throws IllegalArgumentException
/* 180:    */   {
/* 181:330 */     Assert.notNull(pathSegments, "'segments' must not be null");
/* 182:331 */     this.pathBuilder = this.pathBuilder.appendPathSegments(pathSegments);
/* 183:332 */     return this;
/* 184:    */   }
/* 185:    */   
/* 186:    */   public UriComponentsBuilder query(String query)
/* 187:    */   {
/* 188:342 */     if (query != null)
/* 189:    */     {
/* 190:343 */       Matcher m = QUERY_PARAM_PATTERN.matcher(query);
/* 191:344 */       while (m.find())
/* 192:    */       {
/* 193:345 */         String name = m.group(1);
/* 194:346 */         String value = m.group(2);
/* 195:347 */         queryParam(name, new Object[] { value });
/* 196:    */       }
/* 197:    */     }
/* 198:    */     else
/* 199:    */     {
/* 200:351 */       this.queryParams.clear();
/* 201:    */     }
/* 202:353 */     return this;
/* 203:    */   }
/* 204:    */   
/* 205:    */   public UriComponentsBuilder queryParam(String name, Object... values)
/* 206:    */   {
/* 207:367 */     Assert.notNull(name, "'name' must not be null");
/* 208:368 */     if (!ObjectUtils.isEmpty(values)) {
/* 209:369 */       for (Object value : values)
/* 210:    */       {
/* 211:370 */         String valueAsString = value != null ? value.toString() : null;
/* 212:371 */         this.queryParams.add(name, valueAsString);
/* 213:    */       }
/* 214:    */     } else {
/* 215:375 */       this.queryParams.add(name, null);
/* 216:    */     }
/* 217:377 */     return this;
/* 218:    */   }
/* 219:    */   
/* 220:    */   public UriComponentsBuilder fragment(String fragment)
/* 221:    */   {
/* 222:388 */     if (fragment != null)
/* 223:    */     {
/* 224:389 */       Assert.hasLength(fragment, "'fragment' must not be empty");
/* 225:390 */       this.fragment = fragment;
/* 226:    */     }
/* 227:    */     else
/* 228:    */     {
/* 229:393 */       this.fragment = null;
/* 230:    */     }
/* 231:395 */     return this;
/* 232:    */   }
/* 233:    */   
/* 234:    */   private static class FullPathComponentBuilder
/* 235:    */     implements UriComponentsBuilder.PathComponentBuilder
/* 236:    */   {
/* 237:    */     private final StringBuilder path;
/* 238:    */     
/* 239:    */     private FullPathComponentBuilder(String path)
/* 240:    */     {
/* 241:418 */       this.path = new StringBuilder(path);
/* 242:    */     }
/* 243:    */     
/* 244:    */     public UriComponents.PathComponent build()
/* 245:    */     {
/* 246:422 */       return new UriComponents.FullPathComponent(this.path.toString());
/* 247:    */     }
/* 248:    */     
/* 249:    */     public UriComponentsBuilder.PathComponentBuilder appendPath(String path)
/* 250:    */     {
/* 251:426 */       this.path.append(path);
/* 252:427 */       return this;
/* 253:    */     }
/* 254:    */     
/* 255:    */     public UriComponentsBuilder.PathComponentBuilder appendPathSegments(String... pathSegments)
/* 256:    */     {
/* 257:431 */       UriComponentsBuilder.PathComponentCompositeBuilder builder = new UriComponentsBuilder.PathComponentCompositeBuilder(this, null);
/* 258:432 */       builder.appendPathSegments(pathSegments);
/* 259:433 */       return builder;
/* 260:    */     }
/* 261:    */   }
/* 262:    */   
/* 263:    */   private static class PathSegmentComponentBuilder
/* 264:    */     implements UriComponentsBuilder.PathComponentBuilder
/* 265:    */   {
/* 266:442 */     private final List<String> pathSegments = new ArrayList();
/* 267:    */     
/* 268:    */     private PathSegmentComponentBuilder(String... pathSegments)
/* 269:    */     {
/* 270:445 */       Collections.addAll(this.pathSegments, pathSegments);
/* 271:    */     }
/* 272:    */     
/* 273:    */     public UriComponents.PathComponent build()
/* 274:    */     {
/* 275:449 */       return new UriComponents.PathSegmentComponent(this.pathSegments);
/* 276:    */     }
/* 277:    */     
/* 278:    */     public UriComponentsBuilder.PathComponentBuilder appendPath(String path)
/* 279:    */     {
/* 280:453 */       UriComponentsBuilder.PathComponentCompositeBuilder builder = new UriComponentsBuilder.PathComponentCompositeBuilder(this, null);
/* 281:454 */       builder.appendPath(path);
/* 282:455 */       return builder;
/* 283:    */     }
/* 284:    */     
/* 285:    */     public UriComponentsBuilder.PathComponentBuilder appendPathSegments(String... pathSegments)
/* 286:    */     {
/* 287:459 */       Collections.addAll(this.pathSegments, pathSegments);
/* 288:460 */       return this;
/* 289:    */     }
/* 290:    */   }
/* 291:    */   
/* 292:    */   private static class PathComponentCompositeBuilder
/* 293:    */     implements UriComponentsBuilder.PathComponentBuilder
/* 294:    */   {
/* 295:469 */     private final List<UriComponentsBuilder.PathComponentBuilder> pathComponentBuilders = new ArrayList();
/* 296:    */     
/* 297:    */     private PathComponentCompositeBuilder(UriComponentsBuilder.PathComponentBuilder builder)
/* 298:    */     {
/* 299:472 */       this.pathComponentBuilders.add(builder);
/* 300:    */     }
/* 301:    */     
/* 302:    */     public UriComponents.PathComponent build()
/* 303:    */     {
/* 304:476 */       List<UriComponents.PathComponent> pathComponents = 
/* 305:477 */         new ArrayList(this.pathComponentBuilders.size());
/* 306:479 */       for (UriComponentsBuilder.PathComponentBuilder pathComponentBuilder : this.pathComponentBuilders) {
/* 307:480 */         pathComponents.add(pathComponentBuilder.build());
/* 308:    */       }
/* 309:482 */       return new UriComponents.PathComponentComposite(pathComponents);
/* 310:    */     }
/* 311:    */     
/* 312:    */     public UriComponentsBuilder.PathComponentBuilder appendPath(String path)
/* 313:    */     {
/* 314:486 */       this.pathComponentBuilders.add(new UriComponentsBuilder.FullPathComponentBuilder(path, null));
/* 315:487 */       return this;
/* 316:    */     }
/* 317:    */     
/* 318:    */     public UriComponentsBuilder.PathComponentBuilder appendPathSegments(String... pathSegments)
/* 319:    */     {
/* 320:491 */       this.pathComponentBuilders.add(new UriComponentsBuilder.PathSegmentComponentBuilder(pathSegments, null));
/* 321:492 */       return this;
/* 322:    */     }
/* 323:    */   }
/* 324:    */   
/* 325:500 */   private static PathComponentBuilder NULL_PATH_COMPONENT_BUILDER = new PathComponentBuilder()
/* 326:    */   {
/* 327:    */     public UriComponents.PathComponent build()
/* 328:    */     {
/* 329:503 */       return UriComponents.NULL_PATH_COMPONENT;
/* 330:    */     }
/* 331:    */     
/* 332:    */     public UriComponentsBuilder.PathComponentBuilder appendPath(String path)
/* 333:    */     {
/* 334:507 */       return new UriComponentsBuilder.FullPathComponentBuilder(path, null);
/* 335:    */     }
/* 336:    */     
/* 337:    */     public UriComponentsBuilder.PathComponentBuilder appendPathSegments(String... pathSegments)
/* 338:    */     {
/* 339:511 */       return new UriComponentsBuilder.PathSegmentComponentBuilder(pathSegments, null);
/* 340:    */     }
/* 341:    */   };
/* 342:    */   
/* 343:    */   private static abstract interface PathComponentBuilder
/* 344:    */   {
/* 345:    */     public abstract UriComponents.PathComponent build();
/* 346:    */     
/* 347:    */     public abstract PathComponentBuilder appendPath(String paramString);
/* 348:    */     
/* 349:    */     public abstract PathComponentBuilder appendPathSegments(String... paramVarArgs);
/* 350:    */   }
/* 351:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.util.UriComponentsBuilder
 * JD-Core Version:    0.7.0.1
 */