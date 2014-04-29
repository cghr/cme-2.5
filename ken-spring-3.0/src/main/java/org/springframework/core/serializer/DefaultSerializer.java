/*  1:   */ package org.springframework.core.serializer;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.ObjectOutputStream;
/*  5:   */ import java.io.OutputStream;
/*  6:   */ import java.io.Serializable;
/*  7:   */ 
/*  8:   */ public class DefaultSerializer
/*  9:   */   implements Serializer<Object>
/* 10:   */ {
/* 11:   */   public void serialize(Object object, OutputStream outputStream)
/* 12:   */     throws IOException
/* 13:   */   {
/* 14:38 */     if (!(object instanceof Serializable)) {
/* 15:39 */       throw new IllegalArgumentException(getClass().getSimpleName() + " requires a Serializable payload " + 
/* 16:40 */         "but received an object of type [" + object.getClass().getName() + "]");
/* 17:   */     }
/* 18:42 */     ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
/* 19:43 */     objectOutputStream.writeObject(object);
/* 20:44 */     objectOutputStream.flush();
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.serializer.DefaultSerializer
 * JD-Core Version:    0.7.0.1
 */