/*   1:    */ package org.springframework.beans.support;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditor;
/*   4:    */ import java.io.File;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.net.URI;
/*   7:    */ import java.net.URL;
/*   8:    */ import org.springframework.beans.PropertyEditorRegistrar;
/*   9:    */ import org.springframework.beans.PropertyEditorRegistry;
/*  10:    */ import org.springframework.beans.PropertyEditorRegistrySupport;
/*  11:    */ import org.springframework.beans.propertyeditors.ClassArrayEditor;
/*  12:    */ import org.springframework.beans.propertyeditors.ClassEditor;
/*  13:    */ import org.springframework.beans.propertyeditors.FileEditor;
/*  14:    */ import org.springframework.beans.propertyeditors.InputSourceEditor;
/*  15:    */ import org.springframework.beans.propertyeditors.InputStreamEditor;
/*  16:    */ import org.springframework.beans.propertyeditors.URIEditor;
/*  17:    */ import org.springframework.beans.propertyeditors.URLEditor;
/*  18:    */ import org.springframework.core.env.PropertyResolver;
/*  19:    */ import org.springframework.core.env.StandardEnvironment;
/*  20:    */ import org.springframework.core.io.ContextResource;
/*  21:    */ import org.springframework.core.io.Resource;
/*  22:    */ import org.springframework.core.io.ResourceEditor;
/*  23:    */ import org.springframework.core.io.ResourceLoader;
/*  24:    */ import org.springframework.core.io.support.ResourceArrayPropertyEditor;
/*  25:    */ import org.springframework.core.io.support.ResourcePatternResolver;
/*  26:    */ import org.xml.sax.InputSource;
/*  27:    */ 
/*  28:    */ public class ResourceEditorRegistrar
/*  29:    */   implements PropertyEditorRegistrar
/*  30:    */ {
/*  31:    */   private final PropertyResolver propertyResolver;
/*  32:    */   private final ResourceLoader resourceLoader;
/*  33:    */   
/*  34:    */   @Deprecated
/*  35:    */   public ResourceEditorRegistrar(ResourceLoader resourceLoader)
/*  36:    */   {
/*  37: 78 */     this(resourceLoader, new StandardEnvironment());
/*  38:    */   }
/*  39:    */   
/*  40:    */   public ResourceEditorRegistrar(ResourceLoader resourceLoader, PropertyResolver propertyResolver)
/*  41:    */   {
/*  42: 89 */     this.resourceLoader = resourceLoader;
/*  43: 90 */     this.propertyResolver = propertyResolver;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void registerCustomEditors(PropertyEditorRegistry registry)
/*  47:    */   {
/*  48:111 */     ResourceEditor baseEditor = new ResourceEditor(this.resourceLoader, this.propertyResolver);
/*  49:112 */     doRegisterEditor(registry, Resource.class, baseEditor);
/*  50:113 */     doRegisterEditor(registry, ContextResource.class, baseEditor);
/*  51:114 */     doRegisterEditor(registry, InputStream.class, new InputStreamEditor(baseEditor));
/*  52:115 */     doRegisterEditor(registry, InputSource.class, new InputSourceEditor(baseEditor));
/*  53:116 */     doRegisterEditor(registry, File.class, new FileEditor(baseEditor));
/*  54:117 */     doRegisterEditor(registry, URL.class, new URLEditor(baseEditor));
/*  55:    */     
/*  56:119 */     ClassLoader classLoader = this.resourceLoader.getClassLoader();
/*  57:120 */     doRegisterEditor(registry, URI.class, new URIEditor(classLoader));
/*  58:121 */     doRegisterEditor(registry, Class.class, new ClassEditor(classLoader));
/*  59:122 */     doRegisterEditor(registry, [Ljava.lang.Class.class, new ClassArrayEditor(classLoader));
/*  60:124 */     if ((this.resourceLoader instanceof ResourcePatternResolver)) {
/*  61:125 */       doRegisterEditor(registry, [Lorg.springframework.core.io.Resource.class, 
/*  62:126 */         new ResourceArrayPropertyEditor((ResourcePatternResolver)this.resourceLoader, this.propertyResolver));
/*  63:    */     }
/*  64:    */   }
/*  65:    */   
/*  66:    */   private void doRegisterEditor(PropertyEditorRegistry registry, Class<?> requiredType, PropertyEditor editor)
/*  67:    */   {
/*  68:135 */     if ((registry instanceof PropertyEditorRegistrySupport)) {
/*  69:136 */       ((PropertyEditorRegistrySupport)registry).overrideDefaultEditor(requiredType, editor);
/*  70:    */     } else {
/*  71:139 */       registry.registerCustomEditor(requiredType, editor);
/*  72:    */     }
/*  73:    */   }
/*  74:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.support.ResourceEditorRegistrar
 * JD-Core Version:    0.7.0.1
 */