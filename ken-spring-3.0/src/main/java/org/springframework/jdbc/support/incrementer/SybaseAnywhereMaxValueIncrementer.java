/*  1:   */ package org.springframework.jdbc.support.incrementer;
/*  2:   */ 
/*  3:   */ import javax.sql.DataSource;
/*  4:   */ 
/*  5:   */ public class SybaseAnywhereMaxValueIncrementer
/*  6:   */   extends SybaseMaxValueIncrementer
/*  7:   */ {
/*  8:   */   public SybaseAnywhereMaxValueIncrementer() {}
/*  9:   */   
/* 10:   */   public SybaseAnywhereMaxValueIncrementer(DataSource dataSource, String incrementerName, String columnName)
/* 11:   */   {
/* 12:57 */     super(dataSource, incrementerName, columnName);
/* 13:   */   }
/* 14:   */   
/* 15:   */   protected String getIncrementStatement()
/* 16:   */   {
/* 17:63 */     return "insert into " + getIncrementerName() + " values(DEFAULT)";
/* 18:   */   }
/* 19:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.incrementer.SybaseAnywhereMaxValueIncrementer
 * JD-Core Version:    0.7.0.1
 */