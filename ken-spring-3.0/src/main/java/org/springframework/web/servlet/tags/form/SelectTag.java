/*   1:    */ package org.springframework.web.servlet.tags.form;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditor;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.Map;
/*   6:    */ import javax.servlet.jsp.JspException;
/*   7:    */ import javax.servlet.jsp.PageContext;
/*   8:    */ import org.springframework.util.ObjectUtils;
/*   9:    */ import org.springframework.web.servlet.support.BindStatus;
/*  10:    */ 
/*  11:    */ public class SelectTag
/*  12:    */   extends AbstractHtmlInputElementTag
/*  13:    */ {
/*  14:    */   public static final String LIST_VALUE_PAGE_ATTRIBUTE = "org.springframework.web.servlet.tags.form.SelectTag.listValue";
/*  15: 56 */   private static final Object EMPTY = new Object();
/*  16:    */   private Object items;
/*  17:    */   private String itemValue;
/*  18:    */   private String itemLabel;
/*  19:    */   private String size;
/*  20: 87 */   private Object multiple = Boolean.FALSE;
/*  21:    */   private TagWriter tagWriter;
/*  22:    */   
/*  23:    */   public void setItems(Object items)
/*  24:    */   {
/*  25:105 */     this.items = (items != null ? items : EMPTY);
/*  26:    */   }
/*  27:    */   
/*  28:    */   protected Object getItems()
/*  29:    */   {
/*  30:113 */     return this.items;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setItemValue(String itemValue)
/*  34:    */   {
/*  35:124 */     this.itemValue = itemValue;
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected String getItemValue()
/*  39:    */   {
/*  40:132 */     return this.itemValue;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setItemLabel(String itemLabel)
/*  44:    */   {
/*  45:141 */     this.itemLabel = itemLabel;
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected String getItemLabel()
/*  49:    */   {
/*  50:149 */     return this.itemLabel;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setSize(String size)
/*  54:    */   {
/*  55:159 */     this.size = size;
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected String getSize()
/*  59:    */   {
/*  60:167 */     return this.size;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setMultiple(Object multiple)
/*  64:    */   {
/*  65:176 */     this.multiple = multiple;
/*  66:    */   }
/*  67:    */   
/*  68:    */   protected Object getMultiple()
/*  69:    */   {
/*  70:185 */     return this.multiple;
/*  71:    */   }
/*  72:    */   
/*  73:    */   protected int writeTagContent(TagWriter tagWriter)
/*  74:    */     throws JspException
/*  75:    */   {
/*  76:198 */     tagWriter.startTag("select");
/*  77:199 */     writeDefaultAttributes(tagWriter);
/*  78:200 */     if (isMultiple()) {
/*  79:201 */       tagWriter.writeAttribute("multiple", "multiple");
/*  80:    */     }
/*  81:203 */     tagWriter.writeOptionalAttributeValue("size", getDisplayString(evaluate("size", getSize())));
/*  82:    */     
/*  83:205 */     Object items = getItems();
/*  84:206 */     if (items != null)
/*  85:    */     {
/*  86:208 */       if (items != EMPTY)
/*  87:    */       {
/*  88:209 */         Object itemsObject = evaluate("items", items);
/*  89:210 */         if (itemsObject != null)
/*  90:    */         {
/*  91:211 */           final String selectName = getName();
/*  92:212 */           String valueProperty = getItemValue() != null ? 
/*  93:213 */             ObjectUtils.getDisplayString(evaluate("itemValue", getItemValue())) : null;
/*  94:214 */           String labelProperty = getItemLabel() != null ? 
/*  95:215 */             ObjectUtils.getDisplayString(evaluate("itemLabel", getItemLabel())) : null;
/*  96:216 */           OptionWriter optionWriter = 
/*  97:217 */             new OptionWriter(itemsObject, getBindStatus(), valueProperty, labelProperty, isHtmlEscape())
/*  98:    */             {
/*  99:    */               protected String processOptionValue(String resolvedValue)
/* 100:    */               {
/* 101:219 */                 return SelectTag.this.processFieldValue(selectName, resolvedValue, "option");
/* 102:    */               }
/* 103:221 */             };
/* 104:222 */             optionWriter.writeOptions(tagWriter);
/* 105:    */           }
/* 106:    */         }
/* 107:225 */         tagWriter.endTag(true);
/* 108:226 */         writeHiddenTagIfNecessary(tagWriter);
/* 109:227 */         return 0;
/* 110:    */       }
/* 111:231 */       tagWriter.forceBlock();
/* 112:232 */       this.tagWriter = tagWriter;
/* 113:233 */       this.pageContext.setAttribute("org.springframework.web.servlet.tags.form.SelectTag.listValue", getBindStatus());
/* 114:234 */       return 1;
/* 115:    */     }
/* 116:    */     
/* 117:    */     private void writeHiddenTagIfNecessary(TagWriter tagWriter)
/* 118:    */       throws JspException
/* 119:    */     {
/* 120:244 */       if (isMultiple())
/* 121:    */       {
/* 122:245 */         tagWriter.startTag("input");
/* 123:246 */         tagWriter.writeAttribute("type", "hidden");
/* 124:247 */         String name = "_" + getName();
/* 125:248 */         tagWriter.writeAttribute("name", name);
/* 126:249 */         tagWriter.writeAttribute("value", processFieldValue(name, "1", "hidden"));
/* 127:250 */         tagWriter.endTag();
/* 128:    */       }
/* 129:    */     }
/* 130:    */     
/* 131:    */     private boolean isMultiple()
/* 132:    */       throws JspException
/* 133:    */     {
/* 134:255 */       Object multiple = getMultiple();
/* 135:256 */       if ((Boolean.TRUE.equals(multiple)) || ("multiple".equals(multiple))) {
/* 136:257 */         return true;
/* 137:    */       }
/* 138:259 */       if ((this.multiple instanceof String)) {
/* 139:260 */         return evaluateBoolean("multiple", (String)multiple);
/* 140:    */       }
/* 141:262 */       return forceMultiple();
/* 142:    */     }
/* 143:    */     
/* 144:    */     private boolean forceMultiple()
/* 145:    */       throws JspException
/* 146:    */     {
/* 147:270 */       BindStatus bindStatus = getBindStatus();
/* 148:271 */       Class valueType = bindStatus.getValueType();
/* 149:272 */       if ((valueType != null) && (typeRequiresMultiple(valueType))) {
/* 150:273 */         return true;
/* 151:    */       }
/* 152:275 */       if (bindStatus.getEditor() != null)
/* 153:    */       {
/* 154:276 */         Object editorValue = bindStatus.getEditor().getValue();
/* 155:277 */         if ((editorValue != null) && (typeRequiresMultiple(editorValue.getClass()))) {
/* 156:278 */           return true;
/* 157:    */         }
/* 158:    */       }
/* 159:281 */       return false;
/* 160:    */     }
/* 161:    */     
/* 162:    */     private static boolean typeRequiresMultiple(Class type)
/* 163:    */     {
/* 164:289 */       return (type.isArray()) || (Collection.class.isAssignableFrom(type)) || (Map.class.isAssignableFrom(type));
/* 165:    */     }
/* 166:    */     
/* 167:    */     public int doEndTag()
/* 168:    */       throws JspException
/* 169:    */     {
/* 170:298 */       if (this.tagWriter != null)
/* 171:    */       {
/* 172:299 */         this.tagWriter.endTag();
/* 173:300 */         writeHiddenTagIfNecessary(this.tagWriter);
/* 174:    */       }
/* 175:302 */       return 6;
/* 176:    */     }
/* 177:    */     
/* 178:    */     public void doFinally()
/* 179:    */     {
/* 180:311 */       super.doFinally();
/* 181:312 */       this.tagWriter = null;
/* 182:313 */       this.pageContext.removeAttribute("org.springframework.web.servlet.tags.form.SelectTag.listValue");
/* 183:    */     }
/* 184:    */   }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.SelectTag
 * JD-Core Version:    0.7.0.1
 */