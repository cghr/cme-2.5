/*   1:    */ package org.springframework.web.servlet.tags;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.Collection;
/*   5:    */ import javax.servlet.jsp.JspException;
/*   6:    */ import javax.servlet.jsp.JspTagException;
/*   7:    */ import javax.servlet.jsp.JspWriter;
/*   8:    */ import javax.servlet.jsp.PageContext;
/*   9:    */ import org.springframework.context.MessageSource;
/*  10:    */ import org.springframework.context.MessageSourceResolvable;
/*  11:    */ import org.springframework.context.NoSuchMessageException;
/*  12:    */ import org.springframework.util.ObjectUtils;
/*  13:    */ import org.springframework.util.StringUtils;
/*  14:    */ import org.springframework.web.servlet.support.RequestContext;
/*  15:    */ import org.springframework.web.util.ExpressionEvaluationUtils;
/*  16:    */ import org.springframework.web.util.HtmlUtils;
/*  17:    */ import org.springframework.web.util.JavaScriptUtils;
/*  18:    */ import org.springframework.web.util.TagUtils;
/*  19:    */ 
/*  20:    */ public class MessageTag
/*  21:    */   extends HtmlEscapingAwareTag
/*  22:    */ {
/*  23:    */   public static final String DEFAULT_ARGUMENT_SEPARATOR = ",";
/*  24:    */   private Object message;
/*  25:    */   private String code;
/*  26:    */   private Object arguments;
/*  27: 68 */   private String argumentSeparator = ",";
/*  28:    */   private String text;
/*  29:    */   private String var;
/*  30: 74 */   private String scope = "page";
/*  31: 76 */   private boolean javaScriptEscape = false;
/*  32:    */   
/*  33:    */   public void setMessage(Object message)
/*  34:    */   {
/*  35: 87 */     this.message = message;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setCode(String code)
/*  39:    */   {
/*  40: 94 */     this.code = code;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setArguments(Object arguments)
/*  44:    */   {
/*  45:103 */     this.arguments = arguments;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setArgumentSeparator(String argumentSeparator)
/*  49:    */   {
/*  50:112 */     this.argumentSeparator = argumentSeparator;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setText(String text)
/*  54:    */   {
/*  55:119 */     this.text = text;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setVar(String var)
/*  59:    */   {
/*  60:129 */     this.var = var;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setScope(String scope)
/*  64:    */   {
/*  65:140 */     this.scope = scope;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setJavaScriptEscape(String javaScriptEscape)
/*  69:    */     throws JspException
/*  70:    */   {
/*  71:148 */     this.javaScriptEscape = 
/*  72:149 */       ExpressionEvaluationUtils.evaluateBoolean("javaScriptEscape", javaScriptEscape, this.pageContext);
/*  73:    */   }
/*  74:    */   
/*  75:    */   protected final int doStartTagInternal()
/*  76:    */     throws JspException, IOException
/*  77:    */   {
/*  78:    */     try
/*  79:    */     {
/*  80:165 */       String msg = resolveMessage();
/*  81:    */       
/*  82:    */ 
/*  83:168 */       msg = isHtmlEscape() ? HtmlUtils.htmlEscape(msg) : msg;
/*  84:169 */       msg = this.javaScriptEscape ? JavaScriptUtils.javaScriptEscape(msg) : msg;
/*  85:    */       
/*  86:    */ 
/*  87:172 */       String resolvedVar = ExpressionEvaluationUtils.evaluateString("var", this.var, this.pageContext);
/*  88:173 */       if (resolvedVar != null)
/*  89:    */       {
/*  90:174 */         String resolvedScope = ExpressionEvaluationUtils.evaluateString("scope", this.scope, this.pageContext);
/*  91:175 */         this.pageContext.setAttribute(resolvedVar, msg, TagUtils.getScope(resolvedScope));
/*  92:    */       }
/*  93:    */       else
/*  94:    */       {
/*  95:178 */         writeMessage(msg);
/*  96:    */       }
/*  97:181 */       return 1;
/*  98:    */     }
/*  99:    */     catch (NoSuchMessageException ex)
/* 100:    */     {
/* 101:184 */       throw new JspTagException(getNoSuchMessageExceptionDescription(ex));
/* 102:    */     }
/* 103:    */   }
/* 104:    */   
/* 105:    */   protected String resolveMessage()
/* 106:    */     throws JspException, NoSuchMessageException
/* 107:    */   {
/* 108:193 */     MessageSource messageSource = getMessageSource();
/* 109:194 */     if (messageSource == null) {
/* 110:195 */       throw new JspTagException("No corresponding MessageSource found");
/* 111:    */     }
/* 112:199 */     MessageSourceResolvable resolvedMessage = null;
/* 113:200 */     if ((this.message instanceof MessageSourceResolvable))
/* 114:    */     {
/* 115:201 */       resolvedMessage = (MessageSourceResolvable)this.message;
/* 116:    */     }
/* 117:203 */     else if (this.message != null)
/* 118:    */     {
/* 119:204 */       String expr = this.message.toString();
/* 120:205 */       resolvedMessage = (MessageSourceResolvable)
/* 121:206 */         ExpressionEvaluationUtils.evaluate("message", expr, MessageSourceResolvable.class, this.pageContext);
/* 122:    */     }
/* 123:209 */     if (resolvedMessage != null) {
/* 124:211 */       return messageSource.getMessage(resolvedMessage, getRequestContext().getLocale());
/* 125:    */     }
/* 126:214 */     String resolvedCode = ExpressionEvaluationUtils.evaluateString("code", this.code, this.pageContext);
/* 127:215 */     String resolvedText = ExpressionEvaluationUtils.evaluateString("text", this.text, this.pageContext);
/* 128:217 */     if ((resolvedCode != null) || (resolvedText != null))
/* 129:    */     {
/* 130:219 */       Object[] argumentsArray = resolveArguments(this.arguments);
/* 131:220 */       if (resolvedText != null) {
/* 132:222 */         return messageSource.getMessage(
/* 133:223 */           resolvedCode, argumentsArray, resolvedText, getRequestContext().getLocale());
/* 134:    */       }
/* 135:227 */       return messageSource.getMessage(
/* 136:228 */         resolvedCode, argumentsArray, getRequestContext().getLocale());
/* 137:    */     }
/* 138:233 */     return resolvedText;
/* 139:    */   }
/* 140:    */   
/* 141:    */   protected Object[] resolveArguments(Object arguments)
/* 142:    */     throws JspException
/* 143:    */   {
/* 144:244 */     if ((arguments instanceof String))
/* 145:    */     {
/* 146:245 */       String[] stringArray = 
/* 147:246 */         StringUtils.delimitedListToStringArray((String)arguments, this.argumentSeparator);
/* 148:247 */       if (stringArray.length == 1)
/* 149:    */       {
/* 150:248 */         Object argument = ExpressionEvaluationUtils.evaluate("argument", stringArray[0], this.pageContext);
/* 151:249 */         if ((argument != null) && (argument.getClass().isArray())) {
/* 152:250 */           return ObjectUtils.toObjectArray(argument);
/* 153:    */         }
/* 154:253 */         return new Object[] { argument };
/* 155:    */       }
/* 156:257 */       Object[] argumentsArray = new Object[stringArray.length];
/* 157:258 */       for (int i = 0; i < stringArray.length; i++) {
/* 158:259 */         argumentsArray[i] = 
/* 159:260 */           ExpressionEvaluationUtils.evaluate("argument[" + i + "]", stringArray[i], this.pageContext);
/* 160:    */       }
/* 161:262 */       return argumentsArray;
/* 162:    */     }
/* 163:265 */     if ((arguments instanceof Object[])) {
/* 164:266 */       return (Object[])arguments;
/* 165:    */     }
/* 166:268 */     if ((arguments instanceof Collection)) {
/* 167:269 */       return ((Collection)arguments).toArray();
/* 168:    */     }
/* 169:271 */     if (arguments != null) {
/* 170:273 */       return new Object[] { arguments };
/* 171:    */     }
/* 172:276 */     return null;
/* 173:    */   }
/* 174:    */   
/* 175:    */   protected void writeMessage(String msg)
/* 176:    */     throws IOException
/* 177:    */   {
/* 178:287 */     this.pageContext.getOut().write(String.valueOf(msg));
/* 179:    */   }
/* 180:    */   
/* 181:    */   protected MessageSource getMessageSource()
/* 182:    */   {
/* 183:294 */     return getRequestContext().getMessageSource();
/* 184:    */   }
/* 185:    */   
/* 186:    */   protected String getNoSuchMessageExceptionDescription(NoSuchMessageException ex)
/* 187:    */   {
/* 188:301 */     return ex.getMessage();
/* 189:    */   }
/* 190:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.MessageTag
 * JD-Core Version:    0.7.0.1
 */