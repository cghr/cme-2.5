/*   1:    */ package org.springframework.beans.factory.config;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Field;
/*   4:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*   5:    */ import org.springframework.beans.factory.BeanFactoryUtils;
/*   6:    */ import org.springframework.beans.factory.BeanNameAware;
/*   7:    */ import org.springframework.beans.factory.FactoryBean;
/*   8:    */ import org.springframework.beans.factory.FactoryBeanNotInitializedException;
/*   9:    */ import org.springframework.beans.factory.InitializingBean;
/*  10:    */ import org.springframework.util.ClassUtils;
/*  11:    */ import org.springframework.util.ReflectionUtils;
/*  12:    */ import org.springframework.util.StringUtils;
/*  13:    */ 
/*  14:    */ public class FieldRetrievingFactoryBean
/*  15:    */   implements FactoryBean<Object>, BeanNameAware, BeanClassLoaderAware, InitializingBean
/*  16:    */ {
/*  17:    */   private Class targetClass;
/*  18:    */   private Object targetObject;
/*  19:    */   private String targetField;
/*  20:    */   private String staticField;
/*  21:    */   private String beanName;
/*  22: 68 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*  23:    */   private Field fieldObject;
/*  24:    */   
/*  25:    */   public void setTargetClass(Class targetClass)
/*  26:    */   {
/*  27: 82 */     this.targetClass = targetClass;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public Class getTargetClass()
/*  31:    */   {
/*  32: 89 */     return this.targetClass;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setTargetObject(Object targetObject)
/*  36:    */   {
/*  37:100 */     this.targetObject = targetObject;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public Object getTargetObject()
/*  41:    */   {
/*  42:107 */     return this.targetObject;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setTargetField(String targetField)
/*  46:    */   {
/*  47:118 */     this.targetField = StringUtils.trimAllWhitespace(targetField);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public String getTargetField()
/*  51:    */   {
/*  52:125 */     return this.targetField;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setStaticField(String staticField)
/*  56:    */   {
/*  57:136 */     this.staticField = StringUtils.trimAllWhitespace(staticField);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setBeanName(String beanName)
/*  61:    */   {
/*  62:146 */     this.beanName = StringUtils.trimAllWhitespace(BeanFactoryUtils.originalBeanName(beanName));
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void setBeanClassLoader(ClassLoader classLoader)
/*  66:    */   {
/*  67:150 */     this.beanClassLoader = classLoader;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void afterPropertiesSet()
/*  71:    */     throws ClassNotFoundException, NoSuchFieldException
/*  72:    */   {
/*  73:155 */     if ((this.targetClass != null) && (this.targetObject != null)) {
/*  74:156 */       throw new IllegalArgumentException("Specify either targetClass or targetObject, not both");
/*  75:    */     }
/*  76:159 */     if ((this.targetClass == null) && (this.targetObject == null))
/*  77:    */     {
/*  78:160 */       if (this.targetField != null) {
/*  79:161 */         throw new IllegalArgumentException(
/*  80:162 */           "Specify targetClass or targetObject in combination with targetField");
/*  81:    */       }
/*  82:166 */       if (this.staticField == null) {
/*  83:167 */         this.staticField = this.beanName;
/*  84:    */       }
/*  85:171 */       int lastDotIndex = this.staticField.lastIndexOf('.');
/*  86:172 */       if ((lastDotIndex == -1) || (lastDotIndex == this.staticField.length())) {
/*  87:173 */         throw new IllegalArgumentException(
/*  88:174 */           "staticField must be a fully qualified class plus method name: e.g. 'example.MyExampleClass.MY_EXAMPLE_FIELD'");
/*  89:    */       }
/*  90:177 */       String className = this.staticField.substring(0, lastDotIndex);
/*  91:178 */       String fieldName = this.staticField.substring(lastDotIndex + 1);
/*  92:179 */       this.targetClass = ClassUtils.forName(className, this.beanClassLoader);
/*  93:180 */       this.targetField = fieldName;
/*  94:    */     }
/*  95:183 */     else if (this.targetField == null)
/*  96:    */     {
/*  97:185 */       throw new IllegalArgumentException("targetField is required");
/*  98:    */     }
/*  99:189 */     Class targetClass = this.targetObject != null ? this.targetObject.getClass() : this.targetClass;
/* 100:190 */     this.fieldObject = targetClass.getField(this.targetField);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public Object getObject()
/* 104:    */     throws IllegalAccessException
/* 105:    */   {
/* 106:195 */     if (this.fieldObject == null) {
/* 107:196 */       throw new FactoryBeanNotInitializedException();
/* 108:    */     }
/* 109:198 */     ReflectionUtils.makeAccessible(this.fieldObject);
/* 110:199 */     if (this.targetObject != null) {
/* 111:201 */       return this.fieldObject.get(this.targetObject);
/* 112:    */     }
/* 113:205 */     return this.fieldObject.get(null);
/* 114:    */   }
/* 115:    */   
/* 116:    */   public Class<?> getObjectType()
/* 117:    */   {
/* 118:210 */     return this.fieldObject != null ? this.fieldObject.getType() : null;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public boolean isSingleton()
/* 122:    */   {
/* 123:214 */     return false;
/* 124:    */   }
/* 125:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.FieldRetrievingFactoryBean
 * JD-Core Version:    0.7.0.1
 */