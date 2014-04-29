/*  1:   */ package org.springframework.jca.cci.connection;
/*  2:   */ 
/*  3:   */ import javax.resource.cci.Connection;
/*  4:   */ import org.springframework.transaction.support.ResourceHolderSupport;
/*  5:   */ 
/*  6:   */ public class ConnectionHolder
/*  7:   */   extends ResourceHolderSupport
/*  8:   */ {
/*  9:   */   private final Connection connection;
/* 10:   */   
/* 11:   */   public ConnectionHolder(Connection connection)
/* 12:   */   {
/* 13:42 */     this.connection = connection;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public Connection getConnection()
/* 17:   */   {
/* 18:46 */     return this.connection;
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.cci.connection.ConnectionHolder
 * JD-Core Version:    0.7.0.1
 */