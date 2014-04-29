/*  1:   */ package org.springframework.scheduling.annotation;
/*  2:   */ 
/*  3:   */ import java.util.concurrent.Future;
/*  4:   */ import java.util.concurrent.TimeUnit;
/*  5:   */ 
/*  6:   */ public class AsyncResult<V>
/*  7:   */   implements Future<V>
/*  8:   */ {
/*  9:   */   private final V value;
/* 10:   */   
/* 11:   */   public AsyncResult(V value)
/* 12:   */   {
/* 13:40 */     this.value = value;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public boolean cancel(boolean mayInterruptIfRunning)
/* 17:   */   {
/* 18:44 */     return false;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public boolean isCancelled()
/* 22:   */   {
/* 23:48 */     return false;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public boolean isDone()
/* 27:   */   {
/* 28:52 */     return true;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public V get()
/* 32:   */   {
/* 33:56 */     return this.value;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public V get(long timeout, TimeUnit unit)
/* 37:   */   {
/* 38:60 */     return this.value;
/* 39:   */   }
/* 40:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.annotation.AsyncResult
 * JD-Core Version:    0.7.0.1
 */