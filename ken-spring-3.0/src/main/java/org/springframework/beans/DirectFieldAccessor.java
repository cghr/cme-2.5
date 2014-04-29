/*   1:    */ package org.springframework.beans;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyChangeEvent;
/*   4:    */ import java.lang.reflect.Field;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Map;
/*   7:    */ import org.springframework.core.MethodParameter;
/*   8:    */ import org.springframework.core.convert.ConversionException;
/*   9:    */ import org.springframework.core.convert.ConverterNotFoundException;
/*  10:    */ import org.springframework.core.convert.TypeDescriptor;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ import org.springframework.util.ReflectionUtils;
/*  13:    */ import org.springframework.util.ReflectionUtils.FieldCallback;
/*  14:    */ 
/*  15:    */ public class DirectFieldAccessor
/*  16:    */   extends AbstractPropertyAccessor
/*  17:    */ {
/*  18:    */   private final Object target;
/*  19: 52 */   private final Map<String, Field> fieldMap = new HashMap();
/*  20:    */   private final TypeConverterDelegate typeConverterDelegate;
/*  21:    */   
/*  22:    */   public DirectFieldAccessor(Object target)
/*  23:    */   {
/*  24: 62 */     Assert.notNull(target, "Target object must not be null");
/*  25: 63 */     this.target = target;
/*  26: 64 */     ReflectionUtils.doWithFields(this.target.getClass(), new ReflectionUtils.FieldCallback()
/*  27:    */     {
/*  28:    */       public void doWith(Field field)
/*  29:    */       {
/*  30: 66 */         if (!DirectFieldAccessor.this.fieldMap.containsKey(field.getName())) {
/*  31: 69 */           DirectFieldAccessor.this.fieldMap.put(field.getName(), field);
/*  32:    */         }
/*  33:    */       }
/*  34: 72 */     });
/*  35: 73 */     this.typeConverterDelegate = new TypeConverterDelegate(this, target);
/*  36: 74 */     registerDefaultEditors();
/*  37: 75 */     setExtractOldValueForEditor(true);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public boolean isReadableProperty(String propertyName)
/*  41:    */     throws BeansException
/*  42:    */   {
/*  43: 80 */     return this.fieldMap.containsKey(propertyName);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public boolean isWritableProperty(String propertyName)
/*  47:    */     throws BeansException
/*  48:    */   {
/*  49: 84 */     return this.fieldMap.containsKey(propertyName);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public Class<?> getPropertyType(String propertyName)
/*  53:    */     throws BeansException
/*  54:    */   {
/*  55: 89 */     Field field = (Field)this.fieldMap.get(propertyName);
/*  56: 90 */     if (field != null) {
/*  57: 91 */       return field.getType();
/*  58:    */     }
/*  59: 93 */     return null;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public TypeDescriptor getPropertyTypeDescriptor(String propertyName)
/*  63:    */     throws BeansException
/*  64:    */   {
/*  65: 97 */     Field field = (Field)this.fieldMap.get(propertyName);
/*  66: 98 */     if (field != null) {
/*  67: 99 */       return new TypeDescriptor(field);
/*  68:    */     }
/*  69:101 */     return null;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public Object getPropertyValue(String propertyName)
/*  73:    */     throws BeansException
/*  74:    */   {
/*  75:106 */     Field field = (Field)this.fieldMap.get(propertyName);
/*  76:107 */     if (field == null) {
/*  77:108 */       throw new NotReadablePropertyException(
/*  78:109 */         this.target.getClass(), propertyName, "Field '" + propertyName + "' does not exist");
/*  79:    */     }
/*  80:    */     try
/*  81:    */     {
/*  82:112 */       ReflectionUtils.makeAccessible(field);
/*  83:113 */       return field.get(this.target);
/*  84:    */     }
/*  85:    */     catch (IllegalAccessException ex)
/*  86:    */     {
/*  87:116 */       throw new InvalidPropertyException(this.target.getClass(), propertyName, "Field is not accessible", ex);
/*  88:    */     }
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void setPropertyValue(String propertyName, Object newValue)
/*  92:    */     throws BeansException
/*  93:    */   {
/*  94:122 */     Field field = (Field)this.fieldMap.get(propertyName);
/*  95:123 */     if (field == null) {
/*  96:124 */       throw new NotWritablePropertyException(
/*  97:125 */         this.target.getClass(), propertyName, "Field '" + propertyName + "' does not exist");
/*  98:    */     }
/*  99:127 */     Object oldValue = null;
/* 100:    */     try
/* 101:    */     {
/* 102:129 */       ReflectionUtils.makeAccessible(field);
/* 103:130 */       oldValue = field.get(this.target);
/* 104:131 */       Object convertedValue = this.typeConverterDelegate.convertIfNecessary(
/* 105:132 */         field.getName(), oldValue, newValue, field.getType(), new TypeDescriptor(field));
/* 106:133 */       field.set(this.target, convertedValue);
/* 107:    */     }
/* 108:    */     catch (ConverterNotFoundException ex)
/* 109:    */     {
/* 110:136 */       PropertyChangeEvent pce = new PropertyChangeEvent(this.target, propertyName, oldValue, newValue);
/* 111:137 */       throw new ConversionNotSupportedException(pce, field.getType(), ex);
/* 112:    */     }
/* 113:    */     catch (ConversionException ex)
/* 114:    */     {
/* 115:140 */       PropertyChangeEvent pce = new PropertyChangeEvent(this.target, propertyName, oldValue, newValue);
/* 116:141 */       throw new TypeMismatchException(pce, field.getType(), ex);
/* 117:    */     }
/* 118:    */     catch (IllegalStateException ex)
/* 119:    */     {
/* 120:144 */       PropertyChangeEvent pce = new PropertyChangeEvent(this.target, propertyName, oldValue, newValue);
/* 121:145 */       throw new ConversionNotSupportedException(pce, field.getType(), ex);
/* 122:    */     }
/* 123:    */     catch (IllegalArgumentException ex)
/* 124:    */     {
/* 125:148 */       PropertyChangeEvent pce = new PropertyChangeEvent(this.target, propertyName, oldValue, newValue);
/* 126:149 */       throw new TypeMismatchException(pce, field.getType(), ex);
/* 127:    */     }
/* 128:    */     catch (IllegalAccessException ex)
/* 129:    */     {
/* 130:152 */       throw new InvalidPropertyException(this.target.getClass(), propertyName, "Field is not accessible", ex);
/* 131:    */     }
/* 132:    */   }
/* 133:    */   
/* 134:    */   public <T> T convertIfNecessary(Object value, Class<T> requiredType, MethodParameter methodParam)
/* 135:    */     throws TypeMismatchException
/* 136:    */   {
/* 137:    */     try
/* 138:    */     {
/* 139:159 */       return this.typeConverterDelegate.convertIfNecessary(value, requiredType, methodParam);
/* 140:    */     }
/* 141:    */     catch (IllegalArgumentException ex)
/* 142:    */     {
/* 143:162 */       throw new TypeMismatchException(value, requiredType, ex);
/* 144:    */     }
/* 145:    */     catch (IllegalStateException ex)
/* 146:    */     {
/* 147:165 */       throw new ConversionNotSupportedException(value, requiredType, ex);
/* 148:    */     }
/* 149:    */   }
/* 150:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.DirectFieldAccessor
 * JD-Core Version:    0.7.0.1
 */