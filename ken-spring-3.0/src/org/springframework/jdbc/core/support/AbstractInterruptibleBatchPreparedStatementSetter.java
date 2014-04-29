/*  1:   */ package org.springframework.jdbc.core.support;
/*  2:   */ 
/*  3:   */ import java.sql.PreparedStatement;
/*  4:   */ import java.sql.SQLException;
/*  5:   */ import org.springframework.jdbc.core.InterruptibleBatchPreparedStatementSetter;
/*  6:   */ 
/*  7:   */ public abstract class AbstractInterruptibleBatchPreparedStatementSetter
/*  8:   */   implements InterruptibleBatchPreparedStatementSetter
/*  9:   */ {
/* 10:   */   private boolean exhausted;
/* 11:   */   
/* 12:   */   public final void setValues(PreparedStatement ps, int i)
/* 13:   */     throws SQLException
/* 14:   */   {
/* 15:44 */     this.exhausted = (!setValuesIfAvailable(ps, i));
/* 16:   */   }
/* 17:   */   
/* 18:   */   public final boolean isBatchExhausted(int i)
/* 19:   */   {
/* 20:51 */     return this.exhausted;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public int getBatchSize()
/* 24:   */   {
/* 25:59 */     return 2147483647;
/* 26:   */   }
/* 27:   */   
/* 28:   */   protected abstract boolean setValuesIfAvailable(PreparedStatement paramPreparedStatement, int paramInt)
/* 29:   */     throws SQLException;
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.support.AbstractInterruptibleBatchPreparedStatementSetter
 * JD-Core Version:    0.7.0.1
 */