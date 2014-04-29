/*  1:   */ package org.springframework.context.event;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Constructor;
/*  4:   */ import org.aopalliance.intercept.MethodInterceptor;
/*  5:   */ import org.aopalliance.intercept.MethodInvocation;
/*  6:   */ import org.springframework.beans.factory.InitializingBean;
/*  7:   */ import org.springframework.context.ApplicationEvent;
/*  8:   */ import org.springframework.context.ApplicationEventPublisher;
/*  9:   */ import org.springframework.context.ApplicationEventPublisherAware;
/* 10:   */ 
/* 11:   */ public class EventPublicationInterceptor
/* 12:   */   implements MethodInterceptor, ApplicationEventPublisherAware, InitializingBean
/* 13:   */ {
/* 14:   */   private Constructor applicationEventClassConstructor;
/* 15:   */   private ApplicationEventPublisher applicationEventPublisher;
/* 16:   */   
/* 17:   */   public void setApplicationEventClass(Class applicationEventClass)
/* 18:   */   {
/* 19:66 */     if ((ApplicationEvent.class.equals(applicationEventClass)) || 
/* 20:67 */       (!ApplicationEvent.class.isAssignableFrom(applicationEventClass))) {
/* 21:68 */       throw new IllegalArgumentException("applicationEventClass needs to extend ApplicationEvent");
/* 22:   */     }
/* 23:   */     try
/* 24:   */     {
/* 25:71 */       this.applicationEventClassConstructor = 
/* 26:72 */         applicationEventClass.getConstructor(new Class[] { Object.class });
/* 27:   */     }
/* 28:   */     catch (NoSuchMethodException ex)
/* 29:   */     {
/* 30:75 */       throw new IllegalArgumentException("applicationEventClass [" + 
/* 31:76 */         applicationEventClass.getName() + "] does not have the required Object constructor: " + ex);
/* 32:   */     }
/* 33:   */   }
/* 34:   */   
/* 35:   */   public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher)
/* 36:   */   {
/* 37:81 */     this.applicationEventPublisher = applicationEventPublisher;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public void afterPropertiesSet()
/* 41:   */     throws Exception
/* 42:   */   {
/* 43:85 */     if (this.applicationEventClassConstructor == null) {
/* 44:86 */       throw new IllegalArgumentException("applicationEventClass is required");
/* 45:   */     }
/* 46:   */   }
/* 47:   */   
/* 48:   */   public Object invoke(MethodInvocation invocation)
/* 49:   */     throws Throwable
/* 50:   */   {
/* 51:92 */     Object retVal = invocation.proceed();
/* 52:   */     
/* 53:94 */     ApplicationEvent event = 
/* 54:95 */       (ApplicationEvent)this.applicationEventClassConstructor.newInstance(new Object[] { invocation.getThis() });
/* 55:96 */     this.applicationEventPublisher.publishEvent(event);
/* 56:   */     
/* 57:98 */     return retVal;
/* 58:   */   }
/* 59:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.event.EventPublicationInterceptor
 * JD-Core Version:    0.7.0.1
 */