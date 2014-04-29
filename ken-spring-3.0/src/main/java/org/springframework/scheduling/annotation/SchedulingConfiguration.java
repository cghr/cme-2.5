/*  1:   */ package org.springframework.scheduling.annotation;
/*  2:   */ 
/*  3:   */ import org.springframework.context.annotation.Bean;
/*  4:   */ import org.springframework.context.annotation.Configuration;
/*  5:   */ import org.springframework.context.annotation.Role;
/*  6:   */ 
/*  7:   */ @Configuration
/*  8:   */ public class SchedulingConfiguration
/*  9:   */ {
/* 10:   */   @Bean(name={"org.springframework.context.annotation.internalScheduledAnnotationProcessor"})
/* 11:   */   @Role(2)
/* 12:   */   public ScheduledAnnotationBeanPostProcessor scheduledAnnotationProcessor()
/* 13:   */   {
/* 14:45 */     return new ScheduledAnnotationBeanPostProcessor();
/* 15:   */   }
/* 16:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.annotation.SchedulingConfiguration
 * JD-Core Version:    0.7.0.1
 */