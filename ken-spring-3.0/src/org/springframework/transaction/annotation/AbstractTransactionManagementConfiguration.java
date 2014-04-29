/*  1:   */ package org.springframework.transaction.annotation;
/*  2:   */ 
/*  3:   */ import java.util.Collection;
/*  4:   */ import java.util.Iterator;
/*  5:   */ import java.util.Map;
/*  6:   */ import org.springframework.beans.factory.annotation.Autowired;
/*  7:   */ import org.springframework.context.annotation.Configuration;
/*  8:   */ import org.springframework.context.annotation.ImportAware;
/*  9:   */ import org.springframework.core.type.AnnotationMetadata;
/* 10:   */ import org.springframework.transaction.PlatformTransactionManager;
/* 11:   */ import org.springframework.util.Assert;
/* 12:   */ 
/* 13:   */ @Configuration
/* 14:   */ public abstract class AbstractTransactionManagementConfiguration
/* 15:   */   implements ImportAware
/* 16:   */ {
/* 17:   */   protected Map<String, Object> enableTx;
/* 18:   */   protected PlatformTransactionManager txManager;
/* 19:   */   
/* 20:   */   public void setImportMetadata(AnnotationMetadata importMetadata)
/* 21:   */   {
/* 22:44 */     this.enableTx = importMetadata.getAnnotationAttributes(EnableTransactionManagement.class.getName(), false);
/* 23:45 */     Assert.notNull(this.enableTx, 
/* 24:46 */       "@EnableTransactionManagement is not present on importing class " + 
/* 25:47 */       importMetadata.getClassName());
/* 26:   */   }
/* 27:   */   
/* 28:   */   @Autowired(required=false)
/* 29:   */   void setConfigurers(Collection<TransactionManagementConfigurer> configurers)
/* 30:   */   {
/* 31:52 */     if ((configurers == null) || (configurers.isEmpty())) {
/* 32:53 */       return;
/* 33:   */     }
/* 34:56 */     if (configurers.size() > 1) {
/* 35:57 */       throw new IllegalStateException("only one TransactionManagementConfigurer may exist");
/* 36:   */     }
/* 37:60 */     TransactionManagementConfigurer configurer = (TransactionManagementConfigurer)configurers.iterator().next();
/* 38:61 */     this.txManager = configurer.annotationDrivenTransactionManager();
/* 39:   */   }
/* 40:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.annotation.AbstractTransactionManagementConfiguration
 * JD-Core Version:    0.7.0.1
 */