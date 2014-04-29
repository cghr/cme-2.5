/*  1:   */ package org.springframework.remoting.caucho;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import javax.servlet.ServletException;
/*  5:   */ import javax.servlet.http.HttpServletRequest;
/*  6:   */ import javax.servlet.http.HttpServletResponse;
/*  7:   */ import org.springframework.web.HttpRequestHandler;
/*  8:   */ import org.springframework.web.HttpRequestMethodNotSupportedException;
/*  9:   */ import org.springframework.web.util.NestedServletException;
/* 10:   */ 
/* 11:   */ public class HessianServiceExporter
/* 12:   */   extends HessianExporter
/* 13:   */   implements HttpRequestHandler
/* 14:   */ {
/* 15:   */   public void handleRequest(HttpServletRequest request, HttpServletResponse response)
/* 16:   */     throws ServletException, IOException
/* 17:   */   {
/* 18:59 */     if (!"POST".equals(request.getMethod())) {
/* 19:60 */       throw new HttpRequestMethodNotSupportedException(request.getMethod(), 
/* 20:61 */         new String[] { "POST" }, "HessianServiceExporter only supports POST requests");
/* 21:   */     }
/* 22:64 */     response.setContentType("application/x-hessian");
/* 23:   */     try
/* 24:   */     {
/* 25:66 */       invoke(request.getInputStream(), response.getOutputStream());
/* 26:   */     }
/* 27:   */     catch (Throwable ex)
/* 28:   */     {
/* 29:69 */       throw new NestedServletException("Hessian skeleton invocation failed", ex);
/* 30:   */     }
/* 31:   */   }
/* 32:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.caucho.HessianServiceExporter
 * JD-Core Version:    0.7.0.1
 */