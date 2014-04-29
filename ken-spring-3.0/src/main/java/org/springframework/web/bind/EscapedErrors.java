/*   1:    */ package org.springframework.web.bind;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.springframework.validation.Errors;
/*   6:    */ import org.springframework.validation.FieldError;
/*   7:    */ import org.springframework.validation.ObjectError;
/*   8:    */ import org.springframework.web.util.HtmlUtils;
/*   9:    */ 
/*  10:    */ public class EscapedErrors
/*  11:    */   implements Errors
/*  12:    */ {
/*  13:    */   private final Errors source;
/*  14:    */   
/*  15:    */   public EscapedErrors(Errors source)
/*  16:    */   {
/*  17: 50 */     if (source == null) {
/*  18: 51 */       throw new IllegalArgumentException("Cannot wrap a null instance");
/*  19:    */     }
/*  20: 53 */     this.source = source;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public Errors getSource()
/*  24:    */   {
/*  25: 57 */     return this.source;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public String getObjectName()
/*  29:    */   {
/*  30: 62 */     return this.source.getObjectName();
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setNestedPath(String nestedPath)
/*  34:    */   {
/*  35: 66 */     this.source.setNestedPath(nestedPath);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public String getNestedPath()
/*  39:    */   {
/*  40: 70 */     return this.source.getNestedPath();
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void pushNestedPath(String subPath)
/*  44:    */   {
/*  45: 74 */     this.source.pushNestedPath(subPath);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void popNestedPath()
/*  49:    */     throws IllegalStateException
/*  50:    */   {
/*  51: 78 */     this.source.popNestedPath();
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void reject(String errorCode)
/*  55:    */   {
/*  56: 83 */     this.source.reject(errorCode);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void reject(String errorCode, String defaultMessage)
/*  60:    */   {
/*  61: 87 */     this.source.reject(errorCode, defaultMessage);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void reject(String errorCode, Object[] errorArgs, String defaultMessage)
/*  65:    */   {
/*  66: 91 */     this.source.reject(errorCode, errorArgs, defaultMessage);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void rejectValue(String field, String errorCode)
/*  70:    */   {
/*  71: 95 */     this.source.rejectValue(field, errorCode);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void rejectValue(String field, String errorCode, String defaultMessage)
/*  75:    */   {
/*  76: 99 */     this.source.rejectValue(field, errorCode, defaultMessage);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void rejectValue(String field, String errorCode, Object[] errorArgs, String defaultMessage)
/*  80:    */   {
/*  81:103 */     this.source.rejectValue(field, errorCode, errorArgs, defaultMessage);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void addAllErrors(Errors errors)
/*  85:    */   {
/*  86:107 */     this.source.addAllErrors(errors);
/*  87:    */   }
/*  88:    */   
/*  89:    */   public boolean hasErrors()
/*  90:    */   {
/*  91:112 */     return this.source.hasErrors();
/*  92:    */   }
/*  93:    */   
/*  94:    */   public int getErrorCount()
/*  95:    */   {
/*  96:116 */     return this.source.getErrorCount();
/*  97:    */   }
/*  98:    */   
/*  99:    */   public List<ObjectError> getAllErrors()
/* 100:    */   {
/* 101:120 */     return escapeObjectErrors(this.source.getAllErrors());
/* 102:    */   }
/* 103:    */   
/* 104:    */   public boolean hasGlobalErrors()
/* 105:    */   {
/* 106:124 */     return this.source.hasGlobalErrors();
/* 107:    */   }
/* 108:    */   
/* 109:    */   public int getGlobalErrorCount()
/* 110:    */   {
/* 111:128 */     return this.source.getGlobalErrorCount();
/* 112:    */   }
/* 113:    */   
/* 114:    */   public List<ObjectError> getGlobalErrors()
/* 115:    */   {
/* 116:132 */     return escapeObjectErrors(this.source.getGlobalErrors());
/* 117:    */   }
/* 118:    */   
/* 119:    */   public ObjectError getGlobalError()
/* 120:    */   {
/* 121:136 */     return escapeObjectError(this.source.getGlobalError());
/* 122:    */   }
/* 123:    */   
/* 124:    */   public boolean hasFieldErrors()
/* 125:    */   {
/* 126:140 */     return this.source.hasFieldErrors();
/* 127:    */   }
/* 128:    */   
/* 129:    */   public int getFieldErrorCount()
/* 130:    */   {
/* 131:144 */     return this.source.getFieldErrorCount();
/* 132:    */   }
/* 133:    */   
/* 134:    */   public List<FieldError> getFieldErrors()
/* 135:    */   {
/* 136:148 */     return this.source.getFieldErrors();
/* 137:    */   }
/* 138:    */   
/* 139:    */   public FieldError getFieldError()
/* 140:    */   {
/* 141:152 */     return this.source.getFieldError();
/* 142:    */   }
/* 143:    */   
/* 144:    */   public boolean hasFieldErrors(String field)
/* 145:    */   {
/* 146:156 */     return this.source.hasFieldErrors(field);
/* 147:    */   }
/* 148:    */   
/* 149:    */   public int getFieldErrorCount(String field)
/* 150:    */   {
/* 151:160 */     return this.source.getFieldErrorCount(field);
/* 152:    */   }
/* 153:    */   
/* 154:    */   public List<FieldError> getFieldErrors(String field)
/* 155:    */   {
/* 156:164 */     return escapeObjectErrors(this.source.getFieldErrors(field));
/* 157:    */   }
/* 158:    */   
/* 159:    */   public FieldError getFieldError(String field)
/* 160:    */   {
/* 161:168 */     return (FieldError)escapeObjectError(this.source.getFieldError(field));
/* 162:    */   }
/* 163:    */   
/* 164:    */   public Object getFieldValue(String field)
/* 165:    */   {
/* 166:172 */     Object value = this.source.getFieldValue(field);
/* 167:173 */     return (value instanceof String) ? HtmlUtils.htmlEscape((String)value) : value;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public Class getFieldType(String field)
/* 171:    */   {
/* 172:177 */     return this.source.getFieldType(field);
/* 173:    */   }
/* 174:    */   
/* 175:    */   private <T extends ObjectError> T escapeObjectError(T source)
/* 176:    */   {
/* 177:182 */     if (source == null) {
/* 178:183 */       return null;
/* 179:    */     }
/* 180:185 */     if ((source instanceof FieldError))
/* 181:    */     {
/* 182:186 */       FieldError fieldError = (FieldError)source;
/* 183:187 */       Object value = fieldError.getRejectedValue();
/* 184:188 */       if ((value instanceof String)) {
/* 185:189 */         value = HtmlUtils.htmlEscape((String)value);
/* 186:    */       }
/* 187:191 */       return new FieldError(
/* 188:192 */         fieldError.getObjectName(), fieldError.getField(), value, 
/* 189:193 */         fieldError.isBindingFailure(), fieldError.getCodes(), 
/* 190:194 */         fieldError.getArguments(), HtmlUtils.htmlEscape(fieldError.getDefaultMessage()));
/* 191:    */     }
/* 192:197 */     return new ObjectError(
/* 193:198 */       source.getObjectName(), source.getCodes(), source.getArguments(), 
/* 194:199 */       HtmlUtils.htmlEscape(source.getDefaultMessage()));
/* 195:    */   }
/* 196:    */   
/* 197:    */   private <T extends ObjectError> List<T> escapeObjectErrors(List<T> source)
/* 198:    */   {
/* 199:204 */     List<T> escaped = new ArrayList(source.size());
/* 200:205 */     for (T objectError : source) {
/* 201:206 */       escaped.add(escapeObjectError(objectError));
/* 202:    */     }
/* 203:208 */     return escaped;
/* 204:    */   }
/* 205:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.bind.EscapedErrors
 * JD-Core Version:    0.7.0.1
 */