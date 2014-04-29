/*   1:    */ package org.springframework.jdbc.core;
/*   2:    */ 
/*   3:    */ import com.sun.rowset.CachedRowSetImpl;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import java.sql.ResultSet;
/*   6:    */ import java.sql.SQLException;
/*   7:    */ import javax.sql.rowset.CachedRowSet;
/*   8:    */ import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
/*   9:    */ import org.springframework.jdbc.support.rowset.SqlRowSet;
/*  10:    */ import org.springframework.util.ReflectionUtils;
/*  11:    */ 
/*  12:    */ public class SqlRowSetResultSetExtractor
/*  13:    */   implements ResultSetExtractor<SqlRowSet>
/*  14:    */ {
/*  15: 48 */   private static Object rowSetFactory = null;
/*  16: 50 */   private static Method createCachedRowSet = null;
/*  17:    */   
/*  18:    */   static
/*  19:    */   {
/*  20: 53 */     ClassLoader cl = SqlRowSetResultSetExtractor.class.getClassLoader();
/*  21:    */     try
/*  22:    */     {
/*  23: 55 */       Class rowSetProviderClass = cl.loadClass("javax.sql.rowset.RowSetProvider");
/*  24: 56 */       Method newFactory = rowSetProviderClass.getMethod("newFactory", new Class[0]);
/*  25: 57 */       rowSetFactory = ReflectionUtils.invokeMethod(newFactory, null);
/*  26: 58 */       createCachedRowSet = rowSetFactory.getClass().getMethod("createCachedRowSet", new Class[0]);
/*  27:    */     }
/*  28:    */     catch (Exception localException) {}
/*  29:    */   }
/*  30:    */   
/*  31:    */   public SqlRowSet extractData(ResultSet rs)
/*  32:    */     throws SQLException
/*  33:    */   {
/*  34: 67 */     return createSqlRowSet(rs);
/*  35:    */   }
/*  36:    */   
/*  37:    */   protected SqlRowSet createSqlRowSet(ResultSet rs)
/*  38:    */     throws SQLException
/*  39:    */   {
/*  40: 83 */     CachedRowSet rowSet = newCachedRowSet();
/*  41: 84 */     rowSet.populate(rs);
/*  42: 85 */     return new ResultSetWrappingSqlRowSet(rowSet);
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected CachedRowSet newCachedRowSet()
/*  46:    */     throws SQLException
/*  47:    */   {
/*  48: 99 */     if (createCachedRowSet != null) {
/*  49:101 */       return (CachedRowSet)ReflectionUtils.invokeJdbcMethod(createCachedRowSet, rowSetFactory);
/*  50:    */     }
/*  51:104 */     return new CachedRowSetImpl();
/*  52:    */   }
/*  53:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.SqlRowSetResultSetExtractor
 * JD-Core Version:    0.7.0.1
 */