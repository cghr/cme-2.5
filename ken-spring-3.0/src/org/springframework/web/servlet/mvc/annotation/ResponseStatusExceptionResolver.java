/*  1:   */ package org.springframework.web.servlet.mvc.annotation;
/*  2:   */ 
/*  3:   */ import javax.servlet.http.HttpServletRequest;
/*  4:   */ import javax.servlet.http.HttpServletResponse;
/*  5:   */ import org.apache.commons.logging.Log;
/*  6:   */ import org.springframework.core.annotation.AnnotationUtils;
/*  7:   */ import org.springframework.http.HttpStatus;
/*  8:   */ import org.springframework.util.StringUtils;
/*  9:   */ import org.springframework.web.bind.annotation.ResponseStatus;
/* 10:   */ import org.springframework.web.servlet.ModelAndView;
/* 11:   */ import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
/* 12:   */ 
/* 13:   */ public class ResponseStatusExceptionResolver
/* 14:   */   extends AbstractHandlerExceptionResolver
/* 15:   */ {
/* 16:   */   protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/* 17:   */   {
/* 18:43 */     ResponseStatus responseStatus = (ResponseStatus)AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
/* 19:44 */     if (responseStatus != null) {
/* 20:   */       try
/* 21:   */       {
/* 22:46 */         return resolveResponseStatus(responseStatus, request, response, handler, ex);
/* 23:   */       }
/* 24:   */       catch (Exception resolveEx)
/* 25:   */       {
/* 26:49 */         this.logger.warn("Handling of @ResponseStatus resulted in Exception", resolveEx);
/* 27:   */       }
/* 28:   */     }
/* 29:52 */     return null;
/* 30:   */   }
/* 31:   */   
/* 32:   */   protected ModelAndView resolveResponseStatus(ResponseStatus responseStatus, HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/* 33:   */     throws Exception
/* 34:   */   {
/* 35:70 */     int statusCode = responseStatus.value().value();
/* 36:71 */     String reason = responseStatus.reason();
/* 37:72 */     if (!StringUtils.hasLength(reason)) {
/* 38:73 */       response.sendError(statusCode);
/* 39:   */     } else {
/* 40:76 */       response.sendError(statusCode, reason);
/* 41:   */     }
/* 42:78 */     return new ModelAndView();
/* 43:   */   }
/* 44:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver
 * JD-Core Version:    0.7.0.1
 */