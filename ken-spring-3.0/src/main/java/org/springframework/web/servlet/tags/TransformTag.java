/*   1:    */ package org.springframework.web.servlet.tags;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditor;
/*   4:    */ import java.io.IOException;
/*   5:    */ import javax.servlet.jsp.JspException;
/*   6:    */ import javax.servlet.jsp.JspWriter;
/*   7:    */ import javax.servlet.jsp.PageContext;
/*   8:    */ import javax.servlet.jsp.tagext.TagSupport;
/*   9:    */ import org.springframework.web.util.ExpressionEvaluationUtils;
/*  10:    */ import org.springframework.web.util.HtmlUtils;
/*  11:    */ import org.springframework.web.util.TagUtils;
/*  12:    */ 
/*  13:    */ public class TransformTag
/*  14:    */   extends HtmlEscapingAwareTag
/*  15:    */ {
/*  16:    */   private Object value;
/*  17:    */   private String var;
/*  18: 52 */   private String scope = "page";
/*  19:    */   
/*  20:    */   public void setValue(Object value)
/*  21:    */   {
/*  22: 68 */     this.value = value;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void setVar(String var)
/*  26:    */   {
/*  27: 78 */     this.var = var;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setScope(String scope)
/*  31:    */   {
/*  32: 89 */     this.scope = scope;
/*  33:    */   }
/*  34:    */   
/*  35:    */   protected final int doStartTagInternal()
/*  36:    */     throws JspException
/*  37:    */   {
/*  38: 95 */     Object resolvedValue = this.value;
/*  39: 96 */     if ((this.value instanceof String))
/*  40:    */     {
/*  41: 97 */       String strValue = (String)this.value;
/*  42: 98 */       resolvedValue = ExpressionEvaluationUtils.evaluate("value", strValue, this.pageContext);
/*  43:    */     }
/*  44:101 */     if (resolvedValue != null)
/*  45:    */     {
/*  46:103 */       EditorAwareTag tag = (EditorAwareTag)TagSupport.findAncestorWithClass(this, EditorAwareTag.class);
/*  47:104 */       if (tag == null) {
/*  48:105 */         throw new JspException("TransformTag can only be used within EditorAwareTag (e.g. BindTag)");
/*  49:    */       }
/*  50:109 */       String result = null;
/*  51:110 */       PropertyEditor editor = tag.getEditor();
/*  52:111 */       if (editor != null)
/*  53:    */       {
/*  54:113 */         editor.setValue(resolvedValue);
/*  55:114 */         result = editor.getAsText();
/*  56:    */       }
/*  57:    */       else
/*  58:    */       {
/*  59:118 */         result = resolvedValue.toString();
/*  60:    */       }
/*  61:120 */       result = isHtmlEscape() ? HtmlUtils.htmlEscape(result) : result;
/*  62:121 */       String resolvedVar = ExpressionEvaluationUtils.evaluateString("var", this.var, this.pageContext);
/*  63:122 */       if (resolvedVar != null)
/*  64:    */       {
/*  65:123 */         String resolvedScope = ExpressionEvaluationUtils.evaluateString("scope", this.scope, this.pageContext);
/*  66:124 */         this.pageContext.setAttribute(resolvedVar, result, TagUtils.getScope(resolvedScope));
/*  67:    */       }
/*  68:    */       else
/*  69:    */       {
/*  70:    */         try
/*  71:    */         {
/*  72:129 */           this.pageContext.getOut().print(result);
/*  73:    */         }
/*  74:    */         catch (IOException ex)
/*  75:    */         {
/*  76:132 */           throw new JspException(ex);
/*  77:    */         }
/*  78:    */       }
/*  79:    */     }
/*  80:137 */     return 0;
/*  81:    */   }
/*  82:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.TransformTag
 * JD-Core Version:    0.7.0.1
 */