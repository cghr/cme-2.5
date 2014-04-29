/*   1:    */ package org.springframework.web.servlet.tags.form;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import java.util.Map;
/*   6:    */ import java.util.Map.Entry;
/*   7:    */ import java.util.Set;
/*   8:    */ import javax.servlet.jsp.JspException;
/*   9:    */ import org.springframework.beans.BeanWrapper;
/*  10:    */ import org.springframework.beans.PropertyAccessorFactory;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ import org.springframework.util.ObjectUtils;
/*  13:    */ import org.springframework.util.StringUtils;
/*  14:    */ import org.springframework.web.servlet.support.BindStatus;
/*  15:    */ 
/*  16:    */ public abstract class AbstractMultiCheckedElementTag
/*  17:    */   extends AbstractCheckedElementTag
/*  18:    */ {
/*  19:    */   private static final String SPAN_TAG = "span";
/*  20:    */   private Object items;
/*  21:    */   private String itemValue;
/*  22:    */   private String itemLabel;
/*  23: 68 */   private String element = "span";
/*  24:    */   private String delimiter;
/*  25:    */   
/*  26:    */   public void setItems(Object items)
/*  27:    */   {
/*  28: 83 */     Assert.notNull(items, "'items' must not be null");
/*  29: 84 */     this.items = items;
/*  30:    */   }
/*  31:    */   
/*  32:    */   protected Object getItems()
/*  33:    */   {
/*  34: 92 */     return this.items;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setItemValue(String itemValue)
/*  38:    */   {
/*  39:101 */     Assert.hasText(itemValue, "'itemValue' must not be empty");
/*  40:102 */     this.itemValue = itemValue;
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected String getItemValue()
/*  44:    */   {
/*  45:110 */     return this.itemValue;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setItemLabel(String itemLabel)
/*  49:    */   {
/*  50:119 */     Assert.hasText(itemLabel, "'itemLabel' must not be empty");
/*  51:120 */     this.itemLabel = itemLabel;
/*  52:    */   }
/*  53:    */   
/*  54:    */   protected String getItemLabel()
/*  55:    */   {
/*  56:128 */     return this.itemLabel;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void setDelimiter(String delimiter)
/*  60:    */   {
/*  61:137 */     this.delimiter = delimiter;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public String getDelimiter()
/*  65:    */   {
/*  66:145 */     return this.delimiter;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void setElement(String element)
/*  70:    */   {
/*  71:154 */     Assert.hasText(element, "'element' cannot be null or blank");
/*  72:155 */     this.element = element;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public String getElement()
/*  76:    */   {
/*  77:163 */     return this.element;
/*  78:    */   }
/*  79:    */   
/*  80:    */   protected String resolveId()
/*  81:    */     throws JspException
/*  82:    */   {
/*  83:173 */     Object id = evaluate("id", getId());
/*  84:174 */     if (id != null)
/*  85:    */     {
/*  86:175 */       String idString = id.toString();
/*  87:176 */       return StringUtils.hasText(idString) ? TagIdGenerator.nextId(idString, this.pageContext) : null;
/*  88:    */     }
/*  89:178 */     return autogenerateId();
/*  90:    */   }
/*  91:    */   
/*  92:    */   protected int writeTagContent(TagWriter tagWriter)
/*  93:    */     throws JspException
/*  94:    */   {
/*  95:188 */     Object items = getItems();
/*  96:189 */     Object itemsObject = (items instanceof String) ? evaluate("items", items) : items;
/*  97:    */     
/*  98:191 */     String itemValue = getItemValue();
/*  99:192 */     String itemLabel = getItemLabel();
/* 100:193 */     String valueProperty = 
/* 101:194 */       itemValue != null ? ObjectUtils.getDisplayString(evaluate("itemValue", itemValue)) : null;
/* 102:195 */     String labelProperty = 
/* 103:196 */       itemLabel != null ? ObjectUtils.getDisplayString(evaluate("itemLabel", itemLabel)) : null;
/* 104:    */     
/* 105:198 */     Class<?> boundType = getBindStatus().getValueType();
/* 106:199 */     if ((itemsObject == null) && (boundType != null) && (boundType.isEnum())) {
/* 107:200 */       itemsObject = boundType.getEnumConstants();
/* 108:    */     }
/* 109:203 */     if (itemsObject == null) {
/* 110:204 */       throw new IllegalArgumentException("Attribute 'items' is required and must be a Collection, an Array or a Map");
/* 111:    */     }
/* 112:207 */     if (itemsObject.getClass().isArray())
/* 113:    */     {
/* 114:208 */       Object[] itemsArray = (Object[])itemsObject;
/* 115:209 */       for (int i = 0; i < itemsArray.length; i++)
/* 116:    */       {
/* 117:210 */         Object item = itemsArray[i];
/* 118:211 */         writeObjectEntry(tagWriter, valueProperty, labelProperty, item, i);
/* 119:    */       }
/* 120:    */     }
/* 121:214 */     else if ((itemsObject instanceof Collection))
/* 122:    */     {
/* 123:215 */       Collection optionCollection = (Collection)itemsObject;
/* 124:216 */       int itemIndex = 0;
/* 125:217 */       for (Iterator it = optionCollection.iterator(); it.hasNext(); itemIndex++)
/* 126:    */       {
/* 127:218 */         Object item = it.next();
/* 128:219 */         writeObjectEntry(tagWriter, valueProperty, labelProperty, item, itemIndex);
/* 129:    */       }
/* 130:    */     }
/* 131:222 */     else if ((itemsObject instanceof Map))
/* 132:    */     {
/* 133:223 */       Map optionMap = (Map)itemsObject;
/* 134:224 */       int itemIndex = 0;
/* 135:225 */       for (Iterator it = optionMap.entrySet().iterator(); it.hasNext(); itemIndex++)
/* 136:    */       {
/* 137:226 */         Map.Entry entry = (Map.Entry)it.next();
/* 138:227 */         writeMapEntry(tagWriter, valueProperty, labelProperty, entry, itemIndex);
/* 139:    */       }
/* 140:    */     }
/* 141:    */     else
/* 142:    */     {
/* 143:231 */       throw new IllegalArgumentException("Attribute 'items' must be an array, a Collection or a Map");
/* 144:    */     }
/* 145:234 */     return 0;
/* 146:    */   }
/* 147:    */   
/* 148:    */   private void writeObjectEntry(TagWriter tagWriter, String valueProperty, String labelProperty, Object item, int itemIndex)
/* 149:    */     throws JspException
/* 150:    */   {
/* 151:240 */     BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(item);
/* 152:    */     Object renderValue;
/* 153:    */     Object renderValue;
/* 154:242 */     if (valueProperty != null)
/* 155:    */     {
/* 156:243 */       renderValue = wrapper.getPropertyValue(valueProperty);
/* 157:    */     }
/* 158:    */     else
/* 159:    */     {
/* 160:    */       Object renderValue;
/* 161:245 */       if ((item instanceof Enum)) {
/* 162:246 */         renderValue = ((Enum)item).name();
/* 163:    */       } else {
/* 164:249 */         renderValue = item;
/* 165:    */       }
/* 166:    */     }
/* 167:251 */     Object renderLabel = labelProperty != null ? wrapper.getPropertyValue(labelProperty) : item;
/* 168:252 */     writeElementTag(tagWriter, item, renderValue, renderLabel, itemIndex);
/* 169:    */   }
/* 170:    */   
/* 171:    */   private void writeMapEntry(TagWriter tagWriter, String valueProperty, String labelProperty, Map.Entry entry, int itemIndex)
/* 172:    */     throws JspException
/* 173:    */   {
/* 174:258 */     Object mapKey = entry.getKey();
/* 175:259 */     Object mapValue = entry.getValue();
/* 176:260 */     BeanWrapper mapKeyWrapper = PropertyAccessorFactory.forBeanPropertyAccess(mapKey);
/* 177:261 */     BeanWrapper mapValueWrapper = PropertyAccessorFactory.forBeanPropertyAccess(mapValue);
/* 178:262 */     Object renderValue = valueProperty != null ? 
/* 179:263 */       mapKeyWrapper.getPropertyValue(valueProperty) : mapKey.toString();
/* 180:264 */     Object renderLabel = labelProperty != null ? 
/* 181:265 */       mapValueWrapper.getPropertyValue(labelProperty) : mapValue.toString();
/* 182:266 */     writeElementTag(tagWriter, mapKey, renderValue, renderLabel, itemIndex);
/* 183:    */   }
/* 184:    */   
/* 185:    */   private void writeElementTag(TagWriter tagWriter, Object item, Object value, Object label, int itemIndex)
/* 186:    */     throws JspException
/* 187:    */   {
/* 188:272 */     tagWriter.startTag(getElement());
/* 189:273 */     if (itemIndex > 0)
/* 190:    */     {
/* 191:274 */       Object resolvedDelimiter = evaluate("delimiter", getDelimiter());
/* 192:275 */       if (resolvedDelimiter != null) {
/* 193:276 */         tagWriter.appendValue(resolvedDelimiter.toString());
/* 194:    */       }
/* 195:    */     }
/* 196:279 */     tagWriter.startTag("input");
/* 197:280 */     String id = resolveId();
/* 198:281 */     writeOptionalAttribute(tagWriter, "id", id);
/* 199:282 */     writeOptionalAttribute(tagWriter, "name", getName());
/* 200:283 */     writeOptionalAttributes(tagWriter);
/* 201:284 */     tagWriter.writeAttribute("type", getInputType());
/* 202:285 */     renderFromValue(item, value, tagWriter);
/* 203:286 */     tagWriter.endTag();
/* 204:287 */     tagWriter.startTag("label");
/* 205:288 */     tagWriter.writeAttribute("for", id);
/* 206:289 */     tagWriter.appendValue(convertToDisplayString(label));
/* 207:290 */     tagWriter.endTag();
/* 208:291 */     tagWriter.endTag();
/* 209:    */   }
/* 210:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.AbstractMultiCheckedElementTag
 * JD-Core Version:    0.7.0.1
 */