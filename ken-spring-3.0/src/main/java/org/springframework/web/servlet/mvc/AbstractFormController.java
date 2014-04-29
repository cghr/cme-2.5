/*   1:    */ package org.springframework.web.servlet.mvc;
/*   2:    */ 
/*   3:    */ import java.util.Map;
/*   4:    */ import javax.servlet.ServletException;
/*   5:    */ import javax.servlet.http.HttpServletRequest;
/*   6:    */ import javax.servlet.http.HttpServletResponse;
/*   7:    */ import javax.servlet.http.HttpSession;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.springframework.validation.BindException;
/*  10:    */ import org.springframework.validation.Errors;
/*  11:    */ import org.springframework.web.HttpSessionRequiredException;
/*  12:    */ import org.springframework.web.bind.ServletRequestDataBinder;
/*  13:    */ import org.springframework.web.servlet.ModelAndView;
/*  14:    */ 
/*  15:    */ @Deprecated
/*  16:    */ public abstract class AbstractFormController
/*  17:    */   extends BaseCommandController
/*  18:    */ {
/*  19:175 */   private boolean bindOnNewForm = false;
/*  20:177 */   private boolean sessionForm = false;
/*  21:    */   
/*  22:    */   public AbstractFormController()
/*  23:    */   {
/*  24:194 */     setCacheSeconds(0);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public final void setBindOnNewForm(boolean bindOnNewForm)
/*  28:    */   {
/*  29:202 */     this.bindOnNewForm = bindOnNewForm;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public final boolean isBindOnNewForm()
/*  33:    */   {
/*  34:209 */     return this.bindOnNewForm;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public final void setSessionForm(boolean sessionForm)
/*  38:    */   {
/*  39:237 */     this.sessionForm = sessionForm;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public final boolean isSessionForm()
/*  43:    */   {
/*  44:244 */     return this.sessionForm;
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
/*  48:    */     throws Exception
/*  49:    */   {
/*  50:262 */     if (isFormSubmission(request)) {
/*  51:    */       try
/*  52:    */       {
/*  53:265 */         Object command = getCommand(request);
/*  54:266 */         ServletRequestDataBinder binder = bindAndValidate(request, command);
/*  55:267 */         BindException errors = new BindException(binder.getBindingResult());
/*  56:268 */         return processFormSubmission(request, response, command, errors);
/*  57:    */       }
/*  58:    */       catch (HttpSessionRequiredException ex)
/*  59:    */       {
/*  60:272 */         if (this.logger.isDebugEnabled()) {
/*  61:273 */           this.logger.debug("Invalid submit detected: " + ex.getMessage());
/*  62:    */         }
/*  63:275 */         return handleInvalidSubmit(request, response);
/*  64:    */       }
/*  65:    */     }
/*  66:281 */     return showNewForm(request, response);
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected boolean isFormSubmission(HttpServletRequest request)
/*  70:    */   {
/*  71:296 */     return "POST".equals(request.getMethod());
/*  72:    */   }
/*  73:    */   
/*  74:    */   protected String getFormSessionAttributeName(HttpServletRequest request)
/*  75:    */   {
/*  76:310 */     return getFormSessionAttributeName();
/*  77:    */   }
/*  78:    */   
/*  79:    */   protected String getFormSessionAttributeName()
/*  80:    */   {
/*  81:324 */     return getClass().getName() + ".FORM." + getCommandName();
/*  82:    */   }
/*  83:    */   
/*  84:    */   protected final ModelAndView showNewForm(HttpServletRequest request, HttpServletResponse response)
/*  85:    */     throws Exception
/*  86:    */   {
/*  87:340 */     this.logger.debug("Displaying new form");
/*  88:341 */     return showForm(request, response, getErrorsForNewForm(request));
/*  89:    */   }
/*  90:    */   
/*  91:    */   protected final BindException getErrorsForNewForm(HttpServletRequest request)
/*  92:    */     throws Exception
/*  93:    */   {
/*  94:361 */     Object command = formBackingObject(request);
/*  95:362 */     if (command == null) {
/*  96:363 */       throw new ServletException("Form object returned by formBackingObject() must not be null");
/*  97:    */     }
/*  98:365 */     if (!checkCommand(command)) {
/*  99:366 */       throw new ServletException("Form object returned by formBackingObject() must match commandClass");
/* 100:    */     }
/* 101:371 */     ServletRequestDataBinder binder = createBinder(request, command);
/* 102:372 */     BindException errors = new BindException(binder.getBindingResult());
/* 103:373 */     if (isBindOnNewForm())
/* 104:    */     {
/* 105:374 */       this.logger.debug("Binding to new form");
/* 106:375 */       binder.bind(request);
/* 107:376 */       onBindOnNewForm(request, command, errors);
/* 108:    */     }
/* 109:380 */     return errors;
/* 110:    */   }
/* 111:    */   
/* 112:    */   protected void onBindOnNewForm(HttpServletRequest request, Object command, BindException errors)
/* 113:    */     throws Exception
/* 114:    */   {
/* 115:398 */     onBindOnNewForm(request, command);
/* 116:    */   }
/* 117:    */   
/* 118:    */   protected void onBindOnNewForm(HttpServletRequest request, Object command)
/* 119:    */     throws Exception
/* 120:    */   {}
/* 121:    */   
/* 122:    */   protected final Object getCommand(HttpServletRequest request)
/* 123:    */     throws Exception
/* 124:    */   {
/* 125:434 */     if (!isSessionForm()) {
/* 126:435 */       return formBackingObject(request);
/* 127:    */     }
/* 128:439 */     HttpSession session = request.getSession(false);
/* 129:440 */     if (session == null) {
/* 130:441 */       throw new HttpSessionRequiredException("Must have session when trying to bind (in session-form mode)");
/* 131:    */     }
/* 132:443 */     String formAttrName = getFormSessionAttributeName(request);
/* 133:444 */     Object sessionFormObject = session.getAttribute(formAttrName);
/* 134:445 */     if (sessionFormObject == null) {
/* 135:446 */       throw new HttpSessionRequiredException("Form object not found in session (in session-form mode)");
/* 136:    */     }
/* 137:452 */     if (this.logger.isDebugEnabled()) {
/* 138:453 */       this.logger.debug("Removing form session attribute [" + formAttrName + "]");
/* 139:    */     }
/* 140:455 */     session.removeAttribute(formAttrName);
/* 141:    */     
/* 142:457 */     return currentFormObject(request, sessionFormObject);
/* 143:    */   }
/* 144:    */   
/* 145:    */   protected Object formBackingObject(HttpServletRequest request)
/* 146:    */     throws Exception
/* 147:    */   {
/* 148:481 */     return createCommand();
/* 149:    */   }
/* 150:    */   
/* 151:    */   protected Object currentFormObject(HttpServletRequest request, Object sessionFormObject)
/* 152:    */     throws Exception
/* 153:    */   {
/* 154:495 */     return sessionFormObject;
/* 155:    */   }
/* 156:    */   
/* 157:    */   protected abstract ModelAndView showForm(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, BindException paramBindException)
/* 158:    */     throws Exception;
/* 159:    */   
/* 160:    */   protected final ModelAndView showForm(HttpServletRequest request, BindException errors, String viewName)
/* 161:    */     throws Exception
/* 162:    */   {
/* 163:541 */     return showForm(request, errors, viewName, null);
/* 164:    */   }
/* 165:    */   
/* 166:    */   protected final ModelAndView showForm(HttpServletRequest request, BindException errors, String viewName, Map controlModel)
/* 167:    */     throws Exception
/* 168:    */   {
/* 169:565 */     if (isSessionForm())
/* 170:    */     {
/* 171:566 */       String formAttrName = getFormSessionAttributeName(request);
/* 172:567 */       if (this.logger.isDebugEnabled()) {
/* 173:568 */         this.logger.debug("Setting form session attribute [" + formAttrName + "] to: " + errors.getTarget());
/* 174:    */       }
/* 175:570 */       request.getSession().setAttribute(formAttrName, errors.getTarget());
/* 176:    */     }
/* 177:575 */     Map model = errors.getModel();
/* 178:    */     
/* 179:    */ 
/* 180:578 */     Map referenceData = referenceData(request, errors.getTarget(), errors);
/* 181:579 */     if (referenceData != null) {
/* 182:580 */       model.putAll(referenceData);
/* 183:    */     }
/* 184:584 */     if (controlModel != null) {
/* 185:585 */       model.putAll(controlModel);
/* 186:    */     }
/* 187:589 */     return new ModelAndView(viewName, model);
/* 188:    */   }
/* 189:    */   
/* 190:    */   protected Map referenceData(HttpServletRequest request, Object command, Errors errors)
/* 191:    */     throws Exception
/* 192:    */   {
/* 193:605 */     return null;
/* 194:    */   }
/* 195:    */   
/* 196:    */   protected abstract ModelAndView processFormSubmission(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, Object paramObject, BindException paramBindException)
/* 197:    */     throws Exception;
/* 198:    */   
/* 199:    */   protected ModelAndView handleInvalidSubmit(HttpServletRequest request, HttpServletResponse response)
/* 200:    */     throws Exception
/* 201:    */   {
/* 202:672 */     Object command = formBackingObject(request);
/* 203:673 */     ServletRequestDataBinder binder = bindAndValidate(request, command);
/* 204:674 */     BindException errors = new BindException(binder.getBindingResult());
/* 205:675 */     return processFormSubmission(request, response, command, errors);
/* 206:    */   }
/* 207:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.AbstractFormController
 * JD-Core Version:    0.7.0.1
 */