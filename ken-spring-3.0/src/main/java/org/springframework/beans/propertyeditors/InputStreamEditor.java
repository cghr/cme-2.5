/*  1:   */ package org.springframework.beans.propertyeditors;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditorSupport;
/*  4:   */ import java.io.IOException;
/*  5:   */ import org.springframework.core.io.Resource;
/*  6:   */ import org.springframework.core.io.ResourceEditor;
/*  7:   */ import org.springframework.util.Assert;
/*  8:   */ 
/*  9:   */ public class InputStreamEditor
/* 10:   */   extends PropertyEditorSupport
/* 11:   */ {
/* 12:   */   private final ResourceEditor resourceEditor;
/* 13:   */   
/* 14:   */   public InputStreamEditor()
/* 15:   */   {
/* 16:54 */     this.resourceEditor = new ResourceEditor();
/* 17:   */   }
/* 18:   */   
/* 19:   */   public InputStreamEditor(ResourceEditor resourceEditor)
/* 20:   */   {
/* 21:63 */     Assert.notNull(resourceEditor, "ResourceEditor must not be null");
/* 22:64 */     this.resourceEditor = resourceEditor;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public void setAsText(String text)
/* 26:   */     throws IllegalArgumentException
/* 27:   */   {
/* 28:70 */     this.resourceEditor.setAsText(text);
/* 29:71 */     Resource resource = (Resource)this.resourceEditor.getValue();
/* 30:   */     try
/* 31:   */     {
/* 32:73 */       setValue(resource != null ? resource.getInputStream() : null);
/* 33:   */     }
/* 34:   */     catch (IOException ex)
/* 35:   */     {
/* 36:76 */       throw new IllegalArgumentException(
/* 37:77 */         "Could not retrieve InputStream for " + resource + ": " + ex.getMessage());
/* 38:   */     }
/* 39:   */   }
/* 40:   */   
/* 41:   */   public String getAsText()
/* 42:   */   {
/* 43:87 */     return null;
/* 44:   */   }
/* 45:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.InputStreamEditor
 * JD-Core Version:    0.7.0.1
 */