/*  1:   */ package org.springframework.scheduling.annotation;
/*  2:   */ 
/*  3:   */ import java.util.Collection;
/*  4:   */ import java.util.Iterator;
/*  5:   */ import java.util.Map;
/*  6:   */ import java.util.concurrent.Executor;
/*  7:   */ import org.springframework.beans.factory.annotation.Autowired;
/*  8:   */ import org.springframework.context.annotation.Configuration;
/*  9:   */ import org.springframework.context.annotation.ImportAware;
/* 10:   */ import org.springframework.core.type.AnnotationMetadata;
/* 11:   */ import org.springframework.util.Assert;
/* 12:   */ 
/* 13:   */ @Configuration
/* 14:   */ public abstract class AbstractAsyncConfiguration
/* 15:   */   implements ImportAware
/* 16:   */ {
/* 17:   */   protected Map<String, Object> enableAsync;
/* 18:   */   protected Executor executor;
/* 19:   */   
/* 20:   */   public void setImportMetadata(AnnotationMetadata importMetadata)
/* 21:   */   {
/* 22:44 */     this.enableAsync = importMetadata.getAnnotationAttributes(EnableAsync.class.getName(), false);
/* 23:45 */     Assert.notNull(this.enableAsync, 
/* 24:46 */       "@EnableAsync is not present on importing class " + 
/* 25:47 */       importMetadata.getClassName());
/* 26:   */   }
/* 27:   */   
/* 28:   */   public abstract Object asyncAdvisor();
/* 29:   */   
/* 30:   */   @Autowired(required=false)
/* 31:   */   void setConfigurers(Collection<AsyncConfigurer> configurers)
/* 32:   */   {
/* 33:62 */     if ((configurers == null) || (configurers.isEmpty())) {
/* 34:63 */       return;
/* 35:   */     }
/* 36:66 */     if (configurers.size() > 1) {
/* 37:67 */       throw new IllegalStateException("only one AsyncConfigurer may exist");
/* 38:   */     }
/* 39:70 */     AsyncConfigurer configurer = (AsyncConfigurer)configurers.iterator().next();
/* 40:71 */     this.executor = configurer.getAsyncExecutor();
/* 41:   */   }
/* 42:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.annotation.AbstractAsyncConfiguration
 * JD-Core Version:    0.7.0.1
 */