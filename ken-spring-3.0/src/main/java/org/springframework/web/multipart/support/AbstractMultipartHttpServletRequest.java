/*   1:    */ package org.springframework.web.multipart.support;
/*   2:    */ 
/*   3:    */ import java.util.Collections;
/*   4:    */ import java.util.Enumeration;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Set;
/*   9:    */ import javax.servlet.http.HttpServletRequest;
/*  10:    */ import javax.servlet.http.HttpServletRequestWrapper;
/*  11:    */ import org.springframework.http.HttpHeaders;
/*  12:    */ import org.springframework.http.HttpMethod;
/*  13:    */ import org.springframework.util.LinkedMultiValueMap;
/*  14:    */ import org.springframework.util.MultiValueMap;
/*  15:    */ import org.springframework.web.multipart.MultipartFile;
/*  16:    */ import org.springframework.web.multipart.MultipartHttpServletRequest;
/*  17:    */ 
/*  18:    */ public abstract class AbstractMultipartHttpServletRequest
/*  19:    */   extends HttpServletRequestWrapper
/*  20:    */   implements MultipartHttpServletRequest
/*  21:    */ {
/*  22:    */   private MultiValueMap<String, MultipartFile> multipartFiles;
/*  23:    */   
/*  24:    */   protected AbstractMultipartHttpServletRequest(HttpServletRequest request)
/*  25:    */   {
/*  26: 53 */     super(request);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public HttpServletRequest getRequest()
/*  30:    */   {
/*  31: 59 */     return (HttpServletRequest)super.getRequest();
/*  32:    */   }
/*  33:    */   
/*  34:    */   public HttpMethod getRequestMethod()
/*  35:    */   {
/*  36: 63 */     return HttpMethod.valueOf(getRequest().getMethod());
/*  37:    */   }
/*  38:    */   
/*  39:    */   public HttpHeaders getRequestHeaders()
/*  40:    */   {
/*  41: 67 */     HttpHeaders headers = new HttpHeaders();
/*  42: 68 */     Enumeration<String> headerNames = getHeaderNames();
/*  43: 69 */     while (headerNames.hasMoreElements())
/*  44:    */     {
/*  45: 70 */       String headerName = (String)headerNames.nextElement();
/*  46: 71 */       headers.put(headerName, (List)Collections.list(getHeaders(headerName)));
/*  47:    */     }
/*  48: 73 */     return headers;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public Iterator<String> getFileNames()
/*  52:    */   {
/*  53: 77 */     return getMultipartFiles().keySet().iterator();
/*  54:    */   }
/*  55:    */   
/*  56:    */   public MultipartFile getFile(String name)
/*  57:    */   {
/*  58: 81 */     return (MultipartFile)getMultipartFiles().getFirst(name);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public List<MultipartFile> getFiles(String name)
/*  62:    */   {
/*  63: 85 */     List<MultipartFile> multipartFiles = (List)getMultipartFiles().get(name);
/*  64: 86 */     if (multipartFiles != null) {
/*  65: 87 */       return multipartFiles;
/*  66:    */     }
/*  67: 90 */     return Collections.emptyList();
/*  68:    */   }
/*  69:    */   
/*  70:    */   public Map<String, MultipartFile> getFileMap()
/*  71:    */   {
/*  72: 95 */     return getMultipartFiles().toSingleValueMap();
/*  73:    */   }
/*  74:    */   
/*  75:    */   public MultiValueMap<String, MultipartFile> getMultiFileMap()
/*  76:    */   {
/*  77: 99 */     return getMultipartFiles();
/*  78:    */   }
/*  79:    */   
/*  80:    */   protected final void setMultipartFiles(MultiValueMap<String, MultipartFile> multipartFiles)
/*  81:    */   {
/*  82:108 */     this.multipartFiles = 
/*  83:109 */       new LinkedMultiValueMap(Collections.unmodifiableMap(multipartFiles));
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected MultiValueMap<String, MultipartFile> getMultipartFiles()
/*  87:    */   {
/*  88:118 */     if (this.multipartFiles == null) {
/*  89:119 */       initializeMultipart();
/*  90:    */     }
/*  91:121 */     return this.multipartFiles;
/*  92:    */   }
/*  93:    */   
/*  94:    */   protected void initializeMultipart()
/*  95:    */   {
/*  96:129 */     throw new IllegalStateException("Multipart request not initialized");
/*  97:    */   }
/*  98:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.multipart.support.AbstractMultipartHttpServletRequest
 * JD-Core Version:    0.7.0.1
 */