/*   1:    */ package org.springframework.web.servlet.mvc.method;
/*   2:    */ 
/*   3:    */ import javax.servlet.http.HttpServletRequest;
/*   4:    */ import javax.servlet.http.HttpServletResponse;
/*   5:    */ import org.springframework.core.Ordered;
/*   6:    */ import org.springframework.web.method.HandlerMethod;
/*   7:    */ import org.springframework.web.servlet.HandlerAdapter;
/*   8:    */ import org.springframework.web.servlet.ModelAndView;
/*   9:    */ import org.springframework.web.servlet.support.WebContentGenerator;
/*  10:    */ 
/*  11:    */ public abstract class AbstractHandlerMethodAdapter
/*  12:    */   extends WebContentGenerator
/*  13:    */   implements HandlerAdapter, Ordered
/*  14:    */ {
/*  15: 37 */   private int order = 2147483647;
/*  16:    */   
/*  17:    */   public AbstractHandlerMethodAdapter()
/*  18:    */   {
/*  19: 41 */     super(false);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public void setOrder(int order)
/*  23:    */   {
/*  24: 50 */     this.order = order;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public int getOrder()
/*  28:    */   {
/*  29: 54 */     return this.order;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public final boolean supports(Object handler)
/*  33:    */   {
/*  34: 64 */     return ((handler instanceof HandlerMethod)) && (supportsInternal((HandlerMethod)handler));
/*  35:    */   }
/*  36:    */   
/*  37:    */   protected abstract boolean supportsInternal(HandlerMethod paramHandlerMethod);
/*  38:    */   
/*  39:    */   public final ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
/*  40:    */     throws Exception
/*  41:    */   {
/*  42: 80 */     return handleInternal(request, response, (HandlerMethod)handler);
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected abstract ModelAndView handleInternal(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, HandlerMethod paramHandlerMethod)
/*  46:    */     throws Exception;
/*  47:    */   
/*  48:    */   public final long getLastModified(HttpServletRequest request, Object handler)
/*  49:    */   {
/*  50:102 */     return getLastModifiedInternal(request, (HandlerMethod)handler);
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected abstract long getLastModifiedInternal(HttpServletRequest paramHttpServletRequest, HandlerMethod paramHandlerMethod);
/*  54:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter
 * JD-Core Version:    0.7.0.1
 */