/*  1:   */ package org.springframework.beans.propertyeditors;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditorSupport;
/*  4:   */ import java.util.UUID;
/*  5:   */ import org.springframework.util.StringUtils;
/*  6:   */ 
/*  7:   */ public class UUIDEditor
/*  8:   */   extends PropertyEditorSupport
/*  9:   */ {
/* 10:   */   public void setAsText(String text)
/* 11:   */     throws IllegalArgumentException
/* 12:   */   {
/* 13:36 */     if (StringUtils.hasText(text)) {
/* 14:37 */       setValue(UUID.fromString(text));
/* 15:   */     } else {
/* 16:40 */       setValue(null);
/* 17:   */     }
/* 18:   */   }
/* 19:   */   
/* 20:   */   public String getAsText()
/* 21:   */   {
/* 22:46 */     UUID value = (UUID)getValue();
/* 23:47 */     return value != null ? value.toString() : "";
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.UUIDEditor
 * JD-Core Version:    0.7.0.1
 */