/*    1:     */ package org.springframework.transaction.jta;
/*    2:     */ 
/*    3:     */ import java.io.IOException;
/*    4:     */ import java.io.ObjectInputStream;
/*    5:     */ import java.io.Serializable;
/*    6:     */ import java.util.List;
/*    7:     */ import java.util.Properties;
/*    8:     */ import javax.naming.NamingException;
/*    9:     */ import javax.transaction.HeuristicMixedException;
/*   10:     */ import javax.transaction.HeuristicRollbackException;
/*   11:     */ import javax.transaction.InvalidTransactionException;
/*   12:     */ import javax.transaction.NotSupportedException;
/*   13:     */ import javax.transaction.RollbackException;
/*   14:     */ import javax.transaction.Synchronization;
/*   15:     */ import javax.transaction.SystemException;
/*   16:     */ import javax.transaction.Transaction;
/*   17:     */ import javax.transaction.TransactionManager;
/*   18:     */ import javax.transaction.TransactionSynchronizationRegistry;
/*   19:     */ import javax.transaction.UserTransaction;
/*   20:     */ import org.apache.commons.logging.Log;
/*   21:     */ import org.springframework.beans.factory.InitializingBean;
/*   22:     */ import org.springframework.jndi.JndiTemplate;
/*   23:     */ import org.springframework.transaction.CannotCreateTransactionException;
/*   24:     */ import org.springframework.transaction.HeuristicCompletionException;
/*   25:     */ import org.springframework.transaction.IllegalTransactionStateException;
/*   26:     */ import org.springframework.transaction.InvalidIsolationLevelException;
/*   27:     */ import org.springframework.transaction.NestedTransactionNotSupportedException;
/*   28:     */ import org.springframework.transaction.TransactionDefinition;
/*   29:     */ import org.springframework.transaction.TransactionSuspensionNotSupportedException;
/*   30:     */ import org.springframework.transaction.TransactionSystemException;
/*   31:     */ import org.springframework.transaction.UnexpectedRollbackException;
/*   32:     */ import org.springframework.transaction.support.AbstractPlatformTransactionManager;
/*   33:     */ import org.springframework.transaction.support.DefaultTransactionStatus;
/*   34:     */ import org.springframework.transaction.support.TransactionSynchronization;
/*   35:     */ import org.springframework.util.Assert;
/*   36:     */ import org.springframework.util.StringUtils;
/*   37:     */ 
/*   38:     */ public class JtaTransactionManager
/*   39:     */   extends AbstractPlatformTransactionManager
/*   40:     */   implements TransactionFactory, InitializingBean, Serializable
/*   41:     */ {
/*   42:     */   public static final String DEFAULT_USER_TRANSACTION_NAME = "java:comp/UserTransaction";
/*   43: 139 */   public static final String[] FALLBACK_TRANSACTION_MANAGER_NAMES = { "java:comp/TransactionManager", "java:appserver/TransactionManager", 
/*   44: 140 */     "java:pm/TransactionManager", "java:/TransactionManager" };
/*   45:     */   public static final String DEFAULT_TRANSACTION_SYNCHRONIZATION_REGISTRY_NAME = "java:comp/TransactionSynchronizationRegistry";
/*   46:     */   private static final String TRANSACTION_SYNCHRONIZATION_REGISTRY_CLASS_NAME = "javax.transaction.TransactionSynchronizationRegistry";
/*   47:     */   private static Class<?> transactionSynchronizationRegistryClass;
/*   48:     */   
/*   49:     */   static
/*   50:     */   {
/*   51: 156 */     ClassLoader cl = JtaTransactionManager.class.getClassLoader();
/*   52:     */     try
/*   53:     */     {
/*   54: 158 */       transactionSynchronizationRegistryClass = cl.loadClass("javax.transaction.TransactionSynchronizationRegistry");
/*   55:     */     }
/*   56:     */     catch (ClassNotFoundException localClassNotFoundException) {}
/*   57:     */   }
/*   58:     */   
/*   59: 166 */   private transient JndiTemplate jndiTemplate = new JndiTemplate();
/*   60:     */   private transient UserTransaction userTransaction;
/*   61:     */   private String userTransactionName;
/*   62: 172 */   private boolean autodetectUserTransaction = true;
/*   63: 174 */   private boolean cacheUserTransaction = true;
/*   64: 176 */   private boolean userTransactionObtainedFromJndi = false;
/*   65:     */   private transient TransactionManager transactionManager;
/*   66:     */   private String transactionManagerName;
/*   67: 182 */   private boolean autodetectTransactionManager = true;
/*   68:     */   private String transactionSynchronizationRegistryName;
/*   69:     */   private transient Object transactionSynchronizationRegistry;
/*   70: 188 */   private boolean allowCustomIsolationLevels = false;
/*   71:     */   
/*   72:     */   public JtaTransactionManager()
/*   73:     */   {
/*   74: 201 */     setNestedTransactionAllowed(true);
/*   75:     */   }
/*   76:     */   
/*   77:     */   public JtaTransactionManager(UserTransaction userTransaction)
/*   78:     */   {
/*   79: 209 */     this();
/*   80: 210 */     Assert.notNull(userTransaction, "UserTransaction must not be null");
/*   81: 211 */     this.userTransaction = userTransaction;
/*   82:     */   }
/*   83:     */   
/*   84:     */   public JtaTransactionManager(UserTransaction userTransaction, TransactionManager transactionManager)
/*   85:     */   {
/*   86: 220 */     this();
/*   87: 221 */     Assert.notNull(userTransaction, "UserTransaction must not be null");
/*   88: 222 */     Assert.notNull(transactionManager, "TransactionManager must not be null");
/*   89: 223 */     this.userTransaction = userTransaction;
/*   90: 224 */     this.transactionManager = transactionManager;
/*   91:     */   }
/*   92:     */   
/*   93:     */   public JtaTransactionManager(TransactionManager transactionManager)
/*   94:     */   {
/*   95: 232 */     this();
/*   96: 233 */     Assert.notNull(transactionManager, "TransactionManager must not be null");
/*   97: 234 */     this.transactionManager = transactionManager;
/*   98: 235 */     this.userTransaction = buildUserTransaction(transactionManager);
/*   99:     */   }
/*  100:     */   
/*  101:     */   public void setJndiTemplate(JndiTemplate jndiTemplate)
/*  102:     */   {
/*  103: 244 */     Assert.notNull(jndiTemplate, "JndiTemplate must not be null");
/*  104: 245 */     this.jndiTemplate = jndiTemplate;
/*  105:     */   }
/*  106:     */   
/*  107:     */   public JndiTemplate getJndiTemplate()
/*  108:     */   {
/*  109: 252 */     return this.jndiTemplate;
/*  110:     */   }
/*  111:     */   
/*  112:     */   public void setJndiEnvironment(Properties jndiEnvironment)
/*  113:     */   {
/*  114: 261 */     this.jndiTemplate = new JndiTemplate(jndiEnvironment);
/*  115:     */   }
/*  116:     */   
/*  117:     */   public Properties getJndiEnvironment()
/*  118:     */   {
/*  119: 268 */     return this.jndiTemplate.getEnvironment();
/*  120:     */   }
/*  121:     */   
/*  122:     */   public void setUserTransaction(UserTransaction userTransaction)
/*  123:     */   {
/*  124: 280 */     this.userTransaction = userTransaction;
/*  125:     */   }
/*  126:     */   
/*  127:     */   public UserTransaction getUserTransaction()
/*  128:     */   {
/*  129: 287 */     return this.userTransaction;
/*  130:     */   }
/*  131:     */   
/*  132:     */   public void setUserTransactionName(String userTransactionName)
/*  133:     */   {
/*  134: 299 */     this.userTransactionName = userTransactionName;
/*  135:     */   }
/*  136:     */   
/*  137:     */   public void setAutodetectUserTransaction(boolean autodetectUserTransaction)
/*  138:     */   {
/*  139: 313 */     this.autodetectUserTransaction = autodetectUserTransaction;
/*  140:     */   }
/*  141:     */   
/*  142:     */   public void setCacheUserTransaction(boolean cacheUserTransaction)
/*  143:     */   {
/*  144: 329 */     this.cacheUserTransaction = cacheUserTransaction;
/*  145:     */   }
/*  146:     */   
/*  147:     */   public void setTransactionManager(TransactionManager transactionManager)
/*  148:     */   {
/*  149: 343 */     this.transactionManager = transactionManager;
/*  150:     */   }
/*  151:     */   
/*  152:     */   public TransactionManager getTransactionManager()
/*  153:     */   {
/*  154: 350 */     return this.transactionManager;
/*  155:     */   }
/*  156:     */   
/*  157:     */   public void setTransactionManagerName(String transactionManagerName)
/*  158:     */   {
/*  159: 364 */     this.transactionManagerName = transactionManagerName;
/*  160:     */   }
/*  161:     */   
/*  162:     */   public void setAutodetectTransactionManager(boolean autodetectTransactionManager)
/*  163:     */   {
/*  164: 380 */     this.autodetectTransactionManager = autodetectTransactionManager;
/*  165:     */   }
/*  166:     */   
/*  167:     */   public void setTransactionSynchronizationRegistryName(String transactionSynchronizationRegistryName)
/*  168:     */   {
/*  169: 391 */     this.transactionSynchronizationRegistryName = transactionSynchronizationRegistryName;
/*  170:     */   }
/*  171:     */   
/*  172:     */   public void setAllowCustomIsolationLevels(boolean allowCustomIsolationLevels)
/*  173:     */   {
/*  174: 403 */     this.allowCustomIsolationLevels = allowCustomIsolationLevels;
/*  175:     */   }
/*  176:     */   
/*  177:     */   public void afterPropertiesSet()
/*  178:     */     throws TransactionSystemException
/*  179:     */   {
/*  180: 412 */     initUserTransactionAndTransactionManager();
/*  181: 413 */     checkUserTransactionAndTransactionManager();
/*  182: 414 */     initTransactionSynchronizationRegistry();
/*  183:     */   }
/*  184:     */   
/*  185:     */   protected void initUserTransactionAndTransactionManager()
/*  186:     */     throws TransactionSystemException
/*  187:     */   {
/*  188: 423 */     if (this.userTransaction == null) {
/*  189: 424 */       if (StringUtils.hasLength(this.userTransactionName))
/*  190:     */       {
/*  191: 425 */         this.userTransaction = lookupUserTransaction(this.userTransactionName);
/*  192: 426 */         this.userTransactionObtainedFromJndi = true;
/*  193:     */       }
/*  194:     */       else
/*  195:     */       {
/*  196: 429 */         this.userTransaction = retrieveUserTransaction();
/*  197:     */       }
/*  198:     */     }
/*  199: 434 */     if (this.transactionManager == null) {
/*  200: 435 */       if (StringUtils.hasLength(this.transactionManagerName)) {
/*  201: 436 */         this.transactionManager = lookupTransactionManager(this.transactionManagerName);
/*  202:     */       } else {
/*  203: 439 */         this.transactionManager = retrieveTransactionManager();
/*  204:     */       }
/*  205:     */     }
/*  206: 444 */     if ((this.userTransaction == null) && (this.autodetectUserTransaction)) {
/*  207: 445 */       this.userTransaction = findUserTransaction();
/*  208:     */     }
/*  209: 450 */     if ((this.transactionManager == null) && (this.autodetectTransactionManager)) {
/*  210: 451 */       this.transactionManager = findTransactionManager(this.userTransaction);
/*  211:     */     }
/*  212: 455 */     if ((this.userTransaction == null) && (this.transactionManager != null)) {
/*  213: 456 */       this.userTransaction = buildUserTransaction(this.transactionManager);
/*  214:     */     }
/*  215:     */   }
/*  216:     */   
/*  217:     */   protected void checkUserTransactionAndTransactionManager()
/*  218:     */     throws IllegalStateException
/*  219:     */   {
/*  220: 467 */     if (this.userTransaction != null)
/*  221:     */     {
/*  222: 468 */       if (this.logger.isInfoEnabled()) {
/*  223: 469 */         this.logger.info("Using JTA UserTransaction: " + this.userTransaction);
/*  224:     */       }
/*  225:     */     }
/*  226:     */     else {
/*  227: 473 */       throw new IllegalStateException("No JTA UserTransaction available - specify either 'userTransaction' or 'userTransactionName' or 'transactionManager' or 'transactionManagerName'");
/*  228:     */     }
/*  229: 478 */     if (this.transactionManager != null)
/*  230:     */     {
/*  231: 479 */       if (this.logger.isInfoEnabled()) {
/*  232: 480 */         this.logger.info("Using JTA TransactionManager: " + this.transactionManager);
/*  233:     */       }
/*  234:     */     }
/*  235:     */     else {
/*  236: 484 */       this.logger.warn("No JTA TransactionManager found: transaction suspension not available");
/*  237:     */     }
/*  238:     */   }
/*  239:     */   
/*  240:     */   protected void initTransactionSynchronizationRegistry()
/*  241:     */   {
/*  242: 495 */     if (StringUtils.hasLength(this.transactionSynchronizationRegistryName))
/*  243:     */     {
/*  244: 496 */       this.transactionSynchronizationRegistry = 
/*  245: 497 */         lookupTransactionSynchronizationRegistry(this.transactionSynchronizationRegistryName);
/*  246:     */     }
/*  247:     */     else
/*  248:     */     {
/*  249: 500 */       this.transactionSynchronizationRegistry = retrieveTransactionSynchronizationRegistry();
/*  250: 501 */       if (this.transactionSynchronizationRegistry == null) {
/*  251: 502 */         this.transactionSynchronizationRegistry = 
/*  252: 503 */           findTransactionSynchronizationRegistry(this.userTransaction, this.transactionManager);
/*  253:     */       }
/*  254:     */     }
/*  255: 507 */     if ((this.transactionSynchronizationRegistry != null) && 
/*  256: 508 */       (this.logger.isInfoEnabled())) {
/*  257: 509 */       this.logger.info("Using JTA TransactionSynchronizationRegistry: " + this.transactionSynchronizationRegistry);
/*  258:     */     }
/*  259:     */   }
/*  260:     */   
/*  261:     */   protected UserTransaction buildUserTransaction(TransactionManager transactionManager)
/*  262:     */   {
/*  263: 521 */     if ((transactionManager instanceof UserTransaction)) {
/*  264: 522 */       return (UserTransaction)transactionManager;
/*  265:     */     }
/*  266: 525 */     return new UserTransactionAdapter(transactionManager);
/*  267:     */   }
/*  268:     */   
/*  269:     */   protected UserTransaction lookupUserTransaction(String userTransactionName)
/*  270:     */     throws TransactionSystemException
/*  271:     */   {
/*  272:     */     try
/*  273:     */     {
/*  274: 542 */       if (this.logger.isDebugEnabled()) {
/*  275: 543 */         this.logger.debug("Retrieving JTA UserTransaction from JNDI location [" + userTransactionName + "]");
/*  276:     */       }
/*  277: 545 */       return (UserTransaction)getJndiTemplate().lookup(userTransactionName, UserTransaction.class);
/*  278:     */     }
/*  279:     */     catch (NamingException ex)
/*  280:     */     {
/*  281: 548 */       throw new TransactionSystemException(
/*  282: 549 */         "JTA UserTransaction is not available at JNDI location [" + userTransactionName + "]", ex);
/*  283:     */     }
/*  284:     */   }
/*  285:     */   
/*  286:     */   protected TransactionManager lookupTransactionManager(String transactionManagerName)
/*  287:     */     throws TransactionSystemException
/*  288:     */   {
/*  289:     */     try
/*  290:     */     {
/*  291: 566 */       if (this.logger.isDebugEnabled()) {
/*  292: 567 */         this.logger.debug("Retrieving JTA TransactionManager from JNDI location [" + transactionManagerName + "]");
/*  293:     */       }
/*  294: 569 */       return (TransactionManager)getJndiTemplate().lookup(transactionManagerName, TransactionManager.class);
/*  295:     */     }
/*  296:     */     catch (NamingException ex)
/*  297:     */     {
/*  298: 572 */       throw new TransactionSystemException(
/*  299: 573 */         "JTA TransactionManager is not available at JNDI location [" + transactionManagerName + "]", ex);
/*  300:     */     }
/*  301:     */   }
/*  302:     */   
/*  303:     */   protected Object lookupTransactionSynchronizationRegistry(String registryName)
/*  304:     */     throws TransactionSystemException
/*  305:     */   {
/*  306: 588 */     if (transactionSynchronizationRegistryClass == null) {
/*  307: 589 */       throw new TransactionSystemException(
/*  308: 590 */         "JTA 1.1 [javax.transaction.TransactionSynchronizationRegistry] API not available");
/*  309:     */     }
/*  310:     */     try
/*  311:     */     {
/*  312: 593 */       if (this.logger.isDebugEnabled()) {
/*  313: 594 */         this.logger.debug("Retrieving JTA TransactionSynchronizationRegistry from JNDI location [" + registryName + "]");
/*  314:     */       }
/*  315: 596 */       return getJndiTemplate().lookup(registryName, transactionSynchronizationRegistryClass);
/*  316:     */     }
/*  317:     */     catch (NamingException ex)
/*  318:     */     {
/*  319: 599 */       throw new TransactionSystemException(
/*  320: 600 */         "JTA TransactionSynchronizationRegistry is not available at JNDI location [" + registryName + "]", ex);
/*  321:     */     }
/*  322:     */   }
/*  323:     */   
/*  324:     */   protected UserTransaction retrieveUserTransaction()
/*  325:     */     throws TransactionSystemException
/*  326:     */   {
/*  327: 614 */     return null;
/*  328:     */   }
/*  329:     */   
/*  330:     */   protected TransactionManager retrieveTransactionManager()
/*  331:     */     throws TransactionSystemException
/*  332:     */   {
/*  333: 627 */     return null;
/*  334:     */   }
/*  335:     */   
/*  336:     */   protected Object retrieveTransactionSynchronizationRegistry()
/*  337:     */     throws TransactionSystemException
/*  338:     */   {
/*  339: 639 */     return null;
/*  340:     */   }
/*  341:     */   
/*  342:     */   protected UserTransaction findUserTransaction()
/*  343:     */   {
/*  344: 649 */     String jndiName = "java:comp/UserTransaction";
/*  345:     */     try
/*  346:     */     {
/*  347: 651 */       UserTransaction ut = (UserTransaction)getJndiTemplate().lookup(jndiName, UserTransaction.class);
/*  348: 652 */       if (this.logger.isDebugEnabled()) {
/*  349: 653 */         this.logger.debug("JTA UserTransaction found at default JNDI location [" + jndiName + "]");
/*  350:     */       }
/*  351: 655 */       this.userTransactionObtainedFromJndi = true;
/*  352: 656 */       return ut;
/*  353:     */     }
/*  354:     */     catch (NamingException ex)
/*  355:     */     {
/*  356: 659 */       if (this.logger.isDebugEnabled()) {
/*  357: 660 */         this.logger.debug("No JTA UserTransaction found at default JNDI location [" + jndiName + "]", ex);
/*  358:     */       }
/*  359:     */     }
/*  360: 662 */     return null;
/*  361:     */   }
/*  362:     */   
/*  363:     */   protected TransactionManager findTransactionManager(UserTransaction ut)
/*  364:     */   {
/*  365: 675 */     if ((ut instanceof TransactionManager))
/*  366:     */     {
/*  367: 676 */       if (this.logger.isDebugEnabled()) {
/*  368: 677 */         this.logger.debug("JTA UserTransaction object [" + ut + "] implements TransactionManager");
/*  369:     */       }
/*  370: 679 */       return (TransactionManager)ut;
/*  371:     */     }
/*  372: 683 */     for (String jndiName : FALLBACK_TRANSACTION_MANAGER_NAMES) {
/*  373:     */       try
/*  374:     */       {
/*  375: 685 */         TransactionManager tm = (TransactionManager)getJndiTemplate().lookup(jndiName, TransactionManager.class);
/*  376: 686 */         if (this.logger.isDebugEnabled()) {
/*  377: 687 */           this.logger.debug("JTA TransactionManager found at fallback JNDI location [" + jndiName + "]");
/*  378:     */         }
/*  379: 689 */         return tm;
/*  380:     */       }
/*  381:     */       catch (NamingException ex)
/*  382:     */       {
/*  383: 692 */         if (this.logger.isDebugEnabled()) {
/*  384: 693 */           this.logger.debug("No JTA TransactionManager found at fallback JNDI location [" + jndiName + "]", ex);
/*  385:     */         }
/*  386:     */       }
/*  387:     */     }
/*  388: 699 */     return null;
/*  389:     */   }
/*  390:     */   
/*  391:     */   protected Object findTransactionSynchronizationRegistry(UserTransaction ut, TransactionManager tm)
/*  392:     */     throws TransactionSystemException
/*  393:     */   {
/*  394: 716 */     if (transactionSynchronizationRegistryClass == null)
/*  395:     */     {
/*  396: 718 */       if (this.logger.isDebugEnabled()) {
/*  397: 719 */         this.logger.debug("JTA 1.1 [javax.transaction.TransactionSynchronizationRegistry] API not available");
/*  398:     */       }
/*  399: 721 */       return null;
/*  400:     */     }
/*  401: 725 */     if (this.userTransactionObtainedFromJndi)
/*  402:     */     {
/*  403: 728 */       String jndiName = "java:comp/TransactionSynchronizationRegistry";
/*  404:     */       try
/*  405:     */       {
/*  406: 730 */         Object tsr = getJndiTemplate().lookup(jndiName, transactionSynchronizationRegistryClass);
/*  407: 731 */         if (this.logger.isDebugEnabled()) {
/*  408: 732 */           this.logger.debug("JTA TransactionSynchronizationRegistry found at default JNDI location [" + jndiName + "]");
/*  409:     */         }
/*  410: 734 */         return tsr;
/*  411:     */       }
/*  412:     */       catch (NamingException ex)
/*  413:     */       {
/*  414: 737 */         if (this.logger.isDebugEnabled()) {
/*  415: 738 */           this.logger.debug(
/*  416: 739 */             "No JTA TransactionSynchronizationRegistry found at default JNDI location [" + jndiName + "]", ex);
/*  417:     */         }
/*  418:     */       }
/*  419:     */     }
/*  420: 744 */     if (transactionSynchronizationRegistryClass.isInstance(ut)) {
/*  421: 745 */       return ut;
/*  422:     */     }
/*  423: 747 */     if (transactionSynchronizationRegistryClass.isInstance(tm)) {
/*  424: 748 */       return tm;
/*  425:     */     }
/*  426: 752 */     return null;
/*  427:     */   }
/*  428:     */   
/*  429:     */   protected Object doGetTransaction()
/*  430:     */   {
/*  431: 768 */     UserTransaction ut = getUserTransaction();
/*  432: 769 */     if (ut == null) {
/*  433: 770 */       throw new CannotCreateTransactionException("No JTA UserTransaction available - programmatic PlatformTransactionManager.getTransaction usage not supported");
/*  434:     */     }
/*  435: 773 */     if (!this.cacheUserTransaction) {
/*  436: 774 */       ut = lookupUserTransaction(
/*  437: 775 */         this.userTransactionName != null ? this.userTransactionName : "java:comp/UserTransaction");
/*  438:     */     }
/*  439: 777 */     return doGetJtaTransaction(ut);
/*  440:     */   }
/*  441:     */   
/*  442:     */   protected JtaTransactionObject doGetJtaTransaction(UserTransaction ut)
/*  443:     */   {
/*  444: 788 */     return new JtaTransactionObject(ut);
/*  445:     */   }
/*  446:     */   
/*  447:     */   protected boolean isExistingTransaction(Object transaction)
/*  448:     */   {
/*  449: 793 */     JtaTransactionObject txObject = (JtaTransactionObject)transaction;
/*  450:     */     try
/*  451:     */     {
/*  452: 795 */       return txObject.getUserTransaction().getStatus() != 6;
/*  453:     */     }
/*  454:     */     catch (SystemException ex)
/*  455:     */     {
/*  456: 798 */       throw new TransactionSystemException("JTA failure on getStatus", ex);
/*  457:     */     }
/*  458:     */   }
/*  459:     */   
/*  460:     */   protected boolean useSavepointForNestedTransaction()
/*  461:     */   {
/*  462: 812 */     return false;
/*  463:     */   }
/*  464:     */   
/*  465:     */   protected void doBegin(Object transaction, TransactionDefinition definition)
/*  466:     */   {
/*  467: 818 */     JtaTransactionObject txObject = (JtaTransactionObject)transaction;
/*  468:     */     try
/*  469:     */     {
/*  470: 820 */       doJtaBegin(txObject, definition);
/*  471:     */     }
/*  472:     */     catch (NotSupportedException ex)
/*  473:     */     {
/*  474: 824 */       throw new NestedTransactionNotSupportedException(
/*  475: 825 */         "JTA implementation does not support nested transactions", ex);
/*  476:     */     }
/*  477:     */     catch (UnsupportedOperationException ex)
/*  478:     */     {
/*  479: 829 */       throw new NestedTransactionNotSupportedException(
/*  480: 830 */         "JTA implementation does not support nested transactions", ex);
/*  481:     */     }
/*  482:     */     catch (SystemException ex)
/*  483:     */     {
/*  484: 833 */       throw new CannotCreateTransactionException("JTA failure on begin", ex);
/*  485:     */     }
/*  486:     */   }
/*  487:     */   
/*  488:     */   protected void doJtaBegin(JtaTransactionObject txObject, TransactionDefinition definition)
/*  489:     */     throws NotSupportedException, SystemException
/*  490:     */   {
/*  491: 860 */     applyIsolationLevel(txObject, definition.getIsolationLevel());
/*  492: 861 */     int timeout = determineTimeout(definition);
/*  493: 862 */     applyTimeout(txObject, timeout);
/*  494: 863 */     txObject.getUserTransaction().begin();
/*  495:     */   }
/*  496:     */   
/*  497:     */   protected void applyIsolationLevel(JtaTransactionObject txObject, int isolationLevel)
/*  498:     */     throws InvalidIsolationLevelException, SystemException
/*  499:     */   {
/*  500: 883 */     if ((!this.allowCustomIsolationLevels) && (isolationLevel != -1)) {
/*  501: 884 */       throw new InvalidIsolationLevelException(
/*  502: 885 */         "JtaTransactionManager does not support custom isolation levels by default - switch 'allowCustomIsolationLevels' to 'true'");
/*  503:     */     }
/*  504:     */   }
/*  505:     */   
/*  506:     */   protected void applyTimeout(JtaTransactionObject txObject, int timeout)
/*  507:     */     throws SystemException
/*  508:     */   {
/*  509: 901 */     if (timeout > -1) {
/*  510: 902 */       txObject.getUserTransaction().setTransactionTimeout(timeout);
/*  511:     */     }
/*  512:     */   }
/*  513:     */   
/*  514:     */   protected Object doSuspend(Object transaction)
/*  515:     */   {
/*  516: 909 */     JtaTransactionObject txObject = (JtaTransactionObject)transaction;
/*  517:     */     try
/*  518:     */     {
/*  519: 911 */       return doJtaSuspend(txObject);
/*  520:     */     }
/*  521:     */     catch (SystemException ex)
/*  522:     */     {
/*  523: 914 */       throw new TransactionSystemException("JTA failure on suspend", ex);
/*  524:     */     }
/*  525:     */   }
/*  526:     */   
/*  527:     */   protected Object doJtaSuspend(JtaTransactionObject txObject)
/*  528:     */     throws SystemException
/*  529:     */   {
/*  530: 928 */     if (getTransactionManager() == null) {
/*  531: 929 */       throw new TransactionSuspensionNotSupportedException(
/*  532: 930 */         "JtaTransactionManager needs a JTA TransactionManager for suspending a transaction: specify the 'transactionManager' or 'transactionManagerName' property");
/*  533:     */     }
/*  534: 933 */     return getTransactionManager().suspend();
/*  535:     */   }
/*  536:     */   
/*  537:     */   protected void doResume(Object transaction, Object suspendedResources)
/*  538:     */   {
/*  539: 938 */     JtaTransactionObject txObject = (JtaTransactionObject)transaction;
/*  540:     */     try
/*  541:     */     {
/*  542: 940 */       doJtaResume(txObject, suspendedResources);
/*  543:     */     }
/*  544:     */     catch (InvalidTransactionException ex)
/*  545:     */     {
/*  546: 943 */       throw new IllegalTransactionStateException("Tried to resume invalid JTA transaction", ex);
/*  547:     */     }
/*  548:     */     catch (IllegalStateException ex)
/*  549:     */     {
/*  550: 946 */       throw new TransactionSystemException("Unexpected internal transaction state", ex);
/*  551:     */     }
/*  552:     */     catch (SystemException ex)
/*  553:     */     {
/*  554: 949 */       throw new TransactionSystemException("JTA failure on resume", ex);
/*  555:     */     }
/*  556:     */   }
/*  557:     */   
/*  558:     */   protected void doJtaResume(JtaTransactionObject txObject, Object suspendedTransaction)
/*  559:     */     throws InvalidTransactionException, SystemException
/*  560:     */   {
/*  561: 966 */     if (getTransactionManager() == null) {
/*  562: 967 */       throw new TransactionSuspensionNotSupportedException(
/*  563: 968 */         "JtaTransactionManager needs a JTA TransactionManager for suspending a transaction: specify the 'transactionManager' or 'transactionManagerName' property");
/*  564:     */     }
/*  565: 971 */     getTransactionManager().resume((Transaction)suspendedTransaction);
/*  566:     */   }
/*  567:     */   
/*  568:     */   protected boolean shouldCommitOnGlobalRollbackOnly()
/*  569:     */   {
/*  570: 981 */     return true;
/*  571:     */   }
/*  572:     */   
/*  573:     */   protected void doCommit(DefaultTransactionStatus status)
/*  574:     */   {
/*  575: 986 */     JtaTransactionObject txObject = (JtaTransactionObject)status.getTransaction();
/*  576:     */     try
/*  577:     */     {
/*  578: 988 */       int jtaStatus = txObject.getUserTransaction().getStatus();
/*  579: 989 */       if (jtaStatus == 6) {
/*  580: 993 */         throw new UnexpectedRollbackException("JTA transaction already completed - probably rolled back");
/*  581:     */       }
/*  582: 995 */       if (jtaStatus == 4)
/*  583:     */       {
/*  584:     */         try
/*  585:     */         {
/*  586:1000 */           txObject.getUserTransaction().rollback();
/*  587:     */         }
/*  588:     */         catch (IllegalStateException ex)
/*  589:     */         {
/*  590:1003 */           if (this.logger.isDebugEnabled()) {
/*  591:1004 */             this.logger.debug("Rollback failure with transaction already marked as rolled back: " + ex);
/*  592:     */           }
/*  593:     */         }
/*  594:1007 */         throw new UnexpectedRollbackException("JTA transaction already rolled back (probably due to a timeout)");
/*  595:     */       }
/*  596:1009 */       txObject.getUserTransaction().commit();
/*  597:     */     }
/*  598:     */     catch (RollbackException ex)
/*  599:     */     {
/*  600:1012 */       throw new UnexpectedRollbackException(
/*  601:1013 */         "JTA transaction unexpectedly rolled back (maybe due to a timeout)", ex);
/*  602:     */     }
/*  603:     */     catch (HeuristicMixedException ex)
/*  604:     */     {
/*  605:1016 */       throw new HeuristicCompletionException(3, ex);
/*  606:     */     }
/*  607:     */     catch (HeuristicRollbackException ex)
/*  608:     */     {
/*  609:1019 */       throw new HeuristicCompletionException(2, ex);
/*  610:     */     }
/*  611:     */     catch (IllegalStateException ex)
/*  612:     */     {
/*  613:1022 */       throw new TransactionSystemException("Unexpected internal transaction state", ex);
/*  614:     */     }
/*  615:     */     catch (SystemException ex)
/*  616:     */     {
/*  617:1025 */       throw new TransactionSystemException("JTA failure on commit", ex);
/*  618:     */     }
/*  619:     */   }
/*  620:     */   
/*  621:     */   protected void doRollback(DefaultTransactionStatus status)
/*  622:     */   {
/*  623:1031 */     JtaTransactionObject txObject = (JtaTransactionObject)status.getTransaction();
/*  624:     */     try
/*  625:     */     {
/*  626:1033 */       int jtaStatus = txObject.getUserTransaction().getStatus();
/*  627:1034 */       if (jtaStatus != 6) {
/*  628:     */         try
/*  629:     */         {
/*  630:1036 */           txObject.getUserTransaction().rollback();
/*  631:     */         }
/*  632:     */         catch (IllegalStateException ex)
/*  633:     */         {
/*  634:1039 */           if (jtaStatus == 4)
/*  635:     */           {
/*  636:1041 */             if (this.logger.isDebugEnabled()) {
/*  637:1042 */               this.logger.debug("Rollback failure with transaction already marked as rolled back: " + ex);
/*  638:     */             }
/*  639:     */           }
/*  640:     */           else {
/*  641:1046 */             throw new TransactionSystemException("Unexpected internal transaction state", ex);
/*  642:     */           }
/*  643:     */         }
/*  644:     */       }
/*  645:     */     }
/*  646:     */     catch (SystemException ex)
/*  647:     */     {
/*  648:1052 */       throw new TransactionSystemException("JTA failure on rollback", ex);
/*  649:     */     }
/*  650:     */   }
/*  651:     */   
/*  652:     */   protected void doSetRollbackOnly(DefaultTransactionStatus status)
/*  653:     */   {
/*  654:1058 */     JtaTransactionObject txObject = (JtaTransactionObject)status.getTransaction();
/*  655:1059 */     if (status.isDebug()) {
/*  656:1060 */       this.logger.debug("Setting JTA transaction rollback-only");
/*  657:     */     }
/*  658:     */     try
/*  659:     */     {
/*  660:1063 */       int jtaStatus = txObject.getUserTransaction().getStatus();
/*  661:1064 */       if ((jtaStatus != 6) && (jtaStatus != 4)) {
/*  662:1065 */         txObject.getUserTransaction().setRollbackOnly();
/*  663:     */       }
/*  664:     */     }
/*  665:     */     catch (IllegalStateException ex)
/*  666:     */     {
/*  667:1069 */       throw new TransactionSystemException("Unexpected internal transaction state", ex);
/*  668:     */     }
/*  669:     */     catch (SystemException ex)
/*  670:     */     {
/*  671:1072 */       throw new TransactionSystemException("JTA failure on setRollbackOnly", ex);
/*  672:     */     }
/*  673:     */   }
/*  674:     */   
/*  675:     */   protected void registerAfterCompletionWithExistingTransaction(Object transaction, List<TransactionSynchronization> synchronizations)
/*  676:     */   {
/*  677:1081 */     JtaTransactionObject txObject = (JtaTransactionObject)transaction;
/*  678:1082 */     this.logger.debug("Registering after-completion synchronization with existing JTA transaction");
/*  679:     */     try
/*  680:     */     {
/*  681:1084 */       doRegisterAfterCompletionWithJtaTransaction(txObject, synchronizations);
/*  682:     */     }
/*  683:     */     catch (SystemException ex)
/*  684:     */     {
/*  685:1087 */       throw new TransactionSystemException("JTA failure on registerSynchronization", ex);
/*  686:     */     }
/*  687:     */     catch (Exception ex)
/*  688:     */     {
/*  689:1091 */       if (((ex instanceof RollbackException)) || ((ex.getCause() instanceof RollbackException)))
/*  690:     */       {
/*  691:1092 */         this.logger.debug("Participating in existing JTA transaction that has been marked for rollback: cannot register Spring after-completion callbacks with outer JTA transaction - immediately performing Spring after-completion callbacks with outcome status 'rollback'. Original exception: " + 
/*  692:     */         
/*  693:     */ 
/*  694:1095 */           ex);
/*  695:1096 */         invokeAfterCompletion(synchronizations, 1);
/*  696:     */       }
/*  697:     */       else
/*  698:     */       {
/*  699:1099 */         this.logger.debug("Participating in existing JTA transaction, but unexpected internal transaction state encountered: cannot register Spring after-completion callbacks with outer JTA transaction - processing Spring after-completion callbacks with outcome status 'unknown'Original exception: " + 
/*  700:     */         
/*  701:     */ 
/*  702:1102 */           ex);
/*  703:1103 */         invokeAfterCompletion(synchronizations, 2);
/*  704:     */       }
/*  705:     */     }
/*  706:     */   }
/*  707:     */   
/*  708:     */   protected void doRegisterAfterCompletionWithJtaTransaction(JtaTransactionObject txObject, List<TransactionSynchronization> synchronizations)
/*  709:     */     throws RollbackException, SystemException
/*  710:     */   {
/*  711:1128 */     int jtaStatus = txObject.getUserTransaction().getStatus();
/*  712:1129 */     if (jtaStatus == 6) {
/*  713:1130 */       throw new RollbackException("JTA transaction already completed - probably rolled back");
/*  714:     */     }
/*  715:1132 */     if (jtaStatus == 4) {
/*  716:1133 */       throw new RollbackException("JTA transaction already rolled back (probably due to a timeout)");
/*  717:     */     }
/*  718:1136 */     if (this.transactionSynchronizationRegistry != null)
/*  719:     */     {
/*  720:1138 */       new InterposedSynchronizationDelegate(null).registerInterposedSynchronization(
/*  721:1139 */         new JtaAfterCompletionSynchronization(synchronizations));
/*  722:     */     }
/*  723:1142 */     else if (getTransactionManager() != null)
/*  724:     */     {
/*  725:1144 */       Transaction transaction = getTransactionManager().getTransaction();
/*  726:1145 */       if (transaction == null) {
/*  727:1146 */         throw new IllegalStateException("No JTA Transaction available");
/*  728:     */       }
/*  729:1148 */       transaction.registerSynchronization(new JtaAfterCompletionSynchronization(synchronizations));
/*  730:     */     }
/*  731:     */     else
/*  732:     */     {
/*  733:1153 */       this.logger.warn("Participating in existing JTA transaction, but no JTA TransactionManager available: cannot register Spring after-completion callbacks with outer JTA transaction - processing Spring after-completion callbacks with outcome status 'unknown'");
/*  734:     */       
/*  735:     */ 
/*  736:1156 */       invokeAfterCompletion(synchronizations, 2);
/*  737:     */     }
/*  738:     */   }
/*  739:     */   
/*  740:     */   public Transaction createTransaction(String name, int timeout)
/*  741:     */     throws NotSupportedException, SystemException
/*  742:     */   {
/*  743:1166 */     TransactionManager tm = getTransactionManager();
/*  744:1167 */     Assert.state(tm != null, "No JTA TransactionManager available");
/*  745:1168 */     if (timeout >= 0) {
/*  746:1169 */       tm.setTransactionTimeout(timeout);
/*  747:     */     }
/*  748:1171 */     tm.begin();
/*  749:1172 */     return new ManagedTransactionAdapter(tm);
/*  750:     */   }
/*  751:     */   
/*  752:     */   public boolean supportsResourceAdapterManagedTransactions()
/*  753:     */   {
/*  754:1176 */     return false;
/*  755:     */   }
/*  756:     */   
/*  757:     */   private void readObject(ObjectInputStream ois)
/*  758:     */     throws IOException, ClassNotFoundException
/*  759:     */   {
/*  760:1186 */     ois.defaultReadObject();
/*  761:     */     
/*  762:     */ 
/*  763:1189 */     this.jndiTemplate = new JndiTemplate();
/*  764:     */     
/*  765:     */ 
/*  766:1192 */     initUserTransactionAndTransactionManager();
/*  767:1193 */     initTransactionSynchronizationRegistry();
/*  768:     */   }
/*  769:     */   
/*  770:     */   private class InterposedSynchronizationDelegate
/*  771:     */   {
/*  772:     */     private InterposedSynchronizationDelegate() {}
/*  773:     */     
/*  774:     */     public void registerInterposedSynchronization(Synchronization synch)
/*  775:     */     {
/*  776:1204 */       ((TransactionSynchronizationRegistry)JtaTransactionManager.this.transactionSynchronizationRegistry).registerInterposedSynchronization(synch);
/*  777:     */     }
/*  778:     */   }
/*  779:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.jta.JtaTransactionManager
 * JD-Core Version:    0.7.0.1
 */