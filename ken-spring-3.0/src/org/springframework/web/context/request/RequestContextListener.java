/*  1:   */ package org.springframework.web.context.request;
/*  2:   */ 
/*  3:   */ import javax.servlet.ServletRequest;
/*  4:   */ import javax.servlet.ServletRequestEvent;
/*  5:   */ import javax.servlet.ServletRequestListener;
/*  6:   */ import javax.servlet.http.HttpServletRequest;
/*  7:   */ import org.springframework.context.i18n.LocaleContextHolder;
/*  8:   */ 
/*  9:   */ public class RequestContextListener
/* 10:   */   implements ServletRequestListener
/* 11:   */ {
/* 12:49 */   private static final String REQUEST_ATTRIBUTES_ATTRIBUTE = RequestContextListener.class.getName() + ".REQUEST_ATTRIBUTES";
/* 13:   */   
/* 14:   */   public void requestInitialized(ServletRequestEvent requestEvent)
/* 15:   */   {
/* 16:53 */     if (!(requestEvent.getServletRequest() instanceof HttpServletRequest)) {
/* 17:54 */       throw new IllegalArgumentException(
/* 18:55 */         "Request is not an HttpServletRequest: " + requestEvent.getServletRequest());
/* 19:   */     }
/* 20:57 */     HttpServletRequest request = (HttpServletRequest)requestEvent.getServletRequest();
/* 21:58 */     ServletRequestAttributes attributes = new ServletRequestAttributes(request);
/* 22:59 */     request.setAttribute(REQUEST_ATTRIBUTES_ATTRIBUTE, attributes);
/* 23:60 */     LocaleContextHolder.setLocale(request.getLocale());
/* 24:61 */     RequestContextHolder.setRequestAttributes(attributes);
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void requestDestroyed(ServletRequestEvent requestEvent)
/* 28:   */   {
/* 29:65 */     ServletRequestAttributes attributes = 
/* 30:66 */       (ServletRequestAttributes)requestEvent.getServletRequest().getAttribute(REQUEST_ATTRIBUTES_ATTRIBUTE);
/* 31:67 */     ServletRequestAttributes threadAttributes = 
/* 32:68 */       (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
/* 33:69 */     if (threadAttributes != null)
/* 34:   */     {
/* 35:71 */       if (attributes == null) {
/* 36:72 */         attributes = threadAttributes;
/* 37:   */       }
/* 38:74 */       LocaleContextHolder.resetLocaleContext();
/* 39:75 */       RequestContextHolder.resetRequestAttributes();
/* 40:   */     }
/* 41:77 */     if (attributes != null) {
/* 42:78 */       attributes.requestCompleted();
/* 43:   */     }
/* 44:   */   }
/* 45:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.request.RequestContextListener
 * JD-Core Version:    0.7.0.1
 */