/*   1:    */ package org.springframework.util.comparator;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Comparator;
/*   6:    */ import java.util.List;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ 
/*   9:    */ public class CompoundComparator<T>
/*  10:    */   implements Comparator<T>, Serializable
/*  11:    */ {
/*  12:    */   private final List<InvertibleComparator<T>> comparators;
/*  13:    */   
/*  14:    */   public CompoundComparator()
/*  15:    */   {
/*  16: 51 */     this.comparators = new ArrayList();
/*  17:    */   }
/*  18:    */   
/*  19:    */   public CompoundComparator(Comparator[] comparators)
/*  20:    */   {
/*  21: 62 */     this.comparators = new ArrayList(comparators.length);
/*  22: 63 */     for (Comparator<T> comparator : comparators) {
/*  23: 64 */       addComparator(comparator);
/*  24:    */     }
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void addComparator(Comparator<T> comparator)
/*  28:    */   {
/*  29: 77 */     if ((comparator instanceof InvertibleComparator)) {
/*  30: 78 */       this.comparators.add((InvertibleComparator)comparator);
/*  31:    */     } else {
/*  32: 81 */       this.comparators.add(new InvertibleComparator(comparator));
/*  33:    */     }
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void addComparator(Comparator<T> comparator, boolean ascending)
/*  37:    */   {
/*  38: 91 */     this.comparators.add(new InvertibleComparator(comparator, ascending));
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setComparator(int index, Comparator<T> comparator)
/*  42:    */   {
/*  43:103 */     if ((comparator instanceof InvertibleComparator)) {
/*  44:104 */       this.comparators.set(index, (InvertibleComparator)comparator);
/*  45:    */     } else {
/*  46:107 */       this.comparators.set(index, new InvertibleComparator(comparator));
/*  47:    */     }
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setComparator(int index, Comparator<T> comparator, boolean ascending)
/*  51:    */   {
/*  52:118 */     this.comparators.set(index, new InvertibleComparator(comparator, ascending));
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void invertOrder()
/*  56:    */   {
/*  57:126 */     for (InvertibleComparator comparator : this.comparators) {
/*  58:127 */       comparator.invertOrder();
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void invertOrder(int index)
/*  63:    */   {
/*  64:136 */     ((InvertibleComparator)this.comparators.get(index)).invertOrder();
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setAscendingOrder(int index)
/*  68:    */   {
/*  69:144 */     ((InvertibleComparator)this.comparators.get(index)).setAscending(true);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setDescendingOrder(int index)
/*  73:    */   {
/*  74:152 */     ((InvertibleComparator)this.comparators.get(index)).setAscending(false);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public int getComparatorCount()
/*  78:    */   {
/*  79:159 */     return this.comparators.size();
/*  80:    */   }
/*  81:    */   
/*  82:    */   public int compare(T o1, T o2)
/*  83:    */   {
/*  84:164 */     Assert.state(this.comparators.size() > 0, 
/*  85:165 */       "No sort definitions have been added to this CompoundComparator to compare");
/*  86:166 */     for (InvertibleComparator<T> comparator : this.comparators)
/*  87:    */     {
/*  88:167 */       int result = comparator.compare(o1, o2);
/*  89:168 */       if (result != 0) {
/*  90:169 */         return result;
/*  91:    */       }
/*  92:    */     }
/*  93:172 */     return 0;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public boolean equals(Object obj)
/*  97:    */   {
/*  98:177 */     if (this == obj) {
/*  99:178 */       return true;
/* 100:    */     }
/* 101:180 */     if (!(obj instanceof CompoundComparator)) {
/* 102:181 */       return false;
/* 103:    */     }
/* 104:183 */     CompoundComparator other = (CompoundComparator)obj;
/* 105:184 */     return this.comparators.equals(other.comparators);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public int hashCode()
/* 109:    */   {
/* 110:189 */     return this.comparators.hashCode();
/* 111:    */   }
/* 112:    */   
/* 113:    */   public String toString()
/* 114:    */   {
/* 115:194 */     return "CompoundComparator: " + this.comparators;
/* 116:    */   }
/* 117:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.comparator.CompoundComparator
 * JD-Core Version:    0.7.0.1
 */