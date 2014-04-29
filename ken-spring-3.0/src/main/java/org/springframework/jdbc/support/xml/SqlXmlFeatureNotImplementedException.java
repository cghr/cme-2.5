/*  1:   */ package org.springframework.jdbc.support.xml;
/*  2:   */ 
/*  3:   */ import org.springframework.dao.InvalidDataAccessApiUsageException;
/*  4:   */ 
/*  5:   */ public class SqlXmlFeatureNotImplementedException
/*  6:   */   extends InvalidDataAccessApiUsageException
/*  7:   */ {
/*  8:   */   public SqlXmlFeatureNotImplementedException(String msg)
/*  9:   */   {
/* 10:35 */     super(msg);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public SqlXmlFeatureNotImplementedException(String msg, Throwable cause)
/* 14:   */   {
/* 15:44 */     super(msg, cause);
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.xml.SqlXmlFeatureNotImplementedException
 * JD-Core Version:    0.7.0.1
 */