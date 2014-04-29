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
/* 11:   */ public class BurlapServiceExporter
/* 12:   */   extends BurlapExporter
/* 13:   */   implements HttpRequestHandler
/* 14:   */ {
/* 15:   */   public void handleRequest(HttpServletRequest request, HttpServletResponse response)
/* 16:   */     throws ServletException, IOException
/* 17:   */   {
/* 18:60 */     if (!"POST".equals(request.getMethod())) {
/* 19:61 */       throw new HttpRequestMethodNotSupportedException(request.getMethod(), 
/* 20:62 */         new String[] { "POST" }, "BurlapServiceExporter only supports POST requests");
/* 21:   */     }
/* 22:   */     try
/* 23:   */     {
/* 24:66 */       invoke(request.getInputStream(), response.getOutputStream());
/* 25:   */     }
/* 26:   */     catch (Throwable ex)
/* 27:   */     {
/* 28:69 */       throw new NestedServletException("Burlap skeleton invocation failed", ex);
/* 29:   */     }
/* 30:   */   }
/* 31:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.caucho.BurlapServiceExporter
 * JD-Core Version:    0.7.0.1
 */