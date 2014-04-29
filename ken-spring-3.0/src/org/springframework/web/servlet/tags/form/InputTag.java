/*   1:    */ package org.springframework.web.servlet.tags.form;
/*   2:    */ 
/*   3:    */ import javax.servlet.jsp.JspException;
/*   4:    */ 
/*   5:    */ public class InputTag
/*   6:    */   extends AbstractHtmlInputElementTag
/*   7:    */ {
/*   8:    */   public static final String SIZE_ATTRIBUTE = "size";
/*   9:    */   public static final String MAXLENGTH_ATTRIBUTE = "maxlength";
/*  10:    */   public static final String ALT_ATTRIBUTE = "alt";
/*  11:    */   public static final String ONSELECT_ATTRIBUTE = "onselect";
/*  12:    */   public static final String READONLY_ATTRIBUTE = "readonly";
/*  13:    */   public static final String AUTOCOMPLETE_ATTRIBUTE = "autocomplete";
/*  14:    */   private String size;
/*  15:    */   private String maxlength;
/*  16:    */   private String alt;
/*  17:    */   private String onselect;
/*  18:    */   private String autocomplete;
/*  19:    */   
/*  20:    */   public void setSize(String size)
/*  21:    */   {
/*  22: 64 */     this.size = size;
/*  23:    */   }
/*  24:    */   
/*  25:    */   protected String getSize()
/*  26:    */   {
/*  27: 71 */     return this.size;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setMaxlength(String maxlength)
/*  31:    */   {
/*  32: 79 */     this.maxlength = maxlength;
/*  33:    */   }
/*  34:    */   
/*  35:    */   protected String getMaxlength()
/*  36:    */   {
/*  37: 86 */     return this.maxlength;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setAlt(String alt)
/*  41:    */   {
/*  42: 94 */     this.alt = alt;
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected String getAlt()
/*  46:    */   {
/*  47:101 */     return this.alt;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setOnselect(String onselect)
/*  51:    */   {
/*  52:109 */     this.onselect = onselect;
/*  53:    */   }
/*  54:    */   
/*  55:    */   protected String getOnselect()
/*  56:    */   {
/*  57:116 */     return this.onselect;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setAutocomplete(String autocomplete)
/*  61:    */   {
/*  62:124 */     this.autocomplete = autocomplete;
/*  63:    */   }
/*  64:    */   
/*  65:    */   protected String getAutocomplete()
/*  66:    */   {
/*  67:131 */     return this.autocomplete;
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected int writeTagContent(TagWriter tagWriter)
/*  71:    */     throws JspException
/*  72:    */   {
/*  73:142 */     tagWriter.startTag("input");
/*  74:    */     
/*  75:144 */     writeDefaultAttributes(tagWriter);
/*  76:145 */     tagWriter.writeAttribute("type", getType());
/*  77:146 */     writeValue(tagWriter);
/*  78:    */     
/*  79:    */ 
/*  80:149 */     writeOptionalAttribute(tagWriter, "size", getSize());
/*  81:150 */     writeOptionalAttribute(tagWriter, "maxlength", getMaxlength());
/*  82:151 */     writeOptionalAttribute(tagWriter, "alt", getAlt());
/*  83:152 */     writeOptionalAttribute(tagWriter, "onselect", getOnselect());
/*  84:153 */     writeOptionalAttribute(tagWriter, "autocomplete", getAutocomplete());
/*  85:    */     
/*  86:155 */     tagWriter.endTag();
/*  87:156 */     return 0;
/*  88:    */   }
/*  89:    */   
/*  90:    */   protected void writeValue(TagWriter tagWriter)
/*  91:    */     throws JspException
/*  92:    */   {
/*  93:165 */     String value = getDisplayString(getBoundValue(), getPropertyEditor());
/*  94:166 */     tagWriter.writeAttribute("value", processFieldValue(getName(), value, getType()));
/*  95:    */   }
/*  96:    */   
/*  97:    */   protected String getType()
/*  98:    */   {
/*  99:175 */     return "text";
/* 100:    */   }
/* 101:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.InputTag
 * JD-Core Version:    0.7.0.1
 */