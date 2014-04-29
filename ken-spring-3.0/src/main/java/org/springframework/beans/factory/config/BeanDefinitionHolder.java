/*   1:    */ package org.springframework.beans.factory.config;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.BeanMetadataElement;
/*   4:    */ import org.springframework.util.Assert;
/*   5:    */ import org.springframework.util.ObjectUtils;
/*   6:    */ import org.springframework.util.StringUtils;
/*   7:    */ 
/*   8:    */ public class BeanDefinitionHolder
/*   9:    */   implements BeanMetadataElement
/*  10:    */ {
/*  11:    */   private final BeanDefinition beanDefinition;
/*  12:    */   private final String beanName;
/*  13:    */   private final String[] aliases;
/*  14:    */   
/*  15:    */   public BeanDefinitionHolder(BeanDefinition beanDefinition, String beanName)
/*  16:    */   {
/*  17: 53 */     this(beanDefinition, beanName, null);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public BeanDefinitionHolder(BeanDefinition beanDefinition, String beanName, String[] aliases)
/*  21:    */   {
/*  22: 63 */     Assert.notNull(beanDefinition, "BeanDefinition must not be null");
/*  23: 64 */     Assert.notNull(beanName, "Bean name must not be null");
/*  24: 65 */     this.beanDefinition = beanDefinition;
/*  25: 66 */     this.beanName = beanName;
/*  26: 67 */     this.aliases = aliases;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public BeanDefinitionHolder(BeanDefinitionHolder beanDefinitionHolder)
/*  30:    */   {
/*  31: 78 */     Assert.notNull(beanDefinitionHolder, "BeanDefinitionHolder must not be null");
/*  32: 79 */     this.beanDefinition = beanDefinitionHolder.getBeanDefinition();
/*  33: 80 */     this.beanName = beanDefinitionHolder.getBeanName();
/*  34: 81 */     this.aliases = beanDefinitionHolder.getAliases();
/*  35:    */   }
/*  36:    */   
/*  37:    */   public BeanDefinition getBeanDefinition()
/*  38:    */   {
/*  39: 89 */     return this.beanDefinition;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public String getBeanName()
/*  43:    */   {
/*  44: 96 */     return this.beanName;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public String[] getAliases()
/*  48:    */   {
/*  49:104 */     return this.aliases;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public Object getSource()
/*  53:    */   {
/*  54:112 */     return this.beanDefinition.getSource();
/*  55:    */   }
/*  56:    */   
/*  57:    */   public boolean matchesName(String candidateName)
/*  58:    */   {
/*  59:121 */     return (candidateName != null) && ((candidateName.equals(this.beanName)) || (ObjectUtils.containsElement(this.aliases, candidateName)));
/*  60:    */   }
/*  61:    */   
/*  62:    */   public String getShortDescription()
/*  63:    */   {
/*  64:131 */     StringBuilder sb = new StringBuilder();
/*  65:132 */     sb.append("Bean definition with name '").append(this.beanName).append("'");
/*  66:133 */     if (this.aliases != null) {
/*  67:134 */       sb.append(" and aliases [").append(StringUtils.arrayToCommaDelimitedString(this.aliases)).append("]");
/*  68:    */     }
/*  69:136 */     return sb.toString();
/*  70:    */   }
/*  71:    */   
/*  72:    */   public String getLongDescription()
/*  73:    */   {
/*  74:146 */     StringBuilder sb = new StringBuilder(getShortDescription());
/*  75:147 */     sb.append(": ").append(this.beanDefinition);
/*  76:148 */     return sb.toString();
/*  77:    */   }
/*  78:    */   
/*  79:    */   public String toString()
/*  80:    */   {
/*  81:159 */     return getLongDescription();
/*  82:    */   }
/*  83:    */   
/*  84:    */   public boolean equals(Object other)
/*  85:    */   {
/*  86:165 */     if (this == other) {
/*  87:166 */       return true;
/*  88:    */     }
/*  89:168 */     if (!(other instanceof BeanDefinitionHolder)) {
/*  90:169 */       return false;
/*  91:    */     }
/*  92:171 */     BeanDefinitionHolder otherHolder = (BeanDefinitionHolder)other;
/*  93:    */     
/*  94:    */ 
/*  95:174 */     return (this.beanDefinition.equals(otherHolder.beanDefinition)) && (this.beanName.equals(otherHolder.beanName)) && (ObjectUtils.nullSafeEquals(this.aliases, otherHolder.aliases));
/*  96:    */   }
/*  97:    */   
/*  98:    */   public int hashCode()
/*  99:    */   {
/* 100:179 */     int hashCode = this.beanDefinition.hashCode();
/* 101:180 */     hashCode = 29 * hashCode + this.beanName.hashCode();
/* 102:181 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.aliases);
/* 103:182 */     return hashCode;
/* 104:    */   }
/* 105:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.BeanDefinitionHolder
 * JD-Core Version:    0.7.0.1
 */