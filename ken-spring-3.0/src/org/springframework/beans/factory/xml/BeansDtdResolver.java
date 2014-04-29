/*  1:   */ package org.springframework.beans.factory.xml;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.util.Arrays;
/*  5:   */ import org.apache.commons.logging.Log;
/*  6:   */ import org.apache.commons.logging.LogFactory;
/*  7:   */ import org.springframework.core.io.ClassPathResource;
/*  8:   */ import org.springframework.core.io.Resource;
/*  9:   */ import org.xml.sax.EntityResolver;
/* 10:   */ import org.xml.sax.InputSource;
/* 11:   */ 
/* 12:   */ public class BeansDtdResolver
/* 13:   */   implements EntityResolver
/* 14:   */ {
/* 15:   */   private static final String DTD_EXTENSION = ".dtd";
/* 16:48 */   private static final String[] DTD_NAMES = { "spring-beans-2.0", "spring-beans" };
/* 17:50 */   private static final Log logger = LogFactory.getLog(BeansDtdResolver.class);
/* 18:   */   
/* 19:   */   public InputSource resolveEntity(String publicId, String systemId)
/* 20:   */     throws IOException
/* 21:   */   {
/* 22:54 */     if (logger.isTraceEnabled()) {
/* 23:55 */       logger.trace("Trying to resolve XML entity with public ID [" + publicId + 
/* 24:56 */         "] and system ID [" + systemId + "]");
/* 25:   */     }
/* 26:58 */     if ((systemId != null) && (systemId.endsWith(".dtd")))
/* 27:   */     {
/* 28:59 */       int lastPathSeparator = systemId.lastIndexOf("/");
/* 29:60 */       for (String DTD_NAME : DTD_NAMES)
/* 30:   */       {
/* 31:61 */         int dtdNameStart = systemId.indexOf(DTD_NAME);
/* 32:62 */         if (dtdNameStart > lastPathSeparator)
/* 33:   */         {
/* 34:63 */           String dtdFile = systemId.substring(dtdNameStart);
/* 35:64 */           if (logger.isTraceEnabled()) {
/* 36:65 */             logger.trace("Trying to locate [" + dtdFile + "] in Spring jar");
/* 37:   */           }
/* 38:   */           try
/* 39:   */           {
/* 40:68 */             Resource resource = new ClassPathResource(dtdFile, getClass());
/* 41:69 */             InputSource source = new InputSource(resource.getInputStream());
/* 42:70 */             source.setPublicId(publicId);
/* 43:71 */             source.setSystemId(systemId);
/* 44:72 */             if (logger.isDebugEnabled()) {
/* 45:73 */               logger.debug("Found beans DTD [" + systemId + "] in classpath: " + dtdFile);
/* 46:   */             }
/* 47:75 */             return source;
/* 48:   */           }
/* 49:   */           catch (IOException ex)
/* 50:   */           {
/* 51:78 */             if (logger.isDebugEnabled()) {
/* 52:79 */               logger.debug("Could not resolve beans DTD [" + systemId + "]: not found in class path", ex);
/* 53:   */             }
/* 54:   */           }
/* 55:   */         }
/* 56:   */       }
/* 57:   */     }
/* 58:88 */     return null;
/* 59:   */   }
/* 60:   */   
/* 61:   */   public String toString()
/* 62:   */   {
/* 63:94 */     return "EntityResolver for DTDs " + Arrays.toString(DTD_NAMES);
/* 64:   */   }
/* 65:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.BeansDtdResolver
 * JD-Core Version:    0.7.0.1
 */