/*  1:   */ package org.springframework.jdbc.core;
/*  2:   */ 
/*  3:   */ public class SqlReturnResultSet
/*  4:   */   extends ResultSetSupportingSqlParameter
/*  5:   */ {
/*  6:   */   public SqlReturnResultSet(String name, ResultSetExtractor extractor)
/*  7:   */   {
/*  8:39 */     super(name, 0, extractor);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public SqlReturnResultSet(String name, RowCallbackHandler handler)
/* 12:   */   {
/* 13:48 */     super(name, 0, handler);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public SqlReturnResultSet(String name, RowMapper mapper)
/* 17:   */   {
/* 18:57 */     super(name, 0, mapper);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public boolean isResultsParameter()
/* 22:   */   {
/* 23:67 */     return true;
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.SqlReturnResultSet
 * JD-Core Version:    0.7.0.1
 */