/*   1:    */ package org.springframework.transaction.interceptor;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.Map;
/*   5:    */ import org.springframework.beans.factory.BeanFactory;
/*   6:    */ import org.springframework.beans.factory.BeanFactoryUtils;
/*   7:    */ import org.springframework.beans.factory.annotation.Qualifier;
/*   8:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*   9:    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*  10:    */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*  11:    */ import org.springframework.beans.factory.support.AutowireCandidateQualifier;
/*  12:    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*  13:    */ import org.springframework.transaction.PlatformTransactionManager;
/*  14:    */ import org.springframework.util.ObjectUtils;
/*  15:    */ 
/*  16:    */ public abstract class TransactionAspectUtils
/*  17:    */ {
/*  18:    */   public static PlatformTransactionManager getTransactionManager(BeanFactory beanFactory, String qualifier)
/*  19:    */   {
/*  20: 51 */     if ((beanFactory instanceof ConfigurableListableBeanFactory)) {
/*  21: 53 */       return getTransactionManager((ConfigurableListableBeanFactory)beanFactory, qualifier);
/*  22:    */     }
/*  23: 55 */     if (beanFactory.containsBean(qualifier)) {
/*  24: 57 */       return (PlatformTransactionManager)beanFactory.getBean(qualifier, PlatformTransactionManager.class);
/*  25:    */     }
/*  26: 60 */     throw new IllegalStateException("No matching PlatformTransactionManager bean found for bean name '" + 
/*  27: 61 */       qualifier + "'! (Note: Qualifier matching not supported because given BeanFactory does not " + 
/*  28: 62 */       "implement ConfigurableListableBeanFactory.)");
/*  29:    */   }
/*  30:    */   
/*  31:    */   public static PlatformTransactionManager getTransactionManager(ConfigurableListableBeanFactory bf, String qualifier)
/*  32:    */   {
/*  33: 75 */     Map<String, PlatformTransactionManager> tms = 
/*  34: 76 */       BeanFactoryUtils.beansOfTypeIncludingAncestors(bf, PlatformTransactionManager.class);
/*  35: 77 */     PlatformTransactionManager chosen = null;
/*  36: 78 */     for (String beanName : tms.keySet()) {
/*  37: 79 */       if (isQualifierMatch(qualifier, beanName, bf))
/*  38:    */       {
/*  39: 80 */         if (chosen != null) {
/*  40: 81 */           throw new IllegalStateException("No unique PlatformTransactionManager bean found for qualifier '" + 
/*  41: 82 */             qualifier + "'");
/*  42:    */         }
/*  43: 84 */         chosen = (PlatformTransactionManager)tms.get(beanName);
/*  44:    */       }
/*  45:    */     }
/*  46: 87 */     if (chosen != null) {
/*  47: 88 */       return chosen;
/*  48:    */     }
/*  49: 91 */     throw new IllegalStateException("No matching PlatformTransactionManager bean found for qualifier '" + 
/*  50: 92 */       qualifier + "' - neither qualifier match nor bean name match!");
/*  51:    */   }
/*  52:    */   
/*  53:    */   private static boolean isQualifierMatch(String qualifier, String beanName, ConfigurableListableBeanFactory bf)
/*  54:    */   {
/*  55:106 */     if (bf.containsBeanDefinition(beanName))
/*  56:    */     {
/*  57:107 */       BeanDefinition bd = bf.getMergedBeanDefinition(beanName);
/*  58:108 */       if ((bd instanceof AbstractBeanDefinition))
/*  59:    */       {
/*  60:109 */         AbstractBeanDefinition abd = (AbstractBeanDefinition)bd;
/*  61:110 */         AutowireCandidateQualifier candidate = abd.getQualifier(Qualifier.class.getName());
/*  62:111 */         if (((candidate != null) && (qualifier.equals(candidate.getAttribute(AutowireCandidateQualifier.VALUE_KEY)))) || 
/*  63:112 */           (qualifier.equals(beanName)) || (ObjectUtils.containsElement(bf.getAliases(beanName), qualifier))) {
/*  64:113 */           return true;
/*  65:    */         }
/*  66:    */       }
/*  67:116 */       if ((bd instanceof RootBeanDefinition))
/*  68:    */       {
/*  69:117 */         Method factoryMethod = ((RootBeanDefinition)bd).getResolvedFactoryMethod();
/*  70:118 */         if (factoryMethod != null)
/*  71:    */         {
/*  72:119 */           Qualifier targetAnnotation = (Qualifier)factoryMethod.getAnnotation(Qualifier.class);
/*  73:120 */           if ((targetAnnotation != null) && (qualifier.equals(targetAnnotation.value()))) {
/*  74:121 */             return true;
/*  75:    */           }
/*  76:    */         }
/*  77:    */       }
/*  78:    */     }
/*  79:126 */     return false;
/*  80:    */   }
/*  81:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.interceptor.TransactionAspectUtils
 * JD-Core Version:    0.7.0.1
 */