/*  1:   */ package org.springframework.scheduling.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
/*  4:   */ 
/*  5:   */ public class TaskNamespaceHandler
/*  6:   */   extends NamespaceHandlerSupport
/*  7:   */ {
/*  8:   */   public void init()
/*  9:   */   {
/* 10:30 */     registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenBeanDefinitionParser());
/* 11:31 */     registerBeanDefinitionParser("executor", new ExecutorBeanDefinitionParser());
/* 12:32 */     registerBeanDefinitionParser("scheduled-tasks", new ScheduledTasksBeanDefinitionParser());
/* 13:33 */     registerBeanDefinitionParser("scheduler", new SchedulerBeanDefinitionParser());
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.config.TaskNamespaceHandler
 * JD-Core Version:    0.7.0.1
 */