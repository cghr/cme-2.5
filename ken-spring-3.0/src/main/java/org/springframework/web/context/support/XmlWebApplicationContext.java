/*   1:    */ package org.springframework.web.context.support;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import org.springframework.beans.BeansException;
/*   5:    */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*   6:    */ import org.springframework.beans.factory.xml.ResourceEntityResolver;
/*   7:    */ import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
/*   8:    */ 
/*   9:    */ public class XmlWebApplicationContext
/*  10:    */   extends AbstractRefreshableWebApplicationContext
/*  11:    */ {
/*  12:    */   public static final String DEFAULT_CONFIG_LOCATION = "/WEB-INF/applicationContext.xml";
/*  13:    */   public static final String DEFAULT_CONFIG_LOCATION_PREFIX = "/WEB-INF/";
/*  14:    */   public static final String DEFAULT_CONFIG_LOCATION_SUFFIX = ".xml";
/*  15:    */   
/*  16:    */   protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory)
/*  17:    */     throws BeansException, IOException
/*  18:    */   {
/*  19: 83 */     XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
/*  20:    */     
/*  21:    */ 
/*  22:    */ 
/*  23: 87 */     beanDefinitionReader.setEnvironment(getEnvironment());
/*  24: 88 */     beanDefinitionReader.setResourceLoader(this);
/*  25: 89 */     beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));
/*  26:    */     
/*  27:    */ 
/*  28:    */ 
/*  29: 93 */     initBeanDefinitionReader(beanDefinitionReader);
/*  30: 94 */     loadBeanDefinitions(beanDefinitionReader);
/*  31:    */   }
/*  32:    */   
/*  33:    */   protected void initBeanDefinitionReader(XmlBeanDefinitionReader beanDefinitionReader) {}
/*  34:    */   
/*  35:    */   protected void loadBeanDefinitions(XmlBeanDefinitionReader reader)
/*  36:    */     throws IOException
/*  37:    */   {
/*  38:122 */     String[] configLocations = getConfigLocations();
/*  39:123 */     if (configLocations != null) {
/*  40:124 */       for (String configLocation : configLocations) {
/*  41:125 */         reader.loadBeanDefinitions(configLocation);
/*  42:    */       }
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected String[] getDefaultConfigLocations()
/*  47:    */   {
/*  48:137 */     if (getNamespace() != null) {
/*  49:138 */       return new String[] { "/WEB-INF/" + getNamespace() + ".xml" };
/*  50:    */     }
/*  51:141 */     return new String[] { "/WEB-INF/applicationContext.xml" };
/*  52:    */   }
/*  53:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.support.XmlWebApplicationContext
 * JD-Core Version:    0.7.0.1
 */