/*   1:    */ package org.springframework.jmx.access;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.net.MalformedURLException;
/*   5:    */ import java.util.Arrays;
/*   6:    */ import java.util.Map;
/*   7:    */ import javax.management.MBeanServerConnection;
/*   8:    */ import javax.management.ObjectName;
/*   9:    */ import javax.management.remote.JMXServiceURL;
/*  10:    */ import org.apache.commons.logging.Log;
/*  11:    */ import org.apache.commons.logging.LogFactory;
/*  12:    */ import org.springframework.beans.factory.DisposableBean;
/*  13:    */ import org.springframework.beans.factory.InitializingBean;
/*  14:    */ import org.springframework.jmx.JmxException;
/*  15:    */ import org.springframework.jmx.MBeanServerNotFoundException;
/*  16:    */ import org.springframework.jmx.support.NotificationListenerHolder;
/*  17:    */ import org.springframework.util.CollectionUtils;
/*  18:    */ 
/*  19:    */ public class NotificationListenerRegistrar
/*  20:    */   extends NotificationListenerHolder
/*  21:    */   implements InitializingBean, DisposableBean
/*  22:    */ {
/*  23: 53 */   protected final Log logger = LogFactory.getLog(getClass());
/*  24:    */   private MBeanServerConnection server;
/*  25:    */   private JMXServiceURL serviceUrl;
/*  26:    */   private Map<String, ?> environment;
/*  27:    */   private String agentId;
/*  28: 63 */   private final ConnectorDelegate connector = new ConnectorDelegate();
/*  29:    */   private ObjectName[] actualObjectNames;
/*  30:    */   
/*  31:    */   public void setServer(MBeanServerConnection server)
/*  32:    */   {
/*  33: 73 */     this.server = server;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setEnvironment(Map<String, ?> environment)
/*  37:    */   {
/*  38: 81 */     this.environment = environment;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public Map<String, ?> getEnvironment()
/*  42:    */   {
/*  43: 92 */     return this.environment;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setServiceUrl(String url)
/*  47:    */     throws MalformedURLException
/*  48:    */   {
/*  49: 99 */     this.serviceUrl = new JMXServiceURL(url);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setAgentId(String agentId)
/*  53:    */   {
/*  54:111 */     this.agentId = agentId;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void afterPropertiesSet()
/*  58:    */   {
/*  59:116 */     if (getNotificationListener() == null) {
/*  60:117 */       throw new IllegalArgumentException("Property 'notificationListener' is required");
/*  61:    */     }
/*  62:119 */     if (CollectionUtils.isEmpty(this.mappedObjectNames)) {
/*  63:120 */       throw new IllegalArgumentException("Property 'mappedObjectName' is required");
/*  64:    */     }
/*  65:122 */     prepare();
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void prepare()
/*  69:    */   {
/*  70:131 */     if (this.server == null) {
/*  71:132 */       this.server = this.connector.connect(this.serviceUrl, this.environment, this.agentId);
/*  72:    */     }
/*  73:    */     try
/*  74:    */     {
/*  75:135 */       this.actualObjectNames = getResolvedObjectNames();
/*  76:136 */       if (this.logger.isDebugEnabled()) {
/*  77:137 */         this.logger.debug("Registering NotificationListener for MBeans " + Arrays.asList(this.actualObjectNames));
/*  78:    */       }
/*  79:139 */       for (ObjectName actualObjectName : this.actualObjectNames) {
/*  80:140 */         this.server.addNotificationListener(
/*  81:141 */           actualObjectName, getNotificationListener(), getNotificationFilter(), getHandback());
/*  82:    */       }
/*  83:    */     }
/*  84:    */     catch (IOException ex)
/*  85:    */     {
/*  86:145 */       throw new MBeanServerNotFoundException(
/*  87:146 */         "Could not connect to remote MBeanServer at URL [" + this.serviceUrl + "]", ex);
/*  88:    */     }
/*  89:    */     catch (Exception ex)
/*  90:    */     {
/*  91:149 */       throw new JmxException("Unable to register NotificationListener", ex);
/*  92:    */     }
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void destroy()
/*  96:    */   {
/*  97:    */     try
/*  98:    */     {
/*  99:158 */       if (this.actualObjectNames != null) {
/* 100:159 */         for (ObjectName actualObjectName : this.actualObjectNames) {
/* 101:    */           try
/* 102:    */           {
/* 103:161 */             this.server.removeNotificationListener(
/* 104:162 */               actualObjectName, getNotificationListener(), getNotificationFilter(), getHandback());
/* 105:    */           }
/* 106:    */           catch (Exception ex)
/* 107:    */           {
/* 108:165 */             if (this.logger.isDebugEnabled()) {
/* 109:166 */               this.logger.debug("Unable to unregister NotificationListener", ex);
/* 110:    */             }
/* 111:    */           }
/* 112:    */         }
/* 113:    */       }
/* 114:    */     }
/* 115:    */     finally
/* 116:    */     {
/* 117:173 */       this.connector.close();
/* 118:    */     }
/* 119:    */   }
/* 120:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.access.NotificationListenerRegistrar
 * JD-Core Version:    0.7.0.1
 */