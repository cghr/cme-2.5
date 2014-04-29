/*   1:    */ package org.springframework.beans.factory.xml;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*   4:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*   5:    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*   6:    */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*   7:    */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*   8:    */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*   9:    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  10:    */ import org.springframework.util.StringUtils;
/*  11:    */ import org.w3c.dom.Element;
/*  12:    */ 
/*  13:    */ public abstract class AbstractBeanDefinitionParser
/*  14:    */   implements BeanDefinitionParser
/*  15:    */ {
/*  16:    */   public static final String ID_ATTRIBUTE = "id";
/*  17:    */   public static final String NAME_ATTRIBUTE = "name";
/*  18:    */   
/*  19:    */   public final BeanDefinition parse(Element element, ParserContext parserContext)
/*  20:    */   {
/*  21: 59 */     AbstractBeanDefinition definition = parseInternal(element, parserContext);
/*  22: 60 */     if ((definition != null) && (!parserContext.isNested())) {
/*  23:    */       try
/*  24:    */       {
/*  25: 62 */         String id = resolveId(element, definition, parserContext);
/*  26: 63 */         if (!StringUtils.hasText(id)) {
/*  27: 64 */           parserContext.getReaderContext().error(
/*  28: 65 */             "Id is required for element '" + parserContext.getDelegate().getLocalName(element) + 
/*  29: 66 */             "' when used as a top-level tag", element);
/*  30:    */         }
/*  31: 68 */         String[] aliases = new String[0];
/*  32: 69 */         String name = element.getAttribute("name");
/*  33: 70 */         if (StringUtils.hasLength(name)) {
/*  34: 71 */           aliases = StringUtils.trimArrayElements(StringUtils.commaDelimitedListToStringArray(name));
/*  35:    */         }
/*  36: 73 */         BeanDefinitionHolder holder = new BeanDefinitionHolder(definition, id, aliases);
/*  37: 74 */         registerBeanDefinition(holder, parserContext.getRegistry());
/*  38: 75 */         if (shouldFireEvents())
/*  39:    */         {
/*  40: 76 */           BeanComponentDefinition componentDefinition = new BeanComponentDefinition(holder);
/*  41: 77 */           postProcessComponentDefinition(componentDefinition);
/*  42: 78 */           parserContext.registerComponent(componentDefinition);
/*  43:    */         }
/*  44:    */       }
/*  45:    */       catch (BeanDefinitionStoreException ex)
/*  46:    */       {
/*  47: 82 */         parserContext.getReaderContext().error(ex.getMessage(), element);
/*  48: 83 */         return null;
/*  49:    */       }
/*  50:    */     }
/*  51: 86 */     return definition;
/*  52:    */   }
/*  53:    */   
/*  54:    */   protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext)
/*  55:    */     throws BeanDefinitionStoreException
/*  56:    */   {
/*  57:105 */     if (shouldGenerateId()) {
/*  58:106 */       return parserContext.getReaderContext().generateBeanName(definition);
/*  59:    */     }
/*  60:109 */     String id = element.getAttribute("id");
/*  61:110 */     if ((!StringUtils.hasText(id)) && (shouldGenerateIdAsFallback())) {
/*  62:111 */       id = parserContext.getReaderContext().generateBeanName(definition);
/*  63:    */     }
/*  64:113 */     return id;
/*  65:    */   }
/*  66:    */   
/*  67:    */   protected void registerBeanDefinition(BeanDefinitionHolder definition, BeanDefinitionRegistry registry)
/*  68:    */   {
/*  69:132 */     BeanDefinitionReaderUtils.registerBeanDefinition(definition, registry);
/*  70:    */   }
/*  71:    */   
/*  72:    */   protected abstract AbstractBeanDefinition parseInternal(Element paramElement, ParserContext paramParserContext);
/*  73:    */   
/*  74:    */   protected boolean shouldGenerateId()
/*  75:    */   {
/*  76:156 */     return false;
/*  77:    */   }
/*  78:    */   
/*  79:    */   protected boolean shouldGenerateIdAsFallback()
/*  80:    */   {
/*  81:168 */     return false;
/*  82:    */   }
/*  83:    */   
/*  84:    */   protected boolean shouldFireEvents()
/*  85:    */   {
/*  86:184 */     return true;
/*  87:    */   }
/*  88:    */   
/*  89:    */   protected void postProcessComponentDefinition(BeanComponentDefinition componentDefinition) {}
/*  90:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.AbstractBeanDefinitionParser
 * JD-Core Version:    0.7.0.1
 */