/*   1:    */ package org.springframework.http.converter.json;
/*   2:    */ 
/*   3:    */ import java.io.EOFException;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.nio.charset.Charset;
/*   6:    */ import org.codehaus.jackson.JsonEncoding;
/*   7:    */ import org.codehaus.jackson.JsonFactory;
/*   8:    */ import org.codehaus.jackson.JsonGenerationException;
/*   9:    */ import org.codehaus.jackson.JsonGenerator;
/*  10:    */ import org.codehaus.jackson.JsonParseException;
/*  11:    */ import org.codehaus.jackson.map.JsonMappingException;
/*  12:    */ import org.codehaus.jackson.map.ObjectMapper;
/*  13:    */ import org.codehaus.jackson.map.type.TypeFactory;
/*  14:    */ import org.codehaus.jackson.type.JavaType;
/*  15:    */ import org.springframework.http.HttpHeaders;
/*  16:    */ import org.springframework.http.HttpInputMessage;
/*  17:    */ import org.springframework.http.HttpOutputMessage;
/*  18:    */ import org.springframework.http.MediaType;
/*  19:    */ import org.springframework.http.converter.AbstractHttpMessageConverter;
/*  20:    */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*  21:    */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*  22:    */ import org.springframework.util.Assert;
/*  23:    */ 
/*  24:    */ public class MappingJacksonHttpMessageConverter
/*  25:    */   extends AbstractHttpMessageConverter<Object>
/*  26:    */ {
/*  27: 56 */   public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
/*  28: 58 */   private ObjectMapper objectMapper = new ObjectMapper();
/*  29: 60 */   private boolean prefixJson = false;
/*  30:    */   
/*  31:    */   public MappingJacksonHttpMessageConverter()
/*  32:    */   {
/*  33: 67 */     super(new MediaType("application", "json", DEFAULT_CHARSET));
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setObjectMapper(ObjectMapper objectMapper)
/*  37:    */   {
/*  38: 79 */     Assert.notNull(objectMapper, "'objectMapper' must not be null");
/*  39: 80 */     this.objectMapper = objectMapper;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setPrefixJson(boolean prefixJson)
/*  43:    */   {
/*  44: 90 */     this.prefixJson = prefixJson;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public boolean canRead(Class<?> clazz, MediaType mediaType)
/*  48:    */   {
/*  49: 95 */     JavaType javaType = getJavaType(clazz);
/*  50: 96 */     return (this.objectMapper.canDeserialize(javaType)) && (canRead(mediaType));
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected JavaType getJavaType(Class<?> clazz)
/*  54:    */   {
/*  55:118 */     return TypeFactory.type(clazz);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public boolean canWrite(Class<?> clazz, MediaType mediaType)
/*  59:    */   {
/*  60:123 */     return (this.objectMapper.canSerialize(clazz)) && (canWrite(mediaType));
/*  61:    */   }
/*  62:    */   
/*  63:    */   protected boolean supports(Class<?> clazz)
/*  64:    */   {
/*  65:129 */     throw new UnsupportedOperationException();
/*  66:    */   }
/*  67:    */   
/*  68:    */   protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
/*  69:    */     throws IOException, HttpMessageNotReadableException
/*  70:    */   {
/*  71:135 */     JavaType javaType = getJavaType(clazz);
/*  72:    */     try
/*  73:    */     {
/*  74:137 */       return this.objectMapper.readValue(inputMessage.getBody(), javaType);
/*  75:    */     }
/*  76:    */     catch (JsonParseException ex)
/*  77:    */     {
/*  78:140 */       throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
/*  79:    */     }
/*  80:    */     catch (JsonMappingException ex)
/*  81:    */     {
/*  82:143 */       throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
/*  83:    */     }
/*  84:    */     catch (EOFException ex)
/*  85:    */     {
/*  86:146 */       throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
/*  87:    */     }
/*  88:    */   }
/*  89:    */   
/*  90:    */   protected void writeInternal(Object o, HttpOutputMessage outputMessage)
/*  91:    */     throws IOException, HttpMessageNotWritableException
/*  92:    */   {
/*  93:154 */     JsonEncoding encoding = getEncoding(outputMessage.getHeaders().getContentType());
/*  94:155 */     JsonGenerator jsonGenerator = 
/*  95:156 */       this.objectMapper.getJsonFactory().createJsonGenerator(outputMessage.getBody(), encoding);
/*  96:    */     try
/*  97:    */     {
/*  98:158 */       if (this.prefixJson) {
/*  99:159 */         jsonGenerator.writeRaw("{} && ");
/* 100:    */       }
/* 101:161 */       this.objectMapper.writeValue(jsonGenerator, o);
/* 102:    */     }
/* 103:    */     catch (JsonGenerationException ex)
/* 104:    */     {
/* 105:164 */       throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
/* 106:    */     }
/* 107:    */   }
/* 108:    */   
/* 109:    */   private JsonEncoding getEncoding(MediaType contentType)
/* 110:    */   {
/* 111:169 */     if ((contentType != null) && (contentType.getCharSet() != null))
/* 112:    */     {
/* 113:170 */       Charset charset = contentType.getCharSet();
/* 114:171 */       for (JsonEncoding encoding : JsonEncoding.values()) {
/* 115:172 */         if (charset.name().equals(encoding.getJavaName())) {
/* 116:173 */           return encoding;
/* 117:    */         }
/* 118:    */       }
/* 119:    */     }
/* 120:177 */     return JsonEncoding.UTF8;
/* 121:    */   }
/* 122:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.converter.json.MappingJacksonHttpMessageConverter
 * JD-Core Version:    0.7.0.1
 */