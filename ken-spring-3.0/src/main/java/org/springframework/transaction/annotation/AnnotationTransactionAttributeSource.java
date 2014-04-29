/*   1:    */ package org.springframework.transaction.annotation;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.lang.reflect.AnnotatedElement;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import java.util.Collections;
/*   7:    */ import java.util.LinkedHashSet;
/*   8:    */ import java.util.Set;
/*   9:    */ import org.springframework.transaction.interceptor.AbstractFallbackTransactionAttributeSource;
/*  10:    */ import org.springframework.transaction.interceptor.TransactionAttribute;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ import org.springframework.util.ClassUtils;
/*  13:    */ 
/*  14:    */ public class AnnotationTransactionAttributeSource
/*  15:    */   extends AbstractFallbackTransactionAttributeSource
/*  16:    */   implements Serializable
/*  17:    */ {
/*  18: 55 */   private static final boolean ejb3Present = ClassUtils.isPresent(
/*  19: 56 */     "javax.ejb.TransactionAttribute", AnnotationTransactionAttributeSource.class.getClassLoader());
/*  20:    */   private final boolean publicMethodsOnly;
/*  21:    */   private final Set<TransactionAnnotationParser> annotationParsers;
/*  22:    */   
/*  23:    */   public AnnotationTransactionAttributeSource()
/*  24:    */   {
/*  25: 69 */     this(true);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public AnnotationTransactionAttributeSource(boolean publicMethodsOnly)
/*  29:    */   {
/*  30: 82 */     this.publicMethodsOnly = publicMethodsOnly;
/*  31: 83 */     this.annotationParsers = new LinkedHashSet(2);
/*  32: 84 */     this.annotationParsers.add(new SpringTransactionAnnotationParser());
/*  33: 85 */     if (ejb3Present) {
/*  34: 86 */       this.annotationParsers.add(new Ejb3TransactionAnnotationParser());
/*  35:    */     }
/*  36:    */   }
/*  37:    */   
/*  38:    */   public AnnotationTransactionAttributeSource(TransactionAnnotationParser annotationParser)
/*  39:    */   {
/*  40: 95 */     this.publicMethodsOnly = true;
/*  41: 96 */     Assert.notNull(annotationParser, "TransactionAnnotationParser must not be null");
/*  42: 97 */     this.annotationParsers = Collections.singleton(annotationParser);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public AnnotationTransactionAttributeSource(Set<TransactionAnnotationParser> annotationParsers)
/*  46:    */   {
/*  47:105 */     this.publicMethodsOnly = true;
/*  48:106 */     Assert.notEmpty(annotationParsers, "At least one TransactionAnnotationParser needs to be specified");
/*  49:107 */     this.annotationParsers = annotationParsers;
/*  50:    */   }
/*  51:    */   
/*  52:    */   protected TransactionAttribute findTransactionAttribute(Method method)
/*  53:    */   {
/*  54:113 */     return determineTransactionAttribute(method);
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected TransactionAttribute findTransactionAttribute(Class<?> clazz)
/*  58:    */   {
/*  59:118 */     return determineTransactionAttribute(clazz);
/*  60:    */   }
/*  61:    */   
/*  62:    */   protected TransactionAttribute determineTransactionAttribute(AnnotatedElement ae)
/*  63:    */   {
/*  64:133 */     for (TransactionAnnotationParser annotationParser : this.annotationParsers)
/*  65:    */     {
/*  66:134 */       TransactionAttribute attr = annotationParser.parseTransactionAnnotation(ae);
/*  67:135 */       if (attr != null) {
/*  68:136 */         return attr;
/*  69:    */       }
/*  70:    */     }
/*  71:139 */     return null;
/*  72:    */   }
/*  73:    */   
/*  74:    */   protected boolean allowPublicMethodsOnly()
/*  75:    */   {
/*  76:147 */     return this.publicMethodsOnly;
/*  77:    */   }
/*  78:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.annotation.AnnotationTransactionAttributeSource
 * JD-Core Version:    0.7.0.1
 */