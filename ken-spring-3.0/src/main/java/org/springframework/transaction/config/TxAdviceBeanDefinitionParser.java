/*   1:    */ package org.springframework.transaction.config;
/*   2:    */ 
/*   3:    */ import java.util.LinkedList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.springframework.beans.MutablePropertyValues;
/*   6:    */ import org.springframework.beans.factory.config.TypedStringValue;
/*   7:    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*   8:    */ import org.springframework.beans.factory.support.ManagedMap;
/*   9:    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*  10:    */ import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
/*  11:    */ import org.springframework.beans.factory.xml.ParserContext;
/*  12:    */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*  13:    */ import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
/*  14:    */ import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
/*  15:    */ import org.springframework.transaction.interceptor.NoRollbackRuleAttribute;
/*  16:    */ import org.springframework.transaction.interceptor.RollbackRuleAttribute;
/*  17:    */ import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
/*  18:    */ import org.springframework.transaction.interceptor.TransactionInterceptor;
/*  19:    */ import org.springframework.util.StringUtils;
/*  20:    */ import org.springframework.util.xml.DomUtils;
/*  21:    */ import org.w3c.dom.Element;
/*  22:    */ 
/*  23:    */ class TxAdviceBeanDefinitionParser
/*  24:    */   extends AbstractSingleBeanDefinitionParser
/*  25:    */ {
/*  26:    */   private static final String METHOD_ELEMENT = "method";
/*  27:    */   private static final String METHOD_NAME_ATTRIBUTE = "name";
/*  28:    */   private static final String ATTRIBUTES_ELEMENT = "attributes";
/*  29:    */   private static final String TIMEOUT_ATTRIBUTE = "timeout";
/*  30:    */   private static final String READ_ONLY_ATTRIBUTE = "read-only";
/*  31:    */   private static final String PROPAGATION_ATTRIBUTE = "propagation";
/*  32:    */   private static final String ISOLATION_ATTRIBUTE = "isolation";
/*  33:    */   private static final String ROLLBACK_FOR_ATTRIBUTE = "rollback-for";
/*  34:    */   private static final String NO_ROLLBACK_FOR_ATTRIBUTE = "no-rollback-for";
/*  35:    */   
/*  36:    */   protected Class<?> getBeanClass(Element element)
/*  37:    */   {
/*  38: 72 */     return TransactionInterceptor.class;
/*  39:    */   }
/*  40:    */   
/*  41:    */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
/*  42:    */   {
/*  43: 77 */     builder.addPropertyReference("transactionManager", TxNamespaceHandler.getTransactionManagerName(element));
/*  44:    */     
/*  45: 79 */     List<Element> txAttributes = DomUtils.getChildElementsByTagName(element, "attributes");
/*  46: 80 */     if (txAttributes.size() > 1)
/*  47:    */     {
/*  48: 81 */       parserContext.getReaderContext().error(
/*  49: 82 */         "Element <attributes> is allowed at most once inside element <advice>", element);
/*  50:    */     }
/*  51: 84 */     else if (txAttributes.size() == 1)
/*  52:    */     {
/*  53: 86 */       Element attributeSourceElement = (Element)txAttributes.get(0);
/*  54: 87 */       RootBeanDefinition attributeSourceDefinition = parseAttributeSource(attributeSourceElement, parserContext);
/*  55: 88 */       builder.addPropertyValue("transactionAttributeSource", attributeSourceDefinition);
/*  56:    */     }
/*  57:    */     else
/*  58:    */     {
/*  59: 92 */       builder.addPropertyValue("transactionAttributeSource", 
/*  60: 93 */         new RootBeanDefinition(AnnotationTransactionAttributeSource.class));
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   private RootBeanDefinition parseAttributeSource(Element attrEle, ParserContext parserContext)
/*  65:    */   {
/*  66: 98 */     List<Element> methods = DomUtils.getChildElementsByTagName(attrEle, "method");
/*  67: 99 */     ManagedMap<TypedStringValue, RuleBasedTransactionAttribute> transactionAttributeMap = 
/*  68:100 */       new ManagedMap(methods.size());
/*  69:101 */     transactionAttributeMap.setSource(parserContext.extractSource(attrEle));
/*  70:103 */     for (Element methodEle : methods)
/*  71:    */     {
/*  72:104 */       String name = methodEle.getAttribute("name");
/*  73:105 */       TypedStringValue nameHolder = new TypedStringValue(name);
/*  74:106 */       nameHolder.setSource(parserContext.extractSource(methodEle));
/*  75:    */       
/*  76:108 */       RuleBasedTransactionAttribute attribute = new RuleBasedTransactionAttribute();
/*  77:109 */       String propagation = methodEle.getAttribute("propagation");
/*  78:110 */       String isolation = methodEle.getAttribute("isolation");
/*  79:111 */       String timeout = methodEle.getAttribute("timeout");
/*  80:112 */       String readOnly = methodEle.getAttribute("read-only");
/*  81:113 */       if (StringUtils.hasText(propagation)) {
/*  82:114 */         attribute.setPropagationBehaviorName("PROPAGATION_" + propagation);
/*  83:    */       }
/*  84:116 */       if (StringUtils.hasText(isolation)) {
/*  85:117 */         attribute.setIsolationLevelName("ISOLATION_" + isolation);
/*  86:    */       }
/*  87:119 */       if (StringUtils.hasText(timeout)) {
/*  88:    */         try
/*  89:    */         {
/*  90:121 */           attribute.setTimeout(Integer.parseInt(timeout));
/*  91:    */         }
/*  92:    */         catch (NumberFormatException localNumberFormatException)
/*  93:    */         {
/*  94:124 */           parserContext.getReaderContext().error("Timeout must be an integer value: [" + timeout + "]", methodEle);
/*  95:    */         }
/*  96:    */       }
/*  97:127 */       if (StringUtils.hasText(readOnly)) {
/*  98:128 */         attribute.setReadOnly(Boolean.valueOf(methodEle.getAttribute("read-only")).booleanValue());
/*  99:    */       }
/* 100:131 */       List<RollbackRuleAttribute> rollbackRules = new LinkedList();
/* 101:132 */       if (methodEle.hasAttribute("rollback-for"))
/* 102:    */       {
/* 103:133 */         String rollbackForValue = methodEle.getAttribute("rollback-for");
/* 104:134 */         addRollbackRuleAttributesTo(rollbackRules, rollbackForValue);
/* 105:    */       }
/* 106:136 */       if (methodEle.hasAttribute("no-rollback-for"))
/* 107:    */       {
/* 108:137 */         String noRollbackForValue = methodEle.getAttribute("no-rollback-for");
/* 109:138 */         addNoRollbackRuleAttributesTo(rollbackRules, noRollbackForValue);
/* 110:    */       }
/* 111:140 */       attribute.setRollbackRules(rollbackRules);
/* 112:    */       
/* 113:142 */       transactionAttributeMap.put(nameHolder, attribute);
/* 114:    */     }
/* 115:145 */     RootBeanDefinition attributeSourceDefinition = new RootBeanDefinition(NameMatchTransactionAttributeSource.class);
/* 116:146 */     attributeSourceDefinition.setSource(parserContext.extractSource(attrEle));
/* 117:147 */     attributeSourceDefinition.getPropertyValues().add("nameMap", transactionAttributeMap);
/* 118:148 */     return attributeSourceDefinition;
/* 119:    */   }
/* 120:    */   
/* 121:    */   private void addRollbackRuleAttributesTo(List<RollbackRuleAttribute> rollbackRules, String rollbackForValue)
/* 122:    */   {
/* 123:152 */     String[] exceptionTypeNames = StringUtils.commaDelimitedListToStringArray(rollbackForValue);
/* 124:153 */     for (String typeName : exceptionTypeNames) {
/* 125:154 */       rollbackRules.add(new RollbackRuleAttribute(StringUtils.trimWhitespace(typeName)));
/* 126:    */     }
/* 127:    */   }
/* 128:    */   
/* 129:    */   private void addNoRollbackRuleAttributesTo(List<RollbackRuleAttribute> rollbackRules, String noRollbackForValue)
/* 130:    */   {
/* 131:159 */     String[] exceptionTypeNames = StringUtils.commaDelimitedListToStringArray(noRollbackForValue);
/* 132:160 */     for (String typeName : exceptionTypeNames) {
/* 133:161 */       rollbackRules.add(new NoRollbackRuleAttribute(StringUtils.trimWhitespace(typeName)));
/* 134:    */     }
/* 135:    */   }
/* 136:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.config.TxAdviceBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */