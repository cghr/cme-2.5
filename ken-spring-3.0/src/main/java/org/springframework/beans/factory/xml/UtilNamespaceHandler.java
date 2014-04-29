/*   1:    */ package org.springframework.beans.factory.xml;
/*   2:    */ 
/*   3:    */ import java.util.List;
/*   4:    */ import java.util.Map;
/*   5:    */ import java.util.Properties;
/*   6:    */ import java.util.Set;
/*   7:    */ import org.springframework.beans.factory.config.FieldRetrievingFactoryBean;
/*   8:    */ import org.springframework.beans.factory.config.ListFactoryBean;
/*   9:    */ import org.springframework.beans.factory.config.MapFactoryBean;
/*  10:    */ import org.springframework.beans.factory.config.PropertiesFactoryBean;
/*  11:    */ import org.springframework.beans.factory.config.PropertyPathFactoryBean;
/*  12:    */ import org.springframework.beans.factory.config.SetFactoryBean;
/*  13:    */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*  14:    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*  15:    */ import org.springframework.util.StringUtils;
/*  16:    */ import org.w3c.dom.Element;
/*  17:    */ 
/*  18:    */ public class UtilNamespaceHandler
/*  19:    */   extends NamespaceHandlerSupport
/*  20:    */ {
/*  21:    */   private static final String SCOPE_ATTRIBUTE = "scope";
/*  22:    */   
/*  23:    */   public void init()
/*  24:    */   {
/*  25: 49 */     registerBeanDefinitionParser("constant", new ConstantBeanDefinitionParser(null));
/*  26: 50 */     registerBeanDefinitionParser("property-path", new PropertyPathBeanDefinitionParser(null));
/*  27: 51 */     registerBeanDefinitionParser("list", new ListBeanDefinitionParser(null));
/*  28: 52 */     registerBeanDefinitionParser("set", new SetBeanDefinitionParser(null));
/*  29: 53 */     registerBeanDefinitionParser("map", new MapBeanDefinitionParser(null));
/*  30: 54 */     registerBeanDefinitionParser("properties", new PropertiesBeanDefinitionParser(null));
/*  31:    */   }
/*  32:    */   
/*  33:    */   private static class ConstantBeanDefinitionParser
/*  34:    */     extends AbstractSimpleBeanDefinitionParser
/*  35:    */   {
/*  36:    */     protected Class getBeanClass(Element element)
/*  37:    */     {
/*  38: 62 */       return FieldRetrievingFactoryBean.class;
/*  39:    */     }
/*  40:    */     
/*  41:    */     protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext)
/*  42:    */     {
/*  43: 67 */       String id = super.resolveId(element, definition, parserContext);
/*  44: 68 */       if (!StringUtils.hasText(id)) {
/*  45: 69 */         id = element.getAttribute("static-field");
/*  46:    */       }
/*  47: 71 */       return id;
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   private static class PropertyPathBeanDefinitionParser
/*  52:    */     extends AbstractSingleBeanDefinitionParser
/*  53:    */   {
/*  54:    */     protected Class getBeanClass(Element element)
/*  55:    */     {
/*  56: 80 */       return PropertyPathFactoryBean.class;
/*  57:    */     }
/*  58:    */     
/*  59:    */     protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
/*  60:    */     {
/*  61: 85 */       String path = element.getAttribute("path");
/*  62: 86 */       if (!StringUtils.hasText(path))
/*  63:    */       {
/*  64: 87 */         parserContext.getReaderContext().error("Attribute 'path' must not be empty", element);
/*  65: 88 */         return;
/*  66:    */       }
/*  67: 90 */       int dotIndex = path.indexOf(".");
/*  68: 91 */       if (dotIndex == -1)
/*  69:    */       {
/*  70: 92 */         parserContext.getReaderContext().error(
/*  71: 93 */           "Attribute 'path' must follow pattern 'beanName.propertyName'", element);
/*  72: 94 */         return;
/*  73:    */       }
/*  74: 96 */       String beanName = path.substring(0, dotIndex);
/*  75: 97 */       String propertyPath = path.substring(dotIndex + 1);
/*  76: 98 */       builder.addPropertyValue("targetBeanName", beanName);
/*  77: 99 */       builder.addPropertyValue("propertyPath", propertyPath);
/*  78:    */     }
/*  79:    */     
/*  80:    */     protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext)
/*  81:    */     {
/*  82:104 */       String id = super.resolveId(element, definition, parserContext);
/*  83:105 */       if (!StringUtils.hasText(id)) {
/*  84:106 */         id = element.getAttribute("path");
/*  85:    */       }
/*  86:108 */       return id;
/*  87:    */     }
/*  88:    */   }
/*  89:    */   
/*  90:    */   private static class ListBeanDefinitionParser
/*  91:    */     extends AbstractSingleBeanDefinitionParser
/*  92:    */   {
/*  93:    */     protected Class getBeanClass(Element element)
/*  94:    */     {
/*  95:117 */       return ListFactoryBean.class;
/*  96:    */     }
/*  97:    */     
/*  98:    */     protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
/*  99:    */     {
/* 100:122 */       String listClass = element.getAttribute("list-class");
/* 101:123 */       List parsedList = parserContext.getDelegate().parseListElement(element, builder.getRawBeanDefinition());
/* 102:124 */       builder.addPropertyValue("sourceList", parsedList);
/* 103:125 */       if (StringUtils.hasText(listClass)) {
/* 104:126 */         builder.addPropertyValue("targetListClass", listClass);
/* 105:    */       }
/* 106:128 */       String scope = element.getAttribute("scope");
/* 107:129 */       if (StringUtils.hasLength(scope)) {
/* 108:130 */         builder.setScope(scope);
/* 109:    */       }
/* 110:    */     }
/* 111:    */   }
/* 112:    */   
/* 113:    */   private static class SetBeanDefinitionParser
/* 114:    */     extends AbstractSingleBeanDefinitionParser
/* 115:    */   {
/* 116:    */     protected Class getBeanClass(Element element)
/* 117:    */     {
/* 118:140 */       return SetFactoryBean.class;
/* 119:    */     }
/* 120:    */     
/* 121:    */     protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
/* 122:    */     {
/* 123:145 */       String setClass = element.getAttribute("set-class");
/* 124:146 */       Set parsedSet = parserContext.getDelegate().parseSetElement(element, builder.getRawBeanDefinition());
/* 125:147 */       builder.addPropertyValue("sourceSet", parsedSet);
/* 126:148 */       if (StringUtils.hasText(setClass)) {
/* 127:149 */         builder.addPropertyValue("targetSetClass", setClass);
/* 128:    */       }
/* 129:151 */       String scope = element.getAttribute("scope");
/* 130:152 */       if (StringUtils.hasLength(scope)) {
/* 131:153 */         builder.setScope(scope);
/* 132:    */       }
/* 133:    */     }
/* 134:    */   }
/* 135:    */   
/* 136:    */   private static class MapBeanDefinitionParser
/* 137:    */     extends AbstractSingleBeanDefinitionParser
/* 138:    */   {
/* 139:    */     protected Class getBeanClass(Element element)
/* 140:    */     {
/* 141:163 */       return MapFactoryBean.class;
/* 142:    */     }
/* 143:    */     
/* 144:    */     protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
/* 145:    */     {
/* 146:168 */       String mapClass = element.getAttribute("map-class");
/* 147:169 */       Map parsedMap = parserContext.getDelegate().parseMapElement(element, builder.getRawBeanDefinition());
/* 148:170 */       builder.addPropertyValue("sourceMap", parsedMap);
/* 149:171 */       if (StringUtils.hasText(mapClass)) {
/* 150:172 */         builder.addPropertyValue("targetMapClass", mapClass);
/* 151:    */       }
/* 152:174 */       String scope = element.getAttribute("scope");
/* 153:175 */       if (StringUtils.hasLength(scope)) {
/* 154:176 */         builder.setScope(scope);
/* 155:    */       }
/* 156:    */     }
/* 157:    */   }
/* 158:    */   
/* 159:    */   private static class PropertiesBeanDefinitionParser
/* 160:    */     extends AbstractSimpleBeanDefinitionParser
/* 161:    */   {
/* 162:    */     protected Class getBeanClass(Element element)
/* 163:    */     {
/* 164:186 */       return PropertiesFactoryBean.class;
/* 165:    */     }
/* 166:    */     
/* 167:    */     protected boolean isEligibleAttribute(String attributeName)
/* 168:    */     {
/* 169:191 */       return (super.isEligibleAttribute(attributeName)) && (!"scope".equals(attributeName));
/* 170:    */     }
/* 171:    */     
/* 172:    */     protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
/* 173:    */     {
/* 174:196 */       super.doParse(element, parserContext, builder);
/* 175:197 */       Properties parsedProps = parserContext.getDelegate().parsePropsElement(element);
/* 176:198 */       builder.addPropertyValue("properties", parsedProps);
/* 177:199 */       String scope = element.getAttribute("scope");
/* 178:200 */       if (StringUtils.hasLength(scope)) {
/* 179:201 */         builder.setScope(scope);
/* 180:    */       }
/* 181:    */     }
/* 182:    */   }
/* 183:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.UtilNamespaceHandler
 * JD-Core Version:    0.7.0.1
 */