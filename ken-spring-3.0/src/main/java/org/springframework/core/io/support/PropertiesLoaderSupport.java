/*   1:    */ package org.springframework.core.io.support;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.InputStreamReader;
/*   6:    */ import java.util.Properties;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.apache.commons.logging.LogFactory;
/*   9:    */ import org.springframework.core.io.AbstractFileResolvingResource;
/*  10:    */ import org.springframework.core.io.Resource;
/*  11:    */ import org.springframework.util.CollectionUtils;
/*  12:    */ import org.springframework.util.DefaultPropertiesPersister;
/*  13:    */ import org.springframework.util.PropertiesPersister;
/*  14:    */ 
/*  15:    */ public abstract class PropertiesLoaderSupport
/*  16:    */ {
/*  17:    */   public static final String XML_FILE_EXTENSION = ".xml";
/*  18: 46 */   protected final Log logger = LogFactory.getLog(getClass());
/*  19:    */   protected Properties[] localProperties;
/*  20: 50 */   protected boolean localOverride = false;
/*  21:    */   private Resource[] locations;
/*  22: 54 */   private boolean ignoreResourceNotFound = false;
/*  23:    */   private String fileEncoding;
/*  24: 58 */   private PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
/*  25:    */   
/*  26:    */   public void setProperties(Properties properties)
/*  27:    */   {
/*  28: 67 */     this.localProperties = new Properties[] { properties };
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setPropertiesArray(Properties[] propertiesArray)
/*  32:    */   {
/*  33: 75 */     this.localProperties = propertiesArray;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setLocation(Resource location)
/*  37:    */   {
/*  38: 84 */     this.locations = new Resource[] { location };
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setLocations(Resource[] locations)
/*  42:    */   {
/*  43: 97 */     this.locations = locations;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setLocalOverride(boolean localOverride)
/*  47:    */   {
/*  48:107 */     this.localOverride = localOverride;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound)
/*  52:    */   {
/*  53:116 */     this.ignoreResourceNotFound = ignoreResourceNotFound;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setFileEncoding(String encoding)
/*  57:    */   {
/*  58:127 */     this.fileEncoding = encoding;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void setPropertiesPersister(PropertiesPersister propertiesPersister)
/*  62:    */   {
/*  63:136 */     this.propertiesPersister = 
/*  64:137 */       (propertiesPersister != null ? propertiesPersister : new DefaultPropertiesPersister());
/*  65:    */   }
/*  66:    */   
/*  67:    */   protected Properties mergeProperties()
/*  68:    */     throws IOException
/*  69:    */   {
/*  70:146 */     Properties result = new Properties();
/*  71:148 */     if (this.localOverride) {
/*  72:150 */       loadProperties(result);
/*  73:    */     }
/*  74:153 */     if (this.localProperties != null) {
/*  75:154 */       for (Properties localProp : this.localProperties) {
/*  76:155 */         CollectionUtils.mergePropertiesIntoMap(localProp, result);
/*  77:    */       }
/*  78:    */     }
/*  79:159 */     if (!this.localOverride) {
/*  80:161 */       loadProperties(result);
/*  81:    */     }
/*  82:164 */     return result;
/*  83:    */   }
/*  84:    */   
/*  85:    */   protected void loadProperties(Properties props)
/*  86:    */     throws IOException
/*  87:    */   {
/*  88:174 */     if (this.locations != null) {
/*  89:175 */       for (Resource location : this.locations)
/*  90:    */       {
/*  91:176 */         if (this.logger.isInfoEnabled()) {
/*  92:177 */           this.logger.info("Loading properties file from " + location);
/*  93:    */         }
/*  94:179 */         InputStream is = null;
/*  95:    */         try
/*  96:    */         {
/*  97:181 */           is = location.getInputStream();
/*  98:    */           
/*  99:183 */           String filename = (location instanceof AbstractFileResolvingResource) ? 
/* 100:184 */             location.getFilename() : null;
/* 101:185 */           if ((filename != null) && (filename.endsWith(".xml"))) {
/* 102:186 */             this.propertiesPersister.loadFromXml(props, is);
/* 103:189 */           } else if (this.fileEncoding != null) {
/* 104:190 */             this.propertiesPersister.load(props, new InputStreamReader(is, this.fileEncoding));
/* 105:    */           } else {
/* 106:193 */             this.propertiesPersister.load(props, is);
/* 107:    */           }
/* 108:    */         }
/* 109:    */         catch (IOException ex)
/* 110:    */         {
/* 111:198 */           if (this.ignoreResourceNotFound)
/* 112:    */           {
/* 113:199 */             if (this.logger.isWarnEnabled()) {
/* 114:200 */               this.logger.warn("Could not load properties from " + location + ": " + ex.getMessage());
/* 115:    */             }
/* 116:    */           }
/* 117:    */           else {
/* 118:204 */             throw ex;
/* 119:    */           }
/* 120:    */         }
/* 121:    */         finally
/* 122:    */         {
/* 123:208 */           if (is != null) {
/* 124:209 */             is.close();
/* 125:    */           }
/* 126:    */         }
/* 127:    */       }
/* 128:    */     }
/* 129:    */   }
/* 130:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.support.PropertiesLoaderSupport
 * JD-Core Version:    0.7.0.1
 */