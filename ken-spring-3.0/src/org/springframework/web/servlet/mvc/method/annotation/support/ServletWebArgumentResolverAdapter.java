/*  1:   */ package org.springframework.web.servlet.mvc.method.annotation.support;
/*  2:   */ 
/*  3:   */ import org.springframework.web.bind.support.WebArgumentResolver;
/*  4:   */ import org.springframework.web.context.request.NativeWebRequest;
/*  5:   */ import org.springframework.web.context.request.RequestAttributes;
/*  6:   */ import org.springframework.web.context.request.RequestContextHolder;
/*  7:   */ import org.springframework.web.context.request.ServletRequestAttributes;
/*  8:   */ import org.springframework.web.context.request.ServletWebRequest;
/*  9:   */ import org.springframework.web.method.annotation.support.AbstractWebArgumentResolverAdapter;
/* 10:   */ 
/* 11:   */ public class ServletWebArgumentResolverAdapter
/* 12:   */   extends AbstractWebArgumentResolverAdapter
/* 13:   */ {
/* 14:   */   public ServletWebArgumentResolverAdapter(WebArgumentResolver adaptee)
/* 15:   */   {
/* 16:37 */     super(adaptee);
/* 17:   */   }
/* 18:   */   
/* 19:   */   protected NativeWebRequest getWebRequest()
/* 20:   */   {
/* 21:42 */     RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
/* 22:43 */     if ((requestAttributes instanceof ServletRequestAttributes))
/* 23:   */     {
/* 24:44 */       ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)requestAttributes;
/* 25:45 */       return new ServletWebRequest(servletRequestAttributes.getRequest());
/* 26:   */     }
/* 27:47 */     return null;
/* 28:   */   }
/* 29:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.annotation.support.ServletWebArgumentResolverAdapter
 * JD-Core Version:    0.7.0.1
 */