/*  1:   */ package org.springframework.core.serializer.support;
/*  2:   */ 
/*  3:   */ import java.io.ByteArrayInputStream;
/*  4:   */ import org.springframework.core.convert.converter.Converter;
/*  5:   */ import org.springframework.core.serializer.DefaultDeserializer;
/*  6:   */ import org.springframework.core.serializer.Deserializer;
/*  7:   */ import org.springframework.util.Assert;
/*  8:   */ 
/*  9:   */ public class DeserializingConverter
/* 10:   */   implements Converter<byte[], Object>
/* 11:   */ {
/* 12:   */   private final Deserializer<Object> deserializer;
/* 13:   */   
/* 14:   */   public DeserializingConverter()
/* 15:   */   {
/* 16:43 */     this.deserializer = new DefaultDeserializer();
/* 17:   */   }
/* 18:   */   
/* 19:   */   public DeserializingConverter(Deserializer<Object> deserializer)
/* 20:   */   {
/* 21:50 */     Assert.notNull(deserializer, "Deserializer must not be null");
/* 22:51 */     this.deserializer = deserializer;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public Object convert(byte[] source)
/* 26:   */   {
/* 27:56 */     ByteArrayInputStream byteStream = new ByteArrayInputStream(source);
/* 28:   */     try
/* 29:   */     {
/* 30:58 */       return this.deserializer.deserialize(byteStream);
/* 31:   */     }
/* 32:   */     catch (Throwable ex)
/* 33:   */     {
/* 34:61 */       throw new SerializationFailedException("Failed to deserialize payload. Is the byte array a result of corresponding serialization for " + 
/* 35:   */       
/* 36:63 */         this.deserializer.getClass().getSimpleName() + "?", ex);
/* 37:   */     }
/* 38:   */   }
/* 39:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.serializer.support.DeserializingConverter
 * JD-Core Version:    0.7.0.1
 */