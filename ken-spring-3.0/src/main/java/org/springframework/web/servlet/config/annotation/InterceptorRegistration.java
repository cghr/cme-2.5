/*  1:   */ package org.springframework.web.servlet.config.annotation;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.Arrays;
/*  5:   */ import java.util.Collection;
/*  6:   */ import java.util.List;
/*  7:   */ import org.springframework.util.Assert;
/*  8:   */ import org.springframework.web.servlet.HandlerInterceptor;
/*  9:   */ import org.springframework.web.servlet.handler.MappedInterceptor;
/* 10:   */ 
/* 11:   */ public class InterceptorRegistration
/* 12:   */ {
/* 13:   */   private final HandlerInterceptor interceptor;
/* 14:39 */   private final List<String> pathPatterns = new ArrayList();
/* 15:   */   
/* 16:   */   public InterceptorRegistration(HandlerInterceptor interceptor)
/* 17:   */   {
/* 18:45 */     Assert.notNull(interceptor, "Interceptor is required");
/* 19:46 */     this.interceptor = interceptor;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void addPathPatterns(String... pathPatterns)
/* 23:   */   {
/* 24:54 */     this.pathPatterns.addAll((Collection)Arrays.asList(pathPatterns));
/* 25:   */   }
/* 26:   */   
/* 27:   */   protected Object getInterceptor()
/* 28:   */   {
/* 29:62 */     if (this.pathPatterns.isEmpty()) {
/* 30:63 */       return this.interceptor;
/* 31:   */     }
/* 32:65 */     return new MappedInterceptor((String[])this.pathPatterns.toArray(new String[this.pathPatterns.size()]), this.interceptor);
/* 33:   */   }
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.config.annotation.InterceptorRegistration
 * JD-Core Version:    0.7.0.1
 */