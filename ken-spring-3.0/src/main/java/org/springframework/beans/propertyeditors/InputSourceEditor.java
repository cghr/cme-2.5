/*  1:   */ package org.springframework.beans.propertyeditors;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditorSupport;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.net.URL;
/*  6:   */ import org.springframework.core.io.Resource;
/*  7:   */ import org.springframework.core.io.ResourceEditor;
/*  8:   */ import org.springframework.util.Assert;
/*  9:   */ import org.xml.sax.InputSource;
/* 10:   */ 
/* 11:   */ public class InputSourceEditor
/* 12:   */   extends PropertyEditorSupport
/* 13:   */ {
/* 14:   */   private final ResourceEditor resourceEditor;
/* 15:   */   
/* 16:   */   public InputSourceEditor()
/* 17:   */   {
/* 18:53 */     this.resourceEditor = new ResourceEditor();
/* 19:   */   }
/* 20:   */   
/* 21:   */   public InputSourceEditor(ResourceEditor resourceEditor)
/* 22:   */   {
/* 23:62 */     Assert.notNull(resourceEditor, "ResourceEditor must not be null");
/* 24:63 */     this.resourceEditor = resourceEditor;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void setAsText(String text)
/* 28:   */     throws IllegalArgumentException
/* 29:   */   {
/* 30:69 */     this.resourceEditor.setAsText(text);
/* 31:70 */     Resource resource = (Resource)this.resourceEditor.getValue();
/* 32:   */     try
/* 33:   */     {
/* 34:72 */       setValue(resource != null ? new InputSource(resource.getURL().toString()) : null);
/* 35:   */     }
/* 36:   */     catch (IOException ex)
/* 37:   */     {
/* 38:75 */       throw new IllegalArgumentException(
/* 39:76 */         "Could not retrieve URL for " + resource + ": " + ex.getMessage());
/* 40:   */     }
/* 41:   */   }
/* 42:   */   
/* 43:   */   public String getAsText()
/* 44:   */   {
/* 45:82 */     InputSource value = (InputSource)getValue();
/* 46:83 */     return value != null ? value.getSystemId() : "";
/* 47:   */   }
/* 48:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.InputSourceEditor
 * JD-Core Version:    0.7.0.1
 */