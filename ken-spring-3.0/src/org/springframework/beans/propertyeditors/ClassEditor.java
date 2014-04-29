/*  1:   */ package org.springframework.beans.propertyeditors;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditorSupport;
/*  4:   */ import org.springframework.util.ClassUtils;
/*  5:   */ import org.springframework.util.StringUtils;
/*  6:   */ 
/*  7:   */ public class ClassEditor
/*  8:   */   extends PropertyEditorSupport
/*  9:   */ {
/* 10:   */   private final ClassLoader classLoader;
/* 11:   */   
/* 12:   */   public ClassEditor()
/* 13:   */   {
/* 14:47 */     this(null);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public ClassEditor(ClassLoader classLoader)
/* 18:   */   {
/* 19:56 */     this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void setAsText(String text)
/* 23:   */     throws IllegalArgumentException
/* 24:   */   {
/* 25:62 */     if (StringUtils.hasText(text)) {
/* 26:63 */       setValue(ClassUtils.resolveClassName(text.trim(), this.classLoader));
/* 27:   */     } else {
/* 28:66 */       setValue(null);
/* 29:   */     }
/* 30:   */   }
/* 31:   */   
/* 32:   */   public String getAsText()
/* 33:   */   {
/* 34:72 */     Class clazz = (Class)getValue();
/* 35:73 */     if (clazz != null) {
/* 36:74 */       return ClassUtils.getQualifiedName(clazz);
/* 37:   */     }
/* 38:77 */     return "";
/* 39:   */   }
/* 40:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.ClassEditor
 * JD-Core Version:    0.7.0.1
 */