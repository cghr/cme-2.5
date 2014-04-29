/*   1:    */ package org.springframework.beans.factory.support;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.lang.reflect.InvocationTargetException;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import java.lang.reflect.Modifier;
/*   7:    */ import java.security.AccessControlContext;
/*   8:    */ import java.security.AccessController;
/*   9:    */ import java.security.PrivilegedAction;
/*  10:    */ import java.security.PrivilegedActionException;
/*  11:    */ import java.security.PrivilegedExceptionAction;
/*  12:    */ import java.util.ArrayList;
/*  13:    */ import java.util.List;
/*  14:    */ import org.apache.commons.logging.Log;
/*  15:    */ import org.apache.commons.logging.LogFactory;
/*  16:    */ import org.springframework.beans.BeanUtils;
/*  17:    */ import org.springframework.beans.factory.DisposableBean;
/*  18:    */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*  19:    */ import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
/*  20:    */ import org.springframework.util.Assert;
/*  21:    */ import org.springframework.util.ReflectionUtils;
/*  22:    */ 
/*  23:    */ class DisposableBeanAdapter
/*  24:    */   implements DisposableBean, Runnable, Serializable
/*  25:    */ {
/*  26: 61 */   private static final Log logger = LogFactory.getLog(DisposableBeanAdapter.class);
/*  27:    */   private final Object bean;
/*  28:    */   private final String beanName;
/*  29:    */   private final boolean invokeDisposableBean;
/*  30:    */   private final boolean nonPublicAccessAllowed;
/*  31:    */   private String destroyMethodName;
/*  32:    */   private transient Method destroyMethod;
/*  33:    */   private List<DestructionAwareBeanPostProcessor> beanPostProcessors;
/*  34:    */   private final AccessControlContext acc;
/*  35:    */   
/*  36:    */   public DisposableBeanAdapter(Object bean, String beanName, RootBeanDefinition beanDefinition, List<BeanPostProcessor> postProcessors, AccessControlContext acc)
/*  37:    */   {
/*  38: 91 */     Assert.notNull(bean, "Disposable bean must not be null");
/*  39: 92 */     this.bean = bean;
/*  40: 93 */     this.beanName = beanName;
/*  41: 94 */     this.invokeDisposableBean = 
/*  42: 95 */       (((this.bean instanceof DisposableBean)) && (!beanDefinition.isExternallyManagedDestroyMethod("destroy")));
/*  43: 96 */     this.nonPublicAccessAllowed = beanDefinition.isNonPublicAccessAllowed();
/*  44: 97 */     this.acc = acc;
/*  45: 98 */     inferDestroyMethodIfNecessary(beanDefinition);
/*  46: 99 */     String destroyMethodName = beanDefinition.getDestroyMethodName();
/*  47:100 */     if ((destroyMethodName != null) && ((!this.invokeDisposableBean) || (!"destroy".equals(destroyMethodName))) && 
/*  48:101 */       (!beanDefinition.isExternallyManagedDestroyMethod(destroyMethodName)))
/*  49:    */     {
/*  50:102 */       this.destroyMethodName = destroyMethodName;
/*  51:103 */       this.destroyMethod = determineDestroyMethod();
/*  52:104 */       if (this.destroyMethod == null)
/*  53:    */       {
/*  54:105 */         if (beanDefinition.isEnforceDestroyMethod()) {
/*  55:106 */           throw new BeanDefinitionValidationException("Couldn't find a destroy method named '" + 
/*  56:107 */             destroyMethodName + "' on bean with name '" + beanName + "'");
/*  57:    */         }
/*  58:    */       }
/*  59:    */       else
/*  60:    */       {
/*  61:111 */         Class[] paramTypes = this.destroyMethod.getParameterTypes();
/*  62:112 */         if (paramTypes.length > 1) {
/*  63:113 */           throw new BeanDefinitionValidationException("Method '" + destroyMethodName + "' of bean '" + 
/*  64:114 */             beanName + "' has more than one parameter - not supported as destroy method");
/*  65:    */         }
/*  66:116 */         if ((paramTypes.length == 1) && (!paramTypes[0].equals(Boolean.TYPE))) {
/*  67:117 */           throw new BeanDefinitionValidationException("Method '" + destroyMethodName + "' of bean '" + 
/*  68:118 */             beanName + "' has a non-boolean parameter - not supported as destroy method");
/*  69:    */         }
/*  70:    */       }
/*  71:    */     }
/*  72:122 */     this.beanPostProcessors = filterPostProcessors(postProcessors);
/*  73:    */   }
/*  74:    */   
/*  75:    */   private void inferDestroyMethodIfNecessary(RootBeanDefinition beanDefinition)
/*  76:    */   {
/*  77:137 */     if ("(inferred)".equals(beanDefinition.getDestroyMethodName())) {
/*  78:    */       try
/*  79:    */       {
/*  80:139 */         Method candidate = this.bean.getClass().getMethod("close", new Class[0]);
/*  81:140 */         if (Modifier.isPublic(candidate.getModifiers())) {
/*  82:141 */           beanDefinition.setDestroyMethodName(candidate.getName());
/*  83:    */         }
/*  84:    */       }
/*  85:    */       catch (NoSuchMethodException localNoSuchMethodException)
/*  86:    */       {
/*  87:145 */         beanDefinition.setDestroyMethodName(null);
/*  88:    */       }
/*  89:    */     }
/*  90:    */   }
/*  91:    */   
/*  92:    */   private DisposableBeanAdapter(Object bean, String beanName, boolean invokeDisposableBean, boolean nonPublicAccessAllowed, String destroyMethodName, List<DestructionAwareBeanPostProcessor> postProcessors)
/*  93:    */   {
/*  94:157 */     this.bean = bean;
/*  95:158 */     this.beanName = beanName;
/*  96:159 */     this.invokeDisposableBean = invokeDisposableBean;
/*  97:160 */     this.nonPublicAccessAllowed = nonPublicAccessAllowed;
/*  98:161 */     this.destroyMethodName = destroyMethodName;
/*  99:162 */     this.beanPostProcessors = postProcessors;
/* 100:163 */     this.acc = null;
/* 101:    */   }
/* 102:    */   
/* 103:    */   private List<DestructionAwareBeanPostProcessor> filterPostProcessors(List<BeanPostProcessor> postProcessors)
/* 104:    */   {
/* 105:173 */     List<DestructionAwareBeanPostProcessor> filteredPostProcessors = null;
/* 106:174 */     if ((postProcessors != null) && (!postProcessors.isEmpty()))
/* 107:    */     {
/* 108:175 */       filteredPostProcessors = new ArrayList(postProcessors.size());
/* 109:176 */       for (BeanPostProcessor postProcessor : postProcessors) {
/* 110:177 */         if ((postProcessor instanceof DestructionAwareBeanPostProcessor)) {
/* 111:178 */           filteredPostProcessors.add((DestructionAwareBeanPostProcessor)postProcessor);
/* 112:    */         }
/* 113:    */       }
/* 114:    */     }
/* 115:182 */     return filteredPostProcessors;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void run()
/* 119:    */   {
/* 120:187 */     destroy();
/* 121:    */   }
/* 122:    */   
/* 123:    */   public void destroy()
/* 124:    */   {
/* 125:191 */     if ((this.beanPostProcessors != null) && (!this.beanPostProcessors.isEmpty())) {
/* 126:192 */       for (DestructionAwareBeanPostProcessor processor : this.beanPostProcessors) {
/* 127:193 */         processor.postProcessBeforeDestruction(this.bean, this.beanName);
/* 128:    */       }
/* 129:    */     }
/* 130:197 */     if (this.invokeDisposableBean)
/* 131:    */     {
/* 132:198 */       if (logger.isDebugEnabled()) {
/* 133:199 */         logger.debug("Invoking destroy() on bean with name '" + this.beanName + "'");
/* 134:    */       }
/* 135:    */       try
/* 136:    */       {
/* 137:202 */         if (System.getSecurityManager() != null) {
/* 138:203 */           AccessController.doPrivileged(new PrivilegedExceptionAction()
/* 139:    */           {
/* 140:    */             public Object run()
/* 141:    */               throws Exception
/* 142:    */             {
/* 143:205 */               ((DisposableBean)DisposableBeanAdapter.this.bean).destroy();
/* 144:206 */               return null;
/* 145:    */             }
/* 146:208 */           }, this.acc);
/* 147:    */         } else {
/* 148:211 */           ((DisposableBean)this.bean).destroy();
/* 149:    */         }
/* 150:    */       }
/* 151:    */       catch (Throwable ex)
/* 152:    */       {
/* 153:215 */         String msg = "Invocation of destroy method failed on bean with name '" + this.beanName + "'";
/* 154:216 */         if (logger.isDebugEnabled()) {
/* 155:217 */           logger.warn(msg, ex);
/* 156:    */         } else {
/* 157:220 */           logger.warn(msg + ": " + ex);
/* 158:    */         }
/* 159:    */       }
/* 160:    */     }
/* 161:225 */     if (this.destroyMethod != null)
/* 162:    */     {
/* 163:226 */       invokeCustomDestroyMethod(this.destroyMethod);
/* 164:    */     }
/* 165:228 */     else if (this.destroyMethodName != null)
/* 166:    */     {
/* 167:229 */       Method methodToCall = determineDestroyMethod();
/* 168:230 */       if (methodToCall != null) {
/* 169:231 */         invokeCustomDestroyMethod(methodToCall);
/* 170:    */       }
/* 171:    */     }
/* 172:    */   }
/* 173:    */   
/* 174:    */   private Method determineDestroyMethod()
/* 175:    */   {
/* 176:    */     try
/* 177:    */     {
/* 178:239 */       if (System.getSecurityManager() != null) {
/* 179:240 */         (Method)AccessController.doPrivileged(new PrivilegedAction()
/* 180:    */         {
/* 181:    */           public Method run()
/* 182:    */           {
/* 183:242 */             return DisposableBeanAdapter.this.findDestroyMethod();
/* 184:    */           }
/* 185:    */         });
/* 186:    */       }
/* 187:247 */       return findDestroyMethod();
/* 188:    */     }
/* 189:    */     catch (IllegalArgumentException ex)
/* 190:    */     {
/* 191:251 */       throw new BeanDefinitionValidationException("Couldn't find a unique destroy method on bean with name '" + 
/* 192:252 */         this.beanName + ": " + ex.getMessage());
/* 193:    */     }
/* 194:    */   }
/* 195:    */   
/* 196:    */   private Method findDestroyMethod()
/* 197:    */   {
/* 198:257 */     return this.nonPublicAccessAllowed ? 
/* 199:258 */       BeanUtils.findMethodWithMinimalParameters(this.bean.getClass(), this.destroyMethodName) : 
/* 200:259 */       BeanUtils.findMethodWithMinimalParameters(this.bean.getClass().getMethods(), this.destroyMethodName);
/* 201:    */   }
/* 202:    */   
/* 203:    */   private void invokeCustomDestroyMethod(final Method destroyMethod)
/* 204:    */   {
/* 205:269 */     Class[] paramTypes = destroyMethod.getParameterTypes();
/* 206:270 */     final Object[] args = new Object[paramTypes.length];
/* 207:271 */     if (paramTypes.length == 1) {
/* 208:272 */       args[0] = Boolean.TRUE;
/* 209:    */     }
/* 210:274 */     if (logger.isDebugEnabled()) {
/* 211:275 */       logger.debug("Invoking destroy method '" + this.destroyMethodName + 
/* 212:276 */         "' on bean with name '" + this.beanName + "'");
/* 213:    */     }
/* 214:    */     try
/* 215:    */     {
/* 216:279 */       if (System.getSecurityManager() != null)
/* 217:    */       {
/* 218:280 */         AccessController.doPrivileged(new PrivilegedAction()
/* 219:    */         {
/* 220:    */           public Object run()
/* 221:    */           {
/* 222:282 */             ReflectionUtils.makeAccessible(destroyMethod);
/* 223:283 */             return null;
/* 224:    */           }
/* 225:    */         });
/* 226:    */         try
/* 227:    */         {
/* 228:287 */           AccessController.doPrivileged(new PrivilegedExceptionAction()
/* 229:    */           {
/* 230:    */             public Object run()
/* 231:    */               throws Exception
/* 232:    */             {
/* 233:289 */               destroyMethod.invoke(DisposableBeanAdapter.this.bean, args);
/* 234:290 */               return null;
/* 235:    */             }
/* 236:292 */           }, this.acc);
/* 237:    */         }
/* 238:    */         catch (PrivilegedActionException pax)
/* 239:    */         {
/* 240:295 */           throw ((InvocationTargetException)pax.getException());
/* 241:    */         }
/* 242:    */       }
/* 243:    */       else
/* 244:    */       {
/* 245:299 */         ReflectionUtils.makeAccessible(destroyMethod);
/* 246:300 */         destroyMethod.invoke(this.bean, args);
/* 247:    */       }
/* 248:    */     }
/* 249:    */     catch (InvocationTargetException ex)
/* 250:    */     {
/* 251:304 */       String msg = "Invocation of destroy method '" + this.destroyMethodName + 
/* 252:305 */         "' failed on bean with name '" + this.beanName + "'";
/* 253:306 */       if (logger.isDebugEnabled()) {
/* 254:307 */         logger.warn(msg, ex.getTargetException());
/* 255:    */       } else {
/* 256:310 */         logger.warn(msg + ": " + ex.getTargetException());
/* 257:    */       }
/* 258:    */     }
/* 259:    */     catch (Throwable ex)
/* 260:    */     {
/* 261:314 */       logger.error("Couldn't invoke destroy method '" + this.destroyMethodName + 
/* 262:315 */         "' on bean with name '" + this.beanName + "'", ex);
/* 263:    */     }
/* 264:    */   }
/* 265:    */   
/* 266:    */   protected Object writeReplace()
/* 267:    */   {
/* 268:325 */     List<DestructionAwareBeanPostProcessor> serializablePostProcessors = null;
/* 269:326 */     if (this.beanPostProcessors != null)
/* 270:    */     {
/* 271:327 */       serializablePostProcessors = new ArrayList();
/* 272:328 */       for (DestructionAwareBeanPostProcessor postProcessor : this.beanPostProcessors) {
/* 273:329 */         if ((postProcessor instanceof Serializable)) {
/* 274:330 */           serializablePostProcessors.add(postProcessor);
/* 275:    */         }
/* 276:    */       }
/* 277:    */     }
/* 278:334 */     return new DisposableBeanAdapter(this.bean, this.beanName, this.invokeDisposableBean, 
/* 279:335 */       this.nonPublicAccessAllowed, this.destroyMethodName, serializablePostProcessors);
/* 280:    */   }
/* 281:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.DisposableBeanAdapter
 * JD-Core Version:    0.7.0.1
 */