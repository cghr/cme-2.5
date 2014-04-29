/*   1:    */ package org.springframework.core.io;
/*   2:    */ 
/*   3:    */ import java.net.MalformedURLException;
/*   4:    */ import java.net.URL;
/*   5:    */ import org.springframework.util.Assert;
/*   6:    */ import org.springframework.util.ClassUtils;
/*   7:    */ import org.springframework.util.StringUtils;
/*   8:    */ 
/*   9:    */ public class DefaultResourceLoader
/*  10:    */   implements ResourceLoader
/*  11:    */ {
/*  12:    */   private ClassLoader classLoader;
/*  13:    */   
/*  14:    */   public DefaultResourceLoader()
/*  15:    */   {
/*  16: 53 */     this.classLoader = ClassUtils.getDefaultClassLoader();
/*  17:    */   }
/*  18:    */   
/*  19:    */   public DefaultResourceLoader(ClassLoader classLoader)
/*  20:    */   {
/*  21: 62 */     this.classLoader = classLoader;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void setClassLoader(ClassLoader classLoader)
/*  25:    */   {
/*  26: 73 */     this.classLoader = classLoader;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public ClassLoader getClassLoader()
/*  30:    */   {
/*  31: 83 */     return this.classLoader != null ? this.classLoader : ClassUtils.getDefaultClassLoader();
/*  32:    */   }
/*  33:    */   
/*  34:    */   public Resource getResource(String location)
/*  35:    */   {
/*  36: 88 */     Assert.notNull(location, "Location must not be null");
/*  37: 89 */     if (location.startsWith("classpath:")) {
/*  38: 90 */       return new ClassPathResource(location.substring("classpath:".length()), getClassLoader());
/*  39:    */     }
/*  40:    */     try
/*  41:    */     {
/*  42: 95 */       URL url = new URL(location);
/*  43: 96 */       return new UrlResource(url);
/*  44:    */     }
/*  45:    */     catch (MalformedURLException localMalformedURLException) {}
/*  46:100 */     return getResourceByPath(location);
/*  47:    */   }
/*  48:    */   
/*  49:    */   protected Resource getResourceByPath(String path)
/*  50:    */   {
/*  51:117 */     return new ClassPathContextResource(path, getClassLoader());
/*  52:    */   }
/*  53:    */   
/*  54:    */   private static class ClassPathContextResource
/*  55:    */     extends ClassPathResource
/*  56:    */     implements ContextResource
/*  57:    */   {
/*  58:    */     public ClassPathContextResource(String path, ClassLoader classLoader)
/*  59:    */     {
/*  60:128 */       super(classLoader);
/*  61:    */     }
/*  62:    */     
/*  63:    */     public String getPathWithinContext()
/*  64:    */     {
/*  65:132 */       return getPath();
/*  66:    */     }
/*  67:    */     
/*  68:    */     public Resource createRelative(String relativePath)
/*  69:    */     {
/*  70:137 */       String pathToUse = StringUtils.applyRelativePath(getPath(), relativePath);
/*  71:138 */       return new ClassPathContextResource(pathToUse, getClassLoader());
/*  72:    */     }
/*  73:    */   }
/*  74:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.DefaultResourceLoader
 * JD-Core Version:    0.7.0.1
 */