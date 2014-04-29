/*  1:   */ package org.springframework.jca.cci.connection;
/*  2:   */ 
/*  3:   */ import javax.naming.NamingException;
/*  4:   */ import javax.naming.Reference;
/*  5:   */ import javax.resource.ResourceException;
/*  6:   */ import javax.resource.cci.Connection;
/*  7:   */ import javax.resource.cci.ConnectionFactory;
/*  8:   */ import javax.resource.cci.ConnectionSpec;
/*  9:   */ import javax.resource.cci.RecordFactory;
/* 10:   */ import javax.resource.cci.ResourceAdapterMetaData;
/* 11:   */ import org.springframework.beans.factory.InitializingBean;
/* 12:   */ 
/* 13:   */ public class DelegatingConnectionFactory
/* 14:   */   implements ConnectionFactory, InitializingBean
/* 15:   */ {
/* 16:   */   private ConnectionFactory targetConnectionFactory;
/* 17:   */   
/* 18:   */   public void setTargetConnectionFactory(ConnectionFactory targetConnectionFactory)
/* 19:   */   {
/* 20:51 */     this.targetConnectionFactory = targetConnectionFactory;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public ConnectionFactory getTargetConnectionFactory()
/* 24:   */   {
/* 25:58 */     return this.targetConnectionFactory;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void afterPropertiesSet()
/* 29:   */   {
/* 30:63 */     if (getTargetConnectionFactory() == null) {
/* 31:64 */       throw new IllegalArgumentException("Property 'targetConnectionFactory' is required");
/* 32:   */     }
/* 33:   */   }
/* 34:   */   
/* 35:   */   public Connection getConnection()
/* 36:   */     throws ResourceException
/* 37:   */   {
/* 38:70 */     return getTargetConnectionFactory().getConnection();
/* 39:   */   }
/* 40:   */   
/* 41:   */   public Connection getConnection(ConnectionSpec connectionSpec)
/* 42:   */     throws ResourceException
/* 43:   */   {
/* 44:74 */     return getTargetConnectionFactory().getConnection(connectionSpec);
/* 45:   */   }
/* 46:   */   
/* 47:   */   public RecordFactory getRecordFactory()
/* 48:   */     throws ResourceException
/* 49:   */   {
/* 50:78 */     return getTargetConnectionFactory().getRecordFactory();
/* 51:   */   }
/* 52:   */   
/* 53:   */   public ResourceAdapterMetaData getMetaData()
/* 54:   */     throws ResourceException
/* 55:   */   {
/* 56:82 */     return getTargetConnectionFactory().getMetaData();
/* 57:   */   }
/* 58:   */   
/* 59:   */   public Reference getReference()
/* 60:   */     throws NamingException
/* 61:   */   {
/* 62:86 */     return getTargetConnectionFactory().getReference();
/* 63:   */   }
/* 64:   */   
/* 65:   */   public void setReference(Reference reference)
/* 66:   */   {
/* 67:90 */     getTargetConnectionFactory().setReference(reference);
/* 68:   */   }
/* 69:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.cci.connection.DelegatingConnectionFactory
 * JD-Core Version:    0.7.0.1
 */