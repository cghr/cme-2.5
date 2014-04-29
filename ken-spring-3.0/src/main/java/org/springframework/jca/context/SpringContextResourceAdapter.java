/*   1:    */ package org.springframework.jca.context;
/*   2:    */ 
/*   3:    */ import javax.resource.NotSupportedException;
/*   4:    */ import javax.resource.ResourceException;
/*   5:    */ import javax.resource.spi.ActivationSpec;
/*   6:    */ import javax.resource.spi.BootstrapContext;
/*   7:    */ import javax.resource.spi.ResourceAdapter;
/*   8:    */ import javax.resource.spi.ResourceAdapterInternalException;
/*   9:    */ import javax.resource.spi.endpoint.MessageEndpointFactory;
/*  10:    */ import javax.transaction.xa.XAResource;
/*  11:    */ import org.apache.commons.logging.Log;
/*  12:    */ import org.apache.commons.logging.LogFactory;
/*  13:    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  14:    */ import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
/*  15:    */ import org.springframework.context.ConfigurableApplicationContext;
/*  16:    */ import org.springframework.core.env.ConfigurableEnvironment;
/*  17:    */ import org.springframework.core.env.StandardEnvironment;
/*  18:    */ import org.springframework.util.StringUtils;
/*  19:    */ 
/*  20:    */ public class SpringContextResourceAdapter
/*  21:    */   implements ResourceAdapter
/*  22:    */ {
/*  23:    */   public static final String CONFIG_LOCATION_DELIMITERS = ",; \t\n";
/*  24:    */   public static final String DEFAULT_CONTEXT_CONFIG_LOCATION = "META-INF/applicationContext.xml";
/*  25:119 */   protected final Log logger = LogFactory.getLog(getClass());
/*  26:121 */   private String contextConfigLocation = "META-INF/applicationContext.xml";
/*  27:    */   private ConfigurableApplicationContext applicationContext;
/*  28:    */   
/*  29:    */   public void setContextConfigLocation(String contextConfigLocation)
/*  30:    */   {
/*  31:136 */     this.contextConfigLocation = contextConfigLocation;
/*  32:    */   }
/*  33:    */   
/*  34:    */   protected String getContextConfigLocation()
/*  35:    */   {
/*  36:143 */     return this.contextConfigLocation;
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected ConfigurableEnvironment createEnvironment()
/*  40:    */   {
/*  41:152 */     return new StandardEnvironment();
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void start(BootstrapContext bootstrapContext)
/*  45:    */     throws ResourceAdapterInternalException
/*  46:    */   {
/*  47:160 */     if (this.logger.isInfoEnabled()) {
/*  48:161 */       this.logger.info("Starting SpringContextResourceAdapter with BootstrapContext: " + bootstrapContext);
/*  49:    */     }
/*  50:163 */     this.applicationContext = createApplicationContext(bootstrapContext);
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected ConfigurableApplicationContext createApplicationContext(BootstrapContext bootstrapContext)
/*  54:    */   {
/*  55:175 */     ResourceAdapterApplicationContext applicationContext = 
/*  56:176 */       new ResourceAdapterApplicationContext(bootstrapContext);
/*  57:    */     
/*  58:178 */     applicationContext.setClassLoader(getClass().getClassLoader());
/*  59:    */     
/*  60:180 */     String[] configLocations = 
/*  61:181 */       StringUtils.tokenizeToStringArray(getContextConfigLocation(), ",; \t\n");
/*  62:182 */     if (configLocations != null) {
/*  63:183 */       loadBeanDefinitions(applicationContext, configLocations);
/*  64:    */     }
/*  65:185 */     applicationContext.refresh();
/*  66:186 */     return applicationContext;
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected void loadBeanDefinitions(BeanDefinitionRegistry registry, String[] configLocations)
/*  70:    */   {
/*  71:197 */     new XmlBeanDefinitionReader(registry).loadBeanDefinitions(configLocations);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void stop()
/*  75:    */   {
/*  76:204 */     this.logger.info("Stopping SpringContextResourceAdapter");
/*  77:205 */     this.applicationContext.close();
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void endpointActivation(MessageEndpointFactory messageEndpointFactory, ActivationSpec activationSpec)
/*  81:    */     throws ResourceException
/*  82:    */   {
/*  83:215 */     throw new NotSupportedException("SpringContextResourceAdapter does not support message endpoints");
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void endpointDeactivation(MessageEndpointFactory messageEndpointFactory, ActivationSpec activationSpec) {}
/*  87:    */   
/*  88:    */   public XAResource[] getXAResources(ActivationSpec[] activationSpecs)
/*  89:    */     throws ResourceException
/*  90:    */   {
/*  91:228 */     return null;
/*  92:    */   }
/*  93:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.context.SpringContextResourceAdapter
 * JD-Core Version:    0.7.0.1
 */