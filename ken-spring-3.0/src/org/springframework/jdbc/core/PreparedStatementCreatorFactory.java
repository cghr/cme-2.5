/*   1:    */ package org.springframework.jdbc.core;
/*   2:    */ 
/*   3:    */ import java.sql.Connection;
/*   4:    */ import java.sql.PreparedStatement;
/*   5:    */ import java.sql.SQLException;
/*   6:    */ import java.util.Arrays;
/*   7:    */ import java.util.Collection;
/*   8:    */ import java.util.Collections;
/*   9:    */ import java.util.HashSet;
/*  10:    */ import java.util.LinkedList;
/*  11:    */ import java.util.List;
/*  12:    */ import java.util.Set;
/*  13:    */ import org.springframework.dao.InvalidDataAccessApiUsageException;
/*  14:    */ import org.springframework.dao.InvalidDataAccessResourceUsageException;
/*  15:    */ import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
/*  16:    */ import org.springframework.util.Assert;
/*  17:    */ 
/*  18:    */ public class PreparedStatementCreatorFactory
/*  19:    */ {
/*  20:    */   private final String sql;
/*  21:    */   private final List<SqlParameter> declaredParameters;
/*  22: 54 */   private int resultSetType = 1003;
/*  23: 56 */   private boolean updatableResults = false;
/*  24: 58 */   private boolean returnGeneratedKeys = false;
/*  25: 60 */   private String[] generatedKeysColumnNames = null;
/*  26:    */   private NativeJdbcExtractor nativeJdbcExtractor;
/*  27:    */   
/*  28:    */   public PreparedStatementCreatorFactory(String sql)
/*  29:    */   {
/*  30: 70 */     this.sql = sql;
/*  31: 71 */     this.declaredParameters = new LinkedList();
/*  32:    */   }
/*  33:    */   
/*  34:    */   public PreparedStatementCreatorFactory(String sql, int[] types)
/*  35:    */   {
/*  36: 80 */     this.sql = sql;
/*  37: 81 */     this.declaredParameters = SqlParameter.sqlTypesToAnonymousParameterList(types);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public PreparedStatementCreatorFactory(String sql, List<SqlParameter> declaredParameters)
/*  41:    */   {
/*  42: 91 */     this.sql = sql;
/*  43: 92 */     this.declaredParameters = declaredParameters;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void addParameter(SqlParameter param)
/*  47:    */   {
/*  48:102 */     this.declaredParameters.add(param);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setResultSetType(int resultSetType)
/*  52:    */   {
/*  53:113 */     this.resultSetType = resultSetType;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setUpdatableResults(boolean updatableResults)
/*  57:    */   {
/*  58:120 */     this.updatableResults = updatableResults;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void setReturnGeneratedKeys(boolean returnGeneratedKeys)
/*  62:    */   {
/*  63:127 */     this.returnGeneratedKeys = returnGeneratedKeys;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void setGeneratedKeysColumnNames(String[] names)
/*  67:    */   {
/*  68:134 */     this.generatedKeysColumnNames = names;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void setNativeJdbcExtractor(NativeJdbcExtractor nativeJdbcExtractor)
/*  72:    */   {
/*  73:141 */     this.nativeJdbcExtractor = nativeJdbcExtractor;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public PreparedStatementSetter newPreparedStatementSetter(List params)
/*  77:    */   {
/*  78:150 */     return new PreparedStatementCreatorImpl(params != null ? params : Collections.emptyList());
/*  79:    */   }
/*  80:    */   
/*  81:    */   public PreparedStatementSetter newPreparedStatementSetter(Object[] params)
/*  82:    */   {
/*  83:158 */     return new PreparedStatementCreatorImpl(params != null ? Arrays.asList(params) : Collections.emptyList());
/*  84:    */   }
/*  85:    */   
/*  86:    */   public PreparedStatementCreator newPreparedStatementCreator(List<?> params)
/*  87:    */   {
/*  88:166 */     return new PreparedStatementCreatorImpl(params != null ? params : Collections.emptyList());
/*  89:    */   }
/*  90:    */   
/*  91:    */   public PreparedStatementCreator newPreparedStatementCreator(Object[] params)
/*  92:    */   {
/*  93:174 */     return new PreparedStatementCreatorImpl(params != null ? Arrays.asList(params) : Collections.emptyList());
/*  94:    */   }
/*  95:    */   
/*  96:    */   public PreparedStatementCreator newPreparedStatementCreator(String sqlToUse, Object[] params)
/*  97:    */   {
/*  98:184 */     return new PreparedStatementCreatorImpl(
/*  99:185 */       sqlToUse, params != null ? Arrays.asList(params) : Collections.emptyList());
/* 100:    */   }
/* 101:    */   
/* 102:    */   private class PreparedStatementCreatorImpl
/* 103:    */     implements PreparedStatementCreator, PreparedStatementSetter, SqlProvider, ParameterDisposer
/* 104:    */   {
/* 105:    */     private final String actualSql;
/* 106:    */     private final List parameters;
/* 107:    */     
/* 108:    */     public PreparedStatementCreatorImpl()
/* 109:    */     {
/* 110:200 */       this(PreparedStatementCreatorFactory.this.sql, parameters);
/* 111:    */     }
/* 112:    */     
/* 113:    */     public PreparedStatementCreatorImpl(String actualSql, List parameters)
/* 114:    */     {
/* 115:204 */       this.actualSql = actualSql;
/* 116:205 */       Assert.notNull(parameters, "Parameters List must not be null");
/* 117:206 */       this.parameters = parameters;
/* 118:207 */       if (this.parameters.size() != PreparedStatementCreatorFactory.this.declaredParameters.size())
/* 119:    */       {
/* 120:209 */         Set<String> names = new HashSet();
/* 121:210 */         for (int i = 0; i < parameters.size(); i++)
/* 122:    */         {
/* 123:211 */           Object param = parameters.get(i);
/* 124:212 */           if ((param instanceof SqlParameterValue)) {
/* 125:213 */             names.add(((SqlParameterValue)param).getName());
/* 126:    */           } else {
/* 127:216 */             names.add("Parameter #" + i);
/* 128:    */           }
/* 129:    */         }
/* 130:219 */         if (names.size() != PreparedStatementCreatorFactory.this.declaredParameters.size()) {
/* 131:220 */           throw new InvalidDataAccessApiUsageException(
/* 132:221 */             "SQL [" + PreparedStatementCreatorFactory.this.sql + "]: given " + names.size() + 
/* 133:222 */             " parameters but expected " + PreparedStatementCreatorFactory.this.declaredParameters.size());
/* 134:    */         }
/* 135:    */       }
/* 136:    */     }
/* 137:    */     
/* 138:    */     public PreparedStatement createPreparedStatement(Connection con)
/* 139:    */       throws SQLException
/* 140:    */     {
/* 141:228 */       PreparedStatement ps = null;
/* 142:229 */       if ((PreparedStatementCreatorFactory.this.generatedKeysColumnNames != null) || (PreparedStatementCreatorFactory.this.returnGeneratedKeys)) {
/* 143:    */         try
/* 144:    */         {
/* 145:231 */           if (PreparedStatementCreatorFactory.this.generatedKeysColumnNames != null) {
/* 146:232 */             ps = con.prepareStatement(this.actualSql, PreparedStatementCreatorFactory.this.generatedKeysColumnNames);
/* 147:    */           } else {
/* 148:235 */             ps = con.prepareStatement(this.actualSql, 1);
/* 149:    */           }
/* 150:    */         }
/* 151:    */         catch (AbstractMethodError ex)
/* 152:    */         {
/* 153:239 */           throw new InvalidDataAccessResourceUsageException(
/* 154:240 */             "The JDBC driver is not compliant to JDBC 3.0 and thus does not support retrieval of auto-generated keys", 
/* 155:241 */             ex);
/* 156:    */         }
/* 157:244 */       } else if ((PreparedStatementCreatorFactory.this.resultSetType == 1003) && (!PreparedStatementCreatorFactory.this.updatableResults)) {
/* 158:245 */         ps = con.prepareStatement(this.actualSql);
/* 159:    */       } else {
/* 160:248 */         ps = con.prepareStatement(this.actualSql, PreparedStatementCreatorFactory.this.resultSetType, 
/* 161:249 */           PreparedStatementCreatorFactory.this.updatableResults ? 1008 : 1007);
/* 162:    */       }
/* 163:251 */       setValues(ps);
/* 164:252 */       return ps;
/* 165:    */     }
/* 166:    */     
/* 167:    */     public void setValues(PreparedStatement ps)
/* 168:    */       throws SQLException
/* 169:    */     {
/* 170:257 */       PreparedStatement psToUse = ps;
/* 171:258 */       if (PreparedStatementCreatorFactory.this.nativeJdbcExtractor != null) {
/* 172:259 */         psToUse = PreparedStatementCreatorFactory.this.nativeJdbcExtractor.getNativePreparedStatement(ps);
/* 173:    */       }
/* 174:263 */       int sqlColIndx = 1;
/* 175:264 */       for (int i = 0; i < this.parameters.size(); i++)
/* 176:    */       {
/* 177:265 */         Object in = this.parameters.get(i);
/* 178:266 */         SqlParameter declaredParameter = null;
/* 179:269 */         if ((in instanceof SqlParameterValue))
/* 180:    */         {
/* 181:270 */           SqlParameterValue paramValue = (SqlParameterValue)in;
/* 182:271 */           in = paramValue.getValue();
/* 183:272 */           declaredParameter = paramValue;
/* 184:    */         }
/* 185:    */         else
/* 186:    */         {
/* 187:275 */           if (PreparedStatementCreatorFactory.this.declaredParameters.size() <= i) {
/* 188:276 */             throw new InvalidDataAccessApiUsageException(
/* 189:277 */               "SQL [" + PreparedStatementCreatorFactory.this.sql + "]: unable to access parameter number " + (i + 1) + 
/* 190:278 */               " given only " + PreparedStatementCreatorFactory.this.declaredParameters.size() + " parameters");
/* 191:    */           }
/* 192:281 */           declaredParameter = (SqlParameter)PreparedStatementCreatorFactory.this.declaredParameters.get(i);
/* 193:    */         }
/* 194:283 */         if (((in instanceof Collection)) && (declaredParameter.getSqlType() != 2003))
/* 195:    */         {
/* 196:284 */           Collection entries = (Collection)in;
/* 197:285 */           for (Object entry : entries) {
/* 198:286 */             if ((entry instanceof Object[]))
/* 199:    */             {
/* 200:287 */               Object[] valueArray = (Object[])entry;
/* 201:288 */               for (Object argValue : valueArray) {
/* 202:289 */                 StatementCreatorUtils.setParameterValue(psToUse, sqlColIndx++, declaredParameter, argValue);
/* 203:    */               }
/* 204:    */             }
/* 205:    */             else
/* 206:    */             {
/* 207:293 */               StatementCreatorUtils.setParameterValue(psToUse, sqlColIndx++, declaredParameter, entry);
/* 208:    */             }
/* 209:    */           }
/* 210:    */         }
/* 211:    */         else
/* 212:    */         {
/* 213:298 */           StatementCreatorUtils.setParameterValue(psToUse, sqlColIndx++, declaredParameter, in);
/* 214:    */         }
/* 215:    */       }
/* 216:    */     }
/* 217:    */     
/* 218:    */     public String getSql()
/* 219:    */     {
/* 220:304 */       return PreparedStatementCreatorFactory.this.sql;
/* 221:    */     }
/* 222:    */     
/* 223:    */     public void cleanupParameters()
/* 224:    */     {
/* 225:308 */       StatementCreatorUtils.cleanupParameters(this.parameters);
/* 226:    */     }
/* 227:    */     
/* 228:    */     public String toString()
/* 229:    */     {
/* 230:313 */       StringBuilder sb = new StringBuilder();
/* 231:314 */       sb.append("PreparedStatementCreatorFactory.PreparedStatementCreatorImpl: sql=[");
/* 232:315 */       sb.append(PreparedStatementCreatorFactory.this.sql).append("]; parameters=").append(this.parameters);
/* 233:316 */       return sb.toString();
/* 234:    */     }
/* 235:    */   }
/* 236:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.PreparedStatementCreatorFactory
 * JD-Core Version:    0.7.0.1
 */