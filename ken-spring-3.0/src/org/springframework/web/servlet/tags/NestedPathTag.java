/*   1:    */ package org.springframework.web.servlet.tags;
/*   2:    */ 
/*   3:    */ import javax.servlet.jsp.JspException;
/*   4:    */ import javax.servlet.jsp.PageContext;
/*   5:    */ import javax.servlet.jsp.tagext.TagSupport;
/*   6:    */ import javax.servlet.jsp.tagext.TryCatchFinally;
/*   7:    */ import org.springframework.web.util.ExpressionEvaluationUtils;
/*   8:    */ 
/*   9:    */ public class NestedPathTag
/*  10:    */   extends TagSupport
/*  11:    */   implements TryCatchFinally
/*  12:    */ {
/*  13:    */   public static final String NESTED_PATH_VARIABLE_NAME = "nestedPath";
/*  14:    */   private String path;
/*  15:    */   private String previousNestedPath;
/*  16:    */   
/*  17:    */   public void setPath(String path)
/*  18:    */   {
/*  19: 64 */     if (path == null) {
/*  20: 65 */       path = "";
/*  21:    */     }
/*  22: 67 */     if ((path.length() > 0) && (!path.endsWith("."))) {
/*  23: 68 */       path = path + ".";
/*  24:    */     }
/*  25: 70 */     this.path = path;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public String getPath()
/*  29:    */   {
/*  30: 77 */     return this.path;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public int doStartTag()
/*  34:    */     throws JspException
/*  35:    */   {
/*  36: 83 */     String resolvedPath = ExpressionEvaluationUtils.evaluateString("path", getPath(), this.pageContext);
/*  37:    */     
/*  38:    */ 
/*  39:    */ 
/*  40: 87 */     this.previousNestedPath = 
/*  41: 88 */       ((String)this.pageContext.getAttribute("nestedPath", 2));
/*  42: 89 */     String nestedPath = 
/*  43: 90 */       this.previousNestedPath != null ? this.previousNestedPath + resolvedPath : resolvedPath;
/*  44: 91 */     this.pageContext.setAttribute("nestedPath", nestedPath, 2);
/*  45:    */     
/*  46: 93 */     return 1;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public int doEndTag()
/*  50:    */   {
/*  51:101 */     if (this.previousNestedPath != null) {
/*  52:103 */       this.pageContext.setAttribute("nestedPath", this.previousNestedPath, 2);
/*  53:    */     } else {
/*  54:107 */       this.pageContext.removeAttribute("nestedPath", 2);
/*  55:    */     }
/*  56:110 */     return 6;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void doCatch(Throwable throwable)
/*  60:    */     throws Throwable
/*  61:    */   {
/*  62:114 */     throw throwable;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void doFinally()
/*  66:    */   {
/*  67:118 */     this.previousNestedPath = null;
/*  68:    */   }
/*  69:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.NestedPathTag
 * JD-Core Version:    0.7.0.1
 */