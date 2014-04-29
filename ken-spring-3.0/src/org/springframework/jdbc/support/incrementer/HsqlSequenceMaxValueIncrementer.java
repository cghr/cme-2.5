/*  1:   */ package org.springframework.jdbc.support.incrementer;
/*  2:   */ 
/*  3:   */ import javax.sql.DataSource;
/*  4:   */ 
/*  5:   */ public class HsqlSequenceMaxValueIncrementer
/*  6:   */   extends AbstractSequenceMaxValueIncrementer
/*  7:   */ {
/*  8:   */   public HsqlSequenceMaxValueIncrementer() {}
/*  9:   */   
/* 10:   */   public HsqlSequenceMaxValueIncrementer(DataSource dataSource, String incrementerName)
/* 11:   */   {
/* 12:48 */     super(dataSource, incrementerName);
/* 13:   */   }
/* 14:   */   
/* 15:   */   protected String getSequenceQuery()
/* 16:   */   {
/* 17:54 */     return "call next value for " + getIncrementerName();
/* 18:   */   }
/* 19:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.incrementer.HsqlSequenceMaxValueIncrementer
 * JD-Core Version:    0.7.0.1
 */