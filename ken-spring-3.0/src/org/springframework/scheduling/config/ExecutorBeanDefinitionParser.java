/*  1:   */ package org.springframework.scheduling.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*  4:   */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*  5:   */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*  6:   */ import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
/*  7:   */ import org.springframework.beans.factory.xml.ParserContext;
/*  8:   */ import org.springframework.util.StringUtils;
/*  9:   */ import org.w3c.dom.Element;
/* 10:   */ 
/* 11:   */ public class ExecutorBeanDefinitionParser
/* 12:   */   extends AbstractSingleBeanDefinitionParser
/* 13:   */ {
/* 14:   */   protected String getBeanClassName(Element element)
/* 15:   */   {
/* 16:38 */     return "org.springframework.scheduling.config.TaskExecutorFactoryBean";
/* 17:   */   }
/* 18:   */   
/* 19:   */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
/* 20:   */   {
/* 21:43 */     String keepAliveSeconds = element.getAttribute("keep-alive");
/* 22:44 */     if (StringUtils.hasText(keepAliveSeconds)) {
/* 23:45 */       builder.addPropertyValue("keepAliveSeconds", keepAliveSeconds);
/* 24:   */     }
/* 25:47 */     String queueCapacity = element.getAttribute("queue-capacity");
/* 26:48 */     if (StringUtils.hasText(queueCapacity)) {
/* 27:49 */       builder.addPropertyValue("queueCapacity", queueCapacity);
/* 28:   */     }
/* 29:51 */     configureRejectionPolicy(element, builder);
/* 30:52 */     String poolSize = element.getAttribute("pool-size");
/* 31:53 */     if (StringUtils.hasText(poolSize)) {
/* 32:54 */       builder.addPropertyValue("poolSize", poolSize);
/* 33:   */     }
/* 34:   */   }
/* 35:   */   
/* 36:   */   private void configureRejectionPolicy(Element element, BeanDefinitionBuilder builder)
/* 37:   */   {
/* 38:59 */     String rejectionPolicy = element.getAttribute("rejection-policy");
/* 39:60 */     if (!StringUtils.hasText(rejectionPolicy)) {
/* 40:61 */       return;
/* 41:   */     }
/* 42:63 */     String prefix = "java.util.concurrent.ThreadPoolExecutor.";
/* 43:64 */     if (builder.getRawBeanDefinition().getBeanClassName().contains("backport")) {
/* 44:65 */       prefix = "edu.emory.mathcs.backport." + prefix;
/* 45:   */     }
/* 46:   */     String policyClassName;
/* 47:   */     String policyClassName;
/* 48:68 */     if (rejectionPolicy.equals("ABORT"))
/* 49:   */     {
/* 50:69 */       policyClassName = prefix + "AbortPolicy";
/* 51:   */     }
/* 52:   */     else
/* 53:   */     {
/* 54:   */       String policyClassName;
/* 55:71 */       if (rejectionPolicy.equals("CALLER_RUNS"))
/* 56:   */       {
/* 57:72 */         policyClassName = prefix + "CallerRunsPolicy";
/* 58:   */       }
/* 59:   */       else
/* 60:   */       {
/* 61:   */         String policyClassName;
/* 62:74 */         if (rejectionPolicy.equals("DISCARD"))
/* 63:   */         {
/* 64:75 */           policyClassName = prefix + "DiscardPolicy";
/* 65:   */         }
/* 66:   */         else
/* 67:   */         {
/* 68:   */           String policyClassName;
/* 69:77 */           if (rejectionPolicy.equals("DISCARD_OLDEST")) {
/* 70:78 */             policyClassName = prefix + "DiscardOldestPolicy";
/* 71:   */           } else {
/* 72:81 */             policyClassName = rejectionPolicy;
/* 73:   */           }
/* 74:   */         }
/* 75:   */       }
/* 76:   */     }
/* 77:83 */     builder.addPropertyValue("rejectedExecutionHandler", new RootBeanDefinition(policyClassName));
/* 78:   */   }
/* 79:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.config.ExecutorBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */