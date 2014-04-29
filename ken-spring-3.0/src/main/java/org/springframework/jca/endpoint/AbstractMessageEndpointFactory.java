/*   1:    */ package org.springframework.jca.endpoint;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import javax.resource.ResourceException;
/*   5:    */ import javax.resource.spi.ApplicationServerInternalException;
/*   6:    */ import javax.resource.spi.UnavailableException;
/*   7:    */ import javax.resource.spi.endpoint.MessageEndpoint;
/*   8:    */ import javax.resource.spi.endpoint.MessageEndpointFactory;
/*   9:    */ import javax.transaction.Transaction;
/*  10:    */ import javax.transaction.TransactionManager;
/*  11:    */ import javax.transaction.xa.XAResource;
/*  12:    */ import org.apache.commons.logging.Log;
/*  13:    */ import org.apache.commons.logging.LogFactory;
/*  14:    */ import org.springframework.transaction.jta.SimpleTransactionFactory;
/*  15:    */ import org.springframework.transaction.jta.TransactionFactory;
/*  16:    */ 
/*  17:    */ public abstract class AbstractMessageEndpointFactory
/*  18:    */   implements MessageEndpointFactory
/*  19:    */ {
/*  20: 48 */   protected final Log logger = LogFactory.getLog(getClass());
/*  21:    */   private TransactionFactory transactionFactory;
/*  22:    */   private String transactionName;
/*  23: 54 */   private int transactionTimeout = -1;
/*  24:    */   
/*  25:    */   public void setTransactionManager(Object transactionManager)
/*  26:    */   {
/*  27: 71 */     if ((transactionManager instanceof TransactionFactory)) {
/*  28: 72 */       this.transactionFactory = ((TransactionFactory)transactionManager);
/*  29: 74 */     } else if ((transactionManager instanceof TransactionManager)) {
/*  30: 75 */       this.transactionFactory = new SimpleTransactionFactory((TransactionManager)transactionManager);
/*  31:    */     } else {
/*  32: 78 */       throw new IllegalArgumentException("Transaction manager [" + transactionManager + 
/*  33: 79 */         "] is neither a [org.springframework.transaction.jta.TransactionFactory} nor a " + 
/*  34: 80 */         "[javax.transaction.TransactionManager]");
/*  35:    */     }
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setTransactionFactory(TransactionFactory transactionFactory)
/*  39:    */   {
/*  40: 97 */     this.transactionFactory = transactionFactory;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setTransactionName(String transactionName)
/*  44:    */   {
/*  45:106 */     this.transactionName = transactionName;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setTransactionTimeout(int transactionTimeout)
/*  49:    */   {
/*  50:116 */     this.transactionTimeout = transactionTimeout;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public boolean isDeliveryTransacted(Method method)
/*  54:    */     throws NoSuchMethodException
/*  55:    */   {
/*  56:127 */     return this.transactionFactory != null;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public MessageEndpoint createEndpoint(XAResource xaResource)
/*  60:    */     throws UnavailableException
/*  61:    */   {
/*  62:136 */     AbstractMessageEndpoint endpoint = createEndpointInternal();
/*  63:137 */     endpoint.initXAResource(xaResource);
/*  64:138 */     return endpoint;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public MessageEndpoint createEndpoint(XAResource xaResource, long timeout)
/*  68:    */     throws UnavailableException
/*  69:    */   {
/*  70:147 */     AbstractMessageEndpoint endpoint = createEndpointInternal();
/*  71:148 */     endpoint.initXAResource(xaResource);
/*  72:149 */     return endpoint;
/*  73:    */   }
/*  74:    */   
/*  75:    */   protected abstract AbstractMessageEndpoint createEndpointInternal()
/*  76:    */     throws UnavailableException;
/*  77:    */   
/*  78:    */   protected abstract class AbstractMessageEndpoint
/*  79:    */     implements MessageEndpoint
/*  80:    */   {
/*  81:    */     private AbstractMessageEndpointFactory.TransactionDelegate transactionDelegate;
/*  82:170 */     private boolean beforeDeliveryCalled = false;
/*  83:    */     private ClassLoader previousContextClassLoader;
/*  84:    */     
/*  85:    */     protected AbstractMessageEndpoint() {}
/*  86:    */     
/*  87:    */     void initXAResource(XAResource xaResource)
/*  88:    */     {
/*  89:179 */       this.transactionDelegate = new AbstractMessageEndpointFactory.TransactionDelegate(AbstractMessageEndpointFactory.this, xaResource);
/*  90:    */     }
/*  91:    */     
/*  92:    */     public void beforeDelivery(Method method)
/*  93:    */       throws ResourceException
/*  94:    */     {
/*  95:193 */       this.beforeDeliveryCalled = true;
/*  96:    */       try
/*  97:    */       {
/*  98:195 */         this.transactionDelegate.beginTransaction();
/*  99:    */       }
/* 100:    */       catch (Throwable ex)
/* 101:    */       {
/* 102:198 */         throw new ApplicationServerInternalException("Failed to begin transaction", ex);
/* 103:    */       }
/* 104:200 */       Thread currentThread = Thread.currentThread();
/* 105:201 */       this.previousContextClassLoader = currentThread.getContextClassLoader();
/* 106:202 */       currentThread.setContextClassLoader(getEndpointClassLoader());
/* 107:    */     }
/* 108:    */     
/* 109:    */     protected abstract ClassLoader getEndpointClassLoader();
/* 110:    */     
/* 111:    */     protected final boolean hasBeforeDeliveryBeenCalled()
/* 112:    */     {
/* 113:218 */       return this.beforeDeliveryCalled;
/* 114:    */     }
/* 115:    */     
/* 116:    */     protected final void onEndpointException(Throwable ex)
/* 117:    */     {
/* 118:229 */       this.transactionDelegate.setRollbackOnly();
/* 119:    */     }
/* 120:    */     
/* 121:    */     public void afterDelivery()
/* 122:    */       throws ResourceException
/* 123:    */     {
/* 124:240 */       this.beforeDeliveryCalled = false;
/* 125:241 */       Thread.currentThread().setContextClassLoader(this.previousContextClassLoader);
/* 126:242 */       this.previousContextClassLoader = null;
/* 127:    */       try
/* 128:    */       {
/* 129:244 */         this.transactionDelegate.endTransaction();
/* 130:    */       }
/* 131:    */       catch (Throwable ex)
/* 132:    */       {
/* 133:247 */         throw new ApplicationServerInternalException("Failed to complete transaction", ex);
/* 134:    */       }
/* 135:    */     }
/* 136:    */     
/* 137:    */     public void release()
/* 138:    */     {
/* 139:    */       try
/* 140:    */       {
/* 141:253 */         this.transactionDelegate.setRollbackOnly();
/* 142:254 */         this.transactionDelegate.endTransaction();
/* 143:    */       }
/* 144:    */       catch (Throwable ex)
/* 145:    */       {
/* 146:257 */         AbstractMessageEndpointFactory.this.logger.error("Could not complete unfinished transaction on endpoint release", ex);
/* 147:    */       }
/* 148:    */     }
/* 149:    */   }
/* 150:    */   
/* 151:    */   private class TransactionDelegate
/* 152:    */   {
/* 153:    */     private final XAResource xaResource;
/* 154:    */     private Transaction transaction;
/* 155:    */     private boolean rollbackOnly;
/* 156:    */     
/* 157:    */     public TransactionDelegate(XAResource xaResource)
/* 158:    */     {
/* 159:276 */       if ((xaResource == null) && 
/* 160:277 */         (AbstractMessageEndpointFactory.this.transactionFactory != null) && (!AbstractMessageEndpointFactory.this.transactionFactory.supportsResourceAdapterManagedTransactions())) {
/* 161:278 */         throw new IllegalStateException("ResourceAdapter-provided XAResource is required for transaction management. Check your ResourceAdapter's configuration.");
/* 162:    */       }
/* 163:282 */       this.xaResource = xaResource;
/* 164:    */     }
/* 165:    */     
/* 166:    */     public void beginTransaction()
/* 167:    */       throws Exception
/* 168:    */     {
/* 169:286 */       if ((AbstractMessageEndpointFactory.this.transactionFactory != null) && (this.xaResource != null))
/* 170:    */       {
/* 171:287 */         this.transaction = AbstractMessageEndpointFactory.this.transactionFactory.createTransaction(AbstractMessageEndpointFactory.this.transactionName, AbstractMessageEndpointFactory.this.transactionTimeout);
/* 172:288 */         this.transaction.enlistResource(this.xaResource);
/* 173:    */       }
/* 174:    */     }
/* 175:    */     
/* 176:    */     public void setRollbackOnly()
/* 177:    */     {
/* 178:293 */       if (this.transaction != null) {
/* 179:294 */         this.rollbackOnly = true;
/* 180:    */       }
/* 181:    */     }
/* 182:    */     
/* 183:    */     public void endTransaction()
/* 184:    */       throws Exception
/* 185:    */     {
/* 186:299 */       if (this.transaction != null) {
/* 187:    */         try
/* 188:    */         {
/* 189:301 */           if (this.rollbackOnly) {
/* 190:302 */             this.transaction.rollback();
/* 191:    */           } else {
/* 192:305 */             this.transaction.commit();
/* 193:    */           }
/* 194:    */         }
/* 195:    */         finally
/* 196:    */         {
/* 197:309 */           this.transaction = null;
/* 198:310 */           this.rollbackOnly = false;
/* 199:    */         }
/* 200:    */       }
/* 201:    */     }
/* 202:    */   }
/* 203:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.endpoint.AbstractMessageEndpointFactory
 * JD-Core Version:    0.7.0.1
 */