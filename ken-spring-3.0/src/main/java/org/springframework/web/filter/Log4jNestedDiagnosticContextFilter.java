/*  1:   */ package org.springframework.web.filter;
/*  2:   */ 
/*  3:   */ import javax.servlet.http.HttpServletRequest;
/*  4:   */ import org.apache.log4j.Logger;
/*  5:   */ import org.apache.log4j.NDC;
/*  6:   */ 
/*  7:   */ public class Log4jNestedDiagnosticContextFilter
/*  8:   */   extends AbstractRequestLoggingFilter
/*  9:   */ {
/* 10:43 */   protected final Logger log4jLogger = Logger.getLogger(getClass());
/* 11:   */   
/* 12:   */   protected void beforeRequest(HttpServletRequest request, String message)
/* 13:   */   {
/* 14:52 */     if (this.log4jLogger.isDebugEnabled()) {
/* 15:53 */       this.log4jLogger.debug(message);
/* 16:   */     }
/* 17:55 */     NDC.push(getNestedDiagnosticContextMessage(request));
/* 18:   */   }
/* 19:   */   
/* 20:   */   protected String getNestedDiagnosticContextMessage(HttpServletRequest request)
/* 21:   */   {
/* 22:66 */     return createMessage(request, "", "");
/* 23:   */   }
/* 24:   */   
/* 25:   */   protected void afterRequest(HttpServletRequest request, String message)
/* 26:   */   {
/* 27:75 */     NDC.pop();
/* 28:76 */     if (NDC.getDepth() == 0) {
/* 29:77 */       NDC.remove();
/* 30:   */     }
/* 31:79 */     if (this.log4jLogger.isDebugEnabled()) {
/* 32:80 */       this.log4jLogger.debug(message);
/* 33:   */     }
/* 34:   */   }
/* 35:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.filter.Log4jNestedDiagnosticContextFilter
 * JD-Core Version:    0.7.0.1
 */