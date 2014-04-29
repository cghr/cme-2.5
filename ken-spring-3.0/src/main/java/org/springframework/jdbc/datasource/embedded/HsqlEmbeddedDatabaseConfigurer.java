/*  1:   */ package org.springframework.jdbc.datasource.embedded;
/*  2:   */ 
/*  3:   */ import java.sql.Driver;
/*  4:   */ import org.springframework.util.ClassUtils;
/*  5:   */ 
/*  6:   */ final class HsqlEmbeddedDatabaseConfigurer
/*  7:   */   extends AbstractEmbeddedDatabaseConfigurer
/*  8:   */ {
/*  9:   */   private static HsqlEmbeddedDatabaseConfigurer INSTANCE;
/* 10:   */   private final Class<? extends Driver> driverClass;
/* 11:   */   
/* 12:   */   public static synchronized HsqlEmbeddedDatabaseConfigurer getInstance()
/* 13:   */     throws ClassNotFoundException
/* 14:   */   {
/* 15:44 */     if (INSTANCE == null) {
/* 16:45 */       INSTANCE = new HsqlEmbeddedDatabaseConfigurer(
/* 17:46 */         ClassUtils.forName("org.hsqldb.jdbcDriver", HsqlEmbeddedDatabaseConfigurer.class.getClassLoader()));
/* 18:   */     }
/* 19:48 */     return INSTANCE;
/* 20:   */   }
/* 21:   */   
/* 22:   */   private HsqlEmbeddedDatabaseConfigurer(Class<? extends Driver> driverClass)
/* 23:   */   {
/* 24:52 */     this.driverClass = driverClass;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void configureConnectionProperties(ConnectionProperties properties, String databaseName)
/* 28:   */   {
/* 29:56 */     properties.setDriverClass(this.driverClass);
/* 30:57 */     properties.setUrl("jdbc:hsqldb:mem:" + databaseName);
/* 31:58 */     properties.setUsername("sa");
/* 32:59 */     properties.setPassword("");
/* 33:   */   }
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.embedded.HsqlEmbeddedDatabaseConfigurer
 * JD-Core Version:    0.7.0.1
 */