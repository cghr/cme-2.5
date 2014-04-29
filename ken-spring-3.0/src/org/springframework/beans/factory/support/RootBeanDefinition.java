/*   1:    */ package org.springframework.beans.factory.support;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Member;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.HashSet;
/*   7:    */ import java.util.Set;
/*   8:    */ import org.springframework.beans.MutablePropertyValues;
/*   9:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*  10:    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*  11:    */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*  12:    */ import org.springframework.util.Assert;
/*  13:    */ 
/*  14:    */ public class RootBeanDefinition
/*  15:    */   extends AbstractBeanDefinition
/*  16:    */ {
/*  17: 51 */   private final Set<Member> externallyManagedConfigMembers = Collections.synchronizedSet(new HashSet(0));
/*  18: 53 */   private final Set<String> externallyManagedInitMethods = Collections.synchronizedSet(new HashSet(0));
/*  19: 55 */   private final Set<String> externallyManagedDestroyMethods = Collections.synchronizedSet(new HashSet(0));
/*  20:    */   private BeanDefinitionHolder decoratedDefinition;
/*  21:    */   boolean isFactoryMethodUnique;
/*  22:    */   Object resolvedConstructorOrFactoryMethod;
/*  23: 65 */   boolean constructorArgumentsResolved = false;
/*  24:    */   Object[] resolvedConstructorArguments;
/*  25:    */   Object[] preparedConstructorArguments;
/*  26: 73 */   final Object constructorArgumentLock = new Object();
/*  27:    */   volatile Boolean beforeInstantiationResolved;
/*  28: 79 */   boolean postProcessed = false;
/*  29: 81 */   final Object postProcessingLock = new Object();
/*  30:    */   
/*  31:    */   public RootBeanDefinition() {}
/*  32:    */   
/*  33:    */   public RootBeanDefinition(Class beanClass)
/*  34:    */   {
/*  35:105 */     setBeanClass(beanClass);
/*  36:    */   }
/*  37:    */   
/*  38:    */   @Deprecated
/*  39:    */   public RootBeanDefinition(Class beanClass, boolean singleton)
/*  40:    */   {
/*  41:117 */     setBeanClass(beanClass);
/*  42:118 */     setSingleton(singleton);
/*  43:    */   }
/*  44:    */   
/*  45:    */   @Deprecated
/*  46:    */   public RootBeanDefinition(Class beanClass, int autowireMode)
/*  47:    */   {
/*  48:131 */     setBeanClass(beanClass);
/*  49:132 */     setAutowireMode(autowireMode);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public RootBeanDefinition(Class beanClass, int autowireMode, boolean dependencyCheck)
/*  53:    */   {
/*  54:145 */     setBeanClass(beanClass);
/*  55:146 */     setAutowireMode(autowireMode);
/*  56:147 */     if ((dependencyCheck) && (getResolvedAutowireMode() != 3)) {
/*  57:148 */       setDependencyCheck(1);
/*  58:    */     }
/*  59:    */   }
/*  60:    */   
/*  61:    */   @Deprecated
/*  62:    */   public RootBeanDefinition(Class beanClass, MutablePropertyValues pvs)
/*  63:    */   {
/*  64:161 */     super(null, pvs);
/*  65:162 */     setBeanClass(beanClass);
/*  66:    */   }
/*  67:    */   
/*  68:    */   @Deprecated
/*  69:    */   public RootBeanDefinition(Class beanClass, MutablePropertyValues pvs, boolean singleton)
/*  70:    */   {
/*  71:175 */     super(null, pvs);
/*  72:176 */     setBeanClass(beanClass);
/*  73:177 */     setSingleton(singleton);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public RootBeanDefinition(Class beanClass, ConstructorArgumentValues cargs, MutablePropertyValues pvs)
/*  77:    */   {
/*  78:188 */     super(cargs, pvs);
/*  79:189 */     setBeanClass(beanClass);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public RootBeanDefinition(String beanClassName)
/*  83:    */   {
/*  84:199 */     setBeanClassName(beanClassName);
/*  85:    */   }
/*  86:    */   
/*  87:    */   public RootBeanDefinition(String beanClassName, ConstructorArgumentValues cargs, MutablePropertyValues pvs)
/*  88:    */   {
/*  89:211 */     super(cargs, pvs);
/*  90:212 */     setBeanClassName(beanClassName);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public RootBeanDefinition(RootBeanDefinition original)
/*  94:    */   {
/*  95:221 */     this(original);
/*  96:    */   }
/*  97:    */   
/*  98:    */   RootBeanDefinition(BeanDefinition original)
/*  99:    */   {
/* 100:230 */     super(original);
/* 101:231 */     if ((original instanceof RootBeanDefinition))
/* 102:    */     {
/* 103:232 */       RootBeanDefinition originalRbd = (RootBeanDefinition)original;
/* 104:233 */       this.decoratedDefinition = originalRbd.decoratedDefinition;
/* 105:234 */       this.isFactoryMethodUnique = originalRbd.isFactoryMethodUnique;
/* 106:    */     }
/* 107:    */   }
/* 108:    */   
/* 109:    */   public String getParentName()
/* 110:    */   {
/* 111:240 */     return null;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void setParentName(String parentName)
/* 115:    */   {
/* 116:244 */     if (parentName != null) {
/* 117:245 */       throw new IllegalArgumentException("Root bean cannot be changed into a child bean with parent reference");
/* 118:    */     }
/* 119:    */   }
/* 120:    */   
/* 121:    */   public void setUniqueFactoryMethodName(String name)
/* 122:    */   {
/* 123:253 */     Assert.hasText(name, "Factory method name must not be empty");
/* 124:254 */     setFactoryMethodName(name);
/* 125:255 */     this.isFactoryMethodUnique = true;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public boolean isFactoryMethod(Method candidate)
/* 129:    */   {
/* 130:262 */     return (candidate != null) && (candidate.getName().equals(getFactoryMethodName()));
/* 131:    */   }
/* 132:    */   
/* 133:    */   public Method getResolvedFactoryMethod()
/* 134:    */   {
/* 135:270 */     synchronized (this.constructorArgumentLock)
/* 136:    */     {
/* 137:271 */       Object candidate = this.resolvedConstructorOrFactoryMethod;
/* 138:272 */       return (candidate instanceof Method) ? (Method)candidate : null;
/* 139:    */     }
/* 140:    */   }
/* 141:    */   
/* 142:    */   public void registerExternallyManagedConfigMember(Member configMember)
/* 143:    */   {
/* 144:278 */     this.externallyManagedConfigMembers.add(configMember);
/* 145:    */   }
/* 146:    */   
/* 147:    */   public boolean isExternallyManagedConfigMember(Member configMember)
/* 148:    */   {
/* 149:282 */     return this.externallyManagedConfigMembers.contains(configMember);
/* 150:    */   }
/* 151:    */   
/* 152:    */   public void registerExternallyManagedInitMethod(String initMethod)
/* 153:    */   {
/* 154:286 */     this.externallyManagedInitMethods.add(initMethod);
/* 155:    */   }
/* 156:    */   
/* 157:    */   public boolean isExternallyManagedInitMethod(String initMethod)
/* 158:    */   {
/* 159:290 */     return this.externallyManagedInitMethods.contains(initMethod);
/* 160:    */   }
/* 161:    */   
/* 162:    */   public void registerExternallyManagedDestroyMethod(String destroyMethod)
/* 163:    */   {
/* 164:294 */     this.externallyManagedDestroyMethods.add(destroyMethod);
/* 165:    */   }
/* 166:    */   
/* 167:    */   public boolean isExternallyManagedDestroyMethod(String destroyMethod)
/* 168:    */   {
/* 169:298 */     return this.externallyManagedDestroyMethods.contains(destroyMethod);
/* 170:    */   }
/* 171:    */   
/* 172:    */   public void setDecoratedDefinition(BeanDefinitionHolder decoratedDefinition)
/* 173:    */   {
/* 174:302 */     this.decoratedDefinition = decoratedDefinition;
/* 175:    */   }
/* 176:    */   
/* 177:    */   public BeanDefinitionHolder getDecoratedDefinition()
/* 178:    */   {
/* 179:306 */     return this.decoratedDefinition;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public RootBeanDefinition cloneBeanDefinition()
/* 183:    */   {
/* 184:312 */     return new RootBeanDefinition(this);
/* 185:    */   }
/* 186:    */   
/* 187:    */   public boolean equals(Object other)
/* 188:    */   {
/* 189:317 */     return (this == other) || (((other instanceof RootBeanDefinition)) && (super.equals(other)));
/* 190:    */   }
/* 191:    */   
/* 192:    */   public String toString()
/* 193:    */   {
/* 194:322 */     return "Root bean: " + super.toString();
/* 195:    */   }
/* 196:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.RootBeanDefinition
 * JD-Core Version:    0.7.0.1
 */