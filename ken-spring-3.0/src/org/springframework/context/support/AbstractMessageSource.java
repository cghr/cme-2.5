/*   1:    */ package org.springframework.context.support;
/*   2:    */ 
/*   3:    */ import java.text.MessageFormat;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Locale;
/*   7:    */ import org.springframework.context.HierarchicalMessageSource;
/*   8:    */ import org.springframework.context.MessageSource;
/*   9:    */ import org.springframework.context.MessageSourceResolvable;
/*  10:    */ import org.springframework.context.NoSuchMessageException;
/*  11:    */ import org.springframework.util.ObjectUtils;
/*  12:    */ 
/*  13:    */ public abstract class AbstractMessageSource
/*  14:    */   extends MessageSourceSupport
/*  15:    */   implements HierarchicalMessageSource
/*  16:    */ {
/*  17:    */   private MessageSource parentMessageSource;
/*  18: 67 */   private boolean useCodeAsDefaultMessage = false;
/*  19:    */   
/*  20:    */   public void setParentMessageSource(MessageSource parent)
/*  21:    */   {
/*  22: 71 */     this.parentMessageSource = parent;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public MessageSource getParentMessageSource()
/*  26:    */   {
/*  27: 75 */     return this.parentMessageSource;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setUseCodeAsDefaultMessage(boolean useCodeAsDefaultMessage)
/*  31:    */   {
/*  32: 96 */     this.useCodeAsDefaultMessage = useCodeAsDefaultMessage;
/*  33:    */   }
/*  34:    */   
/*  35:    */   protected boolean isUseCodeAsDefaultMessage()
/*  36:    */   {
/*  37:108 */     return this.useCodeAsDefaultMessage;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public final String getMessage(String code, Object[] args, String defaultMessage, Locale locale)
/*  41:    */   {
/*  42:113 */     String msg = getMessageInternal(code, args, locale);
/*  43:114 */     if (msg != null) {
/*  44:115 */       return msg;
/*  45:    */     }
/*  46:117 */     if (defaultMessage == null)
/*  47:    */     {
/*  48:118 */       String fallback = getDefaultMessage(code);
/*  49:119 */       if (fallback != null) {
/*  50:120 */         return fallback;
/*  51:    */       }
/*  52:    */     }
/*  53:123 */     return renderDefaultMessage(defaultMessage, args, locale);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public final String getMessage(String code, Object[] args, Locale locale)
/*  57:    */     throws NoSuchMessageException
/*  58:    */   {
/*  59:127 */     String msg = getMessageInternal(code, args, locale);
/*  60:128 */     if (msg != null) {
/*  61:129 */       return msg;
/*  62:    */     }
/*  63:131 */     String fallback = getDefaultMessage(code);
/*  64:132 */     if (fallback != null) {
/*  65:133 */       return fallback;
/*  66:    */     }
/*  67:135 */     throw new NoSuchMessageException(code, locale);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public final String getMessage(MessageSourceResolvable resolvable, Locale locale)
/*  71:    */     throws NoSuchMessageException
/*  72:    */   {
/*  73:141 */     String[] codes = resolvable.getCodes();
/*  74:142 */     if (codes == null) {
/*  75:143 */       codes = new String[0];
/*  76:    */     }
/*  77:145 */     for (String code : codes)
/*  78:    */     {
/*  79:146 */       String msg = getMessageInternal(code, resolvable.getArguments(), locale);
/*  80:147 */       if (msg != null) {
/*  81:148 */         return msg;
/*  82:    */       }
/*  83:    */     }
/*  84:151 */     String defaultMessage = resolvable.getDefaultMessage();
/*  85:152 */     if (defaultMessage != null) {
/*  86:153 */       return renderDefaultMessage(defaultMessage, resolvable.getArguments(), locale);
/*  87:    */     }
/*  88:155 */     if (codes.length > 0)
/*  89:    */     {
/*  90:156 */       String fallback = getDefaultMessage(codes[0]);
/*  91:157 */       if (fallback != null) {
/*  92:158 */         return fallback;
/*  93:    */       }
/*  94:    */     }
/*  95:161 */     throw new NoSuchMessageException(codes.length > 0 ? codes[(codes.length - 1)] : null, locale);
/*  96:    */   }
/*  97:    */   
/*  98:    */   protected String getMessageInternal(String code, Object[] args, Locale locale)
/*  99:    */   {
/* 100:180 */     if (code == null) {
/* 101:181 */       return null;
/* 102:    */     }
/* 103:183 */     if (locale == null) {
/* 104:184 */       locale = Locale.getDefault();
/* 105:    */     }
/* 106:186 */     Object[] argsToUse = args;
/* 107:188 */     if ((!isAlwaysUseMessageFormat()) && (ObjectUtils.isEmpty(args)))
/* 108:    */     {
/* 109:193 */       String message = resolveCodeWithoutArguments(code, locale);
/* 110:194 */       if (message != null) {
/* 111:195 */         return message;
/* 112:    */       }
/* 113:    */     }
/* 114:    */     else
/* 115:    */     {
/* 116:203 */       argsToUse = resolveArguments(args, locale);
/* 117:    */       
/* 118:205 */       MessageFormat messageFormat = resolveCode(code, locale);
/* 119:206 */       if (messageFormat != null) {
/* 120:207 */         synchronized (messageFormat)
/* 121:    */         {
/* 122:208 */           return messageFormat.format(argsToUse);
/* 123:    */         }
/* 124:    */       }
/* 125:    */     }
/* 126:214 */     return getMessageFromParent(code, argsToUse, locale);
/* 127:    */   }
/* 128:    */   
/* 129:    */   protected String getMessageFromParent(String code, Object[] args, Locale locale)
/* 130:    */   {
/* 131:227 */     MessageSource parent = getParentMessageSource();
/* 132:228 */     if (parent != null)
/* 133:    */     {
/* 134:229 */       if ((parent instanceof AbstractMessageSource)) {
/* 135:232 */         return ((AbstractMessageSource)parent).getMessageInternal(code, args, locale);
/* 136:    */       }
/* 137:236 */       return parent.getMessage(code, args, null, locale);
/* 138:    */     }
/* 139:240 */     return null;
/* 140:    */   }
/* 141:    */   
/* 142:    */   protected String getDefaultMessage(String code)
/* 143:    */   {
/* 144:254 */     if (isUseCodeAsDefaultMessage()) {
/* 145:255 */       return code;
/* 146:    */     }
/* 147:257 */     return null;
/* 148:    */   }
/* 149:    */   
/* 150:    */   protected Object[] resolveArguments(Object[] args, Locale locale)
/* 151:    */   {
/* 152:271 */     if (args == null) {
/* 153:272 */       return new Object[0];
/* 154:    */     }
/* 155:274 */     List<Object> resolvedArgs = new ArrayList(args.length);
/* 156:275 */     for (Object arg : args) {
/* 157:276 */       if ((arg instanceof MessageSourceResolvable)) {
/* 158:277 */         resolvedArgs.add(getMessage((MessageSourceResolvable)arg, locale));
/* 159:    */       } else {
/* 160:280 */         resolvedArgs.add(arg);
/* 161:    */       }
/* 162:    */     }
/* 163:283 */     return resolvedArgs.toArray(new Object[resolvedArgs.size()]);
/* 164:    */   }
/* 165:    */   
/* 166:    */   protected String resolveCodeWithoutArguments(String code, Locale locale)
/* 167:    */   {
/* 168:304 */     MessageFormat messageFormat = resolveCode(code, locale);
/* 169:305 */     if (messageFormat != null) {
/* 170:306 */       synchronized (messageFormat)
/* 171:    */       {
/* 172:307 */         return messageFormat.format(new Object[0]);
/* 173:    */       }
/* 174:    */     }
/* 175:310 */     return null;
/* 176:    */   }
/* 177:    */   
/* 178:    */   protected abstract MessageFormat resolveCode(String paramString, Locale paramLocale);
/* 179:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.AbstractMessageSource
 * JD-Core Version:    0.7.0.1
 */