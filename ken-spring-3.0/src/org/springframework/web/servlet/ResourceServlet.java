/*   1:    */ package org.springframework.web.servlet;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import javax.servlet.RequestDispatcher;
/*   5:    */ import javax.servlet.ServletException;
/*   6:    */ import javax.servlet.http.HttpServletRequest;
/*   7:    */ import javax.servlet.http.HttpServletResponse;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.springframework.util.AntPathMatcher;
/*  10:    */ import org.springframework.util.PathMatcher;
/*  11:    */ import org.springframework.util.StringUtils;
/*  12:    */ import org.springframework.web.context.support.ServletContextResource;
/*  13:    */ 
/*  14:    */ public class ResourceServlet
/*  15:    */   extends HttpServletBean
/*  16:    */ {
/*  17:    */   public static final String RESOURCE_URL_DELIMITERS = ",; \t\n";
/*  18:    */   public static final String RESOURCE_PARAM_NAME = "resource";
/*  19:    */   private String defaultUrl;
/*  20:    */   private String allowedResources;
/*  21:    */   private String contentType;
/*  22:111 */   private boolean applyLastModified = false;
/*  23:    */   private PathMatcher pathMatcher;
/*  24:    */   private long startupTime;
/*  25:    */   
/*  26:    */   public void setDefaultUrl(String defaultUrl)
/*  27:    */   {
/*  28:128 */     this.defaultUrl = defaultUrl;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setAllowedResources(String allowedResources)
/*  32:    */   {
/*  33:137 */     this.allowedResources = allowedResources;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setContentType(String contentType)
/*  37:    */   {
/*  38:149 */     this.contentType = contentType;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setApplyLastModified(boolean applyLastModified)
/*  42:    */   {
/*  43:164 */     this.applyLastModified = applyLastModified;
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected void initServletBean()
/*  47:    */   {
/*  48:173 */     this.pathMatcher = getPathMatcher();
/*  49:174 */     this.startupTime = System.currentTimeMillis();
/*  50:    */   }
/*  51:    */   
/*  52:    */   protected PathMatcher getPathMatcher()
/*  53:    */   {
/*  54:184 */     return new AntPathMatcher();
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected final void doGet(HttpServletRequest request, HttpServletResponse response)
/*  58:    */     throws ServletException, IOException
/*  59:    */   {
/*  60:197 */     String resourceUrl = determineResourceUrl(request);
/*  61:199 */     if (resourceUrl != null)
/*  62:    */     {
/*  63:    */       try
/*  64:    */       {
/*  65:201 */         doInclude(request, response, resourceUrl);
/*  66:    */       }
/*  67:    */       catch (ServletException ex)
/*  68:    */       {
/*  69:204 */         if (this.logger.isWarnEnabled()) {
/*  70:205 */           this.logger.warn("Failed to include content of resource [" + resourceUrl + "]", ex);
/*  71:    */         }
/*  72:208 */         if (includeDefaultUrl(request, response)) {
/*  73:    */           return;
/*  74:    */         }
/*  75:209 */         throw ex;
/*  76:    */       }
/*  77:    */       catch (IOException ex)
/*  78:    */       {
/*  79:213 */         if (this.logger.isWarnEnabled()) {
/*  80:214 */           this.logger.warn("Failed to include content of resource [" + resourceUrl + "]", ex);
/*  81:    */         }
/*  82:217 */         if (includeDefaultUrl(request, response)) {
/*  83:    */           return;
/*  84:    */         }
/*  85:    */       }
/*  86:218 */       throw ex;
/*  87:    */     }
/*  88:224 */     else if (!includeDefaultUrl(request, response))
/*  89:    */     {
/*  90:225 */       throw new ServletException("No target resource URL found for request");
/*  91:    */     }
/*  92:    */   }
/*  93:    */   
/*  94:    */   protected String determineResourceUrl(HttpServletRequest request)
/*  95:    */   {
/*  96:238 */     return request.getParameter("resource");
/*  97:    */   }
/*  98:    */   
/*  99:    */   private boolean includeDefaultUrl(HttpServletRequest request, HttpServletResponse response)
/* 100:    */     throws ServletException, IOException
/* 101:    */   {
/* 102:252 */     if (this.defaultUrl == null) {
/* 103:253 */       return false;
/* 104:    */     }
/* 105:255 */     doInclude(request, response, this.defaultUrl);
/* 106:256 */     return true;
/* 107:    */   }
/* 108:    */   
/* 109:    */   private void doInclude(HttpServletRequest request, HttpServletResponse response, String resourceUrl)
/* 110:    */     throws ServletException, IOException
/* 111:    */   {
/* 112:270 */     if (this.contentType != null) {
/* 113:271 */       response.setContentType(this.contentType);
/* 114:    */     }
/* 115:273 */     String[] resourceUrls = 
/* 116:274 */       StringUtils.tokenizeToStringArray(resourceUrl, ",; \t\n");
/* 117:275 */     for (int i = 0; i < resourceUrls.length; i++)
/* 118:    */     {
/* 119:277 */       if ((this.allowedResources != null) && (!this.pathMatcher.match(this.allowedResources, resourceUrls[i]))) {
/* 120:278 */         throw new ServletException("Resource [" + resourceUrls[i] + 
/* 121:279 */           "] does not match allowed pattern [" + this.allowedResources + "]");
/* 122:    */       }
/* 123:281 */       if (this.logger.isDebugEnabled()) {
/* 124:282 */         this.logger.debug("Including resource [" + resourceUrls[i] + "]");
/* 125:    */       }
/* 126:284 */       RequestDispatcher rd = request.getRequestDispatcher(resourceUrls[i]);
/* 127:285 */       rd.include(request, response);
/* 128:    */     }
/* 129:    */   }
/* 130:    */   
/* 131:    */   protected final long getLastModified(HttpServletRequest request)
/* 132:    */   {
/* 133:304 */     if (this.applyLastModified)
/* 134:    */     {
/* 135:305 */       String resourceUrl = determineResourceUrl(request);
/* 136:306 */       if (resourceUrl == null) {
/* 137:307 */         resourceUrl = this.defaultUrl;
/* 138:    */       }
/* 139:309 */       if (resourceUrl != null)
/* 140:    */       {
/* 141:310 */         String[] resourceUrls = StringUtils.tokenizeToStringArray(resourceUrl, ",; \t\n");
/* 142:311 */         long latestTimestamp = -1L;
/* 143:312 */         for (int i = 0; i < resourceUrls.length; i++)
/* 144:    */         {
/* 145:313 */           long timestamp = getFileTimestamp(resourceUrls[i]);
/* 146:314 */           if (timestamp > latestTimestamp) {
/* 147:315 */             latestTimestamp = timestamp;
/* 148:    */           }
/* 149:    */         }
/* 150:318 */         return latestTimestamp > this.startupTime ? latestTimestamp : this.startupTime;
/* 151:    */       }
/* 152:    */     }
/* 153:321 */     return -1L;
/* 154:    */   }
/* 155:    */   
/* 156:    */   protected long getFileTimestamp(String resourceUrl)
/* 157:    */   {
/* 158:330 */     ServletContextResource resource = new ServletContextResource(getServletContext(), resourceUrl);
/* 159:    */     try
/* 160:    */     {
/* 161:332 */       long lastModifiedTime = resource.lastModified();
/* 162:333 */       if (this.logger.isDebugEnabled()) {
/* 163:334 */         this.logger.debug("Last-modified timestamp of " + resource + " is " + lastModifiedTime);
/* 164:    */       }
/* 165:336 */       return lastModifiedTime;
/* 166:    */     }
/* 167:    */     catch (IOException localIOException)
/* 168:    */     {
/* 169:339 */       this.logger.warn("Couldn't retrieve last-modified timestamp of [" + resource + 
/* 170:340 */         "] - using ResourceServlet startup time");
/* 171:    */     }
/* 172:341 */     return -1L;
/* 173:    */   }
/* 174:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.ResourceServlet
 * JD-Core Version:    0.7.0.1
 */