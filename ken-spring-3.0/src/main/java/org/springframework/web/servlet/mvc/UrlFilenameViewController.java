/*   1:    */ package org.springframework.web.servlet.mvc;
/*   2:    */ 
/*   3:    */ import java.util.Map;
/*   4:    */ import java.util.concurrent.ConcurrentHashMap;
/*   5:    */ import javax.servlet.http.HttpServletRequest;
/*   6:    */ import org.springframework.util.StringUtils;
/*   7:    */ import org.springframework.web.servlet.HandlerMapping;
/*   8:    */ import org.springframework.web.util.UrlPathHelper;
/*   9:    */ 
/*  10:    */ public class UrlFilenameViewController
/*  11:    */   extends AbstractUrlViewController
/*  12:    */ {
/*  13: 52 */   private String prefix = "";
/*  14: 54 */   private String suffix = "";
/*  15: 57 */   private final Map<String, String> viewNameCache = new ConcurrentHashMap();
/*  16:    */   
/*  17:    */   public void setPrefix(String prefix)
/*  18:    */   {
/*  19: 65 */     this.prefix = (prefix != null ? prefix : "");
/*  20:    */   }
/*  21:    */   
/*  22:    */   protected String getPrefix()
/*  23:    */   {
/*  24: 72 */     return this.prefix;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void setSuffix(String suffix)
/*  28:    */   {
/*  29: 80 */     this.suffix = (suffix != null ? suffix : "");
/*  30:    */   }
/*  31:    */   
/*  32:    */   protected String getSuffix()
/*  33:    */   {
/*  34: 87 */     return this.suffix;
/*  35:    */   }
/*  36:    */   
/*  37:    */   protected String getViewNameForRequest(HttpServletRequest request)
/*  38:    */   {
/*  39:100 */     String uri = extractOperableUrl(request);
/*  40:101 */     return getViewNameForUrlPath(uri);
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected String extractOperableUrl(HttpServletRequest request)
/*  44:    */   {
/*  45:111 */     String urlPath = (String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
/*  46:112 */     if (!StringUtils.hasText(urlPath)) {
/*  47:113 */       urlPath = getUrlPathHelper().getLookupPathForRequest(request);
/*  48:    */     }
/*  49:115 */     return urlPath;
/*  50:    */   }
/*  51:    */   
/*  52:    */   protected String getViewNameForUrlPath(String uri)
/*  53:    */   {
/*  54:127 */     String viewName = (String)this.viewNameCache.get(uri);
/*  55:128 */     if (viewName == null)
/*  56:    */     {
/*  57:129 */       viewName = extractViewNameFromUrlPath(uri);
/*  58:130 */       viewName = postProcessViewName(viewName);
/*  59:131 */       this.viewNameCache.put(uri, viewName);
/*  60:    */     }
/*  61:133 */     return viewName;
/*  62:    */   }
/*  63:    */   
/*  64:    */   protected String extractViewNameFromUrlPath(String uri)
/*  65:    */   {
/*  66:142 */     int start = uri.charAt(0) == '/' ? 1 : 0;
/*  67:143 */     int lastIndex = uri.lastIndexOf(".");
/*  68:144 */     int end = lastIndex < 0 ? uri.length() : lastIndex;
/*  69:145 */     return uri.substring(start, end);
/*  70:    */   }
/*  71:    */   
/*  72:    */   protected String postProcessViewName(String viewName)
/*  73:    */   {
/*  74:160 */     return getPrefix() + viewName + getSuffix();
/*  75:    */   }
/*  76:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.UrlFilenameViewController
 * JD-Core Version:    0.7.0.1
 */