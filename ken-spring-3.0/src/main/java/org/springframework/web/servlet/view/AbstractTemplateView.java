/*   1:    */ package org.springframework.web.servlet.view;
/*   2:    */ 
/*   3:    */ import java.util.Enumeration;
/*   4:    */ import java.util.Map;
/*   5:    */ import javax.servlet.ServletException;
/*   6:    */ import javax.servlet.http.HttpServletRequest;
/*   7:    */ import javax.servlet.http.HttpServletResponse;
/*   8:    */ import javax.servlet.http.HttpSession;
/*   9:    */ import org.apache.commons.logging.Log;
/*  10:    */ import org.springframework.web.servlet.support.RequestContext;
/*  11:    */ 
/*  12:    */ public abstract class AbstractTemplateView
/*  13:    */   extends AbstractUrlBasedView
/*  14:    */ {
/*  15:    */   public static final String SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE = "springMacroRequestContext";
/*  16: 55 */   private boolean exposeRequestAttributes = false;
/*  17: 57 */   private boolean allowRequestOverride = false;
/*  18: 59 */   private boolean exposeSessionAttributes = false;
/*  19: 61 */   private boolean allowSessionOverride = false;
/*  20: 63 */   private boolean exposeSpringMacroHelpers = true;
/*  21:    */   
/*  22:    */   public void setExposeRequestAttributes(boolean exposeRequestAttributes)
/*  23:    */   {
/*  24: 71 */     this.exposeRequestAttributes = exposeRequestAttributes;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void setAllowRequestOverride(boolean allowRequestOverride)
/*  28:    */   {
/*  29: 81 */     this.allowRequestOverride = allowRequestOverride;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setExposeSessionAttributes(boolean exposeSessionAttributes)
/*  33:    */   {
/*  34: 89 */     this.exposeSessionAttributes = exposeSessionAttributes;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setAllowSessionOverride(boolean allowSessionOverride)
/*  38:    */   {
/*  39: 99 */     this.allowSessionOverride = allowSessionOverride;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setExposeSpringMacroHelpers(boolean exposeSpringMacroHelpers)
/*  43:    */   {
/*  44:111 */     this.exposeSpringMacroHelpers = exposeSpringMacroHelpers;
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*  48:    */     throws Exception
/*  49:    */   {
/*  50:119 */     if (this.exposeRequestAttributes) {
/*  51:120 */       for (Enumeration en = request.getAttributeNames(); en.hasMoreElements();)
/*  52:    */       {
/*  53:121 */         String attribute = (String)en.nextElement();
/*  54:122 */         if ((model.containsKey(attribute)) && (!this.allowRequestOverride)) {
/*  55:123 */           throw new ServletException("Cannot expose request attribute '" + attribute + 
/*  56:124 */             "' because of an existing model object of the same name");
/*  57:    */         }
/*  58:126 */         Object attributeValue = request.getAttribute(attribute);
/*  59:127 */         if (this.logger.isDebugEnabled()) {
/*  60:128 */           this.logger.debug("Exposing request attribute '" + attribute + 
/*  61:129 */             "' with value [" + attributeValue + "] to model");
/*  62:    */         }
/*  63:131 */         model.put(attribute, attributeValue);
/*  64:    */       }
/*  65:    */     }
/*  66:135 */     if (this.exposeSessionAttributes)
/*  67:    */     {
/*  68:136 */       HttpSession session = request.getSession(false);
/*  69:137 */       if (session != null) {
/*  70:138 */         for (Enumeration en = session.getAttributeNames(); en.hasMoreElements();)
/*  71:    */         {
/*  72:139 */           String attribute = (String)en.nextElement();
/*  73:140 */           if ((model.containsKey(attribute)) && (!this.allowSessionOverride)) {
/*  74:141 */             throw new ServletException("Cannot expose session attribute '" + attribute + 
/*  75:142 */               "' because of an existing model object of the same name");
/*  76:    */           }
/*  77:144 */           Object attributeValue = session.getAttribute(attribute);
/*  78:145 */           if (this.logger.isDebugEnabled()) {
/*  79:146 */             this.logger.debug("Exposing session attribute '" + attribute + 
/*  80:147 */               "' with value [" + attributeValue + "] to model");
/*  81:    */           }
/*  82:149 */           model.put(attribute, attributeValue);
/*  83:    */         }
/*  84:    */       }
/*  85:    */     }
/*  86:154 */     if (this.exposeSpringMacroHelpers)
/*  87:    */     {
/*  88:155 */       if (model.containsKey("springMacroRequestContext")) {
/*  89:156 */         throw new ServletException(
/*  90:157 */           "Cannot expose bind macro helper 'springMacroRequestContext' because of an existing model object of the same name");
/*  91:    */       }
/*  92:161 */       model.put("springMacroRequestContext", 
/*  93:162 */         new RequestContext(request, response, getServletContext(), model));
/*  94:    */     }
/*  95:165 */     applyContentType(response);
/*  96:    */     
/*  97:167 */     renderMergedTemplateModel(model, request, response);
/*  98:    */   }
/*  99:    */   
/* 100:    */   protected void applyContentType(HttpServletResponse response)
/* 101:    */   {
/* 102:180 */     if (response.getContentType() == null) {
/* 103:181 */       response.setContentType(getContentType());
/* 104:    */     }
/* 105:    */   }
/* 106:    */   
/* 107:    */   protected abstract void renderMergedTemplateModel(Map<String, Object> paramMap, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/* 108:    */     throws Exception;
/* 109:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.AbstractTemplateView
 * JD-Core Version:    0.7.0.1
 */