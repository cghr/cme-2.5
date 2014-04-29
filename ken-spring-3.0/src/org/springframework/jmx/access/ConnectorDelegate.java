/*  1:   */ package org.springframework.jmx.access;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.util.Map;
/*  5:   */ import javax.management.MBeanServerConnection;
/*  6:   */ import javax.management.remote.JMXConnector;
/*  7:   */ import javax.management.remote.JMXConnectorFactory;
/*  8:   */ import javax.management.remote.JMXServiceURL;
/*  9:   */ import org.apache.commons.logging.Log;
/* 10:   */ import org.apache.commons.logging.LogFactory;
/* 11:   */ import org.springframework.jmx.MBeanServerNotFoundException;
/* 12:   */ import org.springframework.jmx.support.JmxUtils;
/* 13:   */ 
/* 14:   */ class ConnectorDelegate
/* 15:   */ {
/* 16:41 */   private static final Log logger = LogFactory.getLog(ConnectorDelegate.class);
/* 17:   */   private JMXConnector connector;
/* 18:   */   
/* 19:   */   public MBeanServerConnection connect(JMXServiceURL serviceUrl, Map<String, ?> environment, String agentId)
/* 20:   */     throws MBeanServerNotFoundException
/* 21:   */   {
/* 22:56 */     if (serviceUrl != null)
/* 23:   */     {
/* 24:57 */       if (logger.isDebugEnabled()) {
/* 25:58 */         logger.debug("Connecting to remote MBeanServer at URL [" + serviceUrl + "]");
/* 26:   */       }
/* 27:   */       try
/* 28:   */       {
/* 29:61 */         this.connector = JMXConnectorFactory.connect(serviceUrl, environment);
/* 30:62 */         return this.connector.getMBeanServerConnection();
/* 31:   */       }
/* 32:   */       catch (IOException ex)
/* 33:   */       {
/* 34:65 */         throw new MBeanServerNotFoundException("Could not connect to remote MBeanServer [" + serviceUrl + "]", ex);
/* 35:   */       }
/* 36:   */     }
/* 37:69 */     logger.debug("Attempting to locate local MBeanServer");
/* 38:70 */     return JmxUtils.locateMBeanServer(agentId);
/* 39:   */   }
/* 40:   */   
/* 41:   */   public void close()
/* 42:   */   {
/* 43:78 */     if (this.connector != null) {
/* 44:   */       try
/* 45:   */       {
/* 46:80 */         this.connector.close();
/* 47:   */       }
/* 48:   */       catch (IOException ex)
/* 49:   */       {
/* 50:83 */         logger.debug("Could not close JMX connector", ex);
/* 51:   */       }
/* 52:   */     }
/* 53:   */   }
/* 54:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.access.ConnectorDelegate
 * JD-Core Version:    0.7.0.1
 */