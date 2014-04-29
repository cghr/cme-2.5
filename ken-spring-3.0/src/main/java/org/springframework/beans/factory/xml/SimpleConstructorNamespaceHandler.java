/*   1:    */ package org.springframework.beans.factory.xml;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.Map;
/*   5:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*   6:    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*   7:    */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*   8:    */ import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
/*   9:    */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*  10:    */ import org.springframework.core.Conventions;
/*  11:    */ import org.springframework.util.StringUtils;
/*  12:    */ import org.w3c.dom.Attr;
/*  13:    */ import org.w3c.dom.Element;
/*  14:    */ import org.w3c.dom.Node;
/*  15:    */ 
/*  16:    */ public class SimpleConstructorNamespaceHandler
/*  17:    */   implements NamespaceHandler
/*  18:    */ {
/*  19:    */   private static final String REF_SUFFIX = "-ref";
/*  20:    */   private static final String DELIMITER_PREFIX = "_";
/*  21:    */   
/*  22:    */   public void init() {}
/*  23:    */   
/*  24:    */   public BeanDefinition parse(Element element, ParserContext parserContext)
/*  25:    */   {
/*  26: 66 */     parserContext.getReaderContext().error(
/*  27: 67 */       "Class [" + getClass().getName() + "] does not support custom elements.", element);
/*  28: 68 */     return null;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext)
/*  32:    */   {
/*  33: 72 */     if ((node instanceof Attr))
/*  34:    */     {
/*  35: 73 */       Attr attr = (Attr)node;
/*  36: 74 */       String argName = StringUtils.trimWhitespace(parserContext.getDelegate().getLocalName(attr));
/*  37: 75 */       String argValue = StringUtils.trimWhitespace(attr.getValue());
/*  38:    */       
/*  39: 77 */       ConstructorArgumentValues cvs = definition.getBeanDefinition().getConstructorArgumentValues();
/*  40: 78 */       boolean ref = false;
/*  41: 81 */       if (argName.endsWith("-ref"))
/*  42:    */       {
/*  43: 82 */         ref = true;
/*  44: 83 */         argName = argName.substring(0, argName.length() - "-ref".length());
/*  45:    */       }
/*  46: 86 */       ConstructorArgumentValues.ValueHolder valueHolder = new ConstructorArgumentValues.ValueHolder(ref ? new RuntimeBeanReference(argValue) : argValue);
/*  47: 87 */       valueHolder.setSource(parserContext.getReaderContext().extractSource(attr));
/*  48: 90 */       if (argName.startsWith("_"))
/*  49:    */       {
/*  50: 91 */         String arg = argName.substring(1).trim();
/*  51: 94 */         if (!StringUtils.hasText(arg))
/*  52:    */         {
/*  53: 95 */           cvs.addGenericArgumentValue(valueHolder);
/*  54:    */         }
/*  55:    */         else
/*  56:    */         {
/*  57: 99 */           int index = -1;
/*  58:    */           try
/*  59:    */           {
/*  60:101 */             index = Integer.parseInt(arg);
/*  61:    */           }
/*  62:    */           catch (NumberFormatException localNumberFormatException)
/*  63:    */           {
/*  64:103 */             parserContext.getReaderContext().error(
/*  65:104 */               "Constructor argument '" + argName + "' specifies an invalid integer", attr);
/*  66:    */           }
/*  67:106 */           if (index < 0) {
/*  68:107 */             parserContext.getReaderContext().error(
/*  69:108 */               "Constructor argument '" + argName + "' specifies a negative index", attr);
/*  70:    */           }
/*  71:111 */           if (cvs.hasIndexedArgumentValue(index)) {
/*  72:112 */             parserContext.getReaderContext().error(
/*  73:113 */               "Constructor argument '" + argName + "' with index " + index + " already defined using <constructor-arg>." + 
/*  74:114 */               " Only one approach may be used per argument.", attr);
/*  75:    */           }
/*  76:117 */           cvs.addIndexedArgumentValue(index, valueHolder);
/*  77:    */         }
/*  78:    */       }
/*  79:    */       else
/*  80:    */       {
/*  81:122 */         String name = Conventions.attributeNameToPropertyName(argName);
/*  82:123 */         if (containsArgWithName(name, cvs)) {
/*  83:124 */           parserContext.getReaderContext().error(
/*  84:125 */             "Constructor argument '" + argName + "' already defined using <constructor-arg>." + 
/*  85:126 */             " Only one approach may be used per argument.", attr);
/*  86:    */         }
/*  87:128 */         valueHolder.setName(Conventions.attributeNameToPropertyName(argName));
/*  88:129 */         cvs.addGenericArgumentValue(valueHolder);
/*  89:    */       }
/*  90:    */     }
/*  91:132 */     return definition;
/*  92:    */   }
/*  93:    */   
/*  94:    */   private boolean containsArgWithName(String name, ConstructorArgumentValues cvs)
/*  95:    */   {
/*  96:136 */     if (!checkName(name, cvs.getGenericArgumentValues())) {
/*  97:137 */       return checkName(name, cvs.getIndexedArgumentValues().values());
/*  98:    */     }
/*  99:140 */     return true;
/* 100:    */   }
/* 101:    */   
/* 102:    */   private boolean checkName(String name, Collection<ConstructorArgumentValues.ValueHolder> values)
/* 103:    */   {
/* 104:144 */     for (ConstructorArgumentValues.ValueHolder holder : values) {
/* 105:145 */       if (name.equals(holder.getName())) {
/* 106:146 */         return true;
/* 107:    */       }
/* 108:    */     }
/* 109:149 */     return false;
/* 110:    */   }
/* 111:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.SimpleConstructorNamespaceHandler
 * JD-Core Version:    0.7.0.1
 */