/*   1:    */ package org.springframework.beans.factory.xml;
/*   2:    */ 
/*   3:    */ import java.io.FileNotFoundException;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.util.Map;
/*   6:    */ import java.util.Properties;
/*   7:    */ import java.util.concurrent.ConcurrentHashMap;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.apache.commons.logging.LogFactory;
/*  10:    */ import org.springframework.core.io.ClassPathResource;
/*  11:    */ import org.springframework.core.io.Resource;
/*  12:    */ import org.springframework.core.io.support.PropertiesLoaderUtils;
/*  13:    */ import org.springframework.util.Assert;
/*  14:    */ import org.springframework.util.CollectionUtils;
/*  15:    */ import org.xml.sax.EntityResolver;
/*  16:    */ import org.xml.sax.InputSource;
/*  17:    */ 
/*  18:    */ public class PluggableSchemaResolver
/*  19:    */   implements EntityResolver
/*  20:    */ {
/*  21:    */   public static final String DEFAULT_SCHEMA_MAPPINGS_LOCATION = "META-INF/spring.schemas";
/*  22: 66 */   private static final Log logger = LogFactory.getLog(PluggableSchemaResolver.class);
/*  23:    */   private final ClassLoader classLoader;
/*  24:    */   private final String schemaMappingsLocation;
/*  25:    */   private volatile Map<String, String> schemaMappings;
/*  26:    */   
/*  27:    */   public PluggableSchemaResolver(ClassLoader classLoader)
/*  28:    */   {
/*  29: 84 */     this.classLoader = classLoader;
/*  30: 85 */     this.schemaMappingsLocation = "META-INF/spring.schemas";
/*  31:    */   }
/*  32:    */   
/*  33:    */   public PluggableSchemaResolver(ClassLoader classLoader, String schemaMappingsLocation)
/*  34:    */   {
/*  35: 98 */     Assert.hasText(schemaMappingsLocation, "'schemaMappingsLocation' must not be empty");
/*  36: 99 */     this.classLoader = classLoader;
/*  37:100 */     this.schemaMappingsLocation = schemaMappingsLocation;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public InputSource resolveEntity(String publicId, String systemId)
/*  41:    */     throws IOException
/*  42:    */   {
/*  43:104 */     if (logger.isTraceEnabled()) {
/*  44:105 */       logger.trace("Trying to resolve XML entity with public id [" + publicId + 
/*  45:106 */         "] and system id [" + systemId + "]");
/*  46:    */     }
/*  47:109 */     if (systemId != null)
/*  48:    */     {
/*  49:110 */       String resourceLocation = (String)getSchemaMappings().get(systemId);
/*  50:111 */       if (resourceLocation != null)
/*  51:    */       {
/*  52:112 */         Resource resource = new ClassPathResource(resourceLocation, this.classLoader);
/*  53:    */         try
/*  54:    */         {
/*  55:114 */           InputSource source = new InputSource(resource.getInputStream());
/*  56:115 */           source.setPublicId(publicId);
/*  57:116 */           source.setSystemId(systemId);
/*  58:117 */           if (logger.isDebugEnabled()) {
/*  59:118 */             logger.debug("Found XML schema [" + systemId + "] in classpath: " + resourceLocation);
/*  60:    */           }
/*  61:120 */           return source;
/*  62:    */         }
/*  63:    */         catch (FileNotFoundException ex)
/*  64:    */         {
/*  65:123 */           if (logger.isDebugEnabled()) {
/*  66:124 */             logger.debug("Couldn't find XML schema [" + systemId + "]: " + resource, ex);
/*  67:    */           }
/*  68:    */         }
/*  69:    */       }
/*  70:    */     }
/*  71:129 */     return null;
/*  72:    */   }
/*  73:    */   
/*  74:    */   private Map<String, String> getSchemaMappings()
/*  75:    */   {
/*  76:136 */     if (this.schemaMappings == null) {
/*  77:137 */       synchronized (this)
/*  78:    */       {
/*  79:138 */         if (this.schemaMappings == null)
/*  80:    */         {
/*  81:139 */           if (logger.isDebugEnabled()) {
/*  82:140 */             logger.debug("Loading schema mappings from [" + this.schemaMappingsLocation + "]");
/*  83:    */           }
/*  84:    */           try
/*  85:    */           {
/*  86:143 */             Properties mappings = 
/*  87:144 */               PropertiesLoaderUtils.loadAllProperties(this.schemaMappingsLocation, this.classLoader);
/*  88:145 */             if (logger.isDebugEnabled()) {
/*  89:146 */               logger.debug("Loaded schema mappings: " + mappings);
/*  90:    */             }
/*  91:148 */             Map<String, String> schemaMappings = new ConcurrentHashMap();
/*  92:149 */             CollectionUtils.mergePropertiesIntoMap(mappings, schemaMappings);
/*  93:150 */             this.schemaMappings = schemaMappings;
/*  94:    */           }
/*  95:    */           catch (IOException ex)
/*  96:    */           {
/*  97:153 */             throw new IllegalStateException(
/*  98:154 */               "Unable to load schema mappings from location [" + this.schemaMappingsLocation + "]", ex);
/*  99:    */           }
/* 100:    */         }
/* 101:    */       }
/* 102:    */     }
/* 103:159 */     return this.schemaMappings;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public String toString()
/* 107:    */   {
/* 108:165 */     return "EntityResolver using mappings " + getSchemaMappings();
/* 109:    */   }
/* 110:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.PluggableSchemaResolver
 * JD-Core Version:    0.7.0.1
 */