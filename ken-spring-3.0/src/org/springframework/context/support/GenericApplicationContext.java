/*   1:    */ package org.springframework.context.support;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import org.springframework.beans.BeansException;
/*   5:    */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*   6:    */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*   7:    */ import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
/*   8:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*   9:    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*  10:    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  11:    */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*  12:    */ import org.springframework.context.ApplicationContext;
/*  13:    */ import org.springframework.core.io.Resource;
/*  14:    */ import org.springframework.core.io.ResourceLoader;
/*  15:    */ import org.springframework.core.io.support.ResourcePatternResolver;
/*  16:    */ import org.springframework.util.Assert;
/*  17:    */ 
/*  18:    */ public class GenericApplicationContext
/*  19:    */   extends AbstractApplicationContext
/*  20:    */   implements BeanDefinitionRegistry
/*  21:    */ {
/*  22:    */   private final DefaultListableBeanFactory beanFactory;
/*  23:    */   private ResourceLoader resourceLoader;
/*  24: 94 */   private boolean refreshed = false;
/*  25:    */   
/*  26:    */   public GenericApplicationContext()
/*  27:    */   {
/*  28:103 */     this.beanFactory = new DefaultListableBeanFactory();
/*  29:104 */     this.beanFactory.setAutowireCandidateResolver(new QualifierAnnotationAutowireCandidateResolver());
/*  30:    */   }
/*  31:    */   
/*  32:    */   public GenericApplicationContext(DefaultListableBeanFactory beanFactory)
/*  33:    */   {
/*  34:114 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/*  35:115 */     this.beanFactory = beanFactory;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public GenericApplicationContext(ApplicationContext parent)
/*  39:    */   {
/*  40:125 */     this();
/*  41:126 */     setParent(parent);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public GenericApplicationContext(DefaultListableBeanFactory beanFactory, ApplicationContext parent)
/*  45:    */   {
/*  46:137 */     this(beanFactory);
/*  47:138 */     setParent(parent);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setParent(ApplicationContext parent)
/*  51:    */   {
/*  52:149 */     super.setParent(parent);
/*  53:150 */     this.beanFactory.setParentBeanFactory(getInternalParentBeanFactory());
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setId(String id)
/*  57:    */   {
/*  58:155 */     super.setId(id);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void setAllowBeanDefinitionOverriding(boolean allowBeanDefinitionOverriding)
/*  62:    */   {
/*  63:165 */     this.beanFactory.setAllowBeanDefinitionOverriding(allowBeanDefinitionOverriding);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void setAllowCircularReferences(boolean allowCircularReferences)
/*  67:    */   {
/*  68:176 */     this.beanFactory.setAllowCircularReferences(allowCircularReferences);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void setResourceLoader(ResourceLoader resourceLoader)
/*  72:    */   {
/*  73:198 */     this.resourceLoader = resourceLoader;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public Resource getResource(String location)
/*  77:    */   {
/*  78:209 */     if (this.resourceLoader != null) {
/*  79:210 */       return this.resourceLoader.getResource(location);
/*  80:    */     }
/*  81:212 */     return super.getResource(location);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public Resource[] getResources(String locationPattern)
/*  85:    */     throws IOException
/*  86:    */   {
/*  87:223 */     if ((this.resourceLoader instanceof ResourcePatternResolver)) {
/*  88:224 */       return ((ResourcePatternResolver)this.resourceLoader).getResources(locationPattern);
/*  89:    */     }
/*  90:226 */     return super.getResources(locationPattern);
/*  91:    */   }
/*  92:    */   
/*  93:    */   protected final void refreshBeanFactory()
/*  94:    */     throws IllegalStateException
/*  95:    */   {
/*  96:241 */     if (this.refreshed) {
/*  97:242 */       throw new IllegalStateException(
/*  98:243 */         "GenericApplicationContext does not support multiple refresh attempts: just call 'refresh' once");
/*  99:    */     }
/* 100:245 */     this.beanFactory.setSerializationId(getId());
/* 101:246 */     this.refreshed = true;
/* 102:    */   }
/* 103:    */   
/* 104:    */   protected void cancelRefresh(BeansException ex)
/* 105:    */   {
/* 106:251 */     this.beanFactory.setSerializationId(null);
/* 107:252 */     super.cancelRefresh(ex);
/* 108:    */   }
/* 109:    */   
/* 110:    */   protected final void closeBeanFactory()
/* 111:    */   {
/* 112:261 */     this.beanFactory.setSerializationId(null);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public final ConfigurableListableBeanFactory getBeanFactory()
/* 116:    */   {
/* 117:270 */     return this.beanFactory;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public final DefaultListableBeanFactory getDefaultListableBeanFactory()
/* 121:    */   {
/* 122:282 */     return this.beanFactory;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
/* 126:    */     throws BeanDefinitionStoreException
/* 127:    */   {
/* 128:293 */     this.beanFactory.registerBeanDefinition(beanName, beanDefinition);
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void removeBeanDefinition(String beanName)
/* 132:    */     throws NoSuchBeanDefinitionException
/* 133:    */   {
/* 134:297 */     this.beanFactory.removeBeanDefinition(beanName);
/* 135:    */   }
/* 136:    */   
/* 137:    */   public BeanDefinition getBeanDefinition(String beanName)
/* 138:    */     throws NoSuchBeanDefinitionException
/* 139:    */   {
/* 140:301 */     return this.beanFactory.getBeanDefinition(beanName);
/* 141:    */   }
/* 142:    */   
/* 143:    */   public boolean isBeanNameInUse(String beanName)
/* 144:    */   {
/* 145:305 */     return this.beanFactory.isBeanNameInUse(beanName);
/* 146:    */   }
/* 147:    */   
/* 148:    */   public void registerAlias(String beanName, String alias)
/* 149:    */   {
/* 150:309 */     this.beanFactory.registerAlias(beanName, alias);
/* 151:    */   }
/* 152:    */   
/* 153:    */   public void removeAlias(String alias)
/* 154:    */   {
/* 155:313 */     this.beanFactory.removeAlias(alias);
/* 156:    */   }
/* 157:    */   
/* 158:    */   public boolean isAlias(String beanName)
/* 159:    */   {
/* 160:317 */     return this.beanFactory.isAlias(beanName);
/* 161:    */   }
/* 162:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.GenericApplicationContext
 * JD-Core Version:    0.7.0.1
 */