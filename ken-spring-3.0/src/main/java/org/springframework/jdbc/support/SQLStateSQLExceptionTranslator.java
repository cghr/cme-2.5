/*   1:    */ package org.springframework.jdbc.support;
/*   2:    */ 
/*   3:    */ import java.sql.SQLException;
/*   4:    */ import java.util.HashSet;
/*   5:    */ import java.util.Set;
/*   6:    */ import org.apache.commons.logging.Log;
/*   7:    */ import org.springframework.dao.ConcurrencyFailureException;
/*   8:    */ import org.springframework.dao.DataAccessException;
/*   9:    */ import org.springframework.dao.DataAccessResourceFailureException;
/*  10:    */ import org.springframework.dao.DataIntegrityViolationException;
/*  11:    */ import org.springframework.dao.TransientDataAccessResourceException;
/*  12:    */ import org.springframework.jdbc.BadSqlGrammarException;
/*  13:    */ 
/*  14:    */ public class SQLStateSQLExceptionTranslator
/*  15:    */   extends AbstractFallbackSQLExceptionTranslator
/*  16:    */ {
/*  17: 47 */   private static final Set<String> BAD_SQL_GRAMMAR_CODES = new HashSet(8);
/*  18: 49 */   private static final Set<String> DATA_INTEGRITY_VIOLATION_CODES = new HashSet(8);
/*  19: 51 */   private static final Set<String> DATA_ACCESS_RESOURCE_FAILURE_CODES = new HashSet(8);
/*  20: 53 */   private static final Set<String> TRANSIENT_DATA_ACCESS_RESOURCE_CODES = new HashSet(8);
/*  21: 55 */   private static final Set<String> CONCURRENCY_FAILURE_CODES = new HashSet(4);
/*  22:    */   
/*  23:    */   static
/*  24:    */   {
/*  25: 59 */     BAD_SQL_GRAMMAR_CODES.add("07");
/*  26: 60 */     BAD_SQL_GRAMMAR_CODES.add("21");
/*  27: 61 */     BAD_SQL_GRAMMAR_CODES.add("2A");
/*  28: 62 */     BAD_SQL_GRAMMAR_CODES.add("37");
/*  29: 63 */     BAD_SQL_GRAMMAR_CODES.add("42");
/*  30: 64 */     BAD_SQL_GRAMMAR_CODES.add("65");
/*  31: 65 */     BAD_SQL_GRAMMAR_CODES.add("S0");
/*  32:    */     
/*  33: 67 */     DATA_INTEGRITY_VIOLATION_CODES.add("01");
/*  34: 68 */     DATA_INTEGRITY_VIOLATION_CODES.add("02");
/*  35: 69 */     DATA_INTEGRITY_VIOLATION_CODES.add("22");
/*  36: 70 */     DATA_INTEGRITY_VIOLATION_CODES.add("23");
/*  37: 71 */     DATA_INTEGRITY_VIOLATION_CODES.add("27");
/*  38: 72 */     DATA_INTEGRITY_VIOLATION_CODES.add("44");
/*  39:    */     
/*  40: 74 */     DATA_ACCESS_RESOURCE_FAILURE_CODES.add("08");
/*  41: 75 */     DATA_ACCESS_RESOURCE_FAILURE_CODES.add("53");
/*  42: 76 */     DATA_ACCESS_RESOURCE_FAILURE_CODES.add("54");
/*  43: 77 */     DATA_ACCESS_RESOURCE_FAILURE_CODES.add("57");
/*  44: 78 */     DATA_ACCESS_RESOURCE_FAILURE_CODES.add("58");
/*  45:    */     
/*  46: 80 */     TRANSIENT_DATA_ACCESS_RESOURCE_CODES.add("JW");
/*  47: 81 */     TRANSIENT_DATA_ACCESS_RESOURCE_CODES.add("JZ");
/*  48: 82 */     TRANSIENT_DATA_ACCESS_RESOURCE_CODES.add("S1");
/*  49:    */     
/*  50: 84 */     CONCURRENCY_FAILURE_CODES.add("40");
/*  51: 85 */     CONCURRENCY_FAILURE_CODES.add("61");
/*  52:    */   }
/*  53:    */   
/*  54:    */   protected DataAccessException doTranslate(String task, String sql, SQLException ex)
/*  55:    */   {
/*  56: 91 */     String sqlState = getSqlState(ex);
/*  57: 92 */     if ((sqlState != null) && (sqlState.length() >= 2))
/*  58:    */     {
/*  59: 93 */       String classCode = sqlState.substring(0, 2);
/*  60: 94 */       if (this.logger.isDebugEnabled()) {
/*  61: 95 */         this.logger.debug("Extracted SQL state class '" + classCode + "' from value '" + sqlState + "'");
/*  62:    */       }
/*  63: 97 */       if (BAD_SQL_GRAMMAR_CODES.contains(classCode)) {
/*  64: 98 */         return new BadSqlGrammarException(task, sql, ex);
/*  65:    */       }
/*  66:100 */       if (DATA_INTEGRITY_VIOLATION_CODES.contains(classCode)) {
/*  67:101 */         return new DataIntegrityViolationException(buildMessage(task, sql, ex), ex);
/*  68:    */       }
/*  69:103 */       if (DATA_ACCESS_RESOURCE_FAILURE_CODES.contains(classCode)) {
/*  70:104 */         return new DataAccessResourceFailureException(buildMessage(task, sql, ex), ex);
/*  71:    */       }
/*  72:106 */       if (TRANSIENT_DATA_ACCESS_RESOURCE_CODES.contains(classCode)) {
/*  73:107 */         return new TransientDataAccessResourceException(buildMessage(task, sql, ex), ex);
/*  74:    */       }
/*  75:109 */       if (CONCURRENCY_FAILURE_CODES.contains(classCode)) {
/*  76:110 */         return new ConcurrencyFailureException(buildMessage(task, sql, ex), ex);
/*  77:    */       }
/*  78:    */     }
/*  79:113 */     return null;
/*  80:    */   }
/*  81:    */   
/*  82:    */   private String getSqlState(SQLException ex)
/*  83:    */   {
/*  84:125 */     String sqlState = ex.getSQLState();
/*  85:126 */     if (sqlState == null)
/*  86:    */     {
/*  87:127 */       SQLException nestedEx = ex.getNextException();
/*  88:128 */       if (nestedEx != null) {
/*  89:129 */         sqlState = nestedEx.getSQLState();
/*  90:    */       }
/*  91:    */     }
/*  92:132 */     return sqlState;
/*  93:    */   }
/*  94:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.SQLStateSQLExceptionTranslator
 * JD-Core Version:    0.7.0.1
 */