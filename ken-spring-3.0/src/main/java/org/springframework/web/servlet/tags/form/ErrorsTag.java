/*   1:    */ package org.springframework.web.servlet.tags.form;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.Collection;
/*   6:    */ import java.util.List;
/*   7:    */ import javax.servlet.jsp.JspException;
/*   8:    */ import javax.servlet.jsp.PageContext;
/*   9:    */ import javax.servlet.jsp.tagext.BodyTag;
/*  10:    */ import org.springframework.util.Assert;
/*  11:    */ import org.springframework.util.ObjectUtils;
/*  12:    */ import org.springframework.util.StringUtils;
/*  13:    */ import org.springframework.web.servlet.support.BindStatus;
/*  14:    */ 
/*  15:    */ public class ErrorsTag
/*  16:    */   extends AbstractHtmlElementBodyTag
/*  17:    */   implements BodyTag
/*  18:    */ {
/*  19:    */   public static final String MESSAGES_ATTRIBUTE = "messages";
/*  20:    */   public static final String SPAN_TAG = "span";
/*  21: 60 */   private String element = "span";
/*  22: 62 */   private String delimiter = "<br/>";
/*  23:    */   private Object oldMessages;
/*  24:    */   private boolean errorMessagesWereExposed;
/*  25:    */   
/*  26:    */   public void setElement(String element)
/*  27:    */   {
/*  28: 77 */     Assert.hasText(element, "'element' cannot be null or blank");
/*  29: 78 */     this.element = element;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public String getElement()
/*  33:    */   {
/*  34: 85 */     return this.element;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setDelimiter(String delimiter)
/*  38:    */   {
/*  39: 93 */     this.delimiter = delimiter;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public String getDelimiter()
/*  43:    */   {
/*  44:100 */     return this.delimiter;
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected String autogenerateId()
/*  48:    */     throws JspException
/*  49:    */   {
/*  50:114 */     String path = getPropertyPath();
/*  51:115 */     if (("".equals(path)) || ("*".equals(path))) {
/*  52:116 */       path = (String)this.pageContext.getAttribute(
/*  53:117 */         FormTag.MODEL_ATTRIBUTE_VARIABLE_NAME, 2);
/*  54:    */     }
/*  55:119 */     return StringUtils.deleteAny(path, "[]") + ".errors";
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected String getName()
/*  59:    */     throws JspException
/*  60:    */   {
/*  61:129 */     return null;
/*  62:    */   }
/*  63:    */   
/*  64:    */   protected boolean shouldRender()
/*  65:    */     throws JspException
/*  66:    */   {
/*  67:    */     try
/*  68:    */     {
/*  69:140 */       return getBindStatus().isError();
/*  70:    */     }
/*  71:    */     catch (IllegalStateException localIllegalStateException) {}
/*  72:144 */     return false;
/*  73:    */   }
/*  74:    */   
/*  75:    */   protected void renderDefaultContent(TagWriter tagWriter)
/*  76:    */     throws JspException
/*  77:    */   {
/*  78:150 */     tagWriter.startTag(getElement());
/*  79:151 */     writeDefaultAttributes(tagWriter);
/*  80:152 */     String delimiter = ObjectUtils.getDisplayString(evaluate("delimiter", getDelimiter()));
/*  81:153 */     String[] errorMessages = getBindStatus().getErrorMessages();
/*  82:154 */     for (int i = 0; i < errorMessages.length; i++)
/*  83:    */     {
/*  84:155 */       String errorMessage = errorMessages[i];
/*  85:156 */       if (i > 0) {
/*  86:157 */         tagWriter.appendValue(delimiter);
/*  87:    */       }
/*  88:159 */       tagWriter.appendValue(getDisplayString(errorMessage));
/*  89:    */     }
/*  90:161 */     tagWriter.endTag();
/*  91:    */   }
/*  92:    */   
/*  93:    */   protected void exposeAttributes()
/*  94:    */     throws JspException
/*  95:    */   {
/*  96:172 */     List<String> errorMessages = new ArrayList();
/*  97:173 */     errorMessages.addAll((Collection)Arrays.asList(getBindStatus().getErrorMessages()));
/*  98:174 */     this.oldMessages = this.pageContext.getAttribute("messages", 1);
/*  99:175 */     this.pageContext.setAttribute("messages", errorMessages, 1);
/* 100:176 */     this.errorMessagesWereExposed = true;
/* 101:    */   }
/* 102:    */   
/* 103:    */   protected void removeAttributes()
/* 104:    */   {
/* 105:186 */     if (this.errorMessagesWereExposed) {
/* 106:187 */       if (this.oldMessages != null)
/* 107:    */       {
/* 108:188 */         this.pageContext.setAttribute("messages", this.oldMessages, 1);
/* 109:189 */         this.oldMessages = null;
/* 110:    */       }
/* 111:    */       else
/* 112:    */       {
/* 113:192 */         this.pageContext.removeAttribute("messages", 1);
/* 114:    */       }
/* 115:    */     }
/* 116:    */   }
/* 117:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.ErrorsTag
 * JD-Core Version:    0.7.0.1
 */