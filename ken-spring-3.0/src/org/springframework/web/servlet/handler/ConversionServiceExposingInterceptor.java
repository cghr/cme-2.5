/*  1:   */ package org.springframework.web.servlet.handler;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import javax.servlet.ServletException;
/*  5:   */ import javax.servlet.http.HttpServletRequest;
/*  6:   */ import javax.servlet.http.HttpServletResponse;
/*  7:   */ import org.springframework.core.convert.ConversionService;
/*  8:   */ import org.springframework.util.Assert;
/*  9:   */ 
/* 10:   */ public class ConversionServiceExposingInterceptor
/* 11:   */   extends HandlerInterceptorAdapter
/* 12:   */ {
/* 13:   */   private final ConversionService conversionService;
/* 14:   */   
/* 15:   */   public ConversionServiceExposingInterceptor(ConversionService conversionService)
/* 16:   */   {
/* 17:48 */     Assert.notNull(conversionService, "The ConversionService may not be null");
/* 18:49 */     this.conversionService = conversionService;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
/* 22:   */     throws ServletException, IOException
/* 23:   */   {
/* 24:57 */     request.setAttribute(ConversionService.class.getName(), this.conversionService);
/* 25:58 */     return true;
/* 26:   */   }
/* 27:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.handler.ConversionServiceExposingInterceptor
 * JD-Core Version:    0.7.0.1
 */