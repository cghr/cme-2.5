/*  1:   */ package org.springframework.jdbc;
/*  2:   */ 
/*  3:   */ import org.springframework.dao.DataRetrievalFailureException;
/*  4:   */ 
/*  5:   */ public class IncorrectResultSetColumnCountException
/*  6:   */   extends DataRetrievalFailureException
/*  7:   */ {
/*  8:   */   private int expectedCount;
/*  9:   */   private int actualCount;
/* 10:   */   
/* 11:   */   public IncorrectResultSetColumnCountException(int expectedCount, int actualCount)
/* 12:   */   {
/* 13:42 */     super("Incorrect column count: expected " + expectedCount + ", actual " + actualCount);
/* 14:43 */     this.expectedCount = expectedCount;
/* 15:44 */     this.actualCount = actualCount;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public IncorrectResultSetColumnCountException(String msg, int expectedCount, int actualCount)
/* 19:   */   {
/* 20:54 */     super(msg);
/* 21:55 */     this.expectedCount = expectedCount;
/* 22:56 */     this.actualCount = actualCount;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public int getExpectedCount()
/* 26:   */   {
/* 27:64 */     return this.expectedCount;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public int getActualCount()
/* 31:   */   {
/* 32:71 */     return this.actualCount;
/* 33:   */   }
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.IncorrectResultSetColumnCountException
 * JD-Core Version:    0.7.0.1
 */