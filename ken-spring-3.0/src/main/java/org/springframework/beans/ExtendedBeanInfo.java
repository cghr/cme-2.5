/*   1:    */ package org.springframework.beans;
/*   2:    */ 
/*   3:    */ import java.awt.Image;
/*   4:    */ import java.beans.BeanDescriptor;
/*   5:    */ import java.beans.BeanInfo;
/*   6:    */ import java.beans.EventSetDescriptor;
/*   7:    */ import java.beans.IndexedPropertyDescriptor;
/*   8:    */ import java.beans.IntrospectionException;
/*   9:    */ import java.beans.Introspector;
/*  10:    */ import java.beans.MethodDescriptor;
/*  11:    */ import java.beans.PropertyDescriptor;
/*  12:    */ import java.lang.reflect.Method;
/*  13:    */ import java.util.Comparator;
/*  14:    */ import java.util.SortedSet;
/*  15:    */ import java.util.TreeSet;
/*  16:    */ import org.springframework.util.ReflectionUtils;
/*  17:    */ import org.springframework.util.StringUtils;
/*  18:    */ 
/*  19:    */ class ExtendedBeanInfo
/*  20:    */   implements BeanInfo
/*  21:    */ {
/*  22:    */   private final BeanInfo delegate;
/*  23: 53 */   private final SortedSet<PropertyDescriptor> propertyDescriptors = new TreeSet(new PropertyDescriptorComparator());
/*  24:    */   
/*  25:    */   public ExtendedBeanInfo(BeanInfo delegate)
/*  26:    */     throws IntrospectionException
/*  27:    */   {
/*  28: 65 */     this.delegate = delegate;
/*  29: 74 */     for (MethodDescriptor md : delegate.getMethodDescriptors())
/*  30:    */     {
/*  31: 75 */       Method method = md.getMethod();
/*  32: 78 */       if ((!ReflectionUtils.isObjectMethod(method)) || (method.getName().startsWith("get")))
/*  33:    */       {
/*  34:    */         PropertyDescriptor[] arrayOfPropertyDescriptor2;
/*  35:    */         int k;
/*  36:    */         PropertyDescriptor localPropertyDescriptor1;
/*  37: 83 */         if ((method.getName().startsWith("set")) && (method.getParameterTypes().length == 1))
/*  38:    */         {
/*  39: 84 */           String propertyName = propertyNameFor(method);
/*  40: 85 */           if (propertyName.length() != 0)
/*  41:    */           {
/*  42: 88 */             k = (arrayOfPropertyDescriptor2 = delegate.getPropertyDescriptors()).length;
/*  43: 88 */             for (localPropertyDescriptor1 = 0; localPropertyDescriptor1 < k; localPropertyDescriptor1++)
/*  44:    */             {
/*  45: 88 */               PropertyDescriptor pd = arrayOfPropertyDescriptor2[localPropertyDescriptor1];
/*  46: 89 */               Method readMethod = pd.getReadMethod();
/*  47: 90 */               Method writeMethod = pd.getWriteMethod();
/*  48: 92 */               if ((writeMethod != null) && 
/*  49: 93 */                 (writeMethod.getName().equals(method.getName())))
/*  50:    */               {
/*  51: 95 */                 addOrUpdatePropertyDescriptor(propertyName, readMethod, writeMethod);
/*  52: 96 */                 break;
/*  53:    */               }
/*  54: 99 */               if ((readMethod != null) && 
/*  55:100 */                 (readMethod.getName().equals(getterMethodNameFor(propertyName))) && 
/*  56:101 */                 (readMethod.getReturnType().equals(method.getParameterTypes()[0])))
/*  57:    */               {
/*  58:102 */                 addOrUpdatePropertyDescriptor(propertyName, readMethod, method);
/*  59:103 */                 break;
/*  60:    */               }
/*  61:    */             }
/*  62:108 */             addOrUpdatePropertyDescriptor(propertyName, null, method);
/*  63:    */           }
/*  64:    */         }
/*  65:    */         else
/*  66:    */         {
/*  67:    */           PropertyDescriptor pd;
/*  68:    */           IndexedPropertyDescriptor ipd;
/*  69:113 */           if ((method.getName().startsWith("set")) && (method.getParameterTypes().length == 2) && (method.getParameterTypes()[0].equals(Integer.TYPE)))
/*  70:    */           {
/*  71:114 */             String propertyName = propertyNameFor(method);
/*  72:115 */             if (propertyName.length() != 0)
/*  73:    */             {
/*  74:119 */               k = (arrayOfPropertyDescriptor2 = delegate.getPropertyDescriptors()).length;
/*  75:119 */               for (localPropertyDescriptor1 = 0; localPropertyDescriptor1 < k; localPropertyDescriptor1++)
/*  76:    */               {
/*  77:119 */                 pd = arrayOfPropertyDescriptor2[localPropertyDescriptor1];
/*  78:120 */                 if ((pd instanceof IndexedPropertyDescriptor))
/*  79:    */                 {
/*  80:123 */                   ipd = (IndexedPropertyDescriptor)pd;
/*  81:124 */                   Method readMethod = ipd.getReadMethod();
/*  82:125 */                   Method writeMethod = ipd.getWriteMethod();
/*  83:126 */                   Method indexedReadMethod = ipd.getIndexedReadMethod();
/*  84:127 */                   Method indexedWriteMethod = ipd.getIndexedWriteMethod();
/*  85:129 */                   if ((indexedWriteMethod != null) && 
/*  86:130 */                     (indexedWriteMethod.getName().equals(method.getName())))
/*  87:    */                   {
/*  88:132 */                     addOrUpdatePropertyDescriptor(propertyName, readMethod, writeMethod, indexedReadMethod, indexedWriteMethod);
/*  89:133 */                     break;
/*  90:    */                   }
/*  91:136 */                   if ((indexedReadMethod != null) && 
/*  92:137 */                     (indexedReadMethod.getName().equals(getterMethodNameFor(propertyName))) && 
/*  93:138 */                     (indexedReadMethod.getReturnType().equals(method.getParameterTypes()[1])))
/*  94:    */                   {
/*  95:139 */                     addOrUpdatePropertyDescriptor(propertyName, readMethod, writeMethod, indexedReadMethod, method);
/*  96:140 */                     break;
/*  97:    */                   }
/*  98:    */                 }
/*  99:    */               }
/* 100:145 */               addOrUpdatePropertyDescriptor(propertyName, null, null, null, method);
/* 101:    */             }
/* 102:    */           }
/* 103:    */           else
/* 104:    */           {
/* 105:    */             PropertyDescriptor[] arrayOfPropertyDescriptor1;
/* 106:150 */             localPropertyDescriptor1 = (arrayOfPropertyDescriptor1 = delegate.getPropertyDescriptors()).length;
/* 107:150 */             for (pd = 0; pd < localPropertyDescriptor1; pd++)
/* 108:    */             {
/* 109:150 */               PropertyDescriptor pd = arrayOfPropertyDescriptor1[pd];
/* 110:152 */               for (PropertyDescriptor existingPD : this.propertyDescriptors) {
/* 111:153 */                 if ((method.equals(pd.getReadMethod())) && 
/* 112:154 */                   (existingPD.getName().equals(pd.getName())))
/* 113:    */                 {
/* 114:155 */                   if (existingPD.getReadMethod() != null) {
/* 115:    */                     break;
/* 116:    */                   }
/* 117:157 */                   addOrUpdatePropertyDescriptor(pd.getName(), method, pd.getWriteMethod());
/* 118:    */                   
/* 119:    */ 
/* 120:160 */                   break;
/* 121:    */                 }
/* 122:    */               }
/* 123:163 */               if ((method == pd.getReadMethod()) || (
/* 124:164 */                 ((pd instanceof IndexedPropertyDescriptor)) && (method == ((IndexedPropertyDescriptor)pd).getIndexedReadMethod())))
/* 125:    */               {
/* 126:166 */                 if ((pd instanceof IndexedPropertyDescriptor))
/* 127:    */                 {
/* 128:167 */                   addOrUpdatePropertyDescriptor(pd.getName(), pd.getReadMethod(), pd.getWriteMethod(), ((IndexedPropertyDescriptor)pd).getIndexedReadMethod(), ((IndexedPropertyDescriptor)pd).getIndexedWriteMethod()); break;
/* 129:    */                 }
/* 130:169 */                 addOrUpdatePropertyDescriptor(pd.getName(), pd.getReadMethod(), pd.getWriteMethod());
/* 131:    */                 
/* 132:171 */                 break;
/* 133:    */               }
/* 134:    */             }
/* 135:    */           }
/* 136:    */         }
/* 137:    */       }
/* 138:    */     }
/* 139:    */   }
/* 140:    */   
/* 141:    */   private void addOrUpdatePropertyDescriptor(String propertyName, Method readMethod, Method writeMethod)
/* 142:    */     throws IntrospectionException
/* 143:    */   {
/* 144:178 */     addOrUpdatePropertyDescriptor(propertyName, readMethod, writeMethod, null, null);
/* 145:    */   }
/* 146:    */   
/* 147:    */   private void addOrUpdatePropertyDescriptor(String propertyName, Method readMethod, Method writeMethod, Method indexedReadMethod, Method indexedWriteMethod)
/* 148:    */     throws IntrospectionException
/* 149:    */   {
/* 150:182 */     for (PropertyDescriptor existingPD : this.propertyDescriptors) {
/* 151:183 */       if (existingPD.getName().equals(propertyName))
/* 152:    */       {
/* 153:185 */         if ((existingPD.getReadMethod() != null) && (
/* 154:186 */           ((readMethod != null) && (existingPD.getReadMethod().getReturnType() != readMethod.getReturnType())) || (
/* 155:187 */           (writeMethod != null) && (existingPD.getReadMethod().getReturnType() != writeMethod.getParameterTypes()[0])))) {
/* 156:    */           break;
/* 157:    */         }
/* 158:193 */         if (readMethod != null) {
/* 159:    */           try
/* 160:    */           {
/* 161:195 */             existingPD.setReadMethod(readMethod);
/* 162:    */           }
/* 163:    */           catch (IntrospectionException localIntrospectionException1)
/* 164:    */           {
/* 165:198 */             existingPD.setWriteMethod(null);
/* 166:199 */             existingPD.setReadMethod(readMethod);
/* 167:    */           }
/* 168:    */         }
/* 169:204 */         if ((existingPD.getWriteMethod() != null) && (
/* 170:205 */           ((readMethod != null) && (existingPD.getWriteMethod().getParameterTypes()[0] != readMethod.getReturnType())) || (
/* 171:206 */           (writeMethod != null) && (existingPD.getWriteMethod().getParameterTypes()[0] != writeMethod.getParameterTypes()[0])))) {
/* 172:    */           break;
/* 173:    */         }
/* 174:212 */         if (writeMethod != null) {
/* 175:213 */           existingPD.setWriteMethod(writeMethod);
/* 176:    */         }
/* 177:217 */         if ((existingPD instanceof IndexedPropertyDescriptor))
/* 178:    */         {
/* 179:218 */           IndexedPropertyDescriptor existingIPD = (IndexedPropertyDescriptor)existingPD;
/* 180:221 */           if ((existingIPD.getIndexedReadMethod() != null) && (
/* 181:222 */             ((indexedReadMethod != null) && (existingIPD.getIndexedReadMethod().getReturnType() != indexedReadMethod.getReturnType())) || (
/* 182:223 */             (indexedWriteMethod != null) && (existingIPD.getIndexedReadMethod().getReturnType() != indexedWriteMethod.getParameterTypes()[1])))) {
/* 183:    */             break;
/* 184:    */           }
/* 185:    */           try
/* 186:    */           {
/* 187:230 */             if (indexedReadMethod != null) {
/* 188:231 */               existingIPD.setIndexedReadMethod(indexedReadMethod);
/* 189:    */             }
/* 190:    */           }
/* 191:    */           catch (IntrospectionException localIntrospectionException2)
/* 192:    */           {
/* 193:235 */             existingIPD.setIndexedWriteMethod(null);
/* 194:236 */             existingIPD.setIndexedReadMethod(indexedReadMethod);
/* 195:    */           }
/* 196:240 */           if ((existingIPD.getIndexedWriteMethod() != null) && (
/* 197:241 */             ((indexedReadMethod != null) && (existingIPD.getIndexedWriteMethod().getParameterTypes()[1] != indexedReadMethod.getReturnType())) || (
/* 198:242 */             (indexedWriteMethod != null) && (existingIPD.getIndexedWriteMethod().getParameterTypes()[1] != indexedWriteMethod.getParameterTypes()[1])))) {
/* 199:    */             break;
/* 200:    */           }
/* 201:248 */           if (indexedWriteMethod != null) {
/* 202:249 */             existingIPD.setIndexedWriteMethod(indexedWriteMethod);
/* 203:    */           }
/* 204:    */         }
/* 205:254 */         return;
/* 206:    */       }
/* 207:    */     }
/* 208:259 */     if ((indexedReadMethod == null) && (indexedWriteMethod == null)) {
/* 209:260 */       this.propertyDescriptors.add(new PropertyDescriptor(propertyName, readMethod, writeMethod));
/* 210:    */     } else {
/* 211:262 */       this.propertyDescriptors.add(new IndexedPropertyDescriptor(propertyName, readMethod, writeMethod, indexedReadMethod, indexedWriteMethod));
/* 212:    */     }
/* 213:    */   }
/* 214:    */   
/* 215:    */   private String propertyNameFor(Method method)
/* 216:    */   {
/* 217:267 */     return Introspector.decapitalize(method.getName().substring(3, method.getName().length()));
/* 218:    */   }
/* 219:    */   
/* 220:    */   private Object getterMethodNameFor(String name)
/* 221:    */   {
/* 222:271 */     return "get" + StringUtils.capitalize(name);
/* 223:    */   }
/* 224:    */   
/* 225:    */   public BeanInfo[] getAdditionalBeanInfo()
/* 226:    */   {
/* 227:275 */     return this.delegate.getAdditionalBeanInfo();
/* 228:    */   }
/* 229:    */   
/* 230:    */   public BeanDescriptor getBeanDescriptor()
/* 231:    */   {
/* 232:279 */     return this.delegate.getBeanDescriptor();
/* 233:    */   }
/* 234:    */   
/* 235:    */   public int getDefaultEventIndex()
/* 236:    */   {
/* 237:283 */     return this.delegate.getDefaultEventIndex();
/* 238:    */   }
/* 239:    */   
/* 240:    */   public int getDefaultPropertyIndex()
/* 241:    */   {
/* 242:287 */     return this.delegate.getDefaultPropertyIndex();
/* 243:    */   }
/* 244:    */   
/* 245:    */   public EventSetDescriptor[] getEventSetDescriptors()
/* 246:    */   {
/* 247:291 */     return this.delegate.getEventSetDescriptors();
/* 248:    */   }
/* 249:    */   
/* 250:    */   public Image getIcon(int arg0)
/* 251:    */   {
/* 252:295 */     return this.delegate.getIcon(arg0);
/* 253:    */   }
/* 254:    */   
/* 255:    */   public MethodDescriptor[] getMethodDescriptors()
/* 256:    */   {
/* 257:299 */     return this.delegate.getMethodDescriptors();
/* 258:    */   }
/* 259:    */   
/* 260:    */   public PropertyDescriptor[] getPropertyDescriptors()
/* 261:    */   {
/* 262:309 */     return (PropertyDescriptor[])this.propertyDescriptors.toArray(new PropertyDescriptor[this.propertyDescriptors.size()]);
/* 263:    */   }
/* 264:    */   
/* 265:    */   static class PropertyDescriptorComparator
/* 266:    */     implements Comparator<PropertyDescriptor>
/* 267:    */   {
/* 268:    */     public int compare(PropertyDescriptor desc1, PropertyDescriptor desc2)
/* 269:    */     {
/* 270:320 */       String left = desc1.getName();
/* 271:321 */       String right = desc2.getName();
/* 272:322 */       for (int i = 0; i < left.length(); i++)
/* 273:    */       {
/* 274:323 */         if (right.length() == i) {
/* 275:324 */           return 1;
/* 276:    */         }
/* 277:326 */         int result = left.getBytes()[i] - right.getBytes()[i];
/* 278:327 */         if (result != 0) {
/* 279:328 */           return result;
/* 280:    */         }
/* 281:    */       }
/* 282:331 */       return left.length() - right.length();
/* 283:    */     }
/* 284:    */   }
/* 285:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.ExtendedBeanInfo
 * JD-Core Version:    0.7.0.1
 */