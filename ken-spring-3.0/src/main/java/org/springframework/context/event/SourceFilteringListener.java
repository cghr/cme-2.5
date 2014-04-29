/*  1:   */ package org.springframework.context.event;
/*  2:   */ 
/*  3:   */ import org.springframework.context.ApplicationEvent;
/*  4:   */ import org.springframework.context.ApplicationListener;
/*  5:   */ 
/*  6:   */ public class SourceFilteringListener
/*  7:   */   implements SmartApplicationListener
/*  8:   */ {
/*  9:   */   private final Object source;
/* 10:   */   private SmartApplicationListener delegate;
/* 11:   */   
/* 12:   */   public SourceFilteringListener(Object source, ApplicationListener delegate)
/* 13:   */   {
/* 14:49 */     this.source = source;
/* 15:50 */     this.delegate = ((delegate instanceof SmartApplicationListener) ? 
/* 16:51 */       (SmartApplicationListener)delegate : new GenericApplicationListenerAdapter(delegate));
/* 17:   */   }
/* 18:   */   
/* 19:   */   protected SourceFilteringListener(Object source)
/* 20:   */   {
/* 21:62 */     this.source = source;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void onApplicationEvent(ApplicationEvent event)
/* 25:   */   {
/* 26:67 */     if (event.getSource() == this.source) {
/* 27:68 */       onApplicationEventInternal(event);
/* 28:   */     }
/* 29:   */   }
/* 30:   */   
/* 31:   */   public boolean supportsEventType(Class<? extends ApplicationEvent> eventType)
/* 32:   */   {
/* 33:73 */     return (this.delegate == null) || (this.delegate.supportsEventType(eventType));
/* 34:   */   }
/* 35:   */   
/* 36:   */   public boolean supportsSourceType(Class<?> sourceType)
/* 37:   */   {
/* 38:77 */     return sourceType.isInstance(this.source);
/* 39:   */   }
/* 40:   */   
/* 41:   */   public int getOrder()
/* 42:   */   {
/* 43:81 */     return this.delegate != null ? this.delegate.getOrder() : 2147483647;
/* 44:   */   }
/* 45:   */   
/* 46:   */   protected void onApplicationEventInternal(ApplicationEvent event)
/* 47:   */   {
/* 48:93 */     if (this.delegate == null) {
/* 49:94 */       throw new IllegalStateException(
/* 50:95 */         "Must specify a delegate object or override the onApplicationEventInternal method");
/* 51:   */     }
/* 52:97 */     this.delegate.onApplicationEvent(event);
/* 53:   */   }
/* 54:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.event.SourceFilteringListener
 * JD-Core Version:    0.7.0.1
 */