/*   1:    */ package org.springframework.context.annotation;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.LinkedHashMap;
/*   5:    */ import java.util.LinkedHashSet;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.Set;
/*   8:    */ import org.springframework.beans.factory.parsing.Location;
/*   9:    */ import org.springframework.beans.factory.parsing.Problem;
/*  10:    */ import org.springframework.beans.factory.parsing.ProblemReporter;
/*  11:    */ import org.springframework.core.io.DescriptiveResource;
/*  12:    */ import org.springframework.core.io.Resource;
/*  13:    */ import org.springframework.core.type.AnnotationMetadata;
/*  14:    */ import org.springframework.core.type.MethodMetadata;
/*  15:    */ import org.springframework.core.type.StandardAnnotationMetadata;
/*  16:    */ import org.springframework.core.type.classreading.MetadataReader;
/*  17:    */ import org.springframework.util.ClassUtils;
/*  18:    */ 
/*  19:    */ final class ConfigurationClass
/*  20:    */ {
/*  21:    */   private final AnnotationMetadata metadata;
/*  22:    */   private final Resource resource;
/*  23: 52 */   private final Map<String, Class<?>> importedResources = new LinkedHashMap();
/*  24: 54 */   private final Set<BeanMethod> beanMethods = new LinkedHashSet();
/*  25:    */   private String beanName;
/*  26:    */   
/*  27:    */   public ConfigurationClass(MetadataReader metadataReader, String beanName)
/*  28:    */   {
/*  29: 60 */     this.metadata = metadataReader.getAnnotationMetadata();
/*  30: 61 */     this.resource = metadataReader.getResource();
/*  31: 62 */     this.beanName = beanName;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public ConfigurationClass(Class<?> clazz, String beanName)
/*  35:    */   {
/*  36: 66 */     this.metadata = new StandardAnnotationMetadata(clazz);
/*  37: 67 */     this.resource = new DescriptiveResource(clazz.toString());
/*  38: 68 */     this.beanName = beanName;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public AnnotationMetadata getMetadata()
/*  42:    */   {
/*  43: 73 */     return this.metadata;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public Resource getResource()
/*  47:    */   {
/*  48: 77 */     return this.resource;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public String getSimpleName()
/*  52:    */   {
/*  53: 81 */     return ClassUtils.getShortName(getMetadata().getClassName());
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setBeanName(String beanName)
/*  57:    */   {
/*  58: 85 */     this.beanName = beanName;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public String getBeanName()
/*  62:    */   {
/*  63: 89 */     return this.beanName;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void addBeanMethod(BeanMethod method)
/*  67:    */   {
/*  68: 93 */     this.beanMethods.add(method);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public Set<BeanMethod> getBeanMethods()
/*  72:    */   {
/*  73: 97 */     return this.beanMethods;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void addImportedResource(String importedResource, Class<?> readerClass)
/*  77:    */   {
/*  78:101 */     this.importedResources.put(importedResource, readerClass);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public Map<String, Class<?>> getImportedResources()
/*  82:    */   {
/*  83:105 */     return this.importedResources;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void validate(ProblemReporter problemReporter)
/*  87:    */   {
/*  88:112 */     Map<String, Integer> methodNameCounts = new HashMap();
/*  89:113 */     for (BeanMethod beanMethod : this.beanMethods)
/*  90:    */     {
/*  91:114 */       String dClassName = beanMethod.getMetadata().getDeclaringClassName();
/*  92:115 */       String methodName = beanMethod.getMetadata().getMethodName();
/*  93:116 */       String fqMethodName = dClassName + '#' + methodName;
/*  94:117 */       Integer currentCount = (Integer)methodNameCounts.get(fqMethodName);
/*  95:118 */       int newCount = currentCount != null ? currentCount.intValue() + 1 : 1;
/*  96:119 */       methodNameCounts.put(fqMethodName, Integer.valueOf(newCount));
/*  97:    */     }
/*  98:122 */     for (String methodName : methodNameCounts.keySet())
/*  99:    */     {
/* 100:123 */       int count = ((Integer)methodNameCounts.get(methodName)).intValue();
/* 101:124 */       if (count > 1)
/* 102:    */       {
/* 103:125 */         String shortMethodName = methodName.substring(methodName.indexOf('#') + 1);
/* 104:126 */         problemReporter.error(new BeanMethodOverloadingProblem(shortMethodName, count));
/* 105:    */       }
/* 106:    */     }
/* 107:131 */     if ((getMetadata().isAnnotated(Configuration.class.getName())) && 
/* 108:132 */       (getMetadata().isFinal())) {
/* 109:133 */       problemReporter.error(new FinalConfigurationProblem());
/* 110:    */     }
/* 111:137 */     for (BeanMethod beanMethod : this.beanMethods) {
/* 112:138 */       beanMethod.validate(problemReporter);
/* 113:    */     }
/* 114:    */   }
/* 115:    */   
/* 116:    */   public boolean equals(Object other)
/* 117:    */   {
/* 118:146 */     return (this == other) || (((other instanceof ConfigurationClass)) && (getMetadata().getClassName().equals(((ConfigurationClass)other).getMetadata().getClassName())));
/* 119:    */   }
/* 120:    */   
/* 121:    */   public int hashCode()
/* 122:    */   {
/* 123:151 */     return getMetadata().getClassName().hashCode();
/* 124:    */   }
/* 125:    */   
/* 126:    */   private class FinalConfigurationProblem
/* 127:    */     extends Problem
/* 128:    */   {
/* 129:    */     public FinalConfigurationProblem()
/* 130:    */     {
/* 131:162 */       super(new Location(ConfigurationClass.this.getResource(), ConfigurationClass.this.getMetadata()));
/* 132:    */     }
/* 133:    */   }
/* 134:    */   
/* 135:    */   private class BeanMethodOverloadingProblem
/* 136:    */     extends Problem
/* 137:    */   {
/* 138:    */     public BeanMethodOverloadingProblem(String methodName, int count)
/* 139:    */     {
/* 140:175 */       super(new Location(ConfigurationClass.this.getResource(), ConfigurationClass.this.getMetadata()));
/* 141:    */     }
/* 142:    */   }
/* 143:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.ConfigurationClass
 * JD-Core Version:    0.7.0.1
 */