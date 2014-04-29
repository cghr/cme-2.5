/*   1:    */ package org.springframework.web.bind.annotation.support;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.lang.reflect.Proxy;
/*   5:    */ import java.util.Arrays;
/*   6:    */ import java.util.Collection;
/*   7:    */ import java.util.Collections;
/*   8:    */ import java.util.HashSet;
/*   9:    */ import java.util.LinkedHashSet;
/*  10:    */ import java.util.Set;
/*  11:    */ import org.springframework.core.BridgeMethodResolver;
/*  12:    */ import org.springframework.core.annotation.AnnotationUtils;
/*  13:    */ import org.springframework.util.ClassUtils;
/*  14:    */ import org.springframework.util.ReflectionUtils;
/*  15:    */ import org.springframework.util.ReflectionUtils.MethodCallback;
/*  16:    */ import org.springframework.web.bind.annotation.InitBinder;
/*  17:    */ import org.springframework.web.bind.annotation.ModelAttribute;
/*  18:    */ import org.springframework.web.bind.annotation.RequestMapping;
/*  19:    */ import org.springframework.web.bind.annotation.SessionAttributes;
/*  20:    */ 
/*  21:    */ public class HandlerMethodResolver
/*  22:    */ {
/*  23: 53 */   private final Set<Method> handlerMethods = new LinkedHashSet();
/*  24: 55 */   private final Set<Method> initBinderMethods = new LinkedHashSet();
/*  25: 57 */   private final Set<Method> modelAttributeMethods = new LinkedHashSet();
/*  26:    */   private RequestMapping typeLevelMapping;
/*  27:    */   private boolean sessionAttributesFound;
/*  28: 63 */   private final Set<String> sessionAttributeNames = new HashSet();
/*  29: 65 */   private final Set<Class> sessionAttributeTypes = new HashSet();
/*  30: 67 */   private final Set<String> actualSessionAttributeNames = Collections.synchronizedSet(new HashSet(4));
/*  31:    */   
/*  32:    */   public void init(Class<?> handlerType)
/*  33:    */   {
/*  34: 75 */     Set<Class<?>> handlerTypes = new LinkedHashSet();
/*  35: 76 */     Class<?> specificHandlerType = null;
/*  36: 77 */     if (!Proxy.isProxyClass(handlerType))
/*  37:    */     {
/*  38: 78 */       handlerTypes.add(handlerType);
/*  39: 79 */       specificHandlerType = handlerType;
/*  40:    */     }
/*  41: 81 */     handlerTypes.addAll((Collection)Arrays.asList(handlerType.getInterfaces()));
/*  42: 82 */     for (Class<?> currentHandlerType : handlerTypes)
/*  43:    */     {
/*  44: 83 */       final Class<?> targetClass = specificHandlerType != null ? specificHandlerType : currentHandlerType;
/*  45: 84 */       ReflectionUtils.doWithMethods(currentHandlerType, new ReflectionUtils.MethodCallback()
/*  46:    */       {
/*  47:    */         public void doWith(Method method)
/*  48:    */         {
/*  49: 86 */           Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
/*  50: 87 */           Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
/*  51: 88 */           if ((HandlerMethodResolver.this.isHandlerMethod(specificMethod)) && (
/*  52: 89 */             (bridgedMethod == specificMethod) || (!HandlerMethodResolver.this.isHandlerMethod(bridgedMethod)))) {
/*  53: 90 */             HandlerMethodResolver.this.handlerMethods.add(specificMethod);
/*  54: 92 */           } else if ((HandlerMethodResolver.this.isInitBinderMethod(specificMethod)) && (
/*  55: 93 */             (bridgedMethod == specificMethod) || (!HandlerMethodResolver.this.isInitBinderMethod(bridgedMethod)))) {
/*  56: 94 */             HandlerMethodResolver.this.initBinderMethods.add(specificMethod);
/*  57: 96 */           } else if ((HandlerMethodResolver.this.isModelAttributeMethod(specificMethod)) && (
/*  58: 97 */             (bridgedMethod == specificMethod) || (!HandlerMethodResolver.this.isModelAttributeMethod(bridgedMethod)))) {
/*  59: 98 */             HandlerMethodResolver.this.modelAttributeMethods.add(specificMethod);
/*  60:    */           }
/*  61:    */         }
/*  62:101 */       }, ReflectionUtils.USER_DECLARED_METHODS);
/*  63:    */     }
/*  64:103 */     this.typeLevelMapping = ((RequestMapping)AnnotationUtils.findAnnotation(handlerType, RequestMapping.class));
/*  65:104 */     SessionAttributes sessionAttributes = (SessionAttributes)AnnotationUtils.findAnnotation(handlerType, SessionAttributes.class);
/*  66:105 */     this.sessionAttributesFound = (sessionAttributes != null);
/*  67:106 */     if (this.sessionAttributesFound)
/*  68:    */     {
/*  69:107 */       this.sessionAttributeNames.addAll((Collection)Arrays.asList(sessionAttributes.value()));
/*  70:108 */       this.sessionAttributeTypes.addAll((Collection)Arrays.asList(sessionAttributes.types()));
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   protected boolean isHandlerMethod(Method method)
/*  75:    */   {
/*  76:113 */     return AnnotationUtils.findAnnotation(method, RequestMapping.class) != null;
/*  77:    */   }
/*  78:    */   
/*  79:    */   protected boolean isInitBinderMethod(Method method)
/*  80:    */   {
/*  81:117 */     return AnnotationUtils.findAnnotation(method, InitBinder.class) != null;
/*  82:    */   }
/*  83:    */   
/*  84:    */   protected boolean isModelAttributeMethod(Method method)
/*  85:    */   {
/*  86:121 */     return AnnotationUtils.findAnnotation(method, ModelAttribute.class) != null;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public final boolean hasHandlerMethods()
/*  90:    */   {
/*  91:126 */     return !this.handlerMethods.isEmpty();
/*  92:    */   }
/*  93:    */   
/*  94:    */   public final Set<Method> getHandlerMethods()
/*  95:    */   {
/*  96:130 */     return this.handlerMethods;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public final Set<Method> getInitBinderMethods()
/* 100:    */   {
/* 101:134 */     return this.initBinderMethods;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public final Set<Method> getModelAttributeMethods()
/* 105:    */   {
/* 106:138 */     return this.modelAttributeMethods;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public boolean hasTypeLevelMapping()
/* 110:    */   {
/* 111:142 */     return this.typeLevelMapping != null;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public RequestMapping getTypeLevelMapping()
/* 115:    */   {
/* 116:146 */     return this.typeLevelMapping;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public boolean hasSessionAttributes()
/* 120:    */   {
/* 121:150 */     return this.sessionAttributesFound;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public boolean isSessionAttribute(String attrName, Class attrType)
/* 125:    */   {
/* 126:154 */     if ((this.sessionAttributeNames.contains(attrName)) || (this.sessionAttributeTypes.contains(attrType)))
/* 127:    */     {
/* 128:155 */       this.actualSessionAttributeNames.add(attrName);
/* 129:156 */       return true;
/* 130:    */     }
/* 131:159 */     return false;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public Set<String> getActualSessionAttributeNames()
/* 135:    */   {
/* 136:164 */     return this.actualSessionAttributeNames;
/* 137:    */   }
/* 138:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.bind.annotation.support.HandlerMethodResolver
 * JD-Core Version:    0.7.0.1
 */