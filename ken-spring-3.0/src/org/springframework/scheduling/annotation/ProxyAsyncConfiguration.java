/*  1:   */ package org.springframework.scheduling.annotation;
/*  2:   */ 
/*  3:   */ import java.lang.annotation.Annotation;
/*  4:   */ import java.util.Map;
/*  5:   */ import org.springframework.context.annotation.Bean;
/*  6:   */ import org.springframework.context.annotation.Configuration;
/*  7:   */ import org.springframework.context.annotation.Role;
/*  8:   */ import org.springframework.core.annotation.AnnotationUtils;
/*  9:   */ import org.springframework.util.Assert;
/* 10:   */ 
/* 11:   */ @Configuration
/* 12:   */ public class ProxyAsyncConfiguration
/* 13:   */   extends AbstractAsyncConfiguration
/* 14:   */ {
/* 15:   */   @Bean(name={"org.springframework.context.annotation.internalAsyncAnnotationProcessor"})
/* 16:   */   @Role(2)
/* 17:   */   public AsyncAnnotationBeanPostProcessor asyncAdvisor()
/* 18:   */   {
/* 19:45 */     Assert.notNull(this.enableAsync, "@EnableAsync annotation metadata was not injected");
/* 20:   */     
/* 21:47 */     AsyncAnnotationBeanPostProcessor bpp = new AsyncAnnotationBeanPostProcessor();
/* 22:   */     
/* 23:   */ 
/* 24:50 */     Class<? extends Annotation> customAsyncAnnotation = 
/* 25:51 */       (Class)this.enableAsync.get("annotation");
/* 26:52 */     if (customAsyncAnnotation != AnnotationUtils.getDefaultValue(EnableAsync.class, "annotation")) {
/* 27:53 */       bpp.setAsyncAnnotationType(customAsyncAnnotation);
/* 28:   */     }
/* 29:56 */     if (this.executor != null) {
/* 30:57 */       bpp.setExecutor(this.executor);
/* 31:   */     }
/* 32:60 */     bpp.setProxyTargetClass(((Boolean)this.enableAsync.get("proxyTargetClass")).booleanValue());
/* 33:   */     
/* 34:62 */     bpp.setOrder(((Integer)this.enableAsync.get("order")).intValue());
/* 35:   */     
/* 36:64 */     return bpp;
/* 37:   */   }
/* 38:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.annotation.ProxyAsyncConfiguration
 * JD-Core Version:    0.7.0.1
 */