/*  1:   */ package org.springframework.web.servlet.tags;
/*  2:   */ 
/*  3:   */ import javax.servlet.jsp.JspException;
/*  4:   */ import org.springframework.web.servlet.support.RequestContext;
/*  5:   */ import org.springframework.web.util.ExpressionEvaluationUtils;
/*  6:   */ 
/*  7:   */ public abstract class HtmlEscapingAwareTag
/*  8:   */   extends RequestContextAwareTag
/*  9:   */ {
/* 10:   */   private Boolean htmlEscape;
/* 11:   */   
/* 12:   */   public void setHtmlEscape(String htmlEscape)
/* 13:   */     throws JspException
/* 14:   */   {
/* 15:49 */     this.htmlEscape = Boolean.valueOf(ExpressionEvaluationUtils.evaluateBoolean("htmlEscape", htmlEscape, this.pageContext));
/* 16:   */   }
/* 17:   */   
/* 18:   */   protected boolean isHtmlEscape()
/* 19:   */   {
/* 20:58 */     if (this.htmlEscape != null) {
/* 21:59 */       return this.htmlEscape.booleanValue();
/* 22:   */     }
/* 23:62 */     return isDefaultHtmlEscape();
/* 24:   */   }
/* 25:   */   
/* 26:   */   protected boolean isDefaultHtmlEscape()
/* 27:   */   {
/* 28:73 */     return getRequestContext().isDefaultHtmlEscape();
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.HtmlEscapingAwareTag
 * JD-Core Version:    0.7.0.1
 */