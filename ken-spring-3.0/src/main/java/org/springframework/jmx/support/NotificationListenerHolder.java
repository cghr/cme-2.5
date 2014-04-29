/*   1:    */ package org.springframework.jmx.support;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.LinkedHashSet;
/*   6:    */ import java.util.Set;
/*   7:    */ import javax.management.MalformedObjectNameException;
/*   8:    */ import javax.management.NotificationFilter;
/*   9:    */ import javax.management.NotificationListener;
/*  10:    */ import javax.management.ObjectName;
/*  11:    */ import org.springframework.util.ObjectUtils;
/*  12:    */ 
/*  13:    */ public class NotificationListenerHolder
/*  14:    */ {
/*  15:    */   private NotificationListener notificationListener;
/*  16:    */   private NotificationFilter notificationFilter;
/*  17:    */   private Object handback;
/*  18:    */   protected Set<Object> mappedObjectNames;
/*  19:    */   
/*  20:    */   public void setNotificationListener(NotificationListener notificationListener)
/*  21:    */   {
/*  22: 55 */     this.notificationListener = notificationListener;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public NotificationListener getNotificationListener()
/*  26:    */   {
/*  27: 62 */     return this.notificationListener;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setNotificationFilter(NotificationFilter notificationFilter)
/*  31:    */   {
/*  32: 71 */     this.notificationFilter = notificationFilter;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public NotificationFilter getNotificationFilter()
/*  36:    */   {
/*  37: 80 */     return this.notificationFilter;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setHandback(Object handback)
/*  41:    */   {
/*  42: 91 */     this.handback = handback;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public Object getHandback()
/*  46:    */   {
/*  47:102 */     return this.handback;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setMappedObjectName(Object mappedObjectName)
/*  51:    */   {
/*  52:113 */     setMappedObjectNames(mappedObjectName != null ? new Object[] { mappedObjectName } : null);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setMappedObjectNames(Object[] mappedObjectNames)
/*  56:    */   {
/*  57:124 */     this.mappedObjectNames = (mappedObjectNames != null ? 
/*  58:125 */       new LinkedHashSet((Collection)Arrays.asList(mappedObjectNames)) : null);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public ObjectName[] getResolvedObjectNames()
/*  62:    */     throws MalformedObjectNameException
/*  63:    */   {
/*  64:135 */     if (this.mappedObjectNames == null) {
/*  65:136 */       return null;
/*  66:    */     }
/*  67:138 */     ObjectName[] resolved = new ObjectName[this.mappedObjectNames.size()];
/*  68:139 */     int i = 0;
/*  69:140 */     for (Object objectName : this.mappedObjectNames)
/*  70:    */     {
/*  71:141 */       resolved[i] = ObjectNameManager.getInstance(objectName);
/*  72:142 */       i++;
/*  73:    */     }
/*  74:144 */     return resolved;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public boolean equals(Object other)
/*  78:    */   {
/*  79:150 */     if (this == other) {
/*  80:151 */       return true;
/*  81:    */     }
/*  82:153 */     if (!(other instanceof NotificationListenerHolder)) {
/*  83:154 */       return false;
/*  84:    */     }
/*  85:156 */     NotificationListenerHolder otherNlh = (NotificationListenerHolder)other;
/*  86:    */     
/*  87:    */ 
/*  88:    */ 
/*  89:160 */     return (ObjectUtils.nullSafeEquals(this.notificationListener, otherNlh.notificationListener)) && (ObjectUtils.nullSafeEquals(this.notificationFilter, otherNlh.notificationFilter)) && (ObjectUtils.nullSafeEquals(this.handback, otherNlh.handback)) && (ObjectUtils.nullSafeEquals(this.mappedObjectNames, otherNlh.mappedObjectNames));
/*  90:    */   }
/*  91:    */   
/*  92:    */   public int hashCode()
/*  93:    */   {
/*  94:165 */     int hashCode = ObjectUtils.nullSafeHashCode(this.notificationListener);
/*  95:166 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.notificationFilter);
/*  96:167 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.handback);
/*  97:168 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.mappedObjectNames);
/*  98:169 */     return hashCode;
/*  99:    */   }
/* 100:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.support.NotificationListenerHolder
 * JD-Core Version:    0.7.0.1
 */