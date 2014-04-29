/*   1:    */ package org.springframework.jdbc.object;
/*   2:    */ 
/*   3:    */ import java.sql.ResultSet;
/*   4:    */ import java.sql.SQLException;
/*   5:    */ import java.util.Map;
/*   6:    */ import javax.sql.DataSource;
/*   7:    */ import org.springframework.jdbc.core.RowMapper;
/*   8:    */ 
/*   9:    */ public abstract class UpdatableSqlQuery<T>
/*  10:    */   extends SqlQuery<T>
/*  11:    */ {
/*  12:    */   public UpdatableSqlQuery()
/*  13:    */   {
/*  14: 44 */     setUpdatableResults(true);
/*  15:    */   }
/*  16:    */   
/*  17:    */   public UpdatableSqlQuery(DataSource ds, String sql)
/*  18:    */   {
/*  19: 53 */     super(ds, sql);
/*  20: 54 */     setUpdatableResults(true);
/*  21:    */   }
/*  22:    */   
/*  23:    */   protected RowMapper<T> newRowMapper(Object[] parameters, Map context)
/*  24:    */   {
/*  25: 64 */     return new RowMapperImpl(context);
/*  26:    */   }
/*  27:    */   
/*  28:    */   protected abstract T updateRow(ResultSet paramResultSet, int paramInt, Map paramMap)
/*  29:    */     throws SQLException;
/*  30:    */   
/*  31:    */   protected class RowMapperImpl
/*  32:    */     implements RowMapper<T>
/*  33:    */   {
/*  34:    */     private final Map context;
/*  35:    */     
/*  36:    */     public RowMapperImpl(Map context)
/*  37:    */     {
/*  38: 94 */       this.context = context;
/*  39:    */     }
/*  40:    */     
/*  41:    */     public T mapRow(ResultSet rs, int rowNum)
/*  42:    */       throws SQLException
/*  43:    */     {
/*  44: 98 */       T result = UpdatableSqlQuery.this.updateRow(rs, rowNum, this.context);
/*  45: 99 */       rs.updateRow();
/*  46:100 */       return result;
/*  47:    */     }
/*  48:    */   }
/*  49:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.object.UpdatableSqlQuery
 * JD-Core Version:    0.7.0.1
 */