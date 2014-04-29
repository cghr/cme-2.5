/*  1:   */ package org.springframework.beans.propertyeditors;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditorSupport;
/*  4:   */ import org.springframework.util.StringUtils;
/*  5:   */ 
/*  6:   */ public class StringTrimmerEditor
/*  7:   */   extends PropertyEditorSupport
/*  8:   */ {
/*  9:   */   private final String charsToDelete;
/* 10:   */   private final boolean emptyAsNull;
/* 11:   */   
/* 12:   */   public StringTrimmerEditor(boolean emptyAsNull)
/* 13:   */   {
/* 14:46 */     this.charsToDelete = null;
/* 15:47 */     this.emptyAsNull = emptyAsNull;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public StringTrimmerEditor(String charsToDelete, boolean emptyAsNull)
/* 19:   */   {
/* 20:59 */     this.charsToDelete = charsToDelete;
/* 21:60 */     this.emptyAsNull = emptyAsNull;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void setAsText(String text)
/* 25:   */   {
/* 26:66 */     if (text == null)
/* 27:   */     {
/* 28:67 */       setValue(null);
/* 29:   */     }
/* 30:   */     else
/* 31:   */     {
/* 32:70 */       String value = text.trim();
/* 33:71 */       if (this.charsToDelete != null) {
/* 34:72 */         value = StringUtils.deleteAny(value, this.charsToDelete);
/* 35:   */       }
/* 36:74 */       if ((this.emptyAsNull) && ("".equals(value))) {
/* 37:75 */         setValue(null);
/* 38:   */       } else {
/* 39:78 */         setValue(value);
/* 40:   */       }
/* 41:   */     }
/* 42:   */   }
/* 43:   */   
/* 44:   */   public String getAsText()
/* 45:   */   {
/* 46:85 */     Object value = getValue();
/* 47:86 */     return value != null ? value.toString() : "";
/* 48:   */   }
/* 49:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.StringTrimmerEditor
 * JD-Core Version:    0.7.0.1
 */