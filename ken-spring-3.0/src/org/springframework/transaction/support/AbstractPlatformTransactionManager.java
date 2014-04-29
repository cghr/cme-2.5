/*    1:     */ package org.springframework.transaction.support;
/*    2:     */ 
/*    3:     */ import java.io.IOException;
/*    4:     */ import java.io.ObjectInputStream;
/*    5:     */ import java.io.Serializable;
/*    6:     */ import java.util.List;
/*    7:     */ import org.apache.commons.logging.Log;
/*    8:     */ import org.apache.commons.logging.LogFactory;
/*    9:     */ import org.springframework.core.Constants;
/*   10:     */ import org.springframework.transaction.IllegalTransactionStateException;
/*   11:     */ import org.springframework.transaction.InvalidTimeoutException;
/*   12:     */ import org.springframework.transaction.NestedTransactionNotSupportedException;
/*   13:     */ import org.springframework.transaction.PlatformTransactionManager;
/*   14:     */ import org.springframework.transaction.TransactionDefinition;
/*   15:     */ import org.springframework.transaction.TransactionException;
/*   16:     */ import org.springframework.transaction.TransactionStatus;
/*   17:     */ import org.springframework.transaction.TransactionSuspensionNotSupportedException;
/*   18:     */ import org.springframework.transaction.UnexpectedRollbackException;
/*   19:     */ 
/*   20:     */ public abstract class AbstractPlatformTransactionManager
/*   21:     */   implements PlatformTransactionManager, Serializable
/*   22:     */ {
/*   23:     */   public static final int SYNCHRONIZATION_ALWAYS = 0;
/*   24:     */   public static final int SYNCHRONIZATION_ON_ACTUAL_TRANSACTION = 1;
/*   25:     */   public static final int SYNCHRONIZATION_NEVER = 2;
/*   26: 109 */   private static final Constants constants = new Constants(AbstractPlatformTransactionManager.class);
/*   27: 112 */   protected transient Log logger = LogFactory.getLog(getClass());
/*   28: 114 */   private int transactionSynchronization = 0;
/*   29: 116 */   private int defaultTimeout = -1;
/*   30: 118 */   private boolean nestedTransactionAllowed = false;
/*   31: 120 */   private boolean validateExistingTransaction = false;
/*   32: 122 */   private boolean globalRollbackOnParticipationFailure = true;
/*   33: 124 */   private boolean failEarlyOnGlobalRollbackOnly = false;
/*   34: 126 */   private boolean rollbackOnCommitFailure = false;
/*   35:     */   
/*   36:     */   public final void setTransactionSynchronizationName(String constantName)
/*   37:     */   {
/*   38: 136 */     setTransactionSynchronization(constants.asNumber(constantName).intValue());
/*   39:     */   }
/*   40:     */   
/*   41:     */   public final void setTransactionSynchronization(int transactionSynchronization)
/*   42:     */   {
/*   43: 152 */     this.transactionSynchronization = transactionSynchronization;
/*   44:     */   }
/*   45:     */   
/*   46:     */   public final int getTransactionSynchronization()
/*   47:     */   {
/*   48: 160 */     return this.transactionSynchronization;
/*   49:     */   }
/*   50:     */   
/*   51:     */   public final void setDefaultTimeout(int defaultTimeout)
/*   52:     */   {
/*   53: 172 */     if (defaultTimeout < -1) {
/*   54: 173 */       throw new InvalidTimeoutException("Invalid default timeout", defaultTimeout);
/*   55:     */     }
/*   56: 175 */     this.defaultTimeout = defaultTimeout;
/*   57:     */   }
/*   58:     */   
/*   59:     */   public final int getDefaultTimeout()
/*   60:     */   {
/*   61: 185 */     return this.defaultTimeout;
/*   62:     */   }
/*   63:     */   
/*   64:     */   public final void setNestedTransactionAllowed(boolean nestedTransactionAllowed)
/*   65:     */   {
/*   66: 194 */     this.nestedTransactionAllowed = nestedTransactionAllowed;
/*   67:     */   }
/*   68:     */   
/*   69:     */   public final boolean isNestedTransactionAllowed()
/*   70:     */   {
/*   71: 201 */     return this.nestedTransactionAllowed;
/*   72:     */   }
/*   73:     */   
/*   74:     */   public final void setValidateExistingTransaction(boolean validateExistingTransaction)
/*   75:     */   {
/*   76: 218 */     this.validateExistingTransaction = validateExistingTransaction;
/*   77:     */   }
/*   78:     */   
/*   79:     */   public final boolean isValidateExistingTransaction()
/*   80:     */   {
/*   81: 226 */     return this.validateExistingTransaction;
/*   82:     */   }
/*   83:     */   
/*   84:     */   public final void setGlobalRollbackOnParticipationFailure(boolean globalRollbackOnParticipationFailure)
/*   85:     */   {
/*   86: 262 */     this.globalRollbackOnParticipationFailure = globalRollbackOnParticipationFailure;
/*   87:     */   }
/*   88:     */   
/*   89:     */   public final boolean isGlobalRollbackOnParticipationFailure()
/*   90:     */   {
/*   91: 270 */     return this.globalRollbackOnParticipationFailure;
/*   92:     */   }
/*   93:     */   
/*   94:     */   public final void setFailEarlyOnGlobalRollbackOnly(boolean failEarlyOnGlobalRollbackOnly)
/*   95:     */   {
/*   96: 289 */     this.failEarlyOnGlobalRollbackOnly = failEarlyOnGlobalRollbackOnly;
/*   97:     */   }
/*   98:     */   
/*   99:     */   public final boolean isFailEarlyOnGlobalRollbackOnly()
/*  100:     */   {
/*  101: 297 */     return this.failEarlyOnGlobalRollbackOnly;
/*  102:     */   }
/*  103:     */   
/*  104:     */   public final void setRollbackOnCommitFailure(boolean rollbackOnCommitFailure)
/*  105:     */   {
/*  106: 310 */     this.rollbackOnCommitFailure = rollbackOnCommitFailure;
/*  107:     */   }
/*  108:     */   
/*  109:     */   public final boolean isRollbackOnCommitFailure()
/*  110:     */   {
/*  111: 318 */     return this.rollbackOnCommitFailure;
/*  112:     */   }
/*  113:     */   
/*  114:     */   public final TransactionStatus getTransaction(TransactionDefinition definition)
/*  115:     */     throws TransactionException
/*  116:     */   {
/*  117: 335 */     Object transaction = doGetTransaction();
/*  118:     */     
/*  119:     */ 
/*  120: 338 */     boolean debugEnabled = this.logger.isDebugEnabled();
/*  121: 340 */     if (definition == null) {
/*  122: 342 */       definition = new DefaultTransactionDefinition();
/*  123:     */     }
/*  124: 345 */     if (isExistingTransaction(transaction)) {
/*  125: 347 */       return handleExistingTransaction(definition, transaction, debugEnabled);
/*  126:     */     }
/*  127: 351 */     if (definition.getTimeout() < -1) {
/*  128: 352 */       throw new InvalidTimeoutException("Invalid transaction timeout", definition.getTimeout());
/*  129:     */     }
/*  130: 356 */     if (definition.getPropagationBehavior() == 2) {
/*  131: 357 */       throw new IllegalTransactionStateException(
/*  132: 358 */         "No existing transaction found for transaction marked with propagation 'mandatory'");
/*  133:     */     }
/*  134: 360 */     if ((definition.getPropagationBehavior() == 0) || 
/*  135: 361 */       (definition.getPropagationBehavior() == 3) || 
/*  136: 362 */       (definition.getPropagationBehavior() == 6))
/*  137:     */     {
/*  138: 363 */       SuspendedResourcesHolder suspendedResources = suspend(null);
/*  139: 364 */       if (debugEnabled) {
/*  140: 365 */         this.logger.debug("Creating new transaction with name [" + definition.getName() + "]: " + definition);
/*  141:     */       }
/*  142:     */       try
/*  143:     */       {
/*  144: 368 */         boolean newSynchronization = getTransactionSynchronization() != 2;
/*  145: 369 */         DefaultTransactionStatus status = newTransactionStatus(
/*  146: 370 */           definition, transaction, true, newSynchronization, debugEnabled, suspendedResources);
/*  147: 371 */         doBegin(transaction, definition);
/*  148: 372 */         prepareSynchronization(status, definition);
/*  149: 373 */         return status;
/*  150:     */       }
/*  151:     */       catch (RuntimeException ex)
/*  152:     */       {
/*  153: 376 */         resume(null, suspendedResources);
/*  154: 377 */         throw ex;
/*  155:     */       }
/*  156:     */       catch (Error err)
/*  157:     */       {
/*  158: 380 */         resume(null, suspendedResources);
/*  159: 381 */         throw err;
/*  160:     */       }
/*  161:     */     }
/*  162: 386 */     boolean newSynchronization = getTransactionSynchronization() == 0;
/*  163: 387 */     return prepareTransactionStatus(definition, null, true, newSynchronization, debugEnabled, null);
/*  164:     */   }
/*  165:     */   
/*  166:     */   private TransactionStatus handleExistingTransaction(TransactionDefinition definition, Object transaction, boolean debugEnabled)
/*  167:     */     throws TransactionException
/*  168:     */   {
/*  169: 398 */     if (definition.getPropagationBehavior() == 5) {
/*  170: 399 */       throw new IllegalTransactionStateException(
/*  171: 400 */         "Existing transaction found for transaction marked with propagation 'never'");
/*  172:     */     }
/*  173: 403 */     if (definition.getPropagationBehavior() == 4)
/*  174:     */     {
/*  175: 404 */       if (debugEnabled) {
/*  176: 405 */         this.logger.debug("Suspending current transaction");
/*  177:     */       }
/*  178: 407 */       Object suspendedResources = suspend(transaction);
/*  179: 408 */       boolean newSynchronization = getTransactionSynchronization() == 0;
/*  180: 409 */       return prepareTransactionStatus(
/*  181: 410 */         definition, null, false, newSynchronization, debugEnabled, suspendedResources);
/*  182:     */     }
/*  183: 413 */     if (definition.getPropagationBehavior() == 3)
/*  184:     */     {
/*  185: 414 */       if (debugEnabled) {
/*  186: 415 */         this.logger.debug("Suspending current transaction, creating new transaction with name [" + 
/*  187: 416 */           definition.getName() + "]");
/*  188:     */       }
/*  189: 418 */       SuspendedResourcesHolder suspendedResources = suspend(transaction);
/*  190:     */       try
/*  191:     */       {
/*  192: 420 */         boolean newSynchronization = getTransactionSynchronization() != 2;
/*  193: 421 */         DefaultTransactionStatus status = newTransactionStatus(
/*  194: 422 */           definition, transaction, true, newSynchronization, debugEnabled, suspendedResources);
/*  195: 423 */         doBegin(transaction, definition);
/*  196: 424 */         prepareSynchronization(status, definition);
/*  197: 425 */         return status;
/*  198:     */       }
/*  199:     */       catch (RuntimeException beginEx)
/*  200:     */       {
/*  201: 428 */         resumeAfterBeginException(transaction, suspendedResources, beginEx);
/*  202: 429 */         throw beginEx;
/*  203:     */       }
/*  204:     */       catch (Error beginErr)
/*  205:     */       {
/*  206: 432 */         resumeAfterBeginException(transaction, suspendedResources, beginErr);
/*  207: 433 */         throw beginErr;
/*  208:     */       }
/*  209:     */     }
/*  210: 437 */     if (definition.getPropagationBehavior() == 6)
/*  211:     */     {
/*  212: 438 */       if (!isNestedTransactionAllowed()) {
/*  213: 439 */         throw new NestedTransactionNotSupportedException(
/*  214: 440 */           "Transaction manager does not allow nested transactions by default - specify 'nestedTransactionAllowed' property with value 'true'");
/*  215:     */       }
/*  216: 443 */       if (debugEnabled) {
/*  217: 444 */         this.logger.debug("Creating nested transaction with name [" + definition.getName() + "]");
/*  218:     */       }
/*  219: 446 */       if (useSavepointForNestedTransaction())
/*  220:     */       {
/*  221: 450 */         DefaultTransactionStatus status = 
/*  222: 451 */           prepareTransactionStatus(definition, transaction, false, false, debugEnabled, null);
/*  223: 452 */         status.createAndHoldSavepoint();
/*  224: 453 */         return status;
/*  225:     */       }
/*  226: 459 */       boolean newSynchronization = getTransactionSynchronization() != 2;
/*  227: 460 */       DefaultTransactionStatus status = newTransactionStatus(
/*  228: 461 */         definition, transaction, true, newSynchronization, debugEnabled, null);
/*  229: 462 */       doBegin(transaction, definition);
/*  230: 463 */       prepareSynchronization(status, definition);
/*  231: 464 */       return status;
/*  232:     */     }
/*  233: 469 */     if (debugEnabled) {
/*  234: 470 */       this.logger.debug("Participating in existing transaction");
/*  235:     */     }
/*  236: 472 */     if (isValidateExistingTransaction())
/*  237:     */     {
/*  238: 473 */       if (definition.getIsolationLevel() != -1)
/*  239:     */       {
/*  240: 474 */         Integer currentIsolationLevel = TransactionSynchronizationManager.getCurrentTransactionIsolationLevel();
/*  241: 475 */         if ((currentIsolationLevel == null) || (currentIsolationLevel.intValue() != definition.getIsolationLevel()))
/*  242:     */         {
/*  243: 476 */           Constants isoConstants = DefaultTransactionDefinition.constants;
/*  244: 477 */           throw new IllegalTransactionStateException("Participating transaction with definition [" + 
/*  245: 478 */             definition + "] specifies isolation level which is incompatible with existing transaction: " + (
/*  246: 479 */             currentIsolationLevel != null ? 
/*  247: 480 */             isoConstants.toCode(currentIsolationLevel, "ISOLATION_") : 
/*  248: 481 */             "(unknown)"));
/*  249:     */         }
/*  250:     */       }
/*  251: 484 */       if ((!definition.isReadOnly()) && 
/*  252: 485 */         (TransactionSynchronizationManager.isCurrentTransactionReadOnly())) {
/*  253: 486 */         throw new IllegalTransactionStateException("Participating transaction with definition [" + 
/*  254: 487 */           definition + "] is not marked as read-only but existing transaction is");
/*  255:     */       }
/*  256:     */     }
/*  257: 491 */     boolean newSynchronization = getTransactionSynchronization() != 2;
/*  258: 492 */     return prepareTransactionStatus(definition, transaction, false, newSynchronization, debugEnabled, null);
/*  259:     */   }
/*  260:     */   
/*  261:     */   protected final DefaultTransactionStatus prepareTransactionStatus(TransactionDefinition definition, Object transaction, boolean newTransaction, boolean newSynchronization, boolean debug, Object suspendedResources)
/*  262:     */   {
/*  263: 505 */     DefaultTransactionStatus status = newTransactionStatus(
/*  264: 506 */       definition, transaction, newTransaction, newSynchronization, debug, suspendedResources);
/*  265: 507 */     prepareSynchronization(status, definition);
/*  266: 508 */     return status;
/*  267:     */   }
/*  268:     */   
/*  269:     */   protected DefaultTransactionStatus newTransactionStatus(TransactionDefinition definition, Object transaction, boolean newTransaction, boolean newSynchronization, boolean debug, Object suspendedResources)
/*  270:     */   {
/*  271: 518 */     boolean actualNewSynchronization = (newSynchronization) && 
/*  272: 519 */       (!TransactionSynchronizationManager.isSynchronizationActive());
/*  273: 520 */     return new DefaultTransactionStatus(
/*  274: 521 */       transaction, newTransaction, actualNewSynchronization, 
/*  275: 522 */       definition.isReadOnly(), debug, suspendedResources);
/*  276:     */   }
/*  277:     */   
/*  278:     */   protected void prepareSynchronization(DefaultTransactionStatus status, TransactionDefinition definition)
/*  279:     */   {
/*  280: 529 */     if (status.isNewSynchronization())
/*  281:     */     {
/*  282: 530 */       TransactionSynchronizationManager.setActualTransactionActive(status.hasTransaction());
/*  283: 531 */       TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(
/*  284: 532 */         definition.getIsolationLevel() != -1 ? 
/*  285: 533 */         Integer.valueOf(definition.getIsolationLevel()) : null);
/*  286: 534 */       TransactionSynchronizationManager.setCurrentTransactionReadOnly(definition.isReadOnly());
/*  287: 535 */       TransactionSynchronizationManager.setCurrentTransactionName(definition.getName());
/*  288: 536 */       TransactionSynchronizationManager.initSynchronization();
/*  289:     */     }
/*  290:     */   }
/*  291:     */   
/*  292:     */   protected int determineTimeout(TransactionDefinition definition)
/*  293:     */   {
/*  294: 550 */     if (definition.getTimeout() != -1) {
/*  295: 551 */       return definition.getTimeout();
/*  296:     */     }
/*  297: 553 */     return this.defaultTimeout;
/*  298:     */   }
/*  299:     */   
/*  300:     */   protected final SuspendedResourcesHolder suspend(Object transaction)
/*  301:     */     throws TransactionException
/*  302:     */   {
/*  303: 568 */     if (TransactionSynchronizationManager.isSynchronizationActive())
/*  304:     */     {
/*  305: 569 */       List<TransactionSynchronization> suspendedSynchronizations = doSuspendSynchronization();
/*  306:     */       try
/*  307:     */       {
/*  308: 571 */         Object suspendedResources = null;
/*  309: 572 */         if (transaction != null) {
/*  310: 573 */           suspendedResources = doSuspend(transaction);
/*  311:     */         }
/*  312: 575 */         String name = TransactionSynchronizationManager.getCurrentTransactionName();
/*  313: 576 */         TransactionSynchronizationManager.setCurrentTransactionName(null);
/*  314: 577 */         boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
/*  315: 578 */         TransactionSynchronizationManager.setCurrentTransactionReadOnly(false);
/*  316: 579 */         Integer isolationLevel = TransactionSynchronizationManager.getCurrentTransactionIsolationLevel();
/*  317: 580 */         TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(null);
/*  318: 581 */         boolean wasActive = TransactionSynchronizationManager.isActualTransactionActive();
/*  319: 582 */         TransactionSynchronizationManager.setActualTransactionActive(false);
/*  320: 583 */         return new SuspendedResourcesHolder(
/*  321: 584 */           suspendedResources, suspendedSynchronizations, name, readOnly, isolationLevel, wasActive, null);
/*  322:     */       }
/*  323:     */       catch (RuntimeException ex)
/*  324:     */       {
/*  325: 588 */         doResumeSynchronization(suspendedSynchronizations);
/*  326: 589 */         throw ex;
/*  327:     */       }
/*  328:     */       catch (Error err)
/*  329:     */       {
/*  330: 593 */         doResumeSynchronization(suspendedSynchronizations);
/*  331: 594 */         throw err;
/*  332:     */       }
/*  333:     */     }
/*  334: 597 */     if (transaction != null)
/*  335:     */     {
/*  336: 599 */       Object suspendedResources = doSuspend(transaction);
/*  337: 600 */       return new SuspendedResourcesHolder(suspendedResources, null);
/*  338:     */     }
/*  339: 604 */     return null;
/*  340:     */   }
/*  341:     */   
/*  342:     */   protected final void resume(Object transaction, SuspendedResourcesHolder resourcesHolder)
/*  343:     */     throws TransactionException
/*  344:     */   {
/*  345: 621 */     if (resourcesHolder != null)
/*  346:     */     {
/*  347: 622 */       Object suspendedResources = resourcesHolder.suspendedResources;
/*  348: 623 */       if (suspendedResources != null) {
/*  349: 624 */         doResume(transaction, suspendedResources);
/*  350:     */       }
/*  351: 626 */       List<TransactionSynchronization> suspendedSynchronizations = resourcesHolder.suspendedSynchronizations;
/*  352: 627 */       if (suspendedSynchronizations != null)
/*  353:     */       {
/*  354: 628 */         TransactionSynchronizationManager.setActualTransactionActive(resourcesHolder.wasActive);
/*  355: 629 */         TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(resourcesHolder.isolationLevel);
/*  356: 630 */         TransactionSynchronizationManager.setCurrentTransactionReadOnly(resourcesHolder.readOnly);
/*  357: 631 */         TransactionSynchronizationManager.setCurrentTransactionName(resourcesHolder.name);
/*  358: 632 */         doResumeSynchronization(suspendedSynchronizations);
/*  359:     */       }
/*  360:     */     }
/*  361:     */   }
/*  362:     */   
/*  363:     */   private void resumeAfterBeginException(Object transaction, SuspendedResourcesHolder suspendedResources, Throwable beginEx)
/*  364:     */   {
/*  365: 643 */     String exMessage = "Inner transaction begin exception overridden by outer transaction resume exception";
/*  366:     */     try
/*  367:     */     {
/*  368: 645 */       resume(transaction, suspendedResources);
/*  369:     */     }
/*  370:     */     catch (RuntimeException resumeEx)
/*  371:     */     {
/*  372: 648 */       this.logger.error(exMessage, beginEx);
/*  373: 649 */       throw resumeEx;
/*  374:     */     }
/*  375:     */     catch (Error resumeErr)
/*  376:     */     {
/*  377: 652 */       this.logger.error(exMessage, beginEx);
/*  378: 653 */       throw resumeErr;
/*  379:     */     }
/*  380:     */   }
/*  381:     */   
/*  382:     */   private List<TransactionSynchronization> doSuspendSynchronization()
/*  383:     */   {
/*  384: 663 */     List<TransactionSynchronization> suspendedSynchronizations = 
/*  385: 664 */       TransactionSynchronizationManager.getSynchronizations();
/*  386: 665 */     for (TransactionSynchronization synchronization : suspendedSynchronizations) {
/*  387: 666 */       synchronization.suspend();
/*  388:     */     }
/*  389: 668 */     TransactionSynchronizationManager.clearSynchronization();
/*  390: 669 */     return suspendedSynchronizations;
/*  391:     */   }
/*  392:     */   
/*  393:     */   private void doResumeSynchronization(List<TransactionSynchronization> suspendedSynchronizations)
/*  394:     */   {
/*  395:     */     
/*  396: 679 */     for (TransactionSynchronization synchronization : suspendedSynchronizations)
/*  397:     */     {
/*  398: 680 */       synchronization.resume();
/*  399: 681 */       TransactionSynchronizationManager.registerSynchronization(synchronization);
/*  400:     */     }
/*  401:     */   }
/*  402:     */   
/*  403:     */   public final void commit(TransactionStatus status)
/*  404:     */     throws TransactionException
/*  405:     */   {
/*  406: 696 */     if (status.isCompleted()) {
/*  407: 697 */       throw new IllegalTransactionStateException(
/*  408: 698 */         "Transaction is already completed - do not call commit or rollback more than once per transaction");
/*  409:     */     }
/*  410: 701 */     DefaultTransactionStatus defStatus = (DefaultTransactionStatus)status;
/*  411: 702 */     if (defStatus.isLocalRollbackOnly())
/*  412:     */     {
/*  413: 703 */       if (defStatus.isDebug()) {
/*  414: 704 */         this.logger.debug("Transactional code has requested rollback");
/*  415:     */       }
/*  416: 706 */       processRollback(defStatus);
/*  417: 707 */       return;
/*  418:     */     }
/*  419: 709 */     if ((!shouldCommitOnGlobalRollbackOnly()) && (defStatus.isGlobalRollbackOnly()))
/*  420:     */     {
/*  421: 710 */       if (defStatus.isDebug()) {
/*  422: 711 */         this.logger.debug("Global transaction is marked as rollback-only but transactional code requested commit");
/*  423:     */       }
/*  424: 713 */       processRollback(defStatus);
/*  425: 716 */       if ((status.isNewTransaction()) || (isFailEarlyOnGlobalRollbackOnly())) {
/*  426: 717 */         throw new UnexpectedRollbackException(
/*  427: 718 */           "Transaction rolled back because it has been marked as rollback-only");
/*  428:     */       }
/*  429: 720 */       return;
/*  430:     */     }
/*  431: 723 */     processCommit(defStatus);
/*  432:     */   }
/*  433:     */   
/*  434:     */   private void processCommit(DefaultTransactionStatus status)
/*  435:     */     throws TransactionException
/*  436:     */   {
/*  437:     */     try
/*  438:     */     {
/*  439: 734 */       boolean beforeCompletionInvoked = false;
/*  440:     */       try
/*  441:     */       {
/*  442: 736 */         prepareForCommit(status);
/*  443: 737 */         triggerBeforeCommit(status);
/*  444: 738 */         triggerBeforeCompletion(status);
/*  445: 739 */         beforeCompletionInvoked = true;
/*  446: 740 */         boolean globalRollbackOnly = false;
/*  447: 741 */         if ((status.isNewTransaction()) || (isFailEarlyOnGlobalRollbackOnly())) {
/*  448: 742 */           globalRollbackOnly = status.isGlobalRollbackOnly();
/*  449:     */         }
/*  450: 744 */         if (status.hasSavepoint())
/*  451:     */         {
/*  452: 745 */           if (status.isDebug()) {
/*  453: 746 */             this.logger.debug("Releasing transaction savepoint");
/*  454:     */           }
/*  455: 748 */           status.releaseHeldSavepoint();
/*  456:     */         }
/*  457: 750 */         else if (status.isNewTransaction())
/*  458:     */         {
/*  459: 751 */           if (status.isDebug()) {
/*  460: 752 */             this.logger.debug("Initiating transaction commit");
/*  461:     */           }
/*  462: 754 */           doCommit(status);
/*  463:     */         }
/*  464: 758 */         if (globalRollbackOnly) {
/*  465: 759 */           throw new UnexpectedRollbackException(
/*  466: 760 */             "Transaction silently rolled back because it has been marked as rollback-only");
/*  467:     */         }
/*  468:     */       }
/*  469:     */       catch (UnexpectedRollbackException ex)
/*  470:     */       {
/*  471: 765 */         triggerAfterCompletion(status, 1);
/*  472: 766 */         throw ex;
/*  473:     */       }
/*  474:     */       catch (TransactionException ex)
/*  475:     */       {
/*  476: 770 */         if (isRollbackOnCommitFailure()) {
/*  477: 771 */           doRollbackOnCommitException(status, ex);
/*  478:     */         } else {
/*  479: 774 */           triggerAfterCompletion(status, 2);
/*  480:     */         }
/*  481: 776 */         throw ex;
/*  482:     */       }
/*  483:     */       catch (RuntimeException ex)
/*  484:     */       {
/*  485: 779 */         if (!beforeCompletionInvoked) {
/*  486: 780 */           triggerBeforeCompletion(status);
/*  487:     */         }
/*  488: 782 */         doRollbackOnCommitException(status, ex);
/*  489: 783 */         throw ex;
/*  490:     */       }
/*  491:     */       catch (Error err)
/*  492:     */       {
/*  493: 786 */         if (!beforeCompletionInvoked) {
/*  494: 787 */           triggerBeforeCompletion(status);
/*  495:     */         }
/*  496: 789 */         doRollbackOnCommitException(status, err);
/*  497: 790 */         throw err;
/*  498:     */       }
/*  499:     */       try
/*  500:     */       {
/*  501: 796 */         triggerAfterCommit(status);
/*  502:     */       }
/*  503:     */       finally
/*  504:     */       {
/*  505: 799 */         triggerAfterCompletion(status, 0);
/*  506:     */       }
/*  507:     */     }
/*  508:     */     finally
/*  509:     */     {
/*  510: 804 */       cleanupAfterCompletion(status);
/*  511:     */     }
/*  512:     */   }
/*  513:     */   
/*  514:     */   public final void rollback(TransactionStatus status)
/*  515:     */     throws TransactionException
/*  516:     */   {
/*  517: 816 */     if (status.isCompleted()) {
/*  518: 817 */       throw new IllegalTransactionStateException(
/*  519: 818 */         "Transaction is already completed - do not call commit or rollback more than once per transaction");
/*  520:     */     }
/*  521: 821 */     DefaultTransactionStatus defStatus = (DefaultTransactionStatus)status;
/*  522: 822 */     processRollback(defStatus);
/*  523:     */   }
/*  524:     */   
/*  525:     */   /* Error */
/*  526:     */   private void processRollback(DefaultTransactionStatus status)
/*  527:     */   {
/*  528:     */     // Byte code:
/*  529:     */     //   0: aload_0
/*  530:     */     //   1: aload_1
/*  531:     */     //   2: invokevirtual 484	org/springframework/transaction/support/AbstractPlatformTransactionManager:triggerBeforeCompletion	(Lorg/springframework/transaction/support/DefaultTransactionStatus;)V
/*  532:     */     //   5: aload_1
/*  533:     */     //   6: invokevirtual 488	org/springframework/transaction/support/DefaultTransactionStatus:hasSavepoint	()Z
/*  534:     */     //   9: ifeq +29 -> 38
/*  535:     */     //   12: aload_1
/*  536:     */     //   13: invokevirtual 446	org/springframework/transaction/support/DefaultTransactionStatus:isDebug	()Z
/*  537:     */     //   16: ifeq +15 -> 31
/*  538:     */     //   19: aload_0
/*  539:     */     //   20: getfield 54	org/springframework/transaction/support/AbstractPlatformTransactionManager:logger	Lorg/apache/commons/logging/Log;
/*  540:     */     //   23: ldc_w 524
/*  541:     */     //   26: invokeinterface 178 2 0
/*  542:     */     //   31: aload_1
/*  543:     */     //   32: invokevirtual 526	org/springframework/transaction/support/DefaultTransactionStatus:rollbackToHeldSavepoint	()V
/*  544:     */     //   35: goto +140 -> 175
/*  545:     */     //   38: aload_1
/*  546:     */     //   39: invokevirtual 487	org/springframework/transaction/support/DefaultTransactionStatus:isNewTransaction	()Z
/*  547:     */     //   42: ifeq +30 -> 72
/*  548:     */     //   45: aload_1
/*  549:     */     //   46: invokevirtual 446	org/springframework/transaction/support/DefaultTransactionStatus:isDebug	()Z
/*  550:     */     //   49: ifeq +15 -> 64
/*  551:     */     //   52: aload_0
/*  552:     */     //   53: getfield 54	org/springframework/transaction/support/AbstractPlatformTransactionManager:logger	Lorg/apache/commons/logging/Log;
/*  553:     */     //   56: ldc_w 529
/*  554:     */     //   59: invokeinterface 178 2 0
/*  555:     */     //   64: aload_0
/*  556:     */     //   65: aload_1
/*  557:     */     //   66: invokevirtual 531	org/springframework/transaction/support/AbstractPlatformTransactionManager:doRollback	(Lorg/springframework/transaction/support/DefaultTransactionStatus;)V
/*  558:     */     //   69: goto +106 -> 175
/*  559:     */     //   72: aload_1
/*  560:     */     //   73: invokevirtual 303	org/springframework/transaction/support/DefaultTransactionStatus:hasTransaction	()Z
/*  561:     */     //   76: ifeq +66 -> 142
/*  562:     */     //   79: aload_1
/*  563:     */     //   80: invokevirtual 443	org/springframework/transaction/support/DefaultTransactionStatus:isLocalRollbackOnly	()Z
/*  564:     */     //   83: ifne +10 -> 93
/*  565:     */     //   86: aload_0
/*  566:     */     //   87: invokevirtual 534	org/springframework/transaction/support/AbstractPlatformTransactionManager:isGlobalRollbackOnParticipationFailure	()Z
/*  567:     */     //   90: ifeq +30 -> 120
/*  568:     */     //   93: aload_1
/*  569:     */     //   94: invokevirtual 446	org/springframework/transaction/support/DefaultTransactionStatus:isDebug	()Z
/*  570:     */     //   97: ifeq +15 -> 112
/*  571:     */     //   100: aload_0
/*  572:     */     //   101: getfield 54	org/springframework/transaction/support/AbstractPlatformTransactionManager:logger	Lorg/apache/commons/logging/Log;
/*  573:     */     //   104: ldc_w 536
/*  574:     */     //   107: invokeinterface 178 2 0
/*  575:     */     //   112: aload_0
/*  576:     */     //   113: aload_1
/*  577:     */     //   114: invokevirtual 538	org/springframework/transaction/support/AbstractPlatformTransactionManager:doSetRollbackOnly	(Lorg/springframework/transaction/support/DefaultTransactionStatus;)V
/*  578:     */     //   117: goto +58 -> 175
/*  579:     */     //   120: aload_1
/*  580:     */     //   121: invokevirtual 446	org/springframework/transaction/support/DefaultTransactionStatus:isDebug	()Z
/*  581:     */     //   124: ifeq +51 -> 175
/*  582:     */     //   127: aload_0
/*  583:     */     //   128: getfield 54	org/springframework/transaction/support/AbstractPlatformTransactionManager:logger	Lorg/apache/commons/logging/Log;
/*  584:     */     //   131: ldc_w 541
/*  585:     */     //   134: invokeinterface 178 2 0
/*  586:     */     //   139: goto +36 -> 175
/*  587:     */     //   142: aload_0
/*  588:     */     //   143: getfield 54	org/springframework/transaction/support/AbstractPlatformTransactionManager:logger	Lorg/apache/commons/logging/Log;
/*  589:     */     //   146: ldc_w 543
/*  590:     */     //   149: invokeinterface 178 2 0
/*  591:     */     //   154: goto +21 -> 175
/*  592:     */     //   157: astore_2
/*  593:     */     //   158: aload_0
/*  594:     */     //   159: aload_1
/*  595:     */     //   160: iconst_2
/*  596:     */     //   161: invokespecial 503	org/springframework/transaction/support/AbstractPlatformTransactionManager:triggerAfterCompletion	(Lorg/springframework/transaction/support/DefaultTransactionStatus;I)V
/*  597:     */     //   164: aload_2
/*  598:     */     //   165: athrow
/*  599:     */     //   166: astore_2
/*  600:     */     //   167: aload_0
/*  601:     */     //   168: aload_1
/*  602:     */     //   169: iconst_2
/*  603:     */     //   170: invokespecial 503	org/springframework/transaction/support/AbstractPlatformTransactionManager:triggerAfterCompletion	(Lorg/springframework/transaction/support/DefaultTransactionStatus;I)V
/*  604:     */     //   173: aload_2
/*  605:     */     //   174: athrow
/*  606:     */     //   175: aload_0
/*  607:     */     //   176: aload_1
/*  608:     */     //   177: iconst_1
/*  609:     */     //   178: invokespecial 503	org/springframework/transaction/support/AbstractPlatformTransactionManager:triggerAfterCompletion	(Lorg/springframework/transaction/support/DefaultTransactionStatus;I)V
/*  610:     */     //   181: goto +11 -> 192
/*  611:     */     //   184: astore_3
/*  612:     */     //   185: aload_0
/*  613:     */     //   186: aload_1
/*  614:     */     //   187: invokespecial 516	org/springframework/transaction/support/AbstractPlatformTransactionManager:cleanupAfterCompletion	(Lorg/springframework/transaction/support/DefaultTransactionStatus;)V
/*  615:     */     //   190: aload_3
/*  616:     */     //   191: athrow
/*  617:     */     //   192: aload_0
/*  618:     */     //   193: aload_1
/*  619:     */     //   194: invokespecial 516	org/springframework/transaction/support/AbstractPlatformTransactionManager:cleanupAfterCompletion	(Lorg/springframework/transaction/support/DefaultTransactionStatus;)V
/*  620:     */     //   197: return
/*  621:     */     // Line number table:
/*  622:     */     //   Java source line #834	-> byte code offset #0
/*  623:     */     //   Java source line #835	-> byte code offset #5
/*  624:     */     //   Java source line #836	-> byte code offset #12
/*  625:     */     //   Java source line #837	-> byte code offset #19
/*  626:     */     //   Java source line #839	-> byte code offset #31
/*  627:     */     //   Java source line #841	-> byte code offset #38
/*  628:     */     //   Java source line #842	-> byte code offset #45
/*  629:     */     //   Java source line #843	-> byte code offset #52
/*  630:     */     //   Java source line #845	-> byte code offset #64
/*  631:     */     //   Java source line #847	-> byte code offset #72
/*  632:     */     //   Java source line #848	-> byte code offset #79
/*  633:     */     //   Java source line #849	-> byte code offset #93
/*  634:     */     //   Java source line #850	-> byte code offset #100
/*  635:     */     //   Java source line #852	-> byte code offset #112
/*  636:     */     //   Java source line #855	-> byte code offset #120
/*  637:     */     //   Java source line #856	-> byte code offset #127
/*  638:     */     //   Java source line #861	-> byte code offset #142
/*  639:     */     //   Java source line #864	-> byte code offset #157
/*  640:     */     //   Java source line #865	-> byte code offset #158
/*  641:     */     //   Java source line #866	-> byte code offset #164
/*  642:     */     //   Java source line #868	-> byte code offset #166
/*  643:     */     //   Java source line #869	-> byte code offset #167
/*  644:     */     //   Java source line #870	-> byte code offset #173
/*  645:     */     //   Java source line #872	-> byte code offset #175
/*  646:     */     //   Java source line #874	-> byte code offset #184
/*  647:     */     //   Java source line #875	-> byte code offset #185
/*  648:     */     //   Java source line #876	-> byte code offset #190
/*  649:     */     //   Java source line #875	-> byte code offset #192
/*  650:     */     //   Java source line #877	-> byte code offset #197
/*  651:     */     // Local variable table:
/*  652:     */     //   start	length	slot	name	signature
/*  653:     */     //   0	198	0	this	AbstractPlatformTransactionManager
/*  654:     */     //   0	198	1	status	DefaultTransactionStatus
/*  655:     */     //   157	8	2	ex	RuntimeException
/*  656:     */     //   166	8	2	err	Error
/*  657:     */     //   184	7	3	localObject	Object
/*  658:     */     // Exception table:
/*  659:     */     //   from	to	target	type
/*  660:     */     //   0	154	157	java/lang/RuntimeException
/*  661:     */     //   0	154	166	java/lang/Error
/*  662:     */     //   0	184	184	finally
/*  663:     */   }
/*  664:     */   
/*  665:     */   private void doRollbackOnCommitException(DefaultTransactionStatus status, Throwable ex)
/*  666:     */     throws TransactionException
/*  667:     */   {
/*  668:     */     try
/*  669:     */     {
/*  670: 888 */       if (status.isNewTransaction())
/*  671:     */       {
/*  672: 889 */         if (status.isDebug()) {
/*  673: 890 */           this.logger.debug("Initiating transaction rollback after commit exception", ex);
/*  674:     */         }
/*  675: 892 */         doRollback(status);
/*  676:     */       }
/*  677: 894 */       else if ((status.hasTransaction()) && (isGlobalRollbackOnParticipationFailure()))
/*  678:     */       {
/*  679: 895 */         if (status.isDebug()) {
/*  680: 896 */           this.logger.debug("Marking existing transaction as rollback-only after commit exception", ex);
/*  681:     */         }
/*  682: 898 */         doSetRollbackOnly(status);
/*  683:     */       }
/*  684:     */     }
/*  685:     */     catch (RuntimeException rbex)
/*  686:     */     {
/*  687: 902 */       this.logger.error("Commit exception overridden by rollback exception", ex);
/*  688: 903 */       triggerAfterCompletion(status, 2);
/*  689: 904 */       throw rbex;
/*  690:     */     }
/*  691:     */     catch (Error rberr)
/*  692:     */     {
/*  693: 907 */       this.logger.error("Commit exception overridden by rollback exception", ex);
/*  694: 908 */       triggerAfterCompletion(status, 2);
/*  695: 909 */       throw rberr;
/*  696:     */     }
/*  697: 911 */     triggerAfterCompletion(status, 1);
/*  698:     */   }
/*  699:     */   
/*  700:     */   protected final void triggerBeforeCommit(DefaultTransactionStatus status)
/*  701:     */   {
/*  702: 920 */     if (status.isNewSynchronization())
/*  703:     */     {
/*  704: 921 */       if (status.isDebug()) {
/*  705: 922 */         this.logger.trace("Triggering beforeCommit synchronization");
/*  706:     */       }
/*  707: 924 */       TransactionSynchronizationUtils.triggerBeforeCommit(status.isReadOnly());
/*  708:     */     }
/*  709:     */   }
/*  710:     */   
/*  711:     */   protected final void triggerBeforeCompletion(DefaultTransactionStatus status)
/*  712:     */   {
/*  713: 933 */     if (status.isNewSynchronization())
/*  714:     */     {
/*  715: 934 */       if (status.isDebug()) {
/*  716: 935 */         this.logger.trace("Triggering beforeCompletion synchronization");
/*  717:     */       }
/*  718: 937 */       TransactionSynchronizationUtils.triggerBeforeCompletion();
/*  719:     */     }
/*  720:     */   }
/*  721:     */   
/*  722:     */   private void triggerAfterCommit(DefaultTransactionStatus status)
/*  723:     */   {
/*  724: 946 */     if (status.isNewSynchronization())
/*  725:     */     {
/*  726: 947 */       if (status.isDebug()) {
/*  727: 948 */         this.logger.trace("Triggering afterCommit synchronization");
/*  728:     */       }
/*  729: 950 */       TransactionSynchronizationUtils.triggerAfterCommit();
/*  730:     */     }
/*  731:     */   }
/*  732:     */   
/*  733:     */   private void triggerAfterCompletion(DefaultTransactionStatus status, int completionStatus)
/*  734:     */   {
/*  735: 960 */     if (status.isNewSynchronization())
/*  736:     */     {
/*  737: 961 */       List<TransactionSynchronization> synchronizations = TransactionSynchronizationManager.getSynchronizations();
/*  738: 962 */       if ((!status.hasTransaction()) || (status.isNewTransaction()))
/*  739:     */       {
/*  740: 963 */         if (status.isDebug()) {
/*  741: 964 */           this.logger.trace("Triggering afterCompletion synchronization");
/*  742:     */         }
/*  743: 968 */         invokeAfterCompletion(synchronizations, completionStatus);
/*  744:     */       }
/*  745: 970 */       else if (!synchronizations.isEmpty())
/*  746:     */       {
/*  747: 974 */         registerAfterCompletionWithExistingTransaction(status.getTransaction(), synchronizations);
/*  748:     */       }
/*  749:     */     }
/*  750:     */   }
/*  751:     */   
/*  752:     */   protected final void invokeAfterCompletion(List<TransactionSynchronization> synchronizations, int completionStatus)
/*  753:     */   {
/*  754: 993 */     TransactionSynchronizationUtils.invokeAfterCompletion(synchronizations, completionStatus);
/*  755:     */   }
/*  756:     */   
/*  757:     */   private void cleanupAfterCompletion(DefaultTransactionStatus status)
/*  758:     */   {
/*  759:1003 */     status.setCompleted();
/*  760:1004 */     if (status.isNewSynchronization()) {
/*  761:1005 */       TransactionSynchronizationManager.clear();
/*  762:     */     }
/*  763:1007 */     if (status.isNewTransaction()) {
/*  764:1008 */       doCleanupAfterCompletion(status.getTransaction());
/*  765:     */     }
/*  766:1010 */     if (status.getSuspendedResources() != null)
/*  767:     */     {
/*  768:1011 */       if (status.isDebug()) {
/*  769:1012 */         this.logger.debug("Resuming suspended transaction after completion of inner transaction");
/*  770:     */       }
/*  771:1014 */       resume(status.getTransaction(), (SuspendedResourcesHolder)status.getSuspendedResources());
/*  772:     */     }
/*  773:     */   }
/*  774:     */   
/*  775:     */   protected abstract Object doGetTransaction()
/*  776:     */     throws TransactionException;
/*  777:     */   
/*  778:     */   protected boolean isExistingTransaction(Object transaction)
/*  779:     */     throws TransactionException
/*  780:     */   {
/*  781:1063 */     return false;
/*  782:     */   }
/*  783:     */   
/*  784:     */   protected boolean useSavepointForNestedTransaction()
/*  785:     */   {
/*  786:1083 */     return true;
/*  787:     */   }
/*  788:     */   
/*  789:     */   protected abstract void doBegin(Object paramObject, TransactionDefinition paramTransactionDefinition)
/*  790:     */     throws TransactionException;
/*  791:     */   
/*  792:     */   protected Object doSuspend(Object transaction)
/*  793:     */     throws TransactionException
/*  794:     */   {
/*  795:1120 */     throw new TransactionSuspensionNotSupportedException(
/*  796:1121 */       "Transaction manager [" + getClass().getName() + "] does not support transaction suspension");
/*  797:     */   }
/*  798:     */   
/*  799:     */   protected void doResume(Object transaction, Object suspendedResources)
/*  800:     */     throws TransactionException
/*  801:     */   {
/*  802:1138 */     throw new TransactionSuspensionNotSupportedException(
/*  803:1139 */       "Transaction manager [" + getClass().getName() + "] does not support transaction suspension");
/*  804:     */   }
/*  805:     */   
/*  806:     */   protected boolean shouldCommitOnGlobalRollbackOnly()
/*  807:     */   {
/*  808:1172 */     return false;
/*  809:     */   }
/*  810:     */   
/*  811:     */   protected void prepareForCommit(DefaultTransactionStatus status) {}
/*  812:     */   
/*  813:     */   protected abstract void doCommit(DefaultTransactionStatus paramDefaultTransactionStatus)
/*  814:     */     throws TransactionException;
/*  815:     */   
/*  816:     */   protected abstract void doRollback(DefaultTransactionStatus paramDefaultTransactionStatus)
/*  817:     */     throws TransactionException;
/*  818:     */   
/*  819:     */   protected void doSetRollbackOnly(DefaultTransactionStatus status)
/*  820:     */     throws TransactionException
/*  821:     */   {
/*  822:1220 */     throw new IllegalTransactionStateException(
/*  823:1221 */       "Participating in existing transactions is not supported - when 'isExistingTransaction' returns true, appropriate 'doSetRollbackOnly' behavior must be provided");
/*  824:     */   }
/*  825:     */   
/*  826:     */   protected void registerAfterCompletionWithExistingTransaction(Object transaction, List<TransactionSynchronization> synchronizations)
/*  827:     */     throws TransactionException
/*  828:     */   {
/*  829:1243 */     this.logger.debug("Cannot register Spring after-completion synchronization with existing transaction - processing Spring after-completion callbacks immediately, with outcome status 'unknown'");
/*  830:     */     
/*  831:1245 */     invokeAfterCompletion(synchronizations, 2);
/*  832:     */   }
/*  833:     */   
/*  834:     */   protected void doCleanupAfterCompletion(Object transaction) {}
/*  835:     */   
/*  836:     */   private void readObject(ObjectInputStream ois)
/*  837:     */     throws IOException, ClassNotFoundException
/*  838:     */   {
/*  839:1265 */     ois.defaultReadObject();
/*  840:     */     
/*  841:     */ 
/*  842:1268 */     this.logger = LogFactory.getLog(getClass());
/*  843:     */   }
/*  844:     */   
/*  845:     */   protected static class SuspendedResourcesHolder
/*  846:     */   {
/*  847:     */     private final Object suspendedResources;
/*  848:     */     private List<TransactionSynchronization> suspendedSynchronizations;
/*  849:     */     private String name;
/*  850:     */     private boolean readOnly;
/*  851:     */     private Integer isolationLevel;
/*  852:     */     private boolean wasActive;
/*  853:     */     
/*  854:     */     private SuspendedResourcesHolder(Object suspendedResources)
/*  855:     */     {
/*  856:1285 */       this.suspendedResources = suspendedResources;
/*  857:     */     }
/*  858:     */     
/*  859:     */     private SuspendedResourcesHolder(Object suspendedResources, List<TransactionSynchronization> suspendedSynchronizations, String name, boolean readOnly, Integer isolationLevel, boolean wasActive)
/*  860:     */     {
/*  861:1291 */       this.suspendedResources = suspendedResources;
/*  862:1292 */       this.suspendedSynchronizations = suspendedSynchronizations;
/*  863:1293 */       this.name = name;
/*  864:1294 */       this.readOnly = readOnly;
/*  865:1295 */       this.isolationLevel = isolationLevel;
/*  866:1296 */       this.wasActive = wasActive;
/*  867:     */     }
/*  868:     */   }
/*  869:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.support.AbstractPlatformTransactionManager
 * JD-Core Version:    0.7.0.1
 */