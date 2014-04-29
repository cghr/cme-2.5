/*  1:   */ package org.springframework.web.servlet.tags.form;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditor;
/*  4:   */ import org.springframework.util.ObjectUtils;
/*  5:   */ import org.springframework.web.util.HtmlUtils;
/*  6:   */ 
/*  7:   */ abstract class ValueFormatter
/*  8:   */ {
/*  9:   */   public static String getDisplayString(Object value, boolean htmlEscape)
/* 10:   */   {
/* 11:47 */     String displayValue = ObjectUtils.getDisplayString(value);
/* 12:48 */     return htmlEscape ? HtmlUtils.htmlEscape(displayValue) : displayValue;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public static String getDisplayString(Object value, PropertyEditor propertyEditor, boolean htmlEscape)
/* 16:   */   {
/* 17:59 */     if ((propertyEditor != null) && (!(value instanceof String))) {
/* 18:   */       try
/* 19:   */       {
/* 20:61 */         propertyEditor.setValue(value);
/* 21:62 */         String text = propertyEditor.getAsText();
/* 22:63 */         if (text != null) {
/* 23:64 */           return getDisplayString(text, htmlEscape);
/* 24:   */         }
/* 25:   */       }
/* 26:   */       catch (Throwable localThrowable) {}
/* 27:   */     }
/* 28:71 */     return getDisplayString(value, htmlEscape);
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.ValueFormatter
 * JD-Core Version:    0.7.0.1
 */