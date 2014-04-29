/*  1:   */ package org.springframework.web.context.request;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import javax.servlet.http.HttpSessionBindingEvent;
/*  5:   */ import javax.servlet.http.HttpSessionBindingListener;
/*  6:   */ 
/*  7:   */ public class DestructionCallbackBindingListener
/*  8:   */   implements HttpSessionBindingListener, Serializable
/*  9:   */ {
/* 10:   */   private final Runnable destructionCallback;
/* 11:   */   
/* 12:   */   public DestructionCallbackBindingListener(Runnable destructionCallback)
/* 13:   */   {
/* 14:43 */     this.destructionCallback = destructionCallback;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public void valueBound(HttpSessionBindingEvent event) {}
/* 18:   */   
/* 19:   */   public void valueUnbound(HttpSessionBindingEvent event)
/* 20:   */   {
/* 21:51 */     this.destructionCallback.run();
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.request.DestructionCallbackBindingListener
 * JD-Core Version:    0.7.0.1
 */