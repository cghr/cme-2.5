/*  1:   */ package org.springframework.jdbc.datasource.init;
/*  2:   */ 
/*  3:   */ import java.sql.Connection;
/*  4:   */ import java.sql.SQLException;
/*  5:   */ import javax.sql.DataSource;
/*  6:   */ import org.springframework.dao.DataAccessResourceFailureException;
/*  7:   */ import org.springframework.util.Assert;
/*  8:   */ 
/*  9:   */ public abstract class DatabasePopulatorUtils
/* 10:   */ {
/* 11:   */   public static void execute(DatabasePopulator populator, DataSource dataSource)
/* 12:   */   {
/* 13:40 */     Assert.notNull(populator, "DatabasePopulator must be provided");
/* 14:41 */     Assert.notNull(dataSource, "DataSource must be provided");
/* 15:   */     try
/* 16:   */     {
/* 17:43 */       Connection connection = dataSource.getConnection();
/* 18:   */       try
/* 19:   */       {
/* 20:45 */         populator.populate(connection);
/* 21:   */       }
/* 22:   */       finally
/* 23:   */       {
/* 24:   */         try
/* 25:   */         {
/* 26:49 */           connection.close();
/* 27:   */         }
/* 28:   */         catch (SQLException localSQLException1) {}
/* 29:   */       }
/* 30:   */       try
/* 31:   */       {
/* 32:49 */         connection.close();
/* 33:   */       }
/* 34:   */       catch (SQLException localSQLException2) {}
/* 35:   */       return;
/* 36:   */     }
/* 37:   */     catch (Exception ex)
/* 38:   */     {
/* 39:57 */       throw new DataAccessResourceFailureException("Failed to execute database script", ex);
/* 40:   */     }
/* 41:   */   }
/* 42:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.init.DatabasePopulatorUtils
 * JD-Core Version:    0.7.0.1
 */