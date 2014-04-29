/*  1:   */ package org.springframework.beans;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditorSupport;
/*  4:   */ import java.util.Properties;
/*  5:   */ import org.springframework.beans.propertyeditors.PropertiesEditor;
/*  6:   */ 
/*  7:   */ public class PropertyValuesEditor
/*  8:   */   extends PropertyEditorSupport
/*  9:   */ {
/* 10:39 */   private final PropertiesEditor propertiesEditor = new PropertiesEditor();
/* 11:   */   
/* 12:   */   public void setAsText(String text)
/* 13:   */     throws IllegalArgumentException
/* 14:   */   {
/* 15:43 */     this.propertiesEditor.setAsText(text);
/* 16:44 */     Properties props = (Properties)this.propertiesEditor.getValue();
/* 17:45 */     setValue(new MutablePropertyValues(props));
/* 18:   */   }
/* 19:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.PropertyValuesEditor
 * JD-Core Version:    0.7.0.1
 */