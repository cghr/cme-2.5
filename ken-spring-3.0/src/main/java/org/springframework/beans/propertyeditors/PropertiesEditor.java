/*  1:   */ package org.springframework.beans.propertyeditors;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditorSupport;
/*  4:   */ import java.io.ByteArrayInputStream;
/*  5:   */ import java.io.IOException;
/*  6:   */ import java.util.Map;
/*  7:   */ import java.util.Properties;
/*  8:   */ 
/*  9:   */ public class PropertiesEditor
/* 10:   */   extends PropertyEditorSupport
/* 11:   */ {
/* 12:   */   public void setAsText(String text)
/* 13:   */     throws IllegalArgumentException
/* 14:   */   {
/* 15:48 */     Properties props = new Properties();
/* 16:49 */     if (text != null) {
/* 17:   */       try
/* 18:   */       {
/* 19:52 */         props.load(new ByteArrayInputStream(text.getBytes("ISO-8859-1")));
/* 20:   */       }
/* 21:   */       catch (IOException ex)
/* 22:   */       {
/* 23:56 */         throw new IllegalArgumentException(
/* 24:57 */           "Failed to parse [" + text + "] into Properties", ex);
/* 25:   */       }
/* 26:   */     }
/* 27:60 */     setValue(props);
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void setValue(Object value)
/* 31:   */   {
/* 32:68 */     if ((!(value instanceof Properties)) && ((value instanceof Map)))
/* 33:   */     {
/* 34:69 */       Properties props = new Properties();
/* 35:70 */       props.putAll((Map)value);
/* 36:71 */       super.setValue(props);
/* 37:   */     }
/* 38:   */     else
/* 39:   */     {
/* 40:74 */       super.setValue(value);
/* 41:   */     }
/* 42:   */   }
/* 43:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.PropertiesEditor
 * JD-Core Version:    0.7.0.1
 */