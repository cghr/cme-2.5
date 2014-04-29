/*  1:   */ package org.springframework.web.servlet.tags;
/*  2:   */ 
/*  3:   */ import javax.servlet.ServletException;
/*  4:   */ import javax.servlet.jsp.JspException;
/*  5:   */ import javax.servlet.jsp.PageContext;
/*  6:   */ import org.springframework.validation.Errors;
/*  7:   */ import org.springframework.web.servlet.support.RequestContext;
/*  8:   */ import org.springframework.web.util.ExpressionEvaluationUtils;
/*  9:   */ 
/* 10:   */ public class BindErrorsTag
/* 11:   */   extends HtmlEscapingAwareTag
/* 12:   */ {
/* 13:   */   public static final String ERRORS_VARIABLE_NAME = "errors";
/* 14:   */   private String name;
/* 15:   */   private Errors errors;
/* 16:   */   
/* 17:   */   public void setName(String name)
/* 18:   */   {
/* 19:50 */     this.name = name;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public String getName()
/* 23:   */   {
/* 24:57 */     return this.name;
/* 25:   */   }
/* 26:   */   
/* 27:   */   protected final int doStartTagInternal()
/* 28:   */     throws ServletException, JspException
/* 29:   */   {
/* 30:63 */     String resolvedName = ExpressionEvaluationUtils.evaluateString("name", this.name, this.pageContext);
/* 31:64 */     this.errors = getRequestContext().getErrors(resolvedName, isHtmlEscape());
/* 32:66 */     if ((this.errors != null) && (this.errors.hasErrors()))
/* 33:   */     {
/* 34:67 */       this.pageContext.setAttribute("errors", this.errors, 2);
/* 35:68 */       return 1;
/* 36:   */     }
/* 37:71 */     return 0;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public int doEndTag()
/* 41:   */   {
/* 42:77 */     this.pageContext.removeAttribute("errors", 2);
/* 43:78 */     return 6;
/* 44:   */   }
/* 45:   */   
/* 46:   */   public final Errors getErrors()
/* 47:   */   {
/* 48:86 */     return this.errors;
/* 49:   */   }
/* 50:   */   
/* 51:   */   public void doFinally()
/* 52:   */   {
/* 53:92 */     super.doFinally();
/* 54:93 */     this.errors = null;
/* 55:   */   }
/* 56:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.BindErrorsTag
 * JD-Core Version:    0.7.0.1
 */