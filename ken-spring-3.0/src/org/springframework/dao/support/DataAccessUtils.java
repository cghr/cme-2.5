/*   1:    */ package org.springframework.dao.support;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import org.springframework.dao.DataAccessException;
/*   6:    */ import org.springframework.dao.EmptyResultDataAccessException;
/*   7:    */ import org.springframework.dao.IncorrectResultSizeDataAccessException;
/*   8:    */ import org.springframework.dao.TypeMismatchDataAccessException;
/*   9:    */ import org.springframework.util.Assert;
/*  10:    */ import org.springframework.util.CollectionUtils;
/*  11:    */ import org.springframework.util.NumberUtils;
/*  12:    */ 
/*  13:    */ public abstract class DataAccessUtils
/*  14:    */ {
/*  15:    */   public static <T> T singleResult(Collection<T> results)
/*  16:    */     throws IncorrectResultSizeDataAccessException
/*  17:    */   {
/*  18: 48 */     int size = results != null ? results.size() : 0;
/*  19: 49 */     if (size == 0) {
/*  20: 50 */       return null;
/*  21:    */     }
/*  22: 52 */     if (results.size() > 1) {
/*  23: 53 */       throw new IncorrectResultSizeDataAccessException(1, size);
/*  24:    */     }
/*  25: 55 */     return results.iterator().next();
/*  26:    */   }
/*  27:    */   
/*  28:    */   public static <T> T requiredSingleResult(Collection<T> results)
/*  29:    */     throws IncorrectResultSizeDataAccessException
/*  30:    */   {
/*  31: 69 */     int size = results != null ? results.size() : 0;
/*  32: 70 */     if (size == 0) {
/*  33: 71 */       throw new EmptyResultDataAccessException(1);
/*  34:    */     }
/*  35: 73 */     if (results.size() > 1) {
/*  36: 74 */       throw new IncorrectResultSizeDataAccessException(1, size);
/*  37:    */     }
/*  38: 76 */     return results.iterator().next();
/*  39:    */   }
/*  40:    */   
/*  41:    */   public static <T> T uniqueResult(Collection<T> results)
/*  42:    */     throws IncorrectResultSizeDataAccessException
/*  43:    */   {
/*  44: 90 */     int size = results != null ? results.size() : 0;
/*  45: 91 */     if (size == 0) {
/*  46: 92 */       return null;
/*  47:    */     }
/*  48: 94 */     if (!CollectionUtils.hasUniqueObject(results)) {
/*  49: 95 */       throw new IncorrectResultSizeDataAccessException(1, size);
/*  50:    */     }
/*  51: 97 */     return results.iterator().next();
/*  52:    */   }
/*  53:    */   
/*  54:    */   public static <T> T requiredUniqueResult(Collection<T> results)
/*  55:    */     throws IncorrectResultSizeDataAccessException
/*  56:    */   {
/*  57:112 */     int size = results != null ? results.size() : 0;
/*  58:113 */     if (size == 0) {
/*  59:114 */       throw new EmptyResultDataAccessException(1);
/*  60:    */     }
/*  61:116 */     if (!CollectionUtils.hasUniqueObject(results)) {
/*  62:117 */       throw new IncorrectResultSizeDataAccessException(1, size);
/*  63:    */     }
/*  64:119 */     return results.iterator().next();
/*  65:    */   }
/*  66:    */   
/*  67:    */   public static <T> T objectResult(Collection<?> results, Class<T> requiredType)
/*  68:    */     throws IncorrectResultSizeDataAccessException, TypeMismatchDataAccessException
/*  69:    */   {
/*  70:140 */     Object result = requiredUniqueResult(results);
/*  71:141 */     if ((requiredType != null) && (!requiredType.isInstance(result))) {
/*  72:142 */       if (String.class.equals(requiredType)) {
/*  73:143 */         result = result.toString();
/*  74:145 */       } else if ((Number.class.isAssignableFrom(requiredType)) && (Number.class.isInstance(result))) {
/*  75:    */         try
/*  76:    */         {
/*  77:147 */           result = NumberUtils.convertNumberToTargetClass((Number)result, requiredType);
/*  78:    */         }
/*  79:    */         catch (IllegalArgumentException ex)
/*  80:    */         {
/*  81:150 */           throw new TypeMismatchDataAccessException(ex.getMessage());
/*  82:    */         }
/*  83:    */       } else {
/*  84:154 */         throw new TypeMismatchDataAccessException(
/*  85:155 */           "Result object is of type [" + result.getClass().getName() + 
/*  86:156 */           "] and could not be converted to required type [" + requiredType.getName() + "]");
/*  87:    */       }
/*  88:    */     }
/*  89:159 */     return result;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public static int intResult(Collection results)
/*  93:    */     throws IncorrectResultSizeDataAccessException, TypeMismatchDataAccessException
/*  94:    */   {
/*  95:178 */     return ((Number)objectResult(results, Number.class)).intValue();
/*  96:    */   }
/*  97:    */   
/*  98:    */   public static long longResult(Collection results)
/*  99:    */     throws IncorrectResultSizeDataAccessException, TypeMismatchDataAccessException
/* 100:    */   {
/* 101:197 */     return ((Number)objectResult(results, Number.class)).longValue();
/* 102:    */   }
/* 103:    */   
/* 104:    */   public static RuntimeException translateIfNecessary(RuntimeException rawException, PersistenceExceptionTranslator pet)
/* 105:    */   {
/* 106:212 */     Assert.notNull(pet, "PersistenceExceptionTranslator must not be null");
/* 107:213 */     DataAccessException dex = pet.translateExceptionIfPossible(rawException);
/* 108:214 */     return dex != null ? dex : rawException;
/* 109:    */   }
/* 110:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.dao.support.DataAccessUtils
 * JD-Core Version:    0.7.0.1
 */