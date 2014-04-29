/*   1:    */ package org.springframework.web.servlet.tags.form;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import javax.servlet.jsp.JspException;
/*   5:    */ import javax.servlet.jsp.tagext.BodyContent;
/*   6:    */ import javax.servlet.jsp.tagext.BodyTag;
/*   7:    */ import org.springframework.util.StringUtils;
/*   8:    */ 
/*   9:    */ public abstract class AbstractHtmlElementBodyTag
/*  10:    */   extends AbstractHtmlElementTag
/*  11:    */   implements BodyTag
/*  12:    */ {
/*  13:    */   private BodyContent bodyContent;
/*  14:    */   private TagWriter tagWriter;
/*  15:    */   
/*  16:    */   protected int writeTagContent(TagWriter tagWriter)
/*  17:    */     throws JspException
/*  18:    */   {
/*  19: 45 */     onWriteTagContent();
/*  20: 46 */     this.tagWriter = tagWriter;
/*  21: 47 */     if (shouldRender())
/*  22:    */     {
/*  23: 48 */       exposeAttributes();
/*  24: 49 */       return 2;
/*  25:    */     }
/*  26: 52 */     return 0;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public int doEndTag()
/*  30:    */     throws JspException
/*  31:    */   {
/*  32: 64 */     if (shouldRender()) {
/*  33: 65 */       if ((this.bodyContent != null) && (StringUtils.hasText(this.bodyContent.getString()))) {
/*  34: 66 */         renderFromBodyContent(this.bodyContent, this.tagWriter);
/*  35:    */       } else {
/*  36: 69 */         renderDefaultContent(this.tagWriter);
/*  37:    */       }
/*  38:    */     }
/*  39: 72 */     return 6;
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected void renderFromBodyContent(BodyContent bodyContent, TagWriter tagWriter)
/*  43:    */     throws JspException
/*  44:    */   {
/*  45: 82 */     flushBufferedBodyContent(this.bodyContent);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void doFinally()
/*  49:    */   {
/*  50: 90 */     super.doFinally();
/*  51: 91 */     removeAttributes();
/*  52: 92 */     this.tagWriter = null;
/*  53: 93 */     this.bodyContent = null;
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected void onWriteTagContent() {}
/*  57:    */   
/*  58:    */   protected boolean shouldRender()
/*  59:    */     throws JspException
/*  60:    */   {
/*  61:114 */     return true;
/*  62:    */   }
/*  63:    */   
/*  64:    */   protected void exposeAttributes()
/*  65:    */     throws JspException
/*  66:    */   {}
/*  67:    */   
/*  68:    */   protected void removeAttributes() {}
/*  69:    */   
/*  70:    */   protected void flushBufferedBodyContent(BodyContent bodyContent)
/*  71:    */     throws JspException
/*  72:    */   {
/*  73:    */     try
/*  74:    */     {
/*  75:137 */       bodyContent.writeOut(bodyContent.getEnclosingWriter());
/*  76:    */     }
/*  77:    */     catch (IOException e)
/*  78:    */     {
/*  79:140 */       throw new JspException("Unable to write buffered body content.", e);
/*  80:    */     }
/*  81:    */   }
/*  82:    */   
/*  83:    */   protected abstract void renderDefaultContent(TagWriter paramTagWriter)
/*  84:    */     throws JspException;
/*  85:    */   
/*  86:    */   public void doInitBody()
/*  87:    */     throws JspException
/*  88:    */   {}
/*  89:    */   
/*  90:    */   public void setBodyContent(BodyContent bodyContent)
/*  91:    */   {
/*  92:156 */     this.bodyContent = bodyContent;
/*  93:    */   }
/*  94:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.AbstractHtmlElementBodyTag
 * JD-Core Version:    0.7.0.1
 */