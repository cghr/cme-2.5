/*  1:   */ package org.springframework.beans.propertyeditors;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditorSupport;
/*  4:   */ import java.util.Currency;
/*  5:   */ 
/*  6:   */ public class CurrencyEditor
/*  7:   */   extends PropertyEditorSupport
/*  8:   */ {
/*  9:   */   public void setAsText(String text)
/* 10:   */     throws IllegalArgumentException
/* 11:   */   {
/* 12:34 */     setValue(Currency.getInstance(text));
/* 13:   */   }
/* 14:   */   
/* 15:   */   public String getAsText()
/* 16:   */   {
/* 17:43 */     Currency value = (Currency)getValue();
/* 18:44 */     return value != null ? value.getCurrencyCode() : "";
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.CurrencyEditor
 * JD-Core Version:    0.7.0.1
 */