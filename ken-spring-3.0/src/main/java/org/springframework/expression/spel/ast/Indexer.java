/*   1:    */ package org.springframework.expression.spel.ast;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.List;
/*   5:    */ import java.util.Map;
/*   6:    */ import org.springframework.core.convert.TypeDescriptor;
/*   7:    */ import org.springframework.expression.AccessException;
/*   8:    */ import org.springframework.expression.EvaluationContext;
/*   9:    */ import org.springframework.expression.EvaluationException;
/*  10:    */ import org.springframework.expression.PropertyAccessor;
/*  11:    */ import org.springframework.expression.TypedValue;
/*  12:    */ import org.springframework.expression.spel.ExpressionState;
/*  13:    */ import org.springframework.expression.spel.SpelEvaluationException;
/*  14:    */ import org.springframework.expression.spel.SpelMessage;
/*  15:    */ import org.springframework.expression.spel.SpelNode;
/*  16:    */ import org.springframework.expression.spel.SpelParserConfiguration;
/*  17:    */ import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
/*  18:    */ 
/*  19:    */ public class Indexer
/*  20:    */   extends SpelNodeImpl
/*  21:    */ {
/*  22:    */   private String cachedReadName;
/*  23:    */   private Class<?> cachedReadTargetType;
/*  24:    */   private PropertyAccessor cachedReadAccessor;
/*  25:    */   private String cachedWriteName;
/*  26:    */   private Class<?> cachedWriteTargetType;
/*  27:    */   private PropertyAccessor cachedWriteAccessor;
/*  28:    */   
/*  29:    */   public Indexer(int pos, SpelNodeImpl expr)
/*  30:    */   {
/*  31: 61 */     super(pos, new SpelNodeImpl[] { expr });
/*  32:    */   }
/*  33:    */   
/*  34:    */   public TypedValue getValueInternal(ExpressionState state)
/*  35:    */     throws EvaluationException
/*  36:    */   {
/*  37: 66 */     TypedValue context = state.getActiveContextObject();
/*  38: 67 */     Object targetObject = context.getValue();
/*  39: 68 */     TypeDescriptor targetObjectTypeDescriptor = context.getTypeDescriptor();
/*  40: 69 */     TypedValue indexValue = null;
/*  41: 70 */     Object index = null;
/*  42: 73 */     if (((targetObject instanceof Map)) && ((this.children[0] instanceof PropertyOrFieldReference)))
/*  43:    */     {
/*  44: 74 */       reference = (PropertyOrFieldReference)this.children[0];
/*  45: 75 */       index = reference.getName();
/*  46: 76 */       indexValue = new TypedValue(index);
/*  47:    */     }
/*  48:    */     else
/*  49:    */     {
/*  50:    */       try
/*  51:    */       {
/*  52: 82 */         state.pushActiveContextObject(state.getRootContextObject());
/*  53: 83 */         indexValue = this.children[0].getValueInternal(state);
/*  54: 84 */         index = indexValue.getValue();
/*  55:    */       }
/*  56:    */       finally
/*  57:    */       {
/*  58: 87 */         state.popActiveContextObject();
/*  59:    */       }
/*  60:    */     }
/*  61: 92 */     if ((targetObject instanceof Map))
/*  62:    */     {
/*  63: 93 */       Object key = index;
/*  64: 94 */       if (targetObjectTypeDescriptor.getMapKeyTypeDescriptor() != null) {
/*  65: 95 */         key = state.convertValue(key, targetObjectTypeDescriptor.getMapKeyTypeDescriptor());
/*  66:    */       }
/*  67: 97 */       Object value = ((Map)targetObject).get(key);
/*  68: 98 */       return new TypedValue(value, targetObjectTypeDescriptor.getMapValueTypeDescriptor(value));
/*  69:    */     }
/*  70:101 */     if (targetObject == null) {
/*  71:102 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.CANNOT_INDEX_INTO_NULL_VALUE, new Object[0]);
/*  72:    */     }
/*  73:106 */     if (((targetObject instanceof Collection)) || (targetObject.getClass().isArray()) || ((targetObject instanceof String)))
/*  74:    */     {
/*  75:107 */       int idx = ((Integer)state.convertValue(index, TypeDescriptor.valueOf(Integer.class))).intValue();
/*  76:108 */       if (targetObject.getClass().isArray())
/*  77:    */       {
/*  78:109 */         Object arrayElement = accessArrayElement(targetObject, idx);
/*  79:110 */         return new TypedValue(arrayElement, targetObjectTypeDescriptor.elementTypeDescriptor(arrayElement));
/*  80:    */       }
/*  81:111 */       if ((targetObject instanceof Collection))
/*  82:    */       {
/*  83:112 */         Collection c = (Collection)targetObject;
/*  84:113 */         if ((idx >= c.size()) && 
/*  85:114 */           (!growCollection(state, targetObjectTypeDescriptor, idx, c))) {
/*  86:115 */           throw new SpelEvaluationException(getStartPosition(), SpelMessage.COLLECTION_INDEX_OUT_OF_BOUNDS, new Object[] { Integer.valueOf(c.size()), Integer.valueOf(idx) });
/*  87:    */         }
/*  88:118 */         int pos = 0;
/*  89:119 */         for (Object o : c)
/*  90:    */         {
/*  91:120 */           if (pos == idx) {
/*  92:121 */             return new TypedValue(o, targetObjectTypeDescriptor.elementTypeDescriptor(o));
/*  93:    */           }
/*  94:123 */           pos++;
/*  95:    */         }
/*  96:    */       }
/*  97:125 */       else if ((targetObject instanceof String))
/*  98:    */       {
/*  99:126 */         String ctxString = (String)targetObject;
/* 100:127 */         if (idx >= ctxString.length()) {
/* 101:128 */           throw new SpelEvaluationException(getStartPosition(), SpelMessage.STRING_INDEX_OUT_OF_BOUNDS, new Object[] { Integer.valueOf(ctxString.length()), Integer.valueOf(idx) });
/* 102:    */         }
/* 103:130 */         return new TypedValue(String.valueOf(ctxString.charAt(idx)));
/* 104:    */       }
/* 105:    */     }
/* 106:136 */     if (indexValue.getTypeDescriptor().getType() == String.class)
/* 107:    */     {
/* 108:137 */       Class<?> targetObjectRuntimeClass = getObjectClass(targetObject);
/* 109:138 */       String name = (String)indexValue.getValue();
/* 110:139 */       EvaluationContext eContext = state.getEvaluationContext();
/* 111:    */       try
/* 112:    */       {
/* 113:142 */         if ((this.cachedReadName != null) && (this.cachedReadName.equals(name)) && (this.cachedReadTargetType != null) && (this.cachedReadTargetType.equals(targetObjectRuntimeClass))) {
/* 114:144 */           return this.cachedReadAccessor.read(eContext, targetObject, name);
/* 115:    */         }
/* 116:147 */         List<PropertyAccessor> accessorsToTry = AstUtils.getPropertyAccessorsToTry(targetObjectRuntimeClass, state);
/* 117:149 */         if (accessorsToTry != null) {
/* 118:150 */           for (PropertyAccessor accessor : accessorsToTry) {
/* 119:151 */             if (accessor.canRead(eContext, targetObject, name))
/* 120:    */             {
/* 121:152 */               if ((accessor instanceof ReflectivePropertyAccessor)) {
/* 122:153 */                 accessor = ((ReflectivePropertyAccessor)accessor).createOptimalAccessor(eContext, targetObject, name);
/* 123:    */               }
/* 124:155 */               this.cachedReadAccessor = accessor;
/* 125:156 */               this.cachedReadName = name;
/* 126:157 */               this.cachedReadTargetType = targetObjectRuntimeClass;
/* 127:158 */               return accessor.read(eContext, targetObject, name);
/* 128:    */             }
/* 129:    */           }
/* 130:    */         }
/* 131:    */       }
/* 132:    */       catch (AccessException e)
/* 133:    */       {
/* 134:163 */         throw new SpelEvaluationException(getStartPosition(), e, SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, new Object[] { targetObjectTypeDescriptor.toString() });
/* 135:    */       }
/* 136:    */     }
/* 137:167 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, new Object[] { targetObjectTypeDescriptor.toString() });
/* 138:    */   }
/* 139:    */   
/* 140:    */   public boolean isWritable(ExpressionState expressionState)
/* 141:    */     throws SpelEvaluationException
/* 142:    */   {
/* 143:172 */     return true;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public void setValue(ExpressionState state, Object newValue)
/* 147:    */     throws EvaluationException
/* 148:    */   {
/* 149:178 */     TypedValue contextObject = state.getActiveContextObject();
/* 150:179 */     Object targetObject = contextObject.getValue();
/* 151:180 */     TypeDescriptor targetObjectTypeDescriptor = contextObject.getTypeDescriptor();
/* 152:181 */     TypedValue index = this.children[0].getValueInternal(state);
/* 153:183 */     if (targetObject == null) {
/* 154:184 */       throw new SpelEvaluationException(SpelMessage.CANNOT_INDEX_INTO_NULL_VALUE, new Object[0]);
/* 155:    */     }
/* 156:187 */     if ((targetObject instanceof Map))
/* 157:    */     {
/* 158:188 */       Map map = (Map)targetObject;
/* 159:189 */       Object key = index.getValue();
/* 160:190 */       if (targetObjectTypeDescriptor.getMapKeyTypeDescriptor() != null) {
/* 161:191 */         key = state.convertValue(index, targetObjectTypeDescriptor.getMapKeyTypeDescriptor());
/* 162:    */       }
/* 163:193 */       if (targetObjectTypeDescriptor.getMapValueTypeDescriptor() != null) {
/* 164:194 */         newValue = state.convertValue(newValue, targetObjectTypeDescriptor.getMapValueTypeDescriptor());
/* 165:    */       }
/* 166:196 */       map.put(key, newValue);
/* 167:197 */       return;
/* 168:    */     }
/* 169:200 */     if (targetObjectTypeDescriptor.isArray())
/* 170:    */     {
/* 171:201 */       int idx = ((Integer)state.convertValue(index, TypeDescriptor.valueOf(Integer.class))).intValue();
/* 172:202 */       setArrayElement(state, contextObject.getValue(), idx, newValue, targetObjectTypeDescriptor.getElementTypeDescriptor().getType());
/* 173:203 */       return;
/* 174:    */     }
/* 175:205 */     if ((targetObject instanceof Collection))
/* 176:    */     {
/* 177:206 */       int idx = ((Integer)state.convertValue(index, TypeDescriptor.valueOf(Integer.class))).intValue();
/* 178:207 */       Collection c = (Collection)targetObject;
/* 179:208 */       if ((idx >= c.size()) && 
/* 180:209 */         (!growCollection(state, targetObjectTypeDescriptor, idx, c))) {
/* 181:210 */         throw new SpelEvaluationException(getStartPosition(), SpelMessage.COLLECTION_INDEX_OUT_OF_BOUNDS, new Object[] { Integer.valueOf(c.size()), Integer.valueOf(idx) });
/* 182:    */       }
/* 183:213 */       if ((targetObject instanceof List))
/* 184:    */       {
/* 185:214 */         List list = (List)targetObject;
/* 186:215 */         if (targetObjectTypeDescriptor.getElementTypeDescriptor() != null) {
/* 187:216 */           newValue = state.convertValue(newValue, targetObjectTypeDescriptor.getElementTypeDescriptor());
/* 188:    */         }
/* 189:218 */         list.set(idx, newValue);
/* 190:219 */         return;
/* 191:    */       }
/* 192:222 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, new Object[] { targetObjectTypeDescriptor.toString() });
/* 193:    */     }
/* 194:228 */     if (index.getTypeDescriptor().getType() == String.class)
/* 195:    */     {
/* 196:229 */       Class<?> contextObjectClass = getObjectClass(contextObject.getValue());
/* 197:230 */       String name = (String)index.getValue();
/* 198:231 */       EvaluationContext eContext = state.getEvaluationContext();
/* 199:    */       try
/* 200:    */       {
/* 201:233 */         if ((this.cachedWriteName != null) && (this.cachedWriteName.equals(name)) && (this.cachedWriteTargetType != null) && (this.cachedWriteTargetType.equals(contextObjectClass)))
/* 202:    */         {
/* 203:235 */           this.cachedWriteAccessor.write(eContext, targetObject, name, newValue);
/* 204:236 */           return;
/* 205:    */         }
/* 206:239 */         List<PropertyAccessor> accessorsToTry = AstUtils.getPropertyAccessorsToTry(contextObjectClass, state);
/* 207:240 */         if (accessorsToTry != null) {
/* 208:241 */           for (PropertyAccessor accessor : accessorsToTry) {
/* 209:242 */             if (accessor.canWrite(eContext, contextObject.getValue(), name))
/* 210:    */             {
/* 211:243 */               this.cachedWriteName = name;
/* 212:244 */               this.cachedWriteTargetType = contextObjectClass;
/* 213:245 */               this.cachedWriteAccessor = accessor;
/* 214:246 */               accessor.write(eContext, contextObject.getValue(), name, newValue);
/* 215:247 */               return;
/* 216:    */             }
/* 217:    */           }
/* 218:    */         }
/* 219:    */       }
/* 220:    */       catch (AccessException ae)
/* 221:    */       {
/* 222:252 */         throw new SpelEvaluationException(getStartPosition(), ae, SpelMessage.EXCEPTION_DURING_PROPERTY_WRITE, new Object[] {
/* 223:253 */           name, ae.getMessage() });
/* 224:    */       }
/* 225:    */     }
/* 226:258 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.INDEXING_NOT_SUPPORTED_FOR_TYPE, new Object[] { targetObjectTypeDescriptor.toString() });
/* 227:    */   }
/* 228:    */   
/* 229:    */   private boolean growCollection(ExpressionState state, TypeDescriptor targetType, int index, Collection collection)
/* 230:    */   {
/* 231:273 */     if (state.getConfiguration().isAutoGrowCollections())
/* 232:    */     {
/* 233:274 */       if (targetType.getElementTypeDescriptor() == null) {
/* 234:275 */         throw new SpelEvaluationException(getStartPosition(), SpelMessage.UNABLE_TO_GROW_COLLECTION_UNKNOWN_ELEMENT_TYPE, new Object[0]);
/* 235:    */       }
/* 236:277 */       TypeDescriptor elementType = targetType.getElementTypeDescriptor();
/* 237:278 */       Object newCollectionElement = null;
/* 238:    */       try
/* 239:    */       {
/* 240:280 */         int newElements = index - collection.size();
/* 241:281 */         while (newElements > 0)
/* 242:    */         {
/* 243:282 */           collection.add(elementType.getType().newInstance());
/* 244:283 */           newElements--;
/* 245:    */         }
/* 246:285 */         newCollectionElement = elementType.getType().newInstance();
/* 247:    */       }
/* 248:    */       catch (Exception ex)
/* 249:    */       {
/* 250:288 */         throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.UNABLE_TO_GROW_COLLECTION, new Object[0]);
/* 251:    */       }
/* 252:290 */       collection.add(newCollectionElement);
/* 253:291 */       return true;
/* 254:    */     }
/* 255:293 */     return false;
/* 256:    */   }
/* 257:    */   
/* 258:    */   public String toStringAST()
/* 259:    */   {
/* 260:298 */     StringBuilder sb = new StringBuilder();
/* 261:299 */     sb.append("[");
/* 262:300 */     for (int i = 0; i < getChildCount(); i++)
/* 263:    */     {
/* 264:301 */       if (i > 0) {
/* 265:302 */         sb.append(",");
/* 266:    */       }
/* 267:303 */       sb.append(getChild(i).toStringAST());
/* 268:    */     }
/* 269:305 */     sb.append("]");
/* 270:306 */     return sb.toString();
/* 271:    */   }
/* 272:    */   
/* 273:    */   private void setArrayElement(ExpressionState state, Object ctx, int idx, Object newValue, Class clazz)
/* 274:    */     throws EvaluationException
/* 275:    */   {
/* 276:310 */     Class<?> arrayComponentType = clazz;
/* 277:311 */     if (arrayComponentType == Integer.TYPE)
/* 278:    */     {
/* 279:312 */       int[] array = (int[])ctx;
/* 280:313 */       checkAccess(array.length, idx);
/* 281:314 */       array[idx] = ((Integer)state.convertValue(newValue, TypeDescriptor.valueOf(Integer.class))).intValue();
/* 282:    */     }
/* 283:315 */     else if (arrayComponentType == Boolean.TYPE)
/* 284:    */     {
/* 285:316 */       boolean[] array = (boolean[])ctx;
/* 286:317 */       checkAccess(array.length, idx);
/* 287:318 */       array[idx] = ((Boolean)state.convertValue(newValue, TypeDescriptor.valueOf(Boolean.class))).booleanValue();
/* 288:    */     }
/* 289:319 */     else if (arrayComponentType == Character.TYPE)
/* 290:    */     {
/* 291:320 */       char[] array = (char[])ctx;
/* 292:321 */       checkAccess(array.length, idx);
/* 293:322 */       array[idx] = ((Character)state.convertValue(newValue, TypeDescriptor.valueOf(Character.class))).charValue();
/* 294:    */     }
/* 295:323 */     else if (arrayComponentType == Long.TYPE)
/* 296:    */     {
/* 297:324 */       long[] array = (long[])ctx;
/* 298:325 */       checkAccess(array.length, idx);
/* 299:326 */       array[idx] = ((Long)state.convertValue(newValue, TypeDescriptor.valueOf(Long.class))).longValue();
/* 300:    */     }
/* 301:327 */     else if (arrayComponentType == Short.TYPE)
/* 302:    */     {
/* 303:328 */       short[] array = (short[])ctx;
/* 304:329 */       checkAccess(array.length, idx);
/* 305:330 */       array[idx] = ((Short)state.convertValue(newValue, TypeDescriptor.valueOf(Short.class))).shortValue();
/* 306:    */     }
/* 307:331 */     else if (arrayComponentType == Double.TYPE)
/* 308:    */     {
/* 309:332 */       double[] array = (double[])ctx;
/* 310:333 */       checkAccess(array.length, idx);
/* 311:334 */       array[idx] = ((Double)state.convertValue(newValue, TypeDescriptor.valueOf(Double.class))).doubleValue();
/* 312:    */     }
/* 313:335 */     else if (arrayComponentType == Float.TYPE)
/* 314:    */     {
/* 315:336 */       float[] array = (float[])ctx;
/* 316:337 */       checkAccess(array.length, idx);
/* 317:338 */       array[idx] = ((Float)state.convertValue(newValue, TypeDescriptor.valueOf(Float.class))).floatValue();
/* 318:    */     }
/* 319:339 */     else if (arrayComponentType == Byte.TYPE)
/* 320:    */     {
/* 321:340 */       byte[] array = (byte[])ctx;
/* 322:341 */       checkAccess(array.length, idx);
/* 323:342 */       array[idx] = ((Byte)state.convertValue(newValue, TypeDescriptor.valueOf(Byte.class))).byteValue();
/* 324:    */     }
/* 325:    */     else
/* 326:    */     {
/* 327:344 */       Object[] array = (Object[])ctx;
/* 328:345 */       checkAccess(array.length, idx);
/* 329:346 */       array[idx] = state.convertValue(newValue, TypeDescriptor.valueOf(clazz));
/* 330:    */     }
/* 331:    */   }
/* 332:    */   
/* 333:    */   private Object accessArrayElement(Object ctx, int idx)
/* 334:    */     throws SpelEvaluationException
/* 335:    */   {
/* 336:351 */     Class<?> arrayComponentType = ctx.getClass().getComponentType();
/* 337:352 */     if (arrayComponentType == Integer.TYPE)
/* 338:    */     {
/* 339:353 */       int[] array = (int[])ctx;
/* 340:354 */       checkAccess(array.length, idx);
/* 341:355 */       return Integer.valueOf(array[idx]);
/* 342:    */     }
/* 343:356 */     if (arrayComponentType == Boolean.TYPE)
/* 344:    */     {
/* 345:357 */       boolean[] array = (boolean[])ctx;
/* 346:358 */       checkAccess(array.length, idx);
/* 347:359 */       return Boolean.valueOf(array[idx]);
/* 348:    */     }
/* 349:360 */     if (arrayComponentType == Character.TYPE)
/* 350:    */     {
/* 351:361 */       char[] array = (char[])ctx;
/* 352:362 */       checkAccess(array.length, idx);
/* 353:363 */       return Character.valueOf(array[idx]);
/* 354:    */     }
/* 355:364 */     if (arrayComponentType == Long.TYPE)
/* 356:    */     {
/* 357:365 */       long[] array = (long[])ctx;
/* 358:366 */       checkAccess(array.length, idx);
/* 359:367 */       return Long.valueOf(array[idx]);
/* 360:    */     }
/* 361:368 */     if (arrayComponentType == Short.TYPE)
/* 362:    */     {
/* 363:369 */       short[] array = (short[])ctx;
/* 364:370 */       checkAccess(array.length, idx);
/* 365:371 */       return Short.valueOf(array[idx]);
/* 366:    */     }
/* 367:372 */     if (arrayComponentType == Double.TYPE)
/* 368:    */     {
/* 369:373 */       double[] array = (double[])ctx;
/* 370:374 */       checkAccess(array.length, idx);
/* 371:375 */       return Double.valueOf(array[idx]);
/* 372:    */     }
/* 373:376 */     if (arrayComponentType == Float.TYPE)
/* 374:    */     {
/* 375:377 */       float[] array = (float[])ctx;
/* 376:378 */       checkAccess(array.length, idx);
/* 377:379 */       return Float.valueOf(array[idx]);
/* 378:    */     }
/* 379:380 */     if (arrayComponentType == Byte.TYPE)
/* 380:    */     {
/* 381:381 */       byte[] array = (byte[])ctx;
/* 382:382 */       checkAccess(array.length, idx);
/* 383:383 */       return Byte.valueOf(array[idx]);
/* 384:    */     }
/* 385:385 */     Object[] array = (Object[])ctx;
/* 386:386 */     checkAccess(array.length, idx);
/* 387:387 */     return array[idx];
/* 388:    */   }
/* 389:    */   
/* 390:    */   private void checkAccess(int arrayLength, int index)
/* 391:    */     throws SpelEvaluationException
/* 392:    */   {
/* 393:392 */     if (index > arrayLength) {
/* 394:393 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.ARRAY_INDEX_OUT_OF_BOUNDS, new Object[] { Integer.valueOf(arrayLength), Integer.valueOf(index) });
/* 395:    */     }
/* 396:    */   }
/* 397:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.Indexer
 * JD-Core Version:    0.7.0.1
 */