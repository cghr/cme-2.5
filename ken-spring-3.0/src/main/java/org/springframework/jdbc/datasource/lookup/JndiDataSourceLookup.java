/*  1:   */ package org.springframework.jdbc.datasource.lookup;
/*  2:   */ 
/*  3:   */ import javax.naming.NamingException;
/*  4:   */ import javax.sql.DataSource;
/*  5:   */ import org.springframework.jndi.JndiLocatorSupport;
/*  6:   */ 
/*  7:   */ public class JndiDataSourceLookup
/*  8:   */   extends JndiLocatorSupport
/*  9:   */   implements DataSourceLookup
/* 10:   */ {
/* 11:   */   public JndiDataSourceLookup()
/* 12:   */   {
/* 13:39 */     setResourceRef(true);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public DataSource getDataSource(String dataSourceName)
/* 17:   */     throws DataSourceLookupFailureException
/* 18:   */   {
/* 19:   */     try
/* 20:   */     {
/* 21:44 */       return (DataSource)lookup(dataSourceName, DataSource.class);
/* 22:   */     }
/* 23:   */     catch (NamingException ex)
/* 24:   */     {
/* 25:47 */       throw new DataSourceLookupFailureException(
/* 26:48 */         "Failed to look up JNDI DataSource with name '" + dataSourceName + "'", ex);
/* 27:   */     }
/* 28:   */   }
/* 29:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup
 * JD-Core Version:    0.7.0.1
 */