/*  1:   */ package org.springframework.jdbc.datasource.init;
/*  2:   */ 
/*  3:   */ import javax.sql.DataSource;
/*  4:   */ import org.springframework.beans.factory.DisposableBean;
/*  5:   */ import org.springframework.beans.factory.InitializingBean;
/*  6:   */ 
/*  7:   */ public class DataSourceInitializer
/*  8:   */   implements InitializingBean, DisposableBean
/*  9:   */ {
/* 10:   */   private DataSource dataSource;
/* 11:   */   private DatabasePopulator databasePopulator;
/* 12:   */   private DatabasePopulator databaseCleaner;
/* 13:39 */   private boolean enabled = true;
/* 14:   */   
/* 15:   */   public void setDataSource(DataSource dataSource)
/* 16:   */   {
/* 17:48 */     this.dataSource = dataSource;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void setDatabasePopulator(DatabasePopulator databasePopulator)
/* 21:   */   {
/* 22:57 */     this.databasePopulator = databasePopulator;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public void setDatabaseCleaner(DatabasePopulator databaseCleaner)
/* 26:   */   {
/* 27:66 */     this.databaseCleaner = databaseCleaner;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void setEnabled(boolean enabled)
/* 31:   */   {
/* 32:74 */     this.enabled = enabled;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public void afterPropertiesSet()
/* 36:   */   {
/* 37:82 */     if ((this.databasePopulator != null) && (this.enabled)) {
/* 38:83 */       DatabasePopulatorUtils.execute(this.databasePopulator, this.dataSource);
/* 39:   */     }
/* 40:   */   }
/* 41:   */   
/* 42:   */   public void destroy()
/* 43:   */   {
/* 44:91 */     if ((this.databaseCleaner != null) && (this.enabled)) {
/* 45:92 */       DatabasePopulatorUtils.execute(this.databaseCleaner, this.dataSource);
/* 46:   */     }
/* 47:   */   }
/* 48:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.init.DataSourceInitializer
 * JD-Core Version:    0.7.0.1
 */