/*   1:    */ package org.springframework.context.support;
/*   2:    */ 
/*   3:    */ import java.text.MessageFormat;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Locale;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.MissingResourceException;
/*   8:    */ import java.util.ResourceBundle;
/*   9:    */ import org.apache.commons.logging.Log;
/*  10:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ import org.springframework.util.ClassUtils;
/*  13:    */ import org.springframework.util.StringUtils;
/*  14:    */ 
/*  15:    */ public class ResourceBundleMessageSource
/*  16:    */   extends AbstractMessageSource
/*  17:    */   implements BeanClassLoaderAware
/*  18:    */ {
/*  19: 59 */   private String[] basenames = new String[0];
/*  20:    */   private ClassLoader bundleClassLoader;
/*  21: 63 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*  22: 73 */   private final Map<String, Map<Locale, ResourceBundle>> cachedResourceBundles = new HashMap();
/*  23: 84 */   private final Map<ResourceBundle, Map<String, Map<Locale, MessageFormat>>> cachedBundleMessageFormats = new HashMap();
/*  24:    */   
/*  25:    */   public void setBasename(String basename)
/*  26:    */   {
/*  27:103 */     setBasenames(new String[] { basename });
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setBasenames(String[] basenames)
/*  31:    */   {
/*  32:123 */     if (basenames != null)
/*  33:    */     {
/*  34:124 */       this.basenames = new String[basenames.length];
/*  35:125 */       for (int i = 0; i < basenames.length; i++)
/*  36:    */       {
/*  37:126 */         String basename = basenames[i];
/*  38:127 */         Assert.hasText(basename, "Basename must not be empty");
/*  39:128 */         this.basenames[i] = basename.trim();
/*  40:    */       }
/*  41:    */     }
/*  42:    */     else
/*  43:    */     {
/*  44:132 */       this.basenames = new String[0];
/*  45:    */     }
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setBundleClassLoader(ClassLoader classLoader)
/*  49:    */   {
/*  50:145 */     this.bundleClassLoader = classLoader;
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected ClassLoader getBundleClassLoader()
/*  54:    */   {
/*  55:154 */     return this.bundleClassLoader != null ? this.bundleClassLoader : this.beanClassLoader;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setBeanClassLoader(ClassLoader classLoader)
/*  59:    */   {
/*  60:158 */     this.beanClassLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
/*  61:    */   }
/*  62:    */   
/*  63:    */   protected String resolveCodeWithoutArguments(String code, Locale locale)
/*  64:    */   {
/*  65:168 */     String result = null;
/*  66:169 */     for (int i = 0; (result == null) && (i < this.basenames.length); i++)
/*  67:    */     {
/*  68:170 */       ResourceBundle bundle = getResourceBundle(this.basenames[i], locale);
/*  69:171 */       if (bundle != null) {
/*  70:172 */         result = getStringOrNull(bundle, code);
/*  71:    */       }
/*  72:    */     }
/*  73:175 */     return result;
/*  74:    */   }
/*  75:    */   
/*  76:    */   protected MessageFormat resolveCode(String code, Locale locale)
/*  77:    */   {
/*  78:184 */     MessageFormat messageFormat = null;
/*  79:185 */     for (int i = 0; (messageFormat == null) && (i < this.basenames.length); i++)
/*  80:    */     {
/*  81:186 */       ResourceBundle bundle = getResourceBundle(this.basenames[i], locale);
/*  82:187 */       if (bundle != null) {
/*  83:188 */         messageFormat = getMessageFormat(bundle, code, locale);
/*  84:    */       }
/*  85:    */     }
/*  86:191 */     return messageFormat;
/*  87:    */   }
/*  88:    */   
/*  89:    */   protected ResourceBundle getResourceBundle(String basename, Locale locale)
/*  90:    */   {
/*  91:204 */     synchronized (this.cachedResourceBundles)
/*  92:    */     {
/*  93:205 */       Map<Locale, ResourceBundle> localeMap = (Map)this.cachedResourceBundles.get(basename);
/*  94:206 */       if (localeMap != null)
/*  95:    */       {
/*  96:207 */         ResourceBundle bundle = (ResourceBundle)localeMap.get(locale);
/*  97:208 */         if (bundle != null) {
/*  98:209 */           return bundle;
/*  99:    */         }
/* 100:    */       }
/* 101:    */       try
/* 102:    */       {
/* 103:213 */         ResourceBundle bundle = doGetBundle(basename, locale);
/* 104:214 */         if (localeMap == null)
/* 105:    */         {
/* 106:215 */           localeMap = new HashMap();
/* 107:216 */           this.cachedResourceBundles.put(basename, localeMap);
/* 108:    */         }
/* 109:218 */         localeMap.put(locale, bundle);
/* 110:219 */         return bundle;
/* 111:    */       }
/* 112:    */       catch (MissingResourceException ex)
/* 113:    */       {
/* 114:222 */         if (this.logger.isWarnEnabled()) {
/* 115:223 */           this.logger.warn("ResourceBundle [" + basename + "] not found for MessageSource: " + ex.getMessage());
/* 116:    */         }
/* 117:227 */         return null;
/* 118:    */       }
/* 119:    */     }
/* 120:    */   }
/* 121:    */   
/* 122:    */   protected ResourceBundle doGetBundle(String basename, Locale locale)
/* 123:    */     throws MissingResourceException
/* 124:    */   {
/* 125:242 */     return ResourceBundle.getBundle(basename, locale, getBundleClassLoader());
/* 126:    */   }
/* 127:    */   
/* 128:    */   protected MessageFormat getMessageFormat(ResourceBundle bundle, String code, Locale locale)
/* 129:    */     throws MissingResourceException
/* 130:    */   {
/* 131:258 */     synchronized (this.cachedBundleMessageFormats)
/* 132:    */     {
/* 133:259 */       Map<String, Map<Locale, MessageFormat>> codeMap = (Map)this.cachedBundleMessageFormats.get(bundle);
/* 134:260 */       Map<Locale, MessageFormat> localeMap = null;
/* 135:261 */       if (codeMap != null)
/* 136:    */       {
/* 137:262 */         localeMap = (Map)codeMap.get(code);
/* 138:263 */         if (localeMap != null)
/* 139:    */         {
/* 140:264 */           MessageFormat result = (MessageFormat)localeMap.get(locale);
/* 141:265 */           if (result != null) {
/* 142:266 */             return result;
/* 143:    */           }
/* 144:    */         }
/* 145:    */       }
/* 146:271 */       String msg = getStringOrNull(bundle, code);
/* 147:272 */       if (msg != null)
/* 148:    */       {
/* 149:273 */         if (codeMap == null)
/* 150:    */         {
/* 151:274 */           codeMap = new HashMap();
/* 152:275 */           this.cachedBundleMessageFormats.put(bundle, codeMap);
/* 153:    */         }
/* 154:277 */         if (localeMap == null)
/* 155:    */         {
/* 156:278 */           localeMap = new HashMap();
/* 157:279 */           codeMap.put(code, localeMap);
/* 158:    */         }
/* 159:281 */         MessageFormat result = createMessageFormat(msg, locale);
/* 160:282 */         localeMap.put(locale, result);
/* 161:283 */         return result;
/* 162:    */       }
/* 163:286 */       return null;
/* 164:    */     }
/* 165:    */   }
/* 166:    */   
/* 167:    */   private String getStringOrNull(ResourceBundle bundle, String key)
/* 168:    */   {
/* 169:    */     try
/* 170:    */     {
/* 171:292 */       return bundle.getString(key);
/* 172:    */     }
/* 173:    */     catch (MissingResourceException localMissingResourceException) {}
/* 174:297 */     return null;
/* 175:    */   }
/* 176:    */   
/* 177:    */   public String toString()
/* 178:    */   {
/* 179:307 */     return 
/* 180:308 */       getClass().getName() + ": basenames=[" + StringUtils.arrayToCommaDelimitedString(this.basenames) + "]";
/* 181:    */   }
/* 182:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.ResourceBundleMessageSource
 * JD-Core Version:    0.7.0.1
 */