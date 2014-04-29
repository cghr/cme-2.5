/*   1:    */ package org.springframework.scripting.config;
/*   2:    */ 
/*   3:    */ import java.util.List;
/*   4:    */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*   5:    */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*   6:    */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*   7:    */ import org.springframework.beans.factory.support.BeanDefinitionDefaults;
/*   8:    */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*   9:    */ import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
/*  10:    */ import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
/*  11:    */ import org.springframework.beans.factory.xml.ParserContext;
/*  12:    */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*  13:    */ import org.springframework.scripting.support.ScriptFactoryPostProcessor;
/*  14:    */ import org.springframework.util.StringUtils;
/*  15:    */ import org.springframework.util.xml.DomUtils;
/*  16:    */ import org.w3c.dom.Element;
/*  17:    */ 
/*  18:    */ class ScriptBeanDefinitionParser
/*  19:    */   extends AbstractBeanDefinitionParser
/*  20:    */ {
/*  21:    */   private static final String SCRIPT_SOURCE_ATTRIBUTE = "script-source";
/*  22:    */   private static final String INLINE_SCRIPT_ELEMENT = "inline-script";
/*  23:    */   private static final String SCOPE_ATTRIBUTE = "scope";
/*  24:    */   private static final String AUTOWIRE_ATTRIBUTE = "autowire";
/*  25:    */   private static final String DEPENDENCY_CHECK_ATTRIBUTE = "dependency-check";
/*  26:    */   private static final String INIT_METHOD_ATTRIBUTE = "init-method";
/*  27:    */   private static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";
/*  28:    */   private static final String SCRIPT_INTERFACES_ATTRIBUTE = "script-interfaces";
/*  29:    */   private static final String REFRESH_CHECK_DELAY_ATTRIBUTE = "refresh-check-delay";
/*  30:    */   private static final String PROXY_TARGET_CLASS_ATTRIBUTE = "proxy-target-class";
/*  31:    */   private static final String CUSTOMIZER_REF_ATTRIBUTE = "customizer-ref";
/*  32:    */   private final String scriptFactoryClassName;
/*  33:    */   
/*  34:    */   public ScriptBeanDefinitionParser(String scriptFactoryClassName)
/*  35:    */   {
/*  36: 93 */     this.scriptFactoryClassName = scriptFactoryClassName;
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext)
/*  40:    */   {
/*  41:104 */     String value = resolveScriptSource(element, parserContext.getReaderContext());
/*  42:105 */     if (value == null) {
/*  43:106 */       return null;
/*  44:    */     }
/*  45:110 */     LangNamespaceUtils.registerScriptFactoryPostProcessorIfNecessary(parserContext.getRegistry());
/*  46:    */     
/*  47:    */ 
/*  48:113 */     GenericBeanDefinition bd = new GenericBeanDefinition();
/*  49:114 */     bd.setBeanClassName(this.scriptFactoryClassName);
/*  50:115 */     bd.setSource(parserContext.extractSource(element));
/*  51:116 */     bd.setAttribute(ScriptFactoryPostProcessor.LANGUAGE_ATTRIBUTE, element.getLocalName());
/*  52:    */     
/*  53:    */ 
/*  54:119 */     String scope = element.getAttribute("scope");
/*  55:120 */     if (StringUtils.hasLength(scope)) {
/*  56:121 */       bd.setScope(scope);
/*  57:    */     }
/*  58:125 */     String autowire = element.getAttribute("autowire");
/*  59:126 */     int autowireMode = parserContext.getDelegate().getAutowireMode(autowire);
/*  60:128 */     if (autowireMode == 4) {
/*  61:129 */       autowireMode = 2;
/*  62:131 */     } else if (autowireMode == 3) {
/*  63:132 */       autowireMode = 0;
/*  64:    */     }
/*  65:134 */     bd.setAutowireMode(autowireMode);
/*  66:    */     
/*  67:    */ 
/*  68:137 */     String dependencyCheck = element.getAttribute("dependency-check");
/*  69:138 */     bd.setDependencyCheck(parserContext.getDelegate().getDependencyCheck(dependencyCheck));
/*  70:    */     
/*  71:    */ 
/*  72:141 */     BeanDefinitionDefaults beanDefinitionDefaults = parserContext.getDelegate().getBeanDefinitionDefaults();
/*  73:    */     
/*  74:    */ 
/*  75:144 */     String initMethod = element.getAttribute("init-method");
/*  76:145 */     if (StringUtils.hasLength(initMethod)) {
/*  77:146 */       bd.setInitMethodName(initMethod);
/*  78:148 */     } else if (beanDefinitionDefaults.getInitMethodName() != null) {
/*  79:149 */       bd.setInitMethodName(beanDefinitionDefaults.getInitMethodName());
/*  80:    */     }
/*  81:152 */     String destroyMethod = element.getAttribute("destroy-method");
/*  82:153 */     if (StringUtils.hasLength(destroyMethod)) {
/*  83:154 */       bd.setDestroyMethodName(destroyMethod);
/*  84:156 */     } else if (beanDefinitionDefaults.getDestroyMethodName() != null) {
/*  85:157 */       bd.setDestroyMethodName(beanDefinitionDefaults.getDestroyMethodName());
/*  86:    */     }
/*  87:161 */     String refreshCheckDelay = element.getAttribute("refresh-check-delay");
/*  88:162 */     if (StringUtils.hasText(refreshCheckDelay)) {
/*  89:163 */       bd.setAttribute(ScriptFactoryPostProcessor.REFRESH_CHECK_DELAY_ATTRIBUTE, new Long(refreshCheckDelay));
/*  90:    */     }
/*  91:167 */     String proxyTargetClass = element.getAttribute("proxy-target-class");
/*  92:168 */     if (StringUtils.hasText(proxyTargetClass))
/*  93:    */     {
/*  94:169 */       Boolean flag = new Boolean(proxyTargetClass);
/*  95:170 */       bd.setAttribute(ScriptFactoryPostProcessor.PROXY_TARGET_CLASS_ATTRIBUTE, flag);
/*  96:    */     }
/*  97:174 */     ConstructorArgumentValues cav = bd.getConstructorArgumentValues();
/*  98:175 */     int constructorArgNum = 0;
/*  99:176 */     cav.addIndexedArgumentValue(constructorArgNum++, value);
/* 100:177 */     if (element.hasAttribute("script-interfaces")) {
/* 101:178 */       cav.addIndexedArgumentValue(constructorArgNum++, element.getAttribute("script-interfaces"));
/* 102:    */     }
/* 103:182 */     if (element.hasAttribute("customizer-ref"))
/* 104:    */     {
/* 105:183 */       String customizerBeanName = element.getAttribute("customizer-ref");
/* 106:184 */       if (!StringUtils.hasText(customizerBeanName)) {
/* 107:185 */         parserContext.getReaderContext().error("Attribute 'customizer-ref' has empty value", element);
/* 108:    */       } else {
/* 109:188 */         cav.addIndexedArgumentValue(constructorArgNum++, new RuntimeBeanReference(customizerBeanName));
/* 110:    */       }
/* 111:    */     }
/* 112:193 */     parserContext.getDelegate().parsePropertyElements(element, bd);
/* 113:    */     
/* 114:195 */     return bd;
/* 115:    */   }
/* 116:    */   
/* 117:    */   private String resolveScriptSource(Element element, XmlReaderContext readerContext)
/* 118:    */   {
/* 119:204 */     boolean hasScriptSource = element.hasAttribute("script-source");
/* 120:205 */     List elements = DomUtils.getChildElementsByTagName(element, "inline-script");
/* 121:206 */     if ((hasScriptSource) && (!elements.isEmpty()))
/* 122:    */     {
/* 123:207 */       readerContext.error("Only one of 'script-source' and 'inline-script' should be specified.", element);
/* 124:208 */       return null;
/* 125:    */     }
/* 126:210 */     if (hasScriptSource) {
/* 127:211 */       return element.getAttribute("script-source");
/* 128:    */     }
/* 129:213 */     if (!elements.isEmpty())
/* 130:    */     {
/* 131:214 */       Element inlineElement = (Element)elements.get(0);
/* 132:215 */       return "inline:" + DomUtils.getTextValue(inlineElement);
/* 133:    */     }
/* 134:218 */     readerContext.error("Must specify either 'script-source' or 'inline-script'.", element);
/* 135:219 */     return null;
/* 136:    */   }
/* 137:    */   
/* 138:    */   protected boolean shouldGenerateIdAsFallback()
/* 139:    */   {
/* 140:228 */     return true;
/* 141:    */   }
/* 142:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scripting.config.ScriptBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */