/*   1:    */ package org.springframework.beans.factory.access;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Map;
/*   6:    */ import org.apache.commons.logging.Log;
/*   7:    */ import org.apache.commons.logging.LogFactory;
/*   8:    */ import org.springframework.beans.BeansException;
/*   9:    */ import org.springframework.beans.FatalBeanException;
/*  10:    */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*  11:    */ import org.springframework.beans.factory.BeanFactory;
/*  12:    */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*  13:    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*  14:    */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*  15:    */ import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
/*  16:    */ import org.springframework.core.io.Resource;
/*  17:    */ import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
/*  18:    */ import org.springframework.core.io.support.ResourcePatternResolver;
/*  19:    */ import org.springframework.core.io.support.ResourcePatternUtils;
/*  20:    */ 
/*  21:    */ public class SingletonBeanFactoryLocator
/*  22:    */   implements BeanFactoryLocator
/*  23:    */ {
/*  24:    */   private static final String DEFAULT_RESOURCE_LOCATION = "classpath*:beanRefFactory.xml";
/*  25:274 */   protected static final Log logger = LogFactory.getLog(SingletonBeanFactoryLocator.class);
/*  26:277 */   private static final Map<String, BeanFactoryLocator> instances = new HashMap();
/*  27:    */   
/*  28:    */   public static BeanFactoryLocator getInstance()
/*  29:    */     throws BeansException
/*  30:    */   {
/*  31:289 */     return getInstance(null);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public static BeanFactoryLocator getInstance(String selector)
/*  35:    */     throws BeansException
/*  36:    */   {
/*  37:308 */     String resourceLocation = selector;
/*  38:309 */     if (resourceLocation == null) {
/*  39:310 */       resourceLocation = "classpath*:beanRefFactory.xml";
/*  40:    */     }
/*  41:315 */     if (!ResourcePatternUtils.isUrl(resourceLocation)) {
/*  42:316 */       resourceLocation = "classpath*:" + resourceLocation;
/*  43:    */     }
/*  44:319 */     synchronized (instances)
/*  45:    */     {
/*  46:320 */       if (logger.isTraceEnabled()) {
/*  47:321 */         logger.trace("SingletonBeanFactoryLocator.getInstance(): instances.hashCode=" + 
/*  48:322 */           instances.hashCode() + ", instances=" + instances);
/*  49:    */       }
/*  50:324 */       BeanFactoryLocator bfl = (BeanFactoryLocator)instances.get(resourceLocation);
/*  51:325 */       if (bfl == null)
/*  52:    */       {
/*  53:326 */         bfl = new SingletonBeanFactoryLocator(resourceLocation);
/*  54:327 */         instances.put(resourceLocation, bfl);
/*  55:    */       }
/*  56:329 */       return bfl;
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:335 */   private final Map<String, BeanFactoryGroup> bfgInstancesByKey = new HashMap();
/*  61:337 */   private final Map<BeanFactory, BeanFactoryGroup> bfgInstancesByObj = new HashMap();
/*  62:    */   private final String resourceLocation;
/*  63:    */   
/*  64:    */   protected SingletonBeanFactoryLocator(String resourceLocation)
/*  65:    */   {
/*  66:349 */     this.resourceLocation = resourceLocation;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public BeanFactoryReference useBeanFactory(String factoryKey)
/*  70:    */     throws BeansException
/*  71:    */   {
/*  72:353 */     synchronized (this.bfgInstancesByKey)
/*  73:    */     {
/*  74:354 */       BeanFactoryGroup bfg = (BeanFactoryGroup)this.bfgInstancesByKey.get(this.resourceLocation);
/*  75:356 */       if (bfg != null)
/*  76:    */       {
/*  77:357 */         bfg.refCount += 1;
/*  78:    */       }
/*  79:    */       else
/*  80:    */       {
/*  81:361 */         if (logger.isTraceEnabled()) {
/*  82:362 */           logger.trace("Factory group with resource name [" + this.resourceLocation + 
/*  83:363 */             "] requested. Creating new instance.");
/*  84:    */         }
/*  85:367 */         BeanFactory groupContext = createDefinition(this.resourceLocation, factoryKey);
/*  86:    */         
/*  87:    */ 
/*  88:370 */         bfg = new BeanFactoryGroup(null);
/*  89:371 */         bfg.definition = groupContext;
/*  90:372 */         bfg.refCount = 1;
/*  91:373 */         this.bfgInstancesByKey.put(this.resourceLocation, bfg);
/*  92:374 */         this.bfgInstancesByObj.put(groupContext, bfg);
/*  93:    */         try
/*  94:    */         {
/*  95:381 */           initializeDefinition(groupContext);
/*  96:    */         }
/*  97:    */         catch (BeansException ex)
/*  98:    */         {
/*  99:384 */           this.bfgInstancesByKey.remove(this.resourceLocation);
/* 100:385 */           this.bfgInstancesByObj.remove(groupContext);
/* 101:386 */           throw new BootstrapException("Unable to initialize group definition. Group resource name [" + 
/* 102:387 */             this.resourceLocation + "], factory key [" + factoryKey + "]", ex);
/* 103:    */         }
/* 104:    */       }
/* 105:    */       try
/* 106:    */       {
/* 107:    */         BeanFactory beanFactory;
/* 108:    */         BeanFactory beanFactory;
/* 109:393 */         if (factoryKey != null) {
/* 110:394 */           beanFactory = (BeanFactory)bfg.definition.getBean(factoryKey, BeanFactory.class);
/* 111:    */         } else {
/* 112:397 */           beanFactory = (BeanFactory)bfg.definition.getBean(BeanFactory.class);
/* 113:    */         }
/* 114:399 */         return new CountingBeanFactoryReference(beanFactory, bfg.definition);
/* 115:    */       }
/* 116:    */       catch (BeansException ex)
/* 117:    */       {
/* 118:402 */         throw new BootstrapException("Unable to return specified BeanFactory instance: factory key [" + 
/* 119:403 */           factoryKey + "], from group with resource name [" + this.resourceLocation + "]", ex);
/* 120:    */       }
/* 121:    */     }
/* 122:    */   }
/* 123:    */   
/* 124:    */   protected BeanFactory createDefinition(String resourceLocation, String factoryKey)
/* 125:    */   {
/* 126:426 */     DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
/* 127:427 */     XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
/* 128:428 */     ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
/* 129:    */     try
/* 130:    */     {
/* 131:431 */       Resource[] configResources = resourcePatternResolver.getResources(resourceLocation);
/* 132:432 */       if (configResources.length == 0) {
/* 133:433 */         throw new FatalBeanException("Unable to find resource for specified definition. Group resource name [" + 
/* 134:434 */           this.resourceLocation + "], factory key [" + factoryKey + "]");
/* 135:    */       }
/* 136:436 */       reader.loadBeanDefinitions(configResources);
/* 137:    */     }
/* 138:    */     catch (IOException ex)
/* 139:    */     {
/* 140:439 */       throw new BeanDefinitionStoreException(
/* 141:440 */         "Error accessing bean definition resource [" + this.resourceLocation + "]", ex);
/* 142:    */     }
/* 143:    */     catch (BeanDefinitionStoreException ex)
/* 144:    */     {
/* 145:443 */       throw new FatalBeanException("Unable to load group definition: group resource name [" + 
/* 146:444 */         this.resourceLocation + "], factory key [" + factoryKey + "]", ex);
/* 147:    */     }
/* 148:447 */     return factory;
/* 149:    */   }
/* 150:    */   
/* 151:    */   protected void initializeDefinition(BeanFactory groupDef)
/* 152:    */   {
/* 153:457 */     if ((groupDef instanceof ConfigurableListableBeanFactory)) {
/* 154:458 */       ((ConfigurableListableBeanFactory)groupDef).preInstantiateSingletons();
/* 155:    */     }
/* 156:    */   }
/* 157:    */   
/* 158:    */   protected void destroyDefinition(BeanFactory groupDef, String selector)
/* 159:    */   {
/* 160:468 */     if ((groupDef instanceof ConfigurableBeanFactory))
/* 161:    */     {
/* 162:469 */       if (logger.isTraceEnabled()) {
/* 163:470 */         logger.trace("Factory group with selector '" + selector + 
/* 164:471 */           "' being released, as there are no more references to it");
/* 165:    */       }
/* 166:473 */       ((ConfigurableBeanFactory)groupDef).destroySingletons();
/* 167:    */     }
/* 168:    */   }
/* 169:    */   
/* 170:    */   private static class BeanFactoryGroup
/* 171:    */   {
/* 172:    */     private BeanFactory definition;
/* 173:485 */     private int refCount = 0;
/* 174:    */   }
/* 175:    */   
/* 176:    */   private class CountingBeanFactoryReference
/* 177:    */     implements BeanFactoryReference
/* 178:    */   {
/* 179:    */     private BeanFactory beanFactory;
/* 180:    */     private BeanFactory groupContextRef;
/* 181:    */     
/* 182:    */     public CountingBeanFactoryReference(BeanFactory beanFactory, BeanFactory groupContext)
/* 183:    */     {
/* 184:499 */       this.beanFactory = beanFactory;
/* 185:500 */       this.groupContextRef = groupContext;
/* 186:    */     }
/* 187:    */     
/* 188:    */     public BeanFactory getFactory()
/* 189:    */     {
/* 190:504 */       return this.beanFactory;
/* 191:    */     }
/* 192:    */     
/* 193:    */     public void release()
/* 194:    */       throws FatalBeanException
/* 195:    */     {
/* 196:509 */       synchronized (SingletonBeanFactoryLocator.this.bfgInstancesByKey)
/* 197:    */       {
/* 198:510 */         BeanFactory savedRef = this.groupContextRef;
/* 199:511 */         if (savedRef != null)
/* 200:    */         {
/* 201:512 */           this.groupContextRef = null;
/* 202:513 */           SingletonBeanFactoryLocator.BeanFactoryGroup bfg = (SingletonBeanFactoryLocator.BeanFactoryGroup)SingletonBeanFactoryLocator.this.bfgInstancesByObj.get(savedRef);
/* 203:514 */           if (bfg != null)
/* 204:    */           {
/* 205:515 */             bfg.refCount -= 1;
/* 206:516 */             if (bfg.refCount == 0)
/* 207:    */             {
/* 208:517 */               SingletonBeanFactoryLocator.this.destroyDefinition(savedRef, SingletonBeanFactoryLocator.this.resourceLocation);
/* 209:518 */               SingletonBeanFactoryLocator.this.bfgInstancesByKey.remove(SingletonBeanFactoryLocator.this.resourceLocation);
/* 210:519 */               SingletonBeanFactoryLocator.this.bfgInstancesByObj.remove(savedRef);
/* 211:    */             }
/* 212:    */           }
/* 213:    */           else
/* 214:    */           {
/* 215:524 */             SingletonBeanFactoryLocator.logger.warn("Tried to release a SingletonBeanFactoryLocator group definition more times than it has actually been used. Resource name [" + 
/* 216:525 */               SingletonBeanFactoryLocator.this.resourceLocation + "]");
/* 217:    */           }
/* 218:    */         }
/* 219:    */       }
/* 220:    */     }
/* 221:    */   }
/* 222:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.access.SingletonBeanFactoryLocator
 * JD-Core Version:    0.7.0.1
 */