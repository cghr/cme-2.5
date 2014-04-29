/*   1:    */ package org.springframework.expression.spel.ast;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Array;
/*   4:    */ import java.lang.reflect.InvocationTargetException;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.List;
/*   7:    */ import org.springframework.core.convert.TypeDescriptor;
/*   8:    */ import org.springframework.expression.AccessException;
/*   9:    */ import org.springframework.expression.ConstructorExecutor;
/*  10:    */ import org.springframework.expression.ConstructorResolver;
/*  11:    */ import org.springframework.expression.EvaluationContext;
/*  12:    */ import org.springframework.expression.EvaluationException;
/*  13:    */ import org.springframework.expression.TypeConverter;
/*  14:    */ import org.springframework.expression.TypedValue;
/*  15:    */ import org.springframework.expression.common.ExpressionUtils;
/*  16:    */ import org.springframework.expression.spel.ExpressionState;
/*  17:    */ import org.springframework.expression.spel.SpelEvaluationException;
/*  18:    */ import org.springframework.expression.spel.SpelMessage;
/*  19:    */ import org.springframework.expression.spel.SpelNode;
/*  20:    */ 
/*  21:    */ public class ConstructorReference
/*  22:    */   extends SpelNodeImpl
/*  23:    */ {
/*  24: 54 */   private boolean isArrayConstructor = false;
/*  25:    */   private SpelNodeImpl[] dimensions;
/*  26:    */   private volatile ConstructorExecutor cachedExecutor;
/*  27:    */   
/*  28:    */   public ConstructorReference(int pos, SpelNodeImpl... arguments)
/*  29:    */   {
/*  30: 70 */     super(pos, arguments);
/*  31: 71 */     this.isArrayConstructor = false;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public ConstructorReference(int pos, SpelNodeImpl[] dimensions, SpelNodeImpl... arguments)
/*  35:    */   {
/*  36: 79 */     super(pos, arguments);
/*  37: 80 */     this.isArrayConstructor = true;
/*  38: 81 */     this.dimensions = dimensions;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public TypedValue getValueInternal(ExpressionState state)
/*  42:    */     throws EvaluationException
/*  43:    */   {
/*  44: 90 */     if (this.isArrayConstructor) {
/*  45: 91 */       return createArray(state);
/*  46:    */     }
/*  47: 94 */     return createNewInstance(state);
/*  48:    */   }
/*  49:    */   
/*  50:    */   private TypedValue createNewInstance(ExpressionState state)
/*  51:    */     throws EvaluationException
/*  52:    */   {
/*  53:105 */     Object[] arguments = new Object[getChildCount() - 1];
/*  54:106 */     List<TypeDescriptor> argumentTypes = new ArrayList(getChildCount() - 1);
/*  55:107 */     for (int i = 0; i < arguments.length; i++)
/*  56:    */     {
/*  57:108 */       TypedValue childValue = this.children[(i + 1)].getValueInternal(state);
/*  58:109 */       Object value = childValue.getValue();
/*  59:110 */       arguments[i] = value;
/*  60:111 */       argumentTypes.add(TypeDescriptor.forObject(value));
/*  61:    */     }
/*  62:114 */     ConstructorExecutor executorToUse = this.cachedExecutor;
/*  63:115 */     if (executorToUse != null) {
/*  64:    */       try
/*  65:    */       {
/*  66:117 */         return executorToUse.execute(state.getEvaluationContext(), arguments);
/*  67:    */       }
/*  68:    */       catch (AccessException ae)
/*  69:    */       {
/*  70:130 */         if ((ae.getCause() instanceof InvocationTargetException))
/*  71:    */         {
/*  72:132 */           Throwable rootCause = ae.getCause().getCause();
/*  73:133 */           if ((rootCause instanceof RuntimeException)) {
/*  74:134 */             throw ((RuntimeException)rootCause);
/*  75:    */           }
/*  76:136 */           String typename = (String)this.children[0].getValueInternal(state).getValue();
/*  77:137 */           throw new SpelEvaluationException(getStartPosition(), rootCause, 
/*  78:138 */             SpelMessage.CONSTRUCTOR_INVOCATION_PROBLEM, new Object[] { typename, 
/*  79:139 */             FormatHelper.formatMethodForMessage("", argumentTypes) });
/*  80:    */         }
/*  81:144 */         this.cachedExecutor = null;
/*  82:    */       }
/*  83:    */     }
/*  84:149 */     String typename = (String)this.children[0].getValueInternal(state).getValue();
/*  85:150 */     executorToUse = findExecutorForConstructor(typename, argumentTypes, state);
/*  86:    */     try
/*  87:    */     {
/*  88:152 */       this.cachedExecutor = executorToUse;
/*  89:153 */       return executorToUse.execute(state.getEvaluationContext(), arguments);
/*  90:    */     }
/*  91:    */     catch (AccessException ae)
/*  92:    */     {
/*  93:156 */       throw new SpelEvaluationException(getStartPosition(), ae, SpelMessage.CONSTRUCTOR_INVOCATION_PROBLEM, new Object[] {
/*  94:157 */         typename, FormatHelper.formatMethodForMessage("", argumentTypes) });
/*  95:    */     }
/*  96:    */   }
/*  97:    */   
/*  98:    */   private ConstructorExecutor findExecutorForConstructor(String typename, List<TypeDescriptor> argumentTypes, ExpressionState state)
/*  99:    */     throws SpelEvaluationException
/* 100:    */   {
/* 101:174 */     EvaluationContext eContext = state.getEvaluationContext();
/* 102:175 */     List<ConstructorResolver> cResolvers = eContext.getConstructorResolvers();
/* 103:176 */     if (cResolvers != null) {
/* 104:177 */       for (ConstructorResolver ctorResolver : cResolvers) {
/* 105:    */         try
/* 106:    */         {
/* 107:179 */           ConstructorExecutor cEx = ctorResolver.resolve(state.getEvaluationContext(), typename, 
/* 108:180 */             argumentTypes);
/* 109:181 */           if (cEx != null) {
/* 110:182 */             return cEx;
/* 111:    */           }
/* 112:    */         }
/* 113:    */         catch (AccessException ex)
/* 114:    */         {
/* 115:186 */           throw new SpelEvaluationException(getStartPosition(), ex, 
/* 116:187 */             SpelMessage.CONSTRUCTOR_INVOCATION_PROBLEM, new Object[] { typename, 
/* 117:188 */             FormatHelper.formatMethodForMessage("", argumentTypes) });
/* 118:    */         }
/* 119:    */       }
/* 120:    */     }
/* 121:192 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.CONSTRUCTOR_NOT_FOUND, new Object[] { typename, 
/* 122:193 */       FormatHelper.formatMethodForMessage("", argumentTypes) });
/* 123:    */   }
/* 124:    */   
/* 125:    */   public String toStringAST()
/* 126:    */   {
/* 127:198 */     StringBuilder sb = new StringBuilder();
/* 128:199 */     sb.append("new ");
/* 129:    */     
/* 130:201 */     int index = 0;
/* 131:202 */     sb.append(getChild(index++).toStringAST());
/* 132:203 */     sb.append("(");
/* 133:204 */     for (int i = index; i < getChildCount(); i++)
/* 134:    */     {
/* 135:205 */       if (i > index) {
/* 136:206 */         sb.append(",");
/* 137:    */       }
/* 138:207 */       sb.append(getChild(i).toStringAST());
/* 139:    */     }
/* 140:209 */     sb.append(")");
/* 141:210 */     return sb.toString();
/* 142:    */   }
/* 143:    */   
/* 144:    */   private TypedValue createArray(ExpressionState state)
/* 145:    */     throws EvaluationException
/* 146:    */   {
/* 147:221 */     Object intendedArrayType = getChild(0).getValue(state);
/* 148:222 */     if (!(intendedArrayType instanceof String)) {
/* 149:223 */       throw new SpelEvaluationException(getChild(0).getStartPosition(), 
/* 150:224 */         SpelMessage.TYPE_NAME_EXPECTED_FOR_ARRAY_CONSTRUCTION, new Object[] {
/* 151:225 */         FormatHelper.formatClassNameForMessage(intendedArrayType.getClass()) });
/* 152:    */     }
/* 153:227 */     String type = (String)intendedArrayType;
/* 154:    */     
/* 155:229 */     TypeCode arrayTypeCode = TypeCode.forName(type);
/* 156:    */     Class<?> componentType;
/* 157:    */     Class<?> componentType;
/* 158:230 */     if (arrayTypeCode == TypeCode.OBJECT) {
/* 159:231 */       componentType = state.findType(type);
/* 160:    */     } else {
/* 161:234 */       componentType = arrayTypeCode.getType();
/* 162:    */     }
/* 163:    */     Object newArray;
/* 164:    */     Object newArray;
/* 165:237 */     if (!hasInitializer())
/* 166:    */     {
/* 167:239 */       for (SpelNodeImpl dimension : this.dimensions) {
/* 168:240 */         if (dimension == null) {
/* 169:241 */           throw new SpelEvaluationException(getStartPosition(), SpelMessage.MISSING_ARRAY_DIMENSION, new Object[0]);
/* 170:    */         }
/* 171:    */       }
/* 172:244 */       TypeConverter typeConverter = state.getEvaluationContext().getTypeConverter();
/* 173:    */       Object newArray;
/* 174:247 */       if (this.dimensions.length == 1)
/* 175:    */       {
/* 176:248 */         TypedValue o = this.dimensions[0].getTypedValue(state);
/* 177:249 */         int arraySize = ExpressionUtils.toInt(typeConverter, o);
/* 178:250 */         newArray = Array.newInstance(componentType, arraySize);
/* 179:    */       }
/* 180:    */       else
/* 181:    */       {
/* 182:254 */         int[] dims = new int[this.dimensions.length];
/* 183:255 */         for (int d = 0; d < this.dimensions.length; d++)
/* 184:    */         {
/* 185:256 */           TypedValue o = this.dimensions[d].getTypedValue(state);
/* 186:257 */           dims[d] = ExpressionUtils.toInt(typeConverter, o);
/* 187:    */         }
/* 188:259 */         newArray = Array.newInstance(componentType, dims);
/* 189:    */       }
/* 190:    */     }
/* 191:    */     else
/* 192:    */     {
/* 193:264 */       if (this.dimensions.length > 1) {
/* 194:267 */         throw new SpelEvaluationException(getStartPosition(), 
/* 195:268 */           SpelMessage.MULTIDIM_ARRAY_INITIALIZER_NOT_SUPPORTED, new Object[0]);
/* 196:    */       }
/* 197:270 */       TypeConverter typeConverter = state.getEvaluationContext().getTypeConverter();
/* 198:271 */       InlineList initializer = (InlineList)getChild(1);
/* 199:273 */       if (this.dimensions[0] != null)
/* 200:    */       {
/* 201:274 */         TypedValue dValue = this.dimensions[0].getTypedValue(state);
/* 202:275 */         int i = ExpressionUtils.toInt(typeConverter, dValue);
/* 203:276 */         if (i != initializer.getChildCount()) {
/* 204:277 */           throw new SpelEvaluationException(getStartPosition(), SpelMessage.INITIALIZER_LENGTH_INCORRECT, new Object[0]);
/* 205:    */         }
/* 206:    */       }
/* 207:281 */       int arraySize = initializer.getChildCount();
/* 208:282 */       newArray = Array.newInstance(componentType, arraySize);
/* 209:283 */       if (arrayTypeCode == TypeCode.OBJECT) {
/* 210:284 */         populateReferenceTypeArray(state, newArray, typeConverter, initializer, componentType);
/* 211:286 */       } else if (arrayTypeCode == TypeCode.INT) {
/* 212:287 */         populateIntArray(state, newArray, typeConverter, initializer);
/* 213:289 */       } else if (arrayTypeCode == TypeCode.BOOLEAN) {
/* 214:290 */         populateBooleanArray(state, newArray, typeConverter, initializer);
/* 215:292 */       } else if (arrayTypeCode == TypeCode.CHAR) {
/* 216:293 */         populateCharArray(state, newArray, typeConverter, initializer);
/* 217:295 */       } else if (arrayTypeCode == TypeCode.LONG) {
/* 218:296 */         populateLongArray(state, newArray, typeConverter, initializer);
/* 219:298 */       } else if (arrayTypeCode == TypeCode.SHORT) {
/* 220:299 */         populateShortArray(state, newArray, typeConverter, initializer);
/* 221:301 */       } else if (arrayTypeCode == TypeCode.DOUBLE) {
/* 222:302 */         populateDoubleArray(state, newArray, typeConverter, initializer);
/* 223:304 */       } else if (arrayTypeCode == TypeCode.FLOAT) {
/* 224:305 */         populateFloatArray(state, newArray, typeConverter, initializer);
/* 225:307 */       } else if (arrayTypeCode == TypeCode.BYTE) {
/* 226:308 */         populateByteArray(state, newArray, typeConverter, initializer);
/* 227:    */       } else {
/* 228:311 */         throw new IllegalStateException(arrayTypeCode.name());
/* 229:    */       }
/* 230:    */     }
/* 231:314 */     return new TypedValue(newArray);
/* 232:    */   }
/* 233:    */   
/* 234:    */   private void populateReferenceTypeArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer, Class<?> componentType)
/* 235:    */   {
/* 236:319 */     TypeDescriptor toTypeDescriptor = TypeDescriptor.valueOf(componentType);
/* 237:320 */     Object[] newObjectArray = (Object[])newArray;
/* 238:321 */     for (int i = 0; i < newObjectArray.length; i++)
/* 239:    */     {
/* 240:322 */       SpelNode elementNode = initializer.getChild(i);
/* 241:323 */       Object arrayEntry = elementNode.getValue(state);
/* 242:324 */       newObjectArray[i] = typeConverter.convertValue(arrayEntry, TypeDescriptor.forObject(arrayEntry), toTypeDescriptor);
/* 243:    */     }
/* 244:    */   }
/* 245:    */   
/* 246:    */   private void populateByteArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer)
/* 247:    */   {
/* 248:330 */     byte[] newByteArray = (byte[])newArray;
/* 249:331 */     for (int i = 0; i < newByteArray.length; i++)
/* 250:    */     {
/* 251:332 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 252:333 */       newByteArray[i] = ExpressionUtils.toByte(typeConverter, typedValue);
/* 253:    */     }
/* 254:    */   }
/* 255:    */   
/* 256:    */   private void populateFloatArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer)
/* 257:    */   {
/* 258:339 */     float[] newFloatArray = (float[])newArray;
/* 259:340 */     for (int i = 0; i < newFloatArray.length; i++)
/* 260:    */     {
/* 261:341 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 262:342 */       newFloatArray[i] = ExpressionUtils.toFloat(typeConverter, typedValue);
/* 263:    */     }
/* 264:    */   }
/* 265:    */   
/* 266:    */   private void populateDoubleArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer)
/* 267:    */   {
/* 268:348 */     double[] newDoubleArray = (double[])newArray;
/* 269:349 */     for (int i = 0; i < newDoubleArray.length; i++)
/* 270:    */     {
/* 271:350 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 272:351 */       newDoubleArray[i] = ExpressionUtils.toDouble(typeConverter, typedValue);
/* 273:    */     }
/* 274:    */   }
/* 275:    */   
/* 276:    */   private void populateShortArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer)
/* 277:    */   {
/* 278:357 */     short[] newShortArray = (short[])newArray;
/* 279:358 */     for (int i = 0; i < newShortArray.length; i++)
/* 280:    */     {
/* 281:359 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 282:360 */       newShortArray[i] = ExpressionUtils.toShort(typeConverter, typedValue);
/* 283:    */     }
/* 284:    */   }
/* 285:    */   
/* 286:    */   private void populateLongArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer)
/* 287:    */   {
/* 288:366 */     long[] newLongArray = (long[])newArray;
/* 289:367 */     for (int i = 0; i < newLongArray.length; i++)
/* 290:    */     {
/* 291:368 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 292:369 */       newLongArray[i] = ExpressionUtils.toLong(typeConverter, typedValue);
/* 293:    */     }
/* 294:    */   }
/* 295:    */   
/* 296:    */   private void populateCharArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer)
/* 297:    */   {
/* 298:375 */     char[] newCharArray = (char[])newArray;
/* 299:376 */     for (int i = 0; i < newCharArray.length; i++)
/* 300:    */     {
/* 301:377 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 302:378 */       newCharArray[i] = ExpressionUtils.toChar(typeConverter, typedValue);
/* 303:    */     }
/* 304:    */   }
/* 305:    */   
/* 306:    */   private void populateBooleanArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer)
/* 307:    */   {
/* 308:384 */     boolean[] newBooleanArray = (boolean[])newArray;
/* 309:385 */     for (int i = 0; i < newBooleanArray.length; i++)
/* 310:    */     {
/* 311:386 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 312:387 */       newBooleanArray[i] = ExpressionUtils.toBoolean(typeConverter, typedValue);
/* 313:    */     }
/* 314:    */   }
/* 315:    */   
/* 316:    */   private void populateIntArray(ExpressionState state, Object newArray, TypeConverter typeConverter, InlineList initializer)
/* 317:    */   {
/* 318:393 */     int[] newIntArray = (int[])newArray;
/* 319:394 */     for (int i = 0; i < newIntArray.length; i++)
/* 320:    */     {
/* 321:395 */       TypedValue typedValue = initializer.getChild(i).getTypedValue(state);
/* 322:396 */       newIntArray[i] = ExpressionUtils.toInt(typeConverter, typedValue);
/* 323:    */     }
/* 324:    */   }
/* 325:    */   
/* 326:    */   private boolean hasInitializer()
/* 327:    */   {
/* 328:401 */     return getChildCount() > 1;
/* 329:    */   }
/* 330:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.ConstructorReference
 * JD-Core Version:    0.7.0.1
 */