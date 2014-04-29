/*   1:    */ package org.springframework.validation;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.EmptyStackException;
/*   6:    */ import java.util.LinkedList;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Stack;
/*   9:    */ import org.springframework.util.StringUtils;
/*  10:    */ 
/*  11:    */ public abstract class AbstractErrors
/*  12:    */   implements Errors, Serializable
/*  13:    */ {
/*  14: 39 */   private String nestedPath = "";
/*  15: 41 */   private final Stack<String> nestedPathStack = new Stack();
/*  16:    */   
/*  17:    */   public void setNestedPath(String nestedPath)
/*  18:    */   {
/*  19: 45 */     doSetNestedPath(nestedPath);
/*  20: 46 */     this.nestedPathStack.clear();
/*  21:    */   }
/*  22:    */   
/*  23:    */   public String getNestedPath()
/*  24:    */   {
/*  25: 50 */     return this.nestedPath;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void pushNestedPath(String subPath)
/*  29:    */   {
/*  30: 54 */     this.nestedPathStack.push(getNestedPath());
/*  31: 55 */     doSetNestedPath(getNestedPath() + subPath);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void popNestedPath()
/*  35:    */     throws IllegalArgumentException
/*  36:    */   {
/*  37:    */     try
/*  38:    */     {
/*  39: 60 */       String formerNestedPath = (String)this.nestedPathStack.pop();
/*  40: 61 */       doSetNestedPath(formerNestedPath);
/*  41:    */     }
/*  42:    */     catch (EmptyStackException localEmptyStackException)
/*  43:    */     {
/*  44: 64 */       throw new IllegalStateException("Cannot pop nested path: no nested path on stack");
/*  45:    */     }
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected void doSetNestedPath(String nestedPath)
/*  49:    */   {
/*  50: 73 */     if (nestedPath == null) {
/*  51: 74 */       nestedPath = "";
/*  52:    */     }
/*  53: 76 */     nestedPath = canonicalFieldName(nestedPath);
/*  54: 77 */     if ((nestedPath.length() > 0) && (!nestedPath.endsWith("."))) {
/*  55: 78 */       nestedPath = nestedPath + ".";
/*  56:    */     }
/*  57: 80 */     this.nestedPath = nestedPath;
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected String fixedField(String field)
/*  61:    */   {
/*  62: 88 */     if (StringUtils.hasLength(field)) {
/*  63: 89 */       return getNestedPath() + canonicalFieldName(field);
/*  64:    */     }
/*  65: 92 */     String path = getNestedPath();
/*  66: 93 */     return path.endsWith(".") ? 
/*  67: 94 */       path.substring(0, path.length() - ".".length()) : path;
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected String canonicalFieldName(String field)
/*  71:    */   {
/*  72:105 */     return field;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void reject(String errorCode)
/*  76:    */   {
/*  77:110 */     reject(errorCode, null, null);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void reject(String errorCode, String defaultMessage)
/*  81:    */   {
/*  82:114 */     reject(errorCode, null, defaultMessage);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void rejectValue(String field, String errorCode)
/*  86:    */   {
/*  87:118 */     rejectValue(field, errorCode, null, null);
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void rejectValue(String field, String errorCode, String defaultMessage)
/*  91:    */   {
/*  92:122 */     rejectValue(field, errorCode, null, defaultMessage);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public boolean hasErrors()
/*  96:    */   {
/*  97:127 */     return !getAllErrors().isEmpty();
/*  98:    */   }
/*  99:    */   
/* 100:    */   public int getErrorCount()
/* 101:    */   {
/* 102:131 */     return getAllErrors().size();
/* 103:    */   }
/* 104:    */   
/* 105:    */   public List<ObjectError> getAllErrors()
/* 106:    */   {
/* 107:135 */     List<ObjectError> result = new LinkedList();
/* 108:136 */     result.addAll(getGlobalErrors());
/* 109:137 */     result.addAll(getFieldErrors());
/* 110:138 */     return Collections.unmodifiableList(result);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public boolean hasGlobalErrors()
/* 114:    */   {
/* 115:142 */     return getGlobalErrorCount() > 0;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public int getGlobalErrorCount()
/* 119:    */   {
/* 120:146 */     return getGlobalErrors().size();
/* 121:    */   }
/* 122:    */   
/* 123:    */   public ObjectError getGlobalError()
/* 124:    */   {
/* 125:150 */     List<ObjectError> globalErrors = getGlobalErrors();
/* 126:151 */     return !globalErrors.isEmpty() ? (ObjectError)globalErrors.get(0) : null;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public boolean hasFieldErrors()
/* 130:    */   {
/* 131:155 */     return getFieldErrorCount() > 0;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public int getFieldErrorCount()
/* 135:    */   {
/* 136:159 */     return getFieldErrors().size();
/* 137:    */   }
/* 138:    */   
/* 139:    */   public FieldError getFieldError()
/* 140:    */   {
/* 141:163 */     List<FieldError> fieldErrors = getFieldErrors();
/* 142:164 */     return !fieldErrors.isEmpty() ? (FieldError)fieldErrors.get(0) : null;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public boolean hasFieldErrors(String field)
/* 146:    */   {
/* 147:168 */     return getFieldErrorCount(field) > 0;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public int getFieldErrorCount(String field)
/* 151:    */   {
/* 152:172 */     return getFieldErrors(field).size();
/* 153:    */   }
/* 154:    */   
/* 155:    */   public List<FieldError> getFieldErrors(String field)
/* 156:    */   {
/* 157:176 */     List<FieldError> fieldErrors = getFieldErrors();
/* 158:177 */     List<FieldError> result = new LinkedList();
/* 159:178 */     String fixedField = fixedField(field);
/* 160:179 */     for (FieldError error : fieldErrors) {
/* 161:180 */       if (isMatchingFieldError(fixedField, error)) {
/* 162:181 */         result.add(error);
/* 163:    */       }
/* 164:    */     }
/* 165:184 */     return Collections.unmodifiableList(result);
/* 166:    */   }
/* 167:    */   
/* 168:    */   public FieldError getFieldError(String field)
/* 169:    */   {
/* 170:188 */     List<FieldError> fieldErrors = getFieldErrors(field);
/* 171:189 */     return !fieldErrors.isEmpty() ? (FieldError)fieldErrors.get(0) : null;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public Class<?> getFieldType(String field)
/* 175:    */   {
/* 176:194 */     Object value = getFieldValue(field);
/* 177:195 */     if (value != null) {
/* 178:196 */       return value.getClass();
/* 179:    */     }
/* 180:198 */     return null;
/* 181:    */   }
/* 182:    */   
/* 183:    */   protected boolean isMatchingFieldError(String field, FieldError fieldError)
/* 184:    */   {
/* 185:209 */     return (field.equals(fieldError.getField())) || ((field.endsWith("*")) && (fieldError.getField().startsWith(field.substring(0, field.length() - 1))));
/* 186:    */   }
/* 187:    */   
/* 188:    */   public String toString()
/* 189:    */   {
/* 190:215 */     StringBuilder sb = new StringBuilder(getClass().getName());
/* 191:216 */     sb.append(": ").append(getErrorCount()).append(" errors");
/* 192:217 */     for (ObjectError error : getAllErrors()) {
/* 193:218 */       sb.append('\n').append(error);
/* 194:    */     }
/* 195:220 */     return sb.toString();
/* 196:    */   }
/* 197:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.AbstractErrors
 * JD-Core Version:    0.7.0.1
 */