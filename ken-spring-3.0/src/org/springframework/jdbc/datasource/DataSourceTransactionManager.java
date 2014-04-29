/*   1:    */ package org.springframework.jdbc.datasource;
/*   2:    */ 
/*   3:    */ import java.sql.Connection;
/*   4:    */ import java.sql.SQLException;
/*   5:    */ import javax.sql.DataSource;
/*   6:    */ import org.apache.commons.logging.Log;
/*   7:    */ import org.springframework.beans.factory.InitializingBean;
/*   8:    */ import org.springframework.transaction.CannotCreateTransactionException;
/*   9:    */ import org.springframework.transaction.TransactionDefinition;
/*  10:    */ import org.springframework.transaction.TransactionSystemException;
/*  11:    */ import org.springframework.transaction.support.AbstractPlatformTransactionManager;
/*  12:    */ import org.springframework.transaction.support.DefaultTransactionStatus;
/*  13:    */ import org.springframework.transaction.support.ResourceTransactionManager;
/*  14:    */ import org.springframework.transaction.support.TransactionSynchronizationManager;
/*  15:    */ 
/*  16:    */ public class DataSourceTransactionManager
/*  17:    */   extends AbstractPlatformTransactionManager
/*  18:    */   implements ResourceTransactionManager, InitializingBean
/*  19:    */ {
/*  20:    */   private DataSource dataSource;
/*  21:    */   
/*  22:    */   public DataSourceTransactionManager()
/*  23:    */   {
/*  24:114 */     setNestedTransactionAllowed(true);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public DataSourceTransactionManager(DataSource dataSource)
/*  28:    */   {
/*  29:122 */     this();
/*  30:123 */     setDataSource(dataSource);
/*  31:124 */     afterPropertiesSet();
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setDataSource(DataSource dataSource)
/*  35:    */   {
/*  36:146 */     if ((dataSource instanceof TransactionAwareDataSourceProxy)) {
/*  37:150 */       this.dataSource = ((TransactionAwareDataSourceProxy)dataSource).getTargetDataSource();
/*  38:    */     } else {
/*  39:153 */       this.dataSource = dataSource;
/*  40:    */     }
/*  41:    */   }
/*  42:    */   
/*  43:    */   public DataSource getDataSource()
/*  44:    */   {
/*  45:161 */     return this.dataSource;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void afterPropertiesSet()
/*  49:    */   {
/*  50:165 */     if (getDataSource() == null) {
/*  51:166 */       throw new IllegalArgumentException("Property 'dataSource' is required");
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55:    */   public Object getResourceFactory()
/*  56:    */   {
/*  57:172 */     return getDataSource();
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected Object doGetTransaction()
/*  61:    */   {
/*  62:177 */     DataSourceTransactionObject txObject = new DataSourceTransactionObject(null);
/*  63:178 */     txObject.setSavepointAllowed(isNestedTransactionAllowed());
/*  64:179 */     ConnectionHolder conHolder = 
/*  65:180 */       (ConnectionHolder)TransactionSynchronizationManager.getResource(this.dataSource);
/*  66:181 */     txObject.setConnectionHolder(conHolder, false);
/*  67:182 */     return txObject;
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected boolean isExistingTransaction(Object transaction)
/*  71:    */   {
/*  72:187 */     DataSourceTransactionObject txObject = (DataSourceTransactionObject)transaction;
/*  73:188 */     return (txObject.getConnectionHolder() != null) && (txObject.getConnectionHolder().isTransactionActive());
/*  74:    */   }
/*  75:    */   
/*  76:    */   protected void doBegin(Object transaction, TransactionDefinition definition)
/*  77:    */   {
/*  78:196 */     DataSourceTransactionObject txObject = (DataSourceTransactionObject)transaction;
/*  79:197 */     Connection con = null;
/*  80:    */     try
/*  81:    */     {
/*  82:200 */       if ((txObject.getConnectionHolder() == null) || 
/*  83:201 */         (txObject.getConnectionHolder().isSynchronizedWithTransaction()))
/*  84:    */       {
/*  85:202 */         Connection newCon = this.dataSource.getConnection();
/*  86:203 */         if (this.logger.isDebugEnabled()) {
/*  87:204 */           this.logger.debug("Acquired Connection [" + newCon + "] for JDBC transaction");
/*  88:    */         }
/*  89:206 */         txObject.setConnectionHolder(new ConnectionHolder(newCon), true);
/*  90:    */       }
/*  91:209 */       txObject.getConnectionHolder().setSynchronizedWithTransaction(true);
/*  92:210 */       con = txObject.getConnectionHolder().getConnection();
/*  93:    */       
/*  94:212 */       Integer previousIsolationLevel = DataSourceUtils.prepareConnectionForTransaction(con, definition);
/*  95:213 */       txObject.setPreviousIsolationLevel(previousIsolationLevel);
/*  96:218 */       if (con.getAutoCommit())
/*  97:    */       {
/*  98:219 */         txObject.setMustRestoreAutoCommit(true);
/*  99:220 */         if (this.logger.isDebugEnabled()) {
/* 100:221 */           this.logger.debug("Switching JDBC Connection [" + con + "] to manual commit");
/* 101:    */         }
/* 102:223 */         con.setAutoCommit(false);
/* 103:    */       }
/* 104:225 */       txObject.getConnectionHolder().setTransactionActive(true);
/* 105:    */       
/* 106:227 */       int timeout = determineTimeout(definition);
/* 107:228 */       if (timeout != -1) {
/* 108:229 */         txObject.getConnectionHolder().setTimeoutInSeconds(timeout);
/* 109:    */       }
/* 110:233 */       if (txObject.isNewConnectionHolder()) {
/* 111:234 */         TransactionSynchronizationManager.bindResource(getDataSource(), txObject.getConnectionHolder());
/* 112:    */       }
/* 113:    */     }
/* 114:    */     catch (Exception ex)
/* 115:    */     {
/* 116:239 */       DataSourceUtils.releaseConnection(con, this.dataSource);
/* 117:240 */       throw new CannotCreateTransactionException("Could not open JDBC Connection for transaction", ex);
/* 118:    */     }
/* 119:    */   }
/* 120:    */   
/* 121:    */   protected Object doSuspend(Object transaction)
/* 122:    */   {
/* 123:246 */     DataSourceTransactionObject txObject = (DataSourceTransactionObject)transaction;
/* 124:247 */     txObject.setConnectionHolder(null);
/* 125:248 */     ConnectionHolder conHolder = (ConnectionHolder)
/* 126:249 */       TransactionSynchronizationManager.unbindResource(this.dataSource);
/* 127:250 */     return conHolder;
/* 128:    */   }
/* 129:    */   
/* 130:    */   protected void doResume(Object transaction, Object suspendedResources)
/* 131:    */   {
/* 132:255 */     ConnectionHolder conHolder = (ConnectionHolder)suspendedResources;
/* 133:256 */     TransactionSynchronizationManager.bindResource(this.dataSource, conHolder);
/* 134:    */   }
/* 135:    */   
/* 136:    */   protected void doCommit(DefaultTransactionStatus status)
/* 137:    */   {
/* 138:261 */     DataSourceTransactionObject txObject = (DataSourceTransactionObject)status.getTransaction();
/* 139:262 */     Connection con = txObject.getConnectionHolder().getConnection();
/* 140:263 */     if (status.isDebug()) {
/* 141:264 */       this.logger.debug("Committing JDBC transaction on Connection [" + con + "]");
/* 142:    */     }
/* 143:    */     try
/* 144:    */     {
/* 145:267 */       con.commit();
/* 146:    */     }
/* 147:    */     catch (SQLException ex)
/* 148:    */     {
/* 149:270 */       throw new TransactionSystemException("Could not commit JDBC transaction", ex);
/* 150:    */     }
/* 151:    */   }
/* 152:    */   
/* 153:    */   protected void doRollback(DefaultTransactionStatus status)
/* 154:    */   {
/* 155:276 */     DataSourceTransactionObject txObject = (DataSourceTransactionObject)status.getTransaction();
/* 156:277 */     Connection con = txObject.getConnectionHolder().getConnection();
/* 157:278 */     if (status.isDebug()) {
/* 158:279 */       this.logger.debug("Rolling back JDBC transaction on Connection [" + con + "]");
/* 159:    */     }
/* 160:    */     try
/* 161:    */     {
/* 162:282 */       con.rollback();
/* 163:    */     }
/* 164:    */     catch (SQLException ex)
/* 165:    */     {
/* 166:285 */       throw new TransactionSystemException("Could not roll back JDBC transaction", ex);
/* 167:    */     }
/* 168:    */   }
/* 169:    */   
/* 170:    */   protected void doSetRollbackOnly(DefaultTransactionStatus status)
/* 171:    */   {
/* 172:291 */     DataSourceTransactionObject txObject = (DataSourceTransactionObject)status.getTransaction();
/* 173:292 */     if (status.isDebug()) {
/* 174:293 */       this.logger.debug("Setting JDBC transaction [" + txObject.getConnectionHolder().getConnection() + 
/* 175:294 */         "] rollback-only");
/* 176:    */     }
/* 177:296 */     txObject.setRollbackOnly();
/* 178:    */   }
/* 179:    */   
/* 180:    */   protected void doCleanupAfterCompletion(Object transaction)
/* 181:    */   {
/* 182:301 */     DataSourceTransactionObject txObject = (DataSourceTransactionObject)transaction;
/* 183:304 */     if (txObject.isNewConnectionHolder()) {
/* 184:305 */       TransactionSynchronizationManager.unbindResource(this.dataSource);
/* 185:    */     }
/* 186:309 */     Connection con = txObject.getConnectionHolder().getConnection();
/* 187:    */     try
/* 188:    */     {
/* 189:311 */       if (txObject.isMustRestoreAutoCommit()) {
/* 190:312 */         con.setAutoCommit(true);
/* 191:    */       }
/* 192:314 */       DataSourceUtils.resetConnectionAfterTransaction(con, txObject.getPreviousIsolationLevel());
/* 193:    */     }
/* 194:    */     catch (Throwable ex)
/* 195:    */     {
/* 196:317 */       this.logger.debug("Could not reset JDBC Connection after transaction", ex);
/* 197:    */     }
/* 198:320 */     if (txObject.isNewConnectionHolder())
/* 199:    */     {
/* 200:321 */       if (this.logger.isDebugEnabled()) {
/* 201:322 */         this.logger.debug("Releasing JDBC Connection [" + con + "] after transaction");
/* 202:    */       }
/* 203:324 */       DataSourceUtils.releaseConnection(con, this.dataSource);
/* 204:    */     }
/* 205:327 */     txObject.getConnectionHolder().clear();
/* 206:    */   }
/* 207:    */   
/* 208:    */   private static class DataSourceTransactionObject
/* 209:    */     extends JdbcTransactionObjectSupport
/* 210:    */   {
/* 211:    */     private boolean newConnectionHolder;
/* 212:    */     private boolean mustRestoreAutoCommit;
/* 213:    */     
/* 214:    */     public void setConnectionHolder(ConnectionHolder connectionHolder, boolean newConnectionHolder)
/* 215:    */     {
/* 216:342 */       super.setConnectionHolder(connectionHolder);
/* 217:343 */       this.newConnectionHolder = newConnectionHolder;
/* 218:    */     }
/* 219:    */     
/* 220:    */     public boolean isNewConnectionHolder()
/* 221:    */     {
/* 222:347 */       return this.newConnectionHolder;
/* 223:    */     }
/* 224:    */     
/* 225:    */     public boolean hasTransaction()
/* 226:    */     {
/* 227:351 */       return (getConnectionHolder() != null) && (getConnectionHolder().isTransactionActive());
/* 228:    */     }
/* 229:    */     
/* 230:    */     public void setMustRestoreAutoCommit(boolean mustRestoreAutoCommit)
/* 231:    */     {
/* 232:355 */       this.mustRestoreAutoCommit = mustRestoreAutoCommit;
/* 233:    */     }
/* 234:    */     
/* 235:    */     public boolean isMustRestoreAutoCommit()
/* 236:    */     {
/* 237:359 */       return this.mustRestoreAutoCommit;
/* 238:    */     }
/* 239:    */     
/* 240:    */     public void setRollbackOnly()
/* 241:    */     {
/* 242:363 */       getConnectionHolder().setRollbackOnly();
/* 243:    */     }
/* 244:    */     
/* 245:    */     public boolean isRollbackOnly()
/* 246:    */     {
/* 247:367 */       return getConnectionHolder().isRollbackOnly();
/* 248:    */     }
/* 249:    */   }
/* 250:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.DataSourceTransactionManager
 * JD-Core Version:    0.7.0.1
 */