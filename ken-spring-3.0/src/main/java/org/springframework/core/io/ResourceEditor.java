/*   1:    */ package org.springframework.core.io;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditorSupport;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.net.URL;
/*   6:    */ import org.springframework.core.env.PropertyResolver;
/*   7:    */ import org.springframework.core.env.StandardEnvironment;
/*   8:    */ import org.springframework.util.Assert;
/*   9:    */ import org.springframework.util.StringUtils;
/*  10:    */ 
/*  11:    */ public class ResourceEditor
/*  12:    */   extends PropertyEditorSupport
/*  13:    */ {
/*  14:    */   private final ResourceLoader resourceLoader;
/*  15:    */   private final PropertyResolver propertyResolver;
/*  16:    */   private final boolean ignoreUnresolvablePlaceholders;
/*  17:    */   
/*  18:    */   public ResourceEditor()
/*  19:    */   {
/*  20: 64 */     this(new DefaultResourceLoader(), new StandardEnvironment());
/*  21:    */   }
/*  22:    */   
/*  23:    */   @Deprecated
/*  24:    */   public ResourceEditor(ResourceLoader resourceLoader)
/*  25:    */   {
/*  26: 76 */     this(resourceLoader, new StandardEnvironment(), true);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public ResourceEditor(ResourceLoader resourceLoader, PropertyResolver propertyResolver)
/*  30:    */   {
/*  31: 86 */     this(resourceLoader, propertyResolver, true);
/*  32:    */   }
/*  33:    */   
/*  34:    */   @Deprecated
/*  35:    */   public ResourceEditor(ResourceLoader resourceLoader, boolean ignoreUnresolvablePlaceholders)
/*  36:    */   {
/*  37:100 */     this(resourceLoader, new StandardEnvironment(), ignoreUnresolvablePlaceholders);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public ResourceEditor(ResourceLoader resourceLoader, PropertyResolver propertyResolver, boolean ignoreUnresolvablePlaceholders)
/*  41:    */   {
/*  42:112 */     Assert.notNull(resourceLoader, "ResourceLoader must not be null");
/*  43:113 */     Assert.notNull(propertyResolver, "PropertyResolver must not be null");
/*  44:114 */     this.resourceLoader = resourceLoader;
/*  45:115 */     this.propertyResolver = propertyResolver;
/*  46:116 */     this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void setAsText(String text)
/*  50:    */   {
/*  51:122 */     if (StringUtils.hasText(text))
/*  52:    */     {
/*  53:123 */       String locationToUse = resolvePath(text).trim();
/*  54:124 */       setValue(this.resourceLoader.getResource(locationToUse));
/*  55:    */     }
/*  56:    */     else
/*  57:    */     {
/*  58:127 */       setValue(null);
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   protected String resolvePath(String path)
/*  63:    */   {
/*  64:140 */     return this.ignoreUnresolvablePlaceholders ? 
/*  65:141 */       this.propertyResolver.resolvePlaceholders(path) : 
/*  66:142 */       this.propertyResolver.resolveRequiredPlaceholders(path);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public String getAsText()
/*  70:    */   {
/*  71:148 */     Resource value = (Resource)getValue();
/*  72:    */     try
/*  73:    */     {
/*  74:151 */       return value != null ? value.getURL().toExternalForm() : "";
/*  75:    */     }
/*  76:    */     catch (IOException localIOException) {}
/*  77:156 */     return null;
/*  78:    */   }
/*  79:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.ResourceEditor
 * JD-Core Version:    0.7.0.1
 */