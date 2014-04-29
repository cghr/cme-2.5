/*  1:   */ package org.springframework.web.method;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Method;
/*  4:   */ import java.lang.reflect.Proxy;
/*  5:   */ import java.util.Arrays;
/*  6:   */ import java.util.Collection;
/*  7:   */ import java.util.LinkedHashSet;
/*  8:   */ import java.util.Set;
/*  9:   */ import org.springframework.core.BridgeMethodResolver;
/* 10:   */ import org.springframework.util.ClassUtils;
/* 11:   */ import org.springframework.util.ReflectionUtils;
/* 12:   */ import org.springframework.util.ReflectionUtils.MethodCallback;
/* 13:   */ import org.springframework.util.ReflectionUtils.MethodFilter;
/* 14:   */ 
/* 15:   */ public abstract class HandlerMethodSelector
/* 16:   */ {
/* 17:   */   public static Set<Method> selectMethods(Class<?> handlerType, final ReflectionUtils.MethodFilter handlerMethodFilter)
/* 18:   */   {
/* 19:48 */     final Set<Method> handlerMethods = new LinkedHashSet();
/* 20:49 */     Set<Class<?>> handlerTypes = new LinkedHashSet();
/* 21:50 */     Class<?> specificHandlerType = null;
/* 22:51 */     if (!Proxy.isProxyClass(handlerType))
/* 23:   */     {
/* 24:52 */       handlerTypes.add(handlerType);
/* 25:53 */       specificHandlerType = handlerType;
/* 26:   */     }
/* 27:55 */     handlerTypes.addAll((Collection)Arrays.asList(handlerType.getInterfaces()));
/* 28:56 */     for (Class<?> currentHandlerType : handlerTypes)
/* 29:   */     {
/* 30:57 */       Class<?> targetClass = specificHandlerType != null ? specificHandlerType : currentHandlerType;
/* 31:58 */       ReflectionUtils.doWithMethods(currentHandlerType, new ReflectionUtils.MethodCallback()
/* 32:   */       {
/* 33:   */         public void doWith(Method method)
/* 34:   */         {
/* 35:60 */           Method specificMethod = ClassUtils.getMostSpecificMethod(method, HandlerMethodSelector.this);
/* 36:61 */           Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
/* 37:62 */           if ((handlerMethodFilter.matches(specificMethod)) && (
/* 38:63 */             (bridgedMethod == specificMethod) || (!handlerMethodFilter.matches(bridgedMethod)))) {
/* 39:64 */             handlerMethods.add(specificMethod);
/* 40:   */           }
/* 41:   */         }
/* 42:67 */       }, ReflectionUtils.USER_DECLARED_METHODS);
/* 43:   */     }
/* 44:69 */     return handlerMethods;
/* 45:   */   }
/* 46:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.HandlerMethodSelector
 * JD-Core Version:    0.7.0.1
 */