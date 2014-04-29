/*   1:    */ package org.springframework.beans.factory.support;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.Set;
/*   5:    */ import org.apache.commons.logging.Log;
/*   6:    */ import org.apache.commons.logging.LogFactory;
/*   7:    */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*   8:    */ import org.springframework.core.env.Environment;
/*   9:    */ import org.springframework.core.env.EnvironmentCapable;
/*  10:    */ import org.springframework.core.env.StandardEnvironment;
/*  11:    */ import org.springframework.core.io.Resource;
/*  12:    */ import org.springframework.core.io.ResourceLoader;
/*  13:    */ import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
/*  14:    */ import org.springframework.core.io.support.ResourcePatternResolver;
/*  15:    */ import org.springframework.util.Assert;
/*  16:    */ 
/*  17:    */ public abstract class AbstractBeanDefinitionReader
/*  18:    */   implements EnvironmentCapable, BeanDefinitionReader
/*  19:    */ {
/*  20: 49 */   protected final Log logger = LogFactory.getLog(getClass());
/*  21:    */   private final BeanDefinitionRegistry registry;
/*  22:    */   private ResourceLoader resourceLoader;
/*  23:    */   private ClassLoader beanClassLoader;
/*  24: 57 */   private Environment environment = new StandardEnvironment();
/*  25: 59 */   private BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();
/*  26:    */   
/*  27:    */   protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry)
/*  28:    */   {
/*  29: 80 */     Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
/*  30: 81 */     this.registry = registry;
/*  31: 84 */     if ((this.registry instanceof ResourceLoader)) {
/*  32: 85 */       this.resourceLoader = ((ResourceLoader)this.registry);
/*  33:    */     } else {
/*  34: 88 */       this.resourceLoader = new PathMatchingResourcePatternResolver();
/*  35:    */     }
/*  36: 92 */     if ((this.registry instanceof EnvironmentCapable)) {
/*  37: 93 */       this.environment = ((EnvironmentCapable)this.registry).getEnvironment();
/*  38:    */     } else {
/*  39: 96 */       this.environment = new StandardEnvironment();
/*  40:    */     }
/*  41:    */   }
/*  42:    */   
/*  43:    */   public final BeanDefinitionRegistry getBeanFactory()
/*  44:    */   {
/*  45:102 */     return this.registry;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public final BeanDefinitionRegistry getRegistry()
/*  49:    */   {
/*  50:106 */     return this.registry;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setResourceLoader(ResourceLoader resourceLoader)
/*  54:    */   {
/*  55:121 */     this.resourceLoader = resourceLoader;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public ResourceLoader getResourceLoader()
/*  59:    */   {
/*  60:125 */     return this.resourceLoader;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setBeanClassLoader(ClassLoader beanClassLoader)
/*  64:    */   {
/*  65:136 */     this.beanClassLoader = beanClassLoader;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public ClassLoader getBeanClassLoader()
/*  69:    */   {
/*  70:140 */     return this.beanClassLoader;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void setEnvironment(Environment environment)
/*  74:    */   {
/*  75:149 */     this.environment = environment;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public Environment getEnvironment()
/*  79:    */   {
/*  80:153 */     return this.environment;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator)
/*  84:    */   {
/*  85:162 */     this.beanNameGenerator = (beanNameGenerator != null ? beanNameGenerator : new DefaultBeanNameGenerator());
/*  86:    */   }
/*  87:    */   
/*  88:    */   public BeanNameGenerator getBeanNameGenerator()
/*  89:    */   {
/*  90:166 */     return this.beanNameGenerator;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public int loadBeanDefinitions(Resource... resources)
/*  94:    */     throws BeanDefinitionStoreException
/*  95:    */   {
/*  96:171 */     Assert.notNull(resources, "Resource array must not be null");
/*  97:172 */     int counter = 0;
/*  98:173 */     for (Resource resource : resources) {
/*  99:174 */       counter += loadBeanDefinitions(resource);
/* 100:    */     }
/* 101:176 */     return counter;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public int loadBeanDefinitions(String location)
/* 105:    */     throws BeanDefinitionStoreException
/* 106:    */   {
/* 107:180 */     return loadBeanDefinitions(location, null);
/* 108:    */   }
/* 109:    */   
/* 110:    */   public int loadBeanDefinitions(String location, Set<Resource> actualResources)
/* 111:    */     throws BeanDefinitionStoreException
/* 112:    */   {
/* 113:199 */     ResourceLoader resourceLoader = getResourceLoader();
/* 114:200 */     if (resourceLoader == null) {
/* 115:201 */       throw new BeanDefinitionStoreException(
/* 116:202 */         "Cannot import bean definitions from location [" + location + "]: no ResourceLoader available");
/* 117:    */     }
/* 118:205 */     if ((resourceLoader instanceof ResourcePatternResolver)) {
/* 119:    */       try
/* 120:    */       {
/* 121:208 */         Resource[] resources = ((ResourcePatternResolver)resourceLoader).getResources(location);
/* 122:209 */         int loadCount = loadBeanDefinitions(resources);
/* 123:210 */         if (actualResources != null) {
/* 124:211 */           for (Resource resource : resources) {
/* 125:212 */             actualResources.add(resource);
/* 126:    */           }
/* 127:    */         }
/* 128:215 */         if (this.logger.isDebugEnabled()) {
/* 129:216 */           this.logger.debug("Loaded " + loadCount + " bean definitions from location pattern [" + location + "]");
/* 130:    */         }
/* 131:218 */         return loadCount;
/* 132:    */       }
/* 133:    */       catch (IOException ex)
/* 134:    */       {
/* 135:221 */         throw new BeanDefinitionStoreException(
/* 136:222 */           "Could not resolve bean definition resource pattern [" + location + "]", ex);
/* 137:    */       }
/* 138:    */     }
/* 139:227 */     Resource resource = resourceLoader.getResource(location);
/* 140:228 */     int loadCount = loadBeanDefinitions(resource);
/* 141:229 */     if (actualResources != null) {
/* 142:230 */       actualResources.add(resource);
/* 143:    */     }
/* 144:232 */     if (this.logger.isDebugEnabled()) {
/* 145:233 */       this.logger.debug("Loaded " + loadCount + " bean definitions from location [" + location + "]");
/* 146:    */     }
/* 147:235 */     return loadCount;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public int loadBeanDefinitions(String... locations)
/* 151:    */     throws BeanDefinitionStoreException
/* 152:    */   {
/* 153:240 */     Assert.notNull(locations, "Location array must not be null");
/* 154:241 */     int counter = 0;
/* 155:242 */     for (String location : locations) {
/* 156:243 */       counter += loadBeanDefinitions(location);
/* 157:    */     }
/* 158:245 */     return counter;
/* 159:    */   }
/* 160:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.AbstractBeanDefinitionReader
 * JD-Core Version:    0.7.0.1
 */