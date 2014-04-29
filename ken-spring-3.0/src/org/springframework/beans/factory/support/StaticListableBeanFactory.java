/*   1:    */ package org.springframework.beans.factory.support;
/*   2:    */ 
/*   3:    */ import java.lang.annotation.Annotation;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Collection;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.LinkedHashMap;
/*   8:    */ import java.util.List;
/*   9:    */ import java.util.Map;
/*  10:    */ import java.util.Map.Entry;
/*  11:    */ import org.springframework.beans.BeansException;
/*  12:    */ import org.springframework.beans.factory.BeanCreationException;
/*  13:    */ import org.springframework.beans.factory.BeanFactoryUtils;
/*  14:    */ import org.springframework.beans.factory.BeanIsNotAFactoryException;
/*  15:    */ import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
/*  16:    */ import org.springframework.beans.factory.FactoryBean;
/*  17:    */ import org.springframework.beans.factory.ListableBeanFactory;
/*  18:    */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*  19:    */ import org.springframework.beans.factory.SmartFactoryBean;
/*  20:    */ import org.springframework.core.annotation.AnnotationUtils;
/*  21:    */ import org.springframework.util.StringUtils;
/*  22:    */ 
/*  23:    */ public class StaticListableBeanFactory
/*  24:    */   implements ListableBeanFactory
/*  25:    */ {
/*  26: 60 */   private final Map<String, Object> beans = new HashMap();
/*  27:    */   
/*  28:    */   public void addBean(String name, Object bean)
/*  29:    */   {
/*  30: 70 */     this.beans.put(name, bean);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public Object getBean(String name)
/*  34:    */     throws BeansException
/*  35:    */   {
/*  36: 79 */     String beanName = BeanFactoryUtils.transformedBeanName(name);
/*  37: 80 */     Object bean = this.beans.get(beanName);
/*  38: 82 */     if (bean == null) {
/*  39: 83 */       throw new NoSuchBeanDefinitionException(beanName, 
/*  40: 84 */         "Defined beans are [" + StringUtils.collectionToCommaDelimitedString((Collection)this.beans.keySet()) + "]");
/*  41:    */     }
/*  42: 89 */     if ((BeanFactoryUtils.isFactoryDereference(name)) && (!(bean instanceof FactoryBean))) {
/*  43: 90 */       throw new BeanIsNotAFactoryException(beanName, bean.getClass());
/*  44:    */     }
/*  45: 93 */     if (((bean instanceof FactoryBean)) && (!BeanFactoryUtils.isFactoryDereference(name))) {
/*  46:    */       try
/*  47:    */       {
/*  48: 95 */         return ((FactoryBean)bean).getObject();
/*  49:    */       }
/*  50:    */       catch (Exception ex)
/*  51:    */       {
/*  52: 98 */         throw new BeanCreationException(beanName, "FactoryBean threw exception on object creation", ex);
/*  53:    */       }
/*  54:    */     }
/*  55:102 */     return bean;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public <T> T getBean(String name, Class<T> requiredType)
/*  59:    */     throws BeansException
/*  60:    */   {
/*  61:108 */     Object bean = getBean(name);
/*  62:109 */     if ((requiredType != null) && (!requiredType.isAssignableFrom(bean.getClass()))) {
/*  63:110 */       throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
/*  64:    */     }
/*  65:112 */     return bean;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public <T> T getBean(Class<T> requiredType)
/*  69:    */     throws BeansException
/*  70:    */   {
/*  71:116 */     String[] beanNames = getBeanNamesForType(requiredType);
/*  72:117 */     if (beanNames.length == 1) {
/*  73:118 */       return getBean(beanNames[0], requiredType);
/*  74:    */     }
/*  75:121 */     throw new NoSuchBeanDefinitionException(requiredType, "expected single bean but found " + beanNames.length);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public Object getBean(String name, Object... args)
/*  79:    */     throws BeansException
/*  80:    */   {
/*  81:126 */     if (args != null) {
/*  82:127 */       throw new UnsupportedOperationException(
/*  83:128 */         "StaticListableBeanFactory does not support explicit bean creation arguments)");
/*  84:    */     }
/*  85:130 */     return getBean(name);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public boolean containsBean(String name)
/*  89:    */   {
/*  90:134 */     return this.beans.containsKey(name);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public boolean isSingleton(String name)
/*  94:    */     throws NoSuchBeanDefinitionException
/*  95:    */   {
/*  96:138 */     Object bean = getBean(name);
/*  97:    */     
/*  98:140 */     return ((bean instanceof FactoryBean)) && (((FactoryBean)bean).isSingleton());
/*  99:    */   }
/* 100:    */   
/* 101:    */   public boolean isPrototype(String name)
/* 102:    */     throws NoSuchBeanDefinitionException
/* 103:    */   {
/* 104:144 */     Object bean = getBean(name);
/* 105:    */     
/* 106:    */ 
/* 107:147 */     return (((bean instanceof SmartFactoryBean)) && (((SmartFactoryBean)bean).isPrototype())) || (((bean instanceof FactoryBean)) && (!((FactoryBean)bean).isSingleton()));
/* 108:    */   }
/* 109:    */   
/* 110:    */   public boolean isTypeMatch(String name, Class targetType)
/* 111:    */     throws NoSuchBeanDefinitionException
/* 112:    */   {
/* 113:151 */     Class type = getType(name);
/* 114:152 */     return (targetType == null) || ((type != null) && (targetType.isAssignableFrom(type)));
/* 115:    */   }
/* 116:    */   
/* 117:    */   public Class<?> getType(String name)
/* 118:    */     throws NoSuchBeanDefinitionException
/* 119:    */   {
/* 120:156 */     String beanName = BeanFactoryUtils.transformedBeanName(name);
/* 121:    */     
/* 122:158 */     Object bean = this.beans.get(beanName);
/* 123:159 */     if (bean == null) {
/* 124:160 */       throw new NoSuchBeanDefinitionException(beanName, 
/* 125:161 */         "Defined beans are [" + StringUtils.collectionToCommaDelimitedString((Collection)this.beans.keySet()) + "]");
/* 126:    */     }
/* 127:164 */     if (((bean instanceof FactoryBean)) && (!BeanFactoryUtils.isFactoryDereference(name))) {
/* 128:166 */       return ((FactoryBean)bean).getObjectType();
/* 129:    */     }
/* 130:168 */     return bean.getClass();
/* 131:    */   }
/* 132:    */   
/* 133:    */   public String[] getAliases(String name)
/* 134:    */   {
/* 135:172 */     return new String[0];
/* 136:    */   }
/* 137:    */   
/* 138:    */   public boolean containsBeanDefinition(String name)
/* 139:    */   {
/* 140:181 */     return this.beans.containsKey(name);
/* 141:    */   }
/* 142:    */   
/* 143:    */   public int getBeanDefinitionCount()
/* 144:    */   {
/* 145:185 */     return this.beans.size();
/* 146:    */   }
/* 147:    */   
/* 148:    */   public String[] getBeanDefinitionNames()
/* 149:    */   {
/* 150:189 */     return StringUtils.toStringArray((Collection)this.beans.keySet());
/* 151:    */   }
/* 152:    */   
/* 153:    */   public String[] getBeanNamesForType(Class type)
/* 154:    */   {
/* 155:193 */     return getBeanNamesForType(type, true, true);
/* 156:    */   }
/* 157:    */   
/* 158:    */   public String[] getBeanNamesForType(Class type, boolean includeNonSingletons, boolean includeFactoryBeans)
/* 159:    */   {
/* 160:197 */     boolean isFactoryType = (type != null) && (FactoryBean.class.isAssignableFrom(type));
/* 161:198 */     List<String> matches = new ArrayList();
/* 162:199 */     for (String name : this.beans.keySet())
/* 163:    */     {
/* 164:200 */       Object beanInstance = this.beans.get(name);
/* 165:201 */       if (((beanInstance instanceof FactoryBean)) && (!isFactoryType))
/* 166:    */       {
/* 167:202 */         if (includeFactoryBeans)
/* 168:    */         {
/* 169:203 */           Class objectType = ((FactoryBean)beanInstance).getObjectType();
/* 170:204 */           if ((objectType != null) && ((type == null) || (type.isAssignableFrom(objectType)))) {
/* 171:205 */             matches.add(name);
/* 172:    */           }
/* 173:    */         }
/* 174:    */       }
/* 175:210 */       else if ((type == null) || (type.isInstance(beanInstance))) {
/* 176:211 */         matches.add(name);
/* 177:    */       }
/* 178:    */     }
/* 179:215 */     return StringUtils.toStringArray(matches);
/* 180:    */   }
/* 181:    */   
/* 182:    */   public <T> Map<String, T> getBeansOfType(Class<T> type)
/* 183:    */     throws BeansException
/* 184:    */   {
/* 185:219 */     return getBeansOfType(type, true, true);
/* 186:    */   }
/* 187:    */   
/* 188:    */   public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean includeFactoryBeans)
/* 189:    */     throws BeansException
/* 190:    */   {
/* 191:226 */     boolean isFactoryType = (type != null) && (FactoryBean.class.isAssignableFrom(type));
/* 192:227 */     Map<String, T> matches = new HashMap();
/* 193:229 */     for (Map.Entry<String, Object> entry : this.beans.entrySet())
/* 194:    */     {
/* 195:230 */       String beanName = (String)entry.getKey();
/* 196:231 */       Object beanInstance = entry.getValue();
/* 197:233 */       if (((beanInstance instanceof FactoryBean)) && (!isFactoryType))
/* 198:    */       {
/* 199:234 */         if (includeFactoryBeans)
/* 200:    */         {
/* 201:236 */           FactoryBean factory = (FactoryBean)beanInstance;
/* 202:237 */           Class objectType = factory.getObjectType();
/* 203:238 */           if (((includeNonSingletons) || (factory.isSingleton())) && 
/* 204:239 */             (objectType != null) && ((type == null) || (type.isAssignableFrom(objectType)))) {
/* 205:240 */             matches.put(beanName, getBean(beanName, type));
/* 206:    */           }
/* 207:    */         }
/* 208:    */       }
/* 209:245 */       else if ((type == null) || (type.isInstance(beanInstance)))
/* 210:    */       {
/* 211:248 */         if (isFactoryType) {
/* 212:249 */           beanName = "&" + beanName;
/* 213:    */         }
/* 214:251 */         matches.put(beanName, beanInstance);
/* 215:    */       }
/* 216:    */     }
/* 217:255 */     return matches;
/* 218:    */   }
/* 219:    */   
/* 220:    */   public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType)
/* 221:    */     throws BeansException
/* 222:    */   {
/* 223:261 */     Map<String, Object> results = new LinkedHashMap();
/* 224:262 */     for (String beanName : this.beans.keySet()) {
/* 225:263 */       if (findAnnotationOnBean(beanName, annotationType) != null) {
/* 226:264 */         results.put(beanName, getBean(beanName));
/* 227:    */       }
/* 228:    */     }
/* 229:267 */     return results;
/* 230:    */   }
/* 231:    */   
/* 232:    */   public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType)
/* 233:    */   {
/* 234:271 */     return AnnotationUtils.findAnnotation(getType(beanName), annotationType);
/* 235:    */   }
/* 236:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.StaticListableBeanFactory
 * JD-Core Version:    0.7.0.1
 */