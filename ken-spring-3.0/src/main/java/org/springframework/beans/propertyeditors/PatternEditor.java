/*  1:   */ package org.springframework.beans.propertyeditors;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditorSupport;
/*  4:   */ import java.util.regex.Pattern;
/*  5:   */ 
/*  6:   */ public class PatternEditor
/*  7:   */   extends PropertyEditorSupport
/*  8:   */ {
/*  9:   */   private final int flags;
/* 10:   */   
/* 11:   */   public PatternEditor()
/* 12:   */   {
/* 13:40 */     this.flags = 0;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public PatternEditor(int flags)
/* 17:   */   {
/* 18:54 */     this.flags = flags;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void setAsText(String text)
/* 22:   */   {
/* 23:60 */     setValue(text != null ? Pattern.compile(text, this.flags) : null);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public String getAsText()
/* 27:   */   {
/* 28:65 */     Pattern value = (Pattern)getValue();
/* 29:66 */     return value != null ? value.pattern() : "";
/* 30:   */   }
/* 31:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.PatternEditor
 * JD-Core Version:    0.7.0.1
 */