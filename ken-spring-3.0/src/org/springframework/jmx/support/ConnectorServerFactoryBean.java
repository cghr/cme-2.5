/*   1:    */ package org.springframework.jmx.support;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Map;
/*   6:    */ import java.util.Properties;
/*   7:    */ import javax.management.JMException;
/*   8:    */ import javax.management.MalformedObjectNameException;
/*   9:    */ import javax.management.ObjectName;
/*  10:    */ import javax.management.remote.JMXConnectorServer;
/*  11:    */ import javax.management.remote.JMXConnectorServerFactory;
/*  12:    */ import javax.management.remote.JMXServiceURL;
/*  13:    */ import org.apache.commons.logging.Log;
/*  14:    */ import org.springframework.beans.factory.DisposableBean;
/*  15:    */ import org.springframework.beans.factory.FactoryBean;
/*  16:    */ import org.springframework.beans.factory.InitializingBean;
/*  17:    */ import org.springframework.jmx.JmxException;
/*  18:    */ import org.springframework.util.CollectionUtils;
/*  19:    */ 
/*  20:    */ public class ConnectorServerFactoryBean
/*  21:    */   extends MBeanRegistrationSupport
/*  22:    */   implements FactoryBean<JMXConnectorServer>, InitializingBean, DisposableBean
/*  23:    */ {
/*  24:    */   public static final String DEFAULT_SERVICE_URL = "service:jmx:jmxmp://localhost:9875";
/*  25: 62 */   private String serviceUrl = "service:jmx:jmxmp://localhost:9875";
/*  26: 64 */   private Map<String, Object> environment = new HashMap();
/*  27:    */   private ObjectName objectName;
/*  28: 68 */   private boolean threaded = false;
/*  29: 70 */   private boolean daemon = false;
/*  30:    */   private JMXConnectorServer connectorServer;
/*  31:    */   
/*  32:    */   public void setServiceUrl(String serviceUrl)
/*  33:    */   {
/*  34: 79 */     this.serviceUrl = serviceUrl;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setEnvironment(Properties environment)
/*  38:    */   {
/*  39: 87 */     CollectionUtils.mergePropertiesIntoMap(environment, this.environment);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setEnvironmentMap(Map<String, ?> environment)
/*  43:    */   {
/*  44: 95 */     if (environment != null) {
/*  45: 96 */       this.environment.putAll(environment);
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void setObjectName(Object objectName)
/*  50:    */     throws MalformedObjectNameException
/*  51:    */   {
/*  52:107 */     this.objectName = ObjectNameManager.getInstance(objectName);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setThreaded(boolean threaded)
/*  56:    */   {
/*  57:114 */     this.threaded = threaded;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setDaemon(boolean daemon)
/*  61:    */   {
/*  62:122 */     this.daemon = daemon;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void afterPropertiesSet()
/*  66:    */     throws JMException, IOException
/*  67:    */   {
/*  68:136 */     if (this.server == null) {
/*  69:137 */       this.server = JmxUtils.locateMBeanServer();
/*  70:    */     }
/*  71:141 */     JMXServiceURL url = new JMXServiceURL(this.serviceUrl);
/*  72:    */     
/*  73:    */ 
/*  74:144 */     this.connectorServer = JMXConnectorServerFactory.newJMXConnectorServer(url, this.environment, this.server);
/*  75:147 */     if (this.objectName != null) {
/*  76:148 */       doRegister(this.connectorServer, this.objectName);
/*  77:    */     }
/*  78:    */     try
/*  79:    */     {
/*  80:152 */       if (this.threaded)
/*  81:    */       {
/*  82:154 */         Thread connectorThread = new Thread()
/*  83:    */         {
/*  84:    */           public void run()
/*  85:    */           {
/*  86:    */             try
/*  87:    */             {
/*  88:158 */               ConnectorServerFactoryBean.this.connectorServer.start();
/*  89:    */             }
/*  90:    */             catch (IOException ex)
/*  91:    */             {
/*  92:161 */               throw new JmxException("Could not start JMX connector server after delay", ex);
/*  93:    */             }
/*  94:    */           }
/*  95:165 */         };
/*  96:166 */         connectorThread.setName("JMX Connector Thread [" + this.serviceUrl + "]");
/*  97:167 */         connectorThread.setDaemon(this.daemon);
/*  98:168 */         connectorThread.start();
/*  99:    */       }
/* 100:    */       else
/* 101:    */       {
/* 102:172 */         this.connectorServer.start();
/* 103:    */       }
/* 104:175 */       if (this.logger.isInfoEnabled()) {
/* 105:176 */         this.logger.info("JMX connector server started: " + this.connectorServer);
/* 106:    */       }
/* 107:    */     }
/* 108:    */     catch (IOException ex)
/* 109:    */     {
/* 110:182 */       unregisterBeans();
/* 111:183 */       throw ex;
/* 112:    */     }
/* 113:    */   }
/* 114:    */   
/* 115:    */   public JMXConnectorServer getObject()
/* 116:    */   {
/* 117:189 */     return this.connectorServer;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public Class<? extends JMXConnectorServer> getObjectType()
/* 121:    */   {
/* 122:193 */     return this.connectorServer != null ? this.connectorServer.getClass() : JMXConnectorServer.class;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public boolean isSingleton()
/* 126:    */   {
/* 127:197 */     return true;
/* 128:    */   }
/* 129:    */   
/* 130:    */   public void destroy()
/* 131:    */     throws IOException
/* 132:    */   {
/* 133:207 */     if (this.logger.isInfoEnabled()) {
/* 134:208 */       this.logger.info("Stopping JMX connector server: " + this.connectorServer);
/* 135:    */     }
/* 136:    */     try
/* 137:    */     {
/* 138:211 */       this.connectorServer.stop();
/* 139:    */     }
/* 140:    */     finally
/* 141:    */     {
/* 142:214 */       unregisterBeans();
/* 143:    */     }
/* 144:    */   }
/* 145:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.support.ConnectorServerFactoryBean
 * JD-Core Version:    0.7.0.1
 */