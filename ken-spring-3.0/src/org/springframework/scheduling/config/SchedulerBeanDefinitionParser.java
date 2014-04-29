/*  1:   */ package org.springframework.scheduling.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*  4:   */ import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
/*  5:   */ import org.springframework.util.StringUtils;
/*  6:   */ import org.w3c.dom.Element;
/*  7:   */ 
/*  8:   */ public class SchedulerBeanDefinitionParser
/*  9:   */   extends AbstractSingleBeanDefinitionParser
/* 10:   */ {
/* 11:   */   protected String getBeanClassName(Element element)
/* 12:   */   {
/* 13:35 */     return "org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler";
/* 14:   */   }
/* 15:   */   
/* 16:   */   protected void doParse(Element element, BeanDefinitionBuilder builder)
/* 17:   */   {
/* 18:40 */     String poolSize = element.getAttribute("pool-size");
/* 19:41 */     if (StringUtils.hasText(poolSize)) {
/* 20:42 */       builder.addPropertyValue("poolSize", poolSize);
/* 21:   */     }
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.config.SchedulerBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */