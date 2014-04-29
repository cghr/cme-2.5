/*   1:    */ package org.springframework.web.servlet.view;
/*   2:    */ 
/*   3:    */ import javax.servlet.http.HttpServletRequest;
/*   4:    */ import org.springframework.util.Assert;
/*   5:    */ import org.springframework.util.StringUtils;
/*   6:    */ import org.springframework.web.servlet.RequestToViewNameTranslator;
/*   7:    */ import org.springframework.web.util.UrlPathHelper;
/*   8:    */ 
/*   9:    */ public class DefaultRequestToViewNameTranslator
/*  10:    */   implements RequestToViewNameTranslator
/*  11:    */ {
/*  12:    */   private static final String SLASH = "/";
/*  13: 61 */   private String prefix = "";
/*  14: 63 */   private String suffix = "";
/*  15: 65 */   private String separator = "/";
/*  16: 67 */   private boolean stripLeadingSlash = true;
/*  17: 69 */   private boolean stripTrailingSlash = true;
/*  18: 71 */   private boolean stripExtension = true;
/*  19: 73 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/*  20:    */   
/*  21:    */   public void setPrefix(String prefix)
/*  22:    */   {
/*  23: 81 */     this.prefix = (prefix != null ? prefix : "");
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setSuffix(String suffix)
/*  27:    */   {
/*  28: 89 */     this.suffix = (suffix != null ? suffix : "");
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setSeparator(String separator)
/*  32:    */   {
/*  33: 98 */     this.separator = separator;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setStripLeadingSlash(boolean stripLeadingSlash)
/*  37:    */   {
/*  38:106 */     this.stripLeadingSlash = stripLeadingSlash;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setStripTrailingSlash(boolean stripTrailingSlash)
/*  42:    */   {
/*  43:114 */     this.stripTrailingSlash = stripTrailingSlash;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setStripExtension(boolean stripExtension)
/*  47:    */   {
/*  48:122 */     this.stripExtension = stripExtension;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setAlwaysUseFullPath(boolean alwaysUseFullPath)
/*  52:    */   {
/*  53:133 */     this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setUrlDecode(boolean urlDecode)
/*  57:    */   {
/*  58:145 */     this.urlPathHelper.setUrlDecode(urlDecode);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void setUrlPathHelper(UrlPathHelper urlPathHelper)
/*  62:    */   {
/*  63:155 */     Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
/*  64:156 */     this.urlPathHelper = urlPathHelper;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public String getViewName(HttpServletRequest request)
/*  68:    */   {
/*  69:167 */     String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
/*  70:168 */     return this.prefix + transformPath(lookupPath) + this.suffix;
/*  71:    */   }
/*  72:    */   
/*  73:    */   protected String transformPath(String lookupPath)
/*  74:    */   {
/*  75:180 */     String path = lookupPath;
/*  76:181 */     if ((this.stripLeadingSlash) && (path.startsWith("/"))) {
/*  77:182 */       path = path.substring(1);
/*  78:    */     }
/*  79:184 */     if ((this.stripTrailingSlash) && (path.endsWith("/"))) {
/*  80:185 */       path = path.substring(0, path.length() - 1);
/*  81:    */     }
/*  82:187 */     if (this.stripExtension) {
/*  83:188 */       path = StringUtils.stripFilenameExtension(path);
/*  84:    */     }
/*  85:190 */     if (!"/".equals(this.separator)) {
/*  86:191 */       path = StringUtils.replace(path, "/", this.separator);
/*  87:    */     }
/*  88:193 */     return path;
/*  89:    */   }
/*  90:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator
 * JD-Core Version:    0.7.0.1
 */