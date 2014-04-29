/*   1:    */ package org.springframework.jdbc.object;
/*   2:    */ 
/*   3:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*   4:    */ import org.springframework.jdbc.core.PreparedStatementCreator;
/*   5:    */ import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
/*   6:    */ import org.springframework.jdbc.core.PreparedStatementSetter;
/*   7:    */ import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
/*   8:    */ import org.springframework.jdbc.core.namedparam.ParsedSql;
/*   9:    */ 
/*  10:    */ public abstract class SqlOperation
/*  11:    */   extends RdbmsOperation
/*  12:    */ {
/*  13:    */   private PreparedStatementCreatorFactory preparedStatementFactory;
/*  14:    */   private ParsedSql cachedSql;
/*  15: 47 */   private final Object parsedSqlMonitor = new Object();
/*  16:    */   
/*  17:    */   protected final void compileInternal()
/*  18:    */   {
/*  19: 56 */     this.preparedStatementFactory = new PreparedStatementCreatorFactory(getSql(), getDeclaredParameters());
/*  20: 57 */     this.preparedStatementFactory.setResultSetType(getResultSetType());
/*  21: 58 */     this.preparedStatementFactory.setUpdatableResults(isUpdatableResults());
/*  22: 59 */     this.preparedStatementFactory.setReturnGeneratedKeys(isReturnGeneratedKeys());
/*  23: 60 */     if (getGeneratedKeysColumnNames() != null) {
/*  24: 61 */       this.preparedStatementFactory.setGeneratedKeysColumnNames(getGeneratedKeysColumnNames());
/*  25:    */     }
/*  26: 63 */     this.preparedStatementFactory.setNativeJdbcExtractor(getJdbcTemplate().getNativeJdbcExtractor());
/*  27:    */     
/*  28: 65 */     onCompileInternal();
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected void onCompileInternal() {}
/*  32:    */   
/*  33:    */   protected ParsedSql getParsedSql()
/*  34:    */   {
/*  35: 81 */     synchronized (this.parsedSqlMonitor)
/*  36:    */     {
/*  37: 82 */       if (this.cachedSql == null) {
/*  38: 83 */         this.cachedSql = NamedParameterUtils.parseSqlStatement(getSql());
/*  39:    */       }
/*  40: 85 */       return this.cachedSql;
/*  41:    */     }
/*  42:    */   }
/*  43:    */   
/*  44:    */   protected final PreparedStatementSetter newPreparedStatementSetter(Object[] params)
/*  45:    */   {
/*  46: 96 */     return this.preparedStatementFactory.newPreparedStatementSetter(params);
/*  47:    */   }
/*  48:    */   
/*  49:    */   protected final PreparedStatementCreator newPreparedStatementCreator(Object[] params)
/*  50:    */   {
/*  51:105 */     return this.preparedStatementFactory.newPreparedStatementCreator(params);
/*  52:    */   }
/*  53:    */   
/*  54:    */   protected final PreparedStatementCreator newPreparedStatementCreator(String sqlToUse, Object[] params)
/*  55:    */   {
/*  56:116 */     return this.preparedStatementFactory.newPreparedStatementCreator(sqlToUse, params);
/*  57:    */   }
/*  58:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.object.SqlOperation
 * JD-Core Version:    0.7.0.1
 */