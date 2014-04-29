/*   1:    */ package org.springframework.beans.factory.config;
/*   2:    */ 
/*   3:    */ import java.util.LinkedHashMap;
/*   4:    */ import java.util.LinkedHashSet;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.Map.Entry;
/*   8:    */ import java.util.Set;
/*   9:    */ import org.springframework.beans.MutablePropertyValues;
/*  10:    */ import org.springframework.beans.PropertyValue;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ import org.springframework.util.ObjectUtils;
/*  13:    */ import org.springframework.util.StringValueResolver;
/*  14:    */ 
/*  15:    */ public class BeanDefinitionVisitor
/*  16:    */ {
/*  17:    */   private StringValueResolver valueResolver;
/*  18:    */   
/*  19:    */   public BeanDefinitionVisitor(StringValueResolver valueResolver)
/*  20:    */   {
/*  21: 58 */     Assert.notNull(valueResolver, "StringValueResolver must not be null");
/*  22: 59 */     this.valueResolver = valueResolver;
/*  23:    */   }
/*  24:    */   
/*  25:    */   protected BeanDefinitionVisitor() {}
/*  26:    */   
/*  27:    */   public void visitBeanDefinition(BeanDefinition beanDefinition)
/*  28:    */   {
/*  29: 77 */     visitParentName(beanDefinition);
/*  30: 78 */     visitBeanClassName(beanDefinition);
/*  31: 79 */     visitFactoryBeanName(beanDefinition);
/*  32: 80 */     visitFactoryMethodName(beanDefinition);
/*  33: 81 */     visitScope(beanDefinition);
/*  34: 82 */     visitPropertyValues(beanDefinition.getPropertyValues());
/*  35: 83 */     ConstructorArgumentValues cas = beanDefinition.getConstructorArgumentValues();
/*  36: 84 */     visitIndexedArgumentValues(cas.getIndexedArgumentValues());
/*  37: 85 */     visitGenericArgumentValues(cas.getGenericArgumentValues());
/*  38:    */   }
/*  39:    */   
/*  40:    */   protected void visitParentName(BeanDefinition beanDefinition)
/*  41:    */   {
/*  42: 89 */     String parentName = beanDefinition.getParentName();
/*  43: 90 */     if (parentName != null)
/*  44:    */     {
/*  45: 91 */       String resolvedName = resolveStringValue(parentName);
/*  46: 92 */       if (!parentName.equals(resolvedName)) {
/*  47: 93 */         beanDefinition.setParentName(resolvedName);
/*  48:    */       }
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   protected void visitBeanClassName(BeanDefinition beanDefinition)
/*  53:    */   {
/*  54: 99 */     String beanClassName = beanDefinition.getBeanClassName();
/*  55:100 */     if (beanClassName != null)
/*  56:    */     {
/*  57:101 */       String resolvedName = resolveStringValue(beanClassName);
/*  58:102 */       if (!beanClassName.equals(resolvedName)) {
/*  59:103 */         beanDefinition.setBeanClassName(resolvedName);
/*  60:    */       }
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   protected void visitFactoryBeanName(BeanDefinition beanDefinition)
/*  65:    */   {
/*  66:109 */     String factoryBeanName = beanDefinition.getFactoryBeanName();
/*  67:110 */     if (factoryBeanName != null)
/*  68:    */     {
/*  69:111 */       String resolvedName = resolveStringValue(factoryBeanName);
/*  70:112 */       if (!factoryBeanName.equals(resolvedName)) {
/*  71:113 */         beanDefinition.setFactoryBeanName(resolvedName);
/*  72:    */       }
/*  73:    */     }
/*  74:    */   }
/*  75:    */   
/*  76:    */   protected void visitFactoryMethodName(BeanDefinition beanDefinition)
/*  77:    */   {
/*  78:119 */     String factoryMethodName = beanDefinition.getFactoryMethodName();
/*  79:120 */     if (factoryMethodName != null)
/*  80:    */     {
/*  81:121 */       String resolvedName = resolveStringValue(factoryMethodName);
/*  82:122 */       if (!factoryMethodName.equals(resolvedName)) {
/*  83:123 */         beanDefinition.setFactoryMethodName(resolvedName);
/*  84:    */       }
/*  85:    */     }
/*  86:    */   }
/*  87:    */   
/*  88:    */   protected void visitScope(BeanDefinition beanDefinition)
/*  89:    */   {
/*  90:129 */     String scope = beanDefinition.getScope();
/*  91:130 */     if (scope != null)
/*  92:    */     {
/*  93:131 */       String resolvedScope = resolveStringValue(scope);
/*  94:132 */       if (!scope.equals(resolvedScope)) {
/*  95:133 */         beanDefinition.setScope(resolvedScope);
/*  96:    */       }
/*  97:    */     }
/*  98:    */   }
/*  99:    */   
/* 100:    */   protected void visitPropertyValues(MutablePropertyValues pvs)
/* 101:    */   {
/* 102:139 */     PropertyValue[] pvArray = pvs.getPropertyValues();
/* 103:140 */     for (PropertyValue pv : pvArray)
/* 104:    */     {
/* 105:141 */       Object newVal = resolveValue(pv.getValue());
/* 106:142 */       if (!ObjectUtils.nullSafeEquals(newVal, pv.getValue())) {
/* 107:143 */         pvs.add(pv.getName(), newVal);
/* 108:    */       }
/* 109:    */     }
/* 110:    */   }
/* 111:    */   
/* 112:    */   protected void visitIndexedArgumentValues(Map<Integer, ConstructorArgumentValues.ValueHolder> ias)
/* 113:    */   {
/* 114:149 */     for (ConstructorArgumentValues.ValueHolder valueHolder : ias.values())
/* 115:    */     {
/* 116:150 */       Object newVal = resolveValue(valueHolder.getValue());
/* 117:151 */       if (!ObjectUtils.nullSafeEquals(newVal, valueHolder.getValue())) {
/* 118:152 */         valueHolder.setValue(newVal);
/* 119:    */       }
/* 120:    */     }
/* 121:    */   }
/* 122:    */   
/* 123:    */   protected void visitGenericArgumentValues(List<ConstructorArgumentValues.ValueHolder> gas)
/* 124:    */   {
/* 125:158 */     for (ConstructorArgumentValues.ValueHolder valueHolder : gas)
/* 126:    */     {
/* 127:159 */       Object newVal = resolveValue(valueHolder.getValue());
/* 128:160 */       if (!ObjectUtils.nullSafeEquals(newVal, valueHolder.getValue())) {
/* 129:161 */         valueHolder.setValue(newVal);
/* 130:    */       }
/* 131:    */     }
/* 132:    */   }
/* 133:    */   
/* 134:    */   protected Object resolveValue(Object value)
/* 135:    */   {
/* 136:168 */     if ((value instanceof BeanDefinition))
/* 137:    */     {
/* 138:169 */       visitBeanDefinition((BeanDefinition)value);
/* 139:    */     }
/* 140:171 */     else if ((value instanceof BeanDefinitionHolder))
/* 141:    */     {
/* 142:172 */       visitBeanDefinition(((BeanDefinitionHolder)value).getBeanDefinition());
/* 143:    */     }
/* 144:174 */     else if ((value instanceof RuntimeBeanReference))
/* 145:    */     {
/* 146:175 */       RuntimeBeanReference ref = (RuntimeBeanReference)value;
/* 147:176 */       String newBeanName = resolveStringValue(ref.getBeanName());
/* 148:177 */       if (!newBeanName.equals(ref.getBeanName())) {
/* 149:178 */         return new RuntimeBeanReference(newBeanName);
/* 150:    */       }
/* 151:    */     }
/* 152:181 */     else if ((value instanceof RuntimeBeanNameReference))
/* 153:    */     {
/* 154:182 */       RuntimeBeanNameReference ref = (RuntimeBeanNameReference)value;
/* 155:183 */       String newBeanName = resolveStringValue(ref.getBeanName());
/* 156:184 */       if (!newBeanName.equals(ref.getBeanName())) {
/* 157:185 */         return new RuntimeBeanNameReference(newBeanName);
/* 158:    */       }
/* 159:    */     }
/* 160:188 */     else if ((value instanceof Object[]))
/* 161:    */     {
/* 162:189 */       visitArray((Object[])value);
/* 163:    */     }
/* 164:191 */     else if ((value instanceof List))
/* 165:    */     {
/* 166:192 */       visitList((List)value);
/* 167:    */     }
/* 168:194 */     else if ((value instanceof Set))
/* 169:    */     {
/* 170:195 */       visitSet((Set)value);
/* 171:    */     }
/* 172:197 */     else if ((value instanceof Map))
/* 173:    */     {
/* 174:198 */       visitMap((Map)value);
/* 175:    */     }
/* 176:200 */     else if ((value instanceof TypedStringValue))
/* 177:    */     {
/* 178:201 */       TypedStringValue typedStringValue = (TypedStringValue)value;
/* 179:202 */       String stringValue = typedStringValue.getValue();
/* 180:203 */       if (stringValue != null)
/* 181:    */       {
/* 182:204 */         String visitedString = resolveStringValue(stringValue);
/* 183:205 */         typedStringValue.setValue(visitedString);
/* 184:    */       }
/* 185:    */     }
/* 186:208 */     else if ((value instanceof String))
/* 187:    */     {
/* 188:209 */       return resolveStringValue((String)value);
/* 189:    */     }
/* 190:211 */     return value;
/* 191:    */   }
/* 192:    */   
/* 193:    */   protected void visitArray(Object[] arrayVal)
/* 194:    */   {
/* 195:215 */     for (int i = 0; i < arrayVal.length; i++)
/* 196:    */     {
/* 197:216 */       Object elem = arrayVal[i];
/* 198:217 */       Object newVal = resolveValue(elem);
/* 199:218 */       if (!ObjectUtils.nullSafeEquals(newVal, elem)) {
/* 200:219 */         arrayVal[i] = newVal;
/* 201:    */       }
/* 202:    */     }
/* 203:    */   }
/* 204:    */   
/* 205:    */   protected void visitList(List listVal)
/* 206:    */   {
/* 207:226 */     for (int i = 0; i < listVal.size(); i++)
/* 208:    */     {
/* 209:227 */       Object elem = listVal.get(i);
/* 210:228 */       Object newVal = resolveValue(elem);
/* 211:229 */       if (!ObjectUtils.nullSafeEquals(newVal, elem)) {
/* 212:230 */         listVal.set(i, newVal);
/* 213:    */       }
/* 214:    */     }
/* 215:    */   }
/* 216:    */   
/* 217:    */   protected void visitSet(Set setVal)
/* 218:    */   {
/* 219:237 */     Set newContent = new LinkedHashSet();
/* 220:238 */     boolean entriesModified = false;
/* 221:239 */     for (Object elem : setVal)
/* 222:    */     {
/* 223:240 */       int elemHash = elem != null ? elem.hashCode() : 0;
/* 224:241 */       Object newVal = resolveValue(elem);
/* 225:242 */       int newValHash = newVal != null ? newVal.hashCode() : 0;
/* 226:243 */       newContent.add(newVal);
/* 227:244 */       entriesModified = (entriesModified) || (newVal != elem) || (newValHash != elemHash);
/* 228:    */     }
/* 229:246 */     if (entriesModified)
/* 230:    */     {
/* 231:247 */       setVal.clear();
/* 232:248 */       setVal.addAll(newContent);
/* 233:    */     }
/* 234:    */   }
/* 235:    */   
/* 236:    */   protected void visitMap(Map<?, ?> mapVal)
/* 237:    */   {
/* 238:254 */     Map newContent = new LinkedHashMap();
/* 239:255 */     boolean entriesModified = false;
/* 240:256 */     for (Map.Entry entry : mapVal.entrySet())
/* 241:    */     {
/* 242:257 */       Object key = entry.getKey();
/* 243:258 */       int keyHash = key != null ? key.hashCode() : 0;
/* 244:259 */       Object newKey = resolveValue(key);
/* 245:260 */       int newKeyHash = newKey != null ? newKey.hashCode() : 0;
/* 246:261 */       Object val = entry.getValue();
/* 247:262 */       Object newVal = resolveValue(val);
/* 248:263 */       newContent.put(newKey, newVal);
/* 249:264 */       entriesModified = (entriesModified) || (newVal != val) || (newKey != key) || (newKeyHash != keyHash);
/* 250:    */     }
/* 251:266 */     if (entriesModified)
/* 252:    */     {
/* 253:267 */       mapVal.clear();
/* 254:268 */       mapVal.putAll(newContent);
/* 255:    */     }
/* 256:    */   }
/* 257:    */   
/* 258:    */   protected String resolveStringValue(String strVal)
/* 259:    */   {
/* 260:278 */     if (this.valueResolver == null) {
/* 261:279 */       throw new IllegalStateException("No StringValueResolver specified - pass a resolver object into the constructor or override the 'resolveStringValue' method");
/* 262:    */     }
/* 263:282 */     String resolvedValue = this.valueResolver.resolveStringValue(strVal);
/* 264:    */     
/* 265:284 */     return strVal.equals(resolvedValue) ? strVal : resolvedValue;
/* 266:    */   }
/* 267:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.BeanDefinitionVisitor
 * JD-Core Version:    0.7.0.1
 */