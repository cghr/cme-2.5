/*   1:    */ package org.springframework.jca.cci.connection;
/*   2:    */ 
/*   3:    */ import javax.resource.ResourceException;
/*   4:    */ import javax.resource.cci.Connection;
/*   5:    */ import javax.resource.cci.ConnectionFactory;
/*   6:    */ import javax.resource.cci.ConnectionSpec;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.apache.commons.logging.LogFactory;
/*   9:    */ import org.springframework.jca.cci.CannotGetCciConnectionException;
/*  10:    */ import org.springframework.transaction.support.ResourceHolderSynchronization;
/*  11:    */ import org.springframework.transaction.support.TransactionSynchronizationManager;
/*  12:    */ import org.springframework.util.Assert;
/*  13:    */ 
/*  14:    */ public abstract class ConnectionFactoryUtils
/*  15:    */ {
/*  16: 54 */   private static final Log logger = LogFactory.getLog(ConnectionFactoryUtils.class);
/*  17:    */   
/*  18:    */   public static Connection getConnection(ConnectionFactory cf)
/*  19:    */     throws CannotGetCciConnectionException
/*  20:    */   {
/*  21: 71 */     return getConnection(cf, null);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public static Connection getConnection(ConnectionFactory cf, ConnectionSpec spec)
/*  25:    */     throws CannotGetCciConnectionException
/*  26:    */   {
/*  27:    */     try
/*  28:    */     {
/*  29: 93 */       if (spec != null)
/*  30:    */       {
/*  31: 94 */         Assert.notNull(cf, "No ConnectionFactory specified");
/*  32: 95 */         return cf.getConnection(spec);
/*  33:    */       }
/*  34: 98 */       return doGetConnection(cf);
/*  35:    */     }
/*  36:    */     catch (ResourceException ex)
/*  37:    */     {
/*  38:102 */       throw new CannotGetCciConnectionException("Could not get CCI Connection", ex);
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   public static Connection doGetConnection(ConnectionFactory cf)
/*  43:    */     throws ResourceException
/*  44:    */   {
/*  45:119 */     Assert.notNull(cf, "No ConnectionFactory specified");
/*  46:    */     
/*  47:121 */     ConnectionHolder conHolder = (ConnectionHolder)TransactionSynchronizationManager.getResource(cf);
/*  48:122 */     if (conHolder != null) {
/*  49:123 */       return conHolder.getConnection();
/*  50:    */     }
/*  51:126 */     logger.debug("Opening CCI Connection");
/*  52:127 */     Connection con = cf.getConnection();
/*  53:129 */     if (TransactionSynchronizationManager.isSynchronizationActive())
/*  54:    */     {
/*  55:130 */       logger.debug("Registering transaction synchronization for CCI Connection");
/*  56:131 */       conHolder = new ConnectionHolder(con);
/*  57:132 */       conHolder.setSynchronizedWithTransaction(true);
/*  58:133 */       TransactionSynchronizationManager.registerSynchronization(new ConnectionSynchronization(conHolder, cf));
/*  59:134 */       TransactionSynchronizationManager.bindResource(cf, conHolder);
/*  60:    */     }
/*  61:137 */     return con;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public static boolean isConnectionTransactional(Connection con, ConnectionFactory cf)
/*  65:    */   {
/*  66:149 */     if (cf == null) {
/*  67:150 */       return false;
/*  68:    */     }
/*  69:152 */     ConnectionHolder conHolder = (ConnectionHolder)TransactionSynchronizationManager.getResource(cf);
/*  70:153 */     return (conHolder != null) && (conHolder.getConnection() == con);
/*  71:    */   }
/*  72:    */   
/*  73:    */   public static void releaseConnection(Connection con, ConnectionFactory cf)
/*  74:    */   {
/*  75:    */     try
/*  76:    */     {
/*  77:167 */       doReleaseConnection(con, cf);
/*  78:    */     }
/*  79:    */     catch (ResourceException ex)
/*  80:    */     {
/*  81:170 */       logger.debug("Could not close CCI Connection", ex);
/*  82:    */     }
/*  83:    */     catch (Throwable ex)
/*  84:    */     {
/*  85:174 */       logger.debug("Unexpected exception on closing CCI Connection", ex);
/*  86:    */     }
/*  87:    */   }
/*  88:    */   
/*  89:    */   public static void doReleaseConnection(Connection con, ConnectionFactory cf)
/*  90:    */     throws ResourceException
/*  91:    */   {
/*  92:190 */     if ((con == null) || (isConnectionTransactional(con, cf))) {
/*  93:191 */       return;
/*  94:    */     }
/*  95:193 */     con.close();
/*  96:    */   }
/*  97:    */   
/*  98:    */   private static class ConnectionSynchronization
/*  99:    */     extends ResourceHolderSynchronization<ConnectionHolder, ConnectionFactory>
/* 100:    */   {
/* 101:    */     public ConnectionSynchronization(ConnectionHolder connectionHolder, ConnectionFactory connectionFactory)
/* 102:    */     {
/* 103:205 */       super(connectionFactory);
/* 104:    */     }
/* 105:    */     
/* 106:    */     protected void releaseResource(ConnectionHolder resourceHolder, ConnectionFactory resourceKey)
/* 107:    */     {
/* 108:210 */       ConnectionFactoryUtils.releaseConnection(resourceHolder.getConnection(), resourceKey);
/* 109:    */     }
/* 110:    */   }
/* 111:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.cci.connection.ConnectionFactoryUtils
 * JD-Core Version:    0.7.0.1
 */