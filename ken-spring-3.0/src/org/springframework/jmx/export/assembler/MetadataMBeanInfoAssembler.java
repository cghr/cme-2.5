/*   1:    */ package org.springframework.jmx.export.assembler;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyDescriptor;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import javax.management.Descriptor;
/*   6:    */ import javax.management.MBeanParameterInfo;
/*   7:    */ import javax.management.modelmbean.ModelMBeanNotificationInfo;
/*   8:    */ import org.springframework.aop.support.AopUtils;
/*   9:    */ import org.springframework.beans.BeanUtils;
/*  10:    */ import org.springframework.beans.factory.InitializingBean;
/*  11:    */ import org.springframework.jmx.export.metadata.InvalidMetadataException;
/*  12:    */ import org.springframework.jmx.export.metadata.JmxAttributeSource;
/*  13:    */ import org.springframework.jmx.export.metadata.JmxMetadataUtils;
/*  14:    */ import org.springframework.jmx.export.metadata.ManagedAttribute;
/*  15:    */ import org.springframework.jmx.export.metadata.ManagedMetric;
/*  16:    */ import org.springframework.jmx.export.metadata.ManagedNotification;
/*  17:    */ import org.springframework.jmx.export.metadata.ManagedOperation;
/*  18:    */ import org.springframework.jmx.export.metadata.ManagedOperationParameter;
/*  19:    */ import org.springframework.jmx.export.metadata.ManagedResource;
/*  20:    */ import org.springframework.jmx.support.MetricType;
/*  21:    */ import org.springframework.util.Assert;
/*  22:    */ import org.springframework.util.StringUtils;
/*  23:    */ 
/*  24:    */ public class MetadataMBeanInfoAssembler
/*  25:    */   extends AbstractReflectiveMBeanInfoAssembler
/*  26:    */   implements AutodetectCapableMBeanInfoAssembler, InitializingBean
/*  27:    */ {
/*  28:    */   private JmxAttributeSource attributeSource;
/*  29:    */   
/*  30:    */   public MetadataMBeanInfoAssembler() {}
/*  31:    */   
/*  32:    */   public MetadataMBeanInfoAssembler(JmxAttributeSource attributeSource)
/*  33:    */   {
/*  34: 76 */     Assert.notNull(attributeSource, "JmxAttributeSource must not be null");
/*  35: 77 */     this.attributeSource = attributeSource;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setAttributeSource(JmxAttributeSource attributeSource)
/*  39:    */   {
/*  40: 87 */     Assert.notNull(attributeSource, "JmxAttributeSource must not be null");
/*  41: 88 */     this.attributeSource = attributeSource;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void afterPropertiesSet()
/*  45:    */   {
/*  46: 92 */     if (this.attributeSource == null) {
/*  47: 93 */       throw new IllegalArgumentException("Property 'attributeSource' is required");
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected void checkManagedBean(Object managedBean)
/*  52:    */     throws IllegalArgumentException
/*  53:    */   {
/*  54:104 */     if (AopUtils.isJdkDynamicProxy(managedBean)) {
/*  55:105 */       throw new IllegalArgumentException(
/*  56:106 */         "MetadataMBeanInfoAssembler does not support JDK dynamic proxies - export the target beans directly or use CGLIB proxies instead");
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:    */   public boolean includeBean(Class<?> beanClass, String beanName)
/*  61:    */   {
/*  62:118 */     return this.attributeSource.getManagedResource(getClassToExpose(beanClass)) != null;
/*  63:    */   }
/*  64:    */   
/*  65:    */   protected boolean includeReadAttribute(Method method, String beanKey)
/*  66:    */   {
/*  67:129 */     return (hasManagedAttribute(method)) || (hasManagedMetric(method));
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected boolean includeWriteAttribute(Method method, String beanKey)
/*  71:    */   {
/*  72:140 */     return hasManagedAttribute(method);
/*  73:    */   }
/*  74:    */   
/*  75:    */   protected boolean includeOperation(Method method, String beanKey)
/*  76:    */   {
/*  77:151 */     PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
/*  78:152 */     if ((pd != null) && 
/*  79:153 */       (hasManagedAttribute(method))) {
/*  80:154 */       return true;
/*  81:    */     }
/*  82:157 */     return hasManagedOperation(method);
/*  83:    */   }
/*  84:    */   
/*  85:    */   private boolean hasManagedAttribute(Method method)
/*  86:    */   {
/*  87:164 */     return this.attributeSource.getManagedAttribute(method) != null;
/*  88:    */   }
/*  89:    */   
/*  90:    */   private boolean hasManagedMetric(Method method)
/*  91:    */   {
/*  92:171 */     return this.attributeSource.getManagedMetric(method) != null;
/*  93:    */   }
/*  94:    */   
/*  95:    */   private boolean hasManagedOperation(Method method)
/*  96:    */   {
/*  97:179 */     return this.attributeSource.getManagedOperation(method) != null;
/*  98:    */   }
/*  99:    */   
/* 100:    */   protected String getDescription(Object managedBean, String beanKey)
/* 101:    */   {
/* 102:189 */     ManagedResource mr = this.attributeSource.getManagedResource(getClassToExpose(managedBean));
/* 103:190 */     return mr != null ? mr.getDescription() : "";
/* 104:    */   }
/* 105:    */   
/* 106:    */   protected String getAttributeDescription(PropertyDescriptor propertyDescriptor, String beanKey)
/* 107:    */   {
/* 108:200 */     Method readMethod = propertyDescriptor.getReadMethod();
/* 109:201 */     Method writeMethod = propertyDescriptor.getWriteMethod();
/* 110:    */     
/* 111:203 */     ManagedAttribute getter = 
/* 112:204 */       readMethod != null ? this.attributeSource.getManagedAttribute(readMethod) : null;
/* 113:205 */     ManagedAttribute setter = 
/* 114:206 */       writeMethod != null ? this.attributeSource.getManagedAttribute(writeMethod) : null;
/* 115:208 */     if ((getter != null) && (StringUtils.hasText(getter.getDescription()))) {
/* 116:209 */       return getter.getDescription();
/* 117:    */     }
/* 118:211 */     if ((setter != null) && (StringUtils.hasText(setter.getDescription()))) {
/* 119:212 */       return setter.getDescription();
/* 120:    */     }
/* 121:215 */     ManagedMetric metric = readMethod != null ? this.attributeSource.getManagedMetric(readMethod) : null;
/* 122:216 */     if ((metric != null) && (StringUtils.hasText(metric.getDescription()))) {
/* 123:217 */       return metric.getDescription();
/* 124:    */     }
/* 125:220 */     return propertyDescriptor.getDisplayName();
/* 126:    */   }
/* 127:    */   
/* 128:    */   protected String getOperationDescription(Method method, String beanKey)
/* 129:    */   {
/* 130:229 */     PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
/* 131:230 */     if (pd != null)
/* 132:    */     {
/* 133:231 */       ManagedAttribute ma = this.attributeSource.getManagedAttribute(method);
/* 134:232 */       if ((ma != null) && (StringUtils.hasText(ma.getDescription()))) {
/* 135:233 */         return ma.getDescription();
/* 136:    */       }
/* 137:235 */       ManagedMetric metric = this.attributeSource.getManagedMetric(method);
/* 138:236 */       if ((metric != null) && (StringUtils.hasText(metric.getDescription()))) {
/* 139:237 */         return metric.getDescription();
/* 140:    */       }
/* 141:239 */       return method.getName();
/* 142:    */     }
/* 143:242 */     ManagedOperation mo = this.attributeSource.getManagedOperation(method);
/* 144:243 */     if ((mo != null) && (StringUtils.hasText(mo.getDescription()))) {
/* 145:244 */       return mo.getDescription();
/* 146:    */     }
/* 147:246 */     return method.getName();
/* 148:    */   }
/* 149:    */   
/* 150:    */   protected MBeanParameterInfo[] getOperationParameters(Method method, String beanKey)
/* 151:    */   {
/* 152:257 */     ManagedOperationParameter[] params = this.attributeSource.getManagedOperationParameters(method);
/* 153:258 */     if ((params == null) || (params.length == 0)) {
/* 154:259 */       return new MBeanParameterInfo[0];
/* 155:    */     }
/* 156:262 */     MBeanParameterInfo[] parameterInfo = new MBeanParameterInfo[params.length];
/* 157:263 */     Class[] methodParameters = method.getParameterTypes();
/* 158:265 */     for (int i = 0; i < params.length; i++)
/* 159:    */     {
/* 160:266 */       ManagedOperationParameter param = params[i];
/* 161:267 */       parameterInfo[i] = 
/* 162:268 */         new MBeanParameterInfo(param.getName(), methodParameters[i].getName(), param.getDescription());
/* 163:    */     }
/* 164:271 */     return parameterInfo;
/* 165:    */   }
/* 166:    */   
/* 167:    */   protected ModelMBeanNotificationInfo[] getNotificationInfo(Object managedBean, String beanKey)
/* 168:    */   {
/* 169:280 */     ManagedNotification[] notificationAttributes = 
/* 170:281 */       this.attributeSource.getManagedNotifications(getClassToExpose(managedBean));
/* 171:282 */     ModelMBeanNotificationInfo[] notificationInfos = 
/* 172:283 */       new ModelMBeanNotificationInfo[notificationAttributes.length];
/* 173:285 */     for (int i = 0; i < notificationAttributes.length; i++)
/* 174:    */     {
/* 175:286 */       ManagedNotification attribute = notificationAttributes[i];
/* 176:287 */       notificationInfos[i] = JmxMetadataUtils.convertToModelMBeanNotificationInfo(attribute);
/* 177:    */     }
/* 178:290 */     return notificationInfos;
/* 179:    */   }
/* 180:    */   
/* 181:    */   protected void populateMBeanDescriptor(Descriptor desc, Object managedBean, String beanKey)
/* 182:    */   {
/* 183:301 */     ManagedResource mr = this.attributeSource.getManagedResource(getClassToExpose(managedBean));
/* 184:302 */     if (mr == null) {
/* 185:303 */       throw new InvalidMetadataException(
/* 186:304 */         "No ManagedResource attribute found for class: " + getClassToExpose(managedBean));
/* 187:    */     }
/* 188:307 */     applyCurrencyTimeLimit(desc, mr.getCurrencyTimeLimit());
/* 189:309 */     if (mr.isLog()) {
/* 190:310 */       desc.setField("log", "true");
/* 191:    */     }
/* 192:312 */     if (StringUtils.hasLength(mr.getLogFile())) {
/* 193:313 */       desc.setField("logFile", mr.getLogFile());
/* 194:    */     }
/* 195:316 */     if (StringUtils.hasLength(mr.getPersistPolicy())) {
/* 196:317 */       desc.setField("persistPolicy", mr.getPersistPolicy());
/* 197:    */     }
/* 198:319 */     if (mr.getPersistPeriod() >= 0) {
/* 199:320 */       desc.setField("persistPeriod", Integer.toString(mr.getPersistPeriod()));
/* 200:    */     }
/* 201:322 */     if (StringUtils.hasLength(mr.getPersistName())) {
/* 202:323 */       desc.setField("persistName", mr.getPersistName());
/* 203:    */     }
/* 204:325 */     if (StringUtils.hasLength(mr.getPersistLocation())) {
/* 205:326 */       desc.setField("persistLocation", mr.getPersistLocation());
/* 206:    */     }
/* 207:    */   }
/* 208:    */   
/* 209:    */   protected void populateAttributeDescriptor(Descriptor desc, Method getter, Method setter, String beanKey)
/* 210:    */   {
/* 211:336 */     if ((getter != null) && (hasManagedMetric(getter)))
/* 212:    */     {
/* 213:337 */       populateMetricDescriptor(desc, this.attributeSource.getManagedMetric(getter));
/* 214:    */     }
/* 215:    */     else
/* 216:    */     {
/* 217:340 */       ManagedAttribute gma = 
/* 218:341 */         getter == null ? ManagedAttribute.EMPTY : this.attributeSource.getManagedAttribute(getter);
/* 219:342 */       ManagedAttribute sma = 
/* 220:343 */         setter == null ? ManagedAttribute.EMPTY : this.attributeSource.getManagedAttribute(setter);
/* 221:344 */       populateAttributeDescriptor(desc, gma, sma);
/* 222:    */     }
/* 223:    */   }
/* 224:    */   
/* 225:    */   private void populateAttributeDescriptor(Descriptor desc, ManagedAttribute gma, ManagedAttribute sma)
/* 226:    */   {
/* 227:349 */     applyCurrencyTimeLimit(desc, resolveIntDescriptor(gma.getCurrencyTimeLimit(), sma.getCurrencyTimeLimit()));
/* 228:    */     
/* 229:351 */     Object defaultValue = resolveObjectDescriptor(gma.getDefaultValue(), sma.getDefaultValue());
/* 230:352 */     desc.setField("default", defaultValue);
/* 231:    */     
/* 232:354 */     String persistPolicy = resolveStringDescriptor(gma.getPersistPolicy(), sma.getPersistPolicy());
/* 233:355 */     if (StringUtils.hasLength(persistPolicy)) {
/* 234:356 */       desc.setField("persistPolicy", persistPolicy);
/* 235:    */     }
/* 236:358 */     int persistPeriod = resolveIntDescriptor(gma.getPersistPeriod(), sma.getPersistPeriod());
/* 237:359 */     if (persistPeriod >= 0) {
/* 238:360 */       desc.setField("persistPeriod", Integer.toString(persistPeriod));
/* 239:    */     }
/* 240:    */   }
/* 241:    */   
/* 242:    */   private void populateMetricDescriptor(Descriptor desc, ManagedMetric metric)
/* 243:    */   {
/* 244:365 */     applyCurrencyTimeLimit(desc, metric.getCurrencyTimeLimit());
/* 245:367 */     if (StringUtils.hasLength(metric.getPersistPolicy())) {
/* 246:368 */       desc.setField("persistPolicy", metric.getPersistPolicy());
/* 247:    */     }
/* 248:370 */     if (metric.getPersistPeriod() >= 0) {
/* 249:371 */       desc.setField("persistPeriod", Integer.toString(metric.getPersistPeriod()));
/* 250:    */     }
/* 251:374 */     if (StringUtils.hasLength(metric.getDisplayName())) {
/* 252:375 */       desc.setField("displayName", metric.getDisplayName());
/* 253:    */     }
/* 254:378 */     if (StringUtils.hasLength(metric.getUnit())) {
/* 255:379 */       desc.setField("units", metric.getUnit());
/* 256:    */     }
/* 257:382 */     if (StringUtils.hasLength(metric.getCategory())) {
/* 258:383 */       desc.setField("metricCategory", metric.getCategory());
/* 259:    */     }
/* 260:386 */     String metricType = metric.getMetricType() == null ? MetricType.GAUGE.toString() : metric.getMetricType().toString();
/* 261:387 */     desc.setField("metricType", metricType);
/* 262:    */   }
/* 263:    */   
/* 264:    */   protected void populateOperationDescriptor(Descriptor desc, Method method, String beanKey)
/* 265:    */   {
/* 266:397 */     ManagedOperation mo = this.attributeSource.getManagedOperation(method);
/* 267:398 */     if (mo != null) {
/* 268:399 */       applyCurrencyTimeLimit(desc, mo.getCurrencyTimeLimit());
/* 269:    */     }
/* 270:    */   }
/* 271:    */   
/* 272:    */   private int resolveIntDescriptor(int getter, int setter)
/* 273:    */   {
/* 274:413 */     return getter >= setter ? getter : setter;
/* 275:    */   }
/* 276:    */   
/* 277:    */   private Object resolveObjectDescriptor(Object getter, Object setter)
/* 278:    */   {
/* 279:425 */     return getter != null ? getter : setter;
/* 280:    */   }
/* 281:    */   
/* 282:    */   private String resolveStringDescriptor(String getter, String setter)
/* 283:    */   {
/* 284:439 */     return StringUtils.hasLength(getter) ? getter : setter;
/* 285:    */   }
/* 286:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler
 * JD-Core Version:    0.7.0.1
 */