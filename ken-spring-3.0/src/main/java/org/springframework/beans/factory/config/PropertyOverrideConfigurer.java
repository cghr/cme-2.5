/*   1:    */ package org.springframework.beans.factory.config;
/*   2:    */ 
/*   3:    */ import java.util.Collections;
/*   4:    */ import java.util.Enumeration;
/*   5:    */ import java.util.HashSet;
/*   6:    */ import java.util.Properties;
/*   7:    */ import java.util.Set;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.springframework.beans.BeansException;
/*  10:    */ import org.springframework.beans.MutablePropertyValues;
/*  11:    */ import org.springframework.beans.PropertyValue;
/*  12:    */ import org.springframework.beans.factory.BeanInitializationException;
/*  13:    */ 
/*  14:    */ public class PropertyOverrideConfigurer
/*  15:    */   extends PropertyResourceConfigurer
/*  16:    */ {
/*  17:    */   public static final String DEFAULT_BEAN_NAME_SEPARATOR = ".";
/*  18: 70 */   private String beanNameSeparator = ".";
/*  19: 72 */   private boolean ignoreInvalidKeys = false;
/*  20: 75 */   private Set<String> beanNames = Collections.synchronizedSet(new HashSet());
/*  21:    */   
/*  22:    */   public void setBeanNameSeparator(String beanNameSeparator)
/*  23:    */   {
/*  24: 83 */     this.beanNameSeparator = beanNameSeparator;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void setIgnoreInvalidKeys(boolean ignoreInvalidKeys)
/*  28:    */   {
/*  29: 93 */     this.ignoreInvalidKeys = ignoreInvalidKeys;
/*  30:    */   }
/*  31:    */   
/*  32:    */   protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
/*  33:    */     throws BeansException
/*  34:    */   {
/*  35:101 */     for (Enumeration names = props.propertyNames(); names.hasMoreElements();)
/*  36:    */     {
/*  37:102 */       String key = (String)names.nextElement();
/*  38:    */       try
/*  39:    */       {
/*  40:104 */         processKey(beanFactory, key, props.getProperty(key));
/*  41:    */       }
/*  42:    */       catch (BeansException ex)
/*  43:    */       {
/*  44:107 */         String msg = "Could not process key '" + key + "' in PropertyOverrideConfigurer";
/*  45:108 */         if (!this.ignoreInvalidKeys) {
/*  46:109 */           throw new BeanInitializationException(msg, ex);
/*  47:    */         }
/*  48:111 */         if (this.logger.isDebugEnabled()) {
/*  49:112 */           this.logger.debug(msg, ex);
/*  50:    */         }
/*  51:    */       }
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55:    */   protected void processKey(ConfigurableListableBeanFactory factory, String key, String value)
/*  56:    */     throws BeansException
/*  57:    */   {
/*  58:124 */     int separatorIndex = key.indexOf(this.beanNameSeparator);
/*  59:125 */     if (separatorIndex == -1) {
/*  60:126 */       throw new BeanInitializationException("Invalid key '" + key + 
/*  61:127 */         "': expected 'beanName" + this.beanNameSeparator + "property'");
/*  62:    */     }
/*  63:129 */     String beanName = key.substring(0, separatorIndex);
/*  64:130 */     String beanProperty = key.substring(separatorIndex + 1);
/*  65:131 */     this.beanNames.add(beanName);
/*  66:132 */     applyPropertyValue(factory, beanName, beanProperty, value);
/*  67:133 */     if (this.logger.isDebugEnabled()) {
/*  68:134 */       this.logger.debug("Property '" + key + "' set to value [" + value + "]");
/*  69:    */     }
/*  70:    */   }
/*  71:    */   
/*  72:    */   protected void applyPropertyValue(ConfigurableListableBeanFactory factory, String beanName, String property, String value)
/*  73:    */   {
/*  74:144 */     BeanDefinition bd = factory.getBeanDefinition(beanName);
/*  75:145 */     while (bd.getOriginatingBeanDefinition() != null) {
/*  76:146 */       bd = bd.getOriginatingBeanDefinition();
/*  77:    */     }
/*  78:148 */     PropertyValue pv = new PropertyValue(property, value);
/*  79:149 */     pv.setOptional(this.ignoreInvalidKeys);
/*  80:150 */     bd.getPropertyValues().addPropertyValue(pv);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public boolean hasPropertyOverridesFor(String beanName)
/*  84:    */   {
/*  85:162 */     return this.beanNames.contains(beanName);
/*  86:    */   }
/*  87:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.PropertyOverrideConfigurer
 * JD-Core Version:    0.7.0.1
 */