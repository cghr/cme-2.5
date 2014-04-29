/*   1:    */ package org.springframework.jca.cci.connection;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.InvocationHandler;
/*   4:    */ import java.lang.reflect.InvocationTargetException;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import java.lang.reflect.Proxy;
/*   7:    */ import javax.resource.NotSupportedException;
/*   8:    */ import javax.resource.ResourceException;
/*   9:    */ import javax.resource.cci.Connection;
/*  10:    */ import javax.resource.cci.ConnectionFactory;
/*  11:    */ import javax.resource.cci.ConnectionSpec;
/*  12:    */ import org.apache.commons.logging.Log;
/*  13:    */ import org.apache.commons.logging.LogFactory;
/*  14:    */ import org.springframework.beans.factory.DisposableBean;
/*  15:    */ import org.springframework.util.Assert;
/*  16:    */ 
/*  17:    */ public class SingleConnectionFactory
/*  18:    */   extends DelegatingConnectionFactory
/*  19:    */   implements DisposableBean
/*  20:    */ {
/*  21: 56 */   protected final Log logger = LogFactory.getLog(getClass());
/*  22:    */   private Connection target;
/*  23:    */   private Connection connection;
/*  24: 65 */   private final Object connectionMonitor = new Object();
/*  25:    */   
/*  26:    */   public SingleConnectionFactory() {}
/*  27:    */   
/*  28:    */   public SingleConnectionFactory(Connection target)
/*  29:    */   {
/*  30: 81 */     Assert.notNull(target, "Target Connection must not be null");
/*  31: 82 */     this.target = target;
/*  32: 83 */     this.connection = getCloseSuppressingConnectionProxy(target);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public SingleConnectionFactory(ConnectionFactory targetConnectionFactory)
/*  36:    */   {
/*  37: 93 */     Assert.notNull(targetConnectionFactory, "Target ConnectionFactory must not be null");
/*  38: 94 */     setTargetConnectionFactory(targetConnectionFactory);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void afterPropertiesSet()
/*  42:    */   {
/*  43:103 */     if ((this.connection == null) && (getTargetConnectionFactory() == null)) {
/*  44:104 */       throw new IllegalArgumentException("Connection or 'targetConnectionFactory' is required");
/*  45:    */     }
/*  46:    */   }
/*  47:    */   
/*  48:    */   public Connection getConnection()
/*  49:    */     throws ResourceException
/*  50:    */   {
/*  51:111 */     synchronized (this.connectionMonitor)
/*  52:    */     {
/*  53:112 */       if (this.connection == null) {
/*  54:113 */         initConnection();
/*  55:    */       }
/*  56:115 */       return this.connection;
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:    */   public Connection getConnection(ConnectionSpec connectionSpec)
/*  61:    */     throws ResourceException
/*  62:    */   {
/*  63:121 */     throw new NotSupportedException(
/*  64:122 */       "SingleConnectionFactory does not support custom ConnectionSpec");
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void destroy()
/*  68:    */   {
/*  69:132 */     resetConnection();
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void initConnection()
/*  73:    */     throws ResourceException
/*  74:    */   {
/*  75:143 */     if (getTargetConnectionFactory() == null) {
/*  76:144 */       throw new IllegalStateException(
/*  77:145 */         "'targetConnectionFactory' is required for lazily initializing a Connection");
/*  78:    */     }
/*  79:147 */     synchronized (this.connectionMonitor)
/*  80:    */     {
/*  81:148 */       if (this.target != null) {
/*  82:149 */         closeConnection(this.target);
/*  83:    */       }
/*  84:151 */       this.target = doCreateConnection();
/*  85:152 */       prepareConnection(this.target);
/*  86:153 */       if (this.logger.isInfoEnabled()) {
/*  87:154 */         this.logger.info("Established shared CCI Connection: " + this.target);
/*  88:    */       }
/*  89:156 */       this.connection = getCloseSuppressingConnectionProxy(this.target);
/*  90:    */     }
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void resetConnection()
/*  94:    */   {
/*  95:164 */     synchronized (this.connectionMonitor)
/*  96:    */     {
/*  97:165 */       if (this.target != null) {
/*  98:166 */         closeConnection(this.target);
/*  99:    */       }
/* 100:168 */       this.target = null;
/* 101:169 */       this.connection = null;
/* 102:    */     }
/* 103:    */   }
/* 104:    */   
/* 105:    */   protected Connection doCreateConnection()
/* 106:    */     throws ResourceException
/* 107:    */   {
/* 108:179 */     return getTargetConnectionFactory().getConnection();
/* 109:    */   }
/* 110:    */   
/* 111:    */   protected void prepareConnection(Connection con)
/* 112:    */     throws ResourceException
/* 113:    */   {}
/* 114:    */   
/* 115:    */   protected void closeConnection(Connection con)
/* 116:    */   {
/* 117:    */     try
/* 118:    */     {
/* 119:196 */       con.close();
/* 120:    */     }
/* 121:    */     catch (Throwable ex)
/* 122:    */     {
/* 123:199 */       this.logger.warn("Could not close shared CCI Connection", ex);
/* 124:    */     }
/* 125:    */   }
/* 126:    */   
/* 127:    */   protected Connection getCloseSuppressingConnectionProxy(Connection target)
/* 128:    */   {
/* 129:212 */     return (Connection)Proxy.newProxyInstance(
/* 130:213 */       Connection.class.getClassLoader(), 
/* 131:214 */       new Class[] { Connection.class }, 
/* 132:215 */       new CloseSuppressingInvocationHandler(target, null));
/* 133:    */   }
/* 134:    */   
/* 135:    */   private static class CloseSuppressingInvocationHandler
/* 136:    */     implements InvocationHandler
/* 137:    */   {
/* 138:    */     private final Connection target;
/* 139:    */     
/* 140:    */     private CloseSuppressingInvocationHandler(Connection target)
/* 141:    */     {
/* 142:227 */       this.target = target;
/* 143:    */     }
/* 144:    */     
/* 145:    */     public Object invoke(Object proxy, Method method, Object[] args)
/* 146:    */       throws Throwable
/* 147:    */     {
/* 148:231 */       if (method.getName().equals("equals"))
/* 149:    */       {
/* 150:233 */         if (proxy == args[0]) {
/* 151:233 */           return Boolean.valueOf(true);
/* 152:    */         }
/* 153:233 */         return Boolean.valueOf(false);
/* 154:    */       }
/* 155:235 */       if (method.getName().equals("hashCode")) {
/* 156:237 */         return Integer.valueOf(System.identityHashCode(proxy));
/* 157:    */       }
/* 158:239 */       if (method.getName().equals("close")) {
/* 159:241 */         return null;
/* 160:    */       }
/* 161:    */       try
/* 162:    */       {
/* 163:244 */         return method.invoke(this.target, args);
/* 164:    */       }
/* 165:    */       catch (InvocationTargetException ex)
/* 166:    */       {
/* 167:247 */         throw ex.getTargetException();
/* 168:    */       }
/* 169:    */     }
/* 170:    */   }
/* 171:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.cci.connection.SingleConnectionFactory
 * JD-Core Version:    0.7.0.1
 */