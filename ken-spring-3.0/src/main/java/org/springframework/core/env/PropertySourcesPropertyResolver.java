/*   1:    */ package org.springframework.core.env;
/*   2:    */ 
/*   3:    */ import org.apache.commons.logging.Log;
/*   4:    */ import org.springframework.core.convert.ConversionException;
/*   5:    */ import org.springframework.core.convert.support.ConfigurableConversionService;
/*   6:    */ import org.springframework.util.ClassUtils;
/*   7:    */ 
/*   8:    */ public class PropertySourcesPropertyResolver
/*   9:    */   extends AbstractPropertyResolver
/*  10:    */ {
/*  11:    */   private final PropertySources propertySources;
/*  12:    */   
/*  13:    */   public PropertySourcesPropertyResolver(PropertySources propertySources)
/*  14:    */   {
/*  15: 43 */     this.propertySources = propertySources;
/*  16:    */   }
/*  17:    */   
/*  18:    */   public boolean containsProperty(String key)
/*  19:    */   {
/*  20: 47 */     for (PropertySource<?> propertySource : this.propertySources) {
/*  21: 48 */       if (propertySource.getProperty(key) != null) {
/*  22: 49 */         return true;
/*  23:    */       }
/*  24:    */     }
/*  25: 52 */     return false;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public String getProperty(String key)
/*  29:    */   {
/*  30: 56 */     if (this.logger.isTraceEnabled()) {
/*  31: 57 */       this.logger.trace(String.format("getProperty(\"%s\") (implicit targetType [String])", new Object[] { key }));
/*  32:    */     }
/*  33: 59 */     return (String)getProperty(key, String.class);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public <T> T getProperty(String key, Class<T> targetValueType)
/*  37:    */   {
/*  38: 63 */     boolean debugEnabled = this.logger.isDebugEnabled();
/*  39: 64 */     if (this.logger.isTraceEnabled()) {
/*  40: 65 */       this.logger.trace(String.format("getProperty(\"%s\", %s)", new Object[] { key, targetValueType.getSimpleName() }));
/*  41:    */     }
/*  42: 68 */     for (PropertySource<?> propertySource : this.propertySources)
/*  43:    */     {
/*  44: 69 */       if (debugEnabled) {
/*  45: 70 */         this.logger.debug(String.format("Searching for key '%s' in [%s]", new Object[] { key, propertySource.getName() }));
/*  46:    */       }
/*  47:    */       Object value;
/*  48: 73 */       if ((value = propertySource.getProperty(key)) != null)
/*  49:    */       {
/*  50: 74 */         Class<?> valueType = value.getClass();
/*  51: 75 */         if (debugEnabled) {
/*  52: 76 */           this.logger.debug(
/*  53: 77 */             String.format("Found key '%s' in [%s] with type [%s] and value '%s'", new Object[] {
/*  54: 78 */             key, propertySource.getName(), valueType.getSimpleName(), value }));
/*  55:    */         }
/*  56: 80 */         if (!this.conversionService.canConvert(valueType, targetValueType)) {
/*  57: 81 */           throw new IllegalArgumentException(
/*  58: 82 */             String.format("Cannot convert value [%s] from source type [%s] to target type [%s]", new Object[] {
/*  59: 83 */             value, valueType.getSimpleName(), targetValueType.getSimpleName() }));
/*  60:    */         }
/*  61: 85 */         return this.conversionService.convert(value, targetValueType);
/*  62:    */       }
/*  63:    */     }
/*  64: 89 */     if (debugEnabled) {
/*  65: 90 */       this.logger.debug(String.format("Could not find key '%s' in any property source. Returning [null]", new Object[] { key }));
/*  66:    */     }
/*  67: 92 */     return null;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public <T> Class<T> getPropertyAsClass(String key, Class<T> targetValueType)
/*  71:    */   {
/*  72: 96 */     boolean debugEnabled = this.logger.isDebugEnabled();
/*  73: 97 */     if (this.logger.isTraceEnabled()) {
/*  74: 98 */       this.logger.trace(String.format("getPropertyAsClass(\"%s\", %s)", new Object[] { key, targetValueType.getSimpleName() }));
/*  75:    */     }
/*  76:101 */     for (PropertySource<?> propertySource : this.propertySources)
/*  77:    */     {
/*  78:102 */       if (debugEnabled) {
/*  79:103 */         this.logger.debug(String.format("Searching for key '%s' in [%s]", new Object[] { key, propertySource.getName() }));
/*  80:    */       }
/*  81:    */       Object value;
/*  82:106 */       if ((value = propertySource.getProperty(key)) != null)
/*  83:    */       {
/*  84:107 */         if (debugEnabled) {
/*  85:108 */           this.logger.debug(
/*  86:109 */             String.format("Found key '%s' in [%s] with value '%s'", new Object[] { key, propertySource.getName(), value }));
/*  87:    */         }
/*  88:    */         Class<?> clazz;
/*  89:113 */         if ((value instanceof String))
/*  90:    */         {
/*  91:    */           try
/*  92:    */           {
/*  93:115 */             clazz = ClassUtils.forName((String)value, null);
/*  94:    */           }
/*  95:    */           catch (Exception ex)
/*  96:    */           {
/*  97:    */             Class<?> clazz;
/*  98:117 */             throw new ClassConversionException((String)value, targetValueType, ex);
/*  99:    */           }
/* 100:    */         }
/* 101:    */         else
/* 102:    */         {
/* 103:    */           Class<?> clazz;
/* 104:120 */           if ((value instanceof Class)) {
/* 105:121 */             clazz = (Class)value;
/* 106:    */           } else {
/* 107:123 */             clazz = value.getClass();
/* 108:    */           }
/* 109:    */         }
/* 110:126 */         if (!targetValueType.isAssignableFrom(clazz)) {
/* 111:127 */           throw new ClassConversionException(clazz, targetValueType);
/* 112:    */         }
/* 113:130 */         Class<T> targetClass = clazz;
/* 114:131 */         return targetClass;
/* 115:    */       }
/* 116:    */     }
/* 117:135 */     if (debugEnabled) {
/* 118:136 */       this.logger.debug(String.format("Could not find key '%s' in any property source. Returning [null]", new Object[] { key }));
/* 119:    */     }
/* 120:138 */     return null;
/* 121:    */   }
/* 122:    */   
/* 123:    */   static class ClassConversionException
/* 124:    */     extends ConversionException
/* 125:    */   {
/* 126:    */     public ClassConversionException(Class<?> actual, Class<?> expected)
/* 127:    */     {
/* 128:144 */       super();
/* 129:    */     }
/* 130:    */     
/* 131:    */     public ClassConversionException(String actual, Class<?> expected, Exception ex)
/* 132:    */     {
/* 133:148 */       super(ex);
/* 134:    */     }
/* 135:    */   }
/* 136:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.env.PropertySourcesPropertyResolver
 * JD-Core Version:    0.7.0.1
 */