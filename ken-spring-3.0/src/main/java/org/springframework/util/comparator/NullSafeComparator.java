/*   1:    */ package org.springframework.util.comparator;
/*   2:    */ 
/*   3:    */ import java.util.Comparator;
/*   4:    */ import org.springframework.util.Assert;
/*   5:    */ 
/*   6:    */ public class NullSafeComparator<T>
/*   7:    */   implements Comparator<T>
/*   8:    */ {
/*   9: 38 */   public static final NullSafeComparator NULLS_LOW = new NullSafeComparator(true);
/*  10: 44 */   public static final NullSafeComparator NULLS_HIGH = new NullSafeComparator(false);
/*  11:    */   private final Comparator<T> nonNullComparator;
/*  12:    */   private final boolean nullsLow;
/*  13:    */   
/*  14:    */   private NullSafeComparator(boolean nullsLow)
/*  15:    */   {
/*  16: 68 */     this.nonNullComparator = new ComparableComparator();
/*  17: 69 */     this.nullsLow = nullsLow;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public NullSafeComparator(Comparator<T> comparator, boolean nullsLow)
/*  21:    */   {
/*  22: 82 */     Assert.notNull(comparator, "The non-null comparator is required");
/*  23: 83 */     this.nonNullComparator = comparator;
/*  24: 84 */     this.nullsLow = nullsLow;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public int compare(T o1, T o2)
/*  28:    */   {
/*  29: 89 */     if (o1 == o2) {
/*  30: 90 */       return 0;
/*  31:    */     }
/*  32: 92 */     if (o1 == null) {
/*  33: 93 */       return this.nullsLow ? -1 : 1;
/*  34:    */     }
/*  35: 95 */     if (o2 == null) {
/*  36: 96 */       return this.nullsLow ? 1 : -1;
/*  37:    */     }
/*  38: 98 */     return this.nonNullComparator.compare(o1, o2);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public boolean equals(Object obj)
/*  42:    */   {
/*  43:103 */     if (this == obj) {
/*  44:104 */       return true;
/*  45:    */     }
/*  46:106 */     if (!(obj instanceof NullSafeComparator)) {
/*  47:107 */       return false;
/*  48:    */     }
/*  49:109 */     NullSafeComparator other = (NullSafeComparator)obj;
/*  50:110 */     return (this.nonNullComparator.equals(other.nonNullComparator)) && (this.nullsLow == other.nullsLow);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public int hashCode()
/*  54:    */   {
/*  55:115 */     return (this.nullsLow ? -1 : 1) * this.nonNullComparator.hashCode();
/*  56:    */   }
/*  57:    */   
/*  58:    */   public String toString()
/*  59:    */   {
/*  60:120 */     return 
/*  61:121 */       "NullSafeComparator: non-null comparator [" + this.nonNullComparator + "]; " + (this.nullsLow ? "nulls low" : "nulls high");
/*  62:    */   }
/*  63:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.comparator.NullSafeComparator
 * JD-Core Version:    0.7.0.1
 */