/*  1:   */ package org.springframework.beans.factory.config;
/*  2:   */ 
/*  3:   */ import org.apache.commons.logging.Log;
/*  4:   */ import org.apache.commons.logging.LogFactory;
/*  5:   */ import org.springframework.beans.BeansException;
/*  6:   */ import org.springframework.util.StringUtils;
/*  7:   */ 
/*  8:   */ public class DeprecatedBeanWarner
/*  9:   */   implements BeanFactoryPostProcessor
/* 10:   */ {
/* 11:36 */   protected transient Log logger = LogFactory.getLog(getClass());
/* 12:   */   
/* 13:   */   public void setLoggerName(String loggerName)
/* 14:   */   {
/* 15:47 */     this.logger = LogFactory.getLog(loggerName);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/* 19:   */     throws BeansException
/* 20:   */   {
/* 21:52 */     if (isLogEnabled())
/* 22:   */     {
/* 23:53 */       String[] beanNames = beanFactory.getBeanDefinitionNames();
/* 24:54 */       for (String beanName : beanNames)
/* 25:   */       {
/* 26:55 */         Class<?> beanType = beanFactory.getType(beanName);
/* 27:56 */         if ((beanType != null) && (beanType.isAnnotationPresent(Deprecated.class)))
/* 28:   */         {
/* 29:57 */           BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
/* 30:58 */           logDeprecatedBean(beanName, beanDefinition);
/* 31:   */         }
/* 32:   */       }
/* 33:   */     }
/* 34:   */   }
/* 35:   */   
/* 36:   */   protected void logDeprecatedBean(String beanName, BeanDefinition beanDefinition)
/* 37:   */   {
/* 38:71 */     StringBuilder builder = new StringBuilder();
/* 39:72 */     builder.append(beanDefinition.getBeanClassName());
/* 40:73 */     builder.append(" ['");
/* 41:74 */     builder.append(beanName);
/* 42:75 */     builder.append('\'');
/* 43:76 */     String resourceDescription = beanDefinition.getResourceDescription();
/* 44:77 */     if (StringUtils.hasLength(resourceDescription))
/* 45:   */     {
/* 46:78 */       builder.append(" in ");
/* 47:79 */       builder.append(resourceDescription);
/* 48:   */     }
/* 49:81 */     builder.append(" ] has been deprecated");
/* 50:82 */     this.logger.warn(builder.toString());
/* 51:   */   }
/* 52:   */   
/* 53:   */   protected boolean isLogEnabled()
/* 54:   */   {
/* 55:91 */     return this.logger.isWarnEnabled();
/* 56:   */   }
/* 57:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.DeprecatedBeanWarner
 * JD-Core Version:    0.7.0.1
 */