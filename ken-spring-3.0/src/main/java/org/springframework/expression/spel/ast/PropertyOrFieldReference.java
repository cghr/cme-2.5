/*   1:    */ package org.springframework.expression.spel.ast;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Map;
/*   7:    */ import org.springframework.core.convert.TypeDescriptor;
/*   8:    */ import org.springframework.expression.AccessException;
/*   9:    */ import org.springframework.expression.EvaluationContext;
/*  10:    */ import org.springframework.expression.EvaluationException;
/*  11:    */ import org.springframework.expression.PropertyAccessor;
/*  12:    */ import org.springframework.expression.TypedValue;
/*  13:    */ import org.springframework.expression.spel.ExpressionState;
/*  14:    */ import org.springframework.expression.spel.SpelEvaluationException;
/*  15:    */ import org.springframework.expression.spel.SpelMessage;
/*  16:    */ import org.springframework.expression.spel.SpelParserConfiguration;
/*  17:    */ import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
/*  18:    */ 
/*  19:    */ public class PropertyOrFieldReference
/*  20:    */   extends SpelNodeImpl
/*  21:    */ {
/*  22:    */   private final boolean nullSafe;
/*  23:    */   private final String name;
/*  24:    */   private volatile PropertyAccessor cachedReadAccessor;
/*  25:    */   private volatile PropertyAccessor cachedWriteAccessor;
/*  26:    */   
/*  27:    */   public PropertyOrFieldReference(boolean nullSafe, String propertyOrFieldName, int pos)
/*  28:    */   {
/*  29: 55 */     super(pos, new SpelNodeImpl[0]);
/*  30: 56 */     this.nullSafe = nullSafe;
/*  31: 57 */     this.name = propertyOrFieldName;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public boolean isNullSafe()
/*  35:    */   {
/*  36: 62 */     return this.nullSafe;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String getName()
/*  40:    */   {
/*  41: 66 */     return this.name;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public TypedValue getValueInternal(ExpressionState state)
/*  45:    */     throws EvaluationException
/*  46:    */   {
/*  47: 72 */     TypedValue result = readProperty(state, this.name);
/*  48: 75 */     if ((result.getValue() == null) && (state.getConfiguration().isAutoGrowNullReferences())) {
/*  49: 76 */       if (nextChildIs(new Class[] { Indexer.class, PropertyOrFieldReference.class }))
/*  50:    */       {
/*  51: 77 */         TypeDescriptor resultDescriptor = result.getTypeDescriptor();
/*  52: 79 */         if ((resultDescriptor.getType().equals(List.class)) || (resultDescriptor.getType().equals(Map.class)))
/*  53:    */         {
/*  54: 81 */           if (resultDescriptor.getType().equals(List.class)) {
/*  55:    */             try
/*  56:    */             {
/*  57: 83 */               if (!isWritable(state)) {
/*  58:    */                 return result;
/*  59:    */               }
/*  60: 84 */               List newList = (List)ArrayList.class.newInstance();
/*  61: 85 */               writeProperty(state, this.name, newList);
/*  62: 86 */               result = readProperty(state, this.name);
/*  63:    */             }
/*  64:    */             catch (InstantiationException ex)
/*  65:    */             {
/*  66: 90 */               throw new SpelEvaluationException(getStartPosition(), ex, 
/*  67: 91 */                 SpelMessage.UNABLE_TO_CREATE_LIST_FOR_INDEXING, new Object[0]);
/*  68:    */             }
/*  69:    */             catch (IllegalAccessException ex)
/*  70:    */             {
/*  71: 94 */               throw new SpelEvaluationException(getStartPosition(), ex, 
/*  72: 95 */                 SpelMessage.UNABLE_TO_CREATE_LIST_FOR_INDEXING, new Object[0]);
/*  73:    */             }
/*  74:    */           } else {
/*  75:    */             try
/*  76:    */             {
/*  77:100 */               if (!isWritable(state)) {
/*  78:    */                 return result;
/*  79:    */               }
/*  80:101 */               Map newMap = (Map)HashMap.class.newInstance();
/*  81:102 */               writeProperty(state, this.name, newMap);
/*  82:103 */               result = readProperty(state, this.name);
/*  83:    */             }
/*  84:    */             catch (InstantiationException ex)
/*  85:    */             {
/*  86:107 */               throw new SpelEvaluationException(getStartPosition(), ex, 
/*  87:108 */                 SpelMessage.UNABLE_TO_CREATE_MAP_FOR_INDEXING, new Object[0]);
/*  88:    */             }
/*  89:    */             catch (IllegalAccessException ex)
/*  90:    */             {
/*  91:111 */               throw new SpelEvaluationException(getStartPosition(), ex, 
/*  92:112 */                 SpelMessage.UNABLE_TO_CREATE_MAP_FOR_INDEXING, new Object[0]);
/*  93:    */             }
/*  94:    */           }
/*  95:    */         }
/*  96:    */         else {
/*  97:    */           try
/*  98:    */           {
/*  99:119 */             if (isWritable(state))
/* 100:    */             {
/* 101:120 */               Object newObject = result.getTypeDescriptor().getType().newInstance();
/* 102:121 */               writeProperty(state, this.name, newObject);
/* 103:122 */               result = readProperty(state, this.name);
/* 104:    */             }
/* 105:    */           }
/* 106:    */           catch (InstantiationException ex)
/* 107:    */           {
/* 108:126 */             throw new SpelEvaluationException(getStartPosition(), ex, 
/* 109:127 */               SpelMessage.UNABLE_TO_DYNAMICALLY_CREATE_OBJECT, new Object[] { result.getTypeDescriptor().getType() });
/* 110:    */           }
/* 111:    */           catch (IllegalAccessException ex)
/* 112:    */           {
/* 113:130 */             throw new SpelEvaluationException(getStartPosition(), ex, 
/* 114:131 */               SpelMessage.UNABLE_TO_DYNAMICALLY_CREATE_OBJECT, new Object[] { result.getTypeDescriptor().getType() });
/* 115:    */           }
/* 116:    */         }
/* 117:    */       }
/* 118:    */     }
/* 119:135 */     return result;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public void setValue(ExpressionState state, Object newValue)
/* 123:    */     throws SpelEvaluationException
/* 124:    */   {
/* 125:140 */     writeProperty(state, this.name, newValue);
/* 126:    */   }
/* 127:    */   
/* 128:    */   public boolean isWritable(ExpressionState state)
/* 129:    */     throws SpelEvaluationException
/* 130:    */   {
/* 131:145 */     return isWritableProperty(this.name, state);
/* 132:    */   }
/* 133:    */   
/* 134:    */   public String toStringAST()
/* 135:    */   {
/* 136:150 */     return this.name;
/* 137:    */   }
/* 138:    */   
/* 139:    */   private TypedValue readProperty(ExpressionState state, String name)
/* 140:    */     throws EvaluationException
/* 141:    */   {
/* 142:161 */     TypedValue contextObject = state.getActiveContextObject();
/* 143:162 */     Object targetObject = contextObject.getValue();
/* 144:164 */     if ((targetObject == null) && (this.nullSafe)) {
/* 145:165 */       return TypedValue.NULL;
/* 146:    */     }
/* 147:168 */     PropertyAccessor accessorToUse = this.cachedReadAccessor;
/* 148:169 */     if (accessorToUse != null) {
/* 149:    */       try
/* 150:    */       {
/* 151:171 */         return accessorToUse.read(state.getEvaluationContext(), contextObject.getValue(), name);
/* 152:    */       }
/* 153:    */       catch (AccessException localAccessException1)
/* 154:    */       {
/* 155:176 */         this.cachedReadAccessor = null;
/* 156:    */       }
/* 157:    */     }
/* 158:180 */     Class<?> contextObjectClass = getObjectClass(contextObject.getValue());
/* 159:181 */     List<PropertyAccessor> accessorsToTry = getPropertyAccessorsToTry(contextObjectClass, state);
/* 160:182 */     EvaluationContext eContext = state.getEvaluationContext();
/* 161:187 */     if (accessorsToTry != null) {
/* 162:    */       try
/* 163:    */       {
/* 164:189 */         for (PropertyAccessor accessor : accessorsToTry) {
/* 165:190 */           if (accessor.canRead(eContext, contextObject.getValue(), name))
/* 166:    */           {
/* 167:191 */             if ((accessor instanceof ReflectivePropertyAccessor)) {
/* 168:192 */               accessor = ((ReflectivePropertyAccessor)accessor).createOptimalAccessor(
/* 169:193 */                 eContext, contextObject.getValue(), name);
/* 170:    */             }
/* 171:195 */             this.cachedReadAccessor = accessor;
/* 172:196 */             return accessor.read(eContext, contextObject.getValue(), name);
/* 173:    */           }
/* 174:    */         }
/* 175:    */       }
/* 176:    */       catch (AccessException ae)
/* 177:    */       {
/* 178:201 */         throw new SpelEvaluationException(ae, SpelMessage.EXCEPTION_DURING_PROPERTY_READ, new Object[] { name, ae.getMessage() });
/* 179:    */       }
/* 180:    */     }
/* 181:204 */     if (contextObject.getValue() == null) {
/* 182:205 */       throw new SpelEvaluationException(SpelMessage.PROPERTY_OR_FIELD_NOT_READABLE_ON_NULL, new Object[] { name });
/* 183:    */     }
/* 184:208 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.PROPERTY_OR_FIELD_NOT_READABLE, new Object[] { name, 
/* 185:209 */       FormatHelper.formatClassNameForMessage(contextObjectClass) });
/* 186:    */   }
/* 187:    */   
/* 188:    */   private void writeProperty(ExpressionState state, String name, Object newValue)
/* 189:    */     throws SpelEvaluationException
/* 190:    */   {
/* 191:214 */     TypedValue contextObject = state.getActiveContextObject();
/* 192:215 */     EvaluationContext eContext = state.getEvaluationContext();
/* 193:217 */     if ((contextObject.getValue() == null) && (this.nullSafe)) {
/* 194:218 */       return;
/* 195:    */     }
/* 196:221 */     PropertyAccessor accessorToUse = this.cachedWriteAccessor;
/* 197:222 */     if (accessorToUse != null) {
/* 198:    */       try
/* 199:    */       {
/* 200:224 */         accessorToUse.write(state.getEvaluationContext(), contextObject.getValue(), name, newValue);
/* 201:225 */         return;
/* 202:    */       }
/* 203:    */       catch (AccessException localAccessException1)
/* 204:    */       {
/* 205:230 */         this.cachedWriteAccessor = null;
/* 206:    */       }
/* 207:    */     }
/* 208:234 */     Class<?> contextObjectClass = getObjectClass(contextObject.getValue());
/* 209:    */     
/* 210:236 */     List<PropertyAccessor> accessorsToTry = getPropertyAccessorsToTry(contextObjectClass, state);
/* 211:237 */     if (accessorsToTry != null) {
/* 212:    */       try
/* 213:    */       {
/* 214:239 */         for (PropertyAccessor accessor : accessorsToTry) {
/* 215:240 */           if (accessor.canWrite(eContext, contextObject.getValue(), name))
/* 216:    */           {
/* 217:241 */             this.cachedWriteAccessor = accessor;
/* 218:242 */             accessor.write(eContext, contextObject.getValue(), name, newValue);
/* 219:243 */             return;
/* 220:    */           }
/* 221:    */         }
/* 222:    */       }
/* 223:    */       catch (AccessException ae)
/* 224:    */       {
/* 225:248 */         throw new SpelEvaluationException(getStartPosition(), ae, SpelMessage.EXCEPTION_DURING_PROPERTY_WRITE, new Object[] {
/* 226:249 */           name, ae.getMessage() });
/* 227:    */       }
/* 228:    */     }
/* 229:252 */     if (contextObject.getValue() == null) {
/* 230:253 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.PROPERTY_OR_FIELD_NOT_WRITABLE_ON_NULL, new Object[] { name });
/* 231:    */     }
/* 232:256 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.PROPERTY_OR_FIELD_NOT_WRITABLE, new Object[] { name, 
/* 233:257 */       FormatHelper.formatClassNameForMessage(contextObjectClass) });
/* 234:    */   }
/* 235:    */   
/* 236:    */   public boolean isWritableProperty(String name, ExpressionState state)
/* 237:    */     throws SpelEvaluationException
/* 238:    */   {
/* 239:262 */     Object contextObject = state.getActiveContextObject().getValue();
/* 240:    */     
/* 241:264 */     EvaluationContext eContext = state.getEvaluationContext();
/* 242:265 */     List<PropertyAccessor> resolversToTry = getPropertyAccessorsToTry(getObjectClass(contextObject), state);
/* 243:266 */     if (resolversToTry != null) {
/* 244:267 */       for (PropertyAccessor pfResolver : resolversToTry) {
/* 245:    */         try
/* 246:    */         {
/* 247:269 */           if (pfResolver.canWrite(eContext, contextObject, name)) {
/* 248:270 */             return true;
/* 249:    */           }
/* 250:    */         }
/* 251:    */         catch (AccessException localAccessException) {}
/* 252:    */       }
/* 253:    */     }
/* 254:278 */     return false;
/* 255:    */   }
/* 256:    */   
/* 257:    */   private List<PropertyAccessor> getPropertyAccessorsToTry(Class<?> targetType, ExpressionState state)
/* 258:    */   {
/* 259:293 */     List<PropertyAccessor> specificAccessors = new ArrayList();
/* 260:294 */     List<PropertyAccessor> generalAccessors = new ArrayList();
/* 261:295 */     for (PropertyAccessor resolver : state.getPropertyAccessors())
/* 262:    */     {
/* 263:296 */       Class[] targets = resolver.getSpecificTargetClasses();
/* 264:297 */       if (targets == null) {
/* 265:298 */         generalAccessors.add(resolver);
/* 266:301 */       } else if (targetType != null) {
/* 267:302 */         for (Class<?> clazz : targets)
/* 268:    */         {
/* 269:303 */           if (clazz == targetType)
/* 270:    */           {
/* 271:304 */             specificAccessors.add(resolver);
/* 272:305 */             break;
/* 273:    */           }
/* 274:307 */           if (clazz.isAssignableFrom(targetType)) {
/* 275:308 */             generalAccessors.add(resolver);
/* 276:    */           }
/* 277:    */         }
/* 278:    */       }
/* 279:    */     }
/* 280:314 */     List<PropertyAccessor> resolvers = new ArrayList();
/* 281:315 */     resolvers.addAll(specificAccessors);
/* 282:316 */     generalAccessors.removeAll(specificAccessors);
/* 283:317 */     resolvers.addAll(generalAccessors);
/* 284:318 */     return resolvers;
/* 285:    */   }
/* 286:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.PropertyOrFieldReference
 * JD-Core Version:    0.7.0.1
 */