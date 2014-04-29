/*  1:   */ package org.springframework.context.event;
/*  2:   */ 
/*  3:   */ import org.springframework.aop.support.AopUtils;
/*  4:   */ import org.springframework.context.ApplicationEvent;
/*  5:   */ import org.springframework.context.ApplicationListener;
/*  6:   */ import org.springframework.core.GenericTypeResolver;
/*  7:   */ import org.springframework.core.Ordered;
/*  8:   */ import org.springframework.util.Assert;
/*  9:   */ 
/* 10:   */ public class GenericApplicationListenerAdapter
/* 11:   */   implements SmartApplicationListener
/* 12:   */ {
/* 13:   */   private final ApplicationListener delegate;
/* 14:   */   
/* 15:   */   public GenericApplicationListenerAdapter(ApplicationListener delegate)
/* 16:   */   {
/* 17:44 */     Assert.notNull(delegate, "Delegate listener must not be null");
/* 18:45 */     this.delegate = delegate;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void onApplicationEvent(ApplicationEvent event)
/* 22:   */   {
/* 23:51 */     this.delegate.onApplicationEvent(event);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public boolean supportsEventType(Class<? extends ApplicationEvent> eventType)
/* 27:   */   {
/* 28:55 */     Class typeArg = GenericTypeResolver.resolveTypeArgument(this.delegate.getClass(), ApplicationListener.class);
/* 29:56 */     if ((typeArg == null) || (typeArg.equals(ApplicationEvent.class)))
/* 30:   */     {
/* 31:57 */       Class targetClass = AopUtils.getTargetClass(this.delegate);
/* 32:58 */       if (targetClass != this.delegate.getClass()) {
/* 33:59 */         typeArg = GenericTypeResolver.resolveTypeArgument(targetClass, ApplicationListener.class);
/* 34:   */       }
/* 35:   */     }
/* 36:62 */     return (typeArg == null) || (typeArg.isAssignableFrom(eventType));
/* 37:   */   }
/* 38:   */   
/* 39:   */   public boolean supportsSourceType(Class<?> sourceType)
/* 40:   */   {
/* 41:66 */     return true;
/* 42:   */   }
/* 43:   */   
/* 44:   */   public int getOrder()
/* 45:   */   {
/* 46:70 */     return (this.delegate instanceof Ordered) ? ((Ordered)this.delegate).getOrder() : 2147483647;
/* 47:   */   }
/* 48:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.event.GenericApplicationListenerAdapter
 * JD-Core Version:    0.7.0.1
 */