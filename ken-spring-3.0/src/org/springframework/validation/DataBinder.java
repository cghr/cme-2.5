/*   1:    */ package org.springframework.validation;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditor;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Map;
/*   6:    */ import org.apache.commons.logging.Log;
/*   7:    */ import org.apache.commons.logging.LogFactory;
/*   8:    */ import org.springframework.beans.ConfigurablePropertyAccessor;
/*   9:    */ import org.springframework.beans.MutablePropertyValues;
/*  10:    */ import org.springframework.beans.PropertyAccessException;
/*  11:    */ import org.springframework.beans.PropertyAccessorUtils;
/*  12:    */ import org.springframework.beans.PropertyBatchUpdateException;
/*  13:    */ import org.springframework.beans.PropertyEditorRegistry;
/*  14:    */ import org.springframework.beans.PropertyValue;
/*  15:    */ import org.springframework.beans.PropertyValues;
/*  16:    */ import org.springframework.beans.SimpleTypeConverter;
/*  17:    */ import org.springframework.beans.TypeConverter;
/*  18:    */ import org.springframework.beans.TypeMismatchException;
/*  19:    */ import org.springframework.core.MethodParameter;
/*  20:    */ import org.springframework.core.convert.ConversionService;
/*  21:    */ import org.springframework.util.Assert;
/*  22:    */ import org.springframework.util.ObjectUtils;
/*  23:    */ import org.springframework.util.PatternMatchUtils;
/*  24:    */ import org.springframework.util.StringUtils;
/*  25:    */ 
/*  26:    */ public class DataBinder
/*  27:    */   implements PropertyEditorRegistry, TypeConverter
/*  28:    */ {
/*  29:    */   public static final String DEFAULT_OBJECT_NAME = "target";
/*  30:    */   public static final int DEFAULT_AUTO_GROW_COLLECTION_LIMIT = 256;
/*  31:115 */   protected static final Log logger = LogFactory.getLog(DataBinder.class);
/*  32:    */   private final Object target;
/*  33:    */   private final String objectName;
/*  34:    */   private AbstractPropertyBindingResult bindingResult;
/*  35:    */   private SimpleTypeConverter typeConverter;
/*  36:    */   private BindException bindException;
/*  37:127 */   private boolean ignoreUnknownFields = true;
/*  38:129 */   private boolean ignoreInvalidFields = false;
/*  39:131 */   private boolean autoGrowNestedPaths = true;
/*  40:133 */   private int autoGrowCollectionLimit = 256;
/*  41:    */   private String[] allowedFields;
/*  42:    */   private String[] disallowedFields;
/*  43:    */   private String[] requiredFields;
/*  44:141 */   private BindingErrorProcessor bindingErrorProcessor = new DefaultBindingErrorProcessor();
/*  45:    */   private Validator validator;
/*  46:    */   private ConversionService conversionService;
/*  47:    */   
/*  48:    */   public DataBinder(Object target)
/*  49:    */   {
/*  50:155 */     this(target, "target");
/*  51:    */   }
/*  52:    */   
/*  53:    */   public DataBinder(Object target, String objectName)
/*  54:    */   {
/*  55:165 */     this.target = target;
/*  56:166 */     this.objectName = objectName;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public Object getTarget()
/*  60:    */   {
/*  61:174 */     return this.target;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public String getObjectName()
/*  65:    */   {
/*  66:181 */     return this.objectName;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void setAutoGrowNestedPaths(boolean autoGrowNestedPaths)
/*  70:    */   {
/*  71:195 */     Assert.state(this.bindingResult == null, 
/*  72:196 */       "DataBinder is already initialized - call setAutoGrowNestedPaths before other configuration methods");
/*  73:197 */     this.autoGrowNestedPaths = autoGrowNestedPaths;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public boolean isAutoGrowNestedPaths()
/*  77:    */   {
/*  78:204 */     return this.autoGrowNestedPaths;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void setAutoGrowCollectionLimit(int autoGrowCollectionLimit)
/*  82:    */   {
/*  83:213 */     this.autoGrowCollectionLimit = autoGrowCollectionLimit;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public int getAutoGrowCollectionLimit()
/*  87:    */   {
/*  88:220 */     return this.autoGrowCollectionLimit;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void initBeanPropertyAccess()
/*  92:    */   {
/*  93:229 */     Assert.state(this.bindingResult == null, 
/*  94:230 */       "DataBinder is already initialized - call initBeanPropertyAccess before other configuration methods");
/*  95:231 */     this.bindingResult = new BeanPropertyBindingResult(
/*  96:232 */       getTarget(), getObjectName(), isAutoGrowNestedPaths(), getAutoGrowCollectionLimit());
/*  97:233 */     if (this.conversionService != null) {
/*  98:234 */       this.bindingResult.initConversion(this.conversionService);
/*  99:    */     }
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void initDirectFieldAccess()
/* 103:    */   {
/* 104:244 */     Assert.state(this.bindingResult == null, 
/* 105:245 */       "DataBinder is already initialized - call initDirectFieldAccess before other configuration methods");
/* 106:246 */     this.bindingResult = new DirectFieldBindingResult(getTarget(), getObjectName());
/* 107:247 */     if (this.conversionService != null) {
/* 108:248 */       this.bindingResult.initConversion(this.conversionService);
/* 109:    */     }
/* 110:    */   }
/* 111:    */   
/* 112:    */   protected AbstractPropertyBindingResult getInternalBindingResult()
/* 113:    */   {
/* 114:257 */     if (this.bindingResult == null) {
/* 115:258 */       initBeanPropertyAccess();
/* 116:    */     }
/* 117:260 */     return this.bindingResult;
/* 118:    */   }
/* 119:    */   
/* 120:    */   protected ConfigurablePropertyAccessor getPropertyAccessor()
/* 121:    */   {
/* 122:267 */     return getInternalBindingResult().getPropertyAccessor();
/* 123:    */   }
/* 124:    */   
/* 125:    */   protected SimpleTypeConverter getSimpleTypeConverter()
/* 126:    */   {
/* 127:274 */     if (this.typeConverter == null)
/* 128:    */     {
/* 129:275 */       this.typeConverter = new SimpleTypeConverter();
/* 130:276 */       if (this.conversionService != null) {
/* 131:277 */         this.typeConverter.setConversionService(this.conversionService);
/* 132:    */       }
/* 133:    */     }
/* 134:280 */     return this.typeConverter;
/* 135:    */   }
/* 136:    */   
/* 137:    */   protected PropertyEditorRegistry getPropertyEditorRegistry()
/* 138:    */   {
/* 139:287 */     if (getTarget() != null) {
/* 140:288 */       return getInternalBindingResult().getPropertyAccessor();
/* 141:    */     }
/* 142:291 */     return getSimpleTypeConverter();
/* 143:    */   }
/* 144:    */   
/* 145:    */   protected TypeConverter getTypeConverter()
/* 146:    */   {
/* 147:299 */     if (getTarget() != null) {
/* 148:300 */       return getInternalBindingResult().getPropertyAccessor();
/* 149:    */     }
/* 150:303 */     return getSimpleTypeConverter();
/* 151:    */   }
/* 152:    */   
/* 153:    */   public BindingResult getBindingResult()
/* 154:    */   {
/* 155:317 */     return getInternalBindingResult();
/* 156:    */   }
/* 157:    */   
/* 158:    */   @Deprecated
/* 159:    */   public BindException getErrors()
/* 160:    */   {
/* 161:330 */     if (this.bindException == null) {
/* 162:331 */       this.bindException = new BindException(getBindingResult());
/* 163:    */     }
/* 164:333 */     return this.bindException;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public void setIgnoreUnknownFields(boolean ignoreUnknownFields)
/* 168:    */   {
/* 169:348 */     this.ignoreUnknownFields = ignoreUnknownFields;
/* 170:    */   }
/* 171:    */   
/* 172:    */   public boolean isIgnoreUnknownFields()
/* 173:    */   {
/* 174:355 */     return this.ignoreUnknownFields;
/* 175:    */   }
/* 176:    */   
/* 177:    */   public void setIgnoreInvalidFields(boolean ignoreInvalidFields)
/* 178:    */   {
/* 179:370 */     this.ignoreInvalidFields = ignoreInvalidFields;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public boolean isIgnoreInvalidFields()
/* 183:    */   {
/* 184:377 */     return this.ignoreInvalidFields;
/* 185:    */   }
/* 186:    */   
/* 187:    */   public void setAllowedFields(String... allowedFields)
/* 188:    */   {
/* 189:393 */     this.allowedFields = PropertyAccessorUtils.canonicalPropertyNames(allowedFields);
/* 190:    */   }
/* 191:    */   
/* 192:    */   public String[] getAllowedFields()
/* 193:    */   {
/* 194:401 */     return this.allowedFields;
/* 195:    */   }
/* 196:    */   
/* 197:    */   public void setDisallowedFields(String... disallowedFields)
/* 198:    */   {
/* 199:417 */     this.disallowedFields = PropertyAccessorUtils.canonicalPropertyNames(disallowedFields);
/* 200:    */   }
/* 201:    */   
/* 202:    */   public String[] getDisallowedFields()
/* 203:    */   {
/* 204:425 */     return this.disallowedFields;
/* 205:    */   }
/* 206:    */   
/* 207:    */   public void setRequiredFields(String... requiredFields)
/* 208:    */   {
/* 209:439 */     this.requiredFields = PropertyAccessorUtils.canonicalPropertyNames(requiredFields);
/* 210:440 */     if (logger.isDebugEnabled()) {
/* 211:441 */       logger.debug("DataBinder requires binding of required fields [" + 
/* 212:442 */         StringUtils.arrayToCommaDelimitedString(requiredFields) + "]");
/* 213:    */     }
/* 214:    */   }
/* 215:    */   
/* 216:    */   public String[] getRequiredFields()
/* 217:    */   {
/* 218:451 */     return this.requiredFields;
/* 219:    */   }
/* 220:    */   
/* 221:    */   public void setExtractOldValueForEditor(boolean extractOldValueForEditor)
/* 222:    */   {
/* 223:461 */     getPropertyAccessor().setExtractOldValueForEditor(extractOldValueForEditor);
/* 224:    */   }
/* 225:    */   
/* 226:    */   public void setMessageCodesResolver(MessageCodesResolver messageCodesResolver)
/* 227:    */   {
/* 228:472 */     getInternalBindingResult().setMessageCodesResolver(messageCodesResolver);
/* 229:    */   }
/* 230:    */   
/* 231:    */   public void setBindingErrorProcessor(BindingErrorProcessor bindingErrorProcessor)
/* 232:    */   {
/* 233:482 */     Assert.notNull(bindingErrorProcessor, "BindingErrorProcessor must not be null");
/* 234:483 */     this.bindingErrorProcessor = bindingErrorProcessor;
/* 235:    */   }
/* 236:    */   
/* 237:    */   public BindingErrorProcessor getBindingErrorProcessor()
/* 238:    */   {
/* 239:490 */     return this.bindingErrorProcessor;
/* 240:    */   }
/* 241:    */   
/* 242:    */   public void setValidator(Validator validator)
/* 243:    */   {
/* 244:497 */     if ((validator != null) && (getTarget() != null) && (!validator.supports(getTarget().getClass()))) {
/* 245:498 */       throw new IllegalStateException("Invalid target for Validator [" + validator + "]: " + getTarget());
/* 246:    */     }
/* 247:500 */     this.validator = validator;
/* 248:    */   }
/* 249:    */   
/* 250:    */   public Validator getValidator()
/* 251:    */   {
/* 252:507 */     return this.validator;
/* 253:    */   }
/* 254:    */   
/* 255:    */   public void setConversionService(ConversionService conversionService)
/* 256:    */   {
/* 257:520 */     Assert.state(this.conversionService == null, "DataBinder is already initialized with ConversionService");
/* 258:521 */     this.conversionService = conversionService;
/* 259:522 */     if ((this.bindingResult != null) && (conversionService != null)) {
/* 260:523 */       this.bindingResult.initConversion(conversionService);
/* 261:    */     }
/* 262:    */   }
/* 263:    */   
/* 264:    */   public ConversionService getConversionService()
/* 265:    */   {
/* 266:531 */     return this.conversionService;
/* 267:    */   }
/* 268:    */   
/* 269:    */   public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor)
/* 270:    */   {
/* 271:535 */     getPropertyEditorRegistry().registerCustomEditor(requiredType, propertyEditor);
/* 272:    */   }
/* 273:    */   
/* 274:    */   public void registerCustomEditor(Class<?> requiredType, String field, PropertyEditor propertyEditor)
/* 275:    */   {
/* 276:539 */     getPropertyEditorRegistry().registerCustomEditor(requiredType, field, propertyEditor);
/* 277:    */   }
/* 278:    */   
/* 279:    */   public PropertyEditor findCustomEditor(Class<?> requiredType, String propertyPath)
/* 280:    */   {
/* 281:543 */     return getPropertyEditorRegistry().findCustomEditor(requiredType, propertyPath);
/* 282:    */   }
/* 283:    */   
/* 284:    */   public <T> T convertIfNecessary(Object value, Class<T> requiredType)
/* 285:    */     throws TypeMismatchException
/* 286:    */   {
/* 287:547 */     return getTypeConverter().convertIfNecessary(value, requiredType);
/* 288:    */   }
/* 289:    */   
/* 290:    */   public <T> T convertIfNecessary(Object value, Class<T> requiredType, MethodParameter methodParam)
/* 291:    */     throws TypeMismatchException
/* 292:    */   {
/* 293:553 */     return getTypeConverter().convertIfNecessary(value, requiredType, methodParam);
/* 294:    */   }
/* 295:    */   
/* 296:    */   public void bind(PropertyValues pvs)
/* 297:    */   {
/* 298:571 */     MutablePropertyValues mpvs = (pvs instanceof MutablePropertyValues) ? 
/* 299:572 */       (MutablePropertyValues)pvs : new MutablePropertyValues(pvs);
/* 300:573 */     doBind(mpvs);
/* 301:    */   }
/* 302:    */   
/* 303:    */   protected void doBind(MutablePropertyValues mpvs)
/* 304:    */   {
/* 305:586 */     checkAllowedFields(mpvs);
/* 306:587 */     checkRequiredFields(mpvs);
/* 307:588 */     applyPropertyValues(mpvs);
/* 308:    */   }
/* 309:    */   
/* 310:    */   protected void checkAllowedFields(MutablePropertyValues mpvs)
/* 311:    */   {
/* 312:599 */     PropertyValue[] pvs = mpvs.getPropertyValues();
/* 313:600 */     for (PropertyValue pv : pvs)
/* 314:    */     {
/* 315:601 */       String field = PropertyAccessorUtils.canonicalPropertyName(pv.getName());
/* 316:602 */       if (!isAllowed(field))
/* 317:    */       {
/* 318:603 */         mpvs.removePropertyValue(pv);
/* 319:604 */         getBindingResult().recordSuppressedField(field);
/* 320:605 */         if (logger.isDebugEnabled()) {
/* 321:606 */           logger.debug("Field [" + field + "] has been removed from PropertyValues " + 
/* 322:607 */             "and will not be bound, because it has not been found in the list of allowed fields");
/* 323:    */         }
/* 324:    */       }
/* 325:    */     }
/* 326:    */   }
/* 327:    */   
/* 328:    */   protected boolean isAllowed(String field)
/* 329:    */   {
/* 330:628 */     String[] allowed = getAllowedFields();
/* 331:629 */     String[] disallowed = getDisallowedFields();
/* 332:    */     
/* 333:631 */     return ((ObjectUtils.isEmpty(allowed)) || (PatternMatchUtils.simpleMatch(allowed, field))) && ((ObjectUtils.isEmpty(disallowed)) || (!PatternMatchUtils.simpleMatch(disallowed, field)));
/* 334:    */   }
/* 335:    */   
/* 336:    */   protected void checkRequiredFields(MutablePropertyValues mpvs)
/* 337:    */   {
/* 338:643 */     String[] requiredFields = getRequiredFields();
/* 339:644 */     if (!ObjectUtils.isEmpty(requiredFields))
/* 340:    */     {
/* 341:645 */       Map<String, PropertyValue> propertyValues = new HashMap();
/* 342:646 */       PropertyValue[] pvs = mpvs.getPropertyValues();
/* 343:647 */       for (PropertyValue pv : pvs)
/* 344:    */       {
/* 345:648 */         String canonicalName = PropertyAccessorUtils.canonicalPropertyName(pv.getName());
/* 346:649 */         propertyValues.put(canonicalName, pv);
/* 347:    */       }
/* 348:651 */       for (String field : requiredFields)
/* 349:    */       {
/* 350:652 */         PropertyValue pv = (PropertyValue)propertyValues.get(field);
/* 351:653 */         boolean empty = (pv == null) || (pv.getValue() == null);
/* 352:654 */         if (!empty) {
/* 353:655 */           if ((pv.getValue() instanceof String))
/* 354:    */           {
/* 355:656 */             empty = !StringUtils.hasText((String)pv.getValue());
/* 356:    */           }
/* 357:658 */           else if ((pv.getValue() instanceof String[]))
/* 358:    */           {
/* 359:659 */             String[] values = (String[])pv.getValue();
/* 360:660 */             empty = (values.length == 0) || (!StringUtils.hasText(values[0]));
/* 361:    */           }
/* 362:    */         }
/* 363:663 */         if (empty)
/* 364:    */         {
/* 365:665 */           getBindingErrorProcessor().processMissingFieldError(field, getInternalBindingResult());
/* 366:668 */           if (pv != null)
/* 367:    */           {
/* 368:669 */             mpvs.removePropertyValue(pv);
/* 369:670 */             propertyValues.remove(field);
/* 370:    */           }
/* 371:    */         }
/* 372:    */       }
/* 373:    */     }
/* 374:    */   }
/* 375:    */   
/* 376:    */   protected void applyPropertyValues(MutablePropertyValues mpvs)
/* 377:    */   {
/* 378:    */     try
/* 379:    */     {
/* 380:692 */       getPropertyAccessor().setPropertyValues(mpvs, isIgnoreUnknownFields(), isIgnoreInvalidFields());
/* 381:    */     }
/* 382:    */     catch (PropertyBatchUpdateException ex)
/* 383:    */     {
/* 384:696 */       for (PropertyAccessException pae : ex.getPropertyAccessExceptions()) {
/* 385:697 */         getBindingErrorProcessor().processPropertyAccessException(pae, getInternalBindingResult());
/* 386:    */       }
/* 387:    */     }
/* 388:    */   }
/* 389:    */   
/* 390:    */   public void validate()
/* 391:    */   {
/* 392:709 */     Validator validator = getValidator();
/* 393:710 */     if (validator != null) {
/* 394:711 */       validator.validate(getTarget(), getBindingResult());
/* 395:    */     }
/* 396:    */   }
/* 397:    */   
/* 398:    */   public Map<?, ?> close()
/* 399:    */     throws BindException
/* 400:    */   {
/* 401:723 */     if (getBindingResult().hasErrors()) {
/* 402:724 */       throw new BindException(getBindingResult());
/* 403:    */     }
/* 404:726 */     return getBindingResult().getModel();
/* 405:    */   }
/* 406:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.DataBinder
 * JD-Core Version:    0.7.0.1
 */