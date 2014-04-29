/*   1:    */ package org.springframework.web.servlet.mvc;
/*   2:    */ 
/*   3:    */ import javax.servlet.http.HttpServletRequest;
/*   4:    */ import org.apache.commons.logging.Log;
/*   5:    */ import org.springframework.beans.BeanUtils;
/*   6:    */ import org.springframework.beans.PropertyEditorRegistrar;
/*   7:    */ import org.springframework.validation.BindException;
/*   8:    */ import org.springframework.validation.BindingErrorProcessor;
/*   9:    */ import org.springframework.validation.MessageCodesResolver;
/*  10:    */ import org.springframework.validation.ValidationUtils;
/*  11:    */ import org.springframework.validation.Validator;
/*  12:    */ import org.springframework.web.bind.ServletRequestDataBinder;
/*  13:    */ import org.springframework.web.bind.support.WebBindingInitializer;
/*  14:    */ import org.springframework.web.context.request.ServletWebRequest;
/*  15:    */ 
/*  16:    */ @Deprecated
/*  17:    */ public abstract class BaseCommandController
/*  18:    */   extends AbstractController
/*  19:    */ {
/*  20:    */   public static final String DEFAULT_COMMAND_NAME = "command";
/*  21:143 */   private String commandName = "command";
/*  22:    */   private Class commandClass;
/*  23:    */   private Validator[] validators;
/*  24:149 */   private boolean validateOnBinding = true;
/*  25:    */   private MessageCodesResolver messageCodesResolver;
/*  26:    */   private BindingErrorProcessor bindingErrorProcessor;
/*  27:    */   private PropertyEditorRegistrar[] propertyEditorRegistrars;
/*  28:    */   private WebBindingInitializer webBindingInitializer;
/*  29:    */   
/*  30:    */   public final void setCommandName(String commandName)
/*  31:    */   {
/*  32:165 */     this.commandName = commandName;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public final String getCommandName()
/*  36:    */   {
/*  37:172 */     return this.commandName;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public final void setCommandClass(Class commandClass)
/*  41:    */   {
/*  42:180 */     this.commandClass = commandClass;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public final Class getCommandClass()
/*  46:    */   {
/*  47:187 */     return this.commandClass;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public final void setValidator(Validator validator)
/*  51:    */   {
/*  52:198 */     this.validators = new Validator[] { validator };
/*  53:    */   }
/*  54:    */   
/*  55:    */   public final Validator getValidator()
/*  56:    */   {
/*  57:205 */     return (this.validators != null) && (this.validators.length > 0) ? this.validators[0] : null;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public final void setValidators(Validator[] validators)
/*  61:    */   {
/*  62:213 */     this.validators = validators;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public final Validator[] getValidators()
/*  66:    */   {
/*  67:220 */     return this.validators;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public final void setValidateOnBinding(boolean validateOnBinding)
/*  71:    */   {
/*  72:227 */     this.validateOnBinding = validateOnBinding;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public final boolean isValidateOnBinding()
/*  76:    */   {
/*  77:234 */     return this.validateOnBinding;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public final void setMessageCodesResolver(MessageCodesResolver messageCodesResolver)
/*  81:    */   {
/*  82:246 */     this.messageCodesResolver = messageCodesResolver;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public final MessageCodesResolver getMessageCodesResolver()
/*  86:    */   {
/*  87:253 */     return this.messageCodesResolver;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public final void setBindingErrorProcessor(BindingErrorProcessor bindingErrorProcessor)
/*  91:    */   {
/*  92:265 */     this.bindingErrorProcessor = bindingErrorProcessor;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public final BindingErrorProcessor getBindingErrorProcessor()
/*  96:    */   {
/*  97:272 */     return this.bindingErrorProcessor;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public final void setPropertyEditorRegistrar(PropertyEditorRegistrar propertyEditorRegistrar)
/* 101:    */   {
/* 102:283 */     this.propertyEditorRegistrars = new PropertyEditorRegistrar[] { propertyEditorRegistrar };
/* 103:    */   }
/* 104:    */   
/* 105:    */   public final void setPropertyEditorRegistrars(PropertyEditorRegistrar[] propertyEditorRegistrars)
/* 106:    */   {
/* 107:294 */     this.propertyEditorRegistrars = propertyEditorRegistrars;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public final PropertyEditorRegistrar[] getPropertyEditorRegistrars()
/* 111:    */   {
/* 112:302 */     return this.propertyEditorRegistrars;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public final void setWebBindingInitializer(WebBindingInitializer webBindingInitializer)
/* 116:    */   {
/* 117:312 */     this.webBindingInitializer = webBindingInitializer;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public final WebBindingInitializer getWebBindingInitializer()
/* 121:    */   {
/* 122:320 */     return this.webBindingInitializer;
/* 123:    */   }
/* 124:    */   
/* 125:    */   protected void initApplicationContext()
/* 126:    */   {
/* 127:326 */     if (this.validators != null) {
/* 128:327 */       for (int i = 0; i < this.validators.length; i++) {
/* 129:328 */         if ((this.commandClass != null) && (!this.validators[i].supports(this.commandClass))) {
/* 130:329 */           throw new IllegalArgumentException("Validator [" + this.validators[i] + 
/* 131:330 */             "] does not support command class [" + 
/* 132:331 */             this.commandClass.getName() + "]");
/* 133:    */         }
/* 134:    */       }
/* 135:    */     }
/* 136:    */   }
/* 137:    */   
/* 138:    */   protected Object getCommand(HttpServletRequest request)
/* 139:    */     throws Exception
/* 140:    */   {
/* 141:347 */     return createCommand();
/* 142:    */   }
/* 143:    */   
/* 144:    */   protected final Object createCommand()
/* 145:    */     throws Exception
/* 146:    */   {
/* 147:360 */     if (this.commandClass == null) {
/* 148:361 */       throw new IllegalStateException("Cannot create command without commandClass being set - either set commandClass or (in a form controller) override formBackingObject");
/* 149:    */     }
/* 150:364 */     if (this.logger.isDebugEnabled()) {
/* 151:365 */       this.logger.debug("Creating new command of class [" + this.commandClass.getName() + "]");
/* 152:    */     }
/* 153:367 */     return BeanUtils.instantiateClass(this.commandClass);
/* 154:    */   }
/* 155:    */   
/* 156:    */   protected final boolean checkCommand(Object command)
/* 157:    */   {
/* 158:377 */     return (this.commandClass == null) || (this.commandClass.isInstance(command));
/* 159:    */   }
/* 160:    */   
/* 161:    */   protected final ServletRequestDataBinder bindAndValidate(HttpServletRequest request, Object command)
/* 162:    */     throws Exception
/* 163:    */   {
/* 164:391 */     ServletRequestDataBinder binder = createBinder(request, command);
/* 165:392 */     BindException errors = new BindException(binder.getBindingResult());
/* 166:393 */     if (!suppressBinding(request))
/* 167:    */     {
/* 168:394 */       binder.bind(request);
/* 169:395 */       onBind(request, command, errors);
/* 170:396 */       if ((this.validators != null) && (isValidateOnBinding()) && (!suppressValidation(request, command, errors))) {
/* 171:397 */         for (int i = 0; i < this.validators.length; i++) {
/* 172:398 */           ValidationUtils.invokeValidator(this.validators[i], command, errors);
/* 173:    */         }
/* 174:    */       }
/* 175:401 */       onBindAndValidate(request, command, errors);
/* 176:    */     }
/* 177:403 */     return binder;
/* 178:    */   }
/* 179:    */   
/* 180:    */   protected boolean suppressBinding(HttpServletRequest request)
/* 181:    */   {
/* 182:416 */     return false;
/* 183:    */   }
/* 184:    */   
/* 185:    */   protected ServletRequestDataBinder createBinder(HttpServletRequest request, Object command)
/* 186:    */     throws Exception
/* 187:    */   {
/* 188:439 */     ServletRequestDataBinder binder = new ServletRequestDataBinder(command, getCommandName());
/* 189:440 */     prepareBinder(binder);
/* 190:441 */     initBinder(request, binder);
/* 191:442 */     return binder;
/* 192:    */   }
/* 193:    */   
/* 194:    */   protected final void prepareBinder(ServletRequestDataBinder binder)
/* 195:    */   {
/* 196:455 */     if (useDirectFieldAccess()) {
/* 197:456 */       binder.initDirectFieldAccess();
/* 198:    */     }
/* 199:458 */     if (this.messageCodesResolver != null) {
/* 200:459 */       binder.setMessageCodesResolver(this.messageCodesResolver);
/* 201:    */     }
/* 202:461 */     if (this.bindingErrorProcessor != null) {
/* 203:462 */       binder.setBindingErrorProcessor(this.bindingErrorProcessor);
/* 204:    */     }
/* 205:464 */     if (this.propertyEditorRegistrars != null) {
/* 206:465 */       for (int i = 0; i < this.propertyEditorRegistrars.length; i++) {
/* 207:466 */         this.propertyEditorRegistrars[i].registerCustomEditors(binder);
/* 208:    */       }
/* 209:    */     }
/* 210:    */   }
/* 211:    */   
/* 212:    */   protected boolean useDirectFieldAccess()
/* 213:    */   {
/* 214:481 */     return false;
/* 215:    */   }
/* 216:    */   
/* 217:    */   protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
/* 218:    */     throws Exception
/* 219:    */   {
/* 220:500 */     if (this.webBindingInitializer != null) {
/* 221:501 */       this.webBindingInitializer.initBinder(binder, new ServletWebRequest(request));
/* 222:    */     }
/* 223:    */   }
/* 224:    */   
/* 225:    */   protected void onBind(HttpServletRequest request, Object command, BindException errors)
/* 226:    */     throws Exception
/* 227:    */   {
/* 228:518 */     onBind(request, command);
/* 229:    */   }
/* 230:    */   
/* 231:    */   protected void onBind(HttpServletRequest request, Object command)
/* 232:    */     throws Exception
/* 233:    */   {}
/* 234:    */   
/* 235:    */   protected boolean suppressValidation(HttpServletRequest request, Object command, BindException errors)
/* 236:    */   {
/* 237:545 */     return suppressValidation(request, command);
/* 238:    */   }
/* 239:    */   
/* 240:    */   protected boolean suppressValidation(HttpServletRequest request, Object command)
/* 241:    */   {
/* 242:559 */     return suppressValidation(request);
/* 243:    */   }
/* 244:    */   
/* 245:    */   @Deprecated
/* 246:    */   protected boolean suppressValidation(HttpServletRequest request)
/* 247:    */   {
/* 248:575 */     return false;
/* 249:    */   }
/* 250:    */   
/* 251:    */   protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors)
/* 252:    */     throws Exception
/* 253:    */   {}
/* 254:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.BaseCommandController
 * JD-Core Version:    0.7.0.1
 */