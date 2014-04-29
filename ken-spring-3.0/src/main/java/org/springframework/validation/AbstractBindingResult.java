/*   1:    */ package org.springframework.validation;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditor;
/*   4:    */ import java.io.Serializable;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.HashSet;
/*   7:    */ import java.util.LinkedHashMap;
/*   8:    */ import java.util.LinkedList;
/*   9:    */ import java.util.List;
/*  10:    */ import java.util.Map;
/*  11:    */ import java.util.Set;
/*  12:    */ import org.springframework.beans.PropertyEditorRegistry;
/*  13:    */ import org.springframework.util.Assert;
/*  14:    */ import org.springframework.util.ObjectUtils;
/*  15:    */ import org.springframework.util.StringUtils;
/*  16:    */ 
/*  17:    */ public abstract class AbstractBindingResult
/*  18:    */   extends AbstractErrors
/*  19:    */   implements BindingResult, Serializable
/*  20:    */ {
/*  21:    */   private final String objectName;
/*  22: 49 */   private MessageCodesResolver messageCodesResolver = new DefaultMessageCodesResolver();
/*  23: 51 */   private final List<ObjectError> errors = new LinkedList();
/*  24: 53 */   private final Set<String> suppressedFields = new HashSet();
/*  25:    */   
/*  26:    */   protected AbstractBindingResult(String objectName)
/*  27:    */   {
/*  28: 62 */     this.objectName = objectName;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setMessageCodesResolver(MessageCodesResolver messageCodesResolver)
/*  32:    */   {
/*  33: 71 */     Assert.notNull(messageCodesResolver, "MessageCodesResolver must not be null");
/*  34: 72 */     this.messageCodesResolver = messageCodesResolver;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public MessageCodesResolver getMessageCodesResolver()
/*  38:    */   {
/*  39: 79 */     return this.messageCodesResolver;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public String getObjectName()
/*  43:    */   {
/*  44: 88 */     return this.objectName;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void reject(String errorCode, Object[] errorArgs, String defaultMessage)
/*  48:    */   {
/*  49: 93 */     addError(new ObjectError(getObjectName(), resolveMessageCodes(errorCode), errorArgs, defaultMessage));
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void rejectValue(String field, String errorCode, Object[] errorArgs, String defaultMessage)
/*  53:    */   {
/*  54: 97 */     if (("".equals(getNestedPath())) && (!StringUtils.hasLength(field)))
/*  55:    */     {
/*  56:101 */       reject(errorCode, errorArgs, defaultMessage);
/*  57:102 */       return;
/*  58:    */     }
/*  59:104 */     String fixedField = fixedField(field);
/*  60:105 */     Object newVal = getActualFieldValue(fixedField);
/*  61:106 */     FieldError fe = new FieldError(
/*  62:107 */       getObjectName(), fixedField, newVal, false, 
/*  63:108 */       resolveMessageCodes(errorCode, field), errorArgs, defaultMessage);
/*  64:109 */     addError(fe);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void addError(ObjectError error)
/*  68:    */   {
/*  69:113 */     this.errors.add(error);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void addAllErrors(Errors errors)
/*  73:    */   {
/*  74:117 */     if (!errors.getObjectName().equals(getObjectName())) {
/*  75:118 */       throw new IllegalArgumentException("Errors object needs to have same object name");
/*  76:    */     }
/*  77:120 */     this.errors.addAll(errors.getAllErrors());
/*  78:    */   }
/*  79:    */   
/*  80:    */   public String[] resolveMessageCodes(String errorCode)
/*  81:    */   {
/*  82:131 */     return getMessageCodesResolver().resolveMessageCodes(errorCode, getObjectName());
/*  83:    */   }
/*  84:    */   
/*  85:    */   public String[] resolveMessageCodes(String errorCode, String field)
/*  86:    */   {
/*  87:135 */     Class<?> fieldType = getFieldType(field);
/*  88:136 */     return getMessageCodesResolver().resolveMessageCodes(
/*  89:137 */       errorCode, getObjectName(), fixedField(field), fieldType);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public boolean hasErrors()
/*  93:    */   {
/*  94:143 */     return !this.errors.isEmpty();
/*  95:    */   }
/*  96:    */   
/*  97:    */   public int getErrorCount()
/*  98:    */   {
/*  99:148 */     return this.errors.size();
/* 100:    */   }
/* 101:    */   
/* 102:    */   public List<ObjectError> getAllErrors()
/* 103:    */   {
/* 104:153 */     return Collections.unmodifiableList(this.errors);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public List<ObjectError> getGlobalErrors()
/* 108:    */   {
/* 109:157 */     List<ObjectError> result = new LinkedList();
/* 110:158 */     for (ObjectError objectError : this.errors) {
/* 111:159 */       if (!(objectError instanceof FieldError)) {
/* 112:160 */         result.add(objectError);
/* 113:    */       }
/* 114:    */     }
/* 115:163 */     return Collections.unmodifiableList(result);
/* 116:    */   }
/* 117:    */   
/* 118:    */   public ObjectError getGlobalError()
/* 119:    */   {
/* 120:168 */     for (ObjectError objectError : this.errors) {
/* 121:169 */       if (!(objectError instanceof FieldError)) {
/* 122:170 */         return objectError;
/* 123:    */       }
/* 124:    */     }
/* 125:173 */     return null;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public List<FieldError> getFieldErrors()
/* 129:    */   {
/* 130:177 */     List<FieldError> result = new LinkedList();
/* 131:178 */     for (ObjectError objectError : this.errors) {
/* 132:179 */       if ((objectError instanceof FieldError)) {
/* 133:180 */         result.add((FieldError)objectError);
/* 134:    */       }
/* 135:    */     }
/* 136:183 */     return Collections.unmodifiableList(result);
/* 137:    */   }
/* 138:    */   
/* 139:    */   public FieldError getFieldError()
/* 140:    */   {
/* 141:188 */     for (ObjectError objectError : this.errors) {
/* 142:189 */       if ((objectError instanceof FieldError)) {
/* 143:190 */         return (FieldError)objectError;
/* 144:    */       }
/* 145:    */     }
/* 146:193 */     return null;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public List<FieldError> getFieldErrors(String field)
/* 150:    */   {
/* 151:198 */     List<FieldError> result = new LinkedList();
/* 152:199 */     String fixedField = fixedField(field);
/* 153:200 */     for (ObjectError objectError : this.errors) {
/* 154:201 */       if (((objectError instanceof FieldError)) && (isMatchingFieldError(fixedField, (FieldError)objectError))) {
/* 155:202 */         result.add((FieldError)objectError);
/* 156:    */       }
/* 157:    */     }
/* 158:205 */     return Collections.unmodifiableList(result);
/* 159:    */   }
/* 160:    */   
/* 161:    */   public FieldError getFieldError(String field)
/* 162:    */   {
/* 163:210 */     String fixedField = fixedField(field);
/* 164:211 */     for (ObjectError objectError : this.errors) {
/* 165:212 */       if ((objectError instanceof FieldError))
/* 166:    */       {
/* 167:213 */         FieldError fieldError = (FieldError)objectError;
/* 168:214 */         if (isMatchingFieldError(fixedField, fieldError)) {
/* 169:215 */           return fieldError;
/* 170:    */         }
/* 171:    */       }
/* 172:    */     }
/* 173:219 */     return null;
/* 174:    */   }
/* 175:    */   
/* 176:    */   public Object getFieldValue(String field)
/* 177:    */   {
/* 178:223 */     FieldError fieldError = getFieldError(field);
/* 179:    */     
/* 180:225 */     Object value = fieldError != null ? fieldError.getRejectedValue() : 
/* 181:226 */       getActualFieldValue(fixedField(field));
/* 182:228 */     if ((fieldError == null) || (!fieldError.isBindingFailure())) {
/* 183:229 */       value = formatFieldValue(field, value);
/* 184:    */     }
/* 185:231 */     return value;
/* 186:    */   }
/* 187:    */   
/* 188:    */   public Class<?> getFieldType(String field)
/* 189:    */   {
/* 190:242 */     Object value = getActualFieldValue(fixedField(field));
/* 191:243 */     if (value != null) {
/* 192:244 */       return value.getClass();
/* 193:    */     }
/* 194:246 */     return null;
/* 195:    */   }
/* 196:    */   
/* 197:    */   public Map<String, Object> getModel()
/* 198:    */   {
/* 199:273 */     Map<String, Object> model = new LinkedHashMap(2);
/* 200:    */     
/* 201:275 */     model.put(getObjectName(), getTarget());
/* 202:    */     
/* 203:277 */     model.put(MODEL_KEY_PREFIX + getObjectName(), this);
/* 204:278 */     return model;
/* 205:    */   }
/* 206:    */   
/* 207:    */   public Object getRawFieldValue(String field)
/* 208:    */   {
/* 209:282 */     return getActualFieldValue(fixedField(field));
/* 210:    */   }
/* 211:    */   
/* 212:    */   public PropertyEditor findEditor(String field, Class<?> valueType)
/* 213:    */   {
/* 214:291 */     PropertyEditorRegistry editorRegistry = getPropertyEditorRegistry();
/* 215:292 */     if (editorRegistry != null)
/* 216:    */     {
/* 217:293 */       Class<?> valueTypeToUse = valueType;
/* 218:294 */       if (valueTypeToUse == null) {
/* 219:295 */         valueTypeToUse = getFieldType(field);
/* 220:    */       }
/* 221:297 */       return editorRegistry.findCustomEditor(valueTypeToUse, fixedField(field));
/* 222:    */     }
/* 223:300 */     return null;
/* 224:    */   }
/* 225:    */   
/* 226:    */   public PropertyEditorRegistry getPropertyEditorRegistry()
/* 227:    */   {
/* 228:308 */     return null;
/* 229:    */   }
/* 230:    */   
/* 231:    */   public void recordSuppressedField(String field)
/* 232:    */   {
/* 233:318 */     this.suppressedFields.add(field);
/* 234:    */   }
/* 235:    */   
/* 236:    */   public String[] getSuppressedFields()
/* 237:    */   {
/* 238:328 */     return StringUtils.toStringArray(this.suppressedFields);
/* 239:    */   }
/* 240:    */   
/* 241:    */   public boolean equals(Object other)
/* 242:    */   {
/* 243:334 */     if (this == other) {
/* 244:335 */       return true;
/* 245:    */     }
/* 246:337 */     if (!(other instanceof BindingResult)) {
/* 247:338 */       return false;
/* 248:    */     }
/* 249:340 */     BindingResult otherResult = (BindingResult)other;
/* 250:    */     
/* 251:    */ 
/* 252:343 */     return (getObjectName().equals(otherResult.getObjectName())) && (ObjectUtils.nullSafeEquals(getTarget(), otherResult.getTarget())) && (getAllErrors().equals(otherResult.getAllErrors()));
/* 253:    */   }
/* 254:    */   
/* 255:    */   public int hashCode()
/* 256:    */   {
/* 257:348 */     return getObjectName().hashCode();
/* 258:    */   }
/* 259:    */   
/* 260:    */   public abstract Object getTarget();
/* 261:    */   
/* 262:    */   protected abstract Object getActualFieldValue(String paramString);
/* 263:    */   
/* 264:    */   protected Object formatFieldValue(String field, Object value)
/* 265:    */   {
/* 266:377 */     return value;
/* 267:    */   }
/* 268:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.AbstractBindingResult
 * JD-Core Version:    0.7.0.1
 */