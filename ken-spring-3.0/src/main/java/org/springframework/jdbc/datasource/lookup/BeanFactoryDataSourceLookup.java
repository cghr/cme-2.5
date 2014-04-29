/*  1:   */ package org.springframework.jdbc.datasource.lookup;
/*  2:   */ 
/*  3:   */ import javax.sql.DataSource;
/*  4:   */ import org.springframework.beans.BeansException;
/*  5:   */ import org.springframework.beans.factory.BeanFactory;
/*  6:   */ import org.springframework.beans.factory.BeanFactoryAware;
/*  7:   */ import org.springframework.util.Assert;
/*  8:   */ 
/*  9:   */ public class BeanFactoryDataSourceLookup
/* 10:   */   implements DataSourceLookup, BeanFactoryAware
/* 11:   */ {
/* 12:   */   private BeanFactory beanFactory;
/* 13:   */   
/* 14:   */   public BeanFactoryDataSourceLookup() {}
/* 15:   */   
/* 16:   */   public BeanFactoryDataSourceLookup(BeanFactory beanFactory)
/* 17:   */   {
/* 18:60 */     Assert.notNull(beanFactory, "BeanFactory is required");
/* 19:61 */     this.beanFactory = beanFactory;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void setBeanFactory(BeanFactory beanFactory)
/* 23:   */   {
/* 24:66 */     this.beanFactory = beanFactory;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public DataSource getDataSource(String dataSourceName)
/* 28:   */     throws DataSourceLookupFailureException
/* 29:   */   {
/* 30:71 */     Assert.state(this.beanFactory != null, "BeanFactory is required");
/* 31:   */     try
/* 32:   */     {
/* 33:73 */       return (DataSource)this.beanFactory.getBean(dataSourceName, DataSource.class);
/* 34:   */     }
/* 35:   */     catch (BeansException ex)
/* 36:   */     {
/* 37:76 */       throw new DataSourceLookupFailureException(
/* 38:77 */         "Failed to look up DataSource bean with name '" + dataSourceName + "'", ex);
/* 39:   */     }
/* 40:   */   }
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.lookup.BeanFactoryDataSourceLookup
 * JD-Core Version:    0.7.0.1
 */