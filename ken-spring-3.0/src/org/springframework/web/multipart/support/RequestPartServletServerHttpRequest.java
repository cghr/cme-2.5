/*   1:    */ package org.springframework.web.multipart.support;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import javax.servlet.http.HttpServletRequest;
/*   7:    */ import javax.servlet.http.Part;
/*   8:    */ import org.springframework.http.HttpHeaders;
/*   9:    */ import org.springframework.http.server.ServletServerHttpRequest;
/*  10:    */ import org.springframework.util.ClassUtils;
/*  11:    */ import org.springframework.web.multipart.MultipartException;
/*  12:    */ import org.springframework.web.multipart.MultipartFile;
/*  13:    */ import org.springframework.web.multipart.MultipartHttpServletRequest;
/*  14:    */ 
/*  15:    */ public class RequestPartServletServerHttpRequest
/*  16:    */   extends ServletServerHttpRequest
/*  17:    */ {
/*  18:    */   private final MultipartHttpServletRequest multipartRequest;
/*  19:    */   private final String partName;
/*  20:    */   private final HttpHeaders headers;
/*  21:    */   
/*  22:    */   public RequestPartServletServerHttpRequest(HttpServletRequest request, String partName)
/*  23:    */     throws MissingServletRequestPartException
/*  24:    */   {
/*  25: 63 */     super(request);
/*  26:    */     
/*  27: 65 */     this.multipartRequest = asMultipartRequest(request);
/*  28: 66 */     this.partName = partName;
/*  29:    */     
/*  30: 68 */     this.headers = this.multipartRequest.getMultipartHeaders(this.partName);
/*  31: 69 */     if (this.headers == null)
/*  32:    */     {
/*  33: 70 */       if ((request instanceof MultipartHttpServletRequest)) {
/*  34: 71 */         throw new MissingServletRequestPartException(partName);
/*  35:    */       }
/*  36: 74 */       throw new IllegalArgumentException(
/*  37: 75 */         "Failed to obtain request part: " + partName + ". " + 
/*  38: 76 */         "The part is missing or multipart processing is not configured. " + 
/*  39: 77 */         "Check for a MultipartResolver bean or if Servlet 3.0 multipart processing is enabled.");
/*  40:    */     }
/*  41:    */   }
/*  42:    */   
/*  43:    */   private static MultipartHttpServletRequest asMultipartRequest(HttpServletRequest request)
/*  44:    */   {
/*  45: 83 */     if ((request instanceof MultipartHttpServletRequest)) {
/*  46: 84 */       return (MultipartHttpServletRequest)request;
/*  47:    */     }
/*  48: 86 */     if (ClassUtils.hasMethod(HttpServletRequest.class, "getParts", new Class[0])) {
/*  49: 88 */       return new StandardMultipartHttpServletRequest(request);
/*  50:    */     }
/*  51: 90 */     throw new IllegalArgumentException("Expected MultipartHttpServletRequest: is a MultipartResolver configured?");
/*  52:    */   }
/*  53:    */   
/*  54:    */   public HttpHeaders getHeaders()
/*  55:    */   {
/*  56: 95 */     return this.headers;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public InputStream getBody()
/*  60:    */     throws IOException
/*  61:    */   {
/*  62:100 */     if ((this.multipartRequest instanceof StandardMultipartHttpServletRequest)) {
/*  63:    */       try
/*  64:    */       {
/*  65:102 */         return this.multipartRequest.getPart(this.partName).getInputStream();
/*  66:    */       }
/*  67:    */       catch (Exception ex)
/*  68:    */       {
/*  69:105 */         throw new MultipartException("Could not parse multipart servlet request", ex);
/*  70:    */       }
/*  71:    */     }
/*  72:109 */     MultipartFile file = this.multipartRequest.getFile(this.partName);
/*  73:110 */     if (file != null) {
/*  74:111 */       return file.getInputStream();
/*  75:    */     }
/*  76:114 */     String paramValue = this.multipartRequest.getParameter(this.partName);
/*  77:115 */     return new ByteArrayInputStream(paramValue.getBytes("UTF-8"));
/*  78:    */   }
/*  79:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.multipart.support.RequestPartServletServerHttpRequest
 * JD-Core Version:    0.7.0.1
 */