/*  1:   */ package org.springframework.web.context.request;
/*  2:   */ 
/*  3:   */ import org.apache.log4j.Logger;
/*  4:   */ import org.apache.log4j.NDC;
/*  5:   */ import org.springframework.ui.ModelMap;
/*  6:   */ 
/*  7:   */ public class Log4jNestedDiagnosticContextInterceptor
/*  8:   */   implements WebRequestInterceptor
/*  9:   */ {
/* 10:37 */   protected final Logger log4jLogger = Logger.getLogger(getClass());
/* 11:39 */   private boolean includeClientInfo = false;
/* 12:   */   
/* 13:   */   public void setIncludeClientInfo(boolean includeClientInfo)
/* 14:   */   {
/* 15:47 */     this.includeClientInfo = includeClientInfo;
/* 16:   */   }
/* 17:   */   
/* 18:   */   protected boolean isIncludeClientInfo()
/* 19:   */   {
/* 20:55 */     return this.includeClientInfo;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void preHandle(WebRequest request)
/* 24:   */     throws Exception
/* 25:   */   {
/* 26:63 */     NDC.push(getNestedDiagnosticContextMessage(request));
/* 27:   */   }
/* 28:   */   
/* 29:   */   protected String getNestedDiagnosticContextMessage(WebRequest request)
/* 30:   */   {
/* 31:75 */     return request.getDescription(isIncludeClientInfo());
/* 32:   */   }
/* 33:   */   
/* 34:   */   public void postHandle(WebRequest request, ModelMap model)
/* 35:   */     throws Exception
/* 36:   */   {}
/* 37:   */   
/* 38:   */   public void afterCompletion(WebRequest request, Exception ex)
/* 39:   */     throws Exception
/* 40:   */   {
/* 41:85 */     NDC.pop();
/* 42:86 */     if (NDC.getDepth() == 0) {
/* 43:87 */       NDC.remove();
/* 44:   */     }
/* 45:   */   }
/* 46:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.request.Log4jNestedDiagnosticContextInterceptor
 * JD-Core Version:    0.7.0.1
 */