/*   1:    */ package org.springframework.jdbc.core;
/*   2:    */ 
/*   3:    */ import java.sql.ResultSet;
/*   4:    */ import java.sql.ResultSetMetaData;
/*   5:    */ import java.sql.SQLException;
/*   6:    */ import org.springframework.dao.TypeMismatchDataAccessException;
/*   7:    */ import org.springframework.jdbc.IncorrectResultSetColumnCountException;
/*   8:    */ import org.springframework.jdbc.support.JdbcUtils;
/*   9:    */ import org.springframework.util.NumberUtils;
/*  10:    */ 
/*  11:    */ public class SingleColumnRowMapper<T>
/*  12:    */   implements RowMapper<T>
/*  13:    */ {
/*  14:    */   private Class<T> requiredType;
/*  15:    */   
/*  16:    */   public SingleColumnRowMapper() {}
/*  17:    */   
/*  18:    */   public SingleColumnRowMapper(Class<T> requiredType)
/*  19:    */   {
/*  20: 59 */     this.requiredType = requiredType;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public void setRequiredType(Class<T> requiredType)
/*  24:    */   {
/*  25: 68 */     this.requiredType = requiredType;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public T mapRow(ResultSet rs, int rowNum)
/*  29:    */     throws SQLException
/*  30:    */   {
/*  31: 84 */     ResultSetMetaData rsmd = rs.getMetaData();
/*  32: 85 */     int nrOfColumns = rsmd.getColumnCount();
/*  33: 86 */     if (nrOfColumns != 1) {
/*  34: 87 */       throw new IncorrectResultSetColumnCountException(1, nrOfColumns);
/*  35:    */     }
/*  36: 91 */     Object result = getColumnValue(rs, 1, this.requiredType);
/*  37: 92 */     if ((result != null) && (this.requiredType != null) && (!this.requiredType.isInstance(result))) {
/*  38:    */       try
/*  39:    */       {
/*  40: 95 */         return convertValueToRequiredType(result, this.requiredType);
/*  41:    */       }
/*  42:    */       catch (IllegalArgumentException ex)
/*  43:    */       {
/*  44: 98 */         throw new TypeMismatchDataAccessException(
/*  45: 99 */           "Type mismatch affecting row number " + rowNum + " and column type '" + 
/*  46:100 */           rsmd.getColumnTypeName(1) + "': " + ex.getMessage());
/*  47:    */       }
/*  48:    */     }
/*  49:103 */     return result;
/*  50:    */   }
/*  51:    */   
/*  52:    */   protected Object getColumnValue(ResultSet rs, int index, Class requiredType)
/*  53:    */     throws SQLException
/*  54:    */   {
/*  55:124 */     if (requiredType != null) {
/*  56:125 */       return JdbcUtils.getResultSetValue(rs, index, requiredType);
/*  57:    */     }
/*  58:129 */     return getColumnValue(rs, index);
/*  59:    */   }
/*  60:    */   
/*  61:    */   protected Object getColumnValue(ResultSet rs, int index)
/*  62:    */     throws SQLException
/*  63:    */   {
/*  64:148 */     return JdbcUtils.getResultSetValue(rs, index);
/*  65:    */   }
/*  66:    */   
/*  67:    */   protected Object convertValueToRequiredType(Object value, Class requiredType)
/*  68:    */   {
/*  69:167 */     if (String.class.equals(requiredType)) {
/*  70:168 */       return value.toString();
/*  71:    */     }
/*  72:170 */     if (Number.class.isAssignableFrom(requiredType))
/*  73:    */     {
/*  74:171 */       if ((value instanceof Number)) {
/*  75:173 */         return NumberUtils.convertNumberToTargetClass((Number)value, requiredType);
/*  76:    */       }
/*  77:177 */       return NumberUtils.parseNumber(value.toString(), requiredType);
/*  78:    */     }
/*  79:181 */     throw new IllegalArgumentException(
/*  80:182 */       "Value [" + value + "] is of type [" + value.getClass().getName() + 
/*  81:183 */       "] and cannot be converted to required type [" + requiredType.getName() + "]");
/*  82:    */   }
/*  83:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.SingleColumnRowMapper
 * JD-Core Version:    0.7.0.1
 */