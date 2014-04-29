/*  1:   */ package org.springframework.jmx.export.metadata;
/*  2:   */ 
/*  3:   */ import javax.management.modelmbean.ModelMBeanNotificationInfo;
/*  4:   */ import org.springframework.util.StringUtils;
/*  5:   */ 
/*  6:   */ public abstract class JmxMetadataUtils
/*  7:   */ {
/*  8:   */   public static ModelMBeanNotificationInfo convertToModelMBeanNotificationInfo(ManagedNotification notificationInfo)
/*  9:   */   {
/* 10:37 */     String name = notificationInfo.getName();
/* 11:38 */     if (!StringUtils.hasText(name)) {
/* 12:39 */       throw new IllegalArgumentException("Must specify notification name");
/* 13:   */     }
/* 14:42 */     String[] notifTypes = notificationInfo.getNotificationTypes();
/* 15:43 */     if ((notifTypes == null) || (notifTypes.length == 0)) {
/* 16:44 */       throw new IllegalArgumentException("Must specify at least one notification type");
/* 17:   */     }
/* 18:47 */     String description = notificationInfo.getDescription();
/* 19:48 */     return new ModelMBeanNotificationInfo(notifTypes, name, description);
/* 20:   */   }
/* 21:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.metadata.JmxMetadataUtils
 * JD-Core Version:    0.7.0.1
 */