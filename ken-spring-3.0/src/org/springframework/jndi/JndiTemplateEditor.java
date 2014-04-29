/*  1:   */ package org.springframework.jndi;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditorSupport;
/*  4:   */ import java.util.Properties;
/*  5:   */ import org.springframework.beans.propertyeditors.PropertiesEditor;
/*  6:   */ 
/*  7:   */ public class JndiTemplateEditor
/*  8:   */   extends PropertyEditorSupport
/*  9:   */ {
/* 10:33 */   private final PropertiesEditor propertiesEditor = new PropertiesEditor();
/* 11:   */   
/* 12:   */   public void setAsText(String text)
/* 13:   */     throws IllegalArgumentException
/* 14:   */   {
/* 15:37 */     if (text == null) {
/* 16:38 */       throw new IllegalArgumentException("JndiTemplate cannot be created from null string");
/* 17:   */     }
/* 18:40 */     if ("".equals(text))
/* 19:   */     {
/* 20:42 */       setValue(new JndiTemplate());
/* 21:   */     }
/* 22:   */     else
/* 23:   */     {
/* 24:46 */       this.propertiesEditor.setAsText(text);
/* 25:47 */       Properties props = (Properties)this.propertiesEditor.getValue();
/* 26:48 */       setValue(new JndiTemplate(props));
/* 27:   */     }
/* 28:   */   }
/* 29:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jndi.JndiTemplateEditor
 * JD-Core Version:    0.7.0.1
 */