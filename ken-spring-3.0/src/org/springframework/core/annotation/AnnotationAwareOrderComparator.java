/*  1:   */ package org.springframework.core.annotation;
/*  2:   */ 
/*  3:   */ import org.springframework.core.OrderComparator;
/*  4:   */ import org.springframework.core.Ordered;
/*  5:   */ 
/*  6:   */ public class AnnotationAwareOrderComparator
/*  7:   */   extends OrderComparator
/*  8:   */ {
/*  9:   */   protected int getOrder(Object obj)
/* 10:   */   {
/* 11:38 */     if ((obj instanceof Ordered)) {
/* 12:39 */       return ((Ordered)obj).getOrder();
/* 13:   */     }
/* 14:41 */     if (obj != null)
/* 15:   */     {
/* 16:42 */       Order order = (Order)obj.getClass().getAnnotation(Order.class);
/* 17:43 */       if (order != null) {
/* 18:44 */         return order.value();
/* 19:   */       }
/* 20:   */     }
/* 21:47 */     return 2147483647;
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.annotation.AnnotationAwareOrderComparator
 * JD-Core Version:    0.7.0.1
 */