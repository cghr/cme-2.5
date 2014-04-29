/*   1:    */ package org.springframework.beans;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyDescriptor;
/*   4:    */ import java.io.Serializable;
/*   5:    */ import org.springframework.util.Assert;
/*   6:    */ import org.springframework.util.ObjectUtils;
/*   7:    */ 
/*   8:    */ public class PropertyValue
/*   9:    */   extends BeanMetadataAttributeAccessor
/*  10:    */   implements Serializable
/*  11:    */ {
/*  12:    */   private final String name;
/*  13:    */   private final Object value;
/*  14:    */   private Object source;
/*  15: 50 */   private boolean optional = false;
/*  16: 52 */   private boolean converted = false;
/*  17:    */   private Object convertedValue;
/*  18:    */   volatile Boolean conversionNecessary;
/*  19:    */   volatile Object resolvedTokens;
/*  20:    */   volatile PropertyDescriptor resolvedDescriptor;
/*  21:    */   
/*  22:    */   public PropertyValue(String name, Object value)
/*  23:    */   {
/*  24: 72 */     this.name = name;
/*  25: 73 */     this.value = value;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public PropertyValue(PropertyValue original)
/*  29:    */   {
/*  30: 81 */     Assert.notNull(original, "Original must not be null");
/*  31: 82 */     this.name = original.getName();
/*  32: 83 */     this.value = original.getValue();
/*  33: 84 */     this.source = original.getSource();
/*  34: 85 */     this.optional = original.isOptional();
/*  35: 86 */     this.converted = original.converted;
/*  36: 87 */     this.convertedValue = original.convertedValue;
/*  37: 88 */     this.conversionNecessary = original.conversionNecessary;
/*  38: 89 */     this.resolvedTokens = original.resolvedTokens;
/*  39: 90 */     this.resolvedDescriptor = original.resolvedDescriptor;
/*  40: 91 */     copyAttributesFrom(original);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public PropertyValue(PropertyValue original, Object newValue)
/*  44:    */   {
/*  45:101 */     Assert.notNull(original, "Original must not be null");
/*  46:102 */     this.name = original.getName();
/*  47:103 */     this.value = newValue;
/*  48:104 */     this.source = original;
/*  49:105 */     this.optional = original.isOptional();
/*  50:106 */     this.conversionNecessary = original.conversionNecessary;
/*  51:107 */     this.resolvedTokens = original.resolvedTokens;
/*  52:108 */     this.resolvedDescriptor = original.resolvedDescriptor;
/*  53:109 */     copyAttributesFrom(original);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public String getName()
/*  57:    */   {
/*  58:117 */     return this.name;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public Object getValue()
/*  62:    */   {
/*  63:127 */     return this.value;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public PropertyValue getOriginalPropertyValue()
/*  67:    */   {
/*  68:136 */     PropertyValue original = this;
/*  69:137 */     while (((original.source instanceof PropertyValue)) && (original.source != original)) {
/*  70:138 */       original = (PropertyValue)original.source;
/*  71:    */     }
/*  72:140 */     return original;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void setOptional(boolean optional)
/*  76:    */   {
/*  77:144 */     this.optional = optional;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public boolean isOptional()
/*  81:    */   {
/*  82:148 */     return this.optional;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public synchronized boolean isConverted()
/*  86:    */   {
/*  87:156 */     return this.converted;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public synchronized void setConvertedValue(Object value)
/*  91:    */   {
/*  92:164 */     this.converted = true;
/*  93:165 */     this.convertedValue = value;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public synchronized Object getConvertedValue()
/*  97:    */   {
/*  98:173 */     return this.convertedValue;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public boolean equals(Object other)
/* 102:    */   {
/* 103:179 */     if (this == other) {
/* 104:180 */       return true;
/* 105:    */     }
/* 106:182 */     if (!(other instanceof PropertyValue)) {
/* 107:183 */       return false;
/* 108:    */     }
/* 109:185 */     PropertyValue otherPv = (PropertyValue)other;
/* 110:    */     
/* 111:    */ 
/* 112:188 */     return (this.name.equals(otherPv.name)) && (ObjectUtils.nullSafeEquals(this.value, otherPv.value)) && (ObjectUtils.nullSafeEquals(this.source, otherPv.source));
/* 113:    */   }
/* 114:    */   
/* 115:    */   public int hashCode()
/* 116:    */   {
/* 117:193 */     return this.name.hashCode() * 29 + ObjectUtils.nullSafeHashCode(this.value);
/* 118:    */   }
/* 119:    */   
/* 120:    */   public String toString()
/* 121:    */   {
/* 122:198 */     return "bean property '" + this.name + "'";
/* 123:    */   }
/* 124:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.PropertyValue
 * JD-Core Version:    0.7.0.1
 */