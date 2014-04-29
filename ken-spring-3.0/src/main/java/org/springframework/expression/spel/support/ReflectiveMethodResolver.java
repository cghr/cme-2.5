/*   1:    */ package org.springframework.expression.spel.support;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Arrays;
/*   6:    */ import java.util.Comparator;
/*   7:    */ import java.util.HashMap;
/*   8:    */ import java.util.List;
/*   9:    */ import java.util.Map;
/*  10:    */ import org.springframework.core.MethodParameter;
/*  11:    */ import org.springframework.core.convert.TypeDescriptor;
/*  12:    */ import org.springframework.expression.AccessException;
/*  13:    */ import org.springframework.expression.EvaluationContext;
/*  14:    */ import org.springframework.expression.EvaluationException;
/*  15:    */ import org.springframework.expression.MethodExecutor;
/*  16:    */ import org.springframework.expression.MethodFilter;
/*  17:    */ import org.springframework.expression.MethodResolver;
/*  18:    */ import org.springframework.expression.TypeConverter;
/*  19:    */ import org.springframework.expression.spel.SpelEvaluationException;
/*  20:    */ import org.springframework.expression.spel.SpelMessage;
/*  21:    */ import org.springframework.util.CollectionUtils;
/*  22:    */ 
/*  23:    */ public class ReflectiveMethodResolver
/*  24:    */   implements MethodResolver
/*  25:    */ {
/*  26: 49 */   private static Method[] NO_METHODS = new Method[0];
/*  27: 51 */   private Map<Class<?>, MethodFilter> filters = null;
/*  28: 55 */   private boolean useDistance = false;
/*  29:    */   
/*  30:    */   public ReflectiveMethodResolver() {}
/*  31:    */   
/*  32:    */   public ReflectiveMethodResolver(boolean useDistance)
/*  33:    */   {
/*  34: 72 */     this.useDistance = useDistance;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public MethodExecutor resolve(EvaluationContext context, Object targetObject, String name, List<TypeDescriptor> argumentTypes)
/*  38:    */     throws AccessException
/*  39:    */   {
/*  40:    */     try
/*  41:    */     {
/*  42: 88 */       TypeConverter typeConverter = context.getTypeConverter();
/*  43: 89 */       Class<?> type = (targetObject instanceof Class) ? (Class)targetObject : targetObject.getClass();
/*  44: 90 */       Method[] methods = type.getMethods();
/*  45:    */       
/*  46:    */ 
/*  47: 93 */       MethodFilter filter = this.filters != null ? (MethodFilter)this.filters.get(type) : null;
/*  48: 94 */       if (filter != null)
/*  49:    */       {
/*  50: 95 */         List<Method> methodsForFiltering = new ArrayList();
/*  51: 96 */         for (Method method : methods) {
/*  52: 97 */           methodsForFiltering.add(method);
/*  53:    */         }
/*  54: 99 */         List<Method> methodsFiltered = filter.filter(methodsForFiltering);
/*  55:100 */         if (CollectionUtils.isEmpty(methodsFiltered)) {
/*  56:101 */           methods = NO_METHODS;
/*  57:    */         } else {
/*  58:104 */           methods = (Method[])methodsFiltered.toArray(new Method[methodsFiltered.size()]);
/*  59:    */         }
/*  60:    */       }
/*  61:108 */       Arrays.sort(methods, new Comparator()
/*  62:    */       {
/*  63:    */         public int compare(Method m1, Method m2)
/*  64:    */         {
/*  65:110 */           int m1pl = m1.getParameterTypes().length;
/*  66:111 */           int m2pl = m2.getParameterTypes().length;
/*  67:112 */           return new Integer(m1pl).compareTo(Integer.valueOf(m2pl));
/*  68:    */         }
/*  69:115 */       });
/*  70:116 */       Method closeMatch = null;
/*  71:117 */       int closeMatchDistance = 2147483647;
/*  72:118 */       int[] argsToConvert = (int[])null;
/*  73:119 */       Method matchRequiringConversion = null;
/*  74:120 */       boolean multipleOptions = false;
/*  75:122 */       for (Method method : methods) {
/*  76:123 */         if (!method.isBridge()) {
/*  77:126 */           if (method.getName().equals(name))
/*  78:    */           {
/*  79:127 */             Class[] paramTypes = method.getParameterTypes();
/*  80:128 */             List<TypeDescriptor> paramDescriptors = new ArrayList(paramTypes.length);
/*  81:129 */             for (int i = 0; i < paramTypes.length; i++) {
/*  82:130 */               paramDescriptors.add(new TypeDescriptor(new MethodParameter(method, i)));
/*  83:    */             }
/*  84:132 */             ReflectionHelper.ArgumentsMatchInfo matchInfo = null;
/*  85:133 */             if ((method.isVarArgs()) && (argumentTypes.size() >= paramTypes.length - 1)) {
/*  86:135 */               matchInfo = ReflectionHelper.compareArgumentsVarargs(paramDescriptors, argumentTypes, typeConverter);
/*  87:137 */             } else if (paramTypes.length == argumentTypes.size()) {
/*  88:139 */               matchInfo = ReflectionHelper.compareArguments(paramDescriptors, argumentTypes, typeConverter);
/*  89:    */             }
/*  90:141 */             if (matchInfo != null)
/*  91:    */             {
/*  92:142 */               if (matchInfo.kind == ReflectionHelper.ArgsMatchKind.EXACT) {
/*  93:143 */                 return new ReflectiveMethodExecutor(method, null);
/*  94:    */               }
/*  95:145 */               if (matchInfo.kind == ReflectionHelper.ArgsMatchKind.CLOSE)
/*  96:    */               {
/*  97:146 */                 if (!this.useDistance)
/*  98:    */                 {
/*  99:147 */                   closeMatch = method;
/* 100:    */                 }
/* 101:    */                 else
/* 102:    */                 {
/* 103:149 */                   int matchDistance = ReflectionHelper.getTypeDifferenceWeight(paramDescriptors, argumentTypes);
/* 104:150 */                   if (matchDistance < closeMatchDistance)
/* 105:    */                   {
/* 106:152 */                     closeMatchDistance = matchDistance;
/* 107:153 */                     closeMatch = method;
/* 108:    */                   }
/* 109:    */                 }
/* 110:    */               }
/* 111:157 */               else if (matchInfo.kind == ReflectionHelper.ArgsMatchKind.REQUIRES_CONVERSION)
/* 112:    */               {
/* 113:158 */                 if (matchRequiringConversion != null) {
/* 114:159 */                   multipleOptions = true;
/* 115:    */                 }
/* 116:161 */                 argsToConvert = matchInfo.argsRequiringConversion;
/* 117:162 */                 matchRequiringConversion = method;
/* 118:    */               }
/* 119:    */             }
/* 120:    */           }
/* 121:    */         }
/* 122:    */       }
/* 123:167 */       if (closeMatch != null) {
/* 124:168 */         return new ReflectiveMethodExecutor(closeMatch, null);
/* 125:    */       }
/* 126:170 */       if (matchRequiringConversion != null)
/* 127:    */       {
/* 128:171 */         if (multipleOptions) {
/* 129:172 */           throw new SpelEvaluationException(SpelMessage.MULTIPLE_POSSIBLE_METHODS, new Object[] { name });
/* 130:    */         }
/* 131:174 */         return new ReflectiveMethodExecutor(matchRequiringConversion, argsToConvert);
/* 132:    */       }
/* 133:177 */       return null;
/* 134:    */     }
/* 135:    */     catch (EvaluationException ex)
/* 136:    */     {
/* 137:181 */       throw new AccessException("Failed to resolve method", ex);
/* 138:    */     }
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void registerMethodFilter(Class<?> type, MethodFilter filter)
/* 142:    */   {
/* 143:186 */     if (this.filters == null) {
/* 144:187 */       this.filters = new HashMap();
/* 145:    */     }
/* 146:189 */     if (filter == null) {
/* 147:190 */       this.filters.remove(type);
/* 148:    */     } else {
/* 149:193 */       this.filters.put(type, filter);
/* 150:    */     }
/* 151:    */   }
/* 152:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.support.ReflectiveMethodResolver
 * JD-Core Version:    0.7.0.1
 */