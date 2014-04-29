/*   1:    */ package org.springframework.beans.factory.annotation;
/*   2:    */ 
/*   3:    */ import java.lang.annotation.Annotation;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import java.util.LinkedHashSet;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.Map.Entry;
/*   8:    */ import java.util.Set;
/*   9:    */ import org.springframework.beans.SimpleTypeConverter;
/*  10:    */ import org.springframework.beans.TypeConverter;
/*  11:    */ import org.springframework.beans.factory.BeanFactory;
/*  12:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*  13:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*  14:    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*  15:    */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*  16:    */ import org.springframework.beans.factory.support.AutowireCandidateQualifier;
/*  17:    */ import org.springframework.beans.factory.support.AutowireCandidateResolver;
/*  18:    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*  19:    */ import org.springframework.core.MethodParameter;
/*  20:    */ import org.springframework.core.annotation.AnnotationUtils;
/*  21:    */ import org.springframework.util.Assert;
/*  22:    */ import org.springframework.util.ClassUtils;
/*  23:    */ import org.springframework.util.ObjectUtils;
/*  24:    */ 
/*  25:    */ public class QualifierAnnotationAutowireCandidateResolver
/*  26:    */   implements AutowireCandidateResolver, BeanFactoryAware
/*  27:    */ {
/*  28: 56 */   private final Set<Class<? extends Annotation>> qualifierTypes = new LinkedHashSet();
/*  29: 58 */   private Class<? extends Annotation> valueAnnotationType = Value.class;
/*  30:    */   private BeanFactory beanFactory;
/*  31:    */   
/*  32:    */   public QualifierAnnotationAutowireCandidateResolver()
/*  33:    */   {
/*  34: 70 */     this.qualifierTypes.add(Qualifier.class);
/*  35: 71 */     ClassLoader cl = QualifierAnnotationAutowireCandidateResolver.class.getClassLoader();
/*  36:    */     try
/*  37:    */     {
/*  38: 73 */       this.qualifierTypes.add(cl.loadClass("javax.inject.Qualifier"));
/*  39:    */     }
/*  40:    */     catch (ClassNotFoundException localClassNotFoundException) {}
/*  41:    */   }
/*  42:    */   
/*  43:    */   public QualifierAnnotationAutowireCandidateResolver(Class<? extends Annotation> qualifierType)
/*  44:    */   {
/*  45: 86 */     Assert.notNull(qualifierType, "'qualifierType' must not be null");
/*  46: 87 */     this.qualifierTypes.add(qualifierType);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public QualifierAnnotationAutowireCandidateResolver(Set<Class<? extends Annotation>> qualifierTypes)
/*  50:    */   {
/*  51: 96 */     Assert.notNull(qualifierTypes, "'qualifierTypes' must not be null");
/*  52: 97 */     this.qualifierTypes.addAll(qualifierTypes);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void addQualifierType(Class<? extends Annotation> qualifierType)
/*  56:    */   {
/*  57:112 */     this.qualifierTypes.add(qualifierType);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setValueAnnotationType(Class<? extends Annotation> valueAnnotationType)
/*  61:    */   {
/*  62:125 */     this.valueAnnotationType = valueAnnotationType;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void setBeanFactory(BeanFactory beanFactory)
/*  66:    */   {
/*  67:129 */     this.beanFactory = beanFactory;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor)
/*  71:    */   {
/*  72:146 */     if (!bdHolder.getBeanDefinition().isAutowireCandidate()) {
/*  73:148 */       return false;
/*  74:    */     }
/*  75:150 */     if (descriptor == null) {
/*  76:152 */       return true;
/*  77:    */     }
/*  78:154 */     boolean match = checkQualifiers(bdHolder, descriptor.getAnnotations());
/*  79:155 */     if (match)
/*  80:    */     {
/*  81:156 */       MethodParameter methodParam = descriptor.getMethodParameter();
/*  82:157 */       if (methodParam != null)
/*  83:    */       {
/*  84:158 */         Method method = methodParam.getMethod();
/*  85:159 */         if ((method == null) || (Void.TYPE.equals(method.getReturnType()))) {
/*  86:160 */           match = checkQualifiers(bdHolder, methodParam.getMethodAnnotations());
/*  87:    */         }
/*  88:    */       }
/*  89:    */     }
/*  90:164 */     return match;
/*  91:    */   }
/*  92:    */   
/*  93:    */   protected boolean checkQualifiers(BeanDefinitionHolder bdHolder, Annotation[] annotationsToSearch)
/*  94:    */   {
/*  95:171 */     if (ObjectUtils.isEmpty(annotationsToSearch)) {
/*  96:172 */       return true;
/*  97:    */     }
/*  98:174 */     SimpleTypeConverter typeConverter = new SimpleTypeConverter();
/*  99:175 */     for (Annotation annotation : annotationsToSearch)
/* 100:    */     {
/* 101:176 */       Class<? extends Annotation> type = annotation.annotationType();
/* 102:177 */       if ((isQualifier(type)) && 
/* 103:178 */         (!checkQualifier(bdHolder, annotation, typeConverter))) {
/* 104:179 */         return false;
/* 105:    */       }
/* 106:    */     }
/* 107:183 */     return true;
/* 108:    */   }
/* 109:    */   
/* 110:    */   protected boolean isQualifier(Class<? extends Annotation> annotationType)
/* 111:    */   {
/* 112:190 */     for (Class<? extends Annotation> qualifierType : this.qualifierTypes) {
/* 113:191 */       if ((annotationType.equals(qualifierType)) || (annotationType.isAnnotationPresent(qualifierType))) {
/* 114:192 */         return true;
/* 115:    */       }
/* 116:    */     }
/* 117:195 */     return false;
/* 118:    */   }
/* 119:    */   
/* 120:    */   protected boolean checkQualifier(BeanDefinitionHolder bdHolder, Annotation annotation, TypeConverter typeConverter)
/* 121:    */   {
/* 122:204 */     Class<? extends Annotation> type = annotation.annotationType();
/* 123:205 */     RootBeanDefinition bd = (RootBeanDefinition)bdHolder.getBeanDefinition();
/* 124:206 */     AutowireCandidateQualifier qualifier = bd.getQualifier(type.getName());
/* 125:207 */     if (qualifier == null) {
/* 126:208 */       qualifier = bd.getQualifier(ClassUtils.getShortName(type));
/* 127:    */     }
/* 128:210 */     if (qualifier == null)
/* 129:    */     {
/* 130:211 */       Annotation targetAnnotation = null;
/* 131:212 */       if (bd.getResolvedFactoryMethod() != null) {
/* 132:213 */         targetAnnotation = bd.getResolvedFactoryMethod().getAnnotation(type);
/* 133:    */       }
/* 134:215 */       if (targetAnnotation == null)
/* 135:    */       {
/* 136:217 */         if (this.beanFactory != null)
/* 137:    */         {
/* 138:218 */           Class<?> beanType = this.beanFactory.getType(bdHolder.getBeanName());
/* 139:219 */           if (beanType != null) {
/* 140:220 */             targetAnnotation = ClassUtils.getUserClass(beanType).getAnnotation(type);
/* 141:    */           }
/* 142:    */         }
/* 143:223 */         if ((targetAnnotation == null) && (bd.hasBeanClass())) {
/* 144:224 */           targetAnnotation = ClassUtils.getUserClass(bd.getBeanClass()).getAnnotation(type);
/* 145:    */         }
/* 146:    */       }
/* 147:227 */       if ((targetAnnotation != null) && (targetAnnotation.equals(annotation))) {
/* 148:228 */         return true;
/* 149:    */       }
/* 150:    */     }
/* 151:231 */     Map<String, Object> attributes = AnnotationUtils.getAnnotationAttributes(annotation);
/* 152:232 */     if ((attributes.isEmpty()) && (qualifier == null)) {
/* 153:234 */       return false;
/* 154:    */     }
/* 155:236 */     for (Map.Entry<String, Object> entry : attributes.entrySet())
/* 156:    */     {
/* 157:237 */       String attributeName = (String)entry.getKey();
/* 158:238 */       Object expectedValue = entry.getValue();
/* 159:239 */       Object actualValue = null;
/* 160:241 */       if (qualifier != null) {
/* 161:242 */         actualValue = qualifier.getAttribute(attributeName);
/* 162:    */       }
/* 163:244 */       if (actualValue == null) {
/* 164:246 */         actualValue = bd.getAttribute(attributeName);
/* 165:    */       }
/* 166:248 */       if ((actualValue != null) || (!attributeName.equals(AutowireCandidateQualifier.VALUE_KEY)) || 
/* 167:249 */         (!(expectedValue instanceof String)) || (!bdHolder.matchesName((String)expectedValue)))
/* 168:    */       {
/* 169:253 */         if ((actualValue == null) && (qualifier != null)) {
/* 170:255 */           actualValue = AnnotationUtils.getDefaultValue(annotation, attributeName);
/* 171:    */         }
/* 172:257 */         if (actualValue != null) {
/* 173:258 */           actualValue = typeConverter.convertIfNecessary(actualValue, expectedValue.getClass());
/* 174:    */         }
/* 175:260 */         if (!expectedValue.equals(actualValue)) {
/* 176:261 */           return false;
/* 177:    */         }
/* 178:    */       }
/* 179:    */     }
/* 180:264 */     return true;
/* 181:    */   }
/* 182:    */   
/* 183:    */   public Object getSuggestedValue(DependencyDescriptor descriptor)
/* 184:    */   {
/* 185:273 */     Object value = findValue(descriptor.getAnnotations());
/* 186:274 */     if (value == null)
/* 187:    */     {
/* 188:275 */       MethodParameter methodParam = descriptor.getMethodParameter();
/* 189:276 */       if (methodParam != null) {
/* 190:277 */         value = findValue(methodParam.getMethodAnnotations());
/* 191:    */       }
/* 192:    */     }
/* 193:280 */     return value;
/* 194:    */   }
/* 195:    */   
/* 196:    */   protected Object findValue(Annotation[] annotationsToSearch)
/* 197:    */   {
/* 198:287 */     for (Annotation annotation : annotationsToSearch) {
/* 199:288 */       if (this.valueAnnotationType.isInstance(annotation))
/* 200:    */       {
/* 201:289 */         Object value = AnnotationUtils.getValue(annotation);
/* 202:290 */         if (value == null) {
/* 203:291 */           throw new IllegalStateException("Value annotation must have a value attribute");
/* 204:    */         }
/* 205:293 */         return value;
/* 206:    */       }
/* 207:    */     }
/* 208:296 */     return null;
/* 209:    */   }
/* 210:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver
 * JD-Core Version:    0.7.0.1
 */