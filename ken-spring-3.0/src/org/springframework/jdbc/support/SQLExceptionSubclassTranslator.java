/*   1:    */ package org.springframework.jdbc.support;
/*   2:    */ 
/*   3:    */ import java.sql.SQLDataException;
/*   4:    */ import java.sql.SQLException;
/*   5:    */ import java.sql.SQLFeatureNotSupportedException;
/*   6:    */ import java.sql.SQLIntegrityConstraintViolationException;
/*   7:    */ import java.sql.SQLInvalidAuthorizationSpecException;
/*   8:    */ import java.sql.SQLNonTransientConnectionException;
/*   9:    */ import java.sql.SQLNonTransientException;
/*  10:    */ import java.sql.SQLRecoverableException;
/*  11:    */ import java.sql.SQLSyntaxErrorException;
/*  12:    */ import java.sql.SQLTimeoutException;
/*  13:    */ import java.sql.SQLTransactionRollbackException;
/*  14:    */ import java.sql.SQLTransientConnectionException;
/*  15:    */ import java.sql.SQLTransientException;
/*  16:    */ import org.springframework.dao.ConcurrencyFailureException;
/*  17:    */ import org.springframework.dao.DataAccessException;
/*  18:    */ import org.springframework.dao.DataAccessResourceFailureException;
/*  19:    */ import org.springframework.dao.DataIntegrityViolationException;
/*  20:    */ import org.springframework.dao.InvalidDataAccessApiUsageException;
/*  21:    */ import org.springframework.dao.PermissionDeniedDataAccessException;
/*  22:    */ import org.springframework.dao.RecoverableDataAccessException;
/*  23:    */ import org.springframework.dao.TransientDataAccessResourceException;
/*  24:    */ import org.springframework.jdbc.BadSqlGrammarException;
/*  25:    */ 
/*  26:    */ public class SQLExceptionSubclassTranslator
/*  27:    */   extends AbstractFallbackSQLExceptionTranslator
/*  28:    */ {
/*  29:    */   public SQLExceptionSubclassTranslator()
/*  30:    */   {
/*  31: 61 */     setFallbackTranslator(new SQLStateSQLExceptionTranslator());
/*  32:    */   }
/*  33:    */   
/*  34:    */   protected DataAccessException doTranslate(String task, String sql, SQLException ex)
/*  35:    */   {
/*  36: 66 */     if ((ex instanceof SQLTransientException))
/*  37:    */     {
/*  38: 67 */       if ((ex instanceof SQLTransactionRollbackException)) {
/*  39: 68 */         return new ConcurrencyFailureException(buildMessage(task, sql, ex), ex);
/*  40:    */       }
/*  41: 70 */       if ((ex instanceof SQLTransientConnectionException)) {
/*  42: 71 */         return new TransientDataAccessResourceException(buildMessage(task, sql, ex), ex);
/*  43:    */       }
/*  44: 73 */       if ((ex instanceof SQLTimeoutException)) {
/*  45: 74 */         return new TransientDataAccessResourceException(buildMessage(task, sql, ex), ex);
/*  46:    */       }
/*  47:    */     }
/*  48: 77 */     else if ((ex instanceof SQLNonTransientException))
/*  49:    */     {
/*  50: 78 */       if ((ex instanceof SQLDataException)) {
/*  51: 79 */         return new DataIntegrityViolationException(buildMessage(task, sql, ex), ex);
/*  52:    */       }
/*  53: 81 */       if ((ex instanceof SQLFeatureNotSupportedException)) {
/*  54: 82 */         return new InvalidDataAccessApiUsageException(buildMessage(task, sql, ex), ex);
/*  55:    */       }
/*  56: 84 */       if ((ex instanceof SQLIntegrityConstraintViolationException)) {
/*  57: 85 */         return new DataIntegrityViolationException(buildMessage(task, sql, ex), ex);
/*  58:    */       }
/*  59: 87 */       if ((ex instanceof SQLInvalidAuthorizationSpecException)) {
/*  60: 88 */         return new PermissionDeniedDataAccessException(buildMessage(task, sql, ex), ex);
/*  61:    */       }
/*  62: 90 */       if ((ex instanceof SQLNonTransientConnectionException)) {
/*  63: 91 */         return new DataAccessResourceFailureException(buildMessage(task, sql, ex), ex);
/*  64:    */       }
/*  65: 93 */       if ((ex instanceof SQLSyntaxErrorException)) {
/*  66: 94 */         return new BadSqlGrammarException(task, sql, ex);
/*  67:    */       }
/*  68:    */     }
/*  69: 97 */     else if ((ex instanceof SQLRecoverableException))
/*  70:    */     {
/*  71: 98 */       return new RecoverableDataAccessException(buildMessage(task, sql, ex), ex);
/*  72:    */     }
/*  73:102 */     return null;
/*  74:    */   }
/*  75:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.SQLExceptionSubclassTranslator
 * JD-Core Version:    0.7.0.1
 */