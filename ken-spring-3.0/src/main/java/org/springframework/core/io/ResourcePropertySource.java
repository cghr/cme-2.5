/*   1:    */ package org.springframework.core.io;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.util.Properties;
/*   6:    */ import org.springframework.core.env.PropertiesPropertySource;
/*   7:    */ import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
/*   8:    */ import org.springframework.util.ClassUtils;
/*   9:    */ import org.springframework.util.StringUtils;
/*  10:    */ 
/*  11:    */ public class ResourcePropertySource
/*  12:    */   extends PropertiesPropertySource
/*  13:    */ {
/*  14:    */   public ResourcePropertySource(String name, Resource resource)
/*  15:    */     throws IOException
/*  16:    */   {
/*  17: 43 */     super(name, loadPropertiesForResource(resource));
/*  18:    */   }
/*  19:    */   
/*  20:    */   public ResourcePropertySource(Resource resource)
/*  21:    */     throws IOException
/*  22:    */   {
/*  23: 52 */     this(getNameForResource(resource), resource);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public ResourcePropertySource(String name, String location, ClassLoader classLoader)
/*  27:    */     throws IOException
/*  28:    */   {
/*  29: 61 */     this(name, getResourceForLocation(location, classLoader));
/*  30:    */   }
/*  31:    */   
/*  32:    */   public ResourcePropertySource(String name, String location)
/*  33:    */     throws IOException
/*  34:    */   {
/*  35: 71 */     this(name, location, ClassUtils.getDefaultClassLoader());
/*  36:    */   }
/*  37:    */   
/*  38:    */   public ResourcePropertySource(String location, ClassLoader classLoader)
/*  39:    */     throws IOException
/*  40:    */   {
/*  41: 82 */     this(getResourceForLocation(location, classLoader));
/*  42:    */   }
/*  43:    */   
/*  44:    */   public ResourcePropertySource(String location)
/*  45:    */     throws IOException
/*  46:    */   {
/*  47: 91 */     this(getResourceForLocation(location, ClassUtils.getDefaultClassLoader()));
/*  48:    */   }
/*  49:    */   
/*  50:    */   private static Resource getResourceForLocation(String location, ClassLoader classLoader)
/*  51:    */   {
/*  52: 96 */     return new PathMatchingResourcePatternResolver(classLoader).getResource(location);
/*  53:    */   }
/*  54:    */   
/*  55:    */   private static Properties loadPropertiesForResource(Resource resource)
/*  56:    */     throws IOException
/*  57:    */   {
/*  58:100 */     Properties props = new Properties();
/*  59:101 */     InputStream is = resource.getInputStream();
/*  60:102 */     props.load(is);
/*  61:    */     try
/*  62:    */     {
/*  63:104 */       is.close();
/*  64:    */     }
/*  65:    */     catch (IOException localIOException) {}
/*  66:108 */     return props;
/*  67:    */   }
/*  68:    */   
/*  69:    */   private static String getNameForResource(Resource resource)
/*  70:    */   {
/*  71:116 */     String name = resource.getDescription();
/*  72:117 */     if (!StringUtils.hasText(name)) {
/*  73:118 */       name = resource.getClass().getSimpleName() + "@" + System.identityHashCode(resource);
/*  74:    */     }
/*  75:120 */     return name;
/*  76:    */   }
/*  77:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.ResourcePropertySource
 * JD-Core Version:    0.7.0.1
 */