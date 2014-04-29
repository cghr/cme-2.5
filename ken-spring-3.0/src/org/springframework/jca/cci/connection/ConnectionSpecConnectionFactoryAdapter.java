/*   1:    */ package org.springframework.jca.cci.connection;
/*   2:    */ 
/*   3:    */ import javax.resource.ResourceException;
/*   4:    */ import javax.resource.cci.Connection;
/*   5:    */ import javax.resource.cci.ConnectionFactory;
/*   6:    */ import javax.resource.cci.ConnectionSpec;
/*   7:    */ import org.springframework.core.NamedThreadLocal;
/*   8:    */ 
/*   9:    */ public class ConnectionSpecConnectionFactoryAdapter
/*  10:    */   extends DelegatingConnectionFactory
/*  11:    */ {
/*  12:    */   private ConnectionSpec connectionSpec;
/*  13: 70 */   private final ThreadLocal<ConnectionSpec> threadBoundSpec = new NamedThreadLocal("Current CCI ConnectionSpec");
/*  14:    */   
/*  15:    */   public void setConnectionSpec(ConnectionSpec connectionSpec)
/*  16:    */   {
/*  17: 78 */     this.connectionSpec = connectionSpec;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public void setConnectionSpecForCurrentThread(ConnectionSpec spec)
/*  21:    */   {
/*  22: 90 */     this.threadBoundSpec.set(spec);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void removeConnectionSpecFromCurrentThread()
/*  26:    */   {
/*  27: 99 */     this.threadBoundSpec.remove();
/*  28:    */   }
/*  29:    */   
/*  30:    */   public final Connection getConnection()
/*  31:    */     throws ResourceException
/*  32:    */   {
/*  33:111 */     ConnectionSpec threadSpec = (ConnectionSpec)this.threadBoundSpec.get();
/*  34:112 */     if (threadSpec != null) {
/*  35:113 */       return doGetConnection(threadSpec);
/*  36:    */     }
/*  37:116 */     return doGetConnection(this.connectionSpec);
/*  38:    */   }
/*  39:    */   
/*  40:    */   protected Connection doGetConnection(ConnectionSpec spec)
/*  41:    */     throws ResourceException
/*  42:    */   {
/*  43:131 */     if (getTargetConnectionFactory() == null) {
/*  44:132 */       throw new IllegalStateException("targetConnectionFactory is required");
/*  45:    */     }
/*  46:134 */     if (spec != null) {
/*  47:135 */       return getTargetConnectionFactory().getConnection(spec);
/*  48:    */     }
/*  49:138 */     return getTargetConnectionFactory().getConnection();
/*  50:    */   }
/*  51:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.cci.connection.ConnectionSpecConnectionFactoryAdapter
 * JD-Core Version:    0.7.0.1
 */