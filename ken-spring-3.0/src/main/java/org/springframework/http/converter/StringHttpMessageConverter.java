/*   1:    */ package org.springframework.http.converter;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStreamReader;
/*   5:    */ import java.io.OutputStreamWriter;
/*   6:    */ import java.io.UnsupportedEncodingException;
/*   7:    */ import java.nio.charset.Charset;
/*   8:    */ import java.util.ArrayList;
/*   9:    */ import java.util.List;
/*  10:    */ import java.util.SortedMap;
/*  11:    */ import org.springframework.http.HttpHeaders;
/*  12:    */ import org.springframework.http.HttpInputMessage;
/*  13:    */ import org.springframework.http.HttpOutputMessage;
/*  14:    */ import org.springframework.http.MediaType;
/*  15:    */ import org.springframework.util.FileCopyUtils;
/*  16:    */ 
/*  17:    */ public class StringHttpMessageConverter
/*  18:    */   extends AbstractHttpMessageConverter<String>
/*  19:    */ {
/*  20: 44 */   public static final Charset DEFAULT_CHARSET = Charset.forName("ISO-8859-1");
/*  21:    */   private final List<Charset> availableCharsets;
/*  22: 48 */   private boolean writeAcceptCharset = true;
/*  23:    */   
/*  24:    */   public StringHttpMessageConverter()
/*  25:    */   {
/*  26: 51 */     super(new MediaType[] { new MediaType("text", "plain", DEFAULT_CHARSET), MediaType.ALL });
/*  27: 52 */     this.availableCharsets = new ArrayList(Charset.availableCharsets().values());
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setWriteAcceptCharset(boolean writeAcceptCharset)
/*  31:    */   {
/*  32: 60 */     this.writeAcceptCharset = writeAcceptCharset;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public boolean supports(Class<?> clazz)
/*  36:    */   {
/*  37: 65 */     return String.class.equals(clazz);
/*  38:    */   }
/*  39:    */   
/*  40:    */   protected String readInternal(Class clazz, HttpInputMessage inputMessage)
/*  41:    */     throws IOException
/*  42:    */   {
/*  43: 70 */     Charset charset = getContentTypeCharset(inputMessage.getHeaders().getContentType());
/*  44: 71 */     return FileCopyUtils.copyToString(new InputStreamReader(inputMessage.getBody(), charset));
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected Long getContentLength(String s, MediaType contentType)
/*  48:    */   {
/*  49: 76 */     Charset charset = getContentTypeCharset(contentType);
/*  50:    */     try
/*  51:    */     {
/*  52: 78 */       return Long.valueOf(s.getBytes(charset.name()).length);
/*  53:    */     }
/*  54:    */     catch (UnsupportedEncodingException ex)
/*  55:    */     {
/*  56: 82 */       throw new InternalError(ex.getMessage());
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected void writeInternal(String s, HttpOutputMessage outputMessage)
/*  61:    */     throws IOException
/*  62:    */   {
/*  63: 88 */     if (this.writeAcceptCharset) {
/*  64: 89 */       outputMessage.getHeaders().setAcceptCharset(getAcceptedCharsets());
/*  65:    */     }
/*  66: 91 */     Charset charset = getContentTypeCharset(outputMessage.getHeaders().getContentType());
/*  67: 92 */     FileCopyUtils.copy(s, new OutputStreamWriter(outputMessage.getBody(), charset));
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected List<Charset> getAcceptedCharsets()
/*  71:    */   {
/*  72:103 */     return this.availableCharsets;
/*  73:    */   }
/*  74:    */   
/*  75:    */   private Charset getContentTypeCharset(MediaType contentType)
/*  76:    */   {
/*  77:107 */     if ((contentType != null) && (contentType.getCharSet() != null)) {
/*  78:108 */       return contentType.getCharSet();
/*  79:    */     }
/*  80:111 */     return DEFAULT_CHARSET;
/*  81:    */   }
/*  82:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.converter.StringHttpMessageConverter
 * JD-Core Version:    0.7.0.1
 */