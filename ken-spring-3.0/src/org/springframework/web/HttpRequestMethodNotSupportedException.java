/*  1:   */ package org.springframework.web;
/*  2:   */ 
/*  3:   */ import java.util.Collection;
/*  4:   */ import javax.servlet.ServletException;
/*  5:   */ 
/*  6:   */ public class HttpRequestMethodNotSupportedException
/*  7:   */   extends ServletException
/*  8:   */ {
/*  9:   */   private String method;
/* 10:   */   private String[] supportedMethods;
/* 11:   */   
/* 12:   */   public HttpRequestMethodNotSupportedException(String method)
/* 13:   */   {
/* 14:41 */     this(method, null);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public HttpRequestMethodNotSupportedException(String method, String[] supportedMethods)
/* 18:   */   {
/* 19:50 */     this(method, supportedMethods, "Request method '" + method + "' not supported");
/* 20:   */   }
/* 21:   */   
/* 22:   */   public HttpRequestMethodNotSupportedException(String method, Collection<String> supportedMethods)
/* 23:   */   {
/* 24:59 */     this(method, (String[])supportedMethods.toArray(new String[supportedMethods.size()]));
/* 25:   */   }
/* 26:   */   
/* 27:   */   public HttpRequestMethodNotSupportedException(String method, String msg)
/* 28:   */   {
/* 29:68 */     this(method, null, msg);
/* 30:   */   }
/* 31:   */   
/* 32:   */   public HttpRequestMethodNotSupportedException(String method, String[] supportedMethods, String msg)
/* 33:   */   {
/* 34:78 */     super(msg);
/* 35:79 */     this.method = method;
/* 36:80 */     this.supportedMethods = supportedMethods;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public String getMethod()
/* 40:   */   {
/* 41:88 */     return this.method;
/* 42:   */   }
/* 43:   */   
/* 44:   */   public String[] getSupportedMethods()
/* 45:   */   {
/* 46:95 */     return this.supportedMethods;
/* 47:   */   }
/* 48:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.HttpRequestMethodNotSupportedException
 * JD-Core Version:    0.7.0.1
 */