/*  1:   */ package com.kentropy.util;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ import java.sql.Connection;
/*  5:   */ import java.sql.ResultSet;
/*  6:   */ import java.sql.SQLException;
/*  7:   */ import java.sql.Statement;
/*  8:   */ import javax.sql.DataSource;
/*  9:   */ import org.springframework.context.ApplicationContext;
/* 10:   */ import org.springframework.context.support.ClassPathXmlApplicationContext;
/* 11:   */ 
/* 12:   */ public class DbConnection
/* 13:   */ {
/* 14:16 */   static ApplicationContext appContext = null;
/* 15:17 */   static Connection connection = null;
/* 16:18 */   static DataSource dataSource = null;
/* 17:   */   
/* 18:   */   public DbConnection(DataSource dataSource1)
/* 19:   */   {
/* 20:21 */     dataSource = dataSource1;
/* 21:   */   }
/* 22:   */   
/* 23:   */   static
/* 24:   */   {
/* 25:27 */     appContext = new ClassPathXmlApplicationContext("appContext.xml");
/* 26:28 */     dataSource = (DataSource)appContext.getBean("dataSource");
/* 27:   */     try
/* 28:   */     {
/* 29:31 */       connection = dataSource.getConnection();
/* 30:   */     }
/* 31:   */     catch (SQLException e)
/* 32:   */     {
/* 33:34 */       e.printStackTrace();
/* 34:   */     }
/* 35:   */   }
/* 36:   */   
/* 37:   */   public static Connection getConnection()
/* 38:   */   {
/* 39:   */     try
/* 40:   */     {
/* 41:42 */       if ((connection.isClosed()) || (connection == null)) {
/* 42:43 */         connection = dataSource.getConnection();
/* 43:   */       }
/* 44:   */     }
/* 45:   */     catch (Exception e)
/* 46:   */     {
/* 47:48 */       e.printStackTrace();
/* 48:   */     }
/* 49:51 */     return connection;
/* 50:   */   }
/* 51:   */   
/* 52:   */   public static ApplicationContext getApplicationContext()
/* 53:   */   {
/* 54:55 */     return appContext;
/* 55:   */   }
/* 56:   */   
/* 57:   */   public static DataSource getDataSource()
/* 58:   */   {
/* 59:60 */     return dataSource;
/* 60:   */   }
/* 61:   */   
/* 62:   */   public static void main(String[] args)
/* 63:   */     throws SQLException
/* 64:   */   {
/* 65:64 */     System.out.println(getConnection());
/* 66:65 */     getConnection().close();
/* 67:66 */     System.out.println(getDataSource());
/* 68:67 */     System.out.println(getApplicationContext());
/* 69:68 */     System.out.println(getConnection());
/* 70:   */   }
/* 71:   */   
/* 72:   */   public static void close(Object object, Statement stmt, ResultSet rs)
/* 73:   */     throws SQLException
/* 74:   */   {
/* 75:72 */     stmt.close();
/* 76:73 */     rs.close();
/* 77:   */   }
/* 78:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-util\ken-util.jar
 * Qualified Name:     com.kentropy.util.DbConnection
 * JD-Core Version:    0.7.0.1
 */