/*   1:    */ package org.springframework.jdbc.datasource.lookup;
/*   2:    */ 
/*   3:    */ import org.springframework.core.Constants;
/*   4:    */ import org.springframework.transaction.TransactionDefinition;
/*   5:    */ import org.springframework.transaction.support.TransactionSynchronizationManager;
/*   6:    */ 
/*   7:    */ public class IsolationLevelDataSourceRouter
/*   8:    */   extends AbstractRoutingDataSource
/*   9:    */ {
/*  10: 95 */   private static final Constants constants = new Constants(TransactionDefinition.class);
/*  11:    */   
/*  12:    */   protected Object resolveSpecifiedLookupKey(Object lookupKey)
/*  13:    */   {
/*  14:105 */     if ((lookupKey instanceof Integer)) {
/*  15:106 */       return lookupKey;
/*  16:    */     }
/*  17:108 */     if ((lookupKey instanceof String))
/*  18:    */     {
/*  19:109 */       String constantName = (String)lookupKey;
/*  20:110 */       if (!constantName.startsWith("ISOLATION_")) {
/*  21:111 */         throw new IllegalArgumentException("Only isolation constants allowed");
/*  22:    */       }
/*  23:113 */       return constants.asNumber(constantName);
/*  24:    */     }
/*  25:116 */     throw new IllegalArgumentException(
/*  26:117 */       "Invalid lookup key - needs to be isolation level Integer or isolation level name String: " + lookupKey);
/*  27:    */   }
/*  28:    */   
/*  29:    */   protected Object determineCurrentLookupKey()
/*  30:    */   {
/*  31:123 */     return TransactionSynchronizationManager.getCurrentTransactionIsolationLevel();
/*  32:    */   }
/*  33:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.lookup.IsolationLevelDataSourceRouter
 * JD-Core Version:    0.7.0.1
 */