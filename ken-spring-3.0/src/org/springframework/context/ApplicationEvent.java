/*  1:   */ package org.springframework.context;
/*  2:   */ 
/*  3:   */ import java.util.EventObject;
/*  4:   */ 
/*  5:   */ public abstract class ApplicationEvent
/*  6:   */   extends EventObject
/*  7:   */ {
/*  8:   */   private static final long serialVersionUID = 7099057708183571937L;
/*  9:   */   private final long timestamp;
/* 10:   */   
/* 11:   */   public ApplicationEvent(Object source)
/* 12:   */   {
/* 13:42 */     super(source);
/* 14:43 */     this.timestamp = System.currentTimeMillis();
/* 15:   */   }
/* 16:   */   
/* 17:   */   public final long getTimestamp()
/* 18:   */   {
/* 19:51 */     return this.timestamp;
/* 20:   */   }
/* 21:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.ApplicationEvent
 * JD-Core Version:    0.7.0.1
 */