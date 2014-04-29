/*   1:    */ package org.springframework.web.servlet.resource;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import javax.servlet.RequestDispatcher;
/*   5:    */ import javax.servlet.ServletContext;
/*   6:    */ import javax.servlet.ServletException;
/*   7:    */ import javax.servlet.http.HttpServletRequest;
/*   8:    */ import javax.servlet.http.HttpServletResponse;
/*   9:    */ import org.springframework.util.StringUtils;
/*  10:    */ import org.springframework.web.HttpRequestHandler;
/*  11:    */ import org.springframework.web.context.ServletContextAware;
/*  12:    */ 
/*  13:    */ public class DefaultServletHttpRequestHandler
/*  14:    */   implements HttpRequestHandler, ServletContextAware
/*  15:    */ {
/*  16:    */   private static final String COMMON_DEFAULT_SERVLET_NAME = "default";
/*  17:    */   private static final String GAE_DEFAULT_SERVLET_NAME = "_ah_default";
/*  18:    */   private static final String RESIN_DEFAULT_SERVLET_NAME = "resin-file";
/*  19:    */   private static final String WEBLOGIC_DEFAULT_SERVLET_NAME = "FileServlet";
/*  20:    */   private static final String WEBSPHERE_DEFAULT_SERVLET_NAME = "SimpleFileServlet";
/*  21:    */   private String defaultServletName;
/*  22:    */   private ServletContext servletContext;
/*  23:    */   
/*  24:    */   public void setDefaultServletName(String defaultServletName)
/*  25:    */   {
/*  26: 77 */     this.defaultServletName = defaultServletName;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setServletContext(ServletContext servletContext)
/*  30:    */   {
/*  31: 86 */     this.servletContext = servletContext;
/*  32: 87 */     if (!StringUtils.hasText(this.defaultServletName)) {
/*  33: 88 */       if (this.servletContext.getNamedDispatcher("default") != null) {
/*  34: 89 */         this.defaultServletName = "default";
/*  35: 91 */       } else if (this.servletContext.getNamedDispatcher("_ah_default") != null) {
/*  36: 92 */         this.defaultServletName = "_ah_default";
/*  37: 94 */       } else if (this.servletContext.getNamedDispatcher("resin-file") != null) {
/*  38: 95 */         this.defaultServletName = "resin-file";
/*  39: 97 */       } else if (this.servletContext.getNamedDispatcher("FileServlet") != null) {
/*  40: 98 */         this.defaultServletName = "FileServlet";
/*  41:100 */       } else if (this.servletContext.getNamedDispatcher("SimpleFileServlet") != null) {
/*  42:101 */         this.defaultServletName = "SimpleFileServlet";
/*  43:    */       } else {
/*  44:104 */         throw new IllegalStateException("Unable to locate the default servlet for serving static content. Please set the 'defaultServletName' property explicitly.");
/*  45:    */       }
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void handleRequest(HttpServletRequest request, HttpServletResponse response)
/*  50:    */     throws ServletException, IOException
/*  51:    */   {
/*  52:114 */     RequestDispatcher rd = this.servletContext.getNamedDispatcher(this.defaultServletName);
/*  53:115 */     if (rd == null) {
/*  54:116 */       throw new IllegalStateException("A RequestDispatcher could not be located for the default servlet '" + 
/*  55:117 */         this.defaultServletName + "'");
/*  56:    */     }
/*  57:119 */     rd.forward(request, response);
/*  58:    */   }
/*  59:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler
 * JD-Core Version:    0.7.0.1
 */