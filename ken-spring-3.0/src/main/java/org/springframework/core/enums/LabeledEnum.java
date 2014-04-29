/*  1:   */ package org.springframework.core.enums;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import java.util.Comparator;
/*  5:   */ import org.springframework.util.comparator.CompoundComparator;
/*  6:   */ import org.springframework.util.comparator.NullSafeComparator;
/*  7:   */ 
/*  8:   */ @Deprecated
/*  9:   */ public abstract interface LabeledEnum
/* 10:   */   extends Comparable, Serializable
/* 11:   */ {
/* 12:69 */   public static final Comparator CODE_ORDER = new Comparator()
/* 13:   */   {
/* 14:   */     public int compare(Object o1, Object o2)
/* 15:   */     {
/* 16:71 */       Comparable c1 = ((LabeledEnum)o1).getCode();
/* 17:72 */       Comparable c2 = ((LabeledEnum)o2).getCode();
/* 18:73 */       return c1.compareTo(c2);
/* 19:   */     }
/* 20:   */   };
/* 21:80 */   public static final Comparator LABEL_ORDER = new Comparator()
/* 22:   */   {
/* 23:   */     public int compare(Object o1, Object o2)
/* 24:   */     {
/* 25:82 */       LabeledEnum e1 = (LabeledEnum)o1;
/* 26:83 */       LabeledEnum e2 = (LabeledEnum)o2;
/* 27:84 */       Comparator comp = new NullSafeComparator(String.CASE_INSENSITIVE_ORDER, true);
/* 28:85 */       return comp.compare(e1.getLabel(), e2.getLabel());
/* 29:   */     }
/* 30:   */   };
/* 31:94 */   public static final Comparator DEFAULT_ORDER = new CompoundComparator(new Comparator[] { LABEL_ORDER, CODE_ORDER });
/* 32:   */   
/* 33:   */   public abstract Class getType();
/* 34:   */   
/* 35:   */   public abstract Comparable getCode();
/* 36:   */   
/* 37:   */   public abstract String getLabel();
/* 38:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.enums.LabeledEnum
 * JD-Core Version:    0.7.0.1
 */