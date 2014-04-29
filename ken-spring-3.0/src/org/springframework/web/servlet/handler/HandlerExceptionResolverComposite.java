/*  1:   */ package org.springframework.web.servlet.handler;
/*  2:   */ 
/*  3:   */ import java.util.Collections;
/*  4:   */ import java.util.List;
/*  5:   */ import javax.servlet.http.HttpServletRequest;
/*  6:   */ import javax.servlet.http.HttpServletResponse;
/*  7:   */ import org.springframework.core.Ordered;
/*  8:   */ import org.springframework.web.servlet.HandlerExceptionResolver;
/*  9:   */ import org.springframework.web.servlet.ModelAndView;
/* 10:   */ 
/* 11:   */ public class HandlerExceptionResolverComposite
/* 12:   */   implements HandlerExceptionResolver, Ordered
/* 13:   */ {
/* 14:   */   private List<HandlerExceptionResolver> resolvers;
/* 15:23 */   private int order = 2147483647;
/* 16:   */   
/* 17:   */   public void setOrder(int order)
/* 18:   */   {
/* 19:26 */     this.order = order;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public int getOrder()
/* 23:   */   {
/* 24:30 */     return this.order;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void setExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers)
/* 28:   */   {
/* 29:37 */     this.resolvers = exceptionResolvers;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public List<HandlerExceptionResolver> getExceptionResolvers()
/* 33:   */   {
/* 34:44 */     return Collections.unmodifiableList(this.resolvers);
/* 35:   */   }
/* 36:   */   
/* 37:   */   public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/* 38:   */   {
/* 39:55 */     if (this.resolvers != null) {
/* 40:56 */       for (HandlerExceptionResolver handlerExceptionResolver : this.resolvers)
/* 41:   */       {
/* 42:57 */         ModelAndView mav = handlerExceptionResolver.resolveException(request, response, handler, ex);
/* 43:58 */         if (mav != null) {
/* 44:59 */           return mav;
/* 45:   */         }
/* 46:   */       }
/* 47:   */     }
/* 48:63 */     return null;
/* 49:   */   }
/* 50:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.handler.HandlerExceptionResolverComposite
 * JD-Core Version:    0.7.0.1
 */