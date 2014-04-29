/*   1:    */ package org.springframework.jdbc.core;
/*   2:    */ 
/*   3:    */ public class SqlInOutParameter
/*   4:    */   extends SqlOutParameter
/*   5:    */ {
/*   6:    */   public SqlInOutParameter(String name, int sqlType)
/*   7:    */   {
/*   8: 39 */     super(name, sqlType);
/*   9:    */   }
/*  10:    */   
/*  11:    */   public SqlInOutParameter(String name, int sqlType, int scale)
/*  12:    */   {
/*  13: 50 */     super(name, sqlType, scale);
/*  14:    */   }
/*  15:    */   
/*  16:    */   public SqlInOutParameter(String name, int sqlType, String typeName)
/*  17:    */   {
/*  18: 60 */     super(name, sqlType, typeName);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public SqlInOutParameter(String name, int sqlType, String typeName, SqlReturnType sqlReturnType)
/*  22:    */   {
/*  23: 71 */     super(name, sqlType, typeName, sqlReturnType);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public SqlInOutParameter(String name, int sqlType, ResultSetExtractor rse)
/*  27:    */   {
/*  28: 81 */     super(name, sqlType, rse);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public SqlInOutParameter(String name, int sqlType, RowCallbackHandler rch)
/*  32:    */   {
/*  33: 91 */     super(name, sqlType, rch);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public SqlInOutParameter(String name, int sqlType, RowMapper rm)
/*  37:    */   {
/*  38:101 */     super(name, sqlType, rm);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public boolean isInputValueProvided()
/*  42:    */   {
/*  43:110 */     return true;
/*  44:    */   }
/*  45:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.SqlInOutParameter
 * JD-Core Version:    0.7.0.1
 */