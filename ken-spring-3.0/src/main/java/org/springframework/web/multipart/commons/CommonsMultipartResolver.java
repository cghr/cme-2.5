/*   1:    */ package org.springframework.web.multipart.commons;
/*   2:    */ 
/*   3:    */ import java.util.List;
/*   4:    */ import javax.servlet.ServletContext;
/*   5:    */ import javax.servlet.http.HttpServletRequest;
/*   6:    */ import org.apache.commons.fileupload.FileItem;
/*   7:    */ import org.apache.commons.fileupload.FileItemFactory;
/*   8:    */ import org.apache.commons.fileupload.FileUpload;
/*   9:    */ import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
/*  10:    */ import org.apache.commons.fileupload.FileUploadException;
/*  11:    */ import org.apache.commons.fileupload.disk.DiskFileItemFactory;
/*  12:    */ import org.apache.commons.fileupload.servlet.ServletFileUpload;
/*  13:    */ import org.apache.commons.logging.Log;
/*  14:    */ import org.springframework.util.Assert;
/*  15:    */ import org.springframework.web.context.ServletContextAware;
/*  16:    */ import org.springframework.web.multipart.MaxUploadSizeExceededException;
/*  17:    */ import org.springframework.web.multipart.MultipartException;
/*  18:    */ import org.springframework.web.multipart.MultipartHttpServletRequest;
/*  19:    */ import org.springframework.web.multipart.MultipartResolver;
/*  20:    */ import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
/*  21:    */ import org.springframework.web.util.WebUtils;
/*  22:    */ 
/*  23:    */ public class CommonsMultipartResolver
/*  24:    */   extends CommonsFileUploadSupport
/*  25:    */   implements MultipartResolver, ServletContextAware
/*  26:    */ {
/*  27: 65 */   private boolean resolveLazily = false;
/*  28:    */   
/*  29:    */   public CommonsMultipartResolver() {}
/*  30:    */   
/*  31:    */   public CommonsMultipartResolver(ServletContext servletContext)
/*  32:    */   {
/*  33: 86 */     this();
/*  34: 87 */     setServletContext(servletContext);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setResolveLazily(boolean resolveLazily)
/*  38:    */   {
/*  39:100 */     this.resolveLazily = resolveLazily;
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected FileUpload newFileUpload(FileItemFactory fileItemFactory)
/*  43:    */   {
/*  44:111 */     return new ServletFileUpload(fileItemFactory);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setServletContext(ServletContext servletContext)
/*  48:    */   {
/*  49:115 */     if (!isUploadTempDirSpecified()) {
/*  50:116 */       getFileItemFactory().setRepository(WebUtils.getTempDir(servletContext));
/*  51:    */     }
/*  52:    */   }
/*  53:    */   
/*  54:    */   public boolean isMultipart(HttpServletRequest request)
/*  55:    */   {
/*  56:122 */     return (request != null) && (ServletFileUpload.isMultipartContent(request));
/*  57:    */   }
/*  58:    */   
/*  59:    */   public MultipartHttpServletRequest resolveMultipart(final HttpServletRequest request)
/*  60:    */     throws MultipartException
/*  61:    */   {
/*  62:126 */     Assert.notNull(request, "Request must not be null");
/*  63:127 */     if (this.resolveLazily) {
/*  64:128 */       new DefaultMultipartHttpServletRequest(request)
/*  65:    */       {
/*  66:    */         protected void initializeMultipart()
/*  67:    */         {
/*  68:131 */           CommonsFileUploadSupport.MultipartParsingResult parsingResult = CommonsMultipartResolver.this.parseRequest(request);
/*  69:132 */           setMultipartFiles(parsingResult.getMultipartFiles());
/*  70:133 */           setMultipartParameters(parsingResult.getMultipartParameters());
/*  71:134 */           setMultipartParameterContentTypes(parsingResult.getMultipartParameterContentTypes());
/*  72:    */         }
/*  73:    */       };
/*  74:    */     }
/*  75:139 */     CommonsFileUploadSupport.MultipartParsingResult parsingResult = parseRequest(request);
/*  76:140 */     return new DefaultMultipartHttpServletRequest(request, parsingResult.getMultipartFiles(), 
/*  77:141 */       parsingResult.getMultipartParameters(), parsingResult.getMultipartParameterContentTypes());
/*  78:    */   }
/*  79:    */   
/*  80:    */   protected CommonsFileUploadSupport.MultipartParsingResult parseRequest(HttpServletRequest request)
/*  81:    */     throws MultipartException
/*  82:    */   {
/*  83:153 */     String encoding = determineEncoding(request);
/*  84:154 */     FileUpload fileUpload = prepareFileUpload(encoding);
/*  85:    */     try
/*  86:    */     {
/*  87:156 */       List<FileItem> fileItems = ((ServletFileUpload)fileUpload).parseRequest(request);
/*  88:157 */       return parseFileItems(fileItems, encoding);
/*  89:    */     }
/*  90:    */     catch (FileUploadBase.SizeLimitExceededException ex)
/*  91:    */     {
/*  92:160 */       throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
/*  93:    */     }
/*  94:    */     catch (FileUploadException ex)
/*  95:    */     {
/*  96:163 */       throw new MultipartException("Could not parse multipart servlet request", ex);
/*  97:    */     }
/*  98:    */   }
/*  99:    */   
/* 100:    */   protected String determineEncoding(HttpServletRequest request)
/* 101:    */   {
/* 102:178 */     String encoding = request.getCharacterEncoding();
/* 103:179 */     if (encoding == null) {
/* 104:180 */       encoding = getDefaultEncoding();
/* 105:    */     }
/* 106:182 */     return encoding;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public void cleanupMultipart(MultipartHttpServletRequest request)
/* 110:    */   {
/* 111:186 */     if (request != null) {
/* 112:    */       try
/* 113:    */       {
/* 114:188 */         cleanupFileItems(request.getMultiFileMap());
/* 115:    */       }
/* 116:    */       catch (Throwable ex)
/* 117:    */       {
/* 118:191 */         this.logger.warn("Failed to perform multipart cleanup for servlet request", ex);
/* 119:    */       }
/* 120:    */     }
/* 121:    */   }
/* 122:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.multipart.commons.CommonsMultipartResolver
 * JD-Core Version:    0.7.0.1
 */