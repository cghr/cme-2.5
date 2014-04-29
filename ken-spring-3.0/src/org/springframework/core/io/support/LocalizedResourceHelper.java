/*   1:    */ package org.springframework.core.io.support;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ import org.springframework.core.io.DefaultResourceLoader;
/*   5:    */ import org.springframework.core.io.Resource;
/*   6:    */ import org.springframework.core.io.ResourceLoader;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ 
/*   9:    */ public class LocalizedResourceHelper
/*  10:    */ {
/*  11:    */   public static final String DEFAULT_SEPARATOR = "_";
/*  12:    */   private final ResourceLoader resourceLoader;
/*  13: 41 */   private String separator = "_";
/*  14:    */   
/*  15:    */   public LocalizedResourceHelper()
/*  16:    */   {
/*  17: 49 */     this.resourceLoader = new DefaultResourceLoader();
/*  18:    */   }
/*  19:    */   
/*  20:    */   public LocalizedResourceHelper(ResourceLoader resourceLoader)
/*  21:    */   {
/*  22: 57 */     Assert.notNull(resourceLoader, "ResourceLoader must not be null");
/*  23: 58 */     this.resourceLoader = resourceLoader;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setSeparator(String separator)
/*  27:    */   {
/*  28: 66 */     this.separator = (separator != null ? separator : "_");
/*  29:    */   }
/*  30:    */   
/*  31:    */   public Resource findLocalizedResource(String name, String extension, Locale locale)
/*  32:    */   {
/*  33: 90 */     Assert.notNull(name, "Name must not be null");
/*  34: 91 */     Assert.notNull(extension, "Extension must not be null");
/*  35:    */     
/*  36: 93 */     Resource resource = null;
/*  37: 95 */     if (locale != null)
/*  38:    */     {
/*  39: 96 */       String lang = locale.getLanguage();
/*  40: 97 */       String country = locale.getCountry();
/*  41: 98 */       String variant = locale.getVariant();
/*  42:101 */       if (variant.length() > 0)
/*  43:    */       {
/*  44:102 */         String location = 
/*  45:103 */           name + this.separator + lang + this.separator + country + this.separator + variant + extension;
/*  46:104 */         resource = this.resourceLoader.getResource(location);
/*  47:    */       }
/*  48:108 */       if (((resource == null) || (!resource.exists())) && (country.length() > 0))
/*  49:    */       {
/*  50:109 */         String location = name + this.separator + lang + this.separator + country + extension;
/*  51:110 */         resource = this.resourceLoader.getResource(location);
/*  52:    */       }
/*  53:114 */       if (((resource == null) || (!resource.exists())) && (lang.length() > 0))
/*  54:    */       {
/*  55:115 */         String location = name + this.separator + lang + extension;
/*  56:116 */         resource = this.resourceLoader.getResource(location);
/*  57:    */       }
/*  58:    */     }
/*  59:121 */     if ((resource == null) || (!resource.exists()))
/*  60:    */     {
/*  61:122 */       String location = name + extension;
/*  62:123 */       resource = this.resourceLoader.getResource(location);
/*  63:    */     }
/*  64:126 */     return resource;
/*  65:    */   }
/*  66:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.support.LocalizedResourceHelper
 * JD-Core Version:    0.7.0.1
 */