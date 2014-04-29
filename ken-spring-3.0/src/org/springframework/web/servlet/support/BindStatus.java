/*   1:    */ package org.springframework.web.servlet.support;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditor;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.List;
/*   6:    */ import org.springframework.beans.BeanWrapper;
/*   7:    */ import org.springframework.beans.PropertyAccessorFactory;
/*   8:    */ import org.springframework.context.NoSuchMessageException;
/*   9:    */ import org.springframework.util.StringUtils;
/*  10:    */ import org.springframework.validation.BindingResult;
/*  11:    */ import org.springframework.validation.Errors;
/*  12:    */ import org.springframework.validation.ObjectError;
/*  13:    */ import org.springframework.web.util.HtmlUtils;
/*  14:    */ 
/*  15:    */ public class BindStatus
/*  16:    */ {
/*  17:    */   private final RequestContext requestContext;
/*  18:    */   private final String path;
/*  19:    */   private final boolean htmlEscape;
/*  20:    */   private final String expression;
/*  21:    */   private final Errors errors;
/*  22:    */   private BindingResult bindingResult;
/*  23:    */   private Object value;
/*  24:    */   private Class valueType;
/*  25:    */   private Object actualValue;
/*  26:    */   private PropertyEditor editor;
/*  27:    */   private List objectErrors;
/*  28:    */   private String[] errorCodes;
/*  29:    */   private String[] errorMessages;
/*  30:    */   
/*  31:    */   public BindStatus(RequestContext requestContext, String path, boolean htmlEscape)
/*  32:    */     throws IllegalStateException
/*  33:    */   {
/*  34: 88 */     this.requestContext = requestContext;
/*  35: 89 */     this.path = path;
/*  36: 90 */     this.htmlEscape = htmlEscape;
/*  37:    */     
/*  38:    */ 
/*  39: 93 */     String beanName = null;
/*  40: 94 */     int dotPos = path.indexOf('.');
/*  41: 95 */     if (dotPos == -1)
/*  42:    */     {
/*  43: 97 */       beanName = path;
/*  44: 98 */       this.expression = null;
/*  45:    */     }
/*  46:    */     else
/*  47:    */     {
/*  48:101 */       beanName = path.substring(0, dotPos);
/*  49:102 */       this.expression = path.substring(dotPos + 1);
/*  50:    */     }
/*  51:105 */     this.errors = requestContext.getErrors(beanName, false);
/*  52:107 */     if (this.errors != null)
/*  53:    */     {
/*  54:111 */       if (this.expression != null)
/*  55:    */       {
/*  56:112 */         if ("*".equals(this.expression))
/*  57:    */         {
/*  58:113 */           this.objectErrors = this.errors.getAllErrors();
/*  59:    */         }
/*  60:115 */         else if (this.expression.endsWith("*"))
/*  61:    */         {
/*  62:116 */           this.objectErrors = this.errors.getFieldErrors(this.expression);
/*  63:    */         }
/*  64:    */         else
/*  65:    */         {
/*  66:119 */           this.objectErrors = this.errors.getFieldErrors(this.expression);
/*  67:120 */           this.value = this.errors.getFieldValue(this.expression);
/*  68:121 */           this.valueType = this.errors.getFieldType(this.expression);
/*  69:122 */           if ((this.errors instanceof BindingResult))
/*  70:    */           {
/*  71:123 */             this.bindingResult = ((BindingResult)this.errors);
/*  72:124 */             this.actualValue = this.bindingResult.getRawFieldValue(this.expression);
/*  73:125 */             this.editor = this.bindingResult.findEditor(this.expression, null);
/*  74:    */           }
/*  75:    */         }
/*  76:    */       }
/*  77:    */       else {
/*  78:130 */         this.objectErrors = this.errors.getGlobalErrors();
/*  79:    */       }
/*  80:132 */       initErrorCodes();
/*  81:    */     }
/*  82:    */     else
/*  83:    */     {
/*  84:139 */       Object target = requestContext.getModelObject(beanName);
/*  85:140 */       if (target == null) {
/*  86:141 */         throw new IllegalStateException("Neither BindingResult nor plain target object for bean name '" + 
/*  87:142 */           beanName + "' available as request attribute");
/*  88:    */       }
/*  89:144 */       if ((this.expression != null) && (!"*".equals(this.expression)) && (!this.expression.endsWith("*")))
/*  90:    */       {
/*  91:145 */         BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(target);
/*  92:146 */         this.valueType = bw.getPropertyType(this.expression);
/*  93:147 */         this.value = bw.getPropertyValue(this.expression);
/*  94:    */       }
/*  95:149 */       this.errorCodes = new String[0];
/*  96:150 */       this.errorMessages = new String[0];
/*  97:    */     }
/*  98:153 */     if ((htmlEscape) && ((this.value instanceof String))) {
/*  99:154 */       this.value = HtmlUtils.htmlEscape((String)this.value);
/* 100:    */     }
/* 101:    */   }
/* 102:    */   
/* 103:    */   private void initErrorCodes()
/* 104:    */   {
/* 105:162 */     this.errorCodes = new String[this.objectErrors.size()];
/* 106:163 */     for (int i = 0; i < this.objectErrors.size(); i++)
/* 107:    */     {
/* 108:164 */       ObjectError error = (ObjectError)this.objectErrors.get(i);
/* 109:165 */       this.errorCodes[i] = error.getCode();
/* 110:    */     }
/* 111:    */   }
/* 112:    */   
/* 113:    */   private void initErrorMessages()
/* 114:    */     throws NoSuchMessageException
/* 115:    */   {
/* 116:173 */     if (this.errorMessages == null)
/* 117:    */     {
/* 118:174 */       this.errorMessages = new String[this.objectErrors.size()];
/* 119:175 */       for (int i = 0; i < this.objectErrors.size(); i++)
/* 120:    */       {
/* 121:176 */         ObjectError error = (ObjectError)this.objectErrors.get(i);
/* 122:177 */         this.errorMessages[i] = this.requestContext.getMessage(error, this.htmlEscape);
/* 123:    */       }
/* 124:    */     }
/* 125:    */   }
/* 126:    */   
/* 127:    */   public String getPath()
/* 128:    */   {
/* 129:188 */     return this.path;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public String getExpression()
/* 133:    */   {
/* 134:199 */     return this.expression;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public Object getValue()
/* 138:    */   {
/* 139:209 */     return this.value;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public Class getValueType()
/* 143:    */   {
/* 144:218 */     return this.valueType;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public Object getActualValue()
/* 148:    */   {
/* 149:226 */     return this.actualValue;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public String getDisplayValue()
/* 153:    */   {
/* 154:237 */     if ((this.value instanceof String)) {
/* 155:238 */       return (String)this.value;
/* 156:    */     }
/* 157:240 */     if (this.value != null) {
/* 158:241 */       return this.htmlEscape ? HtmlUtils.htmlEscape(this.value.toString()) : this.value.toString();
/* 159:    */     }
/* 160:243 */     return "";
/* 161:    */   }
/* 162:    */   
/* 163:    */   public boolean isError()
/* 164:    */   {
/* 165:250 */     return (this.errorCodes != null) && (this.errorCodes.length > 0);
/* 166:    */   }
/* 167:    */   
/* 168:    */   public String[] getErrorCodes()
/* 169:    */   {
/* 170:258 */     return this.errorCodes;
/* 171:    */   }
/* 172:    */   
/* 173:    */   public String getErrorCode()
/* 174:    */   {
/* 175:265 */     return this.errorCodes.length > 0 ? this.errorCodes[0] : "";
/* 176:    */   }
/* 177:    */   
/* 178:    */   public String[] getErrorMessages()
/* 179:    */   {
/* 180:273 */     initErrorMessages();
/* 181:274 */     return this.errorMessages;
/* 182:    */   }
/* 183:    */   
/* 184:    */   public String getErrorMessage()
/* 185:    */   {
/* 186:281 */     initErrorMessages();
/* 187:282 */     return this.errorMessages.length > 0 ? this.errorMessages[0] : "";
/* 188:    */   }
/* 189:    */   
/* 190:    */   public String getErrorMessagesAsString(String delimiter)
/* 191:    */   {
/* 192:292 */     initErrorMessages();
/* 193:293 */     return StringUtils.arrayToDelimitedString(this.errorMessages, delimiter);
/* 194:    */   }
/* 195:    */   
/* 196:    */   public Errors getErrors()
/* 197:    */   {
/* 198:303 */     return this.errors;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public PropertyEditor getEditor()
/* 202:    */   {
/* 203:312 */     return this.editor;
/* 204:    */   }
/* 205:    */   
/* 206:    */   public PropertyEditor findEditor(Class valueClass)
/* 207:    */   {
/* 208:322 */     return this.bindingResult != null ? this.bindingResult.findEditor(this.expression, valueClass) : null;
/* 209:    */   }
/* 210:    */   
/* 211:    */   public String toString()
/* 212:    */   {
/* 213:328 */     StringBuilder sb = new StringBuilder("BindStatus: ");
/* 214:329 */     sb.append("expression=[").append(this.expression).append("]; ");
/* 215:330 */     sb.append("value=[").append(this.value).append("]");
/* 216:331 */     if (isError()) {
/* 217:332 */       sb.append("; errorCodes=").append(Arrays.asList(this.errorCodes));
/* 218:    */     }
/* 219:334 */     return sb.toString();
/* 220:    */   }
/* 221:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.support.BindStatus
 * JD-Core Version:    0.7.0.1
 */