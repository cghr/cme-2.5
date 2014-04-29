/*   1:    */ package org.springframework.web.servlet.mvc.support;
/*   2:    */ 
/*   3:    */ import org.springframework.util.ClassUtils;
/*   4:    */ import org.springframework.util.StringUtils;
/*   5:    */ 
/*   6:    */ public class ControllerClassNameHandlerMapping
/*   7:    */   extends AbstractControllerUrlHandlerMapping
/*   8:    */ {
/*   9:    */   private static final String CONTROLLER_SUFFIX = "Controller";
/*  10: 69 */   private boolean caseSensitive = false;
/*  11:    */   private String pathPrefix;
/*  12:    */   private String basePackage;
/*  13:    */   
/*  14:    */   public void setCaseSensitive(boolean caseSensitive)
/*  15:    */   {
/*  16: 83 */     this.caseSensitive = caseSensitive;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public void setPathPrefix(String prefixPath)
/*  20:    */   {
/*  21: 93 */     this.pathPrefix = prefixPath;
/*  22: 94 */     if (StringUtils.hasLength(this.pathPrefix))
/*  23:    */     {
/*  24: 95 */       if (!this.pathPrefix.startsWith("/")) {
/*  25: 96 */         this.pathPrefix = ("/" + this.pathPrefix);
/*  26:    */       }
/*  27: 98 */       if (this.pathPrefix.endsWith("/")) {
/*  28: 99 */         this.pathPrefix = this.pathPrefix.substring(0, this.pathPrefix.length() - 1);
/*  29:    */       }
/*  30:    */     }
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setBasePackage(String basePackage)
/*  34:    */   {
/*  35:117 */     this.basePackage = basePackage;
/*  36:118 */     if ((StringUtils.hasLength(this.basePackage)) && (!this.basePackage.endsWith("."))) {
/*  37:119 */       this.basePackage += ".";
/*  38:    */     }
/*  39:    */   }
/*  40:    */   
/*  41:    */   protected String[] buildUrlsForHandler(String beanName, Class beanClass)
/*  42:    */   {
/*  43:126 */     return generatePathMappings(beanClass);
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected String[] generatePathMappings(Class beanClass)
/*  47:    */   {
/*  48:137 */     StringBuilder pathMapping = buildPathPrefix(beanClass);
/*  49:138 */     String className = ClassUtils.getShortName(beanClass);
/*  50:139 */     String path = className.endsWith("Controller") ? 
/*  51:140 */       className.substring(0, className.lastIndexOf("Controller")) : className;
/*  52:141 */     if (path.length() > 0) {
/*  53:142 */       if (this.caseSensitive) {
/*  54:143 */         pathMapping.append(path.substring(0, 1).toLowerCase()).append(path.substring(1));
/*  55:    */       } else {
/*  56:146 */         pathMapping.append(path.toLowerCase());
/*  57:    */       }
/*  58:    */     }
/*  59:149 */     if (isMultiActionControllerType(beanClass)) {
/*  60:150 */       return new String[] { pathMapping.toString(), pathMapping.toString() + "/*" };
/*  61:    */     }
/*  62:153 */     return new String[] { pathMapping.toString() + "*" };
/*  63:    */   }
/*  64:    */   
/*  65:    */   private StringBuilder buildPathPrefix(Class beanClass)
/*  66:    */   {
/*  67:163 */     StringBuilder pathMapping = new StringBuilder();
/*  68:164 */     if (this.pathPrefix != null)
/*  69:    */     {
/*  70:165 */       pathMapping.append(this.pathPrefix);
/*  71:166 */       pathMapping.append("/");
/*  72:    */     }
/*  73:    */     else
/*  74:    */     {
/*  75:169 */       pathMapping.append("/");
/*  76:    */     }
/*  77:171 */     if (this.basePackage != null)
/*  78:    */     {
/*  79:172 */       String packageName = ClassUtils.getPackageName(beanClass);
/*  80:173 */       if (packageName.startsWith(this.basePackage))
/*  81:    */       {
/*  82:174 */         String subPackage = packageName.substring(this.basePackage.length()).replace('.', '/');
/*  83:175 */         pathMapping.append(this.caseSensitive ? subPackage : subPackage.toLowerCase());
/*  84:176 */         pathMapping.append("/");
/*  85:    */       }
/*  86:    */     }
/*  87:179 */     return pathMapping;
/*  88:    */   }
/*  89:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping
 * JD-Core Version:    0.7.0.1
 */