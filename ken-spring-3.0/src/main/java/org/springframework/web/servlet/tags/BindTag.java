/*   1:    */ package org.springframework.web.servlet.tags;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditor;
/*   4:    */ import javax.servlet.jsp.JspTagException;
/*   5:    */ import javax.servlet.jsp.PageContext;
/*   6:    */ import org.springframework.validation.Errors;
/*   7:    */ import org.springframework.web.servlet.support.BindStatus;
/*   8:    */ import org.springframework.web.util.ExpressionEvaluationUtils;
/*   9:    */ 
/*  10:    */ public class BindTag
/*  11:    */   extends HtmlEscapingAwareTag
/*  12:    */   implements EditorAwareTag
/*  13:    */ {
/*  14:    */   public static final String STATUS_VARIABLE_NAME = "status";
/*  15:    */   private String path;
/*  16: 59 */   private boolean ignoreNestedPath = false;
/*  17:    */   private BindStatus status;
/*  18:    */   private Object previousPageStatus;
/*  19:    */   private Object previousRequestStatus;
/*  20:    */   
/*  21:    */   public void setPath(String path)
/*  22:    */   {
/*  23: 78 */     this.path = path;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public String getPath()
/*  27:    */   {
/*  28: 85 */     return this.path;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setIgnoreNestedPath(boolean ignoreNestedPath)
/*  32:    */   {
/*  33: 93 */     this.ignoreNestedPath = ignoreNestedPath;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public boolean isIgnoreNestedPath()
/*  37:    */   {
/*  38:100 */     return this.ignoreNestedPath;
/*  39:    */   }
/*  40:    */   
/*  41:    */   protected final int doStartTagInternal()
/*  42:    */     throws Exception
/*  43:    */   {
/*  44:106 */     String resolvedPath = ExpressionEvaluationUtils.evaluateString("path", getPath(), this.pageContext);
/*  45:108 */     if (!isIgnoreNestedPath())
/*  46:    */     {
/*  47:109 */       String nestedPath = (String)this.pageContext.getAttribute(
/*  48:110 */         "nestedPath", 2);
/*  49:112 */       if ((nestedPath != null) && (!resolvedPath.startsWith(nestedPath)) && 
/*  50:113 */         (!resolvedPath.equals(nestedPath.substring(0, nestedPath.length() - 1)))) {
/*  51:114 */         resolvedPath = nestedPath + resolvedPath;
/*  52:    */       }
/*  53:    */     }
/*  54:    */     try
/*  55:    */     {
/*  56:119 */       this.status = new BindStatus(getRequestContext(), resolvedPath, isHtmlEscape());
/*  57:    */     }
/*  58:    */     catch (IllegalStateException ex)
/*  59:    */     {
/*  60:122 */       throw new JspTagException(ex.getMessage());
/*  61:    */     }
/*  62:126 */     this.previousPageStatus = this.pageContext.getAttribute("status", 1);
/*  63:127 */     this.previousRequestStatus = this.pageContext.getAttribute("status", 2);
/*  64:    */     
/*  65:    */ 
/*  66:    */ 
/*  67:131 */     this.pageContext.removeAttribute("status", 1);
/*  68:132 */     this.pageContext.setAttribute("status", this.status, 2);
/*  69:    */     
/*  70:134 */     return 1;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public int doEndTag()
/*  74:    */   {
/*  75:140 */     if (this.previousPageStatus != null) {
/*  76:141 */       this.pageContext.setAttribute("status", this.previousPageStatus, 1);
/*  77:    */     }
/*  78:143 */     if (this.previousRequestStatus != null) {
/*  79:144 */       this.pageContext.setAttribute("status", this.previousRequestStatus, 2);
/*  80:    */     } else {
/*  81:147 */       this.pageContext.removeAttribute("status", 2);
/*  82:    */     }
/*  83:149 */     return 6;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public final String getProperty()
/*  87:    */   {
/*  88:161 */     return this.status.getExpression();
/*  89:    */   }
/*  90:    */   
/*  91:    */   public final Errors getErrors()
/*  92:    */   {
/*  93:170 */     return this.status.getErrors();
/*  94:    */   }
/*  95:    */   
/*  96:    */   public final PropertyEditor getEditor()
/*  97:    */   {
/*  98:174 */     return this.status.getEditor();
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void doFinally()
/* 102:    */   {
/* 103:180 */     super.doFinally();
/* 104:181 */     this.status = null;
/* 105:182 */     this.previousPageStatus = null;
/* 106:183 */     this.previousRequestStatus = null;
/* 107:    */   }
/* 108:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.BindTag
 * JD-Core Version:    0.7.0.1
 */