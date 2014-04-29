/*   1:    */ package org.springframework.cache.interceptor;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Collection;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.Set;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.apache.commons.logging.LogFactory;
/*  10:    */ import org.springframework.aop.framework.AopProxyUtils;
/*  11:    */ import org.springframework.beans.factory.InitializingBean;
/*  12:    */ import org.springframework.cache.Cache;
/*  13:    */ import org.springframework.cache.Cache.ValueWrapper;
/*  14:    */ import org.springframework.cache.CacheManager;
/*  15:    */ import org.springframework.expression.EvaluationContext;
/*  16:    */ import org.springframework.util.Assert;
/*  17:    */ import org.springframework.util.ClassUtils;
/*  18:    */ import org.springframework.util.StringUtils;
/*  19:    */ 
/*  20:    */ public abstract class CacheAspectSupport
/*  21:    */   implements InitializingBean
/*  22:    */ {
/*  23: 62 */   protected final Log logger = LogFactory.getLog(getClass());
/*  24:    */   private CacheManager cacheManager;
/*  25:    */   private CacheOperationSource cacheOperationSource;
/*  26: 68 */   private final ExpressionEvaluator evaluator = new ExpressionEvaluator();
/*  27: 70 */   private KeyGenerator keyGenerator = new DefaultKeyGenerator();
/*  28: 72 */   private boolean initialized = false;
/*  29:    */   
/*  30:    */   public void setCacheManager(CacheManager cacheManager)
/*  31:    */   {
/*  32: 79 */     this.cacheManager = cacheManager;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public CacheManager getCacheManager()
/*  36:    */   {
/*  37: 86 */     return this.cacheManager;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setCacheOperationSources(CacheOperationSource... cacheDefinitionSources)
/*  41:    */   {
/*  42: 94 */     Assert.notEmpty(cacheDefinitionSources);
/*  43: 95 */     this.cacheOperationSource = (cacheDefinitionSources.length > 1 ? 
/*  44: 96 */       new CompositeCacheOperationSource(cacheDefinitionSources) : cacheDefinitionSources[0]);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setCacheOperationSource(CacheOperationSource cacheOperationSource)
/*  48:    */   {
/*  49:104 */     this.cacheOperationSource = cacheOperationSource;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public CacheOperationSource getCacheOperationSource()
/*  53:    */   {
/*  54:111 */     return this.cacheOperationSource;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setKeyGenerator(KeyGenerator keyGenerator)
/*  58:    */   {
/*  59:119 */     this.keyGenerator = keyGenerator;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public KeyGenerator getKeyGenerator()
/*  63:    */   {
/*  64:126 */     return this.keyGenerator;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void afterPropertiesSet()
/*  68:    */   {
/*  69:130 */     if (this.cacheManager == null) {
/*  70:131 */       throw new IllegalStateException("'cacheManager' is required");
/*  71:    */     }
/*  72:133 */     if (this.cacheOperationSource == null) {
/*  73:134 */       throw new IllegalStateException("Either 'cacheDefinitionSource' or 'cacheDefinitionSources' is required: If there are no cacheable methods, then don't use a cache aspect.");
/*  74:    */     }
/*  75:138 */     this.initialized = true;
/*  76:    */   }
/*  77:    */   
/*  78:    */   protected String methodIdentification(Method method, Class<?> targetClass)
/*  79:    */   {
/*  80:152 */     Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
/*  81:153 */     return ClassUtils.getQualifiedMethodName(specificMethod);
/*  82:    */   }
/*  83:    */   
/*  84:    */   protected Collection<Cache> getCaches(CacheOperation operation)
/*  85:    */   {
/*  86:158 */     Set<String> cacheNames = operation.getCacheNames();
/*  87:159 */     Collection<Cache> caches = new ArrayList(cacheNames.size());
/*  88:160 */     for (String cacheName : cacheNames)
/*  89:    */     {
/*  90:161 */       Cache cache = this.cacheManager.getCache(cacheName);
/*  91:162 */       if (cache == null) {
/*  92:163 */         throw new IllegalArgumentException("Cannot find cache named [" + cacheName + "] for " + operation);
/*  93:    */       }
/*  94:165 */       caches.add(cache);
/*  95:    */     }
/*  96:167 */     return caches;
/*  97:    */   }
/*  98:    */   
/*  99:    */   protected CacheOperationContext getOperationContext(CacheOperation operation, Method method, Object[] args, Object target, Class<?> targetClass)
/* 100:    */   {
/* 101:173 */     return new CacheOperationContext(operation, method, args, target, targetClass);
/* 102:    */   }
/* 103:    */   
/* 104:    */   protected Object execute(Invoker invoker, Object target, Method method, Object[] args)
/* 105:    */   {
/* 106:179 */     if (!this.initialized) {
/* 107:180 */       return invoker.invoke();
/* 108:    */     }
/* 109:183 */     boolean log = this.logger.isTraceEnabled();
/* 110:    */     
/* 111:    */ 
/* 112:186 */     Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
/* 113:187 */     if ((targetClass == null) && (target != null)) {
/* 114:188 */       targetClass = target.getClass();
/* 115:    */     }
/* 116:190 */     CacheOperation cacheOp = getCacheOperationSource().getCacheOperation(method, targetClass);
/* 117:    */     
/* 118:192 */     Object retVal = null;
/* 119:195 */     if (cacheOp != null)
/* 120:    */     {
/* 121:196 */       CacheOperationContext context = getOperationContext(cacheOp, method, args, target, targetClass);
/* 122:197 */       Collection<Cache> caches = context.getCaches();
/* 123:199 */       if (context.hasConditionPassed())
/* 124:    */       {
/* 125:    */         Cache cache;
/* 126:201 */         if ((cacheOp instanceof CacheUpdateOperation))
/* 127:    */         {
/* 128:202 */           Object key = context.generateKey();
/* 129:203 */           if (log) {
/* 130:204 */             this.logger.trace("Computed cache key " + key + " for definition " + cacheOp);
/* 131:    */           }
/* 132:206 */           if (key == null) {
/* 133:207 */             throw new IllegalArgumentException(
/* 134:208 */               "Null key returned for cache definition (maybe you are using named params on classes without debug info?) " + 
/* 135:209 */               cacheOp);
/* 136:    */           }
/* 137:213 */           boolean cacheHit = false;
/* 138:215 */           for (Iterator<Cache> iterator = caches.iterator(); (iterator.hasNext()) && (!cacheHit);)
/* 139:    */           {
/* 140:216 */             cache = (Cache)iterator.next();
/* 141:217 */             Cache.ValueWrapper wrapper = cache.get(key);
/* 142:218 */             if (wrapper != null)
/* 143:    */             {
/* 144:219 */               cacheHit = true;
/* 145:220 */               retVal = wrapper.get();
/* 146:    */             }
/* 147:    */           }
/* 148:224 */           if (!cacheHit)
/* 149:    */           {
/* 150:225 */             if (log) {
/* 151:226 */               this.logger.trace("Key " + key + " NOT found in cache(s), invoking cached target method  " + 
/* 152:227 */                 method);
/* 153:    */             }
/* 154:229 */             retVal = invoker.invoke();
/* 155:231 */             for (Cache cache : caches) {
/* 156:232 */               cache.put(key, retVal);
/* 157:    */             }
/* 158:    */           }
/* 159:236 */           else if (log)
/* 160:    */           {
/* 161:237 */             this.logger.trace("Key " + key + " found in cache, returning value " + retVal);
/* 162:    */           }
/* 163:    */         }
/* 164:242 */         if ((cacheOp instanceof CacheEvictOperation))
/* 165:    */         {
/* 166:243 */           CacheEvictOperation evictOp = (CacheEvictOperation)cacheOp;
/* 167:    */           
/* 168:    */ 
/* 169:    */ 
/* 170:247 */           Object key = null;
/* 171:249 */           for (Cache cache : caches) {
/* 172:251 */             if (evictOp.isCacheWide())
/* 173:    */             {
/* 174:252 */               cache.clear();
/* 175:253 */               if (log) {
/* 176:254 */                 this.logger.trace("Invalidating entire cache for definition " + cacheOp + 
/* 177:255 */                   " on method " + method);
/* 178:    */               }
/* 179:    */             }
/* 180:    */             else
/* 181:    */             {
/* 182:260 */               if (key == null) {
/* 183:261 */                 key = context.generateKey();
/* 184:    */               }
/* 185:263 */               if (log) {
/* 186:264 */                 this.logger.trace("Invalidating cache key " + key + " for definition " + cacheOp + 
/* 187:265 */                   " on method " + method);
/* 188:    */               }
/* 189:267 */               cache.evict(key);
/* 190:    */             }
/* 191:    */           }
/* 192:270 */           retVal = invoker.invoke();
/* 193:    */         }
/* 194:272 */         return retVal;
/* 195:    */       }
/* 196:275 */       if (log) {
/* 197:276 */         this.logger.trace("Cache condition failed on method " + method + " for definition " + cacheOp);
/* 198:    */       }
/* 199:    */     }
/* 200:281 */     return invoker.invoke();
/* 201:    */   }
/* 202:    */   
/* 203:    */   protected class CacheOperationContext
/* 204:    */   {
/* 205:    */     private final CacheOperation operation;
/* 206:    */     private final Collection<Cache> caches;
/* 207:    */     private final Object target;
/* 208:    */     private final Method method;
/* 209:    */     private final Object[] args;
/* 210:    */     private final EvaluationContext evalContext;
/* 211:    */     
/* 212:    */     public CacheOperationContext(Method operation, Object[] method, Object args, Class<?> target)
/* 213:    */     {
/* 214:302 */       this.operation = operation;
/* 215:303 */       this.caches = CacheAspectSupport.this.getCaches(operation);
/* 216:304 */       this.target = target;
/* 217:305 */       this.method = method;
/* 218:306 */       this.args = args;
/* 219:    */       
/* 220:308 */       this.evalContext = CacheAspectSupport.this.evaluator.createEvaluationContext(this.caches, method, args, target, targetClass);
/* 221:    */     }
/* 222:    */     
/* 223:    */     protected boolean hasConditionPassed()
/* 224:    */     {
/* 225:312 */       if (StringUtils.hasText(this.operation.getCondition())) {
/* 226:313 */         return CacheAspectSupport.this.evaluator.condition(this.operation.getCondition(), this.method, this.evalContext);
/* 227:    */       }
/* 228:315 */       return true;
/* 229:    */     }
/* 230:    */     
/* 231:    */     protected Object generateKey()
/* 232:    */     {
/* 233:323 */       if (StringUtils.hasText(this.operation.getKey())) {
/* 234:324 */         return CacheAspectSupport.this.evaluator.key(this.operation.getKey(), this.method, this.evalContext);
/* 235:    */       }
/* 236:326 */       return CacheAspectSupport.this.keyGenerator.extract(this.target, this.method, this.args);
/* 237:    */     }
/* 238:    */     
/* 239:    */     protected Collection<Cache> getCaches()
/* 240:    */     {
/* 241:330 */       return this.caches;
/* 242:    */     }
/* 243:    */   }
/* 244:    */   
/* 245:    */   public static abstract interface Invoker
/* 246:    */   {
/* 247:    */     public abstract Object invoke();
/* 248:    */   }
/* 249:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.interceptor.CacheAspectSupport
 * JD-Core Version:    0.7.0.1
 */