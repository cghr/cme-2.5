/*  1:   */ package org.springframework.beans.propertyeditors;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditorSupport;
/*  4:   */ import java.util.TimeZone;
/*  5:   */ 
/*  6:   */ public class TimeZoneEditor
/*  7:   */   extends PropertyEditorSupport
/*  8:   */ {
/*  9:   */   public void setAsText(String text)
/* 10:   */     throws IllegalArgumentException
/* 11:   */   {
/* 12:34 */     setValue(TimeZone.getTimeZone(text));
/* 13:   */   }
/* 14:   */   
/* 15:   */   public String getAsText()
/* 16:   */   {
/* 17:43 */     return null;
/* 18:   */   }
/* 19:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.TimeZoneEditor
 * JD-Core Version:    0.7.0.1
 */