/*   1:    */ package org.springframework.util.comparator;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.Comparator;
/*   5:    */ 
/*   6:    */ public class InvertibleComparator<T>
/*   7:    */   implements Comparator<T>, Serializable
/*   8:    */ {
/*   9:    */   private final Comparator<T> comparator;
/*  10: 35 */   private boolean ascending = true;
/*  11:    */   
/*  12:    */   public InvertibleComparator(Comparator<T> comparator)
/*  13:    */   {
/*  14: 44 */     this.comparator = comparator;
/*  15:    */   }
/*  16:    */   
/*  17:    */   public InvertibleComparator(Comparator<T> comparator, boolean ascending)
/*  18:    */   {
/*  19: 54 */     this.comparator = comparator;
/*  20: 55 */     setAscending(ascending);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public void setAscending(boolean ascending)
/*  24:    */   {
/*  25: 63 */     this.ascending = ascending;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public boolean isAscending()
/*  29:    */   {
/*  30: 70 */     return this.ascending;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void invertOrder()
/*  34:    */   {
/*  35: 78 */     this.ascending = (!this.ascending);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public int compare(T o1, T o2)
/*  39:    */   {
/*  40: 83 */     int result = this.comparator.compare(o1, o2);
/*  41: 84 */     if (result != 0)
/*  42:    */     {
/*  43: 86 */       if (!this.ascending) {
/*  44: 87 */         if (-2147483648 == result) {
/*  45: 88 */           result = 2147483647;
/*  46:    */         } else {
/*  47: 91 */           result *= -1;
/*  48:    */         }
/*  49:    */       }
/*  50: 94 */       return result;
/*  51:    */     }
/*  52: 96 */     return 0;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public boolean equals(Object obj)
/*  56:    */   {
/*  57:101 */     if (this == obj) {
/*  58:102 */       return true;
/*  59:    */     }
/*  60:104 */     if (!(obj instanceof InvertibleComparator)) {
/*  61:105 */       return false;
/*  62:    */     }
/*  63:107 */     InvertibleComparator other = (InvertibleComparator)obj;
/*  64:108 */     return (this.comparator.equals(other.comparator)) && (this.ascending == other.ascending);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public int hashCode()
/*  68:    */   {
/*  69:113 */     return this.comparator.hashCode();
/*  70:    */   }
/*  71:    */   
/*  72:    */   public String toString()
/*  73:    */   {
/*  74:118 */     return "InvertibleComparator: [" + this.comparator + "]; ascending=" + this.ascending;
/*  75:    */   }
/*  76:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.comparator.InvertibleComparator
 * JD-Core Version:    0.7.0.1
 */