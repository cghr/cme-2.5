/*   1:    */ package org.springframework.web.servlet.resource;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.List;
/*   5:    */ import javax.servlet.ServletContext;
/*   6:    */ import javax.servlet.ServletException;
/*   7:    */ import javax.servlet.http.HttpServletRequest;
/*   8:    */ import javax.servlet.http.HttpServletResponse;
/*   9:    */ import org.apache.commons.logging.Log;
/*  10:    */ import org.springframework.core.io.Resource;
/*  11:    */ import org.springframework.http.MediaType;
/*  12:    */ import org.springframework.util.Assert;
/*  13:    */ import org.springframework.util.FileCopyUtils;
/*  14:    */ import org.springframework.util.StringUtils;
/*  15:    */ import org.springframework.web.HttpRequestHandler;
/*  16:    */ import org.springframework.web.context.request.ServletWebRequest;
/*  17:    */ import org.springframework.web.servlet.HandlerMapping;
/*  18:    */ import org.springframework.web.servlet.support.WebContentGenerator;
/*  19:    */ 
/*  20:    */ public class ResourceHttpRequestHandler
/*  21:    */   extends WebContentGenerator
/*  22:    */   implements HttpRequestHandler
/*  23:    */ {
/*  24:    */   private List<Resource> locations;
/*  25:    */   
/*  26:    */   public ResourceHttpRequestHandler()
/*  27:    */   {
/*  28: 70 */     super(new String[] { "GET", "HEAD" });
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setLocations(List<Resource> locations)
/*  32:    */   {
/*  33: 78 */     Assert.notEmpty(locations, "Locations list must not be empty");
/*  34: 79 */     this.locations = locations;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void handleRequest(HttpServletRequest request, HttpServletResponse response)
/*  38:    */     throws ServletException, IOException
/*  39:    */   {
/*  40: 98 */     checkAndPrepare(request, response, true);
/*  41:    */     
/*  42:    */ 
/*  43:101 */     Resource resource = getResource(request);
/*  44:102 */     if (resource == null)
/*  45:    */     {
/*  46:103 */       this.logger.debug("No matching resource found - returning 404");
/*  47:104 */       response.sendError(404);
/*  48:105 */       return;
/*  49:    */     }
/*  50:109 */     MediaType mediaType = getMediaType(resource);
/*  51:110 */     if (mediaType != null)
/*  52:    */     {
/*  53:111 */       if (this.logger.isDebugEnabled()) {
/*  54:112 */         this.logger.debug("Determined media type [" + mediaType + "] for " + resource);
/*  55:    */       }
/*  56:    */     }
/*  57:    */     else
/*  58:    */     {
/*  59:116 */       if (this.logger.isDebugEnabled()) {
/*  60:117 */         this.logger.debug("No media type found for " + resource + " - returning 404");
/*  61:    */       }
/*  62:119 */       response.sendError(404);
/*  63:120 */       return;
/*  64:    */     }
/*  65:124 */     if (new ServletWebRequest(request, response).checkNotModified(resource.lastModified()))
/*  66:    */     {
/*  67:125 */       this.logger.debug("Resource not modified - returning 304");
/*  68:126 */       return;
/*  69:    */     }
/*  70:128 */     setHeaders(response, resource, mediaType);
/*  71:131 */     if ("HEAD".equals(request.getMethod()))
/*  72:    */     {
/*  73:132 */       this.logger.trace("HEAD request - skipping content");
/*  74:133 */       return;
/*  75:    */     }
/*  76:135 */     writeContent(response, resource);
/*  77:    */   }
/*  78:    */   
/*  79:    */   protected Resource getResource(HttpServletRequest request)
/*  80:    */   {
/*  81:139 */     String path = (String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
/*  82:140 */     if (path == null) {
/*  83:141 */       throw new IllegalStateException("Required request attribute '" + 
/*  84:142 */         HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE + "' is not set");
/*  85:    */     }
/*  86:145 */     if ((!StringUtils.hasText(path)) || (isInvalidPath(path)))
/*  87:    */     {
/*  88:146 */       if (this.logger.isDebugEnabled()) {
/*  89:147 */         this.logger.debug("Ignoring invalid resource path [" + path + "]");
/*  90:    */       }
/*  91:149 */       return null;
/*  92:    */     }
/*  93:152 */     for (Resource location : this.locations) {
/*  94:    */       try
/*  95:    */       {
/*  96:154 */         if (this.logger.isDebugEnabled()) {
/*  97:155 */           this.logger.debug("Trying relative path [" + path + "] against base location: " + location);
/*  98:    */         }
/*  99:157 */         Resource resource = location.createRelative(path);
/* 100:158 */         if ((resource.exists()) && (resource.isReadable()))
/* 101:    */         {
/* 102:159 */           if (this.logger.isDebugEnabled()) {
/* 103:160 */             this.logger.debug("Found matching resource: " + resource);
/* 104:    */           }
/* 105:162 */           return resource;
/* 106:    */         }
/* 107:164 */         if (this.logger.isTraceEnabled()) {
/* 108:165 */           this.logger.trace("Relative resource doesn't exist or isn't readable: " + resource);
/* 109:    */         }
/* 110:    */       }
/* 111:    */       catch (IOException ex)
/* 112:    */       {
/* 113:169 */         this.logger.debug("Failed to create relative resource - trying next resource location", ex);
/* 114:    */       }
/* 115:    */     }
/* 116:172 */     return null;
/* 117:    */   }
/* 118:    */   
/* 119:    */   protected boolean isInvalidPath(String path)
/* 120:    */   {
/* 121:183 */     return (path.contains("WEB-INF")) || (path.contains("META-INF")) || (StringUtils.cleanPath(path).startsWith(".."));
/* 122:    */   }
/* 123:    */   
/* 124:    */   protected MediaType getMediaType(Resource resource)
/* 125:    */   {
/* 126:192 */     String mimeType = getServletContext().getMimeType(resource.getFilename());
/* 127:193 */     return StringUtils.hasText(mimeType) ? MediaType.parseMediaType(mimeType) : null;
/* 128:    */   }
/* 129:    */   
/* 130:    */   protected void setHeaders(HttpServletResponse response, Resource resource, MediaType mediaType)
/* 131:    */     throws IOException
/* 132:    */   {
/* 133:205 */     long length = resource.contentLength();
/* 134:206 */     if (length > 2147483647L) {
/* 135:207 */       throw new IOException("Resource content too long (beyond Integer.MAX_VALUE): " + resource);
/* 136:    */     }
/* 137:209 */     response.setContentLength((int)length);
/* 138:210 */     response.setContentType(mediaType.toString());
/* 139:    */   }
/* 140:    */   
/* 141:    */   protected void writeContent(HttpServletResponse response, Resource resource)
/* 142:    */     throws IOException
/* 143:    */   {
/* 144:221 */     FileCopyUtils.copy(resource.getInputStream(), response.getOutputStream());
/* 145:    */   }
/* 146:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.resource.ResourceHttpRequestHandler
 * JD-Core Version:    0.7.0.1
 */