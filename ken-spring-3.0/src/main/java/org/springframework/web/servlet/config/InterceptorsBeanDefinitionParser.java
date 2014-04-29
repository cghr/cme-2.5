/*  1:   */ package org.springframework.web.servlet.config;
/*  2:   */ 
/*  3:   */ import java.util.List;
/*  4:   */ import org.springframework.beans.factory.config.BeanDefinition;
/*  5:   */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*  6:   */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*  7:   */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*  8:   */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*  9:   */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/* 10:   */ import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
/* 11:   */ import org.springframework.beans.factory.xml.ParserContext;
/* 12:   */ import org.springframework.beans.factory.xml.XmlReaderContext;
/* 13:   */ import org.springframework.util.xml.DomUtils;
/* 14:   */ import org.springframework.web.servlet.handler.MappedInterceptor;
/* 15:   */ import org.w3c.dom.Element;
/* 16:   */ 
/* 17:   */ class InterceptorsBeanDefinitionParser
/* 18:   */   implements BeanDefinitionParser
/* 19:   */ {
/* 20:   */   public BeanDefinition parse(Element element, ParserContext parserContext)
/* 21:   */   {
/* 22:41 */     CompositeComponentDefinition compDefinition = new CompositeComponentDefinition(element.getTagName(), parserContext.extractSource(element));
/* 23:42 */     parserContext.pushContainingComponent(compDefinition);
/* 24:   */     
/* 25:44 */     List<Element> interceptors = DomUtils.getChildElementsByTagName(element, new String[] { "bean", "ref", "interceptor" });
/* 26:45 */     for (Element interceptor : interceptors)
/* 27:   */     {
/* 28:46 */       RootBeanDefinition mappedInterceptorDef = new RootBeanDefinition(MappedInterceptor.class);
/* 29:47 */       mappedInterceptorDef.setSource(parserContext.extractSource(interceptor));
/* 30:48 */       mappedInterceptorDef.setRole(2);
/* 31:   */       Object interceptorBean;
/* 32:   */       String[] pathPatterns;
/* 33:   */       Object interceptorBean;
/* 34:52 */       if ("interceptor".equals(interceptor.getLocalName()))
/* 35:   */       {
/* 36:53 */         List<Element> paths = DomUtils.getChildElementsByTagName(interceptor, "mapping");
/* 37:54 */         String[] pathPatterns = new String[paths.size()];
/* 38:55 */         for (int i = 0; i < paths.size(); i++) {
/* 39:56 */           pathPatterns[i] = ((Element)paths.get(i)).getAttribute("path");
/* 40:   */         }
/* 41:58 */         Element beanElem = (Element)DomUtils.getChildElementsByTagName(interceptor, new String[] { "bean", "ref" }).get(0);
/* 42:59 */         interceptorBean = parserContext.getDelegate().parsePropertySubElement(beanElem, null);
/* 43:   */       }
/* 44:   */       else
/* 45:   */       {
/* 46:62 */         pathPatterns = (String[])null;
/* 47:63 */         interceptorBean = parserContext.getDelegate().parsePropertySubElement(interceptor, null);
/* 48:   */       }
/* 49:65 */       mappedInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(0, pathPatterns);
/* 50:66 */       mappedInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(1, interceptorBean);
/* 51:   */       
/* 52:68 */       String beanName = parserContext.getReaderContext().registerWithGeneratedName(mappedInterceptorDef);
/* 53:69 */       parserContext.registerComponent(new BeanComponentDefinition(mappedInterceptorDef, beanName));
/* 54:   */     }
/* 55:72 */     parserContext.popAndRegisterContainingComponent();
/* 56:73 */     return null;
/* 57:   */   }
/* 58:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.config.InterceptorsBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */