/*  1:   */ package org.springframework.http;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditorSupport;
/*  4:   */ import org.springframework.util.StringUtils;
/*  5:   */ 
/*  6:   */ public class MediaTypeEditor
/*  7:   */   extends PropertyEditorSupport
/*  8:   */ {
/*  9:   */   public void setAsText(String text)
/* 10:   */   {
/* 11:36 */     if (StringUtils.hasText(text)) {
/* 12:37 */       setValue(MediaType.parseMediaType(text));
/* 13:   */     } else {
/* 14:40 */       setValue(null);
/* 15:   */     }
/* 16:   */   }
/* 17:   */   
/* 18:   */   public String getAsText()
/* 19:   */   {
/* 20:46 */     MediaType mediaType = (MediaType)getValue();
/* 21:47 */     return mediaType != null ? mediaType.toString() : "";
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.MediaTypeEditor
 * JD-Core Version:    0.7.0.1
 */