/*   1:    */ package org.springframework.scheduling.config;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*   4:    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*   5:    */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*   6:    */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*   7:    */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*   8:    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*   9:    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  10:    */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*  11:    */ import org.springframework.beans.factory.xml.ParserContext;
/*  12:    */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*  13:    */ import org.springframework.util.StringUtils;
/*  14:    */ import org.w3c.dom.Element;
/*  15:    */ 
/*  16:    */ public class AnnotationDrivenBeanDefinitionParser
/*  17:    */   implements BeanDefinitionParser
/*  18:    */ {
/*  19:    */   @Deprecated
/*  20:    */   public static final String ASYNC_ANNOTATION_PROCESSOR_BEAN_NAME = "org.springframework.context.annotation.internalAsyncAnnotationProcessor";
/*  21:    */   @Deprecated
/*  22:    */   public static final String ASYNC_EXECUTION_ASPECT_BEAN_NAME = "org.springframework.scheduling.config.internalAsyncExecutionAspect";
/*  23:    */   @Deprecated
/*  24:    */   public static final String SCHEDULED_ANNOTATION_PROCESSOR_BEAN_NAME = "org.springframework.context.annotation.internalScheduledAnnotationProcessor";
/*  25:    */   
/*  26:    */   public BeanDefinition parse(Element element, ParserContext parserContext)
/*  27:    */   {
/*  28: 73 */     Object source = parserContext.extractSource(element);
/*  29:    */     
/*  30:    */ 
/*  31: 76 */     CompositeComponentDefinition compDefinition = new CompositeComponentDefinition(element.getTagName(), source);
/*  32: 77 */     parserContext.pushContainingComponent(compDefinition);
/*  33:    */     
/*  34:    */ 
/*  35: 80 */     BeanDefinitionRegistry registry = parserContext.getRegistry();
/*  36:    */     
/*  37: 82 */     String mode = element.getAttribute("mode");
/*  38: 83 */     if ("aspectj".equals(mode))
/*  39:    */     {
/*  40: 85 */       registerAsyncExecutionAspect(element, parserContext);
/*  41:    */     }
/*  42: 89 */     else if (registry.containsBeanDefinition("org.springframework.context.annotation.internalAsyncAnnotationProcessor"))
/*  43:    */     {
/*  44: 90 */       parserContext.getReaderContext().error(
/*  45: 91 */         "Only one AsyncAnnotationBeanPostProcessor may exist within the context.", source);
/*  46:    */     }
/*  47:    */     else
/*  48:    */     {
/*  49: 94 */       BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(
/*  50: 95 */         "org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor");
/*  51: 96 */       builder.getRawBeanDefinition().setSource(source);
/*  52: 97 */       String executor = element.getAttribute("executor");
/*  53: 98 */       if (StringUtils.hasText(executor)) {
/*  54: 99 */         builder.addPropertyReference("executor", executor);
/*  55:    */       }
/*  56:101 */       if (Boolean.valueOf(element.getAttribute("proxy-target-class")).booleanValue()) {
/*  57:102 */         builder.addPropertyValue("proxyTargetClass", Boolean.valueOf(true));
/*  58:    */       }
/*  59:104 */       registerPostProcessor(parserContext, builder, "org.springframework.context.annotation.internalAsyncAnnotationProcessor");
/*  60:    */     }
/*  61:108 */     if (registry.containsBeanDefinition("org.springframework.context.annotation.internalScheduledAnnotationProcessor"))
/*  62:    */     {
/*  63:109 */       parserContext.getReaderContext().error(
/*  64:110 */         "Only one ScheduledAnnotationBeanPostProcessor may exist within the context.", source);
/*  65:    */     }
/*  66:    */     else
/*  67:    */     {
/*  68:113 */       BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(
/*  69:114 */         "org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor");
/*  70:115 */       builder.getRawBeanDefinition().setSource(source);
/*  71:116 */       String scheduler = element.getAttribute("scheduler");
/*  72:117 */       if (StringUtils.hasText(scheduler)) {
/*  73:118 */         builder.addPropertyReference("scheduler", scheduler);
/*  74:    */       }
/*  75:120 */       registerPostProcessor(parserContext, builder, "org.springframework.context.annotation.internalScheduledAnnotationProcessor");
/*  76:    */     }
/*  77:124 */     parserContext.popAndRegisterContainingComponent();
/*  78:    */     
/*  79:126 */     return null;
/*  80:    */   }
/*  81:    */   
/*  82:    */   private void registerAsyncExecutionAspect(Element element, ParserContext parserContext)
/*  83:    */   {
/*  84:130 */     if (!parserContext.getRegistry().containsBeanDefinition("org.springframework.scheduling.config.internalAsyncExecutionAspect"))
/*  85:    */     {
/*  86:131 */       BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(
/*  87:132 */         "org.springframework.scheduling.aspectj.AnnotationAsyncExecutionAspect");
/*  88:133 */       builder.setFactoryMethod("aspectOf");
/*  89:134 */       String executor = element.getAttribute("executor");
/*  90:135 */       if (StringUtils.hasText(executor)) {
/*  91:136 */         builder.addPropertyReference("executor", executor);
/*  92:    */       }
/*  93:138 */       parserContext.registerBeanComponent(
/*  94:139 */         new BeanComponentDefinition(builder.getBeanDefinition(), 
/*  95:140 */         "org.springframework.scheduling.config.internalAsyncExecutionAspect"));
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   private static void registerPostProcessor(ParserContext parserContext, BeanDefinitionBuilder builder, String beanName)
/* 100:    */   {
/* 101:147 */     builder.setRole(2);
/* 102:148 */     parserContext.getRegistry().registerBeanDefinition(beanName, builder.getBeanDefinition());
/* 103:149 */     BeanDefinitionHolder holder = new BeanDefinitionHolder(builder.getBeanDefinition(), beanName);
/* 104:150 */     parserContext.registerComponent(new BeanComponentDefinition(holder));
/* 105:    */   }
/* 106:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.config.AnnotationDrivenBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */