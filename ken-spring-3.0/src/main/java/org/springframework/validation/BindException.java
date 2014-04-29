/*   1:    */ package org.springframework.validation;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditor;
/*   4:    */ import java.util.List;
/*   5:    */ import java.util.Map;
/*   6:    */ import org.springframework.beans.PropertyEditorRegistry;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ 
/*   9:    */ public class BindException
/*  10:    */   extends Exception
/*  11:    */   implements BindingResult
/*  12:    */ {
/*  13:    */   private final BindingResult bindingResult;
/*  14:    */   
/*  15:    */   public BindException(BindingResult bindingResult)
/*  16:    */   {
/*  17: 54 */     Assert.notNull(bindingResult, "BindingResult must not be null");
/*  18: 55 */     this.bindingResult = bindingResult;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public BindException(Object target, String objectName)
/*  22:    */   {
/*  23: 65 */     Assert.notNull(target, "Target object must not be null");
/*  24: 66 */     this.bindingResult = new BeanPropertyBindingResult(target, objectName);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public final BindingResult getBindingResult()
/*  28:    */   {
/*  29: 76 */     return this.bindingResult;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public String getObjectName()
/*  33:    */   {
/*  34: 81 */     return this.bindingResult.getObjectName();
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setNestedPath(String nestedPath)
/*  38:    */   {
/*  39: 85 */     this.bindingResult.setNestedPath(nestedPath);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public String getNestedPath()
/*  43:    */   {
/*  44: 89 */     return this.bindingResult.getNestedPath();
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void pushNestedPath(String subPath)
/*  48:    */   {
/*  49: 93 */     this.bindingResult.pushNestedPath(subPath);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void popNestedPath()
/*  53:    */     throws IllegalStateException
/*  54:    */   {
/*  55: 97 */     this.bindingResult.popNestedPath();
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void reject(String errorCode)
/*  59:    */   {
/*  60:102 */     this.bindingResult.reject(errorCode);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void reject(String errorCode, String defaultMessage)
/*  64:    */   {
/*  65:106 */     this.bindingResult.reject(errorCode, defaultMessage);
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void reject(String errorCode, Object[] errorArgs, String defaultMessage)
/*  69:    */   {
/*  70:110 */     this.bindingResult.reject(errorCode, errorArgs, defaultMessage);
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void rejectValue(String field, String errorCode)
/*  74:    */   {
/*  75:114 */     this.bindingResult.rejectValue(field, errorCode);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void rejectValue(String field, String errorCode, String defaultMessage)
/*  79:    */   {
/*  80:118 */     this.bindingResult.rejectValue(field, errorCode, defaultMessage);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void rejectValue(String field, String errorCode, Object[] errorArgs, String defaultMessage)
/*  84:    */   {
/*  85:122 */     this.bindingResult.rejectValue(field, errorCode, errorArgs, defaultMessage);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void addAllErrors(Errors errors)
/*  89:    */   {
/*  90:126 */     this.bindingResult.addAllErrors(errors);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public boolean hasErrors()
/*  94:    */   {
/*  95:131 */     return this.bindingResult.hasErrors();
/*  96:    */   }
/*  97:    */   
/*  98:    */   public int getErrorCount()
/*  99:    */   {
/* 100:135 */     return this.bindingResult.getErrorCount();
/* 101:    */   }
/* 102:    */   
/* 103:    */   public List<ObjectError> getAllErrors()
/* 104:    */   {
/* 105:139 */     return this.bindingResult.getAllErrors();
/* 106:    */   }
/* 107:    */   
/* 108:    */   public boolean hasGlobalErrors()
/* 109:    */   {
/* 110:143 */     return this.bindingResult.hasGlobalErrors();
/* 111:    */   }
/* 112:    */   
/* 113:    */   public int getGlobalErrorCount()
/* 114:    */   {
/* 115:147 */     return this.bindingResult.getGlobalErrorCount();
/* 116:    */   }
/* 117:    */   
/* 118:    */   public List<ObjectError> getGlobalErrors()
/* 119:    */   {
/* 120:151 */     return this.bindingResult.getGlobalErrors();
/* 121:    */   }
/* 122:    */   
/* 123:    */   public ObjectError getGlobalError()
/* 124:    */   {
/* 125:155 */     return this.bindingResult.getGlobalError();
/* 126:    */   }
/* 127:    */   
/* 128:    */   public boolean hasFieldErrors()
/* 129:    */   {
/* 130:159 */     return this.bindingResult.hasFieldErrors();
/* 131:    */   }
/* 132:    */   
/* 133:    */   public int getFieldErrorCount()
/* 134:    */   {
/* 135:163 */     return this.bindingResult.getFieldErrorCount();
/* 136:    */   }
/* 137:    */   
/* 138:    */   public List<FieldError> getFieldErrors()
/* 139:    */   {
/* 140:167 */     return this.bindingResult.getFieldErrors();
/* 141:    */   }
/* 142:    */   
/* 143:    */   public FieldError getFieldError()
/* 144:    */   {
/* 145:171 */     return this.bindingResult.getFieldError();
/* 146:    */   }
/* 147:    */   
/* 148:    */   public boolean hasFieldErrors(String field)
/* 149:    */   {
/* 150:175 */     return this.bindingResult.hasFieldErrors(field);
/* 151:    */   }
/* 152:    */   
/* 153:    */   public int getFieldErrorCount(String field)
/* 154:    */   {
/* 155:179 */     return this.bindingResult.getFieldErrorCount(field);
/* 156:    */   }
/* 157:    */   
/* 158:    */   public List<FieldError> getFieldErrors(String field)
/* 159:    */   {
/* 160:183 */     return this.bindingResult.getFieldErrors(field);
/* 161:    */   }
/* 162:    */   
/* 163:    */   public FieldError getFieldError(String field)
/* 164:    */   {
/* 165:187 */     return this.bindingResult.getFieldError(field);
/* 166:    */   }
/* 167:    */   
/* 168:    */   public Object getFieldValue(String field)
/* 169:    */   {
/* 170:191 */     return this.bindingResult.getFieldValue(field);
/* 171:    */   }
/* 172:    */   
/* 173:    */   public Class<?> getFieldType(String field)
/* 174:    */   {
/* 175:195 */     return this.bindingResult.getFieldType(field);
/* 176:    */   }
/* 177:    */   
/* 178:    */   public Object getTarget()
/* 179:    */   {
/* 180:199 */     return this.bindingResult.getTarget();
/* 181:    */   }
/* 182:    */   
/* 183:    */   public Map<String, Object> getModel()
/* 184:    */   {
/* 185:203 */     return this.bindingResult.getModel();
/* 186:    */   }
/* 187:    */   
/* 188:    */   public Object getRawFieldValue(String field)
/* 189:    */   {
/* 190:207 */     return this.bindingResult.getRawFieldValue(field);
/* 191:    */   }
/* 192:    */   
/* 193:    */   public PropertyEditor findEditor(String field, Class valueType)
/* 194:    */   {
/* 195:212 */     return this.bindingResult.findEditor(field, valueType);
/* 196:    */   }
/* 197:    */   
/* 198:    */   public PropertyEditorRegistry getPropertyEditorRegistry()
/* 199:    */   {
/* 200:216 */     return this.bindingResult.getPropertyEditorRegistry();
/* 201:    */   }
/* 202:    */   
/* 203:    */   public void addError(ObjectError error)
/* 204:    */   {
/* 205:220 */     this.bindingResult.addError(error);
/* 206:    */   }
/* 207:    */   
/* 208:    */   public String[] resolveMessageCodes(String errorCode, String field)
/* 209:    */   {
/* 210:224 */     return this.bindingResult.resolveMessageCodes(errorCode, field);
/* 211:    */   }
/* 212:    */   
/* 213:    */   public void recordSuppressedField(String field)
/* 214:    */   {
/* 215:228 */     this.bindingResult.recordSuppressedField(field);
/* 216:    */   }
/* 217:    */   
/* 218:    */   public String[] getSuppressedFields()
/* 219:    */   {
/* 220:232 */     return this.bindingResult.getSuppressedFields();
/* 221:    */   }
/* 222:    */   
/* 223:    */   public String getMessage()
/* 224:    */   {
/* 225:241 */     return this.bindingResult.toString();
/* 226:    */   }
/* 227:    */   
/* 228:    */   public boolean equals(Object other)
/* 229:    */   {
/* 230:246 */     return (this == other) || (this.bindingResult.equals(other));
/* 231:    */   }
/* 232:    */   
/* 233:    */   public int hashCode()
/* 234:    */   {
/* 235:251 */     return this.bindingResult.hashCode();
/* 236:    */   }
/* 237:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.BindException
 * JD-Core Version:    0.7.0.1
 */