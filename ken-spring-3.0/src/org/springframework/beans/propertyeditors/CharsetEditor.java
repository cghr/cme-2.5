/*  1:   */ package org.springframework.beans.propertyeditors;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditorSupport;
/*  4:   */ import java.nio.charset.Charset;
/*  5:   */ import org.springframework.util.StringUtils;
/*  6:   */ 
/*  7:   */ public class CharsetEditor
/*  8:   */   extends PropertyEditorSupport
/*  9:   */ {
/* 10:   */   public void setAsText(String text)
/* 11:   */     throws IllegalArgumentException
/* 12:   */   {
/* 13:39 */     if (StringUtils.hasText(text)) {
/* 14:40 */       setValue(Charset.forName(text));
/* 15:   */     } else {
/* 16:43 */       setValue(null);
/* 17:   */     }
/* 18:   */   }
/* 19:   */   
/* 20:   */   public String getAsText()
/* 21:   */   {
/* 22:49 */     Charset value = (Charset)getValue();
/* 23:50 */     return value != null ? value.name() : "";
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.CharsetEditor
 * JD-Core Version:    0.7.0.1
 */