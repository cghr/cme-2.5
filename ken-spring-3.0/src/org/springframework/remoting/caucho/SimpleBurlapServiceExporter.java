/*  1:   */ package org.springframework.remoting.caucho;
/*  2:   */ 
/*  3:   */ import com.sun.net.httpserver.Headers;
/*  4:   */ import com.sun.net.httpserver.HttpExchange;
/*  5:   */ import com.sun.net.httpserver.HttpHandler;
/*  6:   */ import java.io.ByteArrayOutputStream;
/*  7:   */ import java.io.IOException;
/*  8:   */ import org.apache.commons.logging.Log;
/*  9:   */ import org.springframework.util.FileCopyUtils;
/* 10:   */ 
/* 11:   */ public class SimpleBurlapServiceExporter
/* 12:   */   extends BurlapExporter
/* 13:   */   implements HttpHandler
/* 14:   */ {
/* 15:   */   public void handle(HttpExchange exchange)
/* 16:   */     throws IOException
/* 17:   */   {
/* 18:54 */     if (!"POST".equals(exchange.getRequestMethod()))
/* 19:   */     {
/* 20:55 */       exchange.getResponseHeaders().set("Allow", "POST");
/* 21:56 */       exchange.sendResponseHeaders(405, -1L);
/* 22:57 */       return;
/* 23:   */     }
/* 24:60 */     ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
/* 25:   */     try
/* 26:   */     {
/* 27:62 */       invoke(exchange.getRequestBody(), output);
/* 28:   */     }
/* 29:   */     catch (Throwable ex)
/* 30:   */     {
/* 31:65 */       exchange.sendResponseHeaders(500, -1L);
/* 32:66 */       this.logger.error("Burlap skeleton invocation failed", ex);
/* 33:   */     }
/* 34:69 */     exchange.sendResponseHeaders(200, output.size());
/* 35:70 */     FileCopyUtils.copy(output.toByteArray(), exchange.getResponseBody());
/* 36:   */   }
/* 37:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.caucho.SimpleBurlapServiceExporter
 * JD-Core Version:    0.7.0.1
 */