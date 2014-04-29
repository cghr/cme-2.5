/*   1:    */ package org.springframework.web.servlet.tags.form;
/*   2:    */ 
/*   3:    */ import javax.servlet.jsp.JspException;
/*   4:    */ import javax.servlet.jsp.PageContext;
/*   5:    */ import javax.servlet.jsp.tagext.BodyContent;
/*   6:    */ import javax.servlet.jsp.tagext.BodyTag;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ import org.springframework.web.servlet.support.BindStatus;
/*   9:    */ import org.springframework.web.util.TagUtils;
/*  10:    */ 
/*  11:    */ public class OptionTag
/*  12:    */   extends AbstractHtmlElementBodyTag
/*  13:    */   implements BodyTag
/*  14:    */ {
/*  15:    */   public static final String VALUE_VARIABLE_NAME = "value";
/*  16:    */   public static final String DISPLAY_VALUE_VARIABLE_NAME = "displayValue";
/*  17:    */   private static final String SELECTED_ATTRIBUTE = "selected";
/*  18:    */   private static final String VALUE_ATTRIBUTE = "value";
/*  19:    */   private static final String DISABLED_ATTRIBUTE = "disabled";
/*  20:    */   private Object value;
/*  21:    */   private String label;
/*  22:    */   private Object oldValue;
/*  23:    */   private Object oldDisplayValue;
/*  24:    */   private String disabled;
/*  25:    */   
/*  26:    */   public void setValue(Object value)
/*  27:    */   {
/*  28: 99 */     this.value = value;
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected Object getValue()
/*  32:    */   {
/*  33:106 */     return this.value;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setDisabled(String disabled)
/*  37:    */   {
/*  38:115 */     this.disabled = disabled;
/*  39:    */   }
/*  40:    */   
/*  41:    */   protected String getDisabled()
/*  42:    */   {
/*  43:122 */     return this.disabled;
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected boolean isDisabled()
/*  47:    */     throws JspException
/*  48:    */   {
/*  49:130 */     return evaluateBoolean("disabled", getDisabled());
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setLabel(String label)
/*  53:    */   {
/*  54:138 */     Assert.notNull(label, "'label' must not be null");
/*  55:139 */     this.label = label;
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected String getLabel()
/*  59:    */   {
/*  60:146 */     return this.label;
/*  61:    */   }
/*  62:    */   
/*  63:    */   protected void renderDefaultContent(TagWriter tagWriter)
/*  64:    */     throws JspException
/*  65:    */   {
/*  66:152 */     Object value = this.pageContext.getAttribute("value");
/*  67:153 */     String label = getLabelValue(value);
/*  68:154 */     renderOption(value, label, tagWriter);
/*  69:    */   }
/*  70:    */   
/*  71:    */   protected void renderFromBodyContent(BodyContent bodyContent, TagWriter tagWriter)
/*  72:    */     throws JspException
/*  73:    */   {
/*  74:159 */     Object value = this.pageContext.getAttribute("value");
/*  75:160 */     String label = bodyContent.getString();
/*  76:161 */     renderOption(value, label, tagWriter);
/*  77:    */   }
/*  78:    */   
/*  79:    */   protected void onWriteTagContent()
/*  80:    */   {
/*  81:169 */     assertUnderSelectTag();
/*  82:    */   }
/*  83:    */   
/*  84:    */   protected void exposeAttributes()
/*  85:    */     throws JspException
/*  86:    */   {
/*  87:174 */     Object value = resolveValue();
/*  88:175 */     this.oldValue = this.pageContext.getAttribute("value");
/*  89:176 */     this.pageContext.setAttribute("value", value);
/*  90:177 */     this.oldDisplayValue = this.pageContext.getAttribute("displayValue");
/*  91:178 */     this.pageContext.setAttribute("displayValue", getDisplayString(value, getBindStatus().getEditor()));
/*  92:    */   }
/*  93:    */   
/*  94:    */   protected BindStatus getBindStatus()
/*  95:    */   {
/*  96:183 */     return (BindStatus)this.pageContext.getAttribute("org.springframework.web.servlet.tags.form.SelectTag.listValue");
/*  97:    */   }
/*  98:    */   
/*  99:    */   protected void removeAttributes()
/* 100:    */   {
/* 101:188 */     if (this.oldValue != null)
/* 102:    */     {
/* 103:189 */       this.pageContext.setAttribute("value", this.oldValue);
/* 104:190 */       this.oldValue = null;
/* 105:    */     }
/* 106:    */     else
/* 107:    */     {
/* 108:193 */       this.pageContext.removeAttribute("value");
/* 109:    */     }
/* 110:196 */     if (this.oldDisplayValue != null)
/* 111:    */     {
/* 112:197 */       this.pageContext.setAttribute("displayValue", this.oldDisplayValue);
/* 113:198 */       this.oldDisplayValue = null;
/* 114:    */     }
/* 115:    */     else
/* 116:    */     {
/* 117:201 */       this.pageContext.removeAttribute("displayValue");
/* 118:    */     }
/* 119:    */   }
/* 120:    */   
/* 121:    */   private void renderOption(Object value, String label, TagWriter tagWriter)
/* 122:    */     throws JspException
/* 123:    */   {
/* 124:206 */     tagWriter.startTag("option");
/* 125:207 */     writeOptionalAttribute(tagWriter, "id", resolveId());
/* 126:208 */     writeOptionalAttributes(tagWriter);
/* 127:209 */     String renderedValue = getDisplayString(value, getBindStatus().getEditor());
/* 128:210 */     renderedValue = processFieldValue(getSelectTag().getName(), renderedValue, "option");
/* 129:211 */     tagWriter.writeAttribute("value", renderedValue);
/* 130:212 */     if (isSelected(value)) {
/* 131:213 */       tagWriter.writeAttribute("selected", "selected");
/* 132:    */     }
/* 133:215 */     if (isDisabled()) {
/* 134:216 */       tagWriter.writeAttribute("disabled", "disabled");
/* 135:    */     }
/* 136:218 */     tagWriter.appendValue(label);
/* 137:219 */     tagWriter.endTag();
/* 138:    */   }
/* 139:    */   
/* 140:    */   protected String autogenerateId()
/* 141:    */     throws JspException
/* 142:    */   {
/* 143:224 */     return null;
/* 144:    */   }
/* 145:    */   
/* 146:    */   private String getLabelValue(Object resolvedValue)
/* 147:    */     throws JspException
/* 148:    */   {
/* 149:234 */     String label = getLabel();
/* 150:235 */     Object labelObj = label == null ? resolvedValue : evaluate("label", label);
/* 151:236 */     return getDisplayString(labelObj, getBindStatus().getEditor());
/* 152:    */   }
/* 153:    */   
/* 154:    */   private void assertUnderSelectTag()
/* 155:    */   {
/* 156:240 */     TagUtils.assertHasAncestorOfType(this, SelectTag.class, "option", "select");
/* 157:    */   }
/* 158:    */   
/* 159:    */   private SelectTag getSelectTag()
/* 160:    */   {
/* 161:244 */     return (SelectTag)findAncestorWithClass(this, SelectTag.class);
/* 162:    */   }
/* 163:    */   
/* 164:    */   private boolean isSelected(Object resolvedValue)
/* 165:    */   {
/* 166:248 */     return SelectedValueComparator.isSelected(getBindStatus(), resolvedValue);
/* 167:    */   }
/* 168:    */   
/* 169:    */   private Object resolveValue()
/* 170:    */     throws JspException
/* 171:    */   {
/* 172:252 */     return evaluate("value", getValue());
/* 173:    */   }
/* 174:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.OptionTag
 * JD-Core Version:    0.7.0.1
 */