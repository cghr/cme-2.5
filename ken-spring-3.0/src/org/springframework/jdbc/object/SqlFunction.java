/*   1:    */ package org.springframework.jdbc.object;
/*   2:    */ 
/*   3:    */ import java.sql.ResultSet;
/*   4:    */ import java.sql.SQLException;
/*   5:    */ import javax.sql.DataSource;
/*   6:    */ import org.springframework.dao.TypeMismatchDataAccessException;
/*   7:    */ import org.springframework.jdbc.core.SingleColumnRowMapper;
/*   8:    */ 
/*   9:    */ public class SqlFunction<T>
/*  10:    */   extends MappingSqlQuery<T>
/*  11:    */ {
/*  12: 52 */   private final SingleColumnRowMapper<T> rowMapper = new SingleColumnRowMapper();
/*  13:    */   
/*  14:    */   public SqlFunction()
/*  15:    */   {
/*  16: 64 */     setRowsExpected(1);
/*  17:    */   }
/*  18:    */   
/*  19:    */   public SqlFunction(DataSource ds, String sql)
/*  20:    */   {
/*  21: 74 */     setRowsExpected(1);
/*  22: 75 */     setDataSource(ds);
/*  23: 76 */     setSql(sql);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public SqlFunction(DataSource ds, String sql, int[] types)
/*  27:    */   {
/*  28: 88 */     setRowsExpected(1);
/*  29: 89 */     setDataSource(ds);
/*  30: 90 */     setSql(sql);
/*  31: 91 */     setTypes(types);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public SqlFunction(DataSource ds, String sql, int[] types, Class<T> resultType)
/*  35:    */   {
/*  36:105 */     setRowsExpected(1);
/*  37:106 */     setDataSource(ds);
/*  38:107 */     setSql(sql);
/*  39:108 */     setTypes(types);
/*  40:109 */     setResultType(resultType);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setResultType(Class<T> resultType)
/*  44:    */   {
/*  45:119 */     this.rowMapper.setRequiredType(resultType);
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected T mapRow(ResultSet rs, int rowNum)
/*  49:    */     throws SQLException
/*  50:    */   {
/*  51:130 */     return this.rowMapper.mapRow(rs, rowNum);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public int run()
/*  55:    */   {
/*  56:139 */     return run(new Object[0]);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public int run(int parameter)
/*  60:    */   {
/*  61:148 */     return run(new Object[] { Integer.valueOf(parameter) });
/*  62:    */   }
/*  63:    */   
/*  64:    */   public int run(Object... parameters)
/*  65:    */   {
/*  66:159 */     Object obj = super.findObject(parameters);
/*  67:160 */     if (!(obj instanceof Number)) {
/*  68:161 */       throw new TypeMismatchDataAccessException("Couldn't convert result object [" + obj + "] to int");
/*  69:    */     }
/*  70:163 */     return ((Number)obj).intValue();
/*  71:    */   }
/*  72:    */   
/*  73:    */   public Object runGeneric()
/*  74:    */   {
/*  75:172 */     return findObject(null);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public Object runGeneric(int parameter)
/*  79:    */   {
/*  80:181 */     return findObject(parameter);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public Object runGeneric(Object[] parameters)
/*  84:    */   {
/*  85:193 */     return findObject(parameters);
/*  86:    */   }
/*  87:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.object.SqlFunction
 * JD-Core Version:    0.7.0.1
 */