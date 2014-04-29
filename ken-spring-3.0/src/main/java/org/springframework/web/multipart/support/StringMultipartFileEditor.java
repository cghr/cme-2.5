/*  1:   */ package org.springframework.web.multipart.support;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditorSupport;
/*  4:   */ import java.io.IOException;
/*  5:   */ import org.springframework.web.multipart.MultipartFile;
/*  6:   */ 
/*  7:   */ public class StringMultipartFileEditor
/*  8:   */   extends PropertyEditorSupport
/*  9:   */ {
/* 10:   */   private final String charsetName;
/* 11:   */   
/* 12:   */   public StringMultipartFileEditor()
/* 13:   */   {
/* 14:42 */     this.charsetName = null;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public StringMultipartFileEditor(String charsetName)
/* 18:   */   {
/* 19:51 */     this.charsetName = charsetName;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void setAsText(String text)
/* 23:   */   {
/* 24:57 */     setValue(text);
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void setValue(Object value)
/* 28:   */   {
/* 29:62 */     if ((value instanceof MultipartFile))
/* 30:   */     {
/* 31:63 */       MultipartFile multipartFile = (MultipartFile)value;
/* 32:   */       try
/* 33:   */       {
/* 34:65 */         super.setValue(this.charsetName != null ? 
/* 35:66 */           new String(multipartFile.getBytes(), this.charsetName) : 
/* 36:67 */           new String(multipartFile.getBytes()));
/* 37:   */       }
/* 38:   */       catch (IOException ex)
/* 39:   */       {
/* 40:70 */         throw new IllegalArgumentException("Cannot read contents of multipart file", ex);
/* 41:   */       }
/* 42:   */     }
/* 43:   */     else
/* 44:   */     {
/* 45:74 */       super.setValue(value);
/* 46:   */     }
/* 47:   */   }
/* 48:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.multipart.support.StringMultipartFileEditor
 * JD-Core Version:    0.7.0.1
 */