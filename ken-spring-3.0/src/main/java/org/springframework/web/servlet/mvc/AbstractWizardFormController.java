/*   1:    */ package org.springframework.web.servlet.mvc;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import javax.servlet.ServletException;
/*   6:    */ import javax.servlet.http.HttpServletRequest;
/*   7:    */ import javax.servlet.http.HttpServletResponse;
/*   8:    */ import javax.servlet.http.HttpSession;
/*   9:    */ import org.apache.commons.logging.Log;
/*  10:    */ import org.springframework.validation.BindException;
/*  11:    */ import org.springframework.validation.Errors;
/*  12:    */ import org.springframework.web.servlet.ModelAndView;
/*  13:    */ import org.springframework.web.util.WebUtils;
/*  14:    */ 
/*  15:    */ @Deprecated
/*  16:    */ public abstract class AbstractWizardFormController
/*  17:    */   extends AbstractFormController
/*  18:    */ {
/*  19:    */   public static final String PARAM_FINISH = "_finish";
/*  20:    */   public static final String PARAM_CANCEL = "_cancel";
/*  21:    */   public static final String PARAM_TARGET = "_target";
/*  22:    */   public static final String PARAM_PAGE = "_page";
/*  23:    */   private String[] pages;
/*  24:    */   private String pageAttribute;
/*  25:111 */   private boolean allowDirtyBack = true;
/*  26:113 */   private boolean allowDirtyForward = false;
/*  27:    */   
/*  28:    */   public AbstractWizardFormController()
/*  29:    */   {
/*  30:127 */     setSessionForm(true);
/*  31:    */     
/*  32:    */ 
/*  33:    */ 
/*  34:131 */     setValidateOnBinding(false);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public final void setPages(String[] pages)
/*  38:    */   {
/*  39:140 */     if ((pages == null) || (pages.length == 0)) {
/*  40:141 */       throw new IllegalArgumentException("No wizard pages defined");
/*  41:    */     }
/*  42:143 */     this.pages = pages;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public final String[] getPages()
/*  46:    */   {
/*  47:155 */     return this.pages;
/*  48:    */   }
/*  49:    */   
/*  50:    */   protected final int getPageCount()
/*  51:    */   {
/*  52:169 */     return this.pages.length;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public final void setPageAttribute(String pageAttribute)
/*  56:    */   {
/*  57:181 */     this.pageAttribute = pageAttribute;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public final String getPageAttribute()
/*  61:    */   {
/*  62:188 */     return this.pageAttribute;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public final void setAllowDirtyBack(boolean allowDirtyBack)
/*  66:    */   {
/*  67:197 */     this.allowDirtyBack = allowDirtyBack;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public final boolean isAllowDirtyBack()
/*  71:    */   {
/*  72:204 */     return this.allowDirtyBack;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public final void setAllowDirtyForward(boolean allowDirtyForward)
/*  76:    */   {
/*  77:213 */     this.allowDirtyForward = allowDirtyForward;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public final boolean isAllowDirtyForward()
/*  81:    */   {
/*  82:220 */     return this.allowDirtyForward;
/*  83:    */   }
/*  84:    */   
/*  85:    */   protected final void onBindAndValidate(HttpServletRequest request, Object command, BindException errors)
/*  86:    */     throws Exception
/*  87:    */   {
/*  88:231 */     onBindAndValidate(request, command, errors, getCurrentPage(request));
/*  89:    */   }
/*  90:    */   
/*  91:    */   protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page)
/*  92:    */     throws Exception
/*  93:    */   {}
/*  94:    */   
/*  95:    */   protected boolean isFormSubmission(HttpServletRequest request)
/*  96:    */   {
/*  97:261 */     return (super.isFormSubmission(request)) || (isFinishRequest(request)) || (isCancelRequest(request));
/*  98:    */   }
/*  99:    */   
/* 100:    */   protected final Map referenceData(HttpServletRequest request, Object command, Errors errors)
/* 101:    */     throws Exception
/* 102:    */   {
/* 103:271 */     return referenceData(request, command, errors, getCurrentPage(request));
/* 104:    */   }
/* 105:    */   
/* 106:    */   protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page)
/* 107:    */     throws Exception
/* 108:    */   {
/* 109:291 */     return referenceData(request, page);
/* 110:    */   }
/* 111:    */   
/* 112:    */   protected Map referenceData(HttpServletRequest request, int page)
/* 113:    */     throws Exception
/* 114:    */   {
/* 115:306 */     return null;
/* 116:    */   }
/* 117:    */   
/* 118:    */   protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors)
/* 119:    */     throws Exception
/* 120:    */   {
/* 121:320 */     return showPage(request, errors, getInitialPage(request, errors.getTarget()));
/* 122:    */   }
/* 123:    */   
/* 124:    */   protected final ModelAndView showPage(HttpServletRequest request, BindException errors, int page)
/* 125:    */     throws Exception
/* 126:    */   {
/* 127:336 */     if ((page >= 0) && (page < getPageCount(request, errors.getTarget())))
/* 128:    */     {
/* 129:337 */       if (this.logger.isDebugEnabled()) {
/* 130:338 */         this.logger.debug("Showing wizard page " + page + " for form bean '" + getCommandName() + "'");
/* 131:    */       }
/* 132:342 */       Integer pageInteger = new Integer(page);
/* 133:343 */       String pageAttrName = getPageSessionAttributeName(request);
/* 134:344 */       if (isSessionForm())
/* 135:    */       {
/* 136:345 */         if (this.logger.isDebugEnabled()) {
/* 137:346 */           this.logger.debug("Setting page session attribute [" + pageAttrName + "] to: " + pageInteger);
/* 138:    */         }
/* 139:348 */         request.getSession().setAttribute(pageAttrName, pageInteger);
/* 140:    */       }
/* 141:350 */       request.setAttribute(pageAttrName, pageInteger);
/* 142:    */       
/* 143:    */ 
/* 144:353 */       Map controlModel = new HashMap();
/* 145:354 */       if (this.pageAttribute != null) {
/* 146:355 */         controlModel.put(this.pageAttribute, new Integer(page));
/* 147:    */       }
/* 148:357 */       String viewName = getViewName(request, errors.getTarget(), page);
/* 149:358 */       return showForm(request, errors, viewName, controlModel);
/* 150:    */     }
/* 151:362 */     throw new ServletException("Invalid wizard page number: " + page);
/* 152:    */   }
/* 153:    */   
/* 154:    */   protected int getPageCount(HttpServletRequest request, Object command)
/* 155:    */   {
/* 156:376 */     return getPageCount();
/* 157:    */   }
/* 158:    */   
/* 159:    */   protected String getViewName(HttpServletRequest request, Object command, int page)
/* 160:    */   {
/* 161:391 */     return getPages()[page];
/* 162:    */   }
/* 163:    */   
/* 164:    */   protected int getInitialPage(HttpServletRequest request, Object command)
/* 165:    */   {
/* 166:404 */     return getInitialPage(request);
/* 167:    */   }
/* 168:    */   
/* 169:    */   protected int getInitialPage(HttpServletRequest request)
/* 170:    */   {
/* 171:414 */     return 0;
/* 172:    */   }
/* 173:    */   
/* 174:    */   protected String getPageSessionAttributeName(HttpServletRequest request)
/* 175:    */   {
/* 176:429 */     return getPageSessionAttributeName();
/* 177:    */   }
/* 178:    */   
/* 179:    */   protected String getPageSessionAttributeName()
/* 180:    */   {
/* 181:444 */     return getClass().getName() + ".PAGE." + getCommandName();
/* 182:    */   }
/* 183:    */   
/* 184:    */   protected ModelAndView handleInvalidSubmit(HttpServletRequest request, HttpServletResponse response)
/* 185:    */     throws Exception
/* 186:    */   {
/* 187:464 */     return showNewForm(request, response);
/* 188:    */   }
/* 189:    */   
/* 190:    */   protected final ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
/* 191:    */     throws Exception
/* 192:    */   {
/* 193:476 */     int currentPage = getCurrentPage(request);
/* 194:    */     
/* 195:478 */     String pageAttrName = getPageSessionAttributeName(request);
/* 196:479 */     if (isSessionForm())
/* 197:    */     {
/* 198:480 */       if (this.logger.isDebugEnabled()) {
/* 199:481 */         this.logger.debug("Removing page session attribute [" + pageAttrName + "]");
/* 200:    */       }
/* 201:483 */       request.getSession().removeAttribute(pageAttrName);
/* 202:    */     }
/* 203:485 */     request.setAttribute(pageAttrName, new Integer(currentPage));
/* 204:488 */     if (isCancelRequest(request))
/* 205:    */     {
/* 206:489 */       if (this.logger.isDebugEnabled()) {
/* 207:490 */         this.logger.debug("Cancelling wizard for form bean '" + getCommandName() + "'");
/* 208:    */       }
/* 209:492 */       return processCancel(request, response, command, errors);
/* 210:    */     }
/* 211:496 */     if (isFinishRequest(request))
/* 212:    */     {
/* 213:497 */       if (this.logger.isDebugEnabled()) {
/* 214:498 */         this.logger.debug("Finishing wizard for form bean '" + getCommandName() + "'");
/* 215:    */       }
/* 216:500 */       return validatePagesAndFinish(request, response, command, errors, currentPage);
/* 217:    */     }
/* 218:504 */     if (!suppressValidation(request, command, errors))
/* 219:    */     {
/* 220:505 */       if (this.logger.isDebugEnabled()) {
/* 221:506 */         this.logger.debug("Validating wizard page " + currentPage + " for form bean '" + getCommandName() + "'");
/* 222:    */       }
/* 223:508 */       validatePage(command, errors, currentPage, false);
/* 224:    */     }
/* 225:513 */     postProcessPage(request, command, errors, currentPage);
/* 226:    */     
/* 227:515 */     int targetPage = getTargetPage(request, command, errors, currentPage);
/* 228:516 */     if (this.logger.isDebugEnabled()) {
/* 229:517 */       this.logger.debug("Target page " + targetPage + " requested");
/* 230:    */     }
/* 231:519 */     if ((targetPage != currentPage) && (
/* 232:520 */       (!errors.hasErrors()) || ((this.allowDirtyBack) && (targetPage < currentPage)) || (
/* 233:521 */       (this.allowDirtyForward) && (targetPage > currentPage)))) {
/* 234:523 */       return showPage(request, errors, targetPage);
/* 235:    */     }
/* 236:528 */     return showPage(request, errors, currentPage);
/* 237:    */   }
/* 238:    */   
/* 239:    */   protected int getCurrentPage(HttpServletRequest request)
/* 240:    */   {
/* 241:541 */     String pageAttrName = getPageSessionAttributeName(request);
/* 242:542 */     Integer pageAttr = (Integer)request.getAttribute(pageAttrName);
/* 243:543 */     if (pageAttr != null) {
/* 244:544 */       return pageAttr.intValue();
/* 245:    */     }
/* 246:547 */     String pageParam = request.getParameter("_page");
/* 247:548 */     if (pageParam != null) {
/* 248:549 */       return Integer.parseInt(pageParam);
/* 249:    */     }
/* 250:552 */     if (isSessionForm())
/* 251:    */     {
/* 252:553 */       pageAttr = (Integer)request.getSession().getAttribute(pageAttrName);
/* 253:554 */       if (pageAttr != null) {
/* 254:555 */         return pageAttr.intValue();
/* 255:    */       }
/* 256:    */     }
/* 257:558 */     throw new IllegalStateException(
/* 258:559 */       "Page attribute [" + pageAttrName + "] neither found in session nor in request");
/* 259:    */   }
/* 260:    */   
/* 261:    */   protected boolean isFinishRequest(HttpServletRequest request)
/* 262:    */   {
/* 263:576 */     return WebUtils.hasSubmitParameter(request, "_finish");
/* 264:    */   }
/* 265:    */   
/* 266:    */   protected boolean isCancelRequest(HttpServletRequest request)
/* 267:    */   {
/* 268:593 */     return WebUtils.hasSubmitParameter(request, "_cancel");
/* 269:    */   }
/* 270:    */   
/* 271:    */   protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage)
/* 272:    */   {
/* 273:609 */     return getTargetPage(request, currentPage);
/* 274:    */   }
/* 275:    */   
/* 276:    */   protected int getTargetPage(HttpServletRequest request, int currentPage)
/* 277:    */   {
/* 278:623 */     return WebUtils.getTargetPage(request, "_target", currentPage);
/* 279:    */   }
/* 280:    */   
/* 281:    */   private ModelAndView validatePagesAndFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors, int currentPage)
/* 282:    */     throws Exception
/* 283:    */   {
/* 284:635 */     if (errors.hasErrors()) {
/* 285:636 */       return showPage(request, errors, currentPage);
/* 286:    */     }
/* 287:639 */     if (!suppressValidation(request, command, errors)) {
/* 288:641 */       for (int page = 0; page < getPageCount(request, command); page++)
/* 289:    */       {
/* 290:642 */         validatePage(command, errors, page, true);
/* 291:643 */         if (errors.hasErrors()) {
/* 292:644 */           return showPage(request, errors, page);
/* 293:    */         }
/* 294:    */       }
/* 295:    */     }
/* 296:650 */     return processFinish(request, response, command, errors);
/* 297:    */   }
/* 298:    */   
/* 299:    */   protected void validatePage(Object command, Errors errors, int page, boolean finish)
/* 300:    */   {
/* 301:670 */     validatePage(command, errors, page);
/* 302:    */   }
/* 303:    */   
/* 304:    */   protected void validatePage(Object command, Errors errors, int page) {}
/* 305:    */   
/* 306:    */   protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page)
/* 307:    */     throws Exception
/* 308:    */   {}
/* 309:    */   
/* 310:    */   protected abstract ModelAndView processFinish(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, Object paramObject, BindException paramBindException)
/* 311:    */     throws Exception;
/* 312:    */   
/* 313:    */   protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
/* 314:    */     throws Exception
/* 315:    */   {
/* 316:747 */     throw new ServletException(
/* 317:748 */       "Wizard form controller class [" + getClass().getName() + "] does not support a cancel operation");
/* 318:    */   }
/* 319:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.AbstractWizardFormController
 * JD-Core Version:    0.7.0.1
 */