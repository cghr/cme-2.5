/*  1:   */ package org.springframework.core;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.Collection;
/*  5:   */ import java.util.Collections;
/*  6:   */ import java.util.Comparator;
/*  7:   */ import java.util.Iterator;
/*  8:   */ import java.util.List;
/*  9:   */ import org.springframework.util.Assert;
/* 10:   */ 
/* 11:   */ public class ExceptionDepthComparator
/* 12:   */   implements Comparator<Class<? extends Throwable>>
/* 13:   */ {
/* 14:   */   private final Class<? extends Throwable> targetException;
/* 15:   */   
/* 16:   */   public ExceptionDepthComparator(Throwable exception)
/* 17:   */   {
/* 18:44 */     Assert.notNull(exception, "Target exception must not be null");
/* 19:45 */     this.targetException = exception.getClass();
/* 20:   */   }
/* 21:   */   
/* 22:   */   public ExceptionDepthComparator(Class<? extends Throwable> exceptionType)
/* 23:   */   {
/* 24:53 */     Assert.notNull(exceptionType, "Target exception type must not be null");
/* 25:54 */     this.targetException = exceptionType;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public int compare(Class<? extends Throwable> o1, Class<? extends Throwable> o2)
/* 29:   */   {
/* 30:59 */     int depth1 = getDepth(o1, this.targetException, 0);
/* 31:60 */     int depth2 = getDepth(o2, this.targetException, 0);
/* 32:61 */     return depth1 - depth2;
/* 33:   */   }
/* 34:   */   
/* 35:   */   private int getDepth(Class declaredException, Class exceptionToMatch, int depth)
/* 36:   */   {
/* 37:65 */     if (declaredException.equals(exceptionToMatch)) {
/* 38:67 */       return depth;
/* 39:   */     }
/* 40:70 */     if (Throwable.class.equals(exceptionToMatch)) {
/* 41:71 */       return 2147483647;
/* 42:   */     }
/* 43:73 */     return getDepth(declaredException, exceptionToMatch.getSuperclass(), depth + 1);
/* 44:   */   }
/* 45:   */   
/* 46:   */   public static Class<? extends Throwable> findClosestMatch(Collection<Class<? extends Throwable>> exceptionTypes, Throwable targetException)
/* 47:   */   {
/* 48:86 */     Assert.notEmpty(exceptionTypes, "Exception types must not be empty");
/* 49:87 */     if (exceptionTypes.size() == 1) {
/* 50:88 */       return (Class)exceptionTypes.iterator().next();
/* 51:   */     }
/* 52:90 */     List<Class<? extends Throwable>> handledExceptions = 
/* 53:91 */       new ArrayList(exceptionTypes);
/* 54:92 */     Collections.sort(handledExceptions, new ExceptionDepthComparator(targetException));
/* 55:93 */     return (Class)handledExceptions.get(0);
/* 56:   */   }
/* 57:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.ExceptionDepthComparator
 * JD-Core Version:    0.7.0.1
 */