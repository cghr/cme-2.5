/*  1:   */ package org.springframework.dao;
/*  2:   */ 
/*  3:   */ public class DuplicateKeyException
/*  4:   */   extends DataIntegrityViolationException
/*  5:   */ {
/*  6:   */   public DuplicateKeyException(String msg)
/*  7:   */   {
/*  8:34 */     super(msg);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public DuplicateKeyException(String msg, Throwable cause)
/* 12:   */   {
/* 13:43 */     super(msg, cause);
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.dao.DuplicateKeyException
 * JD-Core Version:    0.7.0.1
 */