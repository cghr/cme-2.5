/*  1:   */ package org.springframework.jdbc.datasource.embedded;
/*  2:   */ 
/*  3:   */ import java.sql.Connection;
/*  4:   */ import java.sql.SQLException;
/*  5:   */ import java.sql.Statement;
/*  6:   */ import javax.sql.DataSource;
/*  7:   */ import org.apache.commons.logging.Log;
/*  8:   */ import org.apache.commons.logging.LogFactory;
/*  9:   */ 
/* 10:   */ abstract class AbstractEmbeddedDatabaseConfigurer
/* 11:   */   implements EmbeddedDatabaseConfigurer
/* 12:   */ {
/* 13:36 */   protected final Log logger = LogFactory.getLog(getClass());
/* 14:   */   
/* 15:   */   public void shutdown(DataSource dataSource, String databaseName)
/* 16:   */   {
/* 17:   */     try
/* 18:   */     {
/* 19:40 */       Connection connection = dataSource.getConnection();
/* 20:41 */       Statement stmt = connection.createStatement();
/* 21:42 */       stmt.execute("SHUTDOWN");
/* 22:   */     }
/* 23:   */     catch (SQLException ex)
/* 24:   */     {
/* 25:45 */       if (this.logger.isWarnEnabled()) {
/* 26:46 */         this.logger.warn("Could not shutdown embedded database", ex);
/* 27:   */       }
/* 28:   */     }
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.embedded.AbstractEmbeddedDatabaseConfigurer
 * JD-Core Version:    0.7.0.1
 */