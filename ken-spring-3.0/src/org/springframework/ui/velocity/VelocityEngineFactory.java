/*   1:    */ package org.springframework.ui.velocity;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.Map.Entry;
/*   8:    */ import java.util.Properties;
/*   9:    */ import org.apache.commons.logging.Log;
/*  10:    */ import org.apache.commons.logging.LogFactory;
/*  11:    */ import org.apache.velocity.app.VelocityEngine;
/*  12:    */ import org.apache.velocity.exception.VelocityException;
/*  13:    */ import org.springframework.core.io.DefaultResourceLoader;
/*  14:    */ import org.springframework.core.io.Resource;
/*  15:    */ import org.springframework.core.io.ResourceLoader;
/*  16:    */ import org.springframework.core.io.support.PropertiesLoaderUtils;
/*  17:    */ import org.springframework.util.CollectionUtils;
/*  18:    */ import org.springframework.util.StringUtils;
/*  19:    */ 
/*  20:    */ public class VelocityEngineFactory
/*  21:    */ {
/*  22: 75 */   protected final Log logger = LogFactory.getLog(getClass());
/*  23:    */   private Resource configLocation;
/*  24: 79 */   private final Map<String, Object> velocityProperties = new HashMap();
/*  25:    */   private String resourceLoaderPath;
/*  26: 83 */   private ResourceLoader resourceLoader = new DefaultResourceLoader();
/*  27: 85 */   private boolean preferFileSystemAccess = true;
/*  28: 87 */   private boolean overrideLogging = true;
/*  29:    */   
/*  30:    */   public void setConfigLocation(Resource configLocation)
/*  31:    */   {
/*  32: 97 */     this.configLocation = configLocation;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setVelocityProperties(Properties velocityProperties)
/*  36:    */   {
/*  37:113 */     CollectionUtils.mergePropertiesIntoMap(velocityProperties, this.velocityProperties);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setVelocityPropertiesMap(Map<String, Object> velocityPropertiesMap)
/*  41:    */   {
/*  42:122 */     if (velocityPropertiesMap != null) {
/*  43:123 */       this.velocityProperties.putAll(velocityPropertiesMap);
/*  44:    */     }
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setResourceLoaderPath(String resourceLoaderPath)
/*  48:    */   {
/*  49:155 */     this.resourceLoaderPath = resourceLoaderPath;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setResourceLoader(ResourceLoader resourceLoader)
/*  53:    */   {
/*  54:166 */     this.resourceLoader = resourceLoader;
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected ResourceLoader getResourceLoader()
/*  58:    */   {
/*  59:173 */     return this.resourceLoader;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void setPreferFileSystemAccess(boolean preferFileSystemAccess)
/*  63:    */   {
/*  64:189 */     this.preferFileSystemAccess = preferFileSystemAccess;
/*  65:    */   }
/*  66:    */   
/*  67:    */   protected boolean isPreferFileSystemAccess()
/*  68:    */   {
/*  69:196 */     return this.preferFileSystemAccess;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setOverrideLogging(boolean overrideLogging)
/*  73:    */   {
/*  74:205 */     this.overrideLogging = overrideLogging;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public VelocityEngine createVelocityEngine()
/*  78:    */     throws IOException, VelocityException
/*  79:    */   {
/*  80:216 */     VelocityEngine velocityEngine = newVelocityEngine();
/*  81:217 */     Map<String, Object> props = new HashMap();
/*  82:220 */     if (this.configLocation != null)
/*  83:    */     {
/*  84:221 */       if (this.logger.isInfoEnabled()) {
/*  85:222 */         this.logger.info("Loading Velocity config from [" + this.configLocation + "]");
/*  86:    */       }
/*  87:224 */       CollectionUtils.mergePropertiesIntoMap(PropertiesLoaderUtils.loadProperties(this.configLocation), props);
/*  88:    */     }
/*  89:228 */     if (!this.velocityProperties.isEmpty()) {
/*  90:229 */       props.putAll(this.velocityProperties);
/*  91:    */     }
/*  92:233 */     if (this.resourceLoaderPath != null) {
/*  93:234 */       initVelocityResourceLoader(velocityEngine, this.resourceLoaderPath);
/*  94:    */     }
/*  95:238 */     if (this.overrideLogging) {
/*  96:239 */       velocityEngine.setProperty("runtime.log.logsystem", new CommonsLoggingLogSystem());
/*  97:    */     }
/*  98:243 */     for (Map.Entry<String, Object> entry : props.entrySet()) {
/*  99:244 */       velocityEngine.setProperty((String)entry.getKey(), entry.getValue());
/* 100:    */     }
/* 101:247 */     postProcessVelocityEngine(velocityEngine);
/* 102:    */     try
/* 103:    */     {
/* 104:251 */       velocityEngine.init();
/* 105:    */     }
/* 106:    */     catch (IOException ex)
/* 107:    */     {
/* 108:254 */       throw ex;
/* 109:    */     }
/* 110:    */     catch (VelocityException ex)
/* 111:    */     {
/* 112:257 */       throw ex;
/* 113:    */     }
/* 114:    */     catch (RuntimeException ex)
/* 115:    */     {
/* 116:260 */       throw ex;
/* 117:    */     }
/* 118:    */     catch (Exception ex)
/* 119:    */     {
/* 120:263 */       this.logger.error("Why does VelocityEngine throw a generic checked exception, after all?", ex);
/* 121:264 */       throw new VelocityException(ex.toString());
/* 122:    */     }
/* 123:267 */     return velocityEngine;
/* 124:    */   }
/* 125:    */   
/* 126:    */   protected VelocityEngine newVelocityEngine()
/* 127:    */     throws IOException, VelocityException
/* 128:    */   {
/* 129:280 */     return new VelocityEngine();
/* 130:    */   }
/* 131:    */   
/* 132:    */   protected void initVelocityResourceLoader(VelocityEngine velocityEngine, String resourceLoaderPath)
/* 133:    */   {
/* 134:295 */     if (isPreferFileSystemAccess())
/* 135:    */     {
/* 136:    */       try
/* 137:    */       {
/* 138:299 */         StringBuilder resolvedPath = new StringBuilder();
/* 139:300 */         String[] paths = StringUtils.commaDelimitedListToStringArray(resourceLoaderPath);
/* 140:301 */         for (int i = 0; i < paths.length; i++)
/* 141:    */         {
/* 142:302 */           String path = paths[i];
/* 143:303 */           Resource resource = getResourceLoader().getResource(path);
/* 144:304 */           File file = resource.getFile();
/* 145:305 */           if (this.logger.isDebugEnabled()) {
/* 146:306 */             this.logger.debug("Resource loader path [" + path + "] resolved to file [" + file.getAbsolutePath() + "]");
/* 147:    */           }
/* 148:308 */           resolvedPath.append(file.getAbsolutePath());
/* 149:309 */           if (i < paths.length - 1) {
/* 150:310 */             resolvedPath.append(',');
/* 151:    */           }
/* 152:    */         }
/* 153:313 */         velocityEngine.setProperty("resource.loader", "file");
/* 154:314 */         velocityEngine.setProperty("file.resource.loader.cache", "true");
/* 155:315 */         velocityEngine.setProperty("file.resource.loader.path", resolvedPath.toString());
/* 156:    */       }
/* 157:    */       catch (IOException ex)
/* 158:    */       {
/* 159:318 */         if (this.logger.isDebugEnabled()) {
/* 160:319 */           this.logger.debug("Cannot resolve resource loader path [" + resourceLoaderPath + 
/* 161:320 */             "] to [java.io.File]: using SpringResourceLoader", ex);
/* 162:    */         }
/* 163:322 */         initSpringResourceLoader(velocityEngine, resourceLoaderPath);
/* 164:    */       }
/* 165:    */     }
/* 166:    */     else
/* 167:    */     {
/* 168:328 */       if (this.logger.isDebugEnabled()) {
/* 169:329 */         this.logger.debug("File system access not preferred: using SpringResourceLoader");
/* 170:    */       }
/* 171:331 */       initSpringResourceLoader(velocityEngine, resourceLoaderPath);
/* 172:    */     }
/* 173:    */   }
/* 174:    */   
/* 175:    */   protected void initSpringResourceLoader(VelocityEngine velocityEngine, String resourceLoaderPath)
/* 176:    */   {
/* 177:344 */     velocityEngine.setProperty(
/* 178:345 */       "resource.loader", "spring");
/* 179:346 */     velocityEngine.setProperty(
/* 180:347 */       "spring.resource.loader.class", SpringResourceLoader.class.getName());
/* 181:348 */     velocityEngine.setProperty(
/* 182:349 */       "spring.resource.loader.cache", "true");
/* 183:350 */     velocityEngine.setApplicationAttribute(
/* 184:351 */       "spring.resource.loader", getResourceLoader());
/* 185:352 */     velocityEngine.setApplicationAttribute(
/* 186:353 */       "spring.resource.loader.path", resourceLoaderPath);
/* 187:    */   }
/* 188:    */   
/* 189:    */   protected void postProcessVelocityEngine(VelocityEngine velocityEngine)
/* 190:    */     throws IOException, VelocityException
/* 191:    */   {}
/* 192:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ui.velocity.VelocityEngineFactory
 * JD-Core Version:    0.7.0.1
 */