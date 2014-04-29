/*   1:    */ package org.springframework.beans.factory.xml;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.util.Set;
/*   6:    */ import javax.xml.parsers.ParserConfigurationException;
/*   7:    */ import org.springframework.beans.BeanUtils;
/*   8:    */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*   9:    */ import org.springframework.beans.factory.parsing.EmptyReaderEventListener;
/*  10:    */ import org.springframework.beans.factory.parsing.FailFastProblemReporter;
/*  11:    */ import org.springframework.beans.factory.parsing.NullSourceExtractor;
/*  12:    */ import org.springframework.beans.factory.parsing.ProblemReporter;
/*  13:    */ import org.springframework.beans.factory.parsing.ReaderEventListener;
/*  14:    */ import org.springframework.beans.factory.parsing.SourceExtractor;
/*  15:    */ import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
/*  16:    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  17:    */ import org.springframework.core.Constants;
/*  18:    */ import org.springframework.core.NamedThreadLocal;
/*  19:    */ import org.springframework.core.io.DescriptiveResource;
/*  20:    */ import org.springframework.core.io.Resource;
/*  21:    */ import org.springframework.core.io.ResourceLoader;
/*  22:    */ import org.springframework.core.io.support.EncodedResource;
/*  23:    */ import org.springframework.util.xml.SimpleSaxErrorHandler;
/*  24:    */ import org.springframework.util.xml.XmlValidationModeDetector;
/*  25:    */ import org.w3c.dom.Document;
/*  26:    */ import org.xml.sax.EntityResolver;
/*  27:    */ import org.xml.sax.ErrorHandler;
/*  28:    */ import org.xml.sax.InputSource;
/*  29:    */ import org.xml.sax.SAXException;
/*  30:    */ import org.xml.sax.SAXParseException;
/*  31:    */ 
/*  32:    */ public class XmlBeanDefinitionReader
/*  33:    */   extends AbstractBeanDefinitionReader
/*  34:    */ {
/*  35:    */   public static final int VALIDATION_NONE = 0;
/*  36:    */   public static final int VALIDATION_AUTO = 1;
/*  37:    */   public static final int VALIDATION_DTD = 2;
/*  38:    */   public static final int VALIDATION_XSD = 3;
/*  39:101 */   private static final Constants constants = new Constants(XmlBeanDefinitionReader.class);
/*  40:103 */   private int validationMode = 1;
/*  41:105 */   private boolean namespaceAware = false;
/*  42:107 */   private Class<?> documentReaderClass = DefaultBeanDefinitionDocumentReader.class;
/*  43:109 */   private ProblemReporter problemReporter = new FailFastProblemReporter();
/*  44:111 */   private ReaderEventListener eventListener = new EmptyReaderEventListener();
/*  45:113 */   private SourceExtractor sourceExtractor = new NullSourceExtractor();
/*  46:    */   private NamespaceHandlerResolver namespaceHandlerResolver;
/*  47:117 */   private DocumentLoader documentLoader = new DefaultDocumentLoader();
/*  48:    */   private EntityResolver entityResolver;
/*  49:121 */   private ErrorHandler errorHandler = new SimpleSaxErrorHandler(this.logger);
/*  50:123 */   private final XmlValidationModeDetector validationModeDetector = new XmlValidationModeDetector();
/*  51:126 */   private final ThreadLocal<Set<EncodedResource>> resourcesCurrentlyBeingLoaded = new NamedThreadLocal("XML bean definition resources currently being loaded");
/*  52:    */   
/*  53:    */   public XmlBeanDefinitionReader(BeanDefinitionRegistry registry)
/*  54:    */   {
/*  55:135 */     super(registry);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setValidating(boolean validating)
/*  59:    */   {
/*  60:146 */     this.validationMode = (validating ? 1 : 0);
/*  61:147 */     this.namespaceAware = (!validating);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void setValidationModeName(String validationModeName)
/*  65:    */   {
/*  66:155 */     setValidationMode(constants.asNumber(validationModeName).intValue());
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void setValidationMode(int validationMode)
/*  70:    */   {
/*  71:165 */     this.validationMode = validationMode;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public int getValidationMode()
/*  75:    */   {
/*  76:172 */     return this.validationMode;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void setNamespaceAware(boolean namespaceAware)
/*  80:    */   {
/*  81:183 */     this.namespaceAware = namespaceAware;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public boolean isNamespaceAware()
/*  85:    */   {
/*  86:190 */     return this.namespaceAware;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void setProblemReporter(ProblemReporter problemReporter)
/*  90:    */   {
/*  91:200 */     this.problemReporter = (problemReporter != null ? problemReporter : new FailFastProblemReporter());
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void setEventListener(ReaderEventListener eventListener)
/*  95:    */   {
/*  96:210 */     this.eventListener = (eventListener != null ? eventListener : new EmptyReaderEventListener());
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void setSourceExtractor(SourceExtractor sourceExtractor)
/* 100:    */   {
/* 101:220 */     this.sourceExtractor = (sourceExtractor != null ? sourceExtractor : new NullSourceExtractor());
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void setNamespaceHandlerResolver(NamespaceHandlerResolver namespaceHandlerResolver)
/* 105:    */   {
/* 106:229 */     this.namespaceHandlerResolver = namespaceHandlerResolver;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public void setDocumentLoader(DocumentLoader documentLoader)
/* 110:    */   {
/* 111:238 */     this.documentLoader = (documentLoader != null ? documentLoader : new DefaultDocumentLoader());
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void setEntityResolver(EntityResolver entityResolver)
/* 115:    */   {
/* 116:247 */     this.entityResolver = entityResolver;
/* 117:    */   }
/* 118:    */   
/* 119:    */   protected EntityResolver getEntityResolver()
/* 120:    */   {
/* 121:255 */     if (this.entityResolver == null)
/* 122:    */     {
/* 123:257 */       ResourceLoader resourceLoader = getResourceLoader();
/* 124:258 */       if (resourceLoader != null) {
/* 125:259 */         this.entityResolver = new ResourceEntityResolver(resourceLoader);
/* 126:    */       } else {
/* 127:262 */         this.entityResolver = new DelegatingEntityResolver(getBeanClassLoader());
/* 128:    */       }
/* 129:    */     }
/* 130:265 */     return this.entityResolver;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public void setErrorHandler(ErrorHandler errorHandler)
/* 134:    */   {
/* 135:277 */     this.errorHandler = errorHandler;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public void setDocumentReaderClass(Class<?> documentReaderClass)
/* 139:    */   {
/* 140:287 */     if ((documentReaderClass == null) || (!BeanDefinitionDocumentReader.class.isAssignableFrom(documentReaderClass))) {
/* 141:288 */       throw new IllegalArgumentException(
/* 142:289 */         "documentReaderClass must be an implementation of the BeanDefinitionDocumentReader interface");
/* 143:    */     }
/* 144:291 */     this.documentReaderClass = documentReaderClass;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public int loadBeanDefinitions(Resource resource)
/* 148:    */     throws BeanDefinitionStoreException
/* 149:    */   {
/* 150:302 */     return loadBeanDefinitions(new EncodedResource(resource));
/* 151:    */   }
/* 152:    */   
/* 153:    */   /* Error */
/* 154:    */   public int loadBeanDefinitions(EncodedResource encodedResource)
/* 155:    */     throws BeanDefinitionStoreException
/* 156:    */   {
/* 157:    */     // Byte code:
/* 158:    */     //   0: aload_1
/* 159:    */     //   1: ldc 212
/* 160:    */     //   3: invokestatic 214	org/springframework/util/Assert:notNull	(Ljava/lang/Object;Ljava/lang/String;)V
/* 161:    */     //   6: aload_0
/* 162:    */     //   7: getfield 90	org/springframework/beans/factory/xml/XmlBeanDefinitionReader:logger	Lorg/apache/commons/logging/Log;
/* 163:    */     //   10: invokeinterface 220 1 0
/* 164:    */     //   15: ifeq +31 -> 46
/* 165:    */     //   18: aload_0
/* 166:    */     //   19: getfield 90	org/springframework/beans/factory/xml/XmlBeanDefinitionReader:logger	Lorg/apache/commons/logging/Log;
/* 167:    */     //   22: new 225	java/lang/StringBuilder
/* 168:    */     //   25: dup
/* 169:    */     //   26: ldc 227
/* 170:    */     //   28: invokespecial 229	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/* 171:    */     //   31: aload_1
/* 172:    */     //   32: invokevirtual 230	org/springframework/core/io/support/EncodedResource:getResource	()Lorg/springframework/core/io/Resource;
/* 173:    */     //   35: invokevirtual 234	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/* 174:    */     //   38: invokevirtual 238	java/lang/StringBuilder:toString	()Ljava/lang/String;
/* 175:    */     //   41: invokeinterface 242 2 0
/* 176:    */     //   46: aload_0
/* 177:    */     //   47: getfield 111	org/springframework/beans/factory/xml/XmlBeanDefinitionReader:resourcesCurrentlyBeingLoaded	Ljava/lang/ThreadLocal;
/* 178:    */     //   50: invokevirtual 246	java/lang/ThreadLocal:get	()Ljava/lang/Object;
/* 179:    */     //   53: checkcast 252	java/util/Set
/* 180:    */     //   56: astore_2
/* 181:    */     //   57: aload_2
/* 182:    */     //   58: ifnonnull +20 -> 78
/* 183:    */     //   61: new 254	java/util/HashSet
/* 184:    */     //   64: dup
/* 185:    */     //   65: iconst_4
/* 186:    */     //   66: invokespecial 256	java/util/HashSet:<init>	(I)V
/* 187:    */     //   69: astore_2
/* 188:    */     //   70: aload_0
/* 189:    */     //   71: getfield 111	org/springframework/beans/factory/xml/XmlBeanDefinitionReader:resourcesCurrentlyBeingLoaded	Ljava/lang/ThreadLocal;
/* 190:    */     //   74: aload_2
/* 191:    */     //   75: invokevirtual 258	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
/* 192:    */     //   78: aload_2
/* 193:    */     //   79: aload_1
/* 194:    */     //   80: invokeinterface 261 2 0
/* 195:    */     //   85: ifne +34 -> 119
/* 196:    */     //   88: new 200	org/springframework/beans/factory/BeanDefinitionStoreException
/* 197:    */     //   91: dup
/* 198:    */     //   92: new 225	java/lang/StringBuilder
/* 199:    */     //   95: dup
/* 200:    */     //   96: ldc_w 265
/* 201:    */     //   99: invokespecial 229	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/* 202:    */     //   102: aload_1
/* 203:    */     //   103: invokevirtual 234	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/* 204:    */     //   106: ldc_w 267
/* 205:    */     //   109: invokevirtual 269	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/* 206:    */     //   112: invokevirtual 238	java/lang/StringBuilder:toString	()Ljava/lang/String;
/* 207:    */     //   115: invokespecial 272	org/springframework/beans/factory/BeanDefinitionStoreException:<init>	(Ljava/lang/String;)V
/* 208:    */     //   118: athrow
/* 209:    */     //   119: aload_1
/* 210:    */     //   120: invokevirtual 230	org/springframework/core/io/support/EncodedResource:getResource	()Lorg/springframework/core/io/Resource;
/* 211:    */     //   123: invokeinterface 273 1 0
/* 212:    */     //   128: astore_3
/* 213:    */     //   129: new 279	org/xml/sax/InputSource
/* 214:    */     //   132: dup
/* 215:    */     //   133: aload_3
/* 216:    */     //   134: invokespecial 281	org/xml/sax/InputSource:<init>	(Ljava/io/InputStream;)V
/* 217:    */     //   137: astore 4
/* 218:    */     //   139: aload_1
/* 219:    */     //   140: invokevirtual 284	org/springframework/core/io/support/EncodedResource:getEncoding	()Ljava/lang/String;
/* 220:    */     //   143: ifnull +12 -> 155
/* 221:    */     //   146: aload 4
/* 222:    */     //   148: aload_1
/* 223:    */     //   149: invokevirtual 284	org/springframework/core/io/support/EncodedResource:getEncoding	()Ljava/lang/String;
/* 224:    */     //   152: invokevirtual 287	org/xml/sax/InputSource:setEncoding	(Ljava/lang/String;)V
/* 225:    */     //   155: aload_0
/* 226:    */     //   156: aload 4
/* 227:    */     //   158: aload_1
/* 228:    */     //   159: invokevirtual 230	org/springframework/core/io/support/EncodedResource:getResource	()Lorg/springframework/core/io/Resource;
/* 229:    */     //   162: invokevirtual 290	org/springframework/beans/factory/xml/XmlBeanDefinitionReader:doLoadBeanDefinitions	(Lorg/xml/sax/InputSource;Lorg/springframework/core/io/Resource;)I
/* 230:    */     //   165: istore 6
/* 231:    */     //   167: aload_3
/* 232:    */     //   168: invokevirtual 294	java/io/InputStream:close	()V
/* 233:    */     //   171: aload_2
/* 234:    */     //   172: aload_1
/* 235:    */     //   173: invokeinterface 299 2 0
/* 236:    */     //   178: pop
/* 237:    */     //   179: aload_2
/* 238:    */     //   180: invokeinterface 302 1 0
/* 239:    */     //   185: ifeq +10 -> 195
/* 240:    */     //   188: aload_0
/* 241:    */     //   189: getfield 111	org/springframework/beans/factory/xml/XmlBeanDefinitionReader:resourcesCurrentlyBeingLoaded	Ljava/lang/ThreadLocal;
/* 242:    */     //   192: invokevirtual 305	java/lang/ThreadLocal:remove	()V
/* 243:    */     //   195: iload 6
/* 244:    */     //   197: ireturn
/* 245:    */     //   198: astore 5
/* 246:    */     //   200: aload_3
/* 247:    */     //   201: invokevirtual 294	java/io/InputStream:close	()V
/* 248:    */     //   204: aload 5
/* 249:    */     //   206: athrow
/* 250:    */     //   207: astore_3
/* 251:    */     //   208: new 200	org/springframework/beans/factory/BeanDefinitionStoreException
/* 252:    */     //   211: dup
/* 253:    */     //   212: new 225	java/lang/StringBuilder
/* 254:    */     //   215: dup
/* 255:    */     //   216: ldc_w 307
/* 256:    */     //   219: invokespecial 229	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/* 257:    */     //   222: aload_1
/* 258:    */     //   223: invokevirtual 230	org/springframework/core/io/support/EncodedResource:getResource	()Lorg/springframework/core/io/Resource;
/* 259:    */     //   226: invokevirtual 234	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/* 260:    */     //   229: invokevirtual 238	java/lang/StringBuilder:toString	()Ljava/lang/String;
/* 261:    */     //   232: aload_3
/* 262:    */     //   233: invokespecial 309	org/springframework/beans/factory/BeanDefinitionStoreException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
/* 263:    */     //   236: athrow
/* 264:    */     //   237: astore 7
/* 265:    */     //   239: aload_2
/* 266:    */     //   240: aload_1
/* 267:    */     //   241: invokeinterface 299 2 0
/* 268:    */     //   246: pop
/* 269:    */     //   247: aload_2
/* 270:    */     //   248: invokeinterface 302 1 0
/* 271:    */     //   253: ifeq +10 -> 263
/* 272:    */     //   256: aload_0
/* 273:    */     //   257: getfield 111	org/springframework/beans/factory/xml/XmlBeanDefinitionReader:resourcesCurrentlyBeingLoaded	Ljava/lang/ThreadLocal;
/* 274:    */     //   260: invokevirtual 305	java/lang/ThreadLocal:remove	()V
/* 275:    */     //   263: aload 7
/* 276:    */     //   265: athrow
/* 277:    */     // Line number table:
/* 278:    */     //   Java source line #313	-> byte code offset #0
/* 279:    */     //   Java source line #314	-> byte code offset #6
/* 280:    */     //   Java source line #315	-> byte code offset #18
/* 281:    */     //   Java source line #318	-> byte code offset #46
/* 282:    */     //   Java source line #319	-> byte code offset #57
/* 283:    */     //   Java source line #320	-> byte code offset #61
/* 284:    */     //   Java source line #321	-> byte code offset #70
/* 285:    */     //   Java source line #323	-> byte code offset #78
/* 286:    */     //   Java source line #324	-> byte code offset #88
/* 287:    */     //   Java source line #325	-> byte code offset #92
/* 288:    */     //   Java source line #324	-> byte code offset #115
/* 289:    */     //   Java source line #328	-> byte code offset #119
/* 290:    */     //   Java source line #330	-> byte code offset #129
/* 291:    */     //   Java source line #331	-> byte code offset #139
/* 292:    */     //   Java source line #332	-> byte code offset #146
/* 293:    */     //   Java source line #334	-> byte code offset #155
/* 294:    */     //   Java source line #337	-> byte code offset #167
/* 295:    */     //   Java source line #345	-> byte code offset #171
/* 296:    */     //   Java source line #346	-> byte code offset #179
/* 297:    */     //   Java source line #347	-> byte code offset #188
/* 298:    */     //   Java source line #334	-> byte code offset #195
/* 299:    */     //   Java source line #336	-> byte code offset #198
/* 300:    */     //   Java source line #337	-> byte code offset #200
/* 301:    */     //   Java source line #338	-> byte code offset #204
/* 302:    */     //   Java source line #340	-> byte code offset #207
/* 303:    */     //   Java source line #341	-> byte code offset #208
/* 304:    */     //   Java source line #342	-> byte code offset #212
/* 305:    */     //   Java source line #341	-> byte code offset #233
/* 306:    */     //   Java source line #344	-> byte code offset #237
/* 307:    */     //   Java source line #345	-> byte code offset #239
/* 308:    */     //   Java source line #346	-> byte code offset #247
/* 309:    */     //   Java source line #347	-> byte code offset #256
/* 310:    */     //   Java source line #349	-> byte code offset #263
/* 311:    */     // Local variable table:
/* 312:    */     //   start	length	slot	name	signature
/* 313:    */     //   0	266	0	this	XmlBeanDefinitionReader
/* 314:    */     //   0	266	1	encodedResource	EncodedResource
/* 315:    */     //   56	192	2	currentResources	Set<EncodedResource>
/* 316:    */     //   128	40	3	inputStream	InputStream
/* 317:    */     //   198	3	3	inputStream	InputStream
/* 318:    */     //   207	26	3	ex	IOException
/* 319:    */     //   137	20	4	inputSource	InputSource
/* 320:    */     //   198	7	5	localObject1	java.lang.Object
/* 321:    */     //   165	31	6	i	int
/* 322:    */     //   237	27	7	localObject2	java.lang.Object
/* 323:    */     // Exception table:
/* 324:    */     //   from	to	target	type
/* 325:    */     //   129	167	198	finally
/* 326:    */     //   119	171	207	java/io/IOException
/* 327:    */     //   198	207	207	java/io/IOException
/* 328:    */     //   119	171	237	finally
/* 329:    */     //   198	237	237	finally
/* 330:    */   }
/* 331:    */   
/* 332:    */   public int loadBeanDefinitions(InputSource inputSource)
/* 333:    */     throws BeanDefinitionStoreException
/* 334:    */   {
/* 335:359 */     return loadBeanDefinitions(inputSource, "resource loaded through SAX InputSource");
/* 336:    */   }
/* 337:    */   
/* 338:    */   public int loadBeanDefinitions(InputSource inputSource, String resourceDescription)
/* 339:    */     throws BeanDefinitionStoreException
/* 340:    */   {
/* 341:373 */     return doLoadBeanDefinitions(inputSource, new DescriptiveResource(resourceDescription));
/* 342:    */   }
/* 343:    */   
/* 344:    */   protected int doLoadBeanDefinitions(InputSource inputSource, Resource resource)
/* 345:    */     throws BeanDefinitionStoreException
/* 346:    */   {
/* 347:    */     try
/* 348:    */     {
/* 349:387 */       int validationMode = getValidationModeForResource(resource);
/* 350:388 */       Document doc = this.documentLoader.loadDocument(
/* 351:389 */         inputSource, getEntityResolver(), this.errorHandler, validationMode, isNamespaceAware());
/* 352:390 */       return registerBeanDefinitions(doc, resource);
/* 353:    */     }
/* 354:    */     catch (BeanDefinitionStoreException ex)
/* 355:    */     {
/* 356:393 */       throw ex;
/* 357:    */     }
/* 358:    */     catch (SAXParseException ex)
/* 359:    */     {
/* 360:396 */       throw new XmlBeanDefinitionStoreException(resource.getDescription(), 
/* 361:397 */         "Line " + ex.getLineNumber() + " in XML document from " + resource + " is invalid", ex);
/* 362:    */     }
/* 363:    */     catch (SAXException ex)
/* 364:    */     {
/* 365:400 */       throw new XmlBeanDefinitionStoreException(resource.getDescription(), 
/* 366:401 */         "XML document from " + resource + " is invalid", ex);
/* 367:    */     }
/* 368:    */     catch (ParserConfigurationException ex)
/* 369:    */     {
/* 370:404 */       throw new BeanDefinitionStoreException(resource.getDescription(), 
/* 371:405 */         "Parser configuration exception parsing XML from " + resource, ex);
/* 372:    */     }
/* 373:    */     catch (IOException ex)
/* 374:    */     {
/* 375:408 */       throw new BeanDefinitionStoreException(resource.getDescription(), 
/* 376:409 */         "IOException parsing XML document from " + resource, ex);
/* 377:    */     }
/* 378:    */     catch (Throwable ex)
/* 379:    */     {
/* 380:412 */       throw new BeanDefinitionStoreException(resource.getDescription(), 
/* 381:413 */         "Unexpected exception parsing XML document from " + resource, ex);
/* 382:    */     }
/* 383:    */   }
/* 384:    */   
/* 385:    */   protected int getValidationModeForResource(Resource resource)
/* 386:    */   {
/* 387:426 */     int validationModeToUse = getValidationMode();
/* 388:427 */     if (validationModeToUse != 1) {
/* 389:428 */       return validationModeToUse;
/* 390:    */     }
/* 391:430 */     int detectedMode = detectValidationMode(resource);
/* 392:431 */     if (detectedMode != 1) {
/* 393:432 */       return detectedMode;
/* 394:    */     }
/* 395:437 */     return 3;
/* 396:    */   }
/* 397:    */   
/* 398:    */   protected int detectValidationMode(Resource resource)
/* 399:    */   {
/* 400:448 */     if (resource.isOpen()) {
/* 401:449 */       throw new BeanDefinitionStoreException(
/* 402:450 */         "Passed-in Resource [" + resource + "] contains an open stream: " + 
/* 403:451 */         "cannot determine validation mode automatically. Either pass in a Resource " + 
/* 404:452 */         "that is able to create fresh streams, or explicitly specify the validationMode " + 
/* 405:453 */         "on your XmlBeanDefinitionReader instance.");
/* 406:    */     }
/* 407:    */     try
/* 408:    */     {
/* 409:458 */       inputStream = resource.getInputStream();
/* 410:    */     }
/* 411:    */     catch (IOException ex)
/* 412:    */     {
/* 413:    */       InputStream inputStream;
/* 414:461 */       throw new BeanDefinitionStoreException(
/* 415:462 */         "Unable to determine validation mode for [" + resource + "]: cannot open InputStream. " + 
/* 416:463 */         "Did you attempt to load directly from a SAX InputSource without specifying the " + 
/* 417:464 */         "validationMode on your XmlBeanDefinitionReader instance?", ex);
/* 418:    */     }
/* 419:    */     try
/* 420:    */     {
/* 421:    */       InputStream inputStream;
/* 422:468 */       return this.validationModeDetector.detectValidationMode(inputStream);
/* 423:    */     }
/* 424:    */     catch (IOException ex)
/* 425:    */     {
/* 426:471 */       throw new BeanDefinitionStoreException("Unable to determine validation mode for [" + 
/* 427:472 */         resource + "]: an error occurred whilst reading from the InputStream.", ex);
/* 428:    */     }
/* 429:    */   }
/* 430:    */   
/* 431:    */   public int registerBeanDefinitions(Document doc, Resource resource)
/* 432:    */     throws BeanDefinitionStoreException
/* 433:    */   {
/* 434:490 */     BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
/* 435:491 */     documentReader.setEnvironment(getEnvironment());
/* 436:492 */     int countBefore = getRegistry().getBeanDefinitionCount();
/* 437:493 */     documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
/* 438:494 */     return getRegistry().getBeanDefinitionCount() - countBefore;
/* 439:    */   }
/* 440:    */   
/* 441:    */   protected BeanDefinitionDocumentReader createBeanDefinitionDocumentReader()
/* 442:    */   {
/* 443:504 */     return (BeanDefinitionDocumentReader)BeanDefinitionDocumentReader.class.cast(BeanUtils.instantiateClass(this.documentReaderClass));
/* 444:    */   }
/* 445:    */   
/* 446:    */   protected XmlReaderContext createReaderContext(Resource resource)
/* 447:    */   {
/* 448:511 */     if (this.namespaceHandlerResolver == null) {
/* 449:512 */       this.namespaceHandlerResolver = createDefaultNamespaceHandlerResolver();
/* 450:    */     }
/* 451:514 */     return new XmlReaderContext(resource, this.problemReporter, this.eventListener, 
/* 452:515 */       this.sourceExtractor, this, this.namespaceHandlerResolver);
/* 453:    */   }
/* 454:    */   
/* 455:    */   protected NamespaceHandlerResolver createDefaultNamespaceHandlerResolver()
/* 456:    */   {
/* 457:523 */     return new DefaultNamespaceHandlerResolver(getResourceLoader().getClassLoader());
/* 458:    */   }
/* 459:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.XmlBeanDefinitionReader
 * JD-Core Version:    0.7.0.1
 */