/*  1:   */ package org.springframework.jdbc.core;
/*  2:   */ 
/*  3:   */ public class SqlReturnUpdateCount
/*  4:   */   extends SqlParameter
/*  5:   */ {
/*  6:   */   public SqlReturnUpdateCount(String name)
/*  7:   */   {
/*  8:20 */     super(name, 4);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public boolean isInputValueProvided()
/* 12:   */   {
/* 13:31 */     return false;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public boolean isResultsParameter()
/* 17:   */   {
/* 18:41 */     return true;
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.SqlReturnUpdateCount
 * JD-Core Version:    0.7.0.1
 */