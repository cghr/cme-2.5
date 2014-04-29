/*   1:    */ package org.springframework.transaction.interceptor;
/*   2:    */ 
/*   3:    */ import java.util.Properties;
/*   4:    */ import org.springframework.aop.Pointcut;
/*   5:    */ import org.springframework.aop.framework.AbstractSingletonProxyFactoryBean;
/*   6:    */ import org.springframework.aop.support.DefaultPointcutAdvisor;
/*   7:    */ import org.springframework.beans.factory.BeanFactory;
/*   8:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*   9:    */ import org.springframework.transaction.PlatformTransactionManager;
/*  10:    */ 
/*  11:    */ public class TransactionProxyFactoryBean
/*  12:    */   extends AbstractSingletonProxyFactoryBean
/*  13:    */   implements BeanFactoryAware
/*  14:    */ {
/*  15:119 */   private final TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
/*  16:    */   private Pointcut pointcut;
/*  17:    */   
/*  18:    */   public void setTransactionManager(PlatformTransactionManager transactionManager)
/*  19:    */   {
/*  20:130 */     this.transactionInterceptor.setTransactionManager(transactionManager);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public void setTransactionAttributes(Properties transactionAttributes)
/*  24:    */   {
/*  25:147 */     this.transactionInterceptor.setTransactionAttributes(transactionAttributes);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setTransactionAttributeSource(TransactionAttributeSource transactionAttributeSource)
/*  29:    */   {
/*  30:163 */     this.transactionInterceptor.setTransactionAttributeSource(transactionAttributeSource);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setPointcut(Pointcut pointcut)
/*  34:    */   {
/*  35:174 */     this.pointcut = pointcut;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setBeanFactory(BeanFactory beanFactory)
/*  39:    */   {
/*  40:185 */     this.transactionInterceptor.setBeanFactory(beanFactory);
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected Object createMainInterceptor()
/*  44:    */   {
/*  45:194 */     this.transactionInterceptor.afterPropertiesSet();
/*  46:195 */     if (this.pointcut != null) {
/*  47:196 */       return new DefaultPointcutAdvisor(this.pointcut, this.transactionInterceptor);
/*  48:    */     }
/*  49:200 */     return new TransactionAttributeSourceAdvisor(this.transactionInterceptor);
/*  50:    */   }
/*  51:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.interceptor.TransactionProxyFactoryBean
 * JD-Core Version:    0.7.0.1
 */