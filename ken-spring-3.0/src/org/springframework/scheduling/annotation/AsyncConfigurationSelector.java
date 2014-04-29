/*  1:   */ package org.springframework.scheduling.annotation;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import org.springframework.context.annotation.ImportSelector;
/*  5:   */ import org.springframework.context.annotation.ImportSelectorContext;
/*  6:   */ import org.springframework.core.type.AnnotationMetadata;
/*  7:   */ import org.springframework.util.Assert;
/*  8:   */ 
/*  9:   */ public class AsyncConfigurationSelector
/* 10:   */   implements ImportSelector
/* 11:   */ {
/* 12:   */   public String[] selectImports(ImportSelectorContext context)
/* 13:   */   {
/* 14:51 */     AnnotationMetadata importingClassMetadata = context.getImportingClassMetadata();
/* 15:52 */     Map<String, Object> enableAsync = 
/* 16:53 */       importingClassMetadata.getAnnotationAttributes(EnableAsync.class.getName());
/* 17:54 */     Assert.notNull(enableAsync, 
/* 18:55 */       "@EnableAsync is not present on importing class " + 
/* 19:56 */       importingClassMetadata.getClassName());
/* 20:58 */     switch ((org.springframework.context.annotation.AdviceMode)enableAsync.get("mode"))
/* 21:   */     {
/* 22:   */     case ASPECTJ: 
/* 23:60 */       return new String[] { ProxyAsyncConfiguration.class.getName() };
/* 24:   */     case PROXY: 
/* 25:62 */       return new String[] { "org.springframework.scheduling.aspectj.AspectJAsyncConfiguration" };
/* 26:   */     }
/* 27:64 */     throw new IllegalArgumentException("Unknown AdviceMode " + enableAsync.get("mode"));
/* 28:   */   }
/* 29:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.annotation.AsyncConfigurationSelector
 * JD-Core Version:    0.7.0.1
 */