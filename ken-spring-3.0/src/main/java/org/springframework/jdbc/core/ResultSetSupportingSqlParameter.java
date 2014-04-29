/*   1:    */ package org.springframework.jdbc.core;
/*   2:    */ 
/*   3:    */ public class ResultSetSupportingSqlParameter
/*   4:    */   extends SqlParameter
/*   5:    */ {
/*   6:    */   private ResultSetExtractor resultSetExtractor;
/*   7:    */   private RowCallbackHandler rowCallbackHandler;
/*   8:    */   private RowMapper rowMapper;
/*   9:    */   
/*  10:    */   public ResultSetSupportingSqlParameter(String name, int sqlType)
/*  11:    */   {
/*  12: 41 */     super(name, sqlType);
/*  13:    */   }
/*  14:    */   
/*  15:    */   public ResultSetSupportingSqlParameter(String name, int sqlType, int scale)
/*  16:    */   {
/*  17: 52 */     super(name, sqlType, scale);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public ResultSetSupportingSqlParameter(String name, int sqlType, String typeName)
/*  21:    */   {
/*  22: 62 */     super(name, sqlType, typeName);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public ResultSetSupportingSqlParameter(String name, int sqlType, ResultSetExtractor rse)
/*  26:    */   {
/*  27: 72 */     super(name, sqlType);
/*  28: 73 */     this.resultSetExtractor = rse;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public ResultSetSupportingSqlParameter(String name, int sqlType, RowCallbackHandler rch)
/*  32:    */   {
/*  33: 83 */     super(name, sqlType);
/*  34: 84 */     this.rowCallbackHandler = rch;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public ResultSetSupportingSqlParameter(String name, int sqlType, RowMapper rm)
/*  38:    */   {
/*  39: 94 */     super(name, sqlType);
/*  40: 95 */     this.rowMapper = rm;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public boolean isResultSetSupported()
/*  44:    */   {
/*  45:104 */     return (this.resultSetExtractor != null) || (this.rowCallbackHandler != null) || (this.rowMapper != null);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public ResultSetExtractor getResultSetExtractor()
/*  49:    */   {
/*  50:111 */     return this.resultSetExtractor;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public RowCallbackHandler getRowCallbackHandler()
/*  54:    */   {
/*  55:118 */     return this.rowCallbackHandler;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public RowMapper getRowMapper()
/*  59:    */   {
/*  60:125 */     return this.rowMapper;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public boolean isInputValueProvided()
/*  64:    */   {
/*  65:134 */     return false;
/*  66:    */   }
/*  67:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.ResultSetSupportingSqlParameter
 * JD-Core Version:    0.7.0.1
 */