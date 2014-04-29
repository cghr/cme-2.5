/*   1:    */ package org.springframework.transaction.jta;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.lang.reflect.InvocationTargetException;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import javax.transaction.InvalidTransactionException;
/*   7:    */ import javax.transaction.NotSupportedException;
/*   8:    */ import javax.transaction.SystemException;
/*   9:    */ import javax.transaction.Transaction;
/*  10:    */ import javax.transaction.TransactionManager;
/*  11:    */ import javax.transaction.UserTransaction;
/*  12:    */ import org.apache.commons.logging.Log;
/*  13:    */ import org.springframework.transaction.TransactionDefinition;
/*  14:    */ import org.springframework.transaction.TransactionSystemException;
/*  15:    */ 
/*  16:    */ public class WebLogicJtaTransactionManager
/*  17:    */   extends JtaTransactionManager
/*  18:    */ {
/*  19:    */   private static final String USER_TRANSACTION_CLASS_NAME = "weblogic.transaction.UserTransaction";
/*  20:    */   private static final String CLIENT_TRANSACTION_MANAGER_CLASS_NAME = "weblogic.transaction.ClientTransactionManager";
/*  21:    */   private static final String TRANSACTION_CLASS_NAME = "weblogic.transaction.Transaction";
/*  22:    */   private static final String TRANSACTION_HELPER_CLASS_NAME = "weblogic.transaction.TransactionHelper";
/*  23:    */   private static final String ISOLATION_LEVEL_KEY = "ISOLATION LEVEL";
/*  24:    */   private boolean weblogicUserTransactionAvailable;
/*  25:    */   private Method beginWithNameMethod;
/*  26:    */   private Method beginWithNameAndTimeoutMethod;
/*  27:    */   private boolean weblogicTransactionManagerAvailable;
/*  28:    */   private Method forceResumeMethod;
/*  29:    */   private Method setPropertyMethod;
/*  30:    */   private Object transactionHelper;
/*  31:    */   
/*  32:    */   public void afterPropertiesSet()
/*  33:    */     throws TransactionSystemException
/*  34:    */   {
/*  35: 98 */     super.afterPropertiesSet();
/*  36: 99 */     loadWebLogicTransactionClasses();
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected UserTransaction retrieveUserTransaction()
/*  40:    */     throws TransactionSystemException
/*  41:    */   {
/*  42:104 */     loadWebLogicTransactionHelper();
/*  43:    */     try
/*  44:    */     {
/*  45:106 */       this.logger.debug("Retrieving JTA UserTransaction from WebLogic TransactionHelper");
/*  46:107 */       Method getUserTransactionMethod = this.transactionHelper.getClass().getMethod("getUserTransaction", new Class[0]);
/*  47:108 */       return (UserTransaction)getUserTransactionMethod.invoke(this.transactionHelper, new Object[0]);
/*  48:    */     }
/*  49:    */     catch (InvocationTargetException ex)
/*  50:    */     {
/*  51:111 */       throw new TransactionSystemException(
/*  52:112 */         "WebLogic's TransactionHelper.getUserTransaction() method failed", ex.getTargetException());
/*  53:    */     }
/*  54:    */     catch (Exception ex)
/*  55:    */     {
/*  56:115 */       throw new TransactionSystemException(
/*  57:116 */         "Could not invoke WebLogic's TransactionHelper.getUserTransaction() method", ex);
/*  58:    */     }
/*  59:    */   }
/*  60:    */   
/*  61:    */   protected TransactionManager retrieveTransactionManager()
/*  62:    */     throws TransactionSystemException
/*  63:    */   {
/*  64:122 */     loadWebLogicTransactionHelper();
/*  65:    */     try
/*  66:    */     {
/*  67:124 */       this.logger.debug("Retrieving JTA TransactionManager from WebLogic TransactionHelper");
/*  68:125 */       Method getTransactionManagerMethod = this.transactionHelper.getClass().getMethod("getTransactionManager", new Class[0]);
/*  69:126 */       return (TransactionManager)getTransactionManagerMethod.invoke(this.transactionHelper, new Object[0]);
/*  70:    */     }
/*  71:    */     catch (InvocationTargetException ex)
/*  72:    */     {
/*  73:129 */       throw new TransactionSystemException(
/*  74:130 */         "WebLogic's TransactionHelper.getTransactionManager() method failed", ex.getTargetException());
/*  75:    */     }
/*  76:    */     catch (Exception ex)
/*  77:    */     {
/*  78:133 */       throw new TransactionSystemException(
/*  79:134 */         "Could not invoke WebLogic's TransactionHelper.getTransactionManager() method", ex);
/*  80:    */     }
/*  81:    */   }
/*  82:    */   
/*  83:    */   private void loadWebLogicTransactionHelper()
/*  84:    */     throws TransactionSystemException
/*  85:    */   {
/*  86:140 */     if (this.transactionHelper == null) {
/*  87:    */       try
/*  88:    */       {
/*  89:142 */         Class transactionHelperClass = getClass().getClassLoader().loadClass("weblogic.transaction.TransactionHelper");
/*  90:143 */         Method getTransactionHelperMethod = transactionHelperClass.getMethod("getTransactionHelper", new Class[0]);
/*  91:144 */         this.transactionHelper = getTransactionHelperMethod.invoke(null, new Object[0]);
/*  92:145 */         this.logger.debug("WebLogic TransactionHelper found");
/*  93:    */       }
/*  94:    */       catch (InvocationTargetException ex)
/*  95:    */       {
/*  96:148 */         throw new TransactionSystemException(
/*  97:149 */           "WebLogic's TransactionHelper.getTransactionHelper() method failed", ex.getTargetException());
/*  98:    */       }
/*  99:    */       catch (Exception ex)
/* 100:    */       {
/* 101:152 */         throw new TransactionSystemException(
/* 102:153 */           "Could not initialize WebLogicJtaTransactionManager because WebLogic API classes are not available", 
/* 103:154 */           ex);
/* 104:    */       }
/* 105:    */     }
/* 106:    */   }
/* 107:    */   
/* 108:    */   private void loadWebLogicTransactionClasses()
/* 109:    */     throws TransactionSystemException
/* 110:    */   {
/* 111:    */     try
/* 112:    */     {
/* 113:161 */       Class userTransactionClass = getClass().getClassLoader().loadClass("weblogic.transaction.UserTransaction");
/* 114:162 */       this.weblogicUserTransactionAvailable = userTransactionClass.isInstance(getUserTransaction());
/* 115:163 */       if (this.weblogicUserTransactionAvailable)
/* 116:    */       {
/* 117:164 */         this.beginWithNameMethod = userTransactionClass.getMethod("begin", new Class[] { String.class });
/* 118:165 */         this.beginWithNameAndTimeoutMethod = userTransactionClass.getMethod("begin", new Class[] { String.class, Integer.TYPE });
/* 119:166 */         this.logger.info("Support for WebLogic transaction names available");
/* 120:    */       }
/* 121:    */       else
/* 122:    */       {
/* 123:169 */         this.logger.info("Support for WebLogic transaction names not available");
/* 124:    */       }
/* 125:173 */       Class transactionManagerClass = 
/* 126:174 */         getClass().getClassLoader().loadClass("weblogic.transaction.ClientTransactionManager");
/* 127:175 */       this.logger.debug("WebLogic ClientTransactionManager found");
/* 128:    */       
/* 129:177 */       this.weblogicTransactionManagerAvailable = transactionManagerClass.isInstance(getTransactionManager());
/* 130:178 */       if (this.weblogicTransactionManagerAvailable)
/* 131:    */       {
/* 132:179 */         Class transactionClass = getClass().getClassLoader().loadClass("weblogic.transaction.Transaction");
/* 133:180 */         this.forceResumeMethod = transactionManagerClass.getMethod("forceResume", new Class[] { Transaction.class });
/* 134:181 */         this.setPropertyMethod = transactionClass.getMethod("setProperty", new Class[] { String.class, Serializable.class });
/* 135:182 */         this.logger.debug("Support for WebLogic forceResume available");
/* 136:    */       }
/* 137:    */       else
/* 138:    */       {
/* 139:185 */         this.logger.warn("Support for WebLogic forceResume not available");
/* 140:    */       }
/* 141:    */     }
/* 142:    */     catch (Exception ex)
/* 143:    */     {
/* 144:189 */       throw new TransactionSystemException(
/* 145:190 */         "Could not initialize WebLogicJtaTransactionManager because WebLogic API classes are not available", 
/* 146:191 */         ex);
/* 147:    */     }
/* 148:    */   }
/* 149:    */   
/* 150:    */   protected void doJtaBegin(JtaTransactionObject txObject, TransactionDefinition definition)
/* 151:    */     throws NotSupportedException, SystemException
/* 152:    */   {
/* 153:200 */     int timeout = determineTimeout(definition);
/* 154:203 */     if ((this.weblogicUserTransactionAvailable) && (definition.getName() != null))
/* 155:    */     {
/* 156:    */       try
/* 157:    */       {
/* 158:205 */         if (timeout > -1) {
/* 159:210 */           this.beginWithNameAndTimeoutMethod.invoke(txObject.getUserTransaction(), new Object[] { definition.getName(), Integer.valueOf(timeout) });
/* 160:    */         } else {
/* 161:217 */           this.beginWithNameMethod.invoke(txObject.getUserTransaction(), new Object[] { definition.getName() });
/* 162:    */         }
/* 163:    */       }
/* 164:    */       catch (InvocationTargetException ex)
/* 165:    */       {
/* 166:221 */         throw new TransactionSystemException(
/* 167:222 */           "WebLogic's UserTransaction.begin() method failed", ex.getTargetException());
/* 168:    */       }
/* 169:    */       catch (Exception ex)
/* 170:    */       {
/* 171:225 */         throw new TransactionSystemException(
/* 172:226 */           "Could not invoke WebLogic's UserTransaction.begin() method", ex);
/* 173:    */       }
/* 174:    */     }
/* 175:    */     else
/* 176:    */     {
/* 177:232 */       applyTimeout(txObject, timeout);
/* 178:233 */       txObject.getUserTransaction().begin();
/* 179:    */     }
/* 180:237 */     if (this.weblogicTransactionManagerAvailable)
/* 181:    */     {
/* 182:238 */       if (definition.getIsolationLevel() != -1) {
/* 183:    */         try
/* 184:    */         {
/* 185:240 */           Transaction tx = getTransactionManager().getTransaction();
/* 186:241 */           Integer isolationLevel = Integer.valueOf(definition.getIsolationLevel());
/* 187:    */           
/* 188:    */ 
/* 189:    */ 
/* 190:    */ 
/* 191:246 */           this.setPropertyMethod.invoke(tx, new Object[] { "ISOLATION LEVEL", isolationLevel });
/* 192:    */         }
/* 193:    */         catch (InvocationTargetException ex)
/* 194:    */         {
/* 195:249 */           throw new TransactionSystemException(
/* 196:250 */             "WebLogic's Transaction.setProperty(String, Serializable) method failed", ex.getTargetException());
/* 197:    */         }
/* 198:    */         catch (Exception ex)
/* 199:    */         {
/* 200:253 */           throw new TransactionSystemException(
/* 201:254 */             "Could not invoke WebLogic's Transaction.setProperty(String, Serializable) method", ex);
/* 202:    */         }
/* 203:    */       }
/* 204:    */     }
/* 205:    */     else {
/* 206:259 */       applyIsolationLevel(txObject, definition.getIsolationLevel());
/* 207:    */     }
/* 208:    */   }
/* 209:    */   
/* 210:    */   protected void doJtaResume(JtaTransactionObject txObject, Object suspendedTransaction)
/* 211:    */     throws InvalidTransactionException, SystemException
/* 212:    */   {
/* 213:    */     try
/* 214:    */     {
/* 215:268 */       getTransactionManager().resume((Transaction)suspendedTransaction);
/* 216:    */     }
/* 217:    */     catch (InvalidTransactionException ex)
/* 218:    */     {
/* 219:271 */       if (!this.weblogicTransactionManagerAvailable) {
/* 220:272 */         throw ex;
/* 221:    */       }
/* 222:275 */       if (this.logger.isDebugEnabled()) {
/* 223:276 */         this.logger.debug("Standard JTA resume threw InvalidTransactionException: " + ex.getMessage() + 
/* 224:277 */           " - trying WebLogic JTA forceResume");
/* 225:    */       }
/* 226:    */       try
/* 227:    */       {
/* 228:285 */         this.forceResumeMethod.invoke(getTransactionManager(), new Object[] { suspendedTransaction });
/* 229:    */       }
/* 230:    */       catch (InvocationTargetException ex2)
/* 231:    */       {
/* 232:288 */         throw new TransactionSystemException(
/* 233:289 */           "WebLogic's TransactionManager.forceResume(Transaction) method failed", ex2.getTargetException());
/* 234:    */       }
/* 235:    */       catch (Exception ex2)
/* 236:    */       {
/* 237:292 */         throw new TransactionSystemException(
/* 238:293 */           "Could not access WebLogic's TransactionManager.forceResume(Transaction) method", ex2);
/* 239:    */       }
/* 240:    */     }
/* 241:    */   }
/* 242:    */   
/* 243:    */   public Transaction createTransaction(String name, int timeout)
/* 244:    */     throws NotSupportedException, SystemException
/* 245:    */   {
/* 246:301 */     if ((this.weblogicUserTransactionAvailable) && (name != null))
/* 247:    */     {
/* 248:    */       try
/* 249:    */       {
/* 250:303 */         if (timeout >= 0) {
/* 251:304 */           this.beginWithNameAndTimeoutMethod.invoke(getUserTransaction(), new Object[] { name, Integer.valueOf(timeout) });
/* 252:    */         } else {
/* 253:307 */           this.beginWithNameMethod.invoke(getUserTransaction(), new Object[] { name });
/* 254:    */         }
/* 255:    */       }
/* 256:    */       catch (InvocationTargetException ex)
/* 257:    */       {
/* 258:311 */         if ((ex.getTargetException() instanceof NotSupportedException)) {
/* 259:312 */           throw ((NotSupportedException)ex.getTargetException());
/* 260:    */         }
/* 261:314 */         if ((ex.getTargetException() instanceof SystemException)) {
/* 262:315 */           throw ((SystemException)ex.getTargetException());
/* 263:    */         }
/* 264:317 */         if ((ex.getTargetException() instanceof RuntimeException)) {
/* 265:318 */           throw ((RuntimeException)ex.getTargetException());
/* 266:    */         }
/* 267:321 */         throw new SystemException(
/* 268:322 */           "WebLogic's begin() method failed with an unexpected error: " + ex.getTargetException());
/* 269:    */       }
/* 270:    */       catch (Exception ex)
/* 271:    */       {
/* 272:326 */         throw new SystemException("Could not invoke WebLogic's UserTransaction.begin() method: " + ex);
/* 273:    */       }
/* 274:328 */       return new ManagedTransactionAdapter(getTransactionManager());
/* 275:    */     }
/* 276:333 */     return super.createTransaction(name, timeout);
/* 277:    */   }
/* 278:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.jta.WebLogicJtaTransactionManager
 * JD-Core Version:    0.7.0.1
 */