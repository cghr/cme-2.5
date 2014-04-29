/*   1:    */ package org.springframework.jmx.support;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.net.MalformedURLException;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.Properties;
/*   8:    */ import javax.management.MBeanServerConnection;
/*   9:    */ import javax.management.remote.JMXConnector;
/*  10:    */ import javax.management.remote.JMXConnectorFactory;
/*  11:    */ import javax.management.remote.JMXServiceURL;
/*  12:    */ import org.springframework.aop.TargetSource;
/*  13:    */ import org.springframework.aop.framework.ProxyFactory;
/*  14:    */ import org.springframework.aop.target.AbstractLazyCreationTargetSource;
/*  15:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  16:    */ import org.springframework.beans.factory.DisposableBean;
/*  17:    */ import org.springframework.beans.factory.FactoryBean;
/*  18:    */ import org.springframework.beans.factory.InitializingBean;
/*  19:    */ import org.springframework.util.ClassUtils;
/*  20:    */ import org.springframework.util.CollectionUtils;
/*  21:    */ 
/*  22:    */ public class MBeanServerConnectionFactoryBean
/*  23:    */   implements FactoryBean<MBeanServerConnection>, BeanClassLoaderAware, InitializingBean, DisposableBean
/*  24:    */ {
/*  25:    */   private JMXServiceURL serviceUrl;
/*  26: 57 */   private Map<String, Object> environment = new HashMap();
/*  27: 59 */   private boolean connectOnStartup = true;
/*  28: 61 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*  29:    */   private JMXConnector connector;
/*  30:    */   private MBeanServerConnection connection;
/*  31:    */   private JMXConnectorLazyInitTargetSource connectorTargetSource;
/*  32:    */   
/*  33:    */   public void setServiceUrl(String url)
/*  34:    */     throws MalformedURLException
/*  35:    */   {
/*  36: 74 */     this.serviceUrl = new JMXServiceURL(url);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setEnvironment(Properties environment)
/*  40:    */   {
/*  41: 82 */     CollectionUtils.mergePropertiesIntoMap(environment, this.environment);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setEnvironmentMap(Map<String, ?> environment)
/*  45:    */   {
/*  46: 90 */     if (environment != null) {
/*  47: 91 */       this.environment.putAll(environment);
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setConnectOnStartup(boolean connectOnStartup)
/*  52:    */   {
/*  53:101 */     this.connectOnStartup = connectOnStartup;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setBeanClassLoader(ClassLoader classLoader)
/*  57:    */   {
/*  58:105 */     this.beanClassLoader = classLoader;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void afterPropertiesSet()
/*  62:    */     throws IOException
/*  63:    */   {
/*  64:114 */     if (this.serviceUrl == null) {
/*  65:115 */       throw new IllegalArgumentException("Property 'serviceUrl' is required");
/*  66:    */     }
/*  67:118 */     if (this.connectOnStartup) {
/*  68:119 */       connect();
/*  69:    */     } else {
/*  70:122 */       createLazyConnection();
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   private void connect()
/*  75:    */     throws IOException
/*  76:    */   {
/*  77:131 */     this.connector = JMXConnectorFactory.connect(this.serviceUrl, this.environment);
/*  78:132 */     this.connection = this.connector.getMBeanServerConnection();
/*  79:    */   }
/*  80:    */   
/*  81:    */   private void createLazyConnection()
/*  82:    */   {
/*  83:139 */     this.connectorTargetSource = new JMXConnectorLazyInitTargetSource(null);
/*  84:140 */     TargetSource connectionTargetSource = new MBeanServerConnectionLazyInitTargetSource(null);
/*  85:    */     
/*  86:142 */     this.connector = ((JMXConnector)
/*  87:143 */       new ProxyFactory(JMXConnector.class, this.connectorTargetSource).getProxy(this.beanClassLoader));
/*  88:144 */     this.connection = ((MBeanServerConnection)
/*  89:145 */       new ProxyFactory(MBeanServerConnection.class, connectionTargetSource).getProxy(this.beanClassLoader));
/*  90:    */   }
/*  91:    */   
/*  92:    */   public MBeanServerConnection getObject()
/*  93:    */   {
/*  94:150 */     return this.connection;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public Class<? extends MBeanServerConnection> getObjectType()
/*  98:    */   {
/*  99:154 */     return this.connection != null ? this.connection.getClass() : MBeanServerConnection.class;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public boolean isSingleton()
/* 103:    */   {
/* 104:158 */     return true;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void destroy()
/* 108:    */     throws IOException
/* 109:    */   {
/* 110:166 */     if ((this.connectorTargetSource == null) || (this.connectorTargetSource.isInitialized())) {
/* 111:167 */       this.connector.close();
/* 112:    */     }
/* 113:    */   }
/* 114:    */   
/* 115:    */   private class JMXConnectorLazyInitTargetSource
/* 116:    */     extends AbstractLazyCreationTargetSource
/* 117:    */   {
/* 118:    */     private JMXConnectorLazyInitTargetSource() {}
/* 119:    */     
/* 120:    */     protected Object createObject()
/* 121:    */       throws Exception
/* 122:    */     {
/* 123:182 */       return JMXConnectorFactory.connect(MBeanServerConnectionFactoryBean.this.serviceUrl, MBeanServerConnectionFactoryBean.this.environment);
/* 124:    */     }
/* 125:    */     
/* 126:    */     public Class<?> getTargetClass()
/* 127:    */     {
/* 128:187 */       return JMXConnector.class;
/* 129:    */     }
/* 130:    */   }
/* 131:    */   
/* 132:    */   private class MBeanServerConnectionLazyInitTargetSource
/* 133:    */     extends AbstractLazyCreationTargetSource
/* 134:    */   {
/* 135:    */     private MBeanServerConnectionLazyInitTargetSource() {}
/* 136:    */     
/* 137:    */     protected Object createObject()
/* 138:    */       throws Exception
/* 139:    */     {
/* 140:199 */       return MBeanServerConnectionFactoryBean.this.connector.getMBeanServerConnection();
/* 141:    */     }
/* 142:    */     
/* 143:    */     public Class<?> getTargetClass()
/* 144:    */     {
/* 145:204 */       return MBeanServerConnection.class;
/* 146:    */     }
/* 147:    */   }
/* 148:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.support.MBeanServerConnectionFactoryBean
 * JD-Core Version:    0.7.0.1
 */