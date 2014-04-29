/*   1:    */ package org.springframework.beans.factory.xml;
/*   2:    */ 
/*   3:    */ import java.util.Stack;
/*   4:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*   5:    */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*   6:    */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*   7:    */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*   8:    */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*   9:    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  10:    */ 
/*  11:    */ public final class ParserContext
/*  12:    */ {
/*  13:    */   private final XmlReaderContext readerContext;
/*  14:    */   private final BeanDefinitionParserDelegate delegate;
/*  15:    */   private BeanDefinition containingBeanDefinition;
/*  16: 47 */   private final Stack<ComponentDefinition> containingComponents = new Stack();
/*  17:    */   
/*  18:    */   public ParserContext(XmlReaderContext readerContext, BeanDefinitionParserDelegate delegate)
/*  19:    */   {
/*  20: 51 */     this.readerContext = readerContext;
/*  21: 52 */     this.delegate = delegate;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public ParserContext(XmlReaderContext readerContext, BeanDefinitionParserDelegate delegate, BeanDefinition containingBeanDefinition)
/*  25:    */   {
/*  26: 58 */     this.readerContext = readerContext;
/*  27: 59 */     this.delegate = delegate;
/*  28: 60 */     this.containingBeanDefinition = containingBeanDefinition;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public final XmlReaderContext getReaderContext()
/*  32:    */   {
/*  33: 65 */     return this.readerContext;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public final BeanDefinitionRegistry getRegistry()
/*  37:    */   {
/*  38: 69 */     return this.readerContext.getRegistry();
/*  39:    */   }
/*  40:    */   
/*  41:    */   public final BeanDefinitionParserDelegate getDelegate()
/*  42:    */   {
/*  43: 73 */     return this.delegate;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public final BeanDefinition getContainingBeanDefinition()
/*  47:    */   {
/*  48: 77 */     return this.containingBeanDefinition;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public final boolean isNested()
/*  52:    */   {
/*  53: 81 */     return this.containingBeanDefinition != null;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public boolean isDefaultLazyInit()
/*  57:    */   {
/*  58: 85 */     return "true".equals(this.delegate.getDefaults().getLazyInit());
/*  59:    */   }
/*  60:    */   
/*  61:    */   public Object extractSource(Object sourceCandidate)
/*  62:    */   {
/*  63: 89 */     return this.readerContext.extractSource(sourceCandidate);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public CompositeComponentDefinition getContainingComponent()
/*  67:    */   {
/*  68: 93 */     return !this.containingComponents.isEmpty() ? 
/*  69: 94 */       (CompositeComponentDefinition)this.containingComponents.lastElement() : null;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void pushContainingComponent(CompositeComponentDefinition containingComponent)
/*  73:    */   {
/*  74: 98 */     this.containingComponents.push(containingComponent);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public CompositeComponentDefinition popContainingComponent()
/*  78:    */   {
/*  79:102 */     return (CompositeComponentDefinition)this.containingComponents.pop();
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void popAndRegisterContainingComponent()
/*  83:    */   {
/*  84:106 */     registerComponent(popContainingComponent());
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void registerComponent(ComponentDefinition component)
/*  88:    */   {
/*  89:110 */     CompositeComponentDefinition containingComponent = getContainingComponent();
/*  90:111 */     if (containingComponent != null) {
/*  91:112 */       containingComponent.addNestedComponent(component);
/*  92:    */     } else {
/*  93:115 */       this.readerContext.fireComponentRegistered(component);
/*  94:    */     }
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void registerBeanComponent(BeanComponentDefinition component)
/*  98:    */   {
/*  99:120 */     BeanDefinitionReaderUtils.registerBeanDefinition(component, getRegistry());
/* 100:121 */     registerComponent(component);
/* 101:    */   }
/* 102:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.ParserContext
 * JD-Core Version:    0.7.0.1
 */