/*   1:    */ package org.springframework.http.converter;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import javax.activation.FileTypeMap;
/*   7:    */ import javax.activation.MimetypesFileTypeMap;
/*   8:    */ import org.springframework.core.io.ByteArrayResource;
/*   9:    */ import org.springframework.core.io.ClassPathResource;
/*  10:    */ import org.springframework.core.io.Resource;
/*  11:    */ import org.springframework.http.HttpInputMessage;
/*  12:    */ import org.springframework.http.HttpOutputMessage;
/*  13:    */ import org.springframework.http.MediaType;
/*  14:    */ import org.springframework.util.ClassUtils;
/*  15:    */ import org.springframework.util.FileCopyUtils;
/*  16:    */ import org.springframework.util.StringUtils;
/*  17:    */ 
/*  18:    */ public class ResourceHttpMessageConverter
/*  19:    */   extends AbstractHttpMessageConverter<Resource>
/*  20:    */ {
/*  21: 47 */   private static final boolean jafPresent = ClassUtils.isPresent("javax.activation.FileTypeMap", ResourceHttpMessageConverter.class.getClassLoader());
/*  22:    */   
/*  23:    */   public ResourceHttpMessageConverter()
/*  24:    */   {
/*  25: 50 */     super(MediaType.ALL);
/*  26:    */   }
/*  27:    */   
/*  28:    */   protected boolean supports(Class<?> clazz)
/*  29:    */   {
/*  30: 54 */     return Resource.class.isAssignableFrom(clazz);
/*  31:    */   }
/*  32:    */   
/*  33:    */   protected Resource readInternal(Class<? extends Resource> clazz, HttpInputMessage inputMessage)
/*  34:    */     throws IOException, HttpMessageNotReadableException
/*  35:    */   {
/*  36: 60 */     byte[] body = FileCopyUtils.copyToByteArray(inputMessage.getBody());
/*  37: 61 */     return new ByteArrayResource(body);
/*  38:    */   }
/*  39:    */   
/*  40:    */   protected MediaType getDefaultContentType(Resource resource)
/*  41:    */   {
/*  42: 66 */     if (jafPresent) {
/*  43: 67 */       return ActivationMediaTypeFactory.getMediaType(resource);
/*  44:    */     }
/*  45: 70 */     return MediaType.APPLICATION_OCTET_STREAM;
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected Long getContentLength(Resource resource, MediaType contentType)
/*  49:    */   {
/*  50:    */     try
/*  51:    */     {
/*  52: 77 */       return Long.valueOf(resource.contentLength());
/*  53:    */     }
/*  54:    */     catch (IOException localIOException) {}
/*  55: 80 */     return null;
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected void writeInternal(Resource resource, HttpOutputMessage outputMessage)
/*  59:    */     throws IOException, HttpMessageNotWritableException
/*  60:    */   {
/*  61: 87 */     FileCopyUtils.copy(resource.getInputStream(), outputMessage.getBody());
/*  62: 88 */     outputMessage.getBody().flush();
/*  63:    */   }
/*  64:    */   
/*  65:    */   private static class ActivationMediaTypeFactory
/*  66:    */   {
/*  67: 99 */     private static final FileTypeMap fileTypeMap = ;
/*  68:    */     
/*  69:    */     private static FileTypeMap loadFileTypeMapFromContextSupportModule()
/*  70:    */     {
/*  71:104 */       Resource mappingLocation = new ClassPathResource("org/springframework/mail/javamail/mime.types");
/*  72:105 */       if (mappingLocation.exists())
/*  73:    */       {
/*  74:106 */         InputStream inputStream = null;
/*  75:    */         try
/*  76:    */         {
/*  77:108 */           inputStream = mappingLocation.getInputStream();
/*  78:109 */           return new MimetypesFileTypeMap(inputStream);
/*  79:    */         }
/*  80:    */         catch (IOException localIOException2) {}finally
/*  81:    */         {
/*  82:115 */           if (inputStream != null) {
/*  83:    */             try
/*  84:    */             {
/*  85:117 */               inputStream.close();
/*  86:    */             }
/*  87:    */             catch (IOException localIOException4) {}
/*  88:    */           }
/*  89:    */         }
/*  90:    */       }
/*  91:125 */       return FileTypeMap.getDefaultFileTypeMap();
/*  92:    */     }
/*  93:    */     
/*  94:    */     public static MediaType getMediaType(Resource resource)
/*  95:    */     {
/*  96:129 */       String mediaType = fileTypeMap.getContentType(resource.getFilename());
/*  97:130 */       return StringUtils.hasText(mediaType) ? MediaType.parseMediaType(mediaType) : null;
/*  98:    */     }
/*  99:    */   }
/* 100:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.converter.ResourceHttpMessageConverter
 * JD-Core Version:    0.7.0.1
 */