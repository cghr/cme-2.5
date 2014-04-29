/*   1:    */ package org.springframework.beans.factory.annotation;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.ObjectInputStream;
/*   5:    */ import java.io.Serializable;
/*   6:    */ import java.lang.annotation.Annotation;
/*   7:    */ import java.lang.reflect.InvocationTargetException;
/*   8:    */ import java.lang.reflect.Method;
/*   9:    */ import java.lang.reflect.Modifier;
/*  10:    */ import java.util.Collection;
/*  11:    */ import java.util.Collections;
/*  12:    */ import java.util.Iterator;
/*  13:    */ import java.util.LinkedHashSet;
/*  14:    */ import java.util.LinkedList;
/*  15:    */ import java.util.Map;
/*  16:    */ import java.util.Set;
/*  17:    */ import java.util.concurrent.ConcurrentHashMap;
/*  18:    */ import org.apache.commons.logging.Log;
/*  19:    */ import org.apache.commons.logging.LogFactory;
/*  20:    */ import org.springframework.beans.BeansException;
/*  21:    */ import org.springframework.beans.factory.BeanCreationException;
/*  22:    */ import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
/*  23:    */ import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
/*  24:    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*  25:    */ import org.springframework.core.PriorityOrdered;
/*  26:    */ import org.springframework.util.ReflectionUtils;
/*  27:    */ 
/*  28:    */ public class InitDestroyAnnotationBeanPostProcessor
/*  29:    */   implements DestructionAwareBeanPostProcessor, MergedBeanDefinitionPostProcessor, PriorityOrdered, Serializable
/*  30:    */ {
/*  31: 79 */   protected transient Log logger = LogFactory.getLog(getClass());
/*  32:    */   private Class<? extends Annotation> initAnnotationType;
/*  33:    */   private Class<? extends Annotation> destroyAnnotationType;
/*  34: 85 */   private int order = 2147483647;
/*  35: 88 */   private final transient Map<Class<?>, LifecycleMetadata> lifecycleMetadataCache = new ConcurrentHashMap();
/*  36:    */   
/*  37:    */   public void setInitAnnotationType(Class<? extends Annotation> initAnnotationType)
/*  38:    */   {
/*  39: 99 */     this.initAnnotationType = initAnnotationType;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setDestroyAnnotationType(Class<? extends Annotation> destroyAnnotationType)
/*  43:    */   {
/*  44:110 */     this.destroyAnnotationType = destroyAnnotationType;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setOrder(int order)
/*  48:    */   {
/*  49:114 */     this.order = order;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public int getOrder()
/*  53:    */   {
/*  54:118 */     return this.order;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName)
/*  58:    */   {
/*  59:123 */     if (beanType != null)
/*  60:    */     {
/*  61:124 */       LifecycleMetadata metadata = findLifecycleMetadata(beanType);
/*  62:125 */       metadata.checkConfigMembers(beanDefinition);
/*  63:    */     }
/*  64:    */   }
/*  65:    */   
/*  66:    */   public Object postProcessBeforeInitialization(Object bean, String beanName)
/*  67:    */     throws BeansException
/*  68:    */   {
/*  69:130 */     LifecycleMetadata metadata = findLifecycleMetadata(bean.getClass());
/*  70:    */     try
/*  71:    */     {
/*  72:132 */       metadata.invokeInitMethods(bean, beanName);
/*  73:    */     }
/*  74:    */     catch (InvocationTargetException ex)
/*  75:    */     {
/*  76:135 */       throw new BeanCreationException(beanName, "Invocation of init method failed", ex.getTargetException());
/*  77:    */     }
/*  78:    */     catch (Throwable ex)
/*  79:    */     {
/*  80:138 */       throw new BeanCreationException(beanName, "Couldn't invoke init method", ex);
/*  81:    */     }
/*  82:140 */     return bean;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public Object postProcessAfterInitialization(Object bean, String beanName)
/*  86:    */     throws BeansException
/*  87:    */   {
/*  88:144 */     return bean;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void postProcessBeforeDestruction(Object bean, String beanName)
/*  92:    */     throws BeansException
/*  93:    */   {
/*  94:148 */     LifecycleMetadata metadata = findLifecycleMetadata(bean.getClass());
/*  95:    */     try
/*  96:    */     {
/*  97:150 */       metadata.invokeDestroyMethods(bean, beanName);
/*  98:    */     }
/*  99:    */     catch (InvocationTargetException ex)
/* 100:    */     {
/* 101:153 */       String msg = "Invocation of destroy method failed on bean with name '" + beanName + "'";
/* 102:154 */       if (this.logger.isDebugEnabled()) {
/* 103:155 */         this.logger.warn(msg, ex.getTargetException());
/* 104:    */       } else {
/* 105:158 */         this.logger.warn(msg + ": " + ex.getTargetException());
/* 106:    */       }
/* 107:    */     }
/* 108:    */     catch (Throwable ex)
/* 109:    */     {
/* 110:162 */       this.logger.error("Couldn't invoke destroy method on bean with name '" + beanName + "'", ex);
/* 111:    */     }
/* 112:    */   }
/* 113:    */   
/* 114:    */   private LifecycleMetadata findLifecycleMetadata(Class<?> clazz)
/* 115:    */   {
/* 116:168 */     if (this.lifecycleMetadataCache == null) {
/* 117:170 */       return buildLifecycleMetadata(clazz);
/* 118:    */     }
/* 119:173 */     LifecycleMetadata metadata = (LifecycleMetadata)this.lifecycleMetadataCache.get(clazz);
/* 120:174 */     if (metadata == null) {
/* 121:175 */       synchronized (this.lifecycleMetadataCache)
/* 122:    */       {
/* 123:176 */         metadata = (LifecycleMetadata)this.lifecycleMetadataCache.get(clazz);
/* 124:177 */         if (metadata == null)
/* 125:    */         {
/* 126:178 */           metadata = buildLifecycleMetadata(clazz);
/* 127:179 */           this.lifecycleMetadataCache.put(clazz, metadata);
/* 128:    */         }
/* 129:181 */         return metadata;
/* 130:    */       }
/* 131:    */     }
/* 132:184 */     return metadata;
/* 133:    */   }
/* 134:    */   
/* 135:    */   private LifecycleMetadata buildLifecycleMetadata(Class<?> clazz)
/* 136:    */   {
/* 137:188 */     boolean debug = this.logger.isDebugEnabled();
/* 138:189 */     LinkedList<LifecycleElement> initMethods = new LinkedList();
/* 139:190 */     LinkedList<LifecycleElement> destroyMethods = new LinkedList();
/* 140:191 */     Class<?> targetClass = clazz;
/* 141:    */     do
/* 142:    */     {
/* 143:194 */       LinkedList<LifecycleElement> currInitMethods = new LinkedList();
/* 144:195 */       LinkedList<LifecycleElement> currDestroyMethods = new LinkedList();
/* 145:196 */       for (Method method : targetClass.getDeclaredMethods())
/* 146:    */       {
/* 147:197 */         if ((this.initAnnotationType != null) && 
/* 148:198 */           (method.getAnnotation(this.initAnnotationType) != null))
/* 149:    */         {
/* 150:199 */           LifecycleElement element = new LifecycleElement(method);
/* 151:200 */           currInitMethods.add(element);
/* 152:201 */           if (debug) {
/* 153:202 */             this.logger.debug("Found init method on class [" + clazz.getName() + "]: " + method);
/* 154:    */           }
/* 155:    */         }
/* 156:206 */         if ((this.destroyAnnotationType != null) && 
/* 157:207 */           (method.getAnnotation(this.destroyAnnotationType) != null))
/* 158:    */         {
/* 159:208 */           currDestroyMethods.add(new LifecycleElement(method));
/* 160:209 */           if (debug) {
/* 161:210 */             this.logger.debug("Found destroy method on class [" + clazz.getName() + "]: " + method);
/* 162:    */           }
/* 163:    */         }
/* 164:    */       }
/* 165:215 */       initMethods.addAll(0, currInitMethods);
/* 166:216 */       destroyMethods.addAll(currDestroyMethods);
/* 167:217 */       targetClass = targetClass.getSuperclass();
/* 168:219 */     } while ((targetClass != null) && (targetClass != Object.class));
/* 169:221 */     return new LifecycleMetadata(clazz, initMethods, destroyMethods);
/* 170:    */   }
/* 171:    */   
/* 172:    */   private void readObject(ObjectInputStream ois)
/* 173:    */     throws IOException, ClassNotFoundException
/* 174:    */   {
/* 175:231 */     ois.defaultReadObject();
/* 176:    */     
/* 177:    */ 
/* 178:234 */     this.logger = LogFactory.getLog(getClass());
/* 179:    */   }
/* 180:    */   
/* 181:    */   private class LifecycleMetadata
/* 182:    */   {
/* 183:    */     private final Set<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> initMethods;
/* 184:    */     private final Set<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> destroyMethods;
/* 185:    */     
/* 186:    */     public LifecycleMetadata(Collection<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> targetClass, Collection<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> initMethods)
/* 187:    */     {
/* 188:250 */       this.initMethods = Collections.synchronizedSet(new LinkedHashSet());
/* 189:251 */       for (InitDestroyAnnotationBeanPostProcessor.LifecycleElement element : initMethods)
/* 190:    */       {
/* 191:252 */         if (InitDestroyAnnotationBeanPostProcessor.this.logger.isDebugEnabled()) {
/* 192:253 */           InitDestroyAnnotationBeanPostProcessor.this.logger.debug("Found init method on class [" + targetClass.getName() + "]: " + element);
/* 193:    */         }
/* 194:255 */         this.initMethods.add(element);
/* 195:    */       }
/* 196:258 */       this.destroyMethods = Collections.synchronizedSet(new LinkedHashSet());
/* 197:259 */       for (InitDestroyAnnotationBeanPostProcessor.LifecycleElement element : destroyMethods)
/* 198:    */       {
/* 199:260 */         if (InitDestroyAnnotationBeanPostProcessor.this.logger.isDebugEnabled()) {
/* 200:261 */           InitDestroyAnnotationBeanPostProcessor.this.logger.debug("Found destroy method on class [" + targetClass.getName() + "]: " + element);
/* 201:    */         }
/* 202:263 */         this.destroyMethods.add(element);
/* 203:    */       }
/* 204:    */     }
/* 205:    */     
/* 206:    */     public void checkConfigMembers(RootBeanDefinition beanDefinition)
/* 207:    */     {
/* 208:268 */       synchronized (this.initMethods)
/* 209:    */       {
/* 210:269 */         for (Iterator<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> it = this.initMethods.iterator(); it.hasNext();)
/* 211:    */         {
/* 212:270 */           String methodIdentifier = ((InitDestroyAnnotationBeanPostProcessor.LifecycleElement)it.next()).getIdentifier();
/* 213:271 */           if (!beanDefinition.isExternallyManagedInitMethod(methodIdentifier)) {
/* 214:272 */             beanDefinition.registerExternallyManagedInitMethod(methodIdentifier);
/* 215:    */           } else {
/* 216:275 */             it.remove();
/* 217:    */           }
/* 218:    */         }
/* 219:    */       }
/* 220:279 */       synchronized (this.destroyMethods)
/* 221:    */       {
/* 222:280 */         for (Iterator<InitDestroyAnnotationBeanPostProcessor.LifecycleElement> it = this.destroyMethods.iterator(); it.hasNext();)
/* 223:    */         {
/* 224:281 */           String methodIdentifier = ((InitDestroyAnnotationBeanPostProcessor.LifecycleElement)it.next()).getIdentifier();
/* 225:282 */           if (!beanDefinition.isExternallyManagedDestroyMethod(methodIdentifier)) {
/* 226:283 */             beanDefinition.registerExternallyManagedDestroyMethod(methodIdentifier);
/* 227:    */           } else {
/* 228:286 */             it.remove();
/* 229:    */           }
/* 230:    */         }
/* 231:    */       }
/* 232:    */     }
/* 233:    */     
/* 234:    */     public void invokeInitMethods(Object target, String beanName)
/* 235:    */       throws Throwable
/* 236:    */     {
/* 237:293 */       if (!this.initMethods.isEmpty())
/* 238:    */       {
/* 239:294 */         boolean debug = InitDestroyAnnotationBeanPostProcessor.this.logger.isDebugEnabled();
/* 240:295 */         for (InitDestroyAnnotationBeanPostProcessor.LifecycleElement element : this.initMethods)
/* 241:    */         {
/* 242:296 */           if (debug) {
/* 243:297 */             InitDestroyAnnotationBeanPostProcessor.this.logger.debug("Invoking init method on bean '" + beanName + "': " + element.getMethod());
/* 244:    */           }
/* 245:299 */           element.invoke(target);
/* 246:    */         }
/* 247:    */       }
/* 248:    */     }
/* 249:    */     
/* 250:    */     public void invokeDestroyMethods(Object target, String beanName)
/* 251:    */       throws Throwable
/* 252:    */     {
/* 253:305 */       if (!this.destroyMethods.isEmpty())
/* 254:    */       {
/* 255:306 */         boolean debug = InitDestroyAnnotationBeanPostProcessor.this.logger.isDebugEnabled();
/* 256:307 */         for (InitDestroyAnnotationBeanPostProcessor.LifecycleElement element : this.destroyMethods)
/* 257:    */         {
/* 258:308 */           if (debug) {
/* 259:309 */             InitDestroyAnnotationBeanPostProcessor.this.logger.debug("Invoking destroy method on bean '" + beanName + "': " + element.getMethod());
/* 260:    */           }
/* 261:311 */           element.invoke(target);
/* 262:    */         }
/* 263:    */       }
/* 264:    */     }
/* 265:    */   }
/* 266:    */   
/* 267:    */   private static class LifecycleElement
/* 268:    */   {
/* 269:    */     private final Method method;
/* 270:    */     private final String identifier;
/* 271:    */     
/* 272:    */     public LifecycleElement(Method method)
/* 273:    */     {
/* 274:328 */       if (method.getParameterTypes().length != 0) {
/* 275:329 */         throw new IllegalStateException("Lifecycle method annotation requires a no-arg method: " + method);
/* 276:    */       }
/* 277:331 */       this.method = method;
/* 278:332 */       this.identifier = (Modifier.isPrivate(method.getModifiers()) ? 
/* 279:333 */         method.getDeclaringClass() + "." + method.getName() : method.getName());
/* 280:    */     }
/* 281:    */     
/* 282:    */     public Method getMethod()
/* 283:    */     {
/* 284:337 */       return this.method;
/* 285:    */     }
/* 286:    */     
/* 287:    */     public String getIdentifier()
/* 288:    */     {
/* 289:341 */       return this.identifier;
/* 290:    */     }
/* 291:    */     
/* 292:    */     public void invoke(Object target)
/* 293:    */       throws Throwable
/* 294:    */     {
/* 295:345 */       ReflectionUtils.makeAccessible(this.method);
/* 296:346 */       this.method.invoke(target, null);
/* 297:    */     }
/* 298:    */     
/* 299:    */     public boolean equals(Object other)
/* 300:    */     {
/* 301:351 */       if (this == other) {
/* 302:352 */         return true;
/* 303:    */       }
/* 304:354 */       if (!(other instanceof LifecycleElement)) {
/* 305:355 */         return false;
/* 306:    */       }
/* 307:357 */       LifecycleElement otherElement = (LifecycleElement)other;
/* 308:358 */       return this.identifier.equals(otherElement.identifier);
/* 309:    */     }
/* 310:    */     
/* 311:    */     public int hashCode()
/* 312:    */     {
/* 313:363 */       return this.identifier.hashCode();
/* 314:    */     }
/* 315:    */   }
/* 316:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.annotation.InitDestroyAnnotationBeanPostProcessor
 * JD-Core Version:    0.7.0.1
 */