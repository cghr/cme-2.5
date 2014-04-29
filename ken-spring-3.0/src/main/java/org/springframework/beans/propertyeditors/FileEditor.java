/*   1:    */ package org.springframework.beans.propertyeditors;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditorSupport;
/*   4:    */ import java.io.File;
/*   5:    */ import java.io.IOException;
/*   6:    */ import org.springframework.core.io.Resource;
/*   7:    */ import org.springframework.core.io.ResourceEditor;
/*   8:    */ import org.springframework.util.Assert;
/*   9:    */ import org.springframework.util.ResourceUtils;
/*  10:    */ import org.springframework.util.StringUtils;
/*  11:    */ 
/*  12:    */ public class FileEditor
/*  13:    */   extends PropertyEditorSupport
/*  14:    */ {
/*  15:    */   private final ResourceEditor resourceEditor;
/*  16:    */   
/*  17:    */   public FileEditor()
/*  18:    */   {
/*  19: 66 */     this.resourceEditor = new ResourceEditor();
/*  20:    */   }
/*  21:    */   
/*  22:    */   public FileEditor(ResourceEditor resourceEditor)
/*  23:    */   {
/*  24: 75 */     Assert.notNull(resourceEditor, "ResourceEditor must not be null");
/*  25: 76 */     this.resourceEditor = resourceEditor;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setAsText(String text)
/*  29:    */     throws IllegalArgumentException
/*  30:    */   {
/*  31: 82 */     if (!StringUtils.hasText(text))
/*  32:    */     {
/*  33: 83 */       setValue(null);
/*  34: 84 */       return;
/*  35:    */     }
/*  36: 89 */     if (!ResourceUtils.isUrl(text))
/*  37:    */     {
/*  38: 90 */       File file = new File(text);
/*  39: 91 */       if (file.isAbsolute())
/*  40:    */       {
/*  41: 92 */         setValue(file);
/*  42: 93 */         return;
/*  43:    */       }
/*  44:    */     }
/*  45: 98 */     this.resourceEditor.setAsText(text);
/*  46: 99 */     Resource resource = (Resource)this.resourceEditor.getValue();
/*  47:102 */     if ((ResourceUtils.isUrl(text)) || (resource.exists())) {
/*  48:    */       try
/*  49:    */       {
/*  50:104 */         setValue(resource.getFile());
/*  51:    */       }
/*  52:    */       catch (IOException ex)
/*  53:    */       {
/*  54:107 */         throw new IllegalArgumentException(
/*  55:108 */           "Could not retrieve File for " + resource + ": " + ex.getMessage());
/*  56:    */       }
/*  57:    */     } else {
/*  58:113 */       setValue(new File(text));
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   public String getAsText()
/*  63:    */   {
/*  64:119 */     File value = (File)getValue();
/*  65:120 */     return value != null ? value.getPath() : "";
/*  66:    */   }
/*  67:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.FileEditor
 * JD-Core Version:    0.7.0.1
 */