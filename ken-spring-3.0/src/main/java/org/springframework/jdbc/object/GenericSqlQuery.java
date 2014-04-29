/*  1:   */ package org.springframework.jdbc.object;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import org.springframework.dao.InvalidDataAccessResourceUsageException;
/*  5:   */ import org.springframework.jdbc.core.RowMapper;
/*  6:   */ import org.springframework.util.Assert;
/*  7:   */ 
/*  8:   */ public class GenericSqlQuery
/*  9:   */   extends SqlQuery
/* 10:   */ {
/* 11:   */   Class rowMapperClass;
/* 12:   */   RowMapper rowMapper;
/* 13:   */   
/* 14:   */   public void setRowMapperClass(Class rowMapperClass)
/* 15:   */     throws IllegalAccessException, InstantiationException
/* 16:   */   {
/* 17:33 */     this.rowMapperClass = rowMapperClass;
/* 18:34 */     if (!RowMapper.class.isAssignableFrom(rowMapperClass)) {
/* 19:35 */       throw new IllegalStateException("The specified class '" + 
/* 20:36 */         rowMapperClass.getName() + " is not a sub class of " + 
/* 21:37 */         "'org.springframework.jdbc.core.RowMapper'");
/* 22:   */     }
/* 23:   */   }
/* 24:   */   
/* 25:   */   public void afterPropertiesSet()
/* 26:   */   {
/* 27:41 */     super.afterPropertiesSet();
/* 28:42 */     Assert.notNull(this.rowMapperClass, "The 'rowMapperClass' property is required");
/* 29:   */   }
/* 30:   */   
/* 31:   */   protected RowMapper newRowMapper(Object[] parameters, Map context)
/* 32:   */   {
/* 33:   */     try
/* 34:   */     {
/* 35:47 */       return (RowMapper)this.rowMapperClass.newInstance();
/* 36:   */     }
/* 37:   */     catch (InstantiationException e)
/* 38:   */     {
/* 39:50 */       throw new InvalidDataAccessResourceUsageException("Unable to instantiate RowMapper", e);
/* 40:   */     }
/* 41:   */     catch (IllegalAccessException e)
/* 42:   */     {
/* 43:53 */       throw new InvalidDataAccessResourceUsageException("Unable to instantiate RowMapper", e);
/* 44:   */     }
/* 45:   */   }
/* 46:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.object.GenericSqlQuery
 * JD-Core Version:    0.7.0.1
 */