/*   1:    */ package org.springframework.jdbc.datasource;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.InvocationHandler;
/*   4:    */ import java.lang.reflect.InvocationTargetException;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import java.lang.reflect.Proxy;
/*   7:    */ import java.sql.Connection;
/*   8:    */ import java.sql.SQLException;
/*   9:    */ import javax.sql.DataSource;
/*  10:    */ import org.apache.commons.logging.Log;
/*  11:    */ import org.apache.commons.logging.LogFactory;
/*  12:    */ import org.springframework.core.Constants;
/*  13:    */ 
/*  14:    */ public class LazyConnectionDataSourceProxy
/*  15:    */   extends DelegatingDataSource
/*  16:    */ {
/*  17: 85 */   private static final Constants constants = new Constants(Connection.class);
/*  18: 87 */   private static final Log logger = LogFactory.getLog(LazyConnectionDataSourceProxy.class);
/*  19:    */   private Boolean defaultAutoCommit;
/*  20:    */   private Integer defaultTransactionIsolation;
/*  21:    */   
/*  22:    */   public LazyConnectionDataSourceProxy() {}
/*  23:    */   
/*  24:    */   public LazyConnectionDataSourceProxy(DataSource targetDataSource)
/*  25:    */   {
/*  26:106 */     setTargetDataSource(targetDataSource);
/*  27:107 */     afterPropertiesSet();
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setDefaultAutoCommit(boolean defaultAutoCommit)
/*  31:    */   {
/*  32:120 */     this.defaultAutoCommit = Boolean.valueOf(defaultAutoCommit);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setDefaultTransactionIsolation(int defaultTransactionIsolation)
/*  36:    */   {
/*  37:137 */     this.defaultTransactionIsolation = Integer.valueOf(defaultTransactionIsolation);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setDefaultTransactionIsolationName(String constantName)
/*  41:    */   {
/*  42:151 */     setDefaultTransactionIsolation(constants.asNumber(constantName).intValue());
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void afterPropertiesSet()
/*  46:    */   {
/*  47:157 */     super.afterPropertiesSet();
/*  48:161 */     if ((this.defaultAutoCommit == null) || (this.defaultTransactionIsolation == null)) {
/*  49:    */       try
/*  50:    */       {
/*  51:163 */         Connection con = getTargetDataSource().getConnection();
/*  52:    */         try
/*  53:    */         {
/*  54:165 */           checkDefaultConnectionProperties(con);
/*  55:    */         }
/*  56:    */         finally
/*  57:    */         {
/*  58:168 */           con.close();
/*  59:    */         }
/*  60:    */       }
/*  61:    */       catch (SQLException ex)
/*  62:    */       {
/*  63:172 */         logger.warn("Could not retrieve default auto-commit and transaction isolation settings", ex);
/*  64:    */       }
/*  65:    */     }
/*  66:    */   }
/*  67:    */   
/*  68:    */   protected synchronized void checkDefaultConnectionProperties(Connection con)
/*  69:    */     throws SQLException
/*  70:    */   {
/*  71:188 */     if (this.defaultAutoCommit == null) {
/*  72:189 */       this.defaultAutoCommit = Boolean.valueOf(con.getAutoCommit());
/*  73:    */     }
/*  74:191 */     if (this.defaultTransactionIsolation == null) {
/*  75:192 */       this.defaultTransactionIsolation = Integer.valueOf(con.getTransactionIsolation());
/*  76:    */     }
/*  77:    */   }
/*  78:    */   
/*  79:    */   protected Boolean defaultAutoCommit()
/*  80:    */   {
/*  81:200 */     return this.defaultAutoCommit;
/*  82:    */   }
/*  83:    */   
/*  84:    */   protected Integer defaultTransactionIsolation()
/*  85:    */   {
/*  86:207 */     return this.defaultTransactionIsolation;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public Connection getConnection()
/*  90:    */     throws SQLException
/*  91:    */   {
/*  92:221 */     return (Connection)Proxy.newProxyInstance(
/*  93:222 */       ConnectionProxy.class.getClassLoader(), 
/*  94:223 */       new Class[] { ConnectionProxy.class }, 
/*  95:224 */       new LazyConnectionInvocationHandler());
/*  96:    */   }
/*  97:    */   
/*  98:    */   public Connection getConnection(String username, String password)
/*  99:    */     throws SQLException
/* 100:    */   {
/* 101:239 */     return (Connection)Proxy.newProxyInstance(
/* 102:240 */       ConnectionProxy.class.getClassLoader(), 
/* 103:241 */       new Class[] { ConnectionProxy.class }, 
/* 104:242 */       new LazyConnectionInvocationHandler(username, password));
/* 105:    */   }
/* 106:    */   
/* 107:    */   private class LazyConnectionInvocationHandler
/* 108:    */     implements InvocationHandler
/* 109:    */   {
/* 110:256 */     private Boolean readOnly = Boolean.FALSE;
/* 111:262 */     private boolean closed = false;
/* 112:267 */     private Boolean autoCommit = LazyConnectionDataSourceProxy.this.defaultAutoCommit();
/* 113:268 */     private Integer transactionIsolation = LazyConnectionDataSourceProxy.this.defaultTransactionIsolation();
/* 114:    */     private String username;
/* 115:    */     private String password;
/* 116:    */     private Connection target;
/* 117:    */     
/* 118:    */     public LazyConnectionInvocationHandler() {}
/* 119:    */     
/* 120:    */     public LazyConnectionInvocationHandler(String username, String password)
/* 121:    */     {
/* 122:272 */       this();
/* 123:273 */       this.username = username;
/* 124:274 */       this.password = password;
/* 125:    */     }
/* 126:    */     
/* 127:    */     public Object invoke(Object proxy, Method method, Object[] args)
/* 128:    */       throws Throwable
/* 129:    */     {
/* 130:280 */       if (method.getName().equals("equals"))
/* 131:    */       {
/* 132:283 */         if (proxy == args[0]) {
/* 133:283 */           return Boolean.valueOf(true);
/* 134:    */         }
/* 135:283 */         return Boolean.valueOf(false);
/* 136:    */       }
/* 137:285 */       if (method.getName().equals("hashCode")) {
/* 138:289 */         return Integer.valueOf(System.identityHashCode(proxy));
/* 139:    */       }
/* 140:291 */       if (method.getName().equals("unwrap"))
/* 141:    */       {
/* 142:292 */         if (((Class)args[0]).isInstance(proxy)) {
/* 143:293 */           return proxy;
/* 144:    */         }
/* 145:    */       }
/* 146:296 */       else if (method.getName().equals("isWrapperFor"))
/* 147:    */       {
/* 148:297 */         if (((Class)args[0]).isInstance(proxy)) {
/* 149:298 */           return Boolean.valueOf(true);
/* 150:    */         }
/* 151:    */       }
/* 152:301 */       else if (method.getName().equals("getTargetConnection")) {
/* 153:303 */         return getTargetConnection(method);
/* 154:    */       }
/* 155:306 */       if (!hasTargetConnection())
/* 156:    */       {
/* 157:311 */         if (method.getName().equals("toString")) {
/* 158:312 */           return "Lazy Connection proxy for target DataSource [" + LazyConnectionDataSourceProxy.this.getTargetDataSource() + "]";
/* 159:    */         }
/* 160:314 */         if (method.getName().equals("isReadOnly")) {
/* 161:315 */           return this.readOnly;
/* 162:    */         }
/* 163:317 */         if (method.getName().equals("setReadOnly"))
/* 164:    */         {
/* 165:318 */           this.readOnly = ((Boolean)args[0]);
/* 166:319 */           return null;
/* 167:    */         }
/* 168:321 */         if (method.getName().equals("getTransactionIsolation"))
/* 169:    */         {
/* 170:322 */           if (this.transactionIsolation != null) {
/* 171:323 */             return this.transactionIsolation;
/* 172:    */           }
/* 173:    */         }
/* 174:    */         else
/* 175:    */         {
/* 176:328 */           if (method.getName().equals("setTransactionIsolation"))
/* 177:    */           {
/* 178:329 */             this.transactionIsolation = ((Integer)args[0]);
/* 179:330 */             return null;
/* 180:    */           }
/* 181:332 */           if (method.getName().equals("getAutoCommit"))
/* 182:    */           {
/* 183:333 */             if (this.autoCommit != null) {
/* 184:334 */               return this.autoCommit;
/* 185:    */             }
/* 186:    */           }
/* 187:    */           else
/* 188:    */           {
/* 189:339 */             if (method.getName().equals("setAutoCommit"))
/* 190:    */             {
/* 191:340 */               this.autoCommit = ((Boolean)args[0]);
/* 192:341 */               return null;
/* 193:    */             }
/* 194:343 */             if (method.getName().equals("commit")) {
/* 195:345 */               return null;
/* 196:    */             }
/* 197:347 */             if (method.getName().equals("rollback")) {
/* 198:349 */               return null;
/* 199:    */             }
/* 200:351 */             if (method.getName().equals("getWarnings")) {
/* 201:352 */               return null;
/* 202:    */             }
/* 203:354 */             if (method.getName().equals("clearWarnings")) {
/* 204:355 */               return null;
/* 205:    */             }
/* 206:357 */             if (method.getName().equals("close"))
/* 207:    */             {
/* 208:359 */               this.closed = true;
/* 209:360 */               return null;
/* 210:    */             }
/* 211:362 */             if (method.getName().equals("isClosed")) {
/* 212:363 */               return Boolean.valueOf(this.closed);
/* 213:    */             }
/* 214:365 */             if (this.closed) {
/* 215:368 */               throw new SQLException("Illegal operation: connection is closed");
/* 216:    */             }
/* 217:    */           }
/* 218:    */         }
/* 219:    */       }
/* 220:    */       try
/* 221:    */       {
/* 222:376 */         return method.invoke(getTargetConnection(method), args);
/* 223:    */       }
/* 224:    */       catch (InvocationTargetException ex)
/* 225:    */       {
/* 226:379 */         throw ex.getTargetException();
/* 227:    */       }
/* 228:    */     }
/* 229:    */     
/* 230:    */     private boolean hasTargetConnection()
/* 231:    */     {
/* 232:387 */       return this.target != null;
/* 233:    */     }
/* 234:    */     
/* 235:    */     private Connection getTargetConnection(Method operation)
/* 236:    */       throws SQLException
/* 237:    */     {
/* 238:394 */       if (this.target == null)
/* 239:    */       {
/* 240:396 */         if (LazyConnectionDataSourceProxy.logger.isDebugEnabled()) {
/* 241:397 */           LazyConnectionDataSourceProxy.logger.debug("Connecting to database for operation '" + operation.getName() + "'");
/* 242:    */         }
/* 243:401 */         this.target = (this.username != null ? 
/* 244:402 */           LazyConnectionDataSourceProxy.this.getTargetDataSource().getConnection(this.username, this.password) : 
/* 245:403 */           LazyConnectionDataSourceProxy.this.getTargetDataSource().getConnection());
/* 246:    */         
/* 247:    */ 
/* 248:406 */         LazyConnectionDataSourceProxy.this.checkDefaultConnectionProperties(this.target);
/* 249:409 */         if (this.readOnly.booleanValue()) {
/* 250:410 */           this.target.setReadOnly(this.readOnly.booleanValue());
/* 251:    */         }
/* 252:412 */         if ((this.transactionIsolation != null) && 
/* 253:413 */           (!this.transactionIsolation.equals(LazyConnectionDataSourceProxy.this.defaultTransactionIsolation()))) {
/* 254:414 */           this.target.setTransactionIsolation(this.transactionIsolation.intValue());
/* 255:    */         }
/* 256:416 */         if ((this.autoCommit != null) && (this.autoCommit.booleanValue() != this.target.getAutoCommit())) {
/* 257:417 */           this.target.setAutoCommit(this.autoCommit.booleanValue());
/* 258:    */         }
/* 259:    */       }
/* 260:423 */       else if (LazyConnectionDataSourceProxy.logger.isDebugEnabled())
/* 261:    */       {
/* 262:424 */         LazyConnectionDataSourceProxy.logger.debug("Using existing database connection for operation '" + operation.getName() + "'");
/* 263:    */       }
/* 264:428 */       return this.target;
/* 265:    */     }
/* 266:    */   }
/* 267:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
 * JD-Core Version:    0.7.0.1
 */