/*  1:   */ package org.springframework.jmx.export;
/*  2:   */ 
/*  3:   */ import java.util.Set;
/*  4:   */ import javax.management.NotificationListener;
/*  5:   */ import org.springframework.beans.factory.InitializingBean;
/*  6:   */ import org.springframework.jmx.support.NotificationListenerHolder;
/*  7:   */ 
/*  8:   */ public class NotificationListenerBean
/*  9:   */   extends NotificationListenerHolder
/* 10:   */   implements InitializingBean
/* 11:   */ {
/* 12:   */   public NotificationListenerBean() {}
/* 13:   */   
/* 14:   */   public NotificationListenerBean(NotificationListener notificationListener)
/* 15:   */   {
/* 16:58 */     setNotificationListener(notificationListener);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public void afterPropertiesSet()
/* 20:   */   {
/* 21:63 */     if (getNotificationListener() == null) {
/* 22:64 */       throw new IllegalArgumentException("Property 'notificationListener' is required");
/* 23:   */     }
/* 24:   */   }
/* 25:   */   
/* 26:   */   void replaceObjectName(Object originalName, Object newName)
/* 27:   */   {
/* 28:69 */     if ((this.mappedObjectNames != null) && (this.mappedObjectNames.contains(originalName)))
/* 29:   */     {
/* 30:70 */       this.mappedObjectNames.remove(originalName);
/* 31:71 */       this.mappedObjectNames.add(newName);
/* 32:   */     }
/* 33:   */   }
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.NotificationListenerBean
 * JD-Core Version:    0.7.0.1
 */