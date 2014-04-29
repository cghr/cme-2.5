/*  1:   */ package org.springframework.cache.interceptor;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Method;
/*  4:   */ import java.util.Collection;
/*  5:   */ import java.util.Map;
/*  6:   */ import java.util.concurrent.ConcurrentHashMap;
/*  7:   */ import org.springframework.cache.Cache;
/*  8:   */ import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
/*  9:   */ import org.springframework.core.ParameterNameDiscoverer;
/* 10:   */ import org.springframework.expression.EvaluationContext;
/* 11:   */ import org.springframework.expression.Expression;
/* 12:   */ import org.springframework.expression.spel.standard.SpelExpressionParser;
/* 13:   */ 
/* 14:   */ class ExpressionEvaluator
/* 15:   */ {
/* 16:42 */   private SpelExpressionParser parser = new SpelExpressionParser();
/* 17:45 */   private ParameterNameDiscoverer paramNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
/* 18:47 */   private Map<Method, Expression> conditionCache = new ConcurrentHashMap();
/* 19:49 */   private Map<Method, Expression> keyCache = new ConcurrentHashMap();
/* 20:51 */   private Map<Method, Method> targetMethodCache = new ConcurrentHashMap();
/* 21:   */   
/* 22:   */   public EvaluationContext createEvaluationContext(Collection<Cache> caches, Method method, Object[] args, Object target, Class<?> targetClass)
/* 23:   */   {
/* 24:57 */     CacheExpressionRootObject rootObject = 
/* 25:58 */       new CacheExpressionRootObject(caches, method, args, target, targetClass);
/* 26:59 */     return new LazyParamAwareEvaluationContext(rootObject, 
/* 27:60 */       this.paramNameDiscoverer, method, args, targetClass, this.targetMethodCache);
/* 28:   */   }
/* 29:   */   
/* 30:   */   public boolean condition(String conditionExpression, Method method, EvaluationContext evalContext)
/* 31:   */   {
/* 32:64 */     Expression condExp = (Expression)this.conditionCache.get(method);
/* 33:65 */     if (condExp == null)
/* 34:   */     {
/* 35:66 */       condExp = this.parser.parseExpression(conditionExpression);
/* 36:67 */       this.conditionCache.put(method, condExp);
/* 37:   */     }
/* 38:69 */     return ((Boolean)condExp.getValue(evalContext, Boolean.TYPE)).booleanValue();
/* 39:   */   }
/* 40:   */   
/* 41:   */   public Object key(String keyExpression, Method method, EvaluationContext evalContext)
/* 42:   */   {
/* 43:73 */     Expression keyExp = (Expression)this.keyCache.get(method);
/* 44:74 */     if (keyExp == null)
/* 45:   */     {
/* 46:75 */       keyExp = this.parser.parseExpression(keyExpression);
/* 47:76 */       this.keyCache.put(method, keyExp);
/* 48:   */     }
/* 49:78 */     return keyExp.getValue(evalContext);
/* 50:   */   }
/* 51:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.interceptor.ExpressionEvaluator
 * JD-Core Version:    0.7.0.1
 */