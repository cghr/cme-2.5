/*  1:   */ package org.springframework.util;
/*  2:   */ 
/*  3:   */ import java.io.ByteArrayInputStream;
/*  4:   */ import java.io.ByteArrayOutputStream;
/*  5:   */ import java.io.IOException;
/*  6:   */ import java.io.ObjectInputStream;
/*  7:   */ import java.io.ObjectOutputStream;
/*  8:   */ 
/*  9:   */ public abstract class SerializationUtils
/* 10:   */ {
/* 11:   */   public static byte[] serialize(Object object)
/* 12:   */   {
/* 13:39 */     if (object == null) {
/* 14:40 */       return null;
/* 15:   */     }
/* 16:42 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 17:   */     try
/* 18:   */     {
/* 19:44 */       ObjectOutputStream oos = new ObjectOutputStream(baos);
/* 20:45 */       oos.writeObject(object);
/* 21:46 */       oos.flush();
/* 22:   */     }
/* 23:   */     catch (IOException ex)
/* 24:   */     {
/* 25:49 */       throw new IllegalArgumentException("Failed to serialize object of type: " + object.getClass(), ex);
/* 26:   */     }
/* 27:51 */     return baos.toByteArray();
/* 28:   */   }
/* 29:   */   
/* 30:   */   public static Object deserialize(byte[] bytes)
/* 31:   */   {
/* 32:60 */     if (bytes == null) {
/* 33:61 */       return null;
/* 34:   */     }
/* 35:   */     try
/* 36:   */     {
/* 37:64 */       ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
/* 38:65 */       return ois.readObject();
/* 39:   */     }
/* 40:   */     catch (IOException ex)
/* 41:   */     {
/* 42:68 */       throw new IllegalArgumentException("Failed to deserialize object", ex);
/* 43:   */     }
/* 44:   */     catch (ClassNotFoundException ex)
/* 45:   */     {
/* 46:71 */       throw new IllegalStateException("Failed to deserialize object type", ex);
/* 47:   */     }
/* 48:   */   }
/* 49:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.SerializationUtils
 * JD-Core Version:    0.7.0.1
 */