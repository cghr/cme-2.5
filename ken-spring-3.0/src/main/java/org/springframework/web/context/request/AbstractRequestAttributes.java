/*  1:   */ package org.springframework.web.context.request;
/*  2:   */ 
/*  3:   */ import java.util.LinkedHashMap;
/*  4:   */ import java.util.Map;
/*  5:   */ import org.springframework.util.Assert;
/*  6:   */ 
/*  7:   */ public abstract class AbstractRequestAttributes
/*  8:   */   implements RequestAttributes
/*  9:   */ {
/* 10:36 */   protected final Map<String, Runnable> requestDestructionCallbacks = new LinkedHashMap(8);
/* 11:38 */   private volatile boolean requestActive = true;
/* 12:   */   
/* 13:   */   public void requestCompleted()
/* 14:   */   {
/* 15:47 */     executeRequestDestructionCallbacks();
/* 16:48 */     updateAccessedSessionAttributes();
/* 17:49 */     this.requestActive = false;
/* 18:   */   }
/* 19:   */   
/* 20:   */   protected final boolean isRequestActive()
/* 21:   */   {
/* 22:57 */     return this.requestActive;
/* 23:   */   }
/* 24:   */   
/* 25:   */   protected final void registerRequestDestructionCallback(String name, Runnable callback)
/* 26:   */   {
/* 27:66 */     Assert.notNull(name, "Name must not be null");
/* 28:67 */     Assert.notNull(callback, "Callback must not be null");
/* 29:68 */     synchronized (this.requestDestructionCallbacks)
/* 30:   */     {
/* 31:69 */       this.requestDestructionCallbacks.put(name, callback);
/* 32:   */     }
/* 33:   */   }
/* 34:   */   
/* 35:   */   protected final void removeRequestDestructionCallback(String name)
/* 36:   */   {
/* 37:78 */     Assert.notNull(name, "Name must not be null");
/* 38:79 */     synchronized (this.requestDestructionCallbacks)
/* 39:   */     {
/* 40:80 */       this.requestDestructionCallbacks.remove(name);
/* 41:   */     }
/* 42:   */   }
/* 43:   */   
/* 44:   */   private void executeRequestDestructionCallbacks()
/* 45:   */   {
/* 46:89 */     synchronized (this.requestDestructionCallbacks)
/* 47:   */     {
/* 48:90 */       for (Runnable runnable : this.requestDestructionCallbacks.values()) {
/* 49:91 */         runnable.run();
/* 50:   */       }
/* 51:93 */       this.requestDestructionCallbacks.clear();
/* 52:   */     }
/* 53:   */   }
/* 54:   */   
/* 55:   */   protected abstract void updateAccessedSessionAttributes();
/* 56:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.request.AbstractRequestAttributes
 * JD-Core Version:    0.7.0.1
 */