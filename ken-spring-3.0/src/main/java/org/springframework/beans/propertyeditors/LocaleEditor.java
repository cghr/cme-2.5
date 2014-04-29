/*  1:   */ package org.springframework.beans.propertyeditors;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditorSupport;
/*  4:   */ import org.springframework.util.StringUtils;
/*  5:   */ 
/*  6:   */ public class LocaleEditor
/*  7:   */   extends PropertyEditorSupport
/*  8:   */ {
/*  9:   */   public void setAsText(String text)
/* 10:   */   {
/* 11:39 */     setValue(StringUtils.parseLocaleString(text));
/* 12:   */   }
/* 13:   */   
/* 14:   */   public String getAsText()
/* 15:   */   {
/* 16:44 */     Object value = getValue();
/* 17:45 */     return value != null ? value.toString() : "";
/* 18:   */   }
/* 19:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.LocaleEditor
 * JD-Core Version:    0.7.0.1
 */