/*  1:   */ package org.springframework.web.servlet.mvc.multiaction;
/*  2:   */ 
/*  3:   */ import javax.servlet.http.HttpServletRequest;
/*  4:   */ import org.apache.commons.logging.Log;
/*  5:   */ import org.apache.commons.logging.LogFactory;
/*  6:   */ import org.springframework.util.Assert;
/*  7:   */ import org.springframework.web.util.UrlPathHelper;
/*  8:   */ 
/*  9:   */ public abstract class AbstractUrlMethodNameResolver
/* 10:   */   implements MethodNameResolver
/* 11:   */ {
/* 12:41 */   protected final Log logger = LogFactory.getLog(getClass());
/* 13:43 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/* 14:   */   
/* 15:   */   public void setAlwaysUseFullPath(boolean alwaysUseFullPath)
/* 16:   */   {
/* 17:54 */     this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void setUrlDecode(boolean urlDecode)
/* 21:   */   {
/* 22:66 */     this.urlPathHelper.setUrlDecode(urlDecode);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public void setUrlPathHelper(UrlPathHelper urlPathHelper)
/* 26:   */   {
/* 27:77 */     Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
/* 28:78 */     this.urlPathHelper = urlPathHelper;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public final String getHandlerMethodName(HttpServletRequest request)
/* 32:   */     throws NoSuchRequestHandlingMethodException
/* 33:   */   {
/* 34:91 */     String urlPath = this.urlPathHelper.getLookupPathForRequest(request);
/* 35:92 */     String name = getHandlerMethodNameForUrlPath(urlPath);
/* 36:93 */     if (name == null) {
/* 37:94 */       throw new NoSuchRequestHandlingMethodException(urlPath, request.getMethod(), request.getParameterMap());
/* 38:   */     }
/* 39:96 */     if (this.logger.isDebugEnabled()) {
/* 40:97 */       this.logger.debug("Returning handler method name '" + name + "' for lookup path: " + urlPath);
/* 41:   */     }
/* 42:99 */     return name;
/* 43:   */   }
/* 44:   */   
/* 45:   */   protected abstract String getHandlerMethodNameForUrlPath(String paramString);
/* 46:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.multiaction.AbstractUrlMethodNameResolver
 * JD-Core Version:    0.7.0.1
 */