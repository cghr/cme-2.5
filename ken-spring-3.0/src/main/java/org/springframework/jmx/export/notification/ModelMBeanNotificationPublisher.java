/*   1:    */ package org.springframework.jmx.export.notification;
/*   2:    */ 
/*   3:    */ import javax.management.AttributeChangeNotification;
/*   4:    */ import javax.management.MBeanException;
/*   5:    */ import javax.management.Notification;
/*   6:    */ import javax.management.ObjectName;
/*   7:    */ import javax.management.modelmbean.ModelMBeanNotificationBroadcaster;
/*   8:    */ import org.springframework.util.Assert;
/*   9:    */ 
/*  10:    */ public class ModelMBeanNotificationPublisher
/*  11:    */   implements NotificationPublisher
/*  12:    */ {
/*  13:    */   private final ModelMBeanNotificationBroadcaster modelMBean;
/*  14:    */   private final ObjectName objectName;
/*  15:    */   private final Object managedResource;
/*  16:    */   
/*  17:    */   public ModelMBeanNotificationPublisher(ModelMBeanNotificationBroadcaster modelMBean, ObjectName objectName, Object managedResource)
/*  18:    */   {
/*  19: 72 */     Assert.notNull(modelMBean, "'modelMBean' must not be null");
/*  20: 73 */     Assert.notNull(objectName, "'objectName' must not be null");
/*  21: 74 */     Assert.notNull(managedResource, "'managedResource' must not be null");
/*  22: 75 */     this.modelMBean = modelMBean;
/*  23: 76 */     this.objectName = objectName;
/*  24: 77 */     this.managedResource = managedResource;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void sendNotification(Notification notification)
/*  28:    */   {
/*  29: 89 */     Assert.notNull(notification, "Notification must not be null");
/*  30: 90 */     replaceNotificationSourceIfNecessary(notification);
/*  31:    */     try
/*  32:    */     {
/*  33: 92 */       if ((notification instanceof AttributeChangeNotification)) {
/*  34: 93 */         this.modelMBean.sendAttributeChangeNotification((AttributeChangeNotification)notification);
/*  35:    */       } else {
/*  36: 96 */         this.modelMBean.sendNotification(notification);
/*  37:    */       }
/*  38:    */     }
/*  39:    */     catch (MBeanException ex)
/*  40:    */     {
/*  41:100 */       throw new UnableToSendNotificationException("Unable to send notification [" + notification + "]", ex);
/*  42:    */     }
/*  43:    */   }
/*  44:    */   
/*  45:    */   private void replaceNotificationSourceIfNecessary(Notification notification)
/*  46:    */   {
/*  47:112 */     if ((notification.getSource() == null) || (notification.getSource().equals(this.managedResource))) {
/*  48:113 */       notification.setSource(this.objectName);
/*  49:    */     }
/*  50:    */   }
/*  51:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.notification.ModelMBeanNotificationPublisher
 * JD-Core Version:    0.7.0.1
 */