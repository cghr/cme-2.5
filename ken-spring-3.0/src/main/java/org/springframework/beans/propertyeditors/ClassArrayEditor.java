/*  1:   */ package org.springframework.beans.propertyeditors;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditorSupport;
/*  4:   */ import org.springframework.util.ClassUtils;
/*  5:   */ import org.springframework.util.ObjectUtils;
/*  6:   */ import org.springframework.util.StringUtils;
/*  7:   */ 
/*  8:   */ public class ClassArrayEditor
/*  9:   */   extends PropertyEditorSupport
/* 10:   */ {
/* 11:   */   private final ClassLoader classLoader;
/* 12:   */   
/* 13:   */   public ClassArrayEditor()
/* 14:   */   {
/* 15:47 */     this(null);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public ClassArrayEditor(ClassLoader classLoader)
/* 19:   */   {
/* 20:57 */     this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void setAsText(String text)
/* 24:   */     throws IllegalArgumentException
/* 25:   */   {
/* 26:63 */     if (StringUtils.hasText(text))
/* 27:   */     {
/* 28:64 */       String[] classNames = StringUtils.commaDelimitedListToStringArray(text);
/* 29:65 */       Class[] classes = new Class[classNames.length];
/* 30:66 */       for (int i = 0; i < classNames.length; i++)
/* 31:   */       {
/* 32:67 */         String className = classNames[i].trim();
/* 33:68 */         classes[i] = ClassUtils.resolveClassName(className, this.classLoader);
/* 34:   */       }
/* 35:70 */       setValue(classes);
/* 36:   */     }
/* 37:   */     else
/* 38:   */     {
/* 39:73 */       setValue(null);
/* 40:   */     }
/* 41:   */   }
/* 42:   */   
/* 43:   */   public String getAsText()
/* 44:   */   {
/* 45:79 */     Class[] classes = (Class[])getValue();
/* 46:80 */     if (ObjectUtils.isEmpty(classes)) {
/* 47:81 */       return "";
/* 48:   */     }
/* 49:83 */     StringBuilder sb = new StringBuilder();
/* 50:84 */     for (int i = 0; i < classes.length; i++)
/* 51:   */     {
/* 52:85 */       if (i > 0) {
/* 53:86 */         sb.append(",");
/* 54:   */       }
/* 55:88 */       sb.append(ClassUtils.getQualifiedName(classes[i]));
/* 56:   */     }
/* 57:90 */     return sb.toString();
/* 58:   */   }
/* 59:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.ClassArrayEditor
 * JD-Core Version:    0.7.0.1
 */