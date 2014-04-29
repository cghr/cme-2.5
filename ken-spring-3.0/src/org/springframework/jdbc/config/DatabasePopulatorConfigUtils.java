/*  1:   */ package org.springframework.jdbc.config;
/*  2:   */ 
/*  3:   */ import java.util.List;
/*  4:   */ import org.springframework.beans.BeanMetadataElement;
/*  5:   */ import org.springframework.beans.factory.config.BeanDefinition;
/*  6:   */ import org.springframework.beans.factory.config.TypedStringValue;
/*  7:   */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*  8:   */ import org.springframework.beans.factory.support.ManagedList;
/*  9:   */ import org.springframework.jdbc.datasource.init.CompositeDatabasePopulator;
/* 10:   */ import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
/* 11:   */ import org.springframework.util.StringUtils;
/* 12:   */ import org.springframework.util.xml.DomUtils;
/* 13:   */ import org.w3c.dom.Element;
/* 14:   */ 
/* 15:   */ class DatabasePopulatorConfigUtils
/* 16:   */ {
/* 17:   */   public static void setDatabasePopulator(Element element, BeanDefinitionBuilder builder)
/* 18:   */   {
/* 19:39 */     List<Element> scripts = DomUtils.getChildElementsByTagName(element, "script");
/* 20:40 */     if (scripts.size() > 0)
/* 21:   */     {
/* 22:41 */       builder.addPropertyValue("databasePopulator", createDatabasePopulator(element, scripts, "INIT"));
/* 23:42 */       builder.addPropertyValue("databaseCleaner", createDatabasePopulator(element, scripts, "DESTROY"));
/* 24:   */     }
/* 25:   */   }
/* 26:   */   
/* 27:   */   private static BeanDefinition createDatabasePopulator(Element element, List<Element> scripts, String execution)
/* 28:   */   {
/* 29:47 */     BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(CompositeDatabasePopulator.class);
/* 30:   */     
/* 31:49 */     boolean ignoreFailedDrops = element.getAttribute("ignore-failures").equals("DROPS");
/* 32:50 */     boolean continueOnError = element.getAttribute("ignore-failures").equals("ALL");
/* 33:   */     
/* 34:52 */     ManagedList<BeanMetadataElement> delegates = new ManagedList();
/* 35:53 */     for (Element scriptElement : scripts)
/* 36:   */     {
/* 37:54 */       String executionAttr = scriptElement.getAttribute("execution");
/* 38:55 */       if (!StringUtils.hasText(executionAttr)) {
/* 39:56 */         executionAttr = "INIT";
/* 40:   */       }
/* 41:58 */       if (execution.equals(executionAttr))
/* 42:   */       {
/* 43:61 */         BeanDefinitionBuilder delegate = BeanDefinitionBuilder.genericBeanDefinition(ResourceDatabasePopulator.class);
/* 44:62 */         delegate.addPropertyValue("ignoreFailedDrops", Boolean.valueOf(ignoreFailedDrops));
/* 45:63 */         delegate.addPropertyValue("continueOnError", Boolean.valueOf(continueOnError));
/* 46:   */         
/* 47:   */ 
/* 48:66 */         BeanDefinitionBuilder resourcesFactory = BeanDefinitionBuilder.genericBeanDefinition(SortedResourcesFactoryBean.class);
/* 49:67 */         resourcesFactory.addConstructorArgValue(new TypedStringValue(scriptElement.getAttribute("location")));
/* 50:68 */         delegate.addPropertyValue("scripts", resourcesFactory.getBeanDefinition());
/* 51:69 */         if (StringUtils.hasLength(scriptElement.getAttribute("encoding"))) {
/* 52:70 */           delegate.addPropertyValue("sqlScriptEncoding", new TypedStringValue(scriptElement.getAttribute("encoding")));
/* 53:   */         }
/* 54:72 */         if (StringUtils.hasLength(scriptElement.getAttribute("separator"))) {
/* 55:73 */           delegate.addPropertyValue("separator", new TypedStringValue(scriptElement.getAttribute("separator")));
/* 56:   */         }
/* 57:75 */         delegates.add(delegate.getBeanDefinition());
/* 58:   */       }
/* 59:   */     }
/* 60:77 */     builder.addPropertyValue("populators", delegates);
/* 61:   */     
/* 62:79 */     return builder.getBeanDefinition();
/* 63:   */   }
/* 64:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.config.DatabasePopulatorConfigUtils
 * JD-Core Version:    0.7.0.1
 */