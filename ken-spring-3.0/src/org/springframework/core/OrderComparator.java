/*  1:   */ package org.springframework.core;
/*  2:   */ 
/*  3:   */ import java.util.Arrays;
/*  4:   */ import java.util.Collections;
/*  5:   */ import java.util.Comparator;
/*  6:   */ import java.util.List;
/*  7:   */ 
/*  8:   */ public class OrderComparator
/*  9:   */   implements Comparator<Object>
/* 10:   */ {
/* 11:43 */   public static OrderComparator INSTANCE = new OrderComparator();
/* 12:   */   
/* 13:   */   public int compare(Object o1, Object o2)
/* 14:   */   {
/* 15:47 */     boolean p1 = o1 instanceof PriorityOrdered;
/* 16:48 */     boolean p2 = o2 instanceof PriorityOrdered;
/* 17:49 */     if ((p1) && (!p2)) {
/* 18:50 */       return -1;
/* 19:   */     }
/* 20:52 */     if ((p2) && (!p1)) {
/* 21:53 */       return 1;
/* 22:   */     }
/* 23:57 */     int i1 = getOrder(o1);
/* 24:58 */     int i2 = getOrder(o2);
/* 25:59 */     return i1 > i2 ? 1 : i1 < i2 ? -1 : 0;
/* 26:   */   }
/* 27:   */   
/* 28:   */   protected int getOrder(Object obj)
/* 29:   */   {
/* 30:70 */     return (obj instanceof Ordered) ? ((Ordered)obj).getOrder() : 2147483647;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public static void sort(List<?> list)
/* 34:   */   {
/* 35:82 */     if (list.size() > 1) {
/* 36:83 */       Collections.sort(list, INSTANCE);
/* 37:   */     }
/* 38:   */   }
/* 39:   */   
/* 40:   */   public static void sort(Object[] array)
/* 41:   */   {
/* 42:95 */     if (array.length > 1) {
/* 43:96 */       Arrays.sort(array, INSTANCE);
/* 44:   */     }
/* 45:   */   }
/* 46:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.OrderComparator
 * JD-Core Version:    0.7.0.1
 */