/*  1:   */ package org.springframework.web.multipart.support;
/*  2:   */ 
/*  3:   */ import javax.servlet.http.HttpServletRequest;
/*  4:   */ import javax.servlet.http.Part;
/*  5:   */ import org.apache.commons.logging.Log;
/*  6:   */ import org.apache.commons.logging.LogFactory;
/*  7:   */ import org.springframework.web.multipart.MultipartException;
/*  8:   */ import org.springframework.web.multipart.MultipartHttpServletRequest;
/*  9:   */ import org.springframework.web.multipart.MultipartResolver;
/* 10:   */ 
/* 11:   */ public class StandardServletMultipartResolver
/* 12:   */   implements MultipartResolver
/* 13:   */ {
/* 14:   */   public boolean isMultipart(HttpServletRequest request)
/* 15:   */   {
/* 16:50 */     if (!"post".equals(request.getMethod().toLowerCase())) {
/* 17:51 */       return false;
/* 18:   */     }
/* 19:53 */     String contentType = request.getContentType();
/* 20:54 */     return (contentType != null) && (contentType.toLowerCase().startsWith("multipart/"));
/* 21:   */   }
/* 22:   */   
/* 23:   */   public MultipartHttpServletRequest resolveMultipart(HttpServletRequest request)
/* 24:   */     throws MultipartException
/* 25:   */   {
/* 26:58 */     return new StandardMultipartHttpServletRequest(request);
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void cleanupMultipart(MultipartHttpServletRequest request)
/* 30:   */   {
/* 31:   */     try
/* 32:   */     {
/* 33:64 */       for (Part part : request.getParts()) {
/* 34:65 */         part.delete();
/* 35:   */       }
/* 36:   */     }
/* 37:   */     catch (Exception ex)
/* 38:   */     {
/* 39:69 */       LogFactory.getLog(getClass()).warn("Failed to perform cleanup of multipart items", ex);
/* 40:   */     }
/* 41:   */   }
/* 42:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.multipart.support.StandardServletMultipartResolver
 * JD-Core Version:    0.7.0.1
 */