/*   1:    */ package org.springframework.beans.factory.config;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*   4:    */ import org.springframework.beans.factory.BeanFactory;
/*   5:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*   6:    */ import org.springframework.beans.factory.BeanNameAware;
/*   7:    */ import org.springframework.util.StringValueResolver;
/*   8:    */ 
/*   9:    */ public abstract class PlaceholderConfigurerSupport
/*  10:    */   extends PropertyResourceConfigurer
/*  11:    */   implements BeanNameAware, BeanFactoryAware
/*  12:    */ {
/*  13:    */   public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";
/*  14:    */   public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";
/*  15:    */   public static final String DEFAULT_VALUE_SEPARATOR = ":";
/*  16:102 */   protected String placeholderPrefix = "${";
/*  17:105 */   protected String placeholderSuffix = "}";
/*  18:108 */   protected String valueSeparator = ":";
/*  19:110 */   protected boolean ignoreUnresolvablePlaceholders = false;
/*  20:    */   protected String nullValue;
/*  21:    */   private BeanFactory beanFactory;
/*  22:    */   private String beanName;
/*  23:    */   
/*  24:    */   public void setPlaceholderPrefix(String placeholderPrefix)
/*  25:    */   {
/*  26:124 */     this.placeholderPrefix = placeholderPrefix;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setPlaceholderSuffix(String placeholderSuffix)
/*  30:    */   {
/*  31:132 */     this.placeholderSuffix = placeholderSuffix;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setValueSeparator(String valueSeparator)
/*  35:    */   {
/*  36:142 */     this.valueSeparator = valueSeparator;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setNullValue(String nullValue)
/*  40:    */   {
/*  41:155 */     this.nullValue = nullValue;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setIgnoreUnresolvablePlaceholders(boolean ignoreUnresolvablePlaceholders)
/*  45:    */   {
/*  46:166 */     this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void setBeanName(String beanName)
/*  50:    */   {
/*  51:178 */     this.beanName = beanName;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setBeanFactory(BeanFactory beanFactory)
/*  55:    */   {
/*  56:190 */     this.beanFactory = beanFactory;
/*  57:    */   }
/*  58:    */   
/*  59:    */   protected void doProcessProperties(ConfigurableListableBeanFactory beanFactoryToProcess, StringValueResolver valueResolver)
/*  60:    */   {
/*  61:197 */     BeanDefinitionVisitor visitor = new BeanDefinitionVisitor(valueResolver);
/*  62:    */     
/*  63:199 */     String[] beanNames = beanFactoryToProcess.getBeanDefinitionNames();
/*  64:200 */     for (String curName : beanNames) {
/*  65:203 */       if ((!curName.equals(this.beanName)) || (!beanFactoryToProcess.equals(this.beanFactory)))
/*  66:    */       {
/*  67:204 */         BeanDefinition bd = beanFactoryToProcess.getBeanDefinition(curName);
/*  68:    */         try
/*  69:    */         {
/*  70:206 */           visitor.visitBeanDefinition(bd);
/*  71:    */         }
/*  72:    */         catch (Exception ex)
/*  73:    */         {
/*  74:209 */           throw new BeanDefinitionStoreException(bd.getResourceDescription(), curName, ex.getMessage());
/*  75:    */         }
/*  76:    */       }
/*  77:    */     }
/*  78:215 */     beanFactoryToProcess.resolveAliases(valueResolver);
/*  79:    */     
/*  80:    */ 
/*  81:218 */     beanFactoryToProcess.addEmbeddedValueResolver(valueResolver);
/*  82:    */   }
/*  83:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.PlaceholderConfigurerSupport
 * JD-Core Version:    0.7.0.1
 */