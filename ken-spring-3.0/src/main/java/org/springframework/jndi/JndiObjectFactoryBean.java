/*   1:    */ package org.springframework.jndi;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.lang.reflect.Modifier;
/*   5:    */ import javax.naming.Context;
/*   6:    */ import javax.naming.NamingException;
/*   7:    */ import org.aopalliance.intercept.MethodInterceptor;
/*   8:    */ import org.aopalliance.intercept.MethodInvocation;
/*   9:    */ import org.apache.commons.logging.Log;
/*  10:    */ import org.springframework.aop.framework.ProxyFactory;
/*  11:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  12:    */ import org.springframework.beans.factory.FactoryBean;
/*  13:    */ import org.springframework.util.ClassUtils;
/*  14:    */ 
/*  15:    */ public class JndiObjectFactoryBean
/*  16:    */   extends JndiObjectLocator
/*  17:    */   implements FactoryBean<Object>, BeanClassLoaderAware
/*  18:    */ {
/*  19:    */   private Class[] proxyInterfaces;
/*  20: 68 */   private boolean lookupOnStartup = true;
/*  21: 70 */   private boolean cache = true;
/*  22: 72 */   private boolean exposeAccessContext = false;
/*  23:    */   private Object defaultObject;
/*  24: 76 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*  25:    */   private Object jndiObject;
/*  26:    */   
/*  27:    */   public void setProxyInterface(Class proxyInterface)
/*  28:    */   {
/*  29: 91 */     this.proxyInterfaces = new Class[] { proxyInterface };
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setProxyInterfaces(Class[] proxyInterfaces)
/*  33:    */   {
/*  34:104 */     this.proxyInterfaces = proxyInterfaces;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setLookupOnStartup(boolean lookupOnStartup)
/*  38:    */   {
/*  39:116 */     this.lookupOnStartup = lookupOnStartup;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setCache(boolean cache)
/*  43:    */   {
/*  44:129 */     this.cache = cache;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setExposeAccessContext(boolean exposeAccessContext)
/*  48:    */   {
/*  49:142 */     this.exposeAccessContext = exposeAccessContext;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setDefaultObject(Object defaultObject)
/*  53:    */   {
/*  54:155 */     this.defaultObject = defaultObject;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setBeanClassLoader(ClassLoader classLoader)
/*  58:    */   {
/*  59:159 */     this.beanClassLoader = classLoader;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void afterPropertiesSet()
/*  63:    */     throws IllegalArgumentException, NamingException
/*  64:    */   {
/*  65:168 */     super.afterPropertiesSet();
/*  66:170 */     if ((this.proxyInterfaces != null) || (!this.lookupOnStartup) || (!this.cache) || (this.exposeAccessContext))
/*  67:    */     {
/*  68:172 */       if (this.defaultObject != null) {
/*  69:173 */         throw new IllegalArgumentException(
/*  70:174 */           "'defaultObject' is not supported in combination with 'proxyInterface'");
/*  71:    */       }
/*  72:177 */       this.jndiObject = JndiObjectProxyFactory.createJndiObjectProxy(this);
/*  73:    */     }
/*  74:    */     else
/*  75:    */     {
/*  76:180 */       if ((this.defaultObject != null) && (getExpectedType() != null) && 
/*  77:181 */         (!getExpectedType().isInstance(this.defaultObject))) {
/*  78:182 */         throw new IllegalArgumentException("Default object [" + this.defaultObject + 
/*  79:183 */           "] of type [" + this.defaultObject.getClass().getName() + 
/*  80:184 */           "] is not of expected type [" + getExpectedType().getName() + "]");
/*  81:    */       }
/*  82:187 */       this.jndiObject = lookupWithFallback();
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected Object lookupWithFallback()
/*  87:    */     throws NamingException
/*  88:    */   {
/*  89:199 */     ClassLoader originalClassLoader = ClassUtils.overrideThreadContextClassLoader(this.beanClassLoader);
/*  90:    */     try
/*  91:    */     {
/*  92:201 */       return lookup();
/*  93:    */     }
/*  94:    */     catch (TypeMismatchNamingException ex)
/*  95:    */     {
/*  96:206 */       throw ex;
/*  97:    */     }
/*  98:    */     catch (NamingException ex)
/*  99:    */     {
/* 100:    */       Object localObject2;
/* 101:209 */       if (this.defaultObject != null)
/* 102:    */       {
/* 103:210 */         if (this.logger.isDebugEnabled()) {
/* 104:211 */           this.logger.debug("JNDI lookup failed - returning specified default object instead", ex);
/* 105:213 */         } else if (this.logger.isInfoEnabled()) {
/* 106:214 */           this.logger.info("JNDI lookup failed - returning specified default object instead: " + ex);
/* 107:    */         }
/* 108:216 */         return this.defaultObject;
/* 109:    */       }
/* 110:    */       NamingException ex;
/* 111:218 */       throw ex;
/* 112:    */     }
/* 113:    */     finally
/* 114:    */     {
/* 115:221 */       if (originalClassLoader != null) {
/* 116:222 */         Thread.currentThread().setContextClassLoader(originalClassLoader);
/* 117:    */       }
/* 118:    */     }
/* 119:    */   }
/* 120:    */   
/* 121:    */   public Object getObject()
/* 122:    */   {
/* 123:232 */     return this.jndiObject;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public Class<?> getObjectType()
/* 127:    */   {
/* 128:236 */     if (this.proxyInterfaces != null)
/* 129:    */     {
/* 130:237 */       if (this.proxyInterfaces.length == 1) {
/* 131:238 */         return this.proxyInterfaces[0];
/* 132:    */       }
/* 133:240 */       if (this.proxyInterfaces.length > 1) {
/* 134:241 */         return createCompositeInterface(this.proxyInterfaces);
/* 135:    */       }
/* 136:    */     }
/* 137:244 */     if (this.jndiObject != null) {
/* 138:245 */       return this.jndiObject.getClass();
/* 139:    */     }
/* 140:248 */     return getExpectedType();
/* 141:    */   }
/* 142:    */   
/* 143:    */   public boolean isSingleton()
/* 144:    */   {
/* 145:253 */     return true;
/* 146:    */   }
/* 147:    */   
/* 148:    */   protected Class createCompositeInterface(Class[] interfaces)
/* 149:    */   {
/* 150:267 */     return ClassUtils.createCompositeInterface(interfaces, this.beanClassLoader);
/* 151:    */   }
/* 152:    */   
/* 153:    */   private static class JndiObjectProxyFactory
/* 154:    */   {
/* 155:    */     private static Object createJndiObjectProxy(JndiObjectFactoryBean jof)
/* 156:    */       throws NamingException
/* 157:    */     {
/* 158:278 */       JndiObjectTargetSource targetSource = new JndiObjectTargetSource();
/* 159:279 */       targetSource.setJndiTemplate(jof.getJndiTemplate());
/* 160:280 */       targetSource.setJndiName(jof.getJndiName());
/* 161:281 */       targetSource.setExpectedType(jof.getExpectedType());
/* 162:282 */       targetSource.setResourceRef(jof.isResourceRef());
/* 163:283 */       targetSource.setLookupOnStartup(jof.lookupOnStartup);
/* 164:284 */       targetSource.setCache(jof.cache);
/* 165:285 */       targetSource.afterPropertiesSet();
/* 166:    */       
/* 167:    */ 
/* 168:288 */       ProxyFactory proxyFactory = new ProxyFactory();
/* 169:289 */       if (jof.proxyInterfaces != null)
/* 170:    */       {
/* 171:290 */         proxyFactory.setInterfaces(jof.proxyInterfaces);
/* 172:    */       }
/* 173:    */       else
/* 174:    */       {
/* 175:293 */         Class targetClass = targetSource.getTargetClass();
/* 176:294 */         if (targetClass == null) {
/* 177:295 */           throw new IllegalStateException(
/* 178:296 */             "Cannot deactivate 'lookupOnStartup' without specifying a 'proxyInterface' or 'expectedType'");
/* 179:    */         }
/* 180:298 */         Class[] ifcs = ClassUtils.getAllInterfacesForClass(targetClass, jof.beanClassLoader);
/* 181:299 */         for (Class ifc : ifcs) {
/* 182:300 */           if (Modifier.isPublic(ifc.getModifiers())) {
/* 183:301 */             proxyFactory.addInterface(ifc);
/* 184:    */           }
/* 185:    */         }
/* 186:    */       }
/* 187:305 */       if (jof.exposeAccessContext) {
/* 188:306 */         proxyFactory.addAdvice(new JndiObjectFactoryBean.JndiContextExposingInterceptor(jof.getJndiTemplate()));
/* 189:    */       }
/* 190:308 */       proxyFactory.setTargetSource(targetSource);
/* 191:309 */       return proxyFactory.getProxy(jof.beanClassLoader);
/* 192:    */     }
/* 193:    */   }
/* 194:    */   
/* 195:    */   private static class JndiContextExposingInterceptor
/* 196:    */     implements MethodInterceptor
/* 197:    */   {
/* 198:    */     private final JndiTemplate jndiTemplate;
/* 199:    */     
/* 200:    */     public JndiContextExposingInterceptor(JndiTemplate jndiTemplate)
/* 201:    */     {
/* 202:323 */       this.jndiTemplate = jndiTemplate;
/* 203:    */     }
/* 204:    */     
/* 205:    */     public Object invoke(MethodInvocation invocation)
/* 206:    */       throws Throwable
/* 207:    */     {
/* 208:327 */       Context ctx = isEligible(invocation.getMethod()) ? this.jndiTemplate.getContext() : null;
/* 209:    */       try
/* 210:    */       {
/* 211:329 */         return invocation.proceed();
/* 212:    */       }
/* 213:    */       finally
/* 214:    */       {
/* 215:332 */         this.jndiTemplate.releaseContext(ctx);
/* 216:    */       }
/* 217:    */     }
/* 218:    */     
/* 219:    */     protected boolean isEligible(Method method)
/* 220:    */     {
/* 221:337 */       return !Object.class.equals(method.getDeclaringClass());
/* 222:    */     }
/* 223:    */   }
/* 224:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jndi.JndiObjectFactoryBean
 * JD-Core Version:    0.7.0.1
 */