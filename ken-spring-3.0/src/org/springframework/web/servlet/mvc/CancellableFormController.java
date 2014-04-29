/*   1:    */ package org.springframework.web.servlet.mvc;
/*   2:    */ 
/*   3:    */ import javax.servlet.http.HttpServletRequest;
/*   4:    */ import javax.servlet.http.HttpServletResponse;
/*   5:    */ import org.springframework.validation.BindException;
/*   6:    */ import org.springframework.web.servlet.ModelAndView;
/*   7:    */ import org.springframework.web.util.WebUtils;
/*   8:    */ 
/*   9:    */ @Deprecated
/*  10:    */ public class CancellableFormController
/*  11:    */   extends SimpleFormController
/*  12:    */ {
/*  13:    */   private static final String PARAM_CANCEL = "_cancel";
/*  14: 72 */   private String cancelParamKey = "_cancel";
/*  15:    */   private String cancelView;
/*  16:    */   
/*  17:    */   public final void setCancelParamKey(String cancelParamKey)
/*  18:    */   {
/*  19: 84 */     this.cancelParamKey = cancelParamKey;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public final String getCancelParamKey()
/*  23:    */   {
/*  24: 91 */     return this.cancelParamKey;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public final void setCancelView(String cancelView)
/*  28:    */   {
/*  29: 98 */     this.cancelView = cancelView;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public final String getCancelView()
/*  33:    */   {
/*  34:105 */     return this.cancelView;
/*  35:    */   }
/*  36:    */   
/*  37:    */   protected boolean isFormSubmission(HttpServletRequest request)
/*  38:    */   {
/*  39:115 */     return (super.isFormSubmission(request)) || (isCancelRequest(request));
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected boolean suppressValidation(HttpServletRequest request, Object command)
/*  43:    */   {
/*  44:124 */     return (super.suppressValidation(request, command)) || (isCancelRequest(request));
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
/*  48:    */     throws Exception
/*  49:    */   {
/*  50:141 */     if (isCancelRequest(request)) {
/*  51:142 */       return onCancel(request, response, command);
/*  52:    */     }
/*  53:145 */     return super.processFormSubmission(request, response, command, errors);
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected boolean isCancelRequest(HttpServletRequest request)
/*  57:    */   {
/*  58:163 */     return WebUtils.hasSubmitParameter(request, getCancelParamKey());
/*  59:    */   }
/*  60:    */   
/*  61:    */   protected ModelAndView onCancel(HttpServletRequest request, HttpServletResponse response, Object command)
/*  62:    */     throws Exception
/*  63:    */   {
/*  64:188 */     return onCancel(command);
/*  65:    */   }
/*  66:    */   
/*  67:    */   protected ModelAndView onCancel(Object command)
/*  68:    */     throws Exception
/*  69:    */   {
/*  70:207 */     return new ModelAndView(getCancelView());
/*  71:    */   }
/*  72:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.CancellableFormController
 * JD-Core Version:    0.7.0.1
 */