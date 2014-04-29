/*  1:   */ package org.springframework.core.serializer;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.InputStream;
/*  5:   */ import java.io.ObjectInputStream;
/*  6:   */ import org.springframework.core.NestedIOException;
/*  7:   */ 
/*  8:   */ public class DefaultDeserializer
/*  9:   */   implements Deserializer<Object>
/* 10:   */ {
/* 11:   */   public Object deserialize(InputStream inputStream)
/* 12:   */     throws IOException
/* 13:   */   {
/* 14:38 */     ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
/* 15:   */     try
/* 16:   */     {
/* 17:40 */       return objectInputStream.readObject();
/* 18:   */     }
/* 19:   */     catch (ClassNotFoundException ex)
/* 20:   */     {
/* 21:43 */       throw new NestedIOException("Failed to deserialize object type", ex);
/* 22:   */     }
/* 23:   */   }
/* 24:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.serializer.DefaultDeserializer
 * JD-Core Version:    0.7.0.1
 */