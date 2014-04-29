/*  1:   */ package org.springframework.web.context.support;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import javax.servlet.ServletException;
/*  5:   */ import javax.servlet.http.HttpServlet;
/*  6:   */ import javax.servlet.http.HttpServletRequest;
/*  7:   */ import javax.servlet.http.HttpServletResponse;
/*  8:   */ import org.springframework.context.i18n.LocaleContextHolder;
/*  9:   */ import org.springframework.util.StringUtils;
/* 10:   */ import org.springframework.web.HttpRequestHandler;
/* 11:   */ import org.springframework.web.HttpRequestMethodNotSupportedException;
/* 12:   */ import org.springframework.web.context.WebApplicationContext;
/* 13:   */ 
/* 14:   */ public class HttpRequestHandlerServlet
/* 15:   */   extends HttpServlet
/* 16:   */ {
/* 17:   */   private HttpRequestHandler target;
/* 18:   */   
/* 19:   */   public void init()
/* 20:   */     throws ServletException
/* 21:   */   {
/* 22:56 */     WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
/* 23:57 */     this.target = ((HttpRequestHandler)wac.getBean(getServletName(), HttpRequestHandler.class));
/* 24:   */   }
/* 25:   */   
/* 26:   */   protected void service(HttpServletRequest request, HttpServletResponse response)
/* 27:   */     throws ServletException, IOException
/* 28:   */   {
/* 29:65 */     LocaleContextHolder.setLocale(request.getLocale());
/* 30:   */     try
/* 31:   */     {
/* 32:67 */       this.target.handleRequest(request, response);
/* 33:   */     }
/* 34:   */     catch (HttpRequestMethodNotSupportedException ex)
/* 35:   */     {
/* 36:70 */       String[] supportedMethods = ex.getSupportedMethods();
/* 37:71 */       if (supportedMethods != null) {
/* 38:72 */         response.setHeader("Allow", StringUtils.arrayToDelimitedString(supportedMethods, ", "));
/* 39:   */       }
/* 40:74 */       response.sendError(405, ex.getMessage());
/* 41:   */     }
/* 42:   */     finally
/* 43:   */     {
/* 44:77 */       LocaleContextHolder.resetLocaleContext();
/* 45:   */     }
/* 46:   */   }
/* 47:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.support.HttpRequestHandlerServlet
 * JD-Core Version:    0.7.0.1
 */