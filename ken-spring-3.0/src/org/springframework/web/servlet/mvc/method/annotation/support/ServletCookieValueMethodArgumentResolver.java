/*  1:   */ package org.springframework.web.servlet.mvc.method.annotation.support;
/*  2:   */ 
/*  3:   */ import javax.servlet.http.Cookie;
/*  4:   */ import javax.servlet.http.HttpServletRequest;
/*  5:   */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*  6:   */ import org.springframework.core.MethodParameter;
/*  7:   */ import org.springframework.web.context.request.NativeWebRequest;
/*  8:   */ import org.springframework.web.method.annotation.support.AbstractCookieValueMethodArgumentResolver;
/*  9:   */ import org.springframework.web.util.UrlPathHelper;
/* 10:   */ import org.springframework.web.util.WebUtils;
/* 11:   */ 
/* 12:   */ public class ServletCookieValueMethodArgumentResolver
/* 13:   */   extends AbstractCookieValueMethodArgumentResolver
/* 14:   */ {
/* 15:38 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/* 16:   */   
/* 17:   */   public ServletCookieValueMethodArgumentResolver(ConfigurableBeanFactory beanFactory)
/* 18:   */   {
/* 19:41 */     super(beanFactory);
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void setUrlPathHelper(UrlPathHelper urlPathHelper)
/* 23:   */   {
/* 24:45 */     this.urlPathHelper = urlPathHelper;
/* 25:   */   }
/* 26:   */   
/* 27:   */   protected Object resolveName(String cookieName, MethodParameter parameter, NativeWebRequest webRequest)
/* 28:   */     throws Exception
/* 29:   */   {
/* 30:51 */     HttpServletRequest servletRequest = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/* 31:52 */     Cookie cookieValue = WebUtils.getCookie(servletRequest, cookieName);
/* 32:53 */     if (Cookie.class.isAssignableFrom(parameter.getParameterType())) {
/* 33:54 */       return cookieValue;
/* 34:   */     }
/* 35:56 */     if (cookieValue != null) {
/* 36:57 */       return this.urlPathHelper.decodeRequestString(servletRequest, cookieValue.getValue());
/* 37:   */     }
/* 38:60 */     return null;
/* 39:   */   }
/* 40:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.annotation.support.ServletCookieValueMethodArgumentResolver
 * JD-Core Version:    0.7.0.1
 */