/*   1:    */ package org.springframework.scheduling.config;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*   4:    */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*   5:    */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*   6:    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*   7:    */ import org.springframework.beans.factory.support.ManagedMap;
/*   8:    */ import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
/*   9:    */ import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
/*  10:    */ import org.springframework.beans.factory.xml.ParserContext;
/*  11:    */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*  12:    */ import org.springframework.util.StringUtils;
/*  13:    */ import org.w3c.dom.Element;
/*  14:    */ import org.w3c.dom.Node;
/*  15:    */ import org.w3c.dom.NodeList;
/*  16:    */ 
/*  17:    */ public class ScheduledTasksBeanDefinitionParser
/*  18:    */   extends AbstractSingleBeanDefinitionParser
/*  19:    */ {
/*  20:    */   private static final String ELEMENT_SCHEDULED = "scheduled";
/*  21:    */   
/*  22:    */   protected boolean shouldGenerateId()
/*  23:    */   {
/*  24: 43 */     return true;
/*  25:    */   }
/*  26:    */   
/*  27:    */   protected String getBeanClassName(Element element)
/*  28:    */   {
/*  29: 48 */     return "org.springframework.scheduling.config.ScheduledTaskRegistrar";
/*  30:    */   }
/*  31:    */   
/*  32:    */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
/*  33:    */   {
/*  34: 53 */     builder.setLazyInit(false);
/*  35: 54 */     ManagedMap<RuntimeBeanReference, String> cronTaskMap = new ManagedMap();
/*  36: 55 */     ManagedMap<RuntimeBeanReference, String> fixedDelayTaskMap = new ManagedMap();
/*  37: 56 */     ManagedMap<RuntimeBeanReference, String> fixedRateTaskMap = new ManagedMap();
/*  38: 57 */     ManagedMap<RuntimeBeanReference, RuntimeBeanReference> triggerTaskMap = new ManagedMap();
/*  39: 58 */     NodeList childNodes = element.getChildNodes();
/*  40: 59 */     for (int i = 0; i < childNodes.getLength(); i++)
/*  41:    */     {
/*  42: 60 */       Node child = childNodes.item(i);
/*  43: 61 */       if (isScheduledElement(child, parserContext))
/*  44:    */       {
/*  45: 64 */         Element taskElement = (Element)child;
/*  46: 65 */         String ref = taskElement.getAttribute("ref");
/*  47: 66 */         String method = taskElement.getAttribute("method");
/*  48: 69 */         if ((!StringUtils.hasText(ref)) || (!StringUtils.hasText(method)))
/*  49:    */         {
/*  50: 70 */           parserContext.getReaderContext().error("Both 'ref' and 'method' are required", taskElement);
/*  51:    */         }
/*  52:    */         else
/*  53:    */         {
/*  54: 75 */           RuntimeBeanReference runnableBeanRef = new RuntimeBeanReference(
/*  55: 76 */             createRunnableBean(ref, method, taskElement, parserContext));
/*  56:    */           
/*  57: 78 */           String cronAttribute = taskElement.getAttribute("cron");
/*  58: 79 */           String fixedDelayAttribute = taskElement.getAttribute("fixed-delay");
/*  59: 80 */           String fixedRateAttribute = taskElement.getAttribute("fixed-rate");
/*  60: 81 */           String triggerAttribute = taskElement.getAttribute("trigger");
/*  61:    */           
/*  62: 83 */           boolean hasCronAttribute = StringUtils.hasText(cronAttribute);
/*  63: 84 */           boolean hasFixedDelayAttribute = StringUtils.hasText(fixedDelayAttribute);
/*  64: 85 */           boolean hasFixedRateAttribute = StringUtils.hasText(fixedRateAttribute);
/*  65: 86 */           boolean hasTriggerAttribute = StringUtils.hasText(triggerAttribute);
/*  66: 88 */           if (!(hasCronAttribute | hasFixedDelayAttribute | hasFixedRateAttribute | hasTriggerAttribute))
/*  67:    */           {
/*  68: 89 */             parserContext.getReaderContext().error(
/*  69: 90 */               "exactly one of the 'cron', 'fixed-delay', 'fixed-rate', or 'trigger' attributes is required", taskElement);
/*  70:    */           }
/*  71:    */           else
/*  72:    */           {
/*  73: 95 */             if (hasCronAttribute) {
/*  74: 96 */               cronTaskMap.put(runnableBeanRef, cronAttribute);
/*  75:    */             }
/*  76: 98 */             if (hasFixedDelayAttribute) {
/*  77: 99 */               fixedDelayTaskMap.put(runnableBeanRef, fixedDelayAttribute);
/*  78:    */             }
/*  79:101 */             if (hasFixedRateAttribute) {
/*  80:102 */               fixedRateTaskMap.put(runnableBeanRef, fixedRateAttribute);
/*  81:    */             }
/*  82:104 */             if (hasTriggerAttribute) {
/*  83:105 */               triggerTaskMap.put(runnableBeanRef, new RuntimeBeanReference(triggerAttribute));
/*  84:    */             }
/*  85:    */           }
/*  86:    */         }
/*  87:    */       }
/*  88:    */     }
/*  89:108 */     String schedulerRef = element.getAttribute("scheduler");
/*  90:109 */     if (StringUtils.hasText(schedulerRef)) {
/*  91:110 */       builder.addPropertyReference("taskScheduler", schedulerRef);
/*  92:    */     }
/*  93:112 */     builder.addPropertyValue("cronTasks", cronTaskMap);
/*  94:113 */     builder.addPropertyValue("fixedDelayTasks", fixedDelayTaskMap);
/*  95:114 */     builder.addPropertyValue("fixedRateTasks", fixedRateTaskMap);
/*  96:115 */     builder.addPropertyValue("triggerTasks", triggerTaskMap);
/*  97:    */   }
/*  98:    */   
/*  99:    */   private boolean isScheduledElement(Node node, ParserContext parserContext)
/* 100:    */   {
/* 101:120 */     return (node.getNodeType() == 1) && ("scheduled".equals(parserContext.getDelegate().getLocalName(node)));
/* 102:    */   }
/* 103:    */   
/* 104:    */   private String createRunnableBean(String ref, String method, Element taskElement, ParserContext parserContext)
/* 105:    */   {
/* 106:124 */     BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(
/* 107:125 */       "org.springframework.scheduling.support.ScheduledMethodRunnable");
/* 108:126 */     builder.addConstructorArgReference(ref);
/* 109:127 */     builder.addConstructorArgValue(method);
/* 110:    */     
/* 111:129 */     builder.getRawBeanDefinition().setSource(parserContext.extractSource(taskElement));
/* 112:130 */     String generatedName = parserContext.getReaderContext().generateBeanName(builder.getRawBeanDefinition());
/* 113:131 */     parserContext.registerBeanComponent(new BeanComponentDefinition(builder.getBeanDefinition(), generatedName));
/* 114:132 */     return generatedName;
/* 115:    */   }
/* 116:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.config.ScheduledTasksBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */