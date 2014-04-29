/*  1:   */ package org.springframework.web.multipart.support;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import org.springframework.beans.propertyeditors.ByteArrayPropertyEditor;
/*  5:   */ import org.springframework.web.multipart.MultipartFile;
/*  6:   */ 
/*  7:   */ public class ByteArrayMultipartFileEditor
/*  8:   */   extends ByteArrayPropertyEditor
/*  9:   */ {
/* 10:   */   public void setValue(Object value)
/* 11:   */   {
/* 12:35 */     if ((value instanceof MultipartFile))
/* 13:   */     {
/* 14:36 */       MultipartFile multipartFile = (MultipartFile)value;
/* 15:   */       try
/* 16:   */       {
/* 17:38 */         super.setValue(multipartFile.getBytes());
/* 18:   */       }
/* 19:   */       catch (IOException ex)
/* 20:   */       {
/* 21:41 */         throw new IllegalArgumentException("Cannot read contents of multipart file", ex);
/* 22:   */       }
/* 23:   */     }
/* 24:44 */     else if ((value instanceof byte[]))
/* 25:   */     {
/* 26:45 */       super.setValue(value);
/* 27:   */     }
/* 28:   */     else
/* 29:   */     {
/* 30:48 */       super.setValue(value != null ? value.toString().getBytes() : null);
/* 31:   */     }
/* 32:   */   }
/* 33:   */   
/* 34:   */   public String getAsText()
/* 35:   */   {
/* 36:54 */     byte[] value = (byte[])getValue();
/* 37:55 */     return value != null ? new String(value) : "";
/* 38:   */   }
/* 39:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.multipart.support.ByteArrayMultipartFileEditor
 * JD-Core Version:    0.7.0.1
 */