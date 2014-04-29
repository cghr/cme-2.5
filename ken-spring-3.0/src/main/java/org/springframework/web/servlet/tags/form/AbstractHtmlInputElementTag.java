/*   1:    */ package org.springframework.web.servlet.tags.form;
/*   2:    */ 
/*   3:    */ import javax.servlet.jsp.JspException;
/*   4:    */ 
/*   5:    */ public abstract class AbstractHtmlInputElementTag
/*   6:    */   extends AbstractHtmlElementTag
/*   7:    */ {
/*   8:    */   public static final String ONFOCUS_ATTRIBUTE = "onfocus";
/*   9:    */   public static final String ONBLUR_ATTRIBUTE = "onblur";
/*  10:    */   public static final String ONCHANGE_ATTRIBUTE = "onchange";
/*  11:    */   public static final String ACCESSKEY_ATTRIBUTE = "accesskey";
/*  12:    */   public static final String DISABLED_ATTRIBUTE = "disabled";
/*  13:    */   public static final String READONLY_ATTRIBUTE = "readonly";
/*  14:    */   private String onfocus;
/*  15:    */   private String onblur;
/*  16:    */   private String onchange;
/*  17:    */   private String accesskey;
/*  18:    */   private String disabled;
/*  19:    */   private String readonly;
/*  20:    */   
/*  21:    */   public void setOnfocus(String onfocus)
/*  22:    */   {
/*  23: 83 */     this.onfocus = onfocus;
/*  24:    */   }
/*  25:    */   
/*  26:    */   protected String getOnfocus()
/*  27:    */   {
/*  28: 90 */     return this.onfocus;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setOnblur(String onblur)
/*  32:    */   {
/*  33: 98 */     this.onblur = onblur;
/*  34:    */   }
/*  35:    */   
/*  36:    */   protected String getOnblur()
/*  37:    */   {
/*  38:105 */     return this.onblur;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setOnchange(String onchange)
/*  42:    */   {
/*  43:113 */     this.onchange = onchange;
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected String getOnchange()
/*  47:    */   {
/*  48:120 */     return this.onchange;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setAccesskey(String accesskey)
/*  52:    */   {
/*  53:128 */     this.accesskey = accesskey;
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected String getAccesskey()
/*  57:    */   {
/*  58:135 */     return this.accesskey;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void setDisabled(String disabled)
/*  62:    */   {
/*  63:143 */     this.disabled = disabled;
/*  64:    */   }
/*  65:    */   
/*  66:    */   protected String getDisabled()
/*  67:    */   {
/*  68:150 */     return this.disabled;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void setReadonly(String readonly)
/*  72:    */   {
/*  73:159 */     this.readonly = readonly;
/*  74:    */   }
/*  75:    */   
/*  76:    */   protected String getReadonly()
/*  77:    */   {
/*  78:168 */     return this.readonly;
/*  79:    */   }
/*  80:    */   
/*  81:    */   protected void writeOptionalAttributes(TagWriter tagWriter)
/*  82:    */     throws JspException
/*  83:    */   {
/*  84:177 */     super.writeOptionalAttributes(tagWriter);
/*  85:    */     
/*  86:179 */     writeOptionalAttribute(tagWriter, "onfocus", getOnfocus());
/*  87:180 */     writeOptionalAttribute(tagWriter, "onblur", getOnblur());
/*  88:181 */     writeOptionalAttribute(tagWriter, "onchange", getOnchange());
/*  89:182 */     writeOptionalAttribute(tagWriter, "accesskey", getAccesskey());
/*  90:183 */     if (isDisabled()) {
/*  91:184 */       tagWriter.writeAttribute("disabled", "disabled");
/*  92:    */     }
/*  93:186 */     if (isReadonly()) {
/*  94:187 */       writeOptionalAttribute(tagWriter, "readonly", "readonly");
/*  95:    */     }
/*  96:    */   }
/*  97:    */   
/*  98:    */   protected boolean isDisabled()
/*  99:    */     throws JspException
/* 100:    */   {
/* 101:195 */     return evaluateBoolean("disabled", getDisabled());
/* 102:    */   }
/* 103:    */   
/* 104:    */   protected boolean isReadonly()
/* 105:    */     throws JspException
/* 106:    */   {
/* 107:205 */     return evaluateBoolean("readonly", getReadonly());
/* 108:    */   }
/* 109:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.AbstractHtmlInputElementTag
 * JD-Core Version:    0.7.0.1
 */