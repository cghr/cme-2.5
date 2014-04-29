/*  1:   */ package org.springframework.jdbc.support.incrementer;
/*  2:   */ 
/*  3:   */ import javax.sql.DataSource;
/*  4:   */ 
/*  5:   */ public class H2SequenceMaxValueIncrementer
/*  6:   */   extends AbstractSequenceMaxValueIncrementer
/*  7:   */ {
/*  8:   */   public H2SequenceMaxValueIncrementer() {}
/*  9:   */   
/* 10:   */   public H2SequenceMaxValueIncrementer(DataSource dataSource, String incrementerName)
/* 11:   */   {
/* 12:43 */     super(dataSource, incrementerName);
/* 13:   */   }
/* 14:   */   
/* 15:   */   protected String getSequenceQuery()
/* 16:   */   {
/* 17:49 */     return "select " + getIncrementerName() + ".nextval from dual";
/* 18:   */   }
/* 19:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.incrementer.H2SequenceMaxValueIncrementer
 * JD-Core Version:    0.7.0.1
 */