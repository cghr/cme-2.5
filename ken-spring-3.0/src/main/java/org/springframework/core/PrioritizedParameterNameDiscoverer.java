/*  1:   */ package org.springframework.core;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Constructor;
/*  4:   */ import java.lang.reflect.Method;
/*  5:   */ import java.util.LinkedList;
/*  6:   */ import java.util.List;
/*  7:   */ 
/*  8:   */ public class PrioritizedParameterNameDiscoverer
/*  9:   */   implements ParameterNameDiscoverer
/* 10:   */ {
/* 11:40 */   private final List<ParameterNameDiscoverer> parameterNameDiscoverers = new LinkedList();
/* 12:   */   
/* 13:   */   public void addDiscoverer(ParameterNameDiscoverer pnd)
/* 14:   */   {
/* 15:48 */     this.parameterNameDiscoverers.add(pnd);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public String[] getParameterNames(Method method)
/* 19:   */   {
/* 20:53 */     for (ParameterNameDiscoverer pnd : this.parameterNameDiscoverers)
/* 21:   */     {
/* 22:54 */       String[] result = pnd.getParameterNames(method);
/* 23:55 */       if (result != null) {
/* 24:56 */         return result;
/* 25:   */       }
/* 26:   */     }
/* 27:59 */     return null;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public String[] getParameterNames(Constructor ctor)
/* 31:   */   {
/* 32:63 */     for (ParameterNameDiscoverer pnd : this.parameterNameDiscoverers)
/* 33:   */     {
/* 34:64 */       String[] result = pnd.getParameterNames(ctor);
/* 35:65 */       if (result != null) {
/* 36:66 */         return result;
/* 37:   */       }
/* 38:   */     }
/* 39:69 */     return null;
/* 40:   */   }
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.PrioritizedParameterNameDiscoverer
 * JD-Core Version:    0.7.0.1
 */