/*  1:   */ package org.springframework.web.servlet.mvc;
/*  2:   */ 
/*  3:   */ import javax.servlet.http.HttpServletRequest;
/*  4:   */ import javax.servlet.http.HttpServletResponse;
/*  5:   */ import org.apache.commons.logging.Log;
/*  6:   */ import org.springframework.util.Assert;
/*  7:   */ import org.springframework.web.servlet.ModelAndView;
/*  8:   */ import org.springframework.web.util.UrlPathHelper;
/*  9:   */ 
/* 10:   */ public abstract class AbstractUrlViewController
/* 11:   */   extends AbstractController
/* 12:   */ {
/* 13:41 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/* 14:   */   
/* 15:   */   public void setAlwaysUseFullPath(boolean alwaysUseFullPath)
/* 16:   */   {
/* 17:52 */     this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void setUrlDecode(boolean urlDecode)
/* 21:   */   {
/* 22:64 */     this.urlPathHelper.setUrlDecode(urlDecode);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public void setUrlPathHelper(UrlPathHelper urlPathHelper)
/* 26:   */   {
/* 27:75 */     Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
/* 28:76 */     this.urlPathHelper = urlPathHelper;
/* 29:   */   }
/* 30:   */   
/* 31:   */   protected UrlPathHelper getUrlPathHelper()
/* 32:   */   {
/* 33:83 */     return this.urlPathHelper;
/* 34:   */   }
/* 35:   */   
/* 36:   */   protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
/* 37:   */   {
/* 38:93 */     String lookupPath = getUrlPathHelper().getLookupPathForRequest(request);
/* 39:94 */     String viewName = getViewNameForRequest(request);
/* 40:95 */     if (this.logger.isDebugEnabled()) {
/* 41:96 */       this.logger.debug("Returning view name '" + viewName + "' for lookup path [" + lookupPath + "]");
/* 42:   */     }
/* 43:98 */     return new ModelAndView(viewName);
/* 44:   */   }
/* 45:   */   
/* 46:   */   protected abstract String getViewNameForRequest(HttpServletRequest paramHttpServletRequest);
/* 47:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.AbstractUrlViewController
 * JD-Core Version:    0.7.0.1
 */