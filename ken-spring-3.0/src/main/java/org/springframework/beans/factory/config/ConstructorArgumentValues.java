/*   1:    */ package org.springframework.beans.factory.config;
/*   2:    */ 
/*   3:    */ import java.util.Collections;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import java.util.LinkedHashMap;
/*   6:    */ import java.util.LinkedList;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Map;
/*   9:    */ import java.util.Map.Entry;
/*  10:    */ import java.util.Set;
/*  11:    */ import org.springframework.beans.BeanMetadataElement;
/*  12:    */ import org.springframework.beans.Mergeable;
/*  13:    */ import org.springframework.util.Assert;
/*  14:    */ import org.springframework.util.ClassUtils;
/*  15:    */ import org.springframework.util.ObjectUtils;
/*  16:    */ 
/*  17:    */ public class ConstructorArgumentValues
/*  18:    */ {
/*  19: 45 */   private final Map<Integer, ValueHolder> indexedArgumentValues = new LinkedHashMap(0);
/*  20: 47 */   private final List<ValueHolder> genericArgumentValues = new LinkedList();
/*  21:    */   
/*  22:    */   public ConstructorArgumentValues() {}
/*  23:    */   
/*  24:    */   public ConstructorArgumentValues(ConstructorArgumentValues original)
/*  25:    */   {
/*  26: 61 */     addArgumentValues(original);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void addArgumentValues(ConstructorArgumentValues other)
/*  30:    */   {
/*  31: 73 */     if (other != null)
/*  32:    */     {
/*  33: 74 */       for (Map.Entry<Integer, ValueHolder> entry : other.indexedArgumentValues.entrySet()) {
/*  34: 75 */         addOrMergeIndexedArgumentValue((Integer)entry.getKey(), ((ValueHolder)entry.getValue()).copy());
/*  35:    */       }
/*  36: 77 */       for (ValueHolder valueHolder : other.genericArgumentValues) {
/*  37: 78 */         if (!this.genericArgumentValues.contains(valueHolder)) {
/*  38: 79 */           addOrMergeGenericArgumentValue(valueHolder.copy());
/*  39:    */         }
/*  40:    */       }
/*  41:    */     }
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void addIndexedArgumentValue(int index, Object value)
/*  45:    */   {
/*  46: 92 */     addIndexedArgumentValue(index, new ValueHolder(value));
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void addIndexedArgumentValue(int index, Object value, String type)
/*  50:    */   {
/*  51:102 */     addIndexedArgumentValue(index, new ValueHolder(value, type));
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void addIndexedArgumentValue(int index, ValueHolder newValue)
/*  55:    */   {
/*  56:111 */     Assert.isTrue(index >= 0, "Index must not be negative");
/*  57:112 */     Assert.notNull(newValue, "ValueHolder must not be null");
/*  58:113 */     addOrMergeIndexedArgumentValue(Integer.valueOf(index), newValue);
/*  59:    */   }
/*  60:    */   
/*  61:    */   private void addOrMergeIndexedArgumentValue(Integer key, ValueHolder newValue)
/*  62:    */   {
/*  63:124 */     ValueHolder currentValue = (ValueHolder)this.indexedArgumentValues.get(key);
/*  64:125 */     if ((currentValue != null) && ((newValue.getValue() instanceof Mergeable)))
/*  65:    */     {
/*  66:126 */       Mergeable mergeable = (Mergeable)newValue.getValue();
/*  67:127 */       if (mergeable.isMergeEnabled()) {
/*  68:128 */         newValue.setValue(mergeable.merge(currentValue.getValue()));
/*  69:    */       }
/*  70:    */     }
/*  71:131 */     this.indexedArgumentValues.put(key, newValue);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public boolean hasIndexedArgumentValue(int index)
/*  75:    */   {
/*  76:139 */     return this.indexedArgumentValues.containsKey(Integer.valueOf(index));
/*  77:    */   }
/*  78:    */   
/*  79:    */   public ValueHolder getIndexedArgumentValue(int index, Class requiredType)
/*  80:    */   {
/*  81:150 */     return getIndexedArgumentValue(index, requiredType, null);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public ValueHolder getIndexedArgumentValue(int index, Class requiredType, String requiredName)
/*  85:    */   {
/*  86:163 */     Assert.isTrue(index >= 0, "Index must not be negative");
/*  87:164 */     ValueHolder valueHolder = (ValueHolder)this.indexedArgumentValues.get(Integer.valueOf(index));
/*  88:165 */     if ((valueHolder != null) && 
/*  89:166 */       ((valueHolder.getType() == null) || (
/*  90:167 */       (requiredType != null) && (ClassUtils.matchesTypeName(requiredType, valueHolder.getType())))) && (
/*  91:168 */       (valueHolder.getName() == null) || (
/*  92:169 */       (requiredName != null) && (requiredName.equals(valueHolder.getName()))))) {
/*  93:170 */       return valueHolder;
/*  94:    */     }
/*  95:172 */     return null;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public Map<Integer, ValueHolder> getIndexedArgumentValues()
/*  99:    */   {
/* 100:181 */     return Collections.unmodifiableMap(this.indexedArgumentValues);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void addGenericArgumentValue(Object value)
/* 104:    */   {
/* 105:192 */     this.genericArgumentValues.add(new ValueHolder(value));
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void addGenericArgumentValue(Object value, String type)
/* 109:    */   {
/* 110:203 */     this.genericArgumentValues.add(new ValueHolder(value, type));
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void addGenericArgumentValue(ValueHolder newValue)
/* 114:    */   {
/* 115:216 */     Assert.notNull(newValue, "ValueHolder must not be null");
/* 116:217 */     if (!this.genericArgumentValues.contains(newValue)) {
/* 117:218 */       addOrMergeGenericArgumentValue(newValue);
/* 118:    */     }
/* 119:    */   }
/* 120:    */   
/* 121:    */   private void addOrMergeGenericArgumentValue(ValueHolder newValue)
/* 122:    */   {
/* 123:228 */     if (newValue.getName() != null) {
/* 124:229 */       for (Iterator<ValueHolder> it = this.genericArgumentValues.iterator(); it.hasNext();)
/* 125:    */       {
/* 126:230 */         ValueHolder currentValue = (ValueHolder)it.next();
/* 127:231 */         if (newValue.getName().equals(currentValue.getName()))
/* 128:    */         {
/* 129:232 */           if ((newValue.getValue() instanceof Mergeable))
/* 130:    */           {
/* 131:233 */             Mergeable mergeable = (Mergeable)newValue.getValue();
/* 132:234 */             if (mergeable.isMergeEnabled()) {
/* 133:235 */               newValue.setValue(mergeable.merge(currentValue.getValue()));
/* 134:    */             }
/* 135:    */           }
/* 136:238 */           it.remove();
/* 137:    */         }
/* 138:    */       }
/* 139:    */     }
/* 140:242 */     this.genericArgumentValues.add(newValue);
/* 141:    */   }
/* 142:    */   
/* 143:    */   public ValueHolder getGenericArgumentValue(Class requiredType)
/* 144:    */   {
/* 145:251 */     return getGenericArgumentValue(requiredType, null, null);
/* 146:    */   }
/* 147:    */   
/* 148:    */   public ValueHolder getGenericArgumentValue(Class requiredType, String requiredName)
/* 149:    */   {
/* 150:261 */     return getGenericArgumentValue(requiredType, requiredName, null);
/* 151:    */   }
/* 152:    */   
/* 153:    */   public ValueHolder getGenericArgumentValue(Class requiredType, String requiredName, Set<ValueHolder> usedValueHolders)
/* 154:    */   {
/* 155:277 */     for (ValueHolder valueHolder : this.genericArgumentValues) {
/* 156:278 */       if ((usedValueHolders == null) || (!usedValueHolders.contains(valueHolder))) {
/* 157:281 */         if ((valueHolder.getName() == null) || (
/* 158:282 */           (requiredName != null) && (valueHolder.getName().equals(requiredName)))) {
/* 159:285 */           if ((valueHolder.getType() == null) || (
/* 160:286 */             (requiredType != null) && (ClassUtils.matchesTypeName(requiredType, valueHolder.getType())))) {
/* 161:289 */             if ((requiredType == null) || (valueHolder.getType() != null) || (valueHolder.getName() != null) || 
/* 162:290 */               (ClassUtils.isAssignableValue(requiredType, valueHolder.getValue()))) {
/* 163:293 */               return valueHolder;
/* 164:    */             }
/* 165:    */           }
/* 166:    */         }
/* 167:    */       }
/* 168:    */     }
/* 169:295 */     return null;
/* 170:    */   }
/* 171:    */   
/* 172:    */   public List<ValueHolder> getGenericArgumentValues()
/* 173:    */   {
/* 174:304 */     return Collections.unmodifiableList(this.genericArgumentValues);
/* 175:    */   }
/* 176:    */   
/* 177:    */   public ValueHolder getArgumentValue(int index, Class requiredType)
/* 178:    */   {
/* 179:316 */     return getArgumentValue(index, requiredType, null, null);
/* 180:    */   }
/* 181:    */   
/* 182:    */   public ValueHolder getArgumentValue(int index, Class requiredType, String requiredName)
/* 183:    */   {
/* 184:328 */     return getArgumentValue(index, requiredType, requiredName, null);
/* 185:    */   }
/* 186:    */   
/* 187:    */   public ValueHolder getArgumentValue(int index, Class requiredType, String requiredName, Set<ValueHolder> usedValueHolders)
/* 188:    */   {
/* 189:344 */     Assert.isTrue(index >= 0, "Index must not be negative");
/* 190:345 */     ValueHolder valueHolder = getIndexedArgumentValue(index, requiredType, requiredName);
/* 191:346 */     if (valueHolder == null) {
/* 192:347 */       valueHolder = getGenericArgumentValue(requiredType, requiredName, usedValueHolders);
/* 193:    */     }
/* 194:349 */     return valueHolder;
/* 195:    */   }
/* 196:    */   
/* 197:    */   public int getArgumentCount()
/* 198:    */   {
/* 199:357 */     return this.indexedArgumentValues.size() + this.genericArgumentValues.size();
/* 200:    */   }
/* 201:    */   
/* 202:    */   public boolean isEmpty()
/* 203:    */   {
/* 204:365 */     return (this.indexedArgumentValues.isEmpty()) && (this.genericArgumentValues.isEmpty());
/* 205:    */   }
/* 206:    */   
/* 207:    */   public void clear()
/* 208:    */   {
/* 209:372 */     this.indexedArgumentValues.clear();
/* 210:373 */     this.genericArgumentValues.clear();
/* 211:    */   }
/* 212:    */   
/* 213:    */   public boolean equals(Object other)
/* 214:    */   {
/* 215:379 */     if (this == other) {
/* 216:380 */       return true;
/* 217:    */     }
/* 218:382 */     if (!(other instanceof ConstructorArgumentValues)) {
/* 219:383 */       return false;
/* 220:    */     }
/* 221:385 */     ConstructorArgumentValues that = (ConstructorArgumentValues)other;
/* 222:386 */     if ((this.genericArgumentValues.size() != that.genericArgumentValues.size()) || 
/* 223:387 */       (this.indexedArgumentValues.size() != that.indexedArgumentValues.size())) {
/* 224:388 */       return false;
/* 225:    */     }
/* 226:390 */     Iterator<ValueHolder> it1 = this.genericArgumentValues.iterator();
/* 227:391 */     Iterator<ValueHolder> it2 = that.genericArgumentValues.iterator();
/* 228:    */     ValueHolder vh2;
/* 229:392 */     while ((it1.hasNext()) && (it2.hasNext()))
/* 230:    */     {
/* 231:393 */       ValueHolder vh1 = (ValueHolder)it1.next();
/* 232:394 */       vh2 = (ValueHolder)it2.next();
/* 233:395 */       if (!vh1.contentEquals(vh2)) {
/* 234:396 */         return false;
/* 235:    */       }
/* 236:    */     }
/* 237:399 */     for (Map.Entry<Integer, ValueHolder> entry : this.indexedArgumentValues.entrySet())
/* 238:    */     {
/* 239:400 */       ValueHolder vh1 = (ValueHolder)entry.getValue();
/* 240:401 */       ValueHolder vh2 = (ValueHolder)that.indexedArgumentValues.get(entry.getKey());
/* 241:402 */       if (!vh1.contentEquals(vh2)) {
/* 242:403 */         return false;
/* 243:    */       }
/* 244:    */     }
/* 245:406 */     return true;
/* 246:    */   }
/* 247:    */   
/* 248:    */   public int hashCode()
/* 249:    */   {
/* 250:411 */     int hashCode = 7;
/* 251:412 */     for (ValueHolder valueHolder : this.genericArgumentValues) {
/* 252:413 */       hashCode = 31 * hashCode + valueHolder.contentHashCode();
/* 253:    */     }
/* 254:415 */     hashCode *= 29;
/* 255:416 */     for (Map.Entry<Integer, ValueHolder> entry : this.indexedArgumentValues.entrySet()) {
/* 256:417 */       hashCode = 31 * hashCode + (((ValueHolder)entry.getValue()).contentHashCode() ^ ((Integer)entry.getKey()).hashCode());
/* 257:    */     }
/* 258:419 */     return hashCode;
/* 259:    */   }
/* 260:    */   
/* 261:    */   public static class ValueHolder
/* 262:    */     implements BeanMetadataElement
/* 263:    */   {
/* 264:    */     private Object value;
/* 265:    */     private String type;
/* 266:    */     private String name;
/* 267:    */     private Object source;
/* 268:437 */     private boolean converted = false;
/* 269:    */     private Object convertedValue;
/* 270:    */     
/* 271:    */     public ValueHolder(Object value)
/* 272:    */     {
/* 273:446 */       this.value = value;
/* 274:    */     }
/* 275:    */     
/* 276:    */     public ValueHolder(Object value, String type)
/* 277:    */     {
/* 278:455 */       this.value = value;
/* 279:456 */       this.type = type;
/* 280:    */     }
/* 281:    */     
/* 282:    */     public ValueHolder(Object value, String type, String name)
/* 283:    */     {
/* 284:466 */       this.value = value;
/* 285:467 */       this.type = type;
/* 286:468 */       this.name = name;
/* 287:    */     }
/* 288:    */     
/* 289:    */     public void setValue(Object value)
/* 290:    */     {
/* 291:476 */       this.value = value;
/* 292:    */     }
/* 293:    */     
/* 294:    */     public Object getValue()
/* 295:    */     {
/* 296:483 */       return this.value;
/* 297:    */     }
/* 298:    */     
/* 299:    */     public void setType(String type)
/* 300:    */     {
/* 301:490 */       this.type = type;
/* 302:    */     }
/* 303:    */     
/* 304:    */     public String getType()
/* 305:    */     {
/* 306:497 */       return this.type;
/* 307:    */     }
/* 308:    */     
/* 309:    */     public void setName(String name)
/* 310:    */     {
/* 311:504 */       this.name = name;
/* 312:    */     }
/* 313:    */     
/* 314:    */     public String getName()
/* 315:    */     {
/* 316:511 */       return this.name;
/* 317:    */     }
/* 318:    */     
/* 319:    */     public void setSource(Object source)
/* 320:    */     {
/* 321:519 */       this.source = source;
/* 322:    */     }
/* 323:    */     
/* 324:    */     public Object getSource()
/* 325:    */     {
/* 326:523 */       return this.source;
/* 327:    */     }
/* 328:    */     
/* 329:    */     public synchronized boolean isConverted()
/* 330:    */     {
/* 331:531 */       return this.converted;
/* 332:    */     }
/* 333:    */     
/* 334:    */     public synchronized void setConvertedValue(Object value)
/* 335:    */     {
/* 336:539 */       this.converted = true;
/* 337:540 */       this.convertedValue = value;
/* 338:    */     }
/* 339:    */     
/* 340:    */     public synchronized Object getConvertedValue()
/* 341:    */     {
/* 342:548 */       return this.convertedValue;
/* 343:    */     }
/* 344:    */     
/* 345:    */     private boolean contentEquals(ValueHolder other)
/* 346:    */     {
/* 347:560 */       return (this == other) || ((ObjectUtils.nullSafeEquals(this.value, other.value)) && (ObjectUtils.nullSafeEquals(this.type, other.type)));
/* 348:    */     }
/* 349:    */     
/* 350:    */     private int contentHashCode()
/* 351:    */     {
/* 352:570 */       return ObjectUtils.nullSafeHashCode(this.value) * 29 + ObjectUtils.nullSafeHashCode(this.type);
/* 353:    */     }
/* 354:    */     
/* 355:    */     public ValueHolder copy()
/* 356:    */     {
/* 357:578 */       ValueHolder copy = new ValueHolder(this.value, this.type, this.name);
/* 358:579 */       copy.setSource(this.source);
/* 359:580 */       return copy;
/* 360:    */     }
/* 361:    */   }
/* 362:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.ConstructorArgumentValues
 * JD-Core Version:    0.7.0.1
 */