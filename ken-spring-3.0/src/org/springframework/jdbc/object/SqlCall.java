/*   1:    */ package org.springframework.jdbc.object;
/*   2:    */ 
/*   3:    */ import java.util.List;
/*   4:    */ import java.util.Map;
/*   5:    */ import javax.sql.DataSource;
/*   6:    */ import org.apache.commons.logging.Log;
/*   7:    */ import org.springframework.jdbc.core.CallableStatementCreator;
/*   8:    */ import org.springframework.jdbc.core.CallableStatementCreatorFactory;
/*   9:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*  10:    */ import org.springframework.jdbc.core.ParameterMapper;
/*  11:    */ import org.springframework.jdbc.core.SqlParameter;
/*  12:    */ 
/*  13:    */ public abstract class SqlCall
/*  14:    */   extends RdbmsOperation
/*  15:    */ {
/*  16:    */   private CallableStatementCreatorFactory callableStatementFactory;
/*  17: 51 */   private boolean function = false;
/*  18: 57 */   private boolean sqlReadyForUse = false;
/*  19:    */   private String callString;
/*  20:    */   
/*  21:    */   public SqlCall() {}
/*  22:    */   
/*  23:    */   public SqlCall(DataSource ds, String sql)
/*  24:    */   {
/*  25: 86 */     setDataSource(ds);
/*  26: 87 */     setSql(sql);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setFunction(boolean function)
/*  30:    */   {
/*  31: 95 */     this.function = function;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public boolean isFunction()
/*  35:    */   {
/*  36:102 */     return this.function;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setSqlReadyForUse(boolean sqlReadyForUse)
/*  40:    */   {
/*  41:109 */     this.sqlReadyForUse = sqlReadyForUse;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public boolean isSqlReadyForUse()
/*  45:    */   {
/*  46:116 */     return this.sqlReadyForUse;
/*  47:    */   }
/*  48:    */   
/*  49:    */   protected final void compileInternal()
/*  50:    */   {
/*  51:127 */     if (isSqlReadyForUse())
/*  52:    */     {
/*  53:128 */       this.callString = getSql();
/*  54:    */     }
/*  55:    */     else
/*  56:    */     {
/*  57:131 */       List<SqlParameter> parameters = getDeclaredParameters();
/*  58:132 */       int parameterCount = 0;
/*  59:133 */       if (isFunction())
/*  60:    */       {
/*  61:134 */         this.callString = ("{? = call " + getSql() + "(");
/*  62:135 */         parameterCount = -1;
/*  63:    */       }
/*  64:    */       else
/*  65:    */       {
/*  66:138 */         this.callString = ("{call " + getSql() + "(");
/*  67:    */       }
/*  68:140 */       for (SqlParameter parameter : parameters) {
/*  69:141 */         if (!parameter.isResultsParameter())
/*  70:    */         {
/*  71:142 */           if (parameterCount > 0) {
/*  72:143 */             this.callString += ", ";
/*  73:    */           }
/*  74:145 */           if (parameterCount >= 0) {
/*  75:146 */             this.callString += "?";
/*  76:    */           }
/*  77:148 */           parameterCount++;
/*  78:    */         }
/*  79:    */       }
/*  80:151 */       this.callString += ")}";
/*  81:    */     }
/*  82:153 */     if (this.logger.isDebugEnabled()) {
/*  83:154 */       this.logger.debug("Compiled stored procedure. Call string is [" + getCallString() + "]");
/*  84:    */     }
/*  85:157 */     this.callableStatementFactory = new CallableStatementCreatorFactory(getCallString(), getDeclaredParameters());
/*  86:158 */     this.callableStatementFactory.setResultSetType(getResultSetType());
/*  87:159 */     this.callableStatementFactory.setUpdatableResults(isUpdatableResults());
/*  88:160 */     this.callableStatementFactory.setNativeJdbcExtractor(getJdbcTemplate().getNativeJdbcExtractor());
/*  89:    */     
/*  90:162 */     onCompileInternal();
/*  91:    */   }
/*  92:    */   
/*  93:    */   protected void onCompileInternal() {}
/*  94:    */   
/*  95:    */   public String getCallString()
/*  96:    */   {
/*  97:176 */     return this.callString;
/*  98:    */   }
/*  99:    */   
/* 100:    */   protected CallableStatementCreator newCallableStatementCreator(Map<String, ?> inParams)
/* 101:    */   {
/* 102:185 */     return this.callableStatementFactory.newCallableStatementCreator(inParams);
/* 103:    */   }
/* 104:    */   
/* 105:    */   protected CallableStatementCreator newCallableStatementCreator(ParameterMapper inParamMapper)
/* 106:    */   {
/* 107:194 */     return this.callableStatementFactory.newCallableStatementCreator(inParamMapper);
/* 108:    */   }
/* 109:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.object.SqlCall
 * JD-Core Version:    0.7.0.1
 */