/*   1:    */ package org.springframework.web.servlet.tags.form;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditor;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.Map;
/*   6:    */ import java.util.Map.Entry;
/*   7:    */ import javax.servlet.jsp.JspException;
/*   8:    */ import org.springframework.beans.BeanWrapper;
/*   9:    */ import org.springframework.beans.PropertyAccessorFactory;
/*  10:    */ import org.springframework.util.Assert;
/*  11:    */ import org.springframework.util.CollectionUtils;
/*  12:    */ import org.springframework.web.servlet.support.BindStatus;
/*  13:    */ 
/*  14:    */ class OptionWriter
/*  15:    */ {
/*  16:    */   private final Object optionSource;
/*  17:    */   private final BindStatus bindStatus;
/*  18:    */   private final String valueProperty;
/*  19:    */   private final String labelProperty;
/*  20:    */   private final boolean htmlEscape;
/*  21:    */   
/*  22:    */   public OptionWriter(Object optionSource, BindStatus bindStatus, String valueProperty, String labelProperty, boolean htmlEscape)
/*  23:    */   {
/*  24:120 */     Assert.notNull(optionSource, "'optionSource' must not be null");
/*  25:121 */     Assert.notNull(bindStatus, "'bindStatus' must not be null");
/*  26:122 */     this.optionSource = optionSource;
/*  27:123 */     this.bindStatus = bindStatus;
/*  28:124 */     this.valueProperty = valueProperty;
/*  29:125 */     this.labelProperty = labelProperty;
/*  30:126 */     this.htmlEscape = htmlEscape;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void writeOptions(TagWriter tagWriter)
/*  34:    */     throws JspException
/*  35:    */   {
/*  36:135 */     if (this.optionSource.getClass().isArray()) {
/*  37:136 */       renderFromArray(tagWriter);
/*  38:138 */     } else if ((this.optionSource instanceof Collection)) {
/*  39:139 */       renderFromCollection(tagWriter);
/*  40:141 */     } else if ((this.optionSource instanceof Map)) {
/*  41:142 */       renderFromMap(tagWriter);
/*  42:144 */     } else if (((this.optionSource instanceof Class)) && (((Class)this.optionSource).isEnum())) {
/*  43:145 */       renderFromEnum(tagWriter);
/*  44:    */     } else {
/*  45:148 */       throw new JspException(
/*  46:149 */         "Type [" + this.optionSource.getClass().getName() + "] is not valid for option items");
/*  47:    */     }
/*  48:    */   }
/*  49:    */   
/*  50:    */   private void renderFromArray(TagWriter tagWriter)
/*  51:    */     throws JspException
/*  52:    */   {
/*  53:158 */     doRenderFromCollection(CollectionUtils.arrayToList(this.optionSource), tagWriter);
/*  54:    */   }
/*  55:    */   
/*  56:    */   private void renderFromMap(TagWriter tagWriter)
/*  57:    */     throws JspException
/*  58:    */   {
/*  59:167 */     Map<?, ?> optionMap = (Map)this.optionSource;
/*  60:168 */     for (Map.Entry entry : optionMap.entrySet())
/*  61:    */     {
/*  62:169 */       Object mapKey = entry.getKey();
/*  63:170 */       Object mapValue = entry.getValue();
/*  64:171 */       Object renderValue = this.valueProperty != null ? 
/*  65:172 */         PropertyAccessorFactory.forBeanPropertyAccess(mapKey).getPropertyValue(this.valueProperty) : 
/*  66:173 */         mapKey;
/*  67:174 */       Object renderLabel = this.labelProperty != null ? 
/*  68:175 */         PropertyAccessorFactory.forBeanPropertyAccess(mapValue).getPropertyValue(this.labelProperty) : 
/*  69:176 */         mapValue;
/*  70:177 */       renderOption(tagWriter, mapKey, renderValue, renderLabel);
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   private void renderFromCollection(TagWriter tagWriter)
/*  75:    */     throws JspException
/*  76:    */   {
/*  77:186 */     doRenderFromCollection((Collection)this.optionSource, tagWriter);
/*  78:    */   }
/*  79:    */   
/*  80:    */   private void renderFromEnum(TagWriter tagWriter)
/*  81:    */     throws JspException
/*  82:    */   {
/*  83:194 */     doRenderFromCollection(CollectionUtils.arrayToList(((Class)this.optionSource).getEnumConstants()), tagWriter);
/*  84:    */   }
/*  85:    */   
/*  86:    */   private void doRenderFromCollection(Collection optionCollection, TagWriter tagWriter)
/*  87:    */     throws JspException
/*  88:    */   {
/*  89:204 */     for (Object item : optionCollection)
/*  90:    */     {
/*  91:205 */       BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(item);
/*  92:    */       Object value;
/*  93:    */       Object value;
/*  94:207 */       if (this.valueProperty != null)
/*  95:    */       {
/*  96:208 */         value = wrapper.getPropertyValue(this.valueProperty);
/*  97:    */       }
/*  98:    */       else
/*  99:    */       {
/* 100:    */         Object value;
/* 101:210 */         if ((item instanceof Enum)) {
/* 102:211 */           value = ((Enum)item).name();
/* 103:    */         } else {
/* 104:214 */           value = item;
/* 105:    */         }
/* 106:    */       }
/* 107:216 */       Object label = this.labelProperty != null ? wrapper.getPropertyValue(this.labelProperty) : item;
/* 108:217 */       renderOption(tagWriter, item, value, label);
/* 109:    */     }
/* 110:    */   }
/* 111:    */   
/* 112:    */   private void renderOption(TagWriter tagWriter, Object item, Object value, Object label)
/* 113:    */     throws JspException
/* 114:    */   {
/* 115:226 */     tagWriter.startTag("option");
/* 116:227 */     writeCommonAttributes(tagWriter);
/* 117:    */     
/* 118:229 */     String valueDisplayString = getDisplayString(value);
/* 119:230 */     String labelDisplayString = getDisplayString(label);
/* 120:    */     
/* 121:232 */     valueDisplayString = processOptionValue(valueDisplayString);
/* 122:    */     
/* 123:    */ 
/* 124:235 */     tagWriter.writeAttribute("value", valueDisplayString);
/* 125:237 */     if ((isOptionSelected(value)) || ((value != item) && (isOptionSelected(item)))) {
/* 126:238 */       tagWriter.writeAttribute("selected", "selected");
/* 127:    */     }
/* 128:240 */     if (isOptionDisabled()) {
/* 129:241 */       tagWriter.writeAttribute("disabled", "disabled");
/* 130:    */     }
/* 131:243 */     tagWriter.appendValue(labelDisplayString);
/* 132:244 */     tagWriter.endTag();
/* 133:    */   }
/* 134:    */   
/* 135:    */   private String getDisplayString(Object value)
/* 136:    */   {
/* 137:252 */     PropertyEditor editor = value != null ? this.bindStatus.findEditor(value.getClass()) : null;
/* 138:253 */     return ValueFormatter.getDisplayString(value, editor, this.htmlEscape);
/* 139:    */   }
/* 140:    */   
/* 141:    */   protected String processOptionValue(String resolvedValue)
/* 142:    */   {
/* 143:261 */     return resolvedValue;
/* 144:    */   }
/* 145:    */   
/* 146:    */   private boolean isOptionSelected(Object resolvedValue)
/* 147:    */   {
/* 148:269 */     return SelectedValueComparator.isSelected(this.bindStatus, resolvedValue);
/* 149:    */   }
/* 150:    */   
/* 151:    */   protected boolean isOptionDisabled()
/* 152:    */     throws JspException
/* 153:    */   {
/* 154:276 */     return false;
/* 155:    */   }
/* 156:    */   
/* 157:    */   protected void writeCommonAttributes(TagWriter tagWriter)
/* 158:    */     throws JspException
/* 159:    */   {}
/* 160:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.OptionWriter
 * JD-Core Version:    0.7.0.1
 */