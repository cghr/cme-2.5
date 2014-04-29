/*   1:    */ package org.springframework.jdbc.datasource;
/*   2:    */ 
/*   3:    */ import java.sql.Connection;
/*   4:    */ import java.sql.DatabaseMetaData;
/*   5:    */ import java.sql.SQLException;
/*   6:    */ import java.sql.Savepoint;
/*   7:    */ import org.springframework.transaction.support.ResourceHolderSupport;
/*   8:    */ import org.springframework.util.Assert;
/*   9:    */ 
/*  10:    */ public class ConnectionHolder
/*  11:    */   extends ResourceHolderSupport
/*  12:    */ {
/*  13:    */   public static final String SAVEPOINT_NAME_PREFIX = "SAVEPOINT_";
/*  14:    */   private ConnectionHandle connectionHandle;
/*  15:    */   private Connection currentConnection;
/*  16: 50 */   private boolean transactionActive = false;
/*  17:    */   private Boolean savepointsSupported;
/*  18: 54 */   private int savepointCounter = 0;
/*  19:    */   
/*  20:    */   public ConnectionHolder(ConnectionHandle connectionHandle)
/*  21:    */   {
/*  22: 62 */     Assert.notNull(connectionHandle, "ConnectionHandle must not be null");
/*  23: 63 */     this.connectionHandle = connectionHandle;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public ConnectionHolder(Connection connection)
/*  27:    */   {
/*  28: 75 */     this.connectionHandle = new SimpleConnectionHandle(connection);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public ConnectionHolder(Connection connection, boolean transactionActive)
/*  32:    */   {
/*  33: 87 */     this(connection);
/*  34: 88 */     this.transactionActive = transactionActive;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public ConnectionHandle getConnectionHandle()
/*  38:    */   {
/*  39: 96 */     return this.connectionHandle;
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected boolean hasConnection()
/*  43:    */   {
/*  44:103 */     return this.connectionHandle != null;
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected void setTransactionActive(boolean transactionActive)
/*  48:    */   {
/*  49:111 */     this.transactionActive = transactionActive;
/*  50:    */   }
/*  51:    */   
/*  52:    */   protected boolean isTransactionActive()
/*  53:    */   {
/*  54:118 */     return this.transactionActive;
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected void setConnection(Connection connection)
/*  58:    */   {
/*  59:129 */     if (this.currentConnection != null)
/*  60:    */     {
/*  61:130 */       this.connectionHandle.releaseConnection(this.currentConnection);
/*  62:131 */       this.currentConnection = null;
/*  63:    */     }
/*  64:133 */     if (connection != null) {
/*  65:134 */       this.connectionHandle = new SimpleConnectionHandle(connection);
/*  66:    */     } else {
/*  67:137 */       this.connectionHandle = null;
/*  68:    */     }
/*  69:    */   }
/*  70:    */   
/*  71:    */   public Connection getConnection()
/*  72:    */   {
/*  73:150 */     Assert.notNull(this.connectionHandle, "Active Connection is required");
/*  74:151 */     if (this.currentConnection == null) {
/*  75:152 */       this.currentConnection = this.connectionHandle.getConnection();
/*  76:    */     }
/*  77:154 */     return this.currentConnection;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public boolean supportsSavepoints()
/*  81:    */     throws SQLException
/*  82:    */   {
/*  83:163 */     if (this.savepointsSupported == null) {
/*  84:164 */       this.savepointsSupported = new Boolean(getConnection().getMetaData().supportsSavepoints());
/*  85:    */     }
/*  86:166 */     return this.savepointsSupported.booleanValue();
/*  87:    */   }
/*  88:    */   
/*  89:    */   public Savepoint createSavepoint()
/*  90:    */     throws SQLException
/*  91:    */   {
/*  92:176 */     this.savepointCounter += 1;
/*  93:177 */     return getConnection().setSavepoint("SAVEPOINT_" + this.savepointCounter);
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void released()
/*  97:    */   {
/*  98:191 */     super.released();
/*  99:192 */     if ((!isOpen()) && (this.currentConnection != null))
/* 100:    */     {
/* 101:193 */       this.connectionHandle.releaseConnection(this.currentConnection);
/* 102:194 */       this.currentConnection = null;
/* 103:    */     }
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void clear()
/* 107:    */   {
/* 108:201 */     super.clear();
/* 109:202 */     this.transactionActive = false;
/* 110:203 */     this.savepointsSupported = null;
/* 111:204 */     this.savepointCounter = 0;
/* 112:    */   }
/* 113:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.ConnectionHolder
 * JD-Core Version:    0.7.0.1
 */