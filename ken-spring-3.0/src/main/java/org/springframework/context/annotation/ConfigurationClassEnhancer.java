/*   1:    */ package org.springframework.context.annotation;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.List;
/*   6:    */ import net.sf.cglib.proxy.Callback;
/*   7:    */ import net.sf.cglib.proxy.CallbackFilter;
/*   8:    */ import net.sf.cglib.proxy.Enhancer;
/*   9:    */ import net.sf.cglib.proxy.MethodInterceptor;
/*  10:    */ import net.sf.cglib.proxy.MethodProxy;
/*  11:    */ import net.sf.cglib.proxy.NoOp;
/*  12:    */ import org.apache.commons.logging.Log;
/*  13:    */ import org.apache.commons.logging.LogFactory;
/*  14:    */ import org.springframework.aop.scope.ScopedProxyFactoryBean;
/*  15:    */ import org.springframework.beans.factory.DisposableBean;
/*  16:    */ import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
/*  17:    */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*  18:    */ import org.springframework.beans.factory.support.SimpleInstantiationStrategy;
/*  19:    */ import org.springframework.core.annotation.AnnotationUtils;
/*  20:    */ import org.springframework.util.Assert;
/*  21:    */ 
/*  22:    */ class ConfigurationClassEnhancer
/*  23:    */ {
/*  24: 54 */   private static final Log logger = LogFactory.getLog(ConfigurationClassEnhancer.class);
/*  25: 56 */   private final List<Callback> callbackInstances = new ArrayList();
/*  26: 58 */   private final List<Class<? extends Callback>> callbackTypes = new ArrayList();
/*  27:    */   private final CallbackFilter callbackFilter;
/*  28:    */   
/*  29:    */   public ConfigurationClassEnhancer(ConfigurableBeanFactory beanFactory)
/*  30:    */   {
/*  31: 67 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/*  32:    */     
/*  33: 69 */     this.callbackInstances.add(new BeanMethodInterceptor(beanFactory));
/*  34: 70 */     this.callbackInstances.add(new DisposableBeanMethodInterceptor(null));
/*  35: 71 */     this.callbackInstances.add(NoOp.INSTANCE);
/*  36: 73 */     for (Callback callback : this.callbackInstances) {
/*  37: 74 */       this.callbackTypes.add(callback.getClass());
/*  38:    */     }
/*  39: 79 */     this.callbackFilter = new CallbackFilter()
/*  40:    */     {
/*  41:    */       public int accept(Method candidateMethod)
/*  42:    */       {
/*  43: 81 */         if (BeanAnnotationHelper.isBeanAnnotated(candidateMethod)) {
/*  44: 82 */           return 0;
/*  45:    */         }
/*  46: 84 */         if (ConfigurationClassEnhancer.DisposableBeanMethodInterceptor.isDestroyMethod(candidateMethod)) {
/*  47: 85 */           return 1;
/*  48:    */         }
/*  49: 87 */         return 2;
/*  50:    */       }
/*  51:    */     };
/*  52:    */   }
/*  53:    */   
/*  54:    */   public Class<?> enhance(Class<?> configClass)
/*  55:    */   {
/*  56: 99 */     Class<?> enhancedClass = createClass(newEnhancer(configClass));
/*  57:100 */     if (logger.isDebugEnabled()) {
/*  58:101 */       logger.debug(
/*  59:102 */         String.format("Successfully enhanced %s; enhanced class name is: %s", new Object[] {configClass.getName(), enhancedClass.getName() }));
/*  60:    */     }
/*  61:104 */     return enhancedClass;
/*  62:    */   }
/*  63:    */   
/*  64:    */   private Enhancer newEnhancer(Class<?> superclass)
/*  65:    */   {
/*  66:111 */     Enhancer enhancer = new Enhancer();
/*  67:    */     
/*  68:    */ 
/*  69:    */ 
/*  70:115 */     enhancer.setUseCache(false);
/*  71:116 */     enhancer.setSuperclass(superclass);
/*  72:117 */     enhancer.setInterfaces(new Class[] { DisposableBean.class });
/*  73:118 */     enhancer.setUseFactory(false);
/*  74:119 */     enhancer.setCallbackFilter(this.callbackFilter);
/*  75:120 */     enhancer.setCallbackTypes((Class[])this.callbackTypes.toArray(new Class[this.callbackTypes.size()]));
/*  76:121 */     return enhancer;
/*  77:    */   }
/*  78:    */   
/*  79:    */   private Class<?> createClass(Enhancer enhancer)
/*  80:    */   {
/*  81:129 */     Class<?> subclass = enhancer.createClass();
/*  82:    */     
/*  83:131 */     Enhancer.registerStaticCallbacks(subclass, (Callback[])this.callbackInstances.toArray(new Callback[this.callbackInstances.size()]));
/*  84:132 */     return subclass;
/*  85:    */   }
/*  86:    */   
/*  87:    */   private static class GetObjectMethodInterceptor
/*  88:    */     implements MethodInterceptor
/*  89:    */   {
/*  90:    */     private final ConfigurableBeanFactory beanFactory;
/*  91:    */     private final String beanName;
/*  92:    */     
/*  93:    */     public GetObjectMethodInterceptor(ConfigurableBeanFactory beanFactory, String beanName)
/*  94:    */     {
/*  95:148 */       this.beanFactory = beanFactory;
/*  96:149 */       this.beanName = beanName;
/*  97:    */     }
/*  98:    */     
/*  99:    */     public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
/* 100:    */       throws Throwable
/* 101:    */     {
/* 102:153 */       return this.beanFactory.getBean(this.beanName);
/* 103:    */     }
/* 104:    */   }
/* 105:    */   
/* 106:    */   private static class DisposableBeanMethodInterceptor
/* 107:    */     implements MethodInterceptor
/* 108:    */   {
/* 109:    */     public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
/* 110:    */       throws Throwable
/* 111:    */     {
/* 112:167 */       Enhancer.registerStaticCallbacks(obj.getClass(), null);
/* 113:170 */       if (DisposableBean.class.isAssignableFrom(obj.getClass().getSuperclass())) {
/* 114:171 */         return proxy.invokeSuper(obj, args);
/* 115:    */       }
/* 116:173 */       return null;
/* 117:    */     }
/* 118:    */     
/* 119:    */     public static boolean isDestroyMethod(Method candidateMethod)
/* 120:    */     {
/* 121:179 */       return (candidateMethod.getName().equals("destroy")) && (candidateMethod.getParameterTypes().length == 0) && (DisposableBean.class.isAssignableFrom(candidateMethod.getDeclaringClass()));
/* 122:    */     }
/* 123:    */   }
/* 124:    */   
/* 125:    */   private static class BeanMethodInterceptor
/* 126:    */     implements MethodInterceptor
/* 127:    */   {
/* 128:    */     private final ConfigurableBeanFactory beanFactory;
/* 129:    */     
/* 130:    */     public BeanMethodInterceptor(ConfigurableBeanFactory beanFactory)
/* 131:    */     {
/* 132:195 */       this.beanFactory = beanFactory;
/* 133:    */     }
/* 134:    */     
/* 135:    */     public Object intercept(Object enhancedConfigInstance, Method beanMethod, Object[] beanMethodArgs, MethodProxy cglibMethodProxy)
/* 136:    */       throws Throwable
/* 137:    */     {
/* 138:209 */       String beanName = BeanAnnotationHelper.determineBeanNameFor(beanMethod);
/* 139:    */       
/* 140:    */ 
/* 141:212 */       Scope scope = (Scope)AnnotationUtils.findAnnotation(beanMethod, Scope.class);
/* 142:213 */       if ((scope != null) && (scope.proxyMode() != ScopedProxyMode.NO))
/* 143:    */       {
/* 144:214 */         String scopedBeanName = ScopedProxyCreator.getTargetBeanName(beanName);
/* 145:215 */         if (this.beanFactory.isCurrentlyInCreation(scopedBeanName)) {
/* 146:216 */           beanName = scopedBeanName;
/* 147:    */         }
/* 148:    */       }
/* 149:227 */       if ((factoryContainsBean('&' + beanName)) && (factoryContainsBean(beanName)))
/* 150:    */       {
/* 151:228 */         Object factoryBean = this.beanFactory.getBean('&' + beanName);
/* 152:229 */         if (!(factoryBean instanceof ScopedProxyFactoryBean)) {
/* 153:234 */           return enhanceFactoryBean(factoryBean.getClass(), beanName);
/* 154:    */         }
/* 155:    */       }
/* 156:238 */       boolean factoryIsCaller = beanMethod.equals(SimpleInstantiationStrategy.getCurrentlyInvokedFactoryMethod());
/* 157:239 */       boolean factoryAlreadyContainsSingleton = this.beanFactory.containsSingleton(beanName);
/* 158:240 */       if ((factoryIsCaller) && (!factoryAlreadyContainsSingleton))
/* 159:    */       {
/* 160:244 */         if (BeanFactoryPostProcessor.class.isAssignableFrom(beanMethod.getReturnType())) {
/* 161:245 */           ConfigurationClassEnhancer.logger.warn(
/* 162:    */           
/* 163:    */ 
/* 164:    */ 
/* 165:    */ 
/* 166:    */ 
/* 167:251 */             String.format("@Bean method %s.%s is non-static and returns an object assignable to Spring's BeanFactoryPostProcessor interface. This will result in a failure to process annotations such as @Autowired, @Resource and @PostConstruct within the method's declaring @Configuration class. Add the 'static' modifier to this method to avoid these container lifecycle issues; see @Bean Javadoc for complete details", new Object[] {beanMethod.getDeclaringClass().getSimpleName(), beanMethod.getName() }));
/* 168:    */         }
/* 169:253 */         return cglibMethodProxy.invokeSuper(enhancedConfigInstance, beanMethodArgs);
/* 170:    */       }
/* 171:260 */       boolean alreadyInCreation = this.beanFactory.isCurrentlyInCreation(beanName);
/* 172:    */       try
/* 173:    */       {
/* 174:262 */         if (alreadyInCreation) {
/* 175:263 */           this.beanFactory.setCurrentlyInCreation(beanName, false);
/* 176:    */         }
/* 177:265 */         return this.beanFactory.getBean(beanName);
/* 178:    */       }
/* 179:    */       finally
/* 180:    */       {
/* 181:267 */         if (alreadyInCreation) {
/* 182:268 */           this.beanFactory.setCurrentlyInCreation(beanName, true);
/* 183:    */         }
/* 184:    */       }
/* 185:    */     }
/* 186:    */     
/* 187:    */     private boolean factoryContainsBean(String beanName)
/* 188:    */     {
/* 189:289 */       boolean containsBean = this.beanFactory.containsBean(beanName);
/* 190:290 */       boolean currentlyInCreation = this.beanFactory.isCurrentlyInCreation(beanName);
/* 191:291 */       return (containsBean) && (!currentlyInCreation);
/* 192:    */     }
/* 193:    */     
/* 194:    */     private Object enhanceFactoryBean(Class<?> fbClass, String beanName)
/* 195:    */       throws InstantiationException, IllegalAccessException
/* 196:    */     {
/* 197:302 */       Enhancer enhancer = new Enhancer();
/* 198:303 */       enhancer.setUseCache(false);
/* 199:304 */       enhancer.setSuperclass(fbClass);
/* 200:305 */       enhancer.setUseFactory(false);
/* 201:306 */       enhancer.setCallbackFilter(new CallbackFilter()
/* 202:    */       {
/* 203:    */         public int accept(Method method)
/* 204:    */         {
/* 205:308 */           return method.getName().equals("getObject") ? 0 : 1;
/* 206:    */         }
/* 207:310 */       });
/* 208:311 */       List<Callback> callbackInstances = new ArrayList();
/* 209:312 */       callbackInstances.add(new ConfigurationClassEnhancer.GetObjectMethodInterceptor(this.beanFactory, beanName));
/* 210:313 */       callbackInstances.add(NoOp.INSTANCE);
/* 211:    */       
/* 212:315 */       List<Class<? extends Callback>> callbackTypes = new ArrayList();
/* 213:316 */       for (Callback callback : callbackInstances) {
/* 214:317 */         callbackTypes.add(callback.getClass());
/* 215:    */       }
/* 216:320 */       enhancer.setCallbackTypes((Class[])callbackTypes.toArray(new Class[callbackTypes.size()]));
/* 217:321 */       Class<?> fbSubclass = enhancer.createClass();
/* 218:322 */       Enhancer.registerCallbacks(fbSubclass, (Callback[])callbackInstances.toArray(new Callback[callbackInstances.size()]));
/* 219:323 */       return fbSubclass.newInstance();
/* 220:    */     }
/* 221:    */   }
/* 222:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.ConfigurationClassEnhancer
 * JD-Core Version:    0.7.0.1
 */