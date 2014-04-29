/*   1:    */ package org.springframework.web.context.support;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.Enumeration;
/*   5:    */ import java.util.LinkedHashSet;
/*   6:    */ import java.util.Set;
/*   7:    */ import java.util.jar.JarEntry;
/*   8:    */ import java.util.jar.JarFile;
/*   9:    */ import javax.servlet.ServletContext;
/*  10:    */ import org.apache.commons.logging.Log;
/*  11:    */ import org.apache.commons.logging.LogFactory;
/*  12:    */ import org.springframework.core.io.Resource;
/*  13:    */ import org.springframework.core.io.ResourceLoader;
/*  14:    */ import org.springframework.core.io.UrlResource;
/*  15:    */ import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
/*  16:    */ import org.springframework.util.PathMatcher;
/*  17:    */ import org.springframework.util.StringUtils;
/*  18:    */ 
/*  19:    */ public class ServletContextResourcePatternResolver
/*  20:    */   extends PathMatchingResourcePatternResolver
/*  21:    */ {
/*  22: 48 */   private static final Log logger = LogFactory.getLog(ServletContextResourcePatternResolver.class);
/*  23:    */   
/*  24:    */   public ServletContextResourcePatternResolver(ServletContext servletContext)
/*  25:    */   {
/*  26: 57 */     super(new ServletContextResourceLoader(servletContext));
/*  27:    */   }
/*  28:    */   
/*  29:    */   public ServletContextResourcePatternResolver(ResourceLoader resourceLoader)
/*  30:    */   {
/*  31: 66 */     super(resourceLoader);
/*  32:    */   }
/*  33:    */   
/*  34:    */   protected Set<Resource> doFindPathMatchingFileResources(Resource rootDirResource, String subPattern)
/*  35:    */     throws IOException
/*  36:    */   {
/*  37: 83 */     if ((rootDirResource instanceof ServletContextResource))
/*  38:    */     {
/*  39: 84 */       ServletContextResource scResource = (ServletContextResource)rootDirResource;
/*  40: 85 */       ServletContext sc = scResource.getServletContext();
/*  41: 86 */       String fullPattern = scResource.getPath() + subPattern;
/*  42: 87 */       Set<Resource> result = new LinkedHashSet(8);
/*  43: 88 */       doRetrieveMatchingServletContextResources(sc, fullPattern, scResource.getPath(), result);
/*  44: 89 */       return result;
/*  45:    */     }
/*  46: 92 */     return super.doFindPathMatchingFileResources(rootDirResource, subPattern);
/*  47:    */   }
/*  48:    */   
/*  49:    */   protected void doRetrieveMatchingServletContextResources(ServletContext servletContext, String fullPattern, String dir, Set<Resource> result)
/*  50:    */     throws IOException
/*  51:    */   {
/*  52:112 */     Set candidates = servletContext.getResourcePaths(dir);
/*  53:113 */     if (candidates != null)
/*  54:    */     {
/*  55:114 */       boolean dirDepthNotFixed = fullPattern.contains("**");
/*  56:115 */       int jarFileSep = fullPattern.indexOf("!/");
/*  57:116 */       String jarFilePath = null;
/*  58:117 */       String pathInJarFile = null;
/*  59:118 */       if ((jarFileSep > 0) && (jarFileSep + "!/".length() < fullPattern.length()))
/*  60:    */       {
/*  61:119 */         jarFilePath = fullPattern.substring(0, jarFileSep);
/*  62:120 */         pathInJarFile = fullPattern.substring(jarFileSep + "!/".length());
/*  63:    */       }
/*  64:122 */       for (Object candidate : candidates)
/*  65:    */       {
/*  66:123 */         String currPath = (String)candidate;
/*  67:124 */         if (!currPath.startsWith(dir))
/*  68:    */         {
/*  69:127 */           int dirIndex = currPath.indexOf(dir);
/*  70:128 */           if (dirIndex != -1) {
/*  71:129 */             currPath = currPath.substring(dirIndex);
/*  72:    */           }
/*  73:    */         }
/*  74:132 */         if ((currPath.endsWith("/")) && ((dirDepthNotFixed) || 
/*  75:133 */           (StringUtils.countOccurrencesOf(currPath, "/") <= StringUtils.countOccurrencesOf(fullPattern, "/")))) {
/*  76:136 */           doRetrieveMatchingServletContextResources(servletContext, fullPattern, currPath, result);
/*  77:    */         }
/*  78:138 */         if ((jarFilePath != null) && (getPathMatcher().match(jarFilePath, currPath)))
/*  79:    */         {
/*  80:140 */           String absoluteJarPath = servletContext.getRealPath(currPath);
/*  81:141 */           if (absoluteJarPath != null) {
/*  82:142 */             doRetrieveMatchingJarEntries(absoluteJarPath, pathInJarFile, result);
/*  83:    */           }
/*  84:    */         }
/*  85:145 */         if (getPathMatcher().match(fullPattern, currPath)) {
/*  86:146 */           result.add(new ServletContextResource(servletContext, currPath));
/*  87:    */         }
/*  88:    */       }
/*  89:    */     }
/*  90:    */   }
/*  91:    */   
/*  92:    */   private void doRetrieveMatchingJarEntries(String jarFilePath, String entryPattern, Set<Resource> result)
/*  93:    */   {
/*  94:161 */     if (logger.isDebugEnabled()) {
/*  95:162 */       logger.debug("Searching jar file [" + jarFilePath + "] for entries matching [" + entryPattern + "]");
/*  96:    */     }
/*  97:    */     try
/*  98:    */     {
/*  99:165 */       JarFile jarFile = new JarFile(jarFilePath);
/* 100:166 */       for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();)
/* 101:    */       {
/* 102:167 */         JarEntry entry = (JarEntry)entries.nextElement();
/* 103:168 */         String entryPath = entry.getName();
/* 104:169 */         if (getPathMatcher().match(entryPattern, entryPath)) {
/* 105:170 */           result.add(new UrlResource("jar:file:" + 
/* 106:171 */             jarFilePath + 
/* 107:172 */             "!/" + entryPath));
/* 108:    */         }
/* 109:    */       }
/* 110:    */     }
/* 111:    */     catch (IOException ex)
/* 112:    */     {
/* 113:177 */       if (logger.isWarnEnabled()) {
/* 114:178 */         logger.warn("Cannot search for matching resources in jar file [" + jarFilePath + 
/* 115:179 */           "] because the jar cannot be opened through the file system", ex);
/* 116:    */       }
/* 117:    */     }
/* 118:    */   }
/* 119:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.support.ServletContextResourcePatternResolver
 * JD-Core Version:    0.7.0.1
 */