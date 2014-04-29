/*  1:   */ package org.springframework.jmx.export.metadata;
/*  2:   */ 
/*  3:   */ import org.springframework.util.StringUtils;
/*  4:   */ 
/*  5:   */ public class ManagedNotification
/*  6:   */ {
/*  7:   */   private String[] notificationTypes;
/*  8:   */   private String name;
/*  9:   */   private String description;
/* 10:   */   
/* 11:   */   public void setNotificationType(String notificationType)
/* 12:   */   {
/* 13:41 */     this.notificationTypes = StringUtils.commaDelimitedListToStringArray(notificationType);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public void setNotificationTypes(String[] notificationTypes)
/* 17:   */   {
/* 18:48 */     this.notificationTypes = notificationTypes;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public String[] getNotificationTypes()
/* 22:   */   {
/* 23:55 */     return this.notificationTypes;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void setName(String name)
/* 27:   */   {
/* 28:62 */     this.name = name;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public String getName()
/* 32:   */   {
/* 33:69 */     return this.name;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public void setDescription(String description)
/* 37:   */   {
/* 38:76 */     this.description = description;
/* 39:   */   }
/* 40:   */   
/* 41:   */   public String getDescription()
/* 42:   */   {
/* 43:83 */     return this.description;
/* 44:   */   }
/* 45:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.metadata.ManagedNotification
 * JD-Core Version:    0.7.0.1
 */