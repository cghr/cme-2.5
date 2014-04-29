/*  1:   */ package org.springframework.web.servlet.tags;
/*  2:   */ 
/*  3:   */ import javax.servlet.jsp.JspException;
/*  4:   */ import org.springframework.web.servlet.support.RequestContext;
/*  5:   */ import org.springframework.web.util.ExpressionEvaluationUtils;
/*  6:   */ 
/*  7:   */ public class HtmlEscapeTag
/*  8:   */   extends RequestContextAwareTag
/*  9:   */ {
/* 10:   */   private String defaultHtmlEscape;
/* 11:   */   
/* 12:   */   public void setDefaultHtmlEscape(String defaultHtmlEscape)
/* 13:   */   {
/* 14:44 */     this.defaultHtmlEscape = defaultHtmlEscape;
/* 15:   */   }
/* 16:   */   
/* 17:   */   protected int doStartTagInternal()
/* 18:   */     throws JspException
/* 19:   */   {
/* 20:50 */     boolean resolvedDefaultHtmlEscape = 
/* 21:51 */       ExpressionEvaluationUtils.evaluateBoolean("defaultHtmlEscape", this.defaultHtmlEscape, this.pageContext);
/* 22:52 */     getRequestContext().setDefaultHtmlEscape(resolvedDefaultHtmlEscape);
/* 23:53 */     return 1;
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.HtmlEscapeTag
 * JD-Core Version:    0.7.0.1
 */