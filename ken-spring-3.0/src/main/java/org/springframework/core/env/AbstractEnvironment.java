/*   1:    */ package org.springframework.core.env;
/*   2:    */ 
/*   3:    */ import java.security.AccessControlException;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.LinkedHashSet;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.Set;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.apache.commons.logging.LogFactory;
/*  10:    */ import org.springframework.core.convert.support.ConfigurableConversionService;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ import org.springframework.util.StringUtils;
/*  13:    */ 
/*  14:    */ public abstract class AbstractEnvironment
/*  15:    */   implements ConfigurableEnvironment
/*  16:    */ {
/*  17:    */   public static final String ACTIVE_PROFILES_PROPERTY_NAME = "spring.profiles.active";
/*  18:    */   public static final String DEFAULT_PROFILES_PROPERTY_NAME = "spring.profiles.default";
/*  19:    */   protected static final String RESERVED_DEFAULT_PROFILE_NAME = "default";
/*  20: 79 */   protected final Log logger = LogFactory.getLog(getClass());
/*  21: 81 */   private Set<String> activeProfiles = new LinkedHashSet();
/*  22: 82 */   private Set<String> defaultProfiles = new LinkedHashSet(getReservedDefaultProfiles());
/*  23: 84 */   private final MutablePropertySources propertySources = new MutablePropertySources();
/*  24: 85 */   private final ConfigurablePropertyResolver propertyResolver = new PropertySourcesPropertyResolver(this.propertySources);
/*  25:    */   
/*  26:    */   public AbstractEnvironment()
/*  27:    */   {
/*  28: 89 */     customizePropertySources(this.propertySources);
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected void customizePropertySources(MutablePropertySources propertySources) {}
/*  32:    */   
/*  33:    */   protected Set<String> getReservedDefaultProfiles()
/*  34:    */   {
/*  35:168 */     return Collections.singleton("default");
/*  36:    */   }
/*  37:    */   
/*  38:    */   public String[] getActiveProfiles()
/*  39:    */   {
/*  40:177 */     return StringUtils.toStringArray(doGetActiveProfiles());
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected Set<String> doGetActiveProfiles()
/*  44:    */   {
/*  45:189 */     if (this.activeProfiles.isEmpty())
/*  46:    */     {
/*  47:190 */       String profiles = this.propertyResolver.getProperty("spring.profiles.active");
/*  48:191 */       if (StringUtils.hasText(profiles)) {
/*  49:192 */         setActiveProfiles(StringUtils.commaDelimitedListToStringArray(StringUtils.trimAllWhitespace(profiles)));
/*  50:    */       }
/*  51:    */     }
/*  52:195 */     return this.activeProfiles;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setActiveProfiles(String... profiles)
/*  56:    */   {
/*  57:199 */     Assert.notNull(profiles, "Profile array must not be null");
/*  58:200 */     this.activeProfiles.clear();
/*  59:201 */     for (String profile : profiles) {
/*  60:202 */       addActiveProfile(profile);
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void addActiveProfile(String profile)
/*  65:    */   {
/*  66:207 */     validateProfile(profile);
/*  67:208 */     this.activeProfiles.add(profile);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public String[] getDefaultProfiles()
/*  71:    */   {
/*  72:212 */     return StringUtils.toStringArray(doGetDefaultProfiles());
/*  73:    */   }
/*  74:    */   
/*  75:    */   protected Set<String> doGetDefaultProfiles()
/*  76:    */   {
/*  77:228 */     if (this.defaultProfiles.equals(getReservedDefaultProfiles()))
/*  78:    */     {
/*  79:229 */       String profiles = this.propertyResolver.getProperty("spring.profiles.default");
/*  80:230 */       if (StringUtils.hasText(profiles)) {
/*  81:231 */         setDefaultProfiles(StringUtils.commaDelimitedListToStringArray(StringUtils.trimAllWhitespace(profiles)));
/*  82:    */       }
/*  83:    */     }
/*  84:234 */     return this.defaultProfiles;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void setDefaultProfiles(String... profiles)
/*  88:    */   {
/*  89:245 */     Assert.notNull(profiles, "Profile array must not be null");
/*  90:246 */     this.defaultProfiles.clear();
/*  91:247 */     for (String profile : profiles)
/*  92:    */     {
/*  93:248 */       validateProfile(profile);
/*  94:249 */       this.defaultProfiles.add(profile);
/*  95:    */     }
/*  96:    */   }
/*  97:    */   
/*  98:    */   public boolean acceptsProfiles(String... profiles)
/*  99:    */   {
/* 100:254 */     Assert.notEmpty(profiles, "Must specify at least one profile");
/* 101:255 */     boolean activeProfileFound = false;
/* 102:256 */     Set<String> activeProfiles = doGetActiveProfiles();
/* 103:257 */     Set<String> defaultProfiles = doGetDefaultProfiles();
/* 104:258 */     for (String profile : profiles)
/* 105:    */     {
/* 106:259 */       validateProfile(profile);
/* 107:260 */       if ((activeProfiles.contains(profile)) || (
/* 108:261 */         (activeProfiles.isEmpty()) && (defaultProfiles.contains(profile))))
/* 109:    */       {
/* 110:262 */         activeProfileFound = true;
/* 111:263 */         break;
/* 112:    */       }
/* 113:    */     }
/* 114:266 */     return activeProfileFound;
/* 115:    */   }
/* 116:    */   
/* 117:    */   protected void validateProfile(String profile)
/* 118:    */   {
/* 119:279 */     Assert.hasText(profile, "Invalid profile [" + profile + "]: must contain text");
/* 120:    */   }
/* 121:    */   
/* 122:    */   public MutablePropertySources getPropertySources()
/* 123:    */   {
/* 124:283 */     return this.propertySources;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public Map<String, Object> getSystemEnvironment()
/* 128:    */   {
/* 129:    */     Map<String, ?> systemEnvironment;
/* 130:    */     try
/* 131:    */     {
/* 132:290 */       systemEnvironment = System.getenv();
/* 133:    */     }
/* 134:    */     catch (AccessControlException localAccessControlException)
/* 135:    */     {
/* 136:    */       Map<String, ?> systemEnvironment;
/* 137:293 */       systemEnvironment = new ReadOnlySystemAttributesMap()
/* 138:    */       {
/* 139:    */         protected String getSystemAttribute(String variableName)
/* 140:    */         {
/* 141:    */           try
/* 142:    */           {
/* 143:297 */             return System.getenv(variableName);
/* 144:    */           }
/* 145:    */           catch (AccessControlException ex)
/* 146:    */           {
/* 147:300 */             if (AbstractEnvironment.this.logger.isInfoEnabled()) {
/* 148:301 */               AbstractEnvironment.this.logger.info(
/* 149:302 */                 String.format("Caught AccessControlException when accessing system environment variable [%s]; its value will be returned [null]. Reason: %s", new Object[] {variableName, ex.getMessage() }));
/* 150:    */             }
/* 151:    */           }
/* 152:304 */           return null;
/* 153:    */         }
/* 154:    */       };
/* 155:    */     }
/* 156:309 */     return systemEnvironment;
/* 157:    */   }
/* 158:    */   
/* 159:    */   public Map<String, Object> getSystemProperties()
/* 160:    */   {
/* 161:    */     Map systemProperties;
/* 162:    */     try
/* 163:    */     {
/* 164:316 */       systemProperties = System.getProperties();
/* 165:    */     }
/* 166:    */     catch (AccessControlException localAccessControlException)
/* 167:    */     {
/* 168:    */       Map systemProperties;
/* 169:319 */       systemProperties = new ReadOnlySystemAttributesMap()
/* 170:    */       {
/* 171:    */         protected String getSystemAttribute(String propertyName)
/* 172:    */         {
/* 173:    */           try
/* 174:    */           {
/* 175:323 */             return System.getProperty(propertyName);
/* 176:    */           }
/* 177:    */           catch (AccessControlException ex)
/* 178:    */           {
/* 179:326 */             if (AbstractEnvironment.this.logger.isInfoEnabled()) {
/* 180:327 */               AbstractEnvironment.this.logger.info(
/* 181:328 */                 String.format("Caught AccessControlException when accessing system property [%s]; its value will be returned [null]. Reason: %s", new Object[] {propertyName, ex.getMessage() }));
/* 182:    */             }
/* 183:    */           }
/* 184:330 */           return null;
/* 185:    */         }
/* 186:    */       };
/* 187:    */     }
/* 188:335 */     return systemProperties;
/* 189:    */   }
/* 190:    */   
/* 191:    */   public boolean containsProperty(String key)
/* 192:    */   {
/* 193:344 */     return this.propertyResolver.containsProperty(key);
/* 194:    */   }
/* 195:    */   
/* 196:    */   public String getProperty(String key)
/* 197:    */   {
/* 198:348 */     return this.propertyResolver.getProperty(key);
/* 199:    */   }
/* 200:    */   
/* 201:    */   public String getProperty(String key, String defaultValue)
/* 202:    */   {
/* 203:352 */     return this.propertyResolver.getProperty(key, defaultValue);
/* 204:    */   }
/* 205:    */   
/* 206:    */   public <T> T getProperty(String key, Class<T> targetType)
/* 207:    */   {
/* 208:356 */     return this.propertyResolver.getProperty(key, targetType);
/* 209:    */   }
/* 210:    */   
/* 211:    */   public <T> T getProperty(String key, Class<T> targetType, T defaultValue)
/* 212:    */   {
/* 213:360 */     return this.propertyResolver.getProperty(key, targetType, defaultValue);
/* 214:    */   }
/* 215:    */   
/* 216:    */   public <T> Class<T> getPropertyAsClass(String key, Class<T> targetType)
/* 217:    */   {
/* 218:364 */     return this.propertyResolver.getPropertyAsClass(key, targetType);
/* 219:    */   }
/* 220:    */   
/* 221:    */   public String getRequiredProperty(String key)
/* 222:    */     throws IllegalStateException
/* 223:    */   {
/* 224:368 */     return this.propertyResolver.getRequiredProperty(key);
/* 225:    */   }
/* 226:    */   
/* 227:    */   public <T> T getRequiredProperty(String key, Class<T> targetType)
/* 228:    */     throws IllegalStateException
/* 229:    */   {
/* 230:372 */     return this.propertyResolver.getRequiredProperty(key, targetType);
/* 231:    */   }
/* 232:    */   
/* 233:    */   public void setRequiredProperties(String... requiredProperties)
/* 234:    */   {
/* 235:376 */     this.propertyResolver.setRequiredProperties(requiredProperties);
/* 236:    */   }
/* 237:    */   
/* 238:    */   public void validateRequiredProperties()
/* 239:    */     throws MissingRequiredPropertiesException
/* 240:    */   {
/* 241:380 */     this.propertyResolver.validateRequiredProperties();
/* 242:    */   }
/* 243:    */   
/* 244:    */   public String resolvePlaceholders(String text)
/* 245:    */   {
/* 246:384 */     return this.propertyResolver.resolvePlaceholders(text);
/* 247:    */   }
/* 248:    */   
/* 249:    */   public String resolveRequiredPlaceholders(String text)
/* 250:    */     throws IllegalArgumentException
/* 251:    */   {
/* 252:388 */     return this.propertyResolver.resolveRequiredPlaceholders(text);
/* 253:    */   }
/* 254:    */   
/* 255:    */   public void setConversionService(ConfigurableConversionService conversionService)
/* 256:    */   {
/* 257:392 */     this.propertyResolver.setConversionService(conversionService);
/* 258:    */   }
/* 259:    */   
/* 260:    */   public ConfigurableConversionService getConversionService()
/* 261:    */   {
/* 262:396 */     return this.propertyResolver.getConversionService();
/* 263:    */   }
/* 264:    */   
/* 265:    */   public void setPlaceholderPrefix(String placeholderPrefix)
/* 266:    */   {
/* 267:400 */     this.propertyResolver.setPlaceholderPrefix(placeholderPrefix);
/* 268:    */   }
/* 269:    */   
/* 270:    */   public void setPlaceholderSuffix(String placeholderSuffix)
/* 271:    */   {
/* 272:405 */     this.propertyResolver.setPlaceholderSuffix(placeholderSuffix);
/* 273:    */   }
/* 274:    */   
/* 275:    */   public void setValueSeparator(String valueSeparator)
/* 276:    */   {
/* 277:410 */     this.propertyResolver.setValueSeparator(valueSeparator);
/* 278:    */   }
/* 279:    */   
/* 280:    */   public String toString()
/* 281:    */   {
/* 282:416 */     return String.format("%s [activeProfiles=%s, defaultProfiles=%s, propertySources=%s]", new Object[] {
/* 283:417 */       getClass().getSimpleName(), this.activeProfiles, this.defaultProfiles, this.propertySources });
/* 284:    */   }
/* 285:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.env.AbstractEnvironment
 * JD-Core Version:    0.7.0.1
 */