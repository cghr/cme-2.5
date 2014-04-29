/*   1:    */ package org.springframework.context.support;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.Properties;
/*   5:    */ import org.springframework.beans.BeansException;
/*   6:    */ import org.springframework.beans.factory.BeanInitializationException;
/*   7:    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*   8:    */ import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;
/*   9:    */ import org.springframework.context.EnvironmentAware;
/*  10:    */ import org.springframework.core.env.ConfigurablePropertyResolver;
/*  11:    */ import org.springframework.core.env.Environment;
/*  12:    */ import org.springframework.core.env.MutablePropertySources;
/*  13:    */ import org.springframework.core.env.PropertiesPropertySource;
/*  14:    */ import org.springframework.core.env.PropertySource;
/*  15:    */ import org.springframework.core.env.PropertySources;
/*  16:    */ import org.springframework.core.env.PropertySourcesPropertyResolver;
/*  17:    */ import org.springframework.util.StringValueResolver;
/*  18:    */ 
/*  19:    */ public class PropertySourcesPlaceholderConfigurer
/*  20:    */   extends PlaceholderConfigurerSupport
/*  21:    */   implements EnvironmentAware
/*  22:    */ {
/*  23:    */   public static final String LOCAL_PROPERTIES_PROPERTY_SOURCE_NAME = "localProperties";
/*  24:    */   public static final String ENVIRONMENT_PROPERTIES_PROPERTY_SOURCE_NAME = "environmentProperties";
/*  25:    */   private MutablePropertySources propertySources;
/*  26:    */   private Environment environment;
/*  27:    */   
/*  28:    */   public void setPropertySources(PropertySources propertySources)
/*  29:    */   {
/*  30: 75 */     this.propertySources = new MutablePropertySources(propertySources);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setEnvironment(Environment environment)
/*  34:    */   {
/*  35: 85 */     this.environment = environment;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*  39:    */     throws BeansException
/*  40:    */   {
/*  41:107 */     if (this.propertySources == null)
/*  42:    */     {
/*  43:108 */       this.propertySources = new MutablePropertySources();
/*  44:109 */       if (this.environment != null) {
/*  45:110 */         this.propertySources.addLast(
/*  46:111 */           new PropertySource("environmentProperties", this.environment)
/*  47:    */           {
/*  48:    */             public String getProperty(String key)
/*  49:    */             {
/*  50:114 */               return ((Environment)this.source).getProperty(key);
/*  51:    */             }
/*  52:    */           });
/*  53:    */       }
/*  54:    */       try
/*  55:    */       {
/*  56:120 */         PropertySource<?> localPropertySource = 
/*  57:121 */           new PropertiesPropertySource("localProperties", mergeProperties());
/*  58:122 */         if (this.localOverride) {
/*  59:123 */           this.propertySources.addFirst(localPropertySource);
/*  60:    */         } else {
/*  61:126 */           this.propertySources.addLast(localPropertySource);
/*  62:    */         }
/*  63:    */       }
/*  64:    */       catch (IOException ex)
/*  65:    */       {
/*  66:130 */         throw new BeanInitializationException("Could not load properties", ex);
/*  67:    */       }
/*  68:    */     }
/*  69:134 */     processProperties(beanFactory, new PropertySourcesPropertyResolver(this.propertySources));
/*  70:    */   }
/*  71:    */   
/*  72:    */   protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, final ConfigurablePropertyResolver propertyResolver)
/*  73:    */     throws BeansException
/*  74:    */   {
/*  75:144 */     propertyResolver.setPlaceholderPrefix(this.placeholderPrefix);
/*  76:145 */     propertyResolver.setPlaceholderSuffix(this.placeholderSuffix);
/*  77:146 */     propertyResolver.setValueSeparator(this.valueSeparator);
/*  78:    */     
/*  79:148 */     StringValueResolver valueResolver = new StringValueResolver()
/*  80:    */     {
/*  81:    */       public String resolveStringValue(String strVal)
/*  82:    */       {
/*  83:150 */         String resolved = PropertySourcesPlaceholderConfigurer.access$0(PropertySourcesPlaceholderConfigurer.this) ? 
/*  84:151 */           propertyResolver.resolvePlaceholders(strVal) : 
/*  85:152 */           propertyResolver.resolveRequiredPlaceholders(strVal);
/*  86:153 */         return resolved.equals(PropertySourcesPlaceholderConfigurer.access$1(PropertySourcesPlaceholderConfigurer.this)) ? null : resolved;
/*  87:    */       }
/*  88:156 */     };
/*  89:157 */     doProcessProperties(beanFactoryToProcess, valueResolver);
/*  90:    */   }
/*  91:    */   
/*  92:    */   @Deprecated
/*  93:    */   protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
/*  94:    */   {
/*  95:168 */     throw new UnsupportedOperationException(
/*  96:169 */       "Call processProperties(ConfigurableListableBeanFactory, ConfigurablePropertyResolver) instead");
/*  97:    */   }
/*  98:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.PropertySourcesPlaceholderConfigurer
 * JD-Core Version:    0.7.0.1
 */