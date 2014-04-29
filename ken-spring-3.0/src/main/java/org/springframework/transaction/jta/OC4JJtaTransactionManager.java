/*   1:    */ package org.springframework.transaction.jta;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.InvocationTargetException;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import javax.transaction.NotSupportedException;
/*   6:    */ import javax.transaction.SystemException;
/*   7:    */ import javax.transaction.Transaction;
/*   8:    */ import javax.transaction.TransactionManager;
/*   9:    */ import javax.transaction.UserTransaction;
/*  10:    */ import org.apache.commons.logging.Log;
/*  11:    */ import org.springframework.transaction.TransactionDefinition;
/*  12:    */ import org.springframework.transaction.TransactionSystemException;
/*  13:    */ import org.springframework.util.ClassUtils;
/*  14:    */ 
/*  15:    */ public class OC4JJtaTransactionManager
/*  16:    */   extends JtaTransactionManager
/*  17:    */ {
/*  18:    */   private static final String TRANSACTION_UTILITY_CLASS_NAME = "oracle.j2ee.transaction.TransactionUtility";
/*  19:    */   private static final String TRANSACTION_MANAGER_CLASS_NAME = "oracle.j2ee.transaction.OC4JTransactionManager";
/*  20:    */   private static final String TRANSACTION_CLASS_NAME = "oracle.j2ee.transaction.OC4JTransaction";
/*  21:    */   private static final String FALLBACK_TRANSACTION_MANAGER_CLASS_NAME = "com.evermind.server.ApplicationServerTransactionManager";
/*  22:    */   private static final String FALLBACK_TRANSACTION_CLASS_NAME = "com.evermind.server.ApplicationServerTransaction";
/*  23:    */   private Method beginWithNameMethod;
/*  24:    */   private Method setTransactionIsolationMethod;
/*  25:    */   
/*  26:    */   public void afterPropertiesSet()
/*  27:    */     throws TransactionSystemException
/*  28:    */   {
/*  29: 93 */     super.afterPropertiesSet();
/*  30: 94 */     loadOC4JTransactionClasses();
/*  31:    */   }
/*  32:    */   
/*  33:    */   protected UserTransaction retrieveUserTransaction()
/*  34:    */     throws TransactionSystemException
/*  35:    */   {
/*  36:    */     try
/*  37:    */     {
/*  38:100 */       Class transactionUtilityClass = getClass().getClassLoader().loadClass("oracle.j2ee.transaction.TransactionUtility");
/*  39:101 */       Method getInstanceMethod = transactionUtilityClass.getMethod("getInstance", new Class[0]);
/*  40:102 */       Object transactionUtility = getInstanceMethod.invoke(null, new Object[0]);
/*  41:103 */       this.logger.debug("Retrieving JTA UserTransaction from OC4J TransactionUtility");
/*  42:104 */       Method getUserTransactionMethod = 
/*  43:105 */         transactionUtility.getClass().getMethod("getOC4JUserTransaction", new Class[0]);
/*  44:106 */       return (UserTransaction)getUserTransactionMethod.invoke(transactionUtility, new Object[0]);
/*  45:    */     }
/*  46:    */     catch (ClassNotFoundException ex)
/*  47:    */     {
/*  48:109 */       this.logger.debug("Could not find OC4J 10.1.3.2 TransactionUtility: " + ex);
/*  49:    */       
/*  50:    */ 
/*  51:112 */       return null;
/*  52:    */     }
/*  53:    */     catch (InvocationTargetException ex)
/*  54:    */     {
/*  55:115 */       throw new TransactionSystemException(
/*  56:116 */         "OC4J's TransactionUtility.getOC4JUserTransaction() method failed", ex.getTargetException());
/*  57:    */     }
/*  58:    */     catch (Exception ex)
/*  59:    */     {
/*  60:119 */       throw new TransactionSystemException(
/*  61:120 */         "Could not invoke OC4J's TransactionUtility.getOC4JUserTransaction() method", ex);
/*  62:    */     }
/*  63:    */   }
/*  64:    */   
/*  65:    */   private void loadOC4JTransactionClasses()
/*  66:    */     throws TransactionSystemException
/*  67:    */   {
/*  68:126 */     Class transactionManagerClass = null;
/*  69:127 */     Class transactionClass = null;
/*  70:    */     try
/*  71:    */     {
/*  72:129 */       transactionManagerClass = getClass().getClassLoader().loadClass("oracle.j2ee.transaction.OC4JTransactionManager");
/*  73:130 */       transactionClass = getClass().getClassLoader().loadClass("oracle.j2ee.transaction.OC4JTransaction");
/*  74:    */     }
/*  75:    */     catch (ClassNotFoundException ex)
/*  76:    */     {
/*  77:    */       try
/*  78:    */       {
/*  79:134 */         transactionManagerClass = getClass().getClassLoader().loadClass("com.evermind.server.ApplicationServerTransactionManager");
/*  80:135 */         transactionClass = getClass().getClassLoader().loadClass("com.evermind.server.ApplicationServerTransaction");
/*  81:    */       }
/*  82:    */       catch (ClassNotFoundException localClassNotFoundException1)
/*  83:    */       {
/*  84:138 */         throw new TransactionSystemException(
/*  85:139 */           "Could not initialize OC4JJtaTransactionManager because OC4J API classes are not available", ex);
/*  86:    */       }
/*  87:    */     }
/*  88:144 */     if (transactionManagerClass.isInstance(getUserTransaction()))
/*  89:    */     {
/*  90:145 */       this.beginWithNameMethod = ClassUtils.getMethodIfAvailable(
/*  91:146 */         transactionManagerClass, "begin", new Class[] { String.class });
/*  92:147 */       this.setTransactionIsolationMethod = ClassUtils.getMethodIfAvailable(
/*  93:148 */         transactionClass, "setTransactionIsolation", new Class[] { Integer.TYPE });
/*  94:149 */       this.logger.info("Support for OC4J transaction names and isolation levels available");
/*  95:    */     }
/*  96:    */     else
/*  97:    */     {
/*  98:152 */       this.logger.info("Support for OC4J transaction names and isolation levels not available");
/*  99:    */     }
/* 100:    */   }
/* 101:    */   
/* 102:    */   protected void doJtaBegin(JtaTransactionObject txObject, TransactionDefinition definition)
/* 103:    */     throws NotSupportedException, SystemException
/* 104:    */   {
/* 105:161 */     int timeout = determineTimeout(definition);
/* 106:162 */     applyTimeout(txObject, timeout);
/* 107:165 */     if ((this.beginWithNameMethod != null) && (definition.getName() != null)) {
/* 108:    */       try
/* 109:    */       {
/* 110:171 */         this.beginWithNameMethod.invoke(txObject.getUserTransaction(), new Object[] { definition.getName() });
/* 111:    */       }
/* 112:    */       catch (InvocationTargetException ex)
/* 113:    */       {
/* 114:174 */         throw new TransactionSystemException(
/* 115:175 */           "OC4J's UserTransaction.begin(String) method failed", ex.getTargetException());
/* 116:    */       }
/* 117:    */       catch (Exception ex)
/* 118:    */       {
/* 119:178 */         throw new TransactionSystemException(
/* 120:179 */           "Could not invoke OC4J's UserTransaction.begin(String) method", ex);
/* 121:    */       }
/* 122:    */     } else {
/* 123:185 */       txObject.getUserTransaction().begin();
/* 124:    */     }
/* 125:189 */     if (this.setTransactionIsolationMethod != null)
/* 126:    */     {
/* 127:190 */       if (definition.getIsolationLevel() != -1) {
/* 128:    */         try
/* 129:    */         {
/* 130:192 */           Transaction tx = getTransactionManager().getTransaction();
/* 131:    */           
/* 132:    */ 
/* 133:    */ 
/* 134:    */ 
/* 135:197 */           this.setTransactionIsolationMethod.invoke(tx, new Object[] { Integer.valueOf(definition.getIsolationLevel()) });
/* 136:    */         }
/* 137:    */         catch (InvocationTargetException ex)
/* 138:    */         {
/* 139:200 */           throw new TransactionSystemException(
/* 140:201 */             "OC4J's Transaction.setTransactionIsolation(int) method failed", ex.getTargetException());
/* 141:    */         }
/* 142:    */         catch (Exception ex)
/* 143:    */         {
/* 144:204 */           throw new TransactionSystemException(
/* 145:205 */             "Could not invoke OC4J's Transaction.setTransactionIsolation(int) method", ex);
/* 146:    */         }
/* 147:    */       }
/* 148:    */     }
/* 149:    */     else {
/* 150:210 */       applyIsolationLevel(txObject, definition.getIsolationLevel());
/* 151:    */     }
/* 152:    */   }
/* 153:    */   
/* 154:    */   public Transaction createTransaction(String name, int timeout)
/* 155:    */     throws NotSupportedException, SystemException
/* 156:    */   {
/* 157:217 */     if ((this.beginWithNameMethod != null) && (name != null))
/* 158:    */     {
/* 159:218 */       UserTransaction ut = getUserTransaction();
/* 160:219 */       if (timeout >= 0) {
/* 161:220 */         ut.setTransactionTimeout(timeout);
/* 162:    */       }
/* 163:    */       try
/* 164:    */       {
/* 165:223 */         this.beginWithNameMethod.invoke(ut, new Object[] { name });
/* 166:    */       }
/* 167:    */       catch (InvocationTargetException ex)
/* 168:    */       {
/* 169:226 */         if ((ex.getTargetException() instanceof NotSupportedException)) {
/* 170:227 */           throw ((NotSupportedException)ex.getTargetException());
/* 171:    */         }
/* 172:229 */         if ((ex.getTargetException() instanceof SystemException)) {
/* 173:230 */           throw ((SystemException)ex.getTargetException());
/* 174:    */         }
/* 175:232 */         if ((ex.getTargetException() instanceof RuntimeException)) {
/* 176:233 */           throw ((RuntimeException)ex.getTargetException());
/* 177:    */         }
/* 178:236 */         throw new SystemException(
/* 179:237 */           "OC4J's begin(String) method failed with an unexpected error: " + ex.getTargetException());
/* 180:    */       }
/* 181:    */       catch (Exception ex)
/* 182:    */       {
/* 183:241 */         throw new SystemException("Could not invoke OC4J's UserTransaction.begin(String) method: " + ex);
/* 184:    */       }
/* 185:243 */       return new ManagedTransactionAdapter(getTransactionManager());
/* 186:    */     }
/* 187:248 */     return super.createTransaction(name, timeout);
/* 188:    */   }
/* 189:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.jta.OC4JJtaTransactionManager
 * JD-Core Version:    0.7.0.1
 */