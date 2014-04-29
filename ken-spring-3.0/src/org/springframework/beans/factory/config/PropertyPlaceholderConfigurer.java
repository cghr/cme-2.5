/*   1:    */ package org.springframework.beans.factory.config;
/*   2:    */ 
/*   3:    */ import java.util.Properties;
/*   4:    */ import java.util.Set;
/*   5:    */ import org.apache.commons.logging.Log;
/*   6:    */ import org.springframework.beans.BeansException;
/*   7:    */ import org.springframework.core.Constants;
/*   8:    */ import org.springframework.util.PropertyPlaceholderHelper;
/*   9:    */ import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;
/*  10:    */ import org.springframework.util.StringValueResolver;
/*  11:    */ 
/*  12:    */ public class PropertyPlaceholderConfigurer
/*  13:    */   extends PlaceholderConfigurerSupport
/*  14:    */ {
/*  15:    */   public static final int SYSTEM_PROPERTIES_MODE_NEVER = 0;
/*  16:    */   public static final int SYSTEM_PROPERTIES_MODE_FALLBACK = 1;
/*  17:    */   public static final int SYSTEM_PROPERTIES_MODE_OVERRIDE = 2;
/*  18: 81 */   private static final Constants constants = new Constants(PropertyPlaceholderConfigurer.class);
/*  19: 83 */   private int systemPropertiesMode = 1;
/*  20: 85 */   private boolean searchSystemEnvironment = true;
/*  21:    */   
/*  22:    */   public void setSystemPropertiesModeName(String constantName)
/*  23:    */     throws IllegalArgumentException
/*  24:    */   {
/*  25: 96 */     this.systemPropertiesMode = constants.asNumber(constantName).intValue();
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setSystemPropertiesMode(int systemPropertiesMode)
/*  29:    */   {
/*  30:112 */     this.systemPropertiesMode = systemPropertiesMode;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setSearchSystemEnvironment(boolean searchSystemEnvironment)
/*  34:    */   {
/*  35:134 */     this.searchSystemEnvironment = searchSystemEnvironment;
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected String resolvePlaceholder(String placeholder, Properties props, int systemPropertiesMode)
/*  39:    */   {
/*  40:154 */     String propVal = null;
/*  41:155 */     if (systemPropertiesMode == 2) {
/*  42:156 */       propVal = resolveSystemProperty(placeholder);
/*  43:    */     }
/*  44:158 */     if (propVal == null) {
/*  45:159 */       propVal = resolvePlaceholder(placeholder, props);
/*  46:    */     }
/*  47:161 */     if ((propVal == null) && (systemPropertiesMode == 1)) {
/*  48:162 */       propVal = resolveSystemProperty(placeholder);
/*  49:    */     }
/*  50:164 */     return propVal;
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected String resolvePlaceholder(String placeholder, Properties props)
/*  54:    */   {
/*  55:181 */     return props.getProperty(placeholder);
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected String resolveSystemProperty(String key)
/*  59:    */   {
/*  60:    */     try
/*  61:    */     {
/*  62:195 */       String value = System.getProperty(key);
/*  63:196 */       if ((value == null) && (this.searchSystemEnvironment)) {}
/*  64:197 */       return System.getenv(key);
/*  65:    */     }
/*  66:    */     catch (Throwable ex)
/*  67:    */     {
/*  68:202 */       if (this.logger.isDebugEnabled()) {
/*  69:203 */         this.logger.debug("Could not access system property '" + key + "': " + ex);
/*  70:    */       }
/*  71:    */     }
/*  72:205 */     return null;
/*  73:    */   }
/*  74:    */   
/*  75:    */   protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
/*  76:    */     throws BeansException
/*  77:    */   {
/*  78:218 */     StringValueResolver valueResolver = new PlaceholderResolvingStringValueResolver(props);
/*  79:    */     
/*  80:220 */     doProcessProperties(beanFactoryToProcess, valueResolver);
/*  81:    */   }
/*  82:    */   
/*  83:    */   @Deprecated
/*  84:    */   protected String parseStringValue(String strVal, Properties props, Set<?> visitedPlaceholders)
/*  85:    */   {
/*  86:235 */     PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper(
/*  87:236 */       this.placeholderPrefix, this.placeholderSuffix, this.valueSeparator, this.ignoreUnresolvablePlaceholders);
/*  88:237 */     PropertyPlaceholderHelper.PlaceholderResolver resolver = new PropertyPlaceholderConfigurerResolver(props, null);
/*  89:238 */     return helper.replacePlaceholders(strVal, resolver);
/*  90:    */   }
/*  91:    */   
/*  92:    */   private class PlaceholderResolvingStringValueResolver
/*  93:    */     implements StringValueResolver
/*  94:    */   {
/*  95:    */     private final PropertyPlaceholderHelper helper;
/*  96:    */     private final PropertyPlaceholderHelper.PlaceholderResolver resolver;
/*  97:    */     
/*  98:    */     public PlaceholderResolvingStringValueResolver(Properties props)
/*  99:    */     {
/* 100:249 */       this.helper = new PropertyPlaceholderHelper(
/* 101:250 */         PropertyPlaceholderConfigurer.this.placeholderPrefix, PropertyPlaceholderConfigurer.this.placeholderSuffix, PropertyPlaceholderConfigurer.this.valueSeparator, PropertyPlaceholderConfigurer.this.ignoreUnresolvablePlaceholders);
/* 102:251 */       this.resolver = new PropertyPlaceholderConfigurer.PropertyPlaceholderConfigurerResolver(PropertyPlaceholderConfigurer.this, props, null);
/* 103:    */     }
/* 104:    */     
/* 105:    */     public String resolveStringValue(String strVal)
/* 106:    */       throws BeansException
/* 107:    */     {
/* 108:255 */       String value = this.helper.replacePlaceholders(strVal, this.resolver);
/* 109:256 */       return value.equals(PropertyPlaceholderConfigurer.this.nullValue) ? null : value;
/* 110:    */     }
/* 111:    */   }
/* 112:    */   
/* 113:    */   private class PropertyPlaceholderConfigurerResolver
/* 114:    */     implements PropertyPlaceholderHelper.PlaceholderResolver
/* 115:    */   {
/* 116:    */     private final Properties props;
/* 117:    */     
/* 118:    */     private PropertyPlaceholderConfigurerResolver(Properties props)
/* 119:    */     {
/* 120:266 */       this.props = props;
/* 121:    */     }
/* 122:    */     
/* 123:    */     public String resolvePlaceholder(String placeholderName)
/* 124:    */     {
/* 125:270 */       return PropertyPlaceholderConfigurer.this.resolvePlaceholder(placeholderName, this.props, PropertyPlaceholderConfigurer.this.systemPropertiesMode);
/* 126:    */     }
/* 127:    */   }
/* 128:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
 * JD-Core Version:    0.7.0.1
 */