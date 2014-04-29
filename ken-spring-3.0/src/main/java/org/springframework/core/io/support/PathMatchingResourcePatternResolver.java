/*   1:    */ package org.springframework.core.io.support;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.lang.reflect.InvocationHandler;
/*   6:    */ import java.lang.reflect.Method;
/*   7:    */ import java.net.JarURLConnection;
/*   8:    */ import java.net.URI;
/*   9:    */ import java.net.URISyntaxException;
/*  10:    */ import java.net.URL;
/*  11:    */ import java.net.URLConnection;
/*  12:    */ import java.util.Collections;
/*  13:    */ import java.util.Enumeration;
/*  14:    */ import java.util.LinkedHashSet;
/*  15:    */ import java.util.Set;
/*  16:    */ import java.util.jar.JarEntry;
/*  17:    */ import java.util.jar.JarFile;
/*  18:    */ import org.apache.commons.logging.Log;
/*  19:    */ import org.apache.commons.logging.LogFactory;
/*  20:    */ import org.springframework.core.io.DefaultResourceLoader;
/*  21:    */ import org.springframework.core.io.FileSystemResource;
/*  22:    */ import org.springframework.core.io.Resource;
/*  23:    */ import org.springframework.core.io.ResourceLoader;
/*  24:    */ import org.springframework.core.io.UrlResource;
/*  25:    */ import org.springframework.core.io.VfsResource;
/*  26:    */ import org.springframework.util.AntPathMatcher;
/*  27:    */ import org.springframework.util.Assert;
/*  28:    */ import org.springframework.util.PathMatcher;
/*  29:    */ import org.springframework.util.ReflectionUtils;
/*  30:    */ import org.springframework.util.ResourceUtils;
/*  31:    */ import org.springframework.util.StringUtils;
/*  32:    */ 
/*  33:    */ public class PathMatchingResourcePatternResolver
/*  34:    */   implements ResourcePatternResolver
/*  35:    */ {
/*  36:169 */   private static final Log logger = LogFactory.getLog(PathMatchingResourcePatternResolver.class);
/*  37:    */   private static Method equinoxResolveMethod;
/*  38:    */   private final ResourceLoader resourceLoader;
/*  39:    */   
/*  40:    */   static
/*  41:    */   {
/*  42:    */     try
/*  43:    */     {
/*  44:176 */       Class<?> fileLocatorClass = PathMatchingResourcePatternResolver.class.getClassLoader().loadClass(
/*  45:177 */         "org.eclipse.core.runtime.FileLocator");
/*  46:178 */       equinoxResolveMethod = fileLocatorClass.getMethod("resolve", new Class[] { URL.class });
/*  47:179 */       logger.debug("Found Equinox FileLocator for OSGi bundle URL resolution");
/*  48:    */     }
/*  49:    */     catch (Throwable localThrowable)
/*  50:    */     {
/*  51:182 */       equinoxResolveMethod = null;
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55:189 */   private PathMatcher pathMatcher = new AntPathMatcher();
/*  56:    */   
/*  57:    */   public PathMatchingResourcePatternResolver()
/*  58:    */   {
/*  59:198 */     this.resourceLoader = new DefaultResourceLoader();
/*  60:    */   }
/*  61:    */   
/*  62:    */   public PathMatchingResourcePatternResolver(ClassLoader classLoader)
/*  63:    */   {
/*  64:209 */     this.resourceLoader = new DefaultResourceLoader(classLoader);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public PathMatchingResourcePatternResolver(ResourceLoader resourceLoader)
/*  68:    */   {
/*  69:219 */     Assert.notNull(resourceLoader, "ResourceLoader must not be null");
/*  70:220 */     this.resourceLoader = resourceLoader;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public ResourceLoader getResourceLoader()
/*  74:    */   {
/*  75:227 */     return this.resourceLoader;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public ClassLoader getClassLoader()
/*  79:    */   {
/*  80:235 */     return getResourceLoader().getClassLoader();
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void setPathMatcher(PathMatcher pathMatcher)
/*  84:    */   {
/*  85:244 */     Assert.notNull(pathMatcher, "PathMatcher must not be null");
/*  86:245 */     this.pathMatcher = pathMatcher;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public PathMatcher getPathMatcher()
/*  90:    */   {
/*  91:252 */     return this.pathMatcher;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public Resource getResource(String location)
/*  95:    */   {
/*  96:257 */     return getResourceLoader().getResource(location);
/*  97:    */   }
/*  98:    */   
/*  99:    */   public Resource[] getResources(String locationPattern)
/* 100:    */     throws IOException
/* 101:    */   {
/* 102:261 */     Assert.notNull(locationPattern, "Location pattern must not be null");
/* 103:262 */     if (locationPattern.startsWith("classpath*:"))
/* 104:    */     {
/* 105:264 */       if (getPathMatcher().isPattern(locationPattern.substring("classpath*:".length()))) {
/* 106:266 */         return findPathMatchingResources(locationPattern);
/* 107:    */       }
/* 108:270 */       return findAllClassPathResources(locationPattern.substring("classpath*:".length()));
/* 109:    */     }
/* 110:276 */     int prefixEnd = locationPattern.indexOf(":") + 1;
/* 111:277 */     if (getPathMatcher().isPattern(locationPattern.substring(prefixEnd))) {
/* 112:279 */       return findPathMatchingResources(locationPattern);
/* 113:    */     }
/* 114:283 */     return new Resource[] { getResourceLoader().getResource(locationPattern) };
/* 115:    */   }
/* 116:    */   
/* 117:    */   protected Resource[] findAllClassPathResources(String location)
/* 118:    */     throws IOException
/* 119:    */   {
/* 120:297 */     String path = location;
/* 121:298 */     if (path.startsWith("/")) {
/* 122:299 */       path = path.substring(1);
/* 123:    */     }
/* 124:301 */     Enumeration<URL> resourceUrls = getClassLoader().getResources(path);
/* 125:302 */     Set<Resource> result = new LinkedHashSet(16);
/* 126:303 */     while (resourceUrls.hasMoreElements())
/* 127:    */     {
/* 128:304 */       URL url = (URL)resourceUrls.nextElement();
/* 129:305 */       result.add(convertClassLoaderURL(url));
/* 130:    */     }
/* 131:307 */     return (Resource[])result.toArray(new Resource[result.size()]);
/* 132:    */   }
/* 133:    */   
/* 134:    */   protected Resource convertClassLoaderURL(URL url)
/* 135:    */   {
/* 136:319 */     return new UrlResource(url);
/* 137:    */   }
/* 138:    */   
/* 139:    */   protected Resource[] findPathMatchingResources(String locationPattern)
/* 140:    */     throws IOException
/* 141:    */   {
/* 142:334 */     String rootDirPath = determineRootDir(locationPattern);
/* 143:335 */     String subPattern = locationPattern.substring(rootDirPath.length());
/* 144:336 */     Resource[] rootDirResources = getResources(rootDirPath);
/* 145:337 */     Set<Resource> result = new LinkedHashSet(16);
/* 146:338 */     for (Resource rootDirResource : rootDirResources)
/* 147:    */     {
/* 148:339 */       rootDirResource = resolveRootDirResource(rootDirResource);
/* 149:340 */       if (isJarResource(rootDirResource)) {
/* 150:341 */         result.addAll(doFindPathMatchingJarResources(rootDirResource, subPattern));
/* 151:343 */       } else if (rootDirResource.getURL().getProtocol().startsWith("vfs")) {
/* 152:344 */         result.addAll(VfsResourceMatchingDelegate.findMatchingResources(rootDirResource, subPattern, getPathMatcher()));
/* 153:    */       } else {
/* 154:347 */         result.addAll(doFindPathMatchingFileResources(rootDirResource, subPattern));
/* 155:    */       }
/* 156:    */     }
/* 157:350 */     if (logger.isDebugEnabled()) {
/* 158:351 */       logger.debug("Resolved location pattern [" + locationPattern + "] to resources " + result);
/* 159:    */     }
/* 160:353 */     return (Resource[])result.toArray(new Resource[result.size()]);
/* 161:    */   }
/* 162:    */   
/* 163:    */   protected String determineRootDir(String location)
/* 164:    */   {
/* 165:369 */     int prefixEnd = location.indexOf(":") + 1;
/* 166:370 */     int rootDirEnd = location.length();
/* 167:371 */     while ((rootDirEnd > prefixEnd) && (getPathMatcher().isPattern(location.substring(prefixEnd, rootDirEnd)))) {
/* 168:372 */       rootDirEnd = location.lastIndexOf('/', rootDirEnd - 2) + 1;
/* 169:    */     }
/* 170:374 */     if (rootDirEnd == 0) {
/* 171:375 */       rootDirEnd = prefixEnd;
/* 172:    */     }
/* 173:377 */     return location.substring(0, rootDirEnd);
/* 174:    */   }
/* 175:    */   
/* 176:    */   protected Resource resolveRootDirResource(Resource original)
/* 177:    */     throws IOException
/* 178:    */   {
/* 179:390 */     if (equinoxResolveMethod != null)
/* 180:    */     {
/* 181:391 */       URL url = original.getURL();
/* 182:392 */       if (url.getProtocol().startsWith("bundle")) {
/* 183:393 */         return new UrlResource((URL)ReflectionUtils.invokeMethod(equinoxResolveMethod, null, new Object[] { url }));
/* 184:    */       }
/* 185:    */     }
/* 186:396 */     return original;
/* 187:    */   }
/* 188:    */   
/* 189:    */   protected boolean isJarResource(Resource resource)
/* 190:    */     throws IOException
/* 191:    */   {
/* 192:411 */     return ResourceUtils.isJarURL(resource.getURL());
/* 193:    */   }
/* 194:    */   
/* 195:    */   protected Set<Resource> doFindPathMatchingJarResources(Resource rootDirResource, String subPattern)
/* 196:    */     throws IOException
/* 197:    */   {
/* 198:427 */     URLConnection con = rootDirResource.getURL().openConnection();
/* 199:    */     
/* 200:    */ 
/* 201:    */ 
/* 202:431 */     boolean newJarFile = false;
/* 203:    */     String rootEntryPath;
/* 204:    */     JarFile jarFile;
/* 205:    */     String jarFileUrl;
/* 206:    */     String rootEntryPath;
/* 207:433 */     if ((con instanceof JarURLConnection))
/* 208:    */     {
/* 209:435 */       JarURLConnection jarCon = (JarURLConnection)con;
/* 210:436 */       jarCon.setUseCaches(false);
/* 211:437 */       JarFile jarFile = jarCon.getJarFile();
/* 212:438 */       String jarFileUrl = jarCon.getJarFileURL().toExternalForm();
/* 213:439 */       JarEntry jarEntry = jarCon.getJarEntry();
/* 214:440 */       rootEntryPath = jarEntry != null ? jarEntry.getName() : "";
/* 215:    */     }
/* 216:    */     else
/* 217:    */     {
/* 218:447 */       String urlFile = rootDirResource.getURL().getFile();
/* 219:448 */       int separatorIndex = urlFile.indexOf("!/");
/* 220:    */       JarFile jarFile;
/* 221:449 */       if (separatorIndex != -1)
/* 222:    */       {
/* 223:450 */         String jarFileUrl = urlFile.substring(0, separatorIndex);
/* 224:451 */         String rootEntryPath = urlFile.substring(separatorIndex + "!/".length());
/* 225:452 */         jarFile = getJarFile(jarFileUrl);
/* 226:    */       }
/* 227:    */       else
/* 228:    */       {
/* 229:455 */         jarFile = new JarFile(urlFile);
/* 230:456 */         jarFileUrl = urlFile;
/* 231:457 */         rootEntryPath = "";
/* 232:    */       }
/* 233:459 */       newJarFile = true;
/* 234:    */     }
/* 235:    */     try
/* 236:    */     {
/* 237:463 */       if (logger.isDebugEnabled()) {
/* 238:464 */         logger.debug("Looking for matching resources in jar file [" + jarFileUrl + "]");
/* 239:    */       }
/* 240:466 */       if ((!"".equals(rootEntryPath)) && (!rootEntryPath.endsWith("/"))) {
/* 241:469 */         rootEntryPath = rootEntryPath + "/";
/* 242:    */       }
/* 243:471 */       Set<Resource> result = new LinkedHashSet(8);
/* 244:472 */       for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();)
/* 245:    */       {
/* 246:473 */         JarEntry entry = (JarEntry)entries.nextElement();
/* 247:474 */         String entryPath = entry.getName();
/* 248:475 */         if (entryPath.startsWith(rootEntryPath))
/* 249:    */         {
/* 250:476 */           String relativePath = entryPath.substring(rootEntryPath.length());
/* 251:477 */           if (getPathMatcher().match(subPattern, relativePath)) {
/* 252:478 */             result.add(rootDirResource.createRelative(relativePath));
/* 253:    */           }
/* 254:    */         }
/* 255:    */       }
/* 256:482 */       return result;
/* 257:    */     }
/* 258:    */     finally
/* 259:    */     {
/* 260:487 */       if (newJarFile) {
/* 261:488 */         jarFile.close();
/* 262:    */       }
/* 263:    */     }
/* 264:    */   }
/* 265:    */   
/* 266:    */   protected JarFile getJarFile(String jarFileUrl)
/* 267:    */     throws IOException
/* 268:    */   {
/* 269:497 */     if (jarFileUrl.startsWith("file:")) {
/* 270:    */       try
/* 271:    */       {
/* 272:499 */         return new JarFile(ResourceUtils.toURI(jarFileUrl).getSchemeSpecificPart());
/* 273:    */       }
/* 274:    */       catch (URISyntaxException localURISyntaxException)
/* 275:    */       {
/* 276:503 */         return new JarFile(jarFileUrl.substring("file:".length()));
/* 277:    */       }
/* 278:    */     }
/* 279:507 */     return new JarFile(jarFileUrl);
/* 280:    */   }
/* 281:    */   
/* 282:    */   protected Set<Resource> doFindPathMatchingFileResources(Resource rootDirResource, String subPattern)
/* 283:    */     throws IOException
/* 284:    */   {
/* 285:    */     try
/* 286:    */     {
/* 287:526 */       rootDir = rootDirResource.getFile().getAbsoluteFile();
/* 288:    */     }
/* 289:    */     catch (IOException ex)
/* 290:    */     {
/* 291:    */       File rootDir;
/* 292:529 */       if (logger.isWarnEnabled()) {
/* 293:530 */         logger.warn("Cannot search for matching files underneath " + rootDirResource + 
/* 294:531 */           " because it does not correspond to a directory in the file system", ex);
/* 295:    */       }
/* 296:533 */       return Collections.emptySet();
/* 297:    */     }
/* 298:    */     File rootDir;
/* 299:535 */     return doFindMatchingFileSystemResources(rootDir, subPattern);
/* 300:    */   }
/* 301:    */   
/* 302:    */   protected Set<Resource> doFindMatchingFileSystemResources(File rootDir, String subPattern)
/* 303:    */     throws IOException
/* 304:    */   {
/* 305:549 */     if (logger.isDebugEnabled()) {
/* 306:550 */       logger.debug("Looking for matching resources in directory tree [" + rootDir.getPath() + "]");
/* 307:    */     }
/* 308:552 */     Set<File> matchingFiles = retrieveMatchingFiles(rootDir, subPattern);
/* 309:553 */     Set<Resource> result = new LinkedHashSet(matchingFiles.size());
/* 310:554 */     for (File file : matchingFiles) {
/* 311:555 */       result.add(new FileSystemResource(file));
/* 312:    */     }
/* 313:557 */     return result;
/* 314:    */   }
/* 315:    */   
/* 316:    */   protected Set<File> retrieveMatchingFiles(File rootDir, String pattern)
/* 317:    */     throws IOException
/* 318:    */   {
/* 319:570 */     if (!rootDir.exists())
/* 320:    */     {
/* 321:572 */       if (logger.isDebugEnabled()) {
/* 322:573 */         logger.debug("Skipping [" + rootDir.getAbsolutePath() + "] because it does not exist");
/* 323:    */       }
/* 324:575 */       return Collections.emptySet();
/* 325:    */     }
/* 326:577 */     if (!rootDir.isDirectory())
/* 327:    */     {
/* 328:579 */       if (logger.isWarnEnabled()) {
/* 329:580 */         logger.warn("Skipping [" + rootDir.getAbsolutePath() + "] because it does not denote a directory");
/* 330:    */       }
/* 331:582 */       return Collections.emptySet();
/* 332:    */     }
/* 333:584 */     if (!rootDir.canRead())
/* 334:    */     {
/* 335:585 */       if (logger.isWarnEnabled()) {
/* 336:586 */         logger.warn("Cannot search for matching files underneath directory [" + rootDir.getAbsolutePath() + 
/* 337:587 */           "] because the application is not allowed to read the directory");
/* 338:    */       }
/* 339:589 */       return Collections.emptySet();
/* 340:    */     }
/* 341:591 */     String fullPattern = StringUtils.replace(rootDir.getAbsolutePath(), File.separator, "/");
/* 342:592 */     if (!pattern.startsWith("/")) {
/* 343:593 */       fullPattern = fullPattern + "/";
/* 344:    */     }
/* 345:595 */     fullPattern = fullPattern + StringUtils.replace(pattern, File.separator, "/");
/* 346:596 */     Set<File> result = new LinkedHashSet(8);
/* 347:597 */     doRetrieveMatchingFiles(fullPattern, rootDir, result);
/* 348:598 */     return result;
/* 349:    */   }
/* 350:    */   
/* 351:    */   protected void doRetrieveMatchingFiles(String fullPattern, File dir, Set<File> result)
/* 352:    */     throws IOException
/* 353:    */   {
/* 354:611 */     if (logger.isDebugEnabled()) {
/* 355:612 */       logger.debug("Searching directory [" + dir.getAbsolutePath() + 
/* 356:613 */         "] for files matching pattern [" + fullPattern + "]");
/* 357:    */     }
/* 358:615 */     File[] dirContents = dir.listFiles();
/* 359:616 */     if (dirContents == null)
/* 360:    */     {
/* 361:617 */       if (logger.isWarnEnabled()) {
/* 362:618 */         logger.warn("Could not retrieve contents of directory [" + dir.getAbsolutePath() + "]");
/* 363:    */       }
/* 364:620 */       return;
/* 365:    */     }
/* 366:622 */     for (File content : dirContents)
/* 367:    */     {
/* 368:623 */       String currPath = StringUtils.replace(content.getAbsolutePath(), File.separator, "/");
/* 369:624 */       if ((content.isDirectory()) && (getPathMatcher().matchStart(fullPattern, currPath + "/"))) {
/* 370:625 */         if (!content.canRead())
/* 371:    */         {
/* 372:626 */           if (logger.isDebugEnabled()) {
/* 373:627 */             logger.debug("Skipping subdirectory [" + dir.getAbsolutePath() + 
/* 374:628 */               "] because the application is not allowed to read the directory");
/* 375:    */           }
/* 376:    */         }
/* 377:    */         else {
/* 378:632 */           doRetrieveMatchingFiles(fullPattern, content, result);
/* 379:    */         }
/* 380:    */       }
/* 381:635 */       if (getPathMatcher().match(fullPattern, currPath)) {
/* 382:636 */         result.add(content);
/* 383:    */       }
/* 384:    */     }
/* 385:    */   }
/* 386:    */   
/* 387:    */   private static class VfsResourceMatchingDelegate
/* 388:    */   {
/* 389:    */     public static Set<Resource> findMatchingResources(Resource rootResource, String locationPattern, PathMatcher pathMatcher)
/* 390:    */       throws IOException
/* 391:    */     {
/* 392:649 */       Object root = VfsPatternUtils.findRoot(rootResource.getURL());
/* 393:650 */       PathMatchingResourcePatternResolver.PatternVirtualFileVisitor visitor = 
/* 394:651 */         new PathMatchingResourcePatternResolver.PatternVirtualFileVisitor(VfsPatternUtils.getPath(root), locationPattern, pathMatcher);
/* 395:652 */       VfsPatternUtils.visit(root, visitor);
/* 396:653 */       return visitor.getResources();
/* 397:    */     }
/* 398:    */   }
/* 399:    */   
/* 400:    */   private static class PatternVirtualFileVisitor
/* 401:    */     implements InvocationHandler
/* 402:    */   {
/* 403:    */     private final String subPattern;
/* 404:    */     private final PathMatcher pathMatcher;
/* 405:    */     private final String rootPath;
/* 406:669 */     private final Set<Resource> resources = new LinkedHashSet();
/* 407:    */     
/* 408:    */     public PatternVirtualFileVisitor(String rootPath, String subPattern, PathMatcher pathMatcher)
/* 409:    */     {
/* 410:672 */       this.subPattern = subPattern;
/* 411:673 */       this.pathMatcher = pathMatcher;
/* 412:674 */       this.rootPath = (rootPath + "/");
/* 413:    */     }
/* 414:    */     
/* 415:    */     public Object invoke(Object proxy, Method method, Object[] args)
/* 416:    */       throws Throwable
/* 417:    */     {
/* 418:678 */       String methodName = method.getName();
/* 419:679 */       if (Object.class.equals(method.getDeclaringClass()))
/* 420:    */       {
/* 421:680 */         if (methodName.equals("equals"))
/* 422:    */         {
/* 423:682 */           if (proxy == args[0]) {
/* 424:682 */             return Boolean.valueOf(true);
/* 425:    */           }
/* 426:682 */           return Boolean.valueOf(false);
/* 427:    */         }
/* 428:684 */         if (methodName.equals("hashCode")) {
/* 429:685 */           return Integer.valueOf(System.identityHashCode(proxy));
/* 430:    */         }
/* 431:    */       }
/* 432:    */       else
/* 433:    */       {
/* 434:688 */         if ("getAttributes".equals(methodName)) {
/* 435:689 */           return getAttributes();
/* 436:    */         }
/* 437:691 */         if ("visit".equals(methodName))
/* 438:    */         {
/* 439:692 */           visit(args[0]);
/* 440:693 */           return null;
/* 441:    */         }
/* 442:695 */         if ("toString".equals(methodName)) {
/* 443:696 */           return toString();
/* 444:    */         }
/* 445:    */       }
/* 446:699 */       throw new IllegalStateException("Unexpected method invocation: " + method);
/* 447:    */     }
/* 448:    */     
/* 449:    */     public void visit(Object vfsResource)
/* 450:    */     {
/* 451:703 */       if (this.pathMatcher.match(this.subPattern, 
/* 452:704 */         VfsPatternUtils.getPath(vfsResource).substring(this.rootPath.length()))) {
/* 453:705 */         this.resources.add(new VfsResource(vfsResource));
/* 454:    */       }
/* 455:    */     }
/* 456:    */     
/* 457:    */     public Object getAttributes()
/* 458:    */     {
/* 459:710 */       return VfsPatternUtils.getVisitorAttribute();
/* 460:    */     }
/* 461:    */     
/* 462:    */     public Set<Resource> getResources()
/* 463:    */     {
/* 464:714 */       return this.resources;
/* 465:    */     }
/* 466:    */     
/* 467:    */     public int size()
/* 468:    */     {
/* 469:719 */       return this.resources.size();
/* 470:    */     }
/* 471:    */     
/* 472:    */     public String toString()
/* 473:    */     {
/* 474:723 */       StringBuilder sb = new StringBuilder();
/* 475:724 */       sb.append("sub-pattern: ").append(this.subPattern);
/* 476:725 */       sb.append(", resources: ").append(this.resources);
/* 477:726 */       return sb.toString();
/* 478:    */     }
/* 479:    */   }
/* 480:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.support.PathMatchingResourcePatternResolver
 * JD-Core Version:    0.7.0.1
 */