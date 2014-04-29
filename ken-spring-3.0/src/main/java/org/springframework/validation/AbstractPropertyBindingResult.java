/*   1:    */ package org.springframework.validation;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditor;
/*   4:    */ import org.springframework.beans.BeanUtils;
/*   5:    */ import org.springframework.beans.ConfigurablePropertyAccessor;
/*   6:    */ import org.springframework.beans.PropertyAccessorUtils;
/*   7:    */ import org.springframework.beans.PropertyEditorRegistry;
/*   8:    */ import org.springframework.core.convert.ConversionService;
/*   9:    */ import org.springframework.core.convert.TypeDescriptor;
/*  10:    */ import org.springframework.core.convert.support.ConvertingPropertyEditorAdapter;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ 
/*  13:    */ public abstract class AbstractPropertyBindingResult
/*  14:    */   extends AbstractBindingResult
/*  15:    */ {
/*  16:    */   private ConversionService conversionService;
/*  17:    */   
/*  18:    */   protected AbstractPropertyBindingResult(String objectName)
/*  19:    */   {
/*  20: 54 */     super(objectName);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public void initConversion(ConversionService conversionService)
/*  24:    */   {
/*  25: 59 */     Assert.notNull(conversionService, "ConversionService must not be null");
/*  26: 60 */     this.conversionService = conversionService;
/*  27: 61 */     if (getTarget() != null) {
/*  28: 62 */       getPropertyAccessor().setConversionService(conversionService);
/*  29:    */     }
/*  30:    */   }
/*  31:    */   
/*  32:    */   public PropertyEditorRegistry getPropertyEditorRegistry()
/*  33:    */   {
/*  34: 72 */     return getPropertyAccessor();
/*  35:    */   }
/*  36:    */   
/*  37:    */   protected String canonicalFieldName(String field)
/*  38:    */   {
/*  39: 81 */     return PropertyAccessorUtils.canonicalPropertyName(field);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public Class<?> getFieldType(String field)
/*  43:    */   {
/*  44: 90 */     return getPropertyAccessor().getPropertyType(fixedField(field));
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected Object getActualFieldValue(String field)
/*  48:    */   {
/*  49: 99 */     return getPropertyAccessor().getPropertyValue(field);
/*  50:    */   }
/*  51:    */   
/*  52:    */   protected Object formatFieldValue(String field, Object value)
/*  53:    */   {
/*  54:108 */     String fixedField = fixedField(field);
/*  55:    */     
/*  56:110 */     PropertyEditor customEditor = getCustomEditor(fixedField);
/*  57:111 */     if (customEditor != null)
/*  58:    */     {
/*  59:112 */       customEditor.setValue(value);
/*  60:113 */       String textValue = customEditor.getAsText();
/*  61:116 */       if (textValue != null) {
/*  62:117 */         return textValue;
/*  63:    */       }
/*  64:    */     }
/*  65:120 */     if (this.conversionService != null)
/*  66:    */     {
/*  67:122 */       TypeDescriptor fieldDesc = getPropertyAccessor().getPropertyTypeDescriptor(fixedField);
/*  68:123 */       TypeDescriptor strDesc = TypeDescriptor.valueOf(String.class);
/*  69:124 */       if ((fieldDesc != null) && (this.conversionService.canConvert(fieldDesc, strDesc))) {
/*  70:125 */         return this.conversionService.convert(value, fieldDesc, strDesc);
/*  71:    */       }
/*  72:    */     }
/*  73:128 */     return value;
/*  74:    */   }
/*  75:    */   
/*  76:    */   protected PropertyEditor getCustomEditor(String fixedField)
/*  77:    */   {
/*  78:137 */     Class<?> targetType = getPropertyAccessor().getPropertyType(fixedField);
/*  79:138 */     PropertyEditor editor = getPropertyAccessor().findCustomEditor(targetType, fixedField);
/*  80:139 */     if (editor == null) {
/*  81:140 */       editor = BeanUtils.findEditorByConvention(targetType);
/*  82:    */     }
/*  83:142 */     return editor;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public PropertyEditor findEditor(String field, Class<?> valueType)
/*  87:    */   {
/*  88:151 */     Class<?> valueTypeForLookup = valueType;
/*  89:152 */     if (valueTypeForLookup == null) {
/*  90:153 */       valueTypeForLookup = getFieldType(field);
/*  91:    */     }
/*  92:155 */     PropertyEditor editor = super.findEditor(field, valueTypeForLookup);
/*  93:156 */     if ((editor == null) && (this.conversionService != null))
/*  94:    */     {
/*  95:157 */       TypeDescriptor td = null;
/*  96:158 */       if (field != null)
/*  97:    */       {
/*  98:159 */         TypeDescriptor ptd = getPropertyAccessor().getPropertyTypeDescriptor(fixedField(field));
/*  99:160 */         if ((valueType == null) || (valueType.isAssignableFrom(ptd.getType()))) {
/* 100:161 */           td = ptd;
/* 101:    */         }
/* 102:    */       }
/* 103:164 */       if (td == null) {
/* 104:165 */         td = TypeDescriptor.valueOf(valueTypeForLookup);
/* 105:    */       }
/* 106:167 */       if (this.conversionService.canConvert(TypeDescriptor.valueOf(String.class), td)) {
/* 107:168 */         editor = new ConvertingPropertyEditorAdapter(this.conversionService, td);
/* 108:    */       }
/* 109:    */     }
/* 110:171 */     return editor;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public abstract ConfigurablePropertyAccessor getPropertyAccessor();
/* 114:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.AbstractPropertyBindingResult
 * JD-Core Version:    0.7.0.1
 */