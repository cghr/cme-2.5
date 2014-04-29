/*   1:    */ package org.springframework.util;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.HashSet;
/*   5:    */ import java.util.Map;
/*   6:    */ import java.util.Properties;
/*   7:    */ import java.util.Set;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.apache.commons.logging.LogFactory;
/*  10:    */ 
/*  11:    */ public class PropertyPlaceholderHelper
/*  12:    */ {
/*  13: 40 */   private static final Log logger = LogFactory.getLog(PropertyPlaceholderHelper.class);
/*  14: 42 */   private static final Map<String, String> wellKnownSimplePrefixes = new HashMap(4);
/*  15:    */   private final String placeholderPrefix;
/*  16:    */   private final String placeholderSuffix;
/*  17:    */   private final String simplePrefix;
/*  18:    */   private final String valueSeparator;
/*  19:    */   private final boolean ignoreUnresolvablePlaceholders;
/*  20:    */   
/*  21:    */   static
/*  22:    */   {
/*  23: 45 */     wellKnownSimplePrefixes.put("}", "{");
/*  24: 46 */     wellKnownSimplePrefixes.put("]", "[");
/*  25: 47 */     wellKnownSimplePrefixes.put(")", "(");
/*  26:    */   }
/*  27:    */   
/*  28:    */   public PropertyPlaceholderHelper(String placeholderPrefix, String placeholderSuffix)
/*  29:    */   {
/*  30: 69 */     this(placeholderPrefix, placeholderSuffix, null, true);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public PropertyPlaceholderHelper(String placeholderPrefix, String placeholderSuffix, String valueSeparator, boolean ignoreUnresolvablePlaceholders)
/*  34:    */   {
/*  35: 84 */     Assert.notNull(placeholderPrefix, "placeholderPrefix must not be null");
/*  36: 85 */     Assert.notNull(placeholderSuffix, "placeholderSuffix must not be null");
/*  37: 86 */     this.placeholderPrefix = placeholderPrefix;
/*  38: 87 */     this.placeholderSuffix = placeholderSuffix;
/*  39: 88 */     String simplePrefixForSuffix = (String)wellKnownSimplePrefixes.get(this.placeholderSuffix);
/*  40: 89 */     if ((simplePrefixForSuffix != null) && (this.placeholderPrefix.endsWith(simplePrefixForSuffix))) {
/*  41: 90 */       this.simplePrefix = simplePrefixForSuffix;
/*  42:    */     } else {
/*  43: 93 */       this.simplePrefix = this.placeholderPrefix;
/*  44:    */     }
/*  45: 95 */     this.valueSeparator = valueSeparator;
/*  46: 96 */     this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public String replacePlaceholders(String value, final Properties properties)
/*  50:    */   {
/*  51:108 */     Assert.notNull(properties, "Argument 'properties' must not be null.");
/*  52:109 */     replacePlaceholders(value, new PlaceholderResolver()
/*  53:    */     {
/*  54:    */       public String resolvePlaceholder(String placeholderName)
/*  55:    */       {
/*  56:111 */         return properties.getProperty(placeholderName);
/*  57:    */       }
/*  58:    */     });
/*  59:    */   }
/*  60:    */   
/*  61:    */   public String replacePlaceholders(String value, PlaceholderResolver placeholderResolver)
/*  62:    */   {
/*  63:124 */     Assert.notNull(value, "Argument 'value' must not be null.");
/*  64:125 */     return parseStringValue(value, placeholderResolver, new HashSet());
/*  65:    */   }
/*  66:    */   
/*  67:    */   protected String parseStringValue(String strVal, PlaceholderResolver placeholderResolver, Set<String> visitedPlaceholders)
/*  68:    */   {
/*  69:131 */     StringBuilder buf = new StringBuilder(strVal);
/*  70:    */     
/*  71:133 */     int startIndex = strVal.indexOf(this.placeholderPrefix);
/*  72:134 */     while (startIndex != -1)
/*  73:    */     {
/*  74:135 */       int endIndex = findPlaceholderEndIndex(buf, startIndex);
/*  75:136 */       if (endIndex != -1)
/*  76:    */       {
/*  77:137 */         String placeholder = buf.substring(startIndex + this.placeholderPrefix.length(), endIndex);
/*  78:138 */         if (!visitedPlaceholders.add(placeholder)) {
/*  79:139 */           throw new IllegalArgumentException(
/*  80:140 */             "Circular placeholder reference '" + placeholder + "' in property definitions");
/*  81:    */         }
/*  82:143 */         placeholder = parseStringValue(placeholder, placeholderResolver, visitedPlaceholders);
/*  83:    */         
/*  84:    */ 
/*  85:146 */         String propVal = placeholderResolver.resolvePlaceholder(placeholder);
/*  86:147 */         if ((propVal == null) && (this.valueSeparator != null))
/*  87:    */         {
/*  88:148 */           int separatorIndex = placeholder.indexOf(this.valueSeparator);
/*  89:149 */           if (separatorIndex != -1)
/*  90:    */           {
/*  91:150 */             String actualPlaceholder = placeholder.substring(0, separatorIndex);
/*  92:151 */             String defaultValue = placeholder.substring(separatorIndex + this.valueSeparator.length());
/*  93:152 */             propVal = placeholderResolver.resolvePlaceholder(actualPlaceholder);
/*  94:153 */             if (propVal == null) {
/*  95:154 */               propVal = defaultValue;
/*  96:    */             }
/*  97:    */           }
/*  98:    */         }
/*  99:158 */         if (propVal != null)
/* 100:    */         {
/* 101:161 */           propVal = parseStringValue(propVal, placeholderResolver, visitedPlaceholders);
/* 102:162 */           buf.replace(startIndex, endIndex + this.placeholderSuffix.length(), propVal);
/* 103:163 */           if (logger.isTraceEnabled()) {
/* 104:164 */             logger.trace("Resolved placeholder '" + placeholder + "'");
/* 105:    */           }
/* 106:166 */           startIndex = buf.indexOf(this.placeholderPrefix, startIndex + propVal.length());
/* 107:    */         }
/* 108:168 */         else if (this.ignoreUnresolvablePlaceholders)
/* 109:    */         {
/* 110:170 */           startIndex = buf.indexOf(this.placeholderPrefix, endIndex + this.placeholderSuffix.length());
/* 111:    */         }
/* 112:    */         else
/* 113:    */         {
/* 114:173 */           throw new IllegalArgumentException("Could not resolve placeholder '" + placeholder + "'");
/* 115:    */         }
/* 116:176 */         visitedPlaceholders.remove(placeholder);
/* 117:    */       }
/* 118:    */       else
/* 119:    */       {
/* 120:179 */         startIndex = -1;
/* 121:    */       }
/* 122:    */     }
/* 123:183 */     return buf.toString();
/* 124:    */   }
/* 125:    */   
/* 126:    */   private int findPlaceholderEndIndex(CharSequence buf, int startIndex)
/* 127:    */   {
/* 128:187 */     int index = startIndex + this.placeholderPrefix.length();
/* 129:188 */     int withinNestedPlaceholder = 0;
/* 130:189 */     while (index < buf.length()) {
/* 131:190 */       if (StringUtils.substringMatch(buf, index, this.placeholderSuffix))
/* 132:    */       {
/* 133:191 */         if (withinNestedPlaceholder > 0)
/* 134:    */         {
/* 135:192 */           withinNestedPlaceholder--;
/* 136:193 */           index += this.placeholderSuffix.length();
/* 137:    */         }
/* 138:    */         else
/* 139:    */         {
/* 140:196 */           return index;
/* 141:    */         }
/* 142:    */       }
/* 143:199 */       else if (StringUtils.substringMatch(buf, index, this.simplePrefix))
/* 144:    */       {
/* 145:200 */         withinNestedPlaceholder++;
/* 146:201 */         index += this.simplePrefix.length();
/* 147:    */       }
/* 148:    */       else
/* 149:    */       {
/* 150:204 */         index++;
/* 151:    */       }
/* 152:    */     }
/* 153:207 */     return -1;
/* 154:    */   }
/* 155:    */   
/* 156:    */   public static abstract interface PlaceholderResolver
/* 157:    */   {
/* 158:    */     public abstract String resolvePlaceholder(String paramString);
/* 159:    */   }
/* 160:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.PropertyPlaceholderHelper
 * JD-Core Version:    0.7.0.1
 */