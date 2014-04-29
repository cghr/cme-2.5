/*   1:    */ package org.springframework.context.annotation;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*   4:    */ import org.springframework.context.support.GenericApplicationContext;
/*   5:    */ import org.springframework.core.env.ConfigurableEnvironment;
/*   6:    */ import org.springframework.util.Assert;
/*   7:    */ 
/*   8:    */ public class AnnotationConfigApplicationContext
/*   9:    */   extends GenericApplicationContext
/*  10:    */ {
/*  11: 50 */   private final AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(this);
/*  12: 52 */   private final ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(this);
/*  13:    */   
/*  14:    */   public AnnotationConfigApplicationContext()
/*  15:    */   {
/*  16: 60 */     delegateEnvironment(super.getEnvironment());
/*  17:    */   }
/*  18:    */   
/*  19:    */   public AnnotationConfigApplicationContext(Class<?>... annotatedClasses)
/*  20:    */   {
/*  21: 70 */     this();
/*  22: 71 */     register(annotatedClasses);
/*  23: 72 */     refresh();
/*  24:    */   }
/*  25:    */   
/*  26:    */   public AnnotationConfigApplicationContext(String... basePackages)
/*  27:    */   {
/*  28: 81 */     this();
/*  29: 82 */     scan(basePackages);
/*  30: 83 */     refresh();
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setEnvironment(ConfigurableEnvironment environment)
/*  34:    */   {
/*  35: 94 */     super.setEnvironment(environment);
/*  36: 95 */     delegateEnvironment(environment);
/*  37:    */   }
/*  38:    */   
/*  39:    */   private void delegateEnvironment(ConfigurableEnvironment environment)
/*  40:    */   {
/*  41: 99 */     this.reader.setEnvironment(environment);
/*  42:100 */     this.scanner.setEnvironment(environment);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator)
/*  46:    */   {
/*  47:113 */     this.reader.setBeanNameGenerator(beanNameGenerator);
/*  48:114 */     this.scanner.setBeanNameGenerator(beanNameGenerator);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setScopeMetadataResolver(ScopeMetadataResolver scopeMetadataResolver)
/*  52:    */   {
/*  53:124 */     this.reader.setScopeMetadataResolver(scopeMetadataResolver);
/*  54:125 */     this.scanner.setScopeMetadataResolver(scopeMetadataResolver);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void register(Class<?>... annotatedClasses)
/*  58:    */   {
/*  59:140 */     Assert.notEmpty(annotatedClasses, "At least one annotated class must be specified");
/*  60:141 */     this.reader.register(annotatedClasses);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void scan(String... basePackages)
/*  64:    */   {
/*  65:153 */     Assert.notEmpty(basePackages, "At least one base package must be specified");
/*  66:154 */     this.scanner.scan(basePackages);
/*  67:    */   }
/*  68:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.AnnotationConfigApplicationContext
 * JD-Core Version:    0.7.0.1
 */