/*   1:    */ package org.springframework.web.context.support;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileNotFoundException;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.net.MalformedURLException;
/*   8:    */ import java.net.URL;
/*   9:    */ import javax.servlet.ServletContext;
/*  10:    */ import org.springframework.core.io.AbstractFileResolvingResource;
/*  11:    */ import org.springframework.core.io.ContextResource;
/*  12:    */ import org.springframework.core.io.Resource;
/*  13:    */ import org.springframework.util.Assert;
/*  14:    */ import org.springframework.util.StringUtils;
/*  15:    */ import org.springframework.web.util.WebUtils;
/*  16:    */ 
/*  17:    */ public class ServletContextResource
/*  18:    */   extends AbstractFileResolvingResource
/*  19:    */   implements ContextResource
/*  20:    */ {
/*  21:    */   private final ServletContext servletContext;
/*  22:    */   private final String path;
/*  23:    */   
/*  24:    */   public ServletContextResource(ServletContext servletContext, String path)
/*  25:    */   {
/*  26: 67 */     Assert.notNull(servletContext, "Cannot resolve ServletContextResource without ServletContext");
/*  27: 68 */     this.servletContext = servletContext;
/*  28:    */     
/*  29:    */ 
/*  30: 71 */     Assert.notNull(path, "Path is required");
/*  31: 72 */     String pathToUse = StringUtils.cleanPath(path);
/*  32: 73 */     if (!pathToUse.startsWith("/")) {
/*  33: 74 */       pathToUse = "/" + pathToUse;
/*  34:    */     }
/*  35: 76 */     this.path = pathToUse;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public final ServletContext getServletContext()
/*  39:    */   {
/*  40: 83 */     return this.servletContext;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public final String getPath()
/*  44:    */   {
/*  45: 90 */     return this.path;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public boolean exists()
/*  49:    */   {
/*  50:    */     try
/*  51:    */     {
/*  52:101 */       URL url = this.servletContext.getResource(this.path);
/*  53:102 */       return url != null;
/*  54:    */     }
/*  55:    */     catch (MalformedURLException localMalformedURLException) {}
/*  56:105 */     return false;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public InputStream getInputStream()
/*  60:    */     throws IOException
/*  61:    */   {
/*  62:115 */     InputStream is = this.servletContext.getResourceAsStream(this.path);
/*  63:116 */     if (is == null) {
/*  64:117 */       throw new FileNotFoundException("Could not open " + getDescription());
/*  65:    */     }
/*  66:119 */     return is;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public URL getURL()
/*  70:    */     throws IOException
/*  71:    */   {
/*  72:129 */     URL url = this.servletContext.getResource(this.path);
/*  73:130 */     if (url == null) {
/*  74:131 */       throw new FileNotFoundException(
/*  75:132 */         getDescription() + " cannot be resolved to URL because it does not exist");
/*  76:    */     }
/*  77:134 */     return url;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public File getFile()
/*  81:    */     throws IOException
/*  82:    */   {
/*  83:144 */     String realPath = WebUtils.getRealPath(this.servletContext, this.path);
/*  84:145 */     return new File(realPath);
/*  85:    */   }
/*  86:    */   
/*  87:    */   public Resource createRelative(String relativePath)
/*  88:    */   {
/*  89:155 */     String pathToUse = StringUtils.applyRelativePath(this.path, relativePath);
/*  90:156 */     return new ServletContextResource(this.servletContext, pathToUse);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public String getFilename()
/*  94:    */   {
/*  95:166 */     return StringUtils.getFilename(this.path);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public String getDescription()
/*  99:    */   {
/* 100:174 */     return "ServletContext resource [" + this.path + "]";
/* 101:    */   }
/* 102:    */   
/* 103:    */   public String getPathWithinContext()
/* 104:    */   {
/* 105:178 */     return this.path;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public boolean equals(Object obj)
/* 109:    */   {
/* 110:187 */     if (obj == this) {
/* 111:188 */       return true;
/* 112:    */     }
/* 113:190 */     if ((obj instanceof ServletContextResource))
/* 114:    */     {
/* 115:191 */       ServletContextResource otherRes = (ServletContextResource)obj;
/* 116:192 */       return (this.servletContext.equals(otherRes.servletContext)) && (this.path.equals(otherRes.path));
/* 117:    */     }
/* 118:194 */     return false;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public int hashCode()
/* 122:    */   {
/* 123:203 */     return this.path.hashCode();
/* 124:    */   }
/* 125:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.support.ServletContextResource
 * JD-Core Version:    0.7.0.1
 */