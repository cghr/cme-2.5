/*   1:    */ package org.springframework.cache.annotation;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.lang.reflect.AnnotatedElement;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import java.util.Collections;
/*   7:    */ import java.util.LinkedHashSet;
/*   8:    */ import java.util.Set;
/*   9:    */ import org.springframework.cache.interceptor.AbstractFallbackCacheOperationSource;
/*  10:    */ import org.springframework.cache.interceptor.CacheOperation;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ 
/*  13:    */ public class AnnotationCacheOperationSource
/*  14:    */   extends AbstractFallbackCacheOperationSource
/*  15:    */   implements Serializable
/*  16:    */ {
/*  17:    */   private final boolean publicMethodsOnly;
/*  18:    */   private final Set<CacheAnnotationParser> annotationParsers;
/*  19:    */   
/*  20:    */   public AnnotationCacheOperationSource()
/*  21:    */   {
/*  22: 56 */     this(true);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public AnnotationCacheOperationSource(boolean publicMethodsOnly)
/*  26:    */   {
/*  27: 67 */     this.publicMethodsOnly = publicMethodsOnly;
/*  28: 68 */     this.annotationParsers = new LinkedHashSet(1);
/*  29: 69 */     this.annotationParsers.add(new SpringCacheAnnotationParser());
/*  30:    */   }
/*  31:    */   
/*  32:    */   public AnnotationCacheOperationSource(CacheAnnotationParser... annotationParsers)
/*  33:    */   {
/*  34: 77 */     this.publicMethodsOnly = true;
/*  35: 78 */     Assert.notEmpty(annotationParsers, "At least one CacheAnnotationParser needs to be specified");
/*  36: 79 */     Set<CacheAnnotationParser> parsers = new LinkedHashSet(annotationParsers.length);
/*  37: 80 */     Collections.addAll(parsers, annotationParsers);
/*  38: 81 */     this.annotationParsers = parsers;
/*  39:    */   }
/*  40:    */   
/*  41:    */   protected CacheOperation findCacheOperation(Class<?> clazz)
/*  42:    */   {
/*  43: 87 */     return determineCacheOperation(clazz);
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected CacheOperation findCacheOperation(Method method)
/*  47:    */   {
/*  48: 92 */     return determineCacheOperation(method);
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected CacheOperation determineCacheOperation(AnnotatedElement ae)
/*  52:    */   {
/*  53:107 */     for (CacheAnnotationParser annotationParser : this.annotationParsers)
/*  54:    */     {
/*  55:108 */       CacheOperation attr = annotationParser.parseCacheAnnotation(ae);
/*  56:109 */       if (attr != null) {
/*  57:110 */         return attr;
/*  58:    */       }
/*  59:    */     }
/*  60:113 */     return null;
/*  61:    */   }
/*  62:    */   
/*  63:    */   protected boolean allowPublicMethodsOnly()
/*  64:    */   {
/*  65:121 */     return this.publicMethodsOnly;
/*  66:    */   }
/*  67:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.annotation.AnnotationCacheOperationSource
 * JD-Core Version:    0.7.0.1
 */