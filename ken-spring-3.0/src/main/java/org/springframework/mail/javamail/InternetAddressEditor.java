/*  1:   */ package org.springframework.mail.javamail;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditorSupport;
/*  4:   */ import javax.mail.internet.AddressException;
/*  5:   */ import javax.mail.internet.InternetAddress;
/*  6:   */ import org.springframework.util.StringUtils;
/*  7:   */ 
/*  8:   */ public class InternetAddressEditor
/*  9:   */   extends PropertyEditorSupport
/* 10:   */ {
/* 11:   */   public void setAsText(String text)
/* 12:   */     throws IllegalArgumentException
/* 13:   */   {
/* 14:41 */     if (StringUtils.hasText(text)) {
/* 15:   */       try
/* 16:   */       {
/* 17:43 */         setValue(new InternetAddress(text));
/* 18:   */       }
/* 19:   */       catch (AddressException ex)
/* 20:   */       {
/* 21:46 */         throw new IllegalArgumentException("Could not parse mail address: " + ex.getMessage());
/* 22:   */       }
/* 23:   */     } else {
/* 24:50 */       setValue(null);
/* 25:   */     }
/* 26:   */   }
/* 27:   */   
/* 28:   */   public String getAsText()
/* 29:   */   {
/* 30:56 */     InternetAddress value = (InternetAddress)getValue();
/* 31:57 */     return value != null ? value.toUnicodeString() : "";
/* 32:   */   }
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.mail.javamail.InternetAddressEditor
 * JD-Core Version:    0.7.0.1
 */