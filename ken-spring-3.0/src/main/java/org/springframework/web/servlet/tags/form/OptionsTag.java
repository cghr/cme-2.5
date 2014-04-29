/*   1:    */ package org.springframework.web.servlet.tags.form;
/*   2:    */ 
/*   3:    */ import javax.servlet.jsp.JspException;
/*   4:    */ import javax.servlet.jsp.PageContext;
/*   5:    */ import org.springframework.util.Assert;
/*   6:    */ import org.springframework.util.ObjectUtils;
/*   7:    */ import org.springframework.util.StringUtils;
/*   8:    */ import org.springframework.web.servlet.support.BindStatus;
/*   9:    */ import org.springframework.web.util.TagUtils;
/*  10:    */ 
/*  11:    */ public class OptionsTag
/*  12:    */   extends AbstractHtmlElementTag
/*  13:    */ {
/*  14:    */   private Object items;
/*  15:    */   private String itemValue;
/*  16:    */   private String itemLabel;
/*  17:    */   private String disabled;
/*  18:    */   
/*  19:    */   public void setItems(Object items)
/*  20:    */   {
/*  21: 71 */     this.items = items;
/*  22:    */   }
/*  23:    */   
/*  24:    */   protected Object getItems()
/*  25:    */   {
/*  26: 80 */     return this.items;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setItemValue(String itemValue)
/*  30:    */   {
/*  31: 91 */     Assert.hasText(itemValue, "'itemValue' must not be empty");
/*  32: 92 */     this.itemValue = itemValue;
/*  33:    */   }
/*  34:    */   
/*  35:    */   protected String getItemValue()
/*  36:    */   {
/*  37:100 */     return this.itemValue;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setItemLabel(String itemLabel)
/*  41:    */   {
/*  42:109 */     Assert.hasText(itemLabel, "'itemLabel' must not be empty");
/*  43:110 */     this.itemLabel = itemLabel;
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected String getItemLabel()
/*  47:    */   {
/*  48:119 */     return this.itemLabel;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setDisabled(String disabled)
/*  52:    */   {
/*  53:128 */     this.disabled = disabled;
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected String getDisabled()
/*  57:    */   {
/*  58:135 */     return this.disabled;
/*  59:    */   }
/*  60:    */   
/*  61:    */   protected boolean isDisabled()
/*  62:    */     throws JspException
/*  63:    */   {
/*  64:143 */     return evaluateBoolean("disabled", getDisabled());
/*  65:    */   }
/*  66:    */   
/*  67:    */   protected int writeTagContent(TagWriter tagWriter)
/*  68:    */     throws JspException
/*  69:    */   {
/*  70:149 */     SelectTag selectTag = getSelectTag();
/*  71:150 */     Object items = getItems();
/*  72:151 */     Object itemsObject = null;
/*  73:152 */     if (items != null)
/*  74:    */     {
/*  75:153 */       itemsObject = (items instanceof String) ? evaluate("items", items) : items;
/*  76:    */     }
/*  77:    */     else
/*  78:    */     {
/*  79:155 */       Class<?> selectTagBoundType = selectTag.getBindStatus().getValueType();
/*  80:156 */       if ((selectTagBoundType != null) && (selectTagBoundType.isEnum())) {
/*  81:157 */         itemsObject = selectTagBoundType.getEnumConstants();
/*  82:    */       }
/*  83:    */     }
/*  84:160 */     if (itemsObject != null)
/*  85:    */     {
/*  86:161 */       String selectName = selectTag.getName();
/*  87:162 */       String itemValue = getItemValue();
/*  88:163 */       String itemLabel = getItemLabel();
/*  89:164 */       String valueProperty = 
/*  90:165 */         itemValue != null ? ObjectUtils.getDisplayString(evaluate("itemValue", itemValue)) : null;
/*  91:166 */       String labelProperty = 
/*  92:167 */         itemLabel != null ? ObjectUtils.getDisplayString(evaluate("itemLabel", itemLabel)) : null;
/*  93:168 */       OptionsWriter optionWriter = new OptionsWriter(selectName, itemsObject, valueProperty, labelProperty);
/*  94:169 */       optionWriter.writeOptions(tagWriter);
/*  95:    */     }
/*  96:171 */     return 0;
/*  97:    */   }
/*  98:    */   
/*  99:    */   protected String resolveId()
/* 100:    */     throws JspException
/* 101:    */   {
/* 102:180 */     Object id = evaluate("id", getId());
/* 103:181 */     if (id != null)
/* 104:    */     {
/* 105:182 */       String idString = id.toString();
/* 106:183 */       return StringUtils.hasText(idString) ? TagIdGenerator.nextId(idString, this.pageContext) : null;
/* 107:    */     }
/* 108:185 */     return null;
/* 109:    */   }
/* 110:    */   
/* 111:    */   private SelectTag getSelectTag()
/* 112:    */   {
/* 113:189 */     TagUtils.assertHasAncestorOfType(this, SelectTag.class, "options", "select");
/* 114:190 */     return (SelectTag)findAncestorWithClass(this, SelectTag.class);
/* 115:    */   }
/* 116:    */   
/* 117:    */   protected BindStatus getBindStatus()
/* 118:    */   {
/* 119:195 */     return (BindStatus)this.pageContext.getAttribute("org.springframework.web.servlet.tags.form.SelectTag.listValue");
/* 120:    */   }
/* 121:    */   
/* 122:    */   private class OptionsWriter
/* 123:    */     extends OptionWriter
/* 124:    */   {
/* 125:    */     private final String selectName;
/* 126:    */     
/* 127:    */     public OptionsWriter(String selectName, Object optionSource, String valueProperty, String labelProperty)
/* 128:    */     {
/* 129:207 */       super(OptionsTag.this.getBindStatus(), valueProperty, labelProperty, OptionsTag.this.isHtmlEscape());
/* 130:208 */       this.selectName = selectName;
/* 131:    */     }
/* 132:    */     
/* 133:    */     protected boolean isOptionDisabled()
/* 134:    */       throws JspException
/* 135:    */     {
/* 136:213 */       return OptionsTag.this.isDisabled();
/* 137:    */     }
/* 138:    */     
/* 139:    */     protected void writeCommonAttributes(TagWriter tagWriter)
/* 140:    */       throws JspException
/* 141:    */     {
/* 142:218 */       OptionsTag.this.writeOptionalAttribute(tagWriter, "id", OptionsTag.this.resolveId());
/* 143:219 */       OptionsTag.this.writeOptionalAttributes(tagWriter);
/* 144:    */     }
/* 145:    */     
/* 146:    */     protected String processOptionValue(String value)
/* 147:    */     {
/* 148:224 */       return OptionsTag.this.processFieldValue(this.selectName, value, "option");
/* 149:    */     }
/* 150:    */   }
/* 151:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.OptionsTag
 * JD-Core Version:    0.7.0.1
 */