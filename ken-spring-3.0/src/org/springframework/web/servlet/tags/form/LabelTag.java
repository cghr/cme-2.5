/*   1:    */ package org.springframework.web.servlet.tags.form;
/*   2:    */ 
/*   3:    */ import javax.servlet.jsp.JspException;
/*   4:    */ import org.springframework.util.Assert;
/*   5:    */ import org.springframework.util.StringUtils;
/*   6:    */ 
/*   7:    */ public class LabelTag
/*   8:    */   extends AbstractHtmlElementTag
/*   9:    */ {
/*  10:    */   private static final String LABEL_TAG = "label";
/*  11:    */   private static final String FOR_ATTRIBUTE = "for";
/*  12:    */   private TagWriter tagWriter;
/*  13:    */   private String forId;
/*  14:    */   
/*  15:    */   public void setFor(String forId)
/*  16:    */   {
/*  17: 68 */     Assert.notNull(forId, "'forId' must not be null");
/*  18: 69 */     this.forId = forId;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public String getFor()
/*  22:    */   {
/*  23: 77 */     return this.forId;
/*  24:    */   }
/*  25:    */   
/*  26:    */   protected int writeTagContent(TagWriter tagWriter)
/*  27:    */     throws JspException
/*  28:    */   {
/*  29: 88 */     tagWriter.startTag("label");
/*  30: 89 */     tagWriter.writeAttribute("for", resolveFor());
/*  31: 90 */     writeDefaultAttributes(tagWriter);
/*  32: 91 */     tagWriter.forceBlock();
/*  33: 92 */     this.tagWriter = tagWriter;
/*  34: 93 */     return 1;
/*  35:    */   }
/*  36:    */   
/*  37:    */   protected String getName()
/*  38:    */     throws JspException
/*  39:    */   {
/*  40:105 */     return null;
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected String resolveFor()
/*  44:    */     throws JspException
/*  45:    */   {
/*  46:115 */     if (StringUtils.hasText(this.forId)) {
/*  47:116 */       return getDisplayString(evaluate("for", this.forId));
/*  48:    */     }
/*  49:119 */     return autogenerateFor();
/*  50:    */   }
/*  51:    */   
/*  52:    */   protected String autogenerateFor()
/*  53:    */     throws JspException
/*  54:    */   {
/*  55:129 */     return StringUtils.deleteAny(getPropertyPath(), "[]");
/*  56:    */   }
/*  57:    */   
/*  58:    */   public int doEndTag()
/*  59:    */     throws JspException
/*  60:    */   {
/*  61:137 */     this.tagWriter.endTag();
/*  62:138 */     return 6;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void doFinally()
/*  66:    */   {
/*  67:146 */     super.doFinally();
/*  68:147 */     this.tagWriter = null;
/*  69:    */   }
/*  70:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.LabelTag
 * JD-Core Version:    0.7.0.1
 */