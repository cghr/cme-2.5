/*  1:   */ package org.springframework.web.servlet.handler;
/*  2:   */ 
/*  3:   */ import java.util.Locale;
/*  4:   */ import javax.servlet.http.HttpServletRequest;
/*  5:   */ import javax.servlet.http.HttpServletResponse;
/*  6:   */ import org.springframework.web.context.request.ServletWebRequest;
/*  7:   */ import org.springframework.web.servlet.support.RequestContextUtils;
/*  8:   */ 
/*  9:   */ public class DispatcherServletWebRequest
/* 10:   */   extends ServletWebRequest
/* 11:   */ {
/* 12:   */   public DispatcherServletWebRequest(HttpServletRequest request)
/* 13:   */   {
/* 14:44 */     super(request);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public DispatcherServletWebRequest(HttpServletRequest request, HttpServletResponse response)
/* 18:   */   {
/* 19:53 */     super(request, response);
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Locale getLocale()
/* 23:   */   {
/* 24:58 */     return RequestContextUtils.getLocale(getRequest());
/* 25:   */   }
/* 26:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.handler.DispatcherServletWebRequest
 * JD-Core Version:    0.7.0.1
 */