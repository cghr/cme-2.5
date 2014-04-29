/*   1:    */ package org.springframework.web.multipart.support;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.Collection;
/*   8:    */ import javax.servlet.http.HttpServletRequest;
/*   9:    */ import javax.servlet.http.Part;
/*  10:    */ import org.springframework.http.HttpHeaders;
/*  11:    */ import org.springframework.util.FileCopyUtils;
/*  12:    */ import org.springframework.util.LinkedMultiValueMap;
/*  13:    */ import org.springframework.util.MultiValueMap;
/*  14:    */ import org.springframework.web.multipart.MultipartException;
/*  15:    */ import org.springframework.web.multipart.MultipartFile;
/*  16:    */ 
/*  17:    */ public class StandardMultipartHttpServletRequest
/*  18:    */   extends AbstractMultipartHttpServletRequest
/*  19:    */ {
/*  20:    */   private static final String CONTENT_DISPOSITION = "Content-Disposition";
/*  21:    */   private static final String FILENAME_KEY = "filename=";
/*  22:    */   
/*  23:    */   public StandardMultipartHttpServletRequest(HttpServletRequest request)
/*  24:    */     throws MultipartException
/*  25:    */   {
/*  26: 55 */     super(request);
/*  27:    */     try
/*  28:    */     {
/*  29: 57 */       Collection<Part> parts = request.getParts();
/*  30: 58 */       MultiValueMap<String, MultipartFile> files = new LinkedMultiValueMap(parts.size());
/*  31: 59 */       for (Part part : parts)
/*  32:    */       {
/*  33: 60 */         String filename = extractFilename(part.getHeader("Content-Disposition"));
/*  34: 61 */         if (filename != null) {
/*  35: 62 */           files.add(part.getName(), new StandardMultipartFile(part, filename));
/*  36:    */         }
/*  37:    */       }
/*  38: 65 */       setMultipartFiles(files);
/*  39:    */     }
/*  40:    */     catch (Exception ex)
/*  41:    */     {
/*  42: 68 */       throw new MultipartException("Could not parse multipart servlet request", ex);
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   private String extractFilename(String contentDisposition)
/*  47:    */   {
/*  48: 73 */     if (contentDisposition == null) {
/*  49: 74 */       return null;
/*  50:    */     }
/*  51: 77 */     int startIndex = contentDisposition.indexOf("filename=");
/*  52: 78 */     if (startIndex == -1) {
/*  53: 79 */       return null;
/*  54:    */     }
/*  55: 81 */     String filename = contentDisposition.substring(startIndex + "filename=".length());
/*  56: 82 */     if (filename.startsWith("\""))
/*  57:    */     {
/*  58: 83 */       int endIndex = filename.indexOf("\"", 1);
/*  59: 84 */       if (endIndex != -1) {
/*  60: 85 */         return filename.substring(1, endIndex);
/*  61:    */       }
/*  62:    */     }
/*  63:    */     else
/*  64:    */     {
/*  65: 89 */       int endIndex = filename.indexOf(";");
/*  66: 90 */       if (endIndex != -1) {
/*  67: 91 */         return filename.substring(0, endIndex);
/*  68:    */       }
/*  69:    */     }
/*  70: 94 */     return filename;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public String getMultipartContentType(String paramOrFileName)
/*  74:    */   {
/*  75:    */     try
/*  76:    */     {
/*  77:100 */       Part part = getPart(paramOrFileName);
/*  78:101 */       return part != null ? part.getContentType() : null;
/*  79:    */     }
/*  80:    */     catch (Exception ex)
/*  81:    */     {
/*  82:104 */       throw new MultipartException("Could not access multipart servlet request", ex);
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   public HttpHeaders getMultipartHeaders(String paramOrFileName)
/*  87:    */   {
/*  88:    */     try
/*  89:    */     {
/*  90:110 */       Part part = getPart(paramOrFileName);
/*  91:111 */       if (part != null)
/*  92:    */       {
/*  93:112 */         HttpHeaders headers = new HttpHeaders();
/*  94:113 */         for (String headerName : part.getHeaderNames()) {
/*  95:114 */           headers.put(headerName, new ArrayList(part.getHeaders(headerName)));
/*  96:    */         }
/*  97:116 */         return headers;
/*  98:    */       }
/*  99:119 */       return null;
/* 100:    */     }
/* 101:    */     catch (Exception ex)
/* 102:    */     {
/* 103:123 */       throw new MultipartException("Could not access multipart servlet request", ex);
/* 104:    */     }
/* 105:    */   }
/* 106:    */   
/* 107:    */   private static class StandardMultipartFile
/* 108:    */     implements MultipartFile
/* 109:    */   {
/* 110:    */     private final Part part;
/* 111:    */     private final String filename;
/* 112:    */     
/* 113:    */     public StandardMultipartFile(Part part, String filename)
/* 114:    */     {
/* 115:138 */       this.part = part;
/* 116:139 */       this.filename = filename;
/* 117:    */     }
/* 118:    */     
/* 119:    */     public String getName()
/* 120:    */     {
/* 121:143 */       return this.part.getName();
/* 122:    */     }
/* 123:    */     
/* 124:    */     public String getOriginalFilename()
/* 125:    */     {
/* 126:147 */       return this.filename;
/* 127:    */     }
/* 128:    */     
/* 129:    */     public String getContentType()
/* 130:    */     {
/* 131:151 */       return this.part.getContentType();
/* 132:    */     }
/* 133:    */     
/* 134:    */     public boolean isEmpty()
/* 135:    */     {
/* 136:155 */       return this.part.getSize() == 0L;
/* 137:    */     }
/* 138:    */     
/* 139:    */     public long getSize()
/* 140:    */     {
/* 141:159 */       return this.part.getSize();
/* 142:    */     }
/* 143:    */     
/* 144:    */     public byte[] getBytes()
/* 145:    */       throws IOException
/* 146:    */     {
/* 147:163 */       return FileCopyUtils.copyToByteArray(this.part.getInputStream());
/* 148:    */     }
/* 149:    */     
/* 150:    */     public InputStream getInputStream()
/* 151:    */       throws IOException
/* 152:    */     {
/* 153:167 */       return this.part.getInputStream();
/* 154:    */     }
/* 155:    */     
/* 156:    */     public void transferTo(File dest)
/* 157:    */       throws IOException, IllegalStateException
/* 158:    */     {
/* 159:171 */       this.part.write(dest.getPath());
/* 160:    */     }
/* 161:    */   }
/* 162:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.multipart.support.StandardMultipartHttpServletRequest
 * JD-Core Version:    0.7.0.1
 */