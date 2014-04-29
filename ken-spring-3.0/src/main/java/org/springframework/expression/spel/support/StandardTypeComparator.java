/*  1:   */ package org.springframework.expression.spel.support;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.TypeComparator;
/*  4:   */ import org.springframework.expression.spel.SpelEvaluationException;
/*  5:   */ import org.springframework.expression.spel.SpelMessage;
/*  6:   */ 
/*  7:   */ public class StandardTypeComparator
/*  8:   */   implements TypeComparator
/*  9:   */ {
/* 10:   */   public int compare(Object left, Object right)
/* 11:   */     throws SpelEvaluationException
/* 12:   */   {
/* 13:35 */     if (left == null) {
/* 14:36 */       return right == null ? 0 : -1;
/* 15:   */     }
/* 16:37 */     if (right == null) {
/* 17:38 */       return 1;
/* 18:   */     }
/* 19:42 */     if (((left instanceof Number)) && ((right instanceof Number)))
/* 20:   */     {
/* 21:43 */       Number leftNumber = (Number)left;
/* 22:44 */       Number rightNumber = (Number)right;
/* 23:45 */       if (((leftNumber instanceof Double)) || ((rightNumber instanceof Double)))
/* 24:   */       {
/* 25:46 */         double d1 = leftNumber.doubleValue();
/* 26:47 */         double d2 = rightNumber.doubleValue();
/* 27:48 */         return Double.compare(d1, d2);
/* 28:   */       }
/* 29:49 */       if (((leftNumber instanceof Float)) || ((rightNumber instanceof Float)))
/* 30:   */       {
/* 31:50 */         float f1 = leftNumber.floatValue();
/* 32:51 */         float f2 = rightNumber.floatValue();
/* 33:52 */         return Float.compare(f1, f2);
/* 34:   */       }
/* 35:53 */       if (((leftNumber instanceof Long)) || ((rightNumber instanceof Long)))
/* 36:   */       {
/* 37:54 */         Long l1 = Long.valueOf(leftNumber.longValue());
/* 38:55 */         Long l2 = Long.valueOf(rightNumber.longValue());
/* 39:56 */         return l1.compareTo(l2);
/* 40:   */       }
/* 41:58 */       Integer i1 = Integer.valueOf(leftNumber.intValue());
/* 42:59 */       Integer i2 = Integer.valueOf(rightNumber.intValue());
/* 43:60 */       return i1.compareTo(i2);
/* 44:   */     }
/* 45:   */     try
/* 46:   */     {
/* 47:65 */       if ((left instanceof Comparable)) {
/* 48:66 */         return ((Comparable)left).compareTo(right);
/* 49:   */       }
/* 50:   */     }
/* 51:   */     catch (ClassCastException cce)
/* 52:   */     {
/* 53:69 */       throw new SpelEvaluationException(cce, SpelMessage.NOT_COMPARABLE, new Object[] { left.getClass(), right.getClass() });
/* 54:   */     }
/* 55:72 */     throw new SpelEvaluationException(SpelMessage.NOT_COMPARABLE, new Object[] { left.getClass(), right.getClass() });
/* 56:   */   }
/* 57:   */   
/* 58:   */   public boolean canCompare(Object left, Object right)
/* 59:   */   {
/* 60:76 */     if ((left == null) || (right == null)) {
/* 61:77 */       return true;
/* 62:   */     }
/* 63:79 */     if (((left instanceof Number)) && ((right instanceof Number))) {
/* 64:80 */       return true;
/* 65:   */     }
/* 66:82 */     if ((left instanceof Comparable)) {
/* 67:83 */       return true;
/* 68:   */     }
/* 69:85 */     return false;
/* 70:   */   }
/* 71:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.support.StandardTypeComparator
 * JD-Core Version:    0.7.0.1
 */