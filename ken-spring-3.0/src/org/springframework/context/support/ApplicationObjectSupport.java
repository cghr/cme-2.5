/*   1:    */ package org.springframework.context.support;
/*   2:    */ 
/*   3:    */ import org.apache.commons.logging.Log;
/*   4:    */ import org.apache.commons.logging.LogFactory;
/*   5:    */ import org.springframework.beans.BeansException;
/*   6:    */ import org.springframework.context.ApplicationContext;
/*   7:    */ import org.springframework.context.ApplicationContextAware;
/*   8:    */ import org.springframework.context.ApplicationContextException;
/*   9:    */ 
/*  10:    */ public abstract class ApplicationObjectSupport
/*  11:    */   implements ApplicationContextAware
/*  12:    */ {
/*  13: 50 */   protected final Log logger = LogFactory.getLog(getClass());
/*  14:    */   private ApplicationContext applicationContext;
/*  15:    */   private MessageSourceAccessor messageSourceAccessor;
/*  16:    */   
/*  17:    */   public final void setApplicationContext(ApplicationContext context)
/*  18:    */     throws BeansException
/*  19:    */   {
/*  20: 60 */     if ((context == null) && (!isContextRequired()))
/*  21:    */     {
/*  22: 62 */       this.applicationContext = null;
/*  23: 63 */       this.messageSourceAccessor = null;
/*  24:    */     }
/*  25: 65 */     else if (this.applicationContext == null)
/*  26:    */     {
/*  27: 67 */       if (!requiredContextClass().isInstance(context)) {
/*  28: 68 */         throw new ApplicationContextException(
/*  29: 69 */           "Invalid application context: needs to be of type [" + requiredContextClass().getName() + "]");
/*  30:    */       }
/*  31: 71 */       this.applicationContext = context;
/*  32: 72 */       this.messageSourceAccessor = new MessageSourceAccessor(context);
/*  33: 73 */       initApplicationContext(context);
/*  34:    */     }
/*  35: 77 */     else if (this.applicationContext != context)
/*  36:    */     {
/*  37: 78 */       throw new ApplicationContextException(
/*  38: 79 */         "Cannot reinitialize with different application context: current one is [" + 
/*  39: 80 */         this.applicationContext + "], passed-in one is [" + context + "]");
/*  40:    */     }
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected boolean isContextRequired()
/*  44:    */   {
/*  45: 93 */     return false;
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected Class requiredContextClass()
/*  49:    */   {
/*  50:103 */     return ApplicationContext.class;
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected void initApplicationContext(ApplicationContext context)
/*  54:    */     throws BeansException
/*  55:    */   {
/*  56:119 */     initApplicationContext();
/*  57:    */   }
/*  58:    */   
/*  59:    */   protected void initApplicationContext()
/*  60:    */     throws BeansException
/*  61:    */   {}
/*  62:    */   
/*  63:    */   public final ApplicationContext getApplicationContext()
/*  64:    */     throws IllegalStateException
/*  65:    */   {
/*  66:139 */     if ((this.applicationContext == null) && (isContextRequired())) {
/*  67:140 */       throw new IllegalStateException(
/*  68:141 */         "ApplicationObjectSupport instance [" + this + "] does not run in an ApplicationContext");
/*  69:    */     }
/*  70:143 */     return this.applicationContext;
/*  71:    */   }
/*  72:    */   
/*  73:    */   protected final MessageSourceAccessor getMessageSourceAccessor()
/*  74:    */     throws IllegalStateException
/*  75:    */   {
/*  76:152 */     if ((this.messageSourceAccessor == null) && (isContextRequired())) {
/*  77:153 */       throw new IllegalStateException(
/*  78:154 */         "ApplicationObjectSupport instance [" + this + "] does not run in an ApplicationContext");
/*  79:    */     }
/*  80:156 */     return this.messageSourceAccessor;
/*  81:    */   }
/*  82:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.ApplicationObjectSupport
 * JD-Core Version:    0.7.0.1
 */