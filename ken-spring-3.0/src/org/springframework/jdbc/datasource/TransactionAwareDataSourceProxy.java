/*   1:    */ package org.springframework.jdbc.datasource;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.InvocationHandler;
/*   4:    */ import java.lang.reflect.InvocationTargetException;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import java.lang.reflect.Proxy;
/*   7:    */ import java.sql.Connection;
/*   8:    */ import java.sql.SQLException;
/*   9:    */ import java.sql.Statement;
/*  10:    */ import javax.sql.DataSource;
/*  11:    */ import org.springframework.transaction.support.TransactionSynchronizationManager;
/*  12:    */ import org.springframework.util.Assert;
/*  13:    */ 
/*  14:    */ public class TransactionAwareDataSourceProxy
/*  15:    */   extends DelegatingDataSource
/*  16:    */ {
/*  17: 82 */   private boolean reobtainTransactionalConnections = false;
/*  18:    */   
/*  19:    */   public TransactionAwareDataSourceProxy() {}
/*  20:    */   
/*  21:    */   public TransactionAwareDataSourceProxy(DataSource targetDataSource)
/*  22:    */   {
/*  23: 97 */     super(targetDataSource);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setReobtainTransactionalConnections(boolean reobtainTransactionalConnections)
/*  27:    */   {
/*  28:110 */     this.reobtainTransactionalConnections = reobtainTransactionalConnections;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public Connection getConnection()
/*  32:    */     throws SQLException
/*  33:    */   {
/*  34:125 */     DataSource ds = getTargetDataSource();
/*  35:126 */     Assert.state(ds != null, "'targetDataSource' is required");
/*  36:127 */     return getTransactionAwareConnectionProxy(ds);
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected Connection getTransactionAwareConnectionProxy(DataSource targetDataSource)
/*  40:    */   {
/*  41:139 */     return (Connection)Proxy.newProxyInstance(
/*  42:140 */       ConnectionProxy.class.getClassLoader(), 
/*  43:141 */       new Class[] { ConnectionProxy.class }, 
/*  44:142 */       new TransactionAwareInvocationHandler(targetDataSource));
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected boolean shouldObtainFixedConnection(DataSource targetDataSource)
/*  48:    */   {
/*  49:157 */     return (!TransactionSynchronizationManager.isSynchronizationActive()) || (!this.reobtainTransactionalConnections);
/*  50:    */   }
/*  51:    */   
/*  52:    */   private class TransactionAwareInvocationHandler
/*  53:    */     implements InvocationHandler
/*  54:    */   {
/*  55:    */     private final DataSource targetDataSource;
/*  56:    */     private Connection target;
/*  57:171 */     private boolean closed = false;
/*  58:    */     
/*  59:    */     public TransactionAwareInvocationHandler(DataSource targetDataSource)
/*  60:    */     {
/*  61:174 */       this.targetDataSource = targetDataSource;
/*  62:    */     }
/*  63:    */     
/*  64:    */     public Object invoke(Object proxy, Method method, Object[] args)
/*  65:    */       throws Throwable
/*  66:    */     {
/*  67:180 */       if (method.getName().equals("equals"))
/*  68:    */       {
/*  69:182 */         if (proxy == args[0]) {
/*  70:182 */           return Boolean.valueOf(true);
/*  71:    */         }
/*  72:182 */         return Boolean.valueOf(false);
/*  73:    */       }
/*  74:184 */       if (method.getName().equals("hashCode")) {
/*  75:186 */         return Integer.valueOf(System.identityHashCode(proxy));
/*  76:    */       }
/*  77:188 */       if (method.getName().equals("toString"))
/*  78:    */       {
/*  79:190 */         StringBuilder sb = new StringBuilder("Transaction-aware proxy for target Connection ");
/*  80:191 */         if (this.target != null) {
/*  81:192 */           sb.append("[").append(this.target.toString()).append("]");
/*  82:    */         } else {
/*  83:195 */           sb.append(" from DataSource [").append(this.targetDataSource).append("]");
/*  84:    */         }
/*  85:197 */         return sb.toString();
/*  86:    */       }
/*  87:199 */       if (method.getName().equals("unwrap"))
/*  88:    */       {
/*  89:200 */         if (((Class)args[0]).isInstance(proxy)) {
/*  90:201 */           return proxy;
/*  91:    */         }
/*  92:    */       }
/*  93:204 */       else if (method.getName().equals("isWrapperFor"))
/*  94:    */       {
/*  95:205 */         if (((Class)args[0]).isInstance(proxy)) {
/*  96:206 */           return Boolean.valueOf(true);
/*  97:    */         }
/*  98:    */       }
/*  99:    */       else
/* 100:    */       {
/* 101:209 */         if (method.getName().equals("close"))
/* 102:    */         {
/* 103:211 */           DataSourceUtils.doReleaseConnection(this.target, this.targetDataSource);
/* 104:212 */           this.closed = true;
/* 105:213 */           return null;
/* 106:    */         }
/* 107:215 */         if (method.getName().equals("isClosed")) {
/* 108:216 */           return Boolean.valueOf(this.closed);
/* 109:    */         }
/* 110:    */       }
/* 111:219 */       if (this.target == null)
/* 112:    */       {
/* 113:220 */         if (this.closed) {
/* 114:221 */           throw new SQLException("Connection handle already closed");
/* 115:    */         }
/* 116:223 */         if (TransactionAwareDataSourceProxy.this.shouldObtainFixedConnection(this.targetDataSource)) {
/* 117:224 */           this.target = DataSourceUtils.doGetConnection(this.targetDataSource);
/* 118:    */         }
/* 119:    */       }
/* 120:227 */       Connection actualTarget = this.target;
/* 121:228 */       if (actualTarget == null) {
/* 122:229 */         actualTarget = DataSourceUtils.doGetConnection(this.targetDataSource);
/* 123:    */       }
/* 124:232 */       if (method.getName().equals("getTargetConnection")) {
/* 125:234 */         return actualTarget;
/* 126:    */       }
/* 127:    */       try
/* 128:    */       {
/* 129:239 */         Object retVal = method.invoke(actualTarget, args);
/* 130:243 */         if ((retVal instanceof Statement)) {
/* 131:244 */           DataSourceUtils.applyTransactionTimeout((Statement)retVal, this.targetDataSource);
/* 132:    */         }
/* 133:247 */         return retVal;
/* 134:    */       }
/* 135:    */       catch (InvocationTargetException ex)
/* 136:    */       {
/* 137:250 */         throw ex.getTargetException();
/* 138:    */       }
/* 139:    */       finally
/* 140:    */       {
/* 141:253 */         if (actualTarget != this.target) {
/* 142:254 */           DataSourceUtils.doReleaseConnection(actualTarget, this.targetDataSource);
/* 143:    */         }
/* 144:    */       }
/* 145:    */     }
/* 146:    */   }
/* 147:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
 * JD-Core Version:    0.7.0.1
 */