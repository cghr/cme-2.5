/*   1:    */ package org.springframework.remoting.jaxws;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.Collection;
/*   6:    */ import java.util.LinkedHashSet;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Set;
/*   9:    */ import java.util.concurrent.Executor;
/*  10:    */ import javax.jws.WebService;
/*  11:    */ import javax.xml.ws.Endpoint;
/*  12:    */ import javax.xml.ws.WebServiceFeature;
/*  13:    */ import javax.xml.ws.WebServiceProvider;
/*  14:    */ import org.springframework.beans.BeanUtils;
/*  15:    */ import org.springframework.beans.factory.BeanFactory;
/*  16:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*  17:    */ import org.springframework.beans.factory.CannotLoadBeanClassException;
/*  18:    */ import org.springframework.beans.factory.DisposableBean;
/*  19:    */ import org.springframework.beans.factory.InitializingBean;
/*  20:    */ import org.springframework.beans.factory.ListableBeanFactory;
/*  21:    */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*  22:    */ import org.springframework.util.Assert;
/*  23:    */ import org.springframework.util.ClassUtils;
/*  24:    */ import org.springframework.util.ReflectionUtils;
/*  25:    */ 
/*  26:    */ public abstract class AbstractJaxWsServiceExporter
/*  27:    */   implements BeanFactoryAware, InitializingBean, DisposableBean
/*  28:    */ {
/*  29:    */   private Map<String, Object> endpointProperties;
/*  30:    */   private Executor executor;
/*  31:    */   private String bindingType;
/*  32:    */   private Object[] webServiceFeatures;
/*  33:    */   private ListableBeanFactory beanFactory;
/*  34: 69 */   private final Set<Endpoint> publishedEndpoints = new LinkedHashSet();
/*  35:    */   
/*  36:    */   public void setEndpointProperties(Map<String, Object> endpointProperties)
/*  37:    */   {
/*  38: 80 */     this.endpointProperties = endpointProperties;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setExecutor(Executor executor)
/*  42:    */   {
/*  43: 89 */     this.executor = executor;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setBindingType(String bindingType)
/*  47:    */   {
/*  48: 97 */     this.bindingType = bindingType;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setWebServiceFeatures(Object[] webServiceFeatures)
/*  52:    */   {
/*  53:106 */     this.webServiceFeatures = webServiceFeatures;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setBeanFactory(BeanFactory beanFactory)
/*  57:    */   {
/*  58:113 */     if (!(beanFactory instanceof ListableBeanFactory)) {
/*  59:114 */       throw new IllegalStateException(getClass().getSimpleName() + " requires a ListableBeanFactory");
/*  60:    */     }
/*  61:116 */     this.beanFactory = ((ListableBeanFactory)beanFactory);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void afterPropertiesSet()
/*  65:    */     throws Exception
/*  66:    */   {
/*  67:125 */     publishEndpoints();
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void publishEndpoints()
/*  71:    */   {
/*  72:134 */     Set<String> beanNames = new LinkedHashSet(this.beanFactory.getBeanDefinitionCount());
/*  73:135 */     beanNames.addAll((Collection)Arrays.asList(this.beanFactory.getBeanDefinitionNames()));
/*  74:136 */     if ((this.beanFactory instanceof ConfigurableBeanFactory)) {
/*  75:137 */       beanNames.addAll((Collection)Arrays.asList(((ConfigurableBeanFactory)this.beanFactory).getSingletonNames()));
/*  76:    */     }
/*  77:139 */     for (String beanName : beanNames) {
/*  78:    */       try
/*  79:    */       {
/*  80:141 */         Class<?> type = this.beanFactory.getType(beanName);
/*  81:142 */         if ((type != null) && (!type.isInterface()))
/*  82:    */         {
/*  83:143 */           WebService wsAnnotation = (WebService)type.getAnnotation(WebService.class);
/*  84:144 */           WebServiceProvider wsProviderAnnotation = (WebServiceProvider)type.getAnnotation(WebServiceProvider.class);
/*  85:145 */           if ((wsAnnotation != null) || (wsProviderAnnotation != null))
/*  86:    */           {
/*  87:146 */             Endpoint endpoint = createEndpoint(this.beanFactory.getBean(beanName));
/*  88:147 */             if (this.endpointProperties != null) {
/*  89:148 */               endpoint.setProperties(this.endpointProperties);
/*  90:    */             }
/*  91:150 */             if (this.executor != null) {
/*  92:151 */               endpoint.setExecutor(this.executor);
/*  93:    */             }
/*  94:153 */             if (wsAnnotation != null) {
/*  95:154 */               publishEndpoint(endpoint, wsAnnotation);
/*  96:    */             } else {
/*  97:157 */               publishEndpoint(endpoint, wsProviderAnnotation);
/*  98:    */             }
/*  99:159 */             this.publishedEndpoints.add(endpoint);
/* 100:    */           }
/* 101:    */         }
/* 102:    */       }
/* 103:    */       catch (CannotLoadBeanClassException localCannotLoadBeanClassException) {}
/* 104:    */     }
/* 105:    */   }
/* 106:    */   
/* 107:    */   protected Endpoint createEndpoint(Object bean)
/* 108:    */   {
/* 109:177 */     if (this.webServiceFeatures != null) {
/* 110:178 */       return new FeatureEndpointProvider(null).createEndpoint(this.bindingType, bean, this.webServiceFeatures);
/* 111:    */     }
/* 112:181 */     return Endpoint.create(this.bindingType, bean);
/* 113:    */   }
/* 114:    */   
/* 115:    */   protected abstract void publishEndpoint(Endpoint paramEndpoint, WebService paramWebService);
/* 116:    */   
/* 117:    */   protected abstract void publishEndpoint(Endpoint paramEndpoint, WebServiceProvider paramWebServiceProvider);
/* 118:    */   
/* 119:    */   public void destroy()
/* 120:    */   {
/* 121:205 */     for (Endpoint endpoint : this.publishedEndpoints) {
/* 122:206 */       endpoint.stop();
/* 123:    */     }
/* 124:    */   }
/* 125:    */   
/* 126:    */   private class FeatureEndpointProvider
/* 127:    */   {
/* 128:    */     private FeatureEndpointProvider() {}
/* 129:    */     
/* 130:    */     public Endpoint createEndpoint(String bindingType, Object implementor, Object[] features)
/* 131:    */     {
/* 132:218 */       WebServiceFeature[] wsFeatures = new WebServiceFeature[features.length];
/* 133:219 */       for (int i = 0; i < features.length; i++) {
/* 134:220 */         wsFeatures[i] = convertWebServiceFeature(features[i]);
/* 135:    */       }
/* 136:    */       try
/* 137:    */       {
/* 138:223 */         Method create = Endpoint.class.getMethod("create", new Class[] { String.class, Object.class, [Ljavax.xml.ws.WebServiceFeature.class });
/* 139:224 */         return (Endpoint)ReflectionUtils.invokeMethod(create, null, new Object[] { bindingType, implementor, wsFeatures });
/* 140:    */       }
/* 141:    */       catch (NoSuchMethodException ex)
/* 142:    */       {
/* 143:227 */         throw new IllegalStateException("JAX-WS 2.2 not available - cannot create feature endpoints", ex);
/* 144:    */       }
/* 145:    */     }
/* 146:    */     
/* 147:    */     private WebServiceFeature convertWebServiceFeature(Object feature)
/* 148:    */     {
/* 149:232 */       Assert.notNull(feature, "WebServiceFeature specification object must not be null");
/* 150:233 */       if ((feature instanceof WebServiceFeature)) {
/* 151:234 */         return (WebServiceFeature)feature;
/* 152:    */       }
/* 153:236 */       if ((feature instanceof Class)) {
/* 154:237 */         return (WebServiceFeature)BeanUtils.instantiate((Class)feature);
/* 155:    */       }
/* 156:239 */       if ((feature instanceof String)) {
/* 157:    */         try
/* 158:    */         {
/* 159:241 */           Class<?> featureClass = getBeanClassLoader().loadClass((String)feature);
/* 160:242 */           return (WebServiceFeature)BeanUtils.instantiate(featureClass);
/* 161:    */         }
/* 162:    */         catch (ClassNotFoundException localClassNotFoundException)
/* 163:    */         {
/* 164:245 */           throw new IllegalArgumentException("Could not load WebServiceFeature class [" + feature + "]");
/* 165:    */         }
/* 166:    */       }
/* 167:249 */       throw new IllegalArgumentException("Unknown WebServiceFeature specification type: " + feature.getClass());
/* 168:    */     }
/* 169:    */     
/* 170:    */     private ClassLoader getBeanClassLoader()
/* 171:    */     {
/* 172:254 */       return (AbstractJaxWsServiceExporter.this.beanFactory instanceof ConfigurableBeanFactory) ? 
/* 173:255 */         ((ConfigurableBeanFactory)AbstractJaxWsServiceExporter.this.beanFactory).getBeanClassLoader() : ClassUtils.getDefaultClassLoader();
/* 174:    */     }
/* 175:    */   }
/* 176:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.jaxws.AbstractJaxWsServiceExporter
 * JD-Core Version:    0.7.0.1
 */