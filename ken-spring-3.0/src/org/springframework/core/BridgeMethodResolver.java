/*   1:    */ package org.springframework.core;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.GenericArrayType;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import java.lang.reflect.Type;
/*   6:    */ import java.lang.reflect.TypeVariable;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.Arrays;
/*   9:    */ import java.util.List;
/*  10:    */ import java.util.Map;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ import org.springframework.util.ClassUtils;
/*  13:    */ import org.springframework.util.ReflectionUtils;
/*  14:    */ 
/*  15:    */ public abstract class BridgeMethodResolver
/*  16:    */ {
/*  17:    */   public static Method findBridgedMethod(Method bridgeMethod)
/*  18:    */   {
/*  19: 62 */     if ((bridgeMethod == null) || (!bridgeMethod.isBridge())) {
/*  20: 63 */       return bridgeMethod;
/*  21:    */     }
/*  22: 66 */     List<Method> candidateMethods = new ArrayList();
/*  23: 67 */     Method[] methods = ReflectionUtils.getAllDeclaredMethods(bridgeMethod.getDeclaringClass());
/*  24: 68 */     for (Method candidateMethod : methods) {
/*  25: 69 */       if (isBridgedCandidateFor(candidateMethod, bridgeMethod)) {
/*  26: 70 */         candidateMethods.add(candidateMethod);
/*  27:    */       }
/*  28:    */     }
/*  29: 74 */     if (candidateMethods.size() == 1) {
/*  30: 75 */       return (Method)candidateMethods.get(0);
/*  31:    */     }
/*  32: 78 */     Method bridgedMethod = searchCandidates(candidateMethods, bridgeMethod);
/*  33: 79 */     if (bridgedMethod != null) {
/*  34: 81 */       return bridgedMethod;
/*  35:    */     }
/*  36: 86 */     return bridgeMethod;
/*  37:    */   }
/*  38:    */   
/*  39:    */   private static Method searchCandidates(List<Method> candidateMethods, Method bridgeMethod)
/*  40:    */   {
/*  41: 97 */     if (candidateMethods.isEmpty()) {
/*  42: 98 */       return null;
/*  43:    */     }
/*  44:100 */     Map<TypeVariable, Type> typeParameterMap = GenericTypeResolver.getTypeVariableMap(bridgeMethod.getDeclaringClass());
/*  45:101 */     Method previousMethod = null;
/*  46:102 */     boolean sameSig = true;
/*  47:103 */     for (Method candidateMethod : candidateMethods)
/*  48:    */     {
/*  49:104 */       if (isBridgeMethodFor(bridgeMethod, candidateMethod, typeParameterMap)) {
/*  50:105 */         return candidateMethod;
/*  51:    */       }
/*  52:107 */       if (previousMethod != null) {
/*  53:108 */         sameSig = (sameSig) && 
/*  54:109 */           (Arrays.equals(candidateMethod.getGenericParameterTypes(), previousMethod.getGenericParameterTypes()));
/*  55:    */       }
/*  56:111 */       previousMethod = candidateMethod;
/*  57:    */     }
/*  58:113 */     return sameSig ? (Method)candidateMethods.get(0) : null;
/*  59:    */   }
/*  60:    */   
/*  61:    */   private static boolean isBridgedCandidateFor(Method candidateMethod, Method bridgeMethod)
/*  62:    */   {
/*  63:125 */     return (!candidateMethod.isBridge()) && (!candidateMethod.equals(bridgeMethod)) && (candidateMethod.getName().equals(bridgeMethod.getName())) && (candidateMethod.getParameterTypes().length == bridgeMethod.getParameterTypes().length);
/*  64:    */   }
/*  65:    */   
/*  66:    */   static boolean isBridgeMethodFor(Method bridgeMethod, Method candidateMethod, Map<TypeVariable, Type> typeVariableMap)
/*  67:    */   {
/*  68:133 */     if (isResolvedTypeMatch(candidateMethod, bridgeMethod, typeVariableMap)) {
/*  69:134 */       return true;
/*  70:    */     }
/*  71:136 */     Method method = findGenericDeclaration(bridgeMethod);
/*  72:137 */     return (method != null) && (isResolvedTypeMatch(method, candidateMethod, typeVariableMap));
/*  73:    */   }
/*  74:    */   
/*  75:    */   private static Method findGenericDeclaration(Method bridgeMethod)
/*  76:    */   {
/*  77:147 */     Class superclass = bridgeMethod.getDeclaringClass().getSuperclass();
/*  78:148 */     while (!Object.class.equals(superclass))
/*  79:    */     {
/*  80:149 */       Method method = searchForMatch(superclass, bridgeMethod);
/*  81:150 */       if ((method != null) && (!method.isBridge())) {
/*  82:151 */         return method;
/*  83:    */       }
/*  84:153 */       superclass = superclass.getSuperclass();
/*  85:    */     }
/*  86:157 */     Class[] interfaces = ClassUtils.getAllInterfacesForClass(bridgeMethod.getDeclaringClass());
/*  87:158 */     for (Class ifc : interfaces)
/*  88:    */     {
/*  89:159 */       Method method = searchForMatch(ifc, bridgeMethod);
/*  90:160 */       if ((method != null) && (!method.isBridge())) {
/*  91:161 */         return method;
/*  92:    */       }
/*  93:    */     }
/*  94:165 */     return null;
/*  95:    */   }
/*  96:    */   
/*  97:    */   private static boolean isResolvedTypeMatch(Method genericMethod, Method candidateMethod, Map<TypeVariable, Type> typeVariableMap)
/*  98:    */   {
/*  99:177 */     Type[] genericParameters = genericMethod.getGenericParameterTypes();
/* 100:178 */     Class[] candidateParameters = candidateMethod.getParameterTypes();
/* 101:179 */     if (genericParameters.length != candidateParameters.length) {
/* 102:180 */       return false;
/* 103:    */     }
/* 104:182 */     for (int i = 0; i < genericParameters.length; i++)
/* 105:    */     {
/* 106:183 */       Type genericParameter = genericParameters[i];
/* 107:184 */       Class candidateParameter = candidateParameters[i];
/* 108:185 */       if (candidateParameter.isArray())
/* 109:    */       {
/* 110:187 */         Type rawType = GenericTypeResolver.getRawType(genericParameter, typeVariableMap);
/* 111:188 */         if ((rawType instanceof GenericArrayType))
/* 112:    */         {
/* 113:189 */           if (candidateParameter.getComponentType().equals(
/* 114:190 */             GenericTypeResolver.resolveType(((GenericArrayType)rawType).getGenericComponentType(), typeVariableMap))) {
/* 115:    */             break;
/* 116:    */           }
/* 117:191 */           return false;
/* 118:    */         }
/* 119:    */       }
/* 120:197 */       Class resolvedParameter = GenericTypeResolver.resolveType(genericParameter, typeVariableMap);
/* 121:198 */       if (!candidateParameter.equals(resolvedParameter)) {
/* 122:199 */         return false;
/* 123:    */       }
/* 124:    */     }
/* 125:202 */     return true;
/* 126:    */   }
/* 127:    */   
/* 128:    */   private static Method searchForMatch(Class type, Method bridgeMethod)
/* 129:    */   {
/* 130:211 */     return ReflectionUtils.findMethod(type, bridgeMethod.getName(), bridgeMethod.getParameterTypes());
/* 131:    */   }
/* 132:    */   
/* 133:    */   public static boolean isJava6VisibilityBridgeMethodPair(Method bridgeMethod, Method bridgedMethod)
/* 134:    */   {
/* 135:222 */     Assert.isTrue(bridgeMethod != null);
/* 136:223 */     Assert.isTrue(bridgedMethod != null);
/* 137:224 */     if (bridgeMethod == bridgedMethod) {
/* 138:225 */       return true;
/* 139:    */     }
/* 140:228 */     return (Arrays.equals(bridgeMethod.getParameterTypes(), bridgedMethod.getParameterTypes())) && (bridgeMethod.getReturnType().equals(bridgedMethod.getReturnType()));
/* 141:    */   }
/* 142:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.BridgeMethodResolver
 * JD-Core Version:    0.7.0.1
 */