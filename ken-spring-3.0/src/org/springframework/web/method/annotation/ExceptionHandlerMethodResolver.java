/*   1:    */ package org.springframework.web.method.annotation;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Arrays;
/*   6:    */ import java.util.Collection;
/*   7:    */ import java.util.Collections;
/*   8:    */ import java.util.Iterator;
/*   9:    */ import java.util.List;
/*  10:    */ import java.util.Map;
/*  11:    */ import java.util.Set;
/*  12:    */ import java.util.concurrent.ConcurrentHashMap;
/*  13:    */ import org.springframework.core.ExceptionDepthComparator;
/*  14:    */ import org.springframework.core.annotation.AnnotationUtils;
/*  15:    */ import org.springframework.util.Assert;
/*  16:    */ import org.springframework.util.ClassUtils;
/*  17:    */ import org.springframework.util.ReflectionUtils.MethodFilter;
/*  18:    */ import org.springframework.web.bind.annotation.ExceptionHandler;
/*  19:    */ import org.springframework.web.method.HandlerMethodSelector;
/*  20:    */ 
/*  21:    */ public class ExceptionHandlerMethodResolver
/*  22:    */ {
/*  23: 48 */   private static final Method NO_METHOD_FOUND = ClassUtils.getMethodIfAvailable(System.class, "currentTimeMillis", new Class[0]);
/*  24: 51 */   private final Map<Class<? extends Throwable>, Method> mappedMethods = new ConcurrentHashMap();
/*  25: 54 */   private final Map<Class<? extends Throwable>, Method> exceptionLookupCache = new ConcurrentHashMap();
/*  26:    */   
/*  27:    */   public ExceptionHandlerMethodResolver(Class<?> handlerType)
/*  28:    */   {
/*  29: 65 */     init(HandlerMethodSelector.selectMethods(handlerType, EXCEPTION_HANDLER_METHODS));
/*  30:    */   }
/*  31:    */   
/*  32:    */   private void init(Set<Method> exceptionHandlerMethods)
/*  33:    */   {
/*  34:    */     Iterator localIterator2;
/*  35: 69 */     for (Iterator localIterator1 = exceptionHandlerMethods.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
/*  36:    */     {
/*  37: 69 */       Method method = (Method)localIterator1.next();
/*  38: 70 */       localIterator2 = detectMappedExceptions(method).iterator(); continue;Class<? extends Throwable> exceptionType = (Class)localIterator2.next();
/*  39: 71 */       addExceptionMapping(exceptionType, method);
/*  40:    */     }
/*  41:    */   }
/*  42:    */   
/*  43:    */   private List<Class<? extends Throwable>> detectMappedExceptions(Method method)
/*  44:    */   {
/*  45: 83 */     List<Class<? extends Throwable>> result = new ArrayList();
/*  46: 84 */     ExceptionHandler annotation = (ExceptionHandler)AnnotationUtils.findAnnotation(method, ExceptionHandler.class);
/*  47: 85 */     if (annotation != null) {
/*  48: 86 */       result.addAll((Collection)Arrays.asList(annotation.value()));
/*  49:    */     }
/*  50: 88 */     if (result.isEmpty()) {
/*  51: 89 */       for (Class<?> paramType : method.getParameterTypes()) {
/*  52: 90 */         if (Throwable.class.isAssignableFrom(paramType)) {
/*  53: 91 */           result.add(paramType);
/*  54:    */         }
/*  55:    */       }
/*  56:    */     }
/*  57: 95 */     Assert.notEmpty(result, "No exception types mapped to {" + method + "}");
/*  58: 96 */     return result;
/*  59:    */   }
/*  60:    */   
/*  61:    */   private void addExceptionMapping(Class<? extends Throwable> exceptionType, Method method)
/*  62:    */   {
/*  63:100 */     Method oldMethod = (Method)this.mappedMethods.put(exceptionType, method);
/*  64:101 */     if ((oldMethod != null) && (!oldMethod.equals(method))) {
/*  65:102 */       throw new IllegalStateException(
/*  66:103 */         "Ambiguous @ExceptionHandler method mapped for [" + exceptionType + "]: {" + 
/*  67:104 */         oldMethod + ", " + method + "}.");
/*  68:    */     }
/*  69:    */   }
/*  70:    */   
/*  71:    */   public Method resolveMethod(Exception exception)
/*  72:    */   {
/*  73:115 */     Class<? extends Exception> exceptionType = exception.getClass();
/*  74:116 */     Method method = (Method)this.exceptionLookupCache.get(exceptionType);
/*  75:117 */     if (method == null)
/*  76:    */     {
/*  77:118 */       method = getMappedMethod(exceptionType);
/*  78:119 */       this.exceptionLookupCache.put(exceptionType, method != null ? method : NO_METHOD_FOUND);
/*  79:    */     }
/*  80:121 */     return method != NO_METHOD_FOUND ? method : null;
/*  81:    */   }
/*  82:    */   
/*  83:    */   private Method getMappedMethod(Class<? extends Exception> exceptionType)
/*  84:    */   {
/*  85:128 */     List<Class<? extends Throwable>> matches = new ArrayList();
/*  86:129 */     for (Class<? extends Throwable> mappedException : this.mappedMethods.keySet()) {
/*  87:130 */       if (mappedException.isAssignableFrom(exceptionType)) {
/*  88:131 */         matches.add(mappedException);
/*  89:    */       }
/*  90:    */     }
/*  91:134 */     if (!matches.isEmpty())
/*  92:    */     {
/*  93:135 */       Collections.sort(matches, new ExceptionDepthComparator(exceptionType));
/*  94:136 */       return (Method)this.mappedMethods.get(matches.get(0));
/*  95:    */     }
/*  96:139 */     return null;
/*  97:    */   }
/*  98:    */   
/*  99:146 */   public static final ReflectionUtils.MethodFilter EXCEPTION_HANDLER_METHODS = new ReflectionUtils.MethodFilter()
/* 100:    */   {
/* 101:    */     public boolean matches(Method method)
/* 102:    */     {
/* 103:149 */       return AnnotationUtils.findAnnotation(method, ExceptionHandler.class) != null;
/* 104:    */     }
/* 105:    */   };
/* 106:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.annotation.ExceptionHandlerMethodResolver
 * JD-Core Version:    0.7.0.1
 */