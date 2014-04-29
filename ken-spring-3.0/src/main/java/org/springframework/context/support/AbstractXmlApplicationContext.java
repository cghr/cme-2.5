/*   1:    */ package org.springframework.context.support;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import org.springframework.beans.BeansException;
/*   5:    */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*   6:    */ import org.springframework.beans.factory.xml.ResourceEntityResolver;
/*   7:    */ import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
/*   8:    */ import org.springframework.context.ApplicationContext;
/*   9:    */ import org.springframework.core.io.Resource;
/*  10:    */ 
/*  11:    */ public abstract class AbstractXmlApplicationContext
/*  12:    */   extends AbstractRefreshableConfigApplicationContext
/*  13:    */ {
/*  14: 47 */   private boolean validating = true;
/*  15:    */   
/*  16:    */   public AbstractXmlApplicationContext() {}
/*  17:    */   
/*  18:    */   public AbstractXmlApplicationContext(ApplicationContext parent)
/*  19:    */   {
/*  20: 61 */     super(parent);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public void setValidating(boolean validating)
/*  24:    */   {
/*  25: 69 */     this.validating = validating;
/*  26:    */   }
/*  27:    */   
/*  28:    */   protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory)
/*  29:    */     throws BeansException, IOException
/*  30:    */   {
/*  31: 82 */     XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
/*  32:    */     
/*  33:    */ 
/*  34:    */ 
/*  35: 86 */     beanDefinitionReader.setEnvironment(getEnvironment());
/*  36: 87 */     beanDefinitionReader.setResourceLoader(this);
/*  37: 88 */     beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));
/*  38:    */     
/*  39:    */ 
/*  40:    */ 
/*  41: 92 */     initBeanDefinitionReader(beanDefinitionReader);
/*  42: 93 */     loadBeanDefinitions(beanDefinitionReader);
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected void initBeanDefinitionReader(XmlBeanDefinitionReader reader)
/*  46:    */   {
/*  47:105 */     reader.setValidating(this.validating);
/*  48:    */   }
/*  49:    */   
/*  50:    */   protected void loadBeanDefinitions(XmlBeanDefinitionReader reader)
/*  51:    */     throws BeansException, IOException
/*  52:    */   {
/*  53:121 */     Resource[] configResources = getConfigResources();
/*  54:122 */     if (configResources != null) {
/*  55:123 */       reader.loadBeanDefinitions(configResources);
/*  56:    */     }
/*  57:125 */     String[] configLocations = getConfigLocations();
/*  58:126 */     if (configLocations != null) {
/*  59:127 */       reader.loadBeanDefinitions(configLocations);
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   protected Resource[] getConfigResources()
/*  64:    */   {
/*  65:140 */     return null;
/*  66:    */   }
/*  67:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.AbstractXmlApplicationContext
 * JD-Core Version:    0.7.0.1
 */