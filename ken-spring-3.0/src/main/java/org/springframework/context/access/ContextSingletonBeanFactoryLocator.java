/*   1:    */ package org.springframework.context.access;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import org.apache.commons.logging.Log;
/*   6:    */ import org.springframework.beans.BeansException;
/*   7:    */ import org.springframework.beans.factory.BeanFactory;
/*   8:    */ import org.springframework.beans.factory.access.BeanFactoryLocator;
/*   9:    */ import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;
/*  10:    */ import org.springframework.context.ConfigurableApplicationContext;
/*  11:    */ import org.springframework.context.support.ClassPathXmlApplicationContext;
/*  12:    */ import org.springframework.core.io.support.ResourcePatternUtils;
/*  13:    */ 
/*  14:    */ public class ContextSingletonBeanFactoryLocator
/*  15:    */   extends SingletonBeanFactoryLocator
/*  16:    */ {
/*  17:    */   private static final String DEFAULT_RESOURCE_LOCATION = "classpath*:beanRefContext.xml";
/*  18: 57 */   private static final Map<String, BeanFactoryLocator> instances = new HashMap();
/*  19:    */   
/*  20:    */   public static BeanFactoryLocator getInstance()
/*  21:    */     throws BeansException
/*  22:    */   {
/*  23: 69 */     return getInstance(null);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public static BeanFactoryLocator getInstance(String selector)
/*  27:    */     throws BeansException
/*  28:    */   {
/*  29: 88 */     String resourceLocation = selector;
/*  30: 89 */     if (resourceLocation == null) {
/*  31: 90 */       resourceLocation = "classpath*:beanRefContext.xml";
/*  32:    */     }
/*  33: 95 */     if (!ResourcePatternUtils.isUrl(resourceLocation)) {
/*  34: 96 */       resourceLocation = "classpath*:" + resourceLocation;
/*  35:    */     }
/*  36: 99 */     synchronized (instances)
/*  37:    */     {
/*  38:100 */       if (logger.isTraceEnabled()) {
/*  39:101 */         logger.trace("ContextSingletonBeanFactoryLocator.getInstance(): instances.hashCode=" + 
/*  40:102 */           instances.hashCode() + ", instances=" + instances);
/*  41:    */       }
/*  42:104 */       BeanFactoryLocator bfl = (BeanFactoryLocator)instances.get(resourceLocation);
/*  43:105 */       if (bfl == null)
/*  44:    */       {
/*  45:106 */         bfl = new ContextSingletonBeanFactoryLocator(resourceLocation);
/*  46:107 */         instances.put(resourceLocation, bfl);
/*  47:    */       }
/*  48:109 */       return bfl;
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   protected ContextSingletonBeanFactoryLocator(String resourceLocation)
/*  53:    */   {
/*  54:121 */     super(resourceLocation);
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected BeanFactory createDefinition(String resourceLocation, String factoryKey)
/*  58:    */   {
/*  59:133 */     return new ClassPathXmlApplicationContext(new String[] { resourceLocation }, false);
/*  60:    */   }
/*  61:    */   
/*  62:    */   protected void initializeDefinition(BeanFactory groupDef)
/*  63:    */   {
/*  64:142 */     if ((groupDef instanceof ConfigurableApplicationContext)) {
/*  65:143 */       ((ConfigurableApplicationContext)groupDef).refresh();
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected void destroyDefinition(BeanFactory groupDef, String selector)
/*  70:    */   {
/*  71:153 */     if ((groupDef instanceof ConfigurableApplicationContext))
/*  72:    */     {
/*  73:154 */       if (logger.isTraceEnabled()) {
/*  74:155 */         logger.trace("Context group with selector '" + selector + 
/*  75:156 */           "' being released, as there are no more references to it");
/*  76:    */       }
/*  77:158 */       ((ConfigurableApplicationContext)groupDef).close();
/*  78:    */     }
/*  79:    */   }
/*  80:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.access.ContextSingletonBeanFactoryLocator
 * JD-Core Version:    0.7.0.1
 */