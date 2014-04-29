/*  1:   */ package org.springframework.jdbc.datasource.embedded;
/*  2:   */ 
/*  3:   */ import java.io.File;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.sql.SQLException;
/*  6:   */ import java.util.Properties;
/*  7:   */ import javax.sql.DataSource;
/*  8:   */ import org.apache.commons.logging.Log;
/*  9:   */ import org.apache.commons.logging.LogFactory;
/* 10:   */ import org.apache.derby.impl.io.VFMemoryStorageFactory;
/* 11:   */ import org.apache.derby.jdbc.EmbeddedDriver;
/* 12:   */ 
/* 13:   */ final class DerbyEmbeddedDatabaseConfigurer
/* 14:   */   implements EmbeddedDatabaseConfigurer
/* 15:   */ {
/* 16:39 */   private static final Log logger = LogFactory.getLog(DerbyEmbeddedDatabaseConfigurer.class);
/* 17:   */   private static final String URL_TEMPLATE = "jdbc:derby:memory:%s;%s";
/* 18:   */   private static final String SHUTDOWN_CODE = "08006";
/* 19:46 */   private static final boolean IS_AT_LEAST_DOT_SIX = new EmbeddedDriver().getMinorVersion() >= 6;
/* 20:49 */   private static final String SHUTDOWN_COMMAND = String.format("%s=true", new Object[] { IS_AT_LEAST_DOT_SIX ? "drop" : "shutdown" });
/* 21:   */   private static DerbyEmbeddedDatabaseConfigurer INSTANCE;
/* 22:   */   
/* 23:   */   public static synchronized DerbyEmbeddedDatabaseConfigurer getInstance()
/* 24:   */     throws ClassNotFoundException
/* 25:   */   {
/* 26:60 */     if (INSTANCE == null)
/* 27:   */     {
/* 28:62 */       System.setProperty("derby.stream.error.method", 
/* 29:63 */         OutputStreamFactory.class.getName() + ".getNoopOutputStream");
/* 30:64 */       INSTANCE = new DerbyEmbeddedDatabaseConfigurer();
/* 31:   */     }
/* 32:66 */     return INSTANCE;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public void configureConnectionProperties(ConnectionProperties properties, String databaseName)
/* 36:   */   {
/* 37:73 */     properties.setDriverClass(EmbeddedDriver.class);
/* 38:74 */     properties.setUrl(String.format("jdbc:derby:memory:%s;%s", new Object[] { databaseName, "create=true" }));
/* 39:75 */     properties.setUsername("sa");
/* 40:76 */     properties.setPassword("");
/* 41:   */   }
/* 42:   */   
/* 43:   */   public void shutdown(DataSource dataSource, String databaseName)
/* 44:   */   {
/* 45:   */     try
/* 46:   */     {
/* 47:81 */       new EmbeddedDriver().connect(
/* 48:82 */         String.format("jdbc:derby:memory:%s;%s", new Object[] { databaseName, SHUTDOWN_COMMAND }), new Properties());
/* 49:   */     }
/* 50:   */     catch (SQLException ex)
/* 51:   */     {
/* 52:85 */       if (!"08006".equals(ex.getSQLState()))
/* 53:   */       {
/* 54:86 */         logger.warn("Could not shutdown in-memory Derby database", ex);
/* 55:87 */         return;
/* 56:   */       }
/* 57:89 */       if (!IS_AT_LEAST_DOT_SIX) {
/* 58:   */         try
/* 59:   */         {
/* 60:93 */           VFMemoryStorageFactory.purgeDatabase(new File(databaseName).getCanonicalPath());
/* 61:   */         }
/* 62:   */         catch (IOException ex2)
/* 63:   */         {
/* 64:96 */           logger.warn("Could not purge in-memory Derby database", ex2);
/* 65:   */         }
/* 66:   */       }
/* 67:   */     }
/* 68:   */   }
/* 69:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.embedded.DerbyEmbeddedDatabaseConfigurer
 * JD-Core Version:    0.7.0.1
 */