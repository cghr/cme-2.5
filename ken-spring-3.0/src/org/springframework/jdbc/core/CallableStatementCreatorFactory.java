/*   1:    */ package org.springframework.jdbc.core;
/*   2:    */ 
/*   3:    */ import java.sql.CallableStatement;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.SQLException;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.LinkedList;
/*   8:    */ import java.util.List;
/*   9:    */ import java.util.Map;
/*  10:    */ import org.springframework.dao.InvalidDataAccessApiUsageException;
/*  11:    */ import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
/*  12:    */ 
/*  13:    */ public class CallableStatementCreatorFactory
/*  14:    */ {
/*  15:    */   private final String callString;
/*  16:    */   private final List<SqlParameter> declaredParameters;
/*  17: 48 */   private int resultSetType = 1003;
/*  18: 50 */   private boolean updatableResults = false;
/*  19:    */   private NativeJdbcExtractor nativeJdbcExtractor;
/*  20:    */   
/*  21:    */   public CallableStatementCreatorFactory(String callString)
/*  22:    */   {
/*  23: 60 */     this.callString = callString;
/*  24: 61 */     this.declaredParameters = new LinkedList();
/*  25:    */   }
/*  26:    */   
/*  27:    */   public CallableStatementCreatorFactory(String callString, List<SqlParameter> declaredParameters)
/*  28:    */   {
/*  29: 70 */     this.callString = callString;
/*  30: 71 */     this.declaredParameters = declaredParameters;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void addParameter(SqlParameter param)
/*  34:    */   {
/*  35: 81 */     this.declaredParameters.add(param);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setResultSetType(int resultSetType)
/*  39:    */   {
/*  40: 93 */     this.resultSetType = resultSetType;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setUpdatableResults(boolean updatableResults)
/*  44:    */   {
/*  45:100 */     this.updatableResults = updatableResults;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setNativeJdbcExtractor(NativeJdbcExtractor nativeJdbcExtractor)
/*  49:    */   {
/*  50:107 */     this.nativeJdbcExtractor = nativeJdbcExtractor;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public CallableStatementCreator newCallableStatementCreator(Map<String, ?> params)
/*  54:    */   {
/*  55:116 */     return new CallableStatementCreatorImpl(params != null ? params : new HashMap());
/*  56:    */   }
/*  57:    */   
/*  58:    */   public CallableStatementCreator newCallableStatementCreator(ParameterMapper inParamMapper)
/*  59:    */   {
/*  60:124 */     return new CallableStatementCreatorImpl(inParamMapper);
/*  61:    */   }
/*  62:    */   
/*  63:    */   private class CallableStatementCreatorImpl
/*  64:    */     implements CallableStatementCreator, SqlProvider, ParameterDisposer
/*  65:    */   {
/*  66:    */     private ParameterMapper inParameterMapper;
/*  67:    */     private Map<String, ?> inParameters;
/*  68:    */     
/*  69:    */     public CallableStatementCreatorImpl(ParameterMapper inParamMapper)
/*  70:    */     {
/*  71:142 */       this.inParameterMapper = inParamMapper;
/*  72:    */     }
/*  73:    */     
/*  74:    */     public CallableStatementCreatorImpl()
/*  75:    */     {
/*  76:150 */       this.inParameters = inParams;
/*  77:    */     }
/*  78:    */     
/*  79:    */     public CallableStatement createCallableStatement(Connection con)
/*  80:    */       throws SQLException
/*  81:    */     {
/*  82:155 */       if (this.inParameterMapper != null) {
/*  83:156 */         this.inParameters = this.inParameterMapper.createMap(con);
/*  84:159 */       } else if (this.inParameters == null) {
/*  85:160 */         throw new InvalidDataAccessApiUsageException(
/*  86:161 */           "A ParameterMapper or a Map of parameters must be provided");
/*  87:    */       }
/*  88:165 */       CallableStatement cs = null;
/*  89:166 */       if ((CallableStatementCreatorFactory.this.resultSetType == 1003) && (!CallableStatementCreatorFactory.this.updatableResults)) {
/*  90:167 */         cs = con.prepareCall(CallableStatementCreatorFactory.this.callString);
/*  91:    */       } else {
/*  92:170 */         cs = con.prepareCall(CallableStatementCreatorFactory.this.callString, CallableStatementCreatorFactory.this.resultSetType, 
/*  93:171 */           CallableStatementCreatorFactory.this.updatableResults ? 1008 : 1007);
/*  94:    */       }
/*  95:175 */       CallableStatement csToUse = cs;
/*  96:176 */       if (CallableStatementCreatorFactory.this.nativeJdbcExtractor != null) {
/*  97:177 */         csToUse = CallableStatementCreatorFactory.this.nativeJdbcExtractor.getNativeCallableStatement(cs);
/*  98:    */       }
/*  99:180 */       int sqlColIndx = 1;
/* 100:181 */       for (SqlParameter declaredParam : CallableStatementCreatorFactory.this.declaredParameters) {
/* 101:182 */         if (!declaredParam.isResultsParameter())
/* 102:    */         {
/* 103:185 */           Object inValue = this.inParameters.get(declaredParam.getName());
/* 104:186 */           if ((declaredParam instanceof ResultSetSupportingSqlParameter))
/* 105:    */           {
/* 106:189 */             if ((declaredParam instanceof SqlOutParameter))
/* 107:    */             {
/* 108:190 */               if (declaredParam.getTypeName() != null) {
/* 109:191 */                 cs.registerOutParameter(sqlColIndx, declaredParam.getSqlType(), declaredParam.getTypeName());
/* 110:194 */               } else if (declaredParam.getScale() != null) {
/* 111:195 */                 cs.registerOutParameter(sqlColIndx, declaredParam.getSqlType(), declaredParam.getScale().intValue());
/* 112:    */               } else {
/* 113:198 */                 cs.registerOutParameter(sqlColIndx, declaredParam.getSqlType());
/* 114:    */               }
/* 115:201 */               if (declaredParam.isInputValueProvided()) {
/* 116:202 */                 StatementCreatorUtils.setParameterValue(csToUse, sqlColIndx, declaredParam, inValue);
/* 117:    */               }
/* 118:    */             }
/* 119:    */           }
/* 120:    */           else
/* 121:    */           {
/* 122:208 */             if (!this.inParameters.containsKey(declaredParam.getName())) {
/* 123:209 */               throw new InvalidDataAccessApiUsageException(
/* 124:210 */                 "Required input parameter '" + declaredParam.getName() + "' is missing");
/* 125:    */             }
/* 126:212 */             StatementCreatorUtils.setParameterValue(csToUse, sqlColIndx, declaredParam, inValue);
/* 127:    */           }
/* 128:214 */           sqlColIndx++;
/* 129:    */         }
/* 130:    */       }
/* 131:218 */       return cs;
/* 132:    */     }
/* 133:    */     
/* 134:    */     public String getSql()
/* 135:    */     {
/* 136:222 */       return CallableStatementCreatorFactory.this.callString;
/* 137:    */     }
/* 138:    */     
/* 139:    */     public void cleanupParameters()
/* 140:    */     {
/* 141:226 */       if (this.inParameters != null) {
/* 142:227 */         StatementCreatorUtils.cleanupParameters(this.inParameters.values());
/* 143:    */       }
/* 144:    */     }
/* 145:    */     
/* 146:    */     public String toString()
/* 147:    */     {
/* 148:233 */       StringBuilder sb = new StringBuilder();
/* 149:234 */       sb.append("CallableStatementCreatorFactory.CallableStatementCreatorImpl: sql=[");
/* 150:235 */       sb.append(CallableStatementCreatorFactory.this.callString).append("]; parameters=").append(this.inParameters);
/* 151:236 */       return sb.toString();
/* 152:    */     }
/* 153:    */   }
/* 154:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.CallableStatementCreatorFactory
 * JD-Core Version:    0.7.0.1
 */