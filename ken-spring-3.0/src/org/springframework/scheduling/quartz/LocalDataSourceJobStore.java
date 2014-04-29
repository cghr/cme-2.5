/*   1:    */ package org.springframework.scheduling.quartz;
/*   2:    */ 
/*   3:    */ import java.sql.Connection;
/*   4:    */ import java.sql.SQLException;
/*   5:    */ import javax.sql.DataSource;
/*   6:    */ import org.quartz.SchedulerConfigException;
/*   7:    */ import org.quartz.impl.jdbcjobstore.JobStoreCMT;
/*   8:    */ import org.quartz.impl.jdbcjobstore.SimpleSemaphore;
/*   9:    */ import org.quartz.spi.ClassLoadHelper;
/*  10:    */ import org.quartz.spi.SchedulerSignaler;
/*  11:    */ import org.quartz.utils.ConnectionProvider;
/*  12:    */ import org.quartz.utils.DBConnectionManager;
/*  13:    */ import org.springframework.jdbc.datasource.DataSourceUtils;
/*  14:    */ import org.springframework.jdbc.support.JdbcUtils;
/*  15:    */ import org.springframework.jdbc.support.MetaDataAccessException;
/*  16:    */ 
/*  17:    */ public class LocalDataSourceJobStore
/*  18:    */   extends JobStoreCMT
/*  19:    */ {
/*  20:    */   public static final String TX_DATA_SOURCE_PREFIX = "springTxDataSource.";
/*  21:    */   public static final String NON_TX_DATA_SOURCE_PREFIX = "springNonTxDataSource.";
/*  22:    */   private DataSource dataSource;
/*  23:    */   
/*  24:    */   public void initialize(ClassLoadHelper loadHelper, SchedulerSignaler signaler)
/*  25:    */     throws SchedulerConfigException
/*  26:    */   {
/*  27: 89 */     this.dataSource = SchedulerFactoryBean.getConfigTimeDataSource();
/*  28: 90 */     if (this.dataSource == null) {
/*  29: 91 */       throw new SchedulerConfigException(
/*  30: 92 */         "No local DataSource found for configuration - 'dataSource' property must be set on SchedulerFactoryBean");
/*  31:    */     }
/*  32: 97 */     setDataSource("springTxDataSource." + getInstanceName());
/*  33: 98 */     setDontSetAutoCommitFalse(true);
/*  34:    */     
/*  35:    */ 
/*  36:101 */     DBConnectionManager.getInstance().addConnectionProvider(
/*  37:102 */       "springTxDataSource." + getInstanceName(), 
/*  38:103 */       new ConnectionProvider()
/*  39:    */       {
/*  40:    */         public Connection getConnection()
/*  41:    */           throws SQLException
/*  42:    */         {
/*  43:106 */           return DataSourceUtils.doGetConnection(LocalDataSourceJobStore.this.dataSource);
/*  44:    */         }
/*  45:    */         
/*  46:    */         public void shutdown() {}
/*  47:115 */       });
/*  48:116 */     DataSource nonTxDataSource = SchedulerFactoryBean.getConfigTimeNonTransactionalDataSource();
/*  49:117 */     final DataSource nonTxDataSourceToUse = 
/*  50:118 */       nonTxDataSource != null ? nonTxDataSource : this.dataSource;
/*  51:    */     
/*  52:    */ 
/*  53:121 */     setNonManagedTXDataSource("springNonTxDataSource." + getInstanceName());
/*  54:    */     
/*  55:    */ 
/*  56:124 */     DBConnectionManager.getInstance().addConnectionProvider(
/*  57:125 */       "springNonTxDataSource." + getInstanceName(), 
/*  58:126 */       new ConnectionProvider()
/*  59:    */       {
/*  60:    */         public Connection getConnection()
/*  61:    */           throws SQLException
/*  62:    */         {
/*  63:129 */           return nonTxDataSourceToUse.getConnection();
/*  64:    */         }
/*  65:    */         
/*  66:    */         public void shutdown() {}
/*  67:    */       });
/*  68:    */     try
/*  69:    */     {
/*  70:139 */       String productName = JdbcUtils.extractDatabaseMetaData(this.dataSource, 
/*  71:140 */         "getDatabaseProductName").toString();
/*  72:141 */       productName = JdbcUtils.commonDatabaseName(productName);
/*  73:142 */       if ((productName != null) && 
/*  74:143 */         (productName.toLowerCase().contains("hsql")))
/*  75:    */       {
/*  76:144 */         setUseDBLocks(false);
/*  77:145 */         setLockHandler(new SimpleSemaphore());
/*  78:    */       }
/*  79:    */     }
/*  80:    */     catch (MetaDataAccessException localMetaDataAccessException)
/*  81:    */     {
/*  82:148 */       logWarnIfNonZero(1, "Could not detect database type.  Assuming locks can be taken.");
/*  83:    */     }
/*  84:151 */     super.initialize(loadHelper, signaler);
/*  85:    */   }
/*  86:    */   
/*  87:    */   protected void closeConnection(Connection con)
/*  88:    */   {
/*  89:158 */     DataSourceUtils.releaseConnection(con, this.dataSource);
/*  90:    */   }
/*  91:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.quartz.LocalDataSourceJobStore
 * JD-Core Version:    0.7.0.1
 */