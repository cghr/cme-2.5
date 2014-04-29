/*   1:    */ package org.springframework.beans.factory.xml;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.net.URL;
/*   6:    */ import java.net.URLDecoder;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.apache.commons.logging.LogFactory;
/*   9:    */ import org.springframework.core.io.Resource;
/*  10:    */ import org.springframework.core.io.ResourceLoader;
/*  11:    */ import org.xml.sax.InputSource;
/*  12:    */ import org.xml.sax.SAXException;
/*  13:    */ 
/*  14:    */ public class ResourceEntityResolver
/*  15:    */   extends DelegatingEntityResolver
/*  16:    */ {
/*  17: 55 */   private static final Log logger = LogFactory.getLog(ResourceEntityResolver.class);
/*  18:    */   private final ResourceLoader resourceLoader;
/*  19:    */   
/*  20:    */   public ResourceEntityResolver(ResourceLoader resourceLoader)
/*  21:    */   {
/*  22: 67 */     super(resourceLoader.getClassLoader());
/*  23: 68 */     this.resourceLoader = resourceLoader;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public InputSource resolveEntity(String publicId, String systemId)
/*  27:    */     throws SAXException, IOException
/*  28:    */   {
/*  29: 74 */     InputSource source = super.resolveEntity(publicId, systemId);
/*  30: 75 */     if ((source == null) && (systemId != null))
/*  31:    */     {
/*  32: 76 */       String resourcePath = null;
/*  33:    */       try
/*  34:    */       {
/*  35: 78 */         String decodedSystemId = URLDecoder.decode(systemId);
/*  36: 79 */         String givenUrl = new URL(decodedSystemId).toString();
/*  37: 80 */         String systemRootUrl = new File("").toURL().toString();
/*  38: 82 */         if (givenUrl.startsWith(systemRootUrl)) {
/*  39: 83 */           resourcePath = givenUrl.substring(systemRootUrl.length());
/*  40:    */         }
/*  41:    */       }
/*  42:    */       catch (Exception ex)
/*  43:    */       {
/*  44: 88 */         if (logger.isDebugEnabled()) {
/*  45: 89 */           logger.debug("Could not resolve XML entity [" + systemId + "] against system root URL", ex);
/*  46:    */         }
/*  47: 92 */         resourcePath = systemId;
/*  48:    */       }
/*  49: 94 */       if (resourcePath != null)
/*  50:    */       {
/*  51: 95 */         if (logger.isTraceEnabled()) {
/*  52: 96 */           logger.trace("Trying to locate XML entity [" + systemId + "] as resource [" + resourcePath + "]");
/*  53:    */         }
/*  54: 98 */         Resource resource = this.resourceLoader.getResource(resourcePath);
/*  55: 99 */         source = new InputSource(resource.getInputStream());
/*  56:100 */         source.setPublicId(publicId);
/*  57:101 */         source.setSystemId(systemId);
/*  58:102 */         if (logger.isDebugEnabled()) {
/*  59:103 */           logger.debug("Found XML entity [" + systemId + "]: " + resource);
/*  60:    */         }
/*  61:    */       }
/*  62:    */     }
/*  63:107 */     return source;
/*  64:    */   }
/*  65:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.ResourceEntityResolver
 * JD-Core Version:    0.7.0.1
 */