/*  1:   */ package org.springframework.context.event;
/*  2:   */ 
/*  3:   */ import org.springframework.context.ApplicationContext;
/*  4:   */ import org.springframework.context.ApplicationEvent;
/*  5:   */ 
/*  6:   */ public abstract class ApplicationContextEvent
/*  7:   */   extends ApplicationEvent
/*  8:   */ {
/*  9:   */   public ApplicationContextEvent(ApplicationContext source)
/* 10:   */   {
/* 11:36 */     super(source);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public final ApplicationContext getApplicationContext()
/* 15:   */   {
/* 16:43 */     return (ApplicationContext)getSource();
/* 17:   */   }
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.event.ApplicationContextEvent
 * JD-Core Version:    0.7.0.1
 */