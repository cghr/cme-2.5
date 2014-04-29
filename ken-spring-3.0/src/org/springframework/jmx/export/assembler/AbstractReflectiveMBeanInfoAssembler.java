/*   1:    */ package org.springframework.jmx.export.assembler;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyDescriptor;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.List;
/*   7:    */ import javax.management.Descriptor;
/*   8:    */ import javax.management.JMException;
/*   9:    */ import javax.management.MBeanParameterInfo;
/*  10:    */ import javax.management.modelmbean.ModelMBeanAttributeInfo;
/*  11:    */ import javax.management.modelmbean.ModelMBeanOperationInfo;
/*  12:    */ import org.springframework.aop.support.AopUtils;
/*  13:    */ import org.springframework.beans.BeanUtils;
/*  14:    */ import org.springframework.jmx.support.JmxUtils;
/*  15:    */ 
/*  16:    */ public abstract class AbstractReflectiveMBeanInfoAssembler
/*  17:    */   extends AbstractMBeanInfoAssembler
/*  18:    */ {
/*  19:    */   protected static final String FIELD_GET_METHOD = "getMethod";
/*  20:    */   protected static final String FIELD_SET_METHOD = "setMethod";
/*  21:    */   protected static final String FIELD_ROLE = "role";
/*  22:    */   protected static final String ROLE_GETTER = "getter";
/*  23:    */   protected static final String ROLE_SETTER = "setter";
/*  24:    */   protected static final String ROLE_OPERATION = "operation";
/*  25:    */   protected static final String FIELD_VISIBILITY = "visibility";
/*  26:    */   protected static final int ATTRIBUTE_OPERATION_VISIBILITY = 4;
/*  27:    */   protected static final String FIELD_CLASS = "class";
/*  28:    */   protected static final String FIELD_LOG = "log";
/*  29:    */   protected static final String FIELD_LOG_FILE = "logFile";
/*  30:    */   protected static final String FIELD_CURRENCY_TIME_LIMIT = "currencyTimeLimit";
/*  31:    */   protected static final String FIELD_DEFAULT = "default";
/*  32:    */   protected static final String FIELD_PERSIST_POLICY = "persistPolicy";
/*  33:    */   protected static final String FIELD_PERSIST_PERIOD = "persistPeriod";
/*  34:    */   protected static final String FIELD_PERSIST_LOCATION = "persistLocation";
/*  35:    */   protected static final String FIELD_PERSIST_NAME = "persistName";
/*  36:    */   protected static final String FIELD_DISPLAY_NAME = "displayName";
/*  37:    */   protected static final String FIELD_UNITS = "units";
/*  38:    */   protected static final String FIELD_METRIC_TYPE = "metricType";
/*  39:    */   protected static final String FIELD_METRIC_CATEGORY = "metricCategory";
/*  40:    */   private Integer defaultCurrencyTimeLimit;
/*  41:175 */   private boolean useStrictCasing = true;
/*  42:177 */   private boolean exposeClassDescriptor = false;
/*  43:    */   
/*  44:    */   public void setDefaultCurrencyTimeLimit(Integer defaultCurrencyTimeLimit)
/*  45:    */   {
/*  46:201 */     this.defaultCurrencyTimeLimit = defaultCurrencyTimeLimit;
/*  47:    */   }
/*  48:    */   
/*  49:    */   protected Integer getDefaultCurrencyTimeLimit()
/*  50:    */   {
/*  51:208 */     return this.defaultCurrencyTimeLimit;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setUseStrictCasing(boolean useStrictCasing)
/*  55:    */   {
/*  56:219 */     this.useStrictCasing = useStrictCasing;
/*  57:    */   }
/*  58:    */   
/*  59:    */   protected boolean isUseStrictCasing()
/*  60:    */   {
/*  61:226 */     return this.useStrictCasing;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void setExposeClassDescriptor(boolean exposeClassDescriptor)
/*  65:    */   {
/*  66:246 */     this.exposeClassDescriptor = exposeClassDescriptor;
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected boolean isExposeClassDescriptor()
/*  70:    */   {
/*  71:253 */     return this.exposeClassDescriptor;
/*  72:    */   }
/*  73:    */   
/*  74:    */   protected ModelMBeanAttributeInfo[] getAttributeInfo(Object managedBean, String beanKey)
/*  75:    */     throws JMException
/*  76:    */   {
/*  77:271 */     PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(getClassToExpose(managedBean));
/*  78:272 */     List<ModelMBeanAttributeInfo> infos = new ArrayList();
/*  79:274 */     for (PropertyDescriptor prop : props)
/*  80:    */     {
/*  81:275 */       Method getter = prop.getReadMethod();
/*  82:276 */       if ((getter == null) || (getter.getDeclaringClass() != Object.class))
/*  83:    */       {
/*  84:279 */         if ((getter != null) && (!includeReadAttribute(getter, beanKey))) {
/*  85:280 */           getter = null;
/*  86:    */         }
/*  87:283 */         Method setter = prop.getWriteMethod();
/*  88:284 */         if ((setter != null) && (!includeWriteAttribute(setter, beanKey))) {
/*  89:285 */           setter = null;
/*  90:    */         }
/*  91:288 */         if ((getter != null) || (setter != null))
/*  92:    */         {
/*  93:290 */           String attrName = JmxUtils.getAttributeName(prop, isUseStrictCasing());
/*  94:291 */           String description = getAttributeDescription(prop, beanKey);
/*  95:292 */           ModelMBeanAttributeInfo info = new ModelMBeanAttributeInfo(attrName, description, getter, setter);
/*  96:    */           
/*  97:294 */           Descriptor desc = info.getDescriptor();
/*  98:295 */           if (getter != null) {
/*  99:296 */             desc.setField("getMethod", getter.getName());
/* 100:    */           }
/* 101:298 */           if (setter != null) {
/* 102:299 */             desc.setField("setMethod", setter.getName());
/* 103:    */           }
/* 104:302 */           populateAttributeDescriptor(desc, getter, setter, beanKey);
/* 105:303 */           info.setDescriptor(desc);
/* 106:304 */           infos.add(info);
/* 107:    */         }
/* 108:    */       }
/* 109:    */     }
/* 110:308 */     return (ModelMBeanAttributeInfo[])infos.toArray(new ModelMBeanAttributeInfo[infos.size()]);
/* 111:    */   }
/* 112:    */   
/* 113:    */   protected ModelMBeanOperationInfo[] getOperationInfo(Object managedBean, String beanKey)
/* 114:    */   {
/* 115:325 */     Method[] methods = getClassToExpose(managedBean).getMethods();
/* 116:326 */     List<ModelMBeanOperationInfo> infos = new ArrayList();
/* 117:328 */     for (Method method : methods) {
/* 118:329 */       if (!method.isSynthetic()) {
/* 119:332 */         if (!method.getDeclaringClass().equals(Object.class))
/* 120:    */         {
/* 121:336 */           ModelMBeanOperationInfo info = null;
/* 122:337 */           PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
/* 123:338 */           if ((pd != null) && (
/* 124:339 */             ((method.equals(pd.getReadMethod())) && (includeReadAttribute(method, beanKey))) || (
/* 125:340 */             (method.equals(pd.getWriteMethod())) && (includeWriteAttribute(method, beanKey)))))
/* 126:    */           {
/* 127:343 */             info = createModelMBeanOperationInfo(method, pd.getName(), beanKey);
/* 128:344 */             Descriptor desc = info.getDescriptor();
/* 129:345 */             if (method.equals(pd.getReadMethod())) {
/* 130:346 */               desc.setField("role", "getter");
/* 131:    */             } else {
/* 132:349 */               desc.setField("role", "setter");
/* 133:    */             }
/* 134:351 */             desc.setField("visibility", Integer.valueOf(4));
/* 135:352 */             if (isExposeClassDescriptor()) {
/* 136:353 */               desc.setField("class", getClassForDescriptor(managedBean).getName());
/* 137:    */             }
/* 138:355 */             info.setDescriptor(desc);
/* 139:    */           }
/* 140:360 */           if ((info == null) && (includeOperation(method, beanKey)))
/* 141:    */           {
/* 142:361 */             info = createModelMBeanOperationInfo(method, method.getName(), beanKey);
/* 143:362 */             Descriptor desc = info.getDescriptor();
/* 144:363 */             desc.setField("role", "operation");
/* 145:364 */             if (isExposeClassDescriptor()) {
/* 146:365 */               desc.setField("class", getClassForDescriptor(managedBean).getName());
/* 147:    */             }
/* 148:367 */             populateOperationDescriptor(desc, method, beanKey);
/* 149:368 */             info.setDescriptor(desc);
/* 150:    */           }
/* 151:371 */           if (info != null) {
/* 152:372 */             infos.add(info);
/* 153:    */           }
/* 154:    */         }
/* 155:    */       }
/* 156:    */     }
/* 157:376 */     return (ModelMBeanOperationInfo[])infos.toArray(new ModelMBeanOperationInfo[infos.size()]);
/* 158:    */   }
/* 159:    */   
/* 160:    */   protected ModelMBeanOperationInfo createModelMBeanOperationInfo(Method method, String name, String beanKey)
/* 161:    */   {
/* 162:389 */     MBeanParameterInfo[] params = getOperationParameters(method, beanKey);
/* 163:390 */     if (params.length == 0) {
/* 164:391 */       return new ModelMBeanOperationInfo(getOperationDescription(method, beanKey), method);
/* 165:    */     }
/* 166:394 */     return new ModelMBeanOperationInfo(name, 
/* 167:395 */       getOperationDescription(method, beanKey), 
/* 168:396 */       getOperationParameters(method, beanKey), 
/* 169:397 */       method.getReturnType().getName(), 
/* 170:398 */       3);
/* 171:    */   }
/* 172:    */   
/* 173:    */   protected Class getClassForDescriptor(Object managedBean)
/* 174:    */   {
/* 175:414 */     if (AopUtils.isJdkDynamicProxy(managedBean)) {
/* 176:415 */       return org.springframework.aop.framework.AopProxyUtils.proxiedUserInterfaces(managedBean)[0];
/* 177:    */     }
/* 178:417 */     return getClassToExpose(managedBean);
/* 179:    */   }
/* 180:    */   
/* 181:    */   protected abstract boolean includeReadAttribute(Method paramMethod, String paramString);
/* 182:    */   
/* 183:    */   protected abstract boolean includeWriteAttribute(Method paramMethod, String paramString);
/* 184:    */   
/* 185:    */   protected abstract boolean includeOperation(Method paramMethod, String paramString);
/* 186:    */   
/* 187:    */   protected String getAttributeDescription(PropertyDescriptor propertyDescriptor, String beanKey)
/* 188:    */   {
/* 189:461 */     return propertyDescriptor.getDisplayName();
/* 190:    */   }
/* 191:    */   
/* 192:    */   protected String getOperationDescription(Method method, String beanKey)
/* 193:    */   {
/* 194:474 */     return method.getName();
/* 195:    */   }
/* 196:    */   
/* 197:    */   protected MBeanParameterInfo[] getOperationParameters(Method method, String beanKey)
/* 198:    */   {
/* 199:486 */     return new MBeanParameterInfo[0];
/* 200:    */   }
/* 201:    */   
/* 202:    */   protected void populateMBeanDescriptor(Descriptor descriptor, Object managedBean, String beanKey)
/* 203:    */   {
/* 204:503 */     applyDefaultCurrencyTimeLimit(descriptor);
/* 205:    */   }
/* 206:    */   
/* 207:    */   protected void populateAttributeDescriptor(Descriptor desc, Method getter, Method setter, String beanKey)
/* 208:    */   {
/* 209:520 */     applyDefaultCurrencyTimeLimit(desc);
/* 210:    */   }
/* 211:    */   
/* 212:    */   protected void populateOperationDescriptor(Descriptor desc, Method method, String beanKey)
/* 213:    */   {
/* 214:536 */     applyDefaultCurrencyTimeLimit(desc);
/* 215:    */   }
/* 216:    */   
/* 217:    */   protected final void applyDefaultCurrencyTimeLimit(Descriptor desc)
/* 218:    */   {
/* 219:546 */     if (getDefaultCurrencyTimeLimit() != null) {
/* 220:547 */       desc.setField("currencyTimeLimit", getDefaultCurrencyTimeLimit().toString());
/* 221:    */     }
/* 222:    */   }
/* 223:    */   
/* 224:    */   protected void applyCurrencyTimeLimit(Descriptor desc, int currencyTimeLimit)
/* 225:    */   {
/* 226:563 */     if (currencyTimeLimit > 0) {
/* 227:565 */       desc.setField("currencyTimeLimit", Integer.toString(currencyTimeLimit));
/* 228:567 */     } else if (currencyTimeLimit == 0) {
/* 229:569 */       desc.setField("currencyTimeLimit", Integer.toString(2147483647));
/* 230:    */     } else {
/* 231:573 */       applyDefaultCurrencyTimeLimit(desc);
/* 232:    */     }
/* 233:    */   }
/* 234:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.assembler.AbstractReflectiveMBeanInfoAssembler
 * JD-Core Version:    0.7.0.1
 */