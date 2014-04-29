/*   1:    */ package org.springframework.core.io;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.net.URI;
/*   7:    */ import java.net.URL;
/*   8:    */ import org.springframework.core.NestedIOException;
/*   9:    */ import org.springframework.util.Assert;
/*  10:    */ 
/*  11:    */ public class VfsResource
/*  12:    */   extends AbstractResource
/*  13:    */ {
/*  14:    */   private final Object resource;
/*  15:    */   
/*  16:    */   public VfsResource(Object resources)
/*  17:    */   {
/*  18: 45 */     Assert.notNull(resources, "VirtualFile must not be null");
/*  19: 46 */     this.resource = resources;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public boolean exists()
/*  23:    */   {
/*  24: 51 */     return VfsUtils.exists(this.resource);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public boolean isReadable()
/*  28:    */   {
/*  29: 55 */     return VfsUtils.isReadable(this.resource);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public long lastModified()
/*  33:    */     throws IOException
/*  34:    */   {
/*  35: 59 */     return VfsUtils.getLastModified(this.resource);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public InputStream getInputStream()
/*  39:    */     throws IOException
/*  40:    */   {
/*  41: 63 */     return VfsUtils.getInputStream(this.resource);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public URL getURL()
/*  45:    */     throws IOException
/*  46:    */   {
/*  47:    */     try
/*  48:    */     {
/*  49: 68 */       return VfsUtils.getURL(this.resource);
/*  50:    */     }
/*  51:    */     catch (Exception ex)
/*  52:    */     {
/*  53: 71 */       throw new NestedIOException("Failed to obtain URL for file " + this.resource, ex);
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   public URI getURI()
/*  58:    */     throws IOException
/*  59:    */   {
/*  60:    */     try
/*  61:    */     {
/*  62: 77 */       return VfsUtils.getURI(this.resource);
/*  63:    */     }
/*  64:    */     catch (Exception ex)
/*  65:    */     {
/*  66: 80 */       throw new NestedIOException("Failed to obtain URI for " + this.resource, ex);
/*  67:    */     }
/*  68:    */   }
/*  69:    */   
/*  70:    */   public File getFile()
/*  71:    */     throws IOException
/*  72:    */   {
/*  73: 85 */     return VfsUtils.getFile(this.resource);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public Resource createRelative(String relativePath)
/*  77:    */     throws IOException
/*  78:    */   {
/*  79: 89 */     if ((!relativePath.startsWith(".")) && (relativePath.contains("/"))) {
/*  80:    */       try
/*  81:    */       {
/*  82: 91 */         return new VfsResource(VfsUtils.getChild(this.resource, relativePath));
/*  83:    */       }
/*  84:    */       catch (IOException localIOException) {}
/*  85:    */     }
/*  86: 98 */     return new VfsResource(VfsUtils.getRelative(new URL(getURL(), relativePath)));
/*  87:    */   }
/*  88:    */   
/*  89:    */   public String getFilename()
/*  90:    */   {
/*  91:102 */     return VfsUtils.getName(this.resource);
/*  92:    */   }
/*  93:    */   
/*  94:    */   public String getDescription()
/*  95:    */   {
/*  96:106 */     return this.resource.toString();
/*  97:    */   }
/*  98:    */   
/*  99:    */   public boolean equals(Object obj)
/* 100:    */   {
/* 101:111 */     return (obj == this) || (((obj instanceof VfsResource)) && (this.resource.equals(((VfsResource)obj).resource)));
/* 102:    */   }
/* 103:    */   
/* 104:    */   public int hashCode()
/* 105:    */   {
/* 106:116 */     return this.resource.hashCode();
/* 107:    */   }
/* 108:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.VfsResource
 * JD-Core Version:    0.7.0.1
 */