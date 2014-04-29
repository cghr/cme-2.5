/*  1:   */ package org.springframework.jmx.export.assembler;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.Collection;
/*  5:   */ import java.util.HashMap;
/*  6:   */ import java.util.List;
/*  7:   */ import java.util.Map;
/*  8:   */ import java.util.Map.Entry;
/*  9:   */ import javax.management.modelmbean.ModelMBeanNotificationInfo;
/* 10:   */ import org.springframework.jmx.export.metadata.JmxMetadataUtils;
/* 11:   */ import org.springframework.jmx.export.metadata.ManagedNotification;
/* 12:   */ import org.springframework.util.StringUtils;
/* 13:   */ 
/* 14:   */ public abstract class AbstractConfigurableMBeanInfoAssembler
/* 15:   */   extends AbstractReflectiveMBeanInfoAssembler
/* 16:   */ {
/* 17:   */   private ModelMBeanNotificationInfo[] notificationInfos;
/* 18:43 */   private final Map<String, ModelMBeanNotificationInfo[]> notificationInfoMappings = new HashMap();
/* 19:   */   
/* 20:   */   public void setNotificationInfos(ManagedNotification[] notificationInfos)
/* 21:   */   {
/* 22:47 */     ModelMBeanNotificationInfo[] infos = new ModelMBeanNotificationInfo[notificationInfos.length];
/* 23:48 */     for (int i = 0; i < notificationInfos.length; i++)
/* 24:   */     {
/* 25:49 */       ManagedNotification notificationInfo = notificationInfos[i];
/* 26:50 */       infos[i] = JmxMetadataUtils.convertToModelMBeanNotificationInfo(notificationInfo);
/* 27:   */     }
/* 28:52 */     this.notificationInfos = infos;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void setNotificationInfoMappings(Map<String, Object> notificationInfoMappings)
/* 32:   */   {
/* 33:56 */     for (Map.Entry<String, Object> entry : notificationInfoMappings.entrySet()) {
/* 34:57 */       this.notificationInfoMappings.put((String)entry.getKey(), extractNotificationMetadata(entry.getValue()));
/* 35:   */     }
/* 36:   */   }
/* 37:   */   
/* 38:   */   protected ModelMBeanNotificationInfo[] getNotificationInfo(Object managedBean, String beanKey)
/* 39:   */   {
/* 40:64 */     ModelMBeanNotificationInfo[] result = (ModelMBeanNotificationInfo[])null;
/* 41:65 */     if (StringUtils.hasText(beanKey)) {
/* 42:66 */       result = (ModelMBeanNotificationInfo[])this.notificationInfoMappings.get(beanKey);
/* 43:   */     }
/* 44:68 */     if (result == null) {
/* 45:69 */       result = this.notificationInfos;
/* 46:   */     }
/* 47:71 */     return result != null ? result : new ModelMBeanNotificationInfo[0];
/* 48:   */   }
/* 49:   */   
/* 50:   */   private ModelMBeanNotificationInfo[] extractNotificationMetadata(Object mapValue)
/* 51:   */   {
/* 52:75 */     if ((mapValue instanceof ManagedNotification))
/* 53:   */     {
/* 54:76 */       ManagedNotification mn = (ManagedNotification)mapValue;
/* 55:77 */       return new ModelMBeanNotificationInfo[] { JmxMetadataUtils.convertToModelMBeanNotificationInfo(mn) };
/* 56:   */     }
/* 57:79 */     if ((mapValue instanceof Collection))
/* 58:   */     {
/* 59:80 */       Collection col = (Collection)mapValue;
/* 60:81 */       List<ModelMBeanNotificationInfo> result = new ArrayList();
/* 61:82 */       for (Object colValue : col)
/* 62:   */       {
/* 63:83 */         if (!(colValue instanceof ManagedNotification)) {
/* 64:84 */           throw new IllegalArgumentException(
/* 65:85 */             "Property 'notificationInfoMappings' only accepts ManagedNotifications for Map values");
/* 66:   */         }
/* 67:87 */         ManagedNotification mn = (ManagedNotification)colValue;
/* 68:88 */         result.add(JmxMetadataUtils.convertToModelMBeanNotificationInfo(mn));
/* 69:   */       }
/* 70:90 */       return (ModelMBeanNotificationInfo[])result.toArray(new ModelMBeanNotificationInfo[result.size()]);
/* 71:   */     }
/* 72:93 */     throw new IllegalArgumentException(
/* 73:94 */       "Property 'notificationInfoMappings' only accepts ManagedNotifications for Map values");
/* 74:   */   }
/* 75:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.assembler.AbstractConfigurableMBeanInfoAssembler
 * JD-Core Version:    0.7.0.1
 */