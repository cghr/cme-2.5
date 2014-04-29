/*   1:    */ package org.springframework.jdbc.support;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Constructor;
/*   4:    */ import java.sql.BatchUpdateException;
/*   5:    */ import java.sql.SQLException;
/*   6:    */ import java.util.Arrays;
/*   7:    */ import javax.sql.DataSource;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.springframework.core.JdkVersion;
/*  10:    */ import org.springframework.dao.CannotAcquireLockException;
/*  11:    */ import org.springframework.dao.CannotSerializeTransactionException;
/*  12:    */ import org.springframework.dao.DataAccessException;
/*  13:    */ import org.springframework.dao.DataAccessResourceFailureException;
/*  14:    */ import org.springframework.dao.DataIntegrityViolationException;
/*  15:    */ import org.springframework.dao.DeadlockLoserDataAccessException;
/*  16:    */ import org.springframework.dao.DuplicateKeyException;
/*  17:    */ import org.springframework.dao.PermissionDeniedDataAccessException;
/*  18:    */ import org.springframework.dao.TransientDataAccessResourceException;
/*  19:    */ import org.springframework.jdbc.BadSqlGrammarException;
/*  20:    */ import org.springframework.jdbc.InvalidResultSetAccessException;
/*  21:    */ 
/*  22:    */ public class SQLErrorCodeSQLExceptionTranslator
/*  23:    */   extends AbstractFallbackSQLExceptionTranslator
/*  24:    */ {
/*  25:    */   private static final int MESSAGE_ONLY_CONSTRUCTOR = 1;
/*  26:    */   private static final int MESSAGE_THROWABLE_CONSTRUCTOR = 2;
/*  27:    */   private static final int MESSAGE_SQLEX_CONSTRUCTOR = 3;
/*  28:    */   private static final int MESSAGE_SQL_THROWABLE_CONSTRUCTOR = 4;
/*  29:    */   private static final int MESSAGE_SQL_SQLEX_CONSTRUCTOR = 5;
/*  30:    */   private SQLErrorCodes sqlErrorCodes;
/*  31:    */   
/*  32:    */   public SQLErrorCodeSQLExceptionTranslator()
/*  33:    */   {
/*  34: 86 */     if (JdkVersion.getMajorJavaVersion() >= 3) {
/*  35: 87 */       setFallbackTranslator(new SQLExceptionSubclassTranslator());
/*  36:    */     } else {
/*  37: 90 */       setFallbackTranslator(new SQLStateSQLExceptionTranslator());
/*  38:    */     }
/*  39:    */   }
/*  40:    */   
/*  41:    */   public SQLErrorCodeSQLExceptionTranslator(DataSource dataSource)
/*  42:    */   {
/*  43:103 */     this();
/*  44:104 */     setDataSource(dataSource);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public SQLErrorCodeSQLExceptionTranslator(String dbName)
/*  48:    */   {
/*  49:116 */     this();
/*  50:117 */     setDatabaseProductName(dbName);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public SQLErrorCodeSQLExceptionTranslator(SQLErrorCodes sec)
/*  54:    */   {
/*  55:126 */     this();
/*  56:127 */     this.sqlErrorCodes = sec;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void setDataSource(DataSource dataSource)
/*  60:    */   {
/*  61:141 */     this.sqlErrorCodes = SQLErrorCodesFactory.getInstance().getErrorCodes(dataSource);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void setDatabaseProductName(String dbName)
/*  65:    */   {
/*  66:153 */     this.sqlErrorCodes = SQLErrorCodesFactory.getInstance().getErrorCodes(dbName);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void setSqlErrorCodes(SQLErrorCodes sec)
/*  70:    */   {
/*  71:161 */     this.sqlErrorCodes = sec;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public SQLErrorCodes getSqlErrorCodes()
/*  75:    */   {
/*  76:170 */     return this.sqlErrorCodes;
/*  77:    */   }
/*  78:    */   
/*  79:    */   protected DataAccessException doTranslate(String task, String sql, SQLException ex)
/*  80:    */   {
/*  81:176 */     SQLException sqlEx = ex;
/*  82:177 */     if (((sqlEx instanceof BatchUpdateException)) && (sqlEx.getNextException() != null))
/*  83:    */     {
/*  84:178 */       SQLException nestedSqlEx = sqlEx.getNextException();
/*  85:179 */       if ((nestedSqlEx.getErrorCode() > 0) || (nestedSqlEx.getSQLState() != null))
/*  86:    */       {
/*  87:180 */         this.logger.debug("Using nested SQLException from the BatchUpdateException");
/*  88:181 */         sqlEx = nestedSqlEx;
/*  89:    */       }
/*  90:    */     }
/*  91:186 */     DataAccessException dex = customTranslate(task, sql, sqlEx);
/*  92:187 */     if (dex != null) {
/*  93:188 */       return dex;
/*  94:    */     }
/*  95:192 */     if (this.sqlErrorCodes != null)
/*  96:    */     {
/*  97:193 */       SQLExceptionTranslator customTranslator = this.sqlErrorCodes.getCustomSqlExceptionTranslator();
/*  98:194 */       if (customTranslator != null)
/*  99:    */       {
/* 100:195 */         DataAccessException customDex = customTranslator.translate(task, sql, sqlEx);
/* 101:196 */         if (customDex != null) {
/* 102:197 */           return customDex;
/* 103:    */         }
/* 104:    */       }
/* 105:    */     }
/* 106:203 */     if (this.sqlErrorCodes != null)
/* 107:    */     {
/* 108:204 */       String errorCode = null;
/* 109:205 */       if (this.sqlErrorCodes.isUseSqlStateForTranslation()) {
/* 110:206 */         errorCode = sqlEx.getSQLState();
/* 111:    */       } else {
/* 112:209 */         errorCode = Integer.toString(sqlEx.getErrorCode());
/* 113:    */       }
/* 114:212 */       if (errorCode != null)
/* 115:    */       {
/* 116:214 */         CustomSQLErrorCodesTranslation[] customTranslations = this.sqlErrorCodes.getCustomTranslations();
/* 117:215 */         if (customTranslations != null) {
/* 118:216 */           for (int i = 0; i < customTranslations.length; i++)
/* 119:    */           {
/* 120:217 */             CustomSQLErrorCodesTranslation customTranslation = customTranslations[i];
/* 121:218 */             if ((Arrays.binarySearch(customTranslation.getErrorCodes(), errorCode) >= 0) && 
/* 122:219 */               (customTranslation.getExceptionClass() != null))
/* 123:    */             {
/* 124:220 */               DataAccessException customException = createCustomException(
/* 125:221 */                 task, sql, sqlEx, customTranslation.getExceptionClass());
/* 126:222 */               if (customException != null)
/* 127:    */               {
/* 128:223 */                 logTranslation(task, sql, sqlEx, true);
/* 129:224 */                 return customException;
/* 130:    */               }
/* 131:    */             }
/* 132:    */           }
/* 133:    */         }
/* 134:231 */         if (Arrays.binarySearch(this.sqlErrorCodes.getBadSqlGrammarCodes(), errorCode) >= 0)
/* 135:    */         {
/* 136:232 */           logTranslation(task, sql, sqlEx, false);
/* 137:233 */           return new BadSqlGrammarException(task, sql, sqlEx);
/* 138:    */         }
/* 139:235 */         if (Arrays.binarySearch(this.sqlErrorCodes.getInvalidResultSetAccessCodes(), errorCode) >= 0)
/* 140:    */         {
/* 141:236 */           logTranslation(task, sql, sqlEx, false);
/* 142:237 */           return new InvalidResultSetAccessException(task, sql, sqlEx);
/* 143:    */         }
/* 144:239 */         if (Arrays.binarySearch(this.sqlErrorCodes.getDuplicateKeyCodes(), errorCode) >= 0)
/* 145:    */         {
/* 146:240 */           logTranslation(task, sql, sqlEx, false);
/* 147:241 */           return new DuplicateKeyException(buildMessage(task, sql, sqlEx), sqlEx);
/* 148:    */         }
/* 149:243 */         if (Arrays.binarySearch(this.sqlErrorCodes.getDataIntegrityViolationCodes(), errorCode) >= 0)
/* 150:    */         {
/* 151:244 */           logTranslation(task, sql, sqlEx, false);
/* 152:245 */           return new DataIntegrityViolationException(buildMessage(task, sql, sqlEx), sqlEx);
/* 153:    */         }
/* 154:247 */         if (Arrays.binarySearch(this.sqlErrorCodes.getPermissionDeniedCodes(), errorCode) >= 0)
/* 155:    */         {
/* 156:248 */           logTranslation(task, sql, sqlEx, false);
/* 157:249 */           return new PermissionDeniedDataAccessException(buildMessage(task, sql, sqlEx), sqlEx);
/* 158:    */         }
/* 159:251 */         if (Arrays.binarySearch(this.sqlErrorCodes.getDataAccessResourceFailureCodes(), errorCode) >= 0)
/* 160:    */         {
/* 161:252 */           logTranslation(task, sql, sqlEx, false);
/* 162:253 */           return new DataAccessResourceFailureException(buildMessage(task, sql, sqlEx), sqlEx);
/* 163:    */         }
/* 164:255 */         if (Arrays.binarySearch(this.sqlErrorCodes.getTransientDataAccessResourceCodes(), errorCode) >= 0)
/* 165:    */         {
/* 166:256 */           logTranslation(task, sql, sqlEx, false);
/* 167:257 */           return new TransientDataAccessResourceException(buildMessage(task, sql, sqlEx), sqlEx);
/* 168:    */         }
/* 169:259 */         if (Arrays.binarySearch(this.sqlErrorCodes.getCannotAcquireLockCodes(), errorCode) >= 0)
/* 170:    */         {
/* 171:260 */           logTranslation(task, sql, sqlEx, false);
/* 172:261 */           return new CannotAcquireLockException(buildMessage(task, sql, sqlEx), sqlEx);
/* 173:    */         }
/* 174:263 */         if (Arrays.binarySearch(this.sqlErrorCodes.getDeadlockLoserCodes(), errorCode) >= 0)
/* 175:    */         {
/* 176:264 */           logTranslation(task, sql, sqlEx, false);
/* 177:265 */           return new DeadlockLoserDataAccessException(buildMessage(task, sql, sqlEx), sqlEx);
/* 178:    */         }
/* 179:267 */         if (Arrays.binarySearch(this.sqlErrorCodes.getCannotSerializeTransactionCodes(), errorCode) >= 0)
/* 180:    */         {
/* 181:268 */           logTranslation(task, sql, sqlEx, false);
/* 182:269 */           return new CannotSerializeTransactionException(buildMessage(task, sql, sqlEx), sqlEx);
/* 183:    */         }
/* 184:    */       }
/* 185:    */     }
/* 186:275 */     if (this.logger.isDebugEnabled())
/* 187:    */     {
/* 188:276 */       String codes = null;
/* 189:277 */       if ((this.sqlErrorCodes != null) && (this.sqlErrorCodes.isUseSqlStateForTranslation())) {
/* 190:278 */         codes = "SQL state '" + sqlEx.getSQLState() + "', error code '" + sqlEx.getErrorCode();
/* 191:    */       } else {
/* 192:281 */         codes = "Error code '" + sqlEx.getErrorCode() + "'";
/* 193:    */       }
/* 194:283 */       this.logger.debug("Unable to translate SQLException with " + codes + ", will now try the fallback translator");
/* 195:    */     }
/* 196:286 */     return null;
/* 197:    */   }
/* 198:    */   
/* 199:    */   protected DataAccessException customTranslate(String task, String sql, SQLException sqlEx)
/* 200:    */   {
/* 201:301 */     return null;
/* 202:    */   }
/* 203:    */   
/* 204:    */   protected DataAccessException createCustomException(String task, String sql, SQLException sqlEx, Class exceptionClass)
/* 205:    */   {
/* 206:    */     try
/* 207:    */     {
/* 208:322 */       int constructorType = 0;
/* 209:323 */       Constructor[] constructors = exceptionClass.getConstructors();
/* 210:324 */       for (int i = 0; i < constructors.length; i++)
/* 211:    */       {
/* 212:325 */         Class[] parameterTypes = constructors[i].getParameterTypes();
/* 213:326 */         if ((parameterTypes.length == 1) && (parameterTypes[0].equals(String.class)) && 
/* 214:327 */           (constructorType < 1)) {
/* 215:328 */           constructorType = 1;
/* 216:    */         }
/* 217:330 */         if ((parameterTypes.length == 2) && (parameterTypes[0].equals(String.class)) && 
/* 218:331 */           (parameterTypes[1].equals(Throwable.class)) && 
/* 219:332 */           (constructorType < 2)) {
/* 220:333 */           constructorType = 2;
/* 221:    */         }
/* 222:335 */         if ((parameterTypes.length == 2) && (parameterTypes[0].equals(String.class)) && 
/* 223:336 */           (parameterTypes[1].equals(SQLException.class)) && 
/* 224:337 */           (constructorType < 3)) {
/* 225:338 */           constructorType = 3;
/* 226:    */         }
/* 227:340 */         if ((parameterTypes.length == 3) && (parameterTypes[0].equals(String.class)) && 
/* 228:341 */           (parameterTypes[1].equals(String.class)) && (parameterTypes[2].equals(Throwable.class)) && 
/* 229:342 */           (constructorType < 4)) {
/* 230:343 */           constructorType = 4;
/* 231:    */         }
/* 232:345 */         if ((parameterTypes.length == 3) && (parameterTypes[0].equals(String.class)) && 
/* 233:346 */           (parameterTypes[1].equals(String.class)) && (parameterTypes[2].equals(SQLException.class)) && 
/* 234:347 */           (constructorType < 5)) {
/* 235:348 */           constructorType = 5;
/* 236:    */         }
/* 237:    */       }
/* 238:353 */       Constructor exceptionConstructor = null;
/* 239:354 */       switch (constructorType)
/* 240:    */       {
/* 241:    */       case 5: 
/* 242:356 */         Class[] messageAndSqlAndSqlExArgsClass = { String.class, String.class, SQLException.class };
/* 243:357 */         Object[] messageAndSqlAndSqlExArgs = { task, sql, sqlEx };
/* 244:358 */         exceptionConstructor = exceptionClass.getConstructor(messageAndSqlAndSqlExArgsClass);
/* 245:359 */         return (DataAccessException)exceptionConstructor.newInstance(messageAndSqlAndSqlExArgs);
/* 246:    */       case 4: 
/* 247:361 */         Class[] messageAndSqlAndThrowableArgsClass = { String.class, String.class, Throwable.class };
/* 248:362 */         Object[] messageAndSqlAndThrowableArgs = { task, sql, sqlEx };
/* 249:363 */         exceptionConstructor = exceptionClass.getConstructor(messageAndSqlAndThrowableArgsClass);
/* 250:364 */         return (DataAccessException)exceptionConstructor.newInstance(messageAndSqlAndThrowableArgs);
/* 251:    */       case 3: 
/* 252:366 */         Class[] messageAndSqlExArgsClass = { String.class, SQLException.class };
/* 253:367 */         Object[] messageAndSqlExArgs = { task + ": " + sqlEx.getMessage(), sqlEx };
/* 254:368 */         exceptionConstructor = exceptionClass.getConstructor(messageAndSqlExArgsClass);
/* 255:369 */         return (DataAccessException)exceptionConstructor.newInstance(messageAndSqlExArgs);
/* 256:    */       case 2: 
/* 257:371 */         Class[] messageAndThrowableArgsClass = { String.class, Throwable.class };
/* 258:372 */         Object[] messageAndThrowableArgs = { task + ": " + sqlEx.getMessage(), sqlEx };
/* 259:373 */         exceptionConstructor = exceptionClass.getConstructor(messageAndThrowableArgsClass);
/* 260:374 */         return (DataAccessException)exceptionConstructor.newInstance(messageAndThrowableArgs);
/* 261:    */       case 1: 
/* 262:376 */         Class[] messageOnlyArgsClass = { String.class };
/* 263:377 */         Object[] messageOnlyArgs = { task + ": " + sqlEx.getMessage() };
/* 264:378 */         exceptionConstructor = exceptionClass.getConstructor(messageOnlyArgsClass);
/* 265:379 */         return (DataAccessException)exceptionConstructor.newInstance(messageOnlyArgs);
/* 266:    */       }
/* 267:381 */       if (this.logger.isWarnEnabled()) {
/* 268:382 */         this.logger.warn("Unable to find appropriate constructor of custom exception class [" + 
/* 269:383 */           exceptionClass.getName() + "]");
/* 270:    */       }
/* 271:385 */       return null;
/* 272:    */     }
/* 273:    */     catch (Throwable ex)
/* 274:    */     {
/* 275:389 */       if (this.logger.isWarnEnabled()) {
/* 276:390 */         this.logger.warn("Unable to instantiate custom exception class [" + exceptionClass.getName() + "]", ex);
/* 277:    */       }
/* 278:    */     }
/* 279:392 */     return null;
/* 280:    */   }
/* 281:    */   
/* 282:    */   private void logTranslation(String task, String sql, SQLException sqlEx, boolean custom)
/* 283:    */   {
/* 284:397 */     if (this.logger.isDebugEnabled())
/* 285:    */     {
/* 286:398 */       String intro = custom ? "Custom translation of" : "Translating";
/* 287:399 */       this.logger.debug(intro + " SQLException with SQL state '" + sqlEx.getSQLState() + 
/* 288:400 */         "', error code '" + sqlEx.getErrorCode() + "', message [" + sqlEx.getMessage() + 
/* 289:401 */         "]; SQL was [" + sql + "] for task [" + task + "]");
/* 290:    */     }
/* 291:    */   }
/* 292:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator
 * JD-Core Version:    0.7.0.1
 */