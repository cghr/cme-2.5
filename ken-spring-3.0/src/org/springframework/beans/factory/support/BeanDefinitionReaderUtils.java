/*   1:    */ package org.springframework.beans.factory.support;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*   4:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*   5:    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*   6:    */ import org.springframework.util.ClassUtils;
/*   7:    */ import org.springframework.util.ObjectUtils;
/*   8:    */ import org.springframework.util.StringUtils;
/*   9:    */ 
/*  10:    */ public class BeanDefinitionReaderUtils
/*  11:    */ {
/*  12:    */   public static final String GENERATED_BEAN_NAME_SEPARATOR = "#";
/*  13:    */   
/*  14:    */   public static AbstractBeanDefinition createBeanDefinition(String parentName, String className, ClassLoader classLoader)
/*  15:    */     throws ClassNotFoundException
/*  16:    */   {
/*  17: 59 */     GenericBeanDefinition bd = new GenericBeanDefinition();
/*  18: 60 */     bd.setParentName(parentName);
/*  19: 61 */     if (className != null) {
/*  20: 62 */       if (classLoader != null) {
/*  21: 63 */         bd.setBeanClass(ClassUtils.forName(className, classLoader));
/*  22:    */       } else {
/*  23: 66 */         bd.setBeanClassName(className);
/*  24:    */       }
/*  25:    */     }
/*  26: 69 */     return bd;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public static String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry, boolean isInnerBean)
/*  30:    */     throws BeanDefinitionStoreException
/*  31:    */   {
/*  32: 89 */     String generatedBeanName = definition.getBeanClassName();
/*  33: 90 */     if (generatedBeanName == null) {
/*  34: 91 */       if (definition.getParentName() != null) {
/*  35: 92 */         generatedBeanName = definition.getParentName() + "$child";
/*  36: 94 */       } else if (definition.getFactoryBeanName() != null) {
/*  37: 95 */         generatedBeanName = definition.getFactoryBeanName() + "$created";
/*  38:    */       }
/*  39:    */     }
/*  40: 98 */     if (!StringUtils.hasText(generatedBeanName)) {
/*  41: 99 */       throw new BeanDefinitionStoreException("Unnamed bean definition specifies neither 'class' nor 'parent' nor 'factory-bean' - can't generate bean name");
/*  42:    */     }
/*  43:103 */     String id = generatedBeanName;
/*  44:104 */     if (isInnerBean)
/*  45:    */     {
/*  46:106 */       id = generatedBeanName + "#" + ObjectUtils.getIdentityHexString(definition);
/*  47:    */     }
/*  48:    */     else
/*  49:    */     {
/*  50:111 */       int counter = -1;
/*  51:112 */       while ((counter == -1) || (registry.containsBeanDefinition(id)))
/*  52:    */       {
/*  53:113 */         counter++;
/*  54:114 */         id = generatedBeanName + "#" + counter;
/*  55:    */       }
/*  56:    */     }
/*  57:117 */     return id;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public static String generateBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry registry)
/*  61:    */     throws BeanDefinitionStoreException
/*  62:    */   {
/*  63:133 */     return generateBeanName(beanDefinition, registry, false);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public static void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
/*  67:    */     throws BeanDefinitionStoreException
/*  68:    */   {
/*  69:147 */     String beanName = definitionHolder.getBeanName();
/*  70:148 */     registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());
/*  71:    */     
/*  72:    */ 
/*  73:151 */     String[] aliases = definitionHolder.getAliases();
/*  74:152 */     if (aliases != null) {
/*  75:153 */       for (String aliase : aliases) {
/*  76:154 */         registry.registerAlias(beanName, aliase);
/*  77:    */       }
/*  78:    */     }
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static String registerWithGeneratedName(AbstractBeanDefinition definition, BeanDefinitionRegistry registry)
/*  82:    */     throws BeanDefinitionStoreException
/*  83:    */   {
/*  84:171 */     String generatedName = generateBeanName(definition, registry, false);
/*  85:172 */     registry.registerBeanDefinition(generatedName, definition);
/*  86:173 */     return generatedName;
/*  87:    */   }
/*  88:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.BeanDefinitionReaderUtils
 * JD-Core Version:    0.7.0.1
 */