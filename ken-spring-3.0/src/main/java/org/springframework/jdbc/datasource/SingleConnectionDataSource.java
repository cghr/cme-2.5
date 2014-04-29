/*   1:    */ package org.springframework.jdbc.datasource;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.InvocationHandler;
/*   4:    */ import java.lang.reflect.InvocationTargetException;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import java.lang.reflect.Proxy;
/*   7:    */ import java.sql.Connection;
/*   8:    */ import java.sql.SQLException;
/*   9:    */ import org.apache.commons.logging.Log;
/*  10:    */ import org.springframework.beans.factory.DisposableBean;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ import org.springframework.util.ObjectUtils;
/*  13:    */ 
/*  14:    */ public class SingleConnectionDataSource
/*  15:    */   extends DriverManagerDataSource
/*  16:    */   implements SmartDataSource, DisposableBean
/*  17:    */ {
/*  18:    */   private boolean suppressClose;
/*  19:    */   private Boolean autoCommit;
/*  20:    */   private Connection target;
/*  21:    */   private Connection connection;
/*  22: 74 */   private final Object connectionMonitor = new Object();
/*  23:    */   
/*  24:    */   public SingleConnectionDataSource() {}
/*  25:    */   
/*  26:    */   @Deprecated
/*  27:    */   public SingleConnectionDataSource(String driverClassName, String url, String username, String password, boolean suppressClose)
/*  28:    */   {
/*  29:101 */     super(driverClassName, url, username, password);
/*  30:102 */     this.suppressClose = suppressClose;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public SingleConnectionDataSource(String url, String username, String password, boolean suppressClose)
/*  34:    */   {
/*  35:116 */     super(url, username, password);
/*  36:117 */     this.suppressClose = suppressClose;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public SingleConnectionDataSource(String url, boolean suppressClose)
/*  40:    */   {
/*  41:129 */     super(url);
/*  42:130 */     this.suppressClose = suppressClose;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public SingleConnectionDataSource(Connection target, boolean suppressClose)
/*  46:    */   {
/*  47:142 */     Assert.notNull(target, "Connection must not be null");
/*  48:143 */     this.target = target;
/*  49:144 */     this.suppressClose = suppressClose;
/*  50:145 */     this.connection = (suppressClose ? getCloseSuppressingConnectionProxy(target) : target);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setSuppressClose(boolean suppressClose)
/*  54:    */   {
/*  55:154 */     this.suppressClose = suppressClose;
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected boolean isSuppressClose()
/*  59:    */   {
/*  60:162 */     return this.suppressClose;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setAutoCommit(boolean autoCommit)
/*  64:    */   {
/*  65:169 */     this.autoCommit = Boolean.valueOf(autoCommit);
/*  66:    */   }
/*  67:    */   
/*  68:    */   protected Boolean getAutoCommitValue()
/*  69:    */   {
/*  70:177 */     return this.autoCommit;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public Connection getConnection()
/*  74:    */     throws SQLException
/*  75:    */   {
/*  76:183 */     synchronized (this.connectionMonitor)
/*  77:    */     {
/*  78:184 */       if (this.connection == null) {
/*  79:186 */         initConnection();
/*  80:    */       }
/*  81:188 */       if (this.connection.isClosed()) {
/*  82:189 */         throw new SQLException(
/*  83:190 */           "Connection was closed in SingleConnectionDataSource. Check that user code checks shouldClose() before closing Connections, or set 'suppressClose' to 'true'");
/*  84:    */       }
/*  85:193 */       return this.connection;
/*  86:    */     }
/*  87:    */   }
/*  88:    */   
/*  89:    */   public Connection getConnection(String username, String password)
/*  90:    */     throws SQLException
/*  91:    */   {
/*  92:204 */     if ((ObjectUtils.nullSafeEquals(username, getUsername())) && 
/*  93:205 */       (ObjectUtils.nullSafeEquals(password, getPassword()))) {
/*  94:206 */       return getConnection();
/*  95:    */     }
/*  96:209 */     throw new SQLException("SingleConnectionDataSource does not support custom username and password");
/*  97:    */   }
/*  98:    */   
/*  99:    */   public boolean shouldClose(Connection con)
/* 100:    */   {
/* 101:217 */     synchronized (this.connectionMonitor)
/* 102:    */     {
/* 103:218 */       return (con != this.connection) && (con != this.target);
/* 104:    */     }
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void destroy()
/* 108:    */   {
/* 109:229 */     synchronized (this.connectionMonitor)
/* 110:    */     {
/* 111:230 */       closeConnection();
/* 112:    */     }
/* 113:    */   }
/* 114:    */   
/* 115:    */   public void initConnection()
/* 116:    */     throws SQLException
/* 117:    */   {
/* 118:239 */     if (getUrl() == null) {
/* 119:240 */       throw new IllegalStateException("'url' property is required for lazily initializing a Connection");
/* 120:    */     }
/* 121:242 */     synchronized (this.connectionMonitor)
/* 122:    */     {
/* 123:243 */       closeConnection();
/* 124:244 */       this.target = getConnectionFromDriver(getUsername(), getPassword());
/* 125:245 */       prepareConnection(this.target);
/* 126:246 */       if (this.logger.isInfoEnabled()) {
/* 127:247 */         this.logger.info("Established shared JDBC Connection: " + this.target);
/* 128:    */       }
/* 129:249 */       this.connection = (isSuppressClose() ? getCloseSuppressingConnectionProxy(this.target) : this.target);
/* 130:    */     }
/* 131:    */   }
/* 132:    */   
/* 133:    */   public void resetConnection()
/* 134:    */   {
/* 135:257 */     synchronized (this.connectionMonitor)
/* 136:    */     {
/* 137:258 */       closeConnection();
/* 138:259 */       this.target = null;
/* 139:260 */       this.connection = null;
/* 140:    */     }
/* 141:    */   }
/* 142:    */   
/* 143:    */   protected void prepareConnection(Connection con)
/* 144:    */     throws SQLException
/* 145:    */   {
/* 146:272 */     Boolean autoCommit = getAutoCommitValue();
/* 147:273 */     if ((autoCommit != null) && (con.getAutoCommit() != autoCommit.booleanValue())) {
/* 148:274 */       con.setAutoCommit(autoCommit.booleanValue());
/* 149:    */     }
/* 150:    */   }
/* 151:    */   
/* 152:    */   private void closeConnection()
/* 153:    */   {
/* 154:282 */     if (this.target != null) {
/* 155:    */       try
/* 156:    */       {
/* 157:284 */         this.target.close();
/* 158:    */       }
/* 159:    */       catch (Throwable ex)
/* 160:    */       {
/* 161:287 */         this.logger.warn("Could not close shared JDBC Connection", ex);
/* 162:    */       }
/* 163:    */     }
/* 164:    */   }
/* 165:    */   
/* 166:    */   protected Connection getCloseSuppressingConnectionProxy(Connection target)
/* 167:    */   {
/* 168:299 */     return (Connection)Proxy.newProxyInstance(
/* 169:300 */       ConnectionProxy.class.getClassLoader(), 
/* 170:301 */       new Class[] { ConnectionProxy.class }, 
/* 171:302 */       new CloseSuppressingInvocationHandler(target));
/* 172:    */   }
/* 173:    */   
/* 174:    */   private static class CloseSuppressingInvocationHandler
/* 175:    */     implements InvocationHandler
/* 176:    */   {
/* 177:    */     private final Connection target;
/* 178:    */     
/* 179:    */     public CloseSuppressingInvocationHandler(Connection target)
/* 180:    */     {
/* 181:314 */       this.target = target;
/* 182:    */     }
/* 183:    */     
/* 184:    */     public Object invoke(Object proxy, Method method, Object[] args)
/* 185:    */       throws Throwable
/* 186:    */     {
/* 187:320 */       if (method.getName().equals("equals"))
/* 188:    */       {
/* 189:322 */         if (proxy == args[0]) {
/* 190:322 */           return Boolean.valueOf(true);
/* 191:    */         }
/* 192:322 */         return Boolean.valueOf(false);
/* 193:    */       }
/* 194:324 */       if (method.getName().equals("hashCode")) {
/* 195:326 */         return Integer.valueOf(System.identityHashCode(proxy));
/* 196:    */       }
/* 197:328 */       if (method.getName().equals("unwrap"))
/* 198:    */       {
/* 199:329 */         if (((Class)args[0]).isInstance(proxy)) {
/* 200:330 */           return proxy;
/* 201:    */         }
/* 202:    */       }
/* 203:333 */       else if (method.getName().equals("isWrapperFor"))
/* 204:    */       {
/* 205:334 */         if (((Class)args[0]).isInstance(proxy)) {
/* 206:335 */           return Boolean.valueOf(true);
/* 207:    */         }
/* 208:    */       }
/* 209:    */       else
/* 210:    */       {
/* 211:338 */         if (method.getName().equals("close")) {
/* 212:340 */           return null;
/* 213:    */         }
/* 214:342 */         if (method.getName().equals("isClosed")) {
/* 215:343 */           return Boolean.valueOf(false);
/* 216:    */         }
/* 217:345 */         if (method.getName().equals("getTargetConnection")) {
/* 218:347 */           return this.target;
/* 219:    */         }
/* 220:    */       }
/* 221:    */       try
/* 222:    */       {
/* 223:352 */         return method.invoke(this.target, args);
/* 224:    */       }
/* 225:    */       catch (InvocationTargetException ex)
/* 226:    */       {
/* 227:355 */         throw ex.getTargetException();
/* 228:    */       }
/* 229:    */     }
/* 230:    */   }
/* 231:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.SingleConnectionDataSource
 * JD-Core Version:    0.7.0.1
 */