/*  1:   */ package org.springframework.util.comparator;
/*  2:   */ 
/*  3:   */ import java.util.Comparator;
/*  4:   */ 
/*  5:   */ public class ComparableComparator<T extends Comparable<T>>
/*  6:   */   implements Comparator<T>
/*  7:   */ {
/*  8:   */   public int compare(T o1, T o2)
/*  9:   */   {
/* 10:33 */     return o1.compareTo(o2);
/* 11:   */   }
/* 12:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.comparator.ComparableComparator
 * JD-Core Version:    0.7.0.1
 */