/*  1:   */ package org.springframework.beans.propertyeditors;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditorSupport;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.net.URL;
/*  6:   */ import org.springframework.core.io.Resource;
/*  7:   */ import org.springframework.core.io.ResourceEditor;
/*  8:   */ import org.springframework.util.Assert;
/*  9:   */ 
/* 10:   */ public class URLEditor
/* 11:   */   extends PropertyEditorSupport
/* 12:   */ {
/* 13:   */   private final ResourceEditor resourceEditor;
/* 14:   */   
/* 15:   */   public URLEditor()
/* 16:   */   {
/* 17:56 */     this.resourceEditor = new ResourceEditor();
/* 18:   */   }
/* 19:   */   
/* 20:   */   public URLEditor(ResourceEditor resourceEditor)
/* 21:   */   {
/* 22:64 */     Assert.notNull(resourceEditor, "ResourceEditor must not be null");
/* 23:65 */     this.resourceEditor = resourceEditor;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void setAsText(String text)
/* 27:   */     throws IllegalArgumentException
/* 28:   */   {
/* 29:71 */     this.resourceEditor.setAsText(text);
/* 30:72 */     Resource resource = (Resource)this.resourceEditor.getValue();
/* 31:   */     try
/* 32:   */     {
/* 33:74 */       setValue(resource != null ? resource.getURL() : null);
/* 34:   */     }
/* 35:   */     catch (IOException ex)
/* 36:   */     {
/* 37:77 */       throw new IllegalArgumentException("Could not retrieve URL for " + resource + ": " + ex.getMessage());
/* 38:   */     }
/* 39:   */   }
/* 40:   */   
/* 41:   */   public String getAsText()
/* 42:   */   {
/* 43:83 */     URL value = (URL)getValue();
/* 44:84 */     return value != null ? value.toExternalForm() : "";
/* 45:   */   }
/* 46:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.URLEditor
 * JD-Core Version:    0.7.0.1
 */