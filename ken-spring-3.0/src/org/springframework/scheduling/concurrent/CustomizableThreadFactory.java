/*  1:   */ package org.springframework.scheduling.concurrent;
/*  2:   */ 
/*  3:   */ import java.util.concurrent.ThreadFactory;
/*  4:   */ import org.springframework.util.CustomizableThreadCreator;
/*  5:   */ 
/*  6:   */ public class CustomizableThreadFactory
/*  7:   */   extends CustomizableThreadCreator
/*  8:   */   implements ThreadFactory
/*  9:   */ {
/* 10:   */   public CustomizableThreadFactory() {}
/* 11:   */   
/* 12:   */   public CustomizableThreadFactory(String threadNamePrefix)
/* 13:   */   {
/* 14:49 */     super(threadNamePrefix);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public Thread newThread(Runnable runnable)
/* 18:   */   {
/* 19:54 */     return createThread(runnable);
/* 20:   */   }
/* 21:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.concurrent.CustomizableThreadFactory
 * JD-Core Version:    0.7.0.1
 */