/*   1:    */ package org.springframework.web.servlet.mvc.support;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.HashSet;
/*   7:    */ import java.util.Set;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.springframework.context.ApplicationContext;
/*  10:    */ import org.springframework.web.servlet.handler.AbstractDetectingUrlHandlerMapping;
/*  11:    */ 
/*  12:    */ public abstract class AbstractControllerUrlHandlerMapping
/*  13:    */   extends AbstractDetectingUrlHandlerMapping
/*  14:    */ {
/*  15: 37 */   private ControllerTypePredicate predicate = new AnnotationControllerTypePredicate();
/*  16: 39 */   private Set<String> excludedPackages = Collections.singleton("org.springframework.web.servlet.mvc");
/*  17: 41 */   private Set<Class> excludedClasses = Collections.emptySet();
/*  18:    */   
/*  19:    */   public void setIncludeAnnotatedControllers(boolean includeAnnotatedControllers)
/*  20:    */   {
/*  21: 48 */     this.predicate = (includeAnnotatedControllers ? 
/*  22: 49 */       new AnnotationControllerTypePredicate() : new ControllerTypePredicate());
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void setExcludedPackages(String[] excludedPackages)
/*  26:    */   {
/*  27: 64 */     this.excludedPackages = (excludedPackages != null ? 
/*  28: 65 */       new HashSet((Collection)Arrays.asList(excludedPackages)) : new HashSet());
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setExcludedClasses(Class[] excludedClasses)
/*  32:    */   {
/*  33: 73 */     this.excludedClasses = (excludedClasses != null ? 
/*  34: 74 */       new HashSet((Collection)Arrays.asList(excludedClasses)) : new HashSet());
/*  35:    */   }
/*  36:    */   
/*  37:    */   protected String[] determineUrlsForHandler(String beanName)
/*  38:    */   {
/*  39: 84 */     Class beanClass = getApplicationContext().getType(beanName);
/*  40: 85 */     if (isEligibleForMapping(beanName, beanClass)) {
/*  41: 86 */       return buildUrlsForHandler(beanName, beanClass);
/*  42:    */     }
/*  43: 89 */     return null;
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected boolean isEligibleForMapping(String beanName, Class beanClass)
/*  47:    */   {
/*  48:102 */     if (beanClass == null)
/*  49:    */     {
/*  50:103 */       if (this.logger.isDebugEnabled()) {
/*  51:104 */         this.logger.debug("Excluding controller bean '" + beanName + "' from class name mapping " + 
/*  52:105 */           "because its bean type could not be determined");
/*  53:    */       }
/*  54:107 */       return false;
/*  55:    */     }
/*  56:109 */     if (this.excludedClasses.contains(beanClass))
/*  57:    */     {
/*  58:110 */       if (this.logger.isDebugEnabled()) {
/*  59:111 */         this.logger.debug("Excluding controller bean '" + beanName + "' from class name mapping " + 
/*  60:112 */           "because its bean class is explicitly excluded: " + beanClass.getName());
/*  61:    */       }
/*  62:114 */       return false;
/*  63:    */     }
/*  64:116 */     String beanClassName = beanClass.getName();
/*  65:117 */     for (String packageName : this.excludedPackages) {
/*  66:118 */       if (beanClassName.startsWith(packageName))
/*  67:    */       {
/*  68:119 */         if (this.logger.isDebugEnabled()) {
/*  69:120 */           this.logger.debug("Excluding controller bean '" + beanName + "' from class name mapping " + 
/*  70:121 */             "because its bean class is defined in an excluded package: " + beanClass.getName());
/*  71:    */         }
/*  72:123 */         return false;
/*  73:    */       }
/*  74:    */     }
/*  75:126 */     return isControllerType(beanClass);
/*  76:    */   }
/*  77:    */   
/*  78:    */   protected boolean isControllerType(Class beanClass)
/*  79:    */   {
/*  80:135 */     return this.predicate.isControllerType(beanClass);
/*  81:    */   }
/*  82:    */   
/*  83:    */   protected boolean isMultiActionControllerType(Class beanClass)
/*  84:    */   {
/*  85:144 */     return this.predicate.isMultiActionControllerType(beanClass);
/*  86:    */   }
/*  87:    */   
/*  88:    */   protected abstract String[] buildUrlsForHandler(String paramString, Class paramClass);
/*  89:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.support.AbstractControllerUrlHandlerMapping
 * JD-Core Version:    0.7.0.1
 */