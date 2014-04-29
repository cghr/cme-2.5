/*  1:   */ package org.springframework.jca.cci;
/*  2:   */ 
/*  3:   */ import java.sql.SQLException;
/*  4:   */ import org.springframework.dao.InvalidDataAccessResourceUsageException;
/*  5:   */ 
/*  6:   */ public class InvalidResultSetAccessException
/*  7:   */   extends InvalidDataAccessResourceUsageException
/*  8:   */ {
/*  9:   */   public InvalidResultSetAccessException(String msg, SQLException ex)
/* 10:   */   {
/* 11:42 */     super(ex.getMessage(), ex);
/* 12:   */   }
/* 13:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.cci.InvalidResultSetAccessException
 * JD-Core Version:    0.7.0.1
 */