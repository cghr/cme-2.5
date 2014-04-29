/*   1:    */ package org.springframework.beans.factory.annotation;
/*   2:    */ 
/*   3:    */ import java.lang.annotation.Annotation;
/*   4:    */ import java.util.Set;
/*   5:    */ import org.springframework.beans.BeansException;
/*   6:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*   7:    */ import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
/*   8:    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*   9:    */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*  10:    */ import org.springframework.core.Ordered;
/*  11:    */ import org.springframework.util.ClassUtils;
/*  12:    */ 
/*  13:    */ public class CustomAutowireConfigurer
/*  14:    */   implements BeanFactoryPostProcessor, BeanClassLoaderAware, Ordered
/*  15:    */ {
/*  16: 51 */   private int order = 2147483647;
/*  17:    */   private Set customQualifierTypes;
/*  18: 55 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*  19:    */   
/*  20:    */   public void setOrder(int order)
/*  21:    */   {
/*  22: 59 */     this.order = order;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public int getOrder()
/*  26:    */   {
/*  27: 63 */     return this.order;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setBeanClassLoader(ClassLoader beanClassLoader)
/*  31:    */   {
/*  32: 67 */     this.beanClassLoader = beanClassLoader;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setCustomQualifierTypes(Set customQualifierTypes)
/*  36:    */   {
/*  37: 81 */     this.customQualifierTypes = customQualifierTypes;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*  41:    */     throws BeansException
/*  42:    */   {
/*  43: 87 */     if (this.customQualifierTypes != null)
/*  44:    */     {
/*  45: 88 */       if (!(beanFactory instanceof DefaultListableBeanFactory)) {
/*  46: 89 */         throw new IllegalStateException(
/*  47: 90 */           "CustomAutowireConfigurer needs to operate on a DefaultListableBeanFactory");
/*  48:    */       }
/*  49: 92 */       DefaultListableBeanFactory dlbf = (DefaultListableBeanFactory)beanFactory;
/*  50: 93 */       if (!(dlbf.getAutowireCandidateResolver() instanceof QualifierAnnotationAutowireCandidateResolver)) {
/*  51: 94 */         dlbf.setAutowireCandidateResolver(new QualifierAnnotationAutowireCandidateResolver());
/*  52:    */       }
/*  53: 96 */       QualifierAnnotationAutowireCandidateResolver resolver = 
/*  54: 97 */         (QualifierAnnotationAutowireCandidateResolver)dlbf.getAutowireCandidateResolver();
/*  55: 98 */       for (Object value : this.customQualifierTypes)
/*  56:    */       {
/*  57: 99 */         Class customType = null;
/*  58:100 */         if ((value instanceof Class))
/*  59:    */         {
/*  60:101 */           customType = (Class)value;
/*  61:    */         }
/*  62:103 */         else if ((value instanceof String))
/*  63:    */         {
/*  64:104 */           String className = (String)value;
/*  65:105 */           customType = ClassUtils.resolveClassName(className, this.beanClassLoader);
/*  66:    */         }
/*  67:    */         else
/*  68:    */         {
/*  69:108 */           throw new IllegalArgumentException(
/*  70:109 */             "Invalid value [" + value + "] for custom qualifier type: needs to be Class or String.");
/*  71:    */         }
/*  72:111 */         if (!Annotation.class.isAssignableFrom(customType)) {
/*  73:112 */           throw new IllegalArgumentException(
/*  74:113 */             "Qualifier type [" + customType.getName() + "] needs to be annotation type");
/*  75:    */         }
/*  76:115 */         resolver.addQualifierType(customType);
/*  77:    */       }
/*  78:    */     }
/*  79:    */   }
/*  80:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.annotation.CustomAutowireConfigurer
 * JD-Core Version:    0.7.0.1
 */