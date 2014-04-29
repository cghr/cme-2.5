/*   1:    */ package org.springframework.core.env;
/*   2:    */ 
/*   3:    */ import java.util.LinkedHashSet;
/*   4:    */ import java.util.Set;
/*   5:    */ import org.apache.commons.logging.Log;
/*   6:    */ import org.apache.commons.logging.LogFactory;
/*   7:    */ import org.springframework.core.convert.support.ConfigurableConversionService;
/*   8:    */ import org.springframework.core.convert.support.DefaultConversionService;
/*   9:    */ import org.springframework.util.PropertyPlaceholderHelper;
/*  10:    */ import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;
/*  11:    */ 
/*  12:    */ public abstract class AbstractPropertyResolver
/*  13:    */   implements ConfigurablePropertyResolver
/*  14:    */ {
/*  15: 42 */   protected final Log logger = LogFactory.getLog(getClass());
/*  16: 44 */   protected ConfigurableConversionService conversionService = new DefaultConversionService();
/*  17:    */   private PropertyPlaceholderHelper nonStrictHelper;
/*  18:    */   private PropertyPlaceholderHelper strictHelper;
/*  19: 49 */   private String placeholderPrefix = "${";
/*  20: 50 */   private String placeholderSuffix = "}";
/*  21: 51 */   private String valueSeparator = ":";
/*  22: 53 */   private final Set<String> requiredProperties = new LinkedHashSet();
/*  23:    */   
/*  24:    */   public ConfigurableConversionService getConversionService()
/*  25:    */   {
/*  26: 56 */     return this.conversionService;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setConversionService(ConfigurableConversionService conversionService)
/*  30:    */   {
/*  31: 60 */     this.conversionService = conversionService;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public String getProperty(String key, String defaultValue)
/*  35:    */   {
/*  36: 64 */     String value = getProperty(key);
/*  37: 65 */     return value == null ? defaultValue : value;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public <T> T getProperty(String key, Class<T> targetType, T defaultValue)
/*  41:    */   {
/*  42: 69 */     T value = getProperty(key, targetType);
/*  43: 70 */     return value == null ? defaultValue : value;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setRequiredProperties(String... requiredProperties)
/*  47:    */   {
/*  48: 74 */     for (String key : requiredProperties) {
/*  49: 75 */       this.requiredProperties.add(key);
/*  50:    */     }
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void validateRequiredProperties()
/*  54:    */   {
/*  55: 80 */     MissingRequiredPropertiesException ex = new MissingRequiredPropertiesException();
/*  56: 81 */     for (String key : this.requiredProperties) {
/*  57: 82 */       if (getProperty(key) == null) {
/*  58: 83 */         ex.addMissingRequiredProperty(key);
/*  59:    */       }
/*  60:    */     }
/*  61: 86 */     if (!ex.getMissingRequiredProperties().isEmpty()) {
/*  62: 87 */       throw ex;
/*  63:    */     }
/*  64:    */   }
/*  65:    */   
/*  66:    */   public String getRequiredProperty(String key)
/*  67:    */     throws IllegalStateException
/*  68:    */   {
/*  69: 92 */     String value = getProperty(key);
/*  70: 93 */     if (value == null) {
/*  71: 94 */       throw new IllegalStateException(String.format("required key [%s] not found", new Object[] { key }));
/*  72:    */     }
/*  73: 96 */     return value;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public <T> T getRequiredProperty(String key, Class<T> valueType)
/*  77:    */     throws IllegalStateException
/*  78:    */   {
/*  79:100 */     T value = getProperty(key, valueType);
/*  80:101 */     if (value == null) {
/*  81:102 */       throw new IllegalStateException(String.format("required key [%s] not found", new Object[] { key }));
/*  82:    */     }
/*  83:104 */     return value;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setPlaceholderPrefix(String placeholderPrefix)
/*  87:    */   {
/*  88:112 */     this.placeholderPrefix = placeholderPrefix;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void setPlaceholderSuffix(String placeholderSuffix)
/*  92:    */   {
/*  93:120 */     this.placeholderSuffix = placeholderSuffix;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void setValueSeparator(String valueSeparator)
/*  97:    */   {
/*  98:128 */     this.valueSeparator = valueSeparator;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public String resolvePlaceholders(String text)
/* 102:    */   {
/* 103:132 */     if (this.nonStrictHelper == null) {
/* 104:133 */       this.nonStrictHelper = createPlaceholderHelper(true);
/* 105:    */     }
/* 106:135 */     return doResolvePlaceholders(text, this.nonStrictHelper);
/* 107:    */   }
/* 108:    */   
/* 109:    */   public String resolveRequiredPlaceholders(String text)
/* 110:    */     throws IllegalArgumentException
/* 111:    */   {
/* 112:139 */     if (this.strictHelper == null) {
/* 113:140 */       this.strictHelper = createPlaceholderHelper(false);
/* 114:    */     }
/* 115:142 */     return doResolvePlaceholders(text, this.strictHelper);
/* 116:    */   }
/* 117:    */   
/* 118:    */   private PropertyPlaceholderHelper createPlaceholderHelper(boolean ignoreUnresolvablePlaceholders)
/* 119:    */   {
/* 120:146 */     return new PropertyPlaceholderHelper(this.placeholderPrefix, this.placeholderSuffix, 
/* 121:147 */       this.valueSeparator, ignoreUnresolvablePlaceholders);
/* 122:    */   }
/* 123:    */   
/* 124:    */   private String doResolvePlaceholders(String text, PropertyPlaceholderHelper helper)
/* 125:    */   {
/* 126:151 */     helper.replacePlaceholders(text, new PropertyPlaceholderHelper.PlaceholderResolver()
/* 127:    */     {
/* 128:    */       public String resolvePlaceholder(String placeholderName)
/* 129:    */       {
/* 130:153 */         return AbstractPropertyResolver.this.getProperty(placeholderName);
/* 131:    */       }
/* 132:    */     });
/* 133:    */   }
/* 134:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.env.AbstractPropertyResolver
 * JD-Core Version:    0.7.0.1
 */