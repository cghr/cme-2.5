/*  1:   */ package org.springframework.jdbc.datasource;
/*  2:   */ 
/*  3:   */ import java.sql.Connection;
/*  4:   */ import org.springframework.util.Assert;
/*  5:   */ 
/*  6:   */ public class SimpleConnectionHandle
/*  7:   */   implements ConnectionHandle
/*  8:   */ {
/*  9:   */   private final Connection connection;
/* 10:   */   
/* 11:   */   public SimpleConnectionHandle(Connection connection)
/* 12:   */   {
/* 13:40 */     Assert.notNull(connection, "Connection must not be null");
/* 14:41 */     this.connection = connection;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public Connection getConnection()
/* 18:   */   {
/* 19:48 */     return this.connection;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void releaseConnection(Connection con) {}
/* 23:   */   
/* 24:   */   public String toString()
/* 25:   */   {
/* 26:61 */     return "SimpleConnectionHandle: " + this.connection;
/* 27:   */   }
/* 28:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.SimpleConnectionHandle
 * JD-Core Version:    0.7.0.1
 */