/*  1:   */ package org.springframework.cache.annotation;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import java.lang.reflect.AnnotatedElement;
/*  5:   */ import org.springframework.cache.interceptor.CacheEvictOperation;
/*  6:   */ import org.springframework.cache.interceptor.CacheOperation;
/*  7:   */ import org.springframework.cache.interceptor.CacheUpdateOperation;
/*  8:   */ import org.springframework.core.annotation.AnnotationUtils;
/*  9:   */ 
/* 10:   */ public class SpringCacheAnnotationParser
/* 11:   */   implements CacheAnnotationParser, Serializable
/* 12:   */ {
/* 13:   */   public CacheOperation parseCacheAnnotation(AnnotatedElement ae)
/* 14:   */   {
/* 15:38 */     Cacheable update = (Cacheable)AnnotationUtils.getAnnotation(ae, Cacheable.class);
/* 16:39 */     if (update != null) {
/* 17:40 */       return parseCacheableAnnotation(ae, update);
/* 18:   */     }
/* 19:42 */     CacheEvict evict = (CacheEvict)AnnotationUtils.getAnnotation(ae, CacheEvict.class);
/* 20:43 */     if (evict != null) {
/* 21:44 */       return parseEvictAnnotation(ae, evict);
/* 22:   */     }
/* 23:46 */     return null;
/* 24:   */   }
/* 25:   */   
/* 26:   */   CacheUpdateOperation parseCacheableAnnotation(AnnotatedElement ae, Cacheable ann)
/* 27:   */   {
/* 28:50 */     CacheUpdateOperation cuo = new CacheUpdateOperation();
/* 29:51 */     cuo.setCacheNames(ann.value());
/* 30:52 */     cuo.setCondition(ann.condition());
/* 31:53 */     cuo.setKey(ann.key());
/* 32:54 */     cuo.setName(ae.toString());
/* 33:55 */     return cuo;
/* 34:   */   }
/* 35:   */   
/* 36:   */   CacheEvictOperation parseEvictAnnotation(AnnotatedElement ae, CacheEvict ann)
/* 37:   */   {
/* 38:59 */     CacheEvictOperation ceo = new CacheEvictOperation();
/* 39:60 */     ceo.setCacheNames(ann.value());
/* 40:61 */     ceo.setCondition(ann.condition());
/* 41:62 */     ceo.setKey(ann.key());
/* 42:63 */     ceo.setCacheWide(ann.allEntries());
/* 43:64 */     ceo.setName(ae.toString());
/* 44:65 */     return ceo;
/* 45:   */   }
/* 46:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.annotation.SpringCacheAnnotationParser
 * JD-Core Version:    0.7.0.1
 */