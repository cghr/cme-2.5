/*   1:    */ package org.springframework.jdbc.datasource;
/*   2:    */ 
/*   3:    */ import java.sql.Connection;
/*   4:    */ import java.sql.SQLException;
/*   5:    */ import java.sql.Statement;
/*   6:    */ import javax.sql.DataSource;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.apache.commons.logging.LogFactory;
/*   9:    */ import org.springframework.jdbc.CannotGetJdbcConnectionException;
/*  10:    */ import org.springframework.transaction.TransactionDefinition;
/*  11:    */ import org.springframework.transaction.support.TransactionSynchronizationAdapter;
/*  12:    */ import org.springframework.transaction.support.TransactionSynchronizationManager;
/*  13:    */ import org.springframework.util.Assert;
/*  14:    */ 
/*  15:    */ public abstract class DataSourceUtils
/*  16:    */ {
/*  17:    */   public static final int CONNECTION_SYNCHRONIZATION_ORDER = 1000;
/*  18: 58 */   private static final Log logger = LogFactory.getLog(DataSourceUtils.class);
/*  19:    */   
/*  20:    */   public static Connection getConnection(DataSource dataSource)
/*  21:    */     throws CannotGetJdbcConnectionException
/*  22:    */   {
/*  23:    */     try
/*  24:    */     {
/*  25: 77 */       return doGetConnection(dataSource);
/*  26:    */     }
/*  27:    */     catch (SQLException ex)
/*  28:    */     {
/*  29: 80 */       throw new CannotGetJdbcConnectionException("Could not get JDBC Connection", ex);
/*  30:    */     }
/*  31:    */   }
/*  32:    */   
/*  33:    */   public static Connection doGetConnection(DataSource dataSource)
/*  34:    */     throws SQLException
/*  35:    */   {
/*  36: 97 */     Assert.notNull(dataSource, "No DataSource specified");
/*  37:    */     
/*  38: 99 */     ConnectionHolder conHolder = (ConnectionHolder)TransactionSynchronizationManager.getResource(dataSource);
/*  39:100 */     if ((conHolder != null) && ((conHolder.hasConnection()) || (conHolder.isSynchronizedWithTransaction())))
/*  40:    */     {
/*  41:101 */       conHolder.requested();
/*  42:102 */       if (!conHolder.hasConnection())
/*  43:    */       {
/*  44:103 */         logger.debug("Fetching resumed JDBC Connection from DataSource");
/*  45:104 */         conHolder.setConnection(dataSource.getConnection());
/*  46:    */       }
/*  47:106 */       return conHolder.getConnection();
/*  48:    */     }
/*  49:110 */     logger.debug("Fetching JDBC Connection from DataSource");
/*  50:111 */     Connection con = dataSource.getConnection();
/*  51:113 */     if (TransactionSynchronizationManager.isSynchronizationActive())
/*  52:    */     {
/*  53:114 */       logger.debug("Registering transaction synchronization for JDBC Connection");
/*  54:    */       
/*  55:    */ 
/*  56:117 */       ConnectionHolder holderToUse = conHolder;
/*  57:118 */       if (holderToUse == null) {
/*  58:119 */         holderToUse = new ConnectionHolder(con);
/*  59:    */       } else {
/*  60:122 */         holderToUse.setConnection(con);
/*  61:    */       }
/*  62:124 */       holderToUse.requested();
/*  63:125 */       TransactionSynchronizationManager.registerSynchronization(
/*  64:126 */         new ConnectionSynchronization(holderToUse, dataSource));
/*  65:127 */       holderToUse.setSynchronizedWithTransaction(true);
/*  66:128 */       if (holderToUse != conHolder) {
/*  67:129 */         TransactionSynchronizationManager.bindResource(dataSource, holderToUse);
/*  68:    */       }
/*  69:    */     }
/*  70:133 */     return con;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public static Integer prepareConnectionForTransaction(Connection con, TransactionDefinition definition)
/*  74:    */     throws SQLException
/*  75:    */   {
/*  76:147 */     Assert.notNull(con, "No Connection specified");
/*  77:150 */     if ((definition != null) && (definition.isReadOnly())) {
/*  78:    */       try
/*  79:    */       {
/*  80:152 */         if (logger.isDebugEnabled()) {
/*  81:153 */           logger.debug("Setting JDBC Connection [" + con + "] read-only");
/*  82:    */         }
/*  83:155 */         con.setReadOnly(true);
/*  84:    */       }
/*  85:    */       catch (SQLException ex)
/*  86:    */       {
/*  87:158 */         Throwable exToCheck = ex;
/*  88:159 */         while (exToCheck != null)
/*  89:    */         {
/*  90:160 */           if (exToCheck.getClass().getSimpleName().contains("Timeout")) {
/*  91:162 */             throw ex;
/*  92:    */           }
/*  93:164 */           exToCheck = exToCheck.getCause();
/*  94:    */         }
/*  95:167 */         logger.debug("Could not set JDBC Connection read-only", ex);
/*  96:    */       }
/*  97:    */       catch (RuntimeException ex)
/*  98:    */       {
/*  99:170 */         Throwable exToCheck = ex;
/* 100:171 */         while (exToCheck != null)
/* 101:    */         {
/* 102:172 */           if (exToCheck.getClass().getSimpleName().contains("Timeout")) {
/* 103:174 */             throw ex;
/* 104:    */           }
/* 105:176 */           exToCheck = exToCheck.getCause();
/* 106:    */         }
/* 107:179 */         logger.debug("Could not set JDBC Connection read-only", ex);
/* 108:    */       }
/* 109:    */     }
/* 110:184 */     Integer previousIsolationLevel = null;
/* 111:185 */     if ((definition != null) && (definition.getIsolationLevel() != -1))
/* 112:    */     {
/* 113:186 */       if (logger.isDebugEnabled()) {
/* 114:187 */         logger.debug("Changing isolation level of JDBC Connection [" + con + "] to " + 
/* 115:188 */           definition.getIsolationLevel());
/* 116:    */       }
/* 117:190 */       int currentIsolation = con.getTransactionIsolation();
/* 118:191 */       if (currentIsolation != definition.getIsolationLevel())
/* 119:    */       {
/* 120:192 */         previousIsolationLevel = Integer.valueOf(currentIsolation);
/* 121:193 */         con.setTransactionIsolation(definition.getIsolationLevel());
/* 122:    */       }
/* 123:    */     }
/* 124:197 */     return previousIsolationLevel;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public static void resetConnectionAfterTransaction(Connection con, Integer previousIsolationLevel)
/* 128:    */   {
/* 129:208 */     Assert.notNull(con, "No Connection specified");
/* 130:    */     try
/* 131:    */     {
/* 132:211 */       if (previousIsolationLevel != null)
/* 133:    */       {
/* 134:212 */         if (logger.isDebugEnabled()) {
/* 135:213 */           logger.debug("Resetting isolation level of JDBC Connection [" + 
/* 136:214 */             con + "] to " + previousIsolationLevel);
/* 137:    */         }
/* 138:216 */         con.setTransactionIsolation(previousIsolationLevel.intValue());
/* 139:    */       }
/* 140:220 */       if (con.isReadOnly())
/* 141:    */       {
/* 142:221 */         if (logger.isDebugEnabled()) {
/* 143:222 */           logger.debug("Resetting read-only flag of JDBC Connection [" + con + "]");
/* 144:    */         }
/* 145:224 */         con.setReadOnly(false);
/* 146:    */       }
/* 147:    */     }
/* 148:    */     catch (Throwable ex)
/* 149:    */     {
/* 150:228 */       logger.debug("Could not reset JDBC Connection after transaction", ex);
/* 151:    */     }
/* 152:    */   }
/* 153:    */   
/* 154:    */   public static boolean isConnectionTransactional(Connection con, DataSource dataSource)
/* 155:    */   {
/* 156:241 */     if (dataSource == null) {
/* 157:242 */       return false;
/* 158:    */     }
/* 159:244 */     ConnectionHolder conHolder = (ConnectionHolder)TransactionSynchronizationManager.getResource(dataSource);
/* 160:245 */     return (conHolder != null) && (connectionEquals(conHolder, con));
/* 161:    */   }
/* 162:    */   
/* 163:    */   public static void applyTransactionTimeout(Statement stmt, DataSource dataSource)
/* 164:    */     throws SQLException
/* 165:    */   {
/* 166:257 */     applyTimeout(stmt, dataSource, 0);
/* 167:    */   }
/* 168:    */   
/* 169:    */   public static void applyTimeout(Statement stmt, DataSource dataSource, int timeout)
/* 170:    */     throws SQLException
/* 171:    */   {
/* 172:270 */     Assert.notNull(stmt, "No Statement specified");
/* 173:271 */     Assert.notNull(dataSource, "No DataSource specified");
/* 174:272 */     ConnectionHolder holder = (ConnectionHolder)TransactionSynchronizationManager.getResource(dataSource);
/* 175:273 */     if ((holder != null) && (holder.hasTimeout())) {
/* 176:275 */       stmt.setQueryTimeout(holder.getTimeToLiveInSeconds());
/* 177:277 */     } else if (timeout > 0) {
/* 178:279 */       stmt.setQueryTimeout(timeout);
/* 179:    */     }
/* 180:    */   }
/* 181:    */   
/* 182:    */   public static void releaseConnection(Connection con, DataSource dataSource)
/* 183:    */   {
/* 184:    */     try
/* 185:    */     {
/* 186:294 */       doReleaseConnection(con, dataSource);
/* 187:    */     }
/* 188:    */     catch (SQLException ex)
/* 189:    */     {
/* 190:297 */       logger.debug("Could not close JDBC Connection", ex);
/* 191:    */     }
/* 192:    */     catch (Throwable ex)
/* 193:    */     {
/* 194:300 */       logger.debug("Unexpected exception on closing JDBC Connection", ex);
/* 195:    */     }
/* 196:    */   }
/* 197:    */   
/* 198:    */   public static void doReleaseConnection(Connection con, DataSource dataSource)
/* 199:    */     throws SQLException
/* 200:    */   {
/* 201:316 */     if (con == null) {
/* 202:317 */       return;
/* 203:    */     }
/* 204:320 */     if (dataSource != null)
/* 205:    */     {
/* 206:321 */       ConnectionHolder conHolder = (ConnectionHolder)TransactionSynchronizationManager.getResource(dataSource);
/* 207:322 */       if ((conHolder != null) && (connectionEquals(conHolder, con)))
/* 208:    */       {
/* 209:324 */         conHolder.released();
/* 210:325 */         return;
/* 211:    */       }
/* 212:    */     }
/* 213:331 */     if ((!(dataSource instanceof SmartDataSource)) || (((SmartDataSource)dataSource).shouldClose(con)))
/* 214:    */     {
/* 215:332 */       logger.debug("Returning JDBC Connection to DataSource");
/* 216:333 */       con.close();
/* 217:    */     }
/* 218:    */   }
/* 219:    */   
/* 220:    */   private static boolean connectionEquals(ConnectionHolder conHolder, Connection passedInCon)
/* 221:    */   {
/* 222:348 */     if (!conHolder.hasConnection()) {
/* 223:349 */       return false;
/* 224:    */     }
/* 225:351 */     Connection heldCon = conHolder.getConnection();
/* 226:    */     
/* 227:    */ 
/* 228:    */ 
/* 229:355 */     return (heldCon == passedInCon) || (heldCon.equals(passedInCon)) || (getTargetConnection(heldCon).equals(passedInCon));
/* 230:    */   }
/* 231:    */   
/* 232:    */   public static Connection getTargetConnection(Connection con)
/* 233:    */   {
/* 234:367 */     Connection conToUse = con;
/* 235:368 */     while ((conToUse instanceof ConnectionProxy)) {
/* 236:369 */       conToUse = ((ConnectionProxy)conToUse).getTargetConnection();
/* 237:    */     }
/* 238:371 */     return conToUse;
/* 239:    */   }
/* 240:    */   
/* 241:    */   private static int getConnectionSynchronizationOrder(DataSource dataSource)
/* 242:    */   {
/* 243:383 */     int order = 1000;
/* 244:384 */     DataSource currDs = dataSource;
/* 245:385 */     while ((currDs instanceof DelegatingDataSource))
/* 246:    */     {
/* 247:386 */       order--;
/* 248:387 */       currDs = ((DelegatingDataSource)currDs).getTargetDataSource();
/* 249:    */     }
/* 250:389 */     return order;
/* 251:    */   }
/* 252:    */   
/* 253:    */   private static class ConnectionSynchronization
/* 254:    */     extends TransactionSynchronizationAdapter
/* 255:    */   {
/* 256:    */     private final ConnectionHolder connectionHolder;
/* 257:    */     private final DataSource dataSource;
/* 258:    */     private int order;
/* 259:406 */     private boolean holderActive = true;
/* 260:    */     
/* 261:    */     public ConnectionSynchronization(ConnectionHolder connectionHolder, DataSource dataSource)
/* 262:    */     {
/* 263:409 */       this.connectionHolder = connectionHolder;
/* 264:410 */       this.dataSource = dataSource;
/* 265:411 */       this.order = DataSourceUtils.getConnectionSynchronizationOrder(dataSource);
/* 266:    */     }
/* 267:    */     
/* 268:    */     public int getOrder()
/* 269:    */     {
/* 270:416 */       return this.order;
/* 271:    */     }
/* 272:    */     
/* 273:    */     public void suspend()
/* 274:    */     {
/* 275:421 */       if (this.holderActive)
/* 276:    */       {
/* 277:422 */         TransactionSynchronizationManager.unbindResource(this.dataSource);
/* 278:423 */         if ((this.connectionHolder.hasConnection()) && (!this.connectionHolder.isOpen()))
/* 279:    */         {
/* 280:428 */           DataSourceUtils.releaseConnection(this.connectionHolder.getConnection(), this.dataSource);
/* 281:429 */           this.connectionHolder.setConnection(null);
/* 282:    */         }
/* 283:    */       }
/* 284:    */     }
/* 285:    */     
/* 286:    */     public void resume()
/* 287:    */     {
/* 288:436 */       if (this.holderActive) {
/* 289:437 */         TransactionSynchronizationManager.bindResource(this.dataSource, this.connectionHolder);
/* 290:    */       }
/* 291:    */     }
/* 292:    */     
/* 293:    */     public void beforeCompletion()
/* 294:    */     {
/* 295:448 */       if (!this.connectionHolder.isOpen())
/* 296:    */       {
/* 297:449 */         TransactionSynchronizationManager.unbindResource(this.dataSource);
/* 298:450 */         this.holderActive = false;
/* 299:451 */         if (this.connectionHolder.hasConnection()) {
/* 300:452 */           DataSourceUtils.releaseConnection(this.connectionHolder.getConnection(), this.dataSource);
/* 301:    */         }
/* 302:    */       }
/* 303:    */     }
/* 304:    */     
/* 305:    */     public void afterCompletion(int status)
/* 306:    */     {
/* 307:462 */       if (this.holderActive)
/* 308:    */       {
/* 309:465 */         TransactionSynchronizationManager.unbindResourceIfPossible(this.dataSource);
/* 310:466 */         this.holderActive = false;
/* 311:467 */         if (this.connectionHolder.hasConnection())
/* 312:    */         {
/* 313:468 */           DataSourceUtils.releaseConnection(this.connectionHolder.getConnection(), this.dataSource);
/* 314:    */           
/* 315:470 */           this.connectionHolder.setConnection(null);
/* 316:    */         }
/* 317:    */       }
/* 318:473 */       this.connectionHolder.reset();
/* 319:    */     }
/* 320:    */   }
/* 321:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.DataSourceUtils
 * JD-Core Version:    0.7.0.1
 */