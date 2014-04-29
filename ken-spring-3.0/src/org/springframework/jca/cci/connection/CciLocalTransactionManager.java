/*   1:    */ package org.springframework.jca.cci.connection;
/*   2:    */ 
/*   3:    */ import javax.resource.NotSupportedException;
/*   4:    */ import javax.resource.ResourceException;
/*   5:    */ import javax.resource.cci.Connection;
/*   6:    */ import javax.resource.cci.ConnectionFactory;
/*   7:    */ import javax.resource.cci.LocalTransaction;
/*   8:    */ import javax.resource.spi.LocalTransactionException;
/*   9:    */ import org.apache.commons.logging.Log;
/*  10:    */ import org.springframework.beans.factory.InitializingBean;
/*  11:    */ import org.springframework.transaction.CannotCreateTransactionException;
/*  12:    */ import org.springframework.transaction.TransactionDefinition;
/*  13:    */ import org.springframework.transaction.TransactionException;
/*  14:    */ import org.springframework.transaction.TransactionSystemException;
/*  15:    */ import org.springframework.transaction.support.AbstractPlatformTransactionManager;
/*  16:    */ import org.springframework.transaction.support.DefaultTransactionStatus;
/*  17:    */ import org.springframework.transaction.support.ResourceTransactionManager;
/*  18:    */ import org.springframework.transaction.support.TransactionSynchronizationManager;
/*  19:    */ 
/*  20:    */ public class CciLocalTransactionManager
/*  21:    */   extends AbstractPlatformTransactionManager
/*  22:    */   implements ResourceTransactionManager, InitializingBean
/*  23:    */ {
/*  24:    */   private ConnectionFactory connectionFactory;
/*  25:    */   
/*  26:    */   public CciLocalTransactionManager() {}
/*  27:    */   
/*  28:    */   public CciLocalTransactionManager(ConnectionFactory connectionFactory)
/*  29:    */   {
/*  30: 82 */     setConnectionFactory(connectionFactory);
/*  31: 83 */     afterPropertiesSet();
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setConnectionFactory(ConnectionFactory cf)
/*  35:    */   {
/*  36: 92 */     if ((cf instanceof TransactionAwareConnectionFactoryProxy)) {
/*  37: 96 */       this.connectionFactory = ((TransactionAwareConnectionFactoryProxy)cf).getTargetConnectionFactory();
/*  38:    */     } else {
/*  39: 99 */       this.connectionFactory = cf;
/*  40:    */     }
/*  41:    */   }
/*  42:    */   
/*  43:    */   public ConnectionFactory getConnectionFactory()
/*  44:    */   {
/*  45:108 */     return this.connectionFactory;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void afterPropertiesSet()
/*  49:    */   {
/*  50:112 */     if (getConnectionFactory() == null) {
/*  51:113 */       throw new IllegalArgumentException("Property 'connectionFactory' is required");
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55:    */   public Object getResourceFactory()
/*  56:    */   {
/*  57:119 */     return getConnectionFactory();
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected Object doGetTransaction()
/*  61:    */   {
/*  62:124 */     CciLocalTransactionObject txObject = new CciLocalTransactionObject(null);
/*  63:125 */     ConnectionHolder conHolder = 
/*  64:126 */       (ConnectionHolder)TransactionSynchronizationManager.getResource(getConnectionFactory());
/*  65:127 */     txObject.setConnectionHolder(conHolder);
/*  66:128 */     return txObject;
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected boolean isExistingTransaction(Object transaction)
/*  70:    */   {
/*  71:133 */     CciLocalTransactionObject txObject = (CciLocalTransactionObject)transaction;
/*  72:    */     
/*  73:135 */     return txObject.getConnectionHolder() != null;
/*  74:    */   }
/*  75:    */   
/*  76:    */   protected void doBegin(Object transaction, TransactionDefinition definition)
/*  77:    */   {
/*  78:140 */     CciLocalTransactionObject txObject = (CciLocalTransactionObject)transaction;
/*  79:    */     
/*  80:142 */     Connection con = null;
/*  81:    */     try
/*  82:    */     {
/*  83:145 */       con = getConnectionFactory().getConnection();
/*  84:146 */       if (this.logger.isDebugEnabled()) {
/*  85:147 */         this.logger.debug("Acquired Connection [" + con + "] for local CCI transaction");
/*  86:    */       }
/*  87:150 */       txObject.setConnectionHolder(new ConnectionHolder(con));
/*  88:151 */       txObject.getConnectionHolder().setSynchronizedWithTransaction(true);
/*  89:    */       
/*  90:153 */       con.getLocalTransaction().begin();
/*  91:154 */       int timeout = determineTimeout(definition);
/*  92:155 */       if (timeout != -1) {
/*  93:156 */         txObject.getConnectionHolder().setTimeoutInSeconds(timeout);
/*  94:    */       }
/*  95:158 */       TransactionSynchronizationManager.bindResource(getConnectionFactory(), txObject.getConnectionHolder());
/*  96:    */     }
/*  97:    */     catch (NotSupportedException ex)
/*  98:    */     {
/*  99:162 */       ConnectionFactoryUtils.releaseConnection(con, getConnectionFactory());
/* 100:163 */       throw new CannotCreateTransactionException("CCI Connection does not support local transactions", ex);
/* 101:    */     }
/* 102:    */     catch (LocalTransactionException ex)
/* 103:    */     {
/* 104:166 */       ConnectionFactoryUtils.releaseConnection(con, getConnectionFactory());
/* 105:167 */       throw new CannotCreateTransactionException("Could not begin local CCI transaction", ex);
/* 106:    */     }
/* 107:    */     catch (ResourceException ex)
/* 108:    */     {
/* 109:170 */       ConnectionFactoryUtils.releaseConnection(con, getConnectionFactory());
/* 110:171 */       throw new TransactionSystemException("Unexpected failure on begin of CCI local transaction", ex);
/* 111:    */     }
/* 112:    */   }
/* 113:    */   
/* 114:    */   protected Object doSuspend(Object transaction)
/* 115:    */   {
/* 116:177 */     CciLocalTransactionObject txObject = (CciLocalTransactionObject)transaction;
/* 117:178 */     txObject.setConnectionHolder(null);
/* 118:179 */     return TransactionSynchronizationManager.unbindResource(getConnectionFactory());
/* 119:    */   }
/* 120:    */   
/* 121:    */   protected void doResume(Object transaction, Object suspendedResources)
/* 122:    */   {
/* 123:184 */     ConnectionHolder conHolder = (ConnectionHolder)suspendedResources;
/* 124:185 */     TransactionSynchronizationManager.bindResource(getConnectionFactory(), conHolder);
/* 125:    */   }
/* 126:    */   
/* 127:    */   protected boolean isRollbackOnly(Object transaction)
/* 128:    */     throws TransactionException
/* 129:    */   {
/* 130:189 */     CciLocalTransactionObject txObject = (CciLocalTransactionObject)transaction;
/* 131:190 */     return txObject.getConnectionHolder().isRollbackOnly();
/* 132:    */   }
/* 133:    */   
/* 134:    */   protected void doCommit(DefaultTransactionStatus status)
/* 135:    */   {
/* 136:195 */     CciLocalTransactionObject txObject = (CciLocalTransactionObject)status.getTransaction();
/* 137:196 */     Connection con = txObject.getConnectionHolder().getConnection();
/* 138:197 */     if (status.isDebug()) {
/* 139:198 */       this.logger.debug("Committing CCI local transaction on Connection [" + con + "]");
/* 140:    */     }
/* 141:    */     try
/* 142:    */     {
/* 143:201 */       con.getLocalTransaction().commit();
/* 144:    */     }
/* 145:    */     catch (LocalTransactionException ex)
/* 146:    */     {
/* 147:204 */       throw new TransactionSystemException("Could not commit CCI local transaction", ex);
/* 148:    */     }
/* 149:    */     catch (ResourceException ex)
/* 150:    */     {
/* 151:207 */       throw new TransactionSystemException("Unexpected failure on commit of CCI local transaction", ex);
/* 152:    */     }
/* 153:    */   }
/* 154:    */   
/* 155:    */   protected void doRollback(DefaultTransactionStatus status)
/* 156:    */   {
/* 157:213 */     CciLocalTransactionObject txObject = (CciLocalTransactionObject)status.getTransaction();
/* 158:214 */     Connection con = txObject.getConnectionHolder().getConnection();
/* 159:215 */     if (status.isDebug()) {
/* 160:216 */       this.logger.debug("Rolling back CCI local transaction on Connection [" + con + "]");
/* 161:    */     }
/* 162:    */     try
/* 163:    */     {
/* 164:219 */       con.getLocalTransaction().rollback();
/* 165:    */     }
/* 166:    */     catch (LocalTransactionException ex)
/* 167:    */     {
/* 168:222 */       throw new TransactionSystemException("Could not roll back CCI local transaction", ex);
/* 169:    */     }
/* 170:    */     catch (ResourceException ex)
/* 171:    */     {
/* 172:225 */       throw new TransactionSystemException("Unexpected failure on rollback of CCI local transaction", ex);
/* 173:    */     }
/* 174:    */   }
/* 175:    */   
/* 176:    */   protected void doSetRollbackOnly(DefaultTransactionStatus status)
/* 177:    */   {
/* 178:231 */     CciLocalTransactionObject txObject = (CciLocalTransactionObject)status.getTransaction();
/* 179:232 */     if (status.isDebug()) {
/* 180:233 */       this.logger.debug("Setting CCI local transaction [" + txObject.getConnectionHolder().getConnection() + 
/* 181:234 */         "] rollback-only");
/* 182:    */     }
/* 183:236 */     txObject.getConnectionHolder().setRollbackOnly();
/* 184:    */   }
/* 185:    */   
/* 186:    */   protected void doCleanupAfterCompletion(Object transaction)
/* 187:    */   {
/* 188:241 */     CciLocalTransactionObject txObject = (CciLocalTransactionObject)transaction;
/* 189:    */     
/* 190:    */ 
/* 191:244 */     TransactionSynchronizationManager.unbindResource(getConnectionFactory());
/* 192:245 */     txObject.getConnectionHolder().clear();
/* 193:    */     
/* 194:247 */     Connection con = txObject.getConnectionHolder().getConnection();
/* 195:248 */     if (this.logger.isDebugEnabled()) {
/* 196:249 */       this.logger.debug("Releasing CCI Connection [" + con + "] after transaction");
/* 197:    */     }
/* 198:251 */     ConnectionFactoryUtils.releaseConnection(con, getConnectionFactory());
/* 199:    */   }
/* 200:    */   
/* 201:    */   private static class CciLocalTransactionObject
/* 202:    */   {
/* 203:    */     private ConnectionHolder connectionHolder;
/* 204:    */     
/* 205:    */     public void setConnectionHolder(ConnectionHolder connectionHolder)
/* 206:    */     {
/* 207:265 */       this.connectionHolder = connectionHolder;
/* 208:    */     }
/* 209:    */     
/* 210:    */     public ConnectionHolder getConnectionHolder()
/* 211:    */     {
/* 212:269 */       return this.connectionHolder;
/* 213:    */     }
/* 214:    */   }
/* 215:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.cci.connection.CciLocalTransactionManager
 * JD-Core Version:    0.7.0.1
 */