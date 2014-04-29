/*   1:    */ package org.springframework.expression.spel.support;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Map;
/*   8:    */ import org.springframework.core.convert.TypeDescriptor;
/*   9:    */ import org.springframework.expression.BeanResolver;
/*  10:    */ import org.springframework.expression.ConstructorResolver;
/*  11:    */ import org.springframework.expression.EvaluationContext;
/*  12:    */ import org.springframework.expression.MethodFilter;
/*  13:    */ import org.springframework.expression.MethodResolver;
/*  14:    */ import org.springframework.expression.OperatorOverloader;
/*  15:    */ import org.springframework.expression.PropertyAccessor;
/*  16:    */ import org.springframework.expression.TypeComparator;
/*  17:    */ import org.springframework.expression.TypeConverter;
/*  18:    */ import org.springframework.expression.TypeLocator;
/*  19:    */ import org.springframework.expression.TypedValue;
/*  20:    */ import org.springframework.util.Assert;
/*  21:    */ 
/*  22:    */ public class StandardEvaluationContext
/*  23:    */   implements EvaluationContext
/*  24:    */ {
/*  25:    */   private TypedValue rootObject;
/*  26:    */   private List<ConstructorResolver> constructorResolvers;
/*  27:    */   private List<MethodResolver> methodResolvers;
/*  28:    */   private ReflectiveMethodResolver reflectiveMethodResolver;
/*  29:    */   private List<PropertyAccessor> propertyAccessors;
/*  30:    */   private TypeLocator typeLocator;
/*  31:    */   private TypeConverter typeConverter;
/*  32: 64 */   private TypeComparator typeComparator = new StandardTypeComparator();
/*  33: 66 */   private OperatorOverloader operatorOverloader = new StandardOperatorOverloader();
/*  34: 68 */   private final Map<String, Object> variables = new HashMap();
/*  35:    */   private BeanResolver beanResolver;
/*  36:    */   
/*  37:    */   public StandardEvaluationContext()
/*  38:    */   {
/*  39: 74 */     setRootObject(null);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public StandardEvaluationContext(Object rootObject)
/*  43:    */   {
/*  44: 78 */     this();
/*  45: 79 */     setRootObject(rootObject);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setRootObject(Object rootObject)
/*  49:    */   {
/*  50: 84 */     if (this.rootObject == null) {
/*  51: 85 */       this.rootObject = TypedValue.NULL;
/*  52:    */     } else {
/*  53: 87 */       this.rootObject = new TypedValue(rootObject);
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setRootObject(Object rootObject, TypeDescriptor typeDescriptor)
/*  58:    */   {
/*  59: 92 */     this.rootObject = new TypedValue(rootObject, typeDescriptor);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public TypedValue getRootObject()
/*  63:    */   {
/*  64: 96 */     return this.rootObject;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void addConstructorResolver(ConstructorResolver resolver)
/*  68:    */   {
/*  69:100 */     ensureConstructorResolversInitialized();
/*  70:101 */     this.constructorResolvers.add(this.constructorResolvers.size() - 1, resolver);
/*  71:    */   }
/*  72:    */   
/*  73:    */   public boolean removeConstructorResolver(ConstructorResolver resolver)
/*  74:    */   {
/*  75:105 */     ensureConstructorResolversInitialized();
/*  76:106 */     return this.constructorResolvers.remove(resolver);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public List<ConstructorResolver> getConstructorResolvers()
/*  80:    */   {
/*  81:110 */     ensureConstructorResolversInitialized();
/*  82:111 */     return this.constructorResolvers;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void setConstructorResolvers(List<ConstructorResolver> constructorResolvers)
/*  86:    */   {
/*  87:115 */     this.constructorResolvers = constructorResolvers;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void addMethodResolver(MethodResolver resolver)
/*  91:    */   {
/*  92:120 */     ensureMethodResolversInitialized();
/*  93:121 */     this.methodResolvers.add(this.methodResolvers.size() - 1, resolver);
/*  94:    */   }
/*  95:    */   
/*  96:    */   public boolean removeMethodResolver(MethodResolver methodResolver)
/*  97:    */   {
/*  98:125 */     ensureMethodResolversInitialized();
/*  99:126 */     return this.methodResolvers.remove(methodResolver);
/* 100:    */   }
/* 101:    */   
/* 102:    */   public List<MethodResolver> getMethodResolvers()
/* 103:    */   {
/* 104:130 */     ensureMethodResolversInitialized();
/* 105:131 */     return this.methodResolvers;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void setBeanResolver(BeanResolver beanResolver)
/* 109:    */   {
/* 110:135 */     this.beanResolver = beanResolver;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public BeanResolver getBeanResolver()
/* 114:    */   {
/* 115:139 */     return this.beanResolver;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void setMethodResolvers(List<MethodResolver> methodResolvers)
/* 119:    */   {
/* 120:143 */     this.methodResolvers = methodResolvers;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public void addPropertyAccessor(PropertyAccessor accessor)
/* 124:    */   {
/* 125:148 */     ensurePropertyAccessorsInitialized();
/* 126:149 */     this.propertyAccessors.add(this.propertyAccessors.size() - 1, accessor);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public boolean removePropertyAccessor(PropertyAccessor accessor)
/* 130:    */   {
/* 131:153 */     return this.propertyAccessors.remove(accessor);
/* 132:    */   }
/* 133:    */   
/* 134:    */   public List<PropertyAccessor> getPropertyAccessors()
/* 135:    */   {
/* 136:157 */     ensurePropertyAccessorsInitialized();
/* 137:158 */     return this.propertyAccessors;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public void setPropertyAccessors(List<PropertyAccessor> propertyAccessors)
/* 141:    */   {
/* 142:162 */     this.propertyAccessors = propertyAccessors;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public void setTypeLocator(TypeLocator typeLocator)
/* 146:    */   {
/* 147:167 */     Assert.notNull(typeLocator, "TypeLocator must not be null");
/* 148:168 */     this.typeLocator = typeLocator;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public TypeLocator getTypeLocator()
/* 152:    */   {
/* 153:172 */     if (this.typeLocator == null) {
/* 154:173 */       this.typeLocator = new StandardTypeLocator();
/* 155:    */     }
/* 156:175 */     return this.typeLocator;
/* 157:    */   }
/* 158:    */   
/* 159:    */   public void setTypeConverter(TypeConverter typeConverter)
/* 160:    */   {
/* 161:179 */     Assert.notNull(typeConverter, "TypeConverter must not be null");
/* 162:180 */     this.typeConverter = typeConverter;
/* 163:    */   }
/* 164:    */   
/* 165:    */   public TypeConverter getTypeConverter()
/* 166:    */   {
/* 167:184 */     if (this.typeConverter == null) {
/* 168:185 */       this.typeConverter = new StandardTypeConverter();
/* 169:    */     }
/* 170:187 */     return this.typeConverter;
/* 171:    */   }
/* 172:    */   
/* 173:    */   public void setTypeComparator(TypeComparator typeComparator)
/* 174:    */   {
/* 175:191 */     Assert.notNull(typeComparator, "TypeComparator must not be null");
/* 176:192 */     this.typeComparator = typeComparator;
/* 177:    */   }
/* 178:    */   
/* 179:    */   public TypeComparator getTypeComparator()
/* 180:    */   {
/* 181:196 */     return this.typeComparator;
/* 182:    */   }
/* 183:    */   
/* 184:    */   public void setOperatorOverloader(OperatorOverloader operatorOverloader)
/* 185:    */   {
/* 186:200 */     Assert.notNull(operatorOverloader, "OperatorOverloader must not be null");
/* 187:201 */     this.operatorOverloader = operatorOverloader;
/* 188:    */   }
/* 189:    */   
/* 190:    */   public OperatorOverloader getOperatorOverloader()
/* 191:    */   {
/* 192:205 */     return this.operatorOverloader;
/* 193:    */   }
/* 194:    */   
/* 195:    */   public void setVariable(String name, Object value)
/* 196:    */   {
/* 197:209 */     this.variables.put(name, value);
/* 198:    */   }
/* 199:    */   
/* 200:    */   public void setVariables(Map<String, Object> variables)
/* 201:    */   {
/* 202:213 */     this.variables.putAll(variables);
/* 203:    */   }
/* 204:    */   
/* 205:    */   public void registerFunction(String name, Method method)
/* 206:    */   {
/* 207:217 */     this.variables.put(name, method);
/* 208:    */   }
/* 209:    */   
/* 210:    */   public Object lookupVariable(String name)
/* 211:    */   {
/* 212:221 */     return this.variables.get(name);
/* 213:    */   }
/* 214:    */   
/* 215:    */   public void registerMethodFilter(Class<?> type, MethodFilter filter)
/* 216:    */   {
/* 217:233 */     ensureMethodResolversInitialized();
/* 218:234 */     this.reflectiveMethodResolver.registerMethodFilter(type, filter);
/* 219:    */   }
/* 220:    */   
/* 221:    */   private void ensurePropertyAccessorsInitialized()
/* 222:    */   {
/* 223:238 */     if (this.propertyAccessors == null) {
/* 224:239 */       initializePropertyAccessors();
/* 225:    */     }
/* 226:    */   }
/* 227:    */   
/* 228:    */   private synchronized void initializePropertyAccessors()
/* 229:    */   {
/* 230:244 */     if (this.propertyAccessors == null)
/* 231:    */     {
/* 232:245 */       List<PropertyAccessor> defaultAccessors = new ArrayList();
/* 233:246 */       defaultAccessors.add(new ReflectivePropertyAccessor());
/* 234:247 */       this.propertyAccessors = defaultAccessors;
/* 235:    */     }
/* 236:    */   }
/* 237:    */   
/* 238:    */   private void ensureMethodResolversInitialized()
/* 239:    */   {
/* 240:252 */     if (this.methodResolvers == null) {
/* 241:253 */       initializeMethodResolvers();
/* 242:    */     }
/* 243:    */   }
/* 244:    */   
/* 245:    */   private synchronized void initializeMethodResolvers()
/* 246:    */   {
/* 247:258 */     if (this.methodResolvers == null)
/* 248:    */     {
/* 249:259 */       List<MethodResolver> defaultResolvers = new ArrayList();
/* 250:260 */       defaultResolvers.add(this.reflectiveMethodResolver = new ReflectiveMethodResolver());
/* 251:261 */       this.methodResolvers = defaultResolvers;
/* 252:    */     }
/* 253:    */   }
/* 254:    */   
/* 255:    */   private void ensureConstructorResolversInitialized()
/* 256:    */   {
/* 257:266 */     if (this.constructorResolvers == null) {
/* 258:267 */       initializeConstructorResolvers();
/* 259:    */     }
/* 260:    */   }
/* 261:    */   
/* 262:    */   private synchronized void initializeConstructorResolvers()
/* 263:    */   {
/* 264:272 */     if (this.constructorResolvers == null)
/* 265:    */     {
/* 266:273 */       List<ConstructorResolver> defaultResolvers = new ArrayList();
/* 267:274 */       defaultResolvers.add(new ReflectiveConstructorResolver());
/* 268:275 */       this.constructorResolvers = defaultResolvers;
/* 269:    */     }
/* 270:    */   }
/* 271:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.support.StandardEvaluationContext
 * JD-Core Version:    0.7.0.1
 */