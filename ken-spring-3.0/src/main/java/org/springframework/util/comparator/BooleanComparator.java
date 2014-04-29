/*  1:   */ package org.springframework.util.comparator;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import java.util.Comparator;
/*  5:   */ 
/*  6:   */ public final class BooleanComparator
/*  7:   */   implements Comparator<Boolean>, Serializable
/*  8:   */ {
/*  9:34 */   public static final BooleanComparator TRUE_LOW = new BooleanComparator(true);
/* 10:40 */   public static final BooleanComparator TRUE_HIGH = new BooleanComparator(false);
/* 11:   */   private final boolean trueLow;
/* 12:   */   
/* 13:   */   public BooleanComparator(boolean trueLow)
/* 14:   */   {
/* 15:57 */     this.trueLow = trueLow;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public int compare(Boolean v1, Boolean v2)
/* 19:   */   {
/* 20:62 */     return (v1.booleanValue() ^ v2.booleanValue()) ? -1 : (v1.booleanValue() ^ this.trueLow) ? 1 : 0;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public boolean equals(Object obj)
/* 24:   */   {
/* 25:67 */     if (this == obj) {
/* 26:68 */       return true;
/* 27:   */     }
/* 28:70 */     if (!(obj instanceof BooleanComparator)) {
/* 29:71 */       return false;
/* 30:   */     }
/* 31:73 */     return this.trueLow == ((BooleanComparator)obj).trueLow;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public int hashCode()
/* 35:   */   {
/* 36:78 */     return (this.trueLow ? -1 : 1) * getClass().hashCode();
/* 37:   */   }
/* 38:   */   
/* 39:   */   public String toString()
/* 40:   */   {
/* 41:83 */     return "BooleanComparator: " + (this.trueLow ? "true low" : "true high");
/* 42:   */   }
/* 43:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.comparator.BooleanComparator
 * JD-Core Version:    0.7.0.1
 */