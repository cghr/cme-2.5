/*   1:    */ package org.springframework.beans.factory.support;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.MutablePropertyValues;
/*   4:    */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*   5:    */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*   6:    */ import org.springframework.util.ObjectUtils;
/*   7:    */ 
/*   8:    */ public class BeanDefinitionBuilder
/*   9:    */ {
/*  10:    */   private AbstractBeanDefinition beanDefinition;
/*  11:    */   private int constructorArgIndex;
/*  12:    */   
/*  13:    */   public static BeanDefinitionBuilder genericBeanDefinition()
/*  14:    */   {
/*  15: 39 */     BeanDefinitionBuilder builder = new BeanDefinitionBuilder();
/*  16: 40 */     builder.beanDefinition = new GenericBeanDefinition();
/*  17: 41 */     return builder;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public static BeanDefinitionBuilder genericBeanDefinition(Class beanClass)
/*  21:    */   {
/*  22: 49 */     BeanDefinitionBuilder builder = new BeanDefinitionBuilder();
/*  23: 50 */     builder.beanDefinition = new GenericBeanDefinition();
/*  24: 51 */     builder.beanDefinition.setBeanClass(beanClass);
/*  25: 52 */     return builder;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public static BeanDefinitionBuilder genericBeanDefinition(String beanClassName)
/*  29:    */   {
/*  30: 60 */     BeanDefinitionBuilder builder = new BeanDefinitionBuilder();
/*  31: 61 */     builder.beanDefinition = new GenericBeanDefinition();
/*  32: 62 */     builder.beanDefinition.setBeanClassName(beanClassName);
/*  33: 63 */     return builder;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public static BeanDefinitionBuilder rootBeanDefinition(Class beanClass)
/*  37:    */   {
/*  38: 71 */     return rootBeanDefinition(beanClass, null);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public static BeanDefinitionBuilder rootBeanDefinition(Class beanClass, String factoryMethodName)
/*  42:    */   {
/*  43: 80 */     BeanDefinitionBuilder builder = new BeanDefinitionBuilder();
/*  44: 81 */     builder.beanDefinition = new RootBeanDefinition();
/*  45: 82 */     builder.beanDefinition.setBeanClass(beanClass);
/*  46: 83 */     builder.beanDefinition.setFactoryMethodName(factoryMethodName);
/*  47: 84 */     return builder;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public static BeanDefinitionBuilder rootBeanDefinition(String beanClassName)
/*  51:    */   {
/*  52: 92 */     return rootBeanDefinition(beanClassName, null);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public static BeanDefinitionBuilder rootBeanDefinition(String beanClassName, String factoryMethodName)
/*  56:    */   {
/*  57:101 */     BeanDefinitionBuilder builder = new BeanDefinitionBuilder();
/*  58:102 */     builder.beanDefinition = new RootBeanDefinition();
/*  59:103 */     builder.beanDefinition.setBeanClassName(beanClassName);
/*  60:104 */     builder.beanDefinition.setFactoryMethodName(factoryMethodName);
/*  61:105 */     return builder;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public static BeanDefinitionBuilder childBeanDefinition(String parentName)
/*  65:    */   {
/*  66:113 */     BeanDefinitionBuilder builder = new BeanDefinitionBuilder();
/*  67:114 */     builder.beanDefinition = new ChildBeanDefinition(parentName);
/*  68:115 */     return builder;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public AbstractBeanDefinition getRawBeanDefinition()
/*  72:    */   {
/*  73:141 */     return this.beanDefinition;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public AbstractBeanDefinition getBeanDefinition()
/*  77:    */   {
/*  78:148 */     this.beanDefinition.validate();
/*  79:149 */     return this.beanDefinition;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public BeanDefinitionBuilder setParentName(String parentName)
/*  83:    */   {
/*  84:157 */     this.beanDefinition.setParentName(parentName);
/*  85:158 */     return this;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public BeanDefinitionBuilder setFactoryMethod(String factoryMethod)
/*  89:    */   {
/*  90:165 */     this.beanDefinition.setFactoryMethodName(factoryMethod);
/*  91:166 */     return this;
/*  92:    */   }
/*  93:    */   
/*  94:    */   @Deprecated
/*  95:    */   public BeanDefinitionBuilder setFactoryBean(String factoryBean, String factoryMethod)
/*  96:    */   {
/*  97:176 */     this.beanDefinition.setFactoryBeanName(factoryBean);
/*  98:177 */     this.beanDefinition.setFactoryMethodName(factoryMethod);
/*  99:178 */     return this;
/* 100:    */   }
/* 101:    */   
/* 102:    */   @Deprecated
/* 103:    */   public BeanDefinitionBuilder addConstructorArg(Object value)
/* 104:    */   {
/* 105:188 */     return addConstructorArgValue(value);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public BeanDefinitionBuilder addConstructorArgValue(Object value)
/* 109:    */   {
/* 110:196 */     this.beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(
/* 111:197 */       this.constructorArgIndex++, value);
/* 112:198 */     return this;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public BeanDefinitionBuilder addConstructorArgReference(String beanName)
/* 116:    */   {
/* 117:206 */     this.beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(
/* 118:207 */       this.constructorArgIndex++, new RuntimeBeanReference(beanName));
/* 119:208 */     return this;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public BeanDefinitionBuilder addPropertyValue(String name, Object value)
/* 123:    */   {
/* 124:215 */     this.beanDefinition.getPropertyValues().add(name, value);
/* 125:216 */     return this;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public BeanDefinitionBuilder addPropertyReference(String name, String beanName)
/* 129:    */   {
/* 130:225 */     this.beanDefinition.getPropertyValues().add(name, new RuntimeBeanReference(beanName));
/* 131:226 */     return this;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public BeanDefinitionBuilder setInitMethodName(String methodName)
/* 135:    */   {
/* 136:233 */     this.beanDefinition.setInitMethodName(methodName);
/* 137:234 */     return this;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public BeanDefinitionBuilder setDestroyMethodName(String methodName)
/* 141:    */   {
/* 142:241 */     this.beanDefinition.setDestroyMethodName(methodName);
/* 143:242 */     return this;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public BeanDefinitionBuilder setScope(String scope)
/* 147:    */   {
/* 148:252 */     this.beanDefinition.setScope(scope);
/* 149:253 */     return this;
/* 150:    */   }
/* 151:    */   
/* 152:    */   @Deprecated
/* 153:    */   public BeanDefinitionBuilder setSingleton(boolean singleton)
/* 154:    */   {
/* 155:263 */     this.beanDefinition.setSingleton(singleton);
/* 156:264 */     return this;
/* 157:    */   }
/* 158:    */   
/* 159:    */   public BeanDefinitionBuilder setAbstract(boolean flag)
/* 160:    */   {
/* 161:271 */     this.beanDefinition.setAbstract(flag);
/* 162:272 */     return this;
/* 163:    */   }
/* 164:    */   
/* 165:    */   public BeanDefinitionBuilder setLazyInit(boolean lazy)
/* 166:    */   {
/* 167:279 */     this.beanDefinition.setLazyInit(lazy);
/* 168:280 */     return this;
/* 169:    */   }
/* 170:    */   
/* 171:    */   public BeanDefinitionBuilder setAutowireMode(int autowireMode)
/* 172:    */   {
/* 173:287 */     this.beanDefinition.setAutowireMode(autowireMode);
/* 174:288 */     return this;
/* 175:    */   }
/* 176:    */   
/* 177:    */   public BeanDefinitionBuilder setDependencyCheck(int dependencyCheck)
/* 178:    */   {
/* 179:295 */     this.beanDefinition.setDependencyCheck(dependencyCheck);
/* 180:296 */     return this;
/* 181:    */   }
/* 182:    */   
/* 183:    */   public BeanDefinitionBuilder addDependsOn(String beanName)
/* 184:    */   {
/* 185:304 */     if (this.beanDefinition.getDependsOn() == null)
/* 186:    */     {
/* 187:305 */       this.beanDefinition.setDependsOn(new String[] { beanName });
/* 188:    */     }
/* 189:    */     else
/* 190:    */     {
/* 191:308 */       String[] added = (String[])ObjectUtils.addObjectToArray(this.beanDefinition.getDependsOn(), beanName);
/* 192:309 */       this.beanDefinition.setDependsOn(added);
/* 193:    */     }
/* 194:311 */     return this;
/* 195:    */   }
/* 196:    */   
/* 197:    */   public BeanDefinitionBuilder setRole(int role)
/* 198:    */   {
/* 199:318 */     this.beanDefinition.setRole(role);
/* 200:319 */     return this;
/* 201:    */   }
/* 202:    */   
/* 203:    */   @Deprecated
/* 204:    */   public BeanDefinitionBuilder setSource(Object source)
/* 205:    */   {
/* 206:329 */     this.beanDefinition.setSource(source);
/* 207:330 */     return this;
/* 208:    */   }
/* 209:    */   
/* 210:    */   @Deprecated
/* 211:    */   public BeanDefinitionBuilder setResourceDescription(String resourceDescription)
/* 212:    */   {
/* 213:340 */     this.beanDefinition.setResourceDescription(resourceDescription);
/* 214:341 */     return this;
/* 215:    */   }
/* 216:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.BeanDefinitionBuilder
 * JD-Core Version:    0.7.0.1
 */