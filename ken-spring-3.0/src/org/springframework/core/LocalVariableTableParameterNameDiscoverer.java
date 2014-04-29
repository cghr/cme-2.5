/*   1:    */ package org.springframework.core;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.lang.reflect.Constructor;
/*   6:    */ import java.lang.reflect.Member;
/*   7:    */ import java.lang.reflect.Method;
/*   8:    */ import java.util.Collections;
/*   9:    */ import java.util.Map;
/*  10:    */ import java.util.concurrent.ConcurrentHashMap;
/*  11:    */ import org.apache.commons.logging.Log;
/*  12:    */ import org.apache.commons.logging.LogFactory;
/*  13:    */ import org.springframework.asm.ClassReader;
/*  14:    */ import org.springframework.asm.Label;
/*  15:    */ import org.springframework.asm.MethodVisitor;
/*  16:    */ import org.springframework.asm.Type;
/*  17:    */ import org.springframework.asm.commons.EmptyVisitor;
/*  18:    */ import org.springframework.util.ClassUtils;
/*  19:    */ 
/*  20:    */ public class LocalVariableTableParameterNameDiscoverer
/*  21:    */   implements ParameterNameDiscoverer
/*  22:    */ {
/*  23: 56 */   private static Log logger = LogFactory.getLog(LocalVariableTableParameterNameDiscoverer.class);
/*  24: 59 */   private static final Map<Member, String[]> NO_DEBUG_INFO_MAP = Collections.emptyMap();
/*  25: 63 */   private final Map<Class<?>, Map<Member, String[]>> parameterNamesCache = new ConcurrentHashMap();
/*  26:    */   
/*  27:    */   public String[] getParameterNames(Method method)
/*  28:    */   {
/*  29: 67 */     Class<?> declaringClass = method.getDeclaringClass();
/*  30: 68 */     Map<Member, String[]> map = (Map)this.parameterNamesCache.get(declaringClass);
/*  31: 69 */     if (map == null)
/*  32:    */     {
/*  33: 71 */       map = inspectClass(declaringClass);
/*  34: 72 */       this.parameterNamesCache.put(declaringClass, map);
/*  35:    */     }
/*  36: 74 */     if (map != NO_DEBUG_INFO_MAP) {
/*  37: 75 */       return (String[])map.get(method);
/*  38:    */     }
/*  39: 77 */     return null;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public String[] getParameterNames(Constructor ctor)
/*  43:    */   {
/*  44: 82 */     Class<?> declaringClass = ctor.getDeclaringClass();
/*  45: 83 */     Map<Member, String[]> map = (Map)this.parameterNamesCache.get(declaringClass);
/*  46: 84 */     if (map == null)
/*  47:    */     {
/*  48: 86 */       map = inspectClass(declaringClass);
/*  49: 87 */       this.parameterNamesCache.put(declaringClass, map);
/*  50:    */     }
/*  51: 89 */     if (map != NO_DEBUG_INFO_MAP) {
/*  52: 90 */       return (String[])map.get(ctor);
/*  53:    */     }
/*  54: 93 */     return null;
/*  55:    */   }
/*  56:    */   
/*  57:    */   private Map<Member, String[]> inspectClass(Class<?> clazz)
/*  58:    */   {
/*  59:101 */     InputStream is = clazz.getResourceAsStream(ClassUtils.getClassFileName(clazz));
/*  60:102 */     if (is == null)
/*  61:    */     {
/*  62:105 */       if (logger.isDebugEnabled()) {
/*  63:106 */         logger.debug("Cannot find '.class' file for class [" + clazz + 
/*  64:107 */           "] - unable to determine constructors/methods parameter names");
/*  65:    */       }
/*  66:109 */       return NO_DEBUG_INFO_MAP;
/*  67:    */     }
/*  68:    */     try
/*  69:    */     {
/*  70:112 */       ClassReader classReader = new ClassReader(is);
/*  71:113 */       Map<Member, String[]> map = new ConcurrentHashMap();
/*  72:114 */       classReader.accept(new ParameterNameDiscoveringVisitor(clazz, map), false);
/*  73:115 */       return map;
/*  74:    */     }
/*  75:    */     catch (IOException ex)
/*  76:    */     {
/*  77:118 */       if (logger.isDebugEnabled()) {
/*  78:119 */         logger.debug("Exception thrown while reading '.class' file for class [" + clazz + 
/*  79:120 */           "] - unable to determine constructors/methods parameter names", ex);
/*  80:    */       }
/*  81:    */     }
/*  82:    */     finally
/*  83:    */     {
/*  84:    */       try
/*  85:    */       {
/*  86:125 */         is.close();
/*  87:    */       }
/*  88:    */       catch (IOException localIOException3) {}
/*  89:    */     }
/*  90:131 */     return NO_DEBUG_INFO_MAP;
/*  91:    */   }
/*  92:    */   
/*  93:    */   private static class ParameterNameDiscoveringVisitor
/*  94:    */     extends EmptyVisitor
/*  95:    */   {
/*  96:    */     private static final String STATIC_CLASS_INIT = "<clinit>";
/*  97:    */     private final Class<?> clazz;
/*  98:    */     private final Map<Member, String[]> memberMap;
/*  99:    */     
/* 100:    */     public ParameterNameDiscoveringVisitor(Class<?> clazz, Map<Member, String[]> memberMap)
/* 101:    */     {
/* 102:147 */       this.clazz = clazz;
/* 103:148 */       this.memberMap = memberMap;
/* 104:    */     }
/* 105:    */     
/* 106:    */     public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
/* 107:    */     {
/* 108:154 */       if ((!isSyntheticOrBridged(access)) && (!"<clinit>".equals(name))) {
/* 109:155 */         return new LocalVariableTableParameterNameDiscoverer.LocalVariableTableVisitor(this.clazz, this.memberMap, name, desc, isStatic(access));
/* 110:    */       }
/* 111:157 */       return null;
/* 112:    */     }
/* 113:    */     
/* 114:    */     private static boolean isSyntheticOrBridged(int access)
/* 115:    */     {
/* 116:161 */       return (access & 0x1000 | access & 0x40) > 0;
/* 117:    */     }
/* 118:    */     
/* 119:    */     private static boolean isStatic(int access)
/* 120:    */     {
/* 121:165 */       return (access & 0x8) > 0;
/* 122:    */     }
/* 123:    */   }
/* 124:    */   
/* 125:    */   private static class LocalVariableTableVisitor
/* 126:    */     extends EmptyVisitor
/* 127:    */   {
/* 128:    */     private static final String CONSTRUCTOR = "<init>";
/* 129:    */     private final Class<?> clazz;
/* 130:    */     private final Map<Member, String[]> memberMap;
/* 131:    */     private final String name;
/* 132:    */     private final Type[] args;
/* 133:    */     private final boolean isStatic;
/* 134:    */     private String[] parameterNames;
/* 135:181 */     private boolean hasLvtInfo = false;
/* 136:    */     private final int[] lvtSlotIndex;
/* 137:    */     
/* 138:    */     public LocalVariableTableVisitor(Class<?> clazz, Map<Member, String[]> map, String name, String desc, boolean isStatic)
/* 139:    */     {
/* 140:191 */       this.clazz = clazz;
/* 141:192 */       this.memberMap = map;
/* 142:193 */       this.name = name;
/* 143:    */       
/* 144:195 */       this.args = Type.getArgumentTypes(desc);
/* 145:196 */       this.parameterNames = new String[this.args.length];
/* 146:197 */       this.isStatic = isStatic;
/* 147:198 */       this.lvtSlotIndex = computeLvtSlotIndices(isStatic, this.args);
/* 148:    */     }
/* 149:    */     
/* 150:    */     public void visitLocalVariable(String name, String description, String signature, Label start, Label end, int index)
/* 151:    */     {
/* 152:204 */       this.hasLvtInfo = true;
/* 153:205 */       for (int i = 0; i < this.lvtSlotIndex.length; i++) {
/* 154:206 */         if (this.lvtSlotIndex[i] == index) {
/* 155:207 */           this.parameterNames[i] = name;
/* 156:    */         }
/* 157:    */       }
/* 158:    */     }
/* 159:    */     
/* 160:    */     public void visitEnd()
/* 161:    */     {
/* 162:214 */       if ((this.hasLvtInfo) || ((this.isStatic) && (this.parameterNames.length == 0))) {
/* 163:219 */         this.memberMap.put(resolveMember(), this.parameterNames);
/* 164:    */       }
/* 165:    */     }
/* 166:    */     
/* 167:    */     private Member resolveMember()
/* 168:    */     {
/* 169:224 */       ClassLoader loader = this.clazz.getClassLoader();
/* 170:225 */       Class[] classes = new Class[this.args.length];
/* 171:228 */       for (int i = 0; i < this.args.length; i++) {
/* 172:229 */         classes[i] = ClassUtils.resolveClassName(this.args[i].getClassName(), loader);
/* 173:    */       }
/* 174:    */       try
/* 175:    */       {
/* 176:232 */         if ("<init>".equals(this.name)) {
/* 177:233 */           return (Member)this.clazz.getDeclaredConstructor(classes);
/* 178:    */         }
/* 179:236 */         return this.clazz.getDeclaredMethod(this.name, classes);
/* 180:    */       }
/* 181:    */       catch (NoSuchMethodException ex)
/* 182:    */       {
/* 183:238 */         throw new IllegalStateException("Method [" + this.name + 
/* 184:239 */           "] was discovered in the .class file but cannot be resolved in the class object", ex);
/* 185:    */       }
/* 186:    */     }
/* 187:    */     
/* 188:    */     private static int[] computeLvtSlotIndices(boolean isStatic, Type[] paramTypes)
/* 189:    */     {
/* 190:244 */       int[] lvtIndex = new int[paramTypes.length];
/* 191:245 */       int nextIndex = isStatic ? 0 : 1;
/* 192:246 */       for (int i = 0; i < paramTypes.length; i++)
/* 193:    */       {
/* 194:247 */         lvtIndex[i] = nextIndex;
/* 195:248 */         if (isWideType(paramTypes[i])) {
/* 196:249 */           nextIndex += 2;
/* 197:    */         } else {
/* 198:251 */           nextIndex++;
/* 199:    */         }
/* 200:    */       }
/* 201:254 */       return lvtIndex;
/* 202:    */     }
/* 203:    */     
/* 204:    */     private static boolean isWideType(Type aType)
/* 205:    */     {
/* 206:259 */       return (aType == Type.LONG_TYPE) || (aType == Type.DOUBLE_TYPE);
/* 207:    */     }
/* 208:    */   }
/* 209:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.LocalVariableTableParameterNameDiscoverer
 * JD-Core Version:    0.7.0.1
 */