/*   1:    */ package org.springframework.transaction.jta;
/*   2:    */ 
/*   3:    */ import com.ibm.wsspi.uow.UOWAction;
/*   4:    */ import com.ibm.wsspi.uow.UOWActionException;
/*   5:    */ import com.ibm.wsspi.uow.UOWException;
/*   6:    */ import com.ibm.wsspi.uow.UOWManager;
/*   7:    */ import com.ibm.wsspi.uow.UOWManagerFactory;
/*   8:    */ import java.util.List;
/*   9:    */ import javax.naming.NamingException;
/*  10:    */ import org.apache.commons.logging.Log;
/*  11:    */ import org.springframework.jndi.JndiTemplate;
/*  12:    */ import org.springframework.transaction.IllegalTransactionStateException;
/*  13:    */ import org.springframework.transaction.InvalidTimeoutException;
/*  14:    */ import org.springframework.transaction.NestedTransactionNotSupportedException;
/*  15:    */ import org.springframework.transaction.TransactionDefinition;
/*  16:    */ import org.springframework.transaction.TransactionException;
/*  17:    */ import org.springframework.transaction.TransactionSystemException;
/*  18:    */ import org.springframework.transaction.support.AbstractPlatformTransactionManager.SuspendedResourcesHolder;
/*  19:    */ import org.springframework.transaction.support.CallbackPreferringPlatformTransactionManager;
/*  20:    */ import org.springframework.transaction.support.DefaultTransactionDefinition;
/*  21:    */ import org.springframework.transaction.support.DefaultTransactionStatus;
/*  22:    */ import org.springframework.transaction.support.TransactionCallback;
/*  23:    */ import org.springframework.transaction.support.TransactionSynchronization;
/*  24:    */ import org.springframework.transaction.support.TransactionSynchronizationManager;
/*  25:    */ import org.springframework.util.ReflectionUtils;
/*  26:    */ 
/*  27:    */ public class WebSphereUowTransactionManager
/*  28:    */   extends JtaTransactionManager
/*  29:    */   implements CallbackPreferringPlatformTransactionManager
/*  30:    */ {
/*  31:    */   public static final String DEFAULT_UOW_MANAGER_NAME = "java:comp/websphere/UOWManager";
/*  32:    */   private UOWManager uowManager;
/*  33:    */   private String uowManagerName;
/*  34:    */   
/*  35:    */   public WebSphereUowTransactionManager()
/*  36:    */   {
/*  37: 99 */     setAutodetectTransactionManager(false);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public WebSphereUowTransactionManager(UOWManager uowManager)
/*  41:    */   {
/*  42:107 */     this();
/*  43:108 */     this.uowManager = uowManager;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setUowManager(UOWManager uowManager)
/*  47:    */   {
/*  48:119 */     this.uowManager = uowManager;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setUowManagerName(String uowManagerName)
/*  52:    */   {
/*  53:129 */     this.uowManagerName = uowManagerName;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void afterPropertiesSet()
/*  57:    */     throws TransactionSystemException
/*  58:    */   {
/*  59:135 */     initUserTransactionAndTransactionManager();
/*  60:138 */     if (this.uowManager == null) {
/*  61:139 */       if (this.uowManagerName != null) {
/*  62:140 */         this.uowManager = lookupUowManager(this.uowManagerName);
/*  63:    */       } else {
/*  64:143 */         this.uowManager = lookupDefaultUowManager();
/*  65:    */       }
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected UOWManager lookupUowManager(String uowManagerName)
/*  70:    */     throws TransactionSystemException
/*  71:    */   {
/*  72:    */     try
/*  73:    */     {
/*  74:158 */       if (this.logger.isDebugEnabled()) {
/*  75:159 */         this.logger.debug("Retrieving WebSphere UOWManager from JNDI location [" + uowManagerName + "]");
/*  76:    */       }
/*  77:161 */       return (UOWManager)getJndiTemplate().lookup(uowManagerName, UOWManager.class);
/*  78:    */     }
/*  79:    */     catch (NamingException ex)
/*  80:    */     {
/*  81:164 */       throw new TransactionSystemException(
/*  82:165 */         "WebSphere UOWManager is not available at JNDI location [" + uowManagerName + "]", ex);
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected UOWManager lookupDefaultUowManager()
/*  87:    */     throws TransactionSystemException
/*  88:    */   {
/*  89:    */     try
/*  90:    */     {
/*  91:178 */       this.logger.debug("Retrieving WebSphere UOWManager from default JNDI location [java:comp/websphere/UOWManager]");
/*  92:179 */       return (UOWManager)getJndiTemplate().lookup("java:comp/websphere/UOWManager", UOWManager.class);
/*  93:    */     }
/*  94:    */     catch (NamingException localNamingException)
/*  95:    */     {
/*  96:182 */       this.logger.debug("WebSphere UOWManager is not available at default JNDI location [java:comp/websphere/UOWManager] - falling back to UOWManagerFactory lookup");
/*  97:    */     }
/*  98:184 */     return UOWManagerFactory.getUOWManager();
/*  99:    */   }
/* 100:    */   
/* 101:    */   protected void doRegisterAfterCompletionWithJtaTransaction(JtaTransactionObject txObject, List<TransactionSynchronization> synchronizations)
/* 102:    */   {
/* 103:195 */     this.uowManager.registerInterposedSynchronization(new JtaAfterCompletionSynchronization(synchronizations));
/* 104:    */   }
/* 105:    */   
/* 106:    */   public boolean supportsResourceAdapterManagedTransactions()
/* 107:    */   {
/* 108:209 */     return true;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public <T> T execute(TransactionDefinition definition, TransactionCallback<T> callback)
/* 112:    */     throws TransactionException
/* 113:    */   {
/* 114:214 */     if (definition == null) {
/* 115:216 */       definition = new DefaultTransactionDefinition();
/* 116:    */     }
/* 117:219 */     if (definition.getTimeout() < -1) {
/* 118:220 */       throw new InvalidTimeoutException("Invalid transaction timeout", definition.getTimeout());
/* 119:    */     }
/* 120:222 */     int pb = definition.getPropagationBehavior();
/* 121:223 */     boolean existingTx = (this.uowManager.getUOWStatus() != 5) && 
/* 122:224 */       (this.uowManager.getUOWType() != 0);
/* 123:    */     
/* 124:226 */     int uowType = 1;
/* 125:227 */     boolean joinTx = false;
/* 126:228 */     boolean newSynch = false;
/* 127:230 */     if (existingTx)
/* 128:    */     {
/* 129:231 */       if (pb == 5) {
/* 130:232 */         throw new IllegalTransactionStateException(
/* 131:233 */           "Transaction propagation 'never' but existing transaction found");
/* 132:    */       }
/* 133:235 */       if (pb == 6) {
/* 134:236 */         throw new NestedTransactionNotSupportedException(
/* 135:237 */           "Transaction propagation 'nested' not supported for WebSphere UOW transactions");
/* 136:    */       }
/* 137:239 */       if ((pb == 1) || 
/* 138:240 */         (pb == 0) || (pb == 2))
/* 139:    */       {
/* 140:241 */         joinTx = true;
/* 141:242 */         newSynch = getTransactionSynchronization() != 2;
/* 142:    */       }
/* 143:244 */       else if (pb == 4)
/* 144:    */       {
/* 145:245 */         uowType = 0;
/* 146:246 */         newSynch = getTransactionSynchronization() == 0;
/* 147:    */       }
/* 148:    */       else
/* 149:    */       {
/* 150:249 */         newSynch = getTransactionSynchronization() != 2;
/* 151:    */       }
/* 152:    */     }
/* 153:    */     else
/* 154:    */     {
/* 155:253 */       if (pb == 2) {
/* 156:254 */         throw new IllegalTransactionStateException(
/* 157:255 */           "Transaction propagation 'mandatory' but no existing transaction found");
/* 158:    */       }
/* 159:257 */       if ((pb == 1) || 
/* 160:258 */         (pb == 4) || (pb == 5))
/* 161:    */       {
/* 162:259 */         uowType = 0;
/* 163:260 */         newSynch = getTransactionSynchronization() == 0;
/* 164:    */       }
/* 165:    */       else
/* 166:    */       {
/* 167:263 */         newSynch = getTransactionSynchronization() != 2;
/* 168:    */       }
/* 169:    */     }
/* 170:267 */     boolean debug = this.logger.isDebugEnabled();
/* 171:268 */     if (debug) {
/* 172:269 */       this.logger.debug("Creating new transaction with name [" + definition.getName() + "]: " + definition);
/* 173:    */     }
/* 174:271 */     AbstractPlatformTransactionManager.SuspendedResourcesHolder suspendedResources = !joinTx ? suspend(null) : null;
/* 175:    */     try
/* 176:    */     {
/* 177:273 */       if (definition.getTimeout() > -1) {
/* 178:274 */         this.uowManager.setUOWTimeout(uowType, definition.getTimeout());
/* 179:    */       }
/* 180:276 */       if (debug) {
/* 181:277 */         this.logger.debug("Invoking WebSphere UOW action: type=" + uowType + ", join=" + joinTx);
/* 182:    */       }
/* 183:279 */       UOWActionAdapter<T> action = new UOWActionAdapter(
/* 184:280 */         definition, callback, uowType == 1, !joinTx, newSynch, debug);
/* 185:281 */       this.uowManager.runUnderUOW(uowType, joinTx, action);
/* 186:282 */       if (debug) {
/* 187:283 */         this.logger.debug("Returned from WebSphere UOW action: type=" + uowType + ", join=" + joinTx);
/* 188:    */       }
/* 189:285 */       return action.getResult();
/* 190:    */     }
/* 191:    */     catch (UOWException ex)
/* 192:    */     {
/* 193:288 */       throw new TransactionSystemException("UOWManager transaction processing failed", ex);
/* 194:    */     }
/* 195:    */     catch (UOWActionException ex)
/* 196:    */     {
/* 197:291 */       throw new TransactionSystemException("UOWManager threw unexpected UOWActionException", ex);
/* 198:    */     }
/* 199:    */     finally
/* 200:    */     {
/* 201:294 */       if (suspendedResources != null) {
/* 202:295 */         resume(null, suspendedResources);
/* 203:    */       }
/* 204:    */     }
/* 205:    */   }
/* 206:    */   
/* 207:    */   private class UOWActionAdapter<T>
/* 208:    */     implements UOWAction
/* 209:    */   {
/* 210:    */     private final TransactionDefinition definition;
/* 211:    */     private final TransactionCallback<T> callback;
/* 212:    */     private final boolean actualTransaction;
/* 213:    */     private final boolean newTransaction;
/* 214:    */     private final boolean newSynchronization;
/* 215:    */     private boolean debug;
/* 216:    */     private T result;
/* 217:    */     private Throwable exception;
/* 218:    */     
/* 219:    */     public UOWActionAdapter(TransactionCallback<T> definition, boolean callback, boolean actualTransaction, boolean newTransaction, boolean newSynchronization)
/* 220:    */     {
/* 221:324 */       this.definition = definition;
/* 222:325 */       this.callback = callback;
/* 223:326 */       this.actualTransaction = actualTransaction;
/* 224:327 */       this.newTransaction = newTransaction;
/* 225:328 */       this.newSynchronization = newSynchronization;
/* 226:329 */       this.debug = debug;
/* 227:    */     }
/* 228:    */     
/* 229:    */     public void run()
/* 230:    */     {
/* 231:333 */       DefaultTransactionStatus status = WebSphereUowTransactionManager.this
/* 232:    */       
/* 233:335 */         .prepareTransactionStatus(this.definition, this.actualTransaction ? this : null, this.newTransaction, this.newSynchronization, this.debug, null);
/* 234:    */       List<TransactionSynchronization> synchronizations;
/* 235:    */       try
/* 236:    */       {
/* 237:337 */         this.result = this.callback.doInTransaction(status);
/* 238:338 */         WebSphereUowTransactionManager.this.triggerBeforeCommit(status);
/* 239:    */       }
/* 240:    */       catch (Throwable ex)
/* 241:    */       {
/* 242:341 */         this.exception = ex;
/* 243:342 */         WebSphereUowTransactionManager.this.uowManager.setRollbackOnly();
/* 244:    */       }
/* 245:    */       finally
/* 246:    */       {
/* 247:    */         List<TransactionSynchronization> synchronizations;
/* 248:345 */         if (status.isLocalRollbackOnly())
/* 249:    */         {
/* 250:346 */           if (status.isDebug()) {
/* 251:347 */             WebSphereUowTransactionManager.access$1(WebSphereUowTransactionManager.this).debug("Transactional code has requested rollback");
/* 252:    */           }
/* 253:349 */           WebSphereUowTransactionManager.this.uowManager.setRollbackOnly();
/* 254:    */         }
/* 255:351 */         WebSphereUowTransactionManager.this.triggerBeforeCompletion(status);
/* 256:352 */         if (status.isNewSynchronization())
/* 257:    */         {
/* 258:353 */           List<TransactionSynchronization> synchronizations = TransactionSynchronizationManager.getSynchronizations();
/* 259:354 */           TransactionSynchronizationManager.clear();
/* 260:355 */           if (!synchronizations.isEmpty()) {
/* 261:356 */             WebSphereUowTransactionManager.this.uowManager.registerInterposedSynchronization(new JtaAfterCompletionSynchronization(synchronizations));
/* 262:    */           }
/* 263:    */         }
/* 264:    */       }
/* 265:    */     }
/* 266:    */     
/* 267:    */     public T getResult()
/* 268:    */     {
/* 269:363 */       if (this.exception != null) {
/* 270:364 */         ReflectionUtils.rethrowRuntimeException(this.exception);
/* 271:    */       }
/* 272:366 */       return this.result;
/* 273:    */     }
/* 274:    */   }
/* 275:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.jta.WebSphereUowTransactionManager
 * JD-Core Version:    0.7.0.1
 */