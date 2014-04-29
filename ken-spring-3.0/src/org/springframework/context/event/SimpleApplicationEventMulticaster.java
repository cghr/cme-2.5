/*  1:   */ package org.springframework.context.event;
/*  2:   */ 
/*  3:   */ import java.util.concurrent.Executor;
/*  4:   */ import org.springframework.beans.factory.BeanFactory;
/*  5:   */ import org.springframework.context.ApplicationEvent;
/*  6:   */ import org.springframework.context.ApplicationListener;
/*  7:   */ 
/*  8:   */ public class SimpleApplicationEventMulticaster
/*  9:   */   extends AbstractApplicationEventMulticaster
/* 10:   */ {
/* 11:   */   private Executor taskExecutor;
/* 12:   */   
/* 13:   */   public SimpleApplicationEventMulticaster() {}
/* 14:   */   
/* 15:   */   public SimpleApplicationEventMulticaster(BeanFactory beanFactory)
/* 16:   */   {
/* 17:57 */     setBeanFactory(beanFactory);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void setTaskExecutor(Executor taskExecutor)
/* 21:   */   {
/* 22:73 */     this.taskExecutor = taskExecutor;
/* 23:   */   }
/* 24:   */   
/* 25:   */   protected Executor getTaskExecutor()
/* 26:   */   {
/* 27:80 */     return this.taskExecutor;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void multicastEvent(final ApplicationEvent event)
/* 31:   */   {
/* 32:86 */     for (final ApplicationListener listener : getApplicationListeners(event))
/* 33:   */     {
/* 34:87 */       Executor executor = getTaskExecutor();
/* 35:88 */       if (executor != null) {
/* 36:89 */         executor.execute(new Runnable()
/* 37:   */         {
/* 38:   */           public void run()
/* 39:   */           {
/* 40:92 */             listener.onApplicationEvent(event);
/* 41:   */           }
/* 42:   */         });
/* 43:   */       } else {
/* 44:97 */         listener.onApplicationEvent(event);
/* 45:   */       }
/* 46:   */     }
/* 47:   */   }
/* 48:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.event.SimpleApplicationEventMulticaster
 * JD-Core Version:    0.7.0.1
 */