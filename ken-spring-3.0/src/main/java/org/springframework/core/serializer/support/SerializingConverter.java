/*  1:   */ package org.springframework.core.serializer.support;
/*  2:   */ 
/*  3:   */ import java.io.ByteArrayOutputStream;
/*  4:   */ import org.springframework.core.convert.converter.Converter;
/*  5:   */ import org.springframework.core.serializer.DefaultSerializer;
/*  6:   */ import org.springframework.core.serializer.Serializer;
/*  7:   */ import org.springframework.util.Assert;
/*  8:   */ 
/*  9:   */ public class SerializingConverter
/* 10:   */   implements Converter<Object, byte[]>
/* 11:   */ {
/* 12:   */   private final Serializer<Object> serializer;
/* 13:   */   
/* 14:   */   public SerializingConverter()
/* 15:   */   {
/* 16:43 */     this.serializer = new DefaultSerializer();
/* 17:   */   }
/* 18:   */   
/* 19:   */   public SerializingConverter(Serializer<Object> serializer)
/* 20:   */   {
/* 21:50 */     Assert.notNull(serializer, "Serializer must not be null");
/* 22:51 */     this.serializer = serializer;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public byte[] convert(Object source)
/* 26:   */   {
/* 27:59 */     ByteArrayOutputStream byteStream = new ByteArrayOutputStream(128);
/* 28:   */     try
/* 29:   */     {
/* 30:61 */       this.serializer.serialize(source, byteStream);
/* 31:62 */       return byteStream.toByteArray();
/* 32:   */     }
/* 33:   */     catch (Throwable ex)
/* 34:   */     {
/* 35:65 */       throw new SerializationFailedException("Failed to serialize object using " + 
/* 36:66 */         this.serializer.getClass().getSimpleName(), ex);
/* 37:   */     }
/* 38:   */   }
/* 39:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.serializer.support.SerializingConverter
 * JD-Core Version:    0.7.0.1
 */