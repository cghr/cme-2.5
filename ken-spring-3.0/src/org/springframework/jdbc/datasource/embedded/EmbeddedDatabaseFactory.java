/*   1:    */ package org.springframework.jdbc.datasource.embedded;
/*   2:    */ 
/*   3:    */ import java.io.PrintWriter;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.SQLException;
/*   6:    */ import java.util.logging.Logger;
/*   7:    */ import javax.sql.DataSource;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.apache.commons.logging.LogFactory;
/*  10:    */ import org.springframework.jdbc.datasource.init.DatabasePopulator;
/*  11:    */ import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
/*  12:    */ import org.springframework.util.Assert;
/*  13:    */ 
/*  14:    */ public class EmbeddedDatabaseFactory
/*  15:    */ {
/*  16: 50 */   private static Log logger = LogFactory.getLog(EmbeddedDatabaseFactory.class);
/*  17: 52 */   private String databaseName = "testdb";
/*  18: 54 */   private DataSourceFactory dataSourceFactory = new SimpleDriverDataSourceFactory();
/*  19:    */   private EmbeddedDatabaseConfigurer databaseConfigurer;
/*  20:    */   private DatabasePopulator databasePopulator;
/*  21:    */   private DataSource dataSource;
/*  22:    */   
/*  23:    */   public void setDatabaseName(String databaseName)
/*  24:    */   {
/*  25: 68 */     Assert.notNull(databaseName, "Database name is required");
/*  26: 69 */     this.databaseName = databaseName;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setDataSourceFactory(DataSourceFactory dataSourceFactory)
/*  30:    */   {
/*  31: 77 */     Assert.notNull(dataSourceFactory, "DataSourceFactory is required");
/*  32: 78 */     this.dataSourceFactory = dataSourceFactory;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setDatabaseType(EmbeddedDatabaseType type)
/*  36:    */   {
/*  37: 87 */     this.databaseConfigurer = EmbeddedDatabaseConfigurerFactory.getConfigurer(type);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setDatabaseConfigurer(EmbeddedDatabaseConfigurer configurer)
/*  41:    */   {
/*  42: 95 */     this.databaseConfigurer = configurer;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setDatabasePopulator(DatabasePopulator populator)
/*  46:    */   {
/*  47:103 */     this.databasePopulator = populator;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public EmbeddedDatabase getDatabase()
/*  51:    */   {
/*  52:110 */     if (this.dataSource == null) {
/*  53:111 */       initDatabase();
/*  54:    */     }
/*  55:113 */     return new EmbeddedDataSourceProxy(this.dataSource);
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected void initDatabase()
/*  59:    */   {
/*  60:123 */     if (logger.isInfoEnabled()) {
/*  61:124 */       logger.info("Creating embedded database '" + this.databaseName + "'");
/*  62:    */     }
/*  63:126 */     if (this.databaseConfigurer == null) {
/*  64:127 */       this.databaseConfigurer = EmbeddedDatabaseConfigurerFactory.getConfigurer(EmbeddedDatabaseType.HSQL);
/*  65:    */     }
/*  66:129 */     this.databaseConfigurer.configureConnectionProperties(
/*  67:130 */       this.dataSourceFactory.getConnectionProperties(), this.databaseName);
/*  68:131 */     this.dataSource = this.dataSourceFactory.getDataSource();
/*  69:134 */     if (this.databasePopulator != null) {
/*  70:    */       try
/*  71:    */       {
/*  72:136 */         DatabasePopulatorUtils.execute(this.databasePopulator, this.dataSource);
/*  73:    */       }
/*  74:    */       catch (RuntimeException ex)
/*  75:    */       {
/*  76:140 */         shutdownDatabase();
/*  77:141 */         throw ex;
/*  78:    */       }
/*  79:    */     }
/*  80:    */   }
/*  81:    */   
/*  82:    */   protected void shutdownDatabase()
/*  83:    */   {
/*  84:151 */     if (this.dataSource != null)
/*  85:    */     {
/*  86:152 */       this.databaseConfigurer.shutdown(this.dataSource, this.databaseName);
/*  87:153 */       this.dataSource = null;
/*  88:    */     }
/*  89:    */   }
/*  90:    */   
/*  91:    */   protected final DataSource getDataSource()
/*  92:    */   {
/*  93:163 */     return this.dataSource;
/*  94:    */   }
/*  95:    */   
/*  96:    */   private class EmbeddedDataSourceProxy
/*  97:    */     implements EmbeddedDatabase
/*  98:    */   {
/*  99:    */     private final DataSource dataSource;
/* 100:    */     
/* 101:    */     public EmbeddedDataSourceProxy(DataSource dataSource)
/* 102:    */     {
/* 103:172 */       this.dataSource = dataSource;
/* 104:    */     }
/* 105:    */     
/* 106:    */     public Connection getConnection()
/* 107:    */       throws SQLException
/* 108:    */     {
/* 109:176 */       return this.dataSource.getConnection();
/* 110:    */     }
/* 111:    */     
/* 112:    */     public Connection getConnection(String username, String password)
/* 113:    */       throws SQLException
/* 114:    */     {
/* 115:180 */       return this.dataSource.getConnection(username, password);
/* 116:    */     }
/* 117:    */     
/* 118:    */     public PrintWriter getLogWriter()
/* 119:    */       throws SQLException
/* 120:    */     {
/* 121:184 */       return this.dataSource.getLogWriter();
/* 122:    */     }
/* 123:    */     
/* 124:    */     public void setLogWriter(PrintWriter out)
/* 125:    */       throws SQLException
/* 126:    */     {
/* 127:188 */       this.dataSource.setLogWriter(out);
/* 128:    */     }
/* 129:    */     
/* 130:    */     public int getLoginTimeout()
/* 131:    */       throws SQLException
/* 132:    */     {
/* 133:192 */       return this.dataSource.getLoginTimeout();
/* 134:    */     }
/* 135:    */     
/* 136:    */     public void setLoginTimeout(int seconds)
/* 137:    */       throws SQLException
/* 138:    */     {
/* 139:196 */       this.dataSource.setLoginTimeout(seconds);
/* 140:    */     }
/* 141:    */     
/* 142:    */     public <T> T unwrap(Class<T> iface)
/* 143:    */       throws SQLException
/* 144:    */     {
/* 145:200 */       return this.dataSource.unwrap(iface);
/* 146:    */     }
/* 147:    */     
/* 148:    */     public boolean isWrapperFor(Class<?> iface)
/* 149:    */       throws SQLException
/* 150:    */     {
/* 151:204 */       return this.dataSource.isWrapperFor(iface);
/* 152:    */     }
/* 153:    */     
/* 154:    */     public Logger getParentLogger()
/* 155:    */     {
/* 156:208 */       return Logger.getLogger("global");
/* 157:    */     }
/* 158:    */     
/* 159:    */     public void shutdown()
/* 160:    */     {
/* 161:212 */       EmbeddedDatabaseFactory.this.shutdownDatabase();
/* 162:    */     }
/* 163:    */   }
/* 164:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory
 * JD-Core Version:    0.7.0.1
 */