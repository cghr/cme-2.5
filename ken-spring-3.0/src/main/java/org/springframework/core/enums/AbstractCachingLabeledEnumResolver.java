/*   1:    */ package org.springframework.core.enums;
/*   2:    */ 
/*   3:    */ import java.util.Collections;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Map;
/*   6:    */ import java.util.Set;
/*   7:    */ import java.util.TreeSet;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.apache.commons.logging.LogFactory;
/*  10:    */ import org.springframework.util.Assert;
/*  11:    */ import org.springframework.util.CachingMapDecorator;
/*  12:    */ import org.springframework.util.ClassUtils;
/*  13:    */ 
/*  14:    */ @Deprecated
/*  15:    */ public abstract class AbstractCachingLabeledEnumResolver
/*  16:    */   implements LabeledEnumResolver
/*  17:    */ {
/*  18: 48 */   protected final transient Log logger = LogFactory.getLog(getClass());
/*  19: 50 */   private final LabeledEnumCache labeledEnumCache = new LabeledEnumCache();
/*  20:    */   
/*  21:    */   public Set<LabeledEnum> getLabeledEnumSet(Class type)
/*  22:    */     throws IllegalArgumentException
/*  23:    */   {
/*  24: 54 */     return new TreeSet(getLabeledEnumMap(type).values());
/*  25:    */   }
/*  26:    */   
/*  27:    */   public Map<Comparable, LabeledEnum> getLabeledEnumMap(Class type)
/*  28:    */     throws IllegalArgumentException
/*  29:    */   {
/*  30: 58 */     Assert.notNull(type, "No type specified");
/*  31: 59 */     return (Map)this.labeledEnumCache.get(type);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public LabeledEnum getLabeledEnumByCode(Class type, Comparable code)
/*  35:    */     throws IllegalArgumentException
/*  36:    */   {
/*  37: 63 */     Assert.notNull(code, "No enum code specified");
/*  38: 64 */     Map<Comparable, LabeledEnum> typeEnums = getLabeledEnumMap(type);
/*  39: 65 */     LabeledEnum codedEnum = (LabeledEnum)typeEnums.get(code);
/*  40: 66 */     if (codedEnum == null) {
/*  41: 67 */       throw new IllegalArgumentException(
/*  42: 68 */         "No enumeration with code '" + code + "'" + " of type [" + type.getName() + 
/*  43: 69 */         "] exists: this is likely a configuration error. " + 
/*  44: 70 */         "Make sure the code value matches a valid instance's code property!");
/*  45:    */     }
/*  46: 72 */     return codedEnum;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public LabeledEnum getLabeledEnumByLabel(Class type, String label)
/*  50:    */     throws IllegalArgumentException
/*  51:    */   {
/*  52: 76 */     Map<Comparable, LabeledEnum> typeEnums = getLabeledEnumMap(type);
/*  53: 77 */     for (LabeledEnum value : typeEnums.values()) {
/*  54: 78 */       if (value.getLabel().equalsIgnoreCase(label)) {
/*  55: 79 */         return value;
/*  56:    */       }
/*  57:    */     }
/*  58: 82 */     throw new IllegalArgumentException(
/*  59: 83 */       "No enumeration with label '" + label + "' of type [" + type + 
/*  60: 84 */       "] exists: this is likely a configuration error. " + 
/*  61: 85 */       "Make sure the label string matches a valid instance's label property!");
/*  62:    */   }
/*  63:    */   
/*  64:    */   protected abstract Set<LabeledEnum> findLabeledEnums(Class paramClass);
/*  65:    */   
/*  66:    */   private class LabeledEnumCache
/*  67:    */     extends CachingMapDecorator<Class, Map<Comparable, LabeledEnum>>
/*  68:    */   {
/*  69:    */     public LabeledEnumCache()
/*  70:    */     {
/*  71:105 */       super();
/*  72:    */     }
/*  73:    */     
/*  74:    */     protected Map<Comparable, LabeledEnum> create(Class key)
/*  75:    */     {
/*  76:110 */       Set<LabeledEnum> typeEnums = AbstractCachingLabeledEnumResolver.this.findLabeledEnums(key);
/*  77:111 */       if ((typeEnums == null) || (typeEnums.isEmpty())) {
/*  78:112 */         throw new IllegalArgumentException(
/*  79:113 */           "Unsupported labeled enumeration type '" + key + "': " + 
/*  80:114 */           "make sure you've properly defined this enumeration! " + 
/*  81:115 */           "If it is static, are the class and its fields public/static/final?");
/*  82:    */       }
/*  83:117 */       Map<Comparable, LabeledEnum> typeEnumMap = new HashMap(typeEnums.size());
/*  84:118 */       for (LabeledEnum labeledEnum : typeEnums) {
/*  85:119 */         typeEnumMap.put(labeledEnum.getCode(), labeledEnum);
/*  86:    */       }
/*  87:121 */       return Collections.unmodifiableMap(typeEnumMap);
/*  88:    */     }
/*  89:    */     
/*  90:    */     protected boolean useWeakValue(Class key, Map<Comparable, LabeledEnum> value)
/*  91:    */     {
/*  92:126 */       if (!ClassUtils.isCacheSafe(key, AbstractCachingLabeledEnumResolver.this.getClass().getClassLoader()))
/*  93:    */       {
/*  94:127 */         if ((AbstractCachingLabeledEnumResolver.this.logger != null) && (AbstractCachingLabeledEnumResolver.this.logger.isDebugEnabled())) {
/*  95:128 */           AbstractCachingLabeledEnumResolver.this.logger.debug("Not strongly caching class [" + key.getName() + "] because it is not cache-safe");
/*  96:    */         }
/*  97:130 */         return true;
/*  98:    */       }
/*  99:133 */       return false;
/* 100:    */     }
/* 101:    */   }
/* 102:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.enums.AbstractCachingLabeledEnumResolver
 * JD-Core Version:    0.7.0.1
 */