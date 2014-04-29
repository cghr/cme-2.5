/*   1:    */ package org.springframework.instrument.classloading;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.net.URL;
/*   6:    */ import java.util.Enumeration;
/*   7:    */ import java.util.HashMap;
/*   8:    */ import java.util.Map;
/*   9:    */ import org.springframework.util.Assert;
/*  10:    */ 
/*  11:    */ public class ResourceOverridingShadowingClassLoader
/*  12:    */   extends ShadowingClassLoader
/*  13:    */ {
/*  14: 38 */   private static final Enumeration<URL> EMPTY_URL_ENUMERATION = new Enumeration()
/*  15:    */   {
/*  16:    */     public boolean hasMoreElements()
/*  17:    */     {
/*  18: 40 */       return false;
/*  19:    */     }
/*  20:    */     
/*  21:    */     public URL nextElement()
/*  22:    */     {
/*  23: 43 */       throw new UnsupportedOperationException("Should not be called. I am empty.");
/*  24:    */     }
/*  25:    */   };
/*  26: 51 */   private Map<String, String> overrides = new HashMap();
/*  27:    */   
/*  28:    */   public ResourceOverridingShadowingClassLoader(ClassLoader enclosingClassLoader)
/*  29:    */   {
/*  30: 60 */     super(enclosingClassLoader);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void override(String oldPath, String newPath)
/*  34:    */   {
/*  35: 71 */     this.overrides.put(oldPath, newPath);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void suppress(String oldPath)
/*  39:    */   {
/*  40: 80 */     this.overrides.put(oldPath, null);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void copyOverrides(ResourceOverridingShadowingClassLoader other)
/*  44:    */   {
/*  45: 88 */     Assert.notNull(other, "Other ClassLoader must not be null");
/*  46: 89 */     this.overrides.putAll(other.overrides);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public URL getResource(String requestedPath)
/*  50:    */   {
/*  51: 95 */     if (this.overrides.containsKey(requestedPath))
/*  52:    */     {
/*  53: 96 */       String overriddenPath = (String)this.overrides.get(requestedPath);
/*  54: 97 */       return overriddenPath != null ? super.getResource(overriddenPath) : null;
/*  55:    */     }
/*  56:100 */     return super.getResource(requestedPath);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public InputStream getResourceAsStream(String requestedPath)
/*  60:    */   {
/*  61:106 */     if (this.overrides.containsKey(requestedPath))
/*  62:    */     {
/*  63:107 */       String overriddenPath = (String)this.overrides.get(requestedPath);
/*  64:108 */       return overriddenPath != null ? super.getResourceAsStream(overriddenPath) : null;
/*  65:    */     }
/*  66:111 */     return super.getResourceAsStream(requestedPath);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public Enumeration<URL> getResources(String requestedPath)
/*  70:    */     throws IOException
/*  71:    */   {
/*  72:117 */     if (this.overrides.containsKey(requestedPath))
/*  73:    */     {
/*  74:118 */       String overriddenLocation = (String)this.overrides.get(requestedPath);
/*  75:119 */       return overriddenLocation != null ? 
/*  76:120 */         super.getResources(overriddenLocation) : EMPTY_URL_ENUMERATION;
/*  77:    */     }
/*  78:123 */     return super.getResources(requestedPath);
/*  79:    */   }
/*  80:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.ResourceOverridingShadowingClassLoader
 * JD-Core Version:    0.7.0.1
 */