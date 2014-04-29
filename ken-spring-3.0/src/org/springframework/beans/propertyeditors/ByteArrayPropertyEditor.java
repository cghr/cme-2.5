/*  1:   */ package org.springframework.beans.propertyeditors;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditorSupport;
/*  4:   */ 
/*  5:   */ public class ByteArrayPropertyEditor
/*  6:   */   extends PropertyEditorSupport
/*  7:   */ {
/*  8:   */   public void setAsText(String text)
/*  9:   */   {
/* 10:33 */     setValue(text != null ? text.getBytes() : null);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public String getAsText()
/* 14:   */   {
/* 15:38 */     byte[] value = (byte[])getValue();
/* 16:39 */     return value != null ? new String(value) : "";
/* 17:   */   }
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.ByteArrayPropertyEditor
 * JD-Core Version:    0.7.0.1
 */