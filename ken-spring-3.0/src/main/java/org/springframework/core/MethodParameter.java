/*   1:    */ package org.springframework.core;
/*   2:    */ 
/*   3:    */ import java.lang.annotation.Annotation;
/*   4:    */ import java.lang.reflect.AnnotatedElement;
/*   5:    */ import java.lang.reflect.Constructor;
/*   6:    */ import java.lang.reflect.Member;
/*   7:    */ import java.lang.reflect.Method;
/*   8:    */ import java.lang.reflect.Type;
/*   9:    */ import java.lang.reflect.TypeVariable;
/*  10:    */ import java.util.HashMap;
/*  11:    */ import java.util.Map;
/*  12:    */ import org.springframework.util.Assert;
/*  13:    */ 
/*  14:    */ public class MethodParameter
/*  15:    */ {
/*  16:    */   private final Method method;
/*  17:    */   private final Constructor constructor;
/*  18:    */   private final int parameterIndex;
/*  19:    */   private Class<?> parameterType;
/*  20:    */   private Type genericParameterType;
/*  21:    */   private Annotation[] parameterAnnotations;
/*  22:    */   private ParameterNameDiscoverer parameterNameDiscoverer;
/*  23:    */   private String parameterName;
/*  24: 60 */   private int nestingLevel = 1;
/*  25:    */   Map<Integer, Integer> typeIndexesPerLevel;
/*  26:    */   Map<TypeVariable, Type> typeVariableMap;
/*  27: 67 */   private int hash = 0;
/*  28:    */   
/*  29:    */   public MethodParameter(Method method, int parameterIndex)
/*  30:    */   {
/*  31: 76 */     this(method, parameterIndex, 1);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public MethodParameter(Method method, int parameterIndex, int nestingLevel)
/*  35:    */   {
/*  36: 90 */     Assert.notNull(method, "Method must not be null");
/*  37: 91 */     this.method = method;
/*  38: 92 */     this.parameterIndex = parameterIndex;
/*  39: 93 */     this.nestingLevel = nestingLevel;
/*  40: 94 */     this.constructor = null;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public MethodParameter(Constructor constructor, int parameterIndex)
/*  44:    */   {
/*  45:103 */     this(constructor, parameterIndex, 1);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public MethodParameter(Constructor constructor, int parameterIndex, int nestingLevel)
/*  49:    */   {
/*  50:115 */     Assert.notNull(constructor, "Constructor must not be null");
/*  51:116 */     this.constructor = constructor;
/*  52:117 */     this.parameterIndex = parameterIndex;
/*  53:118 */     this.nestingLevel = nestingLevel;
/*  54:119 */     this.method = null;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public MethodParameter(MethodParameter original)
/*  58:    */   {
/*  59:128 */     Assert.notNull(original, "Original must not be null");
/*  60:129 */     this.method = original.method;
/*  61:130 */     this.constructor = original.constructor;
/*  62:131 */     this.parameterIndex = original.parameterIndex;
/*  63:132 */     this.parameterType = original.parameterType;
/*  64:133 */     this.genericParameterType = original.genericParameterType;
/*  65:134 */     this.parameterAnnotations = original.parameterAnnotations;
/*  66:135 */     this.parameterNameDiscoverer = original.parameterNameDiscoverer;
/*  67:136 */     this.parameterName = original.parameterName;
/*  68:137 */     this.nestingLevel = original.nestingLevel;
/*  69:138 */     this.typeIndexesPerLevel = original.typeIndexesPerLevel;
/*  70:139 */     this.typeVariableMap = original.typeVariableMap;
/*  71:140 */     this.hash = original.hash;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public Method getMethod()
/*  75:    */   {
/*  76:150 */     return this.method;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public Constructor getConstructor()
/*  80:    */   {
/*  81:159 */     return this.constructor;
/*  82:    */   }
/*  83:    */   
/*  84:    */   private Member getMember()
/*  85:    */   {
/*  86:167 */     return this.method != null ? this.method : this.constructor;
/*  87:    */   }
/*  88:    */   
/*  89:    */   private AnnotatedElement getAnnotatedElement()
/*  90:    */   {
/*  91:175 */     return this.method != null ? this.method : this.constructor;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public Class getDeclaringClass()
/*  95:    */   {
/*  96:182 */     return getMember().getDeclaringClass();
/*  97:    */   }
/*  98:    */   
/*  99:    */   public int getParameterIndex()
/* 100:    */   {
/* 101:190 */     return this.parameterIndex;
/* 102:    */   }
/* 103:    */   
/* 104:    */   void setParameterType(Class<?> parameterType)
/* 105:    */   {
/* 106:197 */     this.parameterType = parameterType;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public Class<?> getParameterType()
/* 110:    */   {
/* 111:205 */     if (this.parameterType == null) {
/* 112:206 */       if (this.parameterIndex < 0) {
/* 113:207 */         this.parameterType = (this.method != null ? this.method.getReturnType() : null);
/* 114:    */       } else {
/* 115:210 */         this.parameterType = (this.method != null ? 
/* 116:211 */           this.method.getParameterTypes()[this.parameterIndex] : 
/* 117:212 */           this.constructor.getParameterTypes()[this.parameterIndex]);
/* 118:    */       }
/* 119:    */     }
/* 120:215 */     return this.parameterType;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public Type getGenericParameterType()
/* 124:    */   {
/* 125:223 */     if (this.genericParameterType == null) {
/* 126:224 */       if (this.parameterIndex < 0) {
/* 127:225 */         this.genericParameterType = (this.method != null ? this.method.getGenericReturnType() : null);
/* 128:    */       } else {
/* 129:228 */         this.genericParameterType = (this.method != null ? 
/* 130:229 */           this.method.getGenericParameterTypes()[this.parameterIndex] : 
/* 131:230 */           this.constructor.getGenericParameterTypes()[this.parameterIndex]);
/* 132:    */       }
/* 133:    */     }
/* 134:233 */     return this.genericParameterType;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public Annotation[] getMethodAnnotations()
/* 138:    */   {
/* 139:240 */     return getAnnotatedElement().getAnnotations();
/* 140:    */   }
/* 141:    */   
/* 142:    */   public <T extends Annotation> T getMethodAnnotation(Class<T> annotationType)
/* 143:    */   {
/* 144:250 */     return getAnnotatedElement().getAnnotation(annotationType);
/* 145:    */   }
/* 146:    */   
/* 147:    */   public Annotation[] getParameterAnnotations()
/* 148:    */   {
/* 149:257 */     if (this.parameterAnnotations == null)
/* 150:    */     {
/* 151:258 */       Annotation[][] annotationArray = this.method != null ? 
/* 152:259 */         this.method.getParameterAnnotations() : this.constructor.getParameterAnnotations();
/* 153:260 */       if ((this.parameterIndex >= 0) && (this.parameterIndex < annotationArray.length)) {
/* 154:261 */         this.parameterAnnotations = annotationArray[this.parameterIndex];
/* 155:    */       } else {
/* 156:264 */         this.parameterAnnotations = new Annotation[0];
/* 157:    */       }
/* 158:    */     }
/* 159:267 */     return this.parameterAnnotations;
/* 160:    */   }
/* 161:    */   
/* 162:    */   public <T extends Annotation> T getParameterAnnotation(Class<T> annotationType)
/* 163:    */   {
/* 164:277 */     Annotation[] anns = getParameterAnnotations();
/* 165:278 */     for (Annotation ann : anns) {
/* 166:279 */       if (annotationType.isInstance(ann)) {
/* 167:280 */         return ann;
/* 168:    */       }
/* 169:    */     }
/* 170:283 */     return null;
/* 171:    */   }
/* 172:    */   
/* 173:    */   public boolean hasParameterAnnotations()
/* 174:    */   {
/* 175:290 */     return getParameterAnnotations().length != 0;
/* 176:    */   }
/* 177:    */   
/* 178:    */   public <T extends Annotation> boolean hasParameterAnnotation(Class<T> annotationType)
/* 179:    */   {
/* 180:297 */     return getParameterAnnotation(annotationType) != null;
/* 181:    */   }
/* 182:    */   
/* 183:    */   public void initParameterNameDiscovery(ParameterNameDiscoverer parameterNameDiscoverer)
/* 184:    */   {
/* 185:307 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/* 186:    */   }
/* 187:    */   
/* 188:    */   public String getParameterName()
/* 189:    */   {
/* 190:318 */     if (this.parameterNameDiscoverer != null)
/* 191:    */     {
/* 192:319 */       String[] parameterNames = this.method != null ? 
/* 193:320 */         this.parameterNameDiscoverer.getParameterNames(this.method) : 
/* 194:321 */         this.parameterNameDiscoverer.getParameterNames(this.constructor);
/* 195:322 */       if (parameterNames != null) {
/* 196:323 */         this.parameterName = parameterNames[this.parameterIndex];
/* 197:    */       }
/* 198:325 */       this.parameterNameDiscoverer = null;
/* 199:    */     }
/* 200:327 */     return this.parameterName;
/* 201:    */   }
/* 202:    */   
/* 203:    */   public void increaseNestingLevel()
/* 204:    */   {
/* 205:335 */     this.nestingLevel += 1;
/* 206:    */   }
/* 207:    */   
/* 208:    */   public void decreaseNestingLevel()
/* 209:    */   {
/* 210:343 */     getTypeIndexesPerLevel().remove(Integer.valueOf(this.nestingLevel));
/* 211:344 */     this.nestingLevel -= 1;
/* 212:    */   }
/* 213:    */   
/* 214:    */   public int getNestingLevel()
/* 215:    */   {
/* 216:353 */     return this.nestingLevel;
/* 217:    */   }
/* 218:    */   
/* 219:    */   public void setTypeIndexForCurrentLevel(int typeIndex)
/* 220:    */   {
/* 221:363 */     getTypeIndexesPerLevel().put(Integer.valueOf(this.nestingLevel), Integer.valueOf(typeIndex));
/* 222:    */   }
/* 223:    */   
/* 224:    */   public Integer getTypeIndexForCurrentLevel()
/* 225:    */   {
/* 226:373 */     return getTypeIndexForLevel(this.nestingLevel);
/* 227:    */   }
/* 228:    */   
/* 229:    */   public Integer getTypeIndexForLevel(int nestingLevel)
/* 230:    */   {
/* 231:383 */     return (Integer)getTypeIndexesPerLevel().get(Integer.valueOf(nestingLevel));
/* 232:    */   }
/* 233:    */   
/* 234:    */   private Map<Integer, Integer> getTypeIndexesPerLevel()
/* 235:    */   {
/* 236:390 */     if (this.typeIndexesPerLevel == null) {
/* 237:391 */       this.typeIndexesPerLevel = new HashMap(4);
/* 238:    */     }
/* 239:393 */     return this.typeIndexesPerLevel;
/* 240:    */   }
/* 241:    */   
/* 242:    */   public static MethodParameter forMethodOrConstructor(Object methodOrConstructor, int parameterIndex)
/* 243:    */   {
/* 244:406 */     if ((methodOrConstructor instanceof Method)) {
/* 245:407 */       return new MethodParameter((Method)methodOrConstructor, parameterIndex);
/* 246:    */     }
/* 247:409 */     if ((methodOrConstructor instanceof Constructor)) {
/* 248:410 */       return new MethodParameter((Constructor)methodOrConstructor, parameterIndex);
/* 249:    */     }
/* 250:413 */     throw new IllegalArgumentException(
/* 251:414 */       "Given object [" + methodOrConstructor + "] is neither a Method nor a Constructor");
/* 252:    */   }
/* 253:    */   
/* 254:    */   public boolean equals(Object obj)
/* 255:    */   {
/* 256:420 */     if (this == obj) {
/* 257:421 */       return true;
/* 258:    */     }
/* 259:423 */     if ((obj != null) && ((obj instanceof MethodParameter)))
/* 260:    */     {
/* 261:424 */       MethodParameter other = (MethodParameter)obj;
/* 262:426 */       if (this.parameterIndex != other.parameterIndex) {
/* 263:427 */         return false;
/* 264:    */       }
/* 265:429 */       if (getMember().equals(other.getMember())) {
/* 266:430 */         return true;
/* 267:    */       }
/* 268:433 */       return false;
/* 269:    */     }
/* 270:436 */     return false;
/* 271:    */   }
/* 272:    */   
/* 273:    */   public int hashCode()
/* 274:    */   {
/* 275:442 */     int result = this.hash;
/* 276:443 */     if (result == 0)
/* 277:    */     {
/* 278:444 */       result = getMember().hashCode();
/* 279:445 */       result = 31 * result + this.parameterIndex;
/* 280:446 */       this.hash = result;
/* 281:    */     }
/* 282:448 */     return result;
/* 283:    */   }
/* 284:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.MethodParameter
 * JD-Core Version:    0.7.0.1
 */