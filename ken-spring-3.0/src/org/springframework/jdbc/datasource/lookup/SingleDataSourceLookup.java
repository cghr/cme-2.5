/*  1:   */ package org.springframework.jdbc.datasource.lookup;
/*  2:   */ 
/*  3:   */ import javax.sql.DataSource;
/*  4:   */ import org.springframework.util.Assert;
/*  5:   */ 
/*  6:   */ public class SingleDataSourceLookup
/*  7:   */   implements DataSourceLookup
/*  8:   */ {
/*  9:   */   private final DataSource dataSource;
/* 10:   */   
/* 11:   */   public SingleDataSourceLookup(DataSource dataSource)
/* 12:   */   {
/* 13:40 */     Assert.notNull(dataSource, "DataSource must not be null");
/* 14:41 */     this.dataSource = dataSource;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public DataSource getDataSource(String dataSourceName)
/* 18:   */   {
/* 19:46 */     return this.dataSource;
/* 20:   */   }
/* 21:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.lookup.SingleDataSourceLookup
 * JD-Core Version:    0.7.0.1
 */