/*   1:    */ package org.springframework.beans.factory.parsing;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.springframework.beans.PropertyValue;
/*   6:    */ import org.springframework.beans.PropertyValues;
/*   7:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*   8:    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*   9:    */ import org.springframework.beans.factory.config.BeanReference;
/*  10:    */ 
/*  11:    */ public class BeanComponentDefinition
/*  12:    */   extends BeanDefinitionHolder
/*  13:    */   implements ComponentDefinition
/*  14:    */ {
/*  15:    */   private BeanDefinition[] innerBeanDefinitions;
/*  16:    */   private BeanReference[] beanReferences;
/*  17:    */   
/*  18:    */   public BeanComponentDefinition(BeanDefinition beanDefinition, String beanName)
/*  19:    */   {
/*  20: 49 */     super(beanDefinition, beanName);
/*  21: 50 */     findInnerBeanDefinitionsAndBeanReferences(beanDefinition);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public BeanComponentDefinition(BeanDefinition beanDefinition, String beanName, String[] aliases)
/*  25:    */   {
/*  26: 60 */     super(beanDefinition, beanName, aliases);
/*  27: 61 */     findInnerBeanDefinitionsAndBeanReferences(beanDefinition);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public BeanComponentDefinition(BeanDefinitionHolder holder)
/*  31:    */   {
/*  32: 70 */     super(holder);
/*  33: 71 */     findInnerBeanDefinitionsAndBeanReferences(holder.getBeanDefinition());
/*  34:    */   }
/*  35:    */   
/*  36:    */   private void findInnerBeanDefinitionsAndBeanReferences(BeanDefinition beanDefinition)
/*  37:    */   {
/*  38: 76 */     List<BeanDefinition> innerBeans = new ArrayList();
/*  39: 77 */     List<BeanReference> references = new ArrayList();
/*  40: 78 */     PropertyValues propertyValues = beanDefinition.getPropertyValues();
/*  41: 79 */     for (int i = 0; i < propertyValues.getPropertyValues().length; i++)
/*  42:    */     {
/*  43: 80 */       PropertyValue propertyValue = propertyValues.getPropertyValues()[i];
/*  44: 81 */       Object value = propertyValue.getValue();
/*  45: 82 */       if ((value instanceof BeanDefinitionHolder)) {
/*  46: 83 */         innerBeans.add(((BeanDefinitionHolder)value).getBeanDefinition());
/*  47: 85 */       } else if ((value instanceof BeanDefinition)) {
/*  48: 86 */         innerBeans.add((BeanDefinition)value);
/*  49: 88 */       } else if ((value instanceof BeanReference)) {
/*  50: 89 */         references.add((BeanReference)value);
/*  51:    */       }
/*  52:    */     }
/*  53: 92 */     this.innerBeanDefinitions = ((BeanDefinition[])innerBeans.toArray(new BeanDefinition[innerBeans.size()]));
/*  54: 93 */     this.beanReferences = ((BeanReference[])references.toArray(new BeanReference[references.size()]));
/*  55:    */   }
/*  56:    */   
/*  57:    */   public String getName()
/*  58:    */   {
/*  59: 98 */     return getBeanName();
/*  60:    */   }
/*  61:    */   
/*  62:    */   public String getDescription()
/*  63:    */   {
/*  64:102 */     return getShortDescription();
/*  65:    */   }
/*  66:    */   
/*  67:    */   public BeanDefinition[] getBeanDefinitions()
/*  68:    */   {
/*  69:106 */     return new BeanDefinition[] { getBeanDefinition() };
/*  70:    */   }
/*  71:    */   
/*  72:    */   public BeanDefinition[] getInnerBeanDefinitions()
/*  73:    */   {
/*  74:110 */     return this.innerBeanDefinitions;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public BeanReference[] getBeanReferences()
/*  78:    */   {
/*  79:114 */     return this.beanReferences;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public String toString()
/*  83:    */   {
/*  84:124 */     return getDescription();
/*  85:    */   }
/*  86:    */   
/*  87:    */   public boolean equals(Object other)
/*  88:    */   {
/*  89:133 */     return (this == other) || (((other instanceof BeanComponentDefinition)) && (super.equals(other)));
/*  90:    */   }
/*  91:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.BeanComponentDefinition
 * JD-Core Version:    0.7.0.1
 */