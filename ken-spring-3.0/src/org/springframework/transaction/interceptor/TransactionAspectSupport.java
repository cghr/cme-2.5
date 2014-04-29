/*   1:    */ package org.springframework.transaction.interceptor;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.Properties;
/*   5:    */ import org.apache.commons.logging.Log;
/*   6:    */ import org.apache.commons.logging.LogFactory;
/*   7:    */ import org.springframework.beans.factory.BeanFactory;
/*   8:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*   9:    */ import org.springframework.beans.factory.BeanFactoryUtils;
/*  10:    */ import org.springframework.beans.factory.InitializingBean;
/*  11:    */ import org.springframework.beans.factory.ListableBeanFactory;
/*  12:    */ import org.springframework.core.NamedThreadLocal;
/*  13:    */ import org.springframework.transaction.NoTransactionException;
/*  14:    */ import org.springframework.transaction.PlatformTransactionManager;
/*  15:    */ import org.springframework.transaction.TransactionStatus;
/*  16:    */ import org.springframework.transaction.TransactionSystemException;
/*  17:    */ import org.springframework.util.ClassUtils;
/*  18:    */ import org.springframework.util.StringUtils;
/*  19:    */ 
/*  20:    */ public abstract class TransactionAspectSupport
/*  21:    */   implements BeanFactoryAware, InitializingBean
/*  22:    */ {
/*  23: 77 */   private static final ThreadLocal<TransactionInfo> transactionInfoHolder = new NamedThreadLocal("Current aspect-driven transaction");
/*  24:    */   
/*  25:    */   protected static TransactionInfo currentTransactionInfo()
/*  26:    */     throws NoTransactionException
/*  27:    */   {
/*  28: 98 */     return (TransactionInfo)transactionInfoHolder.get();
/*  29:    */   }
/*  30:    */   
/*  31:    */   public static TransactionStatus currentTransactionStatus()
/*  32:    */     throws NoTransactionException
/*  33:    */   {
/*  34:109 */     TransactionInfo info = currentTransactionInfo();
/*  35:110 */     if (info == null) {
/*  36:111 */       throw new NoTransactionException("No transaction aspect-managed TransactionStatus in scope");
/*  37:    */     }
/*  38:113 */     return currentTransactionInfo().transactionStatus;
/*  39:    */   }
/*  40:    */   
/*  41:117 */   protected final Log logger = LogFactory.getLog(getClass());
/*  42:    */   private String transactionManagerBeanName;
/*  43:    */   private PlatformTransactionManager transactionManager;
/*  44:    */   private TransactionAttributeSource transactionAttributeSource;
/*  45:    */   private BeanFactory beanFactory;
/*  46:    */   
/*  47:    */   public void setTransactionManagerBeanName(String transactionManagerBeanName)
/*  48:    */   {
/*  49:132 */     this.transactionManagerBeanName = transactionManagerBeanName;
/*  50:    */   }
/*  51:    */   
/*  52:    */   protected final String getTransactionManagerBeanName()
/*  53:    */   {
/*  54:139 */     return this.transactionManagerBeanName;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setTransactionManager(PlatformTransactionManager transactionManager)
/*  58:    */   {
/*  59:146 */     this.transactionManager = transactionManager;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public PlatformTransactionManager getTransactionManager()
/*  63:    */   {
/*  64:153 */     return this.transactionManager;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setTransactionAttributes(Properties transactionAttributes)
/*  68:    */   {
/*  69:169 */     NameMatchTransactionAttributeSource tas = new NameMatchTransactionAttributeSource();
/*  70:170 */     tas.setProperties(transactionAttributes);
/*  71:171 */     this.transactionAttributeSource = tas;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void setTransactionAttributeSources(TransactionAttributeSource[] transactionAttributeSources)
/*  75:    */   {
/*  76:183 */     this.transactionAttributeSource = new CompositeTransactionAttributeSource(transactionAttributeSources);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void setTransactionAttributeSource(TransactionAttributeSource transactionAttributeSource)
/*  80:    */   {
/*  81:196 */     this.transactionAttributeSource = transactionAttributeSource;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public TransactionAttributeSource getTransactionAttributeSource()
/*  85:    */   {
/*  86:203 */     return this.transactionAttributeSource;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void setBeanFactory(BeanFactory beanFactory)
/*  90:    */   {
/*  91:210 */     this.beanFactory = beanFactory;
/*  92:    */   }
/*  93:    */   
/*  94:    */   protected final BeanFactory getBeanFactory()
/*  95:    */   {
/*  96:217 */     return this.beanFactory;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void afterPropertiesSet()
/* 100:    */   {
/* 101:224 */     if ((this.transactionManager == null) && (this.beanFactory == null)) {
/* 102:225 */       throw new IllegalStateException(
/* 103:226 */         "Setting the property 'transactionManager' or running in a ListableBeanFactory is required");
/* 104:    */     }
/* 105:228 */     if (this.transactionAttributeSource == null) {
/* 106:229 */       throw new IllegalStateException(
/* 107:230 */         "Either 'transactionAttributeSource' or 'transactionAttributes' is required: If there are no transactional methods, then don't use a transaction aspect.");
/* 108:    */     }
/* 109:    */   }
/* 110:    */   
/* 111:    */   protected PlatformTransactionManager determineTransactionManager(TransactionAttribute txAttr)
/* 112:    */   {
/* 113:240 */     if ((this.transactionManager != null) || (this.beanFactory == null) || (txAttr == null)) {
/* 114:241 */       return this.transactionManager;
/* 115:    */     }
/* 116:243 */     String qualifier = txAttr.getQualifier();
/* 117:244 */     if (StringUtils.hasLength(qualifier)) {
/* 118:245 */       return TransactionAspectUtils.getTransactionManager(this.beanFactory, qualifier);
/* 119:    */     }
/* 120:247 */     if (this.transactionManagerBeanName != null) {
/* 121:248 */       return (PlatformTransactionManager)this.beanFactory.getBean(this.transactionManagerBeanName, PlatformTransactionManager.class);
/* 122:    */     }
/* 123:250 */     if ((this.beanFactory instanceof ListableBeanFactory)) {
/* 124:251 */       return (PlatformTransactionManager)BeanFactoryUtils.beanOfTypeIncludingAncestors((ListableBeanFactory)this.beanFactory, PlatformTransactionManager.class);
/* 125:    */     }
/* 126:254 */     throw new IllegalStateException(
/* 127:255 */       "Cannot retrieve PlatformTransactionManager beans from non-listable BeanFactory: " + this.beanFactory);
/* 128:    */   }
/* 129:    */   
/* 130:    */   protected TransactionInfo createTransactionIfNecessary(Method method, Class targetClass)
/* 131:    */   {
/* 132:271 */     TransactionAttribute txAttr = getTransactionAttributeSource().getTransactionAttribute(method, targetClass);
/* 133:272 */     PlatformTransactionManager tm = determineTransactionManager(txAttr);
/* 134:273 */     return createTransactionIfNecessary(tm, txAttr, methodIdentification(method, targetClass));
/* 135:    */   }
/* 136:    */   
/* 137:    */   protected String methodIdentification(Method method, Class targetClass)
/* 138:    */   {
/* 139:286 */     String simpleMethodId = methodIdentification(method);
/* 140:287 */     if (simpleMethodId != null) {
/* 141:288 */       return simpleMethodId;
/* 142:    */     }
/* 143:290 */     Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
/* 144:291 */     return ClassUtils.getQualifiedMethodName(specificMethod);
/* 145:    */   }
/* 146:    */   
/* 147:    */   @Deprecated
/* 148:    */   protected String methodIdentification(Method method)
/* 149:    */   {
/* 150:304 */     return null;
/* 151:    */   }
/* 152:    */   
/* 153:    */   protected TransactionInfo createTransactionIfNecessary(PlatformTransactionManager tm, TransactionAttribute txAttr, final String joinpointIdentification)
/* 154:    */   {
/* 155:323 */     if ((txAttr != null) && (txAttr.getName() == null)) {
/* 156:324 */       txAttr = new DelegatingTransactionAttribute(txAttr)
/* 157:    */       {
/* 158:    */         public String getName()
/* 159:    */         {
/* 160:327 */           return joinpointIdentification;
/* 161:    */         }
/* 162:    */       };
/* 163:    */     }
/* 164:332 */     TransactionStatus status = null;
/* 165:333 */     if (txAttr != null) {
/* 166:334 */       if (tm != null) {
/* 167:335 */         status = tm.getTransaction(txAttr);
/* 168:338 */       } else if (this.logger.isDebugEnabled()) {
/* 169:339 */         this.logger.debug("Skipping transactional joinpoint [" + joinpointIdentification + 
/* 170:340 */           "] because no transaction manager has been configured");
/* 171:    */       }
/* 172:    */     }
/* 173:344 */     return prepareTransactionInfo(tm, txAttr, joinpointIdentification, status);
/* 174:    */   }
/* 175:    */   
/* 176:    */   protected TransactionInfo prepareTransactionInfo(PlatformTransactionManager tm, TransactionAttribute txAttr, String joinpointIdentification, TransactionStatus status)
/* 177:    */   {
/* 178:358 */     TransactionInfo txInfo = new TransactionInfo(tm, txAttr, joinpointIdentification);
/* 179:359 */     if (txAttr != null)
/* 180:    */     {
/* 181:361 */       if (this.logger.isTraceEnabled()) {
/* 182:362 */         this.logger.trace("Getting transaction for [" + txInfo.getJoinpointIdentification() + "]");
/* 183:    */       }
/* 184:365 */       txInfo.newTransactionStatus(status);
/* 185:    */     }
/* 186:371 */     else if (this.logger.isTraceEnabled())
/* 187:    */     {
/* 188:372 */       this.logger.trace("Don't need to create transaction for [" + joinpointIdentification + 
/* 189:373 */         "]: This method isn't transactional.");
/* 190:    */     }
/* 191:379 */     txInfo.bindToThread();
/* 192:380 */     return txInfo;
/* 193:    */   }
/* 194:    */   
/* 195:    */   protected void commitTransactionAfterReturning(TransactionInfo txInfo)
/* 196:    */   {
/* 197:389 */     if ((txInfo != null) && (txInfo.hasTransaction()))
/* 198:    */     {
/* 199:390 */       if (this.logger.isTraceEnabled()) {
/* 200:391 */         this.logger.trace("Completing transaction for [" + txInfo.getJoinpointIdentification() + "]");
/* 201:    */       }
/* 202:393 */       txInfo.getTransactionManager().commit(txInfo.getTransactionStatus());
/* 203:    */     }
/* 204:    */   }
/* 205:    */   
/* 206:    */   protected void completeTransactionAfterThrowing(TransactionInfo txInfo, Throwable ex)
/* 207:    */   {
/* 208:404 */     if ((txInfo != null) && (txInfo.hasTransaction()))
/* 209:    */     {
/* 210:405 */       if (this.logger.isTraceEnabled()) {
/* 211:406 */         this.logger.trace("Completing transaction for [" + txInfo.getJoinpointIdentification() + 
/* 212:407 */           "] after exception: " + ex);
/* 213:    */       }
/* 214:409 */       if (txInfo.transactionAttribute.rollbackOn(ex)) {
/* 215:    */         try
/* 216:    */         {
/* 217:411 */           txInfo.getTransactionManager().rollback(txInfo.getTransactionStatus());
/* 218:    */         }
/* 219:    */         catch (TransactionSystemException ex2)
/* 220:    */         {
/* 221:414 */           this.logger.error("Application exception overridden by rollback exception", ex);
/* 222:415 */           ex2.initApplicationException(ex);
/* 223:416 */           throw ex2;
/* 224:    */         }
/* 225:    */         catch (RuntimeException ex2)
/* 226:    */         {
/* 227:419 */           this.logger.error("Application exception overridden by rollback exception", ex);
/* 228:420 */           throw ex2;
/* 229:    */         }
/* 230:    */         catch (Error err)
/* 231:    */         {
/* 232:423 */           this.logger.error("Application exception overridden by rollback error", ex);
/* 233:424 */           throw err;
/* 234:    */         }
/* 235:    */       } else {
/* 236:    */         try
/* 237:    */         {
/* 238:431 */           txInfo.getTransactionManager().commit(txInfo.getTransactionStatus());
/* 239:    */         }
/* 240:    */         catch (TransactionSystemException ex2)
/* 241:    */         {
/* 242:434 */           this.logger.error("Application exception overridden by commit exception", ex);
/* 243:435 */           ex2.initApplicationException(ex);
/* 244:436 */           throw ex2;
/* 245:    */         }
/* 246:    */         catch (RuntimeException ex2)
/* 247:    */         {
/* 248:439 */           this.logger.error("Application exception overridden by commit exception", ex);
/* 249:440 */           throw ex2;
/* 250:    */         }
/* 251:    */         catch (Error err)
/* 252:    */         {
/* 253:443 */           this.logger.error("Application exception overridden by commit error", ex);
/* 254:444 */           throw err;
/* 255:    */         }
/* 256:    */       }
/* 257:    */     }
/* 258:    */   }
/* 259:    */   
/* 260:    */   protected void cleanupTransactionInfo(TransactionInfo txInfo)
/* 261:    */   {
/* 262:456 */     if (txInfo != null) {
/* 263:457 */       txInfo.restoreThreadLocalStatus();
/* 264:    */     }
/* 265:    */   }
/* 266:    */   
/* 267:    */   protected final class TransactionInfo
/* 268:    */   {
/* 269:    */     private final PlatformTransactionManager transactionManager;
/* 270:    */     private final TransactionAttribute transactionAttribute;
/* 271:    */     private final String joinpointIdentification;
/* 272:    */     private TransactionStatus transactionStatus;
/* 273:    */     private TransactionInfo oldTransactionInfo;
/* 274:    */     
/* 275:    */     public TransactionInfo(PlatformTransactionManager transactionManager, TransactionAttribute transactionAttribute, String joinpointIdentification)
/* 276:    */     {
/* 277:480 */       this.transactionManager = transactionManager;
/* 278:481 */       this.transactionAttribute = transactionAttribute;
/* 279:482 */       this.joinpointIdentification = joinpointIdentification;
/* 280:    */     }
/* 281:    */     
/* 282:    */     public PlatformTransactionManager getTransactionManager()
/* 283:    */     {
/* 284:486 */       return this.transactionManager;
/* 285:    */     }
/* 286:    */     
/* 287:    */     public TransactionAttribute getTransactionAttribute()
/* 288:    */     {
/* 289:490 */       return this.transactionAttribute;
/* 290:    */     }
/* 291:    */     
/* 292:    */     public String getJoinpointIdentification()
/* 293:    */     {
/* 294:498 */       return this.joinpointIdentification;
/* 295:    */     }
/* 296:    */     
/* 297:    */     public void newTransactionStatus(TransactionStatus status)
/* 298:    */     {
/* 299:502 */       this.transactionStatus = status;
/* 300:    */     }
/* 301:    */     
/* 302:    */     public TransactionStatus getTransactionStatus()
/* 303:    */     {
/* 304:506 */       return this.transactionStatus;
/* 305:    */     }
/* 306:    */     
/* 307:    */     public boolean hasTransaction()
/* 308:    */     {
/* 309:514 */       return this.transactionStatus != null;
/* 310:    */     }
/* 311:    */     
/* 312:    */     private void bindToThread()
/* 313:    */     {
/* 314:520 */       this.oldTransactionInfo = ((TransactionInfo)TransactionAspectSupport.transactionInfoHolder.get());
/* 315:521 */       TransactionAspectSupport.transactionInfoHolder.set(this);
/* 316:    */     }
/* 317:    */     
/* 318:    */     private void restoreThreadLocalStatus()
/* 319:    */     {
/* 320:527 */       TransactionAspectSupport.transactionInfoHolder.set(this.oldTransactionInfo);
/* 321:    */     }
/* 322:    */     
/* 323:    */     public String toString()
/* 324:    */     {
/* 325:532 */       return this.transactionAttribute.toString();
/* 326:    */     }
/* 327:    */   }
/* 328:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.interceptor.TransactionAspectSupport
 * JD-Core Version:    0.7.0.1
 */