/*  1:   */ package org.springframework.jdbc.core;
/*  2:   */ 
/*  3:   */ import java.sql.PreparedStatement;
/*  4:   */ import java.sql.SQLException;
/*  5:   */ 
/*  6:   */ class ArgPreparedStatementSetter
/*  7:   */   implements PreparedStatementSetter, ParameterDisposer
/*  8:   */ {
/*  9:   */   private final Object[] args;
/* 10:   */   
/* 11:   */   public ArgPreparedStatementSetter(Object[] args)
/* 12:   */   {
/* 13:38 */     this.args = args;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public void setValues(PreparedStatement ps)
/* 17:   */     throws SQLException
/* 18:   */   {
/* 19:43 */     if (this.args != null) {
/* 20:44 */       for (int i = 0; i < this.args.length; i++)
/* 21:   */       {
/* 22:45 */         Object arg = this.args[i];
/* 23:46 */         doSetValue(ps, i + 1, arg);
/* 24:   */       }
/* 25:   */     }
/* 26:   */   }
/* 27:   */   
/* 28:   */   protected void doSetValue(PreparedStatement ps, int parameterPosition, Object argValue)
/* 29:   */     throws SQLException
/* 30:   */   {
/* 31:60 */     if ((argValue instanceof SqlParameterValue))
/* 32:   */     {
/* 33:61 */       SqlParameterValue paramValue = (SqlParameterValue)argValue;
/* 34:62 */       StatementCreatorUtils.setParameterValue(ps, parameterPosition, paramValue, paramValue.getValue());
/* 35:   */     }
/* 36:   */     else
/* 37:   */     {
/* 38:65 */       StatementCreatorUtils.setParameterValue(ps, parameterPosition, -2147483648, argValue);
/* 39:   */     }
/* 40:   */   }
/* 41:   */   
/* 42:   */   public void cleanupParameters()
/* 43:   */   {
/* 44:70 */     StatementCreatorUtils.cleanupParameters(this.args);
/* 45:   */   }
/* 46:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.ArgPreparedStatementSetter
 * JD-Core Version:    0.7.0.1
 */