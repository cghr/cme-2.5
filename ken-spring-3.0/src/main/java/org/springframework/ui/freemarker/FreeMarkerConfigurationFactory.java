/*   1:    */ package org.springframework.ui.freemarker;
/*   2:    */ 
/*   3:    */ import freemarker.cache.FileTemplateLoader;
/*   4:    */ import freemarker.cache.MultiTemplateLoader;
/*   5:    */ import freemarker.cache.TemplateLoader;
/*   6:    */ import freemarker.template.Configuration;
/*   7:    */ import freemarker.template.SimpleHash;
/*   8:    */ import freemarker.template.TemplateException;
/*   9:    */ import java.io.File;
/*  10:    */ import java.io.IOException;
/*  11:    */ import java.util.ArrayList;
/*  12:    */ import java.util.Arrays;
/*  13:    */ import java.util.Collection;
/*  14:    */ import java.util.List;
/*  15:    */ import java.util.Map;
/*  16:    */ import java.util.Properties;
/*  17:    */ import org.apache.commons.logging.Log;
/*  18:    */ import org.apache.commons.logging.LogFactory;
/*  19:    */ import org.springframework.core.io.DefaultResourceLoader;
/*  20:    */ import org.springframework.core.io.Resource;
/*  21:    */ import org.springframework.core.io.ResourceLoader;
/*  22:    */ import org.springframework.core.io.support.PropertiesLoaderUtils;
/*  23:    */ import org.springframework.util.CollectionUtils;
/*  24:    */ 
/*  25:    */ public class FreeMarkerConfigurationFactory
/*  26:    */ {
/*  27: 77 */   protected final Log logger = LogFactory.getLog(getClass());
/*  28:    */   private Resource configLocation;
/*  29:    */   private Properties freemarkerSettings;
/*  30:    */   private Map<String, Object> freemarkerVariables;
/*  31:    */   private String defaultEncoding;
/*  32: 87 */   private final List<TemplateLoader> templateLoaders = new ArrayList();
/*  33:    */   private List<TemplateLoader> preTemplateLoaders;
/*  34:    */   private List<TemplateLoader> postTemplateLoaders;
/*  35:    */   private String[] templateLoaderPaths;
/*  36: 95 */   private ResourceLoader resourceLoader = new DefaultResourceLoader();
/*  37: 97 */   private boolean preferFileSystemAccess = true;
/*  38:    */   
/*  39:    */   public void setConfigLocation(Resource resource)
/*  40:    */   {
/*  41:107 */     this.configLocation = resource;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setFreemarkerSettings(Properties settings)
/*  45:    */   {
/*  46:116 */     this.freemarkerSettings = settings;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void setFreemarkerVariables(Map<String, Object> variables)
/*  50:    */   {
/*  51:125 */     this.freemarkerVariables = variables;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setDefaultEncoding(String defaultEncoding)
/*  55:    */   {
/*  56:137 */     this.defaultEncoding = defaultEncoding;
/*  57:    */   }
/*  58:    */   
/*  59:    */   @Deprecated
/*  60:    */   public void setTemplateLoaders(TemplateLoader[] templateLoaders)
/*  61:    */   {
/*  62:151 */     if (templateLoaders != null) {
/*  63:152 */       this.templateLoaders.addAll((Collection)Arrays.asList(templateLoaders));
/*  64:    */     }
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setPreTemplateLoaders(TemplateLoader[] preTemplateLoaders)
/*  68:    */   {
/*  69:168 */     this.preTemplateLoaders = Arrays.asList(preTemplateLoaders);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setPostTemplateLoaders(TemplateLoader[] postTemplateLoaders)
/*  73:    */   {
/*  74:183 */     this.postTemplateLoaders = Arrays.asList(postTemplateLoaders);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void setTemplateLoaderPath(String templateLoaderPath)
/*  78:    */   {
/*  79:192 */     this.templateLoaderPaths = new String[] { templateLoaderPath };
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void setTemplateLoaderPaths(String[] templateLoaderPaths)
/*  83:    */   {
/*  84:215 */     this.templateLoaderPaths = templateLoaderPaths;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void setResourceLoader(ResourceLoader resourceLoader)
/*  88:    */   {
/*  89:225 */     this.resourceLoader = resourceLoader;
/*  90:    */   }
/*  91:    */   
/*  92:    */   protected ResourceLoader getResourceLoader()
/*  93:    */   {
/*  94:232 */     return this.resourceLoader;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void setPreferFileSystemAccess(boolean preferFileSystemAccess)
/*  98:    */   {
/*  99:248 */     this.preferFileSystemAccess = preferFileSystemAccess;
/* 100:    */   }
/* 101:    */   
/* 102:    */   protected boolean isPreferFileSystemAccess()
/* 103:    */   {
/* 104:255 */     return this.preferFileSystemAccess;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public Configuration createConfiguration()
/* 108:    */     throws IOException, TemplateException
/* 109:    */   {
/* 110:266 */     Configuration config = newConfiguration();
/* 111:267 */     Properties props = new Properties();
/* 112:270 */     if (this.configLocation != null)
/* 113:    */     {
/* 114:271 */       if (this.logger.isInfoEnabled()) {
/* 115:272 */         this.logger.info("Loading FreeMarker configuration from " + this.configLocation);
/* 116:    */       }
/* 117:274 */       PropertiesLoaderUtils.fillProperties(props, this.configLocation);
/* 118:    */     }
/* 119:278 */     if (this.freemarkerSettings != null) {
/* 120:279 */       props.putAll(this.freemarkerSettings);
/* 121:    */     }
/* 122:284 */     if (!props.isEmpty()) {
/* 123:285 */       config.setSettings(props);
/* 124:    */     }
/* 125:288 */     if (!CollectionUtils.isEmpty(this.freemarkerVariables)) {
/* 126:289 */       config.setAllSharedVariables(new SimpleHash(this.freemarkerVariables, config.getObjectWrapper()));
/* 127:    */     }
/* 128:292 */     if (this.defaultEncoding != null) {
/* 129:293 */       config.setDefaultEncoding(this.defaultEncoding);
/* 130:    */     }
/* 131:297 */     if (this.preTemplateLoaders != null) {
/* 132:298 */       this.templateLoaders.addAll(this.preTemplateLoaders);
/* 133:    */     }
/* 134:302 */     if (this.templateLoaderPaths != null) {
/* 135:303 */       for (String path : this.templateLoaderPaths) {
/* 136:304 */         this.templateLoaders.add(getTemplateLoaderForPath(path));
/* 137:    */       }
/* 138:    */     }
/* 139:307 */     postProcessTemplateLoaders(this.templateLoaders);
/* 140:310 */     if (this.postTemplateLoaders != null) {
/* 141:311 */       this.templateLoaders.addAll(this.postTemplateLoaders);
/* 142:    */     }
/* 143:314 */     TemplateLoader loader = getAggregateTemplateLoader(this.templateLoaders);
/* 144:315 */     if (loader != null) {
/* 145:316 */       config.setTemplateLoader(loader);
/* 146:    */     }
/* 147:319 */     postProcessConfiguration(config);
/* 148:320 */     return config;
/* 149:    */   }
/* 150:    */   
/* 151:    */   protected Configuration newConfiguration()
/* 152:    */     throws IOException, TemplateException
/* 153:    */   {
/* 154:333 */     return new Configuration();
/* 155:    */   }
/* 156:    */   
/* 157:    */   protected TemplateLoader getTemplateLoaderForPath(String templateLoaderPath)
/* 158:    */   {
/* 159:346 */     if (isPreferFileSystemAccess()) {
/* 160:    */       try
/* 161:    */       {
/* 162:350 */         Resource path = getResourceLoader().getResource(templateLoaderPath);
/* 163:351 */         File file = path.getFile();
/* 164:352 */         if (this.logger.isDebugEnabled()) {
/* 165:353 */           this.logger.debug(
/* 166:354 */             "Template loader path [" + path + "] resolved to file path [" + file.getAbsolutePath() + "]");
/* 167:    */         }
/* 168:356 */         return new FileTemplateLoader(file);
/* 169:    */       }
/* 170:    */       catch (IOException ex)
/* 171:    */       {
/* 172:359 */         if (this.logger.isDebugEnabled()) {
/* 173:360 */           this.logger.debug("Cannot resolve template loader path [" + templateLoaderPath + 
/* 174:361 */             "] to [java.io.File]: using SpringTemplateLoader as fallback", ex);
/* 175:    */         }
/* 176:363 */         return new SpringTemplateLoader(getResourceLoader(), templateLoaderPath);
/* 177:    */       }
/* 178:    */     }
/* 179:368 */     this.logger.debug("File system access not preferred: using SpringTemplateLoader");
/* 180:369 */     return new SpringTemplateLoader(getResourceLoader(), templateLoaderPath);
/* 181:    */   }
/* 182:    */   
/* 183:    */   protected void postProcessTemplateLoaders(List<TemplateLoader> templateLoaders) {}
/* 184:    */   
/* 185:    */   protected TemplateLoader getAggregateTemplateLoader(List<TemplateLoader> templateLoaders)
/* 186:    */   {
/* 187:397 */     int loaderCount = templateLoaders.size();
/* 188:398 */     switch (loaderCount)
/* 189:    */     {
/* 190:    */     case 0: 
/* 191:400 */       this.logger.info("No FreeMarker TemplateLoaders specified");
/* 192:401 */       return null;
/* 193:    */     case 1: 
/* 194:403 */       return (TemplateLoader)templateLoaders.get(0);
/* 195:    */     }
/* 196:405 */     TemplateLoader[] loaders = (TemplateLoader[])templateLoaders.toArray(new TemplateLoader[loaderCount]);
/* 197:406 */     return new MultiTemplateLoader(loaders);
/* 198:    */   }
/* 199:    */   
/* 200:    */   protected void postProcessConfiguration(Configuration config)
/* 201:    */     throws IOException, TemplateException
/* 202:    */   {}
/* 203:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ui.freemarker.FreeMarkerConfigurationFactory
 * JD-Core Version:    0.7.0.1
 */