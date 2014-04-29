/*   1:    */ package org.springframework.jdbc.datasource;
/*   2:    */ 
/*   3:    */ import java.sql.Connection;
/*   4:    */ import java.sql.SQLException;
/*   5:    */ import java.util.Set;
/*   6:    */ import org.springframework.core.Constants;
/*   7:    */ import org.springframework.transaction.TransactionDefinition;
/*   8:    */ import org.springframework.transaction.support.TransactionSynchronizationManager;
/*   9:    */ 
/*  10:    */ public class IsolationLevelDataSourceAdapter
/*  11:    */   extends UserCredentialsDataSourceAdapter
/*  12:    */ {
/*  13: 58 */   private static final Constants constants = new Constants(TransactionDefinition.class);
/*  14:    */   private Integer isolationLevel;
/*  15:    */   
/*  16:    */   public final void setIsolationLevelName(String constantName)
/*  17:    */     throws IllegalArgumentException
/*  18:    */   {
/*  19: 78 */     if ((constantName == null) || (!constantName.startsWith("ISOLATION_"))) {
/*  20: 79 */       throw new IllegalArgumentException("Only isolation constants allowed");
/*  21:    */     }
/*  22: 81 */     setIsolationLevel(constants.asNumber(constantName).intValue());
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void setIsolationLevel(int isolationLevel)
/*  26:    */   {
/*  27:104 */     if (!constants.getValues("ISOLATION_").contains(Integer.valueOf(isolationLevel))) {
/*  28:105 */       throw new IllegalArgumentException("Only values of isolation constants allowed");
/*  29:    */     }
/*  30:107 */     this.isolationLevel = (isolationLevel != -1 ? Integer.valueOf(isolationLevel) : null);
/*  31:    */   }
/*  32:    */   
/*  33:    */   protected Integer getIsolationLevel()
/*  34:    */   {
/*  35:115 */     return this.isolationLevel;
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected Connection doGetConnection(String username, String password)
/*  39:    */     throws SQLException
/*  40:    */   {
/*  41:127 */     Connection con = super.doGetConnection(username, password);
/*  42:128 */     Boolean readOnlyToUse = getCurrentReadOnlyFlag();
/*  43:129 */     if (readOnlyToUse != null) {
/*  44:130 */       con.setReadOnly(readOnlyToUse.booleanValue());
/*  45:    */     }
/*  46:132 */     Integer isolationLevelToUse = getCurrentIsolationLevel();
/*  47:133 */     if (isolationLevelToUse != null) {
/*  48:134 */       con.setTransactionIsolation(isolationLevelToUse.intValue());
/*  49:    */     }
/*  50:136 */     return con;
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected Integer getCurrentIsolationLevel()
/*  54:    */   {
/*  55:147 */     Integer isolationLevelToUse = TransactionSynchronizationManager.getCurrentTransactionIsolationLevel();
/*  56:148 */     if (isolationLevelToUse == null) {
/*  57:149 */       isolationLevelToUse = getIsolationLevel();
/*  58:    */     }
/*  59:151 */     return isolationLevelToUse;
/*  60:    */   }
/*  61:    */   
/*  62:    */   protected Boolean getCurrentReadOnlyFlag()
/*  63:    */   {
/*  64:161 */     boolean txReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
/*  65:162 */     return txReadOnly ? Boolean.TRUE : null;
/*  66:    */   }
/*  67:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.IsolationLevelDataSourceAdapter
 * JD-Core Version:    0.7.0.1
 */