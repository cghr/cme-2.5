/*   1:    */ package org.springframework.beans.factory.xml;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.Map;
/*   5:    */ import java.util.Properties;
/*   6:    */ import java.util.concurrent.ConcurrentHashMap;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.apache.commons.logging.LogFactory;
/*   9:    */ import org.springframework.beans.BeanUtils;
/*  10:    */ import org.springframework.beans.FatalBeanException;
/*  11:    */ import org.springframework.core.io.support.PropertiesLoaderUtils;
/*  12:    */ import org.springframework.util.Assert;
/*  13:    */ import org.springframework.util.ClassUtils;
/*  14:    */ import org.springframework.util.CollectionUtils;
/*  15:    */ 
/*  16:    */ public class DefaultNamespaceHandlerResolver
/*  17:    */   implements NamespaceHandlerResolver
/*  18:    */ {
/*  19:    */   public static final String DEFAULT_HANDLER_MAPPINGS_LOCATION = "META-INF/spring.handlers";
/*  20: 58 */   protected final Log logger = LogFactory.getLog(getClass());
/*  21:    */   private final ClassLoader classLoader;
/*  22:    */   private final String handlerMappingsLocation;
/*  23:    */   private volatile Map<String, Object> handlerMappings;
/*  24:    */   
/*  25:    */   public DefaultNamespaceHandlerResolver()
/*  26:    */   {
/*  27: 78 */     this(null, "META-INF/spring.handlers");
/*  28:    */   }
/*  29:    */   
/*  30:    */   public DefaultNamespaceHandlerResolver(ClassLoader classLoader)
/*  31:    */   {
/*  32: 89 */     this(classLoader, "META-INF/spring.handlers");
/*  33:    */   }
/*  34:    */   
/*  35:    */   public DefaultNamespaceHandlerResolver(ClassLoader classLoader, String handlerMappingsLocation)
/*  36:    */   {
/*  37:100 */     Assert.notNull(handlerMappingsLocation, "Handler mappings location must not be null");
/*  38:101 */     this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
/*  39:102 */     this.handlerMappingsLocation = handlerMappingsLocation;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public NamespaceHandler resolve(String namespaceUri)
/*  43:    */   {
/*  44:113 */     Map<String, Object> handlerMappings = getHandlerMappings();
/*  45:114 */     Object handlerOrClassName = handlerMappings.get(namespaceUri);
/*  46:115 */     if (handlerOrClassName == null) {
/*  47:116 */       return null;
/*  48:    */     }
/*  49:118 */     if ((handlerOrClassName instanceof NamespaceHandler)) {
/*  50:119 */       return (NamespaceHandler)handlerOrClassName;
/*  51:    */     }
/*  52:122 */     String className = (String)handlerOrClassName;
/*  53:    */     try
/*  54:    */     {
/*  55:124 */       Class<?> handlerClass = ClassUtils.forName(className, this.classLoader);
/*  56:125 */       if (!NamespaceHandler.class.isAssignableFrom(handlerClass)) {
/*  57:126 */         throw new FatalBeanException("Class [" + className + "] for namespace [" + namespaceUri + 
/*  58:127 */           "] does not implement the [" + NamespaceHandler.class.getName() + "] interface");
/*  59:    */       }
/*  60:129 */       NamespaceHandler namespaceHandler = (NamespaceHandler)BeanUtils.instantiateClass(handlerClass);
/*  61:130 */       namespaceHandler.init();
/*  62:131 */       handlerMappings.put(namespaceUri, namespaceHandler);
/*  63:132 */       return namespaceHandler;
/*  64:    */     }
/*  65:    */     catch (ClassNotFoundException ex)
/*  66:    */     {
/*  67:135 */       throw new FatalBeanException("NamespaceHandler class [" + className + "] for namespace [" + 
/*  68:136 */         namespaceUri + "] not found", ex);
/*  69:    */     }
/*  70:    */     catch (LinkageError err)
/*  71:    */     {
/*  72:139 */       throw new FatalBeanException("Invalid NamespaceHandler class [" + className + "] for namespace [" + 
/*  73:140 */         namespaceUri + "]: problem with handler class file or dependent class", err);
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   private Map<String, Object> getHandlerMappings()
/*  78:    */   {
/*  79:149 */     if (this.handlerMappings == null) {
/*  80:150 */       synchronized (this)
/*  81:    */       {
/*  82:151 */         if (this.handlerMappings == null) {
/*  83:    */           try
/*  84:    */           {
/*  85:153 */             Properties mappings = 
/*  86:154 */               PropertiesLoaderUtils.loadAllProperties(this.handlerMappingsLocation, this.classLoader);
/*  87:155 */             if (this.logger.isDebugEnabled()) {
/*  88:156 */               this.logger.debug("Loaded NamespaceHandler mappings: " + mappings);
/*  89:    */             }
/*  90:158 */             Map<String, Object> handlerMappings = new ConcurrentHashMap();
/*  91:159 */             CollectionUtils.mergePropertiesIntoMap(mappings, handlerMappings);
/*  92:160 */             this.handlerMappings = handlerMappings;
/*  93:    */           }
/*  94:    */           catch (IOException ex)
/*  95:    */           {
/*  96:163 */             throw new IllegalStateException(
/*  97:164 */               "Unable to load NamespaceHandler mappings from location [" + this.handlerMappingsLocation + "]", ex);
/*  98:    */           }
/*  99:    */         }
/* 100:    */       }
/* 101:    */     }
/* 102:169 */     return this.handlerMappings;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public String toString()
/* 106:    */   {
/* 107:175 */     return "NamespaceHandlerResolver using mappings " + getHandlerMappings();
/* 108:    */   }
/* 109:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.DefaultNamespaceHandlerResolver
 * JD-Core Version:    0.7.0.1
 */