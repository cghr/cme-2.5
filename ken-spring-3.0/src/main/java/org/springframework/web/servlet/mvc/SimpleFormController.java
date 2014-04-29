/*   1:    */ package org.springframework.web.servlet.mvc;
/*   2:    */ 
/*   3:    */ import java.util.Map;
/*   4:    */ import javax.servlet.ServletException;
/*   5:    */ import javax.servlet.http.HttpServletRequest;
/*   6:    */ import javax.servlet.http.HttpServletResponse;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.springframework.validation.BindException;
/*   9:    */ import org.springframework.validation.Errors;
/*  10:    */ import org.springframework.web.servlet.ModelAndView;
/*  11:    */ 
/*  12:    */ @Deprecated
/*  13:    */ public class SimpleFormController
/*  14:    */   extends AbstractFormController
/*  15:    */ {
/*  16:    */   private String formView;
/*  17:    */   private String successView;
/*  18:    */   
/*  19:    */   public final void setFormView(String formView)
/*  20:    */   {
/*  21:132 */     this.formView = formView;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public final String getFormView()
/*  25:    */   {
/*  26:139 */     return this.formView;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public final void setSuccessView(String successView)
/*  30:    */   {
/*  31:146 */     this.successView = successView;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public final String getSuccessView()
/*  35:    */   {
/*  36:153 */     return this.successView;
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors)
/*  40:    */     throws Exception
/*  41:    */   {
/*  42:178 */     return showForm(request, response, errors, null);
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors, Map controlModel)
/*  46:    */     throws Exception
/*  47:    */   {
/*  48:201 */     return showForm(request, errors, getFormView(), controlModel);
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected Map referenceData(HttpServletRequest request, Object command, Errors errors)
/*  52:    */     throws Exception
/*  53:    */   {
/*  54:218 */     return referenceData(request);
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected Map referenceData(HttpServletRequest request)
/*  58:    */     throws Exception
/*  59:    */   {
/*  60:234 */     return null;
/*  61:    */   }
/*  62:    */   
/*  63:    */   protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
/*  64:    */     throws Exception
/*  65:    */   {
/*  66:259 */     if (errors.hasErrors())
/*  67:    */     {
/*  68:260 */       if (this.logger.isDebugEnabled()) {
/*  69:261 */         this.logger.debug("Data binding errors: " + errors.getErrorCount());
/*  70:    */       }
/*  71:263 */       return showForm(request, response, errors);
/*  72:    */     }
/*  73:265 */     if (isFormChangeRequest(request, command))
/*  74:    */     {
/*  75:266 */       this.logger.debug("Detected form change request -> routing request to onFormChange");
/*  76:267 */       onFormChange(request, response, command, errors);
/*  77:268 */       return showForm(request, response, errors);
/*  78:    */     }
/*  79:271 */     this.logger.debug("No errors -> processing submit");
/*  80:272 */     return onSubmit(request, response, command, errors);
/*  81:    */   }
/*  82:    */   
/*  83:    */   protected boolean suppressValidation(HttpServletRequest request, Object command)
/*  84:    */   {
/*  85:284 */     return isFormChangeRequest(request, command);
/*  86:    */   }
/*  87:    */   
/*  88:    */   protected boolean isFormChangeRequest(HttpServletRequest request, Object command)
/*  89:    */   {
/*  90:303 */     return isFormChangeRequest(request);
/*  91:    */   }
/*  92:    */   
/*  93:    */   protected boolean isFormChangeRequest(HttpServletRequest request)
/*  94:    */   {
/*  95:316 */     return false;
/*  96:    */   }
/*  97:    */   
/*  98:    */   protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
/*  99:    */     throws Exception
/* 100:    */   {
/* 101:339 */     onFormChange(request, response, command);
/* 102:    */   }
/* 103:    */   
/* 104:    */   protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object command)
/* 105:    */     throws Exception
/* 106:    */   {}
/* 107:    */   
/* 108:    */   protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
/* 109:    */     throws Exception
/* 110:    */   {
/* 111:387 */     return onSubmit(command, errors);
/* 112:    */   }
/* 113:    */   
/* 114:    */   protected ModelAndView onSubmit(Object command, BindException errors)
/* 115:    */     throws Exception
/* 116:    */   {
/* 117:415 */     ModelAndView mv = onSubmit(command);
/* 118:416 */     if (mv != null) {
/* 119:418 */       return mv;
/* 120:    */     }
/* 121:422 */     if (getSuccessView() == null) {
/* 122:423 */       throw new ServletException("successView isn't set");
/* 123:    */     }
/* 124:425 */     return new ModelAndView(getSuccessView(), errors.getModel());
/* 125:    */   }
/* 126:    */   
/* 127:    */   protected ModelAndView onSubmit(Object command)
/* 128:    */     throws Exception
/* 129:    */   {
/* 130:449 */     doSubmitAction(command);
/* 131:450 */     return null;
/* 132:    */   }
/* 133:    */   
/* 134:    */   protected void doSubmitAction(Object command)
/* 135:    */     throws Exception
/* 136:    */   {}
/* 137:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.SimpleFormController
 * JD-Core Version:    0.7.0.1
 */