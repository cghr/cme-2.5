/*  1:   */ package org.springframework.dao;
/*  2:   */ 
/*  3:   */ public class EmptyResultDataAccessException
/*  4:   */   extends IncorrectResultSizeDataAccessException
/*  5:   */ {
/*  6:   */   public EmptyResultDataAccessException(int expectedSize)
/*  7:   */   {
/*  8:34 */     super(expectedSize, 0);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public EmptyResultDataAccessException(String msg, int expectedSize)
/* 12:   */   {
/* 13:43 */     super(msg, expectedSize, 0);
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.dao.EmptyResultDataAccessException
 * JD-Core Version:    0.7.0.1
 */