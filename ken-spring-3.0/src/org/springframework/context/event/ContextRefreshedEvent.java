/*  1:   */ package org.springframework.context.event;
/*  2:   */ 
/*  3:   */ import org.springframework.context.ApplicationContext;
/*  4:   */ 
/*  5:   */ public class ContextRefreshedEvent
/*  6:   */   extends ApplicationContextEvent
/*  7:   */ {
/*  8:   */   public ContextRefreshedEvent(ApplicationContext source)
/*  9:   */   {
/* 10:36 */     super(source);
/* 11:   */   }
/* 12:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.event.ContextRefreshedEvent
 * JD-Core Version:    0.7.0.1
 */