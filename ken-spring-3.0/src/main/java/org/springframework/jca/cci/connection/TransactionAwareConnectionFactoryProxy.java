/*   1:    */ package org.springframework.jca.cci.connection;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.InvocationHandler;
/*   4:    */ import java.lang.reflect.InvocationTargetException;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import java.lang.reflect.Proxy;
/*   7:    */ import javax.resource.ResourceException;
/*   8:    */ import javax.resource.cci.Connection;
/*   9:    */ import javax.resource.cci.ConnectionFactory;
/*  10:    */ import javax.resource.spi.IllegalStateException;
/*  11:    */ 
/*  12:    */ public class TransactionAwareConnectionFactoryProxy
/*  13:    */   extends DelegatingConnectionFactory
/*  14:    */ {
/*  15:    */   public TransactionAwareConnectionFactoryProxy() {}
/*  16:    */   
/*  17:    */   public TransactionAwareConnectionFactoryProxy(ConnectionFactory targetConnectionFactory)
/*  18:    */   {
/*  19: 81 */     setTargetConnectionFactory(targetConnectionFactory);
/*  20: 82 */     afterPropertiesSet();
/*  21:    */   }
/*  22:    */   
/*  23:    */   public Connection getConnection()
/*  24:    */     throws ResourceException
/*  25:    */   {
/*  26: 94 */     Connection con = ConnectionFactoryUtils.doGetConnection(getTargetConnectionFactory());
/*  27: 95 */     return getTransactionAwareConnectionProxy(con, getTargetConnectionFactory());
/*  28:    */   }
/*  29:    */   
/*  30:    */   protected Connection getTransactionAwareConnectionProxy(Connection target, ConnectionFactory cf)
/*  31:    */   {
/*  32:108 */     return (Connection)Proxy.newProxyInstance(
/*  33:109 */       Connection.class.getClassLoader(), 
/*  34:110 */       new Class[] { Connection.class }, 
/*  35:111 */       new TransactionAwareInvocationHandler(target, cf));
/*  36:    */   }
/*  37:    */   
/*  38:    */   private static class TransactionAwareInvocationHandler
/*  39:    */     implements InvocationHandler
/*  40:    */   {
/*  41:    */     private final Connection target;
/*  42:    */     private final ConnectionFactory connectionFactory;
/*  43:    */     
/*  44:    */     public TransactionAwareInvocationHandler(Connection target, ConnectionFactory cf)
/*  45:    */     {
/*  46:126 */       this.target = target;
/*  47:127 */       this.connectionFactory = cf;
/*  48:    */     }
/*  49:    */     
/*  50:    */     public Object invoke(Object proxy, Method method, Object[] args)
/*  51:    */       throws Throwable
/*  52:    */     {
/*  53:133 */       if (method.getName().equals("equals"))
/*  54:    */       {
/*  55:135 */         if (proxy == args[0]) {
/*  56:135 */           return Boolean.valueOf(true);
/*  57:    */         }
/*  58:135 */         return Boolean.valueOf(false);
/*  59:    */       }
/*  60:137 */       if (method.getName().equals("hashCode")) {
/*  61:139 */         return Integer.valueOf(System.identityHashCode(proxy));
/*  62:    */       }
/*  63:141 */       if (method.getName().equals("getLocalTransaction"))
/*  64:    */       {
/*  65:142 */         if (ConnectionFactoryUtils.isConnectionTransactional(this.target, this.connectionFactory)) {
/*  66:143 */           throw new IllegalStateException(
/*  67:144 */             "Local transaction handling not allowed within a managed transaction");
/*  68:    */         }
/*  69:    */       }
/*  70:147 */       else if (method.getName().equals("close"))
/*  71:    */       {
/*  72:149 */         ConnectionFactoryUtils.doReleaseConnection(this.target, this.connectionFactory);
/*  73:150 */         return null;
/*  74:    */       }
/*  75:    */       try
/*  76:    */       {
/*  77:155 */         return method.invoke(this.target, args);
/*  78:    */       }
/*  79:    */       catch (InvocationTargetException ex)
/*  80:    */       {
/*  81:158 */         throw ex.getTargetException();
/*  82:    */       }
/*  83:    */     }
/*  84:    */   }
/*  85:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.cci.connection.TransactionAwareConnectionFactoryProxy
 * JD-Core Version:    0.7.0.1
 */