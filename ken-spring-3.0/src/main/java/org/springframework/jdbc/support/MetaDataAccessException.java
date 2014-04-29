/*  1:   */ package org.springframework.jdbc.support;
/*  2:   */ 
/*  3:   */ import org.springframework.core.NestedCheckedException;
/*  4:   */ 
/*  5:   */ public class MetaDataAccessException
/*  6:   */   extends NestedCheckedException
/*  7:   */ {
/*  8:   */   public MetaDataAccessException(String msg)
/*  9:   */   {
/* 10:38 */     super(msg);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public MetaDataAccessException(String msg, Throwable cause)
/* 14:   */   {
/* 15:47 */     super(msg, cause);
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.MetaDataAccessException
 * JD-Core Version:    0.7.0.1
 */