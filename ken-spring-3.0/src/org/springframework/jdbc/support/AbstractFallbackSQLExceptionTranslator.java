/*   1:    */ package org.springframework.jdbc.support;
/*   2:    */ 
/*   3:    */ import java.sql.SQLException;
/*   4:    */ import org.apache.commons.logging.Log;
/*   5:    */ import org.apache.commons.logging.LogFactory;
/*   6:    */ import org.springframework.dao.DataAccessException;
/*   7:    */ import org.springframework.jdbc.UncategorizedSQLException;
/*   8:    */ import org.springframework.util.Assert;
/*   9:    */ 
/*  10:    */ public abstract class AbstractFallbackSQLExceptionTranslator
/*  11:    */   implements SQLExceptionTranslator
/*  12:    */ {
/*  13: 38 */   protected final Log logger = LogFactory.getLog(getClass());
/*  14:    */   private SQLExceptionTranslator fallbackTranslator;
/*  15:    */   
/*  16:    */   public void setFallbackTranslator(SQLExceptionTranslator fallback)
/*  17:    */   {
/*  18: 48 */     this.fallbackTranslator = fallback;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public SQLExceptionTranslator getFallbackTranslator()
/*  22:    */   {
/*  23: 55 */     return this.fallbackTranslator;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public DataAccessException translate(String task, String sql, SQLException ex)
/*  27:    */   {
/*  28: 64 */     Assert.notNull(ex, "Cannot translate a null SQLException");
/*  29: 65 */     if (task == null) {
/*  30: 66 */       task = "";
/*  31:    */     }
/*  32: 68 */     if (sql == null) {
/*  33: 69 */       sql = "";
/*  34:    */     }
/*  35: 72 */     DataAccessException dex = doTranslate(task, sql, ex);
/*  36: 73 */     if (dex != null) {
/*  37: 75 */       return dex;
/*  38:    */     }
/*  39: 78 */     SQLExceptionTranslator fallback = getFallbackTranslator();
/*  40: 79 */     if (fallback != null) {
/*  41: 80 */       return fallback.translate(task, sql, ex);
/*  42:    */     }
/*  43: 83 */     return new UncategorizedSQLException(task, sql, ex);
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected abstract DataAccessException doTranslate(String paramString1, String paramString2, SQLException paramSQLException);
/*  47:    */   
/*  48:    */   protected String buildMessage(String task, String sql, SQLException ex)
/*  49:    */   {
/*  50:110 */     return task + "; SQL [" + sql + "]; " + ex.getMessage();
/*  51:    */   }
/*  52:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.AbstractFallbackSQLExceptionTranslator
 * JD-Core Version:    0.7.0.1
 */