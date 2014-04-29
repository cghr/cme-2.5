/*   1:    */ package org.springframework.jdbc.object;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Map;
/*   6:    */ import javax.sql.DataSource;
/*   7:    */ import org.springframework.dao.DataAccessException;
/*   8:    */ import org.springframework.dao.InvalidDataAccessApiUsageException;
/*   9:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*  10:    */ import org.springframework.jdbc.core.ParameterMapper;
/*  11:    */ import org.springframework.jdbc.core.SqlParameter;
/*  12:    */ 
/*  13:    */ public abstract class StoredProcedure
/*  14:    */   extends SqlCall
/*  15:    */ {
/*  16:    */   protected StoredProcedure() {}
/*  17:    */   
/*  18:    */   protected StoredProcedure(DataSource ds, String name)
/*  19:    */   {
/*  20: 59 */     setDataSource(ds);
/*  21: 60 */     setSql(name);
/*  22:    */   }
/*  23:    */   
/*  24:    */   protected StoredProcedure(JdbcTemplate jdbcTemplate, String name)
/*  25:    */   {
/*  26: 69 */     setJdbcTemplate(jdbcTemplate);
/*  27: 70 */     setSql(name);
/*  28:    */   }
/*  29:    */   
/*  30:    */   protected boolean allowsUnusedParameters()
/*  31:    */   {
/*  32: 80 */     return true;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void declareParameter(SqlParameter param)
/*  36:    */     throws InvalidDataAccessApiUsageException
/*  37:    */   {
/*  38: 96 */     if (param.getName() == null) {
/*  39: 97 */       throw new InvalidDataAccessApiUsageException("Parameters to stored procedures must have names as well as types");
/*  40:    */     }
/*  41: 99 */     super.declareParameter(param);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public Map<String, Object> execute(Object... inParams)
/*  45:    */   {
/*  46:115 */     Map<String, Object> paramsToUse = new HashMap();
/*  47:116 */     validateParameters(inParams);
/*  48:117 */     int i = 0;
/*  49:118 */     for (SqlParameter sqlParameter : getDeclaredParameters()) {
/*  50:119 */       if ((sqlParameter.isInputValueProvided()) && 
/*  51:120 */         (i < inParams.length)) {
/*  52:121 */         paramsToUse.put(sqlParameter.getName(), inParams[(i++)]);
/*  53:    */       }
/*  54:    */     }
/*  55:125 */     return getJdbcTemplate().call(newCallableStatementCreator(paramsToUse), getDeclaredParameters());
/*  56:    */   }
/*  57:    */   
/*  58:    */   public Map<String, Object> execute(Map<String, ?> inParams)
/*  59:    */     throws DataAccessException
/*  60:    */   {
/*  61:143 */     validateParameters(inParams.values().toArray());
/*  62:144 */     return getJdbcTemplate().call(newCallableStatementCreator(inParams), getDeclaredParameters());
/*  63:    */   }
/*  64:    */   
/*  65:    */   public Map<String, Object> execute(ParameterMapper inParamMapper)
/*  66:    */     throws DataAccessException
/*  67:    */   {
/*  68:164 */     checkCompiled();
/*  69:165 */     return getJdbcTemplate().call(newCallableStatementCreator(inParamMapper), getDeclaredParameters());
/*  70:    */   }
/*  71:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.object.StoredProcedure
 * JD-Core Version:    0.7.0.1
 */