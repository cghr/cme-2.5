/*  1:   */ package org.springframework.http.converter;
/*  2:   */ 
/*  3:   */ import java.io.ByteArrayOutputStream;
/*  4:   */ import java.io.IOException;
/*  5:   */ import org.springframework.http.HttpHeaders;
/*  6:   */ import org.springframework.http.HttpInputMessage;
/*  7:   */ import org.springframework.http.HttpOutputMessage;
/*  8:   */ import org.springframework.http.MediaType;
/*  9:   */ import org.springframework.util.FileCopyUtils;
/* 10:   */ 
/* 11:   */ public class ByteArrayHttpMessageConverter
/* 12:   */   extends AbstractHttpMessageConverter<byte[]>
/* 13:   */ {
/* 14:   */   public ByteArrayHttpMessageConverter()
/* 15:   */   {
/* 16:41 */     super(new MediaType[] { new MediaType("application", "octet-stream"), MediaType.ALL });
/* 17:   */   }
/* 18:   */   
/* 19:   */   public boolean supports(Class<?> clazz)
/* 20:   */   {
/* 21:46 */     return [B.class.equals(clazz);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public byte[] readInternal(Class clazz, HttpInputMessage inputMessage)
/* 25:   */     throws IOException
/* 26:   */   {
/* 27:51 */     long contentLength = inputMessage.getHeaders().getContentLength();
/* 28:52 */     if (contentLength >= 0L)
/* 29:   */     {
/* 30:53 */       ByteArrayOutputStream bos = new ByteArrayOutputStream((int)contentLength);
/* 31:54 */       FileCopyUtils.copy(inputMessage.getBody(), bos);
/* 32:55 */       return bos.toByteArray();
/* 33:   */     }
/* 34:58 */     return FileCopyUtils.copyToByteArray(inputMessage.getBody());
/* 35:   */   }
/* 36:   */   
/* 37:   */   protected Long getContentLength(byte[] bytes, MediaType contentType)
/* 38:   */   {
/* 39:64 */     return Long.valueOf(bytes.length);
/* 40:   */   }
/* 41:   */   
/* 42:   */   protected void writeInternal(byte[] bytes, HttpOutputMessage outputMessage)
/* 43:   */     throws IOException
/* 44:   */   {
/* 45:69 */     FileCopyUtils.copy(bytes, outputMessage.getBody());
/* 46:   */   }
/* 47:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.converter.ByteArrayHttpMessageConverter
 * JD-Core Version:    0.7.0.1
 */