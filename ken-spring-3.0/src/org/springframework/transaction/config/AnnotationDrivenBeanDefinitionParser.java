/*   1:    */ package org.springframework.transaction.config;
/*   2:    */ 
/*   3:    */ import org.springframework.aop.config.AopNamespaceUtils;
/*   4:    */ import org.springframework.beans.MutablePropertyValues;
/*   5:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*   6:    */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*   7:    */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*   8:    */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*   9:    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  10:    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*  11:    */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*  12:    */ import org.springframework.beans.factory.xml.ParserContext;
/*  13:    */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*  14:    */ import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
/*  15:    */ import org.springframework.transaction.interceptor.BeanFactoryTransactionAttributeSourceAdvisor;
/*  16:    */ import org.springframework.transaction.interceptor.TransactionInterceptor;
/*  17:    */ import org.w3c.dom.Element;
/*  18:    */ 
/*  19:    */ class AnnotationDrivenBeanDefinitionParser
/*  20:    */   implements BeanDefinitionParser
/*  21:    */ {
/*  22:    */   @Deprecated
/*  23:    */   public static final String TRANSACTION_ADVISOR_BEAN_NAME = "org.springframework.transaction.config.internalTransactionAdvisor";
/*  24:    */   @Deprecated
/*  25:    */   public static final String TRANSACTION_ASPECT_BEAN_NAME = "org.springframework.transaction.config.internalTransactionAspect";
/*  26:    */   
/*  27:    */   public BeanDefinition parse(Element element, ParserContext parserContext)
/*  28:    */   {
/*  29: 77 */     String mode = element.getAttribute("mode");
/*  30: 78 */     if ("aspectj".equals(mode)) {
/*  31: 80 */       registerTransactionAspect(element, parserContext);
/*  32:    */     } else {
/*  33: 84 */       AopAutoProxyConfigurer.configureAutoProxyCreator(element, parserContext);
/*  34:    */     }
/*  35: 86 */     return null;
/*  36:    */   }
/*  37:    */   
/*  38:    */   private void registerTransactionAspect(Element element, ParserContext parserContext)
/*  39:    */   {
/*  40: 90 */     String txAspectBeanName = "org.springframework.transaction.config.internalTransactionAspect";
/*  41: 91 */     String txAspectClassName = "org.springframework.transaction.aspectj.AnnotationTransactionAspect";
/*  42: 92 */     if (!parserContext.getRegistry().containsBeanDefinition(txAspectBeanName))
/*  43:    */     {
/*  44: 93 */       RootBeanDefinition def = new RootBeanDefinition();
/*  45: 94 */       def.setBeanClassName(txAspectClassName);
/*  46: 95 */       def.setFactoryMethodName("aspectOf");
/*  47: 96 */       registerTransactionManager(element, def);
/*  48: 97 */       parserContext.registerBeanComponent(new BeanComponentDefinition(def, txAspectBeanName));
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   private static void registerTransactionManager(Element element, BeanDefinition def)
/*  53:    */   {
/*  54:102 */     def.getPropertyValues().add("transactionManagerBeanName", 
/*  55:103 */       TxNamespaceHandler.getTransactionManagerName(element));
/*  56:    */   }
/*  57:    */   
/*  58:    */   private static class AopAutoProxyConfigurer
/*  59:    */   {
/*  60:    */     public static void configureAutoProxyCreator(Element element, ParserContext parserContext)
/*  61:    */     {
/*  62:113 */       AopNamespaceUtils.registerAutoProxyCreatorIfNecessary(parserContext, element);
/*  63:    */       
/*  64:115 */       String txAdvisorBeanName = "org.springframework.transaction.config.internalTransactionAdvisor";
/*  65:116 */       if (!parserContext.getRegistry().containsBeanDefinition(txAdvisorBeanName))
/*  66:    */       {
/*  67:117 */         Object eleSource = parserContext.extractSource(element);
/*  68:    */         
/*  69:    */ 
/*  70:120 */         RootBeanDefinition sourceDef = new RootBeanDefinition(AnnotationTransactionAttributeSource.class);
/*  71:121 */         sourceDef.setSource(eleSource);
/*  72:122 */         sourceDef.setRole(2);
/*  73:123 */         String sourceName = parserContext.getReaderContext().registerWithGeneratedName(sourceDef);
/*  74:    */         
/*  75:    */ 
/*  76:126 */         RootBeanDefinition interceptorDef = new RootBeanDefinition(TransactionInterceptor.class);
/*  77:127 */         interceptorDef.setSource(eleSource);
/*  78:128 */         interceptorDef.setRole(2);
/*  79:129 */         AnnotationDrivenBeanDefinitionParser.registerTransactionManager(element, interceptorDef);
/*  80:130 */         interceptorDef.getPropertyValues().add("transactionAttributeSource", new RuntimeBeanReference(sourceName));
/*  81:131 */         String interceptorName = parserContext.getReaderContext().registerWithGeneratedName(interceptorDef);
/*  82:    */         
/*  83:    */ 
/*  84:134 */         RootBeanDefinition advisorDef = new RootBeanDefinition(BeanFactoryTransactionAttributeSourceAdvisor.class);
/*  85:135 */         advisorDef.setSource(eleSource);
/*  86:136 */         advisorDef.setRole(2);
/*  87:137 */         advisorDef.getPropertyValues().add("transactionAttributeSource", new RuntimeBeanReference(sourceName));
/*  88:138 */         advisorDef.getPropertyValues().add("adviceBeanName", interceptorName);
/*  89:139 */         if (element.hasAttribute("order")) {
/*  90:140 */           advisorDef.getPropertyValues().add("order", element.getAttribute("order"));
/*  91:    */         }
/*  92:142 */         parserContext.getRegistry().registerBeanDefinition(txAdvisorBeanName, advisorDef);
/*  93:    */         
/*  94:144 */         CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(element.getTagName(), eleSource);
/*  95:145 */         compositeDef.addNestedComponent(new BeanComponentDefinition(sourceDef, sourceName));
/*  96:146 */         compositeDef.addNestedComponent(new BeanComponentDefinition(interceptorDef, interceptorName));
/*  97:147 */         compositeDef.addNestedComponent(new BeanComponentDefinition(advisorDef, txAdvisorBeanName));
/*  98:148 */         parserContext.registerComponent(compositeDef);
/*  99:    */       }
/* 100:    */     }
/* 101:    */   }
/* 102:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.config.AnnotationDrivenBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */