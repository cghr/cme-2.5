/*   1:    */ package org.springframework.jdbc.object;
/*   2:    */ 
/*   3:    */ import java.sql.ResultSet;
/*   4:    */ import java.sql.SQLException;
/*   5:    */ import java.util.Map;
/*   6:    */ import javax.sql.DataSource;
/*   7:    */ import org.springframework.jdbc.core.RowMapper;
/*   8:    */ 
/*   9:    */ public abstract class MappingSqlQueryWithParameters<T>
/*  10:    */   extends SqlQuery<T>
/*  11:    */ {
/*  12:    */   public MappingSqlQueryWithParameters() {}
/*  13:    */   
/*  14:    */   public MappingSqlQueryWithParameters(DataSource ds, String sql)
/*  15:    */   {
/*  16: 64 */     super(ds, sql);
/*  17:    */   }
/*  18:    */   
/*  19:    */   protected RowMapper<T> newRowMapper(Object[] parameters, Map context)
/*  20:    */   {
/*  21: 74 */     return new RowMapperImpl(parameters, context);
/*  22:    */   }
/*  23:    */   
/*  24:    */   protected abstract T mapRow(ResultSet paramResultSet, int paramInt, Object[] paramArrayOfObject, Map paramMap)
/*  25:    */     throws SQLException;
/*  26:    */   
/*  27:    */   protected class RowMapperImpl
/*  28:    */     implements RowMapper<T>
/*  29:    */   {
/*  30:    */     private final Object[] params;
/*  31:    */     private final Map context;
/*  32:    */     
/*  33:    */     public RowMapperImpl(Object[] parameters, Map context)
/*  34:    */     {
/*  35:110 */       this.params = parameters;
/*  36:111 */       this.context = context;
/*  37:    */     }
/*  38:    */     
/*  39:    */     public T mapRow(ResultSet rs, int rowNum)
/*  40:    */       throws SQLException
/*  41:    */     {
/*  42:115 */       return MappingSqlQueryWithParameters.this.mapRow(rs, rowNum, this.params, this.context);
/*  43:    */     }
/*  44:    */   }
/*  45:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.object.MappingSqlQueryWithParameters
 * JD-Core Version:    0.7.0.1
 */