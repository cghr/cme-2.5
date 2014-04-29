/*   1:    */ package org.springframework.web.servlet.tags;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import javax.servlet.jsp.JspException;
/*   5:    */ import javax.servlet.jsp.JspWriter;
/*   6:    */ import javax.servlet.jsp.tagext.BodyContent;
/*   7:    */ import javax.servlet.jsp.tagext.BodyTag;
/*   8:    */ import org.springframework.web.util.ExpressionEvaluationUtils;
/*   9:    */ import org.springframework.web.util.HtmlUtils;
/*  10:    */ import org.springframework.web.util.JavaScriptUtils;
/*  11:    */ 
/*  12:    */ public class EscapeBodyTag
/*  13:    */   extends HtmlEscapingAwareTag
/*  14:    */   implements BodyTag
/*  15:    */ {
/*  16: 48 */   private boolean javaScriptEscape = false;
/*  17:    */   private BodyContent bodyContent;
/*  18:    */   
/*  19:    */   public void setJavaScriptEscape(String javaScriptEscape)
/*  20:    */     throws JspException
/*  21:    */   {
/*  22: 58 */     this.javaScriptEscape = 
/*  23: 59 */       ExpressionEvaluationUtils.evaluateBoolean("javaScriptEscape", javaScriptEscape, this.pageContext);
/*  24:    */   }
/*  25:    */   
/*  26:    */   protected int doStartTagInternal()
/*  27:    */   {
/*  28: 66 */     return 2;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void doInitBody() {}
/*  32:    */   
/*  33:    */   public void setBodyContent(BodyContent bodyContent)
/*  34:    */   {
/*  35: 74 */     this.bodyContent = bodyContent;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public int doAfterBody()
/*  39:    */     throws JspException
/*  40:    */   {
/*  41:    */     try
/*  42:    */     {
/*  43: 80 */       String content = readBodyContent();
/*  44:    */       
/*  45: 82 */       content = isHtmlEscape() ? HtmlUtils.htmlEscape(content) : content;
/*  46: 83 */       content = this.javaScriptEscape ? JavaScriptUtils.javaScriptEscape(content) : content;
/*  47: 84 */       writeBodyContent(content);
/*  48:    */     }
/*  49:    */     catch (IOException ex)
/*  50:    */     {
/*  51: 87 */       throw new JspException("Could not write escaped body", ex);
/*  52:    */     }
/*  53: 89 */     return 0;
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected String readBodyContent()
/*  57:    */     throws IOException
/*  58:    */   {
/*  59: 98 */     return this.bodyContent.getString();
/*  60:    */   }
/*  61:    */   
/*  62:    */   protected void writeBodyContent(String content)
/*  63:    */     throws IOException
/*  64:    */   {
/*  65:108 */     this.bodyContent.getEnclosingWriter().print(content);
/*  66:    */   }
/*  67:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.EscapeBodyTag
 * JD-Core Version:    0.7.0.1
 */