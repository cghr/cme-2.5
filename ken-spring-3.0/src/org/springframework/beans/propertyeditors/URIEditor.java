/*   1:    */ package org.springframework.beans.propertyeditors;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditorSupport;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.net.URI;
/*   6:    */ import java.net.URISyntaxException;
/*   7:    */ import java.net.URL;
/*   8:    */ import org.springframework.core.io.ClassPathResource;
/*   9:    */ import org.springframework.util.ClassUtils;
/*  10:    */ import org.springframework.util.StringUtils;
/*  11:    */ 
/*  12:    */ public class URIEditor
/*  13:    */   extends PropertyEditorSupport
/*  14:    */ {
/*  15:    */   private final ClassLoader classLoader;
/*  16:    */   private final boolean encode;
/*  17:    */   
/*  18:    */   public URIEditor()
/*  19:    */   {
/*  20: 63 */     this.classLoader = null;
/*  21: 64 */     this.encode = true;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public URIEditor(boolean encode)
/*  25:    */   {
/*  26: 73 */     this.classLoader = null;
/*  27: 74 */     this.encode = encode;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public URIEditor(ClassLoader classLoader)
/*  31:    */   {
/*  32: 85 */     this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
/*  33: 86 */     this.encode = true;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public URIEditor(ClassLoader classLoader, boolean encode)
/*  37:    */   {
/*  38: 97 */     this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
/*  39: 98 */     this.encode = encode;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setAsText(String text)
/*  43:    */     throws IllegalArgumentException
/*  44:    */   {
/*  45:104 */     if (StringUtils.hasText(text))
/*  46:    */     {
/*  47:105 */       String uri = text.trim();
/*  48:106 */       if ((this.classLoader != null) && (uri.startsWith("classpath:")))
/*  49:    */       {
/*  50:107 */         ClassPathResource resource = 
/*  51:108 */           new ClassPathResource(uri.substring("classpath:".length()), this.classLoader);
/*  52:    */         try
/*  53:    */         {
/*  54:110 */           String url = resource.getURL().toString();
/*  55:111 */           setValue(createURI(url));
/*  56:    */         }
/*  57:    */         catch (IOException ex)
/*  58:    */         {
/*  59:114 */           throw new IllegalArgumentException("Could not retrieve URI for " + resource + ": " + ex.getMessage());
/*  60:    */         }
/*  61:    */         catch (URISyntaxException ex)
/*  62:    */         {
/*  63:117 */           throw new IllegalArgumentException("Invalid URI syntax: " + ex);
/*  64:    */         }
/*  65:    */       }
/*  66:    */       else
/*  67:    */       {
/*  68:    */         try
/*  69:    */         {
/*  70:122 */           setValue(createURI(uri));
/*  71:    */         }
/*  72:    */         catch (URISyntaxException ex)
/*  73:    */         {
/*  74:125 */           throw new IllegalArgumentException("Invalid URI syntax: " + ex);
/*  75:    */         }
/*  76:    */       }
/*  77:    */     }
/*  78:    */     else
/*  79:    */     {
/*  80:130 */       setValue(null);
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   protected URI createURI(String value)
/*  85:    */     throws URISyntaxException
/*  86:    */   {
/*  87:143 */     int colonIndex = value.indexOf(':');
/*  88:144 */     if ((this.encode) && (colonIndex != -1))
/*  89:    */     {
/*  90:145 */       int fragmentIndex = value.indexOf('#', colonIndex + 1);
/*  91:146 */       String scheme = value.substring(0, colonIndex);
/*  92:147 */       String ssp = value.substring(colonIndex + 1, fragmentIndex > 0 ? fragmentIndex : value.length());
/*  93:148 */       String fragment = fragmentIndex > 0 ? value.substring(fragmentIndex + 1) : null;
/*  94:149 */       return new URI(scheme, ssp, fragment);
/*  95:    */     }
/*  96:153 */     return new URI(value);
/*  97:    */   }
/*  98:    */   
/*  99:    */   public String getAsText()
/* 100:    */   {
/* 101:160 */     URI value = (URI)getValue();
/* 102:161 */     return value != null ? value.toString() : "";
/* 103:    */   }
/* 104:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.URIEditor
 * JD-Core Version:    0.7.0.1
 */