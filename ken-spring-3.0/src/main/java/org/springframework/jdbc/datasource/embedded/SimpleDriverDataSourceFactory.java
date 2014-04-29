/*  1:   */ package org.springframework.jdbc.datasource.embedded;
/*  2:   */ 
/*  3:   */ import java.sql.Driver;
/*  4:   */ import javax.sql.DataSource;
/*  5:   */ import org.springframework.jdbc.datasource.SimpleDriverDataSource;
/*  6:   */ 
/*  7:   */ final class SimpleDriverDataSourceFactory
/*  8:   */   implements DataSourceFactory
/*  9:   */ {
/* 10:34 */   private final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
/* 11:   */   
/* 12:   */   public ConnectionProperties getConnectionProperties()
/* 13:   */   {
/* 14:37 */     new ConnectionProperties()
/* 15:   */     {
/* 16:   */       public void setDriverClass(Class<? extends Driver> driverClass)
/* 17:   */       {
/* 18:39 */         SimpleDriverDataSourceFactory.this.dataSource.setDriverClass(driverClass);
/* 19:   */       }
/* 20:   */       
/* 21:   */       public void setUrl(String url)
/* 22:   */       {
/* 23:43 */         SimpleDriverDataSourceFactory.this.dataSource.setUrl(url);
/* 24:   */       }
/* 25:   */       
/* 26:   */       public void setUsername(String username)
/* 27:   */       {
/* 28:47 */         SimpleDriverDataSourceFactory.this.dataSource.setUsername(username);
/* 29:   */       }
/* 30:   */       
/* 31:   */       public void setPassword(String password)
/* 32:   */       {
/* 33:51 */         SimpleDriverDataSourceFactory.this.dataSource.setPassword(password);
/* 34:   */       }
/* 35:   */     };
/* 36:   */   }
/* 37:   */   
/* 38:   */   public DataSource getDataSource()
/* 39:   */   {
/* 40:57 */     return this.dataSource;
/* 41:   */   }
/* 42:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.embedded.SimpleDriverDataSourceFactory
 * JD-Core Version:    0.7.0.1
 */