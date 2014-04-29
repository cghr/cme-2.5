/*   1:    */ package org.springframework.web.servlet.mvc.multiaction;
/*   2:    */ 
/*   3:    */ import java.util.Map;
/*   4:    */ import java.util.concurrent.ConcurrentHashMap;
/*   5:    */ import org.springframework.web.util.WebUtils;
/*   6:    */ 
/*   7:    */ public class InternalPathMethodNameResolver
/*   8:    */   extends AbstractUrlMethodNameResolver
/*   9:    */ {
/*  10: 41 */   private String prefix = "";
/*  11: 43 */   private String suffix = "";
/*  12: 46 */   private final Map<String, String> methodNameCache = new ConcurrentHashMap();
/*  13:    */   
/*  14:    */   public void setPrefix(String prefix)
/*  15:    */   {
/*  16: 55 */     this.prefix = (prefix != null ? prefix : "");
/*  17:    */   }
/*  18:    */   
/*  19:    */   protected String getPrefix()
/*  20:    */   {
/*  21: 62 */     return this.prefix;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void setSuffix(String suffix)
/*  25:    */   {
/*  26: 71 */     this.suffix = (suffix != null ? suffix : "");
/*  27:    */   }
/*  28:    */   
/*  29:    */   protected String getSuffix()
/*  30:    */   {
/*  31: 78 */     return this.suffix;
/*  32:    */   }
/*  33:    */   
/*  34:    */   protected String getHandlerMethodNameForUrlPath(String urlPath)
/*  35:    */   {
/*  36: 89 */     String methodName = (String)this.methodNameCache.get(urlPath);
/*  37: 90 */     if (methodName == null)
/*  38:    */     {
/*  39: 91 */       methodName = extractHandlerMethodNameFromUrlPath(urlPath);
/*  40: 92 */       methodName = postProcessHandlerMethodName(methodName);
/*  41: 93 */       this.methodNameCache.put(urlPath, methodName);
/*  42:    */     }
/*  43: 95 */     return methodName;
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected String extractHandlerMethodNameFromUrlPath(String uri)
/*  47:    */   {
/*  48:106 */     return WebUtils.extractFilenameFromUrlPath(uri);
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected String postProcessHandlerMethodName(String methodName)
/*  52:    */   {
/*  53:121 */     return getPrefix() + methodName + getSuffix();
/*  54:    */   }
/*  55:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.multiaction.InternalPathMethodNameResolver
 * JD-Core Version:    0.7.0.1
 */