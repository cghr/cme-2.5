/*   1:    */ package org.springframework.beans.factory.config;
/*   2:    */ 
/*   3:    */ import java.util.LinkedHashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import java.util.Map.Entry;
/*   6:    */ import org.springframework.beans.BeanUtils;
/*   7:    */ import org.springframework.beans.TypeConverter;
/*   8:    */ import org.springframework.core.GenericCollectionTypeResolver;
/*   9:    */ 
/*  10:    */ public class MapFactoryBean
/*  11:    */   extends AbstractFactoryBean<Map>
/*  12:    */ {
/*  13:    */   private Map<?, ?> sourceMap;
/*  14:    */   private Class targetMapClass;
/*  15:    */   
/*  16:    */   public void setSourceMap(Map sourceMap)
/*  17:    */   {
/*  18: 46 */     this.sourceMap = sourceMap;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public void setTargetMapClass(Class targetMapClass)
/*  22:    */   {
/*  23: 56 */     if (targetMapClass == null) {
/*  24: 57 */       throw new IllegalArgumentException("'targetMapClass' must not be null");
/*  25:    */     }
/*  26: 59 */     if (!Map.class.isAssignableFrom(targetMapClass)) {
/*  27: 60 */       throw new IllegalArgumentException("'targetMapClass' must implement [java.util.Map]");
/*  28:    */     }
/*  29: 62 */     this.targetMapClass = targetMapClass;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public Class<Map> getObjectType()
/*  33:    */   {
/*  34: 68 */     return Map.class;
/*  35:    */   }
/*  36:    */   
/*  37:    */   protected Map createInstance()
/*  38:    */   {
/*  39: 74 */     if (this.sourceMap == null) {
/*  40: 75 */       throw new IllegalArgumentException("'sourceMap' is required");
/*  41:    */     }
/*  42: 77 */     Map result = null;
/*  43: 78 */     if (this.targetMapClass != null) {
/*  44: 79 */       result = (Map)BeanUtils.instantiateClass(this.targetMapClass);
/*  45:    */     } else {
/*  46: 82 */       result = new LinkedHashMap(this.sourceMap.size());
/*  47:    */     }
/*  48: 84 */     Class keyType = null;
/*  49: 85 */     Class valueType = null;
/*  50: 86 */     if (this.targetMapClass != null)
/*  51:    */     {
/*  52: 87 */       keyType = GenericCollectionTypeResolver.getMapKeyType(this.targetMapClass);
/*  53: 88 */       valueType = GenericCollectionTypeResolver.getMapValueType(this.targetMapClass);
/*  54:    */     }
/*  55: 90 */     if ((keyType != null) || (valueType != null))
/*  56:    */     {
/*  57: 91 */       TypeConverter converter = getBeanTypeConverter();
/*  58: 92 */       for (Map.Entry entry : this.sourceMap.entrySet())
/*  59:    */       {
/*  60: 93 */         Object convertedKey = converter.convertIfNecessary(entry.getKey(), keyType);
/*  61: 94 */         Object convertedValue = converter.convertIfNecessary(entry.getValue(), valueType);
/*  62: 95 */         result.put(convertedKey, convertedValue);
/*  63:    */       }
/*  64:    */     }
/*  65:    */     else
/*  66:    */     {
/*  67: 99 */       result.putAll(this.sourceMap);
/*  68:    */     }
/*  69:101 */     return result;
/*  70:    */   }
/*  71:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.MapFactoryBean
 * JD-Core Version:    0.7.0.1
 */