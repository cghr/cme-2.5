/*  1:   */ package org.springframework.jca.cci;
/*  2:   */ 
/*  3:   */ import javax.resource.ResourceException;
/*  4:   */ import org.springframework.dao.InvalidDataAccessResourceUsageException;
/*  5:   */ 
/*  6:   */ public class CciOperationNotSupportedException
/*  7:   */   extends InvalidDataAccessResourceUsageException
/*  8:   */ {
/*  9:   */   public CciOperationNotSupportedException(String msg, ResourceException ex)
/* 10:   */   {
/* 11:37 */     super(msg, ex);
/* 12:   */   }
/* 13:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.cci.CciOperationNotSupportedException
 * JD-Core Version:    0.7.0.1
 */