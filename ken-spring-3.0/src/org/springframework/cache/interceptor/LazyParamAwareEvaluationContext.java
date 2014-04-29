/*   1:    */ package org.springframework.cache.interceptor;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.Map;
/*   5:    */ import org.springframework.aop.support.AopUtils;
/*   6:    */ import org.springframework.core.ParameterNameDiscoverer;
/*   7:    */ import org.springframework.expression.spel.support.StandardEvaluationContext;
/*   8:    */ import org.springframework.util.ObjectUtils;
/*   9:    */ 
/*  10:    */ class LazyParamAwareEvaluationContext
/*  11:    */   extends StandardEvaluationContext
/*  12:    */ {
/*  13:    */   private final ParameterNameDiscoverer paramDiscoverer;
/*  14:    */   private final Method method;
/*  15:    */   private final Object[] args;
/*  16:    */   private final Class<?> targetClass;
/*  17:    */   private final Map<Method, Method> methodCache;
/*  18: 50 */   private boolean paramLoaded = false;
/*  19:    */   
/*  20:    */   LazyParamAwareEvaluationContext(Object rootObject, ParameterNameDiscoverer paramDiscoverer, Method method, Object[] args, Class<?> targetClass, Map<Method, Method> methodCache)
/*  21:    */   {
/*  22: 55 */     super(rootObject);
/*  23:    */     
/*  24: 57 */     this.paramDiscoverer = paramDiscoverer;
/*  25: 58 */     this.method = method;
/*  26: 59 */     this.args = args;
/*  27: 60 */     this.targetClass = targetClass;
/*  28: 61 */     this.methodCache = methodCache;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public Object lookupVariable(String name)
/*  32:    */   {
/*  33: 70 */     Object variable = super.lookupVariable(name);
/*  34: 71 */     if (variable != null) {
/*  35: 72 */       return variable;
/*  36:    */     }
/*  37: 74 */     if (!this.paramLoaded)
/*  38:    */     {
/*  39: 75 */       loadArgsAsVariables();
/*  40: 76 */       this.paramLoaded = true;
/*  41: 77 */       variable = super.lookupVariable(name);
/*  42:    */     }
/*  43: 79 */     return variable;
/*  44:    */   }
/*  45:    */   
/*  46:    */   private void loadArgsAsVariables()
/*  47:    */   {
/*  48: 84 */     if (ObjectUtils.isEmpty(this.args)) {
/*  49: 85 */       return;
/*  50:    */     }
/*  51: 88 */     Method targetMethod = (Method)this.methodCache.get(this.method);
/*  52: 89 */     if (targetMethod == null)
/*  53:    */     {
/*  54: 90 */       targetMethod = AopUtils.getMostSpecificMethod(this.method, this.targetClass);
/*  55: 91 */       if (targetMethod == null) {
/*  56: 92 */         targetMethod = this.method;
/*  57:    */       }
/*  58: 94 */       this.methodCache.put(this.method, targetMethod);
/*  59:    */     }
/*  60: 98 */     for (int i = 0; i < this.args.length; i++) {
/*  61: 99 */       setVariable("p" + i, this.args[i]);
/*  62:    */     }
/*  63:102 */     String[] parameterNames = this.paramDiscoverer.getParameterNames(targetMethod);
/*  64:104 */     if (parameterNames != null) {
/*  65:105 */       for (int i = 0; i < parameterNames.length; i++) {
/*  66:106 */         setVariable(parameterNames[i], this.args[i]);
/*  67:    */       }
/*  68:    */     }
/*  69:    */   }
/*  70:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.interceptor.LazyParamAwareEvaluationContext
 * JD-Core Version:    0.7.0.1
 */