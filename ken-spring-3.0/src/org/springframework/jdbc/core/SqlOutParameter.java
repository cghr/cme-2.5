/*   1:    */ package org.springframework.jdbc.core;
/*   2:    */ 
/*   3:    */ public class SqlOutParameter
/*   4:    */   extends ResultSetSupportingSqlParameter
/*   5:    */ {
/*   6:    */   private SqlReturnType sqlReturnType;
/*   7:    */   
/*   8:    */   public SqlOutParameter(String name, int sqlType)
/*   9:    */   {
/*  10: 44 */     super(name, sqlType);
/*  11:    */   }
/*  12:    */   
/*  13:    */   public SqlOutParameter(String name, int sqlType, int scale)
/*  14:    */   {
/*  15: 55 */     super(name, sqlType, scale);
/*  16:    */   }
/*  17:    */   
/*  18:    */   public SqlOutParameter(String name, int sqlType, String typeName)
/*  19:    */   {
/*  20: 65 */     super(name, sqlType, typeName);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public SqlOutParameter(String name, int sqlType, String typeName, SqlReturnType sqlReturnType)
/*  24:    */   {
/*  25: 76 */     super(name, sqlType, typeName);
/*  26: 77 */     this.sqlReturnType = sqlReturnType;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public SqlOutParameter(String name, int sqlType, ResultSetExtractor rse)
/*  30:    */   {
/*  31: 87 */     super(name, sqlType, rse);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public SqlOutParameter(String name, int sqlType, RowCallbackHandler rch)
/*  35:    */   {
/*  36: 97 */     super(name, sqlType, rch);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public SqlOutParameter(String name, int sqlType, RowMapper rm)
/*  40:    */   {
/*  41:107 */     super(name, sqlType, rm);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public SqlReturnType getSqlReturnType()
/*  45:    */   {
/*  46:115 */     return this.sqlReturnType;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public boolean isReturnTypeSupported()
/*  50:    */   {
/*  51:122 */     return this.sqlReturnType != null;
/*  52:    */   }
/*  53:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.SqlOutParameter
 * JD-Core Version:    0.7.0.1
 */