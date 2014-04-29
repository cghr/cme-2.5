/*   1:    */ package org.springframework.web.servlet.tags.form;
/*   2:    */ 
/*   3:    */ import java.util.Map;
/*   4:    */ import javax.servlet.ServletRequest;
/*   5:    */ import javax.servlet.ServletResponse;
/*   6:    */ import javax.servlet.http.HttpServletRequest;
/*   7:    */ import javax.servlet.http.HttpServletResponse;
/*   8:    */ import javax.servlet.jsp.JspException;
/*   9:    */ import javax.servlet.jsp.PageContext;
/*  10:    */ import org.springframework.core.Conventions;
/*  11:    */ import org.springframework.util.ObjectUtils;
/*  12:    */ import org.springframework.util.StringUtils;
/*  13:    */ import org.springframework.web.servlet.support.RequestContext;
/*  14:    */ import org.springframework.web.servlet.support.RequestDataValueProcessor;
/*  15:    */ import org.springframework.web.util.HtmlUtils;
/*  16:    */ 
/*  17:    */ public class FormTag
/*  18:    */   extends AbstractHtmlElementTag
/*  19:    */ {
/*  20:    */   private static final String DEFAULT_METHOD = "post";
/*  21:    */   public static final String DEFAULT_COMMAND_NAME = "command";
/*  22:    */   private static final String MODEL_ATTRIBUTE = "modelAttribute";
/*  23: 71 */   public static final String MODEL_ATTRIBUTE_VARIABLE_NAME = Conventions.getQualifiedAttributeName(AbstractFormTag.class, "modelAttribute");
/*  24:    */   private static final String DEFAULT_METHOD_PARAM = "_method";
/*  25:    */   private static final String FORM_TAG = "form";
/*  26:    */   private static final String INPUT_TAG = "input";
/*  27:    */   private static final String ACTION_ATTRIBUTE = "action";
/*  28:    */   private static final String METHOD_ATTRIBUTE = "method";
/*  29:    */   private static final String TARGET_ATTRIBUTE = "target";
/*  30:    */   private static final String ENCTYPE_ATTRIBUTE = "enctype";
/*  31:    */   private static final String ACCEPT_CHARSET_ATTRIBUTE = "accept-charset";
/*  32:    */   private static final String ONSUBMIT_ATTRIBUTE = "onsubmit";
/*  33:    */   private static final String ONRESET_ATTRIBUTE = "onreset";
/*  34:    */   private static final String AUTOCOMPLETE_ATTRIBUTE = "autocomplete";
/*  35:    */   private static final String NAME_ATTRIBUTE = "name";
/*  36:    */   private static final String VALUE_ATTRIBUTE = "value";
/*  37:    */   private static final String TYPE_ATTRIBUTE = "type";
/*  38:    */   private TagWriter tagWriter;
/*  39:105 */   private String modelAttribute = "command";
/*  40:    */   private String name;
/*  41:    */   private String action;
/*  42:111 */   private String method = "post";
/*  43:    */   private String target;
/*  44:    */   private String enctype;
/*  45:    */   private String acceptCharset;
/*  46:    */   private String onsubmit;
/*  47:    */   private String onreset;
/*  48:    */   private String autocomplete;
/*  49:125 */   private String methodParam = "_method";
/*  50:    */   private String previousNestedPath;
/*  51:    */   
/*  52:    */   public void setModelAttribute(String modelAttribute)
/*  53:    */   {
/*  54:136 */     this.modelAttribute = modelAttribute;
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected String getModelAttribute()
/*  58:    */   {
/*  59:143 */     return this.modelAttribute;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void setCommandName(String commandName)
/*  63:    */   {
/*  64:152 */     this.modelAttribute = commandName;
/*  65:    */   }
/*  66:    */   
/*  67:    */   protected String getCommandName()
/*  68:    */   {
/*  69:160 */     return this.modelAttribute;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setName(String name)
/*  73:    */   {
/*  74:170 */     this.name = name;
/*  75:    */   }
/*  76:    */   
/*  77:    */   protected String getName()
/*  78:    */     throws JspException
/*  79:    */   {
/*  80:178 */     return this.name;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void setAction(String action)
/*  84:    */   {
/*  85:186 */     this.action = (action != null ? action : "");
/*  86:    */   }
/*  87:    */   
/*  88:    */   protected String getAction()
/*  89:    */   {
/*  90:193 */     return this.action;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void setMethod(String method)
/*  94:    */   {
/*  95:201 */     this.method = method;
/*  96:    */   }
/*  97:    */   
/*  98:    */   protected String getMethod()
/*  99:    */   {
/* 100:208 */     return this.method;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void setTarget(String target)
/* 104:    */   {
/* 105:216 */     this.target = target;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public String getTarget()
/* 109:    */   {
/* 110:223 */     return this.target;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void setEnctype(String enctype)
/* 114:    */   {
/* 115:231 */     this.enctype = enctype;
/* 116:    */   }
/* 117:    */   
/* 118:    */   protected String getEnctype()
/* 119:    */   {
/* 120:238 */     return this.enctype;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public void setAcceptCharset(String acceptCharset)
/* 124:    */   {
/* 125:246 */     this.acceptCharset = acceptCharset;
/* 126:    */   }
/* 127:    */   
/* 128:    */   protected String getAcceptCharset()
/* 129:    */   {
/* 130:253 */     return this.acceptCharset;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public void setOnsubmit(String onsubmit)
/* 134:    */   {
/* 135:261 */     this.onsubmit = onsubmit;
/* 136:    */   }
/* 137:    */   
/* 138:    */   protected String getOnsubmit()
/* 139:    */   {
/* 140:268 */     return this.onsubmit;
/* 141:    */   }
/* 142:    */   
/* 143:    */   public void setOnreset(String onreset)
/* 144:    */   {
/* 145:276 */     this.onreset = onreset;
/* 146:    */   }
/* 147:    */   
/* 148:    */   protected String getOnreset()
/* 149:    */   {
/* 150:283 */     return this.onreset;
/* 151:    */   }
/* 152:    */   
/* 153:    */   public void setAutocomplete(String autocomplete)
/* 154:    */   {
/* 155:291 */     this.autocomplete = autocomplete;
/* 156:    */   }
/* 157:    */   
/* 158:    */   protected String getAutocomplete()
/* 159:    */   {
/* 160:298 */     return this.autocomplete;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public void setMethodParam(String methodParam)
/* 164:    */   {
/* 165:305 */     this.methodParam = methodParam;
/* 166:    */   }
/* 167:    */   
/* 168:    */   protected String getMethodParameter()
/* 169:    */   {
/* 170:312 */     return this.methodParam;
/* 171:    */   }
/* 172:    */   
/* 173:    */   protected boolean isMethodBrowserSupported(String method)
/* 174:    */   {
/* 175:319 */     return ("get".equalsIgnoreCase(method)) || ("post".equalsIgnoreCase(method));
/* 176:    */   }
/* 177:    */   
/* 178:    */   protected int writeTagContent(TagWriter tagWriter)
/* 179:    */     throws JspException
/* 180:    */   {
/* 181:331 */     this.tagWriter = tagWriter;
/* 182:    */     
/* 183:333 */     tagWriter.startTag("form");
/* 184:334 */     writeDefaultAttributes(tagWriter);
/* 185:335 */     tagWriter.writeAttribute("action", resolveAction());
/* 186:336 */     writeOptionalAttribute(tagWriter, "method", 
/* 187:337 */       isMethodBrowserSupported(getMethod()) ? getMethod() : "post");
/* 188:338 */     writeOptionalAttribute(tagWriter, "target", getTarget());
/* 189:339 */     writeOptionalAttribute(tagWriter, "enctype", getEnctype());
/* 190:340 */     writeOptionalAttribute(tagWriter, "accept-charset", getAcceptCharset());
/* 191:341 */     writeOptionalAttribute(tagWriter, "onsubmit", getOnsubmit());
/* 192:342 */     writeOptionalAttribute(tagWriter, "onreset", getOnreset());
/* 193:343 */     writeOptionalAttribute(tagWriter, "autocomplete", getAutocomplete());
/* 194:    */     
/* 195:345 */     tagWriter.forceBlock();
/* 196:347 */     if (!isMethodBrowserSupported(getMethod()))
/* 197:    */     {
/* 198:348 */       String inputName = getMethodParameter();
/* 199:349 */       String inputType = "hidden";
/* 200:350 */       tagWriter.startTag("input");
/* 201:351 */       writeOptionalAttribute(tagWriter, "type", inputType);
/* 202:352 */       writeOptionalAttribute(tagWriter, "name", inputName);
/* 203:353 */       writeOptionalAttribute(tagWriter, "value", processFieldValue(getName(), getMethod(), inputType));
/* 204:354 */       tagWriter.endTag();
/* 205:    */     }
/* 206:358 */     String modelAttribute = resolveModelAttribute();
/* 207:359 */     this.pageContext.setAttribute(MODEL_ATTRIBUTE_VARIABLE_NAME, modelAttribute, 2);
/* 208:360 */     this.pageContext.setAttribute(COMMAND_NAME_VARIABLE_NAME, modelAttribute, 2);
/* 209:    */     
/* 210:    */ 
/* 211:    */ 
/* 212:364 */     this.previousNestedPath = 
/* 213:365 */       ((String)this.pageContext.getAttribute("nestedPath", 2));
/* 214:366 */     this.pageContext.setAttribute("nestedPath", 
/* 215:367 */       modelAttribute + ".", 2);
/* 216:    */     
/* 217:369 */     return 1;
/* 218:    */   }
/* 219:    */   
/* 220:    */   protected String autogenerateId()
/* 221:    */     throws JspException
/* 222:    */   {
/* 223:377 */     return resolveModelAttribute();
/* 224:    */   }
/* 225:    */   
/* 226:    */   protected String resolveModelAttribute()
/* 227:    */     throws JspException
/* 228:    */   {
/* 229:385 */     Object resolvedModelAttribute = evaluate("modelAttribute", getModelAttribute());
/* 230:386 */     if (resolvedModelAttribute == null) {
/* 231:387 */       throw new IllegalArgumentException("modelAttribute must not be null");
/* 232:    */     }
/* 233:389 */     return (String)resolvedModelAttribute;
/* 234:    */   }
/* 235:    */   
/* 236:    */   protected String resolveAction()
/* 237:    */     throws JspException
/* 238:    */   {
/* 239:401 */     String action = getAction();
/* 240:402 */     if (StringUtils.hasText(action))
/* 241:    */     {
/* 242:403 */       action = getDisplayString(evaluate("action", action));
/* 243:404 */       return processAction(action);
/* 244:    */     }
/* 245:407 */     String requestUri = getRequestContext().getRequestUri();
/* 246:408 */     ServletResponse response = this.pageContext.getResponse();
/* 247:409 */     if ((response instanceof HttpServletResponse))
/* 248:    */     {
/* 249:410 */       requestUri = ((HttpServletResponse)response).encodeURL(requestUri);
/* 250:411 */       String queryString = getRequestContext().getQueryString();
/* 251:412 */       if (StringUtils.hasText(queryString)) {
/* 252:413 */         requestUri = requestUri + "?" + HtmlUtils.htmlEscape(queryString);
/* 253:    */       }
/* 254:    */     }
/* 255:416 */     if (StringUtils.hasText(requestUri)) {
/* 256:417 */       return processAction(requestUri);
/* 257:    */     }
/* 258:420 */     throw new IllegalArgumentException("Attribute 'action' is required. Attempted to resolve against current request URI but request URI was null.");
/* 259:    */   }
/* 260:    */   
/* 261:    */   private String processAction(String action)
/* 262:    */   {
/* 263:431 */     RequestDataValueProcessor processor = getRequestContext().getRequestDataValueProcessor();
/* 264:432 */     ServletRequest request = this.pageContext.getRequest();
/* 265:433 */     if ((processor != null) && ((request instanceof HttpServletRequest))) {
/* 266:434 */       action = processor.processAction((HttpServletRequest)request, action);
/* 267:    */     }
/* 268:436 */     return action;
/* 269:    */   }
/* 270:    */   
/* 271:    */   public int doEndTag()
/* 272:    */     throws JspException
/* 273:    */   {
/* 274:445 */     RequestDataValueProcessor processor = getRequestContext().getRequestDataValueProcessor();
/* 275:446 */     ServletRequest request = this.pageContext.getRequest();
/* 276:447 */     if ((processor != null) && ((request instanceof HttpServletRequest))) {
/* 277:448 */       writeHiddenFields(processor.getExtraHiddenFields((HttpServletRequest)request));
/* 278:    */     }
/* 279:450 */     this.tagWriter.endTag();
/* 280:451 */     return 6;
/* 281:    */   }
/* 282:    */   
/* 283:    */   private void writeHiddenFields(Map<String, String> hiddenFields)
/* 284:    */     throws JspException
/* 285:    */   {
/* 286:458 */     if (hiddenFields != null) {
/* 287:459 */       for (String name : hiddenFields.keySet())
/* 288:    */       {
/* 289:460 */         this.tagWriter.appendValue("<input type=\"hidden\" ");
/* 290:461 */         this.tagWriter.appendValue("name=\"" + name + "\" value=\"" + (String)hiddenFields.get(name) + "\">");
/* 291:462 */         this.tagWriter.appendValue("</input>\n");
/* 292:    */       }
/* 293:    */     }
/* 294:    */   }
/* 295:    */   
/* 296:    */   public void doFinally()
/* 297:    */   {
/* 298:472 */     super.doFinally();
/* 299:473 */     this.pageContext.removeAttribute(MODEL_ATTRIBUTE_VARIABLE_NAME, 2);
/* 300:474 */     this.pageContext.removeAttribute(COMMAND_NAME_VARIABLE_NAME, 2);
/* 301:475 */     if (this.previousNestedPath != null) {
/* 302:477 */       this.pageContext.setAttribute("nestedPath", this.previousNestedPath, 2);
/* 303:    */     } else {
/* 304:481 */       this.pageContext.removeAttribute("nestedPath", 2);
/* 305:    */     }
/* 306:483 */     this.tagWriter = null;
/* 307:484 */     this.previousNestedPath = null;
/* 308:    */   }
/* 309:    */   
/* 310:    */   protected String resolveCssClass()
/* 311:    */     throws JspException
/* 312:    */   {
/* 313:493 */     return ObjectUtils.getDisplayString(evaluate("cssClass", getCssClass()));
/* 314:    */   }
/* 315:    */   
/* 316:    */   public void setPath(String path)
/* 317:    */   {
/* 318:502 */     throw new UnsupportedOperationException("The 'path' attribute is not supported for forms");
/* 319:    */   }
/* 320:    */   
/* 321:    */   public void setCssErrorClass(String cssErrorClass)
/* 322:    */   {
/* 323:511 */     throw new UnsupportedOperationException("The 'cssErrorClass' attribute is not supported for forms");
/* 324:    */   }
/* 325:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.FormTag
 * JD-Core Version:    0.7.0.1
 */