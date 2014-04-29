/*  1:   */ package org.springframework.context.annotation;
/*  2:   */ 
/*  3:   */ import java.util.Set;
/*  4:   */ import org.springframework.beans.factory.config.BeanDefinition;
/*  5:   */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*  6:   */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*  7:   */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*  8:   */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*  9:   */ import org.springframework.beans.factory.xml.ParserContext;
/* 10:   */ import org.w3c.dom.Element;
/* 11:   */ 
/* 12:   */ public class AnnotationConfigBeanDefinitionParser
/* 13:   */   implements BeanDefinitionParser
/* 14:   */ {
/* 15:   */   public BeanDefinition parse(Element element, ParserContext parserContext)
/* 16:   */   {
/* 17:42 */     Object source = parserContext.extractSource(element);
/* 18:   */     
/* 19:   */ 
/* 20:45 */     Set<BeanDefinitionHolder> processorDefinitions = 
/* 21:46 */       AnnotationConfigUtils.registerAnnotationConfigProcessors(parserContext.getRegistry(), source);
/* 22:   */     
/* 23:   */ 
/* 24:49 */     CompositeComponentDefinition compDefinition = new CompositeComponentDefinition(element.getTagName(), source);
/* 25:50 */     parserContext.pushContainingComponent(compDefinition);
/* 26:53 */     for (BeanDefinitionHolder processorDefinition : processorDefinitions) {
/* 27:54 */       parserContext.registerComponent(new BeanComponentDefinition(processorDefinition));
/* 28:   */     }
/* 29:58 */     parserContext.popAndRegisterContainingComponent();
/* 30:   */     
/* 31:60 */     return null;
/* 32:   */   }
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.AnnotationConfigBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */