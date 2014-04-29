/*  1:   */ package org.springframework.core.convert.support;
/*  2:   */ 
/*  3:   */ import java.util.Collections;
/*  4:   */ import java.util.Map;
/*  5:   */ import java.util.Map.Entry;
/*  6:   */ import java.util.Set;
/*  7:   */ import org.springframework.core.CollectionFactory;
/*  8:   */ import org.springframework.core.convert.ConversionService;
/*  9:   */ import org.springframework.core.convert.TypeDescriptor;
/* 10:   */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/* 11:   */ import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
/* 12:   */ 
/* 13:   */ final class MapToMapConverter
/* 14:   */   implements ConditionalGenericConverter
/* 15:   */ {
/* 16:   */   private final ConversionService conversionService;
/* 17:   */   
/* 18:   */   public MapToMapConverter(ConversionService conversionService)
/* 19:   */   {
/* 20:44 */     this.conversionService = conversionService;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/* 24:   */   {
/* 25:48 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Map.class, Map.class));
/* 26:   */   }
/* 27:   */   
/* 28:   */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 29:   */   {
/* 30:52 */     return (canConvertKey(sourceType, targetType)) && (canConvertValue(sourceType, targetType));
/* 31:   */   }
/* 32:   */   
/* 33:   */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 34:   */   {
/* 35:57 */     if (source == null) {
/* 36:58 */       return null;
/* 37:   */     }
/* 38:60 */     boolean copyRequired = !targetType.getType().isInstance(source);
/* 39:61 */     Map<Object, Object> sourceMap = (Map)source;
/* 40:62 */     Map<Object, Object> targetMap = CollectionFactory.createMap(targetType.getType(), sourceMap.size());
/* 41:63 */     for (Map.Entry<Object, Object> entry : sourceMap.entrySet())
/* 42:   */     {
/* 43:64 */       Object sourceKey = entry.getKey();
/* 44:65 */       Object sourceValue = entry.getValue();
/* 45:66 */       Object targetKey = convertKey(sourceKey, sourceType, targetType.getMapKeyTypeDescriptor());
/* 46:67 */       Object targetValue = convertValue(sourceValue, sourceType, targetType.getMapValueTypeDescriptor());
/* 47:68 */       targetMap.put(targetKey, targetValue);
/* 48:69 */       if ((sourceKey != targetKey) || (sourceValue != targetValue)) {
/* 49:70 */         copyRequired = true;
/* 50:   */       }
/* 51:   */     }
/* 52:73 */     return copyRequired ? targetMap : sourceMap;
/* 53:   */   }
/* 54:   */   
/* 55:   */   private boolean canConvertKey(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 56:   */   {
/* 57:79 */     return ConversionUtils.canConvertElements(sourceType.getMapKeyTypeDescriptor(), targetType.getMapKeyTypeDescriptor(), this.conversionService);
/* 58:   */   }
/* 59:   */   
/* 60:   */   private boolean canConvertValue(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 61:   */   {
/* 62:83 */     return ConversionUtils.canConvertElements(sourceType.getMapValueTypeDescriptor(), targetType.getMapValueTypeDescriptor(), this.conversionService);
/* 63:   */   }
/* 64:   */   
/* 65:   */   private Object convertKey(Object sourceKey, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 66:   */   {
/* 67:87 */     if (targetType == null) {
/* 68:88 */       return sourceKey;
/* 69:   */     }
/* 70:90 */     return this.conversionService.convert(sourceKey, sourceType.getMapKeyTypeDescriptor(sourceKey), targetType);
/* 71:   */   }
/* 72:   */   
/* 73:   */   private Object convertValue(Object sourceValue, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 74:   */   {
/* 75:94 */     if (targetType == null) {
/* 76:95 */       return sourceValue;
/* 77:   */     }
/* 78:97 */     return this.conversionService.convert(sourceValue, sourceType.getMapValueTypeDescriptor(sourceValue), targetType);
/* 79:   */   }
/* 80:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.MapToMapConverter
 * JD-Core Version:    0.7.0.1
 */