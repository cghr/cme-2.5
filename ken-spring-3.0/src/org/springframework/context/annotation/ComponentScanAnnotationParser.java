/*   1:    */ package org.springframework.context.annotation;
/*   2:    */ 
/*   3:    */ import java.lang.annotation.Annotation;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.Set;
/*   8:    */ import org.springframework.beans.BeanUtils;
/*   9:    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*  10:    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  11:    */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*  12:    */ import org.springframework.core.env.Environment;
/*  13:    */ import org.springframework.core.io.ResourceLoader;
/*  14:    */ import org.springframework.core.type.filter.AnnotationTypeFilter;
/*  15:    */ import org.springframework.core.type.filter.AssignableTypeFilter;
/*  16:    */ import org.springframework.core.type.filter.TypeFilter;
/*  17:    */ import org.springframework.util.Assert;
/*  18:    */ import org.springframework.util.StringUtils;
/*  19:    */ 
/*  20:    */ class ComponentScanAnnotationParser
/*  21:    */ {
/*  22:    */   private final ResourceLoader resourceLoader;
/*  23:    */   private final Environment environment;
/*  24:    */   private final BeanDefinitionRegistry registry;
/*  25:    */   
/*  26:    */   public ComponentScanAnnotationParser(ResourceLoader resourceLoader, Environment environment, BeanDefinitionRegistry registry)
/*  27:    */   {
/*  28: 53 */     this.resourceLoader = resourceLoader;
/*  29: 54 */     this.environment = environment;
/*  30: 55 */     this.registry = registry;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public Set<BeanDefinitionHolder> parse(Map<String, Object> componentScanAttributes)
/*  34:    */   {
/*  35: 59 */     ClassPathBeanDefinitionScanner scanner = 
/*  36: 60 */       new ClassPathBeanDefinitionScanner(this.registry, ((Boolean)componentScanAttributes.get("useDefaultFilters")).booleanValue());
/*  37:    */     
/*  38: 62 */     Assert.notNull(this.environment, "Environment must not be null");
/*  39: 63 */     scanner.setEnvironment(this.environment);
/*  40:    */     
/*  41: 65 */     Assert.notNull(this.resourceLoader, "ResourceLoader must not be null");
/*  42: 66 */     scanner.setResourceLoader(this.resourceLoader);
/*  43:    */     
/*  44: 68 */     scanner.setBeanNameGenerator((BeanNameGenerator)BeanUtils.instantiateClass(
/*  45: 69 */       (Class)componentScanAttributes.get("nameGenerator"), BeanNameGenerator.class));
/*  46:    */     
/*  47: 71 */     ScopedProxyMode scopedProxyMode = (ScopedProxyMode)componentScanAttributes.get("scopedProxy");
/*  48: 72 */     if (scopedProxyMode != ScopedProxyMode.DEFAULT) {
/*  49: 73 */       scanner.setScopedProxyMode(scopedProxyMode);
/*  50:    */     } else {
/*  51: 75 */       scanner.setScopeMetadataResolver((ScopeMetadataResolver)BeanUtils.instantiateClass(
/*  52: 76 */         (Class)componentScanAttributes.get("scopeResolver"), ScopeMetadataResolver.class));
/*  53:    */     }
/*  54: 79 */     scanner.setResourcePattern((String)componentScanAttributes.get("resourcePattern"));
/*  55: 81 */     for (ComponentScan.Filter filter : (ComponentScan.Filter[])componentScanAttributes.get("includeFilters")) {
/*  56: 82 */       scanner.addIncludeFilter(createTypeFilter(filter));
/*  57:    */     }
/*  58: 84 */     for (ComponentScan.Filter filter : (ComponentScan.Filter[])componentScanAttributes.get("excludeFilters")) {
/*  59: 85 */       scanner.addExcludeFilter(createTypeFilter(filter));
/*  60:    */     }
/*  61: 88 */     List<String> basePackages = new ArrayList();
/*  62: 89 */     for (String pkg : (String[])componentScanAttributes.get("value")) {
/*  63: 90 */       if (StringUtils.hasText(pkg)) {
/*  64: 91 */         basePackages.add(pkg);
/*  65:    */       }
/*  66:    */     }
/*  67: 94 */     for (String pkg : (String[])componentScanAttributes.get("basePackages")) {
/*  68: 95 */       if (StringUtils.hasText(pkg)) {
/*  69: 96 */         basePackages.add(pkg);
/*  70:    */       }
/*  71:    */     }
/*  72: 99 */     for (Class<?> clazz : (Class[])componentScanAttributes.get("basePackageClasses")) {
/*  73:103 */       basePackages.add(clazz.getPackage().getName());
/*  74:    */     }
/*  75:106 */     if (basePackages.isEmpty()) {
/*  76:107 */       throw new IllegalStateException("At least one base package must be specified");
/*  77:    */     }
/*  78:110 */     return scanner.doScan((String[])basePackages.toArray(new String[0]));
/*  79:    */   }
/*  80:    */   
/*  81:    */   private TypeFilter createTypeFilter(ComponentScan.Filter filter)
/*  82:    */   {
/*  83:114 */     switch (filter.type())
/*  84:    */     {
/*  85:    */     case ANNOTATION: 
/*  86:117 */       Class<Annotation> filterClass = filter.value();
/*  87:118 */       return new AnnotationTypeFilter(filterClass);
/*  88:    */     case ASSIGNABLE_TYPE: 
/*  89:120 */       return new AssignableTypeFilter(filter.value());
/*  90:    */     case CUSTOM: 
/*  91:122 */       return (TypeFilter)BeanUtils.instantiateClass(filter.value(), TypeFilter.class);
/*  92:    */     }
/*  93:124 */     throw new IllegalArgumentException("unknown filter type " + filter.type());
/*  94:    */   }
/*  95:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.ComponentScanAnnotationParser
 * JD-Core Version:    0.7.0.1
 */