/*   1:    */ package org.springframework.beans.factory.support;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Array;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.LinkedHashMap;
/*   6:    */ import java.util.LinkedHashSet;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Map;
/*   9:    */ import java.util.Map.Entry;
/*  10:    */ import java.util.Properties;
/*  11:    */ import java.util.Set;
/*  12:    */ import org.springframework.beans.BeansException;
/*  13:    */ import org.springframework.beans.TypeConverter;
/*  14:    */ import org.springframework.beans.factory.BeanCreationException;
/*  15:    */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*  16:    */ import org.springframework.beans.factory.BeanFactory;
/*  17:    */ import org.springframework.beans.factory.FactoryBean;
/*  18:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*  19:    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*  20:    */ import org.springframework.beans.factory.config.RuntimeBeanNameReference;
/*  21:    */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*  22:    */ import org.springframework.beans.factory.config.TypedStringValue;
/*  23:    */ import org.springframework.util.ClassUtils;
/*  24:    */ import org.springframework.util.StringUtils;
/*  25:    */ 
/*  26:    */ class BeanDefinitionValueResolver
/*  27:    */ {
/*  28:    */   private final AbstractBeanFactory beanFactory;
/*  29:    */   private final String beanName;
/*  30:    */   private final BeanDefinition beanDefinition;
/*  31:    */   private final TypeConverter typeConverter;
/*  32:    */   
/*  33:    */   public BeanDefinitionValueResolver(AbstractBeanFactory beanFactory, String beanName, BeanDefinition beanDefinition, TypeConverter typeConverter)
/*  34:    */   {
/*  35: 77 */     this.beanFactory = beanFactory;
/*  36: 78 */     this.beanName = beanName;
/*  37: 79 */     this.beanDefinition = beanDefinition;
/*  38: 80 */     this.typeConverter = typeConverter;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public Object resolveValueIfNecessary(Object argName, Object value)
/*  42:    */   {
/*  43:104 */     if ((value instanceof RuntimeBeanReference))
/*  44:    */     {
/*  45:105 */       RuntimeBeanReference ref = (RuntimeBeanReference)value;
/*  46:106 */       return resolveReference(argName, ref);
/*  47:    */     }
/*  48:108 */     if ((value instanceof RuntimeBeanNameReference))
/*  49:    */     {
/*  50:109 */       String refName = ((RuntimeBeanNameReference)value).getBeanName();
/*  51:110 */       refName = String.valueOf(evaluate(refName));
/*  52:111 */       if (!this.beanFactory.containsBean(refName)) {
/*  53:112 */         throw new BeanDefinitionStoreException(
/*  54:113 */           "Invalid bean name '" + refName + "' in bean reference for " + argName);
/*  55:    */       }
/*  56:115 */       return refName;
/*  57:    */     }
/*  58:117 */     if ((value instanceof BeanDefinitionHolder))
/*  59:    */     {
/*  60:119 */       BeanDefinitionHolder bdHolder = (BeanDefinitionHolder)value;
/*  61:120 */       return resolveInnerBean(argName, bdHolder.getBeanName(), bdHolder.getBeanDefinition());
/*  62:    */     }
/*  63:122 */     if ((value instanceof BeanDefinition))
/*  64:    */     {
/*  65:124 */       BeanDefinition bd = (BeanDefinition)value;
/*  66:125 */       return resolveInnerBean(argName, "(inner bean)", bd);
/*  67:    */     }
/*  68:127 */     if ((value instanceof ManagedArray))
/*  69:    */     {
/*  70:129 */       ManagedArray array = (ManagedArray)value;
/*  71:130 */       Class elementType = array.resolvedElementType;
/*  72:131 */       if (elementType == null)
/*  73:    */       {
/*  74:132 */         String elementTypeName = array.getElementTypeName();
/*  75:133 */         if (StringUtils.hasText(elementTypeName)) {
/*  76:    */           try
/*  77:    */           {
/*  78:135 */             elementType = ClassUtils.forName(elementTypeName, this.beanFactory.getBeanClassLoader());
/*  79:136 */             array.resolvedElementType = elementType;
/*  80:    */           }
/*  81:    */           catch (Throwable ex)
/*  82:    */           {
/*  83:140 */             throw new BeanCreationException(
/*  84:141 */               this.beanDefinition.getResourceDescription(), this.beanName, 
/*  85:142 */               "Error resolving array type for " + argName, ex);
/*  86:    */           }
/*  87:    */         } else {
/*  88:146 */           elementType = Object.class;
/*  89:    */         }
/*  90:    */       }
/*  91:149 */       return resolveManagedArray(argName, (List)value, elementType);
/*  92:    */     }
/*  93:151 */     if ((value instanceof ManagedList)) {
/*  94:153 */       return resolveManagedList(argName, (List)value);
/*  95:    */     }
/*  96:155 */     if ((value instanceof ManagedSet)) {
/*  97:157 */       return resolveManagedSet(argName, (Set)value);
/*  98:    */     }
/*  99:159 */     if ((value instanceof ManagedMap)) {
/* 100:161 */       return resolveManagedMap(argName, (Map)value);
/* 101:    */     }
/* 102:163 */     if ((value instanceof ManagedProperties))
/* 103:    */     {
/* 104:164 */       Properties original = (Properties)value;
/* 105:165 */       Properties copy = new Properties();
/* 106:166 */       for (Map.Entry propEntry : original.entrySet())
/* 107:    */       {
/* 108:167 */         Object propKey = propEntry.getKey();
/* 109:168 */         Object propValue = propEntry.getValue();
/* 110:169 */         if ((propKey instanceof TypedStringValue)) {
/* 111:170 */           propKey = evaluate((TypedStringValue)propKey);
/* 112:    */         }
/* 113:172 */         if ((propValue instanceof TypedStringValue)) {
/* 114:173 */           propValue = evaluate((TypedStringValue)propValue);
/* 115:    */         }
/* 116:175 */         copy.put(propKey, propValue);
/* 117:    */       }
/* 118:177 */       return copy;
/* 119:    */     }
/* 120:179 */     if ((value instanceof TypedStringValue))
/* 121:    */     {
/* 122:181 */       TypedStringValue typedStringValue = (TypedStringValue)value;
/* 123:182 */       Object valueObject = evaluate(typedStringValue);
/* 124:    */       try
/* 125:    */       {
/* 126:184 */         Class<?> resolvedTargetType = resolveTargetType(typedStringValue);
/* 127:185 */         if (resolvedTargetType != null) {
/* 128:186 */           return this.typeConverter.convertIfNecessary(valueObject, resolvedTargetType);
/* 129:    */         }
/* 130:189 */         return valueObject;
/* 131:    */       }
/* 132:    */       catch (Throwable ex)
/* 133:    */       {
/* 134:194 */         throw new BeanCreationException(
/* 135:195 */           this.beanDefinition.getResourceDescription(), this.beanName, 
/* 136:196 */           "Error converting typed String value for " + argName, ex);
/* 137:    */       }
/* 138:    */     }
/* 139:200 */     return evaluate(value);
/* 140:    */   }
/* 141:    */   
/* 142:    */   protected Object evaluate(TypedStringValue value)
/* 143:    */   {
/* 144:210 */     Object result = this.beanFactory.evaluateBeanDefinitionString(value.getValue(), this.beanDefinition);
/* 145:211 */     if (result != value.getValue()) {
/* 146:212 */       value.setDynamic();
/* 147:    */     }
/* 148:214 */     return result;
/* 149:    */   }
/* 150:    */   
/* 151:    */   protected Object evaluate(Object value)
/* 152:    */   {
/* 153:223 */     if ((value instanceof String)) {
/* 154:224 */       return this.beanFactory.evaluateBeanDefinitionString((String)value, this.beanDefinition);
/* 155:    */     }
/* 156:227 */     return value;
/* 157:    */   }
/* 158:    */   
/* 159:    */   protected Class<?> resolveTargetType(TypedStringValue value)
/* 160:    */     throws ClassNotFoundException
/* 161:    */   {
/* 162:239 */     if (value.hasTargetType()) {
/* 163:240 */       return value.getTargetType();
/* 164:    */     }
/* 165:242 */     return value.resolveTargetType(this.beanFactory.getBeanClassLoader());
/* 166:    */   }
/* 167:    */   
/* 168:    */   private Object resolveInnerBean(Object argName, String innerBeanName, BeanDefinition innerBd)
/* 169:    */   {
/* 170:253 */     RootBeanDefinition mbd = null;
/* 171:    */     try
/* 172:    */     {
/* 173:255 */       mbd = this.beanFactory.getMergedBeanDefinition(innerBeanName, innerBd, this.beanDefinition);
/* 174:    */       
/* 175:    */ 
/* 176:258 */       String actualInnerBeanName = innerBeanName;
/* 177:259 */       if (mbd.isSingleton()) {
/* 178:260 */         actualInnerBeanName = adaptInnerBeanName(innerBeanName);
/* 179:    */       }
/* 180:263 */       String[] dependsOn = mbd.getDependsOn();
/* 181:264 */       if (dependsOn != null) {
/* 182:265 */         for (String dependsOnBean : dependsOn)
/* 183:    */         {
/* 184:266 */           this.beanFactory.getBean(dependsOnBean);
/* 185:267 */           this.beanFactory.registerDependentBean(dependsOnBean, actualInnerBeanName);
/* 186:    */         }
/* 187:    */       }
/* 188:270 */       Object innerBean = this.beanFactory.createBean(actualInnerBeanName, mbd, null);
/* 189:271 */       this.beanFactory.registerContainedBean(actualInnerBeanName, this.beanName);
/* 190:272 */       if ((innerBean instanceof FactoryBean))
/* 191:    */       {
/* 192:273 */         boolean synthetic = (mbd != null) && (mbd.isSynthetic());
/* 193:274 */         return this.beanFactory.getObjectFromFactoryBean((FactoryBean)innerBean, actualInnerBeanName, !synthetic);
/* 194:    */       }
/* 195:277 */       return innerBean;
/* 196:    */     }
/* 197:    */     catch (BeansException ex)
/* 198:    */     {
/* 199:281 */       throw new BeanCreationException(
/* 200:282 */         this.beanDefinition.getResourceDescription(), this.beanName, 
/* 201:283 */         "Cannot create inner bean '" + innerBeanName + "' " + (
/* 202:284 */         (mbd != null) && (mbd.getBeanClassName() != null) ? "of type [" + mbd.getBeanClassName() + "] " : "") + 
/* 203:285 */         "while setting " + argName, ex);
/* 204:    */     }
/* 205:    */   }
/* 206:    */   
/* 207:    */   private String adaptInnerBeanName(String innerBeanName)
/* 208:    */   {
/* 209:296 */     String actualInnerBeanName = innerBeanName;
/* 210:297 */     int counter = 0;
/* 211:298 */     while (this.beanFactory.isBeanNameInUse(actualInnerBeanName))
/* 212:    */     {
/* 213:299 */       counter++;
/* 214:300 */       actualInnerBeanName = innerBeanName + "#" + counter;
/* 215:    */     }
/* 216:302 */     return actualInnerBeanName;
/* 217:    */   }
/* 218:    */   
/* 219:    */   private Object resolveReference(Object argName, RuntimeBeanReference ref)
/* 220:    */   {
/* 221:    */     try
/* 222:    */     {
/* 223:310 */       String refName = ref.getBeanName();
/* 224:311 */       refName = String.valueOf(evaluate(refName));
/* 225:312 */       if (ref.isToParent())
/* 226:    */       {
/* 227:313 */         if (this.beanFactory.getParentBeanFactory() == null) {
/* 228:314 */           throw new BeanCreationException(
/* 229:315 */             this.beanDefinition.getResourceDescription(), this.beanName, 
/* 230:316 */             "Can't resolve reference to bean '" + refName + 
/* 231:317 */             "' in parent factory: no parent factory available");
/* 232:    */         }
/* 233:319 */         return this.beanFactory.getParentBeanFactory().getBean(refName);
/* 234:    */       }
/* 235:322 */       Object bean = this.beanFactory.getBean(refName);
/* 236:323 */       this.beanFactory.registerDependentBean(refName, this.beanName);
/* 237:324 */       return bean;
/* 238:    */     }
/* 239:    */     catch (BeansException ex)
/* 240:    */     {
/* 241:328 */       throw new BeanCreationException(
/* 242:329 */         this.beanDefinition.getResourceDescription(), this.beanName, 
/* 243:330 */         "Cannot resolve reference to bean '" + ref.getBeanName() + "' while setting " + argName, ex);
/* 244:    */     }
/* 245:    */   }
/* 246:    */   
/* 247:    */   private Object resolveManagedArray(Object argName, List<?> ml, Class elementType)
/* 248:    */   {
/* 249:338 */     Object resolved = Array.newInstance(elementType, ml.size());
/* 250:339 */     for (int i = 0; i < ml.size(); i++) {
/* 251:340 */       Array.set(resolved, i, 
/* 252:341 */         resolveValueIfNecessary(new KeyedArgName(argName, Integer.valueOf(i)), ml.get(i)));
/* 253:    */     }
/* 254:343 */     return resolved;
/* 255:    */   }
/* 256:    */   
/* 257:    */   private List resolveManagedList(Object argName, List<?> ml)
/* 258:    */   {
/* 259:350 */     List<Object> resolved = new ArrayList(ml.size());
/* 260:351 */     for (int i = 0; i < ml.size(); i++) {
/* 261:352 */       resolved.add(
/* 262:353 */         resolveValueIfNecessary(new KeyedArgName(argName, Integer.valueOf(i)), ml.get(i)));
/* 263:    */     }
/* 264:355 */     return resolved;
/* 265:    */   }
/* 266:    */   
/* 267:    */   private Set resolveManagedSet(Object argName, Set<?> ms)
/* 268:    */   {
/* 269:362 */     Set<Object> resolved = new LinkedHashSet(ms.size());
/* 270:363 */     int i = 0;
/* 271:364 */     for (Object m : ms)
/* 272:    */     {
/* 273:365 */       resolved.add(resolveValueIfNecessary(new KeyedArgName(argName, Integer.valueOf(i)), m));
/* 274:366 */       i++;
/* 275:    */     }
/* 276:368 */     return resolved;
/* 277:    */   }
/* 278:    */   
/* 279:    */   private Map resolveManagedMap(Object argName, Map<?, ?> mm)
/* 280:    */   {
/* 281:375 */     Map<Object, Object> resolved = new LinkedHashMap(mm.size());
/* 282:376 */     for (Map.Entry entry : mm.entrySet())
/* 283:    */     {
/* 284:377 */       Object resolvedKey = resolveValueIfNecessary(argName, entry.getKey());
/* 285:378 */       Object resolvedValue = resolveValueIfNecessary(
/* 286:379 */         new KeyedArgName(argName, entry.getKey()), entry.getValue());
/* 287:380 */       resolved.put(resolvedKey, resolvedValue);
/* 288:    */     }
/* 289:382 */     return resolved;
/* 290:    */   }
/* 291:    */   
/* 292:    */   private static class KeyedArgName
/* 293:    */   {
/* 294:    */     private final Object argName;
/* 295:    */     private final Object key;
/* 296:    */     
/* 297:    */     public KeyedArgName(Object argName, Object key)
/* 298:    */     {
/* 299:396 */       this.argName = argName;
/* 300:397 */       this.key = key;
/* 301:    */     }
/* 302:    */     
/* 303:    */     public String toString()
/* 304:    */     {
/* 305:402 */       return 
/* 306:403 */         this.argName + " with key " + "[" + this.key + "]";
/* 307:    */     }
/* 308:    */   }
/* 309:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.BeanDefinitionValueResolver
 * JD-Core Version:    0.7.0.1
 */