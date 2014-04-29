/*   1:    */ package org.springframework.util;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.InvocationTargetException;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import java.lang.reflect.Modifier;
/*   6:    */ 
/*   7:    */ public class MethodInvoker
/*   8:    */ {
/*   9:    */   private Class targetClass;
/*  10:    */   private Object targetObject;
/*  11:    */   private String targetMethod;
/*  12:    */   private String staticMethod;
/*  13: 51 */   private Object[] arguments = new Object[0];
/*  14:    */   private Method methodObject;
/*  15:    */   
/*  16:    */   public void setTargetClass(Class targetClass)
/*  17:    */   {
/*  18: 65 */     this.targetClass = targetClass;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public Class getTargetClass()
/*  22:    */   {
/*  23: 72 */     return this.targetClass;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setTargetObject(Object targetObject)
/*  27:    */   {
/*  28: 83 */     this.targetObject = targetObject;
/*  29: 84 */     if (targetObject != null) {
/*  30: 85 */       this.targetClass = targetObject.getClass();
/*  31:    */     }
/*  32:    */   }
/*  33:    */   
/*  34:    */   public Object getTargetObject()
/*  35:    */   {
/*  36: 93 */     return this.targetObject;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setTargetMethod(String targetMethod)
/*  40:    */   {
/*  41:104 */     this.targetMethod = targetMethod;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public String getTargetMethod()
/*  45:    */   {
/*  46:111 */     return this.targetMethod;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void setStaticMethod(String staticMethod)
/*  50:    */   {
/*  51:122 */     this.staticMethod = staticMethod;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setArguments(Object[] arguments)
/*  55:    */   {
/*  56:130 */     this.arguments = (arguments != null ? arguments : new Object[0]);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public Object[] getArguments()
/*  60:    */   {
/*  61:137 */     return this.arguments;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void prepare()
/*  65:    */     throws ClassNotFoundException, NoSuchMethodException
/*  66:    */   {
/*  67:148 */     if (this.staticMethod != null)
/*  68:    */     {
/*  69:149 */       int lastDotIndex = this.staticMethod.lastIndexOf('.');
/*  70:150 */       if ((lastDotIndex == -1) || (lastDotIndex == this.staticMethod.length())) {
/*  71:151 */         throw new IllegalArgumentException(
/*  72:152 */           "staticMethod must be a fully qualified class plus method name: e.g. 'example.MyExampleClass.myExampleMethod'");
/*  73:    */       }
/*  74:155 */       String className = this.staticMethod.substring(0, lastDotIndex);
/*  75:156 */       String methodName = this.staticMethod.substring(lastDotIndex + 1);
/*  76:157 */       this.targetClass = resolveClassName(className);
/*  77:158 */       this.targetMethod = methodName;
/*  78:    */     }
/*  79:161 */     Class targetClass = getTargetClass();
/*  80:162 */     String targetMethod = getTargetMethod();
/*  81:163 */     if (targetClass == null) {
/*  82:164 */       throw new IllegalArgumentException("Either 'targetClass' or 'targetObject' is required");
/*  83:    */     }
/*  84:166 */     if (targetMethod == null) {
/*  85:167 */       throw new IllegalArgumentException("Property 'targetMethod' is required");
/*  86:    */     }
/*  87:170 */     Object[] arguments = getArguments();
/*  88:171 */     Class[] argTypes = new Class[arguments.length];
/*  89:172 */     for (int i = 0; i < arguments.length; i++) {
/*  90:173 */       argTypes[i] = (arguments[i] != null ? arguments[i].getClass() : Object.class);
/*  91:    */     }
/*  92:    */     try
/*  93:    */     {
/*  94:178 */       this.methodObject = targetClass.getMethod(targetMethod, argTypes);
/*  95:    */     }
/*  96:    */     catch (NoSuchMethodException ex)
/*  97:    */     {
/*  98:182 */       this.methodObject = findMatchingMethod();
/*  99:183 */       if (this.methodObject == null) {
/* 100:184 */         throw ex;
/* 101:    */       }
/* 102:    */     }
/* 103:    */   }
/* 104:    */   
/* 105:    */   protected Class resolveClassName(String className)
/* 106:    */     throws ClassNotFoundException
/* 107:    */   {
/* 108:198 */     return ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
/* 109:    */   }
/* 110:    */   
/* 111:    */   protected Method findMatchingMethod()
/* 112:    */   {
/* 113:209 */     String targetMethod = getTargetMethod();
/* 114:210 */     Object[] arguments = getArguments();
/* 115:211 */     int argCount = arguments.length;
/* 116:    */     
/* 117:213 */     Method[] candidates = ReflectionUtils.getAllDeclaredMethods(getTargetClass());
/* 118:214 */     int minTypeDiffWeight = 2147483647;
/* 119:215 */     Method matchingMethod = null;
/* 120:217 */     for (Method candidate : candidates) {
/* 121:218 */       if (candidate.getName().equals(targetMethod))
/* 122:    */       {
/* 123:219 */         Class[] paramTypes = candidate.getParameterTypes();
/* 124:220 */         if (paramTypes.length == argCount)
/* 125:    */         {
/* 126:221 */           int typeDiffWeight = getTypeDifferenceWeight(paramTypes, arguments);
/* 127:222 */           if (typeDiffWeight < minTypeDiffWeight)
/* 128:    */           {
/* 129:223 */             minTypeDiffWeight = typeDiffWeight;
/* 130:224 */             matchingMethod = candidate;
/* 131:    */           }
/* 132:    */         }
/* 133:    */       }
/* 134:    */     }
/* 135:230 */     return matchingMethod;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public Method getPreparedMethod()
/* 139:    */     throws IllegalStateException
/* 140:    */   {
/* 141:242 */     if (this.methodObject == null) {
/* 142:243 */       throw new IllegalStateException("prepare() must be called prior to invoke() on MethodInvoker");
/* 143:    */     }
/* 144:245 */     return this.methodObject;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public boolean isPrepared()
/* 148:    */   {
/* 149:253 */     return this.methodObject != null;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public Object invoke()
/* 153:    */     throws InvocationTargetException, IllegalAccessException
/* 154:    */   {
/* 155:267 */     Object targetObject = getTargetObject();
/* 156:268 */     Method preparedMethod = getPreparedMethod();
/* 157:269 */     if ((targetObject == null) && (!Modifier.isStatic(preparedMethod.getModifiers()))) {
/* 158:270 */       throw new IllegalArgumentException("Target method must not be non-static without a target");
/* 159:    */     }
/* 160:272 */     ReflectionUtils.makeAccessible(preparedMethod);
/* 161:273 */     return preparedMethod.invoke(targetObject, getArguments());
/* 162:    */   }
/* 163:    */   
/* 164:    */   public static int getTypeDifferenceWeight(Class[] paramTypes, Object[] args)
/* 165:    */   {
/* 166:295 */     int result = 0;
/* 167:296 */     for (int i = 0; i < paramTypes.length; i++)
/* 168:    */     {
/* 169:297 */       if (!ClassUtils.isAssignableValue(paramTypes[i], args[i])) {
/* 170:298 */         return 2147483647;
/* 171:    */       }
/* 172:300 */       if (args[i] != null)
/* 173:    */       {
/* 174:301 */         Class paramType = paramTypes[i];
/* 175:302 */         Class superClass = args[i].getClass().getSuperclass();
/* 176:303 */         while (superClass != null) {
/* 177:304 */           if (paramType.equals(superClass))
/* 178:    */           {
/* 179:305 */             result += 2;
/* 180:306 */             superClass = null;
/* 181:    */           }
/* 182:308 */           else if (ClassUtils.isAssignable(paramType, superClass))
/* 183:    */           {
/* 184:309 */             result += 2;
/* 185:310 */             superClass = superClass.getSuperclass();
/* 186:    */           }
/* 187:    */           else
/* 188:    */           {
/* 189:313 */             superClass = null;
/* 190:    */           }
/* 191:    */         }
/* 192:316 */         if (paramType.isInterface()) {
/* 193:317 */           result++;
/* 194:    */         }
/* 195:    */       }
/* 196:    */     }
/* 197:321 */     return result;
/* 198:    */   }
/* 199:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.MethodInvoker
 * JD-Core Version:    0.7.0.1
 */