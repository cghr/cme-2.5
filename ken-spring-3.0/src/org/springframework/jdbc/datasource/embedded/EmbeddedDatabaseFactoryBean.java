/*  1:   */ package org.springframework.jdbc.datasource.embedded;
/*  2:   */ 
/*  3:   */ import javax.sql.DataSource;
/*  4:   */ import org.springframework.beans.factory.DisposableBean;
/*  5:   */ import org.springframework.beans.factory.FactoryBean;
/*  6:   */ import org.springframework.beans.factory.InitializingBean;
/*  7:   */ import org.springframework.jdbc.datasource.init.DatabasePopulator;
/*  8:   */ import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
/*  9:   */ 
/* 10:   */ public class EmbeddedDatabaseFactoryBean
/* 11:   */   extends EmbeddedDatabaseFactory
/* 12:   */   implements FactoryBean<DataSource>, InitializingBean, DisposableBean
/* 13:   */ {
/* 14:   */   private DatabasePopulator databaseCleaner;
/* 15:   */   
/* 16:   */   public void setDatabaseCleaner(DatabasePopulator databaseCleaner)
/* 17:   */   {
/* 18:54 */     this.databaseCleaner = databaseCleaner;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void afterPropertiesSet()
/* 22:   */   {
/* 23:58 */     initDatabase();
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void destroy()
/* 27:   */   {
/* 28:62 */     if (this.databaseCleaner != null) {
/* 29:63 */       DatabasePopulatorUtils.execute(this.databaseCleaner, getDataSource());
/* 30:   */     }
/* 31:65 */     shutdownDatabase();
/* 32:   */   }
/* 33:   */   
/* 34:   */   public DataSource getObject()
/* 35:   */   {
/* 36:70 */     return getDataSource();
/* 37:   */   }
/* 38:   */   
/* 39:   */   public Class<? extends DataSource> getObjectType()
/* 40:   */   {
/* 41:74 */     return DataSource.class;
/* 42:   */   }
/* 43:   */   
/* 44:   */   public boolean isSingleton()
/* 45:   */   {
/* 46:78 */     return true;
/* 47:   */   }
/* 48:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactoryBean
 * JD-Core Version:    0.7.0.1
 */