/*  1:   */ package org.springframework.beans.factory.xml;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.config.BeanDefinition;
/*  4:   */ import org.springframework.beans.factory.parsing.ProblemReporter;
/*  5:   */ import org.springframework.beans.factory.parsing.ReaderContext;
/*  6:   */ import org.springframework.beans.factory.parsing.ReaderEventListener;
/*  7:   */ import org.springframework.beans.factory.parsing.SourceExtractor;
/*  8:   */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  9:   */ import org.springframework.beans.factory.support.BeanNameGenerator;
/* 10:   */ import org.springframework.core.io.Resource;
/* 11:   */ import org.springframework.core.io.ResourceLoader;
/* 12:   */ 
/* 13:   */ public class XmlReaderContext
/* 14:   */   extends ReaderContext
/* 15:   */ {
/* 16:   */   private final XmlBeanDefinitionReader reader;
/* 17:   */   private final NamespaceHandlerResolver namespaceHandlerResolver;
/* 18:   */   
/* 19:   */   public XmlReaderContext(Resource resource, ProblemReporter problemReporter, ReaderEventListener eventListener, SourceExtractor sourceExtractor, XmlBeanDefinitionReader reader, NamespaceHandlerResolver namespaceHandlerResolver)
/* 20:   */   {
/* 21:49 */     super(resource, problemReporter, eventListener, sourceExtractor);
/* 22:50 */     this.reader = reader;
/* 23:51 */     this.namespaceHandlerResolver = namespaceHandlerResolver;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public final XmlBeanDefinitionReader getReader()
/* 27:   */   {
/* 28:56 */     return this.reader;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public final BeanDefinitionRegistry getRegistry()
/* 32:   */   {
/* 33:60 */     return this.reader.getRegistry();
/* 34:   */   }
/* 35:   */   
/* 36:   */   public final ResourceLoader getResourceLoader()
/* 37:   */   {
/* 38:64 */     return this.reader.getResourceLoader();
/* 39:   */   }
/* 40:   */   
/* 41:   */   public final ClassLoader getBeanClassLoader()
/* 42:   */   {
/* 43:68 */     return this.reader.getBeanClassLoader();
/* 44:   */   }
/* 45:   */   
/* 46:   */   public final NamespaceHandlerResolver getNamespaceHandlerResolver()
/* 47:   */   {
/* 48:72 */     return this.namespaceHandlerResolver;
/* 49:   */   }
/* 50:   */   
/* 51:   */   public String generateBeanName(BeanDefinition beanDefinition)
/* 52:   */   {
/* 53:77 */     return this.reader.getBeanNameGenerator().generateBeanName(beanDefinition, getRegistry());
/* 54:   */   }
/* 55:   */   
/* 56:   */   public String registerWithGeneratedName(BeanDefinition beanDefinition)
/* 57:   */   {
/* 58:81 */     String generatedName = generateBeanName(beanDefinition);
/* 59:82 */     getRegistry().registerBeanDefinition(generatedName, beanDefinition);
/* 60:83 */     return generatedName;
/* 61:   */   }
/* 62:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.XmlReaderContext
 * JD-Core Version:    0.7.0.1
 */