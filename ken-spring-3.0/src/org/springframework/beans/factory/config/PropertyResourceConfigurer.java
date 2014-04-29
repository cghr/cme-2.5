/*   1:    */ package org.springframework.beans.factory.config;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.Enumeration;
/*   5:    */ import java.util.Properties;
/*   6:    */ import org.springframework.beans.BeansException;
/*   7:    */ import org.springframework.beans.factory.BeanInitializationException;
/*   8:    */ import org.springframework.core.PriorityOrdered;
/*   9:    */ import org.springframework.core.io.support.PropertiesLoaderSupport;
/*  10:    */ import org.springframework.util.ObjectUtils;
/*  11:    */ 
/*  12:    */ public abstract class PropertyResourceConfigurer
/*  13:    */   extends PropertiesLoaderSupport
/*  14:    */   implements BeanFactoryPostProcessor, PriorityOrdered
/*  15:    */ {
/*  16: 55 */   private int order = 2147483647;
/*  17:    */   
/*  18:    */   public void setOrder(int order)
/*  19:    */   {
/*  20: 63 */     this.order = order;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public int getOrder()
/*  24:    */   {
/*  25: 67 */     return this.order;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*  29:    */     throws BeansException
/*  30:    */   {
/*  31:    */     try
/*  32:    */     {
/*  33: 78 */       Properties mergedProps = mergeProperties();
/*  34:    */       
/*  35:    */ 
/*  36: 81 */       convertProperties(mergedProps);
/*  37:    */       
/*  38:    */ 
/*  39: 84 */       processProperties(beanFactory, mergedProps);
/*  40:    */     }
/*  41:    */     catch (IOException ex)
/*  42:    */     {
/*  43: 87 */       throw new BeanInitializationException("Could not load properties", ex);
/*  44:    */     }
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected void convertProperties(Properties props)
/*  48:    */   {
/*  49:100 */     Enumeration<?> propertyNames = props.propertyNames();
/*  50:101 */     while (propertyNames.hasMoreElements())
/*  51:    */     {
/*  52:102 */       String propertyName = (String)propertyNames.nextElement();
/*  53:103 */       String propertyValue = props.getProperty(propertyName);
/*  54:104 */       String convertedValue = convertProperty(propertyName, propertyValue);
/*  55:105 */       if (!ObjectUtils.nullSafeEquals(propertyValue, convertedValue)) {
/*  56:106 */         props.setProperty(propertyName, convertedValue);
/*  57:    */       }
/*  58:    */     }
/*  59:    */   }
/*  60:    */   
/*  61:    */   protected String convertProperty(String propertyName, String propertyValue)
/*  62:    */   {
/*  63:121 */     return convertPropertyValue(propertyValue);
/*  64:    */   }
/*  65:    */   
/*  66:    */   protected String convertPropertyValue(String originalValue)
/*  67:    */   {
/*  68:139 */     return originalValue;
/*  69:    */   }
/*  70:    */   
/*  71:    */   protected abstract void processProperties(ConfigurableListableBeanFactory paramConfigurableListableBeanFactory, Properties paramProperties)
/*  72:    */     throws BeansException;
/*  73:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.PropertyResourceConfigurer
 * JD-Core Version:    0.7.0.1
 */